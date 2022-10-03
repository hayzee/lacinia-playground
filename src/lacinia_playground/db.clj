(ns lacinia-playground.db
  (:require [next.jdbc :as jdbc]
            [migratus.core :as migratus]
            [clojure.java.io :as io]
            [clojure.edn :as edn]))

; read this: https://cljdoc.org/d/com.github.seancorfield/next.jdbc/1.3.834/doc/getting-started

(def config (edn/read-string (slurp (io/resource "migrations/config.edn"))))

(migratus/init config)

(migratus/create config "customer")

;(def db {:dbtype "h2" :dbname "example"})

(def ds (jdbc/get-datasource db))

(jdbc/execute! ds ["
drop table address"])

(jdbc/execute! ds ["
create table address (
  id int auto_increment primary key,
  name varchar(32),
  email varchar(255)
);"])

(defn create-database
  []

  (jdbc/execute! ds ["
drop table if exists customer"])

  (jdbc/execute! ds ["
create table customer (
  id int auto_increment primary key,
  reference varchar2(32),
  name varchar(32),
  email varchar(255)
);"])

  (jdbc/execute! ds ["
drop table if exists address"])

  (jdbc/execute! ds ["
create table address (
  id int auto_increment primary key,
  name varchar(32),
  email varchar(255)
);"])

  )


(create-database)