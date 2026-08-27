=== Deep Analysis: tmp/indexmap/src (rust) -> src/commonMain/kotlin/io/github/kotlinmania/indexmap (kotlin) ===

Scanning source codebase (rust)...
Codebase: tmp/indexmap/src (rust)
  Files: 26
  Total imports: 169
  Most depended: map.slice (5 dependents)

Scanning target codebase (kotlin)...
Codebase: src/commonMain/kotlin/io/github/kotlinmania/indexmap (kotlin)
  Files: 35
  Total imports: 173
  Most depended: map.Entry (2 dependents)

Comparing codebases...
Computing AST similarities...

=== Codebase Comparison Report ===

Source: tmp/indexmap/src (26 files)
Target: src/commonMain/kotlin/io/github/kotlinmania/indexmap (35 files)
Scoring invariant: FnSim is required function body/parameter similarity. Class/type and symbol parity are reported beside it; whole-file shape is diagnostic only.

Matched:   19 files
Unmatched: 7 source, 5 target

=== Matched Files (by porting priority) ===

Source                        Target                        FnSim     Dependents FunctionParityTypeParity  Priority
 --------------------------------------------------------------------------------------------------------------------------
map.slice                     map.Slice                     0.32      5          57/64         1/4         5106807.0 
rayon.map                     map.Typealiases [ZERO] [PROVENANCE-FALLBACK]0.00      0          0/25          0/9         343410.0  
map.raw_entry_v1              map.RawEntryV1                0.44      0          26/35         6/7         104205.6  
serde                         indexmap.Serde                0.13      0          3/7           0/4         81108.7   
map.mutable                   map.Mutable                   0.09      0          1/5           2/5         71009.1   
map.iter                      map.Iter                      0.32      0          11/14         12/14       52806.8   
set.iter                      set.Iter                      0.25      0          9/12          9/11        52307.5   
set.slice                     set.Slice                     0.31      0          34/35         1/4         43906.9   
map.serde_seq                 map.SerdeSeq                  0.09      0          2/4           0/2         40609.1   
inner                         inner.Inner                   0.69      0          52/54         2/3         35703.1   
set                           indexmap.Set                  0.59      0          97/97         1/3         30004.1   
inner.entry                   inner.Entry                   0.64      0          29/31         2/2         23303.6   
map.entry                     map.Entry                     0.51      0          25/27         2/2         22904.9   
map                           indexmap.Map                  0.45      0          108/108       1/2         21005.5   
set.mutable                   set.Mutable                   0.63      0          3/3           1/3         20603.7   
inner.extract                 inner.Extract                 0.60      0          3/4           1/1         10504.0   
lib                           indexmap.Lib                  0.71      0          15/15         5/5         2002.9    
util                          indexmap.Util                 0.68      0          4/4           0/0         403.2     
macros                        indexmap.Macros [ZERO]        0.00      0          0/0           0/0         10.0      

=== Function and Symbol Details ===

map.slice -> map.Slice
  similarity: 0.32, priority: 5106807.0, dependents: 5
  functions: 57/64 matched (target total: 95, required body score: 0.32)
  missing functions: check, slice_index_mut, check_mut, slice_new_mut, slice_get_index_mut, slice_split_first_mut, slice_split_last_mut
  types: 1/4 matched (target total: 3)
  missing types: IntoIter, Item, Output
  tests: 5/12 matched

rayon.map -> map.Typealiases [ZERO] [PROVENANCE-FALLBACK]
  similarity: 0.00, priority: 343410.0, dependents: 0
  provenance warning: port-lint provenance header matched only by basename: `map.rs` vs expected `rayon/map.rs`
  functions: 0/25 matched (target total: 0, required body score: 0.00)
  missing functions: into_par_iter, fmt, clone, par_drain, par_keys, par_values, par_eq, par_values_mut, par_sort_keys, par_sort_by, par_sorted_by, par_sort_by_key, par_sort_unstable_keys, par_sort_unstable_by, par_sorted_unstable_by, par_sort_unstable_by_key, par_sort_by_cached_key, from_par_iter, par_extend, insert_order, partial_eq_and_eq, extend, keys, values, values_mut
  types: 0/9 matched (target total: 1)
  missing types: Item, Iter, IntoParIter, ParIter, ParIterMut, ParDrain, ParKeys, ParValues, ParValuesMut
  *** CHEAT DETECTION / SCORING FAILURE ***
  function-by-function score forced to 0: no target functions found; report scoring is function-by-function only
  tests: 0/6 matched

