package designmode.shipeiqi;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yxcui on 2017/4/28.
 */
public class Fanyi3 extends Player {

    private Map<String,WaijiPlayer> wjPlayers = new HashMap<String, WaijiPlayer>();

    public Fanyi3(String name) {
        super(name);
    }
/*    public Fanyi3(WaijiPlayer wjPlayer){
        super(wjPlayer.getName());
        wjPlayers.put(wjPlayer.getName(),wjPlayer);
    }*/

    public Fanyi3(Map<String,WaijiPlayer> wjPlayers){
        super("");
        this.wjPlayers = wjPlayers;
    }

    public void gongji() {
        for( WaijiPlayer wjPlayer :wjPlayers.values()){
            wjPlayer.attach();
        }
    }

    public void fangshou() {
        for( WaijiPlayer wjPlayer :wjPlayers.values()){
            wjPlayer.attach();
        }
    }
}
