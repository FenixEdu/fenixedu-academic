/*
 * Created on Nov 14, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.student.enrolment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlInputHidden;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.commons.beanutils.BeanComparator;

public class ManageEvaluationsForStudent extends DisplayEvaluationsForStudentToEnrol {

    private List<Evaluation> evaluationsWithEnrolmentPeriodOpened;
    private List<Evaluation> evaluationsWithEnrolmentPeriodClosed;
    private HtmlInputHidden evaluationTypeHidden;
    private Map<Integer, Boolean> enroledEvaluationsForStudent;
    private Map<Integer, String> studentRooms;

    public List<Evaluation> getEvaluationsWithEnrolmentPeriodClosed() {
        if (this.evaluationsWithEnrolmentPeriodClosed == null) {
            processEvaluations();
        }
        return this.evaluationsWithEnrolmentPeriodClosed;
    }

    public void setEvaluationsWithEnrolmentPeriodClosed(
            List<Evaluation> evaluationsWithEnrolmentPeriodClosed) {
        this.evaluationsWithEnrolmentPeriodClosed = evaluationsWithEnrolmentPeriodClosed;
    }

    public List<Evaluation> getEvaluationsWithEnrolmentPeriodOpened() {
        if (this.evaluationsWithEnrolmentPeriodOpened == null) {
            processEvaluations();
        }
        return this.evaluationsWithEnrolmentPeriodOpened;
    }

    public void setEvaluationsWithEnrolmentPeriodOpened(
            List<Evaluation> evaluationsWithEnrolmentPeriodOpened) {
        this.evaluationsWithEnrolmentPeriodOpened = evaluationsWithEnrolmentPeriodOpened;
    }

    private void processEvaluations() {
        this.evaluationsWithEnrolmentPeriodClosed = new ArrayList();
        this.evaluationsWithEnrolmentPeriodOpened = new ArrayList();

        final String evaluationType = getEvaluationTypeString();
        for (final Registration registration : getStudent().getStudent().getRegistrationsSet()) {
            for (final WrittenEvaluation writtenEvaluation : registration.getWrittenEvaluations(
        	    getExecutionPeriod())) {
        		if (writtenEvaluation instanceof Exam) {
        		    final Exam exam = (Exam) writtenEvaluation;
        		    if (!exam.isExamsMapPublished()) {
        			continue;
        		    }
        		}

        		if (writtenEvaluation.getClass().getName().equals(evaluationType)) {
        		    try {
        			if (writtenEvaluation.isInEnrolmentPeriod()) {
        			    this.evaluationsWithEnrolmentPeriodOpened.add(writtenEvaluation);
        			} else {
        			    this.evaluationsWithEnrolmentPeriodClosed.add(writtenEvaluation);
        			    final OldRoom room = registration.getRoomFor(writtenEvaluation);
        			    getStudentRooms().put(writtenEvaluation.getIdInternal(),
        				    room != null ? room.getNome() : "-");
        			}
        		    } catch (final DomainException e) {
        			getEvaluationsWithoutEnrolmentPeriod().add(writtenEvaluation);
        			final OldRoom room = registration.getRoomFor(writtenEvaluation);
        			getStudentRooms().put(writtenEvaluation.getIdInternal(),
        				room != null ? room.getNome() : "-");
        		    } finally {
        			getEnroledEvaluationsForStudent().put(writtenEvaluation.getIdInternal(),
        				Boolean.valueOf(registration.isEnroledIn(writtenEvaluation)));
        			getExecutionCourses().put(writtenEvaluation.getIdInternal(),
        				writtenEvaluation.getAttendingExecutionCoursesFor(registration));
        		    }
        		}
            }
        }
        Collections.sort(this.evaluationsWithEnrolmentPeriodClosed, new BeanComparator("dayDate"));
        Collections.sort(this.evaluationsWithEnrolmentPeriodOpened, new BeanComparator("dayDate"));
        Collections.sort(getEvaluationsWithoutEnrolmentPeriod(), new BeanComparator("dayDate"));
    }

    public String enrolStudent() {
        try {
            final Object args[] = { getUserView().getUtilizador(), getEvaluationID() };
            ServiceUtils.executeService(getUserView(), "EnrolStudentInWrittenEvaluation", args);
            clearAttributes();
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
        } catch (FenixFilterException e) {
            setErrorMessage("errors.impossible.operation");
        } catch (FenixServiceException e) {
            setErrorMessage("errors.impossible.operation");
        }
        return "";
    }

    public String unenrolStudent() {
        try {
            final Object args[] = { getUserView().getUtilizador(), getEvaluationID() };
            ServiceUtils.executeService(getUserView(), "UnEnrollStudentInWrittenEvaluation", args);
            clearAttributes();
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
        } catch (FenixFilterException e) {
            setErrorMessage("errors.impossible.operation");
        } catch (FenixServiceException e) {
            setErrorMessage("errors.impossible.operation");
        }
        return "";
    }

    public Integer getEvaluationID() {
        return Integer.valueOf(getRequestParameter("evaluationID"));
    }

    protected void clearAttributes() {
        setEvaluationsWithEnrolmentPeriodClosed(null);
        setEvaluationsWithEnrolmentPeriodOpened(null);
        setEvaluationsWithoutEnrolmentPeriod(null);
        setExecutionCourses(null);
    }

    public Integer getEvaluationType() {
        if (this.evaluationType == null) {
            if (this.getRequestParameter("evaluationType") != null) {
                this.evaluationType = Integer.valueOf(this.getRequestParameter("evaluationType"));
            } else if (this.getEvaluationTypeHidden().getValue() != null) {
                this.evaluationType = Integer.valueOf(this.getEvaluationTypeHidden().getValue()
                        .toString());
            }
        }
        return this.evaluationType;
    }

    public HtmlInputHidden getEvaluationTypeHidden() {
        if (this.evaluationTypeHidden == null) {
            this.evaluationTypeHidden = new HtmlInputHidden();
            this.evaluationTypeHidden.setValue(getEvaluationType());
        }
        return this.evaluationTypeHidden;
    }

    public void setEvaluationTypeHidden(HtmlInputHidden evaluationTypeHidden) {
        if (evaluationTypeHidden != null) {
            setEvaluationType(Integer.valueOf(evaluationTypeHidden.getValue().toString()));
        }
        this.evaluationTypeHidden = evaluationTypeHidden;
    }

    public Map<Integer, Boolean> getEnroledEvaluationsForStudent() {
        if (this.enroledEvaluationsForStudent == null) {
            this.enroledEvaluationsForStudent = new HashMap<Integer, Boolean>();
        }
        return this.enroledEvaluationsForStudent;
    }

    public void setEnroledEvaluationsForStudent(Map<Integer, Boolean> enroledEvaluationsForStudent) {
        this.enroledEvaluationsForStudent = enroledEvaluationsForStudent;
    }

    public Map<Integer, String> getStudentRooms() {
        if (this.studentRooms == null) {
            this.studentRooms = new HashMap<Integer, String>();
        }
        return this.studentRooms;
    }

    public void setStudentRooms(Map<Integer, String> studentRooms) {
        this.studentRooms = studentRooms;
    }
}
