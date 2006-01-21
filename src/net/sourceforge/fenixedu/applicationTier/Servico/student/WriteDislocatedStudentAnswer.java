/**
* Aug 31, 2005
*/
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DislocatedStudent;
import net.sourceforge.fenixedu.domain.District;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCountry;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;

/**
 * @author Ricardo Rodrigues
 *
 */

public class WriteDislocatedStudentAnswer extends Service {

    public void run(Integer studentID, Integer countryID, Integer dislocatedCountryID, Integer districtID) throws ExcepcaoPersistencia{
        IPersistentCountry persistentCountry = persistentSupport.getIPersistentCountry();
        IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();
        
        Student student = (Student) persistentStudent.readByOID(Student.class, studentID);
        Country country = (Country) persistentCountry.readByOID(Country.class, countryID);
        Country dislocatedCountry = null;
        if(dislocatedCountryID != null){
            dislocatedCountry = (Country) persistentCountry.readByOID(Country.class, dislocatedCountryID);
        }
        District district = (District) persistentObject.readByOID(District.class, districtID);
        
        DislocatedStudent dislocatedStudent = DomainFactory.makeDislocatedStudent();
        dislocatedStudent.setStudent(student);
        dislocatedStudent.setCountry(country);
        dislocatedStudent.setDislocatedCountry(dislocatedCountry);
        dislocatedStudent.setDistrict(district);
    }
}


