# KL Club Backend

API REST do KL Club, projeto do 3º semestre de ADS. Atende os frontends de cadastro de clientes, pedidos e dashboard.

O sistema cobre usuários, funcionários, produtos, categorias e pedidos com pontuação. Por enquanto, estão implementados o cadastro de usuários, a rota de funcionários e a rota de categorias.

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

1. Crie o banco `kl_club` com as tabelas do projeto. O script de criação (`script.sql`) não fica no repositório: peça a um integrante da equipe.
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

### Erros

As respostas de erro da API usam o formato `ProblemDetail`. Erros de validação trazem os campos em `erros`:

```json
{
  "title": "Bad Request",
  "status": 400,
  "detail": "Dados inválidos",
  "instance": "/funcionarios",
  "erros": { "email": "O email é inválido" }
}
```

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
- As rotas de pedidos ainda não foram implementadas.
- O único teste automatizado é o `contextLoads`.
