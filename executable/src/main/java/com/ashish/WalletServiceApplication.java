package com.ashish;

import com.ashish.wallet.config.SwaggerConfig;
import com.ashish.wallet.config.WalletConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude =
        {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
@Import({SwaggerConfig.class, WalletConfig.class})
public class WalletServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WalletServiceApplication.class, args);
    }

}
