package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.domain.vigilancy.strategies.UnavailableInformation;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class ConvokeBean extends VigilantGroupBean implements Serializable {

    private String emailMessage = "";

    private List<DomainReference<Vigilant>> vigilantsSugestion;

    private DomainReference<WrittenEvaluation> writtenEvaluation;

    private DomainReference<ExecutionCourse> selectedExecutionCourse;

    private List<DomainReference<Vigilant>> unavailableVigilants;

    private List<DomainReference<Vigilant>> selectedTeachers;

    private List<DomainReference<Vigilant>> teachersForAGivenCourse;

    private List<DomainReference<Vigilant>> selectedUnavailableVigilants;

    private List<UnavailableInformation> unavailableInformation;
    
    
    public List<UnavailableInformation> getUnavailableInformation() {
		return unavailableInformation;
	}

	public void setUnavailableInformation(List<UnavailableInformation> unavailableInformation) {
		this.unavailableInformation = unavailableInformation;
	}

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
        List<Vigilant> vigilants = new ArrayList<Vigilant>();
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
                this.vigilantsSugestion.add(new DomainReference<Vigilant>(vigilant));
            }
        }
    }

    public List<Vigilant> getSelectedTeachers() {
        List<Vigilant> vigilants = new ArrayList<Vigilant>();
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
                this.selectedTeachers.add(new DomainReference<Vigilant>(vigilant));
            }
        }
    }

    public List<Vigilant> getTeachersForAGivenCourse() {
        List<Vigilant> vigilants = new ArrayList<Vigilant>();
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
                this.teachersForAGivenCourse.add(new DomainReference<Vigilant>(vigilant));
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
        List<Vigilant> vigilants = new ArrayList<Vigilant>();
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
                this.unavailableVigilants.add(new DomainReference<Vigilant>(vigilant));
            }
        }
    }

    public List<Vigilant> getSelectedUnavailableVigilants() {
        List<Vigilant> vigilants = new ArrayList<Vigilant>();
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
                this.selectedUnavailableVigilants.add(new DomainReference<Vigilant>(vigilant));
            }
        }
    }

    public String getVigilantsAsString() {
    	
    	String vigilants = "";
    	for(Object object : this.getVigilants()) {
    		Vigilant  vigilant = (Vigilant) object;
    		vigilants += vigilant.getPerson().getName() + " (" + vigilant.getPerson().getUsername() + ") " + "-" + vigilant.getPerson().getEmail() + "\n"; 
    	}
    	return vigilants;
    }
    
    public String getTeachersAsString() {
    	String teachers="";
    	for(Object object : this.getTeachersForAGivenCourse()) {
    		Vigilant  vigilant = (Vigilant) object;
    		teachers += vigilant.getPerson().getName() + " (" + vigilant.getPerson().getUsername() + ") " + "-" + vigilant.getPerson().getEmail() + "\n"; 
    	}
    	return teachers;
    }
    
    public String getRoomsAsString() {
    	
    	String rooms="";
    	for(OldRoom room : this.getWrittenEvaluation().getAssociatedRooms()) {
    		rooms += room.getName() + "-" + RenderUtils.getResourceString("VIGILANCY_RESOURCES", "label.vigilancy.capacity") + ":"+ room.getCapacidadeExame() + "\n";
    	}
    	return rooms;
    }
}
