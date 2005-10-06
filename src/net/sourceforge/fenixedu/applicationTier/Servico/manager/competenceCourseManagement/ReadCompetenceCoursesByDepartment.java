package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourse;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ICompetenceCourse;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCompetenceCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDepartment;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadCompetenceCoursesByDepartment implements IService{
	
	public List<InfoCompetenceCourse> run(Integer departmentID) throws Exception {
		ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();
		List<InfoCompetenceCourse> result = new ArrayList<InfoCompetenceCourse>();
		if(departmentID != null) {
			IPersistentDepartment persistentDepartment = suportePersistente.getIDepartamentoPersistente();
			IDepartment department = (IDepartment) persistentDepartment.readByOID(Department.class, departmentID);
			if(department == null) {
				throw new NotExistingServiceException("error.manager.noDepartment");
			}
			for (ICompetenceCourse course : department.getCompetenceCourses()) {
				result.add(InfoCompetenceCourse.newInfoFromDomain(course));
			}
		}
		else {
			//read competence course with no associated department
			IPersistentCompetenceCourse persistentCompetenceCourse = suportePersistente.getIPersistentCompetenceCourse();
			List<ICompetenceCourse> allCompetenceCourses = (List<ICompetenceCourse>) persistentCompetenceCourse.readAll(CompetenceCourse.class);
			List<ICompetenceCourse> noDeptCompetenceCourse = (List<ICompetenceCourse>) CollectionUtils.select(allCompetenceCourses, new Predicate() {

				public boolean evaluate(Object arg0) {
					ICompetenceCourse competenceCourse = (ICompetenceCourse) arg0;
					return !competenceCourse.hasAnyDepartments();
				}
				
			});
			
			for (ICompetenceCourse course : noDeptCompetenceCourse) {
				result.add(InfoCompetenceCourse.newInfoFromDomain(course));
			}
		}
		return result;
	}

}
