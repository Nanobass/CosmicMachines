package net.paxyinc.util;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockNameUtil {

    public static String getNiceName(String words) {
        return Stream.of(words.replace('_', ' ').trim().split("\\s"))
                .filter(word -> word.length() > 0)
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }

}
