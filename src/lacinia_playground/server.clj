(ns lacinia-playground.server
  (:require
    [com.walmartlabs.lacinia.pedestal :as lp]
    [io.pedestal.http :as http]
    [lacinia-playground.schema :as schema]
    [clojure.java.browse :refer [browse-url]]
    [mount.core :refer [defstate]]))

(defstate server
          :start (->>
                   (let [schema (schema/load-schema)]
                     (-> schema
                         ; todo - looks like service-map is deprecated since version upgrades.
                         (lp/service-map {:graphiql true})
                         http/create-server
                         http/start)))

          :stop (http/stop server))

