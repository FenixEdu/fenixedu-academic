package ServidorPersistenteJDBC;

import java.sql.Timestamp;
import java.util.ArrayList;

import Dominio.Cartao;

/**
 *
 * @author  Fernanda Quitério e Tania Pousão
 */
public interface ICartaoPersistente {
  public boolean alterarCartao(Cartao cartao);
  public boolean apagarCartao(int codigoInterno);
  public boolean apagarCartaoPorNumero(int numCartao);
  public boolean apagarTodosCartoes();
  public Cartao cartaoAtribuido(int numCartao);
  public ArrayList cartaoUtilizado(int numCartao, Timestamp dataInicio, Timestamp dataFim);
  public ArrayList consultarCartao(ArrayList listaFuncionarios, ArrayList listaCartoes, Timestamp dataInicio, Timestamp dataFim);
  public boolean existeCartao(int numCartao);
  public boolean escreverCartao(Cartao cartao);
  public boolean escreverCartoes(ArrayList listaCartoes);
  public Cartao lerCartao(int codigoInterno);
  public Cartao lerCartaoActualFuncionario(int chaveFuncionario);
  public Cartao lerCartaoLivre();
  public Cartao lerCartaoPorNumero(int numCartao);
  public Cartao lerCartaoPorNumero(int numCartao, Timestamp dataMarcacao);
  public Cartao lerCartaoSubstitutoFuncionario(int chaveFuncionario);
	public Cartao lerCartaoSubstitutoFuncionario(int chaveFuncionario, Timestamp data);	
	public Cartao lerCartaoSubstitutoFuncionario(int chaveFuncionario, Timestamp dataInicio, Timestamp dataFim);
	public ArrayList lerCartoesSubstitutosFuncionario(int chaveFuncionario, Timestamp dataInicio, Timestamp dataFim);
  public ArrayList lerCartoesFuncionario(int chaveFuncionario);
  public ArrayList lerCartoesFuncionarioComValidade(int chaveFuncionario, Timestamp dataInicio, Timestamp dataFim);
  public ArrayList lerHistorialCartao(int numCartao);
  public ArrayList lerTodosCartoes();
  public ArrayList lerTodosCartoesSubstitutos();
  public ArrayList lerTodosCartoesSubstitutosAtribuidos();
  public Cartao lerUltimaUtilizacaoCartao(int numCartao, Timestamp dataMarcacao);
  public int ultimoCodigoInterno();
}