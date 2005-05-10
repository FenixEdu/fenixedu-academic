/*
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IRoom;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public interface IInquiriesRoom extends IDomainObject {
    public IInquiriesCourse getInquiriesCourse();
	
	public void setInquiriesCourse(IInquiriesCourse inquiriesCourse);
	
	public Integer getKeyInquiriesCourse();
	
	public void setKeyInquiriesCourse(Integer keyInquiriesCourse);
	
    /**
     * @return Returns the environmentalConditions.
     */
    public Integer getEnvironmentalConditions();

    /**
     * @param environmentalConditions The environmentalConditions to set.
     */
    public void setEnvironmentalConditions(Integer environmentalConditions);

    /**
     * @return Returns the equipmentQuality.
     */
    public Integer getEquipmentQuality();

    /**
     * @param equipmentQuality The equipmentQuality to set.
     */
    public void setEquipmentQuality(Integer equipmentQuality);

    /**
     * @return Returns the executionCourse.
     */
    public IExecutionCourse getExecutionCourse();

    /**
     * @param executionCourse The executionCourse to set.
     */
    public void setExecutionCourse(IExecutionCourse executionCourse);

    /**
     * @return Returns the executionDegreeCourse.
     */
    public IExecutionDegree getExecutionDegreeCourse();

    /**
     * @param executionDegreeCourse The executionDegreeCourse to set.
     */
    public void setExecutionDegreeCourse(IExecutionDegree executionDegreeCourse);

    /**
     * @return Returns the executionDegreeStudent.
     */
    public IExecutionDegree getExecutionDegreeStudent();

    /**
     * @param executionDegreeStudent The executionDegreeStudent to set.
     */
    public void setExecutionDegreeStudent(
            IExecutionDegree executionDegreeStudent);

    /**
     * @return Returns the executionPeriod.
     */
    public IExecutionPeriod getExecutionPeriod();

    /**
     * @param executionPeriod The executionPeriod to set.
     */
    public void setExecutionPeriod(IExecutionPeriod executionPeriod);

    /**
     * @return Returns the keyExecutionCourse.
     */
    public Integer getKeyExecutionCourse();

    /**
     * @param keyExecutionCourse The keyExecutionCourse to set.
     */
    public void setKeyExecutionCourse(Integer keyExecutionCourse);

    /**
     * @return Returns the keyExecutionDegreeCourse.
     */
    public Integer getKeyExecutionDegreeCourse();

    /**
     * @param keyExecutionDegreeCourse The keyExecutionDegreeCourse to set.
     */
    public void setKeyExecutionDegreeCourse(Integer keyExecutionDegreeCourse);

    /**
     * @return Returns the keyExecutionDegreeStudent.
     */
    public Integer getKeyExecutionDegreeStudent();

    /**
     * @param keyExecutionDegreeStudent The keyExecutionDegreeStudent to set.
     */
    public void setKeyExecutionDegreeStudent(Integer keyExecutionDegreeStudent);

    /**
     * @return Returns the keyExecutionPeriod.
     */
    public Integer getKeyExecutionPeriod();

    /**
     * @param keyExecutionPeriod The keyExecutionPeriod to set.
     */
    public void setKeyExecutionPeriod(Integer keyExecutionPeriod);

    /**
     * @return Returns the keyRoom.
     */
    public Integer getKeyRoom();

    /**
     * @param keyRoom The keyRoom to set.
     */
    public void setKeyRoom(Integer keyRoom);

    /**
     * @return Returns the room.
     */
    public IRoom getRoom();

    /**
     * @param room The room to set.
     */
    public void setRoom(IRoom room);

    /**
     * @return Returns the spaceAdequation.
     */
    public Integer getSpaceAdequation();

    /**
     * @param spaceAdequation The spaceAdequation to set.
     */
    public void setSpaceAdequation(Integer spaceAdequation);
}