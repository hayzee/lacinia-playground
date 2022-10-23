(ns lacinia-playground.attachments
  (:require [lacinia-playground.db :as db])
  (:import (java.text SimpleDateFormat)))

(defn find-all-in-episode
  [context args value]
  (db/execute! ["
          SELECT
          id,
          to_char(SC.NAME) as name
          FROM   starwars_character sc
          WHERE EXISTS (SELECT 1
                        FROM   character_episode ce
                        WHERE  ce.character_id = sc.id
                        AND ce.episode = ?)" (name (:episode args))]))

(defn episodes
  [context args value]
  (->> (db/execute! ["
          SELECT episode
          FROM   character_episode ce
          WHERE  ce.CHARACTER_ID = ?
          AND    (rownum <= ? OR ? IS NULL)
                   " (:id value) (:first args) (:first args)])
       (mapv :episode)))

(defn add-character [context args value]

  (let [{id :id} (db/execute-one! ["SELECT COALESCE(MAX(id)+1,1) AS id FROM starwars_character sc"])
        _        (db/execute-one! ["INSERT INTO starwars_character  VALUES (?, ?)" id (get-in args [:character :name])])
        _        (doseq [episode (get-in args [:character :episodes])]
                   (println episode)
                   (db/execute-one! ["INSERT INTO character_episode VALUES (?, ?)" id (name episode)]))
        ]
       id))

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
