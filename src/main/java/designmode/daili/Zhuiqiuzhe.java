package designmode.daili;

/**
 * Created by yxcui on 2017/5/12.
 */
public class Zhuiqiuzhe implements GiveGift{

    private Meinv meinv;

    public Zhuiqiuzhe(Meinv meinv) {
        this.meinv = meinv;
    }

    public void sendHua() {
        System.out.println(meinv.getName()+" 给你花");
    }

    public void sendYifu() {
        System.out.println(meinv.getName()+" 给你衣服");
    }

    public void sendQiaokl() {
        System.out.println(meinv.getName()+" 给你巧克力");
    }
}
