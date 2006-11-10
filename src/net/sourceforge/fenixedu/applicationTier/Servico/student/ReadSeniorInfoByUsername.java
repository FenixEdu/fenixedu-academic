/*
 * Created on Dec 22, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.student.InfoSenior;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Senior;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Egidio, luis.egidio@ist.utl.pt
 * 
 */
public class ReadSeniorInfoByUsername extends Service {

	public InfoSenior run(IUserView userView) throws FenixServiceException, ExcepcaoPersistencia {
	    	final Person person = Person.readPersonByUsername(userView.getUtilizador());
		final Registration registration = person.getStudentByType(DegreeType.DEGREE);
                if (registration == null) {
                    return null;
                }
                final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
                if (studentCurricularPlan == null) {
                    return null;
                }
                final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
                final Degree degree = degreeCurricularPlan.getDegree();
		Senior senior = registration.getSenior();
		if (senior == null) {
                    final int curricularYear = registration.getCurricularYear();
                    System.out.println("curricularYear: " + curricularYear);
                    if (curricularYear == degree.getDegreeType().getYears()) {
                        senior = new Senior();
                        senior.setStudent(registration);
                    } else {
                        return null;
                    }
		}

		final InfoSenior readInfoSenior = new InfoSenior();
		readInfoSenior.setIdInternal(senior.getIdInternal());
		readInfoSenior.setName(person.getNome());
		readInfoSenior.setAddress(person.getAddress());
		readInfoSenior.setAreaCode(person.getAreaCode());
		readInfoSenior.setAreaCodeArea(person.getAreaOfAreaCode());
		readInfoSenior.setPhone(person.getPhone());
		readInfoSenior.setMobilePhone(person.getMobile());
		readInfoSenior.setEmail(person.getEmail());
		readInfoSenior.setAvailablePhoto(person.getAvailablePhoto());
		readInfoSenior.setPersonID(person.getIdInternal());
		readInfoSenior.setExpectedDegreeTermination(senior.getExpectedDegreeTermination());
		readInfoSenior.setExpectedDegreeAverageGrade(senior.getExpectedDegreeAverageGrade());
		readInfoSenior.setSpecialtyField(senior.getSpecialtyField());
		readInfoSenior.setProfessionalInterests(senior.getProfessionalInterests());
		readInfoSenior.setLanguageSkills(senior.getLanguageSkills());
		readInfoSenior.setInformaticsSkills(senior.getInformaticsSkills());
		readInfoSenior.setExtracurricularActivities(senior.getExtracurricularActivities());
		readInfoSenior.setProfessionalExperience(senior.getProfessionalExperience());
		readInfoSenior.setLastModificationDate(senior.getLastModificationDate());

		return readInfoSenior;
	}
	
}