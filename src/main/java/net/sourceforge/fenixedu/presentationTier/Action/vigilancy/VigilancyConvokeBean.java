/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
