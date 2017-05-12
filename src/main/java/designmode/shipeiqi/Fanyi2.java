package designmode.shipeiqi;

/**
 * Created by yxcui on 2017/4/28.
 */
public class Fanyi2 extends Player {

    private WaijiPlayer wjPlayer;

    public Fanyi2(String name) {
        super(name);
        this.wjPlayer = new Waijiqianfeng2(name);
    }

    public Fanyi2(WaijiPlayer wjPlayer){
        super(wjPlayer.getName());
        this.wjPlayer = wjPlayer;
    }

    public void gongji() {
        wjPlayer.attach();
    }

    public void fangshou() {
        wjPlayer.defend();
    }
}
