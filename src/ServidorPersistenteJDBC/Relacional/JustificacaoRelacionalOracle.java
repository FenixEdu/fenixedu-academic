package ServidorPersistenteJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import Dominio.Justificacao;
import ServidorPersistenteJDBC.IJustificacaoPersistente;
import ServidorPersistenteJDBC.SuportePersistenteOracle;

/**
 * @author Fernanda Quitério e Tania Pousão
 */
public class JustificacaoRelacionalOracle implements IJustificacaoPersistente
{
    public boolean alterarJustificacao(Justificacao justificacao)
    {
        boolean resultado = false;
        return resultado;
    } /* alterarJustificacao */

    public boolean apagarJustificacao(
        int chaveFuncionario,
        java.util.Date diaInicio,
        Time horaInicio,
        java.util.Date diaFim,
        Time horaFim)
    {
        boolean resultado = false;
        return resultado;
    } /* apagarJustificacao */

    public boolean apagarTodasJustificacoes()
    {
        boolean resultado = false;
        return resultado;
    } /* apagarTodasJustificacoes */

    public boolean escreverJustificacao(Justificacao justificacao)
    {
        boolean resultado = false;
        return resultado;
    } /* escreverJustificacao */

    public boolean escreverJustificacoes(ArrayList listaJustificacoes)
    {
        boolean resultado = false;
        return resultado;
    } /* escreverJustificacoes */

    public boolean existeJustificacao(Justificacao justificacao)
    {
        boolean resultado = false;
        return resultado;
    } /* existeJustificacao */

    public Justificacao lerJustificacao(
        int chaveFuncionario,
        java.util.Date diaInicio,
        Time horaInicio,
        java.util.Date diaFim,
        Time horaFim)
    {
        Justificacao justificacao = null;
        return justificacao;
    } /* lerJustificacao */

    public ArrayList lerJustificacoes(int chaveFuncionario, java.util.Date data)
    {
        return null;
    } /* lerJustificacoes */

    public ArrayList lerJustificacoesHoras(int chaveFuncionario, Timestamp data)
    {
        return null;
    } /* lerJustificacoesHoras */

    public ArrayList lerJustificacoesHoras(
        int chaveFuncionario,
        Timestamp data,
        Time horaInicio,
        Time horaFim)
    {
        return null;
    } /* lerJustificacoesHoras */

    public ArrayList lerJustificacoesOcorrencia(int chaveFuncionario, Timestamp data)
    {
        return null;
    } /* lerJustificacoesOcorrencia */

    public ArrayList lerJustificacoesFuncionarioComValidade(
        int chaveFuncionario,
        java.util.Date diaInicio,
        java.util.Date diaFim)
    {
        return null;
    } /* lerJustificacoesFuncionarioComValidade */

    public ArrayList lerTodasJustificacoes()
    {
        //ORACLE: método acede á BD Oracle para fazer carregamento das justificações
        ArrayList listaJustificacoes = null;

        try
        {
            SuportePersistenteOracle.getInstance().iniciarTransaccao();
            try
            {
                PreparedStatement sql =
                    UtilRelacionalOracle.prepararComando("SELECT * FROM ASS_PESSJUS");
                ResultSet resultado = sql.executeQuery();
                listaJustificacoes = new ArrayList();

                // chaveParamJustificacao
                PreparedStatement sql2 =
                    UtilRelacional.prepararComando(
                        "SELECT codigoInterno FROM ass_PARAM_JUSTIFICACAO WHERE sigla = ?");
                ResultSet resultado2 = null;

                Justificacao justificacao = null;
                while (resultado.next())
                {
                    // obtem o codigo interno da justificacao
                    sql2.setString(1, resultado.getString("ASS_PESSJUSJUSTIF"));
                    resultado2 = sql2.executeQuery();
                    if (resultado2.next())
                    {
                        if (resultado.getInt("ASS_PESSJUSTIPO") != 14)
                        { //não é uma justificacao de SALDO
                            justificacao =
                                new Justificacao(
                                    resultado2.getInt("codigoInterno"),
                                    resultado.getInt("ASS_PESSJUSPESSOA"),
                                    java.sql.Date.valueOf(
                                        resultado.getString("ASS_PESSJUSDTINI").substring(
                                            0,
                                            resultado.getString("ASS_PESSJUSDTINI").indexOf(" "))),
                                    new Time((resultado.getInt("ASS_PESSJUSHINI") - 3600) * 1000),
                                    java.sql.Date.valueOf(
                                        resultado.getString("ASS_PESSJUSDTFIM").substring(
                                            0,
                                            resultado.getString("ASS_PESSJUSDTFIM").indexOf(" "))),
                                    new Time((resultado.getInt("ASS_PESSJUSHFIM") - 3600) * 1000),
                                    resultado.getString("ASS_PESSJUSOBS"),
                                    resultado.getInt("ASS_PESSJUSWHO"),
                                    Timestamp.valueOf(resultado.getString("ASS_PESSJUSWHEN")));
                        }
                        else
                        { //é uma justificacao de SALDO
                            justificacao =
                                new Justificacao(
                                    resultado2.getInt("codigoInterno"),
                                    resultado.getInt("ASS_PESSJUSPESSOA"),
                                    java.sql.Date.valueOf(
                                        resultado.getString("ASS_PESSJUSDTINI").substring(
                                            0,
                                            resultado.getString("ASS_PESSJUSDTINI").indexOf(" "))),
                                    new Time(1000 * resultado.getInt("ASS_PESSJUSHINI")),
                                    java.sql.Date.valueOf(
                                        resultado.getString("ASS_PESSJUSDTFIM").substring(
                                            0,
                                            resultado.getString("ASS_PESSJUSDTFIM").indexOf(" "))),
                                    new Time(1000 * resultado.getInt("ASS_PESSJUSHFIM")),
                                    resultado.getString("ASS_PESSJUSOBS"),
                                    resultado.getInt("ASS_PESSJUSWHO"),
                                    Timestamp.valueOf(resultado.getString("ASS_PESSJUSWHEN")));
                        }

                        listaJustificacoes.add(justificacao);
                    }
                    else
                    {
                        sql2.close();
                        sql.close();
                        SuportePersistenteOracle.getInstance().cancelarTransaccao();
                        return null;
                    }
                }
                sql2.close();
                sql.close();
            }
            catch (SQLException SQLe)
            {
                SuportePersistenteOracle.getInstance().cancelarTransaccao();
                System.out.println(
                    "JustificacaoRelacionalOracle.lerTodasJustificacoes: " + SQLe.toString());
                SQLe.printStackTrace();
                return null;
            }
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        }
        catch (Exception e)
        {
            System.out.println("JustificacaoRelacionalOracle.lerTodasJustificacoes: " + e.toString());
            e.printStackTrace();
        }
        return listaJustificacoes;
    } /* lerTodasJustificacoes */

    public ArrayList consultarJustificacoes(
        ArrayList listaChaveFuncionarios,
        ArrayList listaChaveParamJustificacoes,
        Date dataInicio,
        Date dataFim)
    {
        return null;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IJustificacaoPersistente#lerJustificacao(int)
	 */
    public Justificacao lerJustificacao(int codigoInterno)
    {
        return null;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IJustificacaoPersistente#apagarJustificacao(int)
	 */
    public boolean apagarJustificacao(int codigoInterno)
    {
        return false;
    } /* consultarJustificacoes */
}