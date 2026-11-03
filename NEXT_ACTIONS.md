# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 24/26 (92.3%)
- **Function parity:** 757/880 matched (target 1226) — 86.0%
- **Class/type parity:** 69/90 matched (target 119) — 76.7%
- **Combined symbol parity:** 826/970 matched (target 1345) — 85.2%
- **Average inline-code cosine:** 0.12 (function body across 24 matched files)
- **Average documentation cosine:** 0.38 (doc text across 24 matched files)
- **Cheat-zeroed Files:** 1
- **Critical Issues:** 24 files with <0.60 function similarity
- **Needs Review:** 0 files with 0.60-0.84 function similarity
- **Excellent:** 0 files with >=0.85 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. map.slice

- **Target:** `map.Slice`
- **Similarity:** 0.09
- **Dependents:** 5
- **Priority Score:** 5107609.0
- **Functions:** 62/72 matched (target 101)
- **Missing functions:** `into_iter`, `into_iter`, `default`, `default`, `eq`, `eq`, `eq`, `eq`, `check`, `check_mut`
- **Types:** 4/4 matched (target 9)
- **Missing types:** _none_
- **Tests:** 10/12 matched

### 2. rayon.set

- **Target:** `rayon.Set`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 535610.0
- **Functions:** 3/46 matched (target 7)
- **Missing functions:** `into_par_iter`, `fmt`, `into_par_iter`, `into_par_iter`, `clone`, `fmt`, `par_drain`, `par_difference`, `par_symmetric_difference`, `par_intersection`, `par_union`, `par_eq`, `par_is_disjoint`, `par_is_superset`, `par_is_subset`, `clone`, `fmt`, `drive_unindexed`, `clone`, `fmt`, `drive_unindexed`, `clone`, `fmt`, `drive_unindexed`, `clone`, `fmt`, `drive_unindexed`, `par_sorted_by`, `par_sort_by_key`, `par_sort_unstable`, `par_sort_unstable_by`, `par_sorted_unstable_by`, `par_sort_unstable_by_key`, `par_sort_by_cached_key`, `from_par_iter`, `par_extend`, `par_extend`, `insert_order`, `partial_eq_and_eq`, `extend`, `comparisons`, `iter_comparisons`, `check`
- **Types:** 0/10 matched (target 2)
- **Missing types:** `Bucket`, `Item`, `Iter`, `IntoParIter`, `ParIter`, `ParDrain`, `ParDifference`, `ParIntersection`, `ParSymmetricDifference`, `ParUnion`
- **Tests:** 0/6 matched

### 3. rayon.map

- **Target:** `rayon.Map`
- **Similarity:** 0.01
- **Dependents:** 0
- **Priority Score:** 415009.9
- **Functions:** 5/41 matched (target 11)
- **Missing functions:** `into_par_iter`, `fmt`, `into_par_iter`, `into_par_iter`, `clone`, `fmt`, `into_par_iter`, `into_par_iter`, `fmt`, `par_drain`, `par_keys`, `par_values`, `par_eq`, `clone`, `fmt`, `clone`, `fmt`, `par_values_mut`, `par_values_mut`, `par_sorted_by`, `par_sort_by_key`, `par_sort_unstable_keys`, `par_sort_unstable_by`, `par_sorted_unstable_by`, `par_sort_unstable_by_key`, `par_sort_by_cached_key`, `fmt`, `from_par_iter`, `par_extend`, `par_extend`, `insert_order`, `partial_eq_and_eq`, `extend`, `keys`, `values`, `values_mut`
- **Types:** 4/9 matched (target 4)
- **Missing types:** `Item`, `Iter`, `ParIterMut`, `ParDrain`, `ParValuesMut`
- **Tests:** 0/6 matched

### 4. set.iter

- **Target:** `set.Iter`
- **Similarity:** 0.04
- **Dependents:** 0
- **Priority Score:** 96809.6
- **Functions:** 48/57 matched (target 79)
- **Missing functions:** `new`, `new`, `new`, `new`, `new`, `new`, `new`, `size_hint`, `fmt`
- **Types:** 11/11 matched (target 12)
- **Missing types:** _none_

### 5. set.slice

- **Target:** `set.Slice`
- **Similarity:** 0.06
- **Dependents:** 0
- **Priority Score:** 74509.4
- **Functions:** 34/41 matched (target 62)
- **Missing functions:** `into_iter`, `default`, `eq`, `eq`, `eq`, `eq`, `check`
- **Types:** 4/4 matched (target 5)
- **Missing types:** _none_
- **Tests:** 1/2 matched

### 6. map

- **Target:** `indexmap.Map`
- **Similarity:** 0.08
- **Dependents:** 0
- **Priority Score:** 51409.2
- **Functions:** 108/112 matched (target 148)
- **Missing functions:** `fmt`, `index`, `index_mut`, `extend`
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_

### 7. map.iter

- **Target:** `map.Iter`
- **Similarity:** 0.05
- **Dependents:** 0
- **Priority Score:** 38109.5
- **Functions:** 64/67 matched (target 162)
- **Missing functions:** `new`, `new`, `new`
- **Types:** 14/14 matched (target 15)
- **Missing types:** _none_

### 8. map.mutable

- **Target:** `map.Mutable`
- **Similarity:** 0.06
- **Dependents:** 0
- **Priority Score:** 31309.4
- **Functions:** 5/8 matched (target 5)
- **Missing functions:** `key_mut`, `key_mut`, `key_mut`
- **Types:** 5/5 matched
- **Missing types:** _none_

