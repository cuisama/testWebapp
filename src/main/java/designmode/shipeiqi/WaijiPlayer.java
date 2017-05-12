package designmode.shipeiqi;

/**
 * Created by yxcui on 2017/4/28.
 */
public abstract class WaijiPlayer {
    private String name;

    public String getName() {
        return name;
    }

    public WaijiPlayer(String name){
        this.name = name;
    }
    public abstract void attach();
    public abstract void defend();
}
