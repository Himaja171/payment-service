# 💳 Payment Service — CI/CD Pipeline with Jenkins, Docker & AWS ECR

> A production-grade Spring Boot microservice with a fully automated CI/CD pipeline built during my tenure at **VMSigma Finserv**.

---

## 🏗️ Architecture Overview

```
GitHub → Jenkins → SonarQube → Nexus → Docker → AWS ECR → EKS (Production)
```

---

## 🛠️ Tech Stack

| Layer              | Technology                        |
|--------------------|-----------------------------------|
| Application        | Java 17, Spring Boot 3.x          |
| Build Tool         | Maven 3.9                         |
| Containerization   | Docker (Multi-stage build)        |
| CI/CD              | Jenkins (Declarative Pipeline)    |
| Code Quality       | SonarQube + JaCoCo                |
| Artifact Registry  | Nexus Repository Manager          |
| Container Registry | AWS ECR                           |
| Orchestration      | AWS EKS (Kubernetes)              |
| Cloud              | AWS (ap-south-1 — Mumbai)         |

---

## 🚀 CI/CD Pipeline Stages

1. **Checkout** — Pull latest code from GitHub
2. **Build & Unit Test** — Maven build with JaCoCo coverage
3. **SonarQube Analysis** — Static code analysis & code smell detection
4. **Quality Gate** — Pipeline fails if coverage or quality thresholds not met
5. **Upload to Nexus** — JAR artifact versioned and stored in Nexus
6. **Docker Build & Push** — Multi-stage Docker image pushed to AWS ECR
7. **Deploy to EKS** — Rolling deployment to production Kubernetes cluster

---

## 📁 Project Structure

```
payment-service/
├── src/
│   └── main/
│       ├── java/com/vmsigma/paymentservice/
│       │   ├── PaymentServiceApplication.java
│       │   └── PaymentController.java
│       └── resources/
│           └── application.properties
├── Dockerfile
├── Jenkinsfile
├── pom.xml
└── README.md
```

---

## 🐳 Docker

**Build locally:**
```bash
docker build -t payment-service:latest .
docker run -p 8080:8080 payment-service:latest
```

**Test health endpoint:**
```bash
curl http://localhost:8080/actuator/health
```

---

## 🔑 Key Highlights

- ✅ **Multi-stage Docker build** — Reduced image size by ~60%
- ✅ **SonarQube quality gate** — Enforced code coverage threshold (>80%)
- ✅ **Nexus artifact management** — Versioned JAR storage per build number
- ✅ **Zero-downtime deployment** — Kubernetes rolling update strategy
- ✅ **Automated email alerts** — On pipeline failure via Jenkins

---

## 👩‍💻 Author

**Himaja Dudikatla** — AWS DevOps Engineer  
📧 himaja.connect@gmail.com  
🔗 [linkedin.com/in/himajadudikatla](https://linkedin.com/in/himajadudikatla)
