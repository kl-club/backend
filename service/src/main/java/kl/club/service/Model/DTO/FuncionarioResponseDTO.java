package kl.club.service.Model.DTO;

import kl.club.service.Model.enums.NivelFuncionario;

public record FuncionarioResponseDTO(

        Integer idFuncionario,
        String nome,
        String email,
        NivelFuncionario nivel
) {
}
