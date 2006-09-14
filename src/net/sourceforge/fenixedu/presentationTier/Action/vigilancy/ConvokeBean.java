package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;

public class ConvokeBean extends VigilantGroupBean implements Serializable {

    private String emailMessage = "";

    private List<DomainReference<Vigilant>> vigilantsSugestion;

    private DomainReference<WrittenEvaluation> writtenEvaluation;

    private DomainReference<ExecutionCourse> selectedExecutionCourse;

    private List<DomainReference<Vigilant>> unavailableVigilants;

    private List<DomainReference<Vigilant>> selectedTeachers;

    private List<DomainReference<Vigilant>> teachersForAGivenCourse;

    private List<DomainReference<Vigilant>> selectedUnavailableVigilants;

    private TemporalInformationType temporalInformation;

    ConvokeBean() {
        this.vigilantsSugestion = new ArrayList<DomainReference<Vigilant>>();
        this.unavailableVigilants = new ArrayList<DomainReference<Vigilant>>();
        this.selectedUnavailableVigilants = new ArrayList<DomainReference<Vigilant>>();
        this.selectedTeachers = new ArrayList<DomainReference<Vigilant>>();
        this.teachersForAGivenCourse = new ArrayList<DomainReference<Vigilant>>();
        setWrittenEvaluation(null);
        setSelectedExecutionCourse(null);
    }

    public ExecutionCourse getSelectedExecutionCourse() {
        return selectedExecutionCourse.getObject();
    }

    public void setSelectedExecutionCourse(ExecutionCourse course) {
        selectedExecutionCourse = new DomainReference<ExecutionCourse>(course);
    }

    public WrittenEvaluation getWrittenEvaluation() {
        return (this.writtenEvaluation == null) ? null : this.writtenEvaluation.getObject();
    }

    public void setWrittenEvaluation(WrittenEvaluation writtenEvaluation) {
        this.writtenEvaluation = (writtenEvaluation != null) ? new DomainReference<WrittenEvaluation>(
                writtenEvaluation) : null;
    }

    public List<Vigilant> getVigilantsSugestion() {
        List vigilants = new ArrayList<Vigilant>();
        for (DomainReference<Vigilant> vigilant : this.vigilantsSugestion) {
            if (vigilant != null)
                vigilants.add(vigilant.getObject());
        }
        return vigilants;
    }

    public void setVigilantsSugestion(List<Vigilant> vigilantsList) {
        this.vigilantsSugestion = new ArrayList<DomainReference<Vigilant>>();
        for (Vigilant vigilant : vigilantsList) {
            if (vigilant != null) {
                this.vigilantsSugestion.add(new DomainReference(vigilant));
            }
        }
    }

    public List<Vigilant> getSelectedTeachers() {
        List vigilants = new ArrayList<Vigilant>();
        for (DomainReference<Vigilant> vigilant : this.selectedTeachers) {
            if (vigilant != null)
                vigilants.add(vigilant.getObject());
        }
        return vigilants;
    }

    public void setSelectedTeachers(List<Vigilant> vigilantsList) {
        this.selectedTeachers = new ArrayList<DomainReference<Vigilant>>();
        for (Vigilant vigilant : vigilantsList) {
            if (vigilant != null) {
                this.selectedTeachers.add(new DomainReference(vigilant));
            }
        }
    }

    public List<Vigilant> getTeachersForAGivenCourse() {
        List vigilants = new ArrayList<Vigilant>();
        for (DomainReference<Vigilant> vigilant : this.teachersForAGivenCourse) {
            if (vigilant != null)
                vigilants.add(vigilant.getObject());
        }
        return vigilants;
    }

    public void setTeachersForAGivenCourse(List<Vigilant> vigilantsList) {
        this.teachersForAGivenCourse = new ArrayList<DomainReference<Vigilant>>();
        for (Vigilant vigilant : vigilantsList) {
            if (vigilant != null) {
                this.teachersForAGivenCourse.add(new DomainReference(vigilant));
            }
        }
    }

    public String getEmailMessage() {
        return emailMessage;
    }

    public void setEmailMessage(String emailMessage) {
        this.emailMessage = emailMessage;
    }

    public List<Vigilant> getUnavailableVigilants() {
        List vigilants = new ArrayList<Vigilant>();
        for (DomainReference<Vigilant> vigilant : this.unavailableVigilants) {
            if (vigilant != null)
                vigilants.add(vigilant.getObject());
        }
        return vigilants;
    }

    public void setUnavailableVigilants(List<Vigilant> vigilantList) {
        this.unavailableVigilants = new ArrayList<DomainReference<Vigilant>>();
        for (Vigilant vigilant : vigilantList) {
            if (vigilant != null) {
                this.unavailableVigilants.add(new DomainReference(vigilant));
            }
        }
    }

    public List<Vigilant> getSelectedUnavailableVigilants() {
        List vigilants = new ArrayList<Vigilant>();
        for (DomainReference<Vigilant> vigilant : this.selectedUnavailableVigilants) {
            if (vigilant != null)
                vigilants.add(vigilant.getObject());
        }
        return vigilants;
    }

    public void setSelectedUnavailableVigilants(List<Vigilant> vigilantList) {
        this.selectedUnavailableVigilants = new ArrayList<DomainReference<Vigilant>>();
        for (Vigilant vigilant : vigilantList) {
            if (vigilant != null) {
                this.selectedUnavailableVigilants.add(new DomainReference(vigilant));
            }
        }
    }

    public TemporalInformationType getTemporalInformation() {
        return temporalInformation;
    }

    public void setTemporalInformation(TemporalInformationType temporalInformation) {
        this.temporalInformation = temporalInformation;
    }

    public String getWhatSchemaToUseInVigilants() {
        boolean unavailables = this.isShowUnavailables();
        boolean incompatibilities = this.isShowIncompatibilities();

        if (unavailables) {
            if (incompatibilities) {
                return "vigilantsWithAllInformation";
            } else {
                return "vigilantsWithoutIncompatibilities";
            }
        } else {
            if (incompatibilities) {
                return "vigilantsWithOutUnavailables";
            } else {
                return "simpleVigilants";
            }
        }

    }
}
