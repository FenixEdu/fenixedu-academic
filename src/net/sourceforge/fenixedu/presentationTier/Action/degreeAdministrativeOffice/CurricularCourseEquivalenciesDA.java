package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice;

import java.text.Collator;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseEquivalence;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class CurricularCourseEquivalenciesDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	final IUserView userView = SessionUtils.getUserView(request);
        final DynaActionForm actionForm = (DynaActionForm) form;

        setInfoDegrees(request, userView);

        final String degreeIDString = (String) actionForm.get("degreeID");
        if (isValidObjectID(degreeIDString)) {
        	setInfoDegreeCurricularPlans(request, userView, Integer.valueOf(degreeIDString), "infoDegreeCurricularPlans");

        	final String degreeCurricularPlanIDString = (String) actionForm.get("degreeCurricularPlanID");
        	if (isValidObjectID(degreeCurricularPlanIDString)) {
        		final Object[] argsReadCurricularCourseEquivalencies = { Integer.valueOf(degreeCurricularPlanIDString) };
        		final List<InfoCurricularCourseEquivalence> infoCurricularCourseEquivalences = 
        			(List<InfoCurricularCourseEquivalence>) ServiceUtils.executeService(userView, 
        					"ReadCurricularCourseEquivalenciesByDegreeCurricularPlan", argsReadCurricularCourseEquivalencies);
        		sortInfoCurricularCourseEquivalences(infoCurricularCourseEquivalences);
        		request.setAttribute("infoCurricularCourseEquivalences", infoCurricularCourseEquivalences);
        	}
        }

        return mapping.findForward("showEquivalencies");
    }

	public ActionForward prepareCreate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	final IUserView userView = SessionUtils.getUserView(request);
        final DynaActionForm actionForm = (DynaActionForm) form;

    	final String degreeCurricularPlanIDString = (String) actionForm.get("degreeCurricularPlanID");
    	if (isValidObjectID(degreeCurricularPlanIDString)) {
        	setInfoDegrees(request, userView);

    		final String degreeIDString = (String) actionForm.get("degreeID");
            if (isValidObjectID(degreeIDString)) {
            	setInfoDegreeCurricularPlans(request, userView, Integer.valueOf(degreeIDString), "infoDegreeCurricularPlans");
            }

    		setInfoCurricularCourses(request, userView, Integer.valueOf(degreeCurricularPlanIDString), "infoCurricularCourses");

            final String oldDegreeIDString = (String) actionForm.get("oldDegreeID");
            if (isValidObjectID(oldDegreeIDString)) {
                setInfoDegreeCurricularPlans(request, userView, Integer.valueOf(oldDegreeIDString), "oldInfoDegreeCurricularPlans");
            }

            final String oldDegreeCurricularPlanIDString = (String) actionForm.get("oldDegreeCurricularPlanID");
            if (isValidObjectID(oldDegreeCurricularPlanIDString)) {
                setInfoCurricularCourses(request, userView, Integer.valueOf(oldDegreeCurricularPlanIDString), "oldInfoCurricularCourses");
            }

    		return mapping.findForward("showCreateEquivalencyForm");
    	} else {
    		return prepare(mapping, form, request, response);
    	}
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final IUserView userView = SessionUtils.getUserView(request);
        final DynaActionForm actionForm = (DynaActionForm) form;

        final String degreeCurricularPlanIDString = (String) actionForm.get("degreeCurricularPlanID");
        final String curricularCourseIDString = (String) actionForm.get("curricularCourseID");
        final String oldCurricularCourseIDString = (String) actionForm.get("oldCurricularCourseID");
        if (isValidObjectID(degreeCurricularPlanIDString) && isValidObjectID(curricularCourseIDString)
                && isValidObjectID(oldCurricularCourseIDString)) {
            final Object[] args = { Integer.valueOf(degreeCurricularPlanIDString),
                    Integer.valueOf(curricularCourseIDString),
                    Integer.valueOf(oldCurricularCourseIDString) };
            ServiceUtils.executeService(userView, "CreateCurricularCourseEquivalency", args);
        }

        return prepare(mapping, form, request, response);        
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final IUserView userView = SessionUtils.getUserView(request);
        final DynaActionForm actionForm = (DynaActionForm) form;

        final String curricularCourseEquivalencyIDString = (String) actionForm.get("curricularCourseEquivalencyID");
        if (isValidObjectID(curricularCourseEquivalencyIDString)) {
            final Object[] args = { Integer.valueOf(curricularCourseEquivalencyIDString) };
            ServiceUtils.executeService(userView, "DeleteCurricularCourseEquivalency", args);
        }

        return prepare(mapping, form, request, response);        
    }

	private void setInfoDegrees(final HttpServletRequest request, final IUserView userView) 
			throws FenixFilterException, FenixServiceException {
    	final Object[] args = { DegreeType.DEGREE };
    	final List<InfoDegree> infoDegrees = (List<InfoDegree>) ServiceUtils.executeService(userView, "ReadDegrees", args);
    	sortInfoDegrees(infoDegrees);
    	request.setAttribute("infoDegrees", infoDegrees);
	}

    private void setInfoDegreeCurricularPlans(final HttpServletRequest request, final IUserView userView, final Integer degreeID, final String attributeName) 
    		throws FenixFilterException, FenixServiceException {
    	final Object[] argsReadCurricularPlans = { degreeID };
    	final List<InfoDegreeCurricularPlan> infoDegreeCurricularPlans = (List<InfoDegreeCurricularPlan>)
    			ServiceUtils.executeService(userView, "ReadDegreeCurricularPlansByDegree", argsReadCurricularPlans);
    	sortInfoDegreeCurricularPlans(infoDegreeCurricularPlans);
    	request.setAttribute(attributeName, infoDegreeCurricularPlans);
	}

	private void setInfoCurricularCourses(final HttpServletRequest request, final IUserView userView, final Integer degreeCurricularPlanID, final String attribute) 
			throws FenixFilterException, FenixServiceException {
    	final Object[] args = { degreeCurricularPlanID };
    	final List<InfoCurricularCourse> infoCurricularCourses = (List<InfoCurricularCourse>) 
    			ServiceUtils.executeService(userView, "ReadCurricularCoursesByDegreeCurricularPlan", args);
    	sortInfoCurricularCourses(infoCurricularCourses);
    	request.setAttribute(attribute, infoCurricularCourses);
	}

	private boolean isValidObjectID(final String objectIDString) {
		return objectIDString != null && objectIDString.length() > 0 && StringUtils.isNumeric(objectIDString);
	}

	private void sortInfoDegrees(final List<InfoDegree> infoDegrees) {
    	final ComparatorChain comparatorChain = new ComparatorChain();
    	comparatorChain.addComparator(new BeanComparator("tipoCurso"));
    	comparatorChain.addComparator(new BeanComparator("nome"));
    	Collections.sort(infoDegrees, comparatorChain);
	}

	private void sortInfoDegreeCurricularPlans(final List<InfoDegreeCurricularPlan> infoDegreeCurricularPlans) {
		Collections.sort(infoDegreeCurricularPlans, new BeanComparator("name"));
	}

	private void sortInfoCurricularCourseEquivalences(final List<InfoCurricularCourseEquivalence> infoCurricularCourseEquivalences) {
    	final ComparatorChain comparatorChain = new ComparatorChain();
    	comparatorChain.addComparator(new BeanComparator("infoEquivalentCurricularCourse.name", Collator.getInstance()));
    	comparatorChain.addComparator(new BeanComparator("infoEquivalentCurricularCourse.code"));
    	comparatorChain.addComparator(new BeanComparator("infoOldCurricularCourse.name", Collator.getInstance()));
    	comparatorChain.addComparator(new BeanComparator("infoOldCurricularCourse.infoDegreeCurricularPlan.name"));
    	comparatorChain.addComparator(new BeanComparator("infoOldCurricularCourse.code"));
		Collections.sort(infoCurricularCourseEquivalences, comparatorChain);
	}

	private void sortInfoCurricularCourses(final List<InfoCurricularCourse> infoCurricularCourses) {
		Collections.sort(infoCurricularCourses, new BeanComparator("name", Collator.getInstance()));
	}

}