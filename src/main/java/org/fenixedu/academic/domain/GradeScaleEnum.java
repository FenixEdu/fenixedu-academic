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
package org.fenixedu.academic.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.curriculum.grade.StandardType20AbsoluteGradeScaleLogic;
import org.fenixedu.academic.domain.curriculum.grade.StandardType20GradeScaleLogic;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.EvaluationType;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;

public enum GradeScaleEnum {

    TYPE20(true, new StandardType20GradeScaleLogic()),

    /**
     * Scale in which all numeric values are approvals
     */
    TYPE20_ABSOLUTE(true, new StandardType20AbsoluteGradeScaleLogic() ),

    @Deprecated TYPE5(true, new GradeScaleLogic() {
        @Override
        public boolean checkFinal(final Grade grade) {
            final String value = grade.getValue();
            if (value.equals(NA) || value.equals(RE)) {
                return true;
            }

            try {
                final int intValue = Integer.parseInt(value);
                return intValue >= 3 && intValue <= 5;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        @Override
        public boolean checkNotFinal(final Grade grade) {
            final String value = grade.getValue();
            if (value.equals(NA) || value.equals(RE)) {
                return true;
            }

            try {
                final double doubleValue = Double.parseDouble(value);
                return doubleValue >= 0 && doubleValue <= 20;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        @Override
        public String qualify(final Grade grade) {
            if (grade.getGradeScale() != GradeScaleEnum.TYPE5) {
                return StringUtils.EMPTY;
            }

            try {
                final int intValue = Integer.parseInt(grade.getValue());

                if (intValue == 5) {
                    return BundleUtil.getString(Bundle.APPLICATION, "label.grade.a");
                } else if (intValue == 4) {
                    return BundleUtil.getString(Bundle.APPLICATION, "label.grade.b");
                } else if (intValue == 3) {
                    return BundleUtil.getString(Bundle.APPLICATION, "label.grade.c");
                } else {
                    throw new DomainException("GradeScale.unable.to.qualify.given.grade");
                }
            } catch (NumberFormatException e) {
                throw new DomainException("GradeScale.unable.to.qualify.given.grade");
            }
        }

        @Override
        public boolean isNotEvaluated(final Grade grade) {
            if (grade.isEmpty()) {
                return true;
            }

            return grade.getValue().equals(GradeScaleEnum.NA);
        }

        @Override
        public boolean isNotApproved(final Grade grade) {
            final String value = grade.getValue();
            if (value.equals(GradeScaleEnum.RE) || isNotEvaluated(grade)) {
                return true;
            }

            try {
                return Integer.parseInt(value) < 3;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        @Override
        public boolean isApproved(final Grade grade) {
            try {
                final int intValue = Integer.parseInt(grade.getValue());
                return 3 <= intValue && intValue <= 5;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        @Override
        public boolean belongsTo(final String value) {
            if (value.equals(NA) || value.equals(RE)) {
                return true;
            }

            try {
                final double doubleValue = Double.parseDouble(value);
                return doubleValue >= 0 && doubleValue <= 5;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }),

    @Deprecated TYPEAP(true, new GradeScaleLogic() {
        @Override
        public boolean checkFinal(final Grade grade) {
            final String value = grade.getValue();
            return value.equals(NA) || value.equals(RE) || value.equals(AP);
        }

        @Override
        public boolean checkNotFinal(final Grade grade) {
            final String value = grade.getValue();
            return value.equals(NA) || value.equals(RE) || value.equals(AP);
        }

        @Override
        public String qualify(final Grade grade) {
            if (grade.getGradeScale() != GradeScaleEnum.TYPEAP) {
                return StringUtils.EMPTY;
            }

            final String value = grade.getValue();
            if (value.equals(AP)) {
                return BundleUtil.getString(Bundle.APPLICATION, "msg.approved");
            } else if (value.equals(RE)) {
                return BundleUtil.getString(Bundle.APPLICATION, "msg.notApproved");
            } else if (value.equals(NA)) {
                return BundleUtil.getString(Bundle.APPLICATION, "msg.notEvaluated");
            } else {
                throw new DomainException("GradeScale.unable.to.qualify.given.grade");
            }
        }

        @Override
        public boolean isNotEvaluated(final Grade grade) {
            final String value = grade.getValue();
            return grade.isEmpty() || value.equals(GradeScaleEnum.NA);
        }

        @Override
        public boolean isNotApproved(final Grade grade) {
            final String value = grade.getValue();
            return value.equals(GradeScaleEnum.RE) || isNotEvaluated(grade);
        }

        @Override
        public boolean isApproved(final Grade grade) {
            final String value = grade.getValue();
            return value.equals(GradeScaleEnum.AP);
        }

        @Override
        public boolean belongsTo(final String value) {
            return value.equals(NA) || value.equals(RE) || value.equals(AP);
        }
    }),

    @Deprecated TYPEAPT(false, new GradeScaleLogic() {
        @Override
        public boolean checkFinal(final Grade grade) {
            final String value = grade.getValue();
            return value.equals(NA) || value.equals(RE) || value.equals(APT);
        }

        @Override
        public boolean checkNotFinal(final Grade grade) {
            final String value = grade.getValue();
            return value.equals(NA) || value.equals(RE) || value.equals(APT);
        }

        @Override
        public String qualify(final Grade grade) {
            if (grade.getGradeScale() != GradeScaleEnum.TYPEAPT) {
                return StringUtils.EMPTY;
            }

            final String value = grade.getValue();
            if (value.equals(APT)) {
                return BundleUtil.getString(Bundle.APPLICATION, "msg.apt");
            } else if (value.equals(RE)) {
                return BundleUtil.getString(Bundle.APPLICATION, "msg.notApproved");
            } else if (value.equals(NA)) {
                return BundleUtil.getString(Bundle.APPLICATION, "msg.notEvaluated");
            } else {
                throw new DomainException("GradeScale.unable.to.qualify.given.grade");
            }
        }

        @Override
        public boolean isNotEvaluated(final Grade grade) {
            final String value = grade.getValue();
            return grade.isEmpty() || value.equals(GradeScaleEnum.NA);
        }

        @Override
        public boolean isNotApproved(final Grade grade) {
            final String value = grade.getValue();
            return value.equals(GradeScaleEnum.RE) || isNotEvaluated(grade);
        }

        @Override
        public boolean isApproved(final Grade grade) {
            final String value = grade.getValue();
            return value.equals(GradeScaleEnum.APT);
        }

        @Override
        public boolean belongsTo(final String value) {
            return value.equals(NA) || value.equals(RE) || value.equals(APT);
        }
    }),

    TYPEECTS(false, new GradeScaleLogic() {
        @Override
        public boolean belongsTo(String value) {
            return value.equals("A") || value.equals("B") || value.equals("C") || value.equals("D") || value.equals("E")
                    || value.equals("F") || value.equals(NA);
        }

        @Override
        public boolean checkFinal(Grade grade) {
            return false;
        }

        @Override
        public boolean checkNotFinal(Grade grade) {
            return false;
        }

        @Override
        public boolean isApproved(Grade grade) {
            return !(isNotEvaluated(grade) || isNotApproved(grade));
        }

        @Override
        public boolean isNotApproved(Grade grade) {
            return grade.getValue().equals("F");
        }

        @Override
        public boolean isNotEvaluated(Grade grade) {
            return grade.getValue().equals(NA);
        }

        @Override
        public String qualify(Grade grade) {
            return null;
        }
    }),

    TYPEQUALITATIVE(true, new GradeScaleLogic() {
        @Override
        public boolean belongsTo(String value) {
            return NA.equals(value) || RE.equals(value) || S.equals(value) || B.equals(value) || MB.equals(value)
                    || E.equals(value);
        }

        @Override
        public boolean checkFinal(Grade grade) {
            return false;
        }

        @Override
        public boolean checkNotFinal(Grade grade) {
            return false;
        }

        @Override
        public boolean isApproved(Grade grade) {
            return !(isNotEvaluated(grade) || isNotApproved(grade));
        }

        @Override
        public boolean isNotApproved(Grade grade) {
            return RE.equals(grade.getValue());
        }

        @Override
        public boolean isNotEvaluated(Grade grade) {
            return NA.equals(grade.getValue());
        }

        @Override
        public String qualify(Grade grade) {
            return null;
        }
    });

    public static interface GradeScaleLogic {
        @Deprecated
        boolean checkFinal(final Grade grade);

        @Deprecated
        boolean checkNotFinal(final Grade grade);

        @Deprecated
        String qualify(final Grade grade);

        boolean isNotEvaluated(final Grade grade);

        boolean isNotApproved(final Grade grade);

        boolean isApproved(final Grade grade);

        boolean belongsTo(final String value);

        default boolean hasRestrictedGrades() {
            return false;
        }

        default Collection<Grade> getPossibleGrades() {
            return Collections.emptySet();
        }

        default LocalizedString getExtendedValue(Grade grade) {
            return BundleUtil.getLocalizedString(Bundle.ENUMERATION, GradeScaleEnum.class.getSimpleName() + "." + grade.getValue());
        }

        default int compareGrades(Grade grade1, Grade grade2) {
            if (grade2 == null) {
                return 1;
            }
            final boolean isApproved1 = isApproved(grade1);
            final boolean isApproved2 = isApproved(grade2);
            if (isApproved1 && isApproved2) {
                if (grade1.getValue().equals(GradeScaleEnum.AP)) {
                    return 1;
                } else if (grade2.getValue().equals(GradeScaleEnum.AP)) {
                    return -1;
                } else if (grade1.getGradeScale().equals(grade2.getGradeScale())) {
                    return grade1.getValue().compareTo(grade2.getValue());
                } else {
                    throw new DomainException("Grade.unsupported.comparassion.of.grades.of.different.scales");
                }
            } else if (isApproved1 || grade2.getValue().equals(GradeScaleEnum.NA) || grade2.getValue().equals(GradeScaleEnum.RE)) {
                return 1;
            } else if (isApproved2 || grade1.getValue().equals(GradeScaleEnum.NA) || grade1.getValue().equals(GradeScaleEnum.RE)) {
                return -1;
            } else {
                return grade1.getValue().compareTo(grade2.getValue());
            }
        }
    }

    private boolean isPublic;

    private GradeScaleLogic logic;

    static final public String NA = "NA";

    static final public String RE = "RE";

    static final public String AP = "AP";

    static final public String APT = "APT";

    static final public String S = "S";

    static final public String B = "B";

    static final public String MB = "MB";

    static final public String MBDL = "MBDL";

    static final public String BD = "BD";

    static final public String E = "E";

    static final public String R = "R";

    static final public String AD = "AD";

    static final public String ADL = "ADL";

    static final public String A = "A";

    static final public String AMB = "AMB";

    private GradeScaleEnum(final boolean isPublic, GradeScaleLogic logic) {
        setPublic(isPublic);
        setLogic(logic);
    }

    public String getName() {
        return name();
    }

    public void setLogic(GradeScaleLogic logic) {
        this.logic = logic;
    }

    @Deprecated
    public boolean isValid(final String value, final EvaluationType evaluationType) {
        try {
            final Grade grade = Grade.createGrade(value, this);
            if (grade.isEmpty()) {
                return false;
            }
            if (EvaluationType.FINAL_TYPE.equals(evaluationType)) {
                return logic.checkFinal(grade);
            } else {
                return logic.checkNotFinal(grade);
            }
        } catch (DomainException de) {
            return false;
        }
    }

    public boolean isNotEvaluated(final Grade grade) {
        return logic.isNotEvaluated(grade);
    }

    public boolean isNotApproved(final Grade grade) {
        return logic.isNotApproved(grade);
    }

    public boolean isApproved(final Grade grade) {
        return logic.isApproved(grade);
    }

    public boolean belongsTo(final String grade) {
        return logic.belongsTo(grade);
    }

    public boolean belongsTo(final Grade grade) {
        return logic.belongsTo(grade.getValue());
    }

    public LocalizedString getExtendedValue(Grade grade) {
        return logic.getExtendedValue(grade);
    }

    public boolean hasRestrictedGrades() {
        return logic.hasRestrictedGrades();
    }

    public Collection<Grade> getPossibleGrades() {
        return logic.getPossibleGrades();
    }

    @Deprecated
    final public String getQualifiedName(final String value) {
        final Grade grade = Grade.createGrade(value, this);

        if (logic.isApproved(grade)) {
            return logic.qualify(grade);
        } else {
            throw new DomainException("GradeScale.unable.to.qualify.given.grade");
        }
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    static public List<GradeScaleEnum> getPublicGradeScales() {
        List<GradeScaleEnum> publicGradeScales = new ArrayList<GradeScaleEnum>();
        for (GradeScaleEnum gradeScale : values()) {
            if (gradeScale.isPublic) {
                publicGradeScales.add(gradeScale);
            }
        }
        return publicGradeScales;
    }

    public String getDescription() {
        return BundleUtil.getString(Bundle.ENUMERATION, name());
    }

    public String getPossibleValueDescription(boolean isFinal) {
        final String key = isFinal ? "TYPE.final" : name() + ".description";
        return BundleUtil.getString(Bundle.ENUMERATION, key);
    }

    public int compareGrades(Grade grade1, Grade grade2) {
        return logic.compareGrades(grade1, grade2);
    }

}
