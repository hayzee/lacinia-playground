(ns lacinia-playground.components.schema
  (:require [clojure.java.io :as io]
            [clojure.stacktrace :as st]
            [com.walmartlabs.lacinia.parser.schema :as parser-schema]
            [com.walmartlabs.lacinia.schema :as schema]
            [com.walmartlabs.lacinia.util :as util]
            [mount.core :refer [defstate]]
            [lacinia-playground.dbapi :as attachments])
  (:import (java.text SimpleDateFormat)))

; scalars

(def date-formatter
  "Used by custom scalar :Date."
  (SimpleDateFormat. "yyyy-MM-dd"))

(def scalars {:Date
              {:parse     #(when (string? %)
                             (try
                               ; String -> Date
                               (.parse date-formatter %)
                               (catch Throwable _
                                 nil)))
               :serialize #(try
                             ; Date -> String(Buffer)
                             (.format date-formatter %)
                             (catch Throwable _
                               nil))}})

(defn mk-thing
  [thing-id name]
  (schema/tag-with-type
    {:id thing-id
     :name name}
    :Thing))

(defn r-thing
  [context args value]
  (mk-thing "thing-id" "I am a Thing"))

(defn mk-other-thing
  [thing-id name]
  (schema/tag-with-type
    {:id thing-id
     :name name}
    :OtherThing))

(defn r-thing-other-things
  [context args value]
  [(mk-other-thing "thing-id2" "I am a Thing2")
   (mk-other-thing "thing-id3" "I am a Thing3")])

(def resolvers
  {:Query/thing r-thing
   :Thing/otherThings r-thing-other-things
   :OtherThing/thing r-thing})

(defn initiate-stream
  [context args stream]
  (future
    (doseq [n (range 10)]
      (Thread/sleep 1000)
      (stream {:message (str "arse " n)}))
    (stream nil)))

(defn s-updated
  [context args stream]
  (initiate-stream context args stream)
  (constantly (println "Done!")))

(def streamers
  {:Subscription/updated s-updated})

; schema

(defn spitfile
  [data file]
  (spit file (with-out-str (clojure.pprint/pprint data)))
  data)

(defn- load-schema
  []
  (try
    (let [parsed-schema (-> (io/resource "schema.graphql")
                            slurp
                            (parser-schema/parse-schema)
                            (spitfile "./resources/schema.edn")
                            (util/inject-scalar-transformers scalars)
                            (util/inject-resolvers resolvers)
                            (util/inject-streamers streamers)
                            (spitfile "./resources/schema+.edn"))]
      (schema/compile parsed-schema))
    (catch Exception e
      (println (st/root-cause
                 e)))))

(defstate schema
          :start (load-schema))
