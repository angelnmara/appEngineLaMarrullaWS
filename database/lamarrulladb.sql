CREATE EXTENSION pgcrypto;

drop table if exists tbModuloEmpresa;

drop table if exists tbModulo;

drop table if exists tbCatObj;

drop table if exists tbCatCampo;

drop table if exists tbCatTpPer;

drop table if exists tbUsuCveApi;

drop table if exists tbUsuPassw;

drop table if exists tbAmortiza;

drop table if exists tbUsuTabla;

drop table if exists tbPantallaRol;

drop table if exists tbPantallas;

drop table if exists tbTipoPantalla;

drop table if exists tbGruposEmpresas;

drop table if exists tbGruposTareas;

drop table if exists tbGrupos;

drop table if exists tbTrabajoTarea;

drop table if exists tbTareas;

drop table if exists tbTrabajos;

drop table if exists tbTareasStatus;

drop table if exists tbUsuEmpresaRol;

drop table if exists tbUsuEmpresa;

drop table if exists tbUsu;

drop table if exists tbMemberRol;

drop table if exists tbTpGastoMes;

drop table if exists tbPlazoTpGasto;

drop table if exists tbPlazo;

drop table if exists tbTpGasto;

drop table if exists tbGastadoMes;

drop table if exists tbPresupuestoMes;

drop table if exists tbMes;

drop table if exists tbRol;

drop table if exists tbEmpresa;

drop table if exists tbAsistencia;

drop table if exists tbUsuarioAsistencia;

create table if not exists tbCatObj(fiIdObj serial primary key,
				fcObj varchar(4),
				fcObjDesc varchar(100));

create table if not exists tbCatCampo(fiIdCampo serial primary key,
				fcCampo varchar(5) unique,
				fcCampoDesc varchar(100));	

create table if not exists tbCatTpPer(fiIdTpPer serial primary key,
				fcTpPerDesc char(50), 
				fnTpPerStat boolean default true);

create table if not exists tbRol(fiIdRol serial primary key,
				fcRolDesc varchar(100),
                                fnRolStat boolean default true);

create table if not exists tbMemberRol(fiIdMemberRol serial primary key,
				fiIdRol int references tbRol(fiIdRol),
                                fiIdRolMember int references tbRol(fiIdRol),
                                fnMemberRolStat boolean default true,                                
                                unique(fiIdRol, fiIdRolMember));

create table if not exists tbEmpresa(fiIdEmpresa serial primary key,
				fcEmpresaNom varchar(100) not null,
				fcEmpresaStat bool default true not null, 
				fdEmpresaFecCreacion timestamp default CURRENT_TIMESTAMP,
				fdEmpresaFecBaja timestamp default null);

create table if not exists tbModulo(fiIdModulo serial primary key,
				fcModulo varchar(100) not null,
				fcModuloDesc varchar(200) not null,
				fcModuloImg varchar(100) not null,
				fiModuloOrden int default 0,
				fiModuloPadre int default null,					
				fnModuloStat boolean default true,
				fdModuloFechaCreacion timestamp default CURRENT_TIMESTAMP,
				fdModuloFechaBaja timestamp default null);

create table if not exists tbModuloEmpresa(fiIdEmpresaModulo serial primary key,
					fiIdEmpresa int references tbEmpresa(fiIdEmpresa),
					fiIdModulo int references tbModulo(fiIdModulo));				

create table if not exists tbUsu (fiIdUsu serial primary key,
                    fcUsuNom char(50) unique,         
                    fcUsuCorrElec char(100) unique,
                    fnUsuStat boolean default true);                    

create table if not exists tbUsuEmpresa(fiIdUsuEmpresa serial primary key,
					fiIdUsu int references tbUsu(fiIdUsu),
					fiIdEmpresa int references tbEmpresa(fiIdEmpresa));

create table if not exists tbUsuEmpresaRol(fiIdUsuEmpresaRol serial primary key,
					fiIdUsuEmpresa int references tbUsuEmpresa(fiIdUsuEmpresa),
					fiIdRol int references tbRol(fiIdRol));					

create table if not exists tbUsuCveApi(fiIdUsuCveAPI serial primary key,
				fiIdUsu int references tbusu(fiIdUsu),
                                fcCveAPI varchar(1000) not null unique,
                                fnStatCveAPI boolean default true,
                                fdFecIniCveAPI timestamp default CURRENT_TIMESTAMP,
                                fdFecFinCveAPI timestamp default null);     

