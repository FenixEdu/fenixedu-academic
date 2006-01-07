/**
 * Aug 30, 2005
 */
package net.sourceforge.fenixedu.dataTransferObject.student;

import net.sourceforge.fenixedu.dataTransferObject.InfoCountry;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.DislocatedStudent;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class InfoDislocatedStudent extends InfoObject {

    private InfoStudent infoStudent;

    private InfoCountry infoCountry;

    public InfoDislocatedStudent() {
    }

    public InfoCountry getInfoCountry() {
        return infoCountry;
    }

    public void setInfoCountry(InfoCountry infoCountry) {
        this.infoCountry = infoCountry;
    }

    public InfoStudent getInfoStudent() {
        return infoStudent;
    }

    public void setInfoStudent(InfoStudent infoStudent) {
        this.infoStudent = infoStudent;
    }

    public void copyFromDomain(DislocatedStudent dislocatedStudent) {
        super.copyFromDomain(dislocatedStudent);
        if (dislocatedStudent != null) {
            setInfoStudent(InfoStudent.newInfoFromDomain(dislocatedStudent.getStudent()));
            setInfoCountry(InfoCountry.newInfoFromDomain(dislocatedStudent.getCountry()));
        }
    }

    public static InfoDislocatedStudent newInfoFromDomain(DislocatedStudent dislocatedStudent) {
        InfoDislocatedStudent infoDislocatedStudent = null;
        if (dislocatedStudent != null) {
            infoDislocatedStudent = new InfoDislocatedStudent();
            infoDislocatedStudent.copyFromDomain(dislocatedStudent);
        }
        return infoDislocatedStudent;
    }
}
