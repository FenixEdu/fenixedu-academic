package ServidorAplicacao.Servico.assiduousness;

import java.sql.Timestamp;
import java.util.List;

import Dominio.Funcionario;
import Dominio.Horario;
import Dominio.HorarioTipo;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteJDBC.IFeriadoPersistente;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.IHorarioPersistente;
import ServidorPersistenteJDBC.IHorarioTipoPersistente;
import ServidorPersistenteJDBC.IRegimePersistente;
import ServidorPersistenteJDBC.SuportePersistente;
import constants.assiduousness.Constants;

/**
 * 
 * @author Fernanda Quitério & Tania Pousão
 */
public class ServicoSeguroLerHorario extends ServicoSeguro {
    private int _numMecanografico = 0;

    private Timestamp _dataConsulta = null;

    private Horario _horario = null;

    private HorarioTipo _horarioTipo = null;

    private List _listaRegimes = null;

    public ServicoSeguroLerHorario(ServicoAutorizacao servicoAutorizacaoLer, int numMecanografico,
            Timestamp dataConsulta) {
        super(servicoAutorizacaoLer);
        _numMecanografico = numMecanografico;
        _dataConsulta = dataConsulta;
    }

    public void execute() throws NotExecuteException {

        IHorarioPersistente iHorarioPersistente = SuportePersistente.getInstance().iHorarioPersistente();
        IRegimePersistente iRegimePersistente = SuportePersistente.getInstance().iRegimePersistente();

        boolean excepcao = true;
        if ((_horario = iHorarioPersistente.lerExcepcaoHorarioPorNumMecanografico(_numMecanografico,
                _dataConsulta)) == null) {
            excepcao = false;
            if ((_horario = iHorarioPersistente.lerHorarioPorNumFuncionario(_numMecanografico,
                    _dataConsulta)) == null) {
                throw new NotExecuteException("error.funcionario.semHorario");
            }
        }

        IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance()
                .iFuncionarioPersistente();
        Funcionario funcionario = null;
        if ((funcionario = iFuncionarioPersistente.lerFuncionarioPorNumMecanografico(_numMecanografico,
                _dataConsulta)) == null) {
            throw new NotExecuteException("error.funcionario.impossivelLer");
        }

        IFeriadoPersistente iFeriadoPersistente = SuportePersistente.getInstance().iFeriadoPersistente();
        if (iFeriadoPersistente.calendarioFeriado(funcionario.getCalendario(), _dataConsulta)) {
            _horario.transformaFeriado();
        }

        List listaIdRegimes = null;
        if (_horario.getChaveHorarioTipo() == 0) {
            if (excepcao) {
                if ((listaIdRegimes = iHorarioPersistente.lerRegimesHorarioExcepcao(_horario
                        .getCodigoInterno())) == null) {
                    throw new NotExecuteException("error.regime.impossivel");
                }
            } else {
                if ((listaIdRegimes = iHorarioPersistente.lerRegimes(_horario.getCodigoInterno())) == null) {
                    throw new NotExecuteException("error.regime.impossivel");
                }
            }
            if ((_listaRegimes = iRegimePersistente.lerDesignacaoRegimes(listaIdRegimes)) == null) {
                throw new NotExecuteException("error.regime.impossivel");
            }
        } else {
            //é um horário tipo
            IHorarioTipoPersistente iHorarioTipoPersistente = SuportePersistente.getInstance()
                    .iHorarioTipoPersistente();
            if ((_horarioTipo = iHorarioTipoPersistente.lerHorarioTipo(_horario.getChaveHorarioTipo())) == null) {
                throw new NotExecuteException("error.funcionario.semHorario");
            }

            if ((listaIdRegimes = iHorarioTipoPersistente.lerRegimes(_horario.getChaveHorarioTipo())) == null) {
                throw new NotExecuteException("error.regime.impossivel");
            }
            if ((_listaRegimes = iRegimePersistente.lerDesignacaoRegimes(listaIdRegimes)) == null) {
                throw new NotExecuteException("error.regime.impossivel");
            }

            if ((_horario.getSigla() != null)
                    && ((_horario.getSigla().equals(Constants.DS))
                            || (_horario.getSigla().equals(Constants.DSC)) || (_horario.getSigla()
                            .equals(Constants.FERIADO)))) {
                //é um horário de descanso
                _horario.transformaDescanso(_horarioTipo);
            } else {
                _horario.transforma(_horarioTipo);
            }

        }
    }

    public Horario getHorario() {
        return _horario;
    }

    public HorarioTipo getHorarioTipo() {
        return _horarioTipo;
    }

    public List getListaRegimes() {
        return _listaRegimes;
    }
}