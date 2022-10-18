(ns user
  (:require [lacinia-playground.system :as system]
            [lacinia-playground.components.config :as config]
            [lacinia-playground.components.db :as db]
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
 (migratus/init (config/migratus db/datasource)))

(defn rollback-migration
  []
  (migratus/rollback (config/migratus db/datasource)))

(defn create-migration
  [desc]
  (migratus/create (config/migratus db/datasource) desc))

(defn migrate
  []
  (migratus/migrate (config/migratus db/datasource)))
