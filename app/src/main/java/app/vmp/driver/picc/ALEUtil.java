package app.vmp.driver.picc;

public class ALEUtil {

    /**
     * @param   b       source byte array
     * @param   offset  starting offset
     * @param   len     number of bytes in destination (processes len*2)
     * @return  byte[len]
     */
    public static byte[] hex2byte (byte[] b, int offset, int len) {
        byte[] d = new byte[len];
        for (int i=0; i<len*2; i++) {
            int shift = i%2 == 1 ? 0 : 4;
            d[i>>1] |= Character.digit((char) b[offset+i], 16) << shift;
        }
        return d;
    }


    /**
     * @param s source string (with Hex representation)
     * @return byte array
     */
    public static byte[] hex2byte (String s) {
        if (s.length() % 2 == 0) {
            return hex2byte (s.getBytes(), 0, s.length() >> 1);
        } else {
            throw new RuntimeException("Uneven number("+s.length()+") of hex digits passed to hex2byte.");
        }
    }
    /**
     * converts a byte array to hex string
     * (suitable for dumps and ASCII packaging of Binary fields
     * @param b - byte array
     * @return String representation
     */
    public static String hexString(byte[] b) {
        StringBuffer d = new StringBuffer(b.length * 2);
        for (int i=0; i<b.length; i++) {
            char hi = Character.forDigit ((b[i] >> 4) & 0x0F, 16);
            char lo = Character.forDigit (b[i] & 0x0F, 16);
            d.append(Character.toUpperCase(hi));
            d.append(Character.toUpperCase(lo));
        }
        return d.toString();
    }
    /**
     * Concatenates two byte arrays (array1 and array2)
     * @param array1
     * @param array2
     * @return the concatenated array
     */
    public static byte[] concat (byte[] array1, byte[] array2) {
        byte[] concatArray = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, concatArray, 0, array1.length);
        System.arraycopy(array2, 0, concatArray, array1.length, array2.length);
        return  concatArray;
    }

    /**
     * Concatenates two byte arrays (array1 and array2)
     * @param array1
     * @param beginIndex1
     * @param length1
     * @param array2
     * @param beginIndex2
     * @param length2
     * @return the concatenated array
     */
    public static byte[] concat (byte[] array1, int beginIndex1, int length1, byte[] array2,
                                 int beginIndex2, int length2) {
        byte[] concatArray = new byte[length1 + length2];
        System.arraycopy(array1, beginIndex1, concatArray, 0, length1);
        System.arraycopy(array2, beginIndex2, concatArray, length1, length2);
        return  concatArray;
    }

    public static String asciiToHex(String asciiValue)
    {
        char[] chars = asciiValue.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++)
        {
            hex.append(Integer.toHexString((int) chars[i]));
        }
        return hex.toString();
    }

    public static String hexToASCII(String hexValue)
    {
        StringBuilder output = new StringBuilder("");
        for (int i = 0; i < hexValue.length(); i += 2)
        {
            String str = hexValue.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    public static String padLeft(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('-');
        }
        sb.append(inputString);

        return sb.toString();
    }
}
