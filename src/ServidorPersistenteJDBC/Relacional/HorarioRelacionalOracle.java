package ServidorPersistenteJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import Dominio.Horario;
import ServidorPersistenteJDBC.IHorarioPersistente;
import ServidorPersistenteJDBC.SuportePersistenteOracle;
import constants.assiduousness.Constants;

/**
 * @author Fernanda Quitério & Tânia Pousão
 */
public class HorarioRelacionalOracle implements IHorarioPersistente
{

    public boolean alterarHorario(Horario horario)
    {
        return false;
    }

    public boolean alterarExcepcaoHorario(Horario horario)
    {
        return false;
    }

    public boolean alterarDataFimHorario(Date dataFim, int numeroMecanografico)
    {
        return false;
    } /* alterarDataFimHorario */

    public boolean apagarHorario(int codigoInterno)
    {
        return false;
    }

    public boolean apagarTodosHorarios()
    {
        return false;
    }

    public boolean associarExcepcaoHorarioRegime(int chaveHorario, int chaveRegime)
    {
        return false;
    }

    public boolean associarHorarioRegime(int chaveHorario, int chaveRegime)
    {
        return false;
    }

    public int calcularIndiceRotacao(int numDiasRotacao, Date dataInicioHorario, Date dataConsulta)
    {
        return 0;
    }

    public int calcularIndiceDescanso(int indiceRotacao)
    {

        return 0;
    }
    public boolean escreverExcepcaoHorario(Horario horario)
    {
        return false;
    }

    public int calculaIndiceReferenciaDescanso(
        int indiceRotacao,
        int indiceDescanso,
        int numDiasRotacao)
    {
        return 0;
    }
    public boolean escreverHorario(Horario horario)
    {
        return false;
    }

    public boolean escreverRotacoes(ArrayList rotacaoHorario)
    {
        return false;
    }

    public Horario lerExcepcaoHorarioPorNumMecanografico(int numMecanografico, Timestamp dataConsulta)
    {
        return null;
    } /* lerExcepcaoHorarioPorNumMecanografico */

    public ArrayList lerExcepcoesHorarioPorNumMecanografico(int numMecanografico)
    {
        //ORACLE: método acede á BD Oracle para fazer carregamento dos horários
        ArrayList excepcoesHorario = null;
        try
        {
            SuportePersistenteOracle.getInstance().iniciarTransaccao();
            try
            {
                // chave do funcionario
                PreparedStatement sql =
                    UtilRelacional.prepararComando(
                        "SELECT codigoInterno FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");
                sql.setInt(1, numMecanografico);
                ResultSet resultadoQuery = sql.executeQuery();
                int chaveFuncionario = 0;
                if (resultadoQuery.next())
                {
                    chaveFuncionario = resultadoQuery.getInt("codigoInterno");
                }
                else
                {
                    sql.close();
                    SuportePersistenteOracle.getInstance().cancelarTransaccao();
                    return null;
                }
                sql.close();

                // excepcoes de horarios
                sql =
                    UtilRelacionalOracle.prepararComando(
                        "SELECT * FROM ASS_EXCHDIA WHERE ASS_EXCHDIAPESSOA = ? ");
                sql.setInt(1, numMecanografico);
                resultadoQuery = sql.executeQuery();

                // chaves dos horarios dia das excepções de horário
                PreparedStatement sqlChaveHorario =
                    UtilRelacional.prepararComando(
                        "SELECT codigoInterno FROM ass_HORARIO_TIPO " + "WHERE sigla = ?");
                ResultSet resultadoChaveHorario = null;

                excepcoesHorario = new ArrayList();
                while (resultadoQuery.next())
                {
                    sqlChaveHorario.setString(1, resultadoQuery.getString("ASS_EXCHDIAHDIA"));
                    resultadoChaveHorario = sqlChaveHorario.executeQuery();

                    if (resultadoChaveHorario.next())
                    {
                        excepcoesHorario.add(
                            new Horario(
                                resultadoChaveHorario.getInt("codigoInterno"),
                                chaveFuncionario,
                                java.sql.Date.valueOf(
                                    resultadoQuery.getString("ASS_EXCHDIADTINI").substring(
                                        0,
                                        resultadoQuery.getString("ASS_EXCHDIADTINI").indexOf(" "))),
                                java.sql.Date.valueOf(
                                    resultadoQuery.getString("ASS_EXCHDIADTFIM").substring(
                                        0,
                                        resultadoQuery.getString("ASS_EXCHDIADTFIM").indexOf(" "))),
                                Constants.NUMDIAS_ROTACAO,
                                Constants.INICIO_ROTACAO,
                                resultadoQuery.getInt("ASS_EXCHDIAWHO"),
                                resultadoQuery.getTimestamp("ASS_EXCHDIAWHEN")));

                    }
                }
                sqlChaveHorario.close();
                sql.close();
            }
            catch (Exception e)
            {
                SuportePersistenteOracle.getInstance().cancelarTransaccao();
                e.printStackTrace();
                System.out.println(
                    "HorarioRelacionalOracle.lerExcepcoesHorarioPorNumMecanografico: " + e.toString());
                return null;
            }
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        }
        catch (Exception e)
        {
            System.out.println(
                "HorarioRelacionalOracle.lerExcepcoesHorarioPorNumMecanografico: " + e.toString());
            return null;
        }
        return excepcoesHorario;
    } /* lerExcepcoesHorarioPorNumMecanografico */

