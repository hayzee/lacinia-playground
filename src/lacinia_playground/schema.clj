(ns lacinia-playground.schema
  (:require [com.walmartlabs.lacinia.parser.schema :as parser-schema]
            [com.walmartlabs.lacinia.schema :as schema]
            [com.walmartlabs.lacinia.util :as util])
  (:import
    java.text.SimpleDateFormat))

(defn find-all-in-episode [context args value]
  [{:name "Boris Karloff"
    :episodes [:NEWHOPE :BANANAS]}
   {:name "Boris Karloff"
    :episodes [:NEWHOPE :BANANAS]}
   {:name "Boris Karloff"
    :episodes [:NEWHOPE :BANANAS]}
   {:name "Boris Karloff"
    :episodes [:NEWHOPE :BANANAS]}
   {:name "Boris Karloff"
    :episodes [:NEWHOPE :BANANAS]}])

(defn add-character [context args value]
  true)

(def date-formatter
  "Used by custom scalar :Date."
  (SimpleDateFormat. "yyyy-MM-dd"))

(def attach
  {
   :resolvers {:Query    {:find_all_in_episode find-all-in-episode}
               :Mutation {:add_character add-character}}

   :documentation {:Character                         "A Star Wars character"
                   :Character/name                    "Character name"
                   :Query/find_all_in_episode         "Find all characters in the given episode"
                   :Query/find_all_in_episode.episode "Episode for which to find characters."}

   :scalars {:Date
             {:parse  #(when (string? %)
                         (try
                           (.parse date-formatter %)
                           (catch Throwable _
                             nil)))
              :serialize #(try
                            (.format date-formatter %)
                            (catch Throwable _
                              nil))}}})

(defn load-schema
  []
  (try
    (let [parsed-schema (parser-schema/parse-schema (slurp (clojure.java.io/resource "schema.graphql"))
                                                    attach)]
      (schema/compile parsed-schema))
    (catch Exception e
      (println (clojure.stacktrace/root-cause
                 e)))))
