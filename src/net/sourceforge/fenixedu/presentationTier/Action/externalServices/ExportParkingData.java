package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.domain.reports.ParkingDataReportFile;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

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
	List<QueueJob> allJobsForClassOrSubClass = QueueJob.getAllJobsForClassOrSubClass(ParkingDataReportFile.class, 1);
	return allJobsForClassOrSubClass.size() > 0 ? (ParkingDataReportFile) allJobsForClassOrSubClass.get(0) : null;
    }

    private void checkPermissions(String username, String password) throws NotAuthorizedException {
	final String allowedUser = PropertiesManager.getProperty("exportParkingData.username");
	final String allowedPass = PropertiesManager.getProperty("exportParkingData.password");
	if ((!allowedUser.equals(username)) || (!allowedPass.equals(password))) {
	    throw new NotAuthorizedException();
	}
    }

}