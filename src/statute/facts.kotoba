(ns statute.facts
  "General-law compliance catalog for Benin (BEN) -- extends this repo's
  existing `marketentry.facts` (public-procurement market-entry only,
  narrow scope) with a second, orthogonal catalog of statutes a company
  operating in this jurisdiction must generally track for compliance.
  Mirrors cloud-itonami-iso3166-jpn/-deu/-bgr/-aze/-alb/-arm/-atg's
  `statute.facts` (ADR-2607141700, cloud-itonami-compliance-fact-
  federation).

  Every entry cites an OFFICIAL government-hosted (or, for the OHADA
  entry, official supranational-body-hosted) URL -- never fabricated.
  Benin's official legal-acts portal is sgg.gouv.bj (Secrétariat Général
  du Gouvernement, Présidence de la République) -- unlike several other
  jurisdictions this loop has hit with JS-single-page-app or TLS
  problems, sgg.gouv.bj's own `/doc/<slug>/` and `/doc/<slug>/download`
  pages rendered directly on the FIRST WebFetch/curl attempt for every
  law in this catalog, and the downloaded PDF text was read directly
  (via `pdftotext -layout`), confirming each law's own title/number/
  date in its own words -- HIGH confidence, no secondary-source
  fallback needed for any entry below:

  - Companies/commercial-entity law: this iteration specifically
    investigated, rather than assumed by analogy to prior siblings,
    whether Benin has a domestic 'Commerce Act' of its own (the shape
    Bulgaria's Commerce Act, Albania's Ligji Nr. 9901/2008, and
    Armenia's Civil Code + standalone LLC law each provide). It does
    NOT -- and this is a genuinely different, first-of-family finding:
    Benin, as one of OHADA's 17 member states, has company law governed
    DIRECTLY by a SUPRANATIONAL instrument, the OHADA Uniform Act on
    Commercial Companies and Economic Interest Groupings (Acte uniforme
    relatif au droit des sociétés commerciales et du groupement
    d'intérêt économique, AUSCGIE) -- adopted 30 January 2014 in
    Ouagadougou, in force since 5 May 2014 (WebFetch-verified directly
    against ohada.org's own official page). Article 10 of the OHADA
    Treaty (Traité de Port-Louis, 17 October 1993, revised in Québec on
    17 October 2008) gives every Uniform Act direct and obligatory
    effect in every member state, 'nonobstant toutes dispositions
    contraires de droit interne, antérieure ou postérieure' -- so
    AUSCGIE applies in Benin without any domestic transposition act,
    and this entry cites OHADA's own hosting rather than inventing a
    nonexistent Beninese statute. (Article 10's exact wording is
    WebSearch-corroborated across multiple independent legal-commentary
    sources -- village-justice.com, legavox.fr -- rather than fetched
    directly from a primary OHADA treaty-text page, so MODERATE-HIGH
    confidence on the precise wording quoted here; HIGH confidence on
    the substantive direct-effect fact itself, which every independent
    source checked states identically. Separately, RCCM/business-entity
    REGISTRATION -- as opposed to company FORMATION/governance law -- is
    governed by a DIFFERENT OHADA instrument, the Acte Uniforme relatif
    au Droit Commercial Général (AUDCG); this catalog does not conflate
    the two, and `marketentry.facts` cites AUDCG separately for RCCM.)
  - Code du Travail (Labour Code): Loi N°98-004 du 27 janvier 1998
    portant Code du Travail en République du Bénin -- downloaded
    directly from sgg.gouv.bj/doc/loi-98-004/download and read (Article
    1er: 'La présente loi est applicable aux travailleurs et aux
    employeurs exerçant leur activité professionnelle en République du
    Bénin'), independently corroborated by the Ministry of Labour's own
    hosting at travail.gouv.bj. HIGH confidence.
  - Code du Numérique (Digital Code, data protection): Loi N°2017-20 du
    20 avril 2018 portant Code du Numérique en République du Bénin --
    downloaded directly from sgg.gouv.bj/doc/loi-2017-20/download and
    read. The law's own text (Article 1er's definitions section)
    directly confirms that its Livre V governs personal-data protection
    via an 'Autorité de Protection des Données Personnelles (APDP)',
    and separately defines 'CNIL: Commission Nationale de l'Informatique
    et des Libertés' as the predecessor body APDP supersedes -- read
    directly from the primary law text (not a secondary summary), HIGH
    confidence for both the law's own citation and the APDP/Livre-V/CNIL
    -predecessor facts.

  A law not in this table has NO spec-basis, full stop; extend
  `catalog`, do not invent an id/url.")

(def catalog
  "iso3 -> vector of statute entries. `:statute/url` + `:statute/law-number`
  are the citation the governor requires before any compliance-fact
  proposal referencing this law can commit."
  {"BEN"
   [{:statute/id "ben.ohada-auscgie"
     :statute/title "Acte uniforme relatif au droit des sociétés commerciales et du groupement d'intérêt économique (AUSCGIE)"
     :statute/jurisdiction "BEN"
     :statute/kind :law
     :statute/law-number "OHADA Uniform Act -- adopted 30 January 2014 (Ouagadougou), in force 5 May 2014; directly applicable in Benin as an OHADA member state per Traité de Port-Louis Art. 10, no domestic transposition act required"
     :statute/url "https://www.ohada.org/en/commercial-companies-and-economic-interest-groups/"
     :statute/url-provenance :official-ohada-org
     :statute/enacted-date "2014-01-30"
     :statute/retrieved-at "2026-07-21"
     :statute/topic #{:corporate-governance :incorporation}}
    {:statute/id "ben.code-du-travail"
     :statute/title "Code du Travail en République du Bénin"
     :statute/jurisdiction "BEN"
     :statute/kind :law
     :statute/law-number "Loi N°98-004 du 27 janvier 1998"
     :statute/url "https://sgg.gouv.bj/doc/loi-98-004/download"
     :statute/url-provenance :official-sgg-gouv-bj
     :statute/enacted-date "1998-01-27"
     :statute/retrieved-at "2026-07-21"
     :statute/topic #{:labor :employment}}
    {:statute/id "ben.code-du-numerique"
     :statute/title "Code du Numérique en République du Bénin"
     :statute/jurisdiction "BEN"
     :statute/kind :law
     :statute/law-number "Loi N°2017-20 du 20 avril 2018"
     :statute/url "https://sgg.gouv.bj/doc/loi-2017-20/download"
     :statute/url-provenance :official-sgg-gouv-bj
     :statute/enacted-date "2018-04-20"
     :statute/retrieved-at "2026-07-21"
     :statute/topic #{:data-protection :privacy}}]})

(defn spec-basis
  "The jurisdiction's statute vector, or nil -- nil means NO spec-basis
  for that jurisdiction yet."
  [iso3]
  (get catalog iso3))

(defn coverage
  "Honest coverage report, same shape/discipline as `marketentry.facts/coverage`:
  never report a missing jurisdiction as covered."
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-ben statute.facts Wave 0 (ADR-2607141700): "
                 (count (get catalog "BEN")) " BEN statutes seeded with an "
                 "official citation. Extend "
                 "`statute.facts/catalog`, never fabricate a law-id or URL.")})))

(defn by-topic
  "Statutes for `iso3` tagged with `topic` (e.g. :labor, :data-protection)."
  [iso3 topic]
  (filterv #(contains? (:statute/topic %) topic) (spec-basis iso3)))