create table if not exists tbUsuPassw(fiIdUsuPassw serial primary key,
				fiIdUsu int references tbusu(fiIdUsu),
                                fcUsuPassw TEXT not null default '$2a$06$ytswCy0JbEXcAZFd6k6X9.1Ml6AsTn9rpUO78hYaGsN0LixdtBubm',
                                fcSecPassw TEXT not null default '',
                                fcSalt TEXT not null default '',
                                fnStatUsuPassw boolean default true,
                                fdFecIniUsuPassw timestamp default CURRENT_TIMESTAMP,
                                fdFecFinUsuPassw timestamp null);

create table if not exists tbPlazo(fiIdPlazo serial primary key,				
                                fcPlazoNom varchar(200) not null,
                                fiPlazoNoDias int,
                                fnPlazoStat boolean default true,
                                fdPlazoFecIni timestamp default CURRENT_TIMESTAMP,
                                fdPlazoFecFin timestamp null);

create table if not exists tbUsuTabla(fiIdUsuTabla serial primary key,
				fiIdUsu int references tbusu(fiidusu));                                

create table if not exists tbAmortiza(fiNumPagoAmortiza serial primary key,
		fiIdUsuTabla int references tbUsuTabla(fiIdUsuTabla),
		flAmortizaPago decimal(18,2),
		flAmortizaInteresesPagados decimal(18,2),
		flAmortizaCapitalPagado decimal(18,2),
		flAmortizaMontoPrestamo decimal(18,2));

create table if not exists tbTpGasto(fiIdTipoGasto serial primary key,
				fcTipoGasto varchar(500));

create table if not exists tbPlazoTpGasto(fiIdPlazoTipoGasto serial primary key,
					fiIdPlazo int references tbPlazo(fiIdPlazo),
					fiIdTipoGasto int references tbTpGasto(fiIdTipoGasto),
					fmPlazoTipoGastoMonto money not null);

create table if not exists tbMes(fiIdMes serial primary key,
				fcMes varchar(20));

create table if not exists tbTpGastoMes(fiIdTipoGastoMes serial primary key,
					fiIdMes int references tbMes(fiIdMes),
					fiIdPlazoTipoGasto int references tbPlazoTpGasto(fiIdPlazoTipoGasto));				

create table if not exists tbPresupuestoMes(fiIdPresupuestoMes serial primary key,
					fiIdMes int references tbMes(fiIdMes),
					fmPresupuestoMes money);

create table if not exists tbGastadoMes(fiGastadoMes serial primary key,
					fiIdMes int references tbMes(fiIdMes),
					fmGastadoMes money);

create table if not exists tbTipoPantalla(fiIdTipoPantalla serial primary key,
					fcTipoPantallaDesc varchar(200),
					fnTipoPantallaStat bool default true not null,
					fiTipoPantallaUsuUltCamb int references tbUsu(fiIdUsu));

create table if not exists tbPantallas(fiIdPantalla serial primary key,
				fiIdTipoPantalla int references tbTipoPantalla(fiIdTipoPantalla),
				fcPantallaNom varchar(100) not null,
				fcPantallaDesc varchar(1000),
				fcPantallaURL varchar(500),
				fnPantallaStat bool default true not null,
				fiPantallaUsuUltCamb int references tbUsu(fiIdUsu));	

create table if not exists tbPantallaRol(fiIdPantallaRol serial primary key,
					fiIdPantalla int references tbPantallas(fiIdPantalla),
					fiIdRol int references tbRol(fiIdRol),
					fnPantallaRol bool default true,
					fiPantallaRolUsuUltCamb int references tbUsu(fiIdUsu));

create table if not exists tbGrupos(fiIdGrupo serial primary key,
				fcGrupoNom varchar(200) not null,
				fcGrupoDesc varchar(1000),
				fnGrupoStat bool default true not null,
				fiGrupoUsuUltCamb int references tbUsu(fiIdUsu));

create table if not exists tbTareas(fiIdTarea serial primary key,
				fcTareaNom varchar(100) not null,
				fcTareaDesc varchar(1000),
				fcTareaStat bool default true not null,
				fiTareaUsuUltCamb int references tbUsu(fiIdUsu));

