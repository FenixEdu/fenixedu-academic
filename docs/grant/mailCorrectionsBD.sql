#mail 19/11/2003 666-1
#o contrato existe...
#select * from mwgrant_contrato c, bolseiro b where c.dataInicioContrato="2003-09-01" AND c.chaveBolseiro=b.codigoInterno AND b.numero=2124;
#transferencia o contrato 5011 para pra o bolseiro 1976.. codigoInterno=1977
update mwgrant_contrato set chaveBolseiro=1977 where codigoInterno=5011;

#o contracto existe
#select * from contrato c, bolseiro b where c.dataInicioContrato="2003-09-01" AND c.chaveBolseiro=b.codigoInterno AND b.numero=2127;
#transferencia o contrato 5015 para o bolseiro 1985, codigo interno 1986
update mwgrant_contrato set chaveBolseiro=1986 where codigoInterno=5015;

# o contrato existe
#select * from contrato c, bolseiro b where c.dataInicioContrato="2003-11-01" and c.dataFimContrato="2003-10-31" and numeroContrato=2 and c.chaveBolseiro=b.codigoInterno and b.numero=2108;
#apagar esse contrato codigoInterno 4976
delete from mwgrant_contrato where codigoInterno=4976;


#mail de 09/12/2003 666-2 NAO RESOLVIDO AINDA
#o referido contrato de facto nao se encontra na base de dados
#codigo interno do bolseiro 1478 -> 359;
#contratos do bolseiro
#select c.numeroContrato, c.dataInicioContrato, c.dataFimContrato, b.numero from contrato c, bolseiro b where c.chaveBolseiro=b.codigoInterno and b.numero=1478;
#contratos nesse periodo existentes
#select c.numeroContrato, c.dataInicioContrato, c.dataFimContrato, b.numero from contrato c, bolseiro b where c.dataInicioContrato="2003-12-01" and c.dataFimContrato="2003-05-31" and c.chaveBolseiro=b.codigoInterno;

#mail 69-1 de 23 Oct 2003 from Magda Borges to Antonio Rito
update mwgrant_contrato set numeroContrato=6 where codigoInterno=4987;
update mwgrant_contrato set chaveBolseiro=2114 where chaveBolseiro=1714;
delete from mwgrant_pessoa where nome="Ewa Maria Puchalko";
delete from mwgrant_bolseiro where numero=1452;

#correccao do valor do sexo para pessoas com valor de sexo '0' (26/02/2004).
update mwgrant_pessoa set sexo="1" where codigoInterno=1938;
update mwgrant_pessoa set sexo="1" where codigoInterno=4570;
update mwgrant_pessoa set sexo="1" where codigoInterno=5209;
update mwgrant_pessoa set sexo="2" where sexo=0;

#mail 69-2 de 14 Oct 2003 from magda borges to joão
update mwgrant_contrato set chaveBolseiro=904 where dataInicioContrato="2003-09-01" and dataFimContrato="2004-08-31" and chaveBolseiro=2105;

#mail 69-3 de 4 Nov 2003 from Anabela Ramos to rito.silva_at_ist.utl.pt
update mwgrant_contrato set chaveBolseiro=1853 where numeroContrato=2 and dataInicioContrato="2003-09-03" and dataFimContrato="2004-09-02" and chaveBolseiro=2118;
delete from mwgrant_contrato where numeroContrato=3 and dataInicioContrato="2003-09-03" and dataFimContrato="2004-09-02" and chaveBolseiro=2118;

#mail 69-4 de 21 Oct 2003 from Anabela Ramos to Rito Silva
update mwgrant_contrato set chaveBolseiro=781,numeroContrato=4 where numeroContrato=2 and dataInicioContrato="2003-08-01" and dataFimContrato="2004-01-31" and chaveBolseiro=2113;
delete from mwgrant_contrato where numeroContrato=3 and dataInicioContrato="2003-08-01" and dataFimContrato="2004-01-31" and chaveBolseiro=2113;

#mail 69-5 de 9 Dec 2003 from Anabela Ramos to João Luz CC Rito Silva
update mwgrant_contrato set chaveBolseiro=359,numeroContrato=5 where numeroContrato=2 and dataInicioContrato="2003-12-01" and dataFimContrato="2003-05-31" and chaveBolseiro=2139;
delete from mwgrant_contrato where chaveBolseiro=2157 and chaveTipoBolsa=13;


