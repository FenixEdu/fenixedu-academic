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
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.scholarship.utl.report;

import java.util.List;

import net.sourceforge.fenixedu.domain.QualificationType;

public class QualificationTypes {

    public static final List<QualificationType> MASTER_QUALIFICATION_TYPES = java.util.Arrays.asList(new QualificationType[] {
            QualificationType.INTEGRATED_MASTER_DEGREE, QualificationType.MASTER,
            QualificationType.MASTER_DEGREE_FOREIGNER_WITH_EQUIVALENCE, QualificationType.MASTER_DEGREE_WITH_RECOGNITION });

    public static boolean isMasterQualificationType(QualificationType type) {
        return MASTER_QUALIFICATION_TYPES.contains(type);
    }

    public static final List<QualificationType> PHD_QUALIFICATION_TYPES = java.util.Arrays.asList(new QualificationType[] {
            QualificationType.DOCTORATE_DEGREE, QualificationType.DOCTORATE_DEGREE_BOLOGNA,
            QualificationType.DOCTORATE_DEGREE_FOREIGNER_WITH_EQUIVALENCE, QualificationType.DOCTORATE_DEGREE_WITH_RECOGNITION,
            QualificationType.DOCTORATE_DEGREE_WITH_REGISTER });

    public static boolean isPhdQualificationType(QualificationType type) {
        return PHD_QUALIFICATION_TYPES.contains(type);
    }

    public static final List<QualificationType> DEGREE_QUALIFICATION_TYPES = java.util.Arrays.asList(new QualificationType[] {
            QualificationType.BACHELOR_AND_DEGREE, QualificationType.BACHELOR_DEGREE,
            QualificationType.BACHELOR_DEGREE_FOREIGNER_WITH_EQUIVALENCE, QualificationType.DEGREE,
            QualificationType.DEGREE_FOREIGNER_WITH_EQUIVALENCE, QualificationType.INTEGRATED_MASTER_DEGREE, });

    public static boolean isDegreeQualificationType(QualificationType type) {
        return DEGREE_QUALIFICATION_TYPES.contains(type);
    }

    public static boolean isCETQualificationType(QualificationType type) {
        return false;
    }

}
