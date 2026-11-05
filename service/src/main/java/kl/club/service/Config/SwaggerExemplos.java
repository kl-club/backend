package kl.club.service.Config;

/**
 * Exemplos de resposta de erro usados nas anotações do Swagger (@ExampleObject).
 * Cada exemplo reproduz a resposta real da API para o caso: ao mudar uma mensagem ou
 * um código de erro no back-end, atualize o exemplo correspondente.
 * Só documentação: nada aqui é usado pela aplicação em tempo de execução.
 */
public final class SwaggerExemplos {

    private SwaggerExemplos() {
    }

    // ---------------------------------------------------------------------
    // Produtos
    // ---------------------------------------------------------------------

    public static final String PRODUTO_PARAMETRO_INVALIDO = """
            {
              "title": "Bad Request",
              "status": 400,
              "detail": "O categoria é inválido",
              "instance": "/produtos",
              "codigo": "PARAMETRO_INVALIDO"
            }
            """;

    public static final String PRODUTO_DADOS_INVALIDOS = """
            {
              "title": "Bad Request",
              "status": 400,
              "detail": "Dados inválidos",
              "instance": "/produtos",
              "codigo": "DADOS_INVALIDOS",
              "erros": {
                "preco": "O preço deve ser maior que zero"
              }
            }
            """;

    public static final String PRODUTO_NAO_ENCONTRADO = """
            {
              "title": "Not Found",
              "status": 404,
              "detail": "Produto não encontrado",
              "instance": "/produtos/999",
              "codigo": "PRODUTO_NAO_ENCONTRADO"
            }
            """;

    public static final String PRODUTO_CATEGORIA_NAO_ENCONTRADA = """
            {
              "title": "Not Found",
              "status": 404,
              "detail": "Categoria não encontrada",
              "instance": "/produtos",
              "codigo": "CATEGORIA_NAO_ENCONTRADA"
            }
            """;

    public static final String PRODUTO_NOME_EXISTENTE = """
            {
              "title": "Conflict",
              "status": 409,
              "detail": "Produto já cadastrado!",
              "instance": "/produtos",
              "codigo": "PRODUTO_NOME_EXISTENTE"
            }
            """;

    public static final String PRODUTO_DISPONIBILIDADE_INVALIDA = """
            {
              "title": "Bad Request",
              "status": 400,
              "detail": "Dados inválidos",
              "instance": "/produtos/18/disponibilidade",
              "codigo": "DADOS_INVALIDOS",
              "erros": {
                "disponibilidade": "O campo disponibilidade é obrigatório"
              }
            }
            """;

    // ---------------------------------------------------------------------
    // Funcionários
    // ---------------------------------------------------------------------

    public static final String FUNCIONARIO_DADOS_INVALIDOS = """
            {
              "title": "Bad Request",
              "status": 400,
              "detail": "Dados inválidos",
              "instance": "/funcionarios",
              "codigo": "DADOS_INVALIDOS",
              "erros": {
                "email": "O email é inválido"
              }
            }
            """;

    public static final String FUNCIONARIO_EMAIL_EXISTENTE = """
            {
              "title": "Conflict",
              "status": 409,
              "detail": "Email já cadastrado!",
              "instance": "/funcionarios",
              "codigo": "FUNCIONARIO_EMAIL_EXISTENTE"
            }
            """;

    public static final String FUNCIONARIO_NAO_ENCONTRADO = """
            {
              "title": "Not Found",
              "status": 404,
              "detail": "Funcionário não encontrado",
              "instance": "/funcionarios/999",
              "codigo": "FUNCIONARIO_NAO_ENCONTRADO"
            }
            """;

    public static final String FUNCIONARIO_SENHA_DADOS_INVALIDOS = """
            {
              "title": "Bad Request",
              "status": 400,
              "detail": "Dados inválidos",
              "instance": "/funcionarios/1/senha",
              "codigo": "DADOS_INVALIDOS",
              "erros": {
                "novaSenha": "A nova senha é obrigatória",
                "senhaAtual": "A senha atual é obrigatória"
              }
            }
            """;

    public static final String FUNCIONARIO_SENHA_ATUAL_INVALIDA = """
            {
              "title": "Unauthorized",
              "status": 401,
              "detail": "Senha atual inválida",
              "instance": "/funcionarios/1/senha",
              "codigo": "SENHA_ATUAL_INVALIDA"
            }
            """;

