# Blogger Box Backend

API REST Spring Boot pour gerer des categories et des posts de blog.

## 1. Vue d'ensemble

Ce projet expose une API backend complete pour:
- creer, lire, mettre a jour et supprimer des categories
- creer, lire, mettre a jour et supprimer des posts
- filtrer les posts par date et/ou par mot-cle
- naviguer des categories vers leurs posts
- consulter la documentation OpenAPI via Swagger UI

L'application suit une architecture en couches (`controller -> service -> repository -> database`) avec gestion d'erreurs centralisee.

## 2. Stack technique

- Java 17
- Spring Boot 4.0.3
- Spring Web (REST)
- Spring Data JPA
- PostgreSQL
- springdoc OpenAPI / Swagger UI
- Maven (wrapper inclus)

## 3. Fonctionnalites cle

- CRUD complet sur `Category`
- CRUD complet sur `Post`
- Recherche categories par nom (LIKE insensible a la casse)
- Recherche posts par:
  - date (`dd-MM-yyyy`)
  - texte libre dans `title` ou `content`
  - combinaison date + texte
- Tri des posts par date de creation desc
- Validation metier avec erreurs HTTP claires
- Migration de schema automatique sur `post.content` (vers `TEXT`)
- Seeder automatique de donnees de demo au demarrage

## 4. Architecture du code

- `controllers/`: expose les endpoints HTTP
- `services/`: logique metier et validations
- `repositories/`: acces donnees JPA
- `models/`: entites persistantes
- `dtos/`: payloads d'entree
- `exceptions/`: erreurs metier + mapping HTTP global
- `config/`: CORS, migration schema, seeding

## 5. Prerequis

- Java 17 (`java -version`)
- Git
- PostgreSQL accessible (local ou cloud)
- Acces reseau vers la base de donnees

## 6. Installation et demarrage (guide detaille)

### 6.1 Cloner le projet

```bash
git clone https://github.com/dalalsml/blogger-box-backend.git
cd blogger-box-backend
```

### 6.2 Configurer la base de donnees (recommande: variables d'environnement)

Windows PowerShell:

```powershell
$env:DB_HOST="<db-host>"
$env:DB_PORT="5432"
$env:DB_NAME="postgres"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="<db-password>"
```

Mac/Linux:

```bash
export DB_HOST="<db-host>"
export DB_PORT="5432"
export DB_NAME="postgres"
export DB_USERNAME="postgres"
export DB_PASSWORD="<db-password>"
```

### 6.3 Configurer `application.properties`

Utilise preferentiellement ce format:

```properties
spring.application.name=blogger

spring.datasource.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 6.4 Compiler

Windows:

```powershell
.\mvnw.cmd -DskipTests compile
```

Mac/Linux:

```bash
./mvnw -DskipTests compile
```

### 6.5 Lancer l'application

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Mac/Linux:

```bash
./mvnw spring-boot:run
```

Par defaut: `http://localhost:8080`

### 6.6 Verifier le fonctionnement

- Home (redirige): `GET http://localhost:8080/`
- Health simple: `GET http://localhost:8080/api`
- Test hello: `GET http://localhost:8080/hello-world`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## 7. Configuration

Principales proprietes:

- `spring.datasource.url`: URL JDBC PostgreSQL
- `spring.datasource.username`: utilisateur DB
- `spring.datasource.password`: mot de passe DB
- `spring.jpa.hibernate.ddl-auto=update`: update schema auto
- `spring.jpa.show-sql=true`: log SQL

CORS autorise actuellement `http://localhost:4200` sur les routes `/api/**` et `/v1/**`.

## 8. Endpoints API

Les routes existent sous plusieurs prefixes:
- `/v1/...`
- `/api/v1/...`
- `/api/...` (pour categories et posts)

### 8.1 Hello endpoints

- `GET /` -> redirection vers Swagger UI
- `GET /api` -> `"Blogger API is running"`
- `GET /hello-world` -> `"Hello World!"`
- `GET /hello?name=Elie`
- `GET /hello/Elie`

### 8.2 Categories endpoints

Base: `/v1/categories`

- `GET /v1/categories`
- `GET /v1/categories?name=tech`
- `GET /v1/categories/{id}`
- `POST /v1/categories`
- `PUT /v1/categories/{id}`
- `PATCH /v1/categories/{id}`
- `DELETE /v1/categories/{id}`
- `GET /v1/categories/{id}/posts`

Exemple body (`POST`, `PUT`, `PATCH`):

```json
{
  "name": "Technology"
}
```

### 8.3 Posts endpoints

Base: `/v1/posts`

- `GET /v1/posts`
- `GET /v1/posts?date=17-04-2026`
- `GET /v1/posts?value=ai`
- `GET /v1/posts?date=17-04-2026&value=ai`
- `GET /v1/posts/{id}`
- `POST /v1/posts`
- `PUT /v1/posts/{id}`
- `PATCH /v1/posts/{id}`
- `DELETE /v1/posts/{id}`

Exemple `POST /v1/posts`:

