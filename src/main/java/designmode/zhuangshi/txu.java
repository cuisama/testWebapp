package designmode.zhuangshi;

import com.sun.org.apache.xpath.internal.SourceTree;
import jdk.management.resource.internal.inst.SocketOutputStreamRMHooks;

/**
 * Created by yxcui on 2017/4/27.
 */
public class txu extends yifu{

    public txu(Person componet) {
        super(componet);
        show();
    }

    @Override
    public void daban(){
        super.show();
        System.out.println("T恤");
    }
}
