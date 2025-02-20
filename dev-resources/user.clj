(ns user
  (:require [lacinia-playground.system :as system]
            [migratus.core :as migratus]
            [clojure.java.browse :refer [browse-url]]))

(println "\n\nL O A D I N G   U S E R   N A M E S P A C E !\n\n")

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

(defn show
  [o & {:keys [label] :as opts}]
  (when label
   (println label))
  (clojure.pprint/pprint o)
  o)
