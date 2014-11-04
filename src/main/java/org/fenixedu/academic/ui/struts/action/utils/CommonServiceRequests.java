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
/*
 * Created on Apr 3, 2004
 *
 */
package org.fenixedu.academic.ui.struts.action.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.fenixedu.academic.domain.branch.BranchType;
import org.fenixedu.academic.dto.InfoBranch;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.dto.InfoExecutionYear;
import org.fenixedu.academic.service.services.commons.ReadExecutionDegreeByOID;
import org.fenixedu.academic.service.services.commons.ReadExecutionYearsService;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.manager.ReadBranchesByDegreeCurricularPlan;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.bennu.core.domain.User;

/**
 * @author Luis Cruz
 * 
 */
public class CommonServiceRequests {

    public static List getBranchesByDegreeCurricularPlan(User userView, String degreeCurricularPlanOID)
            throws FenixActionException {

        List branches = null;
        try {
            branches = ReadBranchesByDegreeCurricularPlan.run(degreeCurricularPlanOID);
        } catch (FenixServiceException fse) {
            throw new FenixActionException(fse);
        }

        List newBranches = new ArrayList();
        if (branches != null) {
            Iterator iterator = branches.iterator();
            while (iterator.hasNext()) {
                InfoBranch infoBranch = (InfoBranch) iterator.next();
                if (infoBranch != null && infoBranch.getBranchType() != null
                        && !infoBranch.getBranchType().equals(BranchType.COMNBR)) {
                    newBranches.add(infoBranch);
                }
            }
        }
        return newBranches;
    }

    /**
     * @param degreeOID
     * @return
     */
    public static InfoExecutionDegree getInfoExecutionDegree(User userView, String degreeOID) {
        InfoExecutionDegree infoExecutionDegree = null;

        infoExecutionDegree = ReadExecutionDegreeByOID.run(degreeOID);
        return infoExecutionDegree;
    }

    public static List<InfoExecutionYear> getInfoExecutionYears() throws FenixServiceException {
        return ReadExecutionYearsService.run();
    }

}