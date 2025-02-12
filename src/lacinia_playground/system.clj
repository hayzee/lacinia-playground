(ns lacinia-playground.system
  (:require [mount.core :refer [defstate] :as mount]
            [clojure.tools.logging :as logging]
            [migratus.core :as migratus]

            ; components
            [lacinia-playground.components.config :as config]
            [lacinia-playground.components.server :as server]
            [lacinia-playground.components.schema :as schema]
            [lacinia-playground.components.datasource :as datasource]))

(defstate migratus-config
          :start
          (merge
            (:migratus config/config)
            {:db {:datasource datasource/datasource}}))

(defn start
  [profile]
  (logging/log :info (str "Starting " profile " System"))
  (binding [config/profile profile]
    (mount/start #'config/config
                 #'datasource/datasource
                 #'schema/schema
                 #'server/service
                 #'server/runnable-service
                 #'server/server
                 #'migratus-config)
    (migratus/migrate migratus-config)))

(defn stop
  []
  (logging/log :info "Stopping System")
  (mount/stop))

(defn restart
  []
  (let [profile (or (:profile config/config) :dev)]
    (stop)
    (start profile)))
