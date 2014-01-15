package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.domain.reports.ParkingDataReportFile;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;
import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

import com.google.common.collect.Ordering;

@Mapping(module = "external", path = "/exportParkingData", scope = "request", validate = false)
public class ExportParkingData extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final String password = request.getParameter("password");
        final String username = request.getParameter("username");
        checkPermissions(username, password);

        ParkingDataReportFile parkingDataReportFile = getLastParkingDataReportJob();
        if (parkingDataReportFile != null && parkingDataReportFile.getFile() != null) {
            response.setContentType("application/vnd.ms-access");
            response.setHeader("Content-disposition", "attachment; filename=" + parkingDataReportFile.getFilename());
            final OutputStream outputStream = response.getOutputStream();
            outputStream.write(parkingDataReportFile.getFile().getContents());
            outputStream.close();
        }
        return null;
    }

    private ParkingDataReportFile getLastParkingDataReportJob() {
        Set<QueueJob> parkingJobs = new HashSet<QueueJob>();
        for (QueueJob queueJob : Bennu.getInstance().getQueueJobSet()) {
            if (queueJob instanceof ParkingDataReportFile && queueJob.getDone()) {
                parkingJobs.add(queueJob);
            }
        }

        return parkingJobs.size() > 0 ? (ParkingDataReportFile) Ordering.from(new BeanComparator("requestDate")).max(parkingJobs) : null;
    }

    private void checkPermissions(String username, String password) throws NotAuthorizedException {
        final String allowedUser = FenixConfigurationManager.getConfiguration().getExportParkingDataUsername();
        final String allowedPass = FenixConfigurationManager.getConfiguration().getExportParkingDataPassword();
        if ((!allowedUser.equals(username)) || (!allowedPass.equals(password))) {
            throw new NotAuthorizedException();
        }
    }

}