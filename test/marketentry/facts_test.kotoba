(ns marketentry.facts-test
  (:require [clojure.test :refer [deftest is testing]]
            [marketentry.facts :as facts]))

(deftest ben-has-spec-basis
  (let [sb (facts/spec-basis "BEN")]
    (is (some? sb))
    (is (string? (:provenance sb)))
    (is (seq (:required-evidence sb)))
    (is (some? (facts/corporate-number-spec-basis "BEN")))
    (is (some? (facts/mpme-preference-margin-spec-basis "BEN")))))

(deftest ben-rep-spec-basis-is-honestly-absent
  (testing "Benin's Art. 61/62 exclusion-extension provisions cover consortium members and subcontractors, not a bidder's own representatives/directors -- deliberately not claimed"
    (is (nil? (facts/rep-spec-basis "BEN")))))

(deftest unknown-jurisdiction-has-no-spec-basis
  (is (nil? (facts/spec-basis "ATL")))
  (is (nil? (facts/spec-basis "ZZZ"))))

(deftest required-evidence-satisfied
  (let [sb (facts/spec-basis "BEN")
        all (:required-evidence sb)]
    (is (true? (facts/required-evidence-satisfied? "BEN" all)))
    (is (not (facts/required-evidence-satisfied? "BEN" (take 1 all))))
    (is (nil? (facts/required-evidence-satisfied? "ATL" all)))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["BEN" "USA" "ATL"])]
    (is (= 3 (:requested c)))
    (is (= 2 (:covered c)))
    (is (= ["ATL"] (:missing-jurisdictions c)))))
