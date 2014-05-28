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
/**
 * 
 */
package net.sourceforge.fenixedu.domain.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixframework.Atomic;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public enum StudentStatuteType {

    /**
     * Most common statutes
     */
    SENIOR(true, true, false) {
        @Override
        public StudentStatute createStudentStatute(Student student, ExecutionSemester beginExecutionPeriod,
                ExecutionSemester endExecutionPeriod) {

            throw new DomainException("error.studentStatute.mustDefineValidRegistrationMatchingSeniorStatute");
        }

        @Override
        public StudentStatute createStudentStatute(Student student, Registration registration,
                ExecutionSemester beginExecutionPeriod, ExecutionSemester endExecutionPeriod) {
            return new SeniorStatute(student, registration, this, beginExecutionPeriod, endExecutionPeriod);
        }

    },

    WORKING_STUDENT(true, true, true),

    /**
     * AEIST related statutes
     */
    AEIST_ASSOCIATIVE_LEADER(true, true, true),

    AEIST_FISCAL_COUNCIL_AND_GENERAL_ASSEMBLY_SECRETARIES(true, true, true),

    AEIST_AUTONOMOUS_SECTIONS(true, true, true),

    /**
     * Old institutional statutes
     */
    DIRECTIVE_COUNCIL(true, true, true),

    ASSEMBLY_OF_REPRESENTATIVES(true, true, true),

    ASSEMBLY_OF_REPRESENTATIVES_COMMITTEES(true, true, true),

    PEDAGOCICAL_COUNCIL_EXECUTIVE_BOARD(true, true, true),

    PEDAGOGICAL_COUNCIL_COORDINATING_COMMITTEE(true, true, true),

    /**
     * New institutional statutes
     */
    SCHOOL_ASSEMBLY(true, true, true),

    SCHOOL_COUNCIL(true, true, true),

    /**
     * Statutes related with students' extracurricular activities.
     */
    PROFESSIONAL_ATHLETE(true, true, true),

    IST_ATHLETE(true, true, true),

    IST_TUNA_MEMBER(true, true, true),

    ASSOCIATIVE_LEADER(true, true, true),

    FIREMAN(true, true, true),

    /**
     * Life condition imposed statutes.
     */
    HANDICAPPED(false, true, true),

    PARTURIENT_OR_SPOUSE(true, true, true),

    SPECIAL_SEASON_GRANTED_BY_MEDICAL_JUSTIFICATION(true, true, true),

    /**
     * Grants and external protocols related statutes.
     */
    SAS_GRANT_OWNER(true, true, false),

    IST_GRANT_OWNER(true, true, true),

    SPECIAL_SEASON_GRANTED_BY_MILITARY_PROTOCOL(true, true, true),

    /**
     * Other statutes.
     */
    SPECIAL_SEASON_GRANTED_BY_LEGI_TRANSITION_REGIME(true, false, true),

    SPECIAL_SEASON_GRANTED_BY_REQUEST(true, false, true);

    private boolean explicitCreation;

    private boolean visible;

    private boolean specialSeasonGranted;

    private StudentStatuteType(boolean explicitCreation, boolean visible, boolean specialSeasonGranted) {
        this.explicitCreation = explicitCreation;
        this.visible = visible;
        this.specialSeasonGranted = specialSeasonGranted;
    }

    public boolean isExplicitCreation() {
        return explicitCreation;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isSpecialSeasonGranted() {
        return specialSeasonGranted;
    }

    public static final List<StudentStatuteType> VISIBLE_STATUTES;
    static {
        final List<StudentStatuteType> result = new ArrayList<StudentStatuteType>();
        for (final StudentStatuteType statute : values()) {
            if (statute.isVisible()) {
                result.add(statute);
            }
        }
        VISIBLE_STATUTES = Collections.unmodifiableList(result);
    }

    @Atomic
    public StudentStatute createStudentStatute(Student student, ExecutionSemester beginExecutionPeriod,
            ExecutionSemester endExecutionPeriod) {
        return new StudentStatute(student, this, beginExecutionPeriod, endExecutionPeriod);
    }

    public StudentStatute createStudentStatute(Student student, Registration registration,
            ExecutionSemester beginExecutionPeriod, ExecutionSemester endExecutionPeriod) {
        throw new DomainException("error.studentStatute.RegistrationUnrelated");
    }

}
