# Order - JAVA Spring Boot
- Projeto de exemplo de processamento de produtos por mensageria,
- com endpoints de REST API para a consulta dos dados processados,
- e um Scheduler configurado a cada minuto para processar pedidos cadastrados,
- modificando seu status e calculando valores.

## Componentes do sistema:
 - SpringBoot api (8080)
 - Kafka server   (9092)
 - Zookeeper server (2181)
 - Postgres       (5432)

## Pré-requisitos:
- Versão mais recente do Intellij IDEA
- Java 17
- Docker instalado e executando no seu sistema operacional

## Instalação:
- Com o projeto configurado no Intellij, execute as operações clean e package do maven
- Abra o terminal e na pasta do projeto, execute: docker-compose up --build
   * Este comando iniciará os containers necessários, a saber, kafka, zookeper, postgres e springBoot.

## Kafka:
- A aplicação springBoot cria ao iniciar, um tópico chamado orders
- Exemplo de mensagem válida no tópico:
  - {"id":"23234","client":{"name":"User Name","email":"user.name@email.com"},
     "produtos":[{"name":"Monitor","quantity":3,"price":925.20},{"name":"Notebook","quantity":2,"price":3000.00}]}


