/*
 * EditarTurno.java
 *
 * Created on 27 de Outubro de 2002, 21:00
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviço EditarTurno.
 *
 * @author tfc130
 **/
import org.apache.commons.beanutils.BeanUtils;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShift;
import Dominio.Curso;
import Dominio.CursoExecucao;
import Dominio.DisciplinaExecucao;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class EditarTurno implements IServico {

	private static EditarTurno _servico = new EditarTurno();
	/**
	 * The singleton access method of this class.
	 **/
	public static EditarTurno getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private EditarTurno() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "EditarTurno";
	}

	public Object run(
		InfoShift shiftToEdit,
		InfoShift turnoNova) {

		ITurno turno = null;
		boolean result = false;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			//Copia infoExecutionCourse para DisciplinaExecucao
			IDisciplinaExecucao executionCourse = new DisciplinaExecucao();
			ICursoExecucao executionDegree = new CursoExecucao();
			ICurso degree = new Curso();
			InfoExecutionCourse infoExecutionCourse = shiftToEdit.getInfoDisciplinaExecucao();
			try {
				BeanUtils.copyProperties(executionCourse, infoExecutionCourse);
				BeanUtils.copyProperties(
					executionDegree,
					infoExecutionCourse.getInfoLicenciaturaExecucao());
				BeanUtils.copyProperties(
					degree,
					infoExecutionCourse
						.getInfoLicenciaturaExecucao()
						.getInfoLicenciatura());
			} catch (Exception e) {
				e.printStackTrace(System.out);
				throw new RuntimeException(e.getMessage());
			}

			executionCourse.setLicenciaturaExecucao(executionDegree);
			executionDegree.setCurso(degree);
			// fim da cópia 
						
			turno =
				sp.getITurnoPersistente().readByNameAndExecutionCourse(
					shiftToEdit.getNome(),
					executionCourse);
			if (turno != null) {
				System.out.println("*************************************Vou editar!");
				turno.setNome(turnoNova.getNome());
				turno.setTipo(turno.getTipo());
				turno.setLotacao(turnoNova.getLotacao());
				sp.getITurnoPersistente().lockWrite(turno);
				System.out.println("**************************************Consegui editar!");
				result = true;
			}
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace(System.out);
		}

		return new Boolean(result);
	}

}