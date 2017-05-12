package designmode.daili;

/**
 * Created by yxcui on 2017/5/12.
 */
public class Proxy implements GiveGift{

    private Zhuiqiuzhe zqz;

    public Proxy(Meinv meinv){
        zqz = new Zhuiqiuzhe(meinv);
    }
    public void sendHua() {
        zqz.sendHua();
    }

    public void sendYifu() {
        zqz.sendYifu();
    }

    public void sendQiaokl() {
        zqz.sendQiaokl();
    }
}
