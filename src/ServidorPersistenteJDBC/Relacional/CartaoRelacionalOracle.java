package ServidorPersistenteJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import Dominio.Cartao;
import ServidorPersistenteJDBC.ICartaoPersistente;
import ServidorPersistenteJDBC.SuportePersistenteOracle;

/**
 * @author Fernanda Quitério e Tania Pousão
 */
public class CartaoRelacionalOracle implements ICartaoPersistente
{

    public boolean alterarCartao(Cartao cartao)
    {
        boolean resultado = false;
        return resultado;
    } /* alterarCartao */

    public boolean apagarCartao(int codigoInterno)
    {
        boolean resultado = false;
        return resultado;
    } /* apagarCartao */

    public boolean apagarCartaoPorNumero(int numCartao)
    {
        boolean resultado = false;
        return resultado;
    } /* apagarCartaoPorNumero */

    public boolean apagarTodosCartoes()
    {
        return false;
    }

    public Cartao cartaoAtribuido(int numCartao)
    {
        Cartao cartao = null;
        return cartao;
    } /* cartaoAtribuido */

    public ArrayList cartaoUtilizado(int numCartao, Timestamp dataInicio, Timestamp dataFim)
    {
        ArrayList lista = null;
        return lista;
    } /* cartaoUtilizado */

    public boolean escreverCartao(Cartao cartao)
    {
        boolean resultado = false;
        return resultado;
    } /* escreverCartao */

    public boolean escreverCartoes(ArrayList listaCartoes)
    {
        boolean resultado = false;
        return resultado;
    } /* escreverCartoes */

    public boolean existeCartao(int numCartao)
    {
        boolean resultado = false;
        return resultado;
    } /* existeCartao */

    public Cartao lerCartao(int codigoInterno)
    {
        Cartao cartao = null;
        return cartao;
    } /* lerCartao */

    public Cartao lerCartaoActualFuncionario(int chaveFuncionario)
    {
        Cartao cartao = null;
        return cartao;
    } /* lerCartaoActualFuncionario */

    public Cartao lerCartaoLivre()
    {
        Cartao cartao = null;
        return cartao;
    } /* lerCartaoLivre */

    public Cartao lerCartaoPorNumero(int numCartao)
    {
        Cartao cartao = null;
        return cartao;
    } /* lerCartaoPorNumero */

    public Cartao lerCartaoPorNumero(int numCartao, Timestamp dataMarcacao)
    {
        Cartao cartao = null;
        return cartao;
    } /* lerCartaoPorNumero */

    public Cartao lerCartaoSubstitutoFuncionario(int chaveFuncionario)
    {
        Cartao cartao = null;
        return cartao;
    } /* lerCartaoSubstitutoFuncionario */

    public Cartao lerCartaoSubstitutoFuncionario(int chaveFuncionario, Timestamp data)
    {
        Cartao cartao = null;
        return cartao;
    } /* lerCartaoSubstitutoFuncionario */

    public ArrayList lerCartoesFuncionario(int chaveFuncionario)
    {
        ArrayList lista = null;
        return lista;
    } /* lerCartoesFuncionario */

    public ArrayList lerCartoesFuncionarioComValidade(
        int chaveFuncionario,
        Timestamp dataInicio,
        Timestamp dataFim)
    {
        ArrayList lista = null;
        return lista;
    } /* lerCartoesFuncionarioComValidade */

    public ArrayList lerHistorialCartao(int numCartao)
    {
        ArrayList lista = null;
        return lista;
    } /* lerHistorialCartao */

    public ArrayList lerTodosCartoes()
    {
        //ORACLE: acede à BD do teleponto para carregar os cartões
        ArrayList listaCartoesTeleponto = null;

        try
        {
            SuportePersistenteOracle.getInstance().iniciarTransaccao();
            try
            {
                PreparedStatement sql =
                    UtilRelacionalOracle.prepararComando(
                        "SELECT CARSUB.CARTNO_CART, EMPREGADO.EMP_NUM, CARSUB.CARSUB_DHINI, "
                            + "CARSUB.CARSUB_DHFIM, CARSUB.CARSUB_WHO, CARSUB.CARSUB_WHEN FROM CARSUB, EMPREGADO "
                            + "WHERE CARSUB.ENT_NUM = EMPREGADO.ENT_NUM ORDER BY CARSUB.CARSUB_SEQ");

                ResultSet resultado = sql.executeQuery();
                listaCartoesTeleponto = new ArrayList();
                while (resultado.next())
                {
                    listaCartoesTeleponto
                        .add(new Cartao(resultado.getInt("CARTNO_CART"), resultado.getInt("EMP_NUM"),
                    // só pode levar o numero de funcionario, e no servico vamos descobrir qual a chave
					// do funcionario
                    Timestamp.valueOf(resultado.getString("CARSUB_DHINI")),
                        Timestamp.valueOf(resultado.getString("CARSUB_DHFIM")),
                        resultado.getInt("CARSUB_WHO"),
                        Timestamp.valueOf(resultado.getString("CARSUB_WHEN"))));
                }
                sql.close();

            }
            catch (Exception e)
            {
                SuportePersistenteOracle.getInstance().cancelarTransaccao();
                System.out.println("CartaoRelacionalOracle.lerTodosCartoes: " + e.toString());
                return null;
            }
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        }
        catch (Exception e)
        {
            System.out.println("CartaoRelacionalOracle.lerTodosCartoes: " + e.toString());
            listaCartoesTeleponto = null;
        }
        return listaCartoesTeleponto;
    } /* lerTodosCartoes */

    public ArrayList lerTodosCartoesSubstitutos()
    {
        ArrayList lista = null;
        return lista;
    } /* lerTodosCartoesSubstitutos */

    public ArrayList lerTodosCartoesSubstitutosAtribuidos()
    {
        ArrayList lista = null;
        return lista;
    } /* lerTodosCartoesSubstitutosAtribuidos */

    public Cartao lerUltimaUtilizacaoCartao(int numCartao, Timestamp dataMarcacao)
    {
        Cartao cartao = null;
        return cartao;
    } /* lerUltimaUtilizacaoCartao */

    public int ultimoCodigoInterno()
    {
        return 0;
    } /* ultimoCodigoInterno */

    public Cartao lerCartaoSubstitutoFuncionario(
        int chaveFuncionario,
        Timestamp dataInicio,
        Timestamp dataFim)
    {
        return null;
    }

    public ArrayList consultarCartao(
        ArrayList listaFuncionarios,
        ArrayList listaCartoes,
        Timestamp dataInicio,
        Timestamp dataFim)
    {
        return null;
    }

    public ArrayList lerCartoesSubstitutosFuncionario(
        int chaveFuncionario,
        Timestamp dataInicio,
        Timestamp dataFim)
    {
        return null;
    }
}