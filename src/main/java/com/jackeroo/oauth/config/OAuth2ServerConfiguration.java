package com.jackeroo.oauth.config;

import com.jackeroo.oauth.config.handler.OauthAccessDeniedHandler;
import com.jackeroo.oauth.config.handler.OauthExceptionEntryPoint;
import com.jackeroo.oauth.config.handler.OauthTokenExtractor;
import com.jackeroo.oauth.config.handler.OauthUserApprovalHandler;
import com.jackeroo.oauth.config.handler.OauthWebResponseExceptionTranslator;
import com.jackeroo.oauth.config.serializer.FastJsonRedisTokenStoreSerializationStrategy;
import com.jackeroo.oauth.service.OauthClientDetailsService;
import com.jackeroo.oauth.service.OauthCodeService;
import com.jackeroo.oauth.service.OauthUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * Oauth授权服务器配置，Oauth资源服务器配置
 * @author alex
 * @date 2020/07/21
 */
@Slf4j
@Configuration
public class OAuth2ServerConfiguration {

    public static final String RESOURCE_ID = "USER-RESOURCE";

    /**
     * 资源服务器配置
     */
    @Configuration
    @EnableResourceServer
    protected class ApiResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Autowired
        private OauthTokenExtractor oauthTokenExtractor;
        @Autowired
        private OauthExceptionEntryPoint oauthExceptionEntryPoint;
        @Autowired
        private OauthAccessDeniedHandler oauthAccessDeniedHandler;

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.resourceId(RESOURCE_ID).stateless(false);
            // token提取器
            resources.tokenExtractor(oauthTokenExtractor)
                    // token异常处理器
                    .authenticationEntryPoint(oauthExceptionEntryPoint)
                    // 无权限异常处理器
                    .accessDeniedHandler(oauthAccessDeniedHandler);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                // STATELESS表示一定要携带access_token才能访问，无法通过session访问
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .requestMatchers().antMatchers("/get/**")
            .and()
                .authorizeRequests()
                .antMatchers("/get/**").access("#oauth2.hasScope('read')");
        }
    }

    /**
     * 资源服务器配置
     */
    @Configuration
    @EnableResourceServer
    protected class SyncResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Autowired
        private OauthTokenExtractor oauthTokenExtractor;
        @Autowired
        private OauthExceptionEntryPoint oauthExceptionEntryPoint;
        @Autowired
        private OauthAccessDeniedHandler oauthAccessDeniedHandler;

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.resourceId(RESOURCE_ID).stateless(false);
            // token提取器
            resources.tokenExtractor(oauthTokenExtractor)
                    // token异常处理器
                    .authenticationEntryPoint(oauthExceptionEntryPoint)
                    // 无权限异常处理器
                    .accessDeniedHandler(oauthAccessDeniedHandler);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                // STATELESS表示一定要携带access_token才能访问，无法通过session访问
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .requestMatchers().antMatchers("/update/**")
            .and()
                .authorizeRequests()
                .antMatchers("/update/**").access("#oauth2.hasScope('write') && hasRole('admin')");
        }
    }

    /**
     * oauth2.0授权服务器配置
     */
    @Configuration
    @EnableAuthorizationServer
    protected class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
        @Autowired
        private TokenStore tokenStore;

        @Autowired
        private OauthClientDetailsService oauthClientDetailsService;

        @Autowired
        private OauthCodeService authorizationCodeServices;

        @Autowired
        private OauthUserDetailService userDetailService;

        @Autowired
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;
        @Autowired
        private OauthWebResponseExceptionTranslator oauthWebResponseExceptionTranslator;

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.withClientDetails(oauthClientDetailsService);
        }

        /**
         * token 存储处理类，使用redis
         * @param connectionFactory
         * @return
         */
        @Bean
        public TokenStore tokenStore(RedisConnectionFactory connectionFactory) {
            final RedisTokenStore redisTokenStore = new RedisTokenStore(connectionFactory);
            // 前缀
            redisTokenStore.setPrefix("TOKEN:");
            // 序列化策略，使用fastjson
            redisTokenStore.setSerializationStrategy(new FastJsonRedisTokenStoreSerializationStrategy());
            return redisTokenStore;
        }

        /**
         * 认证端点配置
         * @param endpoints
         * @throws Exception
         */
        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints.tokenStore(tokenStore)
                    .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                    // 自定义认证异常处理
                    .exceptionTranslator(oauthWebResponseExceptionTranslator)
                    // 自定义的授权码模式的code（授权码）处理，使用redis存储
                    .authorizationCodeServices(authorizationCodeServices)
                    // 用户信息service
                    .userDetailsService(userDetailService)
                    // 用户授权确认处理器
                    .userApprovalHandler(userApprovalHandler())
                    // 注入authenticationManager来支持password模式
                    .authenticationManager(authenticationManager)
                    // 自定义授权确认页面
                    .pathMapping("/oauth/confirm_access", "/approval");
        }

        /**
         * AuthorizationServer的端点（/oauth/**）安全配置（访问规则、过滤器、返回结果处理等）
         * @param oauthServer
         * @throws Exception
         */
        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
            // 允许 /oauth/token的端点表单认证
            oauthServer.allowFormAuthenticationForClients()
                    .tokenKeyAccess("permitAll()")
                    // 允许 /oauth/token_check端点的访问
                    .checkTokenAccess("permitAll()");
        }

        @Bean
        public OAuth2RequestFactory oAuth2RequestFactory() {
            return new DefaultOAuth2RequestFactory(oauthClientDetailsService);
        }

        @Bean
        public UserApprovalHandler userApprovalHandler() {
            OauthUserApprovalHandler userApprovalHandler = new OauthUserApprovalHandler();
            userApprovalHandler.setTokenStore(tokenStore);
            userApprovalHandler.setClientDetailsService(oauthClientDetailsService);
            userApprovalHandler.setRequestFactory(oAuth2RequestFactory());
            return userApprovalHandler;
        }

    }
}
