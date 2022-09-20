package terafintech.terabank.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="config.token")
@Getter @Setter
public class ConfigProperties {

    private String publicKey;
    private String privateKey;
}
