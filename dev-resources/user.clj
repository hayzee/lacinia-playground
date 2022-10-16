(ns user
  (:require [mount.core :as mount]
            [lacinia-playground.config :as config]
            [lacinia-playground.server :as server]
            [lacinia-playground.db :as db]
            [migratus.core :as migratus]
            [clojure.tools.logging :as logging]))

(defn start
  [& {:keys [profile]
      :or {profile :dev}}]
  (logging/log :info (str "Starting " profile " System"))
  ;  (tn/refresh)
  (binding [config/profile profile]
    (mount/start)))

(defn stop []
  (logging/log :info "Stopping System")
  (mount/stop))

(defn restart []
  (let [profile (or (:profile config/config) :dev)]
    (stop)
    (start :profile profile)))

(defn reset-migrations
  []
 (migratus/init (-> (config/migratus db/datasource)
                    (get-in [:migration :dir]))))

(defn create-migration
  [desc]
  (migratus/create (-> (config/migratus db/datasource)
                       (get-in [:migration :dir])) desc))

(defn migrate
  []
  (migratus/migrate (config/migratus db/datasource)))
