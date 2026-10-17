# Especificaciones de casos de uso

## CUS01 Mantener categorías

Actor: Administrador

Objetivo: Registrar, listar, editar y desactivar categorías.

Precondición: Sesión ADMIN activa.

Flujo principal:

1. Abrir Categorías y seleccionar nuevo o editar.
2. Ingresar nombre, descripción y estado.
3. Validar campos y unicidad del nombre.
4. Guardar los cambios y regresar al listado.

Alternativas: Nombre vacío, demasiado largo o duplicado: mostrar errores sin guardar. La baja lógica conserva las relaciones con productos.

Postcondición: Categoría guardada o desactivada.

## CUS02 Mantener productos

Actor: Administrador

Objetivo: Actualizar catálogo, precio y existencias.

Precondición: Sesión ADMIN activa y categoría disponible.

Flujo principal:

1. Abrir Productos y seleccionar nuevo o editar.
2. Ingresar código, nombre, categoría, precio, stock y mínimo.
3. Validar formato, unicidad y valores positivos o no negativos.
4. Al editar, bloquear el registro y comparar su versión.
5. Guardar y mostrar el listado.

Alternativas: Código duplicado, categoría inválida o valor incorrecto: rechazar. Si la versión cambió, recargar antes de editar.

Postcondición: Producto actualizado sin sobrescribir existencias de una venta posterior.

## CUS03 Mantener clientes

Actor: Administrador o vendedor

Objetivo: Registrar clientes y datos de contacto.

Precondición: Sesión autorizada activa.

Flujo principal:

1. Abrir Clientes y seleccionar nuevo o editar.
2. Ingresar documento, nombre, teléfono y correo.
3. Validar documento de ocho u once dígitos y correo cuando se ingrese.
4. Comprobar unicidad y guardar.

Alternativas: Documento duplicado o formato incorrecto: mostrar error. La desactivación impide nuevas ventas con ese cliente.

Postcondición: Cliente guardado o desactivado conservando sus ventas.

## CUS04 Consultar ventas

Actor: Administrador o vendedor

Objetivo: Revisar operaciones por fechas y cliente.

Precondición: Sesión autorizada activa.

Flujo principal:

1. Abrir Ventas.
2. Elegir desde, hasta y cliente opcional.
3. Validar el rango y consultar incluyendo todo el último día.
4. Mostrar filas, total del período y acceso al detalle.

Alternativas: Rango invertido o inválido: rechazar. Si no hay resultados, presentar un listado vacío y total cero.

Postcondición: Ventas consultadas sin modificar la base.

## CUS05 Consultar inventario

Actor: Administrador o vendedor

Objetivo: Localizar productos y necesidades de reposición.

Precondición: Sesión autorizada activa.

Flujo principal:

1. Abrir Inventario.
2. Elegir categoría, texto y filtro opcional de stock bajo.
3. Consultar productos activos que cumplen los filtros.
4. Mostrar existencias, precio, mínimo y situación de reposición.

Alternativas: Sin coincidencias: mostrar listado vacío. Stock bajo significa stock menor o igual al mínimo.

Postcondición: Inventario consultado sin modificarlo.

## CUS06 Reporte de ventas

Actor: Administrador

Objetivo: Descargar un PDF con las ventas filtradas.

Precondición: Sesión ADMIN activa y rango válido.

Flujo principal:

1. Aplicar filtros en la consulta de ventas.
2. Elegir Descargar reporte PDF.
3. Reutilizar los filtros y preparar filas y total.
4. Compilar y completar el diseño JasperReports.
5. Entregar el PDF.

Alternativas: Sin ventas: emitir PDF con encabezado y total cero. Rango inválido: mostrar error. Un vendedor no tiene autorización.

Postcondición: PDF coherente con el período y cliente consultados.

## CUS07 Reporte de inventario

Actor: Administrador

Objetivo: Descargar un PDF con existencias filtradas.

Precondición: Sesión ADMIN activa.

Flujo principal:

1. Aplicar filtros de categoría, texto y stock bajo.
2. Elegir Descargar reporte PDF.
3. Obtener las filas con los mismos filtros.
4. Completar el reporte JasperReports y entregar el PDF.

Alternativas: Sin productos: emitir un PDF con cero resultados. Bloquear la ruta a cuentas VENDEDOR.

Postcondición: PDF con existencias, precios y mínimos de los resultados consultados.

## CUS08 Registrar venta

Actor: Administrador o vendedor

Objetivo: Guardar una operación y actualizar existencias de forma atómica.

Precondición: Sesión autorizada, cliente activo y productos disponibles.

Flujo principal:

1. Abrir Nueva venta y recibir el token del formulario.
2. Elegir cliente y agregar productos con cantidades.
3. Validar el token y obtener el vendedor de la sesión.
4. Acumular cantidades repetidas y bloquear productos por ID.
5. Validar estados y stock, y calcular importes en el servidor.
6. Guardar cabecera, detalles y descuento de stock.
7. Confirmar la transacción y mostrar el detalle.

Alternativas: Cantidad inválida, cliente inactivo, producto inactivo o stock insuficiente: revertir toda la operación. Token usado: impedir duplicación.

Postcondición: Venta y stock confirmados juntos, o sin cambios por error.

## CUS09 Iniciar y cerrar sesión

Actor: Administrador o vendedor

Objetivo: Acceder mediante credenciales y finalizar la autenticación.

Precondición: Cuenta activa para iniciar sesión.

Flujo principal:

1. Ingresar usuario y contraseña.
2. Validar credenciales con Spring Security y BCrypt.
3. Crear o renovar la sesión y mostrar funciones según el rol.
4. Elegir Salir para invalidar la sesión.

Alternativas: Credenciales incorrectas o usuario inactivo: no autenticar. Acceso sin sesión: dirigir al login.

Postcondición: Sesión autenticada o cerrada según la acción.
