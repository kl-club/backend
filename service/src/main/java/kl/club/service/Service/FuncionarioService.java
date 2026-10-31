package kl.club.service.Service;

import kl.club.service.Exception.CredenciaisInvalidasException;
import kl.club.service.Exception.FuncionarioEmailExistenteException;
import kl.club.service.Model.DTO.FuncionarioLoginRequestDTO;
import kl.club.service.Model.DTO.FuncionarioRequestDTO;
import kl.club.service.Model.DTO.FuncionarioResponseDTO;
import kl.club.service.Model.Entity.Funcionario;
import kl.club.service.Model.Mapper.FuncionarioMapper;
import kl.club.service.Repository.FuncionarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    public List<FuncionarioResponseDTO> listar() {
        return FuncionarioMapper.toResponse(funcionarioRepository.findAll());
    }

    public FuncionarioResponseDTO cadastrar(FuncionarioRequestDTO requestDTO) {

        if (funcionarioRepository.existsByEmail(requestDTO.email())) {
            throw new FuncionarioEmailExistenteException();
        }

        Funcionario funcionarioCadastrar = FuncionarioMapper.toEntity(requestDTO);

        return FuncionarioMapper.toResponse(funcionarioRepository.save(funcionarioCadastrar));
    }

    public FuncionarioResponseDTO login(FuncionarioLoginRequestDTO requestDTO) {

        Funcionario funcionario = funcionarioRepository.findByEmail(requestDTO.email())
                .orElseThrow(CredenciaisInvalidasException::new);

        if (!funcionario.getSenha().equals(requestDTO.senha())) {
            throw new CredenciaisInvalidasException();
        }

        return FuncionarioMapper.toResponse(funcionario);
    }
}
