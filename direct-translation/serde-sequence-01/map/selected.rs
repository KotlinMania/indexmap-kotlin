while let Some((key, value)) = seq.next_element()? {
            map.insert(key, value);
        }
