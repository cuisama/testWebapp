package designmode.zhuangshi2;

/**
 * Created by yxcui on 2017/4/27.
 */
public class ATest {
    public static void main(String[] args) {
        Coffee coffee = new Espresso();
        coffee = new Mocha(coffee);
        coffee = new Mocha(coffee);
        coffee = new Whip(coffee);
        //3.6(2.5 + 0.5 + 0.5 + 0.1)
        System.out.println(coffee.cost());
    }
}
