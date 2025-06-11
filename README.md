# ci‑victor‑terrien

![CI Status](https://github.com/VictorTerrien/ci-victor-terrien/workflows/CI/badge.svg)

Un dépôt dédié aux configurations d’intégration continue (CI) utilisées par Victor Terrien, pour automatiser la construction, les tests et le déploiement de projets.

---

## 📦 Contenu du dépôt

- `.github/workflows/` – Fichiers de configuration GitHub Actions
- `scripts/` – Scripts d’automatisation (ex. lint, test, build)
- `Dockerfile` – Image conteneur personnalisée (si présente)
- `docs/` – Documentation liée aux pipelines CI

---

## 🔧 Prérequis

- GitHub Actions activé dans le dépôt
- Accès à tous les secrets nécessaires (ex. `DOCKERHUB_TOKEN`, `AWS_ACCESS_KEY_ID`)
- Environnement Node.js ≥14 ou Python ≥3.x (à adapter selon le projet)

---

## ✅ Flux CI

1. **Détection des branches**  
   Les workflows sont déclenchés lors des pushes ou pull requests sur `main`, `develop`, ou tout autre branche cible.

2. **Lint & Compilation**  
   - Installation des dépendances (`npm ci`, `pip install -r requirements.txt`, etc.)  
   - Analyse de code avec ESLint/Flake8  
   - Compilation ou transpilation si nécessaire

3. **Tests automatisés**  
   - Exécution des tests unitaires (ex. `jest`, `pytest`)  
   - Collecte et rapport de couverture

4. **Packaging & Déploiement**  
   - Construction d’artefacts (Docker, fichiers dist)  
   - Publication dans un registre (Docker Hub, GitHub Packages, AWS S3…)

---

## 🚀 Utilisation

### Sur GitHub

1. Clone le dépôt :
   ```bash
   git clone https://github.com/VictorTerrien/ci-victor-terrien.git
