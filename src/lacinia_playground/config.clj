(ns lacinia-playground.config
  (:require [mount.core :refer [defstate]]
            [aero.core :as aero]
            [clojure.java.io :as io]
            [clojure.tools.logging :as logging]))

(defonce ^:dynamic profile :prod)

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
          (logging/log :fatal ex)
          (System/exit -1)))
      (do
        (logging/log :fatal (str "Loading the configuration file at envvar:" value " for profile " profile))
        (System/exit -1)))))

(defn- load-config
  []
  (do
    (logging/log :info (str "Loading the " profile " configuration"))
    (aero/read-config (io/resource "config.edn") {:profile profile})))

(defstate config
          :start (load-config))

(defn get-datasource-options
  []
  (:datasource-options config))
