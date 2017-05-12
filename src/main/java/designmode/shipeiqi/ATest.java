package designmode.shipeiqi;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 适配器模式
 * 开始只有前锋 后卫 他自己通过继承运动员类 实现进攻 防守
 * 后来来了个外籍中锋
 * 所以设置了一个翻译类 让翻译类继承运动员类 让翻译去进攻 防守
 * 其实翻译内部的进攻防守是外籍中锋干的
 *
 * 将一个类的接口转换成客户希望的另一个接口 适配器模式使得原来由于接口不兼容而不能一起工作的那些类可以一起工作
 * Created by yxcui on 2017/4/28.
 */
public class ATest {

    @Test
    public void main(){
        Player qianfeng = new Qianfeng("A");
        Player fanyi = new Fanyi("B");
        Player fanyi2 = new Fanyi(new Waijizhongfeng("C"));
        qianfeng.gongji();
        fanyi.gongji();
        fanyi2.fangshou();

    }

    /**
     * 类似于java.io库的适配器
     * 为要适配的类（外籍中锋 外籍前锋） 创建一个共通的父类（外籍运动员）
     * 字符流为了调用字节流
     */
    @Test
    public void main2(){
        Player qianfeng =new Qianfeng("AA");
        Player fanyi = new Fanyi2(new Waijiqianfeng2("BB"));
        Player fanyi2 = new Fanyi2(new Waijizhongfeng2("CC"));
        qianfeng.gongji();
        fanyi.gongji();
        fanyi2.fangshou();
    }

    /**
     * 不像适配器了
     */
    @Test
    public void main3(){
        Player qianfeng =new Qianfeng("AAA");
        Map<String,WaijiPlayer> wjPlayers = new HashMap<String, WaijiPlayer>();
        wjPlayers.put("BBB",new Waijiqianfeng2("BBB"));
        wjPlayers.put("CCC",new Waijiqianfeng2("CCC"));
        Player fanyi = new Fanyi3(wjPlayers);
        qianfeng.gongji();
        fanyi.gongji();
    }
}
