/*
 * Created on 6/Fev/2005
 */
package ServidorAplicacao.Servico.managementAssiduousness;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoEmployeeWithAll;
import DataBeans.managementAssiduousness.InfoExtraWork;
import Dominio.Horario;
import Dominio.IEmployee;
import Dominio.IStrategyHorarios;
import Dominio.IStrategyJustificacoes;
import Dominio.Justificacao;
import Dominio.MarcacaoPonto;
import Dominio.ParamJustificacao;
import Dominio.SuporteStrategyHorarios;
import Dominio.SuporteStrategyJustificacoes;
import Dominio.managementAssiduousness.ExtraWork;
import Dominio.managementAssiduousness.IExtraWork;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.Servico.assiduousness.ServicoAutorizacaoLer;
import ServidorAplicacao.Servico.assiduousness.ServicoSeguroBuscarParamJustificacoes;
import ServidorAplicacao.Servico.assiduousness.ServicoSeguroConstruirEscolhasMarcacoesPonto;
import ServidorAplicacao.Servico.assiduousness.ServicoSeguroConsultarJustificacoesPorDia;
import ServidorAplicacao.Servico.assiduousness.ServicoSeguroConsultarMarcacaoPonto;
import ServidorAplicacao.Servico.assiduousness.ServicoSeguroLerFimAssiduidade;
import ServidorAplicacao.Servico.assiduousness.ServicoSeguroLerHorario;
import ServidorAplicacao.Servico.assiduousness.ServicoSeguroLerInicioAssiduidade;
import ServidorAplicacao.Servico.assiduousness.ServicoSeguroLerJustificacoesComValidade;
import ServidorAplicacao.Servico.assiduousness.ServicoSeguroLerMarcacoesPonto;
import ServidorAplicacao.Servico.assiduousness.ServicoSeguroLerStatusAssiduidadeFuncionario;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistenteJDBC.IFeriadoPersistente;
import ServidorPersistenteJDBC.SuportePersistente;
import Util.Comparador;
import constants.assiduousness.Constants;

/**
 * @author Tânia Pousão
 * 
 */
public class ExtraWorkSheet implements IService {
    private Integer _numMecanografico;

    private Timestamp _dataInicioEscolha = null;

    private Timestamp _dataFimEscolha = null;

    private Locale _locale = null;

    private Horario _horario = null;

    private boolean _mesInjustificado = false;

    private boolean _diasInjustificados = false;

    private boolean _statusPendente = false;

    private List _listaFuncionarios = new ArrayList();

    private List _listaEstados = new ArrayList();

    private List _listaCartoes = null;

    private List _listaRegimes = null;

    private List _listaMarcacoesPonto = null;

    private List _listaJustificacoes = null;

    private List _listaParamJustificacoes = null;

    private List _listaSaldos = new ArrayList();

    private Timestamp _dataConsulta = null;

    Calendar _calendarioConsulta = null;

    private Timestamp _dataInicio = null;

    private Timestamp _dataFim = null;

    public ExtraWorkSheet() {
        super();
    }

    public List run(Integer employeeNumber, Timestamp firstDay,
            Timestamp lastDay, Locale locale) throws Exception {
        List infoExtraWorkList = null;
        List extraWorkList = null;
        IEmployee employee = null;
        ISuportePersistente sp;
        try {
            sp = SuportePersistenteOJB.getInstance();

            IPersistentEmployee employeeDAO = sp.getIPersistentEmployee();
            // »»»»»»»»»read employee
            employee = employeeDAO.readByNumber(employeeNumber);
            if (employee == null) {
                return null;
            }
            // »»»»»»»»»read employee historc to found working cost center
            employee.setHistoricList(employeeDAO
                    .readHistoricByKeyEmployee(employee.getIdInternal()
                            .intValue()));
            employee.fillEmployeeHistoric();

            // »»»»»»»»»build extra work for to fill sheet
            extraWorkList = buildSheet(employeeNumber, firstDay, lastDay,
                    locale);
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw e;
        }

        if (extraWorkList != null && extraWorkList.size() > 0) {
            infoExtraWorkList = (List) CollectionUtils.collect(extraWorkList,
                    new Transformer() {
                        public Object transform(Object arg0) {
                            IExtraWork extraWork = (IExtraWork) arg0;
                            return InfoExtraWork.newInfoFromDomain(extraWork);
                        }
                    });

            infoExtraWorkList.add(0, InfoEmployeeWithAll
                    .newInfoFromDomain(employee));
        } else {
            infoExtraWorkList = new ArrayList();
            infoExtraWorkList.add(0, InfoEmployeeWithAll
                    .newInfoFromDomain(employee));
        }

        return infoExtraWorkList;
    }

