package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.ListIterator;
import java.util.Locale;

import net.sourceforge.fenixedu.applicationTier.ServicoAutorizacao;
import net.sourceforge.fenixedu.applicationTier.ServicoSeguro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.constants.assiduousness.Constants;
import net.sourceforge.fenixedu.domain.Funcionario;
import net.sourceforge.fenixedu.domain.Horario;
import net.sourceforge.fenixedu.domain.IStatusAssiduidade;
import net.sourceforge.fenixedu.domain.IStrategyHorarios;
import net.sourceforge.fenixedu.domain.IStrategyJustificacoes;
import net.sourceforge.fenixedu.domain.Justificacao;
import net.sourceforge.fenixedu.domain.MarcacaoPonto;
import net.sourceforge.fenixedu.domain.ParamJustificacao;
import net.sourceforge.fenixedu.domain.SuporteStrategyHorarios;
import net.sourceforge.fenixedu.domain.SuporteStrategyJustificacoes;
import net.sourceforge.fenixedu.persistenceTierJDBC.IFeriadoPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.SuportePersistente;
import net.sourceforge.fenixedu.util.Comparador;
import net.sourceforge.fenixedu.util.FormataCalendar;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;



/**
 * @author Fernanda Quitério & Tania Pousão
 */
public class ServicoSeguroConsultarVerbete extends ServicoSeguro {

    private Integer _numMecanografico;

    private Timestamp _dataInicioEscolha = null;

    private Timestamp _dataFimEscolha = null;

    private Boolean _oracleDB = null;

    private Locale _locale = null;

    private ArrayList _listaFuncionarios = new ArrayList();

    private ArrayList _listaEstados = new ArrayList();

    private ArrayList _listaCartoes = null;

    private ArrayList _listaRegimes = null;

    private ArrayList _listaMarcacoesPonto = null;

    private ArrayList _listaSaldos = new ArrayList();

    private ArrayList _listaJustificacoes = null;

    private ArrayList _listaParamJustificacoes = null;

    private Horario _horario = null;

    private long _saldoPrimEscalao = 0;

    private long _saldoSegEscalao = 0;

    private long _saldoDepoisSegEscalao = 0;

    private long _saldoNegativo = 0;

    private long _saldoNocturno = 0;

    private long _saldoNoctPrimEscalao = 0;

    private long _saldoNoctSegEscalao = 0;

    private long _saldoNoctDepoisSegEscalao = 0;

    private long _saldoHNFinal = 0;

    private long _saldoPFFinal = 0;

    private long _saldoDSC = 0;

    private long _saldoDS = 0;

    private long _saldoFeriado = 0;

    private int _numRefeicoesNocturno = 0;

    private Timestamp _dataConsulta = null;

    private Calendar _calendarioConsulta = null;

    private Timestamp _dataInicio = null;

    private Timestamp _dataFim = null;

    private Funcionario _funcionario = null;

    private boolean _mesInjustificado = false;

    private boolean _diasInjustificados = false;

    private boolean _statusPendente = false;

    private ArrayList _listaVerbeteDiaria = new ArrayList();

    private ArrayList _listaVerbeteBody = new ArrayList();

    public ServicoSeguroConsultarVerbete(
            ServicoAutorizacao servicoAutorizacaoLer, Integer numMecanografico,
            Timestamp dataInicioEscolha, Timestamp dataFimEscolha,
            Boolean oracleDB, Locale locale) {
        super(servicoAutorizacaoLer);
        _numMecanografico = numMecanografico;
        _dataInicioEscolha = dataInicioEscolha;
        _dataFimEscolha = dataFimEscolha;
        _oracleDB = oracleDB;
        _locale = locale;

        _dataConsulta = new Timestamp(_dataInicioEscolha.getTime());
    }

