
package model;


public class Util {
    
    public static String genarateCode() {
        int r = (int) (Math.random() * 1000000);
        return String.format("%6d", r);
    }
    
    public static boolean isEmailValid(String email) {
        return email.matches("^[a-zA-Z0-9_!#$%&amp;'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    }
    
    public static boolean isPasswordValid(String password) {
        return password.matches("^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$");
    }
    
    public static boolean isMobileValid(String mobile) {
        return mobile.matches("^(0{1})(7{1})([0|1|2|4|5|6|7|8]{1})([0-9]{7})");
    }
    
    public static boolean isCodeValid(String code) {
        return code.matches("^\\d{5}$");
    }

    public static boolean isInteger(String value) {
        return value.matches("^\\d+$");
    }

    public static boolean isDouble(String value) {
        return value.matches("^\\d+(\\.\\d{2})?$");
    }
    
}
