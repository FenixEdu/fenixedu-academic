package net.sourceforge.fenixedu.presentationTier.backBeans.student.enrolment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;

public class DisplayEvaluationsForStudentToEnrol extends FenixBackingBean {

    private final ResourceBundle messages = getResourceBundle("resources/StudentResources");
    private static final ComparatorChain comparatorChain = new ComparatorChain();
    static {
        comparatorChain.addComparator(new ReverseComparator(new BeanComparator("isInEnrolmentPeriod")));
        comparatorChain.addComparator(new BeanComparator("dayDate"));
    }

    protected static final Integer ALL = Integer.valueOf(0);
    protected static final Integer EXAMS = Integer.valueOf(1);
    protected static final Integer WRITTENTESTS = Integer.valueOf(2);

    private Integer executionPeriodID;
    protected Integer evaluationType;
    private ExecutionPeriod executionPeriod;
    private List<SelectItem> executionPeriodsLabels;
    private List<SelectItem> evaluationTypes;
    private Registration student;
    private List<Evaluation> notEnroledEvaluations;
    private List<Evaluation> enroledEvaluations;
    private List<Evaluation> evaluationsWithoutEnrolmentPeriod;
    private Map<Integer, List<ExecutionCourse>> executionCourses;

    public List<SelectItem> getExecutionPeriodsLabels() {
        if (this.executionPeriodsLabels == null) {
            this.executionPeriodsLabels = new ArrayList();

            final List<InfoExecutionPeriod> infoExecutionPeriods = getExecutionPeriods();
            final ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new ReverseComparator(new BeanComparator(
                    "infoExecutionYear.year")));
            comparatorChain.addComparator(new ReverseComparator(new BeanComparator("semester")));
            Collections.sort(infoExecutionPeriods, comparatorChain);
            for (final InfoExecutionPeriod infoExecutionPeriod : infoExecutionPeriods) {
                final SelectItem selectItem = new SelectItem();
                selectItem.setValue(infoExecutionPeriod.getIdInternal());
                selectItem.setLabel(infoExecutionPeriod.getName() + " - "
                        + infoExecutionPeriod.getInfoExecutionYear().getYear());
                this.executionPeriodsLabels.add(selectItem);
            }
        }
        return this.executionPeriodsLabels;
    }

    public List<SelectItem> getEvaluationTypes() {
        if (this.evaluationTypes == null) {
            this.evaluationTypes = new ArrayList(4);
            final String allEvaluations = messages.getString("link.all");
            evaluationTypes.add(new SelectItem(ALL, allEvaluations));
            final String exams = messages.getString("link.exams.enrolment");
            evaluationTypes.add(new SelectItem(EXAMS, exams));
            final String writtenTests = messages.getString("link.writtenTests.enrolment");
            evaluationTypes.add(new SelectItem(WRITTENTESTS, writtenTests));
        }
        return this.evaluationTypes;
    }

    public List<Evaluation> getNotEnroledEvaluations() {
        if (this.notEnroledEvaluations == null) {
            this.notEnroledEvaluations = new ArrayList();

            processNotEnroledEvaluations();
        }
        return this.notEnroledEvaluations;
    }

    public void setNotEnroledEvaluations(List<Evaluation> notEnroledEvaluations) {
        this.notEnroledEvaluations = notEnroledEvaluations;
    }

    public List<Evaluation> getEnroledEvaluations() {
        if (this.enroledEvaluations == null) {
            this.enroledEvaluations = new ArrayList();
            processEnroledEvaluations();
        }
        return this.enroledEvaluations;
    }

    public void setEnroledEvaluations(List<Evaluation> enroledEvaluations) {
        this.enroledEvaluations = enroledEvaluations;
    }

    public List<Evaluation> getEvaluationsWithoutEnrolmentPeriod() {
        if (this.evaluationsWithoutEnrolmentPeriod == null) {
            this.evaluationsWithoutEnrolmentPeriod = new ArrayList();
        }
        return this.evaluationsWithoutEnrolmentPeriod;
    }

    public void setEvaluationsWithoutEnrolmentPeriod(List<Evaluation> evaluationsWithoutEnrolmentPeriod) {
        this.evaluationsWithoutEnrolmentPeriod = evaluationsWithoutEnrolmentPeriod;
    }

    private void processEnroledEvaluations() {
        if (getEvaluationType().equals(ALL) || getEvaluationType().equals(EXAMS)) {
            for (final Exam exam : getStudent().getEnroledExams(getExecutionPeriod())) {
                try {
                    exam.isInEnrolmentPeriod();
                    this.enroledEvaluations.add(exam);
                } catch (final DomainException e) {
                    getEvaluationsWithoutEnrolmentPeriod().add(exam);
                } finally {
                    getExecutionCourses().put(exam.getIdInternal(),
                            exam.getAttendingExecutionCoursesFor(getStudent()));
                }
            }
        }
        if (getEvaluationType().equals(ALL) || getEvaluationType().equals(WRITTENTESTS)) {
            for (final WrittenTest writtenTest : getStudent().getEnroledWrittenTests(
                    getExecutionPeriod())) {
                try {
                    writtenTest.isInEnrolmentPeriod();
                    this.enroledEvaluations.add(writtenTest);
                } catch (final DomainException e) {
                    getEvaluationsWithoutEnrolmentPeriod().add(writtenTest);
                } finally {
                    getExecutionCourses().put(writtenTest.getIdInternal(),
                            writtenTest.getAttendingExecutionCoursesFor(getStudent()));
                }
            }
        }
        Collections.sort(this.enroledEvaluations, comparatorChain);
    }

    private void processNotEnroledEvaluations() {
        if (getEvaluationType().equals(ALL) || getEvaluationType().equals(EXAMS)) {
            for (final Exam exam : getStudent().getUnenroledExams(getExecutionPeriod())) {
            	if (exam.isExamsMapPublished()) {
            		try {
            			exam.isInEnrolmentPeriod();
            			this.notEnroledEvaluations.add(exam);
            		} catch (final DomainException e) {
            			getEvaluationsWithoutEnrolmentPeriod().add(exam);
            		} finally {
            			getExecutionCourses().put(exam.getIdInternal(),
            					exam.getAttendingExecutionCoursesFor(getStudent()));
            		}
            	}
            }
        }
        if (getEvaluationType().equals(ALL) || getEvaluationType().equals(WRITTENTESTS)) {
            for (final WrittenTest writtenTest : getStudent().getUnenroledWrittenTests(
                    getExecutionPeriod())) {
                try {
                    writtenTest.isInEnrolmentPeriod();
                    this.notEnroledEvaluations.add(writtenTest);
                } catch (final DomainException e) {
                    getEvaluationsWithoutEnrolmentPeriod().add(writtenTest);
                } finally {
                    getExecutionCourses().put(writtenTest.getIdInternal(),
                            writtenTest.getAttendingExecutionCoursesFor(getStudent()));
                }
            }
        }
        Collections.sort(this.notEnroledEvaluations, comparatorChain);
    }

    public void changeExecutionPeriod(ValueChangeEvent event) {
        clearAttributes();
    }

    public void changeEvaluationType(ValueChangeEvent event) {
        clearAttributes();
    }

    protected void clearAttributes() {
        setNotEnroledEvaluations(null);
        setEnroledEvaluations(null);
        setEvaluationsWithoutEnrolmentPeriod(null);
        setExecutionCourses(null);
    }

    private List<InfoExecutionPeriod> getExecutionPeriods() {
        try {
            final Object args[] = {};
            return (List<InfoExecutionPeriod>) ServiceManagerServiceFactory.executeService(
                    getUserView(), "ReadNotClosedExecutionPeriods", args);
        } catch (FenixFilterException e) {
        } catch (FenixServiceException e) {
        }
        return new ArrayList();
    }

    private InfoExecutionPeriod getCurrentExecutionPeriod() {
        try {
            final Object args[] = {};
            return (InfoExecutionPeriod) ServiceManagerServiceFactory.executeService(getUserView(),
                    "ReadCurrentExecutionPeriod", args);
        } catch (FenixFilterException e) {
        } catch (FenixServiceException e) {
        }
        return null;
    }

    protected ExecutionPeriod getExecutionPeriod() {
        return executionPeriod == null && getExecutionPeriodID() != null ?
            rootDomainObject.readExecutionPeriodByOID(getExecutionPeriodID()) : executionPeriod;
    }

    protected Registration getStudent() {
        if (this.student == null) {
            try {
                final Object args[] = { getUserView().getUtilizador() };
                this.student = (Registration) ServiceUtils.executeService(getUserView(),
                        "ReadStudentByUsernameForEvaluationEnrolment", args);
            } catch (FenixFilterException e) {
            } catch (FenixServiceException e) {
            }
        }
        return this.student;
    }

    public Integer getExecutionPeriodID() {
        if (this.executionPeriodID == null) {
            this.executionPeriodID = getCurrentExecutionPeriod().getIdInternal();
        }
        return executionPeriodID;
    }

    public void setExecutionPeriodID(Integer executionPeriodID) {
        this.executionPeriodID = executionPeriodID;
    }

    public Integer getEvaluationType() {
        if (this.evaluationType == null) {
            this.evaluationType = ALL;
        }
        return this.evaluationType;
    }

    public String getEvaluationTypeString() {
        final Integer type = getEvaluationType();
        if (type != null && type.equals(EXAMS)) {
            return "net.sourceforge.fenixedu.domain.Exam";
        } else if (type != null && type.equals(WRITTENTESTS)) {
            return "net.sourceforge.fenixedu.domain.WrittenTest";
        }
        return "";
    }

    public void setEvaluationType(Integer evaluationType) {
        this.evaluationType = evaluationType;
    }

    public Map<Integer, List<ExecutionCourse>> getExecutionCourses() {
        if (this.executionCourses == null) {
            this.executionCourses = new HashMap<Integer, List<ExecutionCourse>>();
        }
        return this.executionCourses;
    }

    public void setExecutionCourses(Map<Integer, List<ExecutionCourse>> executionCourses) {
        this.executionCourses = executionCourses;
    }
}
