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
package net.sourceforge.fenixedu.presentationTier.backBeans.gep;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionYears;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.gep.ListMasterDegreeStudents;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ListFirstTimeEnrolmentMasterDegreeStudents extends FenixBackingBean {

    private String selectedExecutionYear;

    public ListFirstTimeEnrolmentMasterDegreeStudents() {
        super();
    }

    public Collection getStudentCurricularPlans() throws FenixServiceException {

        if (getSelectedExecutionYear() == null || getSelectedExecutionYear().length() == 0) {
            return new ArrayList();
        }

        return ListMasterDegreeStudents.run(getSelectedExecutionYear());
    }

    public List<SelectItem> getExecutionYears() throws FenixServiceException {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<InfoExecutionYear> executionYears = ReadNotClosedExecutionYears.run();

        Collections.sort(executionYears, new Comparator<InfoExecutionYear>() {

            @Override
            public int compare(InfoExecutionYear o1, InfoExecutionYear o2) {
                return o1.getYear().compareTo(o2.getYear()) * (-1);
            }

        });

        for (InfoExecutionYear executionYear : executionYears) {
            result.add(new SelectItem(executionYear.getYear(), executionYear.getYear()));
        }

        return result;
    }

    public String getSelectedExecutionYear() {
        return selectedExecutionYear;
    }

    public void setSelectedExecutionYear(String selectedExecutionYear) {
        this.selectedExecutionYear = selectedExecutionYear;
    }

}