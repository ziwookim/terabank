package terafintech.terabank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import terafintech.terabank.config.ConfigProperties;
import terafintech.terabank.token.JwtTokenProvider;

@SpringBootApplication
//@EnableConfigurationProperties(ConfigProperties.class)
public class TerabankApplication {

	public static void main(String[] args) {
		SpringApplication.run(TerabankApplication.class, args);
		JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
	}

}
