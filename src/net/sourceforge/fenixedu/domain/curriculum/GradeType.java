package net.sourceforge.fenixedu.domain.curriculum;

import java.math.BigDecimal;
import java.util.List;

import net.sourceforge.fenixedu.domain.GradeScale;

public enum GradeType {
    GRADETWENTY {
        public IGrade average(List<IGrade> grades) {
            long sum = numericSum(grades);

            BigDecimal divider = (new BigDecimal(sum)).divide(new BigDecimal(grades.size()),
                    BigDecimal.ROUND_HALF_EVEN);
            return GradeFactory.getInstance().getGrade((int) divider.longValue());
        }
    },

    GRADEFIVE {
        public IGrade average(List<IGrade> grades) {
            long sum = numericSum(grades);

            BigDecimal divider = (new BigDecimal(sum)).divide(new BigDecimal(grades.size()),
                    BigDecimal.ROUND_HALF_EVEN);
            return GradeFactory.getInstance().getGrade((int) divider.longValue());
        }
    },

    GRADEAP {
        public IGrade average(List<IGrade> grades) {
            return GradeFactory.getInstance().getGrade(GradeScale.AP);
        }
    },

    GRADERE {
        public IGrade average(List<IGrade> grades) {
            return GradeFactory.getInstance().getGrade(GradeScale.RE);
        }
    },

    GRADENA {
        public IGrade average(List<IGrade> grades) {
            return GradeFactory.getInstance().getGrade(GradeScale.NA);
        }
    };

    protected long numericSum(List<IGrade> grades) {
        long sum = 0;

        for (IGrade grade : grades) {
            sum += ((Integer) grade.getGrade()).intValue();
        }

        return sum;
    }

    public abstract IGrade average(List<IGrade> grades);
}
