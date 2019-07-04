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
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

import com.google.common.collect.Lists;

public enum SchoolLevelType implements IPresentableEnum {

    UNKNOWN(false, false, false),

    DONT_KNOW_HOW_TO_READ_OR_WRITE(false, true, false),

    KNOWS_HOW_TO_READ_WITHOUT_OLD_FOURTH_YEAR(false, true, false),

    FIRST_CYCLE_BASIC_SCHOOL(true, true, false) {
        @Override
        public boolean isSchoolLevelBasicCycle() {
            return true;
        }
    },

    SECOND_CYCLE_BASIC_SCHOOL(true, true, false) {
        @Override
        public boolean isSchoolLevelBasicCycle() {
            return true;
        }
    },

    THIRD_CYCLE_BASIC_SCHOOL(true, true, false) {
        @Override
        public boolean isSchoolLevelBasicCycle() {
            return true;
        }
    },

    HIGH_SCHOOL_OR_EQUIVALENT(true, true, false) {
        @Override
        public boolean isHighSchoolOrEquivalent() {
            return true;
        }
    },

    TECHNICAL_SPECIALIZATION(true, true, false) {

        @Override
        public List<String> getEquivalentDegreeClassifications() {
            return Lists.newArrayList("C", "C0");
        }
    },

    MEDIUM_EDUCATION(false, true, false),

    BACHELOR_DEGREE(true, true, true) {
        @Override
        public boolean isHigherEducation() {
            return true;
        }

        @Override
        public List<String> getEquivalentDegreeClassifications() {
            return Lists.newArrayList("A", "B", "BC", "DB", "EB", "MD");
        }
    },

    BACHELOR_DEGREE_PRE_BOLOGNA(true, true, true) {
        @Override
        public boolean isHigherEducation() {
            return true;
        }

        @Override
        public List<String> getEquivalentDegreeClassifications() {
            return Lists.newArrayList("BL", "LB", "PB");
        }
    },

    DEGREE(true, true, false) {
        @Override
        public boolean isHigherEducation() {
            return true;
        }

        @Override
        public List<String> getEquivalentDegreeClassifications() {
            return Lists.newArrayList("L1", "PM", "LI");
        }
    },

    DEGREE_PRE_BOLOGNA(true, true, false) {
        @Override
        public boolean isHigherEducation() {
            return true;
        }

        @Override
        public List<String> getEquivalentDegreeClassifications() {
            return Lists.newArrayList("DD", "DL", "EL", "L", "LT", "P");
        }
    },

    DEGREE_TERMINAL_PART(true, true, false) {
        @Override
        public boolean isHigherEducation() {
            return true;
        }

        @Override
        public List<String> getEquivalentDegreeClassifications() {
            return Lists.newArrayList("LT");
        }
    },

    MASTER_DEGREE(true, true, true) {
        @Override
        public boolean isHigherEducation() {
            return true;
        }

        @Override
        public List<String> getEquivalentDegreeClassifications() {
            return Lists.newArrayList("M2");
        }
    },

    MASTER_DEGREE_INTEGRATED(true, false, false) {
        @Override
        public boolean isHigherEducation() {
            return true;
        }

        @Override
        public List<String> getEquivalentDegreeClassifications() {
            return Lists.newArrayList("MI", "MT");
        }
    },

    MASTER_DEGREE_PRE_BOLOGNA(true, false, false) {
        @Override
        public boolean isHigherEducation() {
            return true;
        }

        @Override
        public List<String> getEquivalentDegreeClassifications() {
            return Lists.newArrayList("M");
        }
    },

    DOCTORATE_DEGREE(true, true, true) {
        @Override
        public boolean isPhDDegree() {
            return true;
        }

        @Override
        public boolean isHigherEducation() {
            return true;
        }

        @Override
        public List<String> getEquivalentDegreeClassifications() {
            return Lists.newArrayList("D3");
        }
    },

    DOCTORATE_DEGREE_PRE_BOLOGNA(true, false, false) {
        @Override
        public boolean isPhDDegree() {
            return true;
        }

        @Override
        public boolean isHigherEducation() {
            return true;
        }

        @Override
        public List<String> getEquivalentDegreeClassifications() {
            return Lists.newArrayList("D");
        }
    },

    OTHER(true, false, true) {
        @Override
        public boolean isOther() {
            return true;
        }
    },

    OTHER_SITUATION(false, true, false);

    private boolean forStudent;

    private boolean forStudentHousehold;

    private boolean forMobilityStudent;

    private SchoolLevelType(boolean forStudent, boolean forStudentHousehold, boolean forMobilityStudent) {
        this.forStudent = forStudent;
        this.forStudentHousehold = forStudentHousehold;
        this.forMobilityStudent = forMobilityStudent;
    }

    public boolean isOther() {
        return false;
    }

    public boolean isPhDDegree() {
        return false;
    }

    public boolean isSchoolLevelBasicCycle() {
        return false;
    }

    public boolean isHighSchoolOrEquivalent() {
        return false;
    }

    public boolean isHigherEducation() {
        return false;
    }

    public List<String> getEquivalentDegreeClassifications() {
        return new ArrayList<String>();
    }

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return SchoolLevelType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return SchoolLevelType.class.getName() + "." + name();
    }

    public boolean isForStudent() {
        return forStudent;
    }

    public boolean isForStudentHousehold() {
        return forStudentHousehold;
    }

    public boolean isForMobilityStudent() {
        return forMobilityStudent;
    }

    static public List<SchoolLevelType> getTypesForStudent() {
        final List<SchoolLevelType> result = new ArrayList<SchoolLevelType>();

        for (final SchoolLevelType each : values()) {
            if (each.isForStudent()) {
                result.add(each);
            }
        }

        return result;

    }

    static public List<SchoolLevelType> getTypesForStudentMinusOther() {
        return getTypesForStudent().stream().filter(l -> !l.isOther())
                .collect(Collectors.toList());
    }

    static public List<SchoolLevelType> getTypesForMobilityStudent() {
        final List<SchoolLevelType> result = new ArrayList<SchoolLevelType>();

        for (final SchoolLevelType each : values()) {
            if (each.isForMobilityStudent()) {
                result.add(each);
            }
        }

        return result;

    }

    static public List<SchoolLevelType> getTypesForStudentHousehold() {
        final List<SchoolLevelType> result = new ArrayList<SchoolLevelType>();

        for (final SchoolLevelType each : values()) {
            if (each.isForStudentHousehold()) {
                result.add(each);
            }
        }

        return result;
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString(Bundle.ENUMERATION, getQualifiedName());
    }
}
