package net.sourceforge.fenixedu.persistenceTierJDBC;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.Justificacao;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public interface IJustificacaoPersistente {
    public boolean alterarJustificacao(Justificacao justificacao);

    public boolean apagarJustificacao(int codigoInterno);

    public boolean apagarJustificacao(int chaveFuncionario, java.util.Date diaInicio, Time horaInicio,
            java.util.Date diaFim, Time horaFim);

    public boolean apagarTodasJustificacoes();

    public List consultarJustificacoes(List listaChaveFuncionarios, List listaChaveParamJustificacoes,
            Date dataInicio, Date dataFim);

    public boolean escreverJustificacao(Justificacao justificacao);

    public boolean escreverJustificacoes(List listaJustificacoes);

    public boolean existeJustificacao(Justificacao justificacao);

    public Justificacao lerJustificacao(int codigoInterno);

    public Justificacao lerJustificacao(int chaveFuncionario, Date diaInicio, Time horaInicio,
            Date diaFim, Time horaFim);

    public List lerJustificacoes(int chaveFuncionario, Date data);

    public List lerJustificacoesHoras(int chaveFuncionario, Timestamp data);

    public List lerJustificacoesHoras(int chaveFuncionario, Timestamp data, Time horaInicio, Time horaFim);

    public List lerJustificacoesOcorrencia(int chaveFuncionario, Timestamp data);

    public List lerJustificacoesFuncionarioComValidade(int chaveFuncionario, Date diaInicio, Date diaFim);

    public List lerTodasJustificacoes();
}