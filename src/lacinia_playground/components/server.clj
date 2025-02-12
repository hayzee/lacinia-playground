(ns lacinia-playground.components.server
  (:require [com.walmartlabs.lacinia.pedestal2 :as lp]
            [lacinia-playground.components.schema :as schema]
            [io.pedestal.http :as http]
            [io.pedestal.interceptor :as interceptor]
            [mount.core :refer [defstate]]))

(defn null-interceptor
  [interceptor-name]
  "Enables tracing if the `lacinia-tracing` header is present."
  (interceptor/interceptor
    {:name interceptor-name
     :enter (fn [context]
              ;; Must come after the app context is added to the request.
              context)}))

(alter-var-root #'com.walmartlabs.lacinia.pedestal2/enable-tracing-interceptor
                (constantly (null-interceptor ::lp/enable-tracing)))

;;; Use default options:
(defstate service
  :start (lp/default-service schema/schema nil))
;
;;; This is an adapted service map, that can be started and stopped.
;;; From the REPL you can call http/start and http/stop on this service:
(defstate runnable-service
  :start (http/create-server service))
;
(defstate server
          :start (http/start runnable-service)
          :stop (http/stop server))
