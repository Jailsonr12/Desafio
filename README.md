# Defaio Hotel API

API REST para gestão de **hóspedes** e **check-ins** de hotel, com cálculo de valores de hospedagem
(diárias, garagem e diária extra por saída após 16:30).

-   **Stack:** Java 21, Spring Boot 3, Spring Data JPA (Hibernate), PostgreSQL.
-   **Docs interativas (Swagger):** `http://localhost:8080/swagger-ui/index.html`

---

## Requisitos

-   **Java 21**
-   **Maven Wrapper** (já incluso no projeto)
-   **Docker** (opcional, para subir o PostgreSQL rapidamente)

---

## Subindo o banco (PostgreSQL) com Docker

Crie um arquivo `docker-compose.yml` (na raiz do projeto) com o conteúdo abaixo e execute:

```yaml
version: "3.9"
services:
    db:
        image: postgres:16
        container_name: hotel_db
        environment:
            POSTGRES_DB: hotel
            POSTGRES_USER: hotel
            POSTGRES_PASSWORD: hotel
        ports:
            - "5432:5432"
        volumes:
            - pgdata:/var/lib/postgresql/data
volumes:
    pgdata: {}
```

Suba o banco:

```bash
docker compose up -d
```

> **Anote as credenciais** (DB=hotel / USER=hotel / PASS=hotel) e ajuste seu `application.properties`/`application.yml` conforme necessário.

---

## Rodando a aplicação

Na raiz do projeto:

```bash
# Linux/macOS
./mvnw spring-boot:run

# Windows (CMD/PowerShell)
mvnw spring-boot:run
```

A aplicação sobe por padrão em `http://localhost:8080`.

-   **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`
-   **OpenAPI (JSON):** `http://localhost:8080/v3/api-docs`

---

## Regras de negócio (cálculo de valores)

-   **Diária em dias úteis (seg–sex):** R$ 120,00
-   **Diária em finais de semana (sáb–dom):** R$ 150,00
-   **Garagem (acréscimo por diária):** R$ 15,00 (seg–sex) e R$ 20,00 (sáb–dom)
-   **Saída após 16:30:** cobra **uma diária extra** do dia da saída
-   Em **/checkin/open** e **/checkin/closed**, cada hóspede vem com:
    -   `valorTotal`: soma de todas as hospedagens do hóspede
    -   `valorUltimaHospedagem`: valor calculado do **check-in mais recente**
        (se estiver **aberto**, é calculado até **agora**).

---

## Endpoints

### Hóspede (`/hospede`)

-   **POST** `/hospede/create` — cria hóspede  
    **Body (JSON):**

    ```json
    {
        "nome": "Ana Souza",
        "documento": "987654321001",
        "telefone": "1111-2222"
    }
    ```

    > `documento` deve ter **12 dígitos numéricos** (validação no serviço).

-   **GET** `/hospede/read/{id}` — busca por id

-   **PUT** `/hospede/update/{id}` — atualiza hóspede  
    **Body (JSON):**

    ```json
    {
        "nome": "Ana Souza",
        "documento": "987654321001",
        "telefone": "1111-2222"
    }
    ```

-   **DELETE** `/hospede/delete/{id}` — remove hóspede

-   **GET** `/hospede/list` — lista todos

---

### Check-in (`/checkin`)

-   **POST** `/checkin/create` — cria check-in  
    **Body (JSON):**

    ```json
    {
        "hospede": { "id": 1 },
        "dataEntrada": "2025-10-03T08:00:00",
        "dataSaida": "2025-10-04T11:00:00",
        "adicionalVeiculo": true
    }
    ```

    Observações:

    -   `hospede.id` deve existir.
    -   `dataSaida` é **opcional** (check-in **aberto**). Se vier, **não pode** ser anterior a `dataEntrada`.

-   **GET** `/checkin/read/{id}` — busca check-in por id (traz dados do hóspede)

-   **PUT** `/checkin/update/{id}` — atualiza datas e veículo do check-in  
    **Body (JSON):** (não precisa enviar `hospede`)

    ```json
    {
        "dataEntrada": "2025-10-03T13:00:00",
        "dataSaida": "2025-10-05T10:00:00",
        "adicionalVeiculo": false
    }
    ```

-   **DELETE** `/checkin/delete/{id}` — remove check-in

-   **GET** `/checkin/list` — lista todos os check-ins (com hóspede)

-   **GET** `/checkin/open` — **hóspedes atualmente no hotel**

    -   Uma **linha por hóspede** (o check-in **aberto** mais recente)
    -   Inclui `valorTotal` e `valorUltimaHospedagem`

-   **GET** `/checkin/closed` — **hóspedes que já saíram**

    -   Uma **linha por hóspede** (último check-in **fechado**)
    -   Exclui hóspedes que ainda possuem check-in aberto
    -   Inclui `valorTotal` e `valorUltimaHospedagem`

-   **GET** `/checkin/search?term=...` — busca **check-ins** por termo no hóspede
    -   O termo é aplicado em **nome**, **documento** ou **telefone** do hóspede
    -   Retorna **os check-ins** (cada linha é um check-in encontrado do hóspede correspondente)

---

## Exemplos de cURL

> **Windows (CMD)**: lembre-se de escapar aspas com `\"`.

### Criar hóspede

```bash
curl -X POST http://localhost:8080/hospede/create ^
  -H "Content-Type: application/json" ^
  -d "{\"nome\":\"Ana Souza\",\"documento\":\"987654321001\",\"telefone\":\"1111-2222\"}"
```

### Criar check-in (hóspede id=1, sem dataSaida = aberto)

```bash
curl -X POST http://localhost:8080/checkin/create ^
  -H "Content-Type: application/json" ^
  -d "{\"hospede\":{\"id\":1},\"dataEntrada\":\"2025-10-03T08:00:00\",\"adicionalVeiculo\":true}"
```

### Atualizar check-in (id=10)

```bash
curl -X PUT http://localhost:8080/checkin/update/10 ^
  -H "Content-Type: application/json" ^
  -d "{\"dataEntrada\":\"2025-10-03T13:00:00\",\"dataSaida\":\"2025-10-05T10:00:00\",\"adicionalVeiculo\":false}"
```

### Listar abertos / fechados

```bash
curl http://localhost:8080/checkin/open
curl http://localhost:8080/checkin/closed
```

### Buscar check-ins pelo termo (nome/doc/telefone do hóspede)

```bash
curl "http://localhost:8080/checkin/search?term=Ana"
```

---

## Notas

-   Datas estão no padrão **ISO-8601 sem timezone** (`yyyy-MM-dd'T'HH:mm:ss`), ex.: `2025-10-03T08:00:00`
-   O cálculo de valores considera **cada dia** entre entrada e saída (ou “agora”, se aberto), aplicando tarifas
    de **dia útil/fim de semana**, **acréscimo de garagem** e **diária extra** quando a saída é após **16:30**.
-   O endpoint `/checkin/open` e `/checkin/closed` já retornam os campos `valorTotal` e `valorUltimaHospedagem`.
-   Caso use outro banco/credencial, ajuste suas propriedades de datasource.

Bom uso! :)
