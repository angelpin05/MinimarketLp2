# Verificación del proyecto

## Pruebas automáticas

El proyecto contiene 17 pruebas de integración en `ProyectoIntegrationTest`. Su resultado se obtiene con `mvn clean verify`. Las pruebas usan H2 en modo MySQL; la ejecución correcta no demuestra por sí sola la conectividad o comportamiento en MySQL real ni el despliegue en Azure.

Los casos cubren venta y totales, reversión completa, cantidades repetidas, token repetido, dos ventas concurrentes, edición con versión antigua, producto inactivo, cantidad negativa, filtros de fechas y cliente, sesión, permisos de vendedor, CSRF, renderizado de vistas, validación de mantenimientos, venta por formulario y reportes JasperReports.

## Checklist manual con MySQL

- [ ] Importar el proyecto en Eclipse con JDK 17.
- [ ] Crear minimarket_lp2 y comprobar las migraciones Flyway.
- [ ] Comprobar que existen las seis tablas y las cuentas tienen contraseñas BCrypt.
- [ ] Registrar, editar, desactivar y reactivar una categoría, producto y cliente.
- [ ] Rechazar código, documento y nombre de categoría duplicados.
- [ ] Rechazar precio cero, stock negativo y documento inválido.
- [ ] Vender 2 arroces y 1 agua con total S/ 11.00 sobre los datos iniciales.
- [ ] Comprobar stock de arroz 38 y agua 49 después de esa venta.
- [ ] Rechazar una venta con un segundo producto sin stock y comprobar reversión completa.
- [ ] Probar dos ventas simultáneas desde sesiones distintas sobre existencias limitadas.
- [ ] Comprobar que una edición antigua de producto no sobrescribe el stock nuevo.
- [ ] Consultar el último día del rango y filtrar por cliente.
- [ ] Descargar y abrir los dos reportes PDF, con resultados y sin resultados.
- [ ] Bloquear categorías, productos y reportes al vendedor, incluso usando la URL directa.
- [ ] Cerrar sesión y comprobar que una página protegida vuelve a pedir login.
- [ ] Reiniciar la aplicación y comprobar que MySQL conserva las ventas.

## Checklist de publicación

- [ ] Repositorio GitHub real con la última versión del código.
- [ ] Job verificar correcto en Actions.
- [ ] Job desplegar correcto sobre main y environment production.
- [ ] URL HTTPS de Azure funcionando con el perfil prod.
- [ ] Base MySQL accesible desde Azure, con ventas persistentes tras reinicio.
- [ ] Capturas del sistema, Actions y commit que corresponde a lo desplegado.
- [ ] Informe y presentación actualizados con los resultados reales.
- [ ] Video de 3 a 5 minutos y ensayo de sustentación.

No marques casillas sin ejecutar la comprobación. Las conclusiones del informe deben reflejar los resultados obtenidos.