create table if not exists tbGruposEmpresas(fiIdGrupoEmpresa serial primary key,
					fiIdGrupo int references tbGrupos(fiIdGrupo),
					fiIdEmpresa int references tbEmpresa(fiIdEmpresa),
					fnGrupoEmpresaStat bool default true not null,
					fiGruposEmpresasUsuUltCamb int references tbUsu(fiIdUsu) not null);

create table if not exists tbGruposTareas(fiIdGrupoTarea serial primary key,
					fiIdGrupo int references tbGrupos(fiIdGrupo),
					fiIdTarea int references tbTareas(fiIdTarea),
					fiGrupoTareaStat bool default true not null,
					fiGrupoTareaUsuUltCamb int references tbUsu(fiIdUsu) not null);		

create table if not exists tbTrabajos(fiIdTrabajo serial primary key,
				fcTrabajoNom varchar(100) not null,
				fcTrabajoDesc varchar(1000) not null,
				fcTrabajoDireccion varchar(1000) not null,
				fcTrabajoLote varchar(1000) not null,
				fcTrabajoCP varchar(10) not null,
				fcTrabajoReferencia varchar(5000) not null,
				flTrabajoLat decimal(28,10) not null,
				flTrabajoLong decimal(28,10) not null,
				fdTrabajoFecUltAct date default CURRENT_TIMESTAMP,
				fnTrabajoStat bool default true not null,
				fiTrabajoUsuUltCamb int references tbUsu(fiIdUsu) not null);

create table if not exists tbTareasStatus(fiIdTareaStatus serial primary key,
			fcTareasEstatusNom varchar(100),
			fnTareaEstatusStat bool default true,
			fiTareaStatusUsuUltCamb int references tbUsu(fiIdUsu) not null);

create table if not exists tbTrabajoTarea(fiIdTrabajoTarea serial primary key,
			fiIdTrabajo int references tbTrabajos(fiIdTrabajo),
			fiIdTarea int references tbTareas(fiIdTarea),
			fiIdTareasStatus int references tbTareasStatus(fiIdTareaStatus),
			fiTrabajoTareaUsuUltCamb int references tbUsu(fiIdUsu) not null,
			UNIQUE(fiIdTrabajo, fiIdTarea));

create table if not exists tbAsistencia(fiIdAsistencia serial primary key,
			Numero varchar(10),
			VerifMode varchar(2),
			InOutMode varchar(2),
			Ano varchar(4),
			Mes varchar(2),
			Dia varchar(2),
			Hora varchar(2),
			Minuto varchar(2),
			Segundo varchar(2),
			Codigo varchar(2));

create table if not exists tbUsuarioAsistencia(fiIdUsuarioAsistencia serial primary key,
					Numero varchar(10),
					PNombre varchar(50),
					SNombre varchar(50),
					APaterno varchar(50),
					AMaterno varchar(50));				

insert into tbEmpresa(fcEmpresaNom)values('Neurosys');
insert into tbEmpresa(fcEmpresaNom)values('Credifast');

insert into tbRol(fcRolDesc) values ('Admin');
insert into tbRol(fcRolDesc) values ('Gerente');
insert into tbRol(fcRolDesc) values ('Ejecutivo');
insert into tbRol(fcRolDesc) values ('Vendedor');
insert into tbRol(fcRolDesc) values ('Usuario');

insert into tbUsu(fcUsuNom, fcUsuCorrElec) 
values ('DAVER', 'angelnmara@hotmail.com') returning fiIdUsu;

insert into tbUsuEmpresa(fiIdUsu, fiIdEmpresa)
values(1,1);

insert into tbUsuEmpresa(fiIdUsu, fiIdEmpresa)
values(1,2);

insert into tbTareasStatus(fcTareasEstatusNom, fitareastatususuultcamb)values('Nuevo', 1);
insert into tbTareasStatus(fcTareasEstatusNom, fitareastatususuultcamb)values('Asignado', 1);
insert into tbTareasStatus(fcTareasEstatusNom, fitareastatususuultcamb)values('En proceso', 1);
insert into tbTareasStatus(fcTareasEstatusNom, fitareastatususuultcamb)values('Terminado', 1);
insert into tbTareasStatus(fcTareasEstatusNom, fitareastatususuultcamb)values('Cancelado', 1);