    public ArrayList lerExcepcoesHorarioPorNumMecanografico(
        int numMecanografico,
        Date dataInicio,
        Date dataFim)
    {
        return null;
    } /* lerExcepcoesHorarioPorNumMecanografico */

    public ArrayList lerHistoricoHorarioPorNumMecanografico(int numMecanografico)
    {
        //ORACLE: método acede á BD Oracle para fazer carregamento dos horários
        ArrayList historicoHorario = null;

        try
        {
            SuportePersistenteOracle.getInstance().iniciarTransaccao();
            try
            {
                // chave do funcionario
                PreparedStatement sql =
                    UtilRelacional.prepararComando(
                        "SELECT codigoInterno FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");
                sql.setInt(1, numMecanografico);
                ResultSet resultadoQuery = sql.executeQuery();
                int chaveFuncionario = 0;
                if (resultadoQuery.next())
                {
                    chaveFuncionario = resultadoQuery.getInt("codigoInterno");
                }
                else
                {
                    sql.close();
                    SuportePersistenteOracle.getInstance().cancelarTransaccao();
                    return null;
                }
                sql.close();

                // histórico de horários
                sql =
                    UtilRelacionalOracle.prepararComando(
                        "SELECT * FROM ASS_HISEMPREG WHERE EMP_NUM = ? "
                            + "AND ASS_HISEMP_TIPO = 12 ORDER BY ASS_HISEMP_DHINI");
                sql.setInt(1, numMecanografico);
                resultadoQuery = sql.executeQuery();

                // dados dos horários dia do histórico
                PreparedStatement sqlDadosHorario =
                    UtilRelacionalOracle.prepararComando(
                        "SELECT ASS_HTHDHD, ASS_HTHDREPETICAO, ASS_HTHDPOSICAO FROM ASS_HTOPO_HDIAS "
                            + "WHERE ASS_HTHDHT = ? AND ASS_HTHDHD <> 'DS' AND ASS_HTHDHD <> 'DSC'");
                ResultSet resultadoDadosHorario = null;

                // chaves dos horários dias do histórico
                PreparedStatement sqlChaveHorario =
                    UtilRelacional.prepararComando(
                        "SELECT codigoInterno FROM ass_HORARIO_TIPO " + "WHERE sigla = ?");
                ResultSet resultadoChaveHorario = null;

                historicoHorario = new ArrayList();
                String siglaHorario = null;
                Date dataInicioHorario = null;
                Date dataFimHorario = null;

                while (resultadoQuery.next())
                {
                    siglaHorario =
                        resultadoQuery.getString("ASS_HISEMP_CHOSE").substring(
                            0,
                            resultadoQuery.getString("ASS_HISEMP_CHOSE").indexOf("#"));
                    dataInicioHorario =
                        java.sql.Date.valueOf(
                            resultadoQuery.getString("ASS_HISEMP_DHINI").substring(
                                0,
                                resultadoQuery.getString("ASS_HISEMP_DHINI").indexOf(" ")));
                    dataFimHorario =
                        java.sql.Date.valueOf(
                            resultadoQuery.getString("ASS_HISEMP_DHFIM").substring(
                                0,
                                resultadoQuery.getString("ASS_HISEMP_DHFIM").indexOf(" ")));

                    // horários dia do funcionário
                    if (siglaHorario != null)
                    {
                        sqlDadosHorario.setString(1, siglaHorario);
                        resultadoDadosHorario = sqlDadosHorario.executeQuery();
                        while (resultadoDadosHorario.next())
                        {

                            sqlChaveHorario.setString(1, resultadoDadosHorario.getString("ASS_HTHDHD"));
                            resultadoChaveHorario = sqlChaveHorario.executeQuery();
                            if (resultadoChaveHorario.next())
                            {
                                historicoHorario.add(
                                    new Horario(
                                        resultadoChaveHorario.getInt("codigoInterno"),
                                        chaveFuncionario,
                                        dataInicioHorario,
                                        dataFimHorario,
                                        resultadoDadosHorario.getInt("ASS_HTHDREPETICAO"),
                                        resultadoDadosHorario.getInt("ASS_HTHDPOSICAO"),
                                        resultadoQuery.getInt("ASS_HISEMP_WHO"),
                                        resultadoQuery.getTimestamp("ASS_HISEMP_WHEN")));
                            }
                        }
                    }
                }
                sqlChaveHorario.close();
                sqlDadosHorario.close();
                sql.close();
            }
            catch (Exception e)
            {
                SuportePersistenteOracle.getInstance().cancelarTransaccao();
                e.printStackTrace();
                System.out.println(
                    "HorarioRelacionalOracle.lerHistoricoHorarioPorNumMecanografico: " + e.toString());
                return null;
            }
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        }
        catch (Exception e)
        {
            System.out.println(
                "HorarioRelacionalOracle.lerHistoricoHorarioPorNumMecanografico: " + e.toString());
            return null;
        }
        return historicoHorario;
    } /* lerHistoricoHorarioPorNumMecanografico */

