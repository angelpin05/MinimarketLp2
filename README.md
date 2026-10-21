Minimarket LP2
Este es mi proyecto individual para el curso de Lenguaje de Programación II de Cibertec. Es un sistema web para un minimarket, desarrollado con Java 17 y Spring Boot.

La idea del proyecto es llevar el registro de las ventas y saber cuántos productos quedan disponibles. Cuando se registra una venta, el sistema guarda sus detalles y descuenta las cantidades del inventario. También permite mantener los datos de los clientes, organizar el catálogo y descargar reportes en PDF.

Autor: Angel Daniel Pintado Nizama
- Repositorio: https://github.com/angelpin05/MinimarketLp2
- URL configurada en Azure: https://minimarket-angel-lp2-bcgzdfdxgtdycsc2.westus-01.azurewebsites.net
Para probarlo en una computadora, se puede usar la demo con H2 o conectarlo a MySQL. Más abajo dejo los pasos para ambas opciones.
Qué se puede hacer
Opción	Para qué sirve
Categorías	Registrar, editar, desactivar y reactivar las categorías de los productos.
Productos	Mantener código, nombre, categoría, precio, stock y stock mínimo.
Clientes	Registrar y actualizar los datos de los compradores.
Ventas	Seleccionar un cliente, agregar productos y registrar la venta después de comprobar el stock.
Consulta de ventas	Buscar ventas por fechas y cliente, y revisar el total del período.
Consulta de inventario	Buscar por código, nombre o categoría y mostrar los productos con stock bajo.
Reportes	Descargar las consultas de ventas e inventario en PDF.


Los mantenimientos usan una baja lógica. Esto significa que un registro se puede dejar inactivo sin eliminarlo de la base ni perder su relación con ventas anteriores. Para reactivarlo, se edita su estado.
Usuarios y permisos
El sistema tiene dos roles. El administrador puede usar todos los módulos. El vendedor trabaja con clientes, ventas y consultas.
Función	ADMIN	VENDEDOR
Categorías y productos	Sí	No
Clientes	Sí	Sí
Registrar ventas	Sí	Sí
Consultar ventas e inventario	Sí	Sí
Descargar reportes PDF	Sí	No


Los permisos se comprueban con Spring Security. Las contraseñas se guardan con BCrypt y la sesión termina después de 30 minutos de inactividad. Los formularios POST mantienen la protección CSRF.
Tecnologías que utilicé
- Java 17 y Spring Boot 3.5.12 para la aplicación y el servidor Tomcat embebido.
- Spring MVC y Thymeleaf para los formularios y las páginas del sistema.
- Spring Security para el inicio de sesión y los permisos.
- Spring Data JPA e Hibernate para trabajar con las entidades y la base de datos.
- MySQL para guardar los datos de forma persistente y H2 para la demo y las pruebas.
- Flyway para ejecutar los archivos SQL que crean y actualizan el esquema.
- JasperReports 7.0.3 para generar los reportes PDF.
- Maven, GitHub Actions y Azure App Service para compilar, verificar y publicar la aplicación.
Qué se necesita instalar
- JDK 17. Los comandos java y javac deben estar disponibles.
- Git o GitHub Desktop para obtener el proyecto.
- MySQL 8.x si se va a usar la base local. Workbench es opcional.
- Spring Tools, Eclipse o IntelliJ IDEA para abrir el código desde un IDE.
El proyecto incluye Maven Wrapper, así que no hace falta instalar Maven por separado. La primera compilación necesita Internet para descargarlo y obtener las dependencias. Tomcat ya viene incluido en Spring Boot. Esta versión usa los métodos Java escritos en las clases y no requiere instalar Lombok.
Cómo ejecutar el proyecto
Probar la demo sin instalar MySQL
La forma más rápida de probarlo es con el perfil demo. Usa H2 en memoria y carga datos de ejemplo.

En Windows, desde PowerShell:
git clone https://github.com/angelpin05/MinimarketLp2.git
cd MinimarketLp2
.\mvnw.cmd clean verify
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=demo"
En Linux o macOS:
git clone https://github.com/angelpin05/MinimarketLp2.git
cd MinimarketLp2
sh ./mvnw clean verify
sh ./mvnw spring-boot:run -Dspring-boot.run.profiles=demo
Cuando termine de iniciar, se ingresa desde http://localhost:8080 con una de estas cuentas:
Usuario	Contraseña de la demo	Rol
admin	AdminDemo2026!	Administrador
vendedor	VentaDemo2026!	Vendedor