insert into tbTrabajos(
		fcTrabajoNom,
		fcTrabajoDesc, 
		fcTrabajoDireccion, 
		fcTrabajoLote, 
		fcTrabajoCP,
		fcTrabajoReferencia, 
		flTrabajoLat, 
		flTrabajoLong,
		fitrabajousuultcamb)
		values('Primer trabajos',
		'descripcion del primer trabajo',
		'Sierra dorada 29',
		'lt 39',
		'55790',
		'Porton sin pintar',
		106.52658,
		-21.36979,
		1);

insert into tbTareas(fcTareaNom)values('Pintar puerta');
insert into tbTareas(fcTareaNom)values('Pintura externa');
insert into tbTareas(fcTareaNom)values('Pintura interna');
insert into tbTareas(fcTareaNom)values('Limpieza ventanas');
insert into tbTareas(fcTareaNom)values('Limpieza carpeta');

insert into tbTrabajoTarea(fiIdTrabajo,
		fiIdTarea,
		fiIdTareasStatus,
		fiTrabajoTareaUsuUltCamb)values(1,1,1,1);

insert into tbGrupos(fcGrupoNom)values('Pintores');
insert into tbGrupos(fcGrupoNom)values('Limpieza');

insert into tbGruposTareas(fiIdGrupo, fiIdTarea, figrupotareausuultcamb)values(1,1, 1);
insert into tbGruposTareas(fiIdGrupo, fiIdTarea, figrupotareausuultcamb)values(1,2, 1);

insert into tbGruposEmpresas(fiIdGrupo, fiIdEmpresa, fiGruposEmpresasUsuUltCamb)values(1,1,1);
insert into tbGruposEmpresas(fiIdGrupo, fiIdEmpresa, fiGruposEmpresasUsuUltCamb)values(1,1,1);

insert into tbTipoPantalla(fcTipoPantallaDesc)values('WEB');
insert into tbTipoPantalla(fcTipoPantallaDesc)values('Mobil');

insert into tbPantallas(fiIdTipoPantalla, fcPantallaNom, fcPantallaURL)values(1, 'Login', '\Login');

insert into tbRol(fcRolDesc)values('Admin');

insert into tbPantallaRol(fiIdPantalla, fiIdRol)values(1,1);

insert into tbMes(fcMes)values('Enero');
insert into tbMes(fcMes)values('Febrero');
insert into tbMes(fcMes)values('Marzo');
insert into tbMes(fcMes)values('Abril');
insert into tbMes(fcMes)values('Mayo');
insert into tbMes(fcMes)values('Junio');
insert into tbMes(fcMes)values('Julio');
insert into tbMes(fcMes)values('Agosto');
insert into tbMes(fcMes)values('Septiembre');
insert into tbMes(fcMes)values('Octubre');
insert into tbMes(fcMes)values('Noviembre');
insert into tbMes(fcMes)values('Diciembre');

insert into tbPlazo(fcPlazoNom, fiPlazoNoDias)values('Semanal', 7);
insert into tbPlazo(fcPlazoNom, fiPlazoNoDias)values('Quincenal', 15);
insert into tbPlazo(fcPlazoNom, fiPlazoNoDias)values('Mensual', 30);
insert into tbPlazo(fcPlazoNom, fiPlazoNoDias)values('Anual', 360);
insert into tbPlazo(fcPlazoNom, fiPlazoNoDias)values('Bimestral', 60);								

insert into tbTpGasto(fcTipoGasto) values ('Despensa');
insert into tbTpGasto(fcTipoGasto) values ('Colegiaturas');
insert into tbTpGasto(fcTipoGasto) values ('Gasto');
insert into tbTpGasto(fcTipoGasto) values ('Diversión');
insert into tbTpGasto(fcTipoGasto) values ('Cable, Télefono e Internet');						
insert into tbTpGasto(fcTipoGasto) values ('Cable');
insert into tbTpGasto(fcTipoGasto) values ('Teléfono');
insert into tbTpGasto(fcTipoGasto) values ('Internet');
insert into tbTpGasto(fcTipoGasto) values ('Gas');
insert into tbTpGasto(fcTipoGasto) values ('Luz');
insert into tbTpGasto(fcTipoGasto) values ('Agua');

