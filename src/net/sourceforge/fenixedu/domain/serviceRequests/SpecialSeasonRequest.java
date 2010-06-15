package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ExecuteFactoryMethod;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.RegistrationAcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.dataTransferObject.student.ManageStudentStatuteBean;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentStatute;
import net.sourceforge.fenixedu.domain.student.StudentStatuteType;
import net.sourceforge.fenixedu.domain.student.StudentStatute.CreateStudentStatuteFactory;

public class SpecialSeasonRequest extends SpecialSeasonRequest_Base {
    
    public  SpecialSeasonRequest() {
        super();
    }
    
    public SpecialSeasonRequest(final RegistrationAcademicServiceRequestCreateBean bean) {
	this();
	super.init(bean);
	
	super.setBeginExecutionPeriod(bean.getExecutionYear().getFirstExecutionPeriod());
	super.setEndExecutionPeriod(bean.getExecutionYear().getLastExecutionPeriod());
    }
    
    @Override
    protected void createAcademicServiceRequestSituations(AcademicServiceRequestBean academicServiceRequestBean) {
	super.createAcademicServiceRequestSituations(academicServiceRequestBean);

	if(academicServiceRequestBean.isNew()) {
	    AcademicServiceRequestSituation.create(this, new AcademicServiceRequestBean(
		    AcademicServiceRequestSituationType.PROCESSING, academicServiceRequestBean.getEmployee()));
	}
	
	if(academicServiceRequestBean.isToConclude()) {
	    AcademicServiceRequestSituation.create(this, new AcademicServiceRequestBean(
		    AcademicServiceRequestSituationType.DELIVERED, academicServiceRequestBean.getEmployee()));
	}
	
    }
    
    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
	super.internalChangeState(academicServiceRequestBean);
	
	if(academicServiceRequestBean.isToConclude()) {
	    
	    Student student = getRegistration().getStudent();
 
	    new StudentStatute(student, StudentStatuteType.SPECIAL_SEASON_GRANTED_BY_REQUEST,
		    getBeginExecutionPeriod(), getEndExecutionPeriod());
	}
	
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
	return false;
    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
	return AcademicServiceRequestType.SPECIAL_SEASON_REQUEST;
    }

    @Override
    public EventType getEventType() {
	return null;
    }

    @Override
    public boolean hasPersonalInfo() {
	return false;
    }

    @Override
    public boolean isPayedUponCreation() {
	return false;
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
	return false;
    }

    @Override
    public boolean isToPrint() {
	return false;
    }
    
}
