package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourse;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCompetenceCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDepartment;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import net.sourceforge.fenixedu.applicationTier.Service;

public class ReadCompetenceCoursesByDepartment extends Service{
	
	public List<InfoCompetenceCourse> run(Integer departmentID) throws Exception {
		ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();
		List<InfoCompetenceCourse> result = new ArrayList<InfoCompetenceCourse>();
		if(departmentID != null) {
			IPersistentDepartment persistentDepartment = suportePersistente.getIDepartamentoPersistente();
			Department department = (Department) persistentDepartment.readByOID(Department.class, departmentID);
			if(department == null) {
				throw new NotExistingServiceException("error.manager.noDepartment");
			}
			for (CompetenceCourse competenceCourse : department.getCompetenceCourses()) {
                if (competenceCourse.getCurricularStage().equals(CurricularStage.OLD)) {
                    result.add(InfoCompetenceCourse.newInfoFromDomain(competenceCourse));    
                }
			}
		}
		else {
			//read competence course with no associated department
			IPersistentCompetenceCourse persistentCompetenceCourse = suportePersistente.getIPersistentCompetenceCourse();
			List<CompetenceCourse> allCompetenceCourses = (List<CompetenceCourse>) persistentCompetenceCourse.readByCurricularStage(CurricularStage.OLD);
			List<CompetenceCourse> noDeptCompetenceCourse = (List<CompetenceCourse>) CollectionUtils.select(allCompetenceCourses, new Predicate() {

				public boolean evaluate(Object arg0) {
					CompetenceCourse competenceCourse = (CompetenceCourse) arg0;
					return !competenceCourse.hasAnyDepartments();
				}
				
			});
			
			for (CompetenceCourse course : noDeptCompetenceCourse) {
				result.add(InfoCompetenceCourse.newInfoFromDomain(course));
			}
		}
		return result;
	}

}
