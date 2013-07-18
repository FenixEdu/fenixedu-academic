package net.sourceforge.fenixedu.presentationTier.backBeans.manager.gratuity;

import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionYears;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.CreateGratuitySituationsForCurrentExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.util.LabelValueBean;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class UpdateGratuitySituations extends FenixBackingBean {

    private String executionYear;

    public void update(ActionEvent evt) {
        CreateGratuitySituationsForCurrentExecutionYear.runCreateGratuitySituationsForCurrentExecutionYear(this.executionYear);
    }

    public List getExecutionYears() {

        List<LabelValueBean> executionYears = (List) ReadNotClosedExecutionYears.run();

        CollectionUtils.transform(executionYears, new Transformer() {
            @Override
            public Object transform(Object arg0) {
                InfoExecutionYear executionYear = (InfoExecutionYear) arg0;
                return new SelectItem(executionYear.getYear(), executionYear.getYear());
            }
        });

        return executionYears;
    }

    public String getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(String executionYear) {
        this.executionYear = executionYear;
    }

}