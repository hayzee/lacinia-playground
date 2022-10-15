(ns lacinia-playground.db
  (:require [mount.core :refer [defstate]]
            [next.jdbc :as jdbc]
            [clojure.java.io :as io]
            [clojure.edn :as edn]
            [mount.core :refer [defstate]]
            [hikari-cp.core :as hikari]
            [next.jdbc :as jdbc]
            [lacinia-playground.config :as config]
            [clojure.tools.logging :as logging]))

(defn- initialise-hikari-pool
  []
  (let [dso (config/datasource-options)]
    (logging/log :info (str "Initialise Hikari pool using datasource-options:" dso))
    (hikari/make-datasource dso)))

(defn- close-hikari-pool
  [datasource]
  (logging/log :info (str "Closing Hikari pool:" datasource))
  (.close datasource))

(defstate datasource
          :start (initialise-hikari-pool)
          :stop (close-hikari-pool datasource))

; TODO - IGNORE BELOW THIS LINE FOR NOW

;(comment
;
;  (defn -main [& args]
;    (jdbc/with-db-connection [conn {:datasource datasource}]
;                             (let [rows (jdbc/query conn "SELECT 0 FROM dual")]
;                               (println rows)))
;    (close-datasource @datasource))
;
;
;
;
;  ; read this: https://cljdoc.org/d/com.github.seancorfield/next.jdbc/1.3.834/doc/getting-started
;
; (defstate config
;           :start (edn/read-string (slurp (io/resource "migrations/config.edn"))))
;
; ;(def db {:dbtype "h2" :dbname "example"})
;
; (def ds (jdbc/get-datasource db))
;
; (jdbc/execute! ds ["
;drop table address"])
;
; (jdbc/execute! ds ["
;create table address (
;  id int auto_increment primary key,
;  name varchar(32),
;  email varchar(255)
;);"])
;
; (defn create-database
;   []
;
;   (jdbc/execute! ds ["
;drop table if exists customer"])
;
;   (jdbc/execute! ds ["
;create table customer (
;  id int auto_increment primary key,
;  reference varchar2(32),
;  name varchar(32),
;  email varchar(255)
;);"])
;
;   (jdbc/execute! ds ["
;drop table if exists address"])
;
;   (jdbc/execute! ds ["
;create table address (
;  id int auto_increment primary key,
;  name varchar(32),
;  email varchar(255)
;);"])
;
;   )
;
;
; (create-database)
; )