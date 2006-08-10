/*
 * Created on Dec 22, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.student.InfoSenior;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Senior;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Egidio, luis.egidio@ist.utl.pt
 * 
 */
public class ReadSeniorInfoByUsername extends Service {

	public InfoSenior run(IUserView userView) throws FenixServiceException, ExcepcaoPersistencia {
		Person person = Person.readPersonByUsername(userView.getUtilizador());
		Registration student = person.getStudentByType(DegreeType.DEGREE);

		Senior senior = student.getSenior();

		InfoSenior readInfoSenior = null;
		if (senior == null)
			return readInfoSenior;

		readInfoSenior = new InfoSenior();
		readInfoSenior.setIdInternal(senior.getIdInternal());
		readInfoSenior.setName(person.getNome());
		readInfoSenior.setAddress(person.getMorada());
		readInfoSenior.setAreaCode(person.getCodigoPostal());
		readInfoSenior.setAreaCodeArea(person.getLocalidadeCodigoPostal());
		readInfoSenior.setPhone(person.getTelefone());
		readInfoSenior.setMobilePhone(person.getTelemovel());
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