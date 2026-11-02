package kl.club.service.Model.DTO;

public record UsuarioVerificacaoResponseDTO(
        Boolean cpfEmUso,
        Boolean telefoneEmUso
) {
}