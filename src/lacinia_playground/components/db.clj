(ns lacinia-playground.components.db
  (:require [mount.core :refer [defstate]]
            [hikari-cp.core :as hikari]
            [lacinia-playground.components.config :as config]
            [clojure.tools.logging :as logging]))

(defn- initialise-hikari-pool
  []
  (let [dso (:datasource-options config/config)]
    (logging/log :info (str "Initialise Hikari pool using datasource-options:" dso))
    (hikari/make-datasource dso)))

(defn- close-hikari-pool
  [datasource]
  (logging/log :info (str "Closing Hikari pool:" datasource))
  (.close datasource))

(defstate datasource
          :start (initialise-hikari-pool)
          :stop (close-hikari-pool datasource))
