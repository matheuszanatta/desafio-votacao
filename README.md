# Desafio Votação

## Descrição

No cooperativismo, cada associado possui um voto e as decisões são tomadas em pautas, por votação.
Com esse princípio, foi criada esta solução que gerencia as pautas.

Mais detalhes podem ser encontrados documento do desafio: [PDF](resources/desafio.pdf)

## Funcionalidades

- Criação de pautas para votação.
- Criação de sessões em pautas.
- Criação de associados.
- Registro de voto do associado em pauta.
- Verificação do resultado da votação em uma pauta.
- Notificação de sessão encerrada via mensageria.

## Pré-requisitos

- `Docker` e `Docker Compose` instalados para subir os containers de banco de dados e mensageria.
- JDK mínima na versão 17 para execução do projeto.
- [Recomendado] Postman para acessar as funcionalidades.

## Execução do Projeto

- Clone o repositório do projeto em sua máquina local.
- Navegue até o diretório raiz do projeto.
- Execute o comando `docker-compose up -d` para subir os containers necessários.
- Execute o projeto.

Abaixo o exemplo dos comandos citados, que devem ser executes no terminal:

```ssh
git clone git@github.com:matheuszanatta/desafio-votacao.git
cd teste
docker-compose up -d
./mvnw spring-boot:run
```

## Acesso as funcionalidades

O projeto expõe as funcionalidades através de endpoints rest na url `http://localhost:8080` que podem ser facilmente
acessados utilizando ferramentas como o Postman.

Você pode acessar o diretório `resources/postman`, onde encontrará uma **collection** e um **environment** que podem ser
importados no Postman. Eles contêm exemplos de requisições para facilitar o uso da API.

![postman.png](resources/screenshots/postman.png)

## Documentação da API

Para visualizar e interagir a documentação da API, você pode acessar o Swagger UI através do navegador na
url `http://localhost:8080/swagger-ui.html`.

## Testes

- Para executar os testes basta acessar o diretório raiz do projeto e executar o comando `./mvnw test`. Os resultados
  serão exibidos no console.

## Screenshots da Aplicação

### Resultado dos testes (unitários e integração)

![img.png](resources/screenshots/resultado-testes.png)

### Resultado Sonar

![img.png](resources/screenshots/resultado-sonar.png)

### Swagger UI

![img.png](resources/screenshots/diagrama-swagger.png)

### Diagrama ER

![img.png](resources/screenshots/diagrama-ER.png)

### Setup Adminer (client database)

![img.png](resources/screenshots/adminer-login.png)

## Autores

- [Matheus Zanatta](https://github.com/matheuszanatta)

## Contato

Para entrar em contato com a equipe do projeto, envie um e-mail
para `matheus.zanatta.nh@gmail.com` ou abra uma issue no
repositório do GitHub.
