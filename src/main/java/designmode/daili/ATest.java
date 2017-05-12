package designmode.daili;

import org.junit.Test;

/**
 * Created by yxcui on 2017/5/12.
 */
public class ATest {

    @Test
    public void main(){
        Meinv mm = new Meinv("美女");
        Proxy pp = new Proxy(mm);
        pp.sendHua();
        pp.sendQiaokl();
        pp.sendYifu();
    }
}
