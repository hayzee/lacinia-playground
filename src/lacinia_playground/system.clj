(ns lacinia-playground.system
  (:require [mount.core :as mount]
            [clojure.tools.logging :as logging]
            [migratus.core :as migratus]

            ; components
            [lacinia-playground.components.config :as config]
            [lacinia-playground.components.server :as server]
            [lacinia-playground.components.schema :as schema]
            [lacinia-playground.components.db :as db]

            ))

(defn start
  [profile]
  (logging/log :info (str "Starting " profile " System"))
  (binding [config/profile profile]
    (mount/start)
    (migratus/migrate (config/migratus db/datasource))))

(defn stop
  []
  (logging/log :info "Stopping System")
  (mount/stop))

(defn restart
  []
  (let [profile (or (:profile config/config) :dev)]
    (stop)
    (start profile)))
