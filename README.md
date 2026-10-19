# Minimarket LP2

Aplicación académica individual de ventas e inventario para **Lenguaje de Programación II (5484), Cibertec**. Usa Java 17, Spring Boot 3.5.12, Spring MVC, Thymeleaf, Spring Security, JPA, Hibernate, MySQL, Flyway y JasperReports 7.0.3.

## Por dónde empezar

1. Prueba el modo de demostración. Usa una base H2 en memoria y datos ficticios.
2. Importa el proyecto en Eclipse y revisa el flujo de una venta.
3. Configura MySQL. Este es el origen de datos previsto para la entrega del curso.
4. Personaliza la carátula del informe y ensaya la sustentación.
5. Sube el proyecto a tu repositorio y completa el despliegue real en Azure.
  debe coordinarse con él. La base de datos H2 permite probar el proyecto, pero no reemplaza la demostración con MySQL y Azure exigida por la guía.

## Ejecutar la demostración

Instala un **JDK 17**. El paquete incluye `dist/minimarket-lp2.jar`. En Windows puedes abrir `demo.bat`, o ejecutar desde PowerShell:

```powershell
java -jar .\dist\minimarket-lp2.jar --spring.profiles.active=demo
```

Si prefieres compilarlo, instala Maven 3.9 o usa el wrapper incluido:

```powershell
.\mvnw.cmd clean verify
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=demo"
```

Abre <http://localhost:8080>. Cuentas exclusivas del perfil demo:

| Usuario | Contraseña | Rol |
|---|---|---|
| admin | AdminDemo2026! | Administrador |
| vendedor | VentaDemo2026! | Vendedor |

Las ventas y cambios del modo demo desaparecen al cerrar la aplicación. Los perfiles `local` y `prod` exigen contraseñas propias al inicializar una base vacía y las almacenan con BCrypt. El valor de las variables de inicialización no cambia contraseñas de cuentas ya existentes.

## Importar en Eclipse

1. Extrae todo el ZIP en una ruta corta, por ejemplo `C:\Proyectos\MinimarketLP2`.
2. Eclipse: **File > Import > Maven > Existing Maven Projects**.
3. Selecciona la carpeta que contiene `pom.xml` y termina la importación.
4. **Window > Preferences > Java > Installed JREs**: selecciona el JDK 17.
5. Si hay errores de dependencias: clic derecho al proyecto > **Maven > Update Project**.
6. Abre `MinimarketApplication.java` y ejecútalo como **Java Application**.
7. En **Run Configurations > Arguments**, escribe `--spring.profiles.active=demo` para empezar.

Spring Boot inicia su servidor embebido. Este proyecto no necesita agregar un Tomcat externo en Eclipse.

## Usar MySQL

1. Instala e inicia MySQL 8 o superior.
2. En MySQL Workbench ejecuta `database/01_crear_base_mysql.sql`.
3. Configura las variables en **Run Configurations > Environment** de Eclipse. Define `SPRING_PROFILES_ACTIVE=local`, `DB_USER`, `DB_PASSWORD`, `ADMIN_PASSWORD` y `VENDEDOR_PASSWORD`. Las dos últimas deben ser diferentes y tener al menos 12 caracteres.
4. Deja `DB_URL` sin definir para usar `localhost:3306/minimarket_lp2`, o configura tu dirección JDBC.
5. Quita `--spring.profiles.active=demo` de Arguments y ejecuta la aplicación.
6. Flyway crea las seis tablas y los datos ficticios a partir de `src/main/resources/db/migration`.

Alternativa en PowerShell:

```powershell
$env:SPRING_PROFILES_ACTIVE="local"
$env:DB_USER="root"
$env:DB_PASSWORD="TU_CLAVE_MYSQL"
$env:ADMIN_PASSWORD="TU_CLAVE_ADMIN_DE_12_O_MAS"
$env:VENDEDOR_PASSWORD="OTRA_CLAVE_DE_12_O_MAS"
java -jar .\dist\minimarket-lp2.jar
```

El archivo `.env.example` muestra los nombres. **Spring Boot no lo carga automáticamente**. No subas contraseñas al repositorio.

## Funciones y rúbrica

