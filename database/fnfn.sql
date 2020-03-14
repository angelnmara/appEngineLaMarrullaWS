CREATE OR REPLACE FUNCTION public.fnfn(
	character
	)
    RETURNS character varying
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$
declare 	
	funcion alias for $1;
	rest varchar;
begin		

	execute '(select array_to_json(array_agg(row_to_json(t))) from (select * from ' || funcion || ') t );' into rest;
	return rest;

	exception when others then		
		return '{"error":"' || replace(SQLERRM, '"', '') || '"}';
		raise notice '% %', SQLERRM, SQLSTATE;
		
end;
$BODY$;

ALTER FUNCTION public.fnfn(character)
    OWNER TO postgres;	