/*
 * ApagarTurma.java
 *
 * Created on 27 de Outubro de 2002, 18:13
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviço ApagarTurma.
 *
 * @author tfc130
 **/
import DataBeans.InfoClass;
import DataBeans.util.Cloner;
import Dominio.ICursoExecucao;
import Dominio.IExecutionPeriod;
import Dominio.ITurma;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ApagarTurma implements IServico {

	private static ApagarTurma _servico = new ApagarTurma();
	/**
	 * The singleton access method of this class.
	 **/
	public static ApagarTurma getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ApagarTurma() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ApagarTurma";
	}

	public Object run(InfoClass infoClass) {

		ITurma turma = null;
		boolean result = false;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoClass.getInfoExecutionPeriod());
			ICursoExecucao executionDegree = Cloner.copyInfoExecutionDegree2ExecutionDegree(infoClass.getInfoExecutionDegree());

			turma = sp.getITurmaPersistente().readByNameAndExecutionDegreeAndExecutionPeriod(infoClass.getNome(), executionDegree, executionPeriod);
			try {
				if(turma != null) {
					sp.getITurmaPersistente().delete(turma);
					result = true;
				}
			} catch(ExcepcaoPersistencia ex1) {
				throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex1);
			}

		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}

		return new Boolean(result);
	}

/*
	public Object run(ClassKey keyTurma) {

		ITurma turma1 = new Turma();
		boolean result = false;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			turma1.setNome(keyTurma.getNomeTurma());

			sp.iniciarTransaccao();
			List classes = sp.getITurmaPersistente().readByCriteria(turma1);
			sp.confirmarTransaccao();
			sp.iniciarTransaccao();
			sp.getITurmaPersistente().deleteByCriteria(turma1);
			sp.confirmarTransaccao();
			sp.iniciarTransaccao();
			List classes1 = sp.getITurmaPersistente().readByCriteria(turma1);
			sp.confirmarTransaccao();

			if (!classes.isEmpty() && classes1.isEmpty()) {
				result = true;
			}

		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}

		return new Boolean(result);
	}
*/
}