    /*
	 * public ArrayList lerHistoricoHorarioPorNumMecanografico(int numMecanografico) { //ORACLE: método
	 * acede á BD Oracle para fazer carregamento dos horários ArrayList historicoHorario = null;
	 * ArrayList listaHorarios = null; ArrayList listaExcepcoes = null;
	 * 
	 * try { SuportePersistenteOracle.getInstance().iniciarTransaccao(); try { // chave do funcionario
	 * PreparedStatement sql = UtilRelacional.prepararComando( "SELECT codigoInterno FROM funcionario
	 * WHERE numeroMecanografico = ?"); sql.setInt(1, numMecanografico); ResultSet resultadoQuery =
	 * sql.executeQuery(); int chaveFuncionario = 0; if (resultadoQuery.next()) { chaveFuncionario =
	 * resultadoQuery.getInt("codigoInterno"); } else { sql.close(); return null; } sql.close();
	 *  // histórico de horários sql = UtilRelacionalOracle.prepararComando( FROM ASS_HISEMPREG WHERE
	 * EMP_NUM = ? " + "AND ASS_HISEMP_TIPO = 12 ORDER BY ASS_HISEMP_DHINI"); sql.setInt(1,
	 * numMecanografico); resultadoQuery = sql.executeQuery();
	 *  // dados dos horários dia do histórico PreparedStatement sqlDadosHorario =
	 * UtilRelacionalOracle.prepararComando( "SELECT ASS_HTHDHD, ASS_HTHDREPETICAO, ASS_HTHDPOSICAO FROM
	 * ASS_HTOPO_HDIAS " + "WHERE ASS_HTHDHT = ? AND ASS_HTHDHD <> 'DS' AND ASS_HTHDHD <> 'DSC'");
	 * ResultSet resultadoDadosHorario = null;
	 *  // chaves dos horários dias do histórico PreparedStatement sqlChaveHorario =
	 * UtilRelacional.prepararComando( "SELECT codigoInterno FROM horario_tipo " + "WHERE sigla = ?");
	 * ResultSet resultadoChaveHorario = null;
	 * 
	 * historicoHorario = new ArrayList(); listaHorarios = new ArrayList();
	 * 
	 * String siglaHorario = null; Date dataInicioHorario = null; Date dataFimHorario = null;
	 * 
	 * ListIterator iterador = null; Horario horario = null; ListIterator iteradorExcepcoes = null;
	 * Horario excepcao = null;
	 * 
	 * Calendar calendario = Calendar.getInstance(); calendario.setLenient(false);
	 * 
	 * while (resultadoQuery.next()) { siglaHorario =
	 * resultadoQuery.getString("ASS_HISEMP_CHOSE").substring( 0,
	 * resultadoQuery.getString("ASS_HISEMP_CHOSE").indexOf("#")); dataInicioHorario =
	 * java.sql.Date.valueOf( resultadoQuery.getString("ASS_HISEMP_DHINI").substring( 0,
	 * resultadoQuery.getString("ASS_HISEMP_DHINI").indexOf(" "))); dataFimHorario =
	 * java.sql.Date.valueOf( resultadoQuery.getString("ASS_HISEMP_DHFIM").substring( 0,
	 * resultadoQuery.getString("ASS_HISEMP_DHFIM").indexOf(" ")));
	 *  // horários dia do funcionário if (siglaHorario != null) { sqlDadosHorario.setString(1,
	 * siglaHorario); resultadoDadosHorario = sqlDadosHorario.executeQuery(); while
	 * (resultadoDadosHorario.next()) { sqlChaveHorario.setString(1,
	 * resultadoDadosHorario.getString("ASS_HTHDHD")); resultadoChaveHorario =
	 * sqlChaveHorario.executeQuery(); if (resultadoChaveHorario.next()) { listaHorarios.add( new
	 * Horario( resultadoChaveHorario.getInt("codigoInterno"), chaveFuncionario, dataInicioHorario,
	 * dataFimHorario, resultadoDadosHorario.getInt("ASS_HTHDREPETICAO"),
	 * resultadoDadosHorario.getInt("ASS_HTHDPOSICAO"))); } }
	 *  // horários excepções deste horário listaExcepcoes = lerExcepcoesHorarioPorNumMecanografico(
	 * numMecanografico, dataInicioHorario, dataFimHorario); if (listaExcepcoes != null) {
	 * iteradorExcepcoes = listaExcepcoes.listIterator(); while (iteradorExcepcoes.hasNext()) { excepcao =
	 * (Horario)iteradorExcepcoes.next();
	 * 
	 * if (excepcao.getDataInicio() != dataInicioHorario) {
	 * 
	 * iterador = listaHorarios.listIterator(); while (iterador.hasNext()) { horario = (Horario)
	 * ((Horario)iterador.next()).clone(); horario.setDataInicio(dataInicioHorario);
	 * 
	 * //dia anterior ao inicio da excepaco calendario.clear();
	 * calendario.setTime(excepcao.getDataInicio()); calendario.add(Calendar.DAY_OF_MONTH, -1);
	 * 
	 * horario.setDataFim(calendario.getTime()); historicoHorario.add(horario); } }
	 * 
	 * //dia seguinte ao fim da excepaco calendario.clear(); calendario.setTime(excepcao.getDataFim());
	 * calendario.add(Calendar.DAY_OF_MONTH, +1);
	 * 
	 * dataInicioHorario = calendario.getTime();
	 * 
	 * historicoHorario.add(excepcao); } }
	 * 
	 * iterador = listaHorarios.listIterator(); while (iterador.hasNext()) { horario =
	 * (Horario)iterador.next(); horario.setDataInicio(dataInicioHorario);
	 * historicoHorario.add(horario); } } } sqlChaveHorario.close(); sqlDadosHorario.close();
	 * sql.close(); } catch (Exception e) { SuportePersistenteOracle.getInstance().cancelarTransaccao();
	 * System.out.println( "HorarioRelacionalOracle.lerHistoricoHorarioPorNumMecanografico: " +
	 * e.toString()); return null; } SuportePersistenteOracle.getInstance().confirmarTransaccao(); }
	 * catch (Exception e) { System.out.println(
	 * "HorarioRelacionalOracle.lerHistoricoHorarioPorNumMecanografico: " + e.toString()); return null; }
	 * finally { return historicoHorario; }
	 */ /* lerHistoricoHorarioPorNumMecanografico */

