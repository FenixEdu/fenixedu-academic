package ServidorPersistenteJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import Dominio.MarcacaoPonto;
import Dominio.RegularizacaoMarcacaoPonto;
import ServidorPersistenteJDBC.IMarcacaoPontoPersistente;
import ServidorPersistenteJDBC.SuportePersistenteOracle;

/**
 * @author Fernanda Quitério e Tânia Pousão
 */
//««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««
//Marcacao de ponto para SERVIDOR
//»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»
public class MarcacaoPontoRelacional implements IMarcacaoPontoPersistente {
    public boolean alterarMarcacaoPonto(MarcacaoPonto marcacaoPonto) {
        /*
         * //Acesso a BD da nossa Aplicacao boolean resultado = false;
         * 
         * try { PreparedStatement sql = UtilRelacional.prepararComando("UPDATE
         * marcacao_ponto SET " + "codigoInterno = ? , " + "unidade = ? , " +
         * "siglaUnidade = ? , " + "dataMarcacao = ? , " + "numCartao = ? , " +
         * "numFuncionario = ? , " + "estado = ? " + "WHERE codigoInterno = ?");
         * 
         * sql.setInt(1, marcacaoPonto.getCodigoInterno()); sql.setInt(2,
         * marcacaoPonto.getUnidade()); sql.setString(3,
         * marcacaoPonto.getSiglaUnidade()); if(marcacaoPonto.getData() !=
         * null){ sql.setTimestamp(4, new
         * java.sql.Timestamp((marcacaoPonto.getData()).getTime())); }else{
         * sql.setTimestamp(4, null); } sql.setInt(5,
         * marcacaoPonto.getNumCartao()); sql.setInt(6,
         * marcacaoPonto.getNumFuncionario()); sql.setString(7,
         * marcacaoPonto.getEstado()); sql.setInt(8,
         * marcacaoPonto.getCodigoInterno());
         * 
         * sql.executeUpdate(); sql.close(); resultado = true; } catch(Exception
         * e) {
         * System.out.println("MarcacaoPontoRelacional.alterarMarcacaoPonto: " +
         * e.toString()); } finally { return resultado; }
         */

        /**
         * **************************** Acesso a BD Oracle
         * *******************************
         */
        boolean resultado = false;

        try {
            SuportePersistenteOracle.getInstance().iniciarTransaccao();
            try {
                PreparedStatement sql = UtilRelacionalOracle.prepararComando("UPDATE ASS_MARCAS SET "
                        + "ASS_MARPESSOA = ? , " + "ASS_MARCARTAO = ? , "
                        + "ASS_MARDHMARCA = TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS') , "
                        + "ASS_MARUNID = ? , " + "ASS_MARTIPO = ? , " + "ASS_MARAUTOJUST = ? , "
                        + "ASS_MARREGUL = ? , " + "ASS_MARIES = ? , " + "ASS_MARSTAT = ? , "
                        + "ASS_MARSEQ = ? , " + "ASS_MARWHEN = TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS') , "
                        + "ASS_MARWHO = ? " + "WHERE ASS_MARSEQ = ? ");

                sql.setInt(1, marcacaoPonto.getNumFuncionario());
                if (marcacaoPonto.getEstado().equals("regularizada")) {
                    sql.setInt(2, -1);
                } else {
                    sql.setInt(2, marcacaoPonto.getNumCartao());
                }

                sql.setString(3, marcacaoPonto.getData().toString().substring(0,
                        marcacaoPonto.getData().toString().indexOf(".")));

                sql.setString(4, marcacaoPonto.getSiglaUnidade());
                sql.setString(5, new String("N"));
                sql.setString(6, new String("N"));
                if (marcacaoPonto.getEstado().equals("regularizada")) {
                    sql.setString(7, new String("S"));
                } else {
                    sql.setString(7, new String("N"));
                }
                sql.setString(8, new String("N"));
                sql.setString(9, new String("V"));
                sql.setInt(10, marcacaoPonto.getCodigoInterno());

                // data do registo
                sql.setString(11, marcacaoPonto.getData().toString().substring(0,
                        marcacaoPonto.getData().toString().indexOf(".")));

                sql.setInt(12, 0);
                sql.setInt(13, marcacaoPonto.getCodigoInterno());

                sql.executeUpdate();
                sql.close();
                resultado = true;
            } catch (Exception e) {
                SuportePersistenteOracle.getInstance().cancelarTransaccao();
                System.out.println("MarcacaoPontoRelacional.alterarMarcacaoPonto: " + e.toString());
                return resultado;
            }
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        } catch (Exception e) {
            System.out.println("MarcacaoPontoRelacional.alterarMarcacaoPonto: " + e.toString());
        }
        return resultado;
    } /* alterarMarcacaoPonto */

    public boolean apagarMarcacaoPonto(int chaveMarcacao) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("DELETE FROM ass_MARCACAO_PONTO WHERE codigoInterno = ?");

