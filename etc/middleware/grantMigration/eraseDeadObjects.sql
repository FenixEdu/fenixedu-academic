#select * from mwgrant_bolseiro b
#left join mwgrant_migracao_pessoa p
#on b.chavePessoa=p.codigoInterno where p.codigoInterno is null;
#results:1

select concat('delete from mwgrant_bolseiro where codigoInterno=',b.codigoInterno,';') as "" from mwgrant_bolseiro b
left join mwgrant_migracao_pessoa p
on b.chavePessoa=p.codigoInterno where p.codigoInterno is null;


#select * from mwgrant_migracao_docente d
#left join mwgrant_migracao_pessoa p
#on d.chavePessoa=p.codigoInterno where p.codigoInterno is null;
#results:0


#select c.* from mwgrant_contrato c
#left join mwgrant_bolseiro b
#on c.chaveBolseiro=b.codigoInterno where b.codigoInterno is null;
#results:1

select concat('delete from mwgrant_contrato where codigoInterno=',c.codigoInterno,';') as "" from mwgrant_contrato c
left join mwgrant_bolseiro b
on c.chaveBolseiro=b.codigoInterno where b.codigoInterno is null;

#select c.* from mwgrant_contrato c
#left join mwgrant_orientacao o
#on c.chaveOrientadorActual=o.codigoInterno where o.codigoInterno is null;
#results:1063


#select s.* from mwgrant_subsidio s
#left join mwgrant_contrato c
#on s.chaveContrato=c.codigoInterno where c.codigoInterno is null;
#results:10

select concat('delete from mwgrant_subsidio where codigoInterno=',s.codigoInterno,';') as "" from mwgrant_subsidio s
left join mwgrant_contrato c
on s.chaveContrato=c.codigoInterno where c.codigoInterno is null;

#select c.* from mwgrant_comparticipacao c
#left join mwgrant_subsidio s
#on c.chaveSubsidio=s.codigoInterno where s.codigoInterno is null;
#results:18

select concat('delete from mwgrant_comparticipacao where codigoInterno=',c.codigoInterno,';') as "" from mwgrant_comparticipacao c
left join mwgrant_subsidio s
on c.chaveSubsidio=s.codigoInterno where s.codigoInterno is null;




