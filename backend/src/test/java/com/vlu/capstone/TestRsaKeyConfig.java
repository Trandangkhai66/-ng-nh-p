package com.vlu.capstone;

import com.vlu.capstone.common.config.RsaKeyProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@TestConfiguration
@Profile("test")
public class TestRsaKeyConfig {

    @Bean
    @Primary
    public RsaKeyProperties rsaKeyProperties() throws Exception {
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(2048);
        var pair = gen.generateKeyPair();
        RsaKeyProperties p = new RsaKeyProperties();
        p.setPrivateKey((RSAPrivateKey) pair.getPrivate());
        p.setPublicKey((RSAPublicKey) pair.getPublic());
        return p;
    }
}
