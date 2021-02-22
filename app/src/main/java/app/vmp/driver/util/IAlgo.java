package app.vmp.driver.util;

/**
 * Algorithm
 * 
 */
public interface IAlgo {

    /**
     * Crypt operation
     * 
     */
    public enum ECryptOperation {
        /**
         * encrypt
         */
        ENCRYPT,
        /**
         * decrypt
         */
        DECRYPT
    }

    /**
     * crypt mode option
     * 
     */
    public enum ECryptOption {
        /**
         * CBC mode
         */
        CBC,
        /**
         * ECB mode
         */
        ECB
    }

    /**
     * Crypt Padding Option
     */
    public enum ECryptPaddingOption {
        /**
         * NO PADDING
         */
        NO_PADDING,
        /**
         * PCKS5 PADDING
         */
        PCKS5_PADDING,
    }

    /**
     * xor
     * 
     * @param a
     *            [input] array a
     * @param b
     *            [input] array b
     * @param len
     *            length of the bytes to be XOR'ed
     * @return result, null on error
     */
    public byte[] xor(byte[] a, byte[] b, int len);

    /**
     * generate random bytes
     * 
     * @param r
     *            an array to store the random bytes
     */
    public void randomBytes(byte[] r);

    /**
     * MD5
     * 
     * @param data
     *            [input] the data upon which to perform MD5 operation
     * @return 32 bytes result, null on error
     */
    public String md5(byte[] data);

    /**
     * SHA1
     * 
     * @param data
     *            [input] the data unpon which to perform SHA1 operation
     * @return 40 bytes result, null on error
     */
    public String sha1(byte[] data);

    /**
     * SHA256
     * 
     * @param data
     *            [input] the data unpon which to perform SHA256 operation
     * @return 64 bytes result, null on error
     */
    public String sha256(byte[] data);

    /**
     * DES or TDES
     * 
     * @param cryptOperation
     *            crypt operation
     * @param cryptOption
     *            crypt option
     * @param paddingOption
     *            padding option
     * @param data
     *            the data need to be encrypted/decrypted. If paddingOption is {@link ECryptPaddingOption#NO_PADDING},
     *            its length must be multiple of 8
     * @param key
     *            key. 8 bytes for DES, 16 or 24 bytes for TDES
     * @param iv
     * 
     *            Initial vector, 8 bytes, only for {@link ECryptOption#CBC}. For {@link ECryptOption#ECB}, it's
     *            ignored. It will be treated as all 0s if the iv is null
     * @return result, null on error
     */
    public byte[] des(ECryptOperation cryptOperation, ECryptOption cryptOption, ECryptPaddingOption paddingOption,
                      byte[] data, byte[] key, byte[] iv);

    /**
     * AES
     *
     * @param cryptOperation
     *            crypt operation
     * @param cryptOption
     *            crypt option
     * @param paddingOption
     *            padding option
     * @param data
     *            data to encrypt/decrypt. if paddingOption is {@link ECryptPaddingOption#NO_PADDING}, its length must
     *            be multiple of 16
     * @param key
     *            key. 16, 24 or 32 bytes
     * @param iv
     *            Initial vector, 16 bytes, only for {@link ECryptOption#CBC}. For {@link ECryptOption#ECB}, it's
     *            ignored. It will be treated as all 0s if the iv is null
     * @return result, null on error
     */
    public byte[] aes(ECryptOperation cryptOperation, ECryptOption cryptOption, ECryptPaddingOption paddingOption,
                      byte[] data, byte[] key, byte[] iv);

    /**
     * base64 encoding
     * 
     * @param data
     *            the data to be encoded.
     * @return result, null on error
     */
    public String base64Encode(byte[] data);

    /**
     * base64 decoding
     * 
     * @param base64Encoded
     *            base64 encoded string, to decode
     * @return result, null on error
     */
    public byte[] base64Decode(String base64Encoded);

    /**
     * CRC16-CCITT
     * 
     * @param data
     *            the data to be calculated
     * @return result, null on error
     */
    public Short crc16ccitt(byte[] data);

    /**
     * CRC32
     * 
     * @param data
     *            the data to be calculated
     * @return result, null on error
     */
    public Integer crc32(byte[] data);

    /**
     * Perform addition algorithm on 2 integers in string format. use "-" for negative. e.g: "1234"+"23"="1257",
     * "1234"+"-33"="1201", "-1234"+"-22"="-1256", "99999999999999999999"+"1"="100000000000000000000"
     * 
     * 
     * @param left
     *            left operand
     * @param right
     *            right operand
     * @return result, null on error
     */
    public String integerStrAdd(String left, String right);

    /**
     * set bit to 0 or 1
     * 
     * @param data
     *            data to set bit
     * @param index
     *            the index, from left to right, ranged from 1 to (data.length * 8)
     * @param value
     *            either 0 or 1, if greater than 1, then treat it as 1
     * @return result, true on success, false otherwise
     */
    public boolean setBit(byte[] data, int index, byte value);

    /**
     * get bit
     * 
     * @param data
     *            [input] data to get bit
     * @param index
     *            the index, from left to right, ranged from 1 to (data.length * 8)
     * @return null on error, otherwise 0 or 1
     */
    public Byte getBit(byte[] data, int index);
}
