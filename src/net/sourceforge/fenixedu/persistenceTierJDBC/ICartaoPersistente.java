package net.sourceforge.fenixedu.persistenceTierJDBC;

import java.sql.Timestamp;
import java.util.List;

import net.sourceforge.fenixedu.domain.Cartao;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public interface ICartaoPersistente {
    public boolean alterarCartao(Cartao cartao);

    public boolean apagarCartao(int codigoInterno);

    public boolean apagarCartaoPorNumero(int numCartao);

    public boolean apagarTodosCartoes();

    public Cartao cartaoAtribuido(int numCartao);

    public List cartaoUtilizado(int numCartao, Timestamp dataInicio, Timestamp dataFim);

    public List consultarCartao(List listaFuncionarios, List listaCartoes, Timestamp dataInicio,
            Timestamp dataFim);

    public boolean existeCartao(int numCartao);

    public boolean escreverCartao(Cartao cartao);

    public boolean escreverCartoes(List listaCartoes);

    public Cartao lerCartao(int codigoInterno);

    public Cartao lerCartaoActualFuncionario(int chaveFuncionario);

    public Cartao lerCartaoLivre();

    public Cartao lerCartaoPorNumero(int numCartao);

    public Cartao lerCartaoPorNumero(int numCartao, Timestamp dataMarcacao);

    public Cartao lerCartaoSubstitutoFuncionario(int chaveFuncionario);

    public Cartao lerCartaoSubstitutoFuncionario(int chaveFuncionario, Timestamp data);

    public Cartao lerCartaoSubstitutoFuncionario(int chaveFuncionario, Timestamp dataInicio,
            Timestamp dataFim);

    public List lerCartoesSubstitutosFuncionario(int chaveFuncionario, Timestamp dataInicio,
            Timestamp dataFim);

    public List lerCartoesFuncionario(int chaveFuncionario);

    public List lerCartoesFuncionarioComValidade(int chaveFuncionario, Timestamp dataInicio,
            Timestamp dataFim);

    public List lerHistorialCartao(int numCartao);

    public List lerTodosCartoes();

    public List lerTodosCartoesSubstitutos();

    public List lerTodosCartoesSubstitutosAtribuidos();

    public Cartao lerUltimaUtilizacaoCartao(int numCartao, Timestamp dataMarcacao);

    public int ultimoCodigoInterno();
}