    public Horario lerHorario(int codigoInterno)
    {
        return null;
    }

    public Horario lerHorario(int codigoInterno, Timestamp dataConsulta)
    {
        return null;
    }

    public ArrayList lerHorarioActualPorNumMecanografico(int numMecanografico)
    {
        //	ORACLE: método acede á BD Oracle para fazer carregamento dos horários
        ArrayList listaHorariosActuais = null;

        try
        {
            SuportePersistenteOracle.getInstance().iniciarTransaccao();
            try
            {
                // chave do funcionario
                PreparedStatement sql =
                    UtilRelacional.prepararComando(
                        "SELECT codigoInterno FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");
                sql.setInt(1, numMecanografico);
                ResultSet resultado = sql.executeQuery();
                int chaveFuncionario = 0;
                if (resultado.next())
                {
                    chaveFuncionario = resultado.getInt("codigoInterno");
                }
                else
                {
                    sql.close();
                    SuportePersistenteOracle.getInstance().cancelarTransaccao();
                    return null;
                }
                sql.close();

                // sigla do horário Topo do funcionário e data Inicio
                sql =
                    UtilRelacionalOracle.prepararComando(
                        "SELECT ASS_EMPHT, ASS_EMPSTATUS, ASS_EMPDTINI FROM ASS_EMPREG "
                            + "WHERE ASS_EMPPESSOA = ?");
                sql.setInt(1, numMecanografico);
                resultado = sql.executeQuery();
                String siglaHorario = null;
                Date dataInicioHorario = null;
                int status = 0;

                if (resultado.next())
                {
                    siglaHorario = resultado.getString("ASS_EMPHT");
                    status = Integer.valueOf(resultado.getString("ASS_EMPSTATUS")).intValue();
                    dataInicioHorario =
                        java.sql.Date.valueOf(
                            resultado.getString("ASS_EMPDTINI").substring(
                                0,
                                resultado.getString("ASS_EMPDTINI").indexOf(" ")));
                }
                sql.close();

                //data inicio do horário(é igual à data fim do último horário)
                sql =
                    UtilRelacionalOracle.prepararComando(
                        "SELECT MAX(ASS_HISEMP_DHFIM) FROM ASS_HISEMPREG "
                            + "WHERE EMP_NUM = ? AND ASS_HISEMP_TIPO = 12");
                sql.setInt(1, numMecanografico);
                resultado = sql.executeQuery();
                if (resultado.next())
                {
                    if (resultado.getString("MAX(ASS_HISEMP_DHFIM)") != null)
                    {
                        Calendar calendario = Calendar.getInstance();
                        calendario.setLenient(false);
                        calendario.setTime(
                            java.sql.Date.valueOf(
                                resultado.getString("MAX(ASS_HISEMP_DHFIM)").substring(
                                    0,
                                    resultado.getString("MAX(ASS_HISEMP_DHFIM)").indexOf(" "))));
                        calendario.add(Calendar.DAY_OF_MONTH, +1);

                        dataInicioHorario = new java.sql.Date(calendario.getTimeInMillis());
                    }
                }
                sql.close();

                // data fim do horario uma vez que já não cumpre assiduidade
                Date dataFimHorario = null;
                if ((status == 1)
                    || (status == 3)
                    || ((status == 4) || (status == 5) || (status == 9) || (status == 12)))
                {
                    sql =
                        UtilRelacionalOracle.prepararComando(
                            "SELECT ASS_HISEMP_DHFIM FROM ASS_HISEMPREG "
                                + "WHERE EMP_NUM = ? AND ASS_HISEMP_TIPO = 11");
                    sql.setInt(1, numMecanografico);
                    resultado = sql.executeQuery();
                    if (resultado.next())
                    {
                        if (resultado.getString("ASS_HISEMP_DHFIM") != null)
                        {
                            dataFimHorario =
                                java.sql.Date.valueOf(
                                    resultado.getString("ASS_HISEMP_DHFIM").substring(
                                        0,
                                        resultado.getString("ASS_HISEMP_DHFIM").indexOf(" ")));
                        }
                    }
                    sql.close();
                }

                if (siglaHorario != null && dataInicioHorario != null)
                {
                    // Horário actual do Funcionario
                    sql =
                        UtilRelacionalOracle.prepararComando(
                            "SELECT ASS_HTHDHD, ASS_HTHDREPETICAO, ASS_HTHDPOSICAO FROM ASS_HTOPO_HDIAS "
                                + "WHERE ASS_HTHDHT = ? AND ASS_HTHDHD <> 'DS' AND ASS_HTHDHD <> 'DSC'");
                    sql.setString(1, siglaHorario);
                    resultado = sql.executeQuery();

                    PreparedStatement sqlChaveHorario =
                        UtilRelacional.prepararComando(
                            "SELECT codigoInterno FROM ass_HORARIO_TIPO " + "WHERE sigla = ?");
                    ResultSet resultadoChaveHorario = null;

                    listaHorariosActuais = new ArrayList();
                    while (resultado.next())
                    {
                        sqlChaveHorario.setString(1, resultado.getString("ASS_HTHDHD"));
                        resultadoChaveHorario = sqlChaveHorario.executeQuery();

                        if (resultadoChaveHorario.next())
                        {
                            listaHorariosActuais.add(
                                new Horario(
                                    resultadoChaveHorario.getInt("codigoInterno"),
                                    chaveFuncionario,
                                    dataInicioHorario,
                                    dataFimHorario,
                                    resultado.getInt("ASS_HTHDREPETICAO"),
                                    resultado.getInt("ASS_HTHDPOSICAO")));
                        }
                        else
                        {
                            sqlChaveHorario.close();
                            sql.close();
                            SuportePersistenteOracle.getInstance().cancelarTransaccao();
                            return null;
                        }
                    }
                    sqlChaveHorario.close();
                }
                sql.close();
            }
            catch (Exception e)
            {
                SuportePersistenteOracle.getInstance().cancelarTransaccao();
                e.printStackTrace();
                System.out.println(
                    "HorarioRelacionalOracle.lerHorarioActualPorNumMecanografico: " + e.toString());
                return null;
            }
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        }
        catch (Exception e)
        {
            System.out.println(
                "HorarioRelacionalOracle.lerHorarioActualPorNumMecanografico: " + e.toString());
            return null;
        }
        return listaHorariosActuais;
    } /* lerHorarioActualPorNumMecanografico */

