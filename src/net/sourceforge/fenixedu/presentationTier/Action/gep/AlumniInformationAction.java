package net.sourceforge.fenixedu.presentationTier.Action.gep;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.Formation;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class AlumniInformationAction extends FenixDispatchAction {

    public ActionForward showAlumniStatistics(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	int totalAlumniCount = RootDomainObject.getInstance().getAlumnisCount();

	int newAlumniCount = 0;
	for (Alumni alumni : RootDomainObject.getInstance().getAlumnis()) {
	    if (alumni.hasStartedPublicRegistry()) {
		newAlumniCount++;
	    }
	}

	int registeredAlumniCount = 0;
	for (Alumni alumni : RootDomainObject.getInstance().getAlumnis()) {
	    if (alumni.hasFinishedPublicRegistry()) {
		registeredAlumniCount++;
	    }
	}

	int jobCount = RootDomainObject.getInstance().getJobsCount();

	int formationCount = 0;
	for (Qualification q : RootDomainObject.getInstance().getQualifications()) {
	    if (q.getClass().equals(Formation.class)) {
		formationCount++;
	    }
	}

	final ResourceBundle bundle = ResourceBundle.getBundle("resources.GEPResources", Language.getLocale());

	request
		.setAttribute("statistics1", MessageFormat
			.format(bundle.getString("label.alumni.total.alumni"), totalAlumniCount));
	request.setAttribute("statistics2", MessageFormat.format(bundle.getString("label.alumni.new.alumni"), newAlumniCount));
	request.setAttribute("statistics3", MessageFormat.format(bundle.getString("label.alumni.registered.alumni"),
		registeredAlumniCount));
	request.setAttribute("statistics4", MessageFormat.format(bundle.getString("label.alumni.job.alumni"), jobCount));
	request.setAttribute("statistics5", MessageFormat.format(bundle.getString("label.alumni.formation.alumni"),
		formationCount));

	return mapping.findForward("alumni.showAlumniStatistics");
    }
}
