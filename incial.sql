drop table PEDIDOPC;
drop table ESTADOVENTAPCS;
drop table CONFIGURACIONPC;
drop table EMPRESA;
drop table EMPLEADO;
drop table ROLEMPLEADO;
drop table USUARIO;

create table USUARIO
(
NIFCIF VARCHAR(9) not null primary key,
NOMBRE VARCHAR(30) not null,
DIRECCIONPOSTAL VARCHAR(30),
CIUDAD VARCHAR(30),
PAIS VARCHAR(30),
DIRECCIONELECTRONICA VARCHAR(30),
TELEFONO VARCHAR(9),
PASSWORD VARCHAR(20) not null
);
insert into usuario (nifcif,nombre,direccionpostal,ciudad,pais,direccionelectronica,telefono,password) values
 ('111111U','primero','calle primero', 'ciudad1', 'Spain','primero@','111111','111'),
 ('222222U','segundo','calle segundo', 'ciudad2', 'Spain','segundo@','222222','222'),
 ('333333U','tercero','calle tercero', 'ciudad3', 'Great Britain','tercero@','333333','333'),
 ('444444U','cuarto','calle cuarto', 'ciudad4', 'USA','cuarto@','444444','444'),
 ('555555U','cinco','calle cinco', 'ciudad5', 'USA','cinco@','555555','555');
create table ROLEMPLEADO
(
IDROL SMALLINT not null primary key,
NOMBREROL VARCHAR(20) not null
);
INSERT INTO ROLEMPLEADO VALUES
 (1,'EncargadoAlmacen'),
 (2,'TecnicoTaller'),
 (3,'GerenteVentas');
create table EMPLEADO
(
NIFCIF VARCHAR(9) not null primary key,
FECHACONTRATACION DATE not null,
ROL SMALLINT not null,
 FOREIGN KEY(NIFCIF) REFERENCES USUARIO(NIFCIF),
 FOREIGN KEY(ROL) REFERENCES ROLEMPLEADO(IDROL)
);
insert into empleado (nifcif,fechacontratacion,rol) values
 ('222222U','01/01/2001',1),
 ('333333U', '01/01/2003',1),
 ('444444U','02/02/2002',1);
create table EMPRESA
(
NIFCIF VARCHAR(9) not null primary key,
ESCLIENTE SMALLINT,
ESPROVEEDOR SMALLINT,
 FOREIGN KEY(NIFCIF) REFERENCES USUARIO(NIFCIF)
);
insert into empresa (nifcif,escliente,esproveedor) values
 ('111111U',1,0),
 ('555555U',1,0);
create table CONFIGURACIONPC
(
IDCONFIGURACION NUMERIC(5) not null primary key,
TIPOCPU VARCHAR(9) not null,
VELOCIDADCPU NUMERIC(5) not null,
CAPACIDADRAM NUMERIC(5) not null,
CAPACIDADDD NUMERIC(5) not null,
VELOCIDADTARJETAGRAFICA NUMERIC(5),
MEMORIATARJETAGRAFICA NUMERIC(5),
PRECIO REAL not null
);
insert into configuracionpc
(idconfiguracion,tipocpu,velocidadcpu,capacidadram,capacidaddd,velocidadtarjetagrafica,memoriatarjetagrafica,precio) values
 (1, 'Intel',1,1,1,1,1,456.98),
 (2, 'Intel',2,2,2,2,2,1098.87),
 (3, 'AMD',3,3,3,3,3,348.99),
 (4, 'AMD',4,4,4,4,4,789.88);
create table ESTADOVENTAPCS
(
IDESTADOVENTA SMALLINT not null primary key,
NOMBREESTADOVENTA VARCHAR(20) not null
);
INSERT INTO ESTADOVENTAPCs VALUES
 (1,'Solicitado'),
 (2,'EnProceso'),
 (3,'Completado'),
 (4,'Enviado'),
 (5,'Entregado');
create table PEDIDOPC
(
IDPEDIDO NUMERIC(5) not null primary key,
CANTIDADSOLICITADA NUMERIC(5) not null,
 ESTADO SMALLINT not null,
CONFIGURACIONSOLICITADA NUMERIC(5) not null,
ENCARGADOPOR VARCHAR(9) not null,
 FOREIGN KEY(CONFIGURACIONSOLICITADA) REFERENCES CONFIGURACIONPC(IDCONFIGURACION),
 FOREIGN KEY(ENCARGADOPOR) REFERENCES EMPRESA(NIFCIF),
 FOREIGN KEY(ESTADO) REFERENCES ESTADOVENTAPCS(IDESTADOVENTA)
);
insert into pedidopc (idpedido,cantidadsolicitada,estado,configuracionsolicitada,encargadopor) values
 (1,11,3,2,'111111U'),
 (2,22,1,1,'555555U'),
 (3,33,1,3,'111111U'),
 (4,44,2,2,'555555U');