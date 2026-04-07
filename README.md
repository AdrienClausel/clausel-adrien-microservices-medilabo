# Medilabo Solutions
Plateforme de dépistage des risques de maladies pour cliniques de santé et cabinets privés.

# Présentation
Medilabo est une application web développée pour une société internationale
travaillant avec des cliniques de santé et des cabinets privés sur le dépistage des risques de maladies.

Elle permet de gérer les dossiers patients et les notes médicales associées, dans une architecture microservices moderne.

# Architecture

Navigateur => Front => gateway  => patient-service
                                => patient-notes-service
                                => patient-diabetes-risk-service

# Microservices
Front (Interface utilisateur Thymeleaf)
Gateway (Point d'entrée, routage)
patient-service (Gestion des patients)
patient-db (base de données patients)
patient-notes-service (Notes médicales sur les patients)
patient-notes-db (Base de données notes)
patient-diabetes-risk-service (Calcul du risque de diabète du patient)

# Variables d'environnement
Fichier .env

# Compiler les microservices
Depuis le dossier racine lancer la commande:
mvn clean package

# Lancer l'application
docker compose up --build

# Utilisation
Une fois les services démarrés, accédez à l'application depuis l'adresse suivante:
http://localhost:8081

# Identifiants de connexion
Utilisateur : user
Mot de passe : 1234

# Fonctionnalités
Consulter la liste des patients
Ajouter un patient
Modifier un patient
Consulter le détail d'un patient avec ses notes médicales
Ajouter une note médicale pour un patient

# Sécurité
L'authentification est gérée par le microservice front à l'aide de Spring Security
LA gateway vérifie la présence du header X-User-Name sur chaque requête vers les services back
Les services back sont accessibles uniquement via la gateway.