(ns culture.facts
  "Country-level regional-culture catalog for Benin (BEN) -- national
  dishes, protected products, beverages, crafts, festivals and heritage
  sites, per ADR-2607171400 addendum 2 (cloud-itonami-municipality-
  culture-catalog Wave 1, in com-junkawasaki/root). Sibling namespace to
  `marketentry.facts` / `statute.facts` (ADR-2607141700); city-level
  counterparts live in the cloud-itonami-municipality-* repos.

  Catalog is keyed by UPPERCASE ISO3 (mirrors `statute.facts`); entries
  carry no :culture/municipality (that attribute is city-level only).

  Every entry cites a source URL that was actually fetched and read on
  :culture/retrieved-at -- never fabricated. Summaries state only what the
  cited source confirms. An item not in this table has NO spec-basis, full
  stop; extend `catalog`, do not invent an id/url.")

(def catalog
  "iso3 -> vector of culture entries."
  {"BEN"
   [{:culture/id "ben.dish.kuli-kuli"
     :culture/name "Kuli-kuli"
     :culture/country "BEN"
     :culture/kind :dish
     :culture/summary "Peanut-based West African snack first made by the Nupe people of Nigeria; a popular snack in Benin, Nigeria, northern Cameroon and Ghana."
     :culture/url "https://en.wikipedia.org/wiki/Kuli-kuli"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "ben.dish.akara"
     :culture/name "Akara"
     :culture/name-local "Àkàrà"
     :culture/country "BEN"
     :culture/kind :dish
     :culture/summary "Deep-fried fritter of cowpeas (black-eyed peas) originating in Yorubaland, the Yoruba region spanning parts of Nigeria, Benin and Togo."
     :culture/url "https://en.wikipedia.org/wiki/Akara"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "ben.dish.amiwo"
     :culture/name "Amiwo"
     :culture/country "BEN"
     :culture/kind :dish
     :culture/summary "Beninese dish of red corn dough, often made with tomato puree, onion and peppers."
     :culture/url "https://en.wikipedia.org/wiki/Beninese_cuisine"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "ben.beverage.sodabi"
     :culture/name "Sodabi"
     :culture/country "BEN"
     :culture/kind :beverage
     :culture/summary "Traditional West African liquor distilled from palm wine; the term is mainly used in Benin and Togo, where it holds an important place in daily life and religious practices."
     :culture/url "https://en.wikipedia.org/wiki/Sodabi"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "ben.product.wagasi"
     :culture/name "Wagasi"
     :culture/country "BEN"
     :culture/kind :product
     :culture/summary "West African cheese made from cow's milk, commonly made in northern Benin and sold in abundance in Parakou."
     :culture/url "https://en.wikipedia.org/wiki/Wagasi"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "ben.festival.vaudoun-day"
     :culture/name "Vaudoun Day"
     :culture/country "BEN"
     :culture/kind :festival
     :culture/summary "Public holiday of Benin observed on the second Friday in January, celebrating Vodoun practices and observances."
     :culture/url "https://en.wikipedia.org/wiki/Public_holidays_in_Benin"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "ben.heritage.royal-palaces-of-abomey"
     :culture/name "Royal Palaces of Abomey"
     :culture/country "BEN"
     :culture/kind :heritage
     :culture/summary "Ten palaces of the Kingdom of Dahomey in Abomey, built between 1695 and 1900, inscribed by UNESCO on the World Heritage List in 1985."
     :culture/url "https://en.wikipedia.org/wiki/Royal_Palaces_of_Abomey"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}]})

(defn spec-basis [iso3] (get catalog iso3))

(defn coverage
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-ben culture catalog "
                 "(ADR-2607171400 addendum 2, Wave 1): " (count (get catalog "BEN"))
                 " BEN entries, each with a fetched-and-read citation. "
                 "Extend `culture.facts/catalog`, never fabricate an id/url.")})))

(defn by-kind [iso3 kind]
  (filterv #(= (:culture/kind %) kind) (spec-basis iso3)))
