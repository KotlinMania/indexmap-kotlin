# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 4/26 (15.4%)
- **Function parity:** 145/883 matched (target 204) — 16.4%
- **Class/type parity:** 7/95 matched (target 23) — 7.4%
- **Combined symbol parity:** 152/978 matched (target 227) — 15.5%
- **Average inline-code cosine:** 0.46 (function body across 4 matched files)
- **Average documentation cosine:** 0.00 (doc text across 4 matched files)
- **Cheat-zeroed Files:** 0
- **Critical Issues:** 3 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. map.slice

- **Target:** `map.Slice`
- **Similarity:** 0.21
- **Dependents:** 5
- **Priority Score:** 5296808.0
- **Functions:** 38/64 matched (target 57)
- **Missing functions:** `from_slice`, `from_mut_slice`, `from_boxed`, `into_boxed`, `new_mut`, `get_index_mut`, `get_range_mut`, `first_mut`, `last_mut`, `split_at_mut`, `split_at_mut_checked`, `split_first_mut`, `split_last_mut`, `iter_mut`, `values_mut`, `get_disjoint_mut`, `get_disjoint_opt_mut`, `into_iter`, `index_mut`, `check`, `slice_index_mut`, `check_mut`, `slice_new_mut`, `slice_get_index_mut`, `slice_split_first_mut`, `slice_split_last_mut`
- **Types:** 1/4 matched (target 3)
- **Missing types:** `IntoIter`, `Item`, `Output`
- **Tests:** 5/12 matched

### 2. map

- **Target:** `indexmap.Map`
- **Similarity:** 0.37
- **Dependents:** 0
- **Priority Score:** 191006.3
- **Functions:** 91/108 matched (target 101)
- **Missing functions:** `as_entries_mut`, `with_entries`, `iter_mut`, `values_mut`, `entry`, `get_mut`, `get_key_value_mut`, `get_full_mut`, `get_disjoint_mut`, `as_mut_slice`, `into_boxed_slice`, `get_index_mut`, `get_disjoint_indices_mut`, `get_range_mut`, `first_mut`, `last_mut`, `index_mut`
- **Types:** 1/2 matched
- **Missing types:** `Output`

### 3. lib

- **Target:** `indexmap.Lib`
- **Similarity:** 0.57
- **Dependents:** 0
- **Priority Score:** 32004.3
- **Functions:** 12/15 matched (target 32)
- **Missing functions:** `key`, `value`, `fmt`
- **Types:** 5/5 matched (target 11)
- **Missing types:** _none_

### 4. util

- **Target:** `indexmap.Util`
- **Similarity:** 0.68
- **Dependents:** 0
- **Priority Score:** 403.2
- **Functions:** 4/4 matched (target 14)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 7)
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
