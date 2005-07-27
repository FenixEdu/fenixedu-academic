package net.sourceforge.fenixedu.persistenceTierJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.Funcionario;
import net.sourceforge.fenixedu.domain.StatusAssiduidade;
import net.sourceforge.fenixedu.persistenceTierJDBC.IFuncionarioPersistente;

/**
 * @author Fernanda Quitério e Tania Pousão
 */
public class FuncionarioRelacional implements IFuncionarioPersistente {

    public Funcionario lerFuncionarioSemHistorico(int codigoInterno) {
        Funcionario funcionario = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_FUNCIONARIO WHERE codigoInterno = ?");

            sql.setInt(1, codigoInterno);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next()) {
                Date antiguidade = null;
                if (resultado.getString("antiguidade") != null) {
                    antiguidade = java.sql.Date.valueOf(resultado.getString("antiguidade"));
                }

                funcionario = new Funcionario(resultado.getInt("codigoInterno"), resultado
                        .getInt("chavePessoa"), resultado.getInt("numeroMecanografico"), resultado
                        .getInt("chaveHorarioActual"), antiguidade);
            }
            sql.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("FuncionarioRelacional.lerFuncionarioSemHistoricoPorNumMecanografico: "
                    + e.toString());
        }
        return funcionario;
    } /* lerFuncionarioSemHistoricoPorNumMecanografico */

    public Funcionario lerFuncionarioSemHistoricoPorNumMecanografico(int numMecanografico) {
        Funcionario funcionario = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");

            sql.setInt(1, numMecanografico);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next()) {
                Date antiguidade = null;
                if (resultado.getString("antiguidade") != null) {
                    antiguidade = java.sql.Date.valueOf(resultado.getString("antiguidade"));
                }

                funcionario = new Funcionario(resultado.getInt("codigoInterno"), resultado
                        .getInt("chavePessoa"), resultado.getInt("numeroMecanografico"), resultado
                        .getInt("chaveHorarioActual"), antiguidade);
            }
            sql.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("FuncionarioRelacional.lerFuncionarioSemHistoricoPorNumMecanografico: "
                    + e.toString());
        }
        return funcionario;
    } /* lerFuncionarioSemHistoricoPorNumMecanografico */

    public Funcionario lerFuncionarioSemHistoricoPorPessoa(int chavePessoa) {
        Funcionario funcionario = null;
        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_FUNCIONARIO WHERE chavePessoa = ?");
            sql.setInt(1, chavePessoa);
            ResultSet resultado = sql.executeQuery();
            if (resultado.next()) {
                Date antiguidade = null;
                if (resultado.getString("antiguidade") != null) {
                    antiguidade = java.sql.Date.valueOf(resultado.getString("antiguidade"));
                }

                //Funcionario
                funcionario = new Funcionario(resultado.getInt("codigoInterno"), resultado
                        .getInt("chavePessoa"), resultado.getInt("numeroMecanografico"), resultado
                        .getInt("chaveHorarioActual"), antiguidade);
            }
            sql.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("FuncionarioRelacional.lerFuncionarioPorPessoa: " + e.toString());
        }
        return funcionario;
    } /* lerFuncionarioPorPessoa */

    private Funcionario constroirFuncionario(ResultSet resultado, Funcionario funcionario) {
        try {
            while (resultado.next()) {
                resultado.getInt("chaveFuncResponsavel");
                if (!resultado.wasNull()) {
                    funcionario.setChaveFuncResponsavel(new Integer(resultado
                            .getInt("chaveFuncResponsavel")));
                }

                resultado.getInt("chaveCCLocalTrabalho");
                if (!resultado.wasNull()) {
                    funcionario.setChaveCCLocalTrabalho(new Integer(resultado
                            .getInt("chaveCCLocalTrabalho")));
                }

                resultado.getInt("chaveCCCorrespondencia");
                if (!resultado.wasNull()) {
                    funcionario.setChaveCCCorrespondencia(new Integer(resultado
                            .getInt("chaveCCCorrespondencia")));
                }

                resultado.getInt("chaveCCVencimento");
                if (!resultado.wasNull()) {
                    funcionario.setChaveCCVencimento(new Integer(resultado.getInt("chaveCCVencimento")));
                }

                resultado.getString("calendario");
                if (!resultado.wasNull()) {
                    funcionario.setCalendario(resultado.getString("calendario"));
                }

                resultado.getInt("chaveStatus");
                if (!resultado.wasNull()) {
                    funcionario.setChaveStatus(new Integer(resultado.getInt("chaveStatus")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return funcionario;
    } /* constroirFuncionario */

    public Funcionario lerFuncionarioPorNumMecanografico(int numero, Date dataConsulta) {
        Funcionario funcionario = null;
        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");
            sql.setInt(1, numero);
            ResultSet resultado = sql.executeQuery();
            if (resultado.next()) {
                Date antiguidade = null;
                if (resultado.getString("antiguidade") != null) {
                    antiguidade = java.sql.Date.valueOf(resultado.getString("antiguidade"));
                }

                //Funcionario
                funcionario = new Funcionario(resultado.getInt("codigoInterno"), resultado
                        .getInt("chavePessoa"), resultado.getInt("numeroMecanografico"), resultado
                        .getInt("chaveHorarioActual"), antiguidade);

                //Histórico de funcionário
                PreparedStatement sql2 = UtilRelacional
                        .prepararComando("SELECT * FROM ass_FUNCIONARIO_HISTORICO "
                                + "WHERE chaveFuncionario = ? AND "
                                + "((dataInicio <= ? AND dataFim IS NULL) OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?))");

                sql2.setInt(1, resultado.getInt("codigoInterno"));
                sql2.setDate(2, new java.sql.Date(dataConsulta.getTime()));
                sql2.setDate(3, new java.sql.Date(dataConsulta.getTime()));
                sql2.setDate(4, new java.sql.Date(dataConsulta.getTime()));

                ResultSet resultado2 = sql2.executeQuery();
                funcionario = constroirFuncionario(resultado2, funcionario);

                sql2.close();
                sql.close();
            } else {
                sql.close();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("FuncionarioRelacional.lerFuncionarioPorNumMecanografico: "
                    + e.toString());
        }
        return funcionario;
    } /* lerFuncionarioPorNumMecanografico */

    public Funcionario lerFuncionarioPorPessoa(int chavePessoa, Date dataConsulta) {
        Funcionario funcionario = null;
        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_FUNCIONARIO WHERE chavePessoa = ?");
            sql.setInt(1, chavePessoa);
            ResultSet resultado = sql.executeQuery();
            if (resultado.next()) {
                Date antiguidade = null;
                if (resultado.getString("antiguidade") != null) {
                    antiguidade = java.sql.Date.valueOf(resultado.getString("antiguidade"));
                }

                //Funcionario
                funcionario = new Funcionario(resultado.getInt("codigoInterno"), resultado
                        .getInt("chavePessoa"), resultado.getInt("numeroMecanografico"), resultado
                        .getInt("chaveHorarioActual"), antiguidade);

                //Histórico de funcionário
                PreparedStatement sql2 = UtilRelacional
                        .prepararComando("SELECT * FROM ass_FUNCIONARIO_HISTORICO "
                                + "WHERE chaveFuncionario = ? AND "
                                + "((dataInicio <= ? AND dataFim IS NULL) OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?)) "
                                + "ORDER BY dataInicio");
                sql2.setInt(1, resultado.getInt("codigoInterno"));
                sql2.setDate(2, new java.sql.Date(dataConsulta.getTime()));
                sql2.setDate(3, new java.sql.Date(dataConsulta.getTime()));
                sql2.setDate(4, new java.sql.Date(dataConsulta.getTime()));

                ResultSet resultado2 = sql2.executeQuery();
                funcionario = constroirFuncionario(resultado2, funcionario);

                sql2.close();
            }
            sql.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("FuncionarioRelacional.lerFuncionarioPorPessoa: " + e.toString());
        }
        return funcionario;
    } /* lerFuncionarioPorPessoa */

    public List lerStatusAssiduidade(int numMecanografico, Timestamp dataInicial, Timestamp dataFinal) {
        List listaStatusAssiduidade = null;
        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_FUNCIONARIO where numeroMecanografico = ?");
            sql.setInt(1, numMecanografico);
            ResultSet resultado = sql.executeQuery();
            if (resultado.next()) {

                PreparedStatement sql2 = UtilRelacional
                        .prepararComando("SELECT DISTINCT chaveStatus FROM ass_FUNCIONARIO_HISTORICO "
                                + "WHERE chaveFuncionario = ? AND ((dataInicio BETWEEN ? AND ?) "
                                + "OR (dataFim IS NOT NULL AND dataFim BETWEEN ? AND ?) "
                                + "OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?) "
                                + "OR (dataInicio <= ? AND dataFim IS NULL))");
                sql2.setInt(1, resultado.getInt("codigoInterno"));
                sql2.setDate(2, new java.sql.Date(dataInicial.getTime()));
                sql2.setDate(3, new java.sql.Date(dataFinal.getTime()));
                sql2.setDate(4, new java.sql.Date(dataInicial.getTime()));
                sql2.setDate(5, new java.sql.Date(dataFinal.getTime()));
                sql2.setDate(6, new java.sql.Date(dataInicial.getTime()));
                sql2.setDate(7, new java.sql.Date(dataFinal.getTime()));
                sql2.setDate(8, new java.sql.Date(dataInicial.getTime()));
                ResultSet resultado2 = sql2.executeQuery();
                PreparedStatement sql3 = UtilRelacional
                        .prepararComando("SELECT * FROM ass_STATUS where codigoInterno = ?");
                ResultSet resultado3 = null;
                listaStatusAssiduidade = new ArrayList();
                while (resultado2.next()) {
                    sql3.setInt(1, resultado2.getInt("chaveStatus"));
                    resultado3 = sql3.executeQuery();
                    if (resultado3.next()) {
                        listaStatusAssiduidade.add(new StatusAssiduidade(resultado3
                                .getInt("codigoInterno"), resultado3.getString("sigla"), resultado3
                                .getString("designacao"), resultado3.getString("estado"), resultado3
                                .getString("assiduidade")));
                    }
                }
                sql3.close();
                sql2.close();
            }
            sql.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("FuncionarioRelacional.lerStatusAssiduidade: " + e.toString());
            return null;
        }
        return listaStatusAssiduidade;
    } /* lerStatusAssiduidade */

    public List lerTodosFuncionarios(Date dataConsulta) {
        //	TODO: REVER
        List listaFuncionarios = null;
        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_FUNCIONARIO ORDER BY numeroMecanografico");
            ResultSet resultado = sql.executeQuery();
            PreparedStatement sql2 = UtilRelacional
                    .prepararComando("SELECT * FROM ass_FUNCIONARIO_HISTORICO "
                            + "WHERE chaveFuncionario = ? AND "
                            + "((dataInicio <= ? AND dataFim IS NULL) OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?))");
            sql2.setDate(2, new java.sql.Date(dataConsulta.getTime()));
            sql2.setDate(3, new java.sql.Date(dataConsulta.getTime()));
            sql2.setDate(4, new java.sql.Date(dataConsulta.getTime()));
            ResultSet resultado2 = null;
            listaFuncionarios = new ArrayList();
            Date dataInicio = null;
            Date dataFim = null;
            Timestamp dataQuando = null;
            while (resultado.next()) {

                sql2.setInt(1, resultado.getInt("codigoInterno"));
                resultado2 = sql2.executeQuery();
                if (resultado2.last()) {
                    Date antiguidade = null;
                    if (resultado.getString("antiguidade") != null) {
                        antiguidade = java.sql.Date.valueOf(resultado.getString("antiguidade"));
                    }
                    if (resultado2.getString("dataInicio") != null) {
                        dataInicio = java.sql.Date.valueOf(resultado2.getString("dataInicio"));
                    }
                    if (resultado2.getString("dataFim") != null) {
                        dataFim = java.sql.Date.valueOf(resultado2.getString("dataFim"));
                    }
                    if (resultado2.getString("quando") != null) {
                        dataQuando = Timestamp.valueOf(resultado2.getString("quando"));
                    }

                    listaFuncionarios.add(new Funcionario(resultado.getInt("codigoInterno"), resultado
                            .getInt("chavePessoa"), resultado.getInt("numeroMecanografico"), resultado
                            .getInt("chaveHorarioActual"), antiguidade, (Integer) resultado2
                            .getObject("chaveFuncResponsavel"), (Integer) resultado2
                            .getObject("chaveCCLocalTrabalho"), (Integer) resultado2
                            .getObject("chaveCCCorrespondencia"), (Integer) resultado2
                            .getObject("chaveCCVencimento"), resultado2.getString("calendario"),
                            (Integer) resultado2.getObject("chaveStatus"), dataInicio, dataFim,
                            resultado2.getInt("quem"), dataQuando));
                }
            }
            sql2.close();
            sql.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("FuncionarioRelacional.lerTodosFuncionarios: " + e.toString());
            return null;
        }
        return listaFuncionarios;
    } /* lerTodosFuncionarios */

    public List lerTodosFuncionariosAssiduidade(Date dataConsulta) {
        //	TODO: REVER
        List listaFuncionarios = null;
        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_FUNCIONARIO where chaveHorarioActual <> 0 ORDER BY numeroMecanografico");
            ResultSet resultado = sql.executeQuery();
            PreparedStatement sql2 = UtilRelacional
                    .prepararComando("SELECT * FROM ass_FUNCIONARIO_HISTORICO "
                            + "WHERE chaveFuncionario = ? AND "
                            + "((dataInicio <= ? AND dataFim IS NULL) OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?))");
            sql2.setDate(2, new java.sql.Date(dataConsulta.getTime()));
            sql2.setDate(3, new java.sql.Date(dataConsulta.getTime()));
            sql2.setDate(4, new java.sql.Date(dataConsulta.getTime()));
            ResultSet resultado2 = null;
            listaFuncionarios = new ArrayList();
            Date dataInicio = null;
            Date dataFim = null;
            Timestamp dataQuando = null;
            while (resultado.next()) {

                sql2.setInt(1, resultado.getInt("codigoInterno"));
                resultado2 = sql2.executeQuery();
                if (resultado2.last()) {
                    Date antiguidade = null;
                    if (resultado.getString("antiguidade") != null) {
                        antiguidade = java.sql.Date.valueOf(resultado.getString("antiguidade"));
                    }
                    if (resultado2.getString("dataInicio") != null) {
                        dataInicio = java.sql.Date.valueOf(resultado2.getString("dataInicio"));
                    }
                    if (resultado2.getString("dataFim") != null) {
                        dataFim = java.sql.Date.valueOf(resultado2.getString("dataFim"));
                    }
                    if (resultado2.getString("quando") != null) {
                        dataQuando = Timestamp.valueOf(resultado2.getString("quando"));
                    }

                    listaFuncionarios.add(new Funcionario(resultado.getInt("codigoInterno"), resultado
                            .getInt("chavePessoa"), resultado.getInt("numeroMecanografico"), resultado
                            .getInt("chaveHorarioActual"), antiguidade, (Integer) resultado2
                            .getObject("chaveFuncResponsavel"), (Integer) resultado2
                            .getObject("chaveCCLocalTrabalho"), (Integer) resultado2
                            .getObject("chaveCCCorrespondencia"), (Integer) resultado2
                            .getObject("chaveCCVencimento"), resultado2.getString("calendario"),
                            (Integer) resultado2.getObject("chaveStatus"), dataInicio, dataFim,
                            resultado2.getInt("quem"), dataQuando));
                }
            }
            sql2.close();
            sql.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("FuncionarioRelacional.lerTodosFuncionariosAssiduidade: " + e.toString());
            return null;
        }
        return listaFuncionarios;
    } /* lerTodosFuncionariosAssiduidade */

    /***************************************************************************
     * Assiduidade do Funcionario
     **************************************************************************/
    //Se retornar null ocorreu um erro ou o funcionário não tem fim de
    // Assiduidade
    //Se retornar uma data pode não ser o fim de Assiduidade ou o fim do
    // horário
    public Timestamp lerFimAssiduidade(int numMecanografico) {
        Timestamp dataAssiduidade = null;
        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT codigoInterno FROM ass_FUNCIONARIO "
                            + "WHERE numeroMecanografico = ?");
            sql.setInt(1, numMecanografico);
            ResultSet resultadoQuery = sql.executeQuery();
            int chaveFuncionario = 0;
            if (resultadoQuery.next()) {
                chaveFuncionario = resultadoQuery.getInt("codigoInterno");
            } else {
                sql.close();
                return null;
            }
            sql.close();
            sql = UtilRelacional.prepararComando("SELECT MAX(dataInicio) FROM ass_HORARIO "
                    + "WHERE chaveFuncionario = ?");
            sql.setInt(1, chaveFuncionario);
            resultadoQuery = sql.executeQuery();
            Date dataInicioAssiduidade = null;
            if (resultadoQuery.next()) {
                dataInicioAssiduidade = resultadoQuery.getDate("MAX(dataInicio)");
            }
            sql.close();
            sql = UtilRelacional.prepararComando("SELECT dataFim FROM ass_HORARIO "
                    + "WHERE chaveFuncionario = ? AND dataInicio = ?");
            sql.setInt(1, chaveFuncionario);
            sql.setDate(2, new java.sql.Date(dataInicioAssiduidade.getTime()));
            resultadoQuery = sql.executeQuery();
            if (resultadoQuery.next()) {
                if (resultadoQuery.getString("dataFim") != null) {
                    dataAssiduidade = new Timestamp(Timestamp.valueOf(
                            resultadoQuery.getString("dataFim") + " 23:59:59.0").getTime());
                }
            }
            sql.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("FuncionarioRelacional.lerFimAssiduidade: " + e.toString());
        }
        {
            return dataAssiduidade;
        }
    } /* lerFimAssiduidade */

    public Timestamp lerInicioAssiduidade(int numMecanografico) {
        Timestamp dataAssiduidade = null;
        try {
            //chave do funcionário
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT codigoInterno FROM ass_FUNCIONARIO "
                            + "WHERE numeroMecanografico = ?");
            sql.setInt(1, numMecanografico);

            ResultSet resultadoQuery = sql.executeQuery();

            int chaveFuncionario = 0;
            if (resultadoQuery.next()) {
                chaveFuncionario = resultadoQuery.getInt("codigoInterno");
            } else {
                sql.close();
                return null;
            }
            sql.close();

            //data de inicio de assiduidade pelo horário
            sql = UtilRelacional.prepararComando("SELECT MIN(dataInicio) FROM ass_HORARIO "
                    + "WHERE chaveFuncionario = ?");
            sql.setInt(1, chaveFuncionario);

            resultadoQuery = sql.executeQuery();
            if (resultadoQuery.next()) {
                resultadoQuery.getDate("MIN(dataInicio)");
                if (!resultadoQuery.wasNull()) {
                    dataAssiduidade = new Timestamp(resultadoQuery.getDate("MIN(dataInicio)").getTime());
                }
            }
            sql.close();

            //data de inicio de assiduidade pelo histórico
            if (dataAssiduidade == null) {
                sql = UtilRelacional
                        .prepararComando("SELECT MIN(dataInicio) FROM ass_FUNCIONARIO_HISTORICO "
                                + "WHERE chaveFuncionario = ? AND chaveStatus IS NOT NULL");
                sql.setInt(1, chaveFuncionario);

                resultadoQuery = sql.executeQuery();
                if (resultadoQuery.next()) {
                    resultadoQuery.getDate("MIN(dataInicio)");
                    if (!resultadoQuery.wasNull()) {
                        dataAssiduidade = new Timestamp(resultadoQuery.getDate("MIN(dataInicio)")
                                .getTime());
                    }
                }
                sql.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("FuncionarioRelacional.lerInicioAssiduidade: " + e.toString());
        }
        {
            return dataAssiduidade;
        }
    } /* lerInicioAssiduidade */

}
