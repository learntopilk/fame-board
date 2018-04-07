package Webserver;

import com.lambdaworks.crypto.SCrypt.*;
import com.lambdaworks.crypto.SCryptUtil;

/**
 *
 * @author Jonse
 */
public class PwdProcess {
    public static String hash(String pwd){
        String s = SCryptUtil.scrypt(pwd, 8000, 4, 2);
        return s;
    }
}
