package designmode.zhuangshi;

/**
 * Created by yxcui on 2017/4/27.
 */
public class yifu extends Person {

    protected Person componet;

    public yifu(Person componet){
        this.componet = componet;
    }

    @Override
    public void daban(){
        if(componet!=null){
            componet.show();
        }
    }
}
