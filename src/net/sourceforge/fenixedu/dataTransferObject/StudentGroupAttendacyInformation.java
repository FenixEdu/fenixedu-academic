/*
 * Created on 10/Set/2003, 21:48:21
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 10/Set/2003, 21:48:21
 *  
 */
public class StudentGroupAttendacyInformation extends InfoObject {
    private Integer groupNumber;

    private String shiftName;

    private List lessons;

    private List degreesNames;

    private List groupAttends;

    /**
     * @return
     */
    public Integer getGroupNumber() {
        return groupNumber;
    }

    /**
     * @return
     */
    public String getShiftName() {
        return shiftName;
    }

    /**
     * @param integer
     */
    public void setGroupNumber(Integer integer) {
        groupNumber = integer;
    }

    /**
     * @param string
     */
    public void setShiftName(String string) {
        shiftName = string;
    }

    /**
     * @param string
     */

    public String toString() {
        String result = "[StudentGroupAttendacyInformation ";
        result += "GroupNumber: " + this.getGroupNumber() + ";";
        result += "GroupAttends:: " + this.getGroupAttends() + ";";
        result += "ShiftName: " + this.getShiftName() + ";";
        result += "Lessons:" + this.getLessons() + "]";

        return result;
    }

    /**
     * @return
     */
    public List getLessons() {
        return lessons;
    }

    /**
     * @param list
     */
    public void setLessons(List list) {
        lessons = list;
    }

    /**
     * @return
     */
    public List getDegreesNames() {
        return degreesNames;
    }

    /**
     * @param list
     */
    public void setDegreesNames(List list) {
        degreesNames = list;
    }

    /**
     * @return
     */
    public List getGroupAttends() {
        return groupAttends;
    }

    /**
     * @param list
     */
    public void setGroupAttends(List list) {
        groupAttends = list;
    }

    public boolean isStudentMemberOfThisGroup(Integer studentNumber) {

        for (Iterator iterator = this.getGroupAttends().iterator(); iterator.hasNext();) {
            IStudentGroupAttend studentGroupAttend = (IStudentGroupAttend) iterator.next();
            IAttends attendacy = studentGroupAttend.getAttend();
            if (attendacy.getAluno().getNumber().equals(studentNumber))
                return true;
        }

        return false;
    }
}