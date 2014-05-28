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
package net.sourceforge.fenixedu.dataTransferObject.externalServices;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.StudentGroup;
import pt.utl.ist.fenix.tools.util.Pair;

public class EnrolledGroupBean {

    private String groupNumber;
    private List<Pair<String, String>> collegues = new ArrayList<Pair<String, String>>();

    public EnrolledGroupBean(final StudentGroup studentGroup, final Attends attend) {
        setGroupNumber(studentGroup.getGroupNumber().toString());
        for (Attends collegueAttends : studentGroup.getAttends()) {
            if (collegueAttends != attend) {
                getCollegues().add(
                        new Pair<String, String>(collegueAttends.getRegistration().getStudent().getPerson().getIstUsername(),
                                collegueAttends.getRegistration().getStudent().getPerson().getName()));
            }
        }
    }

    public String getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public List<Pair<String, String>> getCollegues() {
        return collegues;
    }

    public void setCollegues(List<Pair<String, String>> collegues) {
        this.collegues = collegues;
    }
}
