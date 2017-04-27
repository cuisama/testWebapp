package designmode.zhuangshi2;

/**
 * Created by yxcui on 2017/4/27.
 */
public class Mocha extends Dressing {
    public Mocha(Coffee coffee){
        super(coffee);
    }

    @Override
    public double cost(){
        System.out.println("加点摩卡");
        return super.cost() + 0.5;
    }
}
