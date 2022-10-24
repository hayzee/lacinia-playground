(ns lacinia-playground.core
  (:require [lacinia-playground.system :as system])
  (:gen-class))

(defn -main
  [& args]
  (system/start :prod))
