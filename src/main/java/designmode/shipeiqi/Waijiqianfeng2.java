package designmode.shipeiqi;

/**
 * Created by yxcui on 2017/4/28.
 */
public class Waijiqianfeng2 extends WaijiPlayer {

    public Waijiqianfeng2(String name) {
        super(name);
    }

    public void attach() {
        System.out.println("前锋"+super.getName()+" 攻击");
    }

    public void defend() {
        System.out.println("前锋"+super.getName()+" 防守");
    }
}
