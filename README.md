Documentação do projeto:

Essa documentação contém alguns passos relevantes para rodar o projeto e algumas observações sobre o mesmo.

1 - O Projeto foi feito em Java, SpringBoot e foi utilizado o H2 como banco de dados, o motivo de se usar o H2 foi por que é um banco de dados mais simples que não requer uma configuração complexa, o que é importante devido ao tempo curto que eu tenho para entregar o projeto.

2 - O Projeto é exclusivamente backend, o motivo foi novamente a falta de tempo para fazer uma integração com qualidade para o React.

3 - Para rodar o projeto, vá até a pasta do mesmo e digite o seguinte comando: mvn-spring-boot:run, em seguida deverá aparecer no terminal a seguinte mensagem: Started VotacaoApplication in 3.991 seconds (JVM running for 4.32)
3.1 - Se aparecer essa mensagem significa que o projeto foi compilado com sucesso, para ver se o projeto está rodando digite o seguinte comando no navegador: http://localhost:8080/pautas/teste, se aparecer a mensagem que o servidor está rodando significa que o projeto está de pé:
3.2 - A partir daí você poderá navegar pelos endpoints do arquivo PautaController
3.3 - Para registrar um voto válido: http://localhost:8080/pautas/1/votar?cpf=47960169888, para registrar um voto inválido: http://localhost:8080/pautas/1/votar?cpf=00000000000, ambos rodam a partir do POST
3.4 - Ao testar a API mencionada ela acabou retornando 404 o que mostra que está fora, nisso foi criado um mock no arquivo MockController contendo 5 cpfs, sendo alguns válidos e outros inválidos que podem ser usados para testar se o endpoint funcionou, exemplos: http://localhost:8080/users/11111111111 - CPF Válido, http://localhost:8080/users/00000000000 - CPF Inválido

4 - Não foi implementado um sistema de mensageria devido a complexidade do mesmo, o que somado a falta de tempo e a vontade de implementar um sistema de qualidade como o Kafka não me permitiu continuar com um sistema de mensageria.

5 - Foram adicionados testes de performance no arquivo PautaControllerPerformanceTest, para rodar o teste vá no terminal na pasta do projeto e digite: mvn test
5.1 - Explicando o teste: foi criada uma pauta aberta antes de cada teste com 10 minutos de duração, em seguida foi criado 100000 votos simulados com ids únicos e todas as respostas devem retornar o status HTTP 200.

6 - Sobre a tarefa 4: Eu versionaria a API da minha aplicação por meio de branches, pois é o meio que estou mais acostumado e que acho melhor em uma equipe de desenvolvedores, com novas features tendo a tag feature(feature/nome-da-feature por exemplo), release para preparar versões e hotfix para correções urgentes.
6.1 - Em projetos open-source sou a favor do versionamento semântico, usando como exemplo um projeto que esteja na versão(1.0.0), usar números quebrados para correções e novas funcionalidades menores(1.0.1) por exemplo e usar um número a mais em correções mais urgentes e novas funcionalidades mais importantes(2.0.0) por exemplo.
