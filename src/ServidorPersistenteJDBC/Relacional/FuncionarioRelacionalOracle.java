package ServidorPersistenteJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Dominio.Funcionario;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.SuportePersistenteOracle;
import constants.assiduousness.Constants;

/**
 * @author Nanda & Tânia
 */
public class FuncionarioRelacionalOracle implements IFuncionarioPersistente
{

    public boolean alterarFuncionario(Funcionario funcionario)
    {
        return false;
    }

    public boolean alterarFuncionarioPorCodigoInterno(Funcionario funcionario)
    {
        return false;
    }

    public boolean alterarHistoricoFuncionario(Funcionario funcionario)
    {
        return false;
    }

    public boolean alterarFuncResponsavel(Funcionario funcionario)
    {
        return false;
    }

    public boolean alterarCCCorrespondenciaFuncionario(Funcionario funcionario)
    {
        return false;
    }

    public boolean alterarCCLocalTrabalhoFuncionario(Funcionario funcionario)
    {
        return false;
    }

    public boolean alterarCCVencimento(Funcionario funcionario)
    {
        return false;
    }

    public boolean alterarCalendario(Funcionario funcionario)
    {
        return false;
    }

    public boolean alterarStatusAssiduidade(Funcionario funcionario)
    {
        return false;
    }

    public boolean alterarFimValidade(int codigoInterno, Date dataFim)
    {
        return false;
    }

    public boolean apagarFuncionario(int numMecanografico)
    {
        return false;
    }

    public boolean escreverFuncionario(Funcionario funcionario)
    {
        return false;
    }

    public boolean escreverHistoricoFuncionario(Funcionario funcionario)
    {
        return false;
    }

    public boolean escreverFimFuncResponsavel(int numeroMecanografico, Date fim)
    {
        return false;
    }
    public boolean escreverFuncResponsavel(Funcionario funcionario)
    {
        return false;
    }

    public boolean escreverFimCCLocalTrabalho(int numeroMecanografico, Date fim)
    {
        return false;
    }
    public boolean escreverCCLocalTrabalho(Funcionario funcionario)
    {
        return false;
    }

    public boolean escreverFimCCCorrespondencia(int numeroMecanografico, Date fim)
    {
        return false;
    }
    public boolean escreverCCCorrespondencia(Funcionario funcionario)
    {
        return false;
    }

    public boolean escreverFimCCVencimento(int numeroMecanografico, Date fim)
    {
        return false;
    }
    public boolean escreverCCVencimento(Funcionario funcionario)
    {
        return false;
    }

    public boolean escreverFimCalendario(int numeroMecanografico, Date fim)
    {
        return false;
    }
    public boolean escreverCalendario(Funcionario funcionario)
    {
        return false;
    }

    public boolean escreverFimStatusAssiduidade(int numeroMecanografico, Date fim)
    {
        return false;
    }
    public boolean escreverStatusAssiduidade(Funcionario funcionario)
    {
        return false;
    }

    public boolean existeHistoricoFuncResponsavel(Funcionario funcionario)
    {
        return false;
    }

    public boolean existeHistoricoCCLocalTrabalho(Funcionario funcionario)
    {
        return false;
    }

    public boolean existeHistoricoCCCorrespondencia(Funcionario funcionario)
    {
        return false;
    }

    public boolean existeHistoricoCCVencimento(Funcionario funcionario)
    {
        return false;
    }

    public boolean existeHistoricoCalendario(Funcionario funcionario)
    {
        return false;
    }

    public boolean existeHistoricoStatusAssiduidade(Funcionario funcionario)
    {
        return false;
    }

    public Funcionario lerFuncionarioSemHistorico(int codigoInterno)
    {
        return null;
    }

    public Funcionario lerFuncionarioSemHistoricoPorNumMecanografico(int numMecanografico)
    {
        return null;
    }

    public Funcionario lerFuncionarioSemHistoricoPorPessoa(int chavePessoa)
    {
        return null;
    }

    public Funcionario lerFuncionario(int codigoInterno, Date dataConsulta)
    {
        return null;
    }

    //	public List lerFuncionario(int codigoInterno, Date dataInicio, Date dataFim) {
    //		return null;
    //	}

    public Funcionario lerFuncionarioPorFuncNaoDocente(int chaveFuncNaoDocente, Date dataConsulta)
    {
        return null;
    }

    public Funcionario lerFuncionarioPorNumMecanografico(int numMecanografico, Date dataConsulta)
    {
        return null;
    }

