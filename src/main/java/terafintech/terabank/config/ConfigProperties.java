package terafintech.terabank.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "config.token")
@Data
public class ConfigProperties {

    private String secretkey;
}
