package cn.kuroneko.demos.commons.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * AES�
 * @author aes对称加密
 *
 */
@Slf4j
public class AESedeSecretUtil {

    private static String DEFALUT_KEY = "ABCDEFGHIJKLMN";
    private final static String UTF_8="utf-8";
    public static final  String IV="0218964300239865";
    /**
     *
     * @param strKey
     * @param
     * @return
     * @throws Exception
     * @date 2017-5-8
     * @author yanghl
     */
    public static String encrypt(String strKey, String strIn) throws Exception {
        SecretKeySpec skeySpec = getKey(strKey);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(IV.getBytes(UTF_8));
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(strIn.getBytes());

        //return new BASE64Encoder().encode(encrypted);
        return parseByte2HexStr(encrypted);
    }

    /**
     *
     * @param strKey
     * @param strIn
     * @return
     * @throws
     * @date 2017-5-8
     * @author yanghl
     */
    public static String decrypt(String strKey, String strIn){
        String originalString="";
        try{
            SecretKeySpec skeySpec = getKey(strKey);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes(UTF_8));
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = parseHexStr2Byte(strIn);

            byte[] original = cipher.doFinal(encrypted1);
            originalString = new String(original,UTF_8);
        }catch(Exception e){
            log.error("AES:   "+strIn);
            log.error("参数解密出错");
            log.error(LogUtils.stackTraceAsString(e));
        }
        return originalString;
    }

    private static SecretKeySpec getKey(String strKey) throws Exception {
        if(strKey == null || "".equals(strKey))
        {
            strKey = DEFALUT_KEY;
        }
        byte[] arrBTmp = strKey.getBytes(UTF_8);
        byte[] arrB = new byte[16]; //

        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        SecretKeySpec skeySpec = new SecretKeySpec(arrB, "AES");
        return skeySpec;
    }

    /**

     * @param hexStr

     * @return

     */

    public static byte[] parseHexStr2Byte(String hexStr) {

        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }

        return result;

    }

		/*

		 * @param buf

		 * @return

		 */

    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        String memberid = encrypt("_encrypt_20190326", "6323241953128420386");
        String xx_memberid = encrypt("card2017szyrgs", "7070031431041359326");
        String xxx_openid = encrypt("card2017szyrgs", "o1yl_jiliWvrRksyrEFgi3up8TzM");
        String e2 = encrypt("encrypt_20190326", "o1yl_jgY0iG6vW9wYHGQiai_Tehs");
        System.out.println("memberid  "+ memberid);
        System.out.println("openid "+e2);
        System.out.println("hrt_memberid "+xx_memberid);
        System.out.println("hrt_openid "+xxx_openid);

    }


}