map.raw_entry_v1 -> map.RawEntryV1
  similarity: 0.44, priority: 104205.6, dependents: 0
  functions: 26/35 matched (target total: 50, required body score: 0.44)
  missing functions: raw_entry_v1, raw_entry_mut_v1, into_key, into_mut, get_key_value, get_key_value_mut, into_key_value_mut, insert_key, assert_send_sync
  types: 6/7 matched (target total: 9)
  missing types: Sealed
  tests: 0/1 matched

serde -> indexmap.Serde
  similarity: 0.13, priority: 81108.7, dependents: 0
  functions: 3/7 matched (target total: 8, required body score: 0.13)
  missing functions: expecting, visit_map, into_deserializer, visit_seq
  types: 0/4 matched (target total: 3)
  missing types: IndexMapVisitor, Value, Deserializer, IndexSetVisitor

map.mutable -> map.Mutable
  similarity: 0.09, priority: 71009.1, dependents: 0
  functions: 1/5 matched (target total: 1, required body score: 0.09)
  missing functions: get_full_mut2, get_index_mut2, iter_mut2, retain2
  types: 2/5 matched (target total: 2)
  missing types: Key, Value, Sealed

map.iter -> map.Iter
  similarity: 0.32, priority: 52806.8, dependents: 0
  functions: 11/14 matched (target total: 136, required body score: 0.32)
  missing functions: into_iter, index, drop
  types: 12/14 matched (target total: 13)
  missing types: Item, Output

set.iter -> set.Iter
  similarity: 0.25, priority: 52307.5, dependents: 0
  functions: 9/12 matched (target total: 71, required body score: 0.25)
  missing functions: into_iter, fold, rfold
  types: 9/11 matched (target total: 10)
  missing types: Item, UnitValue

set.slice -> set.Slice
  similarity: 0.31, priority: 43906.9, dependents: 0
  functions: 34/35 matched (target total: 60, required body score: 0.31)
  missing functions: check
  types: 1/4 matched (target total: 2)
  missing types: IntoIter, Item, Output
  tests: 1/2 matched

map.serde_seq -> map.SerdeSeq
  similarity: 0.09, priority: 40609.1, dependents: 0
  functions: 2/4 matched (target total: 2, required body score: 0.09)
  missing functions: expecting, visit_seq
  types: 0/2 matched (target total: 1)
  missing types: SeqVisitor, Value

inner -> inner.Inner
  similarity: 0.69, priority: 35703.1, dependents: 0
  functions: 52/54 matched (target total: 67, required body score: 0.69)
  missing functions: par_drain, assert_send_sync
  types: 2/3 matched (target total: 3)
  missing types: Entries
  tests: 0/1 matched

set -> indexmap.Set
  similarity: 0.59, priority: 30004.1, dependents: 0
  functions: 97/97 matched (target total: 148, required body score: 0.59)
  missing functions: none
  types: 1/3 matched (target total: 2)
  missing types: Bucket, Output

inner.entry -> inner.Entry
  similarity: 0.64, priority: 23303.6, dependents: 0
  functions: 29/31 matched (target total: 32, required body score: 0.64)
  missing functions: new, from
  types: 2/2 matched (target total: 2)
  missing types: none

map.entry -> map.Entry
  similarity: 0.51, priority: 22904.9, dependents: 0
  functions: 25/27 matched (target total: 63, required body score: 0.51)
  missing functions: key_mut, assert_send_sync
  types: 2/2 matched (target total: 6)
  missing types: none
  tests: 0/1 matched

map -> indexmap.Map
  similarity: 0.45, priority: 21005.5, dependents: 0
  functions: 108/108 matched (target total: 147, required body score: 0.45)
  missing functions: none
  types: 1/2 matched (target total: 2)
  missing types: Output

