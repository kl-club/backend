package kl.club.service.Model.DTO;

public record UsuarioResponseDTO(

        Integer idUsuario,
        String nome,
        String cpf,
        String telefone
) {
}
