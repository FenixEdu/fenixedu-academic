/*
 * Created on Dec 9, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Map;

/**
 * @author Andre Fernandes / Joao Brito
 */
public class InfoCompositionOfAttendAndDegreeCurricularPlanAndShiftsAndStudentGroups
        extends DataTranferObject
{
    private InfoFrequenta _infoAttends;
    private InfoDegreeCurricularPlan _attendingStudentInfoDCP;
    private Map _infoShifts;
    private Map _infoStudentGroups;
    private Integer _numberOfEnrollments;
    
    public InfoCompositionOfAttendAndDegreeCurricularPlanAndShiftsAndStudentGroups(InfoFrequenta iFreq, InfoDegreeCurricularPlan iDCP, Map iShifts, Map iStudentGroup, Integer numberOfEnrollments)
    {
        setInfoAttends(iFreq);
        setAttendingStudentInfoDCP(iDCP);
        setInfoShifts(iShifts);
        setInfoStudentGroups(iStudentGroup);
        setNumberOfEnrollments(numberOfEnrollments);
    }
    
    public InfoCompositionOfAttendAndDegreeCurricularPlanAndShiftsAndStudentGroups(){}
    
    
    public InfoFrequenta getInfoAttends(){
        return _infoAttends;
    }
    
    public InfoDegreeCurricularPlan getAttendingStudentInfoDCP(){
        return _attendingStudentInfoDCP;
    }
    
    public Map getInfoShifts(){
        return _infoShifts;
    }
    
    public Map getInfoStudentGroups(){
        return _infoStudentGroups;
    }
    
    public Integer getNumberOfEnrollments(){
        return _numberOfEnrollments;
    }
    
    
    public void setInfoAttends(InfoFrequenta iFreq){
        _infoAttends=iFreq;
    }
    
    public void setAttendingStudentInfoDCP(InfoDegreeCurricularPlan iDCP){
        _attendingStudentInfoDCP=iDCP;
    }
    
    public void setInfoShifts(Map iShifts){
        _infoShifts=iShifts;
    }
    
    public void setInfoStudentGroups(Map iStudentGroups){
        _infoStudentGroups=iStudentGroups;
    }
    
    public void setNumberOfEnrollments(Integer numberOfEnrollments){
        _numberOfEnrollments = numberOfEnrollments;
    }
}
