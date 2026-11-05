# KL Club Backend

API REST do KL Club, projeto do 3º semestre de ADS. Atende três frontends: o cadastro de clientes, o totem de pedidos e o dashboard administrativo.

O sistema cobre clientes (usuários), funcionários, categorias, produtos e pedidos com programa de pontos. Os pedidos incluem a criação pelo totem e, no painel administrativo, a gestão do dia, o histórico e o dashboard.

## Tecnologias

- Java 21
- Spring Boot 4.1.1 (Web MVC, Data JPA, JDBC e Validation)
- MySQL 8
- Maven (com Maven Wrapper)
- springdoc-openapi 3.1.0 (Swagger)

## Como rodar

### Pré-requisitos

- JDK 21
- MySQL em `localhost:3306`

### Banco de dados

A aplicação usa `spring.jpa.hibernate.ddl-auto=validate`, ou seja, não cria as tabelas sozinha e não sobe se elas não existirem ou divergirem das entidades.

1. Crie o banco `kl_club` com o script de criação (`db/script.sql`) e, se quiser dados de exemplo, rode depois o `db/inserts_kl_club.sql`.
2. Crie um usuário no MySQL com as credenciais definidas em `service/src/main/resources/application.properties`.

O `script.sql` começa com `DROP DATABASE`: rodá-lo de novo apaga todos os dados. O `inserts_kl_club.sql` traz um recorte do cardápio da KD Lanches para demonstração (12 categorias e 21 produtos, 3 deles indisponíveis) e cria pedidos com a data de hoje e de ontem, para o dashboard e a gestão não ficarem vazios. Nos dados de exemplo, todos os funcionários usam a senha `Senha@123`.

Para usar outro endereço de banco, defina a variável de ambiente `DB_URL`.

### Aplicação

Os comandos do Maven rodam na pasta `service/`:

```bash
cd service
./mvnw spring-boot:run
```

No Windows, use `mvnw.cmd spring-boot:run`.

A API sobe em `http://localhost:8080`, e o Swagger fica em `http://localhost:8080/swagger-ui.html`. Todas as rotas estão documentadas no Swagger, com as regras, os parâmetros, cada resposta possível (sucesso e erros, com o `codigo`) e exemplos iguais às respostas reais. Os DTOs mostram descrição e exemplo de cada campo.

O CORS (`Config/CorsConfig.java`) libera chamadas do navegador vindas de `http://localhost` em qualquer porta, para os frontends rodarem com o Vite (o dashboard na 5173 e o totem na 5174).

### Outros comandos

```bash
./mvnw clean test-compile   # compila o código e os testes, sem precisar de banco
./mvnw test                 # exige MySQL ativo com as tabelas criadas
```

### Rodando com Docker

O `service/Dockerfile` gera a imagem da API em dois estágios: o Maven (`maven:3.9-eclipse-temurin-21`) compila o jar sem rodar os testes, e o runtime usa só o JRE (`eclipse-temurin:21-jre`), com `TZ=America/Sao_Paulo`.

O jeito mais simples de subir tudo é o `docker-compose.yml` da raiz deste repositório, que sobe o MySQL, esta API, o `frontend-dashboard` e o `frontend-pedidos`.

O compose constrói os fronts a partir de `../frontend-dashboard` e `../frontend-pedidos`. Clone os três repositórios lado a lado, com esses nomes exatos de pasta:

```
kl-club/
├── backend/              # este repositório (docker-compose.yml)
├── frontend-dashboard/
└── frontend-pedidos/
```

```bash
docker compose up -d --build   # na raiz deste repositório
```

| Serviço | Endereço no host |
|---|---|
| API | `http://localhost:8080` (Swagger em `/swagger-ui.html`) |
| Dashboard | `http://localhost:5173` |
| Totem | `http://localhost:5174/totem` |
| MySQL | `localhost:3307` (usuário `newuser`, senha `1234`) |

- O MySQL cria o banco com o `db/script.sql` e o `db/inserts_kl_club.sql`.
- O MySQL roda num container próprio (`mysql:8.4`) e expõe a porta 3307, para não conflitar com um MySQL local na 3306. Os dados ficam no volume `kl-club_mysql-data`.
- Os dois scripts rodam só na primeira subida, com o volume vazio, e os pedidos do seed ganham a data desse dia. Para recriar o banco com o seed, rode `docker compose down -v` e suba de novo.
- A API só sobe depois que o MySQL termina os scripts (healthcheck). Ela recebe o banco pelas variáveis `DB_URL`, `SPRING_DATASOURCE_USERNAME` e `SPRING_DATASOURCE_PASSWORD`, sem mudar o `application.properties`.
- Os fronts são servidos pelo nginx nas portas 5173 e 5174, então o CORS atual já os libera.

