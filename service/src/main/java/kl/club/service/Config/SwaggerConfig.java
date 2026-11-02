package kl.club.service.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Servidor local de desenvolvimento");

        return new OpenAPI()
                .info(new Info()
                        .title("KL Club API")
                        .version("1.0.0")
                        .description("""
                                API REST do **KL Club**.

                                ### Recursos principais
                                - Usuários
                                - Funcionários
                                - Categorias
                                - Produtos
                                - Pedidos
                                - Programa de fidelidade
                                """)
                        .contact(new Contact()
                                .name("Equipe KL Club")
                                .email("contato@klclub.com")))
                .servers(List.of(localServer));
    }
}