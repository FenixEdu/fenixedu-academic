/*
 * Created on 9/Mai/2003
 *
 */
package Dominio;

import java.util.Calendar;

import Util.EnrolmentGroupPolicyType;
import Util.TipoAula;

/**
 * @author asnr and scpo
 *  
 */
public interface IGroupProperties extends IDomainObject {

    public Integer getMaximumCapacity();

    public Integer getMinimumCapacity();

    public Integer getIdealCapacity();

    public EnrolmentGroupPolicyType getEnrolmentPolicy();

    public Integer getGroupMaximumNumber();

    public String getName();

    public String getProjectDescription();

    public IExecutionCourse getExecutionCourse();

    public TipoAula getShiftType();

    public Calendar getEnrolmentBeginDay();

    public Calendar getEnrolmentEndDay();

    public void setMaximumCapacity(Integer maximumCapacity);

    public void setMinimumCapacity(Integer minimumCapacity);

    public void setIdealCapacity(Integer idealCapacity);

    public void setEnrolmentPolicy(EnrolmentGroupPolicyType enrolmentPolicy);

    public void setGroupMaximumNumber(Integer groupMaximumNumber);

    public void setName(String name);

    public void setProjectDescription(String projectDescription);

    public void setExecutionCourse(IExecutionCourse executionCourse);

    public void setShiftType(TipoAula shiftType);

    public void setEnrolmentBeginDay(Calendar enrolmentBeginDay);

    public void setEnrolmentEndDay(Calendar enrolmentEndDay);

}

