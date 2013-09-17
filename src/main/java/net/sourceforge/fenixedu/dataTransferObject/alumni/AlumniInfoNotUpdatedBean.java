package net.sourceforge.fenixedu.dataTransferObject.alumni;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.accessControl.NotUpdatedAlumniInfoForSpecificTimeGroup;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import pt.ist.fenixframework.Atomic;

public class AlumniInfoNotUpdatedBean implements Serializable {

    private Integer daysNotUpdated;
    private Boolean professionalInfo;
    private Boolean formationInfo;
    private Boolean personalDataInfo;

    public AlumniInfoNotUpdatedBean(Integer daysNotUpdated, Boolean professionalInfo, Boolean formationInfo) {
        setDaysNotUpdated(daysNotUpdated);
        setProfessionalInfo(professionalInfo);
        setFormationInfo(formationInfo);
    }

    public AlumniInfoNotUpdatedBean() {
    }

    public Integer getDaysNotUpdated() {
        return daysNotUpdated;
    }

    public void setDaysNotUpdated(Integer daysNotUpdated) {
        this.daysNotUpdated = daysNotUpdated;
    }

    public Boolean getProfessionalInfo() {
        return professionalInfo;
    }

    public void setProfessionalInfo(Boolean professionalInfo) {
        this.professionalInfo = professionalInfo;
    }

    public Boolean getFormationInfo() {
        return formationInfo;
    }

    public void setFormationInfo(Boolean formationInfo) {
        this.formationInfo = formationInfo;
    }

    public Boolean getPersonalDataInfo() {
        return personalDataInfo;
    }

    public void setPersonalDataInfo(Boolean personalDataInfo) {
        this.personalDataInfo = personalDataInfo;
    }

    @Atomic
    public void createRecipientGroup(Sender sender) {
        NotUpdatedAlumniInfoForSpecificTimeGroup recipientsGroup =
                new NotUpdatedAlumniInfoForSpecificTimeGroup(getDaysNotUpdated(), getProfessionalInfo(), getFormationInfo(),
                        getPersonalDataInfo());
        Recipient recipients = Recipient.newInstance(recipientsGroup);
        sender.addRecipients(recipients);
    }
}