Estas cuentas son para la demo. Al detener la aplicación se pierden sus datos, porque H2 se está usando en memoria.

También se puede ejecutar el JAR que genera la compilación:
java -jar .\target\minimarket-lp2.jar --spring.profiles.active=demo
El ZIP original incluye demo.bat, demo.sh y el JAR en dist. Esos lanzadores necesitan esa carpeta. Si el proyecto se obtiene desde GitHub, se compila y se usa el JAR de target/minimarket-lp2.jar.
Usarlo con MySQL local
Para conservar las ventas y el inventario después de cerrar la aplicación, se utiliza el perfil local con MySQL.

Primero hay que iniciar MySQL y crear la base. El archivo [01_crear_base_mysql.sql](database/01_crear_base_mysql.sql) contiene la instrucción:
CREATE DATABASE IF NOT EXISTS minimarket_lp2
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
Al iniciar el proyecto, Flyway crea las tablas y carga los datos de las migraciones. Por eso no se deben importar manualmente V1__esquema.sql ni V2__datos_demostracion.sql mientras Flyway esté activo.

Después se configuran las credenciales. Este es un ejemplo en PowerShell; los valores deben reemplazarse por las claves que se van a utilizar:
$env:SPRING_PROFILES_ACTIVE="local"
$env:DB_USER="root"
$env:DB_PASSWORD="TU_CLAVE_MYSQL"
$env:ADMIN_PASSWORD="TU_CLAVE_ADMIN_DE_12_O_MAS"
$env:VENDEDOR_PASSWORD="OTRA_CLAVE_DISTINTA_DE_12_O_MAS"
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local"
La conexión local apunta por defecto a localhost:3306 y a la base minimarket_lp2. Se puede cambiar con la variable DB_URL, usando una URL como esta:
jdbc:mysql://localhost:3306/minimarket_lp2?serverTimezone=America/Lima&characterEncoding=UTF-8
Las claves ADMIN_PASSWORD y VENDEDOR_PASSWORD deben ser diferentes y tener al menos 12 caracteres. Se utilizan para crear las cuentas admin y vendedor cuando la tabla de usuarios está vacía. Cambiar las variables después no modifica las contraseñas de cuentas que ya existen.

La aplicación recibe las variables desde el entorno. Guardarlas solamente en un archivo .env no las carga automáticamente en esta configuración.
Abrirlo en Spring Tools
1. Entrar a File > Import > Maven > Existing Maven Projects.
2. Seleccionar la carpeta que contiene pom.xml y src.
3. Configurar el proyecto con JDK 17.
4. Agregar las variables del perfil local en Run Configurations > Environment.
5. Revisar los argumentos y quitar el perfil demo si se utilizó antes.
6. Ejecutar MinimarketApplication con Run As > Spring Boot App. En Eclipse sin Spring Tools también se puede usar Java Application.
Con MySQL y la aplicación en ejecución, el acceso es http://localhost:8080.
Cómo usar el sistema
1. Iniciar sesión. Con admin se pueden revisar todas las opciones.
2. Registrar las categorías y los productos, indicando su precio y sus existencias.
3. Registrar un cliente activo.
4. Abrir Nueva venta, seleccionar al cliente y agregar los productos con sus cantidades.
5. Guardar la venta. El sistema comprueba el stock y calcula los importes.
6. Revisar el detalle y consultar el inventario para comprobar las nuevas cantidades.
7. Usar las consultas para buscar ventas o productos y, como administrador, descargar sus reportes PDF.
8. Cerrar sesión al terminar.
Ejemplo de una venta
Para la demostración uso este ejemplo con los datos de una demo recién iniciada:
Producto	Cantidad	Precio unitario	Subtotal
Arroz 1 kg	2	S/ 4.50	S/ 9.00
Agua 625 ml	1	S/ 2.00	S/ 2.00
Total			S/ 11.00


