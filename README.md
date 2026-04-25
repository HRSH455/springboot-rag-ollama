# Spring AI RAG with Ollama

A Spring Boot application implementing Retrieval-Augmented Generation (RAG) using Spring AI, Ollama, and PostgreSQL with PGVector for document embeddings and similarity search.

## Features

- **Document Ingestion**: Automatically ingests PDF documents on startup
- **Vector Search**: Uses PGVector for efficient similarity search over document embeddings
- **Chat API**: RESTful API for conversational AI powered by Ollama models
- **Ollama Integration**: Supports multiple Ollama models (Mistral, Llama, etc.)
- **Docker Compose**: Easy setup with containerized PostgreSQL and Ollama services

## Snaps

<img width="1901" height="916" alt="Screenshot 2026-04-19 215236" src="https://github.com/user-attachments/assets/de5ca409-c545-458d-bd9a-1e0470a58594" />



<img width="1886" height="904" alt="Screenshot 2026-04-19 215251" src="https://github.com/user-attachments/assets/e1f10b95-d885-4bd8-922b-b7528648215e" />



<img width="1909" height="910" alt="Screenshot 2026-04-19 215533" src="https://github.com/user-attachments/assets/35d085c7-803a-4dab-9252-3f95ece420f1" />


## Prerequisites

- Java 21 or higher
- Maven 3.6+
- Docker and Docker Compose
- Ollama (for local model serving)

## Quick Start

### 1. Clone the Repository

```bash
git clone <repository-url>
cd springboot-rag-ollama
```

### 2. Start Infrastructure Services

```bash
docker compose up -d
```

This starts:
- PostgreSQL with PGVector extension on port 5432
- Ollama service

### 3. Pull Required Ollama Models

```bash
# Pull the embedding model
ollama pull nomic-embed-text

# Pull the chat model
ollama pull mistral
```

### 4. Build and Run the Application

```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 5. Access the Web Interface

Open `http://localhost:8080` in your browser to use the chat interface.

## Configuration

### Application Properties

Key configuration in `src/main/resources/application.properties`:

```properties
# Ollama Configuration
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.init.pull-model-strategy=when_missing

# Embedding Model
spring.ai.ollama.embedding.options.model=nomic-embed-text

# Chat Model
spring.ai.ollama.chat.options.model=mistral

# Vector Store Configuration
spring.ai.vectorstore.pgvector.initialize-schema=true
spring.ai.vectorstore.pgvector.dimensions=768
```

### Environment Variables

For production deployment, consider using environment variables:

- `SPRING_AI_OLLAMA_BASE_URL`: Ollama server URL
- `SPRING_DATASOURCE_URL`: PostgreSQL connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password

## API Usage

### Chat Endpoint

Send POST requests to `/api/chat` with a JSON body containing your message:

```bash
curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d '"What is Spring Boot?"'
```

Response:
```json
"Spring Boot is a framework that simplifies the development of Java applications..."
```

## Project Structure

```
src/
├── main/
│   ├── java/com/work/springai/rag/
│   │   ├── SpringAiRagApplication.java          # Main application class
│   │   ├── ChatController.java                  # REST API for chat
│   │   └── ingestion/
│   │       └── DocumentIngestionService.java    # PDF document ingestion
│   └── resources/
│       ├── application.properties               # Application configuration
│       ├── static/index.html                    # Web interface
│       └── pdf/                                 # PDF documents directory
└── test/
    ├── java/com/work/springai/rag/
    │   ├── SpringAiRagApplicationTests.java     # Unit tests
    │   ├── TestSpringAiRagApplication.java      # Test application
    │   └── TestcontainersConfiguration.java     # Test containers config
    └── resources/
        └── application-test.properties          # Test configuration
```

## Testing

### Unit Tests

Run unit tests:

```bash
mvn test
```

### Integration Tests

The project uses Testcontainers for integration testing. Ensure Docker is running:

```bash
mvn verify
```

Note: Integration tests require Docker to be available for Testcontainers.

## Development

### Adding New Documents

Place PDF files in `src/main/resources/pdf/` directory. The application will automatically ingest them on startup.

### Customizing Models

Update the model names in `application.properties`:

```properties
spring.ai.ollama.embedding.options.model=your-embedding-model
spring.ai.ollama.chat.options.model=your-chat-model
```

### Database Schema

The application automatically creates the required PGVector tables on startup. To manually manage the schema:

```sql
CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS hstore;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE vector_store (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    content text,
    metadata json,
    embedding vector(768)
);

CREATE INDEX ON vector_store USING HNSW (embedding vector_cosine_ops);
```

## Troubleshooting

### Common Issues

1. **Docker Compose Fails**: Ensure Docker Desktop is running and has sufficient resources.

2. **Ollama Connection Error**: Verify Ollama is running on `http://localhost:11434` and required models are pulled.

3. **Database Connection Error**: Check PostgreSQL is running and credentials are correct.

4. **Compilation Errors**: Ensure Java 21+ and Maven 3.6+ are installed.
   
5. **Slow Api Response**: Since Ollama is being run locally , It is adivised to use models with less paramaters(~80m) if on a low end device.Newer vesions of           Ollama allows cloud options.

### Logs

Check application logs for detailed error information:

```bash
mvn spring-boot:run
```

Or run with debug logging:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--debug"
```


## Technologies Used

- **Spring Boot 3.4.3**: Framework for building the application
- **Spring AI 1.1.2**: AI integration framework
- **Ollama**: Local LLM serving
- **PostgreSQL + PGVector**: Vector database for embeddings
- **Docker Compose**: Container orchestration
- **Testcontainers**: Integration testing
- **Maven**: Build tool
