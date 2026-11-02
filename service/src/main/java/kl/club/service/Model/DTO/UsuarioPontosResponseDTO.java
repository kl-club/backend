package kl.club.service.Model.DTO;

public record UsuarioPontosResponseDTO(
        Integer idUsuario,
        String nome,
        Long pontosAcumulados
) {
}