    private List buildSheet(Integer employeeNumber, Timestamp firstDay,
            Timestamp lastDay, Locale locale) throws Exception {
        List extraWorkListPerDay = new ArrayList();

        try {
            //
            SuportePersistente.getInstance().iniciarTransaccao();

            // inicializar variáveis globais
            _numMecanografico = employeeNumber;
            System.out.println("-------------->"
                    + _numMecanografico);
            _dataInicioEscolha = firstDay;
            _dataFimEscolha = lastDay;
            _locale = locale;
            _dataConsulta = new Timestamp(_dataInicioEscolha.getTime());

            lerStatusAssiduidade();
            lerFimAssiduidade();
            lerInicioAssiduidade();
            construirEscolhasMarcacoesPonto();

            verificarMesInjustificado();
            if (!_mesInjustificado) {
                Calendar calendario = Calendar.getInstance();
                calendario.setLenient(false);

                // ====Inicio da construcao da lista a mostrar no jsp ====
                while (!_dataConsulta.after(_dataFimEscolha)) {
                    _calendarioConsulta = Calendar.getInstance();
                    _calendarioConsulta.setLenient(false);

                    calendario.clear();
                    calendario.setTimeInMillis(_dataConsulta.getTime());

                    System.out.println("-------------->"
                            + _dataConsulta);
                    
                    limpaListaSaldos(0);

                    calculateWorkHours(calendario);

                    // ***Dia
                    IExtraWork extraWork = new ExtraWork();
                    extraWork.setDay(calendario.getTime());
                    if (((Long) _listaSaldos.get(0)).longValue() > 0) {
                        System.out.println("-------->Extra Work at "
                                + new Date(((Long) _listaSaldos.get(0))
                                        .longValue()));
                        extraWork.setBeginHour(calculateBeginExtraWork());
                        extraWork.setEndHour(calculateEndExtraWork());
                        extraWork.setTotalExtraWork(calculateHourExtraWork(0, -1));
                        extraWork.setDiurnalFirstHour(calculateHourExtraWork(2,
                                -1));
                        extraWork
                                .setDiurnalAfterSecondHour(calculateHourExtraWork(
                                        3, 4));
                        extraWork.setNocturnalFirstHour(calculateHourExtraWork(
                                8, -1));
                        extraWork
                                .setNocturnalAfterSecondHour(calculateHourExtraWork(
                                        9, 10));
                        extraWork.setRestDay(calculateHourExtraWork(5, 6));

                        // extraWork.setMealSubsidy();//TODO
                    }

                    // ***Add
                    extraWorkListPerDay.add(extraWork);

                    // aumenta um dia no intervalo de consulta do verbete
                    calendario.clear();
                    calendario.setTimeInMillis(_dataConsulta.getTime());
                    calendario.add(Calendar.DAY_OF_MONTH, +1);
                    _dataConsulta.setTime(calendario.getTimeInMillis());
                }
            }

            SuportePersistente.getInstance().confirmarTransaccao();
        } catch (Exception e) {
            e.printStackTrace();
            SuportePersistente.getInstance().cancelarTransaccao();
            throw e;
        }

        return extraWorkListPerDay;
    } /* buildSheet */