    public static final String FUNCIONARIO_LOGIN_DADOS_INVALIDOS = """
            {
              "title": "Bad Request",
              "status": 400,
              "detail": "Dados inválidos",
              "instance": "/funcionarios/login",
              "codigo": "DADOS_INVALIDOS",
              "erros": {
                "email": "O email é obrigatório",
                "senha": "A senha é obrigatória"
              }
            }
            """;

    public static final String FUNCIONARIO_CREDENCIAIS_INVALIDAS = """
            {
              "title": "Unauthorized",
              "status": 401,
              "detail": "Email ou senha inválidos",
              "instance": "/funcionarios/login",
              "codigo": "CREDENCIAIS_INVALIDAS"
            }
            """;

    // ---------------------------------------------------------------------
    // Usuários (clientes)
    // As mensagens de validação de usuário saem em inglês (mensagens padrão do Bean Validation).
    // ---------------------------------------------------------------------

    public static final String USUARIO_DADOS_INVALIDOS = """
            {
              "title": "Bad Request",
              "status": 400,
              "detail": "Dados inválidos",
              "instance": "/usuarios",
              "codigo": "DADOS_INVALIDOS",
              "erros": {
                "cpf": "invalid Brazilian individual taxpayer registry number (CPF)"
              }
            }
            """;

    public static final String USUARIO_CPF_EXISTENTE = """
            {
              "title": "Conflict",
              "status": 409,
              "detail": "CPF já cadastrado!",
              "instance": "/usuarios",
              "codigo": "USUARIO_CPF_EXISTENTE"
            }
            """;

    public static final String USUARIO_TELEFONE_EXISTENTE = """
            {
              "title": "Conflict",
              "status": 409,
              "detail": "Telefone já cadastrado!",
              "instance": "/usuarios",
              "codigo": "USUARIO_TELEFONE_EXISTENTE"
            }
            """;

    /** 404 de usuário nas rotas de /usuarios: sai sem "detail" (a exceção é lançada com mensagem vazia). */
    public static final String USUARIO_NAO_ENCONTRADO = """
            {
              "title": "Not Found",
              "status": 404,
              "instance": "/usuarios/999",
              "codigo": "USUARIO_NAO_ENCONTRADO"
            }
            """;

    public static final String USUARIO_LOGIN_NAO_ENCONTRADO = """
            {
              "title": "Not Found",
              "status": 404,
              "instance": "/usuarios/login",
              "codigo": "USUARIO_NAO_ENCONTRADO"
            }
            """;

    public static final String USUARIO_LOGIN_DADOS_INVALIDOS = """
            {
              "title": "Bad Request",
              "status": 400,
              "detail": "Dados inválidos",
              "instance": "/usuarios/login",
              "codigo": "DADOS_INVALIDOS",
              "erros": {
                "cpf": "size must be between 11 and 11"
              }
            }
            """;

    public static final String USUARIO_VERIFICAR_PARAMETRO_AUSENTE = """
            {
              "title": "Bad Request",
              "status": 400,
              "detail": "O parâmetro telefone é obrigatório",
              "instance": "/usuarios/verificar",
              "codigo": "PARAMETRO_INVALIDO"
            }
            """;

    /** Erro padrão do Spring (ResponseStatusException): outro formato, sem "codigo" nem "detail". */
    public static final String USUARIO_VERIFICAR_SEM_CODIGO = """
            {
              "timestamp": "2026-11-05T15:30:00.000Z",
              "status": 400,
              "error": "Bad Request",
              "path": "/usuarios/verificar"
            }
            """;

    public static final String USUARIO_ATUALIZAR_DADOS_INVALIDOS = """
            {
              "title": "Bad Request",
              "status": 400,
              "detail": "Dados inválidos",
              "instance": "/usuarios/1",
              "codigo": "DADOS_INVALIDOS",
              "erros": {
                "nome": "must match \\".*\\\\S.*\\""
              }
            }
            """;

    /** Erro padrão do Spring (ResponseStatusException): outro formato, sem "codigo" nem "detail". */
    public static final String USUARIO_ATUALIZAR_SEM_CAMPOS = """
            {
              "timestamp": "2026-11-05T15:30:00.000Z",
              "status": 400,
              "error": "Bad Request",
              "path": "/usuarios/1"
            }
            """;

    public static final String USUARIO_ATUALIZAR_TELEFONE_EXISTENTE = """
            {
              "title": "Conflict",
              "status": 409,
              "detail": "Telefone já cadastrado!",
              "instance": "/usuarios/1",
              "codigo": "USUARIO_TELEFONE_EXISTENTE"
            }
            """;

