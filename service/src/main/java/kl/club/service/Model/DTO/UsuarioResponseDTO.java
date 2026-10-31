package kl.club.service.model.dto;

public record UsuarioResponseDTO(

        Integer idUsuario,
        String nome,
        String cpf,
        String telefone
) {
}
