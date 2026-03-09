# Configuración de GitHub Actions

## Secrets Requeridos

Para que el pipeline de CI/CD funcione correctamente, necesitas configurar los siguientes secrets en tu repositorio de GitHub:

### 1. DOCKER_USERNAME
Tu nombre de usuario de Docker Hub

### 2. DOCKER_PASSWORD
Tu token de acceso de Docker Hub (recomendado) o contraseña

## Cómo Configurar los Secrets

1. Ve a tu repositorio en GitHub
2. Click en **Settings** (Configuración)
3. En el menú lateral, click en **Secrets and variables** → **Actions**
4. Click en **New repository secret**
5. Agrega cada secret:
   - Name: `DOCKER_USERNAME`
   - Secret: Tu usuario de Docker Hub
   - Click **Add secret**
   
   Repite para `DOCKER_PASSWORD`

## Crear Token de Docker Hub (Recomendado)

1. Ve a https://hub.docker.com/settings/security
2. Click en **New Access Token**
3. Dale un nombre descriptivo (ej: "github-actions")
4. Copia el token generado
5. Úsalo como valor para `DOCKER_PASSWORD`

## Verificar el Pipeline

Una vez configurados los secrets:
1. Haz commit y push de tu código
2. Ve a la pestaña **Actions** en GitHub
3. Verás el workflow ejecutándose automáticamente
