package ServidorPersistenteJDBC;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import Dominio.Justificacao;

/**
 *
 * @author  Fernanda Quitério e Tania Pousão
 */
public interface IJustificacaoPersistente {
  public boolean alterarJustificacao(Justificacao justificacao);
  public boolean apagarJustificacao(int chaveFuncionario, java.util.Date diaInicio, Time horaInicio, java.util.Date diaFim, Time horaFim);
  public boolean apagarTodasJustificacoes();
	public ArrayList consultarJustificacoes(ArrayList listaChaveFuncionarios, ArrayList listaChaveParamJustificacoes, Date dataInicio, Date dataFim);
  public boolean escreverJustificacao(Justificacao justificacao);
  public boolean escreverJustificacoes(ArrayList listaJustificacoes);
  public boolean existeJustificacao(Justificacao justificacao);
  public Justificacao lerJustificacao(int chaveFuncionario, Date diaInicio, Time horaInicio, Date diaFim, Time horaFim);
  public ArrayList lerJustificacoes(int chaveFuncionario, Date data);
  public ArrayList lerJustificacoesHoras(int chaveFuncionario, Timestamp data);
	public ArrayList lerJustificacoesHoras(int chaveFuncionario, Timestamp data, Time horaInicio, Time horaFim);
	public ArrayList lerJustificacoesOcorrencia(int chaveFuncionario, Timestamp data);
  public ArrayList lerJustificacoesFuncionarioComValidade(int chaveFuncionario, Date diaInicio, Date diaFim);
	public ArrayList lerTodasJustificacoes();
}