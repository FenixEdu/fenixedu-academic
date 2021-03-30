package org.fenixedu.academic.ui.struts.action.student;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public interface StudentDashboard {

    public static final Set<StudentDashboard> SUPPLIERS = new HashSet<>();

    public String getJspPath();

}
