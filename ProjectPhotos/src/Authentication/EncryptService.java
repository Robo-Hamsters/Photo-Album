package Authentication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptService {

 public static String encryptPassword(String password){
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(password.getBytes());
            byte[] encPwd = md5.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte bytes : encPwd) {
                stringBuffer.append(String.format("%02x", bytes & 0xff));
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return password;

    }
}
