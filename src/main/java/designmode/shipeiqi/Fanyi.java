package designmode.shipeiqi;

/**
 * Created by yxcui on 2017/4/28.
 */
public class Fanyi extends Player {
    private Waijizhongfeng wjzf ;

    public Fanyi(String name){
        super(name);
        this.wjzf = new Waijizhongfeng(name);
    }

/*    private Waijizhongfeng wjzf2 = new Waijizhongfeng();

    public Fanyi(String name2){
        super(name2);
        this.wjzf2.setName(name2);
        wjzf = wjzf2;
    }*/


    public Fanyi(Waijizhongfeng wjzf) {
        super(wjzf.getName());
        this.wjzf = wjzf;
    }

    public void gongji() {
        wjzf.attach();
    }

    public void fangshou() {
        wjzf.defend();
    }
}
