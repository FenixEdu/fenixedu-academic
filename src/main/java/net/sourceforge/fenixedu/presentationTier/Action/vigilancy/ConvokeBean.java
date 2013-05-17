package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper;
import net.sourceforge.fenixedu.domain.vigilancy.strategies.UnavailableInformation;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class ConvokeBean extends VigilantGroupBean implements Serializable {

    private String emailMessage = "";

    private List<VigilantWrapper> vigilantsSugestion;

    private WrittenEvaluation writtenEvaluation;

    private ExecutionCourse selectedExecutionCourse;

    private List<VigilantWrapper> unavailableVigilants;

    private List<VigilantWrapper> selectedTeachers;

    private List<VigilantWrapper> teachersForAGivenCourse;

    private List<VigilantWrapper> selectedUnavailableVigilants;

    private List<UnavailableInformation> unavailableInformation;

    public List<UnavailableInformation> getUnavailableInformation() {
        return unavailableInformation;
    }

    public void setUnavailableInformation(List<UnavailableInformation> unavailableInformation) {
        this.unavailableInformation = unavailableInformation;
    }

    ConvokeBean() {
        this.vigilantsSugestion = new ArrayList<VigilantWrapper>();
        this.unavailableVigilants = new ArrayList<VigilantWrapper>();
        this.selectedUnavailableVigilants = new ArrayList<VigilantWrapper>();
        this.selectedTeachers = new ArrayList<VigilantWrapper>();
        this.teachersForAGivenCourse = new ArrayList<VigilantWrapper>();
        setWrittenEvaluation(null);
        setSelectedExecutionCourse(null);
    }

    public ExecutionCourse getSelectedExecutionCourse() {
        return selectedExecutionCourse;
    }

    public void setSelectedExecutionCourse(ExecutionCourse course) {
        selectedExecutionCourse = course;
    }

    public WrittenEvaluation getWrittenEvaluation() {
        return this.writtenEvaluation;
    }

    public void setWrittenEvaluation(WrittenEvaluation writtenEvaluation) {
        this.writtenEvaluation = writtenEvaluation;
    }

    public List<VigilantWrapper> getVigilantsSugestion() {
        List<VigilantWrapper> vigilants = new ArrayList<VigilantWrapper>();
        for (VigilantWrapper vigilant : this.vigilantsSugestion) {
            if (vigilant != null) {
                vigilants.add(vigilant);
            }
        }
        return vigilants;
    }

    public void setVigilantsSugestion(List<VigilantWrapper> vigilantsList) {
        this.vigilantsSugestion = new ArrayList<VigilantWrapper>();
        for (VigilantWrapper vigilant : vigilantsList) {
            if (vigilant != null) {
                this.vigilantsSugestion.add(vigilant);
            }
        }
    }

    public List<VigilantWrapper> getSelectedTeachers() {
        List<VigilantWrapper> vigilants = new ArrayList<VigilantWrapper>();
        for (VigilantWrapper vigilant : this.selectedTeachers) {
            if (vigilant != null) {
                vigilants.add(vigilant);
            }
        }
        return vigilants;
    }

    public void setSelectedTeachers(List<VigilantWrapper> vigilantsList) {
        this.selectedTeachers = new ArrayList<VigilantWrapper>();
        for (VigilantWrapper vigilant : vigilantsList) {
            if (vigilant != null) {
                this.selectedTeachers.add(vigilant);
            }
        }
    }

    public List<VigilantWrapper> getTeachersForAGivenCourse() {
        List<VigilantWrapper> vigilants = new ArrayList<VigilantWrapper>();
        for (VigilantWrapper vigilant : this.teachersForAGivenCourse) {
            if (vigilant != null) {
                vigilants.add(vigilant);
            }
        }
        return vigilants;
    }

    public void setTeachersForAGivenCourse(List<VigilantWrapper> vigilantsList) {
        this.teachersForAGivenCourse = new ArrayList<VigilantWrapper>();
        for (VigilantWrapper vigilant : vigilantsList) {
            if (vigilant != null) {
                this.teachersForAGivenCourse.add(vigilant);
            }
        }
    }

    public String getEmailMessage() {
        return emailMessage;
    }

    public void setEmailMessage(String emailMessage) {
        this.emailMessage = emailMessage;
    }

    public List<VigilantWrapper> getUnavailableVigilants() {
        List<VigilantWrapper> vigilants = new ArrayList<VigilantWrapper>();
        for (VigilantWrapper vigilant : this.unavailableVigilants) {
            if (vigilant != null) {
                vigilants.add(vigilant);
            }
        }
        return vigilants;
    }

    public void setUnavailableVigilants(List<VigilantWrapper> vigilantList) {
        this.unavailableVigilants = new ArrayList<VigilantWrapper>();
        for (VigilantWrapper vigilant : vigilantList) {
            if (vigilant != null) {
                this.unavailableVigilants.add(vigilant);
            }
        }
    }

    public List<VigilantWrapper> getSelectedUnavailableVigilants() {
        List<VigilantWrapper> vigilants = new ArrayList<VigilantWrapper>();
        for (VigilantWrapper vigilant : this.selectedUnavailableVigilants) {
            if (vigilant != null) {
                vigilants.add(vigilant);
            }
        }
        return vigilants;
    }

    public void setSelectedUnavailableVigilants(List<VigilantWrapper> vigilantList) {
        this.selectedUnavailableVigilants = new ArrayList<VigilantWrapper>();
        for (VigilantWrapper vigilant : vigilantList) {
            if (vigilant != null) {
                this.selectedUnavailableVigilants.add(vigilant);
            }
        }
    }

    public String getVigilantsAsString() {

        String vigilants = "";
        for (Object object : this.getVigilants()) {
            VigilantWrapper vigilant = (VigilantWrapper) object;
            vigilants +=
                    vigilant.getPerson().getName() + " (" + vigilant.getPerson().getUsername() + ") " + "-"
                            + vigilant.getPerson().getEmail() + "\n";
        }
        return vigilants;
    }

    public String getTeachersAsString() {
        String teachers = "";
        for (Object object : this.getTeachersForAGivenCourse()) {
            VigilantWrapper vigilant = (VigilantWrapper) object;
            teachers +=
                    vigilant.getPerson().getName() + " (" + vigilant.getPerson().getUsername() + ") " + "-"
                            + vigilant.getPerson().getEmail() + "\n";
        }
        return teachers;
    }

    public String getRoomsAsString() {

        String rooms = "";
        for (AllocatableSpace room : this.getWrittenEvaluation().getAssociatedRooms()) {
            rooms +=
                    room.getName() + "-" + RenderUtils.getResourceString("VIGILANCY_RESOURCES", "label.vigilancy.capacity") + ":"
                            + room.getCapacidadeExame() + "\n";
        }
        return rooms;
    }

    public List<VigilantWrapper> getTeachersInAGivenConvokeProvider() {
        List<VigilantWrapper> teachersForAGivenCourse = this.getTeachersForAGivenCourse();
        WrittenEvaluation writtenEvaluation = this.getWrittenEvaluation();

        if (writtenEvaluation != null && writtenEvaluation.getVigilancies().size() > 0) {
            for (Vigilancy convoke : writtenEvaluation.getVigilancies()) {
                teachersForAGivenCourse.remove(convoke.getVigilantWrapper());
            }

        }
        return teachersForAGivenCourse;
    }
}
