CREATE TABLE categoria (
 id BIGINT AUTO_INCREMENT PRIMARY KEY, nombre VARCHAR(80) NOT NULL UNIQUE,
 descripcion VARCHAR(200), activo BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE TABLE cliente (
 id BIGINT AUTO_INCREMENT PRIMARY KEY, documento VARCHAR(12) NOT NULL UNIQUE,
 nombre VARCHAR(120) NOT NULL, telefono VARCHAR(20), correo VARCHAR(120), activo BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE TABLE usuario (
 id BIGINT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(50) NOT NULL UNIQUE,
 nombre VARCHAR(120) NOT NULL, password VARCHAR(100) NOT NULL, rol VARCHAR(20) NOT NULL,
 activo BOOLEAN NOT NULL DEFAULT TRUE, CONSTRAINT ck_usuario_rol CHECK (rol IN ('ADMIN','VENDEDOR'))
);
CREATE TABLE producto (
 id BIGINT AUTO_INCREMENT PRIMARY KEY, codigo VARCHAR(30) NOT NULL UNIQUE, nombre VARCHAR(120) NOT NULL,
 categoria_id BIGINT NOT NULL, precio DECIMAL(10,2) NOT NULL, stock INT NOT NULL DEFAULT 0,
 stock_minimo INT NOT NULL DEFAULT 5, activo BOOLEAN NOT NULL DEFAULT TRUE, version BIGINT,
 CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES categoria(id),
 CONSTRAINT ck_producto_precio CHECK (precio>0), CONSTRAINT ck_producto_stock CHECK (stock>=0),
 CONSTRAINT ck_producto_minimo CHECK (stock_minimo>=0)
);
CREATE TABLE venta (
 id BIGINT AUTO_INCREMENT PRIMARY KEY, fecha TIMESTAMP NOT NULL, cliente_id BIGINT NOT NULL,
 usuario_id BIGINT NOT NULL, total DECIMAL(12,2) NOT NULL, token VARCHAR(36) NOT NULL UNIQUE,
 CONSTRAINT fk_venta_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id),
 CONSTRAINT fk_venta_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id),
 CONSTRAINT ck_venta_total CHECK (total>=0)
);
CREATE TABLE detalle_venta (
 id BIGINT AUTO_INCREMENT PRIMARY KEY, venta_id BIGINT NOT NULL, producto_id BIGINT NOT NULL,
 nombre_producto VARCHAR(120) NOT NULL, cantidad INT NOT NULL, precio_unitario DECIMAL(10,2) NOT NULL,
 subtotal DECIMAL(12,2) NOT NULL,
 CONSTRAINT fk_detalle_venta FOREIGN KEY (venta_id) REFERENCES venta(id),
 CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id) REFERENCES producto(id),
 CONSTRAINT ck_detalle_cantidad CHECK (cantidad>0), CONSTRAINT ck_detalle_precio CHECK (precio_unitario>0),
 CONSTRAINT ck_detalle_subtotal CHECK (subtotal>=0)
);
CREATE INDEX idx_venta_fecha ON venta(fecha);
CREATE INDEX idx_venta_cliente_fecha ON venta(cliente_id,fecha);
