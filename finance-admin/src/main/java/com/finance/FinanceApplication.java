package com.finance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author finance
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class FinanceApplication
{
    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(FinanceApplication.class, args);
        System.out.println("(=^･ω･^=)ﾉﾞ  系统启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                "    /\\__/\\  \n" +
                "   (  . . ) \n" +
                "   )   v   (\n" +
                "  (  > ∨ <  )\n" +
                " (   |   |   )\n" +
                "(    |   |    )\n" +
                " \\   |___|   /\n" +
                "  \\__|   |__/\n" +
                "     |___|\n");
    }
}