    private void calculateWorkHours(Calendar calendario)
            throws NotExecuteException {
        // consulta do horario do funcionario neste dia
        lerHorario();

        // Se o status do funcionário é Pendente então não vale a
        // pena continuar os cálculos
        verificarStatusAssiduidade();
        if (_statusPendente) {
            _statusPendente = false;
            return;
        }

        // Se os dias do funcionário são injustificados então não
        // vale a pena continuar os cálculos
        verificarDiasInjustificados(calendario);
        if (_diasInjustificados) {
            _diasInjustificados = false;
            return;
        }

        // consulta das marcacoes do funcionario neste dia, tendo
        // atencao ao expediente deste horario
        calcularIntervaloConsulta();
        consultarMarcacoesPonto();

        // consulta das justificacoes do funcionario neste dia
        consultarJustificacoesPorDia();
        buscarParamJustificacoes();

        // Dias de Descanso com marcacoes e sem justificacoes,
        // logo calcula o saldo
        if ((_horario.getSigla() != null)
                && (_horario.getSigla().equals(Constants.DSC)
                        || _horario.getSigla().equals(Constants.DS) || _horario
                        .getSigla().equals(Constants.FERIADO))) {
            if (_listaMarcacoesPonto.size() > 0) {
                calcularSaldoDiario(); // SALDO
            }
        } else { // Dias Uteis
            if (_listaParamJustificacoes.size() > 0) {
                // acrescenta as justificacoes na lista de marcacoes
                // de ponto como se fossem marcacoes de ponto
                completaListaMarcacoes();
            }

            calcularSaldoDiario(); // SALDO

            IStrategyHorarios horarioStrategy = SuporteStrategyHorarios
                    .getInstance().callStrategy(_horario.getModalidade());
            horarioStrategy.setSaldosHorarioVerbeteBody(_horario,
                    _listaRegimes, _listaParamJustificacoes,
                    _listaMarcacoesPonto, _listaSaldos);

            // actualiza o saldo
            if (_listaParamJustificacoes.size() > 0) {
                ListIterator iterJustificacoes = _listaJustificacoes
                        .listIterator();

                ParamJustificacao paramJustificacao = null;
                Justificacao justificacao = null;
                while (iterJustificacoes.hasNext()) {
                    justificacao = (Justificacao) iterJustificacoes.next();
                    paramJustificacao = (ParamJustificacao) _listaParamJustificacoes
                            .get(iterJustificacoes.previousIndex());

                    // actualiza saldo diario
                    IStrategyJustificacoes justificacaoStrategy = SuporteStrategyJustificacoes
                            .getInstance().callStrategy(
                                    paramJustificacao.getTipo());
                    justificacaoStrategy.updateSaldosHorarioVerbeteBody(
                            justificacao, paramJustificacao, _horario,
                            _listaRegimes, _listaMarcacoesPonto, _listaSaldos); // SALDO
                }
            }

            // calculo das horas extraordinarias diurnas e
            // nocturnas
            calcularHorasEscalao();
        }
    }

    private Date calculateBeginExtraWork() {
        // ao retirar o saldo do dia à ultima marcação de ponto
        // resulta o início do trabalho extra

        MarcacaoPonto ultimaMarcacaoPonto = (MarcacaoPonto) _listaMarcacoesPonto
                .get(_listaMarcacoesPonto.size() - 1);
        long beginExtraWorkMillis = ultimaMarcacaoPonto.getData().getTime()
                - ((Long) _listaSaldos.get(0)).longValue();

        return new Date(beginExtraWorkMillis);
    }

    private Date calculateEndExtraWork() {
        // a ultima marcação de ponto
        // resulta no fim do trabalho extra

        MarcacaoPonto ultimaMarcacaoPonto = (MarcacaoPonto) _listaMarcacoesPonto
                .get(_listaMarcacoesPonto.size() - 1);
        long endExtraWorkMillis = ultimaMarcacaoPonto.getData().getTime();
        
        return new Date(endExtraWorkMillis);
    }

    private Date calculateHourExtraWork(int index, int index2) {
        long balance = ((Long) _listaSaldos.get(index)).longValue();
        if (index2 != -1) {
            balance = balance + ((Long) _listaSaldos.get(index2)).longValue();
        }

        if(balance <= 0) {
            return null;
        }         
        //	o calendario tem sempre uma hora a mais quando se pretende a duracao,
        // entao acerta-se
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(balance);
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        
        return calendar.getTime();
    }

