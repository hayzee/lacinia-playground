(ns lacinia-playground.api.db
  (:require [lacinia-playground.db :as db])
  (:import (java.text SimpleDateFormat)))

(defn create-thing
  [name]
  (db/execute-one! ["
    INSERT INTO thing (id, name) VALUES (gen_random_uuid(), ?)
    RETURNING *
    " name]))

(defn update-thing
  [id name]
  (db/execute-one! ["
    UPDATE thing SET name = ? WHERE id = ?
    RETURNING *
    " name id]))

(defn get-thing
  [id]
  (db/execute-one! ["
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
