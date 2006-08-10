package net.sourceforge.fenixedu.domain.research.result.patent;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.joda.time.Partial;

public class ResultPatent extends ResultPatent_Base {

    public enum ResultPatentType {
        
        LOCAL,
        NATIONAL,
        INTERNATIONAL;
              
        public String getName() {
            return name();
        }  
    }
    
    public enum ResultPatentStatus {
        
        /*type defined in CERIF*/
        APPLIED,
        PUBLISHED,
        GRANTED,
        MAINTAINED;
              
        public String getName() {
            return name();
        }  
    }
    
    public  ResultPatent() {
        super();
    }
    
    public ResultPatent(String title, Partial registrationDate, Partial approvalDate) {
        this();
        checkParameters(title, registrationDate, approvalDate);
        setTitle(title);
        setRegistrationDate(registrationDate);
        setApprovalDate(approvalDate);
        
        Person person = AccessControl.getUserView().getPerson();
        setParticipation(person);
        setModificationDateAndAuthor();
    }
    
    public void setPatentEdit(String title, Partial registrationDate, Partial approvalDate){
        checkParameters(title,registrationDate,approvalDate);
        this.setTitle(title);
        this.setRegistrationDate(registrationDate);
        this.setApprovalDate(approvalDate);
        this.setModificationDateAndAuthor();
    }
    
    private void checkParameters(String title, Partial registrationDate, Partial approvalDate) {
        if (title == null || title.equals("")) {
            throw new DomainException("error.ResultPatent.title.cannot.be.null");
        }
        if (registrationDate == null) {
            throw new DomainException("error.ResultPatent.registrationDate.cannot.be.null");
        }
        if (approvalDate == null) {
            throw new DomainException("error.ResultPatent.approvalDate.cannot.be.null");
        }
        if(!isApprovalDateAfterRegistrationDate(approvalDate, registrationDate)){
            throw new DomainException("error.ResultPatent.approvalDate.before.registrationDate");
        }
    }
    
    private boolean isApprovalDateAfterRegistrationDate(Partial approvalDate, Partial registrationDate) {
        if(approvalDate.isEqual(registrationDate)) {
            return false;
        }
        if(approvalDate.isAfter(registrationDate)) {
            return true;
        }
        return false;
    }
    
    /**
     * Method used to call the service responsible for removing a ResultPatent
     * 
     * @param resultId
     * @throws FenixFilterException
     * @throws FenixServiceException
     */
	public static void remove(Integer resultId) throws FenixFilterException, FenixServiceException {
		ServiceUtils.executeService(AccessControl.getUserView(), "DeleteResultPatent", resultId);
	}
}
