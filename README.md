# KL Club Backend

API REST do KL Club, projeto do 3º semestre de ADS. Atende os frontends de cadastro de clientes, pedidos e dashboard.

O sistema cobre usuários, funcionários, produtos, categorias e pedidos com pontuação. Os pedidos incluem a criação pelo totem e a gestão, o histórico e o dashboard do painel administrativo.

## Tecnologias

- Java 21
- Spring Boot 4.1.1 (Web MVC, Data JPA e Validation)
- MySQL
- Maven (com Maven Wrapper)
- springdoc-openapi (Swagger)

## Como rodar

### Pré-requisitos

- JDK 21
- MySQL em `localhost:3306`

### Banco de dados

A aplicação usa `spring.jpa.hibernate.ddl-auto=validate`, ou seja, não cria as tabelas sozinha e não sobe se elas não existirem ou divergirem das entidades.

1. Crie o banco `kl_club` com as tabelas do projeto. O script de criação (`script.sql`) e o de dados de exemplo (`inserts_kl_club.sql`) não ficam no repositório: peça a um integrante da equipe. Use a versão com o status `CANCELADO` em `pedido.status_pedido`; sem ele, o cancelamento de pedidos falha.
2. Crie um usuário no MySQL com as credenciais definidas em `service/src/main/resources/application.properties`.

Para usar outro endereço de banco, defina a variável de ambiente `DB_URL`.

### Aplicação

Os comandos do Maven rodam na pasta `service/`:

```bash
cd service
./mvnw spring-boot:run
```

No Windows, use `mvnw.cmd spring-boot:run`.

A API sobe em `http://localhost:8080`, e o Swagger fica em `http://localhost:8080/swagger-ui.html`.

### Outros comandos

```bash
./mvnw clean test-compile   # compila o código e os testes, sem precisar de banco
./mvnw test                 # exige MySQL ativo com as tabelas criadas
```

## Rotas

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/usuarios` | Cadastra um usuário |
| `GET` | `/funcionarios` | Lista os funcionários |
| `POST` | `/funcionarios` | Cadastra um funcionário |
| `PUT` | `/funcionarios/{id}` | Edita nome, email e nível |
| `DELETE` | `/funcionarios/{id}` | Desativa um funcionário, sem apagar o registro |
| `PATCH` | `/funcionarios/{id}/senha` | Troca a senha, exigindo a senha atual |
| `POST` | `/funcionarios/login` | Valida email e senha |
| `GET` | `/categorias` | Lista as categorias |
| `POST` | `/categorias` | Cadastra uma categoria |
| `PUT` | `/categorias/{id}` | Atualiza o nome de uma categoria |
| `PATCH` | `/categorias/{id}/ativo` | Ativa ou desativa uma categoria |
| `GET` | `/produtos` | Lista os produtos, com os filtros opcionais `categoria` e `disponibilidade` |
| `GET` | `/produtos/{id}` | Busca um produto |
| `POST` | `/produtos` | Cadastra um produto, que nasce disponível |
| `PUT` | `/produtos/{id}` | Atualiza os dados de um produto, exceto a disponibilidade |
| `PATCH` | `/produtos/{id}/disponibilidade` | Altera a disponibilidade de um produto |

### Pedidos

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/pedidos` | Cria um pedido. Recebe só os itens (`idProduto` e `quantidade`) e os dados do pedido; preço, total, pontos, status e data são calculados pela API |
| `GET` | `/pedidos/{id}` | Busca um pedido, com cliente e itens |
| `GET` | `/pedidos/gestao/novos` | Pedidos de hoje em `PENDENTE` ou `PAGO`, mais antigos primeiro |
| `GET` | `/pedidos/gestao/em-preparo` | Pedidos de hoje em `EM_PREPARO` |
| `GET` | `/pedidos/gestao/prontos` | Pedidos de hoje em `PRONTO_PARA_RETIRADA` |
| `GET` | `/pedidos/gestao/concluidos` | Pedidos de hoje em `CONCLUIDO` |
| `PATCH` | `/pedidos/{id}/avancar` | Move o pedido para o próximo status |
| `PATCH` | `/pedidos/{id}/cancelar` | Cancela um pedido `PENDENTE` ou `PAGO` |
| `GET` | `/pedidos/historico` | Histórico paginado, com filtros opcionais por código, nome do cliente, datas e modalidade, e ordenação por valor |
| `GET` | `/pedidos/dashboard/kpis` | Faturamento, pedidos, ticket médio e clientes atendidos do dia |
| `GET` | `/pedidos/dashboard/tabela` | Pedidos do dia, paginados, inclusive os cancelados |
| `GET` | `/pedidos/dashboard/pagamentos` | Valor e percentual de cada forma de pagamento no dia |
| `GET` | `/pedidos/dashboard/produtos-mais-vendidos` | Os 5 produtos mais vendidos no dia |

Ciclo de status: `PENDENTE` ou `PAGO` → `EM_PREPARO` → `PRONTO_PARA_RETIRADA` → `CONCLUIDO`. Pedidos em `DINHEIRO` nascem `PENDENTE`; os demais nascem `PAGO`. As regras completas estão em `Especificação de Pedidos.md`.

### Erros

As respostas de erro da API usam o formato `ProblemDetail`, com a propriedade `codigo` para identificar o caso sem depender do texto. Erros de validação trazem os campos em `erros`:

```json
{
  "title": "Bad Request",
  "status": 400,
  "detail": "Dados inválidos",
  "instance": "/funcionarios",
  "codigo": "DADOS_INVALIDOS",
  "erros": { "email": "O email é inválido" }
}
```

Alguns códigos: `DADOS_INVALIDOS`, `PARAMETRO_INVALIDO`, `PEDIDO_NAO_ENCONTRADO`, `PRODUTO_INDISPONIVEL`, `PRODUTO_REPETIDO`, `TRANSICAO_INVALIDA`, `ROTA_NAO_ENCONTRADA` e `METODO_NAO_PERMITIDO`.

## Estrutura

O código fica em `service/src/main/java/kl/club/service/`:

| Pacote | Responsabilidade |
|---|---|
| `Controller` | Rotas REST |
| `Service` | Regras de negócio |
| `Repository` | Acesso ao banco com Spring Data JPA |
| `Exception` | Exceções de negócio e `GlobalExceptionHandler` |
| `Model.DTO` | Contratos de entrada e saída da API |
| `Model.Entity` | Entidades JPA |
| `Model.enums` | Enums de domínio |
| `Model.Mapper` | Conversão entre DTO e entidade |

## Limitações atuais

- As rotas ainda não têm autenticação.
- As senhas de funcionários são gravadas em texto puro. Não use dados reais.
- Não há integração de pagamentos: o status inicial do pedido depende só da forma de pagamento, e o cancelamento não faz estorno.
- O dashboard só aceita o período `HOJE`.
- O único teste automatizado é o `contextLoads`.
