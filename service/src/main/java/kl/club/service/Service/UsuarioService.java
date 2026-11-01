package kl.club.service.Service;

import kl.club.service.Model.Mapper.PedidoMapper;
import kl.club.service.Model.enums.StatusPedido;
import org.springframework.transaction.annotation.Transactional;
import kl.club.service.Exception.UsuarioCpfExistenteException;
import kl.club.service.Exception.UsuarioNaoEncontradoException;
import kl.club.service.Exception.UsuarioTelefoneExistenteException;
import kl.club.service.Model.DTO.*;
import kl.club.service.Model.Entity.Pedido;
import kl.club.service.Model.Entity.Usuario;
import kl.club.service.Model.Mapper.UsuarioMapper;
import kl.club.service.Repository.PedidoRepository;
import kl.club.service.Repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PedidoRepository pedidoRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO requestDTO) {

        if (usuarioRepository.existsByCpf(requestDTO.cpf())) {
            throw new UsuarioCpfExistenteException();
        }

        if (usuarioRepository.existsByTelefone(requestDTO.telefone())) {
            throw new UsuarioTelefoneExistenteException();
        }

        Usuario usuarioCadastrar = UsuarioMapper.toEntity(requestDTO);

        return UsuarioMapper.toResponse(usuarioRepository.save(usuarioCadastrar));
    }


    public UsuarioVerificacaoResponseDTO verificar(
            String cpf,
            String telefone
    ) {
        if ((cpf == null || cpf.isBlank()) &&
                (telefone == null || telefone.isBlank())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Informe um CPF ou telefone"
            );
        }

        Boolean cpfEmUso = null;
        Boolean telefoneEmUso = null;

        if (cpf != null && !cpf.isBlank()) {
            cpfEmUso = usuarioRepository.existsByCpf(cpf);
        }

        if (telefone != null && !telefone.isBlank()) {
            telefoneEmUso = usuarioRepository.existsByTelefone(telefone);
        }

        return new UsuarioVerificacaoResponseDTO(
                cpfEmUso,
                telefoneEmUso
        );
    }

    public UsuarioResponseDTO login(UsuarioAutenticacaoRequestDTO requestDTO){

        Optional<Usuario> usuario = usuarioRepository.findByCpf(requestDTO.cpf());

        if (usuario.isEmpty()){
            throw new UsuarioNaoEncontradoException("");
        }

        return UsuarioMapper.toResponse(usuario.get());
    }

    @Transactional(readOnly = true)
    public List<UsuarioPedidosResponseDTO> listarTodosComPedidos() {

        List<Usuario> usuarios =
                usuarioRepository.buscarTodosComPedidos();

        List<Integer> idsPedidos = usuarios.stream()
                .flatMap(usuario -> usuario.getPedidos().stream())
                .map(Pedido::getIdPedido)
                .toList();

        if (!idsPedidos.isEmpty()) {
            pedidoRepository.buscarPedidosComItens(idsPedidos);
        }

        return usuarios.stream()
                .map(UsuarioMapper::toResponseComPedidos)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioPedidosResponseDTO buscarPorId(Integer id) {

        Optional<Usuario> usuarioEncontrado =
                usuarioRepository.buscarPorIdComPedidos(id);

        if (usuarioEncontrado.isEmpty()) {
            throw new UsuarioNaoEncontradoException("");
        }

        Usuario usuario = usuarioEncontrado.get();

        List<Integer> idsPedidos = usuario.getPedidos()
                .stream()
                .map(Pedido::getIdPedido)
                .toList();

        if (!idsPedidos.isEmpty()) {
            pedidoRepository.buscarPedidosComItens(idsPedidos);
        }

        return UsuarioMapper.toResponseComPedidos(usuario);
    }

    @Transactional
    public UsuarioResponseDTO atualizar(
            Integer id,
            UsuarioAtualizacaoRequestDTO dto
    ) {

        Optional<Usuario> usuarioEncontrado =
                usuarioRepository.findById(id);

        if (usuarioEncontrado.isEmpty()) {
            throw new UsuarioNaoEncontradoException("");
        }

        Usuario usuario = usuarioEncontrado.get();

        if (dto.nome() == null && dto.telefone() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Informe pelo menos um campo para atualização"
            );
        }

        if (dto.nome() != null) {
            usuario.setNome(dto.nome());
        }

        if (dto.telefone() != null) {

            boolean telefoneExistente =
                    usuarioRepository.existsByTelefoneAndIdUsuarioNot(
                            dto.telefone(),
                            id
                    );

            if (telefoneExistente) {
                throw new UsuarioTelefoneExistenteException();
            }

            usuario.setTelefone(dto.telefone());
        }

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);

        return UsuarioMapper.toResponse(usuarioAtualizado);
    }

    @Transactional(readOnly = true)
    public UsuarioPontosResponseDTO consultarPontos(Integer id) {

        Optional<Usuario> usuarioEncontrado =
                usuarioRepository.findById(id);

        if (usuarioEncontrado.isEmpty()) {
            throw new UsuarioNaoEncontradoException("");
        }

        Usuario usuario = usuarioEncontrado.get();

        Long pontos = pedidoRepository.somarPontosPorUsuario(
                id,
                StatusPedido.CONCLUIDO
        );

        return new UsuarioPontosResponseDTO(
                usuario.getIdUsuario(),
                usuario.getNome(),
                pontos
        );
    }

    @Transactional(readOnly = true)
    public List<PedidoDetalhadoResponseDTO> listarPedidosPorUsuario(Integer id) {

        boolean usuarioExiste = usuarioRepository.existsById(id);

        if (!usuarioExiste) {
            throw new UsuarioNaoEncontradoException("");
        }

        List<Pedido> pedidos = pedidoRepository.buscarPedidosPorUsuario(id);

        return pedidos.stream()
                .map(PedidoMapper::toResponseDetalhado)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResumoResponseDTO buscarResumo(Integer id) {

        Optional<Usuario> usuarioEncontrado =
                usuarioRepository.findById(id);

        if (usuarioEncontrado.isEmpty()) {
            throw new UsuarioNaoEncontradoException("");
        }

        Usuario usuario = usuarioEncontrado.get();

        long quantidadePedidos =
                pedidoRepository.countByUsuario_IdUsuario(id);

        Long pontosAcumulados =
                pedidoRepository.somarPontosPorUsuario(
                        id,
                        StatusPedido.CONCLUIDO
                );

        BigDecimal totalGasto =
                pedidoRepository.somarValorGastoPorUsuario(
                        id,
                        StatusPedido.CONCLUIDO
                );

        if (totalGasto == null) {
            totalGasto = BigDecimal.ZERO;
        }

        Optional<Pedido> ultimoPedido =
                pedidoRepository.findFirstByUsuario_IdUsuarioOrderByDataHoraDescIdPedidoDesc(id);

        Integer ultimoPedidoId = null;

        if (ultimoPedido.isPresent()) {
            ultimoPedidoId = ultimoPedido.get().getIdPedido();
        }

        return new UsuarioResumoResponseDTO(
                usuario.getIdUsuario(),
                usuario.getNome(),
                quantidadePedidos,
                totalGasto,
                pontosAcumulados,
                ultimoPedidoId
        );
    }

    @Transactional(readOnly = true)
    public PedidoDetalhadoResponseDTO buscarPedidoPorUsuario(
            Integer usuarioId,
            Integer pedidoId
    ) {

        boolean usuarioExiste = usuarioRepository.existsById(usuarioId);

        if (!usuarioExiste) {
            throw new UsuarioNaoEncontradoException("");
        }

        Optional<Pedido> pedidoEncontrado =
                pedidoRepository.buscarPedidoPorUsuario(
                        usuarioId,
                        pedidoId
                );

        if (pedidoEncontrado.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Pedido não encontrado para este usuário"
            );
        }

        Pedido pedido = pedidoEncontrado.get();

        return PedidoMapper.toResponseDetalhado(pedido);
    }
}
