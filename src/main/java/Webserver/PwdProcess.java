package Webserver;

import com.lambdaworks.crypto.SCrypt.*;
import com.lambdaworks.crypto.SCryptUtil;

/**
 *
 * @author Jonse
 */
public class PwdProcess {
    public static String hash(String pwd){
        String s = SCryptUtil.scrypt(pwd, 32, 4, 2);
        return s;
    }
}
