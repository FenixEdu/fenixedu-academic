/*
 * Created on Dec 6, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package DataBeans;

import java.util.List;

/**
 * @author André Fernandes / João Brito
 *
 */
public class InfoForReadStudentsWithAttendsByExecutionCourse extends
        DataTranferObject implements ISiteComponent
{    
    private List _infoAttends;
    private List _classTypes;
    private List _infoGroupProperties;
    private List _infoDegreeCurricularPlans;
    private List _infoShifts;
    private InfoExecutionCourse _infoExecutionCourse;
    private InfoAttendsSummary _infoAttendsSummary;
 
    
    public InfoForReadStudentsWithAttendsByExecutionCourse(){}
    
    public List getInfoAttends(){
        return _infoAttends;
    }
    
    public List getClassTypes(){
        return _classTypes;
    }
    
    public List getInfoGroupProperties(){
        return _infoGroupProperties;
    }
    
    public List getInfoDegreeCurricularPlans(){
        return _infoDegreeCurricularPlans;
    }
    
    public List getInfoShifts(){
        return _infoShifts;
    }
    
    public InfoExecutionCourse getInfoExecutionCourse(){
        return _infoExecutionCourse;
    }
    
    public InfoAttendsSummary getInfoAttendsSummary(){
        return _infoAttendsSummary;
    }
    
    public void setInfoAttends(List infoAttends){
        _infoAttends = infoAttends;
    }
    
    public void setClassTypes(List classTypes){
        _classTypes=classTypes;
    }
    
    public void setInfoGroupProperties(List infoGroupProperties){
        _infoGroupProperties=infoGroupProperties;
    }
    
    public void setInfoDegreeCurricularPlans(List infoDCPs){
        _infoDegreeCurricularPlans = infoDCPs;
    }
    
    public void setInfoShifts(List infoShifts){
        _infoShifts = infoShifts;
    }
    
    public void setInfoExecutionCourse(InfoExecutionCourse infoExecutionCourse){
        _infoExecutionCourse = infoExecutionCourse;
    }
    
    public void setInfoAttendsSummary(InfoAttendsSummary infoAttendsSummary){
        _infoAttendsSummary = infoAttendsSummary;
    }
}
