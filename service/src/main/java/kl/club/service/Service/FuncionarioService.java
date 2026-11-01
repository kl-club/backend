package kl.club.service.Service;

import kl.club.service.Exception.CredenciaisInvalidasException;
import kl.club.service.Exception.FuncionarioEmailExistenteException;
import kl.club.service.Exception.FuncionarioNaoEncontradoException;
import kl.club.service.Exception.SenhaAtualInvalidaException;
import kl.club.service.Model.DTO.FuncionarioAtualizacaoRequestDTO;
import kl.club.service.Model.DTO.FuncionarioLoginRequestDTO;
import kl.club.service.Model.DTO.FuncionarioRequestDTO;
import kl.club.service.Model.DTO.FuncionarioResponseDTO;
import kl.club.service.Model.DTO.FuncionarioSenhaRequestDTO;
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

    public FuncionarioResponseDTO atualizar(Integer id, FuncionarioAtualizacaoRequestDTO requestDTO) {

        Funcionario funcionario = buscarPorId(id);

        if (funcionarioRepository.existsByEmailAndIdFuncionarioNot(requestDTO.email(), id)) {
            throw new FuncionarioEmailExistenteException();
        }

        FuncionarioMapper.atualizar(funcionario, requestDTO);

        return FuncionarioMapper.toResponse(funcionarioRepository.save(funcionario));
    }

    public void desativar(Integer id) {

        Funcionario funcionario = buscarPorId(id);
        funcionario.setAtivo(false);

        funcionarioRepository.save(funcionario);
    }

    public void trocarSenha(Integer id, FuncionarioSenhaRequestDTO requestDTO) {

        Funcionario funcionario = buscarPorId(id);

        if (!funcionario.getSenha().equals(requestDTO.senhaAtual())) {
            throw new SenhaAtualInvalidaException();
        }

        funcionario.setSenha(requestDTO.novaSenha());

        funcionarioRepository.save(funcionario);
    }

    public FuncionarioResponseDTO login(FuncionarioLoginRequestDTO requestDTO) {

        Funcionario funcionario = funcionarioRepository.findByEmail(requestDTO.email())
                .orElseThrow(CredenciaisInvalidasException::new);

        if (!funcionario.getAtivo() || !funcionario.getSenha().equals(requestDTO.senha())) {
            throw new CredenciaisInvalidasException();
        }

        return FuncionarioMapper.toResponse(funcionario);
    }

    private Funcionario buscarPorId(Integer id) {
        return funcionarioRepository.findById(id)
                .orElseThrow(FuncionarioNaoEncontradoException::new);
    }
}
