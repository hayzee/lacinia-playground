(ns user
  (:require [mount.core :as mount]
            [lacinia-playground.config :as config]
            [migratus.core :as migratus]))

(defn start []
  (binding [config/profile :dev]
    (mount/start)))

(defn stop []
  (mount/stop))

(defn restart []
  (stop)
  (start))

(defn reset-migrations
  []
 (migratus/init (get-in config/config [:migration :dir])))

(defn create-migration
  [desc]
  (migratus/create (get-in config/config [:migration :dir] ) desc))


(defn migrate
  []
  (migratus/migrate (get-in config/config [:migration :dir])))