    /** Erro padrão do Spring (ResponseStatusException): outro formato, sem "codigo" nem "detail". */
    public static final String USUARIO_PEDIDO_NAO_ENCONTRADO = """
            {
              "timestamp": "2026-11-05T15:30:00.000Z",
              "status": 404,
              "error": "Not Found",
              "path": "/usuarios/3/pedidos/1"
            }
            """;

    // ---------------------------------------------------------------------
    // Pedidos
    // ---------------------------------------------------------------------

    public static final String PEDIDO_DADOS_INVALIDOS = """
            {
              "title": "Bad Request",
              "status": 400,
              "detail": "Dados inválidos",
              "instance": "/pedidos",
              "codigo": "DADOS_INVALIDOS",
              "erros": {
                "itens[0].quantidade": "A quantidade deve ser no mínimo 1"
              }
            }
            """;

    public static final String PEDIDO_PRODUTO_REPETIDO = """
            {
              "title": "Bad Request",
              "status": 400,
              "detail": "O produto 1 aparece mais de uma vez no pedido. Informe cada produto uma única vez, com a quantidade desejada",
              "instance": "/pedidos",
              "codigo": "PRODUTO_REPETIDO"
            }
            """;

    public static final String PEDIDO_USUARIO_NAO_ENCONTRADO = """
            {
              "title": "Not Found",
              "status": 404,
              "detail": "Usuário não encontrado",
              "instance": "/pedidos",
              "codigo": "USUARIO_NAO_ENCONTRADO"
            }
            """;

    public static final String PEDIDO_PRODUTO_NAO_ENCONTRADO = """
            {
              "title": "Not Found",
              "status": 404,
              "detail": "Produto 999 não encontrado",
              "instance": "/pedidos",
              "codigo": "PRODUTO_NAO_ENCONTRADO"
            }
            """;

    public static final String PEDIDO_PRODUTO_INDISPONIVEL = """
            {
              "title": "Conflict",
              "status": 409,
              "detail": "O produto Temaki Empanado está indisponível",
              "instance": "/pedidos",
              "codigo": "PRODUTO_INDISPONIVEL"
            }
            """;

    public static final String PEDIDO_NAO_ENCONTRADO = """
            {
              "title": "Not Found",
              "status": 404,
              "detail": "Pedido não encontrado",
              "instance": "/pedidos/999",
              "codigo": "PEDIDO_NAO_ENCONTRADO"
            }
            """;

    public static final String DASHBOARD_PERIODO_INVALIDO = """
            {
              "title": "Bad Request",
              "status": 400,
              "detail": "O periodo é inválido",
              "instance": "/pedidos/dashboard/kpis",
              "codigo": "PARAMETRO_INVALIDO"
            }
            """;

    public static final String DASHBOARD_PAGINACAO_INVALIDA = """
            {
              "title": "Bad Request",
              "status": 400,
              "detail": "O tamanho deve ser de 1 a 50",
              "instance": "/pedidos/dashboard/tabela",
              "codigo": "PARAMETRO_INVALIDO"
            }
            """;

    public static final String HISTORICO_PARAMETRO_INVALIDO = """
            {
              "title": "Bad Request",
              "status": 400,
              "detail": "A data inicial não pode ser depois da data final",
              "instance": "/pedidos/historico",
              "codigo": "PARAMETRO_INVALIDO"
            }
            """;

    public static final String PEDIDO_AVANCAR_CONCLUIDO = """
            {
              "title": "Conflict",
              "status": 409,
              "detail": "O pedido #9 já foi concluído e não pode avançar",
              "instance": "/pedidos/9/avancar",
              "codigo": "TRANSICAO_INVALIDA"
            }
            """;

    public static final String PEDIDO_AVANCAR_CANCELADO = """
            {
              "title": "Conflict",
              "status": 409,
              "detail": "O pedido #17 foi cancelado e não pode avançar",
              "instance": "/pedidos/17/avancar",
              "codigo": "TRANSICAO_INVALIDA"
            }
            """;

    public static final String PEDIDO_CANCELAR_INVALIDO = """
            {
              "title": "Conflict",
              "status": 409,
              "detail": "O pedido #8 está EM_PREPARO e não pode ser cancelado. Só pedidos PENDENTE ou PAGO podem ser cancelados",
              "instance": "/pedidos/8/cancelar",
              "codigo": "TRANSICAO_INVALIDA"
            }
            """;

    public static final String PEDIDO_AVANCAR_NAO_ENCONTRADO = """
            {
              "title": "Not Found",
              "status": 404,
              "detail": "Pedido não encontrado",
              "instance": "/pedidos/999/avancar",
              "codigo": "PEDIDO_NAO_ENCONTRADO"
            }
            """;
}
