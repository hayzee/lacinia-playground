(ns user
  (:require [lacinia-playground.system :as system]
            [migratus.core :as migratus]
            [clojure.java.browse :refer [browse-url]]
            [next.jdbc :as jdbc]
            [mount.core :as mount :refer [defstate]])
  (:import (io.zonky.test.db.postgres.embedded EmbeddedPostgres)
           (io.zonky.test.db.postgres.junit EmbeddedPostgresRules)))

(defstate ^{:on-reload :noop} embedded-db
  :start (do (taoensso.timbre/info "Starting embedded database")
             (EmbeddedPostgres/start))
  :stop (do (taoensso.timbre/info "Stopping embedded database")
            (.close embedded-db)))

(println "\n\nL O A D I N G   U S E R   N A M E S P A C E !\n\n")

(defn start
  [& {:keys [start-browser]
      :or {start-browser true}}]
  (mount/start #'embedded-db)
  (binding [lacinia-playground.components.datasource/start-datasource
            (fn []
              (taoensso.timbre/info "Starting embedded datasource")
              (.getPostgresDatabase ^EmbeddedPostgres embedded-db))]
   (system/start :dev))
  (when start-browser
    (browse-url "http://localhost:8888/ide")))

(defn stop
  [& {:keys [stop-db]
      :or {stop-db false}}]
  (binding [lacinia-playground.components.datasource/stop-datasource
            (fn [ds]
              (taoensso.timbre/info "NOT STOPPING EMBEDDED DATASOURCE")
              nil)]
    (system/stop))
  (when stop-db
    (mount/stop #'embedded-db)))

(defn restart []
  (stop)
  (start :start-browser false))

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

(defn show
  [o & {:keys [label] :as opts}]
  (when label
   (println label))
  (clojure.pprint/pprint o)
  o)
