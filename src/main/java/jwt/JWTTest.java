package jwt;

import org.junit.Test;

/**
 * Created by yxcui on 2017/4/21.
 */
public class JWTTest {

    @Test
    public void sign(){
        String s = JWT.sign(new User("1","11","111"),60L*60L*1000L);
        System.out.println(s);
        User u= JWT.unsign(s,User.class);
        System.out.println(u);
    }

}
