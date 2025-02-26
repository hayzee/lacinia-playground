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

(def all-states
  [#'config/config
   #'datasource/datasource
   #'schema/schema
   #'server/server
   #'migratus-config])

(defn start
  [profile]
  (logging/log :info (str "Starting " profile " System"))
  (binding [config/profile profile]
    (apply mount/start all-states)
    (migratus/migrate migratus-config)))

(defn stop
  []
  (logging/log :info "Stopping System")
  (apply mount/stop all-states))

(defn restart
  []
  (let [profile (or (:profile config/config) :dev)]
    (stop)
    (start profile)))
