# Decisões de Projeto

- Rabbitmq foi utilizado como componente de mensageria para notificar o encerramento da sessão de votação, o mesmo foi
  escolhido pois não se entende que haja a necessidade de manter histórico destas mensagens. Além disso, ele é de fácil
  configuração e o time possui maior experiência de uso.

- Identificar via log (info) a saída de sucesso da operação principal do endpoint e em caso de falha centralizar no
  handler o erro e o stack trace. Utilização através da anotação `@Slf4j` e faz uso da implementação Logback (default do
  Spring Boot).

- O versionamento dos endpoints deve ser feito através do path, por exemplo: `/v1/pautas`. A versão é definida no path
  para facilitar a evolução da API, pois permite que versões anteriores continuem funcionando sem a necessidade de
  alterar o código.

- A constraint de banco `UK cpf` é essencial para manter a integridade dos dados e poderia por si só executar o papel de
  validação. Entretanto, em pontos onde o item de performance não seja severamente afetado se entende que trazer a
  validação para a api garante uma maior clareza sobre as regras de negócio que o processo deve respeitar, assim como
  pode facilitar o isolamento do problema encontrado. É entendido que isto não é uma verdade absoluta e deve seguir as
  orientações de cada projeto.

- O consumo de apis externas (REST) deve ser feito utilizando o Spring Cloud OpenFeign que apesar de um pouco mais
  burocrático deixa mais claro os endpoints existentes assim como seus requests e responses. Além disso, simplifica a
  utilização de outras funcionalidades de sistemas distribuídos como circuit breaker, cache, retry, etc.

- O mysql por padrão cria índices para `PK`, `FK` e `UNIQUE` sendo assim não foi necessário criar novos índices
  específicos. Entretanto, sempre que forem feitas alterações no projeto é necessário revisar e se necessário criar
  novos.
