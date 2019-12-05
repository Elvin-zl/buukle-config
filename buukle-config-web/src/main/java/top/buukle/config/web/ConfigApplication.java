package top.buukle.config.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan({"top.buukle.config.dao","top.buukle.common.mvc"})
@SpringBootApplication(scanBasePackages={"top.buukle.config.*"})
@EnableFeignClients(basePackages = {"top.buukle.*"})
@EnableRedisHttpSession
@EnableTransactionManagement
public class ConfigApplication {
    private static volatile boolean RUNNING = true;
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ConfigApplication.class, args);
        System.out.println("启动成功");
        synchronized (ConfigApplication.class) {
            while (RUNNING) {
                try {
                    ConfigApplication.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    SpringApplication.exit(context);
                }
            }
        }
    }

    /**
     * @description 配置共享session 的domain,cookie
     * @param
     * @return org.springframework.session.web.http.CookieSerializer
     * @Author elvin
     * @Date 2019/7/30
     */
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
        defaultCookieSerializer.setDomainName("buukle.top");
        defaultCookieSerializer.setCookieName("BUUKLEUID");
        defaultCookieSerializer.setCookiePath("/");
        return defaultCookieSerializer;
    }
}