# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 18/26 (69.2%)
- **Function parity:** 486/747 matched (target 949) — 65.1%
- **Class/type parity:** 46/95 matched (target 78) — 48.4%
- **Combined symbol parity:** 532/842 matched (target 1027) — 63.2%
- **Average inline-code cosine:** 0.42 (function body across 18 matched files)
- **Average documentation cosine:** 0.34 (doc text across 18 matched files)
- **Cheat-zeroed Files:** 1
- **Critical Issues:** 13 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. map.slice

- **Target:** `map.Slice`
- **Similarity:** 0.32
- **Dependents:** 5
- **Priority Score:** 5106807.0
- **Functions:** 57/64 matched (target 95)
- **Missing functions:** `check`, `slice_index_mut`, `check_mut`, `slice_new_mut`, `slice_get_index_mut`, `slice_split_first_mut`, `slice_split_last_mut`
- **Types:** 1/4 matched (target 3)
- **Missing types:** `IntoIter`, `Item`, `Output`
- **Tests:** 5/12 matched

### 2. serde

- **Target:** `indexmap.Serde`
- **Similarity:** 0.13
- **Dependents:** 0
- **Priority Score:** 81108.7
- **Functions:** 3/7 matched (target 8)
- **Missing functions:** `expecting`, `visit_map`, `into_deserializer`, `visit_seq`
- **Types:** 0/4 matched (target 3)
- **Missing types:** `IndexMapVisitor`, `Value`, `Deserializer`, `IndexSetVisitor`

### 3. map.mutable

- **Target:** `map.Mutable`
- **Similarity:** 0.09
- **Dependents:** 0
- **Priority Score:** 71009.1
- **Functions:** 1/5 matched (target 1)
- **Missing functions:** `get_full_mut2`, `get_index_mut2`, `iter_mut2`, `retain2`
- **Types:** 2/5 matched (target 2)
- **Missing types:** `Key`, `Value`, `Sealed`

### 4. map.iter

- **Target:** `map.Iter`
- **Similarity:** 0.32
- **Dependents:** 0
- **Priority Score:** 52806.8
- **Functions:** 11/14 matched (target 136)
- **Missing functions:** `into_iter`, `index`, `drop`
- **Types:** 12/14 matched (target 13)
- **Missing types:** `Item`, `Output`

### 5. set.iter

- **Target:** `set.Iter`
- **Similarity:** 0.25
- **Dependents:** 0
- **Priority Score:** 52307.5
- **Functions:** 9/12 matched (target 71)
- **Missing functions:** `into_iter`, `fold`, `rfold`
- **Types:** 9/11 matched (target 10)
- **Missing types:** `Item`, `UnitValue`

### 6. set.slice

- **Target:** `set.Slice`
- **Similarity:** 0.31
- **Dependents:** 0
- **Priority Score:** 43906.9
- **Functions:** 34/35 matched (target 60)
- **Missing functions:** `check`
- **Types:** 1/4 matched (target 2)
- **Missing types:** `IntoIter`, `Item`, `Output`
- **Tests:** 1/2 matched

### 7. map.serde_seq

- **Target:** `map.SerdeSeq`
- **Similarity:** 0.09
- **Dependents:** 0
- **Priority Score:** 40609.1
- **Functions:** 2/4 matched (target 2)
- **Missing functions:** `expecting`, `visit_seq`
- **Types:** 0/2 matched (target 1)
- **Missing types:** `SeqVisitor`, `Value`

### 8. inner

- **Target:** `inner.Inner`
- **Similarity:** 0.69
- **Dependents:** 0
- **Priority Score:** 35703.1
- **Functions:** 52/54 matched (target 67)
- **Missing functions:** `par_drain`, `assert_send_sync`
- **Types:** 2/3 matched
- **Missing types:** `Entries`
- **Tests:** 0/1 matched
- **Lint issues:** 7

### 9. map.raw_entry_v1

- **Target:** `map.RawEntryV1`
- **Similarity:** 0.50
- **Dependents:** 0
- **Priority Score:** 34205.0
- **Functions:** 33/35 matched (target 61)
- **Missing functions:** `raw_entry_v1`, `raw_entry_mut_v1`
- **Types:** 6/7 matched (target 9)
- **Missing types:** `Sealed`
- **Tests:** 1/1 matched

### 10. set

- **Target:** `indexmap.Set`
- **Similarity:** 0.59
- **Dependents:** 0
- **Priority Score:** 30004.1
- **Functions:** 97/97 matched (target 148)
- **Missing functions:** _none_
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Bucket`, `Output`

### 11. inner.entry

- **Target:** `inner.Entry`
- **Similarity:** 0.64
- **Dependents:** 0
- **Priority Score:** 23303.6
- **Functions:** 29/31 matched (target 32)
- **Missing functions:** `new`, `from`
- **Types:** 2/2 matched
- **Missing types:** _none_

### 12. map.entry

- **Target:** `map.Entry`
- **Similarity:** 0.51
- **Dependents:** 0
- **Priority Score:** 22904.9
- **Functions:** 25/27 matched (target 63)
- **Missing functions:** `key_mut`, `assert_send_sync`
- **Types:** 2/2 matched (target 6)
- **Missing types:** _none_
- **Tests:** 0/1 matched

### 13. map

- **Target:** `indexmap.Map`
- **Similarity:** 0.45
- **Dependents:** 0
- **Priority Score:** 21005.5
- **Functions:** 108/108 matched (target 147)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Output`

### 14. set.mutable

- **Target:** `set.Mutable`
- **Similarity:** 0.63
- **Dependents:** 0
- **Priority Score:** 20603.7
- **Functions:** 3/3 matched
- **Missing functions:** _none_
- **Types:** 1/3 matched (target 1)
- **Missing types:** `Value`, `Sealed`

### 15. inner.extract

- **Target:** `inner.Extract`
- **Similarity:** 0.60
- **Dependents:** 0
- **Priority Score:** 10504.0
- **Functions:** 3/4 matched (target 3)
- **Missing functions:** `drop`
- **Types:** 1/1 matched
- **Missing types:** _none_

### 16. lib

- **Target:** `indexmap.Lib`
- **Similarity:** 0.71
- **Dependents:** 0
- **Priority Score:** 2002.9
- **Functions:** 15/15 matched (target 36)
- **Missing functions:** _none_
- **Types:** 5/5 matched (target 11)
- **Missing types:** _none_

### 17. util

- **Target:** `indexmap.Util`
- **Similarity:** 0.68
- **Dependents:** 0
- **Priority Score:** 403.2
- **Functions:** 4/4 matched (target 14)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 7)
- **Missing types:** _none_

### 18. macros

- **Target:** `indexmap.Macros [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 2)
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
| `rayon.mod` | `rayon.Mod` | 0 | `rayon/mod.rs` | `rayon/Mod.kt` |

