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

/**
 * @author Ricardo Rodrigues
 *
 */

public class WriteDislocatedStudentAnswer extends Service {

    public void run(Integer studentID, Integer countryID, Integer dislocatedCountryID, Integer districtID) throws ExcepcaoPersistencia{
        Student student = (Student) persistentObject.readByOID(Student.class, studentID);
        Country country = (Country) persistentObject.readByOID(Country.class, countryID);
        Country dislocatedCountry = null;
        if(dislocatedCountryID != null){
            dislocatedCountry = (Country) persistentObject.readByOID(Country.class, dislocatedCountryID);
        }
        District district = (District) persistentObject.readByOID(District.class, districtID);
        
        DislocatedStudent dislocatedStudent = DomainFactory.makeDislocatedStudent();
        dislocatedStudent.setStudent(student);
        dislocatedStudent.setCountry(country);
        dislocatedStudent.setDislocatedCountry(dislocatedCountry);
        dislocatedStudent.setDistrict(district);
    }
}


