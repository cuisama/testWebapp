package common;

import org.junit.Test;

/**
 * Created by yxcui on 2017/5/27.
 * 测试下异常的效率
 */
public class TestException {

    @Test
    public void main(){
        int k =100;
        long start = System.currentTimeMillis();
        for (int i=0;i<k;i++){
            Object o= new Object();
        }
        long end = System.currentTimeMillis();
        System.out.println(end-start);

        start = System.currentTimeMillis();
        for (int i=0;i<k;i++){
            try{
                throw new NullPointerException();
            }catch (Exception e){
                Object o= new Object();
                e.printStackTrace();
            }
        }
        end = System.currentTimeMillis();
        System.out.println(end-start);
    }
}
