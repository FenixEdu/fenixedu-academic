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
