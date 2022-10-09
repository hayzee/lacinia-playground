(ns lacinia-playground.config
  (:require [mount.core :refer [defstate] :as mount]
            [aero.core :as aero]
            [clojure.java.io :as io]
            [clojure.tools.logging :as logging]
            [mount.core :as mount]))

(def ^:dynamic profile :prod)

(defmethod aero/reader 'envfile
  [{:keys [resolver source] :as opts} tag value]
  (let [ev-value (System/getenv (name value))]
    (logging/log :info (str "Loading the configuration file at env:" value " for profile " profile))
    (if ev-value
      (try
        (aero/read-config
         (if (map? resolver)
           (get resolver ev-value)
           (resolver source ev-value))
         opts)
        (catch Exception ex
          ))
      (do
        (logging/log :error (str "Loading the configuration file at envvar:" value " for profile " profile))
        nil))))

(defn load-config
  []
  (do
    (logging/log :info (str "Loading the " profile " configuration"))
    (aero/read-config (io/resource "config.edn") {:profile profile})))

(defstate config
          :start (load-config))

(defn get-config
  []
  config)
