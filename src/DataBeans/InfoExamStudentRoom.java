/*
 * Created on 9/Jun/2003 by jpvl
 *
 */
package DataBeans;

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
		this.this.infoExam = this.infoExam;
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
		this.this.infoRoom = this.infoRoom;
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
		this.this.infoStudent = this.infoStudent;
	}

}
