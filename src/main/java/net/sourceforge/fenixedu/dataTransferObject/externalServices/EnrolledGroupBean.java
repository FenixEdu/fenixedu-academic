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