    public void execute() throws NotExecuteException {

        limpaListaSaldos(0);

        lerFuncionario();
        lerStatusAssiduidade();
        lerFimAssiduidade();
        lerInicioAssiduidade();
        construirEscolhasMarcacoesPonto();
        verificarMesInjustificado();

        if (!_mesInjustificado) {
            Calendar agora = Calendar.getInstance();
            agora.set(Calendar.HOUR_OF_DAY, 00);
            agora.set(Calendar.MINUTE, 00);
            agora.set(Calendar.SECOND, 00);
            agora.set(Calendar.MILLISECOND, 00);
            Timestamp agoraTimestamp = new Timestamp(agora.getTimeInMillis());

            _calendarioConsulta = Calendar.getInstance();
            _calendarioConsulta.setLenient(false);

            Calendar calendario = Calendar.getInstance();
            calendario.setLenient(false);

            //			limpaListaSaldos(0);

            //==================================== Inicio da construcao da
            // lista a mostrar no jsp =======================================
            while (_dataConsulta.before(_dataFimEscolha)) {
                calendario.clear();
                calendario.setTimeInMillis(_dataConsulta.getTime());

                // consulta do horario do funcionario neste dia
                lerHorario();

                //Se o status do funcionário é Pendente então não vale a pena
                // continuar os cálculos
                verificarStatusAssiduidade();
                if (_statusPendente) {
                    _statusPendente = false;
                    continue;
                }

                //Se os dias do funcionário são injustificados então não vale a
                // pena continuar os cálculos
                verificarDiasInjustificados(_funcionario,calendario);
                if (_diasInjustificados) {
                    _diasInjustificados = false;
                    continue;
                }

                //consulta das marcacoes do funcionario neste dia, tendo
                // atencao ao expediente deste horario
                calcularIntervaloConsulta();
                consultarMarcacoesPonto();

                //consulta das justificacoes do funcionario neste dia
                consultarJustificacoesPorDia();
                buscarParamJustificacoes();

                //formata o verbete
                _listaVerbeteDiaria.add(0, FormataCalendar.data(calendario)); //data
                _listaVerbeteDiaria.add(1, "&nbsp;"
                        + _horario.preencheSigla(_locale) + "&nbsp;"); //horário
                _listaVerbeteDiaria.add(2, new String("&nbsp;")); // saldo HN
                _listaVerbeteDiaria.add(3, new String("&nbsp;")); // saldo PF
                _listaVerbeteDiaria.add(4, formataJustificacoes()); //justificacoes
                _listaVerbeteDiaria.add(5, formataMarcacoes()); // marcacoes

                if ((_horario.getSigla() != null)
                        && (_horario.getSigla().equals(Constants.DSC)
                                || _horario.getSigla().equals(Constants.DS) || _horario
                                .getSigla().equals(Constants.FERIADO))) {
                    //Dias de Descanso com marcacoes e sem justificacoes, logo
                    // calcula o saldo

                    //calculo do saldo de dias de descanso
                    if (_listaMarcacoesPonto.size() > 0) {

                        //calculo do saldo diário
                        calcularSaldoDiario();

                        calendario.clear();
                        calendario.setTimeInMillis(((Long) _listaSaldos.get(0))
                                .longValue());
                        _listaVerbeteDiaria.set(2, FormataCalendar
                                .horasMinutosDuracao(calendario)); //saldo HN

                        IStrategyHorarios horarioStrategy = SuporteStrategyHorarios
                                .getInstance().callStrategy(
                                        _horario.getModalidade());
                        if (((Long) _listaSaldos.get(0)).longValue() > horarioStrategy
                                .duracaoDiaria(_horario)) {
                            _listaSaldos.set(0, new Long(horarioStrategy
                                    .duracaoDiaria(_horario)));
                        }

                        // para diferenciar entre feriados ao sabado e ao
                        // domingo
                        calendario.clear();
                        calendario.setTimeInMillis(_dataConsulta.getTime());

                        if (_horario.getSigla().equals(Constants.DSC)) {
                            // saldo de sabado
                            _saldoDSC = _saldoDSC
                                    + ((Long) _listaSaldos.get(0)).longValue();
                        } else if (_horario.getSigla().equals(Constants.DSC)
                                || (_horario.getSigla().equals(
                                        Constants.FERIADO) && calendario
                                        .get(Calendar.DAY_OF_MONTH) != Calendar.SUNDAY)) {
                            // saldo de feriado ao sabado ou dias de semana
                            _saldoFeriado = _saldoFeriado
                                    + ((Long) _listaSaldos.get(0)).longValue();
                        } else {
                            // saldo de domingo e saldo de feriado ao domingo
                            _saldoDS = _saldoDS
                                    + ((Long) _listaSaldos.get(0)).longValue();
                        }
                        //ANTES
                        //						if (_horario.getSigla().equals(Constants.DSC)
                        //							|| (_horario.getSigla().equals(Constants.FERIADO)
                        //								&& calendario.get(Calendar.DAY_OF_MONTH) !=
                        // Calendar.SUNDAY)) {
                        //							// saldo de sabado e feriado ao sabado ou dias de
                        // semana
                        //							_saldoDSC = _saldoDSC + ((Long)
                        // _listaSaldos.get(0)).longValue();
                        //						} else {
                        //							// saldo de domingo e saldo de feriado ao domingo
                        //							_saldoDS = _saldoDS + ((Long)
                        // _listaSaldos.get(0)).longValue();
                        //						}
                    }
                } else { //Dias Uteis
                    if (_listaParamJustificacoes.size() > 0) {
                        // acrescenta as justificacoes na lista de marcacoes de
                        // ponto como se fossem marcacoes de ponto
                        completaListaMarcacoes();
                    }

                    // calculo do saldo diário
                    calcularSaldoDiario();

                    // nao efectua calculos de saldo para dias além do dia de
                    // hoje
                    if (!_dataConsulta.after(agoraTimestamp)) {
                        IStrategyHorarios horarioStrategy = SuporteStrategyHorarios
                                .getInstance().callStrategy(
                                        _horario.getModalidade());
                        horarioStrategy.setSaldosHorarioVerbeteBody(_horario,
                                _listaRegimes, _listaParamJustificacoes,
                                _listaMarcacoesPonto, _listaSaldos);
                    }
                    // actualiza o saldo
                    if (_listaParamJustificacoes.size() > 0) {
                        ListIterator iterJustificacoes = _listaJustificacoes
                                .listIterator();

                        ParamJustificacao paramJustificacao = null;
                        Justificacao justificacao = null;
                        while (iterJustificacoes.hasNext()) {
                            justificacao = (Justificacao) iterJustificacoes
                                    .next();
                            paramJustificacao = (ParamJustificacao) _listaParamJustificacoes
                                    .get(iterJustificacoes.previousIndex());

                            //actualiza saldo diario
                            IStrategyJustificacoes justificacaoStrategy = SuporteStrategyJustificacoes
                                    .getInstance().callStrategy(
                                            paramJustificacao.getTipo());
                            justificacaoStrategy
                                    .updateSaldosHorarioVerbeteBody(
                                            justificacao, paramJustificacao,
                                            _horario, _listaRegimes,
                                            _listaMarcacoesPonto, _listaSaldos);
                        }
                    }

                    if (_dataConsulta.before(agoraTimestamp)) {
                        //verifica se este dia nao teve assiduidade
                        if (_listaMarcacoesPonto.size() == 0
                                && _listaParamJustificacoes.size() == 0
                                && (!_dataConsulta.after(agoraTimestamp))) {
                            _listaVerbeteDiaria.set(4, new String("<b>"
                                    + Constants.INJUSTIFICADO + "</b>"));
                        }

                        //saldo global dos dias de consulta não conta com o dia
                        // de hoje pois ainda não terminou
                        _saldoHNFinal = _saldoHNFinal
                                + ((Long) _listaSaldos.get(0)).longValue();
                        _saldoPFFinal = _saldoPFFinal
                                + ((Long) _listaSaldos.get(1)).longValue();
                        // calculo das horas extraordinarias diurnas e nocturnas
                        calcularHorasEscalao();
                    }
                    calendario.clear();
                    calendario.setTimeInMillis(((Long) _listaSaldos.get(0))
                            .longValue());
                    _listaVerbeteDiaria.set(2, FormataCalendar
                            .horasMinutosDuracao(calendario)); //saldo HN
                    calendario.clear();
                    calendario.setTimeInMillis(((Long) _listaSaldos.get(1))
                            .longValue());
                    _listaVerbeteDiaria.set(3, FormataCalendar
                            .horasMinutosDuracao(calendario)); //saldo PF
                }
                // aumenta um dia no intervalo de consulta do verbete
                calendario.clear();
                calendario.setTimeInMillis(_dataConsulta.getTime());
                calendario.add(Calendar.DAY_OF_MONTH, +1);
                _dataConsulta.setTime(calendario.getTimeInMillis());

                //junta a lista diaria à lista do verbete
                _listaVerbeteBody.addAll(_listaVerbeteDiaria);
                _listaVerbeteDiaria.clear();
            }
            descontaSaldoNegativo();
        }
        _listaSaldos.set(0, new Long(_saldoHNFinal));
        _listaSaldos.set(1, new Long(_saldoPFFinal));
        _listaSaldos.set(2, new Long(_saldoPrimEscalao));
        _listaSaldos.set(3, new Long(_saldoSegEscalao));
        _listaSaldos.set(4, new Long(_saldoDepoisSegEscalao));
        _listaSaldos.set(5, new Long(_saldoDSC));
        _listaSaldos.set(6, new Long(_saldoDS));
        _listaSaldos.set(7, new Long(_saldoNocturno));
        _listaSaldos.set(8, new Long(_saldoNoctPrimEscalao));
        _listaSaldos.set(9, new Long(_saldoNoctSegEscalao));
        _listaSaldos.set(10, new Long(_saldoNoctDepoisSegEscalao));
        _listaSaldos.set(11, new Long(_saldoFeriado));
        _listaSaldos.set(12, new Integer(_numRefeicoesNocturno));
    }

