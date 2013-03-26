package net.sourceforge.fenixedu.presentationTier.gwt.manager.UIPlayground.listStudents.server;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.gwt.manager.UIPlayground.listStudents.client.RetrieveSpecificStudentSet;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class RetrieveSpecificStudentSetImpl extends RemoteServiceServlet implements RetrieveSpecificStudentSet {

    @Override
    public List<String> getStudentNames() {
        DegreeCurricularPlan mean = DegreeCurricularPlan.readByNameAndDegreeSigla("MEAN 2006", "MEAN");
        List<String> pupils = new ArrayList<String>();

        for (StudentCurricularPlan scp : mean.getStudentCurricularPlans()) {
            if (scp.getRegistration().getCurricularYear() == 1) {
                pupils.add(scp.getRegistration().getStudent().getPerson().getName());
            }
        }

        return pupils;
    }

}
