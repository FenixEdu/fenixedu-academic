package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;

public class VigilancyConvokeBean implements Serializable {

    private DomainReference<Unit> unit;

    private DomainReference<WrittenEvaluation> writtenEvaluation;

    private DomainReference<VigilantGroup> vigilantGroup;

    private Collection<DomainReference<Vigilant>> vigilantsSugestion;

    private Boolean convokeIsFinal;

    private Integer numberOfVigilantsToSelect;

    public Unit getUnit() {
        return (this.unit != null) ? this.unit.getObject() : null;
    }

    public void setUnit(Unit unit) {
        this.unit = (unit != null) ? new DomainReference<Unit>(unit) : null;
    }

    public WrittenEvaluation getWrittenEvaluation() {
        return (this.writtenEvaluation == null) ? null : this.writtenEvaluation.getObject();
    }

    public void setWrittenEvaluation(WrittenEvaluation writtenEvaluation) {
        this.writtenEvaluation = (writtenEvaluation != null) ? new DomainReference<WrittenEvaluation>(
                writtenEvaluation) : null;
    }

    public VigilantGroup getVigilantGroup() {
        return (this.vigilantGroup == null) ? null : this.vigilantGroup.getObject();
    }

    public void setVigilantGroup(VigilantGroup vigilantGroup) {
        this.vigilantGroup = (vigilantGroup != null) ? new DomainReference<VigilantGroup>(vigilantGroup)
                : null;
    }

    public Boolean getConvokeIsFinal() {
        return convokeIsFinal;
    }

    public void setConvokeIsFinal(Boolean convokeIsFinal) {
        this.convokeIsFinal = convokeIsFinal;
    }

    public Integer getNumberOfVigilantsToSelect() {
        return numberOfVigilantsToSelect;
    }

    public void setNumberOfVigilantsToSelect(Integer numberOfVigilantsToSelect) {
        this.numberOfVigilantsToSelect = numberOfVigilantsToSelect;
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

}