### 9. set

- **Target:** `indexmap.Set`
- **Similarity:** 0.19
- **Dependents:** 0
- **Priority Score:** 30208.1
- **Functions:** 97/99 matched (target 149)
- **Missing functions:** `fmt`, `extend`
- **Types:** 3/3 matched (target 4)
- **Missing types:** _none_

### 10. map.tests

- **Target:** `map.MapTests`
- **Similarity:** 0.27
- **Dependents:** 0
- **Priority Score:** 27007.3
- **Functions:** 67/67 matched (target 74)
- **Missing functions:** _none_
- **Types:** 1/3 matched (target 4)
- **Missing types:** `K`, `V`
- **Tests:** 63/63 matched

### 11. inner

- **Target:** `inner.Inner`
- **Similarity:** 0.18
- **Dependents:** 0
- **Priority Score:** 25808.2
- **Functions:** 53/55 matched (target 68)
- **Missing functions:** `par_drain`, `assert_send_sync`
- **Types:** 3/3 matched (target 4)
- **Missing types:** _none_
- **Tests:** 0/1 matched

### 12. serde

- **Target:** `indexmap.Serde`
- **Similarity:** 0.21
- **Dependents:** 0
- **Priority Score:** 21507.9
- **Functions:** 11/11 matched (target 46)
- **Missing functions:** _none_
- **Types:** 2/4 matched
- **Missing types:** `Value`, `Deserializer`

### 13. arbitrary

- **Target:** `indexmap.Arbitrary`
- **Similarity:** 0.04
- **Dependents:** 0
- **Priority Score:** 20809.6
- **Functions:** 6/8 matched (target 11)
- **Missing functions:** `arbitrary`, `arbitrary`
- **Types:** 0/0 matched (target 6)
- **Missing types:** _none_

### 14. set.tests

- **Target:** `set.SetTests`
- **Similarity:** 0.32
- **Dependents:** 0
- **Priority Score:** 15806.8
- **Functions:** 57/57 matched
- **Missing functions:** _none_
- **Types:** 0/1 matched
- **Missing types:** `Item`
- **Tests:** 53/53 matched

### 15. map.entry

- **Target:** `map.Entry`
- **Similarity:** 0.16
- **Dependents:** 0
- **Priority Score:** 13508.4
- **Functions:** 32/33 matched (target 73)
- **Missing functions:** `assert_send_sync`
- **Types:** 2/2 matched (target 6)
- **Missing types:** _none_
- **Tests:** 0/1 matched

### 16. map.serde_seq

- **Target:** `map.SerdeSeq`
- **Similarity:** 0.16
- **Dependents:** 0
- **Priority Score:** 10808.4
- **Functions:** 6/6 matched (target 12)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Value`

### 17. borsh

- **Target:** `indexmap.Borsh`
- **Similarity:** 0.06
- **Dependents:** 0
- **Priority Score:** 10709.4
- **Functions:** 6/7 matched (target 19)
- **Missing functions:** `serialize`
- **Types:** 0/0 matched (target 7)
- **Missing types:** _none_
- **Tests:** 2/2 matched

### 18. map.raw_entry_v1

- **Target:** `map.RawEntryV1`
- **Similarity:** 0.14
- **Dependents:** 0
- **Priority Score:** 5308.6
- **Functions:** 46/46 matched (target 63)
- **Missing functions:** _none_
- **Types:** 7/7 matched (target 10)
- **Missing types:** _none_
- **Tests:** 1/1 matched

### 19. inner.entry

- **Target:** `inner.Entry`
- **Similarity:** 0.17
- **Dependents:** 0
- **Priority Score:** 3608.3
- **Functions:** 34/34 matched
- **Missing functions:** _none_
- **Types:** 2/2 matched
- **Missing types:** _none_

### 20. set.mutable

- **Target:** `set.Mutable`
- **Similarity:** 0.17
- **Dependents:** 0
- **Priority Score:** 608.3
- **Functions:** 3/3 matched
- **Missing functions:** _none_
- **Types:** 3/3 matched
- **Missing types:** _none_

### 21. inner.extract

- **Target:** `inner.Extract`
- **Similarity:** 0.08
- **Dependents:** 0
- **Priority Score:** 509.2
- **Functions:** 4/4 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 22. util

- **Target:** `indexmap.Util`
- **Similarity:** 0.37
- **Dependents:** 0
- **Priority Score:** 406.3
- **Functions:** 4/4 matched (target 14)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 7)
- **Missing types:** _none_

### 23. sval

- **Target:** `indexmap.Sval`
- **Similarity:** 0.07
- **Dependents:** 0
- **Priority Score:** 209.3
- **Functions:** 2/2 matched (target 18)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 3)
- **Missing types:** _none_

### 24. macros

- **Target:** `indexmap.Macros [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 6)
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

## Reexport / Wiring Modules

These files match `reexport_modules` patterns in `.ast_distance_config.json`. They are filtered out of
normal priority and missing-file ladders because they are wiring
modules, not direct logic ports. Consult them for call-site routing;
do not treat them as the next implementation target by default.

### Missing

| Source | Expected target | Deps | Source path | Expected path |
|--------|-----------------|------|-------------|---------------|
| `lib` | `Lib` | 0 | `lib.rs` | `Lib.kt` |
| `rayon.mod` | `rayon.Mod` | 0 | `rayon/mod.rs` | `rayon/Mod.kt` |

