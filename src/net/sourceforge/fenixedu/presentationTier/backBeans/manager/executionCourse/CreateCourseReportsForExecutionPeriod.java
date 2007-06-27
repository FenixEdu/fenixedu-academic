package net.sourceforge.fenixedu.presentationTier.backBeans.manager.executionCourse;

import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 *
 */
public class CreateCourseReportsForExecutionPeriod extends FenixBackingBean {

    private Integer executionPeriodID;

    public List getExecutionPeriods() throws FenixFilterException, FenixServiceException{
        
        List executionPeriods = (List) ServiceUtils.executeService(getUserView(), "ReadNotClosedExecutionPeriods", null);
        
        CollectionUtils.transform(executionPeriods, new Transformer() {
        
            public Object transform(Object arg0) {
                InfoExecutionPeriod executionPeriod = (InfoExecutionPeriod) arg0;                
                return new SelectItem(executionPeriod.getIdInternal(), executionPeriod.getDescription());
            }
        
        });
        
        return executionPeriods;
                
    }
    
    public void create(ActionEvent evt) throws FenixFilterException, FenixServiceException{
        
        Object[] args = { getExecutionPeriodID() };
        ServiceUtils.executeService(getUserView(), "CreateCourseReports", args);
        
    }
    
    public Integer getExecutionPeriodID() {
        return executionPeriodID;
    }

    public void setExecutionPeriodID(Integer executionPeriodID) {
        this.executionPeriodID = executionPeriodID;
    }
    
    
    
}
