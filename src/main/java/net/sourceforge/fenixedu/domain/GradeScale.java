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
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.EvaluationType;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public enum GradeScale {

    TYPE20(true) {
        @Override
        protected boolean checkFinal(final Grade grade) {
            final String value = grade.getValue();
            if (value.equals(NA) || value.equals(RE)) {
                return true;
            }

            try {
                final int intValue = Integer.parseInt(value);
                return intValue >= 10 && intValue <= 20;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        @Override
        protected boolean checkNotFinal(final Grade grade) {
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
        protected String qualify(final Grade grade) {
            if (grade.getGradeScale() != this) {
                return StringUtils.EMPTY;
            }

            try {
                final int intValue = Integer.parseInt(grade.getValue());

                if (18 <= intValue && intValue <= 20) {
                    return BundleUtil.getString(Bundle.APPLICATION, "label.grade.a");
                } else if (16 <= intValue && intValue <= 17) {
                    return BundleUtil.getString(Bundle.APPLICATION, "label.grade.b");
                } else if (14 <= intValue && intValue <= 15) {
                    return BundleUtil.getString(Bundle.APPLICATION, "label.grade.c");
                } else if (10 <= intValue && intValue <= 13) {
                    return BundleUtil.getString(Bundle.APPLICATION, "label.grade.d");
                } else {
                    throw new DomainException("GradeScale.unable.to.qualify.given.grade");
                }
            } catch (NumberFormatException e) {
                throw new DomainException("GradeScale.unable.to.qualify.given.grade");
            }
        }

        @Override
        protected boolean isNotEvaluated(final Grade grade) {
            final String value = grade.getValue();
            return grade.isEmpty() || value.equals(GradeScale.NA);
        }

        @Override
        protected boolean isNotApproved(final Grade grade) {
            final String value = grade.getValue();
            if (value.equals(GradeScale.RE) || isNotEvaluated(grade)) {
                return true;
            }

            try {
                return Integer.parseInt(value) < 10;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        @Override
        protected boolean isApproved(final Grade grade) {
            final String value = grade.getValue();
            if (value.equals(GradeScale.AP)) {
                return true;
            }

            try {
                final int intValue = Integer.parseInt(value);
                return 10 <= intValue && intValue <= 20;
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
                return doubleValue >= 0 && doubleValue <= 20;
            } catch (NumberFormatException e) {
                return false;
            }
        }

    },

    TYPE5(true) {
        @Override
        protected boolean checkFinal(final Grade grade) {
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
        protected boolean checkNotFinal(final Grade grade) {
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
        protected String qualify(final Grade grade) {
            if (grade.getGradeScale() != this) {
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
        protected boolean isNotEvaluated(final Grade grade) {
            if (grade.isEmpty()) {
                return true;
            }

            return grade.getValue().equals(GradeScale.NA);
        }

        @Override
        protected boolean isNotApproved(final Grade grade) {
            final String value = grade.getValue();
            if (value.equals(GradeScale.RE) || isNotEvaluated(grade)) {
                return true;
            }

            try {
                return Integer.parseInt(value) < 3;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        @Override
        protected boolean isApproved(final Grade grade) {
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

    },

    TYPEAP(true) {
        @Override
        protected boolean checkFinal(final Grade grade) {
            final String value = grade.getValue();
            return value.equals(NA) || value.equals(RE) || value.equals(AP);
        }

        @Override
        protected boolean checkNotFinal(final Grade grade) {
            final String value = grade.getValue();
            return value.equals(NA) || value.equals(RE) || value.equals(AP);
        }

        @Override
        protected String qualify(final Grade grade) {
            if (grade.getGradeScale() != this) {
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
        protected boolean isNotEvaluated(final Grade grade) {
            final String value = grade.getValue();
            return grade.isEmpty() || value.equals(GradeScale.NA);
        }

        @Override
        protected boolean isNotApproved(final Grade grade) {
            final String value = grade.getValue();
            return value.equals(GradeScale.RE) || isNotEvaluated(grade);
        }

        @Override
        protected boolean isApproved(final Grade grade) {
            final String value = grade.getValue();
            return value.equals(GradeScale.AP);
        }

        @Override
        public boolean belongsTo(final String value) {
            return value.equals(NA) || value.equals(RE) || value.equals(AP);
        }

    },

    TYPEAPT(false) {
        @Override
        protected boolean checkFinal(final Grade grade) {
            final String value = grade.getValue();
            return value.equals(NA) || value.equals(RE) || value.equals(APT);
        }

        @Override
        protected boolean checkNotFinal(final Grade grade) {
            final String value = grade.getValue();
            return value.equals(NA) || value.equals(RE) || value.equals(APT);
        }

        @Override
        protected String qualify(final Grade grade) {
            if (grade.getGradeScale() != this) {
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
        protected boolean isNotEvaluated(final Grade grade) {
            final String value = grade.getValue();
            return grade.isEmpty() || value.equals(GradeScale.NA);
        }

        @Override
        protected boolean isNotApproved(final Grade grade) {
            final String value = grade.getValue();
            return value.equals(GradeScale.RE) || isNotEvaluated(grade);
        }

        @Override
        protected boolean isApproved(final Grade grade) {
            final String value = grade.getValue();
            return value.equals(GradeScale.APT);
        }

        @Override
        public boolean belongsTo(final String value) {
            return value.equals(NA) || value.equals(RE) || value.equals(APT);
        }

    },

    TYPEECTS(false) {

        @Override
        public boolean belongsTo(String value) {
            return value.equals("A") || value.equals("B") || value.equals("C") || value.equals("D") || value.equals("E")
                    || value.equals("F") || value.equals(NA);
        }

        @Override
        protected boolean checkFinal(Grade grade) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        protected boolean checkNotFinal(Grade grade) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        protected boolean isApproved(Grade grade) {
            return !(isNotEvaluated(grade) || isNotApproved(grade));
        }

        @Override
        protected boolean isNotApproved(Grade grade) {
            return grade.getValue().equals("F");
        }

        @Override
        protected boolean isNotEvaluated(Grade grade) {
            return grade.getValue().equals(NA);
        }

        @Override
        protected String qualify(Grade grade) {
            // TODO Auto-generated method stub
            return null;
        }

    };

    private boolean isPublic;

    static final public String NA = "NA";

    static final public String RE = "RE";

    static final public String AP = "AP";

    static final public String APT = "APT";

    private GradeScale(final boolean isPublic) {
        setPublic(isPublic);
    }

    public String getName() {
        return name();
    }

    public boolean isValid(final String value, final EvaluationType evaluationType) {
        try {
            final Grade grade = Grade.createGrade(value, this);
            if (grade.isEmpty()) {
                return false;
            }
            if (EvaluationType.FINAL_TYPE.equals(evaluationType)) {
                return checkFinal(grade);
            } else {
                return checkNotFinal(grade);
            }
        } catch (DomainException de) {
            return false;
        }
    }

    abstract protected boolean checkFinal(final Grade grade);

    abstract protected boolean checkNotFinal(final Grade grade);

    abstract protected String qualify(final Grade grade);

    abstract protected boolean isNotEvaluated(final Grade grade);

    abstract protected boolean isNotApproved(final Grade grade);

    abstract protected boolean isApproved(final Grade grade);

    abstract public boolean belongsTo(final String value);

    final protected boolean belongsTo(final Grade grade) {
        return belongsTo(grade.getValue());
    }

    final public String getQualifiedName(final String value) {
        final Grade grade = Grade.createGrade(value, this);

        if (isApproved(grade)) {
            return qualify(grade);
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

    static public List<GradeScale> getPublicGradeScales() {
        List<GradeScale> publicGradeScales = new ArrayList<GradeScale>();
        for (GradeScale gradeScale : values()) {
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

}
