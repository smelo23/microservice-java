# Helm Charts - Microservicio Java

Helm charts simplificados para desplegar el microservicio en Kubernetes.

## 📁 Estructura

```
helm/microservice-java/
├── Chart.yaml              # Metadata del chart
└── templates/
    ├── deployment.yaml     # Deployment de Kubernetes
    ├── service.yaml        # Service de Kubernetes
    └── configmap.yaml      # ConfigMap con configuración
```

**Nota**: Este chart usa valores directos en los templates (sin `values.yaml`) para mayor simplicidad.

---

## 🚀 Instalación

### Instalación Básica

```bash
# Instalar en el namespace default
helm install microservice-java ./helm/microservice-java

# Instalar en un namespace específico
helm install microservice-java ./helm/microservice-java \
  --namespace production \
  --create-namespace
```

---

## 🔄 Actualización

```bash
# Actualizar el deployment
helm upgrade microservice-java ./helm/microservice-java

# Actualizar en un namespace específico
helm upgrade microservice-java ./helm/microservice-java \
  --namespace production
```

---

## 🗑️ Desinstalación

```bash
# Desinstalar del namespace default
helm uninstall microservice-java

# Desinstalar de un namespace específico
helm uninstall microservice-java --namespace production
```

---

## ✅ Validar Templates

```bash
# Ver los manifiestos generados sin instalar
helm template microservice-java ./helm/microservice-java

# Verificar sintaxis del chart
helm lint ./helm/microservice-java
```

---

## 📊 Configuración Actual

### Deployment
- **Réplicas**: 1
- **Imagen**: `smelo23/microservice-java:1.0.0`
- **Puerto**: 8080 (interno)
- **Recursos**:
  - CPU: 200m (requests)
  - Memoria: 500Mi (requests)

### Service
- **Tipo**: LoadBalancer
- **Puerto externo**: 80
- **Puerto interno**: 8080
- **Selector**: `app: microservice-java`

### ConfigMap
- **SERVER_PORT**: "8080"
- **SPRING_PROFILES_ACTIVE**: "prod"

### Health Checks
- **Startup Probe**: `/actuator/health` (delay 20s)

---

## 🔧 Modificar Configuración

Para cambiar la configuración, edita directamente los archivos en `templates/`:

### Cambiar número de réplicas
Edita `templates/deployment.yaml`:
```yaml
spec:
  replicas: 3  # Cambiar aquí
```

### Cambiar versión de imagen
Edita `templates/deployment.yaml`:
```yaml
image: smelo23/microservice-java:2.0.0  # Cambiar tag aquí
```

### Cambiar tipo de Service
Edita `templates/service.yaml`:
```yaml
spec:
  type: ClusterIP  # Cambiar de LoadBalancer a ClusterIP
```

---

## 📦 Recursos de Kubernetes Creados

1. **Deployment** (`microservice-java`)
   - Gestiona los pods del microservicio
   - 1 réplica por defecto
   - Health checks configurados

2. **Service** (`microservice-java`)
   - Tipo LoadBalancer
   - Expone el puerto 80 externamente
   - Redirige al puerto 8080 del contenedor

3. **ConfigMap** (`microservice-java-config`)
   - Variables de entorno para la aplicación
   - Configuración de Spring Boot

---

## 🔍 Verificar Despliegue

```bash
# Ver el estado del release
helm status microservice-java

# Ver todos los recursos creados
helm get manifest microservice-java

# Ver pods
kubectl get pods -l app=microservice-java

# Ver service y obtener IP externa
kubectl get svc microservice-java

# Ver logs
kubectl logs -l app=microservice-java
```

---

## 🌐 Acceder al Microservicio

Después de instalar, obtén la IP externa:

```bash
kubectl get svc microservice-java
```

Luego accede a:
```bash
# Health check
curl http://<EXTERNAL-IP>/actuator/health

# Endpoints custom
curl http://<EXTERNAL-IP>/api/health
curl http://<EXTERNAL-IP>/api/hello
```

---

## 💡 Tips

- **GitOps con ArgoCD**: Este chart está diseñado para usarse con ArgoCD
- **Simplicidad**: Sin variables complejas, fácil de entender y modificar
- **Producción**: Para producción, considera aumentar réplicas y recursos
