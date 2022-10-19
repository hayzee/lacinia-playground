(ns lacinia-playground.components.schema
  (:require [com.walmartlabs.lacinia.parser.schema :as parser-schema]
            [com.walmartlabs.lacinia.schema :as schema]
            [com.walmartlabs.lacinia.util :as util]
            [mount.core :refer [defstate]]
            [lacinia-playground.attachments :as attachments]))

(defn- load-schema
  []
  (try
    (let [parsed-schema (parser-schema/parse-schema (slurp (clojure.java.io/resource "schema.graphql"))
                                                    (attachments/attachments))]
      (schema/compile parsed-schema))
    (catch Exception e
      (println (clojure.stacktrace/root-cause
                 e)))))

(defstate schema
          :start (load-schema))
