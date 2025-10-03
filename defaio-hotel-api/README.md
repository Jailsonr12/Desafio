# Projeto minimalista da API do hotel

## Descrição
- API simples para gerenciar hóspedes e check-ins.

## Pré-requisitos
- Docker (para o Postgres)
- Java 21
- Maven (embutido via wrapper mvnw.cmd)

## Banco de dados (Docker Compose)
- O Postgres é executado via `docker-compose.yml` que, no seu workspace, está na pasta pai (por exemplo: `C:\Users\Jailson Roth\Desktop\Desafio\docker-compose.yml`).
- Para subir o serviço (Windows), abra o CMD na pasta que contém o `docker-compose.yml` e execute:

```cmd
cd /d "C:\Users\Jailson Roth\Desktop\Desafio"
docker-compose up -d
```

## Rodando a aplicação (Windows)
- A partir da raiz do projeto (`c:\Users\Jailson Roth\Desktop\Desafio\defaio-hotel-api`):

```cmd
cd /d "c:\Users\Jailson Roth\Desktop\Desafio\defaio-hotel-api"
mvnw.cmd spring-boot:run
```

## Comandos úteis (Windows)
- Rodar testes: `mvnw.cmd test`
- Gerar pacote: `mvnw.cmd package`

## Config
- Configurações do DB em `src/main/resources/application.properties` (usuário: hotel, senha: hotel, db: hotel).
- `src/main/resources/data.sql` contém inserts para popular a base (3 hóspedes e 3 check-ins).

## Postman
- Coleção pronta em `defaio-hotel.postman_collection.json` na raiz do projeto — importe no Postman e ajuste a variável `base_url` se necessário.

## Notas rápidas
- Endpoints expostos conforme controllers em `src/main/java/com/jailson/hotel/controller`.
- README propositalmente curto — se quiser, adiciono exemplos de chamadas HTTP detalhadas.