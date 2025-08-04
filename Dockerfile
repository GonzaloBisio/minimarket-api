# --- ETAPA 1: Construcción (Build) con Corretto 21 ---
FROM amazoncorretto:21 as builder

# CAMBIO: Instalar 'tar' y 'gzip', que son necesarios para que Maven Wrapper se descomprima a sí mismo.
# La imagen base es Amazon Linux, que usa el gestor de paquetes 'yum'.
RUN yum install -y tar gzip

# Creamos un directorio de trabajo dentro de la imagen
WORKDIR /app

# Copiamos los archivos de Maven Wrapper y el pom.xml para descargar dependencias
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Damos permisos de ejecución al Maven Wrapper
RUN chmod +x mvnw

# Descargamos las dependencias de Maven
RUN ./mvnw dependency:go-offline

# Copiamos el resto del código fuente de la aplicación
COPY src ./src

# Compilamos la aplicación y la empaquetamos en un .jar, saltando los tests
RUN ./mvnw clean package -DskipTests


# --- ETAPA 2: Ejecución (Runtime) con Corretto 21 ---
FROM amazoncorretto:21

# Creamos un directorio de trabajo
WORKDIR /app

# Copiamos SÓLO el archivo .jar compilado desde la etapa 'builder'
COPY --from=builder /app/target/*.jar app.jar

# Exponemos el puerto en el que correrá la aplicación
EXPOSE 8080

# El comando que se ejecutará cuando el contenedor inicie
ENTRYPOINT ["java", "-jar", "app.jar"]