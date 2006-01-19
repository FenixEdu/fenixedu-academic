/**
* Aug 31, 2005
*/
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DislocatedStudent;
import net.sourceforge.fenixedu.domain.District;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCountry;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Ricardo Rodrigues
 *
 */

public class WriteDislocatedStudentAnswer implements IService {

    public void run(Integer studentID, Integer countryID, Integer dislocatedCountryID, Integer districtID) throws ExcepcaoPersistencia{
        
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentCountry persistentCountry = sp.getIPersistentCountry();
        IPersistentStudent persistentStudent = sp.getIPersistentStudent();
        
        Student student = (Student) persistentStudent.readByOID(Student.class, studentID);
        Country country = (Country) persistentCountry.readByOID(Country.class, countryID);
        Country dislocatedCountry = null;
        if(dislocatedCountryID != null){
            dislocatedCountry = (Country) persistentCountry.readByOID(Country.class, dislocatedCountryID);
        }
        District district = (District) sp.getIPersistentObject().readByOID(District.class, districtID);
        
        DislocatedStudent dislocatedStudent = DomainFactory.makeDislocatedStudent();
        dislocatedStudent.setStudent(student);
        dislocatedStudent.setCountry(country);
        dislocatedStudent.setDislocatedCountry(dislocatedCountry);
        dislocatedStudent.setDistrict(district);
    }
}


