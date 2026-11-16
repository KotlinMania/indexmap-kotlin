use indexmap::{IndexMap, IndexSet};
use serde::Deserialize;
use serde_test::{assert_de_tokens, assert_de_tokens_error, Token};
#[derive(Debug, Deserialize)]
struct Sequenced(#[serde(deserialize_with="indexmap::map::serde_seq::deserialize")] IndexMap<Option<String>, Option<i32>>);
impl PartialEq for Sequenced {
    fn eq(&self, other: &Self) -> bool { self.0.iter().eq(other.0.iter()) }
}
fn main() {
    let set: IndexSet<Option<String>> = [None, Some("later".into())].into_iter().collect();
    assert_de_tokens(&set, &[Token::Seq{len:Some(3)}, Token::None, Token::Some, Token::Str("later"), Token::None, Token::SeqEnd]);
    println!("nullable-set:pass");
    let map: IndexMap<Option<String>,Option<i32>> = [(None,Some(9)),(Some("later".into()),None)].into_iter().collect();
    assert_de_tokens(&Sequenced(map), &[
        Token::NewtypeStruct{name:"Sequenced"}, Token::Seq{len:Some(3)},
        Token::Tuple{len:2},Token::None,Token::Some,Token::I32(7),Token::TupleEnd,
        Token::Tuple{len:2},Token::Some,Token::Str("later"),Token::None,Token::TupleEnd,
        Token::Tuple{len:2},Token::None,Token::Some,Token::I32(9),Token::TupleEnd,Token::SeqEnd]);
    println!("nullable-map-duplicate:pass");
    assert_de_tokens(&IndexSet::<i32>::new(), &[Token::Seq{len:Some(0)},Token::SeqEnd]);
    println!("empty-set:pass");
    assert_de_tokens(&Sequenced(IndexMap::new()), &[Token::NewtypeStruct{name:"Sequenced"},Token::Seq{len:Some(0)},Token::SeqEnd]);
    println!("empty-map:pass");
    assert_de_tokens_error::<IndexSet<i32>>(&[Token::Seq{len:Some(2)},Token::I32(1),Token::Str("bad")], "invalid type: string \"bad\", expected i32");
    println!("decode-error:pass");
}
