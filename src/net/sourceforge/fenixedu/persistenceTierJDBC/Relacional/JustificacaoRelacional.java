package net.sourceforge.fenixedu.persistenceTierJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.domain.Justificacao;
import net.sourceforge.fenixedu.persistenceTierJDBC.IJustificacaoPersistente;
import net.sourceforge.fenixedu.constants.assiduousness.Constants;

/**
 * @author Fernanda Quitério e Tania Pousão
 */
public class JustificacaoRelacional implements IJustificacaoPersistente {
    public boolean alterarJustificacao(Justificacao justificacao) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("UPDATE ass_JUSTIFICACAO SET "
                    + "codigoInterno = ? , " + "chaveParamJustificacao = ? , "
                    + "chaveFuncionario = ? , " + "diaInicio = ? , " + "horaInicio = ? , "
                    + "diaFim = ? , " + "horaFim = ? , " + "observacao = ? , " + "quem = ? , "
                    + "quando = ? " + "WHERE codigoInterno = ? ");

            sql.setInt(1, justificacao.getCodigoInterno());
            sql.setInt(2, justificacao.getChaveParamJustificacao());
            sql.setInt(3, justificacao.getChaveFuncionario());
            if (justificacao.getDiaInicio() != null) {
                sql.setDate(4, new java.sql.Date((justificacao.getDiaInicio()).getTime()));
            } else {
                sql.setDate(4, null);
            }
            sql.setTime(5, justificacao.getHoraInicio());
            if (justificacao.getDiaFim() != null) {
                sql.setDate(6, new java.sql.Date((justificacao.getDiaFim()).getTime()));
            } else {
                sql.setDate(6, null);
            }
            sql.setTime(7, justificacao.getHoraFim());
            sql.setString(8, justificacao.getObservacao());
            sql.setInt(9, justificacao.getQuem());
            if (justificacao.getQuando() != null) {
                sql.setTimestamp(10, new Timestamp((justificacao.getQuando()).getTime()));
            } else {
                sql.setTimestamp(10, null);
            }
            sql.setInt(11, justificacao.getCodigoInterno());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("JustificacaoRelacional.alterarJustificacao: " + e.toString());
        }
        return resultado;
    } /* alterarJustificacao */

    public boolean apagarJustificacao(int codigoInterno) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("DELETE FROM ass_JUSTIFICACAO "
                    + "WHERE codigoInterno=?");

            sql.setInt(1, codigoInterno);

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("JustificacaoRelacional.apagarJustificacao: " + e.toString());
        }
        return resultado;
    } /* apagarJustificacao */

    public boolean apagarJustificacao(int chaveFuncionario, java.util.Date diaInicio, Time horaInicio,
            java.util.Date diaFim, Time horaFim) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("DELETE FROM ass_JUSTIFICACAO "
                            + "WHERE chaveFuncionario=? AND diaInicio=? AND horaInicio=? AND diaFim=? AND horaFim=?");

            sql.setInt(1, chaveFuncionario);
            sql.setDate(2, new java.sql.Date(diaInicio.getTime()));
            sql.setTime(3, horaInicio);
            sql.setDate(4, new java.sql.Date(diaFim.getTime()));
            sql.setTime(5, horaFim);

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("JustificacaoRelacional.apagarJustificacao: " + e.toString());
        }
        return resultado;
    } /* apagarJustificacao */

    public boolean apagarTodasJustificacoes() {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("DELETE FROM ass_JUSTIFICACAO");
            sql.executeUpdate();
            sql.close();
            /*
             * sql = UtilRelacional.prepararComando("ALTER TABLE
             * ass_JUSTIFICACAO auto_increment=1"); sql.executeUpdate();
             */

            resultado = true;
        } catch (Exception e) {
            System.out.println("JustificacaoRelacional.apagarTodasJustificacoes: " + e.toString());
        }
        return resultado;
    } /* apagarTodasJustificacoes */

    public List consultarJustificacoes(List listaChaveFuncionarios, List listaChaveParamJustificacoes,
            Date dataInicio, Date dataFim) {
        List justificacoes = null;

        try {
            PreparedStatement sql = null;
            ResultSet resultado = null;

            // obtem as justficacoes no intervalo de datas
            String query = new String(
                    "SELECT * FROM ass_JUSTIFICACAO WHERE "
                            + "((diaInicio BETWEEN ? AND ?) OR (diaFim BETWEEN ? AND ?) OR (diaInicio <= ? AND diaFim >= ?))");

            if (listaChaveFuncionarios != null) {
                if (listaChaveFuncionarios.size() > 0) {
                    query = query.concat(" AND (");

                    ListIterator iterListaFuncionarios = listaChaveFuncionarios.listIterator();
                    while (iterListaFuncionarios.hasNext()) {
                        iterListaFuncionarios.next();
                        if (iterListaFuncionarios.hasNext()) {
                            query = query.concat("chaveFuncionario = ? OR ");
                        } else {
                            query = query.concat("chaveFuncionario = ?)");
                        }
                    }
                }
            }

            if (listaChaveParamJustificacoes != null) {
                if (listaChaveParamJustificacoes.size() > 0) {
                    query = query.concat(" AND (");

                    ListIterator iterListaParamJustificacoes = listaChaveParamJustificacoes
                            .listIterator();
                    while (iterListaParamJustificacoes.hasNext()) {
                        iterListaParamJustificacoes.next();
                        if (iterListaParamJustificacoes.hasNext()) {
                            query = query.concat("chaveParamJustificacao = ? OR ");
                        } else {
                            query = query.concat("chaveParamJustificacao = ?)");
                        }
                    }
                }
            }

            sql = UtilRelacional.prepararComando(query);
            sql.setDate(1, new java.sql.Date(dataInicio.getTime()));
            sql.setDate(2, new java.sql.Date(dataFim.getTime()));
            sql.setDate(3, new java.sql.Date(dataInicio.getTime()));
            sql.setDate(4, new java.sql.Date(dataFim.getTime()));
            sql.setDate(5, new java.sql.Date(dataInicio.getTime()));
            sql.setDate(6, new java.sql.Date(dataFim.getTime()));

            // indice do comando sql
            int indice = 7;

            if (listaChaveFuncionarios != null) {
                ListIterator iterListaFuncionarios = listaChaveFuncionarios.listIterator();
                Integer numFuncionario = null;

                while (iterListaFuncionarios.hasNext()) {
                    numFuncionario = (Integer) iterListaFuncionarios.next();
                    sql.setInt(indice, numFuncionario.intValue());
                    indice++;
                }
            }

            if (listaChaveParamJustificacoes != null) {
                ListIterator iterListaParamJustificacoes = listaChaveParamJustificacoes.listIterator();
                Integer paramJustificacao = null;

                while (iterListaParamJustificacoes.hasNext()) {
                    paramJustificacao = (Integer) iterListaParamJustificacoes.next();
                    sql.setInt(indice, paramJustificacao.intValue());
                    indice++;
                }
            }

            query = query.concat(" ORDER BY chaveFuncionario, chaveParamJustificacao");

            resultado = sql.executeQuery();
            justificacoes = new ArrayList();

            PreparedStatement sql2 = UtilRelacional
                    .prepararComando("SELECT tipo FROM ass_PARAM_JUSTIFICACAO WHERE codigoInterno= ?");
            ResultSet resultado2 = null;

            while (resultado.next()) {
                sql2.setInt(1, resultado.getInt("chaveParamJustificacao"));
                resultado2 = sql2.executeQuery();
                if (resultado2.next()) {
                    Time horaInicio = null;
                    if (resultado2.getString("tipo").equals(Constants.JUSTIFICACAO_SALDO)) {
                        horaInicio = new Time(resultado.getTime("horaInicio").getTime() + 3600 * 1000);
                        // duracao, logo adiciona-se uma hora
                    } else {
                        horaInicio = resultado.getTime("horaInicio");
                    }
                    justificacoes.add(new Justificacao(resultado.getInt("codigoInterno"), resultado
                            .getInt("chaveParamJustificacao"), resultado.getInt("chaveFuncionario"),
                            java.sql.Date.valueOf(resultado.getString("diaInicio")), horaInicio,
                            java.sql.Date.valueOf(resultado.getString("diaFim")), resultado
                                    .getTime("horaFim"), resultado.getString("observacao"), resultado
                                    .getInt("quem"), Timestamp.valueOf(resultado.getString("quando"))));
                }
            }
            sql2.close();
            sql.close();
        } catch (Exception e) {
            System.out.println("JustificacaoRelacional.consultarJustificacoes: " + e.toString());
            e.printStackTrace();
            return null;
        }
        return justificacoes;
    } /* consultarJustificacoes */

    public boolean escreverJustificacao(Justificacao justificacao) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("INSERT INTO ass_JUSTIFICACAO "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            sql.setInt(1, justificacao.getCodigoInterno());
            sql.setInt(2, justificacao.getChaveParamJustificacao());
            sql.setInt(3, justificacao.getChaveFuncionario());
            if (justificacao.getDiaInicio() != null) {
                sql.setDate(4, new java.sql.Date((justificacao.getDiaInicio()).getTime()));
            } else {
                sql.setDate(4, null);
            }
            sql.setTime(5, justificacao.getHoraInicio());
            if (justificacao.getDiaFim() != null) {
                sql.setDate(6, new java.sql.Date((justificacao.getDiaFim()).getTime()));
            } else {
                sql.setDate(6, null);
            }
            sql.setTime(7, justificacao.getHoraFim());
            sql.setString(8, justificacao.getObservacao());
            sql.setInt(9, justificacao.getQuem());
            if (justificacao.getQuando() != null) {
                sql.setTimestamp(10, new Timestamp((justificacao.getQuando()).getTime()));
            } else {
                sql.setTimestamp(10, null);
            }

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("JustificacaoRelacional.escreverJustificacao: " + e.toString());
        }
        return resultado;
    } /* escreverJustificacao */

    public boolean escreverJustificacoes(List listaJustificacoes) {
        boolean resultado = false;
        ListIterator iterador = listaJustificacoes.listIterator();
        Justificacao justificacao = null;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("INSERT INTO ass_JUSTIFICACAO "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            while (iterador.hasNext()) {
                justificacao = (Justificacao) iterador.next();

                sql.setInt(1, justificacao.getCodigoInterno());
                sql.setInt(2, justificacao.getChaveParamJustificacao());
                sql.setInt(3, justificacao.getChaveFuncionario());
                if (justificacao.getDiaInicio() != null) {
                    sql.setDate(4, new java.sql.Date((justificacao.getDiaInicio()).getTime()));
                } else {
                    sql.setDate(4, null);
                }
                sql.setTime(5, justificacao.getHoraInicio());
                if (justificacao.getDiaFim() != null) {
                    sql.setDate(6, new java.sql.Date((justificacao.getDiaFim()).getTime()));
                } else {
                    sql.setDate(6, null);
                }
                sql.setTime(7, justificacao.getHoraFim());
                sql.setString(8, justificacao.getObservacao());
                sql.setInt(9, justificacao.getQuem());
                if (justificacao.getQuando() != null) {
                    sql.setTimestamp(10, new Timestamp((justificacao.getQuando()).getTime()));
                } else {
                    sql.setTimestamp(10, null);
                }

                sql.executeUpdate();
            }
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("JustificacaoRelacional.escreverJustificacoes: " + e.toString());
        }
        return resultado;
    } /* escreverJustificacoes */

    public boolean existeJustificacao(Justificacao justificacao) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_JUSTIFICACAO "
                    + "WHERE chaveParamJustificacao = ? " + "AND chaveFuncionario = ? "
                    + "AND diaInicio = ? "
                    + "AND (horaInicio is null OR (horaInicio is not null AND horaInicio = ?)) "
                    + "AND diaFim = ? "
                    + "AND (horaFim is null OR (horaFim is not null AND horaFim = ?))");

            sql.setInt(1, justificacao.getChaveParamJustificacao());
            sql.setInt(2, justificacao.getChaveFuncionario());
            if (justificacao.getDiaInicio() != null) {
                sql.setDate(3, new java.sql.Date((justificacao.getDiaInicio()).getTime()));
            } else {
                sql.setDate(3, null);
            }
            sql.setTime(4, justificacao.getHoraInicio());
            if (justificacao.getDiaFim() != null) {
                sql.setDate(5, new java.sql.Date((justificacao.getDiaFim()).getTime()));
            } else {
                sql.setDate(5, null);
            }
            sql.setTime(6, justificacao.getHoraFim());

            ResultSet resultadoQuery = sql.executeQuery();
            if (resultadoQuery.next()) {
                resultado = true;
            }
            sql.close();
        } catch (Exception e) {
            System.out.println("JustificacaoRelacional.existeJustificacao: " + e.toString());
            e.printStackTrace();
        }
        return resultado;
    } /* existeJustificacao */

    public Justificacao lerJustificacao(int codigoInterno) {
        Justificacao justificacao = null;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_JUSTIFICACAO "
                    + "WHERE codigoInterno=?");

            sql.setInt(1, codigoInterno);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next()) {
                PreparedStatement sql2 = UtilRelacional
                        .prepararComando("SELECT tipo FROM ass_PARAM_JUSTIFICACAO WHERE codigoInterno=?");
                sql2.setInt(1, resultado.getInt("chaveParamJustificacao"));
                ResultSet resultado2 = sql2.executeQuery();
                if (resultado2.next()) {
                    Time inicio = null;
                    if (resultado2.getString("tipo").equals(Constants.JUSTIFICACAO_SALDO)) {
                        inicio = new Time(resultado.getTime("horaInicio").getTime() + 3600 * 1000);
                        // duracao, logo adiciona-se uma hora
                    } else {
                        inicio = resultado.getTime("horaInicio");
                    }
                    justificacao = new Justificacao(resultado.getInt("codigoInterno"), resultado
                            .getInt("chaveParamJustificacao"), resultado.getInt("chaveFuncionario"),
                            java.sql.Date.valueOf(resultado.getString("diaInicio")), inicio,
                            java.sql.Date.valueOf(resultado.getString("diaFim")), resultado
                                    .getTime("horaFim"), resultado.getString("observacao"), resultado
                                    .getInt("quem"), Timestamp.valueOf(resultado.getString("quando")));
                }
                sql2.close();
            }
            sql.close();
        } catch (Exception e) {
            System.out.println("JustificacaoRelacional.lerJustificacao: " + e.toString());
        }
        return justificacao;
    }

    public Justificacao lerJustificacao(int chaveFuncionario, java.util.Date diaInicio, Time horaInicio,
            java.util.Date diaFim, Time horaFim) {
        Justificacao justificacao = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_JUSTIFICACAO "
                            + "WHERE chaveFuncionario=? AND diaInicio=? AND horaInicio=? AND diaFim=? AND horaFim=?");

            sql.setInt(1, chaveFuncionario);
            sql.setDate(2, new java.sql.Date(diaInicio.getTime()));
            sql.setTime(3, horaInicio);
            sql.setDate(4, new java.sql.Date(diaFim.getTime()));
            sql.setTime(5, horaFim);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next()) {
                PreparedStatement sql2 = UtilRelacional
                        .prepararComando("SELECT tipo FROM ass_PARAM_JUSTIFICACAO WHERE codigoInterno=?");
                sql2.setInt(1, resultado.getInt("chaveParamJustificacao"));
                ResultSet resultado2 = sql2.executeQuery();
                if (resultado2.next()) {
                    Time inicio = null;
                    if (resultado2.getString("tipo").equals(Constants.JUSTIFICACAO_SALDO)) {
                        inicio = new Time(resultado.getTime("horaInicio").getTime() + 3600 * 1000);
                        // duracao, logo adiciona-se uma hora
                    } else {
                        inicio = resultado.getTime("horaInicio");
                    }
                    justificacao = new Justificacao(resultado.getInt("codigoInterno"), resultado
                            .getInt("chaveParamJustificacao"), resultado.getInt("chaveFuncionario"),
                            java.sql.Date.valueOf(resultado.getString("diaInicio")), inicio,
                            java.sql.Date.valueOf(resultado.getString("diaFim")), resultado
                                    .getTime("horaFim"), resultado.getString("observacao"), resultado
                                    .getInt("quem"), Timestamp.valueOf(resultado.getString("quando")));
                }
                sql2.close();
            }
            sql.close();
        } catch (Exception e) {
            System.out.println("JustificacaoRelacional.lerJustificacao: " + e.toString());
        }
        return justificacao;
    } /* lerJustificacao */

    public List lerJustificacoes(int chaveFuncionario, java.util.Date data) {
        List listaJustificacoes = null;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_JUSTIFICACAO "
                    + "WHERE (? BETWEEN diaInicio AND diaFim) AND chaveFuncionario = ?");
            sql.setDate(1, new java.sql.Date(data.getTime()));
            sql.setInt(2, chaveFuncionario);

            ResultSet resultado = sql.executeQuery();
            listaJustificacoes = new ArrayList();

            PreparedStatement sql2 = UtilRelacional
                    .prepararComando("SELECT tipo FROM ass_PARAM_JUSTIFICACAO WHERE codigoInterno= ?");
            ResultSet resultado2 = null;

            while (resultado.next()) {
                sql2.setInt(1, resultado.getInt("chaveParamJustificacao"));
                resultado2 = sql2.executeQuery();
                if (resultado2.next()) {
                    Time horaInicio = null;
                    if (resultado2.getString("tipo").equals(Constants.JUSTIFICACAO_SALDO)) {
                        horaInicio = new Time(resultado.getTime("horaInicio").getTime() + 3600 * 1000);
                        // duracao, logo adiciona-se uma hora
                    } else {
                        horaInicio = resultado.getTime("horaInicio");
                    }
                    listaJustificacoes.add(new Justificacao(resultado.getInt("codigoInterno"), resultado
                            .getInt("chaveParamJustificacao"), resultado.getInt("chaveFuncionario"),
                            java.sql.Date.valueOf(resultado.getString("diaInicio")), horaInicio,
                            java.sql.Date.valueOf(resultado.getString("diaFim")), resultado
                                    .getTime("horaFim"), resultado.getString("observacao"), resultado
                                    .getInt("quem"), Timestamp.valueOf(resultado.getString("quando"))));
                }
            }
            sql2.close();
            sql.close();
        } catch (Exception e) {
            System.out.println("JustificacaoRelacional.lerJustificacoes: " + e.toString());
            return null;
        }
        return listaJustificacoes;
    } /* lerJustificacoes */

    public List lerJustificacoesHoras(int chaveFuncionario, Timestamp data) {
        List listaJustificacoes = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_JUSTIFICACAO "
                            + "WHERE (? BETWEEN diaInicio AND diaFim) AND (? BETWEEN horaInicio AND horaFim) AND chaveFuncionario = ?");

            sql.setString(1, data.toString().substring(0, data.toString().indexOf(" ")));
            sql.setString(2, data.toString().substring(data.toString().indexOf(" ") + 1,
                    data.toString().indexOf(".")));
            sql.setInt(3, chaveFuncionario);

            ResultSet resultado = sql.executeQuery();
            PreparedStatement sql2 = UtilRelacional
                    .prepararComando("SELECT * FROM ass_PARAM_JUSTIFICACAO "
                            + "WHERE codigoInterno=? AND tipo = ?");
            ResultSet resultado2 = null;
            listaJustificacoes = new ArrayList();
            while (resultado.next()) {
                sql2.setInt(1, resultado.getInt("chaveParamJustificacao"));
                sql2.setString(2, Constants.JUSTIFICACAO_HORAS);
                resultado2 = sql2.executeQuery();
                if (resultado2.next()) {
                    listaJustificacoes.add(new Justificacao(resultado.getInt("codigoInterno"), resultado
                            .getInt("chaveParamJustificacao"), resultado.getInt("chaveFuncionario"),
                            java.sql.Date.valueOf(resultado.getString("diaInicio")), resultado
                                    .getTime("horaInicio"), java.sql.Date.valueOf(resultado
                                    .getString("diaFim")), resultado.getTime("horaFim"), resultado
                                    .getString("observacao"), resultado.getInt("quem"), Timestamp
                                    .valueOf(resultado.getString("quando"))));
                }
            }
            sql2.close();
            sql.close();
        } catch (Exception e) {
            System.out.println("JustificacaoRelacional.lerJustificacoesHoras: " + e.toString());
            return null;
        }
        return listaJustificacoes;
    } /* lerJustificacoesHoras */

    public List lerJustificacoesHoras(int chaveFuncionario, Timestamp data, Time horaInicio, Time horaFim) {
        List listaJustificacoes = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_JUSTIFICACAO "
                            + "WHERE (? BETWEEN diaInicio AND diaFim) "
                            + "AND ((horaInicio > ? AND horaInicio < ?) OR (horaFim > ? AND horaFim < ?) OR (horaInicio < ? AND horaFim > ?)) "
                            + "AND chaveFuncionario = ?");

            sql.setString(1, data.toString().substring(0, data.toString().indexOf(" ")));
            sql.setTime(2, horaInicio);
            sql.setTime(3, horaFim);
            sql.setTime(4, horaInicio);
            sql.setTime(5, horaFim);
            sql.setTime(6, horaInicio);
            sql.setTime(7, horaFim);
            sql.setInt(8, chaveFuncionario);

            ResultSet resultado = sql.executeQuery();
            listaJustificacoes = new ArrayList();

            PreparedStatement sql2 = UtilRelacional
                    .prepararComando("SELECT * FROM ass_PARAM_JUSTIFICACAO "
                            + "WHERE codigoInterno=? AND tipo = ?");
            ResultSet resultado2 = null;

            while (resultado.next()) {
                sql2.setInt(1, resultado.getInt("chaveParamJustificacao"));
                sql2.setString(2, Constants.JUSTIFICACAO_HORAS);
                resultado2 = sql2.executeQuery();
                if (resultado2.next()) {
                    listaJustificacoes.add(new Justificacao(resultado.getInt("codigoInterno"), resultado
                            .getInt("chaveParamJustificacao"), resultado.getInt("chaveFuncionario"),
                            java.sql.Date.valueOf(resultado.getString("diaInicio")), resultado
                                    .getTime("horaInicio"), java.sql.Date.valueOf(resultado
                                    .getString("diaFim")), resultado.getTime("horaFim"), resultado
                                    .getString("observacao"), resultado.getInt("quem"), Timestamp
                                    .valueOf(resultado.getString("quando"))));
                }
            }
            sql2.close();
            sql.close();
        } catch (Exception e) {
            System.out.println("JustificacaoRelacional.lerJustificacoesHoras: " + e.toString());
            return null;
        }
        return listaJustificacoes;
    } /* lerJustificacoesHoras */

    public List lerJustificacoesOcorrencia(int chaveFuncionario, Timestamp data) {
        List listaJustificacoes = null;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_JUSTIFICACAO "
                    + "WHERE (? BETWEEN diaInicio AND diaFim) AND chaveFuncionario = ?");

            sql.setString(1, data.toString().substring(0, data.toString().indexOf(" ")));
            sql.setInt(2, chaveFuncionario);

            ResultSet resultado = sql.executeQuery();
            PreparedStatement sql2 = UtilRelacional
                    .prepararComando("SELECT * FROM ass_PARAM_JUSTIFICACAO "
                            + "WHERE codigoInterno=? AND tipo = ?");
            ResultSet resultado2 = null;
            listaJustificacoes = new ArrayList();
            while (resultado.next()) {
                sql2.setInt(1, resultado.getInt("chaveParamJustificacao"));
                sql2.setString(2, Constants.JUSTIFICACAO_OCORRENCIA);
                resultado2 = sql2.executeQuery();
                if (resultado2.next()) {
                    listaJustificacoes.add(new Justificacao(resultado.getInt("codigoInterno"), resultado
                            .getInt("chaveParamJustificacao"), resultado.getInt("chaveFuncionario"),
                            java.sql.Date.valueOf(resultado.getString("diaInicio")), resultado
                                    .getTime("horaInicio"), java.sql.Date.valueOf(resultado
                                    .getString("diaFim")), resultado.getTime("horaFim"), resultado
                                    .getString("observacao"), resultado.getInt("quem"), Timestamp
                                    .valueOf(resultado.getString("quando"))));
                }
            }
            sql2.close();
            sql.close();
        } catch (Exception e) {
            System.out.println("JustificacaoRelacional.lerJustificacoesHoras: " + e.toString());
            return null;
        }
        return listaJustificacoes;
    } /* lerJustificacoesOcorrencia */

    public List lerJustificacoesFuncionarioComValidade(int chaveFuncionario, java.util.Date diaInicio,
            java.util.Date diaFim) {
        List listaJustificacoes = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_JUSTIFICACAO "
                            + "WHERE ((diaInicio BETWEEN ? AND ?) OR (diaFim BETWEEN ? AND ?) OR (diaInicio <= ? AND diaFim >= ?)) "
                            + "AND chaveFuncionario = ?");

            sql.setDate(1, new java.sql.Date(diaInicio.getTime()));
            sql.setDate(2, new java.sql.Date(diaFim.getTime()));
            sql.setDate(3, new java.sql.Date(diaInicio.getTime()));
            sql.setDate(4, new java.sql.Date(diaFim.getTime()));
            sql.setDate(5, new java.sql.Date(diaInicio.getTime()));
            sql.setDate(6, new java.sql.Date(diaFim.getTime()));
            sql.setInt(7, chaveFuncionario);

            ResultSet resultado = sql.executeQuery();
            listaJustificacoes = new ArrayList();

            PreparedStatement sql2 = UtilRelacional
                    .prepararComando("SELECT tipo FROM ass_PARAM_JUSTIFICACAO WHERE codigoInterno= ?");
            ResultSet resultado2 = null;

            while (resultado.next()) {
                sql2.setInt(1, resultado.getInt("chaveParamJustificacao"));
                resultado2 = sql2.executeQuery();
                if (resultado2.next()) {
                    Time horaInicio = null;
                    if (resultado2.getString("tipo").equals(Constants.JUSTIFICACAO_SALDO)) {
                        horaInicio = new Time(resultado.getTime("horaInicio").getTime() + 3600 * 1000);
                        // duracao, logo adiciona-se uma hora
                    } else {
                        horaInicio = resultado.getTime("horaInicio");
                    }
                    listaJustificacoes.add(new Justificacao(resultado.getInt("codigoInterno"), resultado
                            .getInt("chaveParamJustificacao"), resultado.getInt("chaveFuncionario"),
                            java.sql.Date.valueOf(resultado.getString("diaInicio")), horaInicio,
                            java.sql.Date.valueOf(resultado.getString("diaFim")), resultado
                                    .getTime("horaFim"), resultado.getString("observacao"), resultado
                                    .getInt("quem"), Timestamp.valueOf(resultado.getString("quando"))));
                }
            }
            sql2.close();
            sql.close();
        } catch (Exception e) {
            System.out.println("JustificacaoRelacional.lerJustificacoesFuncionarioComValidade: "
                    + e.toString());
            return null;
        }
        return listaJustificacoes;
    } /* lerJustificacoesFuncionarioComValidade */

    public List lerTodasJustificacoes() {
        List listaJustificacoes = null;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_JUSTIFICACAO");

            ResultSet resultado = sql.executeQuery();
            listaJustificacoes = new ArrayList();

            PreparedStatement sql2 = UtilRelacional
                    .prepararComando("SELECT tipo FROM ass_PARAM_JUSTIFICACAO WHERE codigoInterno= ?");
            ResultSet resultado2 = null;

            while (resultado.next()) {
                sql2.setInt(1, resultado.getInt("chaveParamJustificacao"));
                resultado2 = sql2.executeQuery();
                if (resultado2.next()) {
                    Time horaInicio = null;
                    if (resultado2.getString("tipo").equals(Constants.JUSTIFICACAO_SALDO)) {
                        horaInicio = new Time(resultado.getTime("horaInicio").getTime() + 3600 * 1000);
                        // duracao, logo adiciona-se uma hora
                    } else {
                        horaInicio = resultado.getTime("horaInicio");
                    }
                    listaJustificacoes.add(new Justificacao(resultado.getInt("codigoInterno"), resultado
                            .getInt("chaveParamJustificacao"), resultado.getInt("chaveFuncionario"),
                            java.sql.Date.valueOf(resultado.getString("diaInicio")), horaInicio,
                            java.sql.Date.valueOf(resultado.getString("diaFim")), resultado
                                    .getTime("horaFim"), resultado.getString("observacao"), resultado
                                    .getInt("quem"), Timestamp.valueOf(resultado.getString("quando"))));
                }
            }
            sql2.close();
            sql.close();
        } catch (Exception e) {
            System.out.println("JustificacaoRelacional.lerTodasJustificacoes: " + e.toString());
            return null;
        }
        return listaJustificacoes;
    } /* lerTodasJustificacoes */
}