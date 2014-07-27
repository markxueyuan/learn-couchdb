(ns learn-couchdb.crud
  (:use [com.ashafa.clutch :only (create-database with-db put-document
                                                  get-document delete-document)
         :as clutch]))


(def db (create-database "repl-crud"))

(put-document db {:_id "foo" :some-data "bar"})

(put-document db (assoc *1 :other-data "quux"))

(get-document db "foo")

(delete-document db *1)

(clutch/bulk-update (create-database "logging")
                    [{:evt-type "auth/new-user" :username "Chas"}
                     {:evt-type "auth/new-user" :username "Dave"}
                     {:evt-type "sales/purchase" :username "Chas" :products ["widget1"]}
                     {:evt-type "sales/purchase" :username "Robin" :products ["widget14"]}
                     {:evt-type "sales/RFQ" :username "Robin" :budget 20000}])

(clutch/save-view "logging" "jsviews"
                  (clutch/view-server-fns :javascript
                                          {:type-counts
                                           {:map "function(doc) {
                                            emit(doc['evt-type'], null);
                                            }"
                                            :reduce "function (keys, vals, rereduce) {
                                            return rereduce ? sum(vals) : vals.length;
                                            }"}}))

