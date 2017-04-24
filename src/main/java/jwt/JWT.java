package jwt;

import java.awt.*;
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
import org.springframework.aop.TargetClassAware;
import sun.misc.BASE64Decoder;

public class JWT {

    private static final String SECRET = "XX#$%()(#*!()!KL<><MQLMNQNQJQK sdfkjsdrow32234545fdf>?N<:{LWPW";

    private static final String EXP = "exp";//exp(Expiration time)：是一个时间戳，代表这个JWT的过期时间；

    private static final String PAYLOAD = "payload";

    //eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9
    // .eyJleHAiOjE0OTMwMDQ2MDk4NzQsInBheWxvYWQiOiJ7XCJpZFwiOlwiMVwiLFwibmFtZVwiOlwiMTFcIixcInBhc3N3b3JkXCI6XCIxMTFcIn0ifQ
    // .TU0D9hTLSNAmhCIaSAcpweFA3Qv6hm6B5xMFmTbWHk4

    //{"typ":"JWT","alg":"HS256"} HS256("HmacSHA256")

    //部分接口 用户无权限调用
    /**
     * get jwt String of object
     * @param object
     *            the POJO object
     * @param maxAge
     *            the milliseconds of life time
     * @return the jwt token
     */
    public static <T> String sign(T object, long maxAge) {
        try {
            final JWTSigner signer = new JWTSigner(SECRET);
            final Map<String, Object> claims = new HashMap<String, Object>();
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(object);
            claims.put(PAYLOAD, jsonString);
            claims.put(EXP, System.currentTimeMillis() + maxAge);
            return signer.sign(claims);
        } catch(Exception e) {
            return null;
        }
    }

    /**
     * get the object of jwt if not expired
     * @param jwt
     * @return POJO object
     */
    public static<T> T unsign(String jwt, Class<T> classT) throws Exception{
        final JWTVerifier verifier = new JWTVerifier(SECRET);
        final Map<String,Object> claims= verifier.verify(jwt);
        return verifyData(claims,classT);
    }



    public static <T> String signRSA(T object,long maxAge) throws Exception{
        JWTSigner.Options options = new JWTSigner.Options();
        options.setAlgorithm(Algorithm.RS256);

        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(new BASE64Decoder().decodeBuffer(readKey("rsa_private_key.pem")));
        KeyFactory keyf = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyf.generatePrivate(priPKCS8);

        final JWTSigner signer = new JWTSigner(privateKey);
        final Map<String, Object> claims = new HashMap<String, Object>();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(object);
        claims.put(PAYLOAD, jsonString);
        claims.put(EXP, System.currentTimeMillis() + maxAge);
        return signer.sign(claims,options);
    }

    public static<T> T unSignRSA(String jwt, Class<T> classT) throws Exception{
        X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(
                new BASE64Decoder().decodeBuffer(readKey("rsa_public_key.pem")));
        java.security.KeyFactory keyFactory;
        keyFactory = java.security.KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(bobPubKeySpec);

        final JWTVerifier verifier = new JWTVerifier(publicKey);
        final Map<String,Object> claims= verifier.verify(jwt);
        return verifyData(claims,classT);
    }

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