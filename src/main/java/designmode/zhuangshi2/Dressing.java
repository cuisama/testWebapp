package designmode.zhuangshi2;

/**
 * Created by yxcui on 2017/4/27.
 */
public class Dressing implements Coffee {
    private Coffee coffee;

    public Dressing(Coffee coffee){
        this.coffee = coffee;
    }

    public double cost(){
        System.out.println("加工");
        return coffee.cost();
    }
}
