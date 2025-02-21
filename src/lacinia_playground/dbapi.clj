(ns lacinia-playground.dbapi
  (:require [lacinia-playground.db :as db]
            [com.walmartlabs.lacinia.schema :as schema])
  (:import (java.text SimpleDateFormat)))

(defn create-thing
  [name]
  (db/execute! ["
    INSERT INTO thing (name) VALUES (?)
  " name]))

(defn update-thing
  [id name]
  (db/execute! ["
    UPDATE thing SET name = ? WHERE id = ?
  " name id]))

(defn get-thing
  [id]
  (db/execute! ["
    SELECT * FROM thing WHERE ID = ?
  " id]))

(defn get-things
  []
  (db/execute! ["
    SELECT * FROM thing
  "]))

; utilities

(def date-formatter
  "Used by custom scalar :Date."
  (SimpleDateFormat. "yyyy-MM-dd"))
