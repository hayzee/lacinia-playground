(ns lacinia-playground.components.server
  (:require
    [com.walmartlabs.lacinia.pedestal :as lp]
    [io.pedestal.http :as http]
    [lacinia-playground.components.schema :as schema]
    [mount.core :refer [defstate]]))

(defn- start-http-server
  []
  (->>
    (let [schema schema/schema]
      (-> schema
          ; todo - looks like service-map is deprecated since version upgrades.
          (lp/service-map {:graphiql true})
          http/create-server
          http/start))))

(defn- stop-http-server
  [server]
  (http/stop server))

(defstate server
          :start (start-http-server)
          :stop (stop-http-server server))
