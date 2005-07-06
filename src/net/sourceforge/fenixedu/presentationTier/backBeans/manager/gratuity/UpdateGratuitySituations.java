package net.sourceforge.fenixedu.presentationTier.backBeans.manager.gratuity;

import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.util.LabelValueBean;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class UpdateGratuitySituations extends FenixBackingBean {

    private String executionYear;
    
    public void update(ActionEvent evt) {

        Object[] args = { this.executionYear };
        try {
            ServiceUtils.executeService(userView, "CreateGratuitySituationsForCurrentExecutionYear",
                    args);

        } catch (FenixFilterException e) {
        } catch (FenixServiceException e1) {
        }

    }

    public List getExecutionYears() {

        try {
            Object[] argsEmpty = {};
            List<LabelValueBean> executionYears = (List) ServiceUtils.executeService(userView,
                    "ReadNotClosedExecutionYears", argsEmpty);

            CollectionUtils.transform(executionYears, new Transformer() {
                public Object transform(Object arg0) {
                    InfoExecutionYear executionYear = (InfoExecutionYear) arg0;
                    return new SelectItem(executionYear.getYear(), executionYear.getYear());
                }
            });

            return executionYears;

        } catch (FenixFilterException e) {
            return null;
        } catch (FenixServiceException e1) {
            return null;
        }
    }

    public String getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(String executionYear) {
        this.executionYear = executionYear;
    }

}
