/*
 * Created on 9/Jun/2003 by jpvl
 *
 */
package DataBeans;

import Dominio.IDomainObject;

/**
 * @author jpvl
 */
public class InfoExamStudentRoom extends InfoObject {
    private InfoExam infoExam;

    private InfoStudent infoStudent;

    private InfoRoom infoRoom;

    /**
     * @return
     */
    public InfoExam getInfoExam() {
        return this.infoExam;
    }

    /**
     * @param infoExam
     */
    public void setInfoExam(InfoExam infoExam) {
        this.infoExam = infoExam;
    }

    /**
     * @return
     */
    public InfoRoom getInfoRoom() {
        return this.infoRoom;
    }

    /**
     * @param infoRoom
     */
    public void setInfoRoom(InfoRoom infoRoom) {
        this.infoRoom = infoRoom;
    }

    /**
     * @return
     */
    public InfoStudent getInfoStudent() {
        return this.infoStudent;
    }

    /**
     * @param infoStudent
     */
    public void setInfoStudent(InfoStudent infoStudent) {
        this.infoStudent = infoStudent;
    }

    public static InfoExamStudentRoom newInfoFromDomain(IDomainObject domainObject) {
        InfoExamStudentRoom infoExamStudentRoom = null;
        if (domainObject != null) {
            infoExamStudentRoom = new InfoExamStudentRoom();
            infoExamStudentRoom.copyFromDomain(domainObject);
        }
        return infoExamStudentRoom;
    }
}