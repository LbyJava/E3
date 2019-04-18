import com.lby.common.jedis.JedisClient;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.Jedis;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/18 16:00
 */
public class JedisTest {
    @Test
    public void jedisTest() {
        Jedis jedis = new Jedis("192.168.25.129",6379);
        jedis.set("test123", "Oh my God");
        String s = jedis.get("test123");
        System.out.println("s = " + s);
        jedis.close();
    }

    @Test
    public void jedisClientTest() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
        JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
        jedisClient.set("myTest", "jedisTest");
        System.out.println(jedisClient.get("myTest"));
    }
}
