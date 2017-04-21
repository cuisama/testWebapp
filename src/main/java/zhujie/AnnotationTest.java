package zhujie;

import org.springframework.stereotype.Service;

/**
 * Created by yxcui on 2017/4/7.
 */
@MyTest
@Service
public class AnnotationTest {


    public static void main(String[] args){
        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        MyTest test = AnnotationTest.class.getAnnotation(MyTest.class);
        System.out.println(test.value());

    }

}
