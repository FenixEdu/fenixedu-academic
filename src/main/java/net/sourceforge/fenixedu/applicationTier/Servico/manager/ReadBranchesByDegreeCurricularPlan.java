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
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import pt.ist.fenixframework.FenixFramework;

public class ReadBranchesByDegreeCurricularPlan {

    public static List<InfoBranch> run(String idDegreeCurricularPlan) throws FenixServiceException {
        DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(idDegreeCurricularPlan);
        if (degreeCurricularPlan == null) {
            throw new NonExistingServiceException();
        }

        Collection<Branch> allBranches = degreeCurricularPlan.getAreas();
        if (allBranches == null || allBranches.isEmpty()) {
            return null;
        }

        List<InfoBranch> result = new ArrayList<InfoBranch>(allBranches.size());
        for (Branch branch : allBranches) {
            result.add(InfoBranch.newInfoFromDomain(branch));
        }
        return result;
    }

}