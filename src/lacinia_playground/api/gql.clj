(ns lacinia-playground.api.gql
  (:require [com.walmartlabs.lacinia.schema :as schema]
            [lacinia-playground.api.db :as db])
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
  (when-let [thing (db/get-thing (:id args))]
    (mk-thing (:id thing) (:name thing))))

(defn r-things
  [context args value]
  (mapv #(mk-thing (:id %) (:name %)) (db/get-things)))

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

(defn m-create-thing
  [context args value]
  (let [thing (db/create-thing (-> args :thing :name))]
    (mk-thing (:id thing) (:name thing))))

(def resolvers
  {:Query/thing r-thing
   :Query/things r-things
   :Thing/otherThings r-thing-other-things
   :OtherThing/thing r-thing
   :Mutation/createThing m-create-thing})

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
