package net.sourceforge.fenixedu.persistenceTierJDBC;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.domain.Horario;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public interface IHorarioPersistente {
    public boolean alterarHorario(Horario horario);

    public boolean alterarExcepcaoHorario(Horario horario);

    public boolean alterarDataFimHorario(Date dataFim, int numeroMecanografico);

    public boolean apagarHorario(int codigoInterno);

    public boolean apagarHorarioExcepcao(int codigoInterno);

    public boolean apagarTodosHorarios();

    public boolean associarHorarioRegime(int chaveHorario, int chaveRegime);

    public boolean associarExcepcaoHorarioRegime(int chaveHorario, int regime);

    public int calcularIndiceRotacao(int numDiasRotacao, Date dataInicioHorario, Date dataConsulta);

    public int calcularIndiceDescanso(int indiceRotacao);

    public int calculaIndiceReferenciaDescanso(int indiceRotacao, int indiceDescanso, int numDiasRotacao);

    public boolean desassociarHorarioRegime(int codigoInternoHorario, int codigoInternoRegime);

    public boolean desassociarHorarioExcepcaoRegime(int codigoInternoHorario, int codigoInternoRegime);

    public boolean escreverExcepcaoHorario(Horario horario);

    public boolean escreverHorario(Horario horario);

    public boolean escreverRotacoes(List rotacaoHorario);

    public Horario existeExcepcaoHorario(Horario horario);

    public Horario existeHorario(Horario horario);

    public Horario lerExcepcaoHorario(int codigoInterno);

    public Horario lerExcepcaoHorarioPorNumMecanografico(int numMecanografico, Timestamp dataConsulta);

    public List lerExcepcoesHorarioPorNumMecanografico(int numMecanografico);

    public List lerExcepcoesHorarioPorNumMecanografico(int numMecanografico, Date dataInicio,
            Date dataFim);

    public List lerHistoricoHorarioPorNumMecanografico(int numMecanografico);

    public Horario lerHorario(int codigoInterno);

    public Horario lerHorario(int codigoInterno, Timestamp dataConsulta);

    public List lerHorarioActualPorNumMecanografico(int numMecanografico);

    public Horario lerHorarioPorNumFuncionario(int numMecanografico, Timestamp dataConsulta);

    public List lerHorariosPorNumMecanografico(int numMecanografico, Date dataInicio, Date dataFim);

    public Horario lerHorarioPorFuncionario(int chaveFuncNaoDocente);

    public List lerHorariosSemFimValidade(int numMecanografico);

    public List lerRegimes(int chaveHorario);

    public List lerRegimesHorarioExcepcao(int chaveHorario);

    public HashMap lerRegimesRotacoes(List rotacaoHorario);

    public List lerRotacoesPorNumMecanografico(int numMecanografico);

    public List lerTodosHorariosExcepcao(Date dataInicio, Date dataFim);

    public List lerTodosHorarios(Date dataInicio, Date dataFim);

    public List lerTodosHorariosExcepcaoComRegime(int chaveRegime, List listaHorariosTipoComRegime,
            Date dataInicio, Date dataFim);

    public List lerTodosHorariosComRegime(int chaveRegime, List listaHorariosTipoComRegime,
            Date dataInicio, Date dataFim);

    public int ultimoCodigoInterno();

    public int ultimoCodigoInternoExcepcaoHorario();
}