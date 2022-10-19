(ns lacinia-playground.attachments
  (:import (java.text SimpleDateFormat)))

;
;(defn apply-fns-to-row [fns row]
;  (mapv (fn [f attr] (f attr)) fns row))
;
;(defn apply-fns-to-rows [fns rows]
;  (mapv #(apply-fns-to-row fns %) rows))

(defn find-all-in-episode
  [context args value]
  (lacinia-playground.db/execute! ["
          SELECT
          id,
          to_char(SC.NAME) as name
          FROM   starwars_character sc"]))

(defn episodes
  [context args value]

  (clojure.tools.logging/log :info (str "episodes - args: " args))

  (->> (lacinia-playground.db/execute! ["
          SELECT episode
          FROM   character_episode ce
          WHERE  ce.CHARACTER_ID = ?
          AND    rownum <= ?
                   " (:id value) (:first args)])
      (mapv :episode)))

(defn add-character [context args value]
  true)

(def date-formatter
  "Used by custom scalar :Date."
  (SimpleDateFormat. "yyyy-MM-dd"))

(defn attachments
  []
  {
   :resolvers {:Query    {:find_all_in_episode find-all-in-episode}
               :Mutation {:add_character add-character}
               :Character {:episodes episodes}}

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
