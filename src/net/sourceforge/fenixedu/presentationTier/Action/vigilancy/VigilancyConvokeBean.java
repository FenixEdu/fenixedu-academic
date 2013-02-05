package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper;

public class VigilancyConvokeBean implements Serializable {

    private Unit unit;

    private WrittenEvaluation writtenEvaluation;

    private VigilantGroup vigilantGroup;

    private Collection<VigilantWrapper> vigilantsSugestion;

    private Boolean convokeIsFinal;

    private Integer numberOfVigilantsToSelect;

    public Unit getUnit() {
        return this.unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public WrittenEvaluation getWrittenEvaluation() {
        return this.writtenEvaluation;
    }

    public void setWrittenEvaluation(WrittenEvaluation writtenEvaluation) {
        this.writtenEvaluation = writtenEvaluation;
    }

    public VigilantGroup getVigilantGroup() {
        return this.vigilantGroup;
    }

    public void setVigilantGroup(VigilantGroup vigilantGroup) {
        this.vigilantGroup = vigilantGroup;
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

    public List<VigilantWrapper> getVigilantsSugestion() {
        List vigilants = new ArrayList<VigilantWrapper>();
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

}
