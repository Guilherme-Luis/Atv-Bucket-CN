# Cardápio do Restaurante — Object Store (Google Cloud Storage)

Projeto da disciplina de Computação em Nuvem 2 (FATEC). Tema: **restaurante**.

A aplicação cadastra pratos (nome, descrição e preço), permite enviar várias fotos para cada prato e escolher qual delas é a principal (capa). Os dados ficam no MongoDB e somente os arquivos de imagem ficam no bucket do grupo no Google Cloud Storage.

| Item | Valor |
| --- | --- |
| Bucket | `fatec-2026-grupo-j` (leitura pública) |
| Service Account | `sa-grupo-j@serjava-demo.iam.gserviceaccount.com` |
| Banco de dados | MongoDB (coleção `pratos`) |
| Backend | Java 21, Spring Boot 4, Google Cloud Storage Client |
| Frontend | React 19 + Vite |

## Estrutura

```
.
├── backend/                         API REST em Java (Spring Boot)
│   ├── pom.xml
│   ├── .env.example
│   └── src/main/java/restaurante/
│       ├── config/                  Storage do GCP, CORS, auditoria do Mongo
│       ├── controller/              Endpoints de pratos e imagens
│       ├── dto/                     Objetos de entrada e saída da API
│       ├── exception/               Exceções e tratamento global de erros
│       ├── model/                   Documentos Prato e Imagem
│       ├── repository/              Acesso ao MongoDB
│       └── service/                 Regras de negócio e integração com o bucket
└── frontend/                        Interface web (React)
    └── src/
        ├── pages/                   Lista, Cadastro e Detalhe do prato
        ├── components/              Card, galeria, seletor de imagens, etc.
        ├── services/                Chamadas à API
        ├── utils/                   Formatação de preço, data e tamanho
        └── styles/                  Estilos globais
```

## Modelo de dados

Cada prato é um documento da coleção `pratos`. As imagens ficam embutidas no próprio documento, guardando a referência do objeto no bucket, a URL pública, a data de envio e se é a imagem principal.

```json
{
  "_id": "6ad361b32dc179d1c22792c4",
  "nome": "Pessoni Dog Quente",
  "descricao": "Pão de leite macio, salsicha artesanal grelhada...",
  "preco": 24.90,
  "imagens": [
    {
      "id": "f1c0...",
      "objeto": "pratos/6ad361b32dc179d1c22792c4/2d5e2263-....jpg",
      "url": "https://storage.googleapis.com/fatec-2026-grupo-j/pratos/6ad3.../2d5e2263-....jpg",
      "nomeOriginal": "Hot_dog_01.jpg",
      "contentType": "image/jpeg",
      "tamanho": 91872,
      "principal": true,
      "dataUpload": "2026-10-17T11:53:20Z"
    }
  ],
  "criadoEm": "2026-10-17T11:53:15Z",
  "atualizadoEm": "2026-10-17T11:53:28Z"
}
```

Os arquivos são gravados no bucket no caminho `pratos/{idDoPrato}/{uuid}.{extensao}`.

## Como executar

### Pré-requisitos

- JDK 21 ou superior (a variável `JAVA_HOME` deve apontar para ele)
- Node.js 20.19+ ou 22.12+
- Dados da chave da Service Account do grupo (`sa-grupo-j-key.json`), que vão para o `.env`

### Backend

```bash
cd backend
cp .env.example .env
```

Preencha o `.env`:

| Variável | Descrição |
| --- | --- |
| `DB_URL` | String de conexão do MongoDB |
| `GCP_PROJECT_ID` | Projeto do GCP (`serjava-demo`) |
| `GCP_BUCKET_NAME` | Bucket do grupo (`fatec-2026-grupo-j`) |
| `GCP_CLIENT_ID` | Campo `client_id` da chave JSON da Service Account |
| `GCP_CLIENT_EMAIL` | Campo `client_email` da chave JSON |
| `GCP_PRIVATE_KEY_ID` | Campo `private_key_id` da chave JSON |
| `GCP_PRIVATE_KEY` | Campo `private_key` da chave JSON, em uma única linha com as quebras escritas como `\n` |
| `CORS_ORIGENS` | Endereço do frontend (padrão `http://localhost:5173`) |

```bash
.\mvnw.cmd spring-boot:run
```

A API sobe em `http://localhost:8080`.

### Frontend

```bash
cd frontend
cp .env.example .env
npm install
npm run dev
```

A interface fica disponível em `http://localhost:5173`.

## Endpoints

| Método | Rota | Descrição |
| --- | --- | --- |
| `GET` | `/api/pratos?busca=` | Lista os pratos com a URL da imagem principal |
| `GET` | `/api/pratos/{id}` | Detalhe do prato com todas as imagens |
| `POST` | `/api/pratos` | Cadastra um prato |
| `PUT` | `/api/pratos/{id}` | Altera os dados do prato |
| `DELETE` | `/api/pratos/{id}` | Exclui o prato e suas imagens do bucket |
| `POST` | `/api/pratos/{id}/imagens?principal=` | Envia uma ou mais imagens (`multipart/form-data`, campo `arquivos`) |
| `PATCH` | `/api/pratos/{id}/imagens/{imagemId}/principal` | Define a imagem principal |
| `DELETE` | `/api/pratos/{id}/imagens/{imagemId}` | Exclui a imagem do bucket e do banco |
| `GET` | `/api/pratos/{id}/imagens/{imagemId}/download` | Baixa a imagem do bucket pela aplicação |
| `GET` | `/api/pratos/{id}/imagens/bucket` | Lista os objetos do prato no bucket |

## Requisitos atendidos

| Requisito | Onde |
| --- | --- |
| Cadastro da entidade (nome, descrição, preço) | Tela **Cadastrar prato** e `POST /api/pratos` |
| Upload de n imagens com marcação da principal | Seletor de fotos no cadastro e na tela de detalhe, `ImagemService` |
| Listagem com miniatura da imagem principal pela URL pública | Tela **Cardápio** (lista) |
| Detalhe com a principal em destaque e galeria | Tela de detalhe do prato |
| Banco local para os dados, bucket apenas para as imagens | MongoDB (`Prato`/`Imagem`) e `BucketService` |
| Uso exclusivo do bucket e da chave da SA | `GcpStorageConfig`, com a credencial da SA montada a partir do `.env` |
| Upload, listagem de objetos e download feitos pela aplicação | `BucketService.enviar`, `listar` e `baixar` |
| Leitura pública pelas URLs `storage.googleapis.com` | Imagens exibidas diretamente pela URL pública |
| Pelo menos 10 registros com 3 imagens cada | 10 pratos cadastrados no MongoDB, com 30 imagens no bucket |
| Exclusão de imagens | Botão **Excluir** na galeria e `DELETE /api/pratos/{id}/imagens/{imagemId}` |

## Testes

```bash
cd backend
./mvnw test
```

## Créditos das imagens

As fotos dos pratos cadastrados são do [Wikimedia Commons](https://commons.wikimedia.org/) e pertencem aos seus respectivos autores, sob as licenças indicadas em cada arquivo.
