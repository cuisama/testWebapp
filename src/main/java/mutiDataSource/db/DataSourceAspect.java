package mutiDataSource.db;

import cn.cmatc.frame.util.Global;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

public class DataSourceAspect {

    public void before(JoinPoint point) throws NoSuchMethodException {
        Object target = point.getTarget();

        Class<?>[] classz = target.getClass().getInterfaces();

        DataSource data=null;
        String method = point.getSignature().getName();
        Class<?>[] parameterTypes = ((MethodSignature) point.getSignature())
                .getMethod().getParameterTypes();
        Method m = classz[0].getMethod(method, parameterTypes);
        if (m != null && m.isAnnotationPresent(DataSource.class)) {
            data = m.getAnnotation(DataSource.class);
            DynamicDataSourceHolder.putDataSource(data.value());
        }else if(classz[0].isAnnotationPresent(DataSource.class)){
            data = classz[0].getAnnotation(DataSource.class);
            DynamicDataSourceHolder.putDataSource(data.value());
        }else{
            DynamicDataSourceHolder.putDataSource(Global.MASTER_DB);
        }
    }

}