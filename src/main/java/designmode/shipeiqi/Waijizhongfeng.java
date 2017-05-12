package designmode.shipeiqi;

/**
 * Created by yxcui on 2017/4/28.
 */
public class Waijizhongfeng {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Waijizhongfeng() {
    }

    public Waijizhongfeng(String name) {
        this.name = name;
    }

    public void attach(){
        System.out.println("中锋"+name+" 攻击");
    }
    public void defend(){
        System.out.println("中锋"+name+" 防守");
    }
}
