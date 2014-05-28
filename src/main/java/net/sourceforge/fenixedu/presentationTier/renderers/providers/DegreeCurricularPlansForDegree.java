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
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementBaseBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeCurricularPlansForDegree implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        final MarkSheetManagementBaseBean markSheetManagementBean = (MarkSheetManagementBaseBean) source;
        final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();

        if (markSheetManagementBean.getDegree() != null && markSheetManagementBean.getExecutionPeriod() != null) {

            Set<Degree> availableDegrees =
                    AcademicAuthorizationGroup.getDegreesForOperation(AccessControl.getPerson(), AcademicOperationType.MANAGE_MARKSHEETS);

            for (final DegreeCurricularPlan dcp : markSheetManagementBean.getDegree().getDegreeCurricularPlansSet()) {
                if (availableDegrees.contains(dcp.getDegree())) {
                    if (hasExecutionDegreeForYear(markSheetManagementBean, dcp)
                            || canSubmitImprovementMarksheets(markSheetManagementBean, dcp)) {
                        result.add(dcp);
                    }
                }
            }
        }
        Collections.sort(result, DegreeCurricularPlan.COMPARATOR_BY_NAME);
        return result;
    }

    private boolean canSubmitImprovementMarksheets(final MarkSheetManagementBaseBean markSheetManagementBean,
            final DegreeCurricularPlan degreeCurricularPlan) {
        return degreeCurricularPlan.canSubmitImprovementMarkSheets(markSheetManagementBean.getExecutionPeriod()
                .getExecutionYear());
    }

    private boolean hasExecutionDegreeForYear(final MarkSheetManagementBaseBean markSheetManagementBean,
            final DegreeCurricularPlan degreeCurricularPlan) {
        return degreeCurricularPlan.hasAnyExecutionDegreeFor(markSheetManagementBean.getExecutionYear());
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }
}
