/*
 * Created on 13/Jul/2004
 *
 */
package DataBeans;

import Dominio.IExamStudentRoom;

/**
 * @author Tânia Pousão
 *
 */
public class InfoExamStudentRoomWithInfoStudentAndInfoRoom extends InfoExamStudentRoom {

    /* (non-Javadoc)
     * @see DataBeans.InfoObject#copyFromDomain(Dominio.IDomainObject)
     */
    public void copyFromDomain(IExamStudentRoom examStudentRoom) {
        super.copyFromDomain(examStudentRoom);
        if(examStudentRoom != null) {
            setInfoStudent(InfoStudentWithInfoPerson.newInfoFromDomain(examStudentRoom.getStudent()));
            setInfoRoom(InfoRoom.newInfoFromDomain(examStudentRoom.getRoom()));
        }        
    }
    
	public static InfoExamStudentRoom newInfoFromDomain(IExamStudentRoom examStudentRoom) {
	    InfoExamStudentRoomWithInfoStudentAndInfoRoom infoExamStudentRoom = null;
	    if(examStudentRoom != null) {
	        infoExamStudentRoom = new InfoExamStudentRoomWithInfoStudentAndInfoRoom();
	        infoExamStudentRoom.copyFromDomain(examStudentRoom);
	    }
	    return infoExamStudentRoom;
	}
}
