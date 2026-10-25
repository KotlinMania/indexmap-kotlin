# Generated Serde sequence-loop repair

Serde 0.1.12 returns `SeqElement.Some(value)` or `SeqElement.None`. A nullable
payload is a real item; it must not terminate the sequence. The former caller
code used `?: break` and now fails compilation after this source-faithful API
correction.

The generator reads the complete original Rust files and selects their existing
Syn `while let` nodes. The two `generated.kt` fragments are integrated in
`Serde.kt` and `map/SerdeSeq.kt` with indentation only. `input.rs`, `selected.rs`,
`receipt.txt`, `adoption.json` and `repair.diff` retain source correspondence and
ordinary diff evidence. No whole visitor/file translation is claimed; surrounding
hand-translated capacity, allocation and hash behavior is outside this repair.

Reproduce from `rust-kotlin` with its tested executable:

```sh
build/bin/nativeMacos/debugExecutable/rust-kotlin.kexe serde-sequence-loop ../indexmap-kotlin/tmp/indexmap/src/serde.rs NEW_SET_DIRECTORY --line=133 --seed=seed --bind=seq:access --bind=values:values
build/bin/nativeMacos/debugExecutable/rust-kotlin.kexe serde-sequence-loop ../indexmap-kotlin/tmp/indexmap/src/map/serde_seq.rs NEW_MAP_DIRECTORY --line=108 --seed=pairSeed --bind=seq:access --bind=map:map
```

The `rust/` oracle uses the actual local IndexMap 2.13.0 source and cached Serde
1.0.228 / serde_test 1.0.176. It checks nullable set items followed by more values,
nullable map keys/values, duplicate-key insertion order, empty sequences and exact
decoding errors. Matching Kotlin assertions use the production visitors, not a
replacement implementation. The order check explicitly compares iteration order;
ordinary map equality alone would not establish it.

`validation.json` records completed checks and their limitations. All project
targets remain enabled. Compilation/publication does not imply runtime testing on
every target. User configuration and staged directory cleanup remain separate.
