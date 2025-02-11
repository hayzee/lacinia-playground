(ns lacinia-playground.attachments
  (:require [lacinia-playground.db :as db]
            [com.walmartlabs.lacinia.schema :as schema]
            [lacinia-playground.util :as u])
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

(defn get-person [context args value]
  (println :args args :value value)
  (schema/tag-with-type
   {:id            12345
    :name          "Chary Farnesbarnes"
    :nastinessScore -99
    :nicenessScore 99
    }
   :NastyPerson))

(def date-formatter
  "Used by custom scalar :Date."
  (SimpleDateFormat. "yyyy-MM-dd"))

(declare nasty-people)

(def nice-people [(schema/tag-with-type
                    {:id            654321
                     :name          "Bob Nice"
                     :nastinessScore 0
                     :nicenessScore 999
                     :friends nasty-people
                     }
                    :NastyPerson)
                  (schema/tag-with-type
                    {:id             54321
                     :name           "Sally Sunshine"
                     :nastinessScore 0
                     :nicenessScore 888
                     :friends nasty-people
                     }
                    :NastyPerson)])

(def nasty-people [(schema/tag-with-type
                     {:id            12346
                      :name          "Billy Nasty"
                      :nastinessScore 999
                      :nicenessScore 0
                      :friends nice-people
                      }
                     :NastyPerson)
                   (schema/tag-with-type
                     {:id             12345
                      :name           "Charlie Farnesbarnes"
                      :nastinessScore 888
                      :nicenessScore 0
                      :friends nice-people
                      }
                     :NastyPerson)])

(def all-people (into nice-people nasty-people))

(defn query-all-people
  [context args value]
  all-people)

(defn query-nice-people
  [context args value]
  nice-people)

(defn query-nasty-people
  [context args value]
  nasty-people)

(defn r-things
  [context args value]
  [(schema/tag-with-type
     {:id   "id"
      :name "Mary"
      }
     :Thing)
   (schema/tag-with-type
     {:id   "id2"
      :name "Bob"
      }
     :Thing)])

(defn get-directives
  [context]
  (->> (-> (get-in context [:request :parsed-lacinia-query :selections])
           first
           :field-definition
           :directives)
       (mapv (juxt :directive-type :directive-args))
       (reduce merge {})))

(defn r-bank
  [context args value]

  (clojure.pprint/pprint (get-directives context))

  ;(clojure.pprint/pprint [{:directives (get-directives context)
  ;                         :args args
  ;                         :value value}])
  (schema/tag-with-type
    {
      :name "Billy Smudgekins Western Allied Bank"
     }
    :Bank))

(defn r-privates
  [context args value]
  ["Thus" "as" "a" "secsssret"])


(defn s-burp
  [context args source-stream]

  (let []
    (println "STREAMER")
    (future
      (Thread/sleep 5000)
      (source-stream nil)))
  (fn [x] "Whatever")
  ;; Create an object for the subscription.
  #_(let [subscription (create-log-subscription)]
    (on-publish subscription
                (fn [log-event]
                  (-> log-event :payload source-stream)))
    ;; Return a function to cleanup the subscription
    #(stop-log-subscription subscription)))

(defn attachments
  []
  {:resolvers {:Query    {:find_all_in_episode find-all-in-episode
                          :get_person     get-person
                          :allPeople      query-all-people
                          :nicePeople     query-nice-people
                          :nastyPeople    query-nasty-people
                          :things         r-things
                          :bank           r-bank}
               :Thing {
                       :things         r-things
                       }
               :Bank {
                      :privates r-privates
                      }
               :Mutation {:add_character add-character}
               :Character {:episodes episodes}
               ;:Subscription {
               ;               :result :s-result
               ;               }
               }

   ;:streamers {
   ;            :burp s-burp
   ;            }

   :documentation {:Character                         "A Star Wars character"
                   :Character/name                    "Character name"
                   :Query/find_all_in_episode         "Find all characters in the given episode"
                   :Query/find_all_in_episode.episode "Episode for which to find characters."}

   :scalars {:Date
             {:parse     #(when (string? %)
                            (try
                              ; String -> Date
                              (.parse date-formatter %)
                              (catch Throwable _
                                nil)))
              :serialize #(try
                            ; Date -> String(Buffer)
                            (.format date-formatter %)
                            (catch Throwable _
                              nil))}}})