    public Funcionario lerFuncionarioPorPessoa(int chavePessoa, Date dataConsulta)
    {
        //ORACLE: acede à BD do teleponto para carregar os dados dos funcionarios
        //Já não é usada
        Funcionario funcionario = null;

        try
        {
            SuportePersistenteOracle.getInstance().iniciarTransaccao();
            try
            {

                PreparedStatement sql =
                    UtilRelacional.prepararComando(
                        "SELECT * FROM ass_FUNCIONARIO WHERE chavePessoa = ?");
                sql.setInt(1, chavePessoa);
                ResultSet resultado = sql.executeQuery();
                int numMecanografico = 0;
                if (resultado.next())
                {
                    numMecanografico = resultado.getInt("numeroMecanografico");

                    PreparedStatement sql2 =
                        UtilRelacional.prepararComando(
                            "SELECT * FROM ass_FUNCIONARIO_HISTORICO " + "WHERE chaveFuncionario = ?");

                    sql2.setInt(1, resultado.getInt("codigoInterno"));
                    ResultSet resultado2 = sql2.executeQuery();
                    if (resultado2.last())
                    {
                        Date antiguidade = null;
                        if (resultado.getString("antiguidade") != null)
                        {
                            antiguidade = java.sql.Date.valueOf(resultado.getString("antiguidade"));
                        }
                        Date dataInicio = null;
                        if (resultado2.getString("dataInicio") != null)
                        {
                            dataInicio = java.sql.Date.valueOf(resultado2.getString("dataInicio"));
                        }
                        Date dataFim = null;
                        if (resultado2.getString("dataFim") != null)
                        {
                            dataFim = java.sql.Date.valueOf(resultado2.getString("dataFim"));
                        }
                        Timestamp dataQuando = null;
                        if (resultado2.getString("quando") != null)
                        {
                            dataQuando = Timestamp.valueOf(resultado2.getString("quando"));
                        }

                        funcionario =
                            new Funcionario(
                                resultado.getInt("codigoInterno"),
                                resultado.getInt("chavePessoa"),
                                resultado.getInt("numeroMecanografico"),
                                resultado.getInt("chaveHorarioActual"),
                                antiguidade,
                                (Integer) resultado2.getObject("chaveFuncResponsavel"),
                                (Integer) resultado2.getObject("chaveCCLocalTrabalho"),
                                (Integer) resultado2.getObject("chaveCCCorrespondencia"),
                                (Integer) resultado2.getObject("chaveCCVencimento"),
                                resultado2.getString("calendario"),
                                (Integer) resultado2.getObject("chaveStatus"),
                                dataInicio,
                                dataFim,
                                resultado2.getInt("quem"),
                                dataQuando);
                    }
                    sql2.close();
                }
                else
                {
                    sql.close();
                    SuportePersistenteOracle.getInstance().cancelarTransaccao();
                    return null;
                }
                sql.close();

                //data inicio de assiduidade e status de assiduidade
                sql =
                    UtilRelacionalOracle.prepararComando(
                        "SELECT ASS_EMPDTINI, ASS_EMPSTATUS FROM ASS_EMPREG WHERE ASS_EMPPESSOA=?");
                sql.setInt(1, numMecanografico);
                resultado = sql.executeQuery();
                int status = 0;
                Date dataInicio = null;
                if (resultado.next())
                {
                    status = resultado.getInt("ASS_EMPSTATUS") + 1;
                    dataInicio = java.sql.Date.valueOf(resultado.getString("ASS_EMPDTINI"));
                    //dataInicio =
					// java.sql.Date.valueOf(resultado.getString("ASS_EMPDTINI").substring(0,
					// resultado.getString("ASS_EMPDTINI").indexOf(" ")));
                    funcionario.setChaveStatus(new Integer(status));
                    funcionario.setDataInicio(dataInicio);
                    funcionario.setAntiguidade(dataInicio);
                }
                sql.close();

                //actividade do status
                sql = UtilRelacional.prepararComando("SELECT * FROM ass_STATUS WHERE codigoInterno = ?");
                sql.setInt(1, status);
                resultado = sql.executeQuery();
                boolean isStatusInactivo = false;
                if (resultado.next())
                {
                    if (!resultado.getString("estado").equals(Constants.ASSIDUIDADE_ACTIVO))
                    {
                        //status actual é inactivo ou pendente
                        isStatusInactivo = true;
                    }
                }
                sql.close();

                //data fim do status activo de assiduidade
                Date dataFimHorario = null;
                if (isStatusInactivo)
                {
                    sql =
                        UtilRelacionalOracle.prepararComando(
                            "SELECT MAX(ASS_HISEMP_DHFIM) FROM ASS_HISEMPREG "
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

                            funcionario.setDataFim(dataFimHorario);
                        }
                    }
                    sql.close();
                }
            }
            catch (Exception e)
            {
                SuportePersistenteOracle.getInstance().cancelarTransaccao();
                e.printStackTrace();
                System.out.println(
                    "FuncionarioRelacionalOrcale.lerFuncionarioPorPessoa: " + e.toString());
                return null;
            }
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("FuncionarioRelacionalOracle.lerFuncionarioPorPessoa: " + e.toString());
            return null;
        }
        return funcionario;
    } /* lerFuncionarioPorPessoa */

    public ArrayList lerFuncionariosCCLocalTrabalho(int chaveCCLocalTrabalho, Date data)
    {
        return null;
    }

    public ArrayList lerDadosTodosFuncionarios(Date dataConsulta)
    {
        return null;
    }

    public ArrayList lerTodosFuncionarios(Date dataConsulta)
    {
        return null;
    }

    public ArrayList lerTodosFuncionariosAssiduidade(Date dataConsulta)
    {
        return null;
    }

    public ArrayList lerTodosFuncionariosNaoDocentes(Date dataConsulta)
    {
        return null;
    }

    public ArrayList lerTodosNumeros()
    {
        return null;
    }

    public ArrayList lerTodosNumerosAssiduidade()
    {
        return null;
    }

    public ArrayList lerTodosNumerosAssiduidade(Timestamp dataInicioConsulta)
    {
        return null;
    }

    public ArrayList lerTodosNumerosFuncNaoDocentes()
    {
        return null;
    }

    public List lerHistoricoFuncionario(int codigoInterno, Date data)
    {
        return null;
    }

    public Funcionario lerHistorico(int codigoInterno)
    {
        return null;
    }

    public ArrayList lerStatusAssiduidade(int numMecanografico, Timestamp dataInicio, Timestamp dataFim)
    {
        //ORACLE: acede à BD do teleponto para carregar os dados dos funcionarios sobre o status de
		// Assiduidade
        //Nota: a ligação é feita fora do serviço ServicoSeguroInicializarStatusAssiduidadeFuncionarios
		// em LeituraStatusAssiduidadeFuncionario
        ArrayList historicoStatus = null;

        //		try {
        //			SuportePersistenteOracle.getInstance().iniciarTransaccao();
        try
        {

            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "SELECT * FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");
            sql.setInt(1, numMecanografico);
            ResultSet resultado = sql.executeQuery();

            int codigoInterno = 0;
            int chavePessoa = 0;
            int chaveHorarioActual = 0;

            if (resultado.next())
            {
                codigoInterno = resultado.getInt("codigoInterno");
                chavePessoa = resultado.getInt("chavePessoa");
                chaveHorarioActual = resultado.getInt("chaveHorarioActual");
            }
            else
            {
                sql.close();
                //					SuportePersistenteOracle.getInstance().cancelarTransaccao();
                return null;
            }
            sql.close();

            historicoStatus = new ArrayList();

            //historico de status de assiduidade
            sql =
                UtilRelacionalOracle.prepararComando(
                    "SELECT * FROM ASS_HISEMPREG WHERE EMP_NUM=? AND ASS_HISEMP_TIPO = 11 ORDER BY ASS_HISEMP_DHFIM");
            sql.setInt(1, numMecanografico);
            resultado = sql.executeQuery();

            Date dataInicioHistorico = null;
            Date dataFimHistorico = null;
            int status = 0;
            while (resultado.next())
            {
                status = resultado.getInt("ASS_HISEMP_CHOSE") + 1;
                dataInicioHistorico =
                    java.sql.Date.valueOf(
                        resultado.getString("ASS_HISEMP_DHINI").substring(
                            0,
                            resultado.getString("ASS_HISEMP_DHINI").indexOf(" ")));
                dataFimHistorico =
                    java.sql.Date.valueOf(
                        resultado.getString("ASS_HISEMP_DHFIM").substring(
                            0,
                            resultado.getString("ASS_HISEMP_DHFIM").indexOf(" ")));
                historicoStatus.add(
                    new Funcionario(
                        codigoInterno,
                        chavePessoa,
                        numMecanografico,
                        chaveHorarioActual,
                        null,
                        null,
                        null,
                        null,
                        null,
                        Constants.CALENDARIO_LISBOA,
                        new Integer(status),
                        dataInicioHistorico,
                        dataFimHistorico,
                        0,
                        new Timestamp(Calendar.getInstance().getTimeInMillis())));
            }
            sql.close();

            //status actual de assiduidade
            sql = UtilRelacionalOracle.prepararComando("SELECT * FROM ASS_EMPREG WHERE ASS_EMPPESSOA=?");
            sql.setInt(1, numMecanografico);
            resultado = sql.executeQuery();
            if (resultado.next())
            {
                status = resultado.getInt("ASS_EMPSTATUS") + 1;

                if (dataFimHistorico != null)
                {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(dataFimHistorico.getTime());
                    calendar.add(Calendar.DAY_OF_MONTH, +1);
                    dataFimHistorico = calendar.getTime();
                }
                else
                {
                    dataFimHistorico =
                        java.sql.Date.valueOf(
                            resultado.getString("ASS_EMPDTINI").substring(
                                0,
                                resultado.getString("ASS_EMPDTINI").indexOf(" ")));
                }

                historicoStatus.add(
                    new Funcionario(
                        codigoInterno,
                        chavePessoa,
                        numMecanografico,
                        chaveHorarioActual,
                        null,
                        null,
                        null,
                        null,
                        null,
                        Constants.CALENDARIO_LISBOA,
                        new Integer(status),
                        dataFimHistorico,
                        null,
                        0,
                        new Timestamp(Calendar.getInstance().getTimeInMillis())));
            }
            sql.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //				SuportePersistenteOracle.getInstance().cancelarTransaccao();
            System.out.println("FuncionarioRelacionalOrcale.lerStatusAssiduidade: " + e.toString());
            return null;
            //			}
            //			SuportePersistenteOracle.getInstance().confirmarTransaccao();
            //		} catch (Exception e) {
            //			e.printStackTrace();
            //			System.out.println("FuncionarioRelacionalOracle.lerStatusAssiduidade: " + e.toString());
            //			return null;
        }
        return historicoStatus;
    }

    public boolean sobreposicaoHistoricos(int codigoInterno, Date dataInicio, Date dataFim)
    {
        boolean existeSobreposicao = false;

        return existeSobreposicao;
    }

    public boolean sobreposicaoFuncResponsavel(int codigoInterno, Date dataInicio, Date dataFim)
    {
        boolean existeSobreposicao = false;

        return existeSobreposicao;
    }

    public boolean sobreposicaoCCLocalTrabalho(int codigoInterno, Date dataInicio, Date dataFim)
    {
        boolean existeSobreposicao = false;

        return existeSobreposicao;
    }

    public boolean sobreposicaoCCCorrespondencia(int codigoInterno, Date dataInicio, Date dataFim)
    {
        boolean existeSobreposicao = false;

        return existeSobreposicao;
    }

    public boolean sobreposicaoCCVencimento(int codigoInterno, Date dataInicio, Date dataFim)
    {
        boolean existeSobreposicao = false;

        return existeSobreposicao;
    }
    public boolean sobreposicaoCalendario(int codigoInterno, Date dataInicio, Date dataFim)
    {
        boolean existeSobreposicao = false;

        return existeSobreposicao;
    }

    public boolean sobreposicaoStatusAssiduidade(int codigoInterno, Date dataInicio, Date dataFim)
    {
        boolean existeSobreposicao = false;

        return existeSobreposicao;
    }

    public boolean temHistorico(int numeroMecanografico)
    {
        boolean temHistorico = false;
        return temHistorico;
    }

    public boolean temHistoricoFuncResponsavel(int numeroMecanografico)
    {
        return false;
    }

    public boolean temHistoricoCCLocalTrabalho(int numeroMecanografico)
    {
        return false;
    }

    public boolean temHistoricoCCCorrespondencia(int numeroMecanografico)
    {
        return false;
    }

    public boolean temHistoricoCCVencimento(int numeroMecanografico)
    {
        return false;
    }

    public boolean temHistoricoCalendario(int numeroMecanografico)
    {
        return false;
    }

    public boolean temHistoricoStatusAssiduidade(int numeroMecanografico)
    {
        return false;
    }

    public int ultimoCodigoInterno()
    {
        return 0;
    }

    /****************************************************************************************************
	 * Assiduidade do Funcionario
	 ***************************************************************************************************/
    public boolean escreverFimAssiduidade(int numMecanografico, Date fimAssiduidade)
    {
        return false;
    }

    public Timestamp lerFimAssiduidade(int numeroMecanografico)
    {
        return null;
    }

    public Timestamp lerInicioAssiduidade(int numMecanografico)
    {
        return null;
    }

    public boolean verificaFimAssiduidade(int numMecanografico, Date dataFimAssiduidade)
    {
        return false;
    }
}
