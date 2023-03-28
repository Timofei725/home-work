package transliterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TransliteratorImpl implements Transliterator {
    private static final Map<Character, String> KIRILL_TO_LATIN_MAP = getTranslatingMap();

    @Override
    public String transliterate(String source) {
        StringBuilder translatedString = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            if (Character.isLetter(c) && KIRILL_TO_LATIN_MAP.containsKey(c)) {
                translatedString.append(KIRILL_TO_LATIN_MAP.get(c));
            } else {
                translatedString.append(c);

            }
        }
        return translatedString.toString();
    }

    private static Map<Character, String> getTranslatingMap() {
        Map<Character, String> kirilToLatinMap = new HashMap<>();
        kirilToLatinMap.put('A', "A");
        kirilToLatinMap.put('Б', "B");
        kirilToLatinMap.put('В', "V");
        kirilToLatinMap.put('Г', "G");
        kirilToLatinMap.put('Д', "D");
        kirilToLatinMap.put('Е', "E");
        kirilToLatinMap.put('Ё', "E");
        kirilToLatinMap.put('Ж', "ZH");
        kirilToLatinMap.put('З', "Z");
        kirilToLatinMap.put('И', "I");
        kirilToLatinMap.put('Й', "I");
        kirilToLatinMap.put('К', "K");
        kirilToLatinMap.put('Л', "L");
        kirilToLatinMap.put('М', "M");
        kirilToLatinMap.put('Н', "N");
        kirilToLatinMap.put('П', "P");
        kirilToLatinMap.put('Р', "R");
        kirilToLatinMap.put('С', "S");
        kirilToLatinMap.put('Т', "T");
        kirilToLatinMap.put('У', "U");
        kirilToLatinMap.put('Ф', "F");
        kirilToLatinMap.put('Х', "KH");
        kirilToLatinMap.put('Ц', "TS");
        kirilToLatinMap.put('Ч', "CH");
        kirilToLatinMap.put('Ш', "SH");
        kirilToLatinMap.put('Щ', "SHCH");
        kirilToLatinMap.put('Ы', "Y");
        kirilToLatinMap.put('Ь', "");
        kirilToLatinMap.put('Ъ', "IE");
        kirilToLatinMap.put('Э', "E");
        kirilToLatinMap.put('Ю', "IU");
        kirilToLatinMap.put('Я', "IA");
        return kirilToLatinMap;
    }
}
