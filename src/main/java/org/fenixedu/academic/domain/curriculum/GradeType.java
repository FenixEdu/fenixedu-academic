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
package org.fenixedu.academic.domain.curriculum;

import java.math.BigDecimal;
import java.util.List;

import org.fenixedu.academic.domain.GradeScaleEnum;

@Deprecated
public enum GradeType {
    GRADETWENTY {
        @Override
        public IGrade average(List<IGrade> grades) {
            long sum = numericSum(grades);

            BigDecimal divider = (new BigDecimal(sum)).divide(new BigDecimal(grades.size()), BigDecimal.ROUND_HALF_EVEN);
            return GradeFactory.getInstance().getGrade((int) divider.longValue());
        }
    },

    GRADEFIVE {
        @Override
        public IGrade average(List<IGrade> grades) {
            long sum = numericSum(grades);

            BigDecimal divider = (new BigDecimal(sum)).divide(new BigDecimal(grades.size()), BigDecimal.ROUND_HALF_EVEN);
            return GradeFactory.getInstance().getGrade((int) divider.longValue());
        }
    },

    GRADEAP {
        @Override
        public IGrade average(List<IGrade> grades) {
            return GradeFactory.getInstance().getGrade(GradeScaleEnum.AP);
        }
    },

    GRADERE {
        @Override
        public IGrade average(List<IGrade> grades) {
            return GradeFactory.getInstance().getGrade(GradeScaleEnum.RE);
        }
    },

    GRADENA {
        @Override
        public IGrade average(List<IGrade> grades) {
            return GradeFactory.getInstance().getGrade(GradeScaleEnum.NA);
        }
    };

    protected long numericSum(List<IGrade> grades) {
        long sum = 0;

        for (IGrade grade : grades) {
            sum += ((Integer) grade.getGradeValue()).intValue();
        }

        return sum;
    }

    public abstract IGrade average(List<IGrade> grades);
}
