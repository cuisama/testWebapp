package common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yxcui on 2017/4/24.
 */
public class verifyorktailF {

    public static void main(String[] args) throws ParseException {
        //String date = "2016-11-11";
        SimpleDateFormat sf = new SimpleDateFormat("yy-MM-dd");
        Date now = sf.parse("2000-03-30");
        System.out.println(now);
        int[] day31 = new int[]{1,0,3,0,5,0,7,8,0,10,0,12};
        int[] day30 = new int[]{0,0,0,4,0,6,0,0,9,0,11,0};
        int y = now.getYear()+1900;
        int m = now.getMonth();
        int d = now.getDate();
        if(m==11&&d==31){
            System.out.println("年末");
        }else if(day31[m]>0&&d==31){
            System.out.println("月末");
        }else if(day30[m]>0&&d==30){
            System.out.println("月末");
        }else if(m==1&&(d==28||d==29)){
            if(((y%100==0&&y%400==0)||(y%100!=0&&y%4==0))&&d==28){
                System.out.println("其他");
            }else{
                System.out.println("月末");
            }
        }else{
            System.out.println("其他");
        }
    }
}
