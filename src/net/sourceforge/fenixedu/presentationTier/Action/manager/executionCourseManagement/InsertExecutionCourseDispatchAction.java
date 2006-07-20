package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Fernanda Quitério 17/Dez/2003
 *  
 */
public class InsertExecutionCourseDispatchAction extends FenixDispatchAction {
    public ActionForward prepareInsertExecutionCourse(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {
        IUserView userView = SessionUtils.getUserView(request);

        List infoExecutionPeriods = null;
        try {
            infoExecutionPeriods = (List) ServiceUtils.executeService(userView, "ReadExecutionPeriods",
                    null);
        } catch (FenixServiceException ex) {
            throw new FenixActionException();
        }

        if (infoExecutionPeriods != null && !infoExecutionPeriods.isEmpty()) {
            // exclude closed execution periods
            infoExecutionPeriods = (List) CollectionUtils.select(infoExecutionPeriods, new Predicate() {
                public boolean evaluate(Object input) {
                    InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) input;
                    if (!infoExecutionPeriod.getState().equals(PeriodState.CLOSED)) {
                        return true;
                    }
                    return false;
                }
            });

            ComparatorChain comparator = new ComparatorChain();
            comparator.addComparator(new BeanComparator("infoExecutionYear.year"), true);
            comparator.addComparator(new BeanComparator("name"), true);
            Collections.sort(infoExecutionPeriods, comparator);

            List executionPeriodLabels = new ArrayList();
            CollectionUtils.collect(infoExecutionPeriods, new Transformer() {
                public Object transform(Object arg0) {
                    InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) arg0;

                    LabelValueBean executionPeriod = new LabelValueBean(infoExecutionPeriod.getName()
                            + " - " + infoExecutionPeriod.getInfoExecutionYear().getYear(),
                            infoExecutionPeriod.getIdInternal().toString());
                    return executionPeriod;
                }
            }, executionPeriodLabels);

            request.setAttribute(SessionConstants.LIST_EXECUTION_PERIODS, executionPeriodLabels);
        }

        DynaActionForm executionCourseForm = (DynaActionForm) form;
        executionCourseForm.set("theoreticalHours", new String("0.0"));
        executionCourseForm.set("praticalHours", new String("0.0"));
        executionCourseForm.set("theoPratHours", new String("0.0"));
        executionCourseForm.set("labHours", new String("0.0"));
        executionCourseForm.set("seminaryHours", new String("0.0"));
        executionCourseForm.set("problemsHours", new String("0.0"));
        executionCourseForm.set("fieldWorkHours", new String("0.0"));
        executionCourseForm.set("trainingPeriodHours", new String("0.0"));
        executionCourseForm.set("tutorialOrientationHours", new String("0.0"));

        return mapping.findForward("insertExecutionCourse");
    }

    public ActionForward insertExecutionCourse(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {
        IUserView userView = SessionUtils.getUserView(request);

        InfoExecutionCourse infoExecutionCourse = fillInfoExecutionCourse(form, request);

        Object args[] = { infoExecutionCourse };
        try {
            ServiceUtils.executeService(userView, "InsertExecutionCourseAtExecutionPeriod", args);
        } catch (ExistingServiceException ex) {
            throw new ExistingActionException(ex.getMessage(), ex);
        } catch (NonExistingServiceException exception) {
            throw new NonExistingActionException(exception.getMessage(), mapping.getInputForward());
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return mapping.findForward("firstPage");
    }

    private InfoExecutionCourse fillInfoExecutionCourse(ActionForm form, HttpServletRequest request) {
        DynaActionForm dynaForm = (DynaValidatorForm) form;

        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();

        String name = (String) dynaForm.get("name");
        infoExecutionCourse.setNome(name);

        String code = (String) dynaForm.get("code");
        infoExecutionCourse.setSigla(code);

        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
        infoExecutionPeriod.setIdInternal(new Integer((String) dynaForm.get("executionPeriodId")));
        infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);

        String labHours = (String) dynaForm.get("labHours");
        if (labHours.compareTo("") != 0) {
            infoExecutionCourse.setLabHours(new Double(labHours));
        }
        String praticalHours = (String) dynaForm.get("praticalHours");
        if (praticalHours.compareTo("") != 0) {
            infoExecutionCourse.setPraticalHours(new Double(praticalHours));
        }
        String theoPratHours = (String) dynaForm.get("theoPratHours");
        if (theoPratHours.compareTo("") != 0) {
            infoExecutionCourse.setTheoPratHours(new Double(theoPratHours));
        }
        String theoreticalHours = (String) dynaForm.get("theoreticalHours");
        if (theoreticalHours.compareTo("") != 0) {
            infoExecutionCourse.setTheoreticalHours(new Double(theoreticalHours));
        }

        String seminaryHours = (String) dynaForm.get("seminaryHours");
        if (seminaryHours.compareTo("") != 0) {
            infoExecutionCourse.setSeminaryHours(new Double(seminaryHours));
        }
        String problemsHours = (String) dynaForm.get("problemsHours");
        if (problemsHours.compareTo("") != 0) {
            infoExecutionCourse.setProblemsHours(new Double(problemsHours));
        }
        String fieldWorkHours = (String) dynaForm.get("fieldWorkHours");
        if (fieldWorkHours.compareTo("") != 0) {
            infoExecutionCourse.setFieldWorkHours(new Double(fieldWorkHours));
        }
        String trainingPeriodHours = (String) dynaForm.get("trainingPeriodHours");
        if (trainingPeriodHours.compareTo("") != 0) {
            infoExecutionCourse.setTrainingPeriodHours(new Double(trainingPeriodHours));
        }
        String tutorialOrientationHours = (String) dynaForm.get("tutorialOrientationHours");
        if (tutorialOrientationHours.compareTo("") != 0) {
            infoExecutionCourse.setTutorialOrientationHours(new Double(tutorialOrientationHours));
        }

        String comment = new String("");
        if ((String) dynaForm.get("comment") != null) {
            comment = (String) dynaForm.get("comment");
        }
        infoExecutionCourse.setComment(comment);
        return infoExecutionCourse;
    }
}