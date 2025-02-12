(ns lacinia-playground.components.schema
  (:require [clojure.java.io :as io]
            [clojure.stacktrace :as st]
            [com.walmartlabs.lacinia.parser.schema :as parser-schema]
            [com.walmartlabs.lacinia.schema :as schema]
            [com.walmartlabs.lacinia.util :as util]
            [mount.core :refer [defstate]]
            [lacinia-playground.attachments :as attachments])
  (:import (java.text SimpleDateFormat)))

(def date-formatter
  "Used by custom scalar :Date."
  (SimpleDateFormat. "yyyy-MM-dd"))

(defn- load-schema
  []
  (try
    (let [parsed-schema (-> (io/resource "schema.graphql")
                            slurp
                            (parser-schema/parse-schema #_(attachments/attachments))
                            (util/inject-scalar-transformers {:Date
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
                                                                               nil))}}))]
      (schema/compile parsed-schema))
    (catch Exception e
      (println (st/root-cause
                 e)))))

(defstate schema
          :start (load-schema))

;(-> (parser-schema/parse-schema (slurp (clojure.java.io/resource "schema.graphql"))
;                             #_(attachments/attachments))
;    (util/attach-streamers {:Subscription {:fields {:burp {:type String}}}}))
