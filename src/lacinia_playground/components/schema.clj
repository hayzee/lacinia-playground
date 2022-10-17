(ns lacinia-playground.components.schema
  (:require [com.walmartlabs.lacinia.parser.schema :as parser-schema]
            [com.walmartlabs.lacinia.schema :as schema]
            [com.walmartlabs.lacinia.util :as util]
            [mount.core :refer [defstate]])
  (:import
    java.text.SimpleDateFormat))

(defn find-all-in-episode [context args value]
  [{:name "Boris Karloff 888"
    :episodes [:NEWHOPE :EMPIRE "JEDI"]}
   {:name "Boris Karloff 2"
    :episodes [:NEWHOPE :EMPIRE :JEDI]}
   {:name "Boris Karloff 3"
    :episodes [:NEWHOPE :EMPIRE :JEDI]}
   {:name "Boris Karloff 4"
    :episodes [:NEWHOPE :EMPIRE :JEDI]}
   {:name "Boris Karloff 5"
    :episodes [:NEWHOPE :EMPIRE :JEDI]}
   {:name "Boris Karloff 6"
    :episodes [:NEWHOPE :EMPIRE :JEDI]}
   {:name "Boris Karloff 7"
    :episodes [:NEWHOPE :EMPIRE :JEDI]}
   {:name "Boris Karloff 8"
    :episodes [:NEWHOPE :EMPIRE :JEDI]}
   {:name "Boris Karloff 9"
    :episodes [:NEWHOPE :EMPIRE :JEDI]}
   {:name "Boris Karloff 10"
    :episodes [:NEWHOPE :EMPIRE :JEDI]}])

(defn add-character [context args value]
  true)

(def date-formatter
  "Used by custom scalar :Date."
  (SimpleDateFormat. "yyyy-MM-dd"))

(defstate attachments
  :start {
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

(defn- load-schema
  []
  (try
    (let [parsed-schema (parser-schema/parse-schema (slurp (clojure.java.io/resource "schema.graphql"))
                                                    attachments)]
      (schema/compile parsed-schema))
    (catch Exception e
      (println (clojure.stacktrace/root-cause
                 e)))))

(defstate schema
          :start (load-schema))
