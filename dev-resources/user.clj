(ns user
  (:require [lacinia-playground.system :as system]
            [migratus.core :as migratus]
            [clojure.java.browse :refer [browse-url]]))

(defn start
  []
  (system/start :dev)
  (browse-url "http://localhost:8888/"))

(defn stop []
  (system/stop))

(defn restart []
  (system/restart))

(defn reset-migrations
  []
 (migratus/init system/migratus-config))

(defn rollback-migration
  []
  (migratus/rollback system/migratus-config))

(defn create-migration
  [desc]
  (migratus/create system/migratus-config desc))

(defn migrate
  []
  (migratus/migrate system/migratus-config))