```json
{
  "title": "How AI is changing daily work",
  "content": "Practical examples...",
  "categoryId": "<uuid-category>"
}
```

Exemple `PUT /v1/posts/{id}`:

```json
{
  "title": "Updated title",
  "content": "Updated content",
  "categoryId": "<uuid-category>"
}
```

Exemple `PATCH /v1/posts/{id}`:

```json
{
  "content": "Only content is updated"
}
```

## 9. Erreurs HTTP gerees

Gestion centralisee dans `GlobalDefaultExceptionHandler`:

- `400 Bad Request`
- `404 Not Found`
- `409 Conflict`
- `503 Service Unavailable` (DB indisponible)
- `500 Internal Server Error`

Format de reponse erreur:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "...",
  "timestamp": "2026-04-17T18:42:10.123"
}
```

## 10. Comportements au demarrage

- `DbSchemaMigrationRunner`: verifie la colonne `post.content` et applique `ALTER TABLE ... TYPE TEXT` si necessaire.
- `DataSeeder`: cree 12 categories et 24 posts par defaut (sans doublons) pour faciliter les tests.

## 11. Securite et bonnes pratiques

- Ne jamais versionner de vrais secrets (`username/password`) dans Git.
- Utiliser des variables d'environnement.
- Si des identifiants ont deja ete pushes, les regenerer immediatement.
- Limiter CORS en production aux domaines frontend reels.

## 12. Inventaire fichier par fichier

### Racine

- `.gitattributes`: force les fins de ligne (`mvnw` en LF, `*.cmd` en CRLF).
- `.gitignore`: ignore `target`, IDE files, wrapper jar, etc.
- `.mvn/wrapper/maven-wrapper.properties`: configuration Maven Wrapper (Maven 3.9.12).
- `mvnw`: script Maven Wrapper Unix.
- `mvnw.cmd`: script Maven Wrapper Windows.
- `pom.xml`: dependances et configuration Maven/Spring Boot.
- `README.md`: documentation principale du projet.
- `INSTALLATION.md`: guide d'installation local detaille.
- `replay_pid21400.log`: log JVM/JIT de replay (fichier volumineux de debug, non necessaire au runtime applicatif).

### Source principale (`src/main/java/com/dauphine/blogger`)

- `BloggerApplication.java`: point d'entree Spring Boot + metadonnees OpenAPI.

#### `config/`

- `CorsConfig.java`: regles CORS pour `/api/**` et `/v1/**`.
- `DbSchemaMigrationRunner.java`: migration runtime de type SQL pour `post.content`.
- `DataSeeder.java`: injection de donnees de demo (categories + posts) au demarrage.

#### `controllers/`

- `HelloWorldController.java`: endpoints hello/home/api.
- `CategoryController.java`: endpoints REST categories + posts d'une categorie.
- `PostController.java`: endpoints REST posts + filtres de recherche.

#### `dtos/`

- `CategoryRequest.java`: DTO d'entree pour creer/modifier une categorie.
- `CreatePostRequest.java`: DTO d'entree pour creation de post.
- `UpdatePostRequest.java`: DTO d'entree pour mise a jour complete de post.
- `PatchPostRequest.java`: DTO d'entree pour mise a jour partielle (content).

#### `exceptions/`

- `BadRequestException.java`: exception metier 400.
- `CategoryNotFoundByIdException.java`: exception metier 404 categorie.
- `PostNotFoundByIdException.java`: exception metier 404 post.
- `ErrorResponse.java`: structure standard de retour d'erreur.
- `GlobalDefaultExceptionHandler.java`: mapping global exceptions -> status HTTP.

#### `models/`

- `Category.java`: entite JPA `category` (UUID + name).
- `Post.java`: entite JPA `post` (UUID, title, content TEXT, createdDate, category).

#### `repositories/`

- `CategoryRepository.java`: acces JPA categories + requete de recherche nom.
- `PostRepository.java`: acces JPA posts + recherche combinee texte/date.

#### `services/`

- `CategoryService.java`: contrat metier categories.
- `CategoryServiceImpl.java`: implementation metier categories.
- `PostService.java`: contrat metier posts.
- `PostServiceImpl.java`: implementation metier posts.

### Ressources (`src/main/resources`)

- `application.properties`: configuration Spring et datasource PostgreSQL.

### Tests (`src/test/java/com/dauphine/blogger`)

- `BloggerApplicationTests.java`: test minimal de chargement du contexte Spring.

## 13. Commandes utiles

Compiler sans tests:

```bash
./mvnw -DskipTests compile
```

Lancer les tests:

```bash
./mvnw test
```

Build jar:

```bash
./mvnw clean package
```

## 14. Ameliorations recommandees

- Ajouter de vrais tests unitaires et integration (services + controllers).
- Ajouter Bean Validation (`@Valid`, `@NotBlank`, etc.) sur DTOs.
- Ajouter migration versionnee (Flyway/Liquibase) au lieu d'une migration ad hoc.
- Externaliser entierement la config sensible (profils, variables, secret manager).
