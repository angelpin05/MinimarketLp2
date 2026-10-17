USE minimarket_lp2;
SELECT codigo,nombre,stock,stock_minimo FROM producto ORDER BY nombre;
SELECT v.id,v.fecha,c.nombre AS cliente,u.username,v.total
FROM venta v JOIN cliente c ON c.id=v.cliente_id JOIN usuario u ON u.id=v.usuario_id ORDER BY v.id DESC;
SELECT venta_id,SUM(subtotal) AS suma_detalle FROM detalle_venta GROUP BY venta_id;
SELECT v.id,v.total,SUM(d.subtotal) AS suma_detalle FROM venta v
JOIN detalle_venta d ON d.venta_id=v.id GROUP BY v.id,v.total HAVING v.total<>SUM(d.subtotal);
-- La última consulta debe devolver cero filas.
