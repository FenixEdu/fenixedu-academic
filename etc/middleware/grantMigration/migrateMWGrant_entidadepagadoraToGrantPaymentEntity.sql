#Centros de custo -- como o key_techer nao pode ser null.. vamos por a 0 (Problema a resolver mais tarde)
select concat('insert into GRANT_PAYMENT_ENTITY values (',e.codigoInterno,',1,"Dominio.grant.contract.GrantCostCenter","',c.numero,'","',c.designacao,'",null,null);') as "" from mwgrant_entidadepagadora e inner join mwgrant_centrocusto c on c.codigoInterno = e.chaveCentroCusto;

#correcao de designação de projectos (pois o select concat nao lida bem com campos null)
update mwgrant_projecto set designacaoProjecto='' where designacaoProjecto is null;

#Projectos
select concat('insert into GRANT_PAYMENT_ENTITY values (',e.codigoInterno,',1,"Dominio.grant.contract.GrantProject","',p.numeroProjecto,'","',p.designacaoProjecto,'",',p.chaveDocente,',null);') as "" from mwgrant_entidadepagadora e inner join mwgrant_projecto p on p.codigoInterno = e.chaveProjecto;
