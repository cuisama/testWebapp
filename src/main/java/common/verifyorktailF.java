package common;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yxcui on 2017/4/24.
 */
public class verifyorktailF {

    @Test
    public void main() throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yy-MM-dd");
        String[] dates = new String[]{"2000-02-28","2000-02-29","2000-02-30",
                "2000-03-28","2000-03-31","2000-03-30",
                "2001-02-28","2001-02-29","2001-02-30",
                "2000-12-31","2000-12-30"};
        for(int i=dates.length-1;i>-1;i--){
            Date now = sf.parse(dates[i]);
            System.out.println(dates[i]+"  "+verify(now));
        }
    }

    private String verify(Date now){
        int[] day31 = new int[]{1,0,3,0,5,0,7,8,0,10,0,12};
        int[] day30 = new int[]{0,0,0,4,0,6,0,0,9,0,11,0};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        int y = calendar.get(Calendar.YEAR);//now.getYear()+1900;
        int m = calendar.get(Calendar.MONTH);//now.getMonth();
        int d = calendar.get(Calendar.DATE);//now.getDate();
        if(m==11&&d==31){
            return "年末";
        }else if(day31[m]>0&&d==31){
            return "月末";
        }else if(day30[m]>0&&d==30){
            return "月末";
        }else if(m==1&&(d==28||d==29)){
            if(((y%100==0&&y%400==0)||(y%100!=0&&y%4==0))&&d==28){
                return "其他";
            }else{
                return "月末";
            }
        }else{
            return "其他";
        }
    }
}
