package designmode.shipeiqi;

/**
 * Created by yxcui on 2017/4/28.
 */
public class Waijizhongfeng2 extends WaijiPlayer {

    public Waijizhongfeng2(String name) {
        super(name);
    }

    public void attach() {
        System.out.println("中锋"+super.getName()+" 攻击");
    }

    public void defend() {
        System.out.println("中锋"+super.getName()+" 防守");
    }
}
