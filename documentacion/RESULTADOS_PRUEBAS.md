# Resultados de comprobación

Fecha: 30 de septiembre de 2026 (America/Lima).

## Batería de integración

Java 17.0.20, Maven 3.9.9, Spring Boot 3.5.12 y perfil test (H2 en modo MySQL).

Resultado: 17 pruebas, 0 fallos, 0 errores y 0 omisiones.

| Prueba | Resultado |
|---|---|
| cantidadNegativaNoModificaStock | Aprobada |
| dosVentasConcurrentesNoVendenMasQueElStock | Aprobada |
| tokenRepetidoNoDuplicaVenta | Aprobada |
| formularioAntiguoNoSobrescribeStockDeUnaVenta | Aprobada |
| errorEnSegundaLineaRevierteTodaLaOperacion | Aprobada |
| loginRealYLogoutInvalidanSesion | Aprobada |
| ventaPorFormularioUsaUsuarioDeLaSesion | Aprobada |
| mantenimientosValidanDuplicadosYBajaLogica | Aprobada |
| fechaFinalIncluyeVentasDelDiaYFiltraCliente | Aprobada |
| accesoSinSesionRedirigeAlLogin | Aprobada |
| reportesGeneranPdfConYSinResultadosYExportanJrxml | Aprobada |
| productoInactivoNoSePuedeVender | Aprobada |
| productosRepetidosSumanCantidadesAntesDeValidarStock | Aprobada |
| vendedorTienePermisosLimitadosEnServidor | Aprobada |
| ventaGuardaDetalleTotalYDescuentaStock | Aprobada |
| pantallasPrincipalesRenderizanConDatosReales | Aprobada |
| csrfBloqueaMutacionSinToken | Aprobada |

La prueba de generación de reportes se repitió tras revisar el formato PDF: 1 prueba adicional aprobada, sin errores. El JAR final se volvió a empaquetar correctamente.

## Ejecución del JAR de demostración

- Inicio real del JAR y login ADMIN
- Venta del ejemplo: 2 arroz + 1 agua = S/ 11.00
- Stock: arroz 38, agua 49
- Stock insuficiente: venta rechazada
- PDF inventario generado desde el JAR
- PDF ventas generado desde el JAR

Los PDF de documentacion/ejemplos contienen datos ficticios del perfil demo. La base se reinicia al terminar la aplicación.

## Comprobaciones pendientes

MySQL real, importación en el Eclipse del estudiante, repositorio GitHub y ejecución de Actions, despliegue en Azure y grabación del video. VERIFICACION.md contiene los criterios manuales; estas tareas no se marcan como realizadas.
