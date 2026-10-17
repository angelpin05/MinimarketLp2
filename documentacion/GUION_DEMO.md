# Guion de video de aproximadamente cuatro minutos

| Tiempo | Mostrar | Explicación |
|---|---|---|
| 0:00 a 0:25 | Inicio y objetivo | Sistema web de ventas e inventario para un minimarket, desarrollado individualmente |
| 0:25 a 1:05 | Admin, categorías, productos y clientes | Mostrar un registro y una edición. Explicar baja lógica y roles |
| 1:05 a 2:10 | Sesión vendedor y venta con dos productos | Identificar cantidades, calcular total y comprobar el descuento de stock |
| 2:10 a 2:40 | Venta con stock insuficiente | Mostrar el mensaje y que la operación no deja una venta incompleta |
| 2:40 a 3:20 | Consultas y ambos reportes | Filtrar ventas e inventario y abrir los PDF JasperReports como administrador |
| 3:20 a 4:00 | Azure, repositorio y Actions | Mostrar URL real y ejecución correcta. Cerrar con alcance y mejora futura |

Usa el despliegue real cuando esté listo. Si todavía estás preparando el avance, presenta esa situación con precisión. El video no reemplaza la sustentación en clase.

## Preguntas para la sustentación

- ¿Dónde está el patrón MVC? Modelos en model, vistas en templates y controladores en web. Los servicios contienen las reglas de negocio.
- ¿Qué aporta JPA y qué hace Hibernate? JPA define el mapeo y acceso persistente. Hibernate implementa ese contrato y ejecuta el SQL.
- ¿Por qué `@Transactional`? La venta, detalles y actualización de stock deben confirmarse o revertirse juntos.
- ¿Qué ocurre si dos vendedores compran el mismo stock? El bloqueo pesimista serializa la modificación del producto y la segunda operación revalida sus existencias.
- ¿Por qué BigDecimal? Permite representar y calcular importes decimales evitando los errores habituales de `double`.
- ¿Cómo controla roles? Spring Security valida permisos en el servidor antes de ejecutar el controlador.
- ¿Qué es una sesión? El servidor conserva la autenticación entre solicitudes mediante una cookie de sesión. Logout la invalida.
- ¿Qué hace Actions? Compila y prueba cada cambio; despliega main cuando Azure ya está configurado.
- ¿Qué falta para usarlo en un negocio real? Validar el proceso con el negocio, respaldos y operación, y desarrollar los módulos fuera del alcance acordado.
