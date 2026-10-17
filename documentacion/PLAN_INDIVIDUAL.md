# Plan de trabajo individual

El desarrollo usa Kanban con entregas incrementales. El estudiante asume análisis, desarrollo, pruebas y documentación. El docente cumple el rol de cliente que valida el tema y los criterios. Se registra la modalidad individual acordada con él sin inventar integrantes.

| Orden | Entrega | Criterio para cerrar |
|---|---|---|
| 1 | Tema, requisitos y modelo de datos | Cada requisito mínimo tiene un módulo y un caso de uso |
| 2 | Entorno y base de datos | Proyecto abre en Eclipse, migra MySQL y permite iniciar sesión |
| 3 | Seguridad y mantenimientos | Tres mantenimientos completos y bloqueo por rol |
| 4 | Registro de venta | Cabecera y detalle correctos, stock actualizado y reversión comprobada |
| 5 | Consultas y reportes | Filtros correctos y dos PDF JasperReports con y sin resultados |
| 6 | GitHub y Azure | Actions pasa, despliega y la aplicación persiste datos en MySQL |
| 7 | Informe y sustentación | Evidencias reales, referencias APA y ensayo de demostración |

## Avance de semana 11

Presenta el análisis, modelo de datos, seguridad y los tres mantenimientos funcionando con MySQL. Prepara al menos el flujo básico de venta. La guía pide un mínimo de 50 %, así que documenta qué casos de uso ya se pueden demostrar y cuáles siguen pendientes.

## Entrega de semana 14

Demuestra los ocho casos de uso funcionales, sesiones, permisos, GitHub Actions y Azure. Completa el informe y la presentación, y graba el video de 3 a 5 minutos. Usa las semanas de tu calendario de clase, no fechas inventadas.

## Tablero sugerido

Usa las columnas Pendiente, En desarrollo, En pruebas y Terminado. Mantén una tarea de desarrollo a la vez. Las tareas deben tener un resultado comprobable: por ejemplo, «rechazar venta sin stock y verificar que no se guarda el detalle».

## Orden para estudiar el código

1. Entidades y relaciones JPA.
2. Repositorios y consultas.
3. CatalogoService y validaciones de formularios.
4. VentaService y sus pruebas de reversión y concurrencia.
5. Controladores y vistas.
6. Seguridad, JasperReports y pipeline de Azure.

Evita memorizar clases sin ejecutar un ejemplo. Sigue una venta desde el formulario hasta las tablas y explica qué sucede si falla el segundo producto.
