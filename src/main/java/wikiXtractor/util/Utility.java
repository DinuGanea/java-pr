package wikiXtractor.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

/**
 * Mainly used for small routines needed as helpers in project classes
 *
 * @author Ionut Urs
 */
public class Utility {

    /**
     * Generates a hash code for a sequence of input values. The hash
     * code is generated as if all the input values were placed into an
     * array.
     *
     * @param values Seed values
     * @return Hash code
     */
    public static int hash(Object... values) {
        return Arrays.hashCode(values);
    }


    /**
     * Custom hash function.
     *
     * Generate a hash from given values
     *
     * @param values Values seed
     * @return Hash String
     */
    public static String customHash(Object... values) {
        /**
         * A temporary pseudo-hash function made very simple to avoid collisions
         * TODO replace hash function with a real one that covers all collisions
         *
         * Collision example input:
         *
         * Input #1: 14 Wattenscheid
         * Input #2; 14 Rüttenscheid
         *
         */
        StringBuilder hash = new StringBuilder();

        for(Object o : values) {
            hash.append(o.toString().replaceAll("\\s+",""));
        }

        return hash.toString();
    }

}
