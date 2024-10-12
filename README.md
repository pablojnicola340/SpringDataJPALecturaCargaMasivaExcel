# Lectura de Fichero Excel Masivo con Spring Data JPA

Este proyecto consiste en la lectura de un fichero Excel masivo utilizando Spring Data JPA. Se realiza la inserción de un millón de registros en una base de datos PostgreSQL.

## Descripción de los Proyectos

- **FileGenerator**: Este es un proyecto para generar un archivo de Excel con un millón de registros.

- **SpringbootReader**: Esta aplicación se encarga de leer el millón de registros del archivo Excel generado y los inserta en la base de datos.

## Librería para Leer Excel

Utilizamos la librería [excel-streaming-reader](https://github.com/monitorjbl/excel-streaming-reader) para facilitar la lectura de archivos Excel. Puedes encontrar instrucciones sobre su uso en el repositorio.

## Uso de PostgreSQL con Docker

Para utilizar PostgreSQL en este proyecto, puedes usar la imagen de Docker de PostgreSQL. A continuación se muestran los comandos necesarios:

1. **Descargar la imagen de PostgreSQL**:

   ```bash
   docker pull postgres
   
2. **Ejecutar un contenedor de PostgreSQL**:

   ```bash
   docker run --name mi_postgres -e POSTGRES_USER=mi_usuario -e POSTGRES_PASSWORD=mi_contraseña -e POSTGRES_DB=mi_base_de_datos -p 5432:5432 -d postgres

3. **Ejecutar un contenedor de PostgreSQL**:

   ```bash
   docker exec -it mi_postgres psql -U mi_usuario -d mi_base_de_datos