            sql.setInt(1, chaveMarcacao);

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("MarcacaoPontoRelacional.apagarMarcacaoPonto: " + e.toString());
        }
        return resultado;
    } /* apagarMarcacaoPonto */

    public boolean apagarMarcacaoPonto(Timestamp dataMarcacao) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("DELETE FROM ass_MARCACAO_PONTO WHERE dataMarcacao = ?");

            sql.setTimestamp(1, new java.sql.Timestamp(dataMarcacao.getTime()));

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("MarcacaoPontoRelacional.apagarMarcacaoPonto: " + e.toString());
        }
        return resultado;
    } /* apagarMarcacaoPonto */

    public boolean apagarMarcacoesPonto() {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("DELETE FROM ass_MARCACAO_PONTO");
            sql.executeUpdate();
            sql.close();
            /*
             * sql = UtilRelacional.prepararComando("ALTER TABLE
             * ass_MARCACAO_PONTO auto_increment=1"); sql.executeUpdate();
             * sql.close();
             */
            resultado = true;
        } catch (Exception e) {
            System.out.println("MarcacaoPontoRelacional.apagarMarcacoesPonto: " + e.toString());
        }
        return resultado;
    } /* apagarMarcacoesPonto */

    public List consultarMarcacoesPonto(List listaFuncionarios, List listaCartoes, List listaEstados,
            Timestamp dataInicio, Timestamp dataFim) {
        /*
         * //Acesso a BD da nossa Aplicacao List marcacoesPonto = null;
         * 
         * try { PreparedStatement sql = null; ResultSet resultado = null; //
         * obtem as marcacoes no intervalo de datas StringBuffer query = new
         * StringBuffer( FROM marcacao_ponto WHERE " + "((dataMarcacao IS NULL)
         * OR (dataMarcacao BETWEEN ? AND ?))");
         * 
         * if (listaFuncionarios != null) { if (listaFuncionarios.size() > 0) {
         * query = query.append(" AND (");
         * 
         * ListIterator iterListaFuncionarios =
         * listaFuncionarios.listIterator(); while
         * (iterListaFuncionarios.hasNext()) { iterListaFuncionarios.next(); if
         * (iterListaFuncionarios.hasNext()) { query =
         * query.append("numFuncionario = ? OR "); } else { query =
         * query.append("numFuncionario = ?)"); } } } }
         * 
         * if (listaCartoes != null) { if (listaCartoes.size() > 0) { query =
         * query.append(" AND (");
         * 
         * ListIterator iterListaCartoes = listaCartoes.listIterator(); while
         * (iterListaCartoes.hasNext()) { iterListaCartoes.next(); if
         * (iterListaCartoes.hasNext()) { query = query.append("numCartao = ? OR
         * "); } else { query = query.append("numCartao = ?)"); } } } }
         * 
         * if (listaEstados != null) { if (listaEstados.size() > 0) { query =
         * query.append(" AND (");
         * 
         * ListIterator iterListaEstados = listaEstados.listIterator(); while
         * (iterListaEstados.hasNext()) { iterListaEstados.next(); if
         * (iterListaEstados.hasNext()) { query = query.append("estado = ? OR
         * "); } else { query = query.append("estado = ?)"); } } } }
         * 
         * sql = UtilRelacional.prepararComando(query); sql.setTimestamp(1, new
         * Timestamp(dataInicio.getTime())); sql.setTimestamp(2, new
         * Timestamp(dataFim.getTime())); // indice do comando sql int indice =
         * 3;
         * 
         * if (listaFuncionarios != null) { ListIterator iterListaFuncionarios =
         * listaFuncionarios.listIterator(); Integer numFuncionario = null;
         * 
         * while (iterListaFuncionarios.hasNext()) { numFuncionario = (Integer)
         * iterListaFuncionarios.next(); sql.setInt(indice,
         * numFuncionario.intValue()); indice++; } }
         * 
         * if (listaCartoes != null) { ListIterator iterListaCartoes =
         * listaCartoes.listIterator(); Integer numCartao = null;
         * 
         * while (iterListaCartoes.hasNext()) { numCartao = (Integer)
         * iterListaCartoes.next(); sql.setInt(indice, numCartao.intValue());
         * indice++; } }
         * 
         * if (listaEstados != null) { ListIterator iterListaEstados =
         * listaEstados.listIterator(); String estado = null;
         * 
         * while (iterListaEstados.hasNext()) { estado = (String)
         * iterListaEstados.next(); sql.setString(indice, estado); indice++; } }
         * 
         * //query = query.append(" ORDER BY dataMarcacao");
         * 
         * resultado = sql.executeQuery(); marcacoesPonto = new ArrayList();
         * Timestamp dataMarcacao = null;
         * 
         * PreparedStatement sql2 = UtilRelacionalOracle.prepararComando( FROM
         * ass_REGULARIZACAO_MARCACAO WHERE " + "chaveMarcacaoPonto = ? AND
         * chaveParamRegularizacao = 6"); ResultSet resultado2 = null;
         * 
         * while (resultado.next()) { // retirar as marcacoes de ponto que foram
         * marcadas para anular sql2.setInt(1,
         * resultado.getInt("codigoInterno")); resultado2 = sql2.executeQuery();
         * if (!resultado2.next()) { if (resultado.getString("dataMarcacao") !=
         * null) { dataMarcacao =
         * Timestamp.valueOf(resultado.getString("dataMarcacao")); }
         * marcacoesPonto.add( new MarcacaoPonto(
         * resultado.getInt("codigoInterno"), resultado.getInt("unidade"),
         * resultado.getString("siglaUnidade"), dataMarcacao,
         * resultado.getInt("numCartao"), resultado.getInt("numFuncionario"),
         * resultado.getString("estado"))); } } sql.close(); } catch (Exception
         * e) { System.out.println(
         * "MarcacaoPontoRelacional.consultarMarcacoesPonto: " + e.toString());
         * return null; } finally { return marcacoesPonto; }
         */
        /**
         * ********************* Acesso a BD Oracle
         * *************************************
         */
        //ATENCAO: Acede à BD do Teleponto para ler as marcacoes de ponto
        List marcacoesPonto = null;

        try {
            SuportePersistenteOracle.getInstance().iniciarTransaccao();
            try {

                // procura de marcacoes de ponto normais
                StringBuffer query = new StringBuffer(
                        "SELECT * FROM ASS_MARCAS WHERE ((ASS_MARDHMARCA IS NULL) OR "
                                + "(ASS_MARDHMARCA BETWEEN TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS') AND TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS')))");
                if (listaFuncionarios != null) {
                    if (listaFuncionarios.size() > 0) {
                        query = query.append(" AND (");

                        ListIterator iterListaFuncionarios = listaFuncionarios.listIterator();
                        while (iterListaFuncionarios.hasNext()) {
                            iterListaFuncionarios.next();
                            if (iterListaFuncionarios.hasNext()) {
                                query = query.append("ASS_MARPESSOA = ? OR ");
                            } else {
                                query = query.append("ASS_MARPESSOA = ?)");
                            }
                        }
                    }
                }
                if (listaCartoes != null) {
                    if (listaCartoes.size() > 0) {
                        query = query.append(" AND ((");

                        ListIterator iterListaCartoes = listaCartoes.listIterator();
                        while (iterListaCartoes.hasNext()) {
                            iterListaCartoes.next();
                            if (iterListaCartoes.hasNext()) {
                                query = query.append("ASS_MARCARTAO = ? OR ");
                            } else {
                                query = query.append("ASS_MARCARTAO = ?)");
                            }
                        }
                    }
                }

                if (listaCartoes != null && listaFuncionarios != null) {
                    if (listaCartoes.size() > 0 && listaFuncionarios.size() > 0) {
                        query = query.append(" OR (ASS_MARCARTAO = -1 AND (");

                        ListIterator iterListaFuncionarios = listaFuncionarios.listIterator();
                        while (iterListaFuncionarios.hasNext()) {
                            iterListaFuncionarios.next();
                            if (iterListaFuncionarios.hasNext()) {
                                query = query.append("ASS_MARPESSOA = ? OR ");
                            } else {
                                query = query.append("ASS_MARPESSOA = ?)))");
                            }
                        }
                    } else {
                        if (listaCartoes.size() > 0) {
                            query = query.append(")");
                        }
                    }
                } else {
                    if (listaCartoes != null) {
                        query = query.append(")");
                    }
                }

                PreparedStatement sql = UtilRelacionalOracle.prepararComando(query.toString());
                sql.setString(1, dataInicio.toString().substring(0, dataInicio.toString().indexOf(".")));
                sql.setString(2, dataFim.toString().substring(0, dataFim.toString().indexOf(".")));

                // indice do comando sql
                int indice = 3;

                if (listaFuncionarios != null) {
                    ListIterator iterListaFuncionarios = listaFuncionarios.listIterator();
                    Integer numFuncionario = null;

                    while (iterListaFuncionarios.hasNext()) {
                        numFuncionario = (Integer) iterListaFuncionarios.next();
                        sql.setInt(indice, numFuncionario.intValue());
                        indice++;
                    }
                }

                if (listaCartoes != null) {
                    ListIterator iterListaCartoes = listaCartoes.listIterator();
                    Integer numCartao = null;

                    while (iterListaCartoes.hasNext()) {
                        numCartao = (Integer) iterListaCartoes.next();
                        sql.setInt(indice, numCartao.intValue());
                        indice++;
                    }
                }

                if (listaCartoes != null && listaFuncionarios != null) {
                    if (listaCartoes.size() > 0 && listaFuncionarios.size() > 0) {
                        ListIterator iterListaFuncionarios = listaFuncionarios.listIterator();
                        Integer numFuncionario = null;

                        while (iterListaFuncionarios.hasNext()) {
                            numFuncionario = (Integer) iterListaFuncionarios.next();
                            sql.setInt(indice, numFuncionario.intValue());
                            indice++;
                        }
                    }
                }
                //query = query.append(" ORDER BY ASS_MARDHMARCA");

                ResultSet resultado = sql.executeQuery();
                marcacoesPonto = new ArrayList();
                Timestamp dataMarcacao = null;

                //				obtem o codigo das marcacoes a anular
                PreparedStatement sql2 = UtilRelacional
                        .prepararComando("SELECT * FROM ass_PARAM_REGULARIZACAO WHERE sigla = 'MA'");
                ResultSet resultado2 = sql2.executeQuery();
                int marcacaoAnular = 0;
                if (resultado2.next()) {
                    marcacaoAnular = resultado2.getInt("codigoInterno");
                } else {
                    sql2.close();
                    SuportePersistenteOracle.getInstance().cancelarTransaccao();
                    return null;
                }
                sql2.close();

                // marcacaoes regularizadas
                PreparedStatement sql3 = UtilRelacional
                        .prepararComando("SELECT * FROM ass_REGULARIZACAO_MARCACAO WHERE "
                                + "chaveMarcacaoPonto = ?");
                ResultSet resultado3 = null;
                while (resultado.next()) {
                    if (resultado.getString("ASS_MARDHMARCA") != null) {
                        dataMarcacao = Timestamp.valueOf(resultado.getString("ASS_MARDHMARCA"));
                    }
                    MarcacaoPonto marcacaoPonto = new MarcacaoPonto(resultado.getInt("ASS_MARSEQ"),
                            resultado.getString("ASS_MARUNID"), dataMarcacao, resultado
                                    .getInt("ASS_MARCARTAO"), resultado.getInt("ASS_MARPESSOA"));

                    // diferenciar as marcacoes regularizadas
                    sql3.setInt(1, resultado.getInt("ASS_MARSEQ"));
                    resultado3 = sql3.executeQuery();
                    if (resultado3.next()) {
                        // retirar as marcacoes de ponto que foram marcadas para
                        // anular
                        if (resultado3.getInt("chaveParamRegularizacao") == marcacaoAnular) {
                            continue;
                        }

                        marcacaoPonto.setEstado("regularizada");
                        marcacaoPonto.setSiglaUnidade("");
                        marcacaoPonto.setNumCartao(0);

                    }
                    marcacoesPonto.add(marcacaoPonto);
                }
                sql3.close();
                sql.close();
            } catch (Exception e) {
                SuportePersistenteOracle.getInstance().cancelarTransaccao();
                System.out.println("MarcacaoPontoRelacional.consultarMarcacoesPonto: " + e.toString());
                return null;
            }
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        } catch (Exception e) {
            System.out.println("MarcacaoPontoRelacional.consultarMarcacoesPonto: " + e.toString());
            return null;
        }
        return marcacoesPonto;
    } /* consultarMarcacoesPonto */

    public List consultarMarcacoesPontoErros(String estado) {
        List errosLeitura = null;

        try {
            /*
             * FROM marcacao_ponto WHERE estado = '" + estado + "'";
             * PreparedStatement sql = UtilRelacional.prepararComando(query);
             * 
             * ResultSet resultado = sql.executeQuery(); errosLeitura = new
             * ArrayList(); Timestamp dataMarcacao = null;
             * 
             * while (resultado.next()) { if
             * (resultado.getString("dataMarcacao") != null) { dataMarcacao =
             * Timestamp.valueOf(resultado.getString("dataMarcacao")); }
             * errosLeitura.add( new MarcacaoPonto(
             * resultado.getInt("codigoInterno"), resultado.getInt("unidade"),
             * resultado.getString("siglaUnidade"), dataMarcacao,
             * resultado.getInt("numCartao"),
             * resultado.getInt("numFuncionario"),
             * resultado.getString("estado"))); } sql.close();
             */
        } catch (Exception e) {
            System.out.println("MarcacaoPontoRelacional.consultarMarcacoesPontoErros: " + e.toString());
            return null;
        }
        return errosLeitura;
    } /* consultarMarcacoesPontoErros */

    public boolean escreverMarcacaoPonto(MarcacaoPonto marcacaoPonto) {
        boolean resultado = false;
        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("INSERT INTO ass_MARCACAO_PONTO VALUES (?, ?, ?, ?, ?, ?, ?)");

            sql.setInt(1, marcacaoPonto.getCodigoInterno());
            sql.setInt(2, marcacaoPonto.getUnidade());
            sql.setString(3, marcacaoPonto.getSiglaUnidade());
            if (marcacaoPonto.getData() != null) {
                sql.setTimestamp(4, new Timestamp((marcacaoPonto.getData()).getTime()));
            } else {
                sql.setTimestamp(4, null);
            }
            sql.setInt(5, marcacaoPonto.getNumCartao());
            sql.setInt(6, marcacaoPonto.getNumFuncionario());
            sql.setString(7, marcacaoPonto.getEstado());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("MarcacaoPontoRelacional.escreverMarcacaoPonto: " + e.toString());
        }
        return resultado;
    } /* escreverMarcacaoPonto */

    public boolean escreverMarcacoesPonto(List listaMarcacoes) {
        boolean resultado = false;

        ListIterator iterador = listaMarcacoes.listIterator();
        MarcacaoPonto marcacaoPonto = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("INSERT INTO ass_MARCACAO_PONTO VALUES (?, ?, ?, ?, ?, ?, ?)");

            while (iterador.hasNext()) {
                marcacaoPonto = (MarcacaoPonto) iterador.next();

                sql.setInt(1, marcacaoPonto.getCodigoInterno());
                sql.setInt(2, marcacaoPonto.getUnidade());
                sql.setString(3, marcacaoPonto.getSiglaUnidade());
                if (marcacaoPonto.getData() != null) {
                    sql.setTimestamp(4, new Timestamp((marcacaoPonto.getData()).getTime()));
                } else {
                    sql.setTimestamp(4, null);
                }
                sql.setInt(5, marcacaoPonto.getNumCartao());
                sql.setInt(6, marcacaoPonto.getNumFuncionario());
                sql.setString(7, marcacaoPonto.getEstado());

                sql.executeUpdate();
            }
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("MarcacaoPontoRelacional.escreverMarcacoesPonto: " + e.toString());
        }
        return resultado;
    } /* escreverMarcacoesPonto */

    public MarcacaoPonto lerMarcacaoPonto(int codigoInterno) {
        MarcacaoPonto marcacaoPonto = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_MARCACAO_PONTO WHERE codigoInterno = ?");

            sql.setInt(1, codigoInterno);

            ResultSet resultado = sql.executeQuery();
            Timestamp dataMarcacao = null;
            if (resultado.next()) {
                if (resultado.getString("dataMarcacao") != null) {
                    dataMarcacao = Timestamp.valueOf(resultado.getString("dataMarcacao"));
                }
                marcacaoPonto = new MarcacaoPonto(resultado.getInt("codigoInterno"), resultado
                        .getInt("unidade"), resultado.getString("siglaUnidade"), dataMarcacao, resultado
                        .getInt("numCartao"), resultado.getInt("numFuncionario"), resultado
                        .getString("estado"));
            }
            sql.close();
        } catch (Exception e) {
            System.out.println("MarcacaoPontoRelacional.lerMarcacaoPonto: " + e.toString());
        }
        return marcacaoPonto;
    } /* lerMarcacaoPonto */

    public MarcacaoPonto lerMarcacaoPonto(Timestamp dataMarcacao, int numFuncionario) {
        /*
         * //Acesso a nossa base de dados MarcacaoPonto marcacaoPonto = null;
         * 
         * try { PreparedStatement sql = FROM marcacao_ponto " + "WHERE
         * dataMarcacao = ? AND numFuncionario = ?");
         * 
         * sql.setTimestamp(1, dataMarcacao); sql.setInt(2, numFuncionario);
         * 
         * ResultSet resultado = sql.executeQuery(); if (resultado.next()) {
         * Timestamp data = null; if(resultado.getString("dataMarcacao") !=
         * null){ data = Timestamp.valueOf(resultado.getString("dataMarcacao")); }
         * marcacaoPonto = new MarcacaoPonto(resultado.getInt("codigoInterno"),
         * resultado.getInt("unidade"), resultado.getString("siglaUnidade"),
         * data, resultado.getInt("numCartao"),
         * resultado.getInt("numFuncionario"), resultado.getString("estado")); }
         * sql.close(); } catch(Exception e) {
         * System.out.println("MarcacaoPontoRelacional.lerMarcacaoPonto: " +
         * e.toString()); } finally { return marcacaoPonto; }
         */
        /**
         * ************************************ ler a marcacao de ponto na BD
         * Oracle ********************************************
         */
        MarcacaoPonto marcacaoPonto = null;

        try {
            SuportePersistenteOracle.getInstance().iniciarTransaccao();
            try {

                PreparedStatement sql = UtilRelacionalOracle
                        .prepararComando("SELECT * FROM ASS_MARCAS "
                                + "WHERE ASS_MARDHMARCA = TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS') AND ASS_MARPESSOA = ?");

                sql.setTimestamp(1, new Timestamp(dataMarcacao.getTime()));
                sql.setInt(2, numFuncionario);

                ResultSet resultadoQuery = sql.executeQuery();
                if (resultadoQuery.next()) {
                    marcacaoPonto = new MarcacaoPonto(resultadoQuery.getInt("ASS_MARSEQ"),
                            resultadoQuery.getString("ASS_MARUNID"), Timestamp.valueOf(resultadoQuery
                                    .getString("ASS_MARDHMARCA")), resultadoQuery
                                    .getInt("ASS_MARCARTAO"), resultadoQuery.getInt("ASS_MARPESSOA"));
                } else {
                    sql.close();
                    return null;
                }
                sql.close();

                if (marcacaoPonto != null) {
                    sql = UtilRelacional
                            .prepararComando("SELECT codigoInterno FROM ass_UNIDADE_MARCACAO "
                                    + "WHERE sigla = ?");

                    sql.setString(1, marcacaoPonto.getSiglaUnidade());
                    resultadoQuery = sql.executeQuery();
                    if (resultadoQuery.next()) {
                        marcacaoPonto.setUnidade(resultadoQuery.getInt("codigoInterno"));
                    }
                    sql.close();

                    sql = UtilRelacionalOracle.prepararComando("SELECT * FROM ASS_MARREG "
                            + "WHERE ASS_MARREGMARCAS = ?");

                    sql.setInt(1, marcacaoPonto.getCodigoInterno());
                    resultadoQuery = sql.executeQuery();
                    if (resultadoQuery.next()) {
                        marcacaoPonto.setEstado("regularizada");
                    }
                    sql.close();
                }
            } catch (Exception e) {
                SuportePersistenteOracle.getInstance().cancelarTransaccao();
                System.out.println("MarcacaoPontoRelacional.lerMarcacaoPonto: " + e.toString());
                return null;
            }
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        } catch (Exception e) {
            System.out.println("MarcacaoPontoRelacional.lerMarcacaoPonto: " + e.toString());
            return null;
        }
        return marcacaoPonto;
    } /* lerMarcacaoPonto */

    public List lerMarcacoesPonto() {
        /*
         * // Acesso a BD da nossa Aplicação List marcacoesPonto = null;
         * 
         * try { PreparedStatement sql = FROM marcacao_ponto");
         * 
         * ResultSet resultado = sql.executeQuery(); marcacoesPonto = new
         * ArrayList(); Timestamp dataMarcacao = null; while(resultado.next()) {
         * if(resultado.getString("dataMarcacao") != null){ dataMarcacao =
         * Timestamp.valueOf(resultado.getString("dataMarcacao")); }
         * marcacoesPonto.add(new
         * MarcacaoPonto(resultado.getInt("codigoInterno"),
         * resultado.getInt("unidade"), resultado.getString("siglaUnidade"),
         * dataMarcacao, resultado.getInt("numCartao"),
         * resultado.getInt("numFuncionario"), resultado.getString("estado")));
         * dataMarcacao = null; } sql.close(); } catch(Exception e) {
         * System.out.println("MarcacaoPontoRelacional.lerMarcacoesPonto: " +
         * e.toString()); } finally { return marcacoesPonto;
         */

        /**
         * ************************** ler Marcações da BD Oracle
         * ********************************
         */
        List marcacoesPonto = null;

        try {
            SuportePersistenteOracle.getInstance().iniciarTransaccao();
            try {
                PreparedStatement sql = UtilRelacionalOracle.prepararComando("SELECT * FROM ASS_MARCAS");
                ResultSet resultado = sql.executeQuery();

                marcacoesPonto = new ArrayList();
                Timestamp dataMarcacao = null;
                MarcacaoPonto marcacaoPonto = null;

                // chaveUnidade
                PreparedStatement sql2 = UtilRelacional
                        .prepararComando("SELECT codigoInterno FROM ass_UNIDADE_MARCACAO WHERE sigla = ?");
                ResultSet resultado2 = null;

                // marcacoes de ponto regularizadas nao tem unidade de marcacao
                String sigla = new String(" ");
                int chaveUnidade = 0;

                while (resultado.next()) {
                    if (resultado.getString("ASS_MARUNID") != null) {
                        // obtem o codigo interno da unidade de marcacao
                        sql2.setString(1, resultado.getString("ASS_MARUNID"));
                        resultado2 = sql2.executeQuery();
                        if (resultado2.next()) {
                            sigla = resultado.getString("ASS_MARUNID");
                            chaveUnidade = resultado2.getInt("codigoInterno");
                        }
                    }
                    if (resultado.getString("ASS_MARDHMARCA") != null) {
                        dataMarcacao = Timestamp.valueOf(resultado.getString("ASS_MARDHMARCA"));
                    }
                    marcacaoPonto = new MarcacaoPonto(resultado.getInt("ASS_MARSEQ"), chaveUnidade,
                            sigla, dataMarcacao, resultado.getInt("ASS_MARCARTAO"), resultado
                                    .getInt("ASS_MARPESSOA"));

                    if (resultado.getString("ASS_MARREGUL").equals("S")) {
                        marcacaoPonto.setEstado("regularizada");
                    }
                    marcacoesPonto.add(marcacaoPonto);
                    dataMarcacao = null;
                    marcacaoPonto = null;
                }
                sql2.close();
                sql.close();

            } catch (Exception e) {
                SuportePersistenteOracle.getInstance().cancelarTransaccao();
                System.out.println("MarcacaoPontoRelacional.lerMarcacoesPonto: " + e.toString());
                return null;
            }
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        } catch (Exception e) {
            System.out.println("MarcacaoPontoRelacional.lerMarcacoesPonto: " + e.toString());
            return null;
        }
        return marcacoesPonto;
    } /* lerMarcacoesPonto */

    public List lerMarcacoesPonto(int numCartao) {
        /*
         * // Acesso a BD da nossa Aplicação List marcacoesPonto = null;
         * 
         * try { PreparedStatement sql = FROM marcacao_ponto " + "WHERE
         * numCartao = ? ");
         * 
         * sql.setInt(1, numCartao);
         * 
         * ResultSet resultado = sql.executeQuery(); marcacoesPonto = new
         * ArrayList(); Timestamp dataMarcacao = null; while(resultado.next()) {
         * if(resultado.getString("dataMarcacao") != null){ dataMarcacao =
         * Timestamp.valueOf(resultado.getString("dataMarcacao")); }
         * marcacoesPonto.add(new
         * MarcacaoPonto(resultado.getInt("codigoInterno"),
         * resultado.getInt("unidade"), resultado.getString("siglaUnidade"),
         * dataMarcacao, resultado.getInt("numCartao"),
         * resultado.getInt("numFuncionario"), resultado.getString("estado")));
         * dataMarcacao = null; } sql.close(); } catch(Exception e) {
         * System.out.println("MarcacaoPontoRelacional.lerMarcacoesPonto: " +
         * e.toString()); } finally { return marcacoesPonto; }
         */

        /**
         * ************************** ler Marcações da BD Oracle
         * ********************************
         */
        List marcacoesPonto = null;

        try {
            SuportePersistenteOracle.getInstance().iniciarTransaccao();
            try {
                PreparedStatement sql = UtilRelacionalOracle
                        .prepararComando("SELECT * FROM ASS_MARCAS WHERE ASS_MARCARTAO = ? ");

                sql.setInt(1, numCartao);

                ResultSet resultado = sql.executeQuery();

                marcacoesPonto = new ArrayList();
                Timestamp dataMarcacao = null;
                while (resultado.next()) {
                    if (resultado.getString("ASS_MARDHMARCA") != null) {
                        dataMarcacao = java.sql.Timestamp.valueOf(resultado.getString("ASS_MARDHMARCA"));
                    }

                    marcacoesPonto.add(new MarcacaoPonto(resultado.getInt("ASS_MARSEQ"), resultado
                            .getString("ASS_MARUNID"), dataMarcacao, resultado.getInt("ASS_MARCARTAO"),
                            resultado.getInt("ASS_MARPESSOA")));
                    dataMarcacao = null;
                }
                sql.close();
            } catch (Exception e) {
                SuportePersistenteOracle.getInstance().cancelarTransaccao();
                System.out.println("MarcacaoPontoRelacional.lerMarcacoesPonto: " + e.toString());
                return null;
            }
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        } catch (Exception e) {
            System.out.println("MarcacaoPontoRelacional.lerMarcacoesPonto: " + e.toString());
            return null;
        }
        return marcacoesPonto;
    } /* lerMarcacoesPonto */

    public List obterMarcacoesPonto(int numFuncionario, int numCartao, Timestamp dataFim) {
        /*
         * // Acesso a BD da nossa Aplicacao List marcacoesPonto = null;
         * 
         * try { PreparedStatement sql = FROM marcacao_ponto " + "WHERE
         * dataMarcacao>=? AND numFuncionario=? AND numCartao=?");
         * 
         * sql.setTimestamp(1, new Timestamp(dataFim.getTime())); sql.setInt(2,
         * numFuncionario); sql.setInt(3, numCartao);
         * 
         * ResultSet resultado = sql.executeQuery(); marcacoesPonto = new
         * ArrayList(); Timestamp dataMarcacao = null; while(resultado.next()) {
         * if(resultado.getString("dataMarcacao") != null){ dataMarcacao =
         * Timestamp.valueOf(resultado.getString("dataMarcacao")); }
         * marcacoesPonto.add(new
         * MarcacaoPonto(resultado.getInt("codigoInterno"),
         * resultado.getInt("unidade"), resultado.getString("siglaUnidade"),
         * dataMarcacao, resultado.getInt("numCartao"),
         * resultado.getInt("numFuncionario"), resultado.getString("estado")));
         * dataMarcacao = null; } sql.close(); } catch(Exception e) {
         * System.out.println("MarcacaoPontoRelacional.obterMarcacoesPonto: " +
         * e.toString()); } finally { return marcacoesPonto; }
         */
        /**
         * ******************** Acesso a BD oracle
         * ********************************
         */
        List marcacoesPonto = null;

        try {
            SuportePersistenteOracle.getInstance().iniciarTransaccao();
            try {

                PreparedStatement sql = UtilRelacionalOracle
                        .prepararComando("SELECT * FROM ASS_MARCAS "
                                + "WHERE ASS_MARDHMARCA>=TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS') AND ASS_MARPESSOA=? AND ASS_MARCARTAO=?");

                sql.setString(1, dataFim.toString().substring(0, dataFim.toString().indexOf(".")));

                sql.setInt(2, numFuncionario);
                sql.setInt(3, numCartao);

                ResultSet resultado = sql.executeQuery();
                marcacoesPonto = new ArrayList();
                Timestamp dataMarcacao = null;

                while (resultado.next()) {
                    if (resultado.getString("ASS_MARDHMARCA") != null) {
                        dataMarcacao = java.sql.Timestamp.valueOf(resultado.getString("ASS_MARDHMARCA"));
                    }

                    marcacoesPonto.add(new MarcacaoPonto(resultado.getInt("ASS_MARSEQ"), resultado
                            .getString("ASS_MARUNID"), dataMarcacao, resultado.getInt("ASS_MARCARTAO"),
                            resultado.getInt("ASS_MARPESSOA")));

                    dataMarcacao = null;
                }
                sql.close();

            } catch (Exception e) {
                SuportePersistenteOracle.getInstance().cancelarTransaccao();
                System.out.println("MarcacaoPontoRelacional.obterMarcacoesPonto: " + e.toString());
                return null;
            }
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        } catch (Exception e) {
            System.out.println("MarcacaoPontoRelacional.obterMarcacoesPonto: " + e.toString());
            return null;
        }
        return marcacoesPonto;
    } /* obterMarcacoesPonto */

    public List obterMarcacoesPonto(int numCartao, Timestamp dataInicio, Timestamp dataFim) {
        /*
         * // Acesso a BD da nossa Aplicacao List marcacoesPonto = null;
         * 
         * try { PreparedStatement sql = FROM marcacao_ponto " + "WHERE
         * numCartao=? AND dataMarcacao>=? AND dataMarcacao <=?");
         * 
         * sql.setInt(1, numCartao); sql.setTimestamp(2, new
         * Timestamp(dataInicio.getTime())); sql.setTimestamp(3, new
         * Timestamp(dataFim.getTime()));
         * 
         * ResultSet resultado = sql.executeQuery(); marcacoesPonto = new
         * ArrayList(); Timestamp dataMarcacao = null; while(resultado.next()) {
         * if(resultado.getString("dataMarcacao") != null){ dataMarcacao =
         * Timestamp.valueOf(resultado.getString("dataMarcacao")); }
         * marcacoesPonto.add(new
         * MarcacaoPonto(resultado.getInt("codigoInterno"),
         * resultado.getInt("unidade"), resultado.getString("siglaUnidade"),
         * dataMarcacao, resultado.getInt("numCartao"),
         * resultado.getInt("numFuncionario"), resultado.getString("estado")));
         * dataMarcacao = null; } sql.close(); } catch(Exception e) {
         * System.out.println("MarcacaoPontoRelacional.obterMarcacoesPonto: " +
         * e.toString()); } finally { return marcacoesPonto;
         */

        /**
         * ******************** Acesso a BD oracle
         * ********************************
         */
        List marcacoesPonto = null;

        try {
            SuportePersistenteOracle.getInstance().iniciarTransaccao();
            try {
                PreparedStatement sql = UtilRelacionalOracle
                        .prepararComando("SELECT * FROM ASS_MARCAS "
                                + "WHERE ASS_MARCARTAO=? AND "
                                + "ASS_MARDHMARCA>=TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS') AND ASS_MARDHMARCA<=TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS')");

                sql.setInt(1, numCartao);

                sql.setString(2, dataInicio.toString().substring(0, dataInicio.toString().indexOf(".")));
                sql.setString(3, dataFim.toString().substring(0, dataFim.toString().indexOf(".")));

                ResultSet resultado = sql.executeQuery();

                marcacoesPonto = new ArrayList();
                Timestamp dataMarcacao = null;
                while (resultado.next()) {
                    if (resultado.getString("ASS_MARDHMARCA") != null) {
                        dataMarcacao = java.sql.Timestamp.valueOf(resultado.getString("ASS_MARDHMARCA"));
                    }

                    marcacoesPonto.add(new MarcacaoPonto(resultado.getInt("ASS_MARSEQ"), resultado
                            .getString("ASS_MARUNID"), dataMarcacao, resultado.getInt("ASS_MARCARTAO"),
                            resultado.getInt("ASS_MARPESSOA")));
                }
                sql.close();
            } catch (Exception e) {
                SuportePersistenteOracle.getInstance().cancelarTransaccao();
                System.out.println("MarcacaoPontoRelacional.obterMarcacoesPonto: " + e.toString());
                return null;
            }
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        } catch (Exception e) {
            System.out.println("MarcacaoPontoRelacional.obterMarcacoesPonto: " + e.toString());
            return null;
        }
        return marcacoesPonto;
    } /* obterMarcacoesPonto */

    public int ultimoCodigoInterno() {
        /*
         * int ultimo = 0;
         * 
         * try { PreparedStatement sql = UtilRelacional.prepararComando("SELECT
         * MAX(codigoInterno) FROM marcacao_ponto");
         * 
         * ResultSet resultado = sql.executeQuery(); if (resultado.next()) {
         * ultimo = resultado.getInt(1); } sql.close(); } catch (Exception e) {
         * System.out.println("MarcacaoPontoRelacional.ultimoCodigoInterno: " +
         * e.toString()); } finally { return ultimo; }
         */
        int ultimo = 0;
        try {
            SuportePersistenteOracle.getInstance().iniciarTransaccao();
            try {
                // último número de sequencia na tabela
                PreparedStatement sql = UtilRelacionalOracle
                        .prepararComando("SELECT s_ordem.NEXTVAL FROM SYS.dual");
                ResultSet resultadoQuery = sql.executeQuery();
                if (resultadoQuery.next()) {
                    ultimo = resultadoQuery.getInt(1);
                }
                sql.close();

            } catch (Exception e) {
                SuportePersistenteOracle.getInstance().cancelarTransaccao();
                System.out.println("MarcacaoPontoRelacional.ultimoCodigoInterno: " + e.toString());
                return ultimo;
            }
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        } catch (Exception e) {
            System.out.println("MarcacaoPontoRelacional.ultimoCodigoInterno: " + e.toString());
        }
        return ultimo;
    } /* ultimoCodigoInterno */

    // ATENCAO: DESNECESSARIO PARA A APLICACAO DE ASSIDUIDADE, APENAS CONSTRUIDO
    // PARA CONSEGUIR ESCREVER
    // OS DADOS NA BD ORACLE
    public boolean alterarMarcacaoPontoRegularizacao(RegularizacaoMarcacaoPonto regularizacao) {
        boolean resultado = false;

        /**
         * ************************** escrever a marcacao de ponto na BD Oracle
         * ********************************************
         */
        try {
            SuportePersistenteOracle.getInstance().iniciarTransaccao();
            try {
                // numero mecanografico do funcionario da sessao
                int numMecanograficoSessao = 0;
                PreparedStatement sql = UtilRelacional
                        .prepararComando("SELECT numeroMecanografico FROM ass_FUNCIONARIO WHERE codigoInterno = ?");
                sql.setInt(1, regularizacao.getQuem());
                ResultSet resultadoQuery = sql.executeQuery();
                if (resultadoQuery.next()) {
                    numMecanograficoSessao = resultadoQuery.getInt("numeroMecanografico");
                } else {
                    sql.close();
                    SuportePersistenteOracle.getInstance().cancelarTransaccao();
                    return false;
                }
                sql.close();

                // obtem a sigla de regularizacao de marcacao de ponto
                sql = UtilRelacional.prepararComando("SELECT sigla FROM ass_PARAM_REGULARIZACAO "
                        + "WHERE codigoInterno = ?");

                sql.setInt(1, regularizacao.getChaveParamRegularizacao());
                resultadoQuery = sql.executeQuery();
                String sigla = null;
                if (resultadoQuery.next()) {
                    sigla = resultadoQuery.getString("sigla");
                } else {
                    sql.close();
                    return false;
                }
                sql.close();

                // altera a marcacao de ponto na tabela ASS_MARCAS
                sql = UtilRelacionalOracle.prepararComando("UPDATE ASS_MARCAS SET "
                        + "ASS_MARWHEN = TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS') , " + "ASS_MARWHO = ? "
                        + "WHERE ASS_MARSEQ = ? ");

                // data do registo
                sql.setString(1, regularizacao.getQuando().toString().substring(0,
                        regularizacao.getQuando().toString().indexOf(".")));

                sql.setInt(2, numMecanograficoSessao);
                sql.setInt(3, regularizacao.getChaveMarcacaoPonto());

                sql.executeUpdate();
                sql.close();

                // altera a regularizacao de marcacao de ponto na tabela
                // ASS_MARREG
                sql = UtilRelacionalOracle.prepararComando("UPDATE ASS_MARREG SET "
                        + "ASS_MARREGREGUL = ? " + "WHERE ASS_MARREGMARCAS = ? ");

                sql.setString(1, sigla);
                sql.setInt(2, regularizacao.getChaveMarcacaoPonto());

                sql.executeUpdate();
                sql.close();
                resultado = true;
            } catch (Exception e) {
                SuportePersistenteOracle.getInstance().cancelarTransaccao();
                System.out.println("MarcacaoPontoRelacional.alterarMarcacaoPontoRegularizacao: "
                        + e.toString());
                return false;
            }
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        } catch (Exception e) {
            System.out.println("MarcacaoPontoRelacional.alterarMarcacaoPontoRegularizacao: "
                    + e.toString());
        }
        return resultado;
    } /* alterarMarcacaoPontoRegularizacao */

    // ATENCAO: DESNECESSARIO PARA A APLICACAO DE ASSIDUIDADE, APENAS CONSTRUIDO
    // PARA CONSEGUIR ESCREVER
    // OS DADOS NA BD ORACLE
    public boolean alterarMarcacaoPontoEscreverRegularizacao(RegularizacaoMarcacaoPonto regularizacao) {
        boolean resultado = false;

        /**
         * ************************** escrever a marcacao de ponto na BD Oracle
         * ********************************************
         */
        try {
            SuportePersistenteOracle.getInstance().iniciarTransaccao();
            try {
                // numero mecanografico do funcionario da sessao
                int numMecanograficoSessao = 0;
                PreparedStatement sql = UtilRelacional
                        .prepararComando("SELECT numeroMecanografico FROM ass_FUNCIONARIO WHERE codigoInterno = ?");
                sql.setInt(1, regularizacao.getQuem());
                ResultSet resultadoQuery = sql.executeQuery();
                if (resultadoQuery.next()) {
                    numMecanograficoSessao = resultadoQuery.getInt("numeroMecanografico");
                } else {
                    sql.close();
                    SuportePersistenteOracle.getInstance().cancelarTransaccao();
                    return false;
                }
                sql.close();

                // obtem a sigla de regularizacao de marcacao de ponto
                sql = UtilRelacional.prepararComando("SELECT sigla FROM ass_PARAM_REGULARIZACAO "
                        + "WHERE codigoInterno = ?");

                sql.setInt(1, regularizacao.getChaveParamRegularizacao());
                resultadoQuery = sql.executeQuery();
                String sigla = null;
                if (resultadoQuery.next()) {
                    sigla = resultadoQuery.getString("sigla");
                } else {
                    sql.close();
                    return false;
                }
                sql.close();

                // altera a marcacao de ponto na tabela ASS_MARCAS
                sql = UtilRelacionalOracle.prepararComando("UPDATE ASS_MARCAS SET "
                        + "ASS_MARCARTAO = ?, " + "ASS_MARREGUL = ?, "
                        + "ASS_MARWHEN = TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS') , " + "ASS_MARWHO = ? "
                        + "WHERE ASS_MARSEQ = ? ");

                sql.setInt(1, -1);
                sql.setString(2, "S");
                // data do registo
                sql.setString(3, regularizacao.getQuando().toString().substring(0,
                        regularizacao.getQuando().toString().indexOf(".")));

                sql.setInt(4, numMecanograficoSessao);
                sql.setInt(5, regularizacao.getChaveMarcacaoPonto());

                sql.executeUpdate();
                sql.close();

                // insere a regularizacao de marcacao de ponto na tabela
                // ASS_MARREG
                sql = UtilRelacionalOracle.prepararComando("SELECT ASS_MARPESSOA FROM ASS_MARCAS "
                        + "WHERE ASS_MARSEQ = ?");
                sql.setInt(1, regularizacao.getChaveMarcacaoPonto());
                resultadoQuery = sql.executeQuery();
                PreparedStatement sql2 = null;
                if (resultadoQuery.next()) {
                    sql2 = UtilRelacionalOracle.prepararComando("INSERT INTO ASS_MARREG "
                            + "VALUES (?, ?, ?, ?)");

                    sql2.setInt(1, resultadoQuery.getInt("ASS_MARPESSOA"));
                    sql2.setInt(2, regularizacao.getChaveMarcacaoPonto());
                    sql2.setString(3, sigla);
                    sql2.setInt(4, 1);

                    sql2.executeUpdate();
                    sql2.close();
                } else {
                    sql2.close();
                    sql.close();
                    SuportePersistenteOracle.getInstance().cancelarTransaccao();
                    return false;
                }
                resultado = true;
            } catch (Exception e) {
                SuportePersistenteOracle.getInstance().cancelarTransaccao();
                System.out.println("MarcacaoPontoRelacional.alterarMarcacaoPontoEscreverRegularizacao: "
                        + e.toString());
                return false;
            }
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        } catch (Exception e) {
            System.out.println("MarcacaoPontoRelacional.alterarMarcacaoPontoEscreverRegularizacao: "
                    + e.toString());
        }
        return resultado;
    } /* alterarMarcacaoPontoEscreverRegularizacao */

    // ATENCAO: DESNECESSARIO PARA A APLICACAO DE ASSIDUIDADE, APENAS CONSTRUIDO
    // PARA CONSEGUIR ESCREVER
    // OS DADOS NA BD ORACLE
    public int escreverMarcacaoPontoRegularizacao(MarcacaoPonto marcacaoPonto,
            RegularizacaoMarcacaoPonto regularizacao) {
        int ultimo = 0;

        /**
         * ************************** escrever a marcacao de ponto na BD Oracle
         * ********************************************
         */
        try {
            SuportePersistenteOracle.getInstance().iniciarTransaccao();
            try {

                // numero mecanografico do funcionario da sessao
                int numMecanograficoSessao = 0;
                PreparedStatement sql = UtilRelacional
                        .prepararComando("SELECT numeroMecanografico FROM ass_FUNCIONARIO WHERE codigoInterno = ?");
                sql.setInt(1, regularizacao.getQuem());
                ResultSet resultadoQuery = sql.executeQuery();
                if (resultadoQuery.next()) {
                    numMecanograficoSessao = resultadoQuery.getInt("numeroMecanografico");
                } else {
                    sql.close();
                    SuportePersistenteOracle.getInstance().cancelarTransaccao();
                    return ultimo;
                }
                sql.close();

                // obtem a sigla de regularizacao de marcacao de ponto
                sql = UtilRelacional.prepararComando("SELECT sigla FROM ass_PARAM_REGULARIZACAO "
                        + "WHERE codigoInterno = ?");

                sql.setInt(1, regularizacao.getChaveParamRegularizacao());

                resultadoQuery = sql.executeQuery();
                String sigla = null;
                if (resultadoQuery.next()) {
                    sigla = resultadoQuery.getString("sigla");
                } else {
                    sql.close();
                    return ultimo;
                }
                sql.close();
                // último número de sequencia na tabela

                sql = UtilRelacionalOracle.prepararComando("SELECT s_ordem.NEXTVAL FROM SYS.dual");
                resultadoQuery = sql.executeQuery();
                if (resultadoQuery.next()) {
                    ultimo = resultadoQuery.getInt(1);
                    //ultimo = resultadoQuery.getInt(1) + 1;
                }
                sql.close();

                // insere a marcacao de ponto na tabela ASS_MARCAS
                sql = UtilRelacionalOracle
                        .prepararComando("INSERT INTO ASS_MARCAS "
                                + "VALUES (?, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), ?, ?, ?, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), ?)");

                sql.setInt(1, marcacaoPonto.getNumFuncionario());
                sql.setInt(2, -1);
                if (marcacaoPonto.getData() != null) {
                    sql.setString(3, marcacaoPonto.getData().toString().substring(0,
                            marcacaoPonto.getData().toString().indexOf(".")));
                } else {
                    sql.setString(3, null);
                }
                sql.setString(4, marcacaoPonto.getSiglaUnidade());
                sql.setString(5, "N");
                sql.setString(6, "N");
                sql.setString(7, "S");
                sql.setString(8, "N");
                sql.setString(9, "V");
                sql.setInt(10, ultimo);

                sql.setString(11, regularizacao.getQuando().toString().substring(0,
                        regularizacao.getQuando().toString().indexOf(".")));

                sql.setInt(12, numMecanograficoSessao);

                sql.executeUpdate();
                sql.close();

                // insere a regularizacao de marcacao de ponto na tabela
                // ASS_MARREG
                sql = UtilRelacionalOracle.prepararComando("INSERT INTO ASS_MARREG "
                        + "VALUES (?, ?, ?, ?)");

                sql.setInt(1, marcacaoPonto.getNumFuncionario());
                sql.setInt(2, ultimo);
                sql.setString(3, sigla);
                sql.setInt(4, 1);

                sql.executeUpdate();
                sql.close();
            } catch (Exception e) {
                SuportePersistenteOracle.getInstance().cancelarTransaccao();
                System.out.println("MarcacaoPontoRelacional.escreverMarcacaoPontoRegularizacao: "
                        + e.toString());
                return 0;
            }
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        } catch (Exception e) {
            System.out.println("MarcacaoPontoRelacional.escreverMarcacaoPontoRegularizacao: "
                    + e.toString());
        }
        return ultimo;
    } /* escreverMarcacaoPontoRegularizacao */
}

