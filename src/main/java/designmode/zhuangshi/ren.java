package designmode.zhuangshi;

/**
 * Created by yxcui on 2017/4/27.
 */
public class ren extends Person {
    private String name;

    public ren(String name){
        this.name = name;
    }

    @Override
    public void show(){
        System.out.println("装饰人"+name);
    }
}
