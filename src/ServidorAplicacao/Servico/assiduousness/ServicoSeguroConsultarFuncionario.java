package ServidorAplicacao.Servico.assiduousness;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.ListIterator;

import Dominio.CentroCusto;
import Dominio.FuncNaoDocente;
import Dominio.Funcionario;
import Dominio.Horario;
import Dominio.HorarioTipo;
import Dominio.Pessoa;
import Dominio.StatusAssiduidade;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteJDBC.ICentroCustoPersistente;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.IHorarioPersistente;
import ServidorPersistenteJDBC.IHorarioTipoPersistente;
import ServidorPersistenteJDBC.IPessoaPersistente;
import ServidorPersistenteJDBC.IRegimePersistente;
import ServidorPersistenteJDBC.IStatusAssiduidadePersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 *
 * @author  Fernanda Quitério & Tania Pousão
 */
public class ServicoSeguroConsultarFuncionario extends ServicoSeguro {

	private int _numMecanografico;

	private Funcionario _funcionario = null;
	private StatusAssiduidade _status = null;
	private CentroCusto _centroCusto = null;
	private Pessoa _pessoa = null;
	private FuncNaoDocente _funcNaoDocente = null;
	private ArrayList _rotacaoHorario = null;
	private HashMap _listaRegimesRotacao = null;

	public ServicoSeguroConsultarFuncionario(
		ServicoAutorizacao servicoAutorizacaoLerFuncionario,
		int numMecanografico) {
		super(servicoAutorizacaoLerFuncionario);
		_numMecanografico = numMecanografico;
	}

	public void execute() throws NotExecuteException {

		IFuncionarioPersistente iFuncionarioPersistente =
			SuportePersistente.getInstance().iFuncionarioPersistente();
		if ((_funcionario =
			iFuncionarioPersistente.lerFuncionarioPorNumMecanografico(_numMecanografico, Calendar.getInstance().getTime()))
			== null) {
			throw new NotExecuteException("error.funcionario.naoExiste");
		}

		IStatusAssiduidadePersistente iStatusPersistente =
			SuportePersistente.getInstance().iStatusAssiduidadePersistente();
		if(_funcionario.getChaveStatus() != null) 
		{
		    if ((_status = iStatusPersistente.lerStatus(_funcionario.getChaveStatus().intValue())) == null) {
		        throw new NotExecuteException("error.assiduidade.naoExiste");
		    }
		} else {
		    throw new NotExecuteException("error.assiduidade.situacao.nao.regularizada");
		}
		
		
		ICentroCustoPersistente iCentroCustoPersistente =
			SuportePersistente.getInstance().iCentroCustoPersistente();
		if(_funcionario.getChaveCCCorrespondencia() != null) 
		{
			if ((_centroCusto =
				iCentroCustoPersistente.lerCentroCusto(_funcionario.getChaveCCCorrespondencia().intValue()))
				== null) {
				throw new NotExecuteException("error.centroCusto.naoExiste");
			}
		} else {
		    throw new NotExecuteException("error.assiduidade.situacao.nao.regularizada");
		}

		IPessoaPersistente iPessoaPersistente = SuportePersistente.getInstance().iPessoaPersistente();
		if ((_pessoa = iPessoaPersistente.lerPessoa(_funcionario.getChavePessoa())) == null) {
			throw new NotExecuteException("error.pessoa.naoExiste");
		}
		
//		IFuncNaoDocentePersistente iFuncNaoDocentePersistente =
//			SuportePersistente.getInstance().iFuncNaoDocentePersistente();
//		if ((_funcNaoDocente =
//			iFuncNaoDocentePersistente.lerFuncNaoDocentePorFuncionario(_funcionario.getCodigoInterno()))
//			== null) {
//			throw new NotExecuteException("error.funcionario.naoExiste");
//		}

		IHorarioPersistente iHorarioPersistente =
			SuportePersistente.getInstance().iHorarioPersistente();
		// Leitura de uma horário seja ele rotativo ou não 
		_rotacaoHorario = iHorarioPersistente.lerHorarioActualPorNumMecanografico(_numMecanografico);
		//_rotacaoHorario = iHorarioPersistente.lerRotacoesPorNumMecanografico(_numMecanografico);
		if (_rotacaoHorario == null) {
			throw new NotExecuteException("error.funcionario.semHorario");
		} else if (_rotacaoHorario.isEmpty()) {
			throw new NotExecuteException("error.funcionario.semAssiduidade");
		}

		IHorarioTipoPersistente iHorarioTipoPersistente =
			SuportePersistente.getInstance().iHorarioTipoPersistente();
		IRegimePersistente iRegimePersistente = SuportePersistente.getInstance().iRegimePersistente();

		_listaRegimesRotacao = new HashMap();
		ListIterator iterador = _rotacaoHorario.listIterator();

		Horario horario = null;
		HorarioTipo horarioTipo = null;
		ArrayList listaIdRegimes = null;
		ArrayList listaRegimes = null;
		while (iterador.hasNext()) {
			horario = (Horario)iterador.next();

			if (horario.getChaveHorarioTipo() == 0) {
				if ((listaIdRegimes = iHorarioPersistente.lerRegimes(horario.getCodigoInterno()))
					== null) {
					throw new NotExecuteException("error.regime.impossivel");
				}
				if ((listaRegimes = iRegimePersistente.lerDesignacaoRegimes(listaIdRegimes)) == null) {
					throw new NotExecuteException("error.regime.impossivel");
				}
			} else {
				if ((horarioTipo = iHorarioTipoPersistente.lerHorarioTipo(horario.getChaveHorarioTipo()))
					== null) {
					throw new NotExecuteException("error.funcionario.semHorario");
				}

				/* regista a modalidade e a sigla */
				horario.transforma(horarioTipo);

				if ((listaIdRegimes = iHorarioTipoPersistente.lerRegimes(horario.getChaveHorarioTipo()))
					== null) {
					throw new NotExecuteException("error.regime.impossivel");
				}
				if ((listaRegimes = iRegimePersistente.lerDesignacaoRegimes(listaIdRegimes)) == null) {
					throw new NotExecuteException("error.regime.impossivel");
				}
			}

			_listaRegimesRotacao.put(new Integer(horario.getPosicao()), listaRegimes);
		}
	}

	public Funcionario getFuncionario() {
		return _funcionario;
	}
	public StatusAssiduidade getStatusAssiduidade() {
		return _status;
	}
	public CentroCusto getCentroCusto() {
		return _centroCusto;
	}
	public Pessoa getPessoa() {
		return _pessoa;
	}
	public FuncNaoDocente getFuncNaoDocente() {
		return _funcNaoDocente;
	}
	public ArrayList getRotacaoHorario() {
		return _rotacaoHorario;
	}
	public HashMap getListaRegimesRotacao() {
		return _listaRegimesRotacao;
	}
}