/*
 * EditarTurma.java
 *
 * Created on 27 de Outubro de 2002, 20:48
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviço EditarTurma.
 *
 * @author tfc130
 **/
import DataBeans.InfoClass;
import DataBeans.util.Cloner;
import Dominio.ICursoExecucao;
import Dominio.IExecutionPeriod;
import Dominio.ITurma;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.sop.exceptions.ExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class EditarTurma implements IServico {

	private static EditarTurma _servico = new EditarTurma();
	/**
	 * The singleton access method of this class.
	 **/
	public static EditarTurma getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private EditarTurma() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "EditarTurma";
	}

	public Object run(InfoClass oldClassView, InfoClass newClassView) throws FenixServiceException {

		ITurma turma = null;
		boolean result = false;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IExecutionPeriod executionPeriod =
				Cloner.copyInfoExecutionPeriod2IExecutionPeriod(
					oldClassView.getInfoExecutionPeriod());

			ICursoExecucao executionDegree =
				Cloner.copyInfoExecutionDegree2ExecutionDegree(
					oldClassView.getInfoExecutionDegree());

			turma =
				sp
					.getITurmaPersistente()
					.readByNameAndExecutionDegreeAndExecutionPeriod(
					oldClassView.getNome(),
					executionDegree, executionPeriod);
			/* :FIXME: we have to change more things... to dump one year to another */			
			if (turma != null) {
				turma.setNome(newClassView.getNome());
				
				try {
					sp.getITurmaPersistente().lockWrite(turma);
				} catch (ExistingPersistentException ex) {
					throw new ExistingServiceException(ex);
				}
				
				result = true;
			}
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex.getMessage());
		}

		return new Boolean(result);
	}

}