package wiscom.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "2026 wiscom backend"),
        servers = @Server(url = "/", description = "현재 접속한 서버")
)
public class SwaggerConfig {
}
