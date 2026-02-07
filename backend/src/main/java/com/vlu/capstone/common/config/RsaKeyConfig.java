package com.vlu.capstone.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary; // <--- MỚI THÊM
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
@Configuration
@Profile("!test")
public class RsaKeyConfig {

    @Value("${jwt.private-key-path:classpath:keys/private_key.pem}")
    private Resource privateKeyResource;

    @Value("${jwt.public-key-path:classpath:keys/public_key.pem}")
    private Resource publicKeyResource;

    @Bean
    @Primary  // <--- QUAN TRỌNG: Lệnh này bảo Spring "Hãy chọn tôi đi!"
    public RsaKeyProperties rsaKeyProperties() throws Exception {
        RsaKeyProperties props = new RsaKeyProperties();
        props.setPrivateKey(loadPrivateKey());
        props.setPublicKey(loadPublicKey());
        return props;
    }

    private RSAPrivateKey loadPrivateKey() throws Exception {
        try (InputStream is = privateKeyResource.getInputStream()) {
            String pem = new String(is.readAllBytes());
            String base64 = pem
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] decoded = Base64.getDecoder().decode(base64);
            return (RSAPrivateKey) KeyFactory.getInstance("RSA")
                    .generatePrivate(new PKCS8EncodedKeySpec(decoded));
        } catch (Exception e) {
            log.error("Cannot load private key from {}", privateKeyResource, e);
            throw new IllegalStateException("JWT private key error", e);
        }
    }

    private RSAPublicKey loadPublicKey() throws Exception {
        try (InputStream is = publicKeyResource.getInputStream()) {
            String pem = new String(is.readAllBytes());
            String base64 = pem
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] decoded = Base64.getDecoder().decode(base64);
            return (RSAPublicKey) KeyFactory.getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(decoded));
        } catch (Exception e) {
            log.error("Cannot load public key from {}", publicKeyResource, e);
            throw new IllegalStateException("JWT public key error", e);
        }
    }
}