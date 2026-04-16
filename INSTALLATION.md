# Guide d'installation

Ce guide explique comment installer et lancer le backend **Blogger Box** en local.

## 1) Pré-requis

- Java 17 (`java -version`)
- Maven Wrapper (déjà inclus: `mvnw`, `mvnw.cmd`)
- Accès à une base PostgreSQL (Supabase ou locale)
- Git

## 2) Cloner le projet

```bash
git clone https://github.com/dalalsml/blogger-box-backend.git
cd blogger-box-backend
```

## 3) Configurer la base de données

### Option recommandée: variables d'environnement

#### Windows PowerShell

```powershell
$env:DB_HOST="<your-db-host>"
$env:DB_PORT="5432"
$env:DB_NAME="postgres"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="<your-password>"
```

#### Mac/Linux

```bash
export DB_HOST="<your-db-host>"
export DB_PORT="5432"
export DB_NAME="postgres"
export DB_USERNAME="postgres"
export DB_PASSWORD="<your-password>"
```

### application.properties conseillé

Place ce contenu dans `src/main/resources/application.properties`:

```properties
spring.application.name=blogger

spring.datasource.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

> Si ton mot de passe contient des caractères spéciaux, garde-le tel quel ici (pas en URL JDBC brute dans le code).

## 4) Installer les dépendances et compiler

```bash
# Windows
.\mvnw.cmd -DskipTests compile

# Mac/Linux
./mvnw -DskipTests compile
```

## 5) Lancer l'application

```bash
# Windows
.\mvnw.cmd spring-boot:run

# Mac/Linux
./mvnw spring-boot:run
```

Par défaut, l’application démarre sur `http://localhost:8080`.

## 6) Vérifier que tout fonctionne

- `GET http://localhost:8080/hello-world`
- Swagger: `http://localhost:8080/swagger-ui/index.html`

## 7) Endpoints utiles pour test rapide

### Categories

- `GET /v1/categories`
- `GET /v1/categories?name=Child`
- `POST /v1/categories`

Body JSON:

```json
{
  "name": "Adoption"
}
```

### Posts

- `GET /v1/posts`
- `GET /v1/posts?value=adoption`
- `GET /v1/posts?date=16-04-2026`
- `POST /v1/posts`

Body JSON:

```json
{
  "title": "Mon post",
  "content": "Contenu du post",
  "categoryId": "<uuid-category>"
}
```

## 8) Dépannage

### Erreur Hibernate / Dialect / JDBC metadata

Vérifie que les variables DB sont bien définies et que l’URL datasource est correcte.

### Erreur Supabase projet "paused"

Le projet Supabase doit être relancé depuis le dashboard avant connexion.

### `non-fast-forward` au push Git

Ton local est en retard sur le remote. Fais:

```bash
git pull --rebase origin main
git push origin main
```

### `index.lock: Permission denied`

Ferme les processus Git/IDE qui verrouillent le repo puis réessaie.

## 9) Sécurité

- Ne jamais commit de mot de passe réel.
- Si un secret a été exposé, le régénérer immédiatement.
- Ajouter `.env` dans `.gitignore` si utilisé.
