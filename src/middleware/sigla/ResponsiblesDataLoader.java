/*
 * Created on 22/Mai/2003
 *
 * 
 */
package middleware.sigla;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.DisciplinaExecucao;
import Dominio.ExecutionPeriod;
import Dominio.ICurricularCourse;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import Dominio.Professorship;
import Dominio.ResponsibleFor;
import Dominio.Teacher;
import Util.PeriodState;

/**
 * @author João Mota
 *
 */
public class ResponsiblesDataLoader {
	private List siglaProblems;
	private List siglaResponsibles;
	private List siglaAddedResponsibles;
	private List fenixProblems;

	/**
	 * 
	 */
	public ResponsiblesDataLoader() {
		setSiglaAddedResponsibles(new ArrayList());
		setSiglaProblems(new ArrayList());
		setFenixProblems(new ArrayList());

	}

	public static void main(String[] args) {
		ResponsiblesDataLoader loader = new ResponsiblesDataLoader();
		PersistenceBroker broker =
			PersistenceBrokerFactory.defaultPersistenceBroker();
		loader.updateFenix(loader, broker);
		loader.printProblems(loader);
		broker.close();
	}

	/**
	 * 
	 */
	private void printProblems(ResponsiblesDataLoader loader) {
		loader.setSiglaProblems(
			loader.addAllToListWithoutRepetitions(
				loader.getSiglaProblems(),
				(List) CollectionUtils.subtract(
					loader.getSiglaResponsibles(),
					loader.getSiglaAddedResponsibles())));
		System.out.println("disciplinas do sigla com problemas");
		loader.printSiglaProblems(loader);
		//		System.out.println("disciplinas execucao do fenix com problemas");
		//		loader.printFenixProblems(loader);
	}

	/**
	 * @param loader
	 */
	private void printFenixProblems(ResponsiblesDataLoader loader) {
		Iterator iterator = loader.getFenixProblems().iterator();
		while (iterator.hasNext()) {
			IDisciplinaExecucao element = (IDisciplinaExecucao) iterator.next();
			System.out.println(element);
		}
	}

	/**
	 * @param loader
	 */
	private void printSiglaProblems(ResponsiblesDataLoader loader) {
		Iterator iter = loader.getSiglaProblems().iterator();
		while (iter.hasNext()) {
			Responsavel element = (Responsavel) iter.next();
			System.out.println(element);
		}

	}

	/**
	 * @param broker
	 */
	private void loadSiglaResponsibles(
		PersistenceBroker broker,
		ResponsiblesDataLoader loader) {
		Criteria crit = new Criteria();
		crit.addEqualTo("semestre", "1");
		crit.addNotEqualTo("no_mec", new Integer(0));
		Query query = new QueryByCriteria(Responsavel.class, crit);
		List siglaResponsibles = (List) broker.getCollectionByQuery(query);
		System.out.println(
			"finished loading Sigla Responsibles! size->"
				+ siglaResponsibles.size());
		loader.setSiglaResponsibles(siglaResponsibles);
	}

	private Integer sigla2FenixDegreeNumbers(Integer siglaDegreeNumber) {
		if (siglaDegreeNumber.intValue() == 24) {
			siglaDegreeNumber = new Integer(51);
		}
		return siglaDegreeNumber;
	}

	private Integer fenix2SiglaFenixDegreeNumbers(Integer siglaDegreeNumber) {
		if (siglaDegreeNumber.intValue() == 51) {
			siglaDegreeNumber = new Integer(24);
		}
		return siglaDegreeNumber;
	}

	private void updateFenix(
		ResponsiblesDataLoader loader,
		PersistenceBroker broker) {

		System.out.println("A carregar responsaveis do sigla");
		loader.loadSiglaResponsibles(broker, loader);
		List fenixExecutionCourses =
			loader.loadFenixExecutionCourses(broker, loader);
		Iterator iterFenix = fenixExecutionCourses.iterator();
		while (iterFenix.hasNext()) {
			loader.updateExecutionCourse(
				(IDisciplinaExecucao) iterFenix.next(),
				broker,
				loader);
		}
	}

	/**
	 * @param executionCourse
	 * @param broker
	 * @param loader
	 */
	private void updateExecutionCourse(
		IDisciplinaExecucao executionCourse,
		PersistenceBroker broker,
		ResponsiblesDataLoader loader) {
		List siglaResponsibles =
			loader.getSiglaResponsibles(executionCourse, broker, loader);
		loader.writeResponsibles(
			executionCourse,
			siglaResponsibles,
			broker,
			loader);
	}

