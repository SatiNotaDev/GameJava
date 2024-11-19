# Jeu de Course 🏎️
Un jeu de course dans le navigateur avec backend Spring Boot et frontend JavaScript vanilla. Développé en 4 heures sans bibliothèques JavaScript tierces.

## Description du Projet
Le jeu est un simulateur de course avec trois types de voitures, différents niveaux de difficulté et un système de collecte de pièces.

### Caractéristiques
* 3 types de voitures avec des caractéristiques uniques
* 3 niveaux de difficulté
* Système de points et de pièces
* Sauvegarde des statistiques du joueur
* Physique de mouvement réaliste

## Stack Technique
* Backend : Java + Spring Boot
* Frontend : HTML5, CSS3, JavaScript vanilla
* Base de données : H2 (embarquée)

## Structure du Projet

### Backend
#### Entités
```java
// Vehicle.java - Classe de base pour tous les véhicules
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double vitesseBase;
    private double acceleration;
    // ...
}
```

#### Contrôleurs
```java
// GameController.java - Gestion du gameplay
@RestController
@RequestMapping("/api/jeu")
public class GameController {
    @PostMapping("/initialiser")
    public ResponseEntity<ApiResponse<Vehicle>> initialiserVehicule(
        @RequestBody Map<String, String> requete) {
        // Initialisation du véhicule
    }
}
```

### Frontend
#### Mécanique de Jeu
```javascript
// jeu.js - Logique principale du jeu
function miseAJour() {
    if (!jeuActif) return;
    
    // Mise à jour du temps
    tempsJeu -= 1 / 60;
    document.getElementById('tempsRestant').textContent = Math.ceil(tempsJeu);
    
    // Mise à jour de la position du joueur
    joueur.x += joueur.vitesse;
    if (joueur.x > LARGEUR_JEU - joueur.largeur) {
        joueur.x = LARGEUR_JEU - joueur.largeur;
    }
}
```

### Types de Voitures
1. **Voiture Standard**
```java
@Entity
public class VoitureStandard extends Vehicle {
    private double facteurStabilite = 1.2;
    
    @Override
    public void accelerer() {
        setVitesseBase(getVitesseBase() + getAcceleration() * facteurStabilite);
    }
}
```

2. **Voiture Sport**
```java
@Entity
public class VoitureSport extends Vehicle {
    private double boostSport = 1.3;
    private double reserveEnergie = 100.0;
    
    @Override
    public void accelerer() {
        if (reserveEnergie > 0) {
            setVitesseBase(getVitesseBase() + getAcceleration() * boostSport);
            reserveEnergie -= 0.3;
        }
    }
}
```

3. **Super Voiture**
```java
@Entity
public class SuperVoiture extends Vehicle {
    private double boostTurbo = 1.5;
    private double chargeNitro = 100.0;
    
    @Override
    public void accelerer() {
        if (chargeNitro > 0) {
            setVitesseBase(getVitesseBase() + getAcceleration() * boostTurbo);
            chargeNitro -= 0.5;
        }
    }
}
```

## Points d'API
### API du Jeu
* `POST /api/jeu/initialiser` - Initialisation du jeu
* `POST /api/jeu/deplacer` - Mise à jour de la position
* `GET /api/jeu/score/{idVehicule}` - Obtention du score

### API du Joueur
* `POST /api/joueur/connexion` - Connexion du joueur
* `POST /api/joueur/score` - Mise à jour du score
* `GET /api/joueur/stats/{pseudo}` - Obtention des statistiques

## Lancement du Projet
1. Cloner le dépôt
```bash
git clone https://github.com/utilisateur/jeu-course.git
```

2. Lancer le backend
```bash
./mvnw spring-boot:run
```

3. Ouvrir dans le navigateur
```
http://localhost:8080
```

## Commandes
* ↑ - Monter
* ↓ - Descendre
* Espace - Freiner
* ← - Activer le mode turbo