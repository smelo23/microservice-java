# Helm Charts - Microservicio Java

Helm charts para desplegar el microservicio en Kubernetes.

## Estructura

```
helm/microservice-java/
├── Chart.yaml              # Metadata del chart
├── values.yaml             # Valores por defecto
├── values-dev.yaml         # Configuración para desarrollo
├── values-prod.yaml        # Configuración para producción
└── templates/
    ├── deployment.yaml     # Deployment de Kubernetes
    ├── service.yaml        # Service de Kubernetes
    ├── configmap.yaml      # ConfigMap con configuración
    └── _helpers.tpl        # Funciones helper de templates
```

## Instalación

### Desarrollo

```bash
helm install microservice-dev ./helm/microservice-java \
  -f ./helm/microservice-java/values-dev.yaml \
  --namespace dev \
  --create-namespace
```

### Producción

```bash
helm install microservice-prod ./helm/microservice-java \
  -f ./helm/microservice-java/values-prod.yaml \
  --namespace prod \
  --create-namespace
```

## Actualización

```bash
# Desarrollo
helm upgrade microservice-dev ./helm/microservice-java \
  -f ./helm/microservice-java/values-dev.yaml \
  --namespace dev

# Producción
helm upgrade microservice-prod ./helm/microservice-java \
  -f ./helm/microservice-java/values-prod.yaml \
  --namespace prod
```

## Desinstalación

```bash
helm uninstall microservice-dev --namespace dev
helm uninstall microservice-prod --namespace prod
```

## Validar Templates

```bash
# Ver los manifiestos generados sin instalar
helm template microservice-dev ./helm/microservice-java \
  -f ./helm/microservice-java/values-dev.yaml
```

## Verificar el Chart

```bash
helm lint ./helm/microservice-java
```

## Configuraciones por Entorno

### Desarrollo (`values-dev.yaml`)
- 1 réplica
- Recursos limitados (256Mi RAM, 250m CPU)
- Tag de imagen: `main`
- Profile: `dev`

### Producción (`values-prod.yaml`)
- 3 réplicas (autoscaling hasta 10)
- Más recursos (1Gi RAM, 1000m CPU)
- Tag de imagen: `latest`
- Profile: `prod`
- Autoscaling habilitado

## Health Checks

El chart incluye:
- **Liveness Probe**: Verifica que el contenedor esté vivo
- **Readiness Probe**: Verifica que esté listo para recibir tráfico
- Ambos usan el endpoint `/actuator/health` de Spring Boot

## Recursos de Kubernetes Creados

1. **Deployment**: Gestiona los pods del microservicio
2. **Service**: Expone el microservicio dentro del cluster
3. **ConfigMap**: Contiene la configuración de la aplicación