    /*
	 * public ArrayList lerHorarioActualPorNumMecanografico(int numMecanografico) { // ORACLE: método
	 * acede á BD Oracle para fazer carregamento dos horários ArrayList listaHorariosActuais = null;
	 * ArrayList listaHorarios = null; ArrayList listaExcepcoes = null;
	 * 
	 * try { SuportePersistenteOracle.getInstance().iniciarTransaccao(); try { // chave do funcionario
	 * PreparedStatement sql = UtilRelacional.prepararComando( "SELECT codigoInterno FROM funcionario
	 * WHERE numeroMecanografico = ?"); sql.setInt(1, numMecanografico); ResultSet resultado =
	 * sql.executeQuery(); int chaveFuncionario = 0; if (resultado.next()) { chaveFuncionario =
	 * resultado.getInt("codigoInterno"); } else { sql.close(); return null; } sql.close();
	 * System.out.println("Horario Actual: chaveFuncionario");
	 *  // sigla do horário Topo do funcionário e data Inicio sql =
	 * UtilRelacionalOracle.prepararComando( "SELECT ASS_EMPHT, ASS_EMPSTATUS, ASS_EMPDTINI FROM
	 * ASS_EMPREG " + "WHERE ASS_EMPPESSOA = ?"); sql.setInt(1, numMecanografico); resultado =
	 * sql.executeQuery(); String siglaHorario = null; Date dataInicioHorario = null; int status = 0; if
	 * (resultado.next()) { siglaHorario = resultado.getString("ASS_EMPHT"); status =
	 * Integer.valueOf(resultado.getString("ASS_EMPSTATUS")).intValue(); dataInicioHorario =
	 * java.sql.Date.valueOf( resultado.getString("ASS_EMPDTINI").substring( 0,
	 * resultado.getString("ASS_EMPDTINI").indexOf(" "))); System.out.println("Horario Actual:
	 * dataInicio em ASS_EMPREG: " + dataInicioHorario); } else { //Funcionario sem horário actualmente
	 * sql.close(); //return null; } sql.close(); System.out.println("Horario Actual: ASS_EMPREG");
	 * 
	 * //data inicio do horário(é igual à data fim do último horário) sql =
	 * UtilRelacionalOracle.prepararComando( "SELECT MAX(ASS_HISEMP_DHFIM) FROM ASS_HISEMPREG " + "WHERE
	 * EMP_NUM = ? AND ASS_HISEMP_TIPO = 12"); sql.setInt(1, numMecanografico); resultado =
	 * sql.executeQuery(); if (resultado.next()) { if (resultado.getString("MAX(ASS_HISEMP_DHFIM)") !=
	 * null) { Calendar calendario = Calendar.getInstance(); calendario.setLenient(false);
	 * calendario.setTime( java.sql.Date.valueOf(
	 * resultado.getString("MAX(ASS_HISEMP_DHFIM)").substring( 0,
	 * resultado.getString("MAX(ASS_HISEMP_DHFIM)").indexOf(" "))));
	 * calendario.add(Calendar.DAY_OF_MONTH, +1);
	 * 
	 * dataInicioHorario = new java.sql.Date(calendario.getTimeInMillis()); System.out.println("Horario
	 * Actual: dataInicio em ASS_HIS: " + dataInicioHorario); } } else { sql.close(); return null; }
	 * sql.close(); System.out.println("Horario Actual: data fim do historico");
	 *  // data fim do horario uma vez que já não cumpre assiduidade Date dataFimHorario = null; if
	 * ((status == 1) || (status == 3) || ((status == 4) || (status == 5) || (status == 9))) { sql =
	 * UtilRelacionalOracle.prepararComando( "SELECT ASS_HISEMP_DHFIM FROM ASS_HISEMPREG " + "WHERE
	 * EMP_NUM = ? AND ASS_HISEMP_TIPO = 11"); sql.setInt(1, numMecanografico); resultado =
	 * sql.executeQuery(); if (resultado.next()) { if (resultado.getString("ASS_HISEMP_DHFIM") != null) {
	 * dataFimHorario = java.sql.Date.valueOf( resultado.getString("ASS_HISEMP_DHFIM").substring( 0,
	 * resultado.getString("ASS_HISEMP_DHFIM").indexOf(" "))); System.out.println("Horario Actual:
	 * dataFim em ASS_HIS: " + dataFimHorario); } } sql.close(); } System.out.println("Horario Actual:
	 * data fim de assiduidade");
	 * 
	 * if (siglaHorario != null && dataInicioHorario != null) { // Horário actual do Funcionario sql =
	 * UtilRelacionalOracle.prepararComando( "SELECT ASS_HTHDHD, ASS_HTHDREPETICAO, ASS_HTHDPOSICAO FROM
	 * ASS_HTOPO_HDIAS " + "WHERE ASS_HTHDHT = ? AND ASS_HTHDHD <> 'DS' AND ASS_HTHDHD <> 'DSC'");
	 * sql.setString(1, siglaHorario); resultado = sql.executeQuery();
	 * 
	 * PreparedStatement sqlChaveHorario = UtilRelacional.prepararComando( "SELECT codigoInterno FROM
	 * horario_tipo " + "WHERE sigla = ?"); ResultSet resultadoChaveHorario = null;
	 * 
	 * listaHorarios = new ArrayList(); while (resultado.next()) { sqlChaveHorario.setString(1,
	 * resultado.getString("ASS_HTHDHD")); resultadoChaveHorario = sqlChaveHorario.executeQuery();
	 * 
	 * if (resultadoChaveHorario.next()) { listaHorarios.add( new Horario(
	 * resultadoChaveHorario.getInt("codigoInterno"), chaveFuncionario, dataInicioHorario,
	 * dataFimHorario, resultado.getInt("ASS_HTHDREPETICAO"), resultado.getInt("ASS_HTHDPOSICAO"))); }
	 * else { sqlChaveHorario.close(); sql.close(); return null; } } System.out.println( "Horario
	 * Actual: constroiu os horarios Actuais" + listaHorarios.size());
	 * 
	 * listaHorariosActuais = new ArrayList();
	 * 
	 * ListIterator iterador = null; Horario horario = null; ListIterator iteradorExcepcoes = null;
	 * Horario excepcao = null;
	 * 
	 * Calendar calendario = Calendar.getInstance(); calendario.setLenient(false);
	 *  // horários excepções deste horário listaExcepcoes = lerExcepcoesHorarioPorNumMecanografico(
	 * numMecanografico, dataInicioHorario, dataFimHorario); if (listaExcepcoes != null) {
	 * 
	 * iteradorExcepcoes = listaExcepcoes.listIterator(); while (iteradorExcepcoes.hasNext()) { excepcao =
	 * (Horario)iteradorExcepcoes.next();
	 * 
	 * if (excepcao.getDataInicio() != dataInicioHorario) {
	 * 
	 * iterador = listaHorarios.listIterator(); while (iterador.hasNext()) { horario = (Horario)
	 * ((Horario)iterador.next()).clone(); horario.setDataInicio(dataInicioHorario);
	 * 
	 * //dia anterior ao inicio da excepcao calendario.clear();
	 * calendario.setTime(excepcao.getDataInicio()); calendario.add(Calendar.DAY_OF_MONTH, -1);
	 * 
	 * horario.setDataFim(calendario.getTime()); listaHorariosActuais.add(horario); } } //dia seguinte
	 * ao fim da excepaco calendario.clear(); calendario.setTime(excepcao.getDataFim());
	 * calendario.add(Calendar.DAY_OF_MONTH, +1);
	 * 
	 * dataInicioHorario = calendario.getTime(); listaHorariosActuais.add(excepcao); } }
	 * 
	 * iterador = listaHorarios.listIterator(); while (iterador.hasNext()) { horario =
	 * (Horario)iterador.next(); horario.setDataInicio(dataInicioHorario);
	 * listaHorariosActuais.add(horario); } sqlChaveHorario.close(); System.out.println( "Horario
	 * Actual: lista horarios actuais: " + listaHorariosActuais.size()); } sql.close(); } catch
	 * (Exception e) { SuportePersistenteOracle.getInstance().cancelarTransaccao(); System.out.println(
	 * "HorarioRelacionalOracle.lerHorarioActualPorNumMecanografico: " + e.toString()); return null; }
	 * SuportePersistenteOracle.getInstance().confirmarTransaccao(); } catch (Exception e) {
	 * System.out.println( "HorarioRelacionalOracle.lerHorarioActualPorNumMecanografico: " +
	 * e.toString()); return null; } finally { return listaHorariosActuais; }
	 */ /* lerHorarioActualPorNumMecanografico */

