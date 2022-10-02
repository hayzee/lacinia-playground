(ns lacinia-playground.server
  (:require
    [com.walmartlabs.lacinia.pedestal :as lp]
    [io.pedestal.http :as http]
    [lacinia-playground.schema :as schema]
    [clojure.java.browse :refer [browse-url]]))

;(ns-unmap *ns* 'server)
(defonce server (atom nil))

(defn start-server
  [& {:keys [silent]}]
  (if @server
    (or silent (println "Server already running"))
    (do
      (->>
        (let [schema (schema/load-schema)]
          (-> schema
              ; todo - looks like service-map is deprecated since version upgrades.
              (lp/service-map {:graphiql true})
              http/create-server
              http/start))
        (reset! server))
      (or silent (println "Server started"))
      #_(browse-url "http://localhost:8888/"))))

(defn stop-server
  [& {:keys [silent]}]
  (if @server
    (do
      (or silent (println "Stopping server"))
      (http/stop @server)
      (reset! server nil))
    (or silent (println "Server not running"))))

(defn restart-server
  []
  (stop-server)
  (start-server))

(comment

 (restart-server)

 (start-server)

 (stop-server)

 )

