package ServidorPersistenteJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class MarcacaoPontoRelacionalOracle implements IMarcacaoPontoPersistente {
    public boolean alterarMarcacaoPonto(MarcacaoPonto marcacaoPonto) {
        boolean resultado = false;

        try {
            SuportePersistenteOracle.getInstance().iniciarTransaccao();
            try {
                PreparedStatement sql = UtilRelacionalOracle.prepararComando("UPDATE ASS_MARCAS SET "
                        + "ASS_MARPESSOA = ? , " + "ASS_MARCARTAO = ? , "
                        //+ "ASS_MARDHMARCA = TO_DATE(?, 'YYYY-MM-DD
                        // HH24:MI:SS') , "
                        + "ASS_MARDHMARCA = ?, " + "ASS_MARUNID = ? , " + "ASS_MARTIPO = ? , "
                        + "ASS_MARAUTOJUST = ? , " + "ASS_MARREGUL = ? , " + "ASS_MARIES = ? , "
                        + "ASS_MARSTAT = ? , " + "ASS_MARSEQ = ? , "
                        //+ "ASS_MARWHEN = TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS')
                        // , "
                        + "ASS_MARWHEN = ?, " + "ASS_MARWHO = ? " + "WHERE ASS_MARSEQ = ? ");

                sql.setInt(1, marcacaoPonto.getNumFuncionario());
                if (marcacaoPonto.getEstado().equals("regularizada")) {
                    sql.setInt(2, -1);
                } else {
                    sql.setInt(2, marcacaoPonto.getNumCartao());
                }

                //sql.setString(3,
                // marcacaoPonto.getData().toString().substring(0,
                // marcacaoPonto.getData().toString().indexOf(".")));
                sql.setTimestamp(3, new Timestamp(marcacaoPonto.getData().getTime()));

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
                //sql.setString(11,
                // marcacaoPonto.getData().toString().substring(0,
                // marcacaoPonto.getData().toString().indexOf(".")));
                sql.setTimestamp(11, new Timestamp(marcacaoPonto.getData().getTime()));

                sql.setInt(12, 0);
                sql.setInt(13, marcacaoPonto.getCodigoInterno());

                sql.executeUpdate();
                sql.close();
                resultado = true;
            } catch (SQLException SQLe) {
                SuportePersistenteOracle.getInstance().cancelarTransaccao();
                System.out.println("MarcacaoPontoRelacionalOracle.alterarMarcacaoPonto: "
                        + SQLe.toString());
                return false;
            }
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        } catch (Exception e) {
            System.out.println("MarcacaoPontoRelacionalOracle.alterarMarcacaoPonto: " + e.toString());
        }
        return resultado;
    } /* alterarMarcacaoPonto */

    public boolean apagarMarcacaoPonto(int chaveMarcacao) {
        boolean resultado = false;
        return resultado;
    } /* apagarMarcacaoPonto */

    public boolean apagarMarcacaoPonto(Timestamp dataMarcacao) {
        boolean resultado = false;
        return resultado;
    } /* apagarMarcacaoPonto */

    public boolean apagarMarcacoesPonto() {
        boolean resultado = false;
        return resultado;
    } /* apagarMarcacoesPonto */

    public List consultarMarcacoesPonto(List listaFuncionarios, List listaCartoes, List listaEstados,
            Timestamp dataInicio, Timestamp dataFim) {
        List marcacoesPonto = null;

        try {
            SuportePersistenteOracle.getInstance().iniciarTransaccao();
            try {
                // procura de marcacoes de ponto normais
                String query = new String("SELECT * FROM ASS_MARCAS WHERE ((ASS_MARDHMARCA IS NULL) OR "
                //+ "(ASS_MARDHMARCA BETWEEN TO_DATE(?, 'YYYY-MM-DD
                        // HH24:MI:SS') AND TO_DATE(?,
                        // 'YYYY-MM-DD HH24:MI:SS')))");
                        + "(ASS_MARDHMARCA BETWEEN ? AND ?))");
                if (listaFuncionarios != null) {
                    if (listaFuncionarios.size() > 0) {
                        query = query.concat(" AND (");

                        ListIterator iterListaFuncionarios = listaFuncionarios.listIterator();
                        while (iterListaFuncionarios.hasNext()) {
                            iterListaFuncionarios.next();
                            if (iterListaFuncionarios.hasNext()) {
                                query = query.concat("ASS_MARPESSOA = ? OR ");
                            } else {
                                query = query.concat("ASS_MARPESSOA = ?)");
                            }
                        }
                    }
                }
                if (listaCartoes != null) {
                    if (listaCartoes.size() > 0) {
                        query = query.concat(" AND ((");

                        ListIterator iterListaCartoes = listaCartoes.listIterator();
                        while (iterListaCartoes.hasNext()) {
                            iterListaCartoes.next();
                            if (iterListaCartoes.hasNext()) {
                                query = query.concat("ASS_MARCARTAO = ? OR ");
                            } else {
                                query = query.concat("ASS_MARCARTAO = ?)");
                            }
                        }
                    }
                }

                if (listaCartoes != null && listaFuncionarios != null) {
                    if (listaCartoes.size() > 0 && listaFuncionarios.size() > 0) {
                        query = query.concat(" OR (ASS_MARCARTAO = -1 AND (");

                        ListIterator iterListaFuncionarios = listaFuncionarios.listIterator();
                        while (iterListaFuncionarios.hasNext()) {
                            iterListaFuncionarios.next();
                            if (iterListaFuncionarios.hasNext()) {
                                query = query.concat("ASS_MARPESSOA = ? OR ");
                            } else {
                                query = query.concat("ASS_MARPESSOA = ?)))");
                            }
                        }
                    } else {
                        if (listaCartoes.size() > 0) {
                            query = query.concat(")");
                        }
                    }
                } else {
                    if (listaCartoes != null) {
                        query = query.concat(")");
                    }
                }

                PreparedStatement sql = UtilRelacionalOracle.prepararComando(query);
                //sql.setString(1, dataInicio.toString().substring(0,
                // dataInicio.toString().indexOf(".")));
                //sql.setString(2, dataFim.toString().substring(0,
                // dataFim.toString().indexOf(".")));
                sql.setTimestamp(1, new Timestamp(dataInicio.getTime()));
                sql.setTimestamp(2, new Timestamp(dataFim.getTime()));

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
                //query = query.concat(" ORDER BY ASS_MARDHMARCA");

                ResultSet resultado = sql.executeQuery();
                marcacoesPonto = new ArrayList();
                Timestamp dataMarcacao = null;

                PreparedStatement sql2 = UtilRelacionalOracle
                        .prepararComando("SELECT * FROM ASS_MARREG WHERE ASS_MARREGMARCAS = ? AND "
                                + "ASS_MARREGREGUL = 'MA'");
                ResultSet resultado2 = null;
                while (resultado.next()) {
                    // retirar as marcacoes de ponto que foram marcadas para
                    // anular
                    sql2.setInt(1, resultado.getInt("ASS_MARSEQ"));
                    resultado2 = sql2.executeQuery();
                    if (!resultado2.next()) {
                        if (resultado.getString("ASS_MARDHMARCA") != null) {
                            dataMarcacao = Timestamp.valueOf(resultado.getString("ASS_MARDHMARCA"));
                        }

                        MarcacaoPonto marcacaoPonto = new MarcacaoPonto(resultado.getInt("ASS_MARSEQ"),
                                resultado.getString("ASS_MARUNID"), dataMarcacao, resultado
                                        .getInt("ASS_MARCARTAO"), resultado.getInt("ASS_MARPESSOA"));
                        // diferenciar as marcacoes regularizadas
                        if (resultado.getString("ASS_MARREGUL").equals("S")) {
                            marcacaoPonto.setEstado("regularizada");
                            marcacaoPonto.setSiglaUnidade("");
                            marcacaoPonto.setNumCartao(0);
                        }
                        marcacoesPonto.add(marcacaoPonto);
                    }
                }
                sql2.close();
                sql.close();

            } catch (SQLException SQLe) {
                SuportePersistenteOracle.getInstance().cancelarTransaccao();
                System.out.println("MarcacaoPontoRelacionalOracle.consultarMarcacoesPonto: "
                        + SQLe.toString());
                return null;
            }
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        } catch (Exception e) {
            System.out.println("MarcacaoPontoRelacionalOracle.consultarMarcacoesPonto: " + e.toString());
        }
        return marcacoesPonto;
    } /* consultarMarcacoesPonto */

    public List consultarMarcacoesPontoErros(String estado) { //não será
        // preciso
        List errosLeitura = null;
        return errosLeitura;
    } /* consultarMarcacoesPontoErros */

    public boolean escreverMarcacaoPonto(MarcacaoPonto marcacaoPonto) {
        boolean resultado = false;
        return resultado;
    } /* escreverMarcacaoPonto */

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

                //sql = UtilRelacionalOracle.prepararComando("SELECT
                // s_ordem.NEXTVAL FROM SYS.dual");
                sql = UtilRelacionalOracle.prepararComando("SELECT MAX(ASS_MARSEQ) FROM ASS_MARCAS");
                resultadoQuery = sql.executeQuery();
                if (resultadoQuery.next()) {
                    //ultimo = resultadoQuery.getInt(1);
                    ultimo = resultadoQuery.getInt(1) + 1;
                }
                sql.close();

                // insere a marcacao de ponto na tabela ASS_MARCAS
                sql = UtilRelacionalOracle.prepararComando("INSERT INTO ASS_MARCAS "
                //+ "VALUES (?, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), ?, ?,
                        // ?, ?, ?, ?, ?,
                        // TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), ?)");
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                sql.setInt(1, marcacaoPonto.getNumFuncionario());
                sql.setInt(2, -1);
                if (marcacaoPonto.getData() != null) {
                    sql.setTimestamp(3, new Timestamp((marcacaoPonto.getData()).getTime()));
                    //sql.setString(3,
                    // marcacaoPonto.getData().toString().substring(0,
                    // marcacaoPonto.getData().toString().indexOf(".")));
                } else {
                    sql.setTimestamp(3, null);
                    //sql.setString(3, null);
                }
                sql.setString(4, marcacaoPonto.getSiglaUnidade());
                sql.setString(5, "N");
                sql.setString(6, "N");
                sql.setString(7, "S");
                sql.setString(8, "N");
                sql.setString(9, "V");
                sql.setInt(10, ultimo);

                sql.setTimestamp(11, regularizacao.getQuando());
                //sql.setString(11,
                // regularizacao.getQuando().toString().substring(0,
                // regularizacao.getQuando().toString().indexOf(".")));

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

    public boolean escreverMarcacoesPonto(List listaMarcacoes) {
        boolean resultado = false;
        return resultado;
    } /* escreverMarcacoesPonto */

    public MarcacaoPonto lerMarcacaoPonto(int codigoInterno) {
        MarcacaoPonto marcacaoPonto = null;
        return marcacaoPonto;
    } /* lerMarcacaoPonto */

    public MarcacaoPonto lerMarcacaoPonto(Timestamp dataMarcacao, int numFuncionario) {
        MarcacaoPonto marcacaoPonto = null;

        try {
            SuportePersistenteOracle.getInstance().iniciarTransaccao();
            try {
                PreparedStatement sql = UtilRelacionalOracle.prepararComando("SELECT * FROM ASS_MARCAS "
                        + "WHERE ASS_MARDHMARCA = ? AND ASS_MARPESSOA = ?");
                //"WHERE ASS_MARDHMARCA = TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS')
                // AND ASS_MARPESSOA = ?");

                //sql.setString(1, dataMarcacao.toString().substring(0,
                // dataMarcacao.toString().indexOf(".")));
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
                    SuportePersistenteOracle.getInstance().cancelarTransaccao();
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
            } catch (SQLException SQLe) {
                SuportePersistenteOracle.getInstance().cancelarTransaccao();
                System.out.println("MarcacaoPontoRelacionalOracle.lerMarcacaoPonto: " + SQLe.toString());
                return null;
            }
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        } catch (Exception e) {
            System.out.println("MarcacaoPontoRelacionalOracle.lerMarcacaoPonto: " + e.toString());
        }
        return marcacaoPonto;
    } /* lerMarcacaoPonto */

    public List lerMarcacoesPonto() {
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
            } catch (SQLException SQLe) {
                SuportePersistenteOracle.getInstance().cancelarTransaccao();
                System.out
                        .println("MarcacaoPontoRelacionalOracle.lerMarcacoesPonto: " + SQLe.toString());
                return null;
            }
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        } catch (Exception e) {
            System.out.println("MarcacaoPontoRelacionalOracle.lerMarcacoesPonto: " + e.toString());
        }
        return marcacoesPonto;
    } /* lerMarcacoesPonto */

    public List lerMarcacoesPonto(int numCartao) {
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
            } catch (SQLException SQLe) {
                SuportePersistenteOracle.getInstance().cancelarTransaccao();
                System.out
                        .println("MarcacaoPontoRelacionalOracle.lerMarcacoesPonto: " + SQLe.toString());
                return null;
            }
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        } catch (Exception e) {
            System.out.println("MarcacaoPontoRelacionalOracle.lerMarcacoesPonto: " + e.toString());
        }
        return marcacoesPonto;
    } /* lerMarcacoesPonto */

    public List obterMarcacoesPonto(int numFuncionario, int numCartao, Timestamp dataFim) {
        List marcacoesPonto = null;

        try {
            SuportePersistenteOracle.getInstance().iniciarTransaccao();
            try {
                PreparedStatement sql = UtilRelacionalOracle.prepararComando("SELECT * FROM ASS_MARCAS "
                        + "WHERE ASS_MARDHMARCA>=? AND ASS_MARPESSOA=? AND ASS_MARCARTAO=?");
                //"ASS_MARDHMARCA>=TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS') AND
                // ASS_MARPESSOA=? AND
                // ASS_MARCARTAO=?");

                //sql.setString(1, dataFim.toString().substring(0,
                // dataFim.toString().indexOf(".")));
                sql.setTimestamp(1, dataFim);

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
            } catch (SQLException SQLe) {
                SuportePersistenteOracle.getInstance().cancelarTransaccao();
                System.out.println("MarcacaoPontoRelacionalOracle.obterMarcacoesPonto: "
                        + SQLe.toString());
                return null;
            }
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        } catch (Exception e) {
            System.out.println("MarcacaoPontoRelacionalOracle.obterMarcacoesPonto: " + e.toString());
        }
        return marcacoesPonto;
    } /* obterMarcacoesPonto */

    public List obterMarcacoesPonto(int numCartao, Timestamp dataInicio, Timestamp dataFim) {
        List marcacoesPonto = null;

        try {
            SuportePersistenteOracle.getInstance().iniciarTransaccao();
            try {
                PreparedStatement sql = UtilRelacionalOracle.prepararComando("SELECT * FROM ASS_MARCAS "
                        + "WHERE ASS_MARCARTAO=? AND " + "ASS_MARDHMARCA>=? AND ASS_MARDHMARCA<=?");
                //+ "ASS_MARDHMARCA>=TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS') AND
                // ASS_MARDHMARCA<=TO_DATE(?,
                // 'YYYY-MM-DD HH24:MI:SS')");

                sql.setInt(1, numCartao);

                sql.setTimestamp(2, dataInicio);
                sql.setTimestamp(3, dataFim);
                //sql.setString(2, dataInicio.toString().substring(0,
                // dataInicio.toString().indexOf(".")));
                //sql.setString(3, dataFim.toString().substring(0,
                // dataFim.toString().indexOf(".")));

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
            } catch (SQLException SQLe) {
                SuportePersistenteOracle.getInstance().cancelarTransaccao();
                System.out.println("MarcacaoPontoRelacionalOracle.obterMarcacoesPonto: "
                        + SQLe.toString());
                return null;
            }
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        } catch (Exception e) {
            System.out.println("MarcacaoPontoRelacionalOracle.obterMarcacoesPonto: " + e.toString());
        }
        return marcacoesPonto;
    } /* obterMarcacoesPonto */

    public int ultimoCodigoInterno() {
        int ultimo = 0;

        try {
            SuportePersistenteOracle.getInstance().iniciarTransaccao();
            try {
                PreparedStatement sql = UtilRelacional
                        .prepararComando("SELECT MAX(ASS_MARSEQ) FROM ASS_MARCAS");

                ResultSet resultado = sql.executeQuery();
                if (resultado.next()) {
                    ultimo = resultado.getInt(1);
                }
                sql.close();
            } catch (SQLException SQLe) {
                SuportePersistenteOracle.getInstance().cancelarTransaccao();
                System.out.println("MarcacaoPontoRelacionalOracle.ultimoCodigoInterno: "
                        + SQLe.toString());
                return 0;
            }
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        } catch (Exception e) {
            System.out.println("MarcacaoPontoRelacionalOracle.ultimoCodigoInterno: " + e.toString());
        }
        return ultimo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IMarcacaoPontoPersistente#alterarMarcacaoPontoRegularizacao(Dominio.RegularizacaoMarcacaoPonto)
     */
    public boolean alterarMarcacaoPontoRegularizacao(RegularizacaoMarcacaoPonto regularizacao) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IMarcacaoPontoPersistente#alterarMarcacaoPontoEscreverRegularizacao(Dominio.RegularizacaoMarcacaoPonto)
     */
    public boolean alterarMarcacaoPontoEscreverRegularizacao(RegularizacaoMarcacaoPonto regularizacao) {
        return false;
    } /* ultimoCodigoInterno */
}