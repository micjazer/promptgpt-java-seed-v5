# Prompt GPT – Application de génération et gestion de Prompts IA

## Description
Prompt GPT est une application Java/Spring Boot permettant de **créer, gérer et générer des prompts intelligents** pour différents métiers (développeurs, enseignants, juristes, médecins, RH, etc.).  
Elle intègre **OpenAI/Azure OpenAI** comme fournisseur d’IA, un **CostGuard** pour contrôler les coûts et un système de **sécurité avec rôles (USER/ADMIN)**.

---

## Architecture globale

### Schéma MVC
```
Utilisateur -> Controller -> Service -> Repository -> Base de données
```

- **Controller** : gère les requêtes HTTP (REST + UI Thymeleaf).  
- **Service** : implémente la logique métier (génération, gestion des prompts).  
- **Repository** : interaction avec PostgreSQL (relationnel) et MongoDB (NoSQL).  

### Diagramme UML – Cas d’utilisation
- Génération d’un prompt (acteur = utilisateur).  
- Gestion des modèles (acteur = administrateur).  

### Diagramme UML – Séquence (exemple)
1. L’utilisateur demande la génération d’un prompt.  
2. Le Controller appelle le Service.  
3. Le Service contacte OpenAI/Azure via WebClient.  
4. Le résultat est renvoyé en **streaming SSE** au front-end.  

### MPD (PostgreSQL + MongoDB)
- `users (id, email, password, role)`  
- `templates (id, title, category, yaml_content)`  
- `history (id, user_id, template_id, generated_text, timestamp)`  
- `promptsIndex { promptText, embedding, metadata }` *(MongoDB)*  

---

## Stack technique
- **Langage** : Java 21  
- **Framework** : Spring Boot 3.3.1  
- **Gestion de dépendances** : Gradle (Kotlin DSL)  
- **Front-end** : Thymeleaf + Tailwind CSS  
- **Bases de données** : PostgreSQL (relationnel), MongoDB (NoSQL)  
- **Sécurité** : Spring Security, JWT, BCrypt  
- **Tests** : JUnit 5, Mockito, MockMvc, JaCoCo  
- **CI/CD** : GitHub Actions  
- **Déploiement** : Docker + Docker Compose  

---

## Prérequis
Avant de lancer l’application, installer :
- [Java 21](https://adoptium.net/)  
- [Docker](https://www.docker.com/get-started/) + [Docker Compose](https://docs.docker.com/compose/)  
- [Git](https://git-scm.com/downloads)  
- IDE recommandé : [IntelliJ IDEA](https://www.jetbrains.com/idea/)  

---

## Installation et configuration

### 1. Cloner le projet
```bash
git clone https://github.com/ton-compte/prompt-gpt.git
cd prompt-gpt
```

### 2. Configurer les variables d’environnement
Créer un fichier `.env` :
```env
AI_PROVIDER=openai
OPENAI_API_KEY=sk-xxxx
AZURE_API_KEY=xxxx
AZURE_ENDPOINT=https://mon-endpoint-azure.openai.azure.com/

POSTGRES_USER=promptgpt
POSTGRES_PASSWORD=promptgpt123
POSTGRES_DB=promptgptdb

MONGO_INITDB_ROOT_USERNAME=admin
MONGO_INITDB_ROOT_PASSWORD=admin123
MONGO_DB=promptgpt
```

### 3. Lancer avec Docker
```bash
docker-compose up --build
```

---

## Utilisation

### Accès à l’interface
- Application web : [http://localhost:8080](http://localhost:8080)  
- Swagger API Docs : [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  

### Identifiants par défaut
- **Utilisateur (ROLE_USER)** : `user@promptgpt.com / user123`  
- **Administrateur (ROLE_ADMIN)** : `admin@promptgpt.com / admin123`  

### Fonctionnalités principales
- Génération de prompts en **temps réel (SSE)**  
- Gestion des templates YAML (CRUD)  
- Historique des prompts générés  
- Tableau de bord Admin avec statistiques  
- Sécurité par rôles (USER/ADMIN)  

---

## Tests

### Lancer tous les tests
```bash
./gradlew test
```

### Générer rapport JaCoCo
```bash
./gradlew jacocoTestReport
```
Rapport disponible :  
```
build/reports/jacoco/test/html/index.html
```

---

## CI/CD

### GitHub Actions
Chaque commit déclenche automatiquement :  
1. Compilation du projet.  
2. Lancement des tests (JUnit, MockMvc).  
3. Analyse de couverture (JaCoCo).  
4. Build et push de l’image Docker vers DockerHub.  

Fichier : `.github/workflows/ci.yml`

---

## Déploiement Docker

### Lancer les services
```bash
docker-compose up -d
```

### Arrêter les services
```bash
docker-compose down
```

### Vérifier les conteneurs
```bash
docker ps
```

---

## Schémas et architecture
📎 À inclure dans les annexes de ton dossier :  
- **Diagrammes UML** (cas d’utilisation, séquence).  
- **MPD** PostgreSQL + MongoDB.  
- **Architecture MVC**.  
- **Diagrammes CI/CD** (workflow GitHub Actions).  

---

## Résumé rapide (cheat sheet)
```bash
# Cloner le projet
git clone https://github.com/ton-compte/prompt-gpt.git

# Configurer l’environnement
cp .env.example .env

# Lancer l’application
docker-compose up --build

# Accéder à l’UI
http://localhost:8080

# Lancer les tests
./gradlew test

# Générer rapport JaCoCo
./gradlew jacocoTestReport
```

---

## Auteurs
- **Michel Jazeron** – Concepteur Développeur d’Applications CDA  
- Projet réalisé dans le cadre du Titre Professionnel CDA.  
