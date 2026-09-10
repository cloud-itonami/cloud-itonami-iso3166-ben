# cloud-itonami-iso3166-ben

**BEN**: Republic of Benin.

- ARMP e-procurement
- RCCM + IFU

AGPL-3.0-or-later.

## Market-entry / statute catalogs

Governed public-sector market-entry compliance actor, same architecture
as `cloud-itonami-iso3166-jpn`/`-deu` (minus the JPN-specific `goyoukiki`
bridge):

- `src/marketentry/{facts,governor,phase,sim,operation,registry,store,
  marketentryllm}.cljc` -- the actor. `facts.cljc` cites the Autorité de
  Régulation des Marchés Publics (ARMP, Loi N°2020-26 du 29 septembre
  2020), the Direction Nationale de Contrôle des Marchés Publics (DNCMP,
  SIGMAP/marches-publics.bj), RCCM (Tribunal de Commerce, OHADA's Acte
  Uniforme relatif au Droit Commercial Général) and IFU (Direction
  Générale des Impôts, Décret N°2006-201). `governor.cljc`'s flagship
  check independently recomputes Loi N°2020-26 Art. 77 al.3's mandatory
  5% MPME bid-evaluation preference margin.
- `src/statute/facts.kotoba` -- general-law catalog: the OHADA Uniform Act
  on Commercial Companies (AUSCGIE, directly applicable, no domestic
  transposition act), the Code du Travail (Loi N°98-004), and the Code
  du Numérique (Loi N°2017-20, creates the APDP data-protection
  authority).

Every citation is WebFetch/curl-verified against an official source
(sgg.gouv.bj, ohada.org, armp.bj, justice.gouv.bj); see each namespace's
docstring for the full research trail and any honestly-narrowed scope.

## Culture catalog

Alongside the market-entry / statute catalogs, this repo carries a
**country-level regional-culture catalog** (ADR-2607171400 addendum 2,
`cloud-itonami-municipality-culture-catalog` Wave 1, in
`com-junkawasaki/root`) — national dishes, protected products, beverages,
crafts, festivals and heritage sites for Benin:

- `src/culture/facts.kotoba` — the catalog, source of truth (keyed by
  uppercase ISO3, mirroring `statute.facts`).
- `schema/culture.edn` — DataScript schema.
- `data/culture-tx.edn` — derived DataScript tx-data (regenerated from
  the catalog, never hand-edited).

City-level counterparts live in the `cloud-itonami-municipality-*` repos.
Same provenance discipline as the compliance catalogs: every entry cites a
source URL that was actually fetched and read on `:culture/retrieved-at`;
summaries state only what the cited source confirms. An item not in
`culture.facts/catalog` has no spec-basis — never fabricate one.