	/**
	 * @param executionCourse
	 * @param siglaResponsibles2
	 * @param broker
	 * @param loader
	 */
	private void writeResponsibles(
		IDisciplinaExecucao executionCourse,
		List siglaResponsibles,
		PersistenceBroker broker,
		ResponsiblesDataLoader loader) {
		List fenixResponsibles =
			loader.getFenixResponsibles(executionCourse, broker, loader);
		List responsiblesToDelete =
			loader.getResponsiblesToDelete(
				fenixResponsibles,
				siglaResponsibles);
		siglaResponsibles =
			loader.getResponsiblesToWrite(fenixResponsibles, siglaResponsibles);

		loader.updateProblems(
			executionCourse,
			fenixResponsibles,
			siglaResponsibles,
			responsiblesToDelete,
			loader);
		loader.deleteResponsibles(responsiblesToDelete, broker, loader);
		Iterator iter = siglaResponsibles.iterator();
		while (iter.hasNext()) {
			Responsavel element = (Responsavel) iter.next();
			
				loader.writeResponsible(
					executionCourse,
					element,
					broker,
					loader);
			
		}
	}

	/**
	 * @param fenixResponsibles
	 * @param siglaResponsibles2
	 * @param responsiblesToDelete
	 * @param loader
	 */
	private void updateProblems(
		IDisciplinaExecucao executionCourse,
		List fenixResponsibles,
		List siglaResponsibles2,
		List responsiblesToDelete,
		ResponsiblesDataLoader loader) {
		if ((fenixResponsibles == null || fenixResponsibles.isEmpty())
			&& (siglaResponsibles2 == null || siglaResponsibles2.isEmpty())) {
			loader.addToFenixProblems(executionCourse, loader);
		}

	}

	/**
	 * @param executionCourse
	 */
	private void addToFenixProblems(
		IDisciplinaExecucao executionCourse,
		ResponsiblesDataLoader loader) {
		if (!loader.getFenixProblems().contains(executionCourse)) {
			loader.getFenixProblems().add(executionCourse);
		}

	}

	/**
	 * @param responsiblesToDelete
	 * @param broker
	 * @param loader
	 */
	private void deleteResponsibles(
		List responsiblesToDelete,
		PersistenceBroker broker,
		ResponsiblesDataLoader loader) {
		if (responsiblesToDelete != null && !responsiblesToDelete.isEmpty()) {
			Iterator iter = responsiblesToDelete.iterator();
			broker.beginTransaction();
			while (iter.hasNext()) {
				ResponsibleFor element = (ResponsibleFor) iter.next();
				broker.delete(element);
			}
			broker.commitTransaction();
		}
	}

	/**
	 * @param fenixResponsibles
	 * @param siglaResponsibles2
	 * @return
	 */
	private List getResponsiblesToDelete(
		List fenixResponsibles,
		List siglaResponsibles2) {
		if (siglaResponsibles2==null || siglaResponsibles2.isEmpty()) {
			return null;
		}else {
			return fenixResponsibles;
		}
		
	}

	/**
	 * @param fenixResponsibles
	 * @param siglaResponsibles
	 * @return
	 */
	private List getResponsiblesToWrite(
		List fenixResponsibles,
		List siglaResponsibles) {
		// TODO Auto-generated method stub
		return siglaResponsibles;
	}

	/**
	 * @param executionCourse
	 * @param broker
	 * @param loader
	 * @return
	 */
	private List getFenixResponsibles(
		IDisciplinaExecucao executionCourse,
		PersistenceBroker broker,
		ResponsiblesDataLoader loader) {
		Criteria crit = new Criteria();
		crit.addEqualTo(
			"executionCourse.idInternal",
			executionCourse.getIdInternal());
		Query query = new QueryByCriteria(ResponsibleFor.class, crit);
		List fenixResponsibles = (List) broker.getCollectionByQuery(query);
		return fenixResponsibles;
	}

	/**
	 * @param executionCourse
	 * @param element
	 * @param broker
	 * @param loader
	 */
	private void writeResponsible(
		IDisciplinaExecucao executionCourse,
		Responsavel siglaResponsible,
		PersistenceBroker broker,
		ResponsiblesDataLoader loader) {
		Criteria crit = new Criteria();
		crit.addEqualTo("teacherNumber", siglaResponsible.getNo_mec());
		Query query = new QueryByCriteria(Teacher.class, crit);
		ITeacher teacher = (ITeacher) broker.getObjectByQuery(query);
		if (teacher == null) {
			loader.addToSiglaProblems(siglaResponsible, loader);
		} else {
			IResponsibleFor responsibleFor =
				new ResponsibleFor(teacher, executionCourse);
			IProfessorship professorship =
				new Professorship(teacher, executionCourse);
			loader.isResponsibleForInDB(responsibleFor, broker, loader);
			if (!loader.isResponsibleForInDB(responsibleFor, broker, loader)) {
				broker.beginTransaction();
				broker.store(responsibleFor);
				broker.commitTransaction();
			}
			if (!loader.isProfessorhipInDB(professorship, broker, loader)) {
				broker.beginTransaction();
				broker.store(professorship);
				broker.commitTransaction();
			}

		}

	}

