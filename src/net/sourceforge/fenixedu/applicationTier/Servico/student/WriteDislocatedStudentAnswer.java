/**
* Aug 31, 2005
*/
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.District;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ICountry;
import net.sourceforge.fenixedu.domain.IDislocatedStudent;
import net.sourceforge.fenixedu.domain.IDistrict;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCountry;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDistrict;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 *
 */

public class WriteDislocatedStudentAnswer implements IService {

    public void run(Integer studentID, Integer countryID, Integer dislocatedCountryID, Integer districtID) throws ExcepcaoPersistencia{
        
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentCountry persistentCountry = sp.getIPersistentCountry();
        IPersistentDistrict persistentDistrict = sp.getIPersistentDistrict();
        IPersistentStudent persistentStudent = sp.getIPersistentStudent();
        
        IStudent student = (IStudent) persistentStudent.readByOID(Student.class, studentID);
        ICountry country = (ICountry) persistentCountry.readByOID(Country.class, countryID);
        ICountry dislocatedCountry = null;
        if(dislocatedCountryID != null){
            dislocatedCountry = (ICountry) persistentCountry.readByOID(Country.class, dislocatedCountryID);
        }
        IDistrict district = (IDistrict) persistentDistrict.readByOID(District.class, districtID);
        
        IDislocatedStudent dislocatedStudent = DomainFactory.makeDislocatedStudent();
        dislocatedStudent.setStudent(student);
        dislocatedStudent.setCountry(country);
        dislocatedStudent.setDislocatedCountry(dislocatedCountry);
        dislocatedStudent.setDistrict(district);
    }
}


