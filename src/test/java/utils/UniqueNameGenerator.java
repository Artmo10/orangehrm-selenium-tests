package utils;

import java.time.Instant;

public class UniqueNameGenerator {

    public static String appendUniqueSuffix(String baseValue){
        long timestamp = Instant.now().getEpochSecond();
        return baseValue + "_" + timestamp;
    }
}