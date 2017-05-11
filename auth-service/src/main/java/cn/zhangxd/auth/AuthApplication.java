package cn.zhangxd.auth;

import cn.zhangxd.auth.service.DbUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@SpringBootApplication
@EnableResourceServer
@EnableDiscoveryClient
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @Configuration
    @EnableWebSecurity
    protected class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private DbUserDetailsService userDetailsService;

        @Bean
        public PasswordEncoder passwordEncoder() {
            return NoOpPasswordEncoder.getInstance();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .csrf().disable()
            ;
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .userDetailsService(userDetailsService)
                    .passwordEncoder(this.passwordEncoder())
            ;
        }
    }

    @Configuration
    @EnableAuthorizationServer
    protected class OAuth2ServerConfig extends AuthorizationServerConfigurerAdapter {

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private DbUserDetailsService userDetailsService;

        @Bean
        public JwtAccessTokenConverter jwtAccessTokenConverter() {
            return new JwtAccessTokenConverter();
        }

        @Bean
        public TokenStore tokenStore() {
            return new JwtTokenStore(jwtAccessTokenConverter());
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory()
                    .withClient("client")
                    .secret("secret")
                    .authorizedGrantTypes("password", "refresh_token")
                    .scopes("read")
                    .accessTokenValiditySeconds(3600) // 1 hour
                    .refreshTokenValiditySeconds(2592000) // 30 days
            ;
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                    .authenticationManager(authenticationManager)
                    .userDetailsService(userDetailsService)
                    .accessTokenConverter(jwtAccessTokenConverter())
                    .tokenStore(tokenStore())
            ;
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
            security
                    .tokenKeyAccess("permitAll()")
                    .checkTokenAccess("isAuthenticated()")
            ;
        }
    }
}
