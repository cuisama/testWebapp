package designmode.zhuangshi2;

/**
 * Created by yxcui on 2017/4/27.
 */
public class Whip extends Dressing {
    public Whip(Coffee coffee){
        super(coffee);
    }

    @Override
    public double cost(){
        System.out.println("搅一搅");
        return super.cost() + 0.1;
    }
}
