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
/*
 * Created on Oct 14, 2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.MasterDegreeThesis;

/**
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class InfoMasterDegreeThesis extends InfoObject {
    private InfoStudentCurricularPlan infoStudentCurricularPlan;

    public void setInfoStudentCurricularPlan(InfoStudentCurricularPlan infoStudentCurricularPlan) {
        this.infoStudentCurricularPlan = infoStudentCurricularPlan;
    }

    public InfoStudentCurricularPlan getInfoStudentCurricularPlan() {
        return infoStudentCurricularPlan;
    }

    @Override
    public String toString() {
        String result = "[" + this.getClass().getName() + ": \n";
        result += "externalId = " + getExternalId() + "; \n";
        result += "infoStudentCurricularPlan = " + this.infoStudentCurricularPlan.getExternalId() + "; \n";
        result += "] \n";

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof InfoMasterDegreeThesis) {
            InfoMasterDegreeThesis infoMasterDegreeThesis = (InfoMasterDegreeThesis) obj;
            result = this.infoStudentCurricularPlan.equals(infoMasterDegreeThesis.getInfoStudentCurricularPlan());
        }
        return result;
    }

    public static InfoMasterDegreeThesis newInfoFromDomain(MasterDegreeThesis masterDegreeThesis) {
        InfoMasterDegreeThesis infoMasterDegreeThesis = null;
        if (masterDegreeThesis != null) {
            infoMasterDegreeThesis = new InfoMasterDegreeThesis();
            infoMasterDegreeThesis.copyFromDomain(masterDegreeThesis);
        }
        return infoMasterDegreeThesis;
    }

    public void copyFromDomain(MasterDegreeThesis masterDegreeThesis) {
        super.copyFromDomain(masterDegreeThesis);
        if (masterDegreeThesis != null) {
            setInfoStudentCurricularPlan(InfoStudentCurricularPlan.newInfoFromDomain(masterDegreeThesis
                    .getStudentCurricularPlan()));
        }
    }

}