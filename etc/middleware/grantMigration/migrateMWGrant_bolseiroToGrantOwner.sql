select concat('insert into GRANT_OWNER values (', mwb.codigoInterno,',1,',mwb.chavePessoa,',',mwb.numero,',',mwb.dataEnvioCGD,',',mwb.numeroViaCartao,');') as "" from mwgrant_bolseiro mwb where not(mwb.numeroViaCartao is null) and not(mwb.dataEnvioCGD is null);

select concat('insert into GRANT_OWNER values (', mwb.codigoInterno,',1,',mwb.chavePessoa,',',mwb.numero,',null,',mwb.numeroViaCartao,');') as "" from mwgrant_bolseiro mwb where not(mwb.numeroViaCartao is null) and mwb.dataEnvioCGD is null;

select concat('insert into GRANT_OWNER values (', mwb.codigoInterno,',1,',mwb.chavePessoa,',',mwb.numero,',',mwb.dataEnvioCGD,',null);') as "" from mwgrant_bolseiro mwb where mwb.numeroViaCartao is null and not(mwb.dataEnvioCGD is null);
