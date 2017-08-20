package common;

import org.junit.Test;

/**
 * Created by yxcui on 2017/5/19.
 */
public class getLaw
{
    @Test
    public void main(){
        int x=12;
        System.out.println((x>>3));
        System.out.println(((((x>>2)+(x>>3))*2)%8));
    }
}
