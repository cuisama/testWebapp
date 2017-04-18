package classloader;

public class ClassLoaderTest {

    public static void main(String[] args) {
        try {
            ClassLoader loader = ClassLoaderTest.class.getClassLoader();  //获得ClassLoaderTest这个类的类加载器
            while(loader != null) {
                System.out.println(loader);
                loader = loader.getParent();    //获得父加载器的引用
            }
            System.out.println(loader);


//            String rootUrl = "http://localhost:8080/httpweb/classes";
//            NetworkClassLoader networkClassLoader = new NetworkClassLoader(rootUrl);
//            String classname = "org.classloader.simple.NetClassLoaderTest";
//            Class clazz = networkClassLoader.loadClass(classname);
//            System.out.println(clazz.getClassLoader());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}