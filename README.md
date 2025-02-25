# **Product Management API** 🚀

Ce projet est un back-end RESTful développé avec **Java Spring Boot**, permettant la gestion de produits avec **authentification JWT**, **gestion des paniers** et **listes d’envie**.

## **📌 Prérequis**

- **JDK 11+**
- **Maven**
- **IntelliJ IDEA /** (recommandé)
- **Postman** (pour tester l'API manuellement)

## **⚙️ Installation & Exécution**

1. **Cloner le projet**

   ```bash
   git clone https://github.com/ton-repo.git
   cd ton-repo
   ```

2. **Configurer l’application**   Vérifie le fichier **`application.yml`** pour la configuration de la **base de données H2** et **Swagger**.

 
## **🔗 Accéder à Swagger UI**

L’API est documentée avec **Swagger**, disponible à l’URL suivante :👉 **[Swagger UI](http://localhost:8080/swagger-ui.html)**

📌 **Endpoints utiles :**

- `POST /account` → Création de compte
- `POST /token` → Connexion & génération du token JWT
- `GET /products` → Récupération des produits
- `PATCH /products/{id}` → Modification d’un produit (Admin uniquement)

---

## **📚 Accès à la base de données H2**

Une base de données **H2 en mémoire** est utilisée pour le développement et les tests.

👉 **Accès à la console H2 :****[H2 Database Console](http://localhost:8080/h2-console)**

📌 **Configuration de connexion :**

- **JDBC URL** : `jdbc:h2:mem:productdb`
- **User** : `sa`
- **Password** : *(laisser vide si non défini)*

Si l’accès est refusé, vérifie que `h2-console` est activé dans `application.yml` :

 

---

## **🔒 Authentification & Sécurité JWT**

L'API est sécurisée avec **JSON Web Tokens (JWT)**.

### **1⃣ Créer un compte utilisateur**

**Endpoint** : `POST /account`

```json
{
  "username": "user1",
  "firstname": "John",
  "email": "user1@example.com",
  "password": "password123"
}
```

### **2⃣ Se connecter et récupérer le token JWT**

**Endpoint** : `POST /token`

```json
{
  "email": "user1@example.com",
  "password": "password123"
}
```

**Réponse** :

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsIn..."
}
```

### **3⃣ Utiliser le token pour accéder aux endpoints sécurisés**

Ajouter le token dans **Postman** ou Swagger :

```
Authorization: Bearer {your_token_here}
```

📌 **Seul `admin@admin.com` peut ajouter/modifier/supprimer des produits**.

 
 