insert into tbPlazoTpGasto(fiIdPlazo, fiIdTipoGasto, fmPlazoTipoGastoMonto) values(3,1, 1400); 
insert into tbPlazoTpGasto(fiIdPlazo, fiIdTipoGasto, fmPlazoTipoGastoMonto) values(2,2, 2600);
insert into tbPlazoTpGasto(fiIdPlazo, fiIdTipoGasto, fmPlazoTipoGastoMonto) values(1,3, 1400);
insert into tbPlazoTpGasto(fiIdPlazo, fiIdTipoGasto, fmPlazoTipoGastoMonto) values(2,4, 800);
insert into tbPlazoTpGasto(fiIdPlazo, fiIdTipoGasto, fmPlazoTipoGastoMonto) values(3,5, 850);
insert into tbPlazoTpGasto(fiIdPlazo, fiIdTipoGasto, fmPlazoTipoGastoMonto) values(3,9, 200);
insert into tbPlazoTpGasto(fiIdPlazo, fiIdTipoGasto, fmPlazoTipoGastoMonto) values(5,10, 300);
insert into tbPlazoTpGasto(fiIdPlazo, fiIdTipoGasto, fmPlazoTipoGastoMonto) values(4,11, 800);

insert into tbCatObj(fcObj, fcObjDesc) values ('tb', 'tabla');
insert into tbCatObj(fcObj, fcObjDesc) values ('sp', 'store procedure');        
insert into tbCatObj(fcObj, fcObjDesc) values ('fn', 'funcion');
insert into tbCatObj(fcObj, fcObjDesc) values ('tr', 'trigger');
insert into tbCatObj(fcObj, fcObjDesc) values ('vw', 'vista');

insert into tbCatCampo(fcCampo, fcCampoDesc) values('fi', 'formato entero');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('fc', 'formato caracter');                                    
insert into tbCatCampo(fcCampo, fcCampoDesc) values('fn', 'formato boolean');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('fd', 'formato fecha');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('fm', 'formato money');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('fl', 'formato decimal');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('Desc', 'Descripcion campo');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('Id', 'Campo Identificador');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('Stat', 'Status del campo');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('Tp', 'Tipo');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('Ap', 'Apellido');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('Per', 'Persona');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('Cat', 'Catalogo');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('Dt', 'Datos');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('Cve', 'Clave');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('API', 'Aplicacion');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('Fec', 'fecha');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('Ini', 'Inicial');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('Passw', 'Contraseña / Password');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('Abr', 'Abrebiado');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('Lat', 'Latitud');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('Long', 'Longitud');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('Tam', 'Tamaño');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('Dir', 'Direccion');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('Corr', 'Correo');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('Elec', 'Electronico');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('Obj', 'Objeto');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('No', 'Numero');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('Nom', 'Nombre');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('Cte', 'Cliente');
insert into tbCatCampo(fcCampo, fcCampoDesc) values('Img', 'Imagen');

insert into tbCatTpPer(fcTpPerDesc) values ('Fisica');
insert into tbCatTpPer(fcTpPerDesc) values ('Moral');
insert into tbCatTpPer(fcTpPerDesc) values ('Fisica con actividad empresarial');

insert into tbMemberRol(fiIdRol, fiIdRolMember) values (1,2);
insert into tbMemberRol(fiIdRol, fiIdRolMember) values (1,3);

insert into tbUsuCveApi(fiIdUsu, fcCveAPI) values(1,'1234');

insert into tbUsuPassw(fiIdUsu) values (1);

insert into tbModulo(fcModulo, fcModuloDesc, fcModuloImg) 
values ('Credito Simple', 'Pantalla para generar credito simple', 'ic_menu_send');
									 
insert into tbModulo(fcModulo, fcModuloDesc, fcModuloImg) 
values ('Arrendamiento Puro', 'Pantalla para generar arrendamiento puro', 'ic_menu_share');

insert into tbModuloEmpresa(fiIdEmpresa, fiIdModulo) values (2,1);
insert into tbModuloEmpresa(fiIdEmpresa, fiIdModulo) values (2,2);

select *
from tbUsuPassw;

select *
from tbPlazo;

select *
from tbUsuTabla;

select *
from tbAmortiza;