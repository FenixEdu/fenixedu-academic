#select * from mwgrant_contrato mwc inner join GRANT_CONTRACT g on mwc.codigoInterno=g.id_internal;

select concat('UPDATE GRANT_CONTRACT SET DATE_BEGIN_CONTRACT="',mwc.dataInicioContrato,'", DATE_END_CONTRACT="', mwc.dataFimContrato,'" WHERE ID_INTERNAL=',mwc.codigoInterno,';') as "" from mwgrant_contrato mwc inner join GRANT_CONTRACT g on mwc.codigoInterno=g.id_internal;

#select * from mwgrant_subsidio mwc inner join GRANT_SUBSIDY g on mwc.codigoInterno=g.ID_INTERNAL where mwc.dataInicio is not null;

select concat('UPDATE GRANT_SUBSIDY SET DATE_BEGIN_SUBSIDY="',mwc.dataInicio,'" WHERE ID_INTERNAL=',mwc.codigoInterno,';') as "" from mwgrant_subsidio mwc inner join GRANT_SUBSIDY g on mwc.codigoInterno=g.ID_INTERNAL where mwc.dataInicio is not null;

#select * from mwgrant_orientacao mwc inner join GRANT_ORIENTATION_TEACHER g on mwc.codigoInterno=g.ID_INTERNAL;

select concat('UPDATE GRANT_ORIENTATION_TEACHER SET DATE_BEGIN="',mwc.dataInicio,'", DATE_END="', mwc.dataFim, '" WHERE ID_INTERNAL=',mwc.codigoInterno,';') as "" from mwgrant_orientacao mwc inner join GRANT_ORIENTATION_TEACHER g on mwc.codigoInterno=g.ID_INTERNAL;