    /**
     * @throws NotExecuteException
     * 
     */
    private void lerFuncionario() throws NotExecuteException {
        ServicoAutorizacao servicoAutorizacao = new ServicoAutorizacao();
        ServicoSeguroLerFuncionarioPorDia servicoSeguroLerFuncionarioPorDia = new ServicoSeguroLerFuncionarioPorDia(
                servicoAutorizacao, _numMecanografico.intValue(), _dataConsulta);
        servicoSeguroLerFuncionarioPorDia.execute();

        _funcionario = servicoSeguroLerFuncionarioPorDia.getFuncionario();
    }

    private void lerStatusAssiduidade() throws NotExecuteException {
        ServicoAutorizacao servicoAutorizacao = new ServicoAutorizacao();

        ServicoSeguroLerStatusAssiduidadeFuncionario servicoSeguroLerStatusAssiduidadeFuncionario = new ServicoSeguroLerStatusAssiduidadeFuncionario(
                servicoAutorizacao, _numMecanografico.intValue(),
                _dataInicioEscolha, _dataFimEscolha);
        servicoSeguroLerStatusAssiduidadeFuncionario.execute();

        if (servicoSeguroLerStatusAssiduidadeFuncionario
                .getListaStatusAssiduidade() != null) {
            if (!servicoSeguroLerStatusAssiduidadeFuncionario
                    .getListaEstadosStatusAssiduidade().contains(
                            Constants.ASSIDUIDADE_ACTIVO)) { throw new NotExecuteException(
                    "error.assiduidade.naoExiste"); }
        }
    } /* lerStatusAssiduidade */

    private void lerFimAssiduidade() throws NotExecuteException {
        ServicoAutorizacao servicoAutorizacao = new ServicoAutorizacao();

        ServicoSeguroLerFimAssiduidade servicoSeguroLerFimAssiduidade = new ServicoSeguroLerFimAssiduidade(
                servicoAutorizacao, _numMecanografico.intValue(),
                _dataInicioEscolha, _dataFimEscolha);
        servicoSeguroLerFimAssiduidade.execute();

        if (servicoSeguroLerFimAssiduidade.getDataAssiduidade() != null) {
            _dataFimEscolha = new Timestamp(servicoSeguroLerFimAssiduidade
                    .getDataAssiduidade().getTime());
        }
    } /* lerFimAssiduidade */

    private void lerInicioAssiduidade() throws NotExecuteException {
        ServicoAutorizacao servicoAutorizacao = new ServicoAutorizacao();

        ServicoSeguroLerInicioAssiduidade servicoSeguroLerInicioAssiduidade = new ServicoSeguroLerInicioAssiduidade(
                servicoAutorizacao, _numMecanografico.intValue(),
                _dataInicioEscolha, _dataFimEscolha);
        servicoSeguroLerInicioAssiduidade.execute();

        if (servicoSeguroLerInicioAssiduidade.getDataAssiduidade() != null) {
            _dataConsulta = new Timestamp(servicoSeguroLerInicioAssiduidade
                    .getDataAssiduidade().getTime());
            _dataInicioEscolha = servicoSeguroLerInicioAssiduidade
                    .getDataAssiduidade();
        }
    } /* lerInicioAssiduidade */

    private void construirEscolhasMarcacoesPonto() throws NotExecuteException {
        _listaFuncionarios.add(_numMecanografico);

        ServicoAutorizacaoLer servicoAutorizacao = new ServicoAutorizacaoLer();
        ServicoSeguroConstruirEscolhasMarcacoesPonto servicoSeguro = new ServicoSeguroConstruirEscolhasMarcacoesPonto(
                servicoAutorizacao, _listaFuncionarios, _listaCartoes,
                _dataInicioEscolha, _dataFimEscolha);
        servicoSeguro.execute();
        _listaCartoes = (ArrayList) servicoSeguro.getListaCartoes();

    } /* construirEscolhasMarcacoesPonto */

