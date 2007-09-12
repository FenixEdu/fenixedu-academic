package net.sourceforge.fenixedu.domain.serviceRequest;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;

public abstract class CurricularCourseRequest extends CurricularCourseRequest_Base {
    
    public  CurricularCourseRequest() {
        super();
    }
    
    @Override
    public Set<AdministrativeOfficeType> getPossibleAdministrativeOffices() {
	Set<AdministrativeOfficeType> types = new HashSet<AdministrativeOfficeType>();
	types.add(AdministrativeOfficeType.MASTER_DEGREE);
	return types;
    }
}
