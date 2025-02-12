#!/bin/bash

# Variables
APP_NAME="nandoshop-app"
GIT_REPO="https://github.com/CHNandoBravo/NandoShop.git"
DEPLOY_DIR="/root/nandoshop-test"
JAR_FILE="target/$APP_NAME.jar"

# 1. Actualiza el código desde el repositorio
echo "Actualizando el repositorio..."
cd $DEPLOY_DIR
git pull origin main || { echo "Error al hacer pull del repositorio"; exit 1; }

# 2. Compila la aplicación usando Maven
echo "Compilando la aplicación..."
mvn clean install -DskipTests || { echo "Error al compilar la aplicación"; exit 1; }

# 3. Detén la aplicación si está corriendo (si es necesario)
echo "Deteniendo la aplicación si está en ejecución..."
PID=$(ps aux | grep "$APP_NAME.jar" | grep -v grep | awk '{print $2}')
if [ -n "$PID" ]; then
  kill -9 $PID
  echo "Aplicación detenida."
else
  echo "No se encontró una instancia en ejecución."
fi

# 4. Ejecuta el nuevo .jar
echo "Iniciando la aplicación..."
nohup java -jar $JAR_FILE > /dev/null 2>&1 &

# 5. Verifica que la aplicación esté corriendo (opcional)
echo "Verificando que la aplicación esté corriendo..."
sleep 5  # espera unos segundos para que la aplicación arranque
ps aux | grep "$APP_NAME.jar" || { echo "La aplicación no se está ejecutando correctamente"; exit 1; }

echo "Despliegue completado con éxito."

# 6. Actualiza el VPS 
mvn clean package
sudo systemctl restart api-test.service
