package io;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

//import java.io.BufferedReader;
//import java.io.InputStreamReader;

//import java.io.InputStream;
//import java.io.BufferedInputStream;

import java.io.*;

import java.net.FileNameMap;
import java.security.PublicKey;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

/**
 * *stream 字节流
 *
 * Created by yxcui on 2017/4/26.
 */
public class ioTest {

    @Test
    public void main() throws FileNotFoundException {
//        BufferedInputStream
//        BufferedReader
//        Properties
//   InputStreamReader

//        OutputStream
//        Writer
        new BufferedReader(new InputStreamReader(new FileInputStream("")));
    }

    @BeforeClass
    public static void mkdir(){
        File file = new File("d:/iotest");
        if(!file.exists()){
            file.mkdir();
        }
        System.out.println("-----------mkdir()-----------end");
    }

    @Before
    public void testBef(){
        System.out.println("testBef");
    }


    @Test
    public void fileStream(){
        byte[] buffer=new byte[512];   //一次取出的字节数大小,缓冲区大小
        int numberRead=0;
        FileInputStream input=null;
        FileOutputStream out =null;
        try {
            input=new FileInputStream("D:/iotest/aa.png");
            out=new FileOutputStream("D:/iotest/bb.jpg"); //如果文件不存在会自动创建

            while ((numberRead=input.read(buffer))!=-1) {  //numberRead的目的在于防止最后一次读取的字节小于buffer长度，
                out.write(buffer, 0, numberRead);       //否则会自动被填充0
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }finally{
            try {
                input.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * ObjectOutputStream.writeObject()的作用是把一个实例的对象以文件的形式保存到磁盘上，这个过程就叫Java对象的持久化。
     * 而这个文件是以二进制的形式编写的，当你用文本编辑器将它打开，这些二进制代码与某个字符集映射之后，显示出来的东西就成了乱码。
     * 即使输出的是一个String的对象，也是以该String对象的二进制编码的形式输出，而不是输出String对象的内容。
     *
     */
    @Test
    public void ObjetStream(){
        ObjectOutputStream objectwriter=null;
        ObjectInputStream objectreader=null;

        try {
            objectwriter=new ObjectOutputStream(new FileOutputStream("D:/iotest/ObjetStream_student.txt"));
            objectwriter.writeObject(new Student("gg", 22));
            objectwriter.writeObject(new Student("tt", 18));
            objectwriter.writeObject(new Student("rr", 17));
            objectreader=new ObjectInputStream(new FileInputStream("D:/iotest/ObjetStream_student.txt"));
            for (int i = 0; i < 3; i++) {
                System.out.println(objectreader.readObject());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        }finally{
            try {
                objectreader.close();
                objectwriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void DataStream(){
        Student[] members = {new Student("Justin",90),
                new Student("momor",95),
                new Student("Bush",88)};
        try
        {
            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream("D:/iotest/DataStream_student.txt"));

            for(Student member:members)
            {
                //写入UTF字符串
                dataOutputStream.writeUTF(member.getName());
                //写入int数据
                dataOutputStream.writeInt(member.getAge());
            }

            //所有数据至目的地
            dataOutputStream.flush();
            //关闭流
            dataOutputStream.close();

            DataInputStream dataInputStream = new DataInputStream(new FileInputStream("D:/iotest/DataStream_student.txt"));

            //读出数据并还原为对象
            for(int i=0;i<members.length;i++)
            {
                //读出UTF字符串
                String name =dataInputStream.readUTF();
                //读出int数据
                int score =dataInputStream.readInt();
                members[i] = new Student(name,score);
            }

            //关闭流
            dataInputStream.close();

            //显示还原后的数据
            for(Student member : members)
            {
                System.out.printf("%s\t%d%n",member.getName(),member.getAge());
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }


    @Test
    public void PushBackInputStream() throws IOException {
        String str = "hello,rollenholt";
        PushbackInputStream push = null; // 声明回退流对象
        ByteArrayInputStream bat = null; // 声明字节数组流对象
        bat = new ByteArrayInputStream(str.getBytes());
        push = new PushbackInputStream(bat); // 创建回退流对象，将拆解的字节数组流传入
        int temp = 0;
        while ((temp = push.read()) != -1) { // push.read()逐字节读取存放在temp中，如果读取完成返回-1
            if (temp == ',') { // 判断读取的是否是逗号
                push.unread(temp); //回到temp的位置
                temp = push.read(); //接着读取字节
                System.out.print("(回退" + (char) temp + ") "); // 输出回退的字符
            } else {
                System.out.print((char) temp); // 否则输出字符
            }
        }
    }

    /**
     *            SequenceInputStream合并流，将与之相连接的流集组合成一个输入流并从第一个输入流开始读取，
     *            直到到达文件末尾，接着从第二个输入流读取，依次类推，直到到达包含的最后一个输入流的文件末尾为止。
     *            合并流的作用是将多个源合并合一个源。可接收枚举类所封闭的多个字节流对象。
     */
    @Test
    public void SequenceInputStream(){
        // 创建一个合并流的对象
        SequenceInputStream sis = null;
        // 创建输出流。
        BufferedOutputStream bos = null;
        try {
            // 构建流集合。
            Vector<InputStream> vector = new Vector<InputStream>();
            vector.addElement(new FileInputStream("D:\\iotest\\text1.txt"));
            vector.addElement(new FileInputStream("D:\\iotest\\text2.txt"));
            vector.addElement(new FileInputStream("D:\\iotest\\text3.txt"));
            Enumeration<InputStream> e = vector.elements();

            sis = new SequenceInputStream(e);

            bos = new BufferedOutputStream(new FileOutputStream("D:\\iotest\\SequenceInputStream_text4.txt"));
            // 读写数据
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = sis.read(buf)) != -1) {
                bos.write(buf, 0, len);
                bos.flush();
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (sis != null)
                    sis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (bos != null)
                    bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void FileReader(){
        char[] buffer=new char[512];   //一次取出的字节数大小,缓冲区大小
        int numberRead=0;
        FileReader reader=null;        //读取字符文件的流
        PrintWriter writer=null;    //写字符到控制台的流

        try {
            reader=new FileReader("D:/iotest/text1.txt");
            writer=new PrintWriter(System.out);  //PrintWriter可以输出字符到文件，也可以输出到控制台
            while ((numberRead=reader.read(buffer))!=-1) {
                writer.write(buffer, 0, numberRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            writer.close();       //这个不用抛异常
        }
    }


    @Test
    public void BufferedReader() throws IOException {
        String[] fileName = new String[]{"D:/iotest/text1.txt","D:/iotest/text2.txt","D:/iotest/text3.txt"};
        String str;
        //构建对该文件您的输入流
        BufferedWriter writer=new BufferedWriter(new FileWriter("D:/iotest/FileConcatenate.txt"));
        for(String name: fileName){
            BufferedReader reader=new BufferedReader(new FileReader(name));

            while ((str=reader.readLine())!=null) {
                System.out.println(str);
                writer.write(str);
                writer.newLine();
            }
        }
    }

    /**
     * 使用StreamTokenizer来统计文件中的字符数
     * StreamTokenizer 类获取输入流并将其分析为“标记”，允许一次读取一个标记。
     * 分析过程由一个表和许多可以设置为各种状态的标志控制。
     * 该流的标记生成器可以识别标识符、数字、引用的字符串和各种注释样式。
     *
     *  默认情况下，StreamTokenizer认为下列内容是Token: 字母、数字、除C和C++注释符号以外的其他符号。
     *  如符号"/"不是Token，注释后的内容也不是，而"\"是Token。单引号和双引号以及其中的内容，只能算是一个Token。
     *  统计文章字符数的程序，不是简单的统计Token数就万事大吉，因为字符数不等于Token。按照Token的规定，
     *  引号中的内容就算是10页也算一个Token。如果希望引号和引号中的内容都算作Token，应该调用下面的代码：
     *    st.ordinaryChar('\'');
     * st.ordinaryChar('\"');
     */
    @Test
    public void StreamTokenizer(){
        FileReader fileReader = null;
        try {
            fileReader = new FileReader("D:/iotest/text1.txt");
            //创建分析给定字符流的标记生成器
            StreamTokenizer st = new StreamTokenizer(new BufferedReader(
                    fileReader));

            //ordinaryChar方法指定字符参数在此标记生成器中是“普通”字符。
            //下面指定单引号、双引号和注释符号是普通字符
            st.ordinaryChar('\'');
            st.ordinaryChar('\"');
            st.ordinaryChar('/');

            String s;
            int numberSum = 0;
            int wordSum = 0;
            int symbolSum = 0;
            int total = 0;
            //nextToken方法读取下一个Token.
            //TT_EOF指示已读到流末尾的常量。
            while (st.nextToken() !=StreamTokenizer.TT_EOF) {
                //在调用 nextToken 方法之后，ttype字段将包含刚读取的标记的类型
                switch (st.ttype) {
                    //TT_EOL指示已读到行末尾的常量。
                    case StreamTokenizer.TT_EOL:
                        break;
                    //TT_NUMBER指示已读到一个数字标记的常量
                    case StreamTokenizer.TT_NUMBER:
                        //如果当前标记是一个数字，nval字段将包含该数字的值
                        s = String.valueOf((st.nval));
                        System.out.println("数字有："+s);
                        numberSum ++;
                        break;
                    //TT_WORD指示已读到一个文字标记的常量
                    case StreamTokenizer.TT_WORD:
                        //如果当前标记是一个文字标记，sval字段包含一个给出该文字标记的字符的字符串
                        s = st.sval;
                        System.out.println("单词有： "+s);
                        wordSum ++;
                        break;
                    default:
                        //如果以上3中类型都不是，则为英文的标点符号
                        s = String.valueOf((char) st.ttype);
                        System.out.println("标点有： "+s);
                        symbolSum ++;
                }
            }
            System.out.println("数字有 " + numberSum+"个");
            System.out.println("单词有 " + wordSum+"个");
            System.out.println("标点符号有： " + symbolSum+"个");
            total = symbolSum + numberSum +wordSum;
            System.out.println("Total = " + total);
            System.out.println(total); ;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(-1);
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e1) {
                }
            }
        }
    }


}

