(defproject lacinia-playground "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.0"]

                 ; GraphQL
                 [com.walmartlabs/lacinia "1.2-alpha-3"]
                 [com.walmartlabs/lacinia-pedestal "1.1"]

                 ; Database
                 [com.h2database/h2 "1.4.200"]
                 [com.github.seancorfield/next.jdbc "1.3.834"]
                 [migratus "1.4.4"]
                 [hikari-cp "2.14.0"]

                 ; Logging
                 ;[io.aviso/logging "0.2.0"]
                 [com.taoensso/timbre "5.2.1"]
                 [com.fzakaria/slf4j-timbre "0.3.21"]
                 ]
  :repl-options {:init-ns lacinia-playground.core}
  )
