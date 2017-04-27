package designmode.zhuangshi;

import org.junit.Test;

/**
 * Created by yxcui on 2017/4/27.
 */
public class ATest {

    @Test
    public void main(){
        Person p = new ren("pp");
        yifu yifu = new yifu(p);
        yifu txu = new txu(p);
        yifu kucha = new kucha(p);
        p.show();
    }
}
