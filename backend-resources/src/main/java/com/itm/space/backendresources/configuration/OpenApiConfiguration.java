package com.itm.space.backendresources.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "oauth2_auth_code",
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(
                authorizationCode = @OAuthFlow(
                        authorizationUrl = "http://backend-keycloak-auth:8080/auth/realms/ITM/protocol/openid-connect/auth",
                        tokenUrl = "http://backend-keycloak-auth:8080/auth/realms/ITM/protocol/openid-connect/token",
                        scopes = {
                                @OAuthScope(name = "openid", description = "Read access")
                        }
                )
        ),
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfiguration {

    @Bean
    public OpenAPI publicApi() {
        return new OpenAPI();
    }
}