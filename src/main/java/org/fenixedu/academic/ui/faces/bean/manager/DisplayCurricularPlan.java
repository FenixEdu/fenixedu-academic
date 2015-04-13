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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.fenixedu.academic.domain.CurricularCourseScope.DegreeModuleScopeCurricularCourseScope;
import org.fenixedu.academic.domain.DegreeModuleScope;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.dto.CurricularCourseScopesForPrintDTO;
import org.fenixedu.academic.dto.InfoCurricularCourseScope;
import org.fenixedu.academic.dto.InfoDegreeCurricularPlan;
import org.fenixedu.academic.dto.InfoExecutionYear;
import org.fenixedu.academic.service.services.commons.ReadActiveDegreeCurricularPlansByDegreeType;
import org.fenixedu.academic.service.services.commons.ReadExecutionYearByID;
import org.fenixedu.academic.service.services.commons.ReadNotClosedExecutionYears;
import org.fenixedu.academic.service.services.commons.curriculumHistoric.ReadActiveCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear;
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

        List degreeCurricularPlans = (List) ReadActiveDegreeCurricularPlansByDegreeType.run(DegreeType::isPreBolonhaDegree);

        List<SelectItem> result = new ArrayList<SelectItem>(degreeCurricularPlans.size());
        for (InfoDegreeCurricularPlan degreeCurricularPlan : (List<InfoDegreeCurricularPlan>) degreeCurricularPlans) {
            String label = degreeCurricularPlan.getInfoDegree().getNome() + " - " + degreeCurricularPlan.getName();
            result.add(new SelectItem(degreeCurricularPlan.getExternalId(), label));
        }

        return result;
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

    public List getScopes() throws FenixServiceException {

        List<InfoCurricularCourseScope> scopes = new ArrayList<InfoCurricularCourseScope>();

        for (String degreeCurricularPlanID : this.getChoosenDegreeCurricularPlansIDs()) {
            Collection<DegreeModuleScope> degreeModuleScopes =
                    ReadActiveCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear.run(degreeCurricularPlanID,
                            this.choosenExecutionYearID);

            for (final DegreeModuleScope degreeModuleScope : degreeModuleScopes) {
                if (degreeModuleScope instanceof DegreeModuleScopeCurricularCourseScope) {
                    scopes.add(InfoCurricularCourseScope
                            .newInfoFromDomain(((DegreeModuleScopeCurricularCourseScope) degreeModuleScope)
                                    .getCurricularCourseScope()));
                }
            }
        }

        sortScopes(scopes);

        CurricularCourseScopesForPrintDTO scopesForPrintDTO = new CurricularCourseScopesForPrintDTO();
        for (InfoCurricularCourseScope scope : scopes) {
            scopesForPrintDTO.add(scope);
        }

        return scopesForPrintDTO.getDegreeCurricularPlans();
    }

    private void sortScopes(List<InfoCurricularCourseScope> scopes) {
        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.infoCurricularYear.year"));
        comparatorChain.addComparator(new BeanComparator("infoBranch.name"));
        comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.semester"));
        comparatorChain.addComparator(new BeanComparator("infoCurricularCourse.name"));
        Collections.sort(scopes, comparatorChain);
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