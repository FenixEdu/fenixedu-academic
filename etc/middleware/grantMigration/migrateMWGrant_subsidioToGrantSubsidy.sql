select concat('insert into GRANT_SUBSIDY values (', mws.codigoInterno, ',1,null,null,null,', mws.valor,',null,', mws.chaveContrato,');') as "" from mwgrant_subsidio mws where mws.encargoTotal is null and mws.dataInicio is null and not(mws.valor is null);

select concat('insert into GRANT_SUBSIDY values (', mws.codigoInterno, ',1,null,null,null,0,null,', mws.chaveContrato,');') as "" from mwgrant_subsidio mws where mws.encargoTotal is null and mws.dataInicio is null and mws.valor is null;

select concat('insert into GRANT_SUBSIDY values (', mws.codigoInterno, ',1,',mws.dataInicio,',null,null,', mws.valor,',null,', mws.chaveContrato,');') as "" from mwgrant_subsidio mws where mws.encargoTotal is null and not(dataInicio is null) and not(mws.valor is null);

select concat('insert into GRANT_SUBSIDY values (', mws.codigoInterno, ',1,',mws.dataInicio,',null,null,', mws.valor,',', mws.encargoTotal,',', mws.chaveContrato,');') as "" from mwgrant_subsidio mws where not (mws.encargoTotal is null) and not(dataInicio is null) and not(mws.valor is null);