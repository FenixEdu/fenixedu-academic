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
package org.fenixedu.academic.ui.faces.bean.manager;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.dto.InfoDegreeCurricularPlan;
import org.fenixedu.academic.dto.InfoExecutionYear;
import org.fenixedu.academic.service.services.commons.ReadActiveDegreeCurricularPlansByDegreeType;
import org.fenixedu.academic.service.services.commons.ReadExecutionYearByID;
import org.fenixedu.academic.service.services.commons.ReadNotClosedExecutionYears;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.faces.bean.base.FenixBackingBean;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DisplayCurricularPlan extends FenixBackingBean {

    private String[] choosenDegreeCurricularPlansIDs;

    private String choosenExecutionYearID;

    public String choose() {
        return "success";
    }

    public List getDegreeCurricularPlans() throws FenixServiceException {

        return List.of();
    }

    public List getExecutionYears() throws FenixServiceException {

        List<InfoExecutionYear> executionYears = ReadNotClosedExecutionYears.run();

        List<SelectItem> result = new ArrayList<SelectItem>(executionYears.size());
        for (InfoExecutionYear executionYear : executionYears) {
            result.add(new SelectItem(executionYear.getExternalId(), executionYear.getYear()));
        }

        if (executionYears.size() > 0) {
            setChoosenExecutionYearID(executionYears.get(executionYears.size() - 1).getExternalId());
        }

        return result;
    }

    public String getChoosenExecutionYear() throws FenixServiceException {

        InfoExecutionYear executionYear = ReadExecutionYearByID.run(getChoosenExecutionYearID());

        return executionYear.getYear();
    }

    public String getChoosenExecutionYearID() {
        return choosenExecutionYearID;
    }

    public void setChoosenExecutionYearID(String choosenExecutionYearID) {
        this.choosenExecutionYearID = choosenExecutionYearID;
    }

    public String[] getChoosenDegreeCurricularPlansIDs() {
        return choosenDegreeCurricularPlansIDs;
    }

    public void setChoosenDegreeCurricularPlansIDs(String[] choosenDegreeCurricularPlansIDs) {
        this.choosenDegreeCurricularPlansIDs = choosenDegreeCurricularPlansIDs;
    }

}