set.mutable -> set.Mutable
  similarity: 0.63, priority: 20603.7, dependents: 0
  functions: 3/3 matched (target total: 3, required body score: 0.63)
  missing functions: none
  types: 1/3 matched (target total: 1)
  missing types: Value, Sealed

inner.extract -> inner.Extract
  similarity: 0.60, priority: 10504.0, dependents: 0
  functions: 3/4 matched (target total: 3, required body score: 0.60)
  missing functions: drop
  types: 1/1 matched (target total: 1)
  missing types: none

lib -> indexmap.Lib
  similarity: 0.71, priority: 2002.9, dependents: 0
  functions: 15/15 matched (target total: 36, required body score: 0.71)
  missing functions: none
  types: 5/5 matched (target total: 11)
  missing types: none

util -> indexmap.Util
  similarity: 0.68, priority: 403.2, dependents: 0
  functions: 4/4 matched (target total: 14, required body score: 0.68)
  missing functions: none
  types: 0/0 matched (target total: 7)
  missing types: none

macros -> indexmap.Macros [ZERO]
  similarity: 0.00, priority: 10.0, dependents: 0
  functions: 0/0 matched (target total: 2, required body score: 0.00)
  missing functions: none
  types: 0/0 matched (target total: 0)
  missing types: none
  *** CHEAT DETECTION / SCORING FAILURE ***
  function-by-function score forced to 0: no source functions found; target defines functions; report scoring is function-by-function only


=== Scores Forced To 0 ===

  - rayon.map -> map.Typealiases: no target functions found; report scoring is function-by-function only
  - macros -> indexmap.Macros: no source functions found; target defines functions; report scoring is function-by-function only

=== Provenance Header Fallbacks ===

These files were paired only after normalization; fix the port-lint source header.
  - rayon.map -> map.Typealiases: port-lint provenance header matched only by basename: `map.rs` vs expected `rayon/map.rs`
    proposed: // port-lint: source rayon/map.rs

=== Missing from Target (need to port) ===

File                          Deps    Path
------------------------------------------------------------------------------
arbitrary                     0       arbitrary.rs
borsh                         0       borsh.rs
map.tests                     0       map/tests.rs
rayon.mod                     0       rayon/mod.rs
rayon.set                     0       rayon/set.rs
set.tests                     0       set/tests.rs
sval                          0       sval.rs

=== Porting Quality Summary ===

Matched by exact header:          18 / 19
Matched by provenance fallback:   1 / 19
Matched by name:                  0 / 19
Total TODOs in target: 0
Total lint errors:    8
Stub files:           0

=== Big Picture ===

- Missing files: 7
- Incomplete ports (similarity < 60%): 14
- Stub files: 0
- Files missing functions: 13 (total deficit: 65 functions)
- Type definitions missing: 35
- Files missing tests: 6 (total deficit: 17 unported `#[test]` functions)
- Documentation coverage: 426 / 5766 lines (7%)

Primary focus: create missing files (highest deps first)

=== Files with Issues ===

File                          Similarity LineRatio  FunctionParityTests     TODOs Lint  Status
----------------------------------------------------------------------------------------------------
map.Slice                     0.32       0.00       57/64         5/12      0     0     LOW_SIM
  missing functions: `check`, `slice_index_mut`, `check_mut`, `slice_new_mut`, `slice_get_index_mut`, `slice_split_first_mut`, `slice_split_last_mut`
  missing types: `IntoIter`, `Item`, `Output`
