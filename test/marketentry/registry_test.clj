(ns marketentry.registry-test
  (:require [clojure.test :refer [deftest is testing]]
            [marketentry.registry :as registry]))

(deftest engagement-fee-recompute
  (let [e {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12 :claimed-fee 860000.0}]
    (is (== 860000.0 (registry/compute-engagement-fee e)))
    (is (true? (registry/engagement-fee-matches-claim? e))))
  (let [bad {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12 :claimed-fee 999000.0}]
    (is (false? (registry/engagement-fee-matches-claim? bad)))))

(deftest register-draft-and-submit
  (let [d (registry/register-draft "eng-1" "BEN" 0)
        s (registry/register-submit "eng-1" "BEN" 0)]
    (is (= "BEN-DFT-000000" (get d "draft_number")))
    (is (= "BEN-SUB-000000" (get s "submit_number")))
    (is (nil? (get-in d ["certificate" "proof"])))
    (is (= "draft-unsigned" (get-in s ["certificate" "status"])))))

(deftest register-requires-ids
  (is (thrown? Exception (registry/register-draft "" "BEN" 0)))
  (is (thrown? Exception (registry/register-submit "eng-1" "" 0))))

(deftest required-preference-margin-tiers
  (testing "verified MPME -> mandatory 5% (Art. 77 al.3)"
    (is (== 0.05 (registry/required-preference-margin {:mpme-status? true}))))
  (testing "non-MPME (false or missing) -> no free preference"
    (is (== 0.0 (registry/required-preference-margin {:mpme-status? false})))
    (is (== 0.0 (registry/required-preference-margin {})))))

(deftest compute-evaluated-price-applies-margin-only-when-entitled
  (testing "verified MPME -> evaluated price is 95% of base price"
    (is (== 950000.0 (registry/compute-evaluated-price {:mpme-status? true :base-price 1000000}))))
  (testing "non-MPME -> evaluated price equals base price, no discount"
    (is (== 1000000.0 (registry/compute-evaluated-price {:mpme-status? false :base-price 1000000})))))

(deftest evaluated-price-mismatch
  (testing "claimed evaluated price matches the recompute -> no mismatch"
    (is (false? (registry/evaluated-price-mismatches-claim?
                 {:mpme-status? true :base-price 1000000 :claimed-evaluated-price 950000.0}))))
  (testing "non-MPME claiming a preference it is not entitled to -> mismatch"
    (is (true? (registry/evaluated-price-mismatches-claim?
                {:mpme-status? false :base-price 1000000 :claimed-evaluated-price 950000.0}))))
  (testing "verified MPME denied the preference it IS entitled to -> mismatch"
    (is (true? (registry/evaluated-price-mismatches-claim?
                {:mpme-status? true :base-price 1000000 :claimed-evaluated-price 1000000.0})))))
