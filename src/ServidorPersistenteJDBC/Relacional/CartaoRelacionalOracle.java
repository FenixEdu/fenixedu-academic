package ServidorPersistenteJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import Dominio.Cartao;
import ServidorPersistenteJDBC.ICartaoPersistente;
import ServidorPersistenteJDBC.SuportePersistenteOracle;

/**
 * @author Fernanda Quitério e Tania Pousão
 */
public class CartaoRelacionalOracle implements ICartaoPersistente {

    public boolean alterarCartao(Cartao cartao) {
        boolean resultado = false;
        return resultado;
    } /* alterarCartao */

    public boolean apagarCartao(int codigoInterno) {
        boolean resultado = false;
        return resultado;
    } /* apagarCartao */

    public boolean apagarCartaoPorNumero(int numCartao) {
        boolean resultado = false;
        return resultado;
    } /* apagarCartaoPorNumero */

    public boolean apagarTodosCartoes() {
        return false;
    }

    public Cartao cartaoAtribuido(int numCartao) {
        Cartao cartao = null;
        return cartao;
    } /* cartaoAtribuido */

    public List cartaoUtilizado(int numCartao, Timestamp dataInicio, Timestamp dataFim) {
        List lista = null;
        return lista;
    } /* cartaoUtilizado */

    public boolean escreverCartao(Cartao cartao) {
        boolean resultado = false;
        return resultado;
    } /* escreverCartao */

    public boolean escreverCartoes(List listaCartoes) {
        boolean resultado = false;
        return resultado;
    } /* escreverCartoes */

    public boolean existeCartao(int numCartao) {
        boolean resultado = false;
        return resultado;
    } /* existeCartao */

    public Cartao lerCartao(int codigoInterno) {
        Cartao cartao = null;
        return cartao;
    } /* lerCartao */

    public Cartao lerCartaoActualFuncionario(int chaveFuncionario) {
        Cartao cartao = null;
        return cartao;
    } /* lerCartaoActualFuncionario */

    public Cartao lerCartaoLivre() {
        Cartao cartao = null;
        return cartao;
    } /* lerCartaoLivre */

    public Cartao lerCartaoPorNumero(int numCartao) {
        Cartao cartao = null;
        return cartao;
    } /* lerCartaoPorNumero */

    public Cartao lerCartaoPorNumero(int numCartao, Timestamp dataMarcacao) {
        Cartao cartao = null;
        return cartao;
    } /* lerCartaoPorNumero */

    public Cartao lerCartaoSubstitutoFuncionario(int chaveFuncionario) {
        Cartao cartao = null;
        return cartao;
    } /* lerCartaoSubstitutoFuncionario */

    public Cartao lerCartaoSubstitutoFuncionario(int chaveFuncionario, Timestamp data) {
        Cartao cartao = null;
        return cartao;
    } /* lerCartaoSubstitutoFuncionario */

    public List lerCartoesFuncionario(int chaveFuncionario) {
        List lista = null;
        return lista;
    } /* lerCartoesFuncionario */

    public List lerCartoesFuncionarioComValidade(int chaveFuncionario, Timestamp dataInicio,
            Timestamp dataFim) {
        List lista = null;
        return lista;
    } /* lerCartoesFuncionarioComValidade */

    public List lerHistorialCartao(int numCartao) {
        List lista = null;
        return lista;
    } /* lerHistorialCartao */

    public List lerTodosCartoes() {
        //ORACLE: acede à BD do teleponto para carregar os cartões
        List listaCartoesTeleponto = null;

        try {
            SuportePersistenteOracle.getInstance().iniciarTransaccao();
            try {
                PreparedStatement sql = UtilRelacionalOracle
                        .prepararComando("SELECT CARSUB.CARTNO_CART, EMPREGADO.EMP_NUM, CARSUB.CARSUB_DHINI, "
                                + "CARSUB.CARSUB_DHFIM, CARSUB.CARSUB_WHO, CARSUB.CARSUB_WHEN FROM CARSUB, EMPREGADO "
                                + "WHERE CARSUB.ENT_NUM = EMPREGADO.ENT_NUM ORDER BY CARSUB.CARSUB_SEQ");

                ResultSet resultado = sql.executeQuery();
                listaCartoesTeleponto = new ArrayList();
                while (resultado.next()) {
                    listaCartoesTeleponto.add(new Cartao(resultado.getInt("CARTNO_CART"), resultado
                            .getInt("EMP_NUM"),
                    // só pode levar o numero de funcionario, e no servico vamos
                            // descobrir qual a chave
                            // do funcionario
                            Timestamp.valueOf(resultado.getString("CARSUB_DHINI")), Timestamp
                                    .valueOf(resultado.getString("CARSUB_DHFIM")), resultado
                                    .getInt("CARSUB_WHO"), Timestamp.valueOf(resultado
                                    .getString("CARSUB_WHEN"))));
                }
                sql.close();

            } catch (Exception e) {
                SuportePersistenteOracle.getInstance().cancelarTransaccao();
                System.out.println("CartaoRelacionalOracle.lerTodosCartoes: " + e.toString());
                return null;
            }
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        } catch (Exception e) {
            System.out.println("CartaoRelacionalOracle.lerTodosCartoes: " + e.toString());
            listaCartoesTeleponto = null;
        }
        return listaCartoesTeleponto;
    } /* lerTodosCartoes */

    public List lerTodosCartoesSubstitutos() {
        List lista = null;
        return lista;
    } /* lerTodosCartoesSubstitutos */

    public List lerTodosCartoesSubstitutosAtribuidos() {
        List lista = null;
        return lista;
    } /* lerTodosCartoesSubstitutosAtribuidos */

    public Cartao lerUltimaUtilizacaoCartao(int numCartao, Timestamp dataMarcacao) {
        Cartao cartao = null;
        return cartao;
    } /* lerUltimaUtilizacaoCartao */

    public int ultimoCodigoInterno() {
        return 0;
    } /* ultimoCodigoInterno */

    public Cartao lerCartaoSubstitutoFuncionario(int chaveFuncionario, Timestamp dataInicio,
            Timestamp dataFim) {
        return null;
    }

    public List consultarCartao(List listaFuncionarios, List listaCartoes, Timestamp dataInicio,
            Timestamp dataFim) {
        return null;
    }

    public List lerCartoesSubstitutosFuncionario(int chaveFuncionario, Timestamp dataInicio,
            Timestamp dataFim) {
        return null;
    }
}