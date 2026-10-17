# GitHub y despliegue real en Azure

Esta guía prepara la publicación de la aplicación Java. La carpeta incluye el workflow, pero **todavía no existe un repositorio publicado ni un despliegue en la cuenta del estudiante**. Se requieren una cuenta GitHub y una suscripción Azure con permisos para crear recursos.

## GitHub

Crea un repositorio para este proyecto. Sube la carpeta que contiene `pom.xml`, incluyendo `.github/workflows/ci-azure.yml`, los scripts SQL y la documentación. No subas `target`, `.env` ni credenciales.

Si usas Git local desde la carpeta del proyecto:

```bash
git init
git add .
git commit -m "Implementar ventas e inventario de minimarket"
git branch -M main
git remote add origin URL_DE_TU_REPOSITORIO
git push -u origin main
```

En la pestaña Actions comprueba que el job `verificar` termina correctamente. El job `desplegar` se omite mientras no exista la variable `AZURE_WEBAPP_NAME`. Esto permite configurar la cuenta antes del primer despliegue.

## Recursos de Azure

1. Crea un grupo de recursos para el proyecto.
2. Crea una **App Service Web App**, publicación Code, sistema Linux, pila **Java 17 y Java SE** para ejecutar el JAR de Spring Boot.
3. Crea una base **Azure Database for MySQL Flexible Server** y la base `minimarket_lp2`.
4. Autoriza la conectividad del App Service a MySQL mediante las salidas de la aplicación o una red privada. Verifica las opciones en tu suscripción.
5. En la Web App configura las variables de entorno indicadas abajo y activa HTTPS Only.

La elección del plan depende de tu suscripción. Antes de crear los recursos revisa el costo mostrado por el portal. La guía del curso exige Azure, pero no especifica un plan. El proyecto no afirma que App Service y MySQL sean gratuitos.

## Variables en la Web App

| Nombre | Valor |
|---|---|
| SPRING_PROFILES_ACTIVE | prod |
| DB_URL | jdbc:mysql://TU_SERVIDOR.mysql.database.azure.com:3306/minimarket_lp2?sslMode=VERIFY_IDENTITY&serverTimezone=America/Lima&characterEncoding=UTF-8 |
| DB_USER | Usuario de la base de datos |
| DB_PASSWORD | Contraseña de la base de datos |
| ADMIN_PASSWORD | Contraseña propia de al menos 12 caracteres para el primer inicio |
| VENDEDOR_PASSWORD | Otra contraseña propia de al menos 12 caracteres para el primer inicio |

Guarda los valores en la configuración de Azure o en Key Vault. Las contraseñas de inicialización solo se usan al crear las primeras cuentas en una base vacía. La aplicación no imprime estas claves en sus pantallas ni en sus mensajes de error.

## Autenticación de Actions con Azure

El workflow usa **OpenID Connect**, método recomendado por Microsoft, con `azure/login@v2` y `azure/webapps-deploy@v3`.

1. Crea una aplicación o identidad de despliegue en Microsoft Entra.
2. Asígnale el rol Website Contributor con alcance limitado a tu Web App.
3. Crea una credencial federada de GitHub con issuer `https://token.actions.githubusercontent.com`, audience `api://AzureADTokenExchange` y subject `repo:TU_USUARIO/TU_REPOSITORIO:environment:production`.
4. En GitHub crea el environment **production**. Ese nombre coincide con el workflow y con el subject anterior.
5. En GitHub Secrets and variables > Actions agrega los secretos `AZURE_CLIENT_ID`, `AZURE_TENANT_ID` y `AZURE_SUBSCRIPTION_ID` obtenidos de Azure.
6. Agrega la variable de repositorio `AZURE_WEBAPP_NAME` con el nombre exacto de tu Web App.

El workflow descarga el JAR generado por el job de pruebas y lo publica. No publica desde un pull request. Un nuevo push a main o una ejecución manual en main puede iniciar el despliegue cuando la configuración esté completa.

## Evidencia que debes reunir

Comprueba que abre `/login`, que funcionan las cuentas propias y que las ventas persisten después de reiniciar la Web App. Prueba ambos PDF con datos reales de demostración en MySQL. Guarda la URL pública, una captura de la ejecución de Actions y el identificador del commit desplegado. En las capturas evita mostrar secretos o información de clientes reales.

Si MySQL falla, revisa la cadena JDBC, las reglas de conectividad y los logs de la Web App. Usa `/actuator/health` para comprobar el estado general sin revelar detalles internos.

## Referencia oficial

Microsoft. (s. f.). *Deploy to App Service using GitHub Actions*. Microsoft Learn. https://learn.microsoft.com/en-us/azure/app-service/deploy-github-actions

Esta configuración concreta adapta las instrucciones oficiales al perfil Java del proyecto. Los menús del portal y opciones disponibles pueden variar según la suscripción.
