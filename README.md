# Microservicio Java - Spring Boot para Kubernetes

Microservicio completo con CI/CD, Docker, Kubernetes, Helm y ArgoCD para despliegue GitOps.

## 📋 Tabla de Contenidos

- [Tecnologías](#tecnologías)
- [Arquitectura](#arquitectura)
- [Endpoints API](#endpoints-api)
- [Desarrollo Local](#desarrollo-local)
- [Docker](#docker)
- [Kubernetes](#kubernetes)
- [CI/CD](#cicd)
- [ArgoCD GitOps](#argocd-gitops)
- [Probar en Producción](#probar-en-producción)

---

## 🛠️ Tecnologías

- **Backend**: Java 17, Spring Boot 3.2.0
- **Build**: Gradle 8.5
- **Contenedores**: Docker (multi-stage build)
- **Orquestación**: Kubernetes
- **Gestión**: Helm Charts
- **GitOps**: ArgoCD
- **CI/CD**: GitHub Actions
- **Cloud**: DigitalOcean Kubernetes

---

## 🏗️ Arquitectura

```
┌─────────────────────────────────────────────────────────┐
│  1. DESARROLLO                                          │
│     • Código Java (Spring Boot)                         │
│     • Commit → GitHub                                   │
├─────────────────────────────────────────────────────────┤
│  2. CI/CD (GitHub Actions)                              │
│     • Compilar con Gradle                               │
│     • Construir imagen Docker                           │
│     • Push a Docker Hub                                 │
├─────────────────────────────────────────────────────────┤
│  3. KUBERNETES (DigitalOcean)                           │
│     • Deployment (1 réplica)                            │
│     • Service (LoadBalancer)                            │
│     • ConfigMap (variables de entorno)                  │
├─────────────────────────────────────────────────────────┤
│  4. ARGOCD (GitOps)                                     │
│     • Sincronización automática desde Git               │
│     • Despliegue continuo                               │
│     • Rollbacks automáticos                             │
└─────────────────────────────────────────────────────────┘
```

---

## 🌐 Endpoints API

### Endpoints Custom

- **`GET /api/health`** - Estado del servicio
  ```json
  {
    "service": "microservice-java",
    "version": "1.0.0",
    "status": "UP",
    "timestamp": "2026-03-09T03:34:14"
  }
  ```

- **`GET /api/hello`** - Saludo simple
  ```json
  {
    "message": "Hola desde el microservicio!",
    "author": "smelo23"
  }
  ```

- **`GET /api/hello/{name}`** - Saludo personalizado
  ```json
  {
    "message": "Hola Sebastian!",
    "timestamp": "2026-03-09T03:34:14"
  }
  ```

### Spring Boot Actuator

- **`GET /actuator/health`** - Health check completo
- **`GET /actuator/info`** - Información de la aplicación
- **`GET /actuator/metrics`** - Métricas del sistema

---

## 💻 Desarrollo Local

### Requisitos

- Java 17 (JDK)
- Gradle 8.5+ (o usar el wrapper incluido)
- Docker (opcional, para pruebas con contenedores)

### Ejecutar Localmente

```bash
# Con Gradle wrapper (recomendado)
./gradlew bootRun

# Con Gradle instalado
gradle bootRun

# El servicio estará disponible en:
# http://localhost:8080
```

### Compilar

```bash
./gradlew clean build
```

### Ejecutar JAR

```bash
java -jar build/libs/microservice-1.0.0.jar
```

### Ejecutar Tests

```bash
./gradlew test
```

---

## 🐳 Docker

### Construir Imagen

```bash
docker build -t smelo23/microservice-java:1.0.0 .
```

### Ejecutar Contenedor

```bash
docker run -d -p 8080:8080 --name microservice smelo23/microservice-java:1.0.0
```

### Probar

```bash
curl http://localhost:8080/api/health
curl http://localhost:8080/api/hello
```

### Detener y Limpiar

```bash
docker stop microservice
docker rm microservice
```

---

## ☸️ Kubernetes

### Estructura de Helm Charts

```
helm/microservice-java/
├── Chart.yaml              # Metadata del chart
└── templates/
    ├── deployment.yaml     # 1 réplica, 200m CPU, 500Mi RAM
    ├── service.yaml        # LoadBalancer en puerto 8080
    └── configmap.yaml      # Variables de entorno
```

### Desplegar con Helm

```bash
# Instalar
helm install microservice-java ./helm/microservice-java

# Actualizar
helm upgrade microservice-java ./helm/microservice-java

# Desinstalar
helm uninstall microservice-java
```

### Verificar Despliegue

```bash
# Ver pods
kubectl get pods

# Ver services
kubectl get svc

# Ver logs
kubectl logs -l app=microservice-java

# Obtener IP externa
kubectl get svc microservice-java
```

---

## 🔄 CI/CD

### GitHub Actions Pipeline

El pipeline se ejecuta automáticamente en cada push a `main` o `develop`:

1. **Build and Test**
   - Checkout del código
   - Setup JDK 17
   - Compilar con Gradle
   - Ejecutar tests
   - Guardar JAR como artefacto

2. **Docker Build & Push**
   - Construir imagen Docker
   - Publicar a Docker Hub
   - Tags automáticos (latest, branch, sha)

### Configuración Necesaria

Secrets en GitHub (Settings → Secrets):
- `DOCKER_USERNAME`: Usuario de Docker Hub
- `DOCKER_PASSWORD`: Token de Docker Hub

Ver: `.github/SETUP.md` para más detalles

---

## 🚀 ArgoCD GitOps

### Instalación de ArgoCD en el Cluster

```bash
# 1. Crear namespace
kubectl create namespace argocd

# 2. Instalar ArgoCD
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

# 3. Exponer UI
kubectl patch svc argocd-server -n argocd -p '{"spec": {"type": "LoadBalancer"}}'

# 4. Obtener password
kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d
```

### Configurar Aplicación desde ArgoCD UI

1. Accede a la UI de ArgoCD con la IP del LoadBalancer
2. Click en **"+ NEW APP"**
3. Configuración:
   - **Application Name**: `microservice-java`
   - **Project**: `default`
   - **Sync Policy**: `Automatic`
   - **Repository URL**: `https://github.com/smelo23/microservice-java.git`
   - **Revision**: `main`
   - **Path**: `helm/microservice-java`
   - **Cluster URL**: `https://kubernetes.default.svc`
   - **Namespace**: `default`
4. Click en **"CREATE"**

### O Configurar desde kubectl

```bash
kubectl apply -f - <<EOF
apiVersion: argoproj.io/v1alpha1
kind: Application
metadata:
  name: microservice-java
  namespace: argocd
spec:
  project: default
  source:
    repoURL: https://github.com/smelo23/microservice-java.git
    targetRevision: main
    path: helm/microservice-java
  destination:
    server: https://kubernetes.default.svc
    namespace: default
  syncPolicy:
    automated:
      prune: true
      selfHeal: true
EOF
```

### Verificar Estado

```bash
# Ver aplicaciones
kubectl get applications -n argocd

# Ver detalles
kubectl describe application microservice-java -n argocd
```

---

## 🌍 Probar en Producción

### Microservicio Desplegado

**URL Base**: `http://24.199.66.62`

### Probar Endpoints

```bash
# Health check
curl http://24.199.66.62/actuator/health

# Estado del servicio
curl http://24.199.66.62/api/health

# Saludo simple
curl http://24.199.66.62/api/hello

# Saludo personalizado
curl http://24.199.66.62/api/hello/TuNombre
```

### Respuestas Esperadas

```bash
# /actuator/health
{"status":"UP","components":{"diskSpace":{"status":"UP"},...}}

# /api/health
{"service":"microservice-java","version":"1.0.0","status":"UP","timestamp":"..."}

# /api/hello
{"author":"smelo23","message":"Hola desde el microservicio!"}

# /api/hello/Sebastian
{"message":"Hola Sebastian!","timestamp":"..."}
```

---

## 📁 Estructura del Proyecto

```
microservice-java/
├── src/
│   └── main/
│       ├── java/com/usabana/microservice/
│       │   ├── MicroserviceApplication.java
│       │   └── controller/
│       │       └── HealthController.java
│       └── resources/
│           └── application.properties
├── helm/microservice-java/
│   ├── Chart.yaml
│   └── templates/
│       ├── deployment.yaml
│       ├── service.yaml
│       └── configmap.yaml
├── .github/workflows/
│   └── ci-cd.yaml
├── Dockerfile
├── build.gradle
├── settings.gradle
└── README.md
```

---

## 🎯 Características Implementadas

✅ API REST con Spring Boot  
✅ Health checks y métricas (Actuator)  
✅ Dockerfile multi-stage optimizado  
✅ GitHub Actions CI/CD  
✅ Helm Charts para Kubernetes  
✅ Desplegado en DigitalOcean Kubernetes  
✅ Service tipo LoadBalancer con IP pública  
✅ ArgoCD para GitOps  
✅ Despliegue continuo automático  

---

## 📚 Documentación Adicional

- **Helm Charts**: Ver `helm/README.md`
- **GitHub Actions**: Ver `.github/SETUP.md`

---

## 👤 Autor

**Sebastian Melo**
- GitHub: [@smelo23](https://github.com/smelo23)
- Docker Hub: [smelo23/microservice-java](https://hub.docker.com/r/smelo23/microservice-java)

---

## 📝 Licencia

Este proyecto fue creado como parte de una actividad académica de la Universidad de La Sabana.