map.Typealiases [ZERO] [PROV  0.00       0.00       0/25          0/6       0     1     LOW_SIM
  missing functions: `into_par_iter`, `fmt`, `clone`, `par_drain`, `par_keys`, `par_values`, `par_eq`, `par_values_mut`, `par_sort_keys`, `par_sort_by`, `par_sorted_by`, `par_sort_by_key`, `par_sort_unstable_keys`, `par_sort_unstable_by`, `par_sorted_unstable_by`, `par_sort_unstable_by_key`, `par_sort_by_cached_key`, `from_par_iter`, `par_extend`, `insert_order`, `partial_eq_and_eq`, `extend`, `keys`, `values`, `values_mut`
  missing types: `Item`, `Iter`, `IntoParIter`, `ParIter`, `ParIterMut`, `ParDrain`, `ParKeys`, `ParValues`, `ParValuesMut`
map.RawEntryV1                0.44       0.00       26/35         0/1       0     0     MISSING_FUNCS
  missing functions: `raw_entry_v1`, `raw_entry_mut_v1`, `into_key`, `into_mut`, `get_key_value`, `get_key_value_mut`, `into_key_value_mut`, `insert_key`, `assert_send_sync`
  missing types: `Sealed`
indexmap.Serde                0.13       0.00       3/7           -         0     0     LOW_SIM
  missing functions: `expecting`, `visit_map`, `into_deserializer`, `visit_seq`
  missing types: `IndexMapVisitor`, `Value`, `Deserializer`, `IndexSetVisitor`
map.Mutable                   0.09       0.00       1/5           -         0     0     LOW_SIM
  missing functions: `get_full_mut2`, `get_index_mut2`, `iter_mut2`, `retain2`
  missing types: `Key`, `Value`, `Sealed`
map.Iter                      0.32       0.00       11/14         -         0     0     LOW_SIM
  missing functions: `into_iter`, `index`, `drop`
  missing types: `Item`, `Output`
set.Iter                      0.25       0.00       9/12          -         0     0     LOW_SIM
  missing functions: `into_iter`, `fold`, `rfold`
  missing types: `Item`, `UnitValue`
set.Slice                     0.31       0.00       34/35         1/2       0     0     LOW_SIM
  missing functions: `check`
  missing types: `IntoIter`, `Item`, `Output`
map.SerdeSeq                  0.09       0.00       2/4           -         0     0     LOW_SIM
  missing functions: `expecting`, `visit_seq`
  missing types: `SeqVisitor`, `Value`
inner.Inner                   0.69       0.00       52/54         0/1       0     7     MISSING_FUNCS
  missing functions: `par_drain`, `assert_send_sync`
  missing types: `Entries`
indexmap.Set                  0.59       0.00       97/97         -         0     0     MISSING_TYPES
  missing types: `Bucket`, `Output`
inner.Entry                   0.64       0.00       29/31         -         0     0     MISSING_FUNCS
  missing functions: `new`, `from`
map.Entry                     0.51       0.00       25/27         0/1       0     0     MISSING_FUNCS
  missing functions: `key_mut`, `assert_send_sync`
indexmap.Map                  0.45       0.00       108/108       -         0     0     MISSING_TYPES
  missing types: `Output`
set.Mutable                   0.63       0.00       3/3           -         0     0     MISSING_TYPES
  missing types: `Value`, `Sealed`
inner.Extract                 0.60       0.00       3/4           -         0     0     MISSING_FUNCS
  missing functions: `drop`
indexmap.Macros [ZERO]        0.00       0.00       -             -         0     0     LOW_SIM

=== Porting Recommendations ===

Incomplete ports (similarity < 60%): 14
Missing files: 7

Incomplete ports to complete:
  map.slice                      similarity=0.32 function_parity=57/64 dependents=5
    missing functions: `check`, `slice_index_mut`, `check_mut`, `slice_new_mut`, `slice_get_index_mut`, `slice_split_first_mut`, `slice_split_last_mut`
    missing types: `IntoIter`, `Item`, `Output`
  rayon.map                      similarity=0.00 function_parity=0/25 dependents=0
    missing functions: `into_par_iter`, `fmt`, `clone`, `par_drain`, `par_keys`, `par_values`, `par_eq`, `par_values_mut`, `par_sort_keys`, `par_sort_by`, `par_sorted_by`, `par_sort_by_key`, `par_sort_unstable_keys`, `par_sort_unstable_by`, `par_sorted_unstable_by`, `par_sort_unstable_by_key`, `par_sort_by_cached_key`, `from_par_iter`, `par_extend`, `insert_order`, `partial_eq_and_eq`, `extend`, `keys`, `values`, `values_mut`
    missing types: `Item`, `Iter`, `IntoParIter`, `ParIter`, `ParIterMut`, `ParDrain`, `ParKeys`, `ParValues`, `ParValuesMut`
  map.raw_entry_v1               similarity=0.44 function_parity=26/35 dependents=0
    missing functions: `raw_entry_v1`, `raw_entry_mut_v1`, `into_key`, `into_mut`, `get_key_value`, `get_key_value_mut`, `into_key_value_mut`, `insert_key`, `assert_send_sync`
    missing types: `Sealed`
  serde                          similarity=0.13 function_parity=3/7 dependents=0
    missing functions: `expecting`, `visit_map`, `into_deserializer`, `visit_seq`
    missing types: `IndexMapVisitor`, `Value`, `Deserializer`, `IndexSetVisitor`
  map.mutable                    similarity=0.09 function_parity=1/5 dependents=0
    missing functions: `get_full_mut2`, `get_index_mut2`, `iter_mut2`, `retain2`
    missing types: `Key`, `Value`, `Sealed`
  map.iter                       similarity=0.32 function_parity=11/14 dependents=0
    missing functions: `into_iter`, `index`, `drop`
    missing types: `Item`, `Output`
  set.iter                       similarity=0.25 function_parity=9/12 dependents=0
    missing functions: `into_iter`, `fold`, `rfold`
    missing types: `Item`, `UnitValue`
  set.slice                      similarity=0.31 function_parity=34/35 dependents=0
    missing functions: `check`
    missing types: `IntoIter`, `Item`, `Output`
  map.serde_seq                  similarity=0.09 function_parity=2/4 dependents=0
    missing functions: `expecting`, `visit_seq`
    missing types: `SeqVisitor`, `Value`
  set                            similarity=0.59 function_parity=97/97 dependents=0
    missing types: `Bucket`, `Output`
  map.entry                      similarity=0.51 function_parity=25/27 dependents=0
    missing functions: `key_mut`, `assert_send_sync`
  map                            similarity=0.45 function_parity=108/108 dependents=0
    missing types: `Output`
  inner.extract                  similarity=0.60 function_parity=3/4 dependents=0
    missing functions: `drop`
  macros                         similarity=0.00 function_parity=- dependents=0

=== Missing Files (by Dependents) ===

Source File                   Expected Target                       Dependents Path
-----------------------------------------------------------------------------------------------------------------------
arbitrary                     Arbitrary                             0          arbitrary.rs
borsh                         Borsh                                 0          borsh.rs
map.tests                     map.Tests                             0          map/tests.rs
rayon.set                     rayon.Set                             0          rayon/set.rs
set.tests                     set.Tests                             0          set/tests.rs
sval                          Sval                                  0          sval.rs

=== Reexport / Wiring Modules (consult, don't transliterate) ===

rayon.mod                     rayon.Mod                             0          rayon/mod.rs

=== Documentation Gaps ===

There is missing documentation that is hurting overall scoring.
Documentation coverage: 426 / 5766 lines (7%)
Files with >20% doc gap: 17

File                          Src Docs    Tgt Docs    Gap %     DocSim    DocAmt    DocEq     
----------------------------------------------------------------------------------------------
map                           1780        0           100%      0.00      0.00      0.00      
set                           1398        0           100%      0.00      0.00      0.00      
map.raw_entry_v1              564         0           100%      0.00      0.00      0.00      
inner.entry                   256         6           97%       0.32      0.02      0.17      
lib                           212         0           100%      0.00      0.00      0.00      
rayon.map                     192         0           100%      0.00      0.00      0.00      
map.iter                      238         51          78%       0.68      0.21      0.45      
map.entry                     178         0           100%      0.00      0.00      0.00      
macros                        156         0           100%      0.00      0.00      0.00      
inner                         116         3           97%       0.16      0.03      0.09      
map.serde_seq                 106         3           97%       0.56      0.03      0.29      
map.mutable                   122         47          61%       0.85      0.39      0.62      
set.iter                      78          36          53%       0.61      0.46      0.53      
map.slice                     176         135         23%       0.94      0.77      0.85      
set.slice                     118         89          24%       0.85      0.75      0.80      
set.mutable                   62          42          32%       0.93      0.68      0.80      
serde                         14          11          21%       0.31      0.79      0.55      

=== Generating Reports ===

✅ Generated: port_status_report.md
✅ Generated: high_priority_ports.md
✅ Generated: NEXT_ACTIONS.md
✅ Generated: port_lint_proposed_changes.md

📁 All reports generated successfully!
