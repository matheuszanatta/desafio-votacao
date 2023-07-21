# Decisões de Projeto

- Rabbitmq foi utilizado como componente de mensageria para notificar o encerramento da sessão de votação, o mesmo foi
  escolhido pois não se entende que haja a necessidade de manter histórico destas mensagens. Além disso, ele é de fácil
  configuração e o time possuí maior experiência de uso.

- Identificar via log (info) a saída de sucesso da operação principal do endpoint e em caso de falha centralizar no
  handler o erro e o stack trace. Utilização através da anotação `@Slf4j` e faz uso da implementação Logback (default do
  Spring Boot).

- O versionamento dos endpoints é feito através do path, por exemplo: `/v1/pautas`. A versão é definida no path
  para facilitar a evolução da API, pois permite que versões anteriores continuem funcionando sem a necessidade de
  alterar o código.
