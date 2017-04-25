package jwt;

import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.Algorithm;
import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.internal.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;

/**
 * 用户--用户组 用户组--权限列表 权限列表--密钥对
 */
public class JWT {

    private static final String SECRET = "XX#$%()(#*!()!KL<><DSGFSGJYUKXFD 4e56shgf76653tssdfgudd>?N<:{LWPW";

    private static final String ISS = "iss";
    private static final String EXP = "exp";

    private static final String PAYLOAD = "payload";


    /**
     * 设置默认参数
     * iss(Issuser)：代表这个JWT的签发主体；
     * sub(Subject)：代表这个JWT的主体，即它的所有人；
     * aud(Audience)：代表这个JWT的接收对象；
     * exp(Expiration time)：是一个时间戳，代表这个JWT的过期时间；
     * nbf(Not Before)：是一个时间戳，代表这个JWT生效的开始时间，意味着在这个时间之前验证JWT是会失败的；
     * iat(Issued at)：是一个时间戳，代表这个JWT的签发时间；
     * jti(JWT ID)：是JWT的唯一标识。
     * @return
     */
    private static Map<String,Object> getDefaultClaims(){
        Map<String,Object> claims = new HashMap<String, Object>();
        claims.put(ISS,"www.cmatc.com");
        claims.put(EXP, System.currentTimeMillis() + 2L*60L*60L*1000L);
        return claims;
    }

    /**
     * 默认加密方式 {"typ":"JWT","alg":"HS256"} HS256("HmacSHA256")
     * @param object
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> String sign(T object) throws Exception{
        final JWTSigner signer = new JWTSigner(SECRET);
        final Map<String, Object> claims = getDefaultClaims();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(object);
        claims.put(PAYLOAD, jsonString);
        return signer.sign(claims);
    }

    /**
     * 默认方式解密
     * @param jwt
     * @param classT
     * @param <T>
     * @return
     * @throws Exception
     */
    public static<T> T unsign(String jwt, Class<T> classT) throws Exception{
        final JWTVerifier verifier = new JWTVerifier(SECRET);
        final Map<String,Object> claims= verifier.verify(jwt);
        return verifyData(claims,classT);
    }

    /**
     * RSA利用私钥加密
     * @param privateKeyFile
     * @param object
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> String signRSA(String privateKeyFile,T object) throws Exception{
        JWTSigner.Options options = new JWTSigner.Options();
        options.setAlgorithm(Algorithm.RS256);

        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(new BASE64Decoder().decodeBuffer(readKey(privateKeyFile)));
        KeyFactory keyf = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyf.generatePrivate(priPKCS8);

        final JWTSigner signer = new JWTSigner(privateKey);
        final Map<String, Object> claims = getDefaultClaims();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(object);
        claims.put(PAYLOAD, jsonString);

        return signer.sign(claims,options);
    }

    /**
     * RSA 利用公钥解密
     * @param publicKeyFile
     * @param jwt
     * @param classT
     * @param <T>
     * @return
     * @throws Exception
     */
    public static<T> T unSignRSA(String publicKeyFile,String jwt, Class<T> classT) throws Exception{
        X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(
                new BASE64Decoder().decodeBuffer(readKey(publicKeyFile)));
        java.security.KeyFactory keyFactory;
        keyFactory = java.security.KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(bobPubKeySpec);

        final JWTVerifier verifier = new JWTVerifier(publicKey);
        final Map<String,Object> claims= verifier.verify(jwt);
        return verifyData(claims,classT);
    }

    /**
     * 从根目录下读取密钥
     * @param fileName 文件名
     * @return
     * @throws IOException
     */
    private static String readKey(String fileName) throws IOException{
        //File file=new File(new JWT().getClass().getClassLoader().getResource("rsa_public_key.pem").getFile());
        InputStream fis = new JWT().getClass().getClassLoader().getResourceAsStream(fileName);
        InputStreamReader read = new InputStreamReader(fis,"UTF-8");
        BufferedReader bufferedReader = new BufferedReader(read);
        String priKey = "";
        String lineTxt = null;
        while((lineTxt = bufferedReader.readLine()) != null){
            priKey+=lineTxt;
        }
        read.close();
        return priKey;
    }

    /**
     * 获取密钥的有效数据 过期则不再有数据
     * @param claims
     * @param classT
     * @param <T>
     * @return
     * @throws IOException
     */
    private static<T> T verifyData(Map<String,Object> claims, Class<T> classT) throws IOException{
        if (claims.containsKey(EXP) && claims.containsKey(PAYLOAD)) {
            long exp = (Long)claims.get(EXP);
            long currentTimeMillis = System.currentTimeMillis();
            if (exp > currentTimeMillis) {
                String json = (String)claims.get(PAYLOAD);
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(json, classT);
            }
        }
        return null;
    }
}