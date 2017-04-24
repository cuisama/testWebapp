package jwt;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.junit.Test;

import java.util.HashMap;

/**
 * Created by yxcui on 2017/4/21.
 */
public class JWTTest {

    @Test
    public void sign() throws Exception{
//        for (int i=0;i<10000;i++){
//            String s = JWT.sign(new User("1","11","111"),60L*60L*1000L);
//        }
//        JWT.sign(new HashMap<String,String>(),10L);

        String RSA = JWT.signRSA(new User("1","11","111"),60L*60L*1000L);
        System.out.println(RSA);
        System.out.println(JWT.unSignRSA(RSA,User.class));
/*        System.out.println(s);
        User u= JWT.unsign(s,User.class);
        System.out.println(u);*/
    }

}
