-- Todos los clientes y registros de este script son ficticios.
INSERT INTO categoria(nombre,descripcion,activo) VALUES
 ('Abarrotes','Productos de despensa',TRUE),('Bebidas','Bebidas sin alcohol',TRUE),('Limpieza','Limpieza del hogar',TRUE);
INSERT INTO cliente(documento,nombre,telefono,correo,activo) VALUES
 ('00000000','Cliente de demostración','','',TRUE),('00000001','Cliente de prueba','','',TRUE);
INSERT INTO producto(codigo,nombre,categoria_id,precio,stock,stock_minimo,activo,version) VALUES
 ('ARR001','Arroz 1 kg',1,4.50,40,10,TRUE,0),('AZU001','Azúcar 1 kg',1,4.00,25,8,TRUE,0),
 ('AGU001','Agua 625 ml',2,2.00,50,12,TRUE,0),('JUG001','Jugo 1 litro',2,6.50,5,8,TRUE,0),
 ('DET001','Detergente 500 g',3,7.90,15,5,TRUE,0);