    // »»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»
    // FUNÇÕES AUXILIARES DO SERVIÇO SEGURO CONSULTAR VERBETE
    // »»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»
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
                            Constants.ASSIDUIDADE_ACTIVO)) {
                throw new NotExecuteException("error.assiduidade.naoExiste");
            }
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
        _listaCartoes = servicoSeguro.getListaCartoes();

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
                new Timestamp(calendarioFim.getTimeInMillis()));
        servicoSeguroLerMarcacoesPonto.execute();
        List lista = servicoSeguroLerMarcacoesPonto.getListaMarcacoesPonto();

        if ((lista == null) || (lista.size() <= 0)) {
            ServicoSeguroLerJustificacoesComValidade servicoSeguroLerJustificacoes = new ServicoSeguroLerJustificacoesComValidade(
                    servicoAutorizacao, _numMecanografico.intValue(), new Date(
                            calendarioInicio.getTimeInMillis()), new Date(
                            calendarioFim.getTimeInMillis()));
            servicoSeguroLerJustificacoes.execute();

            lista = servicoSeguroLerJustificacoes.getListaJustificacoes();
            if ((lista == null) || (lista.size() <= 0)) {
                // o funcionario nao teve assiduidade neste intervalo de datas
                return false;
            }
        }
        return true;
    } /* verAssiduidade */

    private void lerHorario() throws NotExecuteException {

        ServicoAutorizacao servicoAutorizacao = new ServicoAutorizacao();
        ServicoSeguroLerHorario servicoSeguro = new ServicoSeguroLerHorario(
                servicoAutorizacao, _numMecanografico.intValue(), _dataConsulta);

        servicoSeguro.execute();
        _horario = servicoSeguro.getHorario();
        _listaRegimes = servicoSeguro.getListaRegimes();
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
            calendarioFim.add(Calendar.DAY_OF_MONTH, 1);
            // se o ciclo ultrapassar a data fim de escolha já não nos interessa
            // continuar
            if (calendarioFim.getTimeInMillis() > _dataFimEscolha.getTime()) {
                return;
            }

            servicoSeguroLerStatusAssiduidadeFuncionario = new ServicoSeguroLerStatusAssiduidadeFuncionario(
                    servicoAutorizacao, _numMecanografico.intValue(),
                    _dataConsulta, _dataConsulta);
            servicoSeguroLerStatusAssiduidadeFuncionario.execute();
            _statusPendente = true;
        }
    } /* verificarStatusAssiduidade */

    private void verificarDiasInjustificados(Calendar calendario)
            throws NotExecuteException {

        IFeriadoPersistente iFeriadoPersistente = SuportePersistente
                .getInstance().iFeriadoPersistente();

        Calendar agora = Calendar.getInstance();
        agora.add(Calendar.DAY_OF_MONTH, +1);
        agora.set(Calendar.HOUR_OF_DAY, 00);
        agora.set(Calendar.MINUTE, 00);
        agora.set(Calendar.SECOND, 00);
        agora.set(Calendar.MILLISECOND, 00);

        // só classifica dias injustificados antes do dia de hoje
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
                        || iFeriadoPersistente.diaFeriado(calendarioAnterior
                                .getTime())) {
                    calendarioAnterior.add(Calendar.DAY_OF_MONTH, -1);
                }

                // só classifica dias injustificados antes do dia de hoje
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
                            || iFeriadoPersistente
                                    .diaFeriado(calendarioPosterior.getTime())) {
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
                _listaEstados, _dataInicio, _dataFim);
        servicoSeguro.execute();
        _listaMarcacoesPonto = servicoSeguro.getListaMarcacoesPonto();
    } /* consultarMarcacaoPonto */

    private void consultarJustificacoesPorDia() throws NotExecuteException {

        ServicoAutorizacao servicoAutorizacao = new ServicoAutorizacao();
        ServicoSeguroConsultarJustificacoesPorDia servicoSeguro = new ServicoSeguroConsultarJustificacoesPorDia(
                servicoAutorizacao, _numMecanografico.intValue(), _dataInicio);
        servicoSeguro.execute();
        _listaJustificacoes = servicoSeguro.getListaJustificacoes();

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
        _listaParamJustificacoes = servicoSeguro.getListaJustificacoes();
    } /* buscarParamJustificacoes */

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

        // retirar os segundos que nao entram na contabilidade do saldo
        CollectionUtils.transform(_listaMarcacoesPonto, new Transformer() {
            public Object transform(Object arg0) {
                MarcacaoPonto marcacao = (MarcacaoPonto) arg0;

                Calendar calendario = Calendar.getInstance();
                calendario.setTimeInMillis(marcacao.getData().getTime()
                        - _calendarioConsulta.getTimeInMillis());
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

                return true;
            }
        }
        return false;
    }

    private void calcularHorasEscalao() {
        long saldo = ((Long) _listaSaldos.get(0)).longValue();

        if (saldo > 0) {
            IStrategyHorarios horarioStrategy = SuporteStrategyHorarios
                    .getInstance().callStrategy(_horario.getModalidade());
            horarioStrategy.calcularHorasExtraordinarias(_horario,
                    _listaMarcacoesPonto, _listaSaldos);
        }
    } /* calcularHorasEscalao */

    private void calcularTrabalhoNocturno(MarcacaoPonto entrada,
            MarcacaoPonto saida) {
        IStrategyHorarios horarioStrategy = SuporteStrategyHorarios
                .getInstance().callStrategy(_horario.getModalidade());

        _listaSaldos.set(7, new Long(horarioStrategy.calcularTrabalhoNocturno(
                _horario, entrada, saida)));
    } /* calcularTrabalhoNocturno */

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
    }
}
