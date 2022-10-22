(ns lacinia-playground.attachments
  (:require [lacinia-playground.db :as db])
  (:import (java.text SimpleDateFormat)))

;(defn apply-fns-to-row [fns row]
;  (mapv (fn [f attr] (f attr)) fns row))
;
;(defn apply-fns-to-rows [fns rows]
;  (mapv #(apply-fns-to-row fns %) rows))

(defn find-all-in-episode
  [context args value]
  (db/execute! ["
          SELECT
          id,
          to_char(SC.NAME) as name
          FROM   starwars_character sc
                 JOIN character_episode ce
                   ON ce.character_id = sc.id
                  AND ce.episode = ?" (name (:episode args))]))

(comment

  (db/execute! ["
            SELECT episode
            FROM   character_episode ce
                     " ])

  )

(defn episodes
  [context args value]
  (->> (db/execute! ["
          SELECT episode
          FROM   character_episode ce
          WHERE  ce.CHARACTER_ID = ?
          AND    (rownum <= ? OR ? IS NULL)
                   " (:id value) (:first args) (:first args)])
       (mapv :episode)
       ))

(comment
  (episodes {} {:first 9} {:id 3})
  )

(defn add-character [context args value]
  ; {:character {:name Johan Bastard, :episodes [:NEWHOPE]}}
;  (db/execute! "INSERT INTO character ")
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
