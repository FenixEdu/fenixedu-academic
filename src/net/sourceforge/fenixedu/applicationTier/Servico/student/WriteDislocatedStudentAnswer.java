package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DislocatedStudent;
import net.sourceforge.fenixedu.domain.District;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class WriteDislocatedStudentAnswer extends Service {

    public void run(Integer studentID, Integer countryID, Integer dislocatedCountryID, Integer districtID) throws ExcepcaoPersistencia{
        Registration registration = rootDomainObject.readRegistrationByOID(studentID);
        Country country = rootDomainObject.readCountryByOID(countryID);
        Country dislocatedCountry = null;
        if(dislocatedCountryID != null){
            dislocatedCountry = rootDomainObject.readCountryByOID(dislocatedCountryID);
        }
        District district = rootDomainObject.readDistrictByOID(districtID);
        
        DislocatedStudent dislocatedStudent = new DislocatedStudent();
        dislocatedStudent.setStudent(registration);
        dislocatedStudent.setCountry(country);
        dislocatedStudent.setDislocatedCountry(dislocatedCountry);
        dislocatedStudent.setDistrict(district);
    }
    
}