Para rodar só a imagem da API, contra um MySQL fora do Docker:

```bash
cd service
docker build -t kl-club-backend .
docker run --rm -p 8080:8080 -e DB_URL="jdbc:mysql://host.docker.internal:3306/kl_club?useSSL=false&serverTimezone=America/Sao_Paulo&allowPublicKeyRetrieval=true" kl-club-backend
```

## Rotas

### Usuários (clientes)

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/usuarios` | Cadastra um cliente. CPF válido e telefone com 11 dígitos, ambos únicos |
| `GET` | `/usuarios/verificar` | Informa se o CPF e o telefone já estão em uso. Recebe `cpf` e `telefone` como parâmetros |
| `POST` | `/usuarios/login` | Identifica o cliente pelo CPF e devolve os dados dele, sem senha nem token |
| `GET` | `/usuarios` | Lista os clientes com os pedidos de cada um |
| `GET` | `/usuarios/{id}` | Busca um cliente com os pedidos |
| `PATCH` | `/usuarios/{id}` | Atualiza o nome e/ou o telefone |
| `GET` | `/usuarios/{id}/pontos` | Saldo de pontos do cliente |
| `GET` | `/usuarios/{id}/pedidos` | Pedidos do cliente, mais recentes primeiro |
| `GET` | `/usuarios/{id}/pedidos/{pedidoId}` | Um pedido do cliente |
| `GET` | `/usuarios/{id}/resumo` | Quantidade de pedidos, total gasto, pontos e último pedido |

### Funcionários

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/funcionarios` | Lista os funcionários |
| `POST` | `/funcionarios` | Cadastra um funcionário, que nasce ativo |
| `PUT` | `/funcionarios/{id}` | Edita nome, email e nível |
| `DELETE` | `/funcionarios/{id}` | Desativa um funcionário, sem apagar o registro (responde `204`) |
| `PATCH` | `/funcionarios/{id}/senha` | Troca a senha, exigindo a senha atual (responde `204`) |
| `POST` | `/funcionarios/login` | Valida email e senha. Funcionário desativado não entra |

