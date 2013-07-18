package net.sourceforge.fenixedu.presentationTier.backBeans.gep;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionYears;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.gep.ListMasterDegreeStudents;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
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

    public Collection getStudentCurricularPlans() throws  FenixServiceException {

        if (getSelectedExecutionYear() == null || getSelectedExecutionYear().length() == 0) {
            return new ArrayList();
        }

        return ListMasterDegreeStudents.run(getSelectedExecutionYear());
    }

    public List<SelectItem> getExecutionYears() throws  FenixServiceException {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<InfoExecutionYear> executionYears = ReadNotClosedExecutionYears.run();

        Collections.sort(executionYears, new Comparator<InfoExecutionYear>() {

            @Override
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