    public Horario lerHorarioPorNumFuncionario(int numMecanografico, Timestamp dataConsulta)
    {
        return null;
    } /* lerHorarioPorNumFuncionario */

    public ArrayList lerHorariosPorNumMecanografico(int numMecanografico, Date dataInicio, Date dataFim)
    {
        return null;
    } /* lerHorariosPorNumMecanografico */

    public Horario lerHorarioPorFuncionario(int chaveFuncNaoDocente)
    {
        return null;
    }

    public ArrayList lerHorariosSemFimValidade(int numMecanografico)
    {
        return null;
    }

    public Date lerInicioAssiduidade(int numMecanografico)
    {
        return null;
    } /* lerInicioAssiduidade */

    public ArrayList lerRegimes(int chaveHorario)
    {
        return null;
    }

    public HashMap lerRegimesRotacoes(ArrayList rotacaoHorario)
    {
        return null;
    }

    public ArrayList lerRotacoesPorNumMecanografico(int numMecanografico)
    {
        return null;
    } /* lerRotacoesPorNumMecanografico */

    public int ultimoCodigoInterno()
    {
        return 0;
    }

    public int ultimoCodigoInternoExcepcaoHorario()
    {
        return 0;
    }

    public ArrayList lerTodosHorariosExcepcao(Date dataInicio, Date dataFim)
    {
        return null;
    }