| Requisito | Implementación |
|---|---|
| Base de datos | MySQL, con migraciones Flyway y entidades JPA |
| Roles y sesiones | ADMIN y VENDEDOR, Spring Security, sesión de 30 minutos, cierre de sesión |
| Mantenimiento 1 | Categorías: registrar, listar, editar, desactivar y reactivar |
| Mantenimiento 2 | Productos: código, nombre, categoría, precio y stock |
| Mantenimiento 3 | Clientes: documento, nombre y contacto |
| Consulta 1 | Ventas por rango de fechas y cliente |
| Consulta 2 | Inventario por categoría, código o nombre y stock bajo |
| Reporte 1 | Ventas filtradas en PDF con JasperReports |
| Reporte 2 | Inventario filtrado en PDF con JasperReports |
| Transacción | Venta con cabecera, detalle, cálculo del total y descuento de stock |
| GitHub Actions | Verificación automática y despliegue condicionado a Azure configurado |
| Azure | Perfil prod y guía de configuración. El despliegue real queda por realizar en tu cuenta |

La baja es lógica: conserva el historial y permite reactivar un registro desde Editar. Si tu docente exige borrado físico, revisa ese criterio con él antes de cambiar las claves foráneas.

El administrador usa todas las funciones. El vendedor mantiene clientes, registra ventas y consulta ventas e inventario. El servidor le bloquea los mantenimientos de categorías y productos y la descarga de reportes.

## Una venta de ejemplo

En demo, entra como vendedor y selecciona el cliente de demostración. Agrega 2 unidades de Arroz 1 kg a S/ 4.50 y 1 de Agua 625 ml a S/ 2.00. El total esperado es **S/ 11.00**. El stock de arroz baja de 40 a 38 y el del agua de 50 a 49.

Luego intenta vender 51 botellas de agua. Debes recibir un mensaje de stock insuficiente. No se guarda una venta nueva ni se modifica el stock. Consulta el inventario y el historial para comprobarlo.

## Cómo funciona la transacción

`VentaController` recibe únicamente el cliente, los productos y sus cantidades. `VentaService.registrar` tiene `@Transactional`, calcula importes con `BigDecimal` y obtiene el vendedor de la sesión.

El servicio acumula productos repetidos y bloquea los productos por ID ascendente con `PESSIMISTIC_WRITE`. Una excepción de negocio revierte los cambios. El token único impide procesar dos veces el mismo formulario. Los precios se toman de la base de datos y se copian al detalle junto con el nombre del producto, para preservar el registro histórico.

La edición de productos compara la versión del formulario con la versión actual. Si alguien vendió ese producto mientras se editaba, se solicita recargar para evitar sobrescribir las existencias.

## Arquitectura

| Carpeta | Responsabilidad |
|---|---|
| model | Entidades y relaciones JPA |
| repository | Consultas y acceso a datos con Spring Data JPA |
| service | Reglas de negocio, transacciones y reportes |
| web | Controladores MVC |
| web/form | Datos de formularios y validaciones |
| config | Seguridad e inicialización de cuentas |
| resources/templates | Vistas HTML Thymeleaf |
| resources/static | CSS y JavaScript |
| resources/db/migration | Esquema y datos ficticios versionados |
| src/test | Pruebas de integración |

Los reportes se diseñan en `ReporteService`. `reportes/inventario.jrxml` y `reportes/ventas.jrxml` son exportaciones del mismo diseño para inspeccionarlo en Jaspersoft Studio 7. Los cambios de esos JRXML no alteran automáticamente el diseño Java, que es el usado por la aplicación.

## Verificar

```powershell
.\mvnw.cmd clean verify
```

Las pruebas usan H2 e incluyen reversión completa, concurrencia de ventas, productos repetidos, protección de la edición de stock, permisos por rol, CSRF, sesión real y generación de PDF con y sin resultados. Debes completar también el checklist manual en MySQL y la evidencia de Azure del documento `documentacion/VERIFICACION.md`.

Los resultados de la comprobación del paquete están en `documentacion/RESULTADOS_PRUEBAS.md`. Los dos PDF de `documentacion/ejemplos` muestran la venta de demostración y su inventario posterior.


## Alcance

Se desarrolla para una tienda y una moneda, con cantidades enteras. Los precios representan el importe de venta configurado por el administrador. No calcula impuestos por separado ni emite boletas o facturas SUNAT. El alcance no incluye pagos en línea, compras a proveedores, devoluciones, anulación de ventas, multitienda ni operación sin conexión. Esos módulos pueden añadirse después de cubrir y defender los requisitos mínimos.
