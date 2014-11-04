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
package org.fenixedu.academic.ui.spring.controller.teacher;

import java.util.HashMap;
import java.util.Map;

public class AttendsBean {

    private Map<String, Boolean> removeStudent;
    private Map<String, Boolean> addStudent;

    public AttendsBean() {
        super();
        this.removeStudent = new HashMap<String, Boolean>();
        this.addStudent = new HashMap<String, Boolean>();
    }

    public Map<String, Boolean> getRemoveStudent() {
        return removeStudent;
    }

    public void setRemoveStudent(Map<String, Boolean> removeStudent) {
        this.removeStudent = removeStudent;
    }

    public Map<String, Boolean> getAddStudent() {
        return addStudent;
    }

    public void setAddStudent(Map<String, Boolean> addStudent) {
        this.addStudent = addStudent;
    }

}
