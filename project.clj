(defproject lacinia-playground "0.1.0-SNAPSHOT"
  :description "Playground project to try out some ideas with Lacinia and GraphQL"
  :url "http://www.hayzee.com/"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [
                 ; Clojure
                 [org.clojure/clojure "1.12.0"]

                 ; Lacinia/GraphQL
                 [com.walmartlabs/lacinia "1.2.2"]
                 [com.walmartlabs/lacinia-pedestal "1.3.1"]

                 ; Components
                 [mount "0.1.21"]

                 ; Configuration
                 [aero "1.1.6"]

                 ; Database, JDBC, Pooling and Migrations
                 [org.postgresql/postgresql "42.7.4"]
                 [com.github.seancorfield/next.jdbc "1.3.834"]
                 [hikari-cp "3.0.0"]
                 [migratus "1.4.4"]

                 ; Logging
                 ;[io.aviso/logging "0.2.0"]
                 [com.taoensso/timbre "5.2.1"]
                 [com.fzakaria/slf4j-timbre "0.3.21"]

                 ]

  :profiles {:dev
             {:dependencies [

                             [io.zonky.test/embedded-postgres "2.1.0"]
                             ;[io.zonky.test.postgres/embedded-postgres-binaries-darwin-amd64 "17.2.0"]
                             ;[io.zonky.test.postgres/embedded-postgres-binaries-linux-amd64 "17.2.0"]
                             [io.zonky.test.postgres/embedded-postgres-binaries-windows-amd64 "17.2.0"]

                             [org.clojure/tools.namespace "1.5.0"]

                             ]}}

  :repl-options {:init-ns user}

  :main lacinia-playground.core
  :aot [lacinia-playground.core]

  )
