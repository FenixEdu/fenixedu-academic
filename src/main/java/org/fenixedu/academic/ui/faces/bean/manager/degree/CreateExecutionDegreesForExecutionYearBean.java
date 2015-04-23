/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.faces.bean.manager.degree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import javax.faces.component.UISelectItems;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.predicate.IllegalDataAccessException;
import org.fenixedu.academic.service.services.manager.CreateExecutionDegreesForExecutionYear;
import org.fenixedu.academic.ui.faces.bean.base.FenixBackingBean;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.Data;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.spaces.domain.Space;

import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CreateExecutionDegreesForExecutionYearBean extends FenixBackingBean {
    private String chosenDegreeType;

    private String[] choosenDegreeCurricularPlansIDs;

    private String[] choosenBolonhaDegreeCurricularPlansIDs;

    private UISelectItems degreeCurricularPlansSelectItems;

    public UISelectItems bolonhaDegreeCurricularPlansSelectItems;

    private String campus;

    private Boolean temporaryExamMap;

    private List<DegreeCurricularPlan> createdDegreeCurricularPlans;

    public CreateExecutionDegreesForExecutionYearBean() {
        super();
    }

    public List<SelectItem> getDegreeTypes() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem("dropDown.Default", BundleUtil.getString(Bundle.ENUMERATION, "dropDown.Default")));
        DegreeType.all().forEach(type -> result.add(new SelectItem(type.getExternalId(), type.getName().getContent())));

        return result;
    }

    public String getChosenDegreeType() {
        return chosenDegreeType;
    }

    public void setChosenDegreeType(String chosenDegreeType) {
        this.chosenDegreeType = chosenDegreeType;
    }

    public UISelectItems getDegreeCurricularPlansSelectItems() {
        if (this.degreeCurricularPlansSelectItems == null) {
            final DegreeType degreeType = getDegreeType(getChosenDegreeType());

            final List<SelectItem> result;
            if (degreeType == null) {
                result = Collections.EMPTY_LIST;
            } else {
                result = new ArrayList<SelectItem>();

                final List<DegreeCurricularPlan> toShow =
                        DegreeCurricularPlan.readByDegreeTypeAndState(Predicate.isEqual(degreeType),
                                DegreeCurricularPlanState.ACTIVE);
                Collections
                        .sort(toShow,
                                DegreeCurricularPlan.DEGREE_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_EXECUTION_DEGREE_AND_DEGREE_CODE);

                for (final DegreeCurricularPlan degreeCurricularPlan : toShow) {
                    result.add(new SelectItem(degreeCurricularPlan.getExternalId(), degreeType.getName().getContent() + " "
                            + degreeCurricularPlan.getDegree().getName() + " - " + degreeCurricularPlan.getName()));
                }
            }
            this.degreeCurricularPlansSelectItems = new UISelectItems();
            this.degreeCurricularPlansSelectItems.setValue(result);
        }

        return this.degreeCurricularPlansSelectItems;
    }

    public void setDegreeCurricularPlansSelectItems(UISelectItems degreeCurricularPlansSelectItems) {
        this.degreeCurricularPlansSelectItems = degreeCurricularPlansSelectItems;
    }

    public UISelectItems getBolonhaDegreeCurricularPlansSelectItems() {
        if (this.bolonhaDegreeCurricularPlansSelectItems == null) {
            final DegreeType bolonhaDegreeType = FenixFramework.getDomainObject(getChosenDegreeType());

            final List<DegreeCurricularPlan> toShow = new ArrayList<DegreeCurricularPlan>();
            for (final Degree degree : Degree.readBolonhaDegrees()) {
                if (degree.getDegreeType() == bolonhaDegreeType) {
                    for (final DegreeCurricularPlan degreeCurricularPlan : degree.getActiveDegreeCurricularPlans()) {
                        if (!degreeCurricularPlan.isDraft()) {
                            toShow.add(degreeCurricularPlan);
                        }
                    }
                }
            }
            Collections.sort(toShow,
                    DegreeCurricularPlan.DEGREE_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_EXECUTION_DEGREE_AND_DEGREE_CODE);

            final List<SelectItem> result = new ArrayList<SelectItem>();
            for (final DegreeCurricularPlan degreeCurricularPlan : toShow) {
                result.add(new SelectItem(degreeCurricularPlan.getExternalId(), bolonhaDegreeType.getName().getContent() + " "
                        + degreeCurricularPlan.getDegree().getName() + " - " + degreeCurricularPlan.getName()));
            }

            this.bolonhaDegreeCurricularPlansSelectItems = new UISelectItems();
            this.bolonhaDegreeCurricularPlansSelectItems.setValue(result);
        }
        return this.bolonhaDegreeCurricularPlansSelectItems;
    }

    public void setBolonhaDegreeCurricularPlansSelectItems(UISelectItems bolonhaDegreeCurricularPlansSelectItems) {
        this.bolonhaDegreeCurricularPlansSelectItems = bolonhaDegreeCurricularPlansSelectItems;
    }

    private DegreeType getDegreeType(final String chosenDegreeType) {
        return FenixFramework.getDomainObject(chosenDegreeType);
    }

    public List getExecutionYears() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (final ExecutionYear executionYear : ExecutionYear.readNotClosedExecutionYears()) {
            result.add(new SelectItem(executionYear.getExternalId(), executionYear.getYear()));
        }
        if (getChoosenExecutionYearID() == null && result.size() > 0) {
            setChoosenExecutionYearID(ExecutionYear.readCurrentExecutionYear().getExternalId());
        }
        return result;
    }

    public void onChoosenExecutionYearChanged(ValueChangeEvent valueChangeEvent) {
        setChoosenExecutionYearID((String) valueChangeEvent.getNewValue());
    }

    public List getAllCampus() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (final Space campus : Space.getAllCampus()) {
            result.add(new SelectItem(campus.getName(), campus.getName()));
        }
        return result;
    }

    public String createExecutionDegrees() throws FenixActionException {
        try {
            createdDegreeCurricularPlans =
                    CreateExecutionDegreesForExecutionYear.run(getChoosenDegreeCurricularPlansIDs(),
                            getChoosenBolonhaDegreeCurricularPlansIDs(), getChoosenExecutionYearID(), getCampus(),
                            !getTemporaryExamMap());
        } catch (IllegalDataAccessException e) {
            throw new FenixActionException(e);
        } catch (DomainException e) {
            addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getKey(), e.getArgs()));
            setChoosenDegreeCurricularPlansIDs(null);
            return "";
        }

        return "success";
    }

    public List<DegreeCurricularPlan> getCreatedDegreeCurricularPlans() {
        return createdDegreeCurricularPlans;
    }

    public List getDays() {
        return Data.getMonthDaysSelectItems();
    }

    public List getMonths() {
        return Data.getMonthsSelectItems();
    }

    public List getYears() {
        return Data.getExpirationYearsSelectItems();
    }

    public String[] getChoosenDegreeCurricularPlansIDs() {
        return choosenDegreeCurricularPlansIDs;
    }

    public void setChoosenDegreeCurricularPlansIDs(String[] choosenDegreeCurricularPlansIDs) {
        this.choosenDegreeCurricularPlansIDs = choosenDegreeCurricularPlansIDs;
    }

    public String[] getChoosenBolonhaDegreeCurricularPlansIDs() {
        return choosenBolonhaDegreeCurricularPlansIDs;
    }

    public void setChoosenBolonhaDegreeCurricularPlansIDs(String[] choosenBolonhaDegreeCurricularPlansIDs) {
        this.choosenBolonhaDegreeCurricularPlansIDs = choosenBolonhaDegreeCurricularPlansIDs;
    }

    public ExecutionYear getChoosenExecutionYear() {
        return FenixFramework.getDomainObject(getChoosenExecutionYearID());
    }

    public String getChoosenExecutionYearID() {
        return (String) this.getViewState().getAttribute("choosenExecutionYearID");
    }

    public void setChoosenExecutionYearID(String choosenExecutionYearID) {
        this.getViewState().setAttribute("choosenExecutionYearID", choosenExecutionYearID);
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public Boolean getTemporaryExamMap() {
        return temporaryExamMap;
    }

    public void setTemporaryExamMap(Boolean temporaryExamMap) {
        this.temporaryExamMap = temporaryExamMap;
    }

}