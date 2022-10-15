(ns user
  (:require [mount.core :as mount]
            [lacinia-playground.config :as config]
            [lacinia-playground.server :as server]
            [lacinia-playground.db :as db]
            [migratus.core :as migratus]
            [clojure.tools.logging :as logging]))

(defn start
  [& {:keys [profile & rest]
      :or {profile :dev}}]

  (println  rest)

  (logging/log :info (str "Starting " profile " System"))
  ;  (tn/refresh)
  (binding [config/profile profile]
    (mount/start)))

(defn stop []
  (logging/log :info "Stopping System")
  (mount/stop))

(defn restart []
  (let [profile (:profile config/config)]
    (stop)
    (start profile)))

(defn reset-migrations
  []
 (migratus/init (get-in config/config [:migration :dir])))

(defn create-migration
  [desc]
  (migratus/create (get-in config/config [:migration :dir] ) desc))


(defn migrate
  []
  (migratus/migrate (get-in config/config [:migration :dir])))