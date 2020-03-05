-- Function: public.fnapi(character varying)

 --DROP FUNCTION public.fnapi(int, character, character, character);

CREATE OR REPLACE FUNCTION public.fnapi(int, character, character default '', character default '', int default 0)
  RETURNS character varying AS
$BODY$
declare 
	metodo alias for $1;
	tabla alias for $2;
	campos alias for $3;
	res alias for $4;
	idTabla alias for $5;
	rest varchar;
	countCampos int;	
	insertT character;
	tbcampostempCount int;
	tbrestempCount int;
	salida varchar(3000);
	prmaryKey varchar(100);
	tieneLlave int;
	lastId int;
	updateStr varchar;
	--strArrCampos text[];
	--strArrRes text[];	
begin		
	drop table if exists tbcampostemp;
	drop table if exists tbResTemp;
	create temp table tbCamposTemp(fiIdCampos serial, fcCampo varchar);
	create temp table tbResTemp(fiIdRes serial, fcRes varchar);

	SELECT column_name into prmaryKey
	FROM information_schema.columns
	where table_name = tabla
	and column_default like '%nextval%';

	insert into tbcampostemp(fcCampo)
	select trim(a) from unnest(string_to_array(campos, ',')) as a;

	insert into tbrestemp(fcRes)
	select trim(a) from unnest(string_to_array(res, ',')) as a;

	-- GET
	if metodo = 1 then
			-- GET Sin Id
			if idTabla = 0 then
				raise notice 'entra 0';
				execute '(select array_to_json(array_agg(row_to_json(t))) from (select * from ' || tabla || ' order by ' || prmarykey || ') t );' into rest;
			-- GET Con Id
			else
				raise notice 'entra > 0';
				execute '(select array_to_json(array_agg(row_to_json(t))) from (select * from ' || tabla || ' where ' || prmarykey || ' = ' || idTabla || ') t);' into rest;
			end if;
			return '{"' || tabla || '":' || rest || '}';
		-- POST	
		elsif metodo = 2 then
			--strArrCampos = string_to_array(campos, ',');
			--strArrRes = string_to_array(res, ',');			

			select count(*) into tieneLlave
			from tbcampostemp
			where fccampo = prmaryKey;			
			
			select count(*) into countCampos
			from (SELECT column_name
			FROM information_schema.columns
			where table_name = tabla
			and is_nullable = 'NO'
			and (column_default = '') is not false) a
			left outer join tbcampostemp b
			on a.column_name = b.fcCampo
			where b.fcCampo is null;

			select count(*) into tbcampostempCount
			from tbcampostemp;

			select count(*) into tbrestempCount
			from tbrestemp;
			
			if tieneLlave > 0 then
				raise notice 'entra aquí se tiene que insertar llave';
				salida := 'no se puede insertar una llave: ' || prmaryKey;							
			elsif countCampos = 0 then
				if tbcampostempCount = tbrestempCount then
					campos = '(' || campos || ')';
					res = '(' || res || ')';													
					execute('insert into ' || tabla || campos || 'values' || res || 'RETURNING ' || prmaryKey) into lastId;
					--raise notice ltrim ('(select array_to_json(array_agg(row_to_json(t))) from (select trim(*) from ');-- || tabla || ' where ' || prmaryKey || ' = ' || lastId || ') t);';
					execute '(select array_to_json(array_agg(row_to_json(t))) from (select * from ' || tabla || ' where ' || prmaryKey || ' = ' || lastId || ') t);' into rest;
					salida := '{"' || tabla || '":' || rest || '}';
				else
					salida := 'No se tienen los mismos campos y los mismos res ' || tbcampostempCount || ' ' || tbrestempCount;
				end if;
			else
				salida := '{"API":"Faltan campos requeridos para insertar"}';
			end if;
			return salida;
		-- PUT		
		elsif metodo = 3 then				
			select array_to_string(array_agg(a.fcCampo || ' = ' || b.fcRes), ',') into updateStr
			from tbcampostemp a
			inner join tbResTemp b
			on a.fiIdCampos = b.fiIdRes;
			raise notice '% ', salida;
			execute ('update ' || tabla || ' set ' || updateStr || ' where ' || prmaryKey || ' = ' || idTabla);
			GET DIAGNOSTICS salida := ROW_COUNT;

			return '{"' || tabla || '":"Se actualizaron ' || salida || ' registros"}';
		-- DELETE
		elsif metodo = 4 then
			execute('delete from ' || tabla || ' where ' || prmaryKey || ' = ' || idTabla);
			GET DIAGNOSTICS salida := ROW_COUNT;						
			return '{"' || tabla || '":"Se eliminaron ' || salida || ' registros."}';
		else 
			return 'dos';
	end if;	
	exception when others then		
		return '{"error":"' || replace(SQLERRM, '"', '') || '"}';
		raise notice '% %', SQLERRM, SQLSTATE;
		
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION public.fnapi(int, character, character, character, int)
  OWNER TO postgres;


select *
from public.fnapi(3, 'tbusu', 'fcUsuNom, fcUsuCorrElec', '''drinconABC'', ''VADa@bcd.cso''',35);

select *
from public.fnapi(2, 'tbusu', 'fcusunom, fcusucorrelec', '''dave'', ''rincon''', 35);