    private void verificarMesInjustificado() throws NotExecuteException {
        Calendar agora = Calendar.getInstance();
        agora.set(Calendar.HOUR_OF_DAY, 23);
        agora.set(Calendar.MINUTE, 59);
        agora.set(Calendar.SECOND, 59);
        agora.set(Calendar.MILLISECOND, 00);
        Timestamp agoraTimestamp = new Timestamp(agora.getTimeInMillis());

        Calendar calendarioInicio = Calendar.getInstance();
        calendarioInicio.setLenient(false);
        calendarioInicio.setTimeInMillis(_dataInicioEscolha.getTime());

        Calendar calendarioFim = Calendar.getInstance();
        calendarioFim.setLenient(false);
        calendarioFim.setTimeInMillis(_dataFimEscolha.getTime());

        // se estivermos dentro do mesmo mes e o fim de consulta nao for o dia
        // de hoje
        if (calendarioInicio.get(Calendar.MONTH) == calendarioFim
                .get(Calendar.MONTH)
                && _dataFimEscolha.before(agoraTimestamp)) {
            Calendar mes = Calendar.getInstance();
            mes.setLenient(false);
            mes.clear();
            mes.set(Calendar.MONTH, calendarioInicio.get(Calendar.MONTH));

            // verificar se a consulta abrange todo o mes
            if ((calendarioInicio.get(Calendar.DAY_OF_MONTH) == mes
                    .getActualMinimum(Calendar.DAY_OF_MONTH))
                    && (calendarioFim.get(Calendar.DAY_OF_MONTH) == mes
                            .getActualMaximum(Calendar.DAY_OF_MONTH))) {

                if (!verAssiduidade(_dataInicioEscolha, _dataFimEscolha)) {
                    // o funcionario faltou o mes todo, logo tem que se avisar
                    // no verbete
                    _mesInjustificado = true;

                    construirListaDiasInjustificados(_dataFimEscolha);
                }
            }
        }
    } /* verificarMesInjustificado */

    private boolean verAssiduidade(Timestamp dataInicio, Timestamp dataFim)
            throws NotExecuteException {

        Calendar calendarioInicio = Calendar.getInstance();
        calendarioInicio.setLenient(false);
        calendarioInicio.setTimeInMillis(dataInicio.getTime());
        Calendar calendarioFim = Calendar.getInstance();
        calendarioFim.setLenient(false);
        calendarioFim.setTimeInMillis(dataFim.getTime());
        ServicoAutorizacao servicoAutorizacao = new ServicoAutorizacao();

        ServicoSeguroLerMarcacoesPonto servicoSeguroLerMarcacoesPonto = new ServicoSeguroLerMarcacoesPonto(
                servicoAutorizacao, _listaFuncionarios, _listaCartoes, null,
                new Timestamp(calendarioInicio.getTimeInMillis()),
                new Timestamp(calendarioFim.getTimeInMillis()), _oracleDB);
        servicoSeguroLerMarcacoesPonto.execute();
        ArrayList lista = servicoSeguroLerMarcacoesPonto
                .getListaMarcacoesPonto();

        if ((lista == null) || (lista.size() <= 0)) {
            ServicoSeguroLerJustificacoesComValidade servicoSeguroLerJustificacoes = new ServicoSeguroLerJustificacoesComValidade(
                    servicoAutorizacao, _numMecanografico.intValue(), new Date(
                            calendarioInicio.getTimeInMillis()), new Date(
                            calendarioFim.getTimeInMillis()));
            servicoSeguroLerJustificacoes.execute();

            lista = (ArrayList) servicoSeguroLerJustificacoes.getListaJustificacoes();
            if ((lista == null) || (lista.size() <= 0)) {
            // o funcionario nao teve assiduidade neste intervalo de datas
            return false; }
        }
        return true;
    } /* verAssiduidade */

    private void construirListaDiasInjustificados(Timestamp dataFim)
            throws NotExecuteException {
        Calendar calendario = Calendar.getInstance();
        calendario.setLenient(false);
        while (_dataConsulta.before(dataFim)) {
            calendario.clear();
            calendario.setTimeInMillis(_dataConsulta.getTime());
            _listaVerbeteDiaria.add(0, FormataCalendar.data(calendario));

            // consulta do horario do funcionario numa determinada data
            try {
                lerHorario();
            } catch (NotExecuteException nee) {
                throw new NotExecuteException(nee.getMessage());
            }

            _listaVerbeteDiaria.add(1, "&nbsp;"
                    + _horario.preencheSigla(_locale) + "&nbsp;");
            _listaVerbeteDiaria.add(2, new String("&nbsp;"));
            _listaVerbeteDiaria.add(3, new String("&nbsp;"));

            _listaJustificacoes = new ArrayList();
            _listaParamJustificacoes = new ArrayList();
            _listaVerbeteDiaria.add(4, new String("<b>"
                    + Constants.INJUSTIFICADO + "</b>"));

            _listaMarcacoesPonto = new ArrayList();
            _listaVerbeteDiaria.add(5, formataMarcacoes());
            //calcula o saldo diario de trabalho

            if (!((_horario.getSigla() != null) && (_horario.getSigla().equals(
                    Constants.DSC)
                    || _horario.getSigla().equals(Constants.DS) || _horario
                    .getSigla().equals(Constants.FERIADO)))) {
                IStrategyHorarios horarioStrategy = SuporteStrategyHorarios
                        .getInstance().callStrategy(_horario.getModalidade());
                horarioStrategy.setSaldosHorarioVerbeteBody(_horario,
                        _listaRegimes, _listaParamJustificacoes,
                        _listaMarcacoesPonto, _listaSaldos);

                calendario.clear();
                calendario.setTimeInMillis(((Long) _listaSaldos.get(0))
                        .longValue());
                _listaVerbeteDiaria.set(2, FormataCalendar
                        .horasMinutosDuracao(calendario));
                calendario.clear();
                calendario.setTimeInMillis(((Long) _listaSaldos.get(1))
                        .longValue());
                _listaVerbeteDiaria.set(3, FormataCalendar
                        .horasMinutosDuracao(calendario));

                _saldoHNFinal = _saldoHNFinal
                        + ((Long) _listaSaldos.get(0)).longValue();
                _saldoPFFinal = _saldoPFFinal
                        + ((Long) _listaSaldos.get(1)).longValue();
            }
            // diminui um dia no intervalo de consulta do verbete
            calendario.clear();
            calendario.setTimeInMillis(_dataConsulta.getTime());
            calendario.add(Calendar.DAY_OF_MONTH, +1);
            _dataConsulta.setTime(calendario.getTimeInMillis());

            //junta a lista diária à lista verbete
            _listaVerbeteBody.addAll(_listaVerbeteDiaria);
            _listaVerbeteDiaria.clear();
            limpaListaSaldos(0);
        }
    } /* construirListaDiasInjustificados */

    private void lerHorario() throws NotExecuteException {

        ServicoAutorizacao servicoAutorizacao = new ServicoAutorizacao();
        ServicoSeguroLerHorario servicoSeguro = new ServicoSeguroLerHorario(
                servicoAutorizacao, _numMecanografico.intValue(), _dataConsulta);

        servicoSeguro.execute();
        _horario = servicoSeguro.getHorario();
        _listaRegimes = (ArrayList) servicoSeguro.getListaRegimes();
    } /* lerHorario */

    /*
     * Verifica se o status do funcionário é PENDENTE Caso seja escreve no
     * verbete até deixar de ser
     */
    private void verificarStatusAssiduidade() throws NotExecuteException {
        Calendar calendarioFim = Calendar.getInstance();
        calendarioFim.setLenient(false);
        calendarioFim.setTimeInMillis(_dataConsulta.getTime());

        // verifica se neste dia tem status pendente
        ServicoAutorizacao servicoAutorizacao = new ServicoAutorizacao();
        ServicoSeguroLerStatusAssiduidadeFuncionario servicoSeguroLerStatusAssiduidadeFuncionario = new ServicoSeguroLerStatusAssiduidadeFuncionario(
                servicoAutorizacao, _numMecanografico.intValue(),
                _dataConsulta, _dataConsulta);
        servicoSeguroLerStatusAssiduidadeFuncionario.execute();

        while (servicoSeguroLerStatusAssiduidadeFuncionario
                .getListaEstadosStatusAssiduidade().contains(
                        Constants.ASSIDUIDADE_PENDENTE)) {
            //enquanto tiver status pendente escreve o status a que corresponde
            construirListaDiasPendente(
                    (IStatusAssiduidade) servicoSeguroLerStatusAssiduidadeFuncionario
                            .getListaStatusAssiduidade().get(0), new Timestamp(
                            calendarioFim.getTimeInMillis()));

            calendarioFim.add(Calendar.DAY_OF_MONTH, 1);
            //se o ciclo ultrapassar a data fim de escolha já não nos interessa
            // continuar
            if (calendarioFim.getTimeInMillis() > _dataFimEscolha.getTime()) { return; }

            servicoSeguroLerStatusAssiduidadeFuncionario = new ServicoSeguroLerStatusAssiduidadeFuncionario(
                    servicoAutorizacao, _numMecanografico.intValue(),
                    _dataConsulta, _dataConsulta);
            servicoSeguroLerStatusAssiduidadeFuncionario.execute();
            _statusPendente = true;
        }
    } /* verificarStatusAssiduidade */

    private void construirListaDiasPendente(IStatusAssiduidade status,
            Timestamp dataFim) throws NotExecuteException {
        Calendar calendario = Calendar.getInstance();
        calendario.setLenient(false);
        while (!_dataConsulta.after(dataFim)) {
            calendario.clear();
            calendario.setTimeInMillis(_dataConsulta.getTime());
            _listaVerbeteDiaria.add(0, FormataCalendar.data(calendario));

            // consulta do horario do funcionario numa determinada data
            try {
                lerHorario();
            } catch (NotExecuteException nee) {
                throw new NotExecuteException(nee.getMessage());
            }

            _listaVerbeteDiaria.add(1, "&nbsp;"
                    + _horario.preencheSigla(_locale) + "&nbsp;");
            _listaVerbeteDiaria.add(2, new String("00:00"));
            _listaVerbeteDiaria.add(3, new String("00:00"));

            _listaJustificacoes = new ArrayList();
            _listaParamJustificacoes = new ArrayList();
            _listaVerbeteDiaria.add(4, new String("<b>" + status.getSigla()
                    + "</b>"));

            _listaMarcacoesPonto = new ArrayList();
            _listaVerbeteDiaria.add(5, formataMarcacoes());

            // aumenta um dia no intervalo de consulta do verbete
            calendario.clear();
            calendario.setTimeInMillis(_dataConsulta.getTime());
            calendario.add(Calendar.DAY_OF_MONTH, +1);
            _dataConsulta.setTime(calendario.getTimeInMillis());

            //junta a lista diária à lista verbete
            _listaVerbeteBody.addAll(_listaVerbeteDiaria);
            _listaVerbeteDiaria.clear();
        }
    } /* construirListaDiasPendente */

    private void verificarDiasInjustificados(Funcionario funcionario, Calendar calendario)
            throws NotExecuteException {

        IFeriadoPersistente iFeriadoPersistente = SuportePersistente
                .getInstance().iFeriadoPersistente();

        Calendar agora = Calendar.getInstance();
        agora.add(Calendar.DAY_OF_MONTH, +1);
        agora.set(Calendar.HOUR_OF_DAY, 00);
        agora.set(Calendar.MINUTE, 00);
        agora.set(Calendar.SECOND, 00);
        agora.set(Calendar.MILLISECOND, 00);

        //só classifica dias injustificados antes do dia de hoje
        if (calendario.before(agora)) {

            if (_horario.getSigla() == Constants.DSC
                    || _horario.getSigla() == Constants.FERIADO) {

                // encontrar a data inicio de consulta
                Calendar calendarioAnterior = Calendar.getInstance();
                calendarioAnterior.setLenient(false);
                calendarioAnterior
                        .setTimeInMillis(calendario.getTimeInMillis());
                while (calendarioAnterior.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                        || calendarioAnterior.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
                        || iFeriadoPersistente.calendarioFeriado(funcionario
                                .getCalendario(), calendarioAnterior.getTime())) {
                    calendarioAnterior.add(Calendar.DAY_OF_MONTH, -1);
                }

                //só classifica dias injustificados antes do dia de hoje
                if (calendarioAnterior.before(agora)) {
                    // encontrar a data fim de consulta
                    Calendar calendarioPosterior = Calendar.getInstance();
                    calendarioPosterior.setLenient(false);
                    calendarioPosterior.setTimeInMillis(calendario
                            .getTimeInMillis());
                    calendarioPosterior.set(Calendar.HOUR_OF_DAY, 23);
                    calendarioPosterior.set(Calendar.MINUTE, 59);
                    calendarioPosterior.set(Calendar.SECOND, 59);
                    while (calendarioPosterior.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                            || calendarioPosterior.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
                            || iFeriadoPersistente.calendarioFeriado(funcionario
                                    .getCalendario(), calendarioAnterior.getTime())) {
                        calendarioPosterior.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    Timestamp dataFimConsulta = new Timestamp(
                            calendarioPosterior.getTimeInMillis());

                    // a data fim de dias injustificados nao pode ser posterior
                    // à data fim de consulta
                    if (dataFimConsulta.after(_dataFimEscolha)) {
                        dataFimConsulta = _dataFimEscolha;
                    }

                    // a data fim de dias injustificados nao pode ser posterior
                    // à data de hoje
                    if (dataFimConsulta.after(agora.getTime())) {
                      agora.add(Calendar.DAY_OF_MONTH, +1);
                      dataFimConsulta = new Timestamp(agora.getTimeInMillis());
                    }

                    if (!verAssiduidade(new Timestamp(calendarioAnterior
                            .getTimeInMillis()), dataFimConsulta)) {
                        construirListaDiasInjustificados(dataFimConsulta);
                        _diasInjustificados = true;
                    }
                }
            }
        }
    } /* verificarDiasInjustificados */

    public void calcularIntervaloConsulta() {

        Calendar calendario = Calendar.getInstance();
        calendario.setLenient(false);

        _calendarioConsulta.clear();
        _calendarioConsulta.setTimeInMillis(_dataConsulta.getTime());
        _calendarioConsulta.set(Calendar.HOUR_OF_DAY, 01);
        _calendarioConsulta.set(Calendar.MINUTE, 00);
        _calendarioConsulta.set(Calendar.SECOND, 00);

        calendario.clear();
        calendario.setTimeInMillis(_horario.getInicioExpediente().getTime());
        calendario.add(Calendar.DAY_OF_MONTH, 1);
        long margemExpediente = (calendario.getTimeInMillis() - _horario
                .getFimExpediente().getTime()) / 2;

        calendario.clear();
        calendario.setTimeInMillis(_calendarioConsulta.getTimeInMillis()
                + _horario.getInicioExpediente().getTime() - margemExpediente);
        _dataInicio = new Timestamp(calendario.getTimeInMillis());
        calendario.clear();
        calendario.setTimeInMillis(_calendarioConsulta.getTimeInMillis()
                + _horario.getFimExpediente().getTime() + margemExpediente - 60
                * 1000);
        _dataFim = new Timestamp(calendario.getTimeInMillis());
    } /* calcularIntervaloConsulta */

    private void consultarMarcacoesPonto() throws NotExecuteException {
        _listaEstados.add(new String("valida"));
        _listaEstados.add(new String("regularizada"));
        _listaEstados.add(new String("cartaoFuncionarioInvalido"));
        _listaEstados.add(new String("cartaoSubstitutoInvalido"));

        ServicoAutorizacao servicoAutorizacao = new ServicoAutorizacao();
        ServicoSeguroConsultarMarcacaoPonto servicoSeguro = new ServicoSeguroConsultarMarcacaoPonto(
                servicoAutorizacao, _listaFuncionarios, _listaCartoes,
                _listaEstados, _dataInicio, _dataFim, _oracleDB);
        servicoSeguro.execute();
        _listaMarcacoesPonto = servicoSeguro.getListaMarcacoesPonto();
    } /* consultarMarcacaoPonto */

    private String formataMarcacoes() {
        Calendar calendario = Calendar.getInstance();
        String marcacoes = new String();

        if (_listaMarcacoesPonto.size() > 0) {
            // lista ordenada das marcacoes de ponto
            Comparador comparadorMarcacoes = new Comparador(new String(
                    "MarcacaoPonto"), new String("crescente"));
            Collections.sort(_listaMarcacoesPonto, comparadorMarcacoes);

            // formatacao do campo das marcacoes
            ListIterator iteradorMarcacoes = _listaMarcacoesPonto
                    .listIterator();
            MarcacaoPonto entrada = null;
            MarcacaoPonto saida = null;

            while (iteradorMarcacoes.hasNext()) {
                entrada = (MarcacaoPonto) iteradorMarcacoes.next();

                //escreve marcacao no verbete
                calendario.clear();
                calendario.setTime(entrada.getData());
                if (entrada.getEstado().equals("regularizada")) {
                    marcacoes = marcacoes.concat("<b>"
                            + FormataCalendar.horas(calendario) + "</b>");
                } else {
                    marcacoes = marcacoes.concat(FormataCalendar
                            .horas(calendario));
                }

                if (iteradorMarcacoes.hasNext()) {
                    saida = (MarcacaoPonto) iteradorMarcacoes.next();
                    //escreve marcacao no verbete
                    calendario.clear();
                    calendario.setTime(saida.getData());
                    marcacoes = marcacoes.concat("-");
                    if (saida.getEstado().equals("regularizada")) {
                        marcacoes = marcacoes.concat("<b>"
                                + FormataCalendar.horas(calendario) + "</b>");
                    } else {
                        marcacoes = marcacoes.concat(FormataCalendar
                                .horas(calendario));
                    }
                }
            }
        } else { // nao existem marcacoes de ponto neste dia
            marcacoes = marcacoes.concat("&nbsp;");
        }

        return marcacoes;
    } /* formataMarcacoes */

    private void consultarJustificacoesPorDia() throws NotExecuteException {

        ServicoAutorizacao servicoAutorizacao = new ServicoAutorizacao();
        ServicoSeguroConsultarJustificacoesPorDia servicoSeguro = new ServicoSeguroConsultarJustificacoesPorDia(
                servicoAutorizacao, _numMecanografico.intValue(), _dataInicio);
        servicoSeguro.execute();
        _listaJustificacoes = (ArrayList) servicoSeguro.getListaJustificacoes();

        // lista ordenada das justificacoes
        Comparador comparadorJustificacoes = new Comparador(new String(
                "Justificacao"), new String("crescente"));
        Collections.sort(_listaJustificacoes, comparadorJustificacoes);
    } /* consultarJustificacoesPorDia */

    private void buscarParamJustificacoes() throws NotExecuteException {

        ServicoAutorizacao servicoAutorizacao = new ServicoAutorizacao();
        ServicoSeguroBuscarParamJustificacoes servicoSeguro = new ServicoSeguroBuscarParamJustificacoes(
                servicoAutorizacao, _listaJustificacoes);

        servicoSeguro.execute();
        _listaParamJustificacoes = (ArrayList) servicoSeguro.getListaJustificacoes();
    } /* buscarParamJustificacoes */

    private String formataJustificacoes() {
        String justificacoes = new String();

        // formata justificacoes para o jsp
        if (_listaParamJustificacoes.size() > 0) {
            ListIterator iterJustificacoes = _listaJustificacoes.listIterator();

            ParamJustificacao paramJustificacao = null;
            while (iterJustificacoes.hasNext()) {
                iterJustificacoes.next();
                paramJustificacao = (ParamJustificacao) _listaParamJustificacoes
                        .get(iterJustificacoes.previousIndex());

                if ((_horario.getSigla() != null)
                        && (_horario.getSigla().equals(Constants.DSC)
                                || _horario.getSigla().equals(Constants.DS) || _horario
                                .getSigla().equals(Constants.FERIADO))) { //Dias
                                                                          // de
                                                                          // Descanso
                    if (paramJustificacao.getTipoDias().equals(Constants.TODOS)) {
                        // formatacao necessaria para o caso de varias
                        // justificacoes por dia
                        justificacoes = justificacoes.concat(new String(
                                paramJustificacao.getSigla()));
                        if (iterJustificacoes.hasNext()) {
                            justificacoes = justificacoes.concat("<br>");
                        }
                    } else {
                        justificacoes = justificacoes.concat("&nbsp;");
                    }
                } else {
                    justificacoes = justificacoes.concat(new String(
                            paramJustificacao.getSigla()));
                    if (iterJustificacoes.hasNext()) {
                        justificacoes = justificacoes.concat("<br>");
                    }
                }
            }
        } else { // nao existem justificacoes de ponto neste dia
            justificacoes = justificacoes.concat("&nbsp;");
        }
        return justificacoes;
    } /* formataJustificacoes */

    private void completaListaMarcacoes() {
        ListIterator iterador = _listaJustificacoes.listIterator();
        while (iterador.hasNext()) {
            Justificacao justificacao = (Justificacao) iterador.next();
            ParamJustificacao paramJustificacao = (ParamJustificacao) _listaParamJustificacoes
                    .get(iterador.previousIndex());

            IStrategyJustificacoes justificacaoStrategy = SuporteStrategyJustificacoes
                    .getInstance().callStrategy(paramJustificacao.getTipo());

            justificacaoStrategy.completaListaMarcacoes(_dataConsulta,
                    justificacao, _listaMarcacoesPonto);
        }
    } /* completaListaMarcacoes */

    private void calcularSaldoDiario() {
        long saldo = 0;
        boolean limita5Horas = false;

        final Calendar calendarioConsulta = _calendarioConsulta;

        // retirar os segundos que nao entram na contabilidade do saldo
        CollectionUtils.transform(_listaMarcacoesPonto, new Transformer() {

            public Object transform(Object arg0) {
                MarcacaoPonto marcacao = (MarcacaoPonto) arg0;

                Calendar calendario = Calendar.getInstance();
                calendario.setTimeInMillis(marcacao.getData().getTime()
                        - calendarioConsulta.getTimeInMillis());
                calendario.set(Calendar.SECOND, 00);
                marcacao.setData(new Timestamp(calendario.getTimeInMillis()));
                return marcacao;
            }
        });
        // ordenar as marcacoes de ponto que estao juntas com as justificacoes
        Comparador comparadorMarcacoes = new Comparador(new String(
                "MarcacaoPonto"), new String("crescente"));
        Collections.sort(_listaMarcacoesPonto, comparadorMarcacoes);

        if (_listaMarcacoesPonto.size() > 0) {

            ListIterator iteradorMarcacoes = _listaMarcacoesPonto
                    .listIterator();
            MarcacaoPonto entrada = null;
            MarcacaoPonto saida = null;

            while (iteradorMarcacoes.hasNext()) {
                entrada = (MarcacaoPonto) iteradorMarcacoes.next();

                if (iteradorMarcacoes.hasNext()) {
                    saida = (MarcacaoPonto) iteradorMarcacoes.next();

                    if (entrada.getData().getTime() < _horario
                            .getInicioExpediente().getTime()) {
                        entrada.setData(new Timestamp(_horario
                                .getInicioExpediente().getTime()));
                    }
                    if (saida.getData().getTime() > _horario.getFimExpediente()
                            .getTime()) {
                        saida.setData(new Timestamp(_horario.getFimExpediente()
                                .getTime()));
                    }

                    if (iteradorMarcacoes.nextIndex() < 3) {
                        if (!coincideJustificacoes(entrada, saida)) {
                            limita5Horas = true;
                        }
                    }
                    IStrategyHorarios horarioStrategy = SuporteStrategyHorarios
                            .getInstance().callStrategy(
                                    _horario.getModalidade());
                    saldo = saldo
                            + horarioStrategy.limitaTrabalhoSeguido(_horario,
                                    entrada.getData().getTime(), saida
                                            .getData().getTime(), limita5Horas);
                    limita5Horas = false;

                    if ((_horario.getSigla() != null)
                            && !((_horario.getSigla().equals(Constants.DSC)
                                    || _horario.getSigla().equals(Constants.DS) || _horario
                                    .getSigla().equals(Constants.FERIADO)))) {
                        calcularTrabalhoNocturno(entrada, saida);
                    }
                }
            }
        }

        limpaListaSaldos(saldo);
    }

    private boolean coincideJustificacoes(MarcacaoPonto entrada,
            MarcacaoPonto saida) {
        Calendar inicioJustificacao = Calendar.getInstance();
        inicioJustificacao.setTimeInMillis(entrada.getData().getTime());
        inicioJustificacao.set(Calendar.DAY_OF_MONTH, 1);
        inicioJustificacao.set(Calendar.MONTH, 0);
        inicioJustificacao.set(Calendar.YEAR, 1970);

        Calendar fimJustificacao = Calendar.getInstance();
        fimJustificacao.setTimeInMillis(saida.getData().getTime());
        fimJustificacao.set(Calendar.DAY_OF_MONTH, 1);
        fimJustificacao.set(Calendar.MONTH, 0);
        fimJustificacao.set(Calendar.YEAR, 1970);

        ListIterator iterador = _listaJustificacoes.listIterator();
        while (iterador.hasNext()) {
            Justificacao justificacao = (Justificacao) iterador.next();

            if (justificacao.getHoraInicio() != null
                    && justificacao.getHoraInicio().equals(
                            inicioJustificacao.getTime())
                    && justificacao.getHoraFim().equals(
                            fimJustificacao.getTime())) {

            return true; }
        }
        return false;
    }

    private void limpaListaSaldos(long saldo) {
        _listaSaldos.clear();
        // horario normal
        _listaSaldos.add(new Long(saldo));
        // plataformas fixas
        _listaSaldos.add(new Long(0));
        // trabalho extraordinário 1º escalão
        _listaSaldos.add(new Long(0));
        // trabalho extraordinário 2º escalão
        _listaSaldos.add(new Long(0));
        // trabalho extraordinário depois do 2º escalão
        _listaSaldos.add(new Long(0));
        // horas em dia de descanso complementar DSC
        _listaSaldos.add(new Long(0));
        // horas em dia de descanso semanal DS
        _listaSaldos.add(new Long(0));
        // trabalho nocturno
        _listaSaldos.add(new Long(0));
        // trabalho extraordinário nocturno 1º escalao
        _listaSaldos.add(new Long(0));
        // trabalho extraordinário nocturno 2º escalao
        _listaSaldos.add(new Long(0));
        // trabalho extraordinário nocturno depois 2º escalao
        _listaSaldos.add(new Long(0));
        // horas em dia de descanso Feriado
        _listaSaldos.add(new Long(0));
        // número de refeições devido a trabalho extra nocturno
        _listaSaldos.add(new Long(0));
    }

    private void calcularHorasEscalao() {
        long saldo = ((Long) _listaSaldos.get(0)).longValue();
        if (saldo > 0) {
            IStrategyHorarios horarioStrategy = SuporteStrategyHorarios
                    .getInstance().callStrategy(_horario.getModalidade());
            horarioStrategy.calcularHorasExtraordinarias(_horario,
                    _listaMarcacoesPonto, _listaSaldos);

            _saldoPrimEscalao = _saldoPrimEscalao
                    + ((Long) _listaSaldos.get(2)).longValue();
            _saldoSegEscalao = _saldoSegEscalao
                    + ((Long) _listaSaldos.get(3)).longValue();
            _saldoDepoisSegEscalao = _saldoDepoisSegEscalao
                    + ((Long) _listaSaldos.get(4)).longValue();
            _saldoNoctPrimEscalao = _saldoNoctPrimEscalao
                    + ((Long) _listaSaldos.get(8)).longValue();
            _saldoNoctSegEscalao = _saldoNoctSegEscalao
                    + ((Long) _listaSaldos.get(9)).longValue();
            _saldoNoctDepoisSegEscalao = _saldoNoctDepoisSegEscalao
                    + ((Long) _listaSaldos.get(10)).longValue();
            if (((Long) _listaSaldos.get(8)).longValue() > 0) {
                _numRefeicoesNocturno++;
            }
        } else {
            _saldoNegativo = _saldoNegativo + saldo;
        }
    } /* calcularHorasEscalao */

    private void descontaSaldoNegativo() {
        // valor negativo de saldo, portanto retira o valor em primeiro lugar do
        // saldoDepoisSegEscalao,
        // depois do saldoSegEscalao e por ultimo do saldoPrimEscalao
        if (_saldoNoctPrimEscalao != 0 || _saldoNoctSegEscalao != 0
                || _saldoNoctDepoisSegEscalao != 0 || _saldoPrimEscalao != 0
                || _saldoSegEscalao != 0 || _saldoDepoisSegEscalao != 0) {
            if (-_saldoNegativo > _saldoNoctDepoisSegEscalao) {
                _saldoNegativo = _saldoNegativo + _saldoNoctDepoisSegEscalao;
                _saldoNoctDepoisSegEscalao = 0;
                if (-_saldoNegativo > _saldoNoctSegEscalao) {
                    _saldoNegativo = _saldoNegativo + _saldoNoctSegEscalao;
                    _saldoNoctSegEscalao = 0;
                    if (-_saldoNegativo > _saldoNoctPrimEscalao) {
                        _saldoNegativo = _saldoNegativo + _saldoNoctPrimEscalao;
                        _saldoNoctPrimEscalao = 0;
                        if (-_saldoNegativo > _saldoDepoisSegEscalao) {
                            _saldoNegativo = _saldoNegativo
                                    + _saldoDepoisSegEscalao;
                            _saldoDepoisSegEscalao = 0;
                            if (-_saldoNegativo > _saldoSegEscalao) {
                                _saldoNegativo = _saldoNegativo
                                        + _saldoSegEscalao;
                                _saldoSegEscalao = 0;
                                if (-_saldoNegativo > _saldoPrimEscalao) {
                                    _saldoPrimEscalao = 0;
                                } else {
                                    _saldoPrimEscalao = _saldoPrimEscalao
                                            + _saldoNegativo;
                                }
                            } else {
                                _saldoSegEscalao = _saldoSegEscalao
                                        + _saldoNegativo;
                            }
                        } else {
                            _saldoDepoisSegEscalao = _saldoDepoisSegEscalao
                                    + _saldoNegativo;
                        }
                    } else {
                        _saldoNoctPrimEscalao = _saldoNoctPrimEscalao
                                + _saldoNegativo;
                    }
                } else {
                    _saldoNoctSegEscalao = _saldoNoctSegEscalao
                            + _saldoNegativo;
                }
            } else {
                _saldoNoctDepoisSegEscalao = _saldoNoctDepoisSegEscalao
                        + _saldoNegativo;
            }
        }
    } /* descontaSaldoNegativo */

    private void calcularTrabalhoNocturno(MarcacaoPonto entrada,
            MarcacaoPonto saida) {
        IStrategyHorarios horarioStrategy = SuporteStrategyHorarios
                .getInstance().callStrategy(_horario.getModalidade());

        _saldoNocturno = _saldoNocturno
                + horarioStrategy.calcularTrabalhoNocturno(_horario, entrada,
                        saida);
    } /* calcularTrabalhoNocturno */

    public ArrayList getVerbete() {
        return _listaVerbeteBody;
    }

    public ArrayList getListaSaldos() {
        return _listaSaldos;
    }
}