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
package org.fenixedu.academic.domain.thesis;

public enum ThesisParticipationType {
    STATE_CREATOR(true), STATE_SUBMITTER(true), STATE_PROPOSAL_APPROVER(true), STATE_CONFIRMER(true), STATE_EVALUATION_APPROVER(
            true),

    ORIENTATOR(false), COORIENTATOR(false), PRESIDENT(true), VOWEL(false);

    private boolean single;

    public String getName() {
        return name();
    }

    private ThesisParticipationType(boolean single) {
        this.single = single;
    }

    /**
     * @return <code>true</code> if only one person can have this participation
     *         type in a thesis
     */
    public boolean isSingle() {
        return this.single;
    }
}
