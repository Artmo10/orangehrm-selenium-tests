package utils;

public class UniqueIdGenerator {

    private static final int MAX_EMPLOYEE_ID_LENGTH = 10;

    public static String generateEmployeeId(){
        String millis = String.valueOf(System.currentTimeMillis());
        return millis.substring(millis.length() - MAX_EMPLOYEE_ID_LENGTH);
    }
}
