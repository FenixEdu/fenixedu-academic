package net.sourceforge.fenixedu.dataTransferObject.alumni;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accessControl.ConclusionYearDegreesStudentsGroup;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import pt.ist.fenixWebFramework.services.Service;

public class AlumniMailSendToBean implements Serializable {

    private ExecutionYear endExecutionYear;
    private DegreeType degreeType;
    private List<Degree> degrees;

    public AlumniMailSendToBean(DegreeType degreeType, List<Degree> degrees, ExecutionYear endYear) {
        setDegreeType(degreeType);
        setDegrees(degrees);
        setEndExecutionYear(endYear);
    }

    public AlumniMailSendToBean() {
    }

    public ExecutionYear getEndExecutionYear() {
        return this.endExecutionYear;
    }

    public void setEndExecutionYear(ExecutionYear executionYear) {
        this.endExecutionYear = executionYear;
    }

    public DegreeType getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(DegreeType arg) {
        this.degreeType = arg;
    }

    public List<Degree> getDegrees() {
        return this.degrees;
    }

    public void setDegrees(List<Degree> degrees) {
        this.degrees = degrees;
    }

    @Service
    public void createRecipientGroup(Sender sender) {
        ConclusionYearDegreesStudentsGroup recipientsGroup =
                new ConclusionYearDegreesStudentsGroup(getDegrees(), getEndExecutionYear());
        Recipient recipients = Recipient.newInstance(recipientsGroup);
        sender.addRecipients(recipients);
    }
}
