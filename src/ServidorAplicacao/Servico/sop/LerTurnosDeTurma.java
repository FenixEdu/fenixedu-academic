/*
 * LerTurnosDeTurma.java
 *
 * Created on 28 de Outubro de 2002, 20:26
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerTurnosDeTurma
 *
 * @author tfc130
 **/
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.ICursoExecucao;
import Dominio.IExecutionPeriod;
import Dominio.ITurma;
import Dominio.ITurno;
import Dominio.Turma;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaTurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerTurnosDeTurma implements IServico {

	private static LerTurnosDeTurma _servico = new LerTurnosDeTurma();
	/**
	 * The singleton access method of this class.
	 **/
	public static LerTurnosDeTurma getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private LerTurnosDeTurma() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "LerTurnosDeTurma";
	}

	public Object run(
		String className,
		InfoExecutionDegree infoExecutionDegree,
		InfoExecutionPeriod infoExecutionPeriod) throws FenixServiceException {
		
		List infoShiftAndLessons = new ArrayList();

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			ITurmaTurnoPersistente classShiftDAO =
				sp.getITurmaTurnoPersistente();

			IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);
			ICursoExecucao executionDegree = Cloner.copyInfoExecutionDegree2ExecutionDegree(infoExecutionDegree);

			ITurma group = new Turma();

			group.setExecutionDegree(executionDegree);
			group.setExecutionPeriod(executionPeriod);
			group.setNome(className);

			List shiftList = classShiftDAO.readByClass(group);

			Iterator iterator = shiftList.iterator();
//			infoTurnos = new ArrayList();

			while (iterator.hasNext()) {
				ITurno turno = (ITurno) iterator.next();
				InfoShift infoTurno =  (InfoShift) Cloner.get(turno);
				
				List aulas = turno.getAssociatedLessons();
				Iterator itLessons = aulas.iterator();
				
				List infoLessons = new ArrayList();
				InfoLesson infoLesson;
					
				while(itLessons.hasNext()){
					 infoLesson = Cloner.copyILesson2InfoLesson((IAula) itLessons.next());					
					
					 infoLesson.setInfoShift(infoTurno);
					 infoLessons.add(infoLesson);
				}
				
				infoTurno.setInfoLessons(infoLessons);
				infoShiftAndLessons.add(infoTurno);

			}
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex);
		}
		return infoShiftAndLessons;

	}

}