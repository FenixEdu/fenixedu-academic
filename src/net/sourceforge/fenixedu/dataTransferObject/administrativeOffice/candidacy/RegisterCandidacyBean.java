/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.student.EnrolmentModel;

import org.joda.time.YearMonthDay;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RegisterCandidacyBean implements Serializable {

    private EnrolmentModel enrolmentModel;

    private YearMonthDay startDate = new YearMonthDay();

    private DomainReference<StudentCandidacy> candidacy;

    public RegisterCandidacyBean(StudentCandidacy candidacy) {
	super();
	this.candidacy = new DomainReference<StudentCandidacy>(candidacy);
    }

    public EnrolmentModel getEnrolmentModel() {
	return enrolmentModel;
    }

    public void setEnrolmentModel(EnrolmentModel enrolmentModel) {
	this.enrolmentModel = enrolmentModel;
    }

    public YearMonthDay getStartDate() {
	return startDate;
    }

    public void setStartDate(YearMonthDay startDate) {
	this.startDate = startDate;
    }

    public StudentCandidacy getCandidacy() {
	return candidacy == null ? null : candidacy.getObject();
    }

}
