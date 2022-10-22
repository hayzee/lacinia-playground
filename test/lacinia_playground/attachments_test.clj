(ns lacinia-playground.attachments-test
  (:require [clojure.test :refer :all]
            [com.walmartlabs.lacinia :as lacinia]
            [lacinia-playground.components.schema :as schema])
  (:import (clojure.lang IPersistentMap)))

(def query
  "query {
     find_all_in_episode(episode: JEDI) {
       name
       episodes(first: 10)
     }
   }")

(defn simplify
  "Converts all ordered maps nested within the map into standard hash maps, and
   sequences into vectors, which makes for easier constants in the tests, and eliminates ordering problems."
  [m]
  (clojure.walk/postwalk
    (fn [node]
      (cond
        (instance? IPersistentMap node)
        (into {} node)

        (seq? node)
        (vec node)

        :else
        node))
    m))

(comment

 (->
   (lacinia/execute schema/schema query nil nil)
   simplify)

 )
