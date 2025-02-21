(ns lacinia-playground.components.schema
  (:require [clojure.java.io :as io]
            [clojure.pprint :as pp]
            [clojure.stacktrace :as st]
            [com.walmartlabs.lacinia.parser.schema :as parser-schema]
            [com.walmartlabs.lacinia.schema :as schema]
            [com.walmartlabs.lacinia.util :as util]
            [mount.core :refer [defstate]]
            [lacinia-playground.api.gql :as gql]))


; schema

(defn spit-file
  [data file]
  (spit file (with-out-str (pp/pprint data)))
  data)

(defn- load-schema
  []
  (try
    (let [parsed-schema (-> (io/resource "schema.graphql")
                            slurp
                            (parser-schema/parse-schema)
                            (spit-file "./resources/schema.edn")
                            (util/inject-scalar-transformers gql/scalars)
                            (util/inject-resolvers gql/resolvers)
                            (util/inject-streamers gql/streamers)
                            (spit-file "./resources/schema+.edn"))]
      (schema/compile parsed-schema))
    (catch Exception e
      (println (st/root-cause
                 e)))))

(defstate schema
          :start (load-schema))
