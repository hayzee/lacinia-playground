(ns lacinia-playground.db
  (:require [next.jdbc :as jdbc]
            [lacinia-playground.components.datasource :refer [datasource]]
            [next.jdbc.result-set :as rs]))

(defn get-pooled-connection
  []
  (jdbc/get-connection datasource))

(defn datasource-logger
  [sym sql-params]
  (clojure.tools.logging/log :info [sym sql-params]))

(defn execute!
  [sql]
  (let [logging-datasource (jdbc/with-logging datasource
                                              datasource-logger)]
    (jdbc/execute! logging-datasource sql {:builder-fn rs/as-unqualified-lower-maps})))
