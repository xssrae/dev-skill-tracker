# Dev Skill Tracker

Projeto Java com microsservicos e mensageria Kafka para rastrear tecnologias estudadas e projetos concluidos com progresso visual.

## Arquitetura

- `producer-service`: webhook WebFlux que recebe eventos de exercicios/projetos concluidos e publica no Kafka.
- `consumer-service`: consome eventos, persiste o log bruto no PostgreSQL e atualiza uma visao agregada para dashboard.
- `shared-events`: contratos de eventos compartilhados entre os servicos.
- Stack local: Spring Boot, Kafka, PostgreSQL, R2DBC, Flyway e Docker Compose.
- CI/CD: GitHub Actions com build, testes, push Docker para ECR e deploy ECS.

## Rodando localmente

1. Suba Kafka e PostgreSQL:

```bash
docker compose up -d
```

2. Compile os servicos:

```bash
mvn clean verify
```

3. Rode o producer:

```bash
mvn -pl producer-service spring-boot:run
```

4. Em outro terminal, rode o consumer:

```bash
mvn -pl consumer-service spring-boot:run
```

## Enviando um evento

```bash
curl -X POST http://localhost:8081/webhooks/completions \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "fonte",
    "repository": "dev-skill-tracker",
    "projectName": "Spring Kafka Basics",
    "type": "PROJECT_COMPLETED",
    "technologies": ["Java", "Spring Boot", "Kafka"],
    "progressDelta": 20
  }'
```

## Consultando o dashboard

```bash
curl http://localhost:8082/dashboard/fonte/progress
```

## Event sourcing basico

O consumer salva cada mensagem em `skill_events` antes de atualizar a tabela `technology_progress`. Isso mantem um historico auditavel e permite recriar a visao agregada se a regra de progresso mudar.

## AWS Free Tier

Os arquivos em `infra/ecs` sao modelos iniciais para ECS Fargate. Antes do deploy, substitua `<account-id>`, endpoints de Kafka/RDS e crie os repositorios ECR, cluster ECS, servicos, security groups e parametro SSM para a senha do PostgreSQL.
