package cn.kuroneko.demos.utils;



import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.util.Base64;

public class DesUtils {

	private static String DEFAULT_CHARSET = "utf-8";
	/**
	 * 加密方法/运算模式/填充模式
	 */
	private final static String Algorithm = "DESede/ECB/PKCS5Padding";

	/**
	 * 密钥算法
	 */
	private static final String KEY_ALGORITHM = "DESede";

	/**
	 * 加密
	 * 
	 * @param src
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String src, String key) throws Exception {
		byte[] data = src.getBytes(DEFAULT_CHARSET);
		String result = new String(Base64.getEncoder().encode(encrypt(data, key)), DEFAULT_CHARSET);
		return replace(result, true);
	}

	/**
	 * 加密
	 * @param data
	 * @param key
	 * @return
	 */
	private static byte[] encrypt(byte[] data, String key) throws Exception {
		// 初始化向量
		DESedeKeySpec desKey = new DESedeKeySpec(key.getBytes(DEFAULT_CHARSET));
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成securekey
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(Algorithm);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey);
		// 现在，获取数据并加密
		// 正式执行加密操作
		return cipher.doFinal(data);
	}

	/**
	 * 解密
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String data, String key) throws Exception {
		try {
			data = replace(data, false);
			return new String(decrypt(Base64.getDecoder().decode(data), key), DEFAULT_CHARSET);
		} catch (Exception e) {
			throw new RuntimeException("签名错误,请检查参数");
		}
	}

	/**
	 * 解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static byte[] decrypt(byte[] data, String key) throws Exception {
		// 创建一个DESKeySpec对象
		DESedeKeySpec desKey = new DESedeKeySpec(key.getBytes());
		// 创建一个密匙工厂
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
		// 将DESKeySpec对象转换成SecretKey对象
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(Algorithm);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey);
		// 真正开始解密操作
		return cipher.doFinal(data);
	}

	/**
	 * 字符替换,替换链接中不允许字符;
	 *
	 * @param str 密文
	 * @param flag 标识正向替换或者反向替换
	 * @return 解密后密码
	 */
	private static String replace(String str, boolean flag) {
		if (flag) {
			return str.replaceAll("\\+", "*").replaceAll("\\/", ":").replaceAll("\\=", "_");
		} else {
			return str.replaceAll("\\*", "+").replaceAll("\\:", "/").replaceAll("\\_", "=");
		}
	}

	public static void main(String[] args) throws Exception {
		/**
		 * des加密的key长度需要大于24位
		 */
		System.out.println("kuroneko_des_en_de_crypt_key_lent_should_longger_than_24".length());
		String encrypt = encrypt("xxxxxx", "kuroneko_des_en_de_crypt_key_lent_should_longger_than_24");
		System.out.println(encrypt);

	}

}
