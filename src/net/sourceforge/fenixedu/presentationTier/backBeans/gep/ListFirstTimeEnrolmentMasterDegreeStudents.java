package net.sourceforge.fenixedu.presentationTier.backBeans.gep;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ListFirstTimeEnrolmentMasterDegreeStudents extends FenixBackingBean {

    private String selectedExecutionYear;

    public ListFirstTimeEnrolmentMasterDegreeStudents() {
        super();
    }

    public Collection getStudentCurricularPlans() throws FenixFilterException, FenixServiceException {

        if (getSelectedExecutionYear() == null || getSelectedExecutionYear().length() == 0) {
            return new ArrayList();
        }

        Object[] args = { getSelectedExecutionYear() };
        return (Collection) ServiceUtils.executeService("ListMasterDegreeStudents", args);
    }

    public List<SelectItem> getExecutionYears() throws FenixFilterException, FenixServiceException {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<InfoExecutionYear> executionYears = (List<InfoExecutionYear>) ServiceUtils.executeService(
                "ReadNotClosedExecutionYears", null);

        Collections.sort(executionYears, new Comparator<InfoExecutionYear>() {

            public int compare(InfoExecutionYear o1, InfoExecutionYear o2) {
                return o1.getYear().compareTo(o2.getYear()) * (-1);
            }

        });

        for (InfoExecutionYear executionYear : executionYears) {
            result.add(new SelectItem(executionYear.getYear(), executionYear.getYear()));
        }

        return result;
    }

    public String getSelectedExecutionYear() {
        return selectedExecutionYear;
    }

    public void setSelectedExecutionYear(String selectedExecutionYear) {
        this.selectedExecutionYear = selectedExecutionYear;
    }

}
