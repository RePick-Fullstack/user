package TheNaeunEconomy.util;

import java.util.List;
import java.util.Random;

public class NicknameGenerator {

    private static final List<String> ADJECTIVES = List.of(
            "빠른", "똑똑한", "예쁜", "조용한", "큰", "작은", "친절한", "용감한", "이상한", "귀여운"
    );

    private static final List<String> FISH = List.of(
            "고등어", "참치", "광어", "연어", "농어", "가자미", "상어", "황새치", "메기", "복어"
    );

    private static final Random RANDOM = new Random();

    public static String generate() {
        String adjective = ADJECTIVES.get(RANDOM.nextInt(ADJECTIVES.size()));
        String fish = FISH.get(RANDOM.nextInt(FISH.size()));
        return adjective + " " + fish;
    }
}
