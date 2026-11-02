package kl.club.service.Model.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

@Schema(
        name = "ErroResponse",
        description = "Resposta padronizada de erro da API."
)
public record ErroResponseDTO(

        @Schema(example = "about:blank")
        String type,

        @Schema(example = "Not Found")
        String title,

        @Schema(example = "404")
        Integer status,

        @Schema(example = "Categoria não encontrada")
        String detail,

        @Schema(
                description = "Identificador da ocorrência, quando informado.",
                example = "/categorias/999",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        String instance,

        @Schema(
                description = "Erros específicos dos campos, quando houver.",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        Map<String, String> erros

) {}