### Categorias

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/categorias` | Lista as categorias, ativas e inativas |
| `POST` | `/categorias` | Cadastra uma categoria, que nasce ativa |
| `PUT` | `/categorias/{id}` | Atualiza o nome de uma categoria |
| `PATCH` | `/categorias/{id}/ativo` | Ativa ou desativa uma categoria |

### Produtos

| Método | Rota | Descrição |
|---|---|---|
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
| `GET` | `/pedidos/gestao/em-preparo` | Pedidos de hoje em `EM_PREPARO`, mais antigos primeiro |
| `GET` | `/pedidos/gestao/prontos` | Pedidos de hoje em `PRONTO_PARA_RETIRADA`, mais antigos primeiro |
| `GET` | `/pedidos/gestao/concluidos` | Pedidos de hoje em `CONCLUIDO`, mais recentes primeiro (pela hora do pedido) |
| `PATCH` | `/pedidos/{id}/avancar` | Move o pedido para o próximo status |
| `PATCH` | `/pedidos/{id}/cancelar` | Cancela um pedido `PENDENTE` ou `PAGO` |
| `GET` | `/pedidos/historico` | Histórico paginado, com filtros opcionais por código, nome do cliente, datas e modalidade, e ordenação por valor |
| `GET` | `/pedidos/dashboard/kpis` | Faturamento, pedidos, ticket médio e clientes atendidos do dia |
| `GET` | `/pedidos/dashboard/tabela` | Pedidos do dia, paginados, inclusive os cancelados |
| `GET` | `/pedidos/dashboard/pagamentos` | Valor e percentual de cada forma de pagamento no dia |
| `GET` | `/pedidos/dashboard/produtos-mais-vendidos` | Os 5 produtos mais vendidos no dia |

As regras completas de pedidos estão em `Especificação de Pedidos.md`.

## Regras principais

- **Pedido sem cadastro:** o cliente é opcional. Pedido sem cliente não gera pontos.
- **Status do pedido:** `PENDENTE` ou `PAGO` → `EM_PREPARO` → `PRONTO_PARA_RETIRADA` → `CONCLUIDO`. Só pedidos `PENDENTE` ou `PAGO` podem ser cancelados.
- **Status inicial:** pedidos em `DINHEIRO` nascem `PENDENTE` (pagamento no balcão); os demais nascem `PAGO`.
- **Pontos:** cada produto tem uma pontuação. Os pontos de um pedido contam no saldo do cliente a partir de `PAGO` e deixam de contar se ele for cancelado.
- **Valores do pedido:** cada item guarda o preço e a pontuação do produto no momento da compra. Mudanças posteriores no produto não alteram pedidos antigos. Os itens não podem ser alterados depois da criação.
- **Dia do pedido:** as listas do dia e o dashboard usam o horário de São Paulo.
- **Paginação:** `pagina` começa em 1 (padrão 1) e `tamanho` vai de 1 a 50 (padrão 10). A resposta vem em `{ conteudo, pagina, tamanho, total, totalPaginas }`.

## Erros

As respostas de erro usam o formato `ProblemDetail`, com a propriedade `codigo` para identificar o caso sem depender do texto. Erros de validação trazem os campos em `erros`:

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

| HTTP | `codigo` | Quando |
|---|---|---|
| 400 | `DADOS_INVALIDOS` | Corpo inválido, malformado ou com valor de enum desconhecido |
| 400 | `PARAMETRO_INVALIDO` | Parâmetro da URL ausente, com tipo errado ou fora das regras (período, paginação, datas, ordenação) |
| 400 | `PRODUTO_REPETIDO` | O mesmo produto aparece mais de uma vez nos itens do pedido |
| 401 | `CREDENCIAIS_INVALIDAS` | Email ou senha inválidos, ou funcionário desativado |
| 401 | `SENHA_ATUAL_INVALIDA` | Senha atual errada na troca de senha |
| 404 | `USUARIO_NAO_ENCONTRADO`, `FUNCIONARIO_NAO_ENCONTRADO`, `CATEGORIA_NAO_ENCONTRADA`, `PRODUTO_NAO_ENCONTRADO`, `PEDIDO_NAO_ENCONTRADO` | Registro inexistente |
| 404 | `ROTA_NAO_ENCONTRADA` | Rota inexistente |
| 405 | `METODO_NAO_PERMITIDO` | Método HTTP não aceito pela rota |
| 409 | `USUARIO_CPF_EXISTENTE`, `USUARIO_TELEFONE_EXISTENTE`, `FUNCIONARIO_EMAIL_EXISTENTE`, `CATEGORIA_NOME_EXISTENTE`, `PRODUTO_NOME_EXISTENTE` | Valor que precisa ser único já está em uso |
| 409 | `PRODUTO_INDISPONIVEL` | Pedido com produto indisponível |
| 409 | `TRANSICAO_INVALIDA` | Avançar ou cancelar um pedido em status que não permite |
| 409 | `DADO_JA_CADASTRADO` | Violação de integridade no banco |

Três erros de `/usuarios` ainda saem sem `codigo` (ver [Limitações atuais](#limitações-atuais)).

## Estrutura

O código fica em `service/src/main/java/kl/club/service/`:

| Pacote | Responsabilidade |
|---|---|
| `Controller` | Rotas REST |
| `Service` | Regras de negócio |
| `Repository` | Acesso ao banco com Spring Data JPA |
| `Exception` | Exceções de negócio (`NegocioException` e subclasses) e `GlobalExceptionHandler` |
| `Model.DTO` | Contratos de entrada e saída da API |
| `Model.Entity` | Entidades JPA |
| `Model.enums` | Enums de domínio |
| `Model.Mapper` | Conversão entre DTO e entidade |
| `Config` | Configuração do Swagger e do CORS, e os exemplos de erro do Swagger (`SwaggerExemplos`) |

## Limitações atuais

- As rotas não têm autenticação. O login de clientes e funcionários só confere os dados e não gera token.
- O CORS só libera origens `http://localhost`. Um ambiente publicado precisa incluir a própria origem em `CorsConfig`.
- As senhas de funcionários são gravadas em texto puro. Não use dados reais.
- Não há integração de pagamentos: o status inicial do pedido depende só da forma de pagamento, e o cancelamento não faz estorno.
- O totem ainda não identifica o cliente: os pedidos dele vão sem `idUsuario` e não geram pontos.
- O dashboard só aceita o período `HOJE`.
- Três erros de `/usuarios` não passam pelo tratamento padrão e saem sem `codigo`: "Informe um CPF ou telefone", "Informe pelo menos um campo para atualização" e "Pedido não encontrado para este usuário".
- O único teste automatizado é o `contextLoads`.
