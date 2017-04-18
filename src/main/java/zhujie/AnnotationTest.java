package zhujie;

import org.springframework.stereotype.Service;

/**
 * Created by yxcui on 2017/4/7.
 */
@Test
@Service
public class AnnotationTest {


    public static void main(String[] args){
        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        Test test = AnnotationTest.class.getAnnotation(Test.class);
        System.out.println(test.value());

    }

}
