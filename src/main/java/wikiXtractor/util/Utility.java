package wikiXtractor.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class Utility {

    public static int hash(Object... values) {
        return Arrays.hashCode(values);
    }

    public static String getMD5(String string) {
        return string;
        //System.out.println(Objects(14, "Wattenscheid") + " " + Objects.hashCode(14, "Rüttenscheid"));
        /*try {
            MessageDigest m = MessageDigest.getInstance("SHA-384");

            m.update(string.getBytes(), 0, string.length());
            return new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }*/
    }

}
