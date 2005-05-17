/*
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.lang.reflect.InvocationTargetException;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.util.CopyUtils;
import net.sourceforge.fenixedu.domain.inquiries.IInquiriesRoom;
import net.sourceforge.fenixedu.util.InquiriesUtil;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class InfoInquiriesRoom extends InfoObject implements Comparable {

    private Integer keyInquiriesCourse;
	private InfoInquiriesCourse inquiriesCourse;

	private Integer keyExecutionPeriod;
	private InfoExecutionPeriod executionPeriod;
    
	private Integer keyExecutionCourse;
	private InfoExecutionCourse executionCourse;
    
	private Integer keyExecutionDegreeCourse;
	private InfoExecutionDegree executionDegreeCourse;
    
	private Integer keyExecutionDegreeStudent;
	private InfoExecutionDegree executionDegreeStudent;
    
	private Integer keyRoom;
	private InfoRoomWithInfoInquiriesRoom room;
    
	private Integer spaceAdequation;
	private Integer environmentalConditions;
	private Integer equipmentQuality;
    

    
    public InfoInquiriesCourse getInquiriesCourse() {
		return inquiriesCourse;
	}
	
	public void setInquiriesCourse(InfoInquiriesCourse inquiriesCourse) {
		this.inquiriesCourse = inquiriesCourse;
	}
	
	public Integer getKeyInquiriesCourse() {
		return keyInquiriesCourse;
	}
	
	public void setKeyInquiriesCourse(Integer keyInquiriesCourse) {
		this.keyInquiriesCourse = keyInquiriesCourse;
	}
	
	/**
     * @return Returns the environmentalConditions.
     */
    public Integer getEnvironmentalConditions() {
        return environmentalConditions;
    }
    /**
     * @param environmentalConditions The environmentalConditions to set.
     */
    public void setEnvironmentalConditions(Integer environmentalConditions) {
		if(InquiriesUtil.isValidAnswer(environmentalConditions))
	        this.environmentalConditions = environmentalConditions;
		else
			this.environmentalConditions = null;
    }
    /**
     * @return Returns the equipmentQuality.
     */
    public Integer getEquipmentQuality() {
        return equipmentQuality;
    }
    /**
     * @param equipmentQuality The equipmentQuality to set.
     */
    public void setEquipmentQuality(Integer equipmentQuality) {
		if(InquiriesUtil.isValidAnswer(equipmentQuality))
	        this.equipmentQuality = equipmentQuality;
		else
			this.equipmentQuality = null;
    }
    /**
     * @return Returns the executionCourse.
     */
    public InfoExecutionCourse getExecutionCourse() {
        return executionCourse;
    }
    /**
     * @param executionCourse The executionCourse to set.
     */
    public void setExecutionCourse(InfoExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }
    /**
     * @return Returns the executionDegreeCourse.
     */
    public InfoExecutionDegree getExecutionDegreeCourse() {
        return executionDegreeCourse;
    }
    /**
     * @param executionDegreeCourse The executionDegreeCourse to set.
     */
    public void setExecutionDegreeCourse(
            InfoExecutionDegree executionDegreeCourse) {
        this.executionDegreeCourse = executionDegreeCourse;
    }
    /**
     * @return Returns the executionDegreeStudent.
     */
    public InfoExecutionDegree getExecutionDegreeStudent() {
        return executionDegreeStudent;
    }
    /**
     * @param executionDegreeStudent The executionDegreeStudent to set.
     */
    public void setExecutionDegreeStudent(
            InfoExecutionDegree executionDegreeStudent) {
        this.executionDegreeStudent = executionDegreeStudent;
    }
    /**
     * @return Returns the executionPeriod.
     */
    public InfoExecutionPeriod getExecutionPeriod() {
        return executionPeriod;
    }
    /**
     * @param executionPeriod The executionPeriod to set.
     */
    public void setExecutionPeriod(InfoExecutionPeriod executionPeriod) {
        this.executionPeriod = executionPeriod;
    }
    /**
     * @return Returns the keyExecutionCourse.
     */
    public Integer getKeyExecutionCourse() {
        return keyExecutionCourse;
    }
    /**
     * @param keyExecutionCourse The keyExecutionCourse to set.
     */
    public void setKeyExecutionCourse(Integer keyExecutionCourse) {
        this.keyExecutionCourse = keyExecutionCourse;
    }
    /**
     * @return Returns the keyExecutionDegreeCourse.
     */
    public Integer getKeyExecutionDegreeCourse() {
        return keyExecutionDegreeCourse;
    }
    /**
     * @param keyExecutionDegreeCourse The keyExecutionDegreeCourse to set.
     */
    public void setKeyExecutionDegreeCourse(Integer keyExecutionDegreeCourse) {
        this.keyExecutionDegreeCourse = keyExecutionDegreeCourse;
    }
    /**
     * @return Returns the keyExecutionDegreeStudent.
     */
    public Integer getKeyExecutionDegreeStudent() {
        return keyExecutionDegreeStudent;
    }
    /**
     * @param keyExecutionDegreeStudent The keyExecutionDegreeStudent to set.
     */
    public void setKeyExecutionDegreeStudent(Integer keyExecutionDegreeStudent) {
        this.keyExecutionDegreeStudent = keyExecutionDegreeStudent;
    }
    /**
     * @return Returns the keyExecutionPeriod.
     */
    public Integer getKeyExecutionPeriod() {
        return keyExecutionPeriod;
    }
    /**
     * @param keyExecutionPeriod The keyExecutionPeriod to set.
     */
    public void setKeyExecutionPeriod(Integer keyExecutionPeriod) {
        this.keyExecutionPeriod = keyExecutionPeriod;
    }
    /**
     * @return Returns the keyRoom.
     */
    public Integer getKeyRoom() {
        return keyRoom;
    }
    /**
     * @param keyRoom The keyRoom to set.
     */
    public void setKeyRoom(Integer keyRoom) {
        this.keyRoom = keyRoom;
    }
    /**
     * @return Returns the room.
     */
    public InfoRoomWithInfoInquiriesRoom getRoom() {
        return room;
    }
    /**
     * @param room The room to set.
     */
    public void setRoom(InfoRoomWithInfoInquiriesRoom room) {
        this.room = room;
    }
    /**
     * @return Returns the spaceAdequation.
     */
    public Integer getSpaceAdequation() {
        return spaceAdequation;
    }
    /**
     * @param spaceAdequation The spaceAdequation to set.
     */
    public void setSpaceAdequation(Integer spaceAdequation) {
		if(InquiriesUtil.isValidAnswer(spaceAdequation))
	        this.spaceAdequation = spaceAdequation;
		else
			this.spaceAdequation = null;
    }

    public int compareTo(Object arg0) {
        return 0;
    }

    public static InfoInquiriesRoom newInfoFromDomain(IInquiriesRoom inquiriesRoom) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        InfoInquiriesRoom newInfo = null;
        if(inquiriesRoom != null) {
            newInfo = new InfoInquiriesRoom();
            newInfo.copyFromDomain(inquiriesRoom);
        }
        return newInfo;
    }
       
    public void copyFromDomain(IInquiriesRoom inquiriesRoom) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (inquiriesRoom != null) {
            super.copyFromDomain(inquiriesRoom);
        }
        
        CopyUtils.copyPropertiesNullConvertion(this, inquiriesRoom);
        this.setInquiriesCourse(InfoInquiriesCourse.newInfoFromDomain(inquiriesRoom.getInquiriesCourse()));
        this.setExecutionPeriod(InfoExecutionPeriod.newInfoFromDomain(inquiriesRoom.getExecutionPeriod()));
        this.setExecutionCourse(InfoExecutionCourse.newInfoFromDomain(inquiriesRoom.getExecutionCourse()));
        this.setExecutionDegreeCourse(InfoExecutionDegree.newInfoFromDomain(inquiriesRoom.getExecutionDegreeCourse()));
        this.setExecutionDegreeStudent(InfoExecutionDegree.newInfoFromDomain(inquiriesRoom.getExecutionDegreeStudent()));
        this.setRoom(InfoRoomWithInfoInquiriesRoom.newInfoFromDomain(inquiriesRoom.getRoom()));        
    }
        
	public String toString() {
        String result = "[INFOINQUIRIESROOM";
        result += ", id=" + getIdInternal();
		if(executionPeriod != null)
			result += ", executionPeriod=" + executionPeriod.toString();
		if(executionCourse != null)
			result += ", executionCourse=" + executionCourse.toString();
		if(executionDegreeCourse != null)
			result += ", executionDegreeCourse" + executionDegreeCourse.toString();
		if(executionDegreeStudent != null)
			result += ", executionDegreeStudent" + executionDegreeStudent.toString();
		if(room != null)
			result += ", room=" + room.toString();
		result += ", spaceAdequation=" + spaceAdequation;
		result += ", environmentalConditions=" + environmentalConditions;
		result += ", equipmentQuality=" + equipmentQuality;
        result += "]\n";
        return result;
	}

}