//««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««
//Marcacao de ponto para LOCALHOST
//»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»
//public class MarcacaoPontoRelacional implements IMarcacaoPontoPersistente {
//	public boolean alterarMarcacaoPonto(MarcacaoPonto marcacaoPonto) {
//		/*
//		 //Acesso a BD da nossa Aplicacao
//		 boolean resultado = false;
//		 
//		try {
//			PreparedStatement sql =
//			UtilRelacional.prepararComando("UPDATE marcacao_ponto SET " +
//			"codigoInterno = ? , " +
//			"unidade = ? , " +
//			"siglaUnidade = ? , " +
//			"dataMarcacao = ? , " +
//			"numCartao = ? , " +
//			"numFuncionario = ? , " +
//			"estado = ? " +
//			"WHERE codigoInterno = ?");
//		 
//			sql.setInt(1, marcacaoPonto.getCodigoInterno());
//			sql.setInt(2, marcacaoPonto.getUnidade());
//			sql.setString(3, marcacaoPonto.getSiglaUnidade());
//			if(marcacaoPonto.getData() != null){
//				sql.setTimestamp(4, new
// java.sql.Timestamp((marcacaoPonto.getData()).getTime()));
//			}else{
//				sql.setTimestamp(4, null);
//			}
//			sql.setInt(5, marcacaoPonto.getNumCartao());
//			sql.setInt(6, marcacaoPonto.getNumFuncionario());
//			sql.setString(7, marcacaoPonto.getEstado());
//			sql.setInt(8, marcacaoPonto.getCodigoInterno());
//		 
//			sql.executeUpdate();
//			sql.close();
//			resultado = true;
//		}
//		catch(Exception e) {
//			System.out.println("MarcacaoPontoRelacional.alterarMarcacaoPonto: " +
// e.toString());
//		}
//		finally {
//			return resultado;
//		}
//		*/
//
//		/****************************** Acesso a BD Oracle
// ********************************/
//		boolean resultado = false;
//
//		try {
//			SuportePersistenteOracle.getInstance().iniciarTransaccao();
//			try {
//				PreparedStatement sql =
//					UtilRelacionalOracle.prepararComando(
//						"UPDATE ASS_MARCAS SET "
//							+ "ASS_MARPESSOA = ? , "
//							+ "ASS_MARCARTAO = ? , "
//							+ "ASS_MARDHMARCA = ?, "
//							+ "ASS_MARUNID = ? , "
//							+ "ASS_MARTIPO = ? , "
//							+ "ASS_MARAUTOJUST = ? , "
//							+ "ASS_MARREGUL = ? , "
//							+ "ASS_MARIES = ? , "
//							+ "ASS_MARSTAT = ? , "
//							+ "ASS_MARSEQ = ? , "
//							+ "ASS_MARWHEN = ?, "
//							+ "ASS_MARWHO = ? "
//							+ "WHERE ASS_MARSEQ = ? ");
//
//				sql.setInt(1, marcacaoPonto.getNumFuncionario());
//				if (marcacaoPonto.getEstado().equals("regularizada")) {
//					sql.setInt(2, -1);
//				} else {
//					sql.setInt(2, marcacaoPonto.getNumCartao());
//				}
//
//				sql.setTimestamp(3, new Timestamp(marcacaoPonto.getData().getTime()));
//
//				sql.setString(4, marcacaoPonto.getSiglaUnidade());
//				sql.setString(5, new String("N"));
//				sql.setString(6, new String("N"));
//				if (marcacaoPonto.getEstado().equals("regularizada")) {
//					sql.setString(7, new String("S"));
//				} else {
//					sql.setString(7, new String("N"));
//				}
//				sql.setString(8, new String("N"));
//				sql.setString(9, new String("V"));
//				sql.setInt(10, marcacaoPonto.getCodigoInterno());
//
//				// data do registo
//				sql.setTimestamp(11, new Timestamp(marcacaoPonto.getData().getTime()));
//
//				sql.setInt(12, 0);
//				sql.setInt(13, marcacaoPonto.getCodigoInterno());
//
//				sql.executeUpdate();
//				sql.close();
//				resultado = true;
//			} catch (Exception e) {
//				SuportePersistenteOracle.getInstance().cancelarTransaccao();
//				System.out.println("MarcacaoPontoRelacional.alterarMarcacaoPonto: " +
// e.toString());
//				return resultado;
//			}
//			SuportePersistenteOracle.getInstance().confirmarTransaccao();
//		} catch (Exception e) {
//			System.out.println("MarcacaoPontoRelacional.alterarMarcacaoPonto: " +
// e.toString());
//		} finally {
//			return resultado;
//		}
//	} /* alterarMarcacaoPonto */
//
//	public boolean apagarMarcacaoPonto(int chaveMarcacao) {
//		boolean resultado = false;
//
//		try {
//			PreparedStatement sql = UtilRelacional.prepararComando("DELETE FROM
// ass_MARCACAO_PONTO WHERE
// codigoInterno = ?");
//
//			sql.setInt(1, chaveMarcacao);
//
//			sql.executeUpdate();
//			sql.close();
//			resultado = true;
//		} catch (Exception e) {
//			System.out.println("MarcacaoPontoRelacional.apagarMarcacaoPonto: " +
// e.toString());
//		} finally {
//			return resultado;
//		}
//	} /* apagarMarcacaoPonto */
//
//	public boolean apagarMarcacaoPonto(Timestamp dataMarcacao) {
//		boolean resultado = false;
//
//		try {
//			PreparedStatement sql = UtilRelacional.prepararComando("DELETE FROM
// ass_MARCACAO_PONTO WHERE
// dataMarcacao = ?");
//
//			sql.setTimestamp(1, new java.sql.Timestamp(dataMarcacao.getTime()));
//
//			sql.executeUpdate();
//			sql.close();
//			resultado = true;
//		} catch (Exception e) {
//			System.out.println("MarcacaoPontoRelacional.apagarMarcacaoPonto: " +
// e.toString());
//		} finally {
//			return resultado;
//		}
//	} /* apagarMarcacaoPonto */
//
//	public boolean apagarMarcacoesPonto() {
//		boolean resultado = false;
//
//		try {
//			PreparedStatement sql = UtilRelacional.prepararComando("DELETE FROM
// ass_MARCACAO_PONTO");
//			sql.executeUpdate();
//			sql.close();
//			/*
//			sql = UtilRelacional.prepararComando("ALTER TABLE ass_MARCACAO_PONTO
// auto_increment=1");
//			sql.executeUpdate();
//			sql.close();
//			*/
//			resultado = true;
//		} catch (Exception e) {
//			System.out.println("MarcacaoPontoRelacional.apagarMarcacoesPonto: " +
// e.toString());
//		} finally {
//			return resultado;
//		}
//	} /* apagarMarcacoesPonto */
//
//	public List consultarMarcacoesPonto(
//		List listaFuncionarios,
//		List listaCartoes,
//		List listaEstados,
//		Timestamp dataInicio,
//		Timestamp dataFim) {
//		/*
//		//Acesso a BD da nossa Aplicacao
//		List marcacoesPonto = null;
//		
//		try {
//			PreparedStatement sql = null;
//			ResultSet resultado = null;
//		
//			// obtem as marcacoes no intervalo de datas
//			StringBuffer query =
//				new StringBuffer(
//					"SELECT * FROM marcacao_ponto WHERE "
//						+ "((dataMarcacao IS NULL) OR (dataMarcacao BETWEEN ? AND ?))");
//		
//			if (listaFuncionarios != null) {
//				if (listaFuncionarios.size() > 0) {
//					query = query.append(" AND (");
//		
//					ListIterator iterListaFuncionarios = listaFuncionarios.listIterator();
//					while (iterListaFuncionarios.hasNext()) {
//						iterListaFuncionarios.next();
//						if (iterListaFuncionarios.hasNext()) {
//							query = query.append("numFuncionario = ? OR ");
//						} else {
//							query = query.append("numFuncionario = ?)");
//						}
//					}
//				}
//			}
//		
//			if (listaCartoes != null) {
//				if (listaCartoes.size() > 0) {
//					query = query.append(" AND (");
//		
//					ListIterator iterListaCartoes = listaCartoes.listIterator();
//					while (iterListaCartoes.hasNext()) {
//						iterListaCartoes.next();
//						if (iterListaCartoes.hasNext()) {
//							query = query.append("numCartao = ? OR ");
//						} else {
//							query = query.append("numCartao = ?)");
//						}
//					}
//				}
//			}
//		
//			if (listaEstados != null) {
//				if (listaEstados.size() > 0) {
//					query = query.append(" AND (");
//		
//					ListIterator iterListaEstados = listaEstados.listIterator();
//					while (iterListaEstados.hasNext()) {
//						iterListaEstados.next();
//						if (iterListaEstados.hasNext()) {
//							query = query.append("estado = ? OR ");
//						} else {
//							query = query.append("estado = ?)");
//						}
//					}
//				}
//			}
//		
//			sql = UtilRelacional.prepararComando(query);
//			sql.setTimestamp(1, new Timestamp(dataInicio.getTime()));
//			sql.setTimestamp(2, new Timestamp(dataFim.getTime()));
//		
//			// indice do comando sql
//			int indice = 3;
//		
//			if (listaFuncionarios != null) {
//				ListIterator iterListaFuncionarios = listaFuncionarios.listIterator();
//				Integer numFuncionario = null;
//		
//				while (iterListaFuncionarios.hasNext()) {
//					numFuncionario = (Integer) iterListaFuncionarios.next();
//					sql.setInt(indice, numFuncionario.intValue());
//					indice++;
//				}
//			}
//		
//			if (listaCartoes != null) {
//				ListIterator iterListaCartoes = listaCartoes.listIterator();
//				Integer numCartao = null;
//		
//				while (iterListaCartoes.hasNext()) {
//					numCartao = (Integer) iterListaCartoes.next();
//					sql.setInt(indice, numCartao.intValue());
//					indice++;
//				}
//			}
//		
//			if (listaEstados != null) {
//				ListIterator iterListaEstados = listaEstados.listIterator();
//				String estado = null;
//		
//				while (iterListaEstados.hasNext()) {
//					estado = (String) iterListaEstados.next();
//					sql.setString(indice, estado);
//					indice++;
//				}
//			}
//		
//			//query = query.append(" ORDER BY dataMarcacao");
//		
//			resultado = sql.executeQuery();
//			marcacoesPonto = new ArrayList();
//			Timestamp dataMarcacao = null;
//		
//			PreparedStatement sql2 =
//				UtilRelacionalOracle.prepararComando(
//					"SELECT * FROM ass_REGULARIZACAO_MARCACAO WHERE "
//						+ "chaveMarcacaoPonto = ? AND chaveParamRegularizacao = 6");
//			ResultSet resultado2 = null;
//		
//			while (resultado.next()) {
//				// retirar as marcacoes de ponto que foram marcadas para anular
//				sql2.setInt(1, resultado.getInt("codigoInterno"));
//				resultado2 = sql2.executeQuery();
//				if (!resultado2.next()) {
//					if (resultado.getString("dataMarcacao") != null) {
//						dataMarcacao = Timestamp.valueOf(resultado.getString("dataMarcacao"));
//					}
//					marcacoesPonto.add(
//						new MarcacaoPonto(
//							resultado.getInt("codigoInterno"),
//							resultado.getInt("unidade"),
//							resultado.getString("siglaUnidade"),
//							dataMarcacao,
//							resultado.getInt("numCartao"),
//							resultado.getInt("numFuncionario"),
//							resultado.getString("estado")));
//				}
//			}
//			sql.close();
//		} catch (Exception e) {
//			System.out.println(
//				"MarcacaoPontoRelacional.consultarMarcacoesPonto: " + e.toString());
//			return null;
//		} finally {
//			return marcacoesPonto;
//		}
//		*/
//		/*********************** Acesso a BD Oracle
// **************************************/
//		//ATENCAO: Acede à BD do Teleponto para ler as marcacoes de ponto
//
//		List marcacoesPonto = null;
//
//		try {
//			SuportePersistenteOracle.getInstance().iniciarTransaccao();
//			try {
//
//				// procura de marcacoes de ponto normais
//				StringBuffer query =
//					new StringBuffer(
//						"SELECT * FROM ASS_MARCAS WHERE ((ASS_MARDHMARCA IS NULL) OR " +
// "(ASS_MARDHMARCA BETWEEN ? AND
// ?))");
//				if (listaFuncionarios != null) {
//					if (listaFuncionarios.size() > 0) {
//						query = query.append(" AND (");
//
//						ListIterator iterListaFuncionarios = listaFuncionarios.listIterator();
//						while (iterListaFuncionarios.hasNext()) {
//							iterListaFuncionarios.next();
//							if (iterListaFuncionarios.hasNext()) {
//								query = query.append("ASS_MARPESSOA = ? OR ");
//							} else {
//								query = query.append("ASS_MARPESSOA = ?)");
//							}
//						}
//					}
//				}
//				if (listaCartoes != null) {
//					if (listaCartoes.size() > 0) {
//						query = query.append(" AND ((");
//
//						ListIterator iterListaCartoes = listaCartoes.listIterator();
//						while (iterListaCartoes.hasNext()) {
//							iterListaCartoes.next();
//							if (iterListaCartoes.hasNext()) {
//								query = query.append("ASS_MARCARTAO = ? OR ");
//							} else {
//								query = query.append("ASS_MARCARTAO = ?)");
//							}
//						}
//					}
//				}
//
//				if (listaCartoes != null && listaFuncionarios != null) {
//					if (listaCartoes.size() > 0 && listaFuncionarios.size() > 0) {
//						query = query.append(" OR (ASS_MARCARTAO = -1 AND (");
//
//						ListIterator iterListaFuncionarios = listaFuncionarios.listIterator();
//						while (iterListaFuncionarios.hasNext()) {
//							iterListaFuncionarios.next();
//							if (iterListaFuncionarios.hasNext()) {
//								query = query.append("ASS_MARPESSOA = ? OR ");
//							} else {
//								query = query.append("ASS_MARPESSOA = ?)))");
//							}
//						}
//					} else {
//						if (listaCartoes.size() > 0) {
//							query = query.append(")");
//						}
//					}
//				} else {
//					if (listaCartoes != null) {
//						query = query.append(")");
//					}
//				}
//
//				PreparedStatement sql =
// UtilRelacionalOracle.prepararComando(query.toString());
//				sql.setTimestamp(1, new Timestamp(dataInicio.getTime()));
//				sql.setTimestamp(2, new Timestamp(dataFim.getTime()));
//
//				// indice do comando sql
//				int indice = 3;
//
//				if (listaFuncionarios != null) {
//					ListIterator iterListaFuncionarios = listaFuncionarios.listIterator();
//					Integer numFuncionario = null;
//
//					while (iterListaFuncionarios.hasNext()) {
//						numFuncionario = (Integer) iterListaFuncionarios.next();
//						sql.setInt(indice, numFuncionario.intValue());
//						indice++;
//					}
//				}
//
//				if (listaCartoes != null) {
//					ListIterator iterListaCartoes = listaCartoes.listIterator();
//					Integer numCartao = null;
//
//					while (iterListaCartoes.hasNext()) {
//						numCartao = (Integer) iterListaCartoes.next();
//						sql.setInt(indice, numCartao.intValue());
//						indice++;
//					}
//				}
//
//				if (listaCartoes != null && listaFuncionarios != null) {
//					if (listaCartoes.size() > 0 && listaFuncionarios.size() > 0) {
//						ListIterator iterListaFuncionarios = listaFuncionarios.listIterator();
//						Integer numFuncionario = null;
//
//						while (iterListaFuncionarios.hasNext()) {
//							numFuncionario = (Integer) iterListaFuncionarios.next();
//							sql.setInt(indice, numFuncionario.intValue());
//							indice++;
//						}
//					}
//				}
//				//query = query.append(" ORDER BY ASS_MARDHMARCA");
//
//				ResultSet resultado = sql.executeQuery();
//				marcacoesPonto = new ArrayList();
//				Timestamp dataMarcacao = null;
//
//				// obtem o codigo das marcacoes a anular
//				PreparedStatement sql2 = UtilRelacional.prepararComando("SELECT * FROM
// ass_PARAM_REGULARIZACAO WHERE
// sigla = 'MA'");
//				ResultSet resultado2 = sql2.executeQuery();
//				int marcacaoAnular = 0;
//				if (resultado2.next()) {
//					marcacaoAnular = resultado2.getInt("codigoInterno");
//				} else {
//					sql2.close();
//					SuportePersistenteOracle.getInstance().cancelarTransaccao();
//					return null;
//				}
//				sql2.close();
//
//				// marcacaoes regularizadas
//				PreparedStatement sql3 =
//					UtilRelacional.prepararComando("SELECT * FROM ass_REGULARIZACAO_MARCACAO
// WHERE " +
// "chaveMarcacaoPonto = ?");
//				ResultSet resultado3 = null;
//				while (resultado.next()) {
//					if (resultado.getString("ASS_MARDHMARCA") != null) {
//						dataMarcacao = Timestamp.valueOf(resultado.getString("ASS_MARDHMARCA"));
//					}
//					MarcacaoPonto marcacaoPonto =
//						new MarcacaoPonto(
//							resultado.getInt("ASS_MARSEQ"),
//							resultado.getString("ASS_MARUNID"),
//							dataMarcacao,
//							resultado.getInt("ASS_MARCARTAO"),
//							resultado.getInt("ASS_MARPESSOA"));
//
//					// diferenciar as marcacoes regularizadas
//					sql3.setInt(1, resultado.getInt("ASS_MARSEQ"));
//					resultado3 = sql3.executeQuery();
//					if (resultado3.next()) {
//						// retirar as marcacoes de ponto que foram marcadas para anular
//						if (resultado3.getInt("chaveParamRegularizacao") == marcacaoAnular) {
//							continue;
//						} else {
//							marcacaoPonto.setEstado("regularizada");
//							marcacaoPonto.setSiglaUnidade("");
//							marcacaoPonto.setNumCartao(0);
//						}
//					}
//					marcacoesPonto.add(marcacaoPonto);
//				}
//				sql3.close();
//				sql.close();
//			} catch (Exception e) {
//				SuportePersistenteOracle.getInstance().cancelarTransaccao();
//				System.out.println("MarcacaoPontoRelacional.consultarMarcacoesPonto: " +
// e.toString());
//				return null;
//			}
//			SuportePersistenteOracle.getInstance().confirmarTransaccao();
//		} catch (Exception e) {
//			System.out.println("MarcacaoPontoRelacional.consultarMarcacoesPonto: " +
// e.toString());
//			return null;
//		} finally {
//			return marcacoesPonto;
//		}
//	} /* consultarMarcacoesPonto */
//
//	public List consultarMarcacoesPontoErros(String estado) { //não será
// preciso
//		List errosLeitura = null;
//
//		try {
//			/*
//			String query = "SELECT * FROM marcacao_ponto WHERE estado = '" + estado +
// "'";
//			PreparedStatement sql = UtilRelacional.prepararComando(query);
//			
//			ResultSet resultado = sql.executeQuery();
//			errosLeitura = new ArrayList();
//			Timestamp dataMarcacao = null;
//			
//			while (resultado.next()) {
//				if (resultado.getString("dataMarcacao") != null) {
//					dataMarcacao = Timestamp.valueOf(resultado.getString("dataMarcacao"));
//				}
//				errosLeitura.add(
//					new MarcacaoPonto(
//						resultado.getInt("codigoInterno"),
//						resultado.getInt("unidade"),
//						resultado.getString("siglaUnidade"),
//						dataMarcacao,
//						resultado.getInt("numCartao"),
//						resultado.getInt("numFuncionario"),
//						resultado.getString("estado")));
//			}
//			sql.close();
//			*/
//		} catch (Exception e) {
//			System.out.println("MarcacaoPontoRelacional.consultarMarcacoesPontoErros: " +
// e.toString());
//			return null;
//		} finally {
//			return errosLeitura;
//		}
//	} /* consultarMarcacoesPontoErros */
//
//	public boolean escreverMarcacaoPonto(MarcacaoPonto marcacaoPonto) {
//		boolean resultado = false;
//		try {
//			PreparedStatement sql = UtilRelacional.prepararComando("INSERT INTO
// ass_MARCACAO_PONTO VALUES (?, ?,
// ?, ?, ?, ?, ?)");
//
//			sql.setInt(1, marcacaoPonto.getCodigoInterno());
//			sql.setInt(2, marcacaoPonto.getUnidade());
//			sql.setString(3, marcacaoPonto.getSiglaUnidade());
//			if (marcacaoPonto.getData() != null) {
//				sql.setTimestamp(4, new Timestamp((marcacaoPonto.getData()).getTime()));
//			} else {
//				sql.setTimestamp(4, null);
//			}
//			sql.setInt(5, marcacaoPonto.getNumCartao());
//			sql.setInt(6, marcacaoPonto.getNumFuncionario());
//			sql.setString(7, marcacaoPonto.getEstado());
//
//			sql.executeUpdate();
//			sql.close();
//			resultado = true;
//		} catch (Exception e) {
//			System.out.println("MarcacaoPontoRelacional.escreverMarcacaoPonto: " +
// e.toString());
//		} finally {
//			return resultado;
//		}
//	} /* escreverMarcacaoPonto */
//
//	public boolean escreverMarcacoesPonto(List listaMarcacoes) {
//		boolean resultado = false;
//
//		ListIterator iterador = listaMarcacoes.listIterator();
//		MarcacaoPonto marcacaoPonto = null;
//
//		try {
//			PreparedStatement sql = UtilRelacional.prepararComando("INSERT INTO
// ass_MARCACAO_PONTO VALUES (?, ?,
// ?, ?, ?, ?, ?)");
//
//			while (iterador.hasNext()) {
//				marcacaoPonto = (MarcacaoPonto) iterador.next();
//
//				sql.setInt(1, marcacaoPonto.getCodigoInterno());
//				sql.setInt(2, marcacaoPonto.getUnidade());
//				sql.setString(3, marcacaoPonto.getSiglaUnidade());
//				if (marcacaoPonto.getData() != null) {
//					sql.setTimestamp(4, new Timestamp((marcacaoPonto.getData()).getTime()));
//				} else {
//					sql.setTimestamp(4, null);
//				}
//				sql.setInt(5, marcacaoPonto.getNumCartao());
//				sql.setInt(6, marcacaoPonto.getNumFuncionario());
//				sql.setString(7, marcacaoPonto.getEstado());
//
//				sql.executeUpdate();
//			}
//			sql.close();
//			resultado = true;
//		} catch (Exception e) {
//			System.out.println("MarcacaoPontoRelacional.escreverMarcacoesPonto: " +
// e.toString());
//		} finally {
//			return resultado;
//		}
//	} /* escreverMarcacoesPonto */
//
//	public MarcacaoPonto lerMarcacaoPonto(int codigoInterno) {
//		MarcacaoPonto marcacaoPonto = null;
//
//		try {
//			PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM
// ass_MARCACAO_PONTO WHERE
// codigoInterno = ?");
//
//			sql.setInt(1, codigoInterno);
//
//			ResultSet resultado = sql.executeQuery();
//			Timestamp dataMarcacao = null;
//			if (resultado.next()) {
//				if (resultado.getString("dataMarcacao") != null) {
//					dataMarcacao = Timestamp.valueOf(resultado.getString("dataMarcacao"));
//				}
//				marcacaoPonto =
//					new MarcacaoPonto(
//						resultado.getInt("codigoInterno"),
//						resultado.getInt("unidade"),
//						resultado.getString("siglaUnidade"),
//						dataMarcacao,
//						resultado.getInt("numCartao"),
//						resultado.getInt("numFuncionario"),
//						resultado.getString("estado"));
//			}
//			sql.close();
//		} catch (Exception e) {
//			System.out.println("MarcacaoPontoRelacional.lerMarcacaoPonto: " +
// e.toString());
//		} finally {
//			return marcacaoPonto;
//		}
//	} /* lerMarcacaoPonto */
//
//	public MarcacaoPonto lerMarcacaoPonto(Timestamp dataMarcacao, int
// numFuncionario) {
//		/*
//		// Acesso a nossa base de dados
//		MarcacaoPonto marcacaoPonto = null;
//		 
//		try {
//			PreparedStatement sql =
//			UtilRelacional.prepararComando("SELECT * FROM marcacao_ponto " +
//			"WHERE dataMarcacao = ? AND numFuncionario = ?");
//		 
//			sql.setTimestamp(1, dataMarcacao);
//			sql.setInt(2, numFuncionario);
//		 
//			ResultSet resultado = sql.executeQuery();
//			if (resultado.next()) {
//				Timestamp data = null;
//				if(resultado.getString("dataMarcacao") != null){
//					data = Timestamp.valueOf(resultado.getString("dataMarcacao"));
//				}
//				marcacaoPonto = new MarcacaoPonto(resultado.getInt("codigoInterno"),
//				resultado.getInt("unidade"),
//				resultado.getString("siglaUnidade"),
//				data,
//				resultado.getInt("numCartao"),
//				resultado.getInt("numFuncionario"),
//				resultado.getString("estado"));
//			}
//			sql.close();
//		}
//		catch(Exception e) {
//			System.out.println("MarcacaoPontoRelacional.lerMarcacaoPonto: " +
// e.toString());
//		}
//		finally {
//			return marcacaoPonto;
//		}
//		 */
//		/************************************** ler a marcacao de ponto na BD Oracle
// *********************************************/
//		MarcacaoPonto marcacaoPonto = null;
//
//		try {
//			SuportePersistenteOracle.getInstance().iniciarTransaccao();
//			try {
//
//				PreparedStatement sql =
//					UtilRelacionalOracle.prepararComando(
//						"SELECT * FROM ASS_MARCAS " + "WHERE ASS_MARDHMARCA = ? AND ASS_MARPESSOA =
// ?");
//
//				sql.setTimestamp(1, new Timestamp(dataMarcacao.getTime()));
//				sql.setInt(2, numFuncionario);
//
//				ResultSet resultadoQuery = sql.executeQuery();
//				if (resultadoQuery.next()) {
//					marcacaoPonto =
//						new MarcacaoPonto(
//							resultadoQuery.getInt("ASS_MARSEQ"),
//							resultadoQuery.getString("ASS_MARUNID"),
//							Timestamp.valueOf(resultadoQuery.getString("ASS_MARDHMARCA")),
//							resultadoQuery.getInt("ASS_MARCARTAO"),
//							resultadoQuery.getInt("ASS_MARPESSOA"));
//				} else {
//					sql.close();
//					return null;
//				}
//				sql.close();
//
//				if (marcacaoPonto != null) {
//					sql = UtilRelacional.prepararComando("SELECT codigoInterno FROM
// ass_UNIDADE_MARCACAO " + "WHERE sigla
// = ?");
//
//					sql.setString(1, marcacaoPonto.getSiglaUnidade());
//					resultadoQuery = sql.executeQuery();
//					if (resultadoQuery.next()) {
//						marcacaoPonto.setUnidade(resultadoQuery.getInt("codigoInterno"));
//					}
//					sql.close();
//
//					sql = UtilRelacionalOracle.prepararComando("SELECT * FROM ASS_MARREG " +
// "WHERE ASS_MARREGMARCAS =
// ?");
//
//					sql.setInt(1, marcacaoPonto.getCodigoInterno());
//					resultadoQuery = sql.executeQuery();
//					if (resultadoQuery.next()) {
//						marcacaoPonto.setEstado("regularizada");
//					}
//					sql.close();
//				}
//			} catch (Exception e) {
//				SuportePersistenteOracle.getInstance().cancelarTransaccao();
//				System.out.println("MarcacaoPontoRelacional.lerMarcacaoPonto: " +
// e.toString());
//				return null;
//			}
//			SuportePersistenteOracle.getInstance().confirmarTransaccao();
//		} catch (Exception e) {
//			System.out.println("MarcacaoPontoRelacional.lerMarcacaoPonto: " +
// e.toString());
//			return null;
//		} finally {
//			return marcacaoPonto;
//		}
//	} /* lerMarcacaoPonto */
//
//	public List lerMarcacoesPonto() {
//		/*
//		 //Acesso a BD da nossa Aplicação
//		 List marcacoesPonto = null;
//		 
//		try {
//			PreparedStatement sql =
//			UtilRelacional.prepararComando("SELECT * FROM marcacao_ponto");
//		 
//			ResultSet resultado = sql.executeQuery();
//			marcacoesPonto = new ArrayList();
//			Timestamp dataMarcacao = null;
//			while(resultado.next()) {
//				if(resultado.getString("dataMarcacao") != null){
//					dataMarcacao = Timestamp.valueOf(resultado.getString("dataMarcacao"));
//				}
//				marcacoesPonto.add(new MarcacaoPonto(resultado.getInt("codigoInterno"),
//				resultado.getInt("unidade"),
//				resultado.getString("siglaUnidade"),
//				dataMarcacao,
//				resultado.getInt("numCartao"),
//				resultado.getInt("numFuncionario"),
//				resultado.getString("estado")));
//				dataMarcacao = null;
//			}
//			sql.close();
//		}
//		catch(Exception e) {
//			System.out.println("MarcacaoPontoRelacional.lerMarcacoesPonto: " +
// e.toString());
//		}
//		finally {
//			return marcacoesPonto;
//		}
//		*/
//
//		/**************************** ler Marcações da BD Oracle
// *********************************/
//		List marcacoesPonto = null;
//
//		try {
//			SuportePersistenteOracle.getInstance().iniciarTransaccao();
//			try {
//				PreparedStatement sql = UtilRelacionalOracle.prepararComando("SELECT * FROM
// ASS_MARCAS");
//				ResultSet resultado = sql.executeQuery();
//
//				marcacoesPonto = new ArrayList();
//				Timestamp dataMarcacao = null;
//				MarcacaoPonto marcacaoPonto = null;
//
//				// chaveUnidade
//				PreparedStatement sql2 =
//					UtilRelacional.prepararComando("SELECT codigoInterno FROM
// ass_UNIDADE_MARCACAO WHERE sigla = ?");
//				ResultSet resultado2 = null;
//
//				// marcacoes de ponto regularizadas nao tem unidade de marcacao
//				String sigla = new String(" ");
//				int chaveUnidade = 0;
//
//				while (resultado.next()) {
//					if (resultado.getString("ASS_MARUNID") != null) {
//						// obtem o codigo interno da unidade de marcacao
//						sql2.setString(1, resultado.getString("ASS_MARUNID"));
//						resultado2 = sql2.executeQuery();
//						if (resultado2.next()) {
//							sigla = resultado.getString("ASS_MARUNID");
//							chaveUnidade = resultado2.getInt("codigoInterno");
//						}
//					}
//					if (resultado.getString("ASS_MARDHMARCA") != null) {
//						dataMarcacao = Timestamp.valueOf(resultado.getString("ASS_MARDHMARCA"));
//					}
//					marcacaoPonto =
//						new MarcacaoPonto(
//							resultado.getInt("ASS_MARSEQ"),
//							chaveUnidade,
//							sigla,
//							dataMarcacao,
//							resultado.getInt("ASS_MARCARTAO"),
//							resultado.getInt("ASS_MARPESSOA"));
//
//					if (resultado.getString("ASS_MARREGUL").equals("S")) {
//						marcacaoPonto.setEstado("regularizada");
//					}
//					marcacoesPonto.add(marcacaoPonto);
//					dataMarcacao = null;
//					marcacaoPonto = null;
//				}
//				sql2.close();
//				sql.close();
//
//			} catch (Exception e) {
//				SuportePersistenteOracle.getInstance().cancelarTransaccao();
//				System.out.println("MarcacaoPontoRelacional.lerMarcacoesPonto: " +
// e.toString());
//				return null;
//			}
//			SuportePersistenteOracle.getInstance().confirmarTransaccao();
//		} catch (Exception e) {
//			System.out.println("MarcacaoPontoRelacional.lerMarcacoesPonto: " +
// e.toString());
//			return null;
//		} finally {
//			return marcacoesPonto;
//		}
//	} /* lerMarcacoesPonto */
//
//	public List lerMarcacoesPonto(int numCartao) {
//		/*
//		 // Acesso a BD da nossa Aplicação
//		List marcacoesPonto = null;
//		 
//		try {
//			PreparedStatement sql =
//			UtilRelacional.prepararComando("SELECT * FROM marcacao_ponto " +
//			"WHERE numCartao = ? ");
//		 
//			sql.setInt(1, numCartao);
//		 
//			ResultSet resultado = sql.executeQuery();
//			marcacoesPonto = new ArrayList();
//			Timestamp dataMarcacao = null;
//			while(resultado.next()) {
//				if(resultado.getString("dataMarcacao") != null){
//					dataMarcacao = Timestamp.valueOf(resultado.getString("dataMarcacao"));
//				}
//				marcacoesPonto.add(new MarcacaoPonto(resultado.getInt("codigoInterno"),
//				resultado.getInt("unidade"),
//				resultado.getString("siglaUnidade"),
//				dataMarcacao,
//				resultado.getInt("numCartao"),
//				resultado.getInt("numFuncionario"),
//				resultado.getString("estado")));
//				dataMarcacao = null;
//			}
//			sql.close();
//		}
//		catch(Exception e) {
//			System.out.println("MarcacaoPontoRelacional.lerMarcacoesPonto: " +
// e.toString());
//		}
//		finally {
//			return marcacoesPonto;
//		}
//		*/
//
//		/**************************** ler Marcações da BD Oracle
// *********************************/
//		List marcacoesPonto = null;
//
//		try {
//			SuportePersistenteOracle.getInstance().iniciarTransaccao();
//			try {
//				PreparedStatement sql = UtilRelacionalOracle.prepararComando("SELECT * FROM
// ASS_MARCAS WHERE
// ASS_MARCARTAO = ? ");
//
//				sql.setInt(1, numCartao);
//
//				ResultSet resultado = sql.executeQuery();
//
//				marcacoesPonto = new ArrayList();
//				Timestamp dataMarcacao = null;
//				while (resultado.next()) {
//					if (resultado.getString("ASS_MARDHMARCA") != null) {
//						dataMarcacao =
// java.sql.Timestamp.valueOf(resultado.getString("ASS_MARDHMARCA"));
//					}
//
//					marcacoesPonto.add(
//						new MarcacaoPonto(
//							resultado.getInt("ASS_MARSEQ"),
//							resultado.getString("ASS_MARUNID"),
//							dataMarcacao,
//							resultado.getInt("ASS_MARCARTAO"),
//							resultado.getInt("ASS_MARPESSOA")));
//					dataMarcacao = null;
//				}
//				sql.close();
//			} catch (Exception e) {
//				SuportePersistenteOracle.getInstance().cancelarTransaccao();
//				System.out.println("MarcacaoPontoRelacional.lerMarcacoesPonto: " +
// e.toString());
//				return null;
//			}
//			SuportePersistenteOracle.getInstance().confirmarTransaccao();
//		} catch (Exception e) {
//			System.out.println("MarcacaoPontoRelacional.lerMarcacoesPonto: " +
// e.toString());
//			return null;
//		} finally {
//			return marcacoesPonto;
//		}
//	} /* lerMarcacoesPonto */
//
//	public List obterMarcacoesPonto(int numFuncionario, int numCartao,
// Timestamp dataFim) {
//		/*
//		 // Acesso a BD da nossa Aplicacao
//		 List marcacoesPonto = null;
//		 
//		try {
//			PreparedStatement sql =
//			UtilRelacional.prepararComando("SELECT * FROM marcacao_ponto " +
//			"WHERE dataMarcacao>=? AND numFuncionario=? AND numCartao=?");
//		 
//			sql.setTimestamp(1, new Timestamp(dataFim.getTime()));
//			sql.setInt(2, numFuncionario);
//			sql.setInt(3, numCartao);
//		 
//			ResultSet resultado = sql.executeQuery();
//			marcacoesPonto = new ArrayList();
//			Timestamp dataMarcacao = null;
//			while(resultado.next()) {
//				if(resultado.getString("dataMarcacao") != null){
//					dataMarcacao = Timestamp.valueOf(resultado.getString("dataMarcacao"));
//				}
//				marcacoesPonto.add(new MarcacaoPonto(resultado.getInt("codigoInterno"),
//				resultado.getInt("unidade"),
//				resultado.getString("siglaUnidade"),
//				dataMarcacao,
//				resultado.getInt("numCartao"),
//				resultado.getInt("numFuncionario"),
//				resultado.getString("estado")));
//				dataMarcacao = null;
//			}
//			sql.close();
//		}
//		catch(Exception e) {
//			System.out.println("MarcacaoPontoRelacional.obterMarcacoesPonto: " +
// e.toString());
//		}
//		finally {
//			return marcacoesPonto;
//		}
//		*/
//		/********************** Acesso a BD oracle *********************************/
//		List marcacoesPonto = null;
//
//		try {
//			SuportePersistenteOracle.getInstance().iniciarTransaccao();
//			try {
//
//				PreparedStatement sql =
//					UtilRelacionalOracle.prepararComando(
//						"SELECT * FROM ASS_MARCAS " + "WHERE ASS_MARDHMARCA>=? AND ASS_MARPESSOA=?
// AND ASS_MARCARTAO=?");
//				sql.setTimestamp(1, dataFim);
//
//				sql.setInt(2, numFuncionario);
//				sql.setInt(3, numCartao);
//
//				ResultSet resultado = sql.executeQuery();
//				marcacoesPonto = new ArrayList();
//				Timestamp dataMarcacao = null;
//
//				while (resultado.next()) {
//					if (resultado.getString("ASS_MARDHMARCA") != null) {
//						dataMarcacao =
// java.sql.Timestamp.valueOf(resultado.getString("ASS_MARDHMARCA"));
//					}
//
//					marcacoesPonto.add(
//						new MarcacaoPonto(
//							resultado.getInt("ASS_MARSEQ"),
//							resultado.getString("ASS_MARUNID"),
//							dataMarcacao,
//							resultado.getInt("ASS_MARCARTAO"),
//							resultado.getInt("ASS_MARPESSOA")));
//
//					dataMarcacao = null;
//				}
//				sql.close();
//
//			} catch (Exception e) {
//				SuportePersistenteOracle.getInstance().cancelarTransaccao();
//				System.out.println("MarcacaoPontoRelacional.obterMarcacoesPonto: " +
// e.toString());
//				return null;
//			}
//			SuportePersistenteOracle.getInstance().confirmarTransaccao();
//		} catch (Exception e) {
//			System.out.println("MarcacaoPontoRelacional.obterMarcacoesPonto: " +
// e.toString());
//			return null;
//		} finally {
//			return marcacoesPonto;
//		}
//	} /* obterMarcacoesPonto */
//
//	public List obterMarcacoesPonto(int numCartao, Timestamp dataInicio,
// Timestamp dataFim) {
//		/*
//		 // Acesso a BD da nossa Aplicacao
//		 List marcacoesPonto = null;
//		 
//		try {
//			PreparedStatement sql =
//			UtilRelacional.prepararComando("SELECT * FROM marcacao_ponto " +
//			"WHERE numCartao=? AND dataMarcacao>=? AND dataMarcacao<=?");
//		 
//			sql.setInt(1, numCartao);
//			sql.setTimestamp(2, new Timestamp(dataInicio.getTime()));
//			sql.setTimestamp(3, new Timestamp(dataFim.getTime()));
//		 
//			ResultSet resultado = sql.executeQuery();
//			marcacoesPonto = new ArrayList();
//			Timestamp dataMarcacao = null;
//			while(resultado.next()) {
//				if(resultado.getString("dataMarcacao") != null){
//					dataMarcacao = Timestamp.valueOf(resultado.getString("dataMarcacao"));
//				}
//				marcacoesPonto.add(new MarcacaoPonto(resultado.getInt("codigoInterno"),
//				resultado.getInt("unidade"),
//				resultado.getString("siglaUnidade"),
//				dataMarcacao,
//				resultado.getInt("numCartao"),
//				resultado.getInt("numFuncionario"),
//				resultado.getString("estado")));
//				dataMarcacao = null;
//			}
//			sql.close();
//		}
//		catch(Exception e) {
//			System.out.println("MarcacaoPontoRelacional.obterMarcacoesPonto: " +
// e.toString());
//		}
//		finally {
//			return marcacoesPonto;
//		}
//		*/
//
//		/********************** Acesso a BD oracle *********************************/
//		List marcacoesPonto = null;
//
//		try {
//			SuportePersistenteOracle.getInstance().iniciarTransaccao();
//			try {
//				PreparedStatement sql =
//					UtilRelacionalOracle.prepararComando(
//						"SELECT * FROM ASS_MARCAS " + "WHERE ASS_MARCARTAO=? AND " +
// "ASS_MARDHMARCA>=? AND
// ASS_MARDHMARCA<=?");
//
//				sql.setInt(1, numCartao);
//
//				sql.setTimestamp(2, dataInicio);
//				sql.setTimestamp(3, dataFim);
//
//				ResultSet resultado = sql.executeQuery();
//
//				marcacoesPonto = new ArrayList();
//				Timestamp dataMarcacao = null;
//				while (resultado.next()) {
//					if (resultado.getString("ASS_MARDHMARCA") != null) {
//						dataMarcacao =
// java.sql.Timestamp.valueOf(resultado.getString("ASS_MARDHMARCA"));
//					}
//
//					marcacoesPonto.add(
//						new MarcacaoPonto(
//							resultado.getInt("ASS_MARSEQ"),
//							resultado.getString("ASS_MARUNID"),
//							dataMarcacao,
//							resultado.getInt("ASS_MARCARTAO"),
//							resultado.getInt("ASS_MARPESSOA")));
//				}
//				sql.close();
//			} catch (Exception e) {
//				SuportePersistenteOracle.getInstance().cancelarTransaccao();
//				System.out.println("MarcacaoPontoRelacional.obterMarcacoesPonto: " +
// e.toString());
//				return null;
//			}
//			SuportePersistenteOracle.getInstance().confirmarTransaccao();
//		} catch (Exception e) {
//			System.out.println("MarcacaoPontoRelacional.obterMarcacoesPonto: " +
// e.toString());
//			return null;
//		} finally {
//			return marcacoesPonto;
//		}
//	} /* obterMarcacoesPonto */
//
//	public int ultimoCodigoInterno() {
//		/*
//		int ultimo = 0;
//		
//		try {
//			PreparedStatement sql = UtilRelacional.prepararComando("SELECT
// MAX(codigoInterno) FROM
// marcacao_ponto");
//		
//			ResultSet resultado = sql.executeQuery();
//			if (resultado.next()) {
//				ultimo = resultado.getInt(1);
//			}
//			sql.close();
//		} catch (Exception e) {
//			System.out.println("MarcacaoPontoRelacional.ultimoCodigoInterno: " +
// e.toString());
//		} finally {
//			return ultimo;
//		}
//		*/
//		int ultimo = 0;
//		try {
//			SuportePersistenteOracle.getInstance().iniciarTransaccao();
//			try {
//				// último número de sequencia na tabela
//				PreparedStatement sql = UtilRelacionalOracle.prepararComando("SELECT
// MAX(ASS_MARSEQ) FROM
// ASS_MARCAS");
//				ResultSet resultadoQuery = sql.executeQuery();
//				if (resultadoQuery.next()) {
//					ultimo = resultadoQuery.getInt(1) + 1;
//				}
//				sql.close();
//
//			} catch (Exception e) {
//				SuportePersistenteOracle.getInstance().cancelarTransaccao();
//				System.out.println("MarcacaoPontoRelacional.ultimoCodigoInterno: " +
// e.toString());
//				return ultimo;
//			}
//			SuportePersistenteOracle.getInstance().confirmarTransaccao();
//		} catch (Exception e) {
//			System.out.println("MarcacaoPontoRelacional.ultimoCodigoInterno: " +
// e.toString());
//		} finally {
//			return ultimo;
//		}
//	} /* ultimoCodigoInterno */
//
//	// ATENCAO: DESNECESSARIO PARA A APLICACAO DE ASSIDUIDADE, APENAS CONSTRUIDO
// PARA CONSEGUIR ESCREVER
// OS DADOS NA BD ORACLE
//	public boolean alterarMarcacaoPontoRegularizacao(RegularizacaoMarcacaoPonto
// regularizacao) {
//		boolean resultado = false;
//
//		/**************************** escrever a marcacao de ponto na BD Oracle
// *********************************************/
//		try {
//			SuportePersistenteOracle.getInstance().iniciarTransaccao();
//			try {
//				// numero mecanografico do funcionario da sessao
//				int numMecanograficoSessao = 0;
//				PreparedStatement sql =
//					UtilRelacional.prepararComando("SELECT numeroMecanografico FROM
// ass_FUNCIONARIO WHERE codigoInterno =
// ?");
//				sql.setInt(1, regularizacao.getQuem());
//				ResultSet resultadoQuery = sql.executeQuery();
//				if (resultadoQuery.next()) {
//					numMecanograficoSessao = resultadoQuery.getInt("numeroMecanografico");
//				} else {
//					sql.close();
//					SuportePersistenteOracle.getInstance().cancelarTransaccao();
//					return false;
//				}
//				sql.close();
//
//				// obtem a sigla de regularizacao de marcacao de ponto
//				sql = UtilRelacional.prepararComando("SELECT sigla FROM
// ass_PARAM_REGULARIZACAO " + "WHERE
// codigoInterno = ?");
//
//				sql.setInt(1, regularizacao.getChaveParamRegularizacao());
//				resultadoQuery = sql.executeQuery();
//				String sigla = null;
//				if (resultadoQuery.next()) {
//					sigla = resultadoQuery.getString("sigla");
//				} else {
//					sql.close();
//					return false;
//				}
//				sql.close();
//
//				// altera a marcacao de ponto na tabela ASS_MARCAS
//				sql =
//					UtilRelacionalOracle.prepararComando(
//						"UPDATE ASS_MARCAS SET " + "ASS_MARWHEN = ?, " + "ASS_MARWHO = ? " + "WHERE
// ASS_MARSEQ = ? ");
//
//				// data do registo
//				sql.setTimestamp(1, new Timestamp(regularizacao.getQuando().getTime()));
//
//				sql.setInt(2, numMecanograficoSessao);
//				sql.setInt(3, regularizacao.getChaveMarcacaoPonto());
//
//				sql.executeUpdate();
//				sql.close();
//
//				// altera a regularizacao de marcacao de ponto na tabela ASS_MARREG
//				sql =
//					UtilRelacionalOracle.prepararComando(
//						"UPDATE ASS_MARREG SET " + "ASS_MARREGREGUL = ? " + "WHERE ASS_MARREGMARCAS =
// ? ");
//
//				sql.setString(1, sigla);
//				sql.setInt(2, regularizacao.getChaveMarcacaoPonto());
//
//				sql.executeUpdate();
//				sql.close();
//				resultado = true;
//			} catch (Exception e) {
//				SuportePersistenteOracle.getInstance().cancelarTransaccao();
//				System.out.println("MarcacaoPontoRelacional.alterarMarcacaoPontoRegularizacao:
// " + e.toString());
//				return false;
//			}
//			SuportePersistenteOracle.getInstance().confirmarTransaccao();
//		} catch (Exception e) {
//			System.out.println("MarcacaoPontoRelacional.alterarMarcacaoPontoRegularizacao:
// " + e.toString());
//		} finally {
//			return resultado;
//		}
//	} /* alterarMarcacaoPontoRegularizacao */
//
//	// ATENCAO: DESNECESSARIO PARA A APLICACAO DE ASSIDUIDADE, APENAS CONSTRUIDO
// PARA CONSEGUIR ESCREVER
// OS DADOS NA BD ORACLE
//	public boolean
// alterarMarcacaoPontoEscreverRegularizacao(RegularizacaoMarcacaoPonto
// regularizacao) {
//		boolean resultado = false;
//
//		/**************************** escrever a marcacao de ponto na BD Oracle
// *********************************************/
//		try {
//			SuportePersistenteOracle.getInstance().iniciarTransaccao();
//			try {
//				// numero mecanografico do funcionario da sessao
//				int numMecanograficoSessao = 0;
//				PreparedStatement sql =
//					UtilRelacional.prepararComando("SELECT numeroMecanografico FROM
// ass_FUNCIONARIO WHERE codigoInterno =
// ?");
//				sql.setInt(1, regularizacao.getQuem());
//				ResultSet resultadoQuery = sql.executeQuery();
//				if (resultadoQuery.next()) {
//					numMecanograficoSessao = resultadoQuery.getInt("numeroMecanografico");
//				} else {
//					sql.close();
//					SuportePersistenteOracle.getInstance().cancelarTransaccao();
//					return false;
//				}
//				sql.close();
//
//				// obtem a sigla de regularizacao de marcacao de ponto
//				sql = UtilRelacional.prepararComando("SELECT sigla FROM
// ass_PARAM_REGULARIZACAO " + "WHERE
// codigoInterno = ?");
//
//				sql.setInt(1, regularizacao.getChaveParamRegularizacao());
//				resultadoQuery = sql.executeQuery();
//				String sigla = null;
//				if (resultadoQuery.next()) {
//					sigla = resultadoQuery.getString("sigla");
//				} else {
//					sql.close();
//					return false;
//				}
//				sql.close();
//
//				// altera a marcacao de ponto na tabela ASS_MARCAS
//				sql =
//					UtilRelacionalOracle.prepararComando(
//						"UPDATE ASS_MARCAS SET "
//							+ "ASS_MARCARTAO = ?, "
//							+ "ASS_MARREGUL = ?, "
//							+ "ASS_MARWHEN = ?, "
//							+ "ASS_MARWHO = ? "
//							+ "WHERE ASS_MARSEQ = ? ");
//
//				sql.setInt(1, -1);
//				sql.setString(2, "S");
//				// data do registo
//				//sql.setString(3, regularizacao.getQuando().toString().substring(0,
// regularizacao.getQuando().toString().indexOf(".")));
//				sql.setTimestamp(3, new Timestamp(regularizacao.getQuando().getTime()));
//
//				sql.setInt(4, numMecanograficoSessao);
//				sql.setInt(5, regularizacao.getChaveMarcacaoPonto());
//
//				sql.executeUpdate();
//				sql.close();
//
//				// insere a regularizacao de marcacao de ponto na tabela ASS_MARREG
//				sql = UtilRelacionalOracle.prepararComando("SELECT ASS_MARPESSOA FROM
// ASS_MARCAS " + "WHERE
// ASS_MARSEQ = ?");
//				sql.setInt(1, regularizacao.getChaveMarcacaoPonto());
//				resultadoQuery = sql.executeQuery();
//				PreparedStatement sql2 = null;
//				if (resultadoQuery.next()) {
//					sql2 = UtilRelacionalOracle.prepararComando("INSERT INTO ASS_MARREG " +
// "VALUES (?, ?, ?, ?)");
//
//					sql2.setInt(1, resultadoQuery.getInt("ASS_MARPESSOA"));
//					sql2.setInt(2, regularizacao.getChaveMarcacaoPonto());
//					sql2.setString(3, sigla);
//					sql2.setInt(4, 1);
//
//					sql2.executeUpdate();
//					sql2.close();
//				} else {
//					sql2.close();
//					sql.close();
//					SuportePersistenteOracle.getInstance().cancelarTransaccao();
//					return false;
//				}
//				resultado = true;
//			} catch (Exception e) {
//				SuportePersistenteOracle.getInstance().cancelarTransaccao();
//				System.out.println("MarcacaoPontoRelacional.alterarMarcacaoPontoEscreverRegularizacao:
// " +
// e.toString());
//				return false;
//			}
//			SuportePersistenteOracle.getInstance().confirmarTransaccao();
//		} catch (Exception e) {
//			System.out.println("MarcacaoPontoRelacional.alterarMarcacaoPontoEscreverRegularizacao:
// " +
// e.toString());
//		} finally {
//			return resultado;
//		}
//	} /* alterarMarcacaoPontoEscreverRegularizacao */
//
//	// ATENCAO: DESNECESSARIO PARA A APLICACAO DE ASSIDUIDADE, APENAS CONSTRUIDO
// PARA CONSEGUIR ESCREVER
// OS DADOS NA BD ORACLE
//	public int escreverMarcacaoPontoRegularizacao(MarcacaoPonto marcacaoPonto,
// RegularizacaoMarcacaoPonto
// regularizacao) {
//		int ultimo = 0;
//
//		/**************************** escrever a marcacao de ponto na BD Oracle
// *********************************************/
//		try {
//			SuportePersistenteOracle.getInstance().iniciarTransaccao();
//			try {
//
//				// numero mecanografico do funcionario da sessao
//				int numMecanograficoSessao = 0;
//				PreparedStatement sql =
//					UtilRelacional.prepararComando("SELECT numeroMecanografico FROM
// ass_FUNCIONARIO WHERE codigoInterno =
// ?");
//				sql.setInt(1, regularizacao.getQuem());
//				ResultSet resultadoQuery = sql.executeQuery();
//				if (resultadoQuery.next()) {
//					numMecanograficoSessao = resultadoQuery.getInt("numeroMecanografico");
//				} else {
//					sql.close();
//					SuportePersistenteOracle.getInstance().cancelarTransaccao();
//					return ultimo;
//				}
//				sql.close();
//
//				// obtem a sigla de regularizacao de marcacao de ponto
//				sql = UtilRelacional.prepararComando("SELECT sigla FROM
// ass_PARAM_REGULARIZACAO " + "WHERE
// codigoInterno = ?");
//
//				sql.setInt(1, regularizacao.getChaveParamRegularizacao());
//
//				resultadoQuery = sql.executeQuery();
//				String sigla = null;
//				if (resultadoQuery.next()) {
//					sigla = resultadoQuery.getString("sigla");
//				} else {
//					sql.close();
//					return ultimo;
//				}
//				sql.close();
//				// último número de sequencia na tabela
//
//				sql = UtilRelacionalOracle.prepararComando("SELECT MAX(ASS_MARSEQ) FROM
// ASS_MARCAS");
//				resultadoQuery = sql.executeQuery();
//				if (resultadoQuery.next()) {
//					ultimo = resultadoQuery.getInt(1) + 1;
//				}
//				sql.close();
//
//				// insere a marcacao de ponto na tabela ASS_MARCAS
//				sql = UtilRelacionalOracle.prepararComando("INSERT INTO ASS_MARCAS " +
// "VALUES (?, ?, ?, ?, ?, ?, ?,
// ?, ?, ?, ?, ?)");
//
//				sql.setInt(1, marcacaoPonto.getNumFuncionario());
//				sql.setInt(2, -1);
//				if (marcacaoPonto.getData() != null) {
//					sql.setTimestamp(3, new Timestamp((marcacaoPonto.getData()).getTime()));
//				} else {
//					sql.setTimestamp(3, null);
//				}
//				sql.setString(4, marcacaoPonto.getSiglaUnidade());
//				sql.setString(5, "N");
//				sql.setString(6, "N");
//				sql.setString(7, "S");
//				sql.setString(8, "N");
//				sql.setString(9, "V");
//				sql.setInt(10, ultimo);
//
//				sql.setTimestamp(11, regularizacao.getQuando());
//				sql.setInt(12, numMecanograficoSessao);
//
//				sql.executeUpdate();
//				sql.close();
//
//				// insere a regularizacao de marcacao de ponto na tabela ASS_MARREG
//				sql = UtilRelacionalOracle.prepararComando("INSERT INTO ASS_MARREG " +
// "VALUES (?, ?, ?, ?)");
//
//				sql.setInt(1, marcacaoPonto.getNumFuncionario());
//				sql.setInt(2, ultimo);
//				sql.setString(3, sigla);
//				sql.setInt(4, 1);
//
//				sql.executeUpdate();
//				sql.close();
//			} catch (Exception e) {
//				SuportePersistenteOracle.getInstance().cancelarTransaccao();
//				System.out.println("MarcacaoPontoRelacional.escreverMarcacaoPontoRegularizacao:
// " + e.toString());
//				return 0;
//			}
//			SuportePersistenteOracle.getInstance().confirmarTransaccao();
//		} catch (Exception e) {
//			System.out.println("MarcacaoPontoRelacional.escreverMarcacaoPontoRegularizacao:
// " + e.toString());
//		} finally {
//			return ultimo;
//		}
//	} /* escreverMarcacaoPontoRegularizacao */
//}