    public ArrayList lerTodosHorarios(Date dataInicio, Date dataFim)
    {
        return null;
    }

    public ArrayList lerTodosHorariosExcepcaoComRegime(
        int chaveRegime,
        ArrayList listaHorariosTipoComRegime,
        Date dataInicio,
        Date dataFim)
    {
        return null;
    }

    public ArrayList lerTodosHorariosComRegime(
        int chaveRegime,
        ArrayList listaHorariosTipoComRegime,
        Date dataInicio,
        Date dataFim)
    {
        return null;
    }

    public Horario existeExcepcaoHorario(Horario horario)
    {
        return null;
    }

    public Horario existeHorario(Horario horario)
    {
        return null;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IHorarioPersistente#lerExcepcaoHorario(int)
	 */
    public Horario lerExcepcaoHorario(int codigoInterno)
    {
        return null;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IHorarioPersistente#apagarHorarioExcepcao(int)
	 */
    public boolean apagarHorarioExcepcao(int codigoInterno)
    {
        return false;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IHorarioPersistente#desassociarHorarioRegime(int, int)
	 */
    public boolean desassociarHorarioRegime(int codigoInternoHorario, int codigoInternoRegime)
    {
        return false;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IHorarioPersistente#desassociarHorarioExcepcaoRegime(int, int)
	 */
    public boolean desassociarHorarioExcepcaoRegime(int codigoInternoHorario, int codigoInternoRegime)
    {
        return false;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IHorarioPersistente#lerRegimesHorarioExcepcao(int)
	 */
    public ArrayList lerRegimesHorarioExcepcao(int chaveHorario)
    {
        return null;
    }
}
