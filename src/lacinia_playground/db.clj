(ns lacinia-playground.db
  (:require [next.jdbc :as jdbc]
            [lacinia-playground.components.datasource :refer [datasource]]
            [next.jdbc.result-set :as rs]))

(defn get-pooled-connection
  []
  (jdbc/get-connection datasource))

(defn execute!
  [sql]
  (jdbc/execute! datasource sql {:builder-fn rs/as-unqualified-lower-maps}))

