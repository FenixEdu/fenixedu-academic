package net.sourceforge.fenixedu.applicationTier.Servico.teacher.teacherService;




import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.teacher.distribution.DistributionTeacherServicesByTeachersDTO;
import net.sourceforge.fenixedu.dataTransferObject.teacher.distribution.DistributionTeacherServicesByTeachersDTO.TeacherDistributionServiceEntryDTO;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.ICurricularSemester;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;


/**
 * 
 * @author jpmsit, amak
 */ 
public class ReadTeacherServiceDistributionByTeachers implements IService {
	
	
	
	public List run(String username, Integer executionYearID) throws FenixServiceException {
		
		DistributionTeacherServicesByTeachersDTO returnDTO = new DistributionTeacherServicesByTeachersDTO();
		
		List<ITeacher> teachers;
		
		List<IExecutionPeriod> executionPeriodList;
		
		try {
							
			ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
			
			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
									
			IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
			
			executionPeriodList = persistentExecutionPeriod.readByExecutionYear(executionYearID);
						
			IDepartment department = persistentTeacher.readTeacherByUsername(username).getLastWorkingDepartment();
			
			teachers = department.getTeachers();
			
		} catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
		
							
		/* Information about teacher */
		Double hoursSpentByTeacher;
				
		
		for(ITeacher teacher : teachers){
			returnDTO.addTeacher(teacher.getIdInternal(), teacher.getTeacherNumber(), teacher.getCategory().getCode(), teacher.getPerson().getNome(), 0, 0d);
			
			for(IProfessorship professorShip : teacher.getProfessorships()){
				IExecutionCourse executionCourse = professorShip.getExecutionCourse();
				
				if(!executionPeriodList.contains(executionCourse.getExecutionPeriod())){
					continue;
				}
				
				Set<String> curricularYears = new HashSet<String>();
				Set<String> degreeNames = new HashSet<String>();
				for(ICurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()){
					degreeNames.add(curricularCourse.getDegreeCurricularPlan().getDegree().getSigla());
					
					for (ICurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
						 ICurricularSemester curricularSemester = curricularCourseScope.getCurricularSemester();
						 curricularYears.add(curricularSemester.getCurricularYear().getYear().toString());
					}
				}
				
				hoursSpentByTeacher = StrictMath.ceil(teacher.getHoursLecturedOnExecutionCourse(executionCourse));
				
				returnDTO.addExecutionCourseToTeacher(teacher.getIdInternal(), executionCourse.getIdInternal(), 
						executionCourse.getNome(), 
						hoursSpentByTeacher.intValue(), degreeNames, curricularYears, executionCourse.getExecutionPeriod().getName());
				
			}
		}
					
		ArrayList<TeacherDistributionServiceEntryDTO> returnArraylist = new ArrayList<TeacherDistributionServiceEntryDTO>();
		
		for(TeacherDistributionServiceEntryDTO teacher: returnDTO.getTeachersMap().values()) {
			returnArraylist.add(teacher);
		}
		
		Collections.sort(returnArraylist);
		
		return returnArraylist;
	}
	
	
}


