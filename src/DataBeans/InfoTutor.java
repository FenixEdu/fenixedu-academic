/*
 * Created on 3/Fev/2004
 *
 */
package DataBeans;

import Dominio.ITutor;

/**
 * @author Tânia Pousão
 *
 */
public class InfoTutor extends InfoObject
{
	private InfoTeacher infoTeacher;
	private InfoStudent infoStudent;
		
	/**
	 * @return Returns the infoStudent.
	 */
	public InfoStudent getInfoStudent()
	{
		return infoStudent;
	}

	/**
	 * @param infoStudent The infoStudent to set.
	 */
	public void setInfoStudent(InfoStudent infoStudent)
	{
		this.infoStudent = infoStudent;
	}

	/**
	 * @return Returns the infoTeacher.
	 */
	public InfoTeacher getInfoTeacher()
	{
		return infoTeacher;
	}

	/**
	 * @param infoTeacher The infoTeacher to set.
	 */
	public void setInfoTeacher(InfoTeacher infoTeacher)
	{
		this.infoTeacher = infoTeacher;
	}
	
	public static InfoTutor newInfoFromDomain(ITutor tutor) {
	    InfoTutor infoTutor = null;
	    if(tutor != null) {
	        infoTutor = new InfoTutor();
	        infoTutor.copyFromDomain(tutor);
	    }
	    return infoTutor;
	}
}
