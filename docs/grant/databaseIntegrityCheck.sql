#Integridade geral
select numeroDocumentoIdentificacao,tipoDocumentoIdentificacao,nome from pessoa where numeroDocumentoIdentificacao=14;
select numeroContrato,dataInicioContrato,dataFimContrato,chaveBolseiro from contrato where dataInicioContrato>dataFimContrato;

