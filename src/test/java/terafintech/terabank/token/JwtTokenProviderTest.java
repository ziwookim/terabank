package terafintech.terabank.token;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import terafintech.terabank.config.ConfigProperties;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtTokenProviderTest {

    @Autowired
    ConfigProperties configProperties;

    @Test
    public void printToken() throws Exception {

        String publicKey = configProperties.getPublicKey();

        System.out.println("token: " + publicKey);

        assertEquals("mWWgJg3iq1RLusf6V5gVebggZKWLtuv5lyqL9V574YveaAuwUgJJQRM4w392C4gq8F79ewBVwIqVx0s8b5JXlDMAsaOBV2k7rQ8tJnXNBJXuZs2h3poN2KLF5Ci4hFmCpbYX3qCeVOy0akpMTyJcr9",
                publicKey);
    }
}