(ns lacinia-playground.attachments
  (:import (java.text SimpleDateFormat)))

(defn find-all-in-episode-xx
  [context args value]

  [{:name "Obi-Wan Kenobi", :episodes ["NEWHOPE" "EMPIRE" "JEDI"]}
   {:name "Darth Vader", :episodes ["NEWHOPE" "EMPIRE" "JEDI"]}
   {:name "Chewbacca", :episodes ["NEWHOPE" "EMPIRE" "JEDI"]}
   {:name "Princess Leia", :episodes ["NEWHOPE" "EMPIRE" "JEDI"]}
   {:name "R2-D2", :episodes ["NEWHOPE" "EMPIRE" "JEDI"]}
   {:name "C3-PO", :episodes ["NEWHOPE" "EMPIRE" "JEDI"]}]

  #_[{:name "Boris Karloff 888"
    :episodes [:NEWHOPE :EMPIRE "JEDI"]}
   {:name "Boris Karloff 999"
    :episodes [:NEWHOPE :EMPIRE :JEDI]}
   {:name "Boris Karloff 333"
    :episodes [:NEWHOPE :EMPIRE :JEDI]}
   {:name "Boris Karloff 444"
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


(defn apply-fns-to-row [fns row]
  (mapv (fn [f attr] (f attr)) fns row))

(defn apply-fns-to-rows [fns rows]
  (mapv #(apply-fns-to-row fns %) rows))

(defn find-all-in-episode
  [context args value]
  (->> (lacinia-playground.db/execute! ["
          SELECT to_char(SC.NAME) as name,
                 CE.EPISODE
          FROM   starwars_character sc
                 JOIN character_episode ce
                   ON ce.CHARACTER_ID  = sc.ID"])
       (group-by :name)
       (apply-fns-to-rows [#(vector :name %)
                           #(vector :episodes (mapv :episode %))])
       ;(map #(vector (first %)))
       (mapv (partial into {}))
      ))



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
               :Character {:episodes (constantly ["EMPIRE"])}}

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
