/*
 * Created on 30/Jun/2003
 *
 * 
 */
package ServidorAplicacao.Servico.sop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import DataBeans.ISiteComponent;
import DataBeans.InfoClass;
import DataBeans.InfoSiteClassesComponent;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.IDisciplinaExecucao;
import Dominio.ITurma;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *
 * 30/Jun/2003
 * fenix-branch
 * ServidorAplicacao.Servico.sop
 * 
 */
public class ReadAvailableClassesForShift implements IServico {

	/**
	 * 
	 */
	private ReadAvailableClassesForShift() {
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "ReadAvailableClassesForShift";
	}

	private static ReadAvailableClassesForShift service =
		new ReadAvailableClassesForShift();

	public static ReadAvailableClassesForShift getService() {
		return service;
	}

	public SiteView run(Integer shiftOID) throws FenixServiceException {
		List infoClasses = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			ITurnoPersistente shiftDAO = sp.getITurnoPersistente();

			ITurno shift =
				(ITurno) shiftDAO.readByOId(new Turno(shiftOID), false);

			ICursoExecucaoPersistente executionDegreeDAO =
				sp.getICursoExecucaoPersistente();
			IDisciplinaExecucao executionCourse = shift.getDisciplinaExecucao();

			Collection degreeCurricularPlans =
				extractCurricularPlans(executionCourse);

			List executionDegree =
				executionDegreeDAO.readByExecutionYearAndDegreeCurricularPlans(
					executionCourse.getExecutionPeriod().getExecutionYear(),
					degreeCurricularPlans);

			ITurmaPersistente classDAO = sp.getITurmaPersistente();
			List classes =
				classDAO.readByExecutionPeriod(
					executionCourse.getExecutionPeriod());

			infoClasses = new ArrayList();
			Iterator iter = classes.iterator();
			while (iter.hasNext()) {
				ITurma classImpl = (ITurma) iter.next();
				if (executionDegree.contains(classImpl.getExecutionDegree())) {
					InfoClass infoClass = Cloner.copyClass2InfoClass(classImpl);
					infoClasses.add(infoClass);
				}
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		ISiteComponent classesComponent =
			new InfoSiteClassesComponent(infoClasses);
		SiteView siteView = new SiteView(classesComponent);

		return siteView;
	}

	private Collection extractCurricularPlans(IDisciplinaExecucao executionCourse) {
		List curricularCourses =
			executionCourse.getAssociatedCurricularCourses();

		Iterator curricularCoursesIterator = curricularCourses.iterator();
		Collection degreeCurricularPlans = new ArrayList();
		
		while (curricularCoursesIterator.hasNext()) {
			ICurricularCourse curricularCourse =
				(ICurricularCourse) curricularCoursesIterator.next();
			IDegreeCurricularPlan degreeCurricularPlan =
				curricularCourse.getDegreeCurricularPlan();
			if (!degreeCurricularPlans.contains(degreeCurricularPlan)) {
				degreeCurricularPlans.add(degreeCurricularPlan);
			}
		}
		return degreeCurricularPlans;
	}

}
