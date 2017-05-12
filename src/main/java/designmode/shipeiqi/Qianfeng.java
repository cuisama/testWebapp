package designmode.shipeiqi;

/**
 * Created by yxcui on 2017/4/28.
 */
public class Qianfeng extends Player {
    //private String name;

    public Qianfeng(String name) {
        super(name);
    }

    public void gongji() {
        System.out.println("qiangfeng"+super.getName()+" gongji");
    }

    public void fangshou() {
        System.out.println("qianfeng"+super.getName()+" fangshou");
    }
}
