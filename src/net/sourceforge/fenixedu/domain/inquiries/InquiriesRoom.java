/*
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IRoom;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class InquiriesRoom extends DomainObject implements IInquiriesRoom {

    private Integer keyInquiriesCourse;
    private IInquiriesCourse inquiriesCourse;

    private Integer keyExecutionPeriod;
    private IExecutionPeriod executionPeriod;
    
    private Integer keyExecutionCourse;
    private IExecutionCourse executionCourse;
    
    private Integer keyExecutionDegreeCourse;
    private IExecutionDegree executionDegreeCourse;
    
    private Integer keyExecutionDegreeStudent;
    private IExecutionDegree executionDegreeStudent;
    
    private Integer keyRoom;
    private IRoom room;
    
    private Integer spaceAdequation;
    private Integer environmentalConditions;
    private Integer equipmentQuality;
    
    
    public IInquiriesCourse getInquiriesCourse() {
		return inquiriesCourse;
	}
	
	public void setInquiriesCourse(IInquiriesCourse inquiriesCourse) {
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
        this.environmentalConditions = environmentalConditions;
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
        this.equipmentQuality = equipmentQuality;
    }
    /**
     * @return Returns the executionCourse.
     */
    public IExecutionCourse getExecutionCourse() {
        return executionCourse;
    }
    /**
     * @param executionCourse The executionCourse to set.
     */
    public void setExecutionCourse(IExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }
    /**
     * @return Returns the executionDegreeCourse.
     */
    public IExecutionDegree getExecutionDegreeCourse() {
        return executionDegreeCourse;
    }
    /**
     * @param executionDegreeCourse The executionDegreeCourse to set.
     */
    public void setExecutionDegreeCourse(IExecutionDegree executionDegreeCourse) {
        this.executionDegreeCourse = executionDegreeCourse;
    }
    /**
     * @return Returns the executionDegreeStudent.
     */
    public IExecutionDegree getExecutionDegreeStudent() {
        return executionDegreeStudent;
    }
    /**
     * @param executionDegreeStudent The executionDegreeStudent to set.
     */
    public void setExecutionDegreeStudent(
            IExecutionDegree executionDegreeStudent) {
        this.executionDegreeStudent = executionDegreeStudent;
    }
    /**
     * @return Returns the executionPeriod.
     */
    public IExecutionPeriod getExecutionPeriod() {
        return executionPeriod;
    }
    /**
     * @param executionPeriod The executionPeriod to set.
     */
    public void setExecutionPeriod(IExecutionPeriod executionPeriod) {
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
    public IRoom getRoom() {
        return room;
    }
    /**
     * @param room The room to set.
     */
    public void setRoom(IRoom room) {
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
        this.spaceAdequation = spaceAdequation;
    }
}
