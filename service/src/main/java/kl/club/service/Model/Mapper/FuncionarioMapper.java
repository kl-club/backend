package kl.club.service.Model.Mapper;

import kl.club.service.Model.DTO.FuncionarioAtualizacaoRequestDTO;
import kl.club.service.Model.DTO.FuncionarioRequestDTO;
import kl.club.service.Model.DTO.FuncionarioResponseDTO;
import kl.club.service.Model.Entity.Funcionario;

import java.util.ArrayList;
import java.util.List;

public final class FuncionarioMapper {

    private FuncionarioMapper() {
    }

    public static FuncionarioResponseDTO toResponse(Funcionario funcionario) {
        return new FuncionarioResponseDTO(
                funcionario.getIdFuncionario(),
                funcionario.getNome(),
                funcionario.getEmail(),
                funcionario.getNivel(),
                funcionario.getAtivo());
    }

    public static List<FuncionarioResponseDTO> toResponse(List<Funcionario> funcionarios) {

        List<FuncionarioResponseDTO> funcionariosResponse = new ArrayList<>();

        for (Funcionario funcionario : funcionarios) {
            funcionariosResponse.add(toResponse(funcionario));
        }

        return funcionariosResponse;
    }

    public static Funcionario toEntity(FuncionarioRequestDTO dto) {

        Funcionario funcionario = new Funcionario();

        funcionario.setNome(dto.nome());
        funcionario.setEmail(dto.email());
        funcionario.setSenha(dto.senha());
        funcionario.setNivel(dto.nivel());

        return funcionario;
    }

    public static void atualizar(Funcionario funcionario, FuncionarioAtualizacaoRequestDTO dto) {
        funcionario.setNome(dto.nome());
        funcionario.setEmail(dto.email());
        funcionario.setNivel(dto.nivel());
    }
}