	/**
	 * @param responsibleFor
	 * @param broker
	 * @param loader
	 */
	private boolean isResponsibleForInDB(
		IResponsibleFor responsibleFor,
		PersistenceBroker broker,
		ResponsiblesDataLoader loader) {
		Criteria crit = new Criteria();
		crit.addEqualTo(
			"teacher.idInternal",
			responsibleFor.getTeacher().getIdInternal());
		crit.addEqualTo(
			"executionCourse.idInternal",
			responsibleFor.getExecutionCourse().getIdInternal());
		Query query = new QueryByCriteria(ResponsibleFor.class, crit);
		IResponsibleFor responsibleFor2 =
			(ResponsibleFor) broker.getObjectByQuery(query);
		return responsibleFor2 != null;

	}

	private boolean isProfessorhipInDB(
		IProfessorship professorship,
		PersistenceBroker broker,
		ResponsiblesDataLoader loader) {
		Criteria crit = new Criteria();
		crit.addEqualTo(
			"teacher.idInternal",
			professorship.getTeacher().getIdInternal());
		crit.addEqualTo(
			"executionCourse.idInternal",
			professorship.getExecutionCourse().getIdInternal());
		Query query = new QueryByCriteria(Professorship.class, crit);
		IProfessorship professorship2 =
			(Professorship) broker.getObjectByQuery(query);
		return professorship2 != null;

	}

	/**
	 * @param siglaResponsible
	 * @param loader
	 */
	private void addToSiglaProblems(
		Responsavel siglaResponsible,
		ResponsiblesDataLoader loader) {
		if (!loader.getSiglaProblems().contains(siglaResponsible)) {
			loader.getSiglaProblems().add(siglaResponsible);
		}

	}

	/**
	 * @param executionCourse
	 * @param broker
	 * @param loader
	 * @return
	 */
	private List getSiglaResponsibles(
		IDisciplinaExecucao executionCourse,
		PersistenceBroker broker,
		ResponsiblesDataLoader loader) {
		List curricularCourses =
			executionCourse.getAssociatedCurricularCourses();
		Iterator iter = curricularCourses.iterator();
		List siglaResponsibles = new ArrayList();
		while (iter.hasNext()) {
			ICurricularCourse curricularCourse =
				(ICurricularCourse) iter.next();
			siglaResponsibles =
				loader.addAllToListWithoutRepetitions(
					siglaResponsibles,
					(List) CollectionUtils.select(
						loader.getSiglaResponsibles(),
						new PredicateForSiglaResponsible(curricularCourse)));			
			loader.setSiglaAddedResponsibles(
				loader.addAllToListWithoutRepetitions(
					loader.getSiglaAddedResponsibles(),
					siglaResponsibles));
		}
		return siglaResponsibles;
	}

	/**
	 * @param siglaResponsibles2
	 * @param collection
	 * @return
	 */
	private List addAllToListWithoutRepetitions(
		List listToReturn,
		List listToAdd) {
		Iterator iter = listToAdd.iterator();
		while (iter.hasNext()) {
			Object element = iter.next();
			if (!listToReturn.contains(element)) {
				listToReturn.add(element);
			}
		}
		return listToReturn;
	}

	/**
	 * @param broker
	 * @param loader
	 * @return
	 */
	private List loadFenixExecutionCourses(
		PersistenceBroker broker,
		ResponsiblesDataLoader loader) {
		Criteria crit1 = new Criteria();
		crit1.addEqualTo("state", PeriodState.CURRENT);
		Query query1 = new QueryByCriteria(ExecutionPeriod.class, crit1);
		IExecutionPeriod executionPeriod =
			(IExecutionPeriod) broker.getObjectByQuery(query1);
		Criteria crit2 = new Criteria();
		crit2.addEqualTo(
			"executionPeriod.idInternal",
			executionPeriod.getIdInternal());
		Query query2 = new QueryByCriteria(DisciplinaExecucao.class, crit2);
		List executionCourses = (List) broker.getCollectionByQuery(query2);
		return executionCourses;
	}

	/**
	 * @param string
	 */
	private static boolean sigla2FenixBasic(String string) {
		return string.equals("True");
	}

	/**
	 * @return
	 */
	public List getSiglaProblems() {
		return siglaProblems;
	}

	/**
	 * @param siglaProblems
	 */
	public void setSiglaProblems(List siglaProblems) {
		this.siglaProblems = siglaProblems;
	}

	/**
	 * @return
	 */
	public List getSiglaResponsibles() {
		return siglaResponsibles;
	}

	/**
	 * @param siglaResponsibles
	 */
	public void setSiglaResponsibles(List siglaResponsibles) {
		this.siglaResponsibles = siglaResponsibles;
	}

	/**
	 * @return
	 */
	public List getSiglaAddedResponsibles() {
		return siglaAddedResponsibles;
	}

	/**
	 * @param siglaAddedResponsibles
	 */
	public void setSiglaAddedResponsibles(List siglaAddedResponsibles) {
		this.siglaAddedResponsibles = siglaAddedResponsibles;
	}

	/**
	 * @return
	 */
	public List getFenixProblems() {
		return fenixProblems;
	}

	/**
	 * @param fenixProblems
	 */
	public void setFenixProblems(List fenixProblems) {
		this.fenixProblems = fenixProblems;
	}

}