Después de guardar, el arroz pasa de 40 a 38 unidades y el agua de 50 a 49. Si luego se intenta vender 51 botellas de agua, la operación se rechaza por stock insuficiente.
Cómo organicé el código
Separé el código por responsabilidades. Los controladores reciben los formularios, los servicios aplican las reglas y los repositorios trabajan con la base de datos. Las páginas se muestran con Thymeleaf y los reportes se devuelven como archivos PDF.
Carpeta	Qué contiene
model	Las seis entidades y sus relaciones.
repository	Las consultas y el acceso a datos con JPA.
service	Las reglas de las ventas, las consultas y los reportes.
web y web/form	Los controladores y los formularios con sus validaciones.
config	La seguridad y la creación de las cuentas iniciales.
src/main/resources/templates	Las páginas HTML del sistema.
src/main/resources/static	Los archivos CSS y JavaScript.
src/main/resources/db/migration	Los archivos SQL de Flyway.
src/test	Las pruebas de integración.


Las carpetas Java de la tabla están dentro de src/main/java/pe/edu/cibertec/minimarket.

El recorrido de una venta empieza en VentaController, que recibe el formulario y obtiene al usuario de la sesión. Después llama a VentaService.registrar. Allí se revisan el cliente, los productos, las cantidades y el stock, antes de guardar la operación.

Utilicé @Transactional para que guardar la venta y descontar el inventario formen una sola operación. Si falla una regla de negocio, se deshacen sus cambios. Los precios se toman de la base y los importes se calculan con BigDecimal.

Para las ventas simultáneas, los productos se bloquean con PESSIMISTIC_WRITE en orden de ID. @Version ayuda a evitar que un formulario antiguo sobrescriba un stock que ya cambió. También se valida un token del formulario, único en la base, para evitar registrar la misma venta dos veces. Los productos repetidos en el formulario se agrupan antes de comprobar sus cantidades.
Base de datos
Tabla	Qué guarda
categoria	La clasificación de los productos.
producto	Código, categoría, precio, stock, mínimo y versión.
cliente	El documento y los datos del comprador.
usuario	La cuenta, contraseña BCrypt, rol y estado.
venta	Cliente, vendedor, fecha, total y token.
detalle_venta	Productos vendidos, cantidades, precio, subtotal y nombre histórico.


El detalle conserva el nombre y el precio usados en la venta. Así, un cambio posterior en el catálogo no altera esos datos de las ventas anteriores. Flyway también utiliza flyway_schema_history para llevar el registro de las migraciones.

El [modelo de datos](documentacion/diagramas/modelo_datos.png) muestra las relaciones entre las tablas.
<details>
<summary>Rutas principales de la aplicación</summary>

Método	Ruta	Uso
GET	/login	Inicio de sesión.
GET	/	Página principal.
GET	/categorias, /productos, /clientes	Mantenimientos.
GET	/ventas/nueva	Formulario de venta.
POST	/ventas/registrar	Registro de la venta.
GET	/ventas/{id}	Detalle guardado.
GET	/consultas/inventario	Consulta de productos y existencias.
GET	/consultas/ventas	Consulta por fechas y cliente.
GET	/reportes/inventario.pdf, /reportes/ventas.pdf	Reportes para ADMIN.
GET	/actuator/health	Estado de salud de la aplicación.


Estas rutas muestran páginas HTML o archivos PDF. Los formularios POST requieren sesión, permisos y token CSRF según corresponda.
</details>

Compilación y pruebas
Para compilar, ejecutar las pruebas y generar el JAR:
.\mvnw.cmd clean verify
La clase ProyectoIntegrationTest contiene 17 pruebas de integración con H2. Revisan, entre otros casos, el registro de ventas, la reversión de una operación con error, las ventas simultáneas, el stock, los permisos y los reportes PDF. Los resultados de cada ejecución quedan en target/surefire-reports.

Las comprobaciones con MySQL y con la aplicación publicada están descritas en [VERIFICACION.md](documentacion/VERIFICACION.md).

Los diseños de los reportes que usa la aplicación están en ReporteService. Los archivos reportes/inventario.jrxml y reportes/ventas.jrxml sirven para revisar el diseño en Jaspersoft Studio 7; editarlos no cambia automáticamente el diseño definido en Java.
GitHub y Azure
En GitHub guardo el código, las migraciones, las pruebas y la documentación. Para subir un cambio con GitHub Desktop, primero sincronizo la rama, reviso Changes, hago el commit y después Push origin. El commit guarda la versión local y el push la envía al repositorio.

El proyecto incluye el workflow [ci-azure.yml](.github/workflows/ci-azure.yml). Se activa con un push a main, un pull request hacia main o una ejecución manual. Su job verificar prepara Java 17, ejecuta mvn -B -ntp clean verify y conserva el JAR de target/minimarket-lp2.jar.

Ese archivo también tiene un job de despliegue condicionado a AZURE_WEBAPP_NAME y a la configuración de identidad de Azure. App Service puede tener otro workflow creado desde su Centro de implementación. Antes de cambiar el despliegue, hay que revisar cuál está conectado a la aplicación para evitar dos publicaciones del mismo cambio.

Para Azure, la configuración utiliza Java 17, Java SE y Linux. El perfil prod recibe estas variables desde el entorno de App Service:
Variable	Qué se configura
SPRING_PROFILES_ACTIVE	prod
DB_URL	La URL JDBC de MySQL con la base minimarket_lp2 y TLS.
DB_USER	El usuario de MySQL.
DB_PASSWORD	Su contraseña de MySQL.
ADMIN_PASSWORD	La clave inicial del administrador, de 12 caracteres o más.
VENDEDOR_PASSWORD	Otra clave inicial, diferente y de 12 caracteres o más.


Esta es la estructura de la conexión, reemplazando el nombre del servidor:
jdbc:mysql://SERVIDOR_MYSQL.mysql.database.azure.com:3306/minimarket_lp2?sslMode=VERIFY_IDENTITY&serverTimezone=America/Lima&characterEncoding=UTF-8
La base debe estar creada y permitir la conexión desde App Service. Flyway se encarga de las tablas. El perfil de producción utiliza cookies seguras, por lo que se accede por HTTPS.

La identidad OpenID Connect del workflow sirve para autenticarse en Azure. Las variables DB_USER y DB_PASSWORD son las credenciales que utiliza la aplicación para conectarse a MySQL.

Para revisar una publicación, además de ver Actions, hay que abrir la URL, iniciar sesión y comprobar una venta, el stock y los reportes. El estado Running de Azure por sí solo no confirma todo ese recorrido.

Los archivos generados de target, los JAR, las claves, los archivos .env y la configuración personal del IDE deben quedar fuera de los cambios publicados. .gitignore ayuda con los archivos nuevos, pero no elimina un archivo que ya se haya subido ni borra contraseñas del historial.
Problemas que se pueden presentar
Problema	Qué revisar
No existe la tabla producto	La base seleccionada y el resultado de las migraciones al iniciar el proyecto.
MySQL muestra Access denied	El usuario, la contraseña, los permisos y el servidor configurado.
No se crean los usuarios iniciales	Que las dos claves sean diferentes y tengan al menos 12 caracteres antes de iniciar con una tabla de usuarios vacía.
El puerto 8080 está ocupado	Detener la otra aplicación o ejecutar el JAR con --server.port=8081.
demo.bat no encuentra el JAR	En una copia de GitHub, compilar y usar el archivo de target.
Azure muestra QuotaExceeded	Las cuotas del plan y los registros de arranque. Si vuelve a aumentar el consumo después de reiniciarse el contador, revisar las detenciones de la aplicación.
El PDF no muestra registros	Los filtros y el rango de fechas. Los reportes también admiten consultas sin resultados.


Documentación del proyecto
- [Configuración de Azure y GitHub](documentacion/AZURE_Y_GITHUB.md)
- [Casos de uso](documentacion/CASOS_DE_USO.md)
- [Guion de demostración](documentacion/GUION_DEMO.md)
- [Lista de verificación](documentacion/VERIFICACION.md)
- Diagramas
Alcance
El proyecto está pensado para una tienda, con importes en soles y cantidades enteras. Las compras a proveedores, las devoluciones, la anulación de ventas, los pagos en línea, la gestión de varias tiendas y los comprobantes electrónicos SUNAT quedan fuera de esta versión. Son módulos que se pueden plantear para una ampliación.
