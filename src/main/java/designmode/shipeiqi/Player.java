package designmode.shipeiqi;

/**
 * Created by yxcui on 2017/4/28.
 */
public abstract class Player {
    private String name;

    public String getName() {
        return name;
    }

    public Player(String name){
        this.name = name;
    }
    public abstract void gongji();
    public abstract void fangshou();
}
