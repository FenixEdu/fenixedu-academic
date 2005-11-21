package net.sourceforge.fenixedu.presentationTier.backBeans.student;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IProject;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.IWrittenEvaluation;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.presentationTier.jsf.components.util.CalendarLink;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.util.MessageResources;

public class StudentCalendarBackingBean extends FenixBackingBean {

	private static final DateFormat hourFormat = new SimpleDateFormat("HH:mm");
	private static final MessageResources messages = MessageResources.getMessageResources("ServidorApresentacao/StudentResources");
	private static final ComparatorChain executionPeriodComparator = new ComparatorChain();
	static {
		executionPeriodComparator.addComparator(new BeanComparator("executionYear.year"), true);
		executionPeriodComparator.addComparator(new BeanComparator("semester"), true);
	}
	private static final Comparator<IExecutionCourse> executionCourseComparator = new BeanComparator("nome");

	private Collection<IExecutionPeriod> executionPeriods;
	private Collection<IExecutionCourse> executionCourses;
	private IExecutionPeriod executionPeriod;
	private IStudent student;

	private Integer executionPeriodID;
	private Integer executionCourseID;
    private String evaluationTypeClassname;

	public Collection<IExecutionPeriod> getExecutionPeriods() throws FenixFilterException, FenixServiceException {
		if (executionPeriods == null) {
	        final IStudent student = getStudent();

	        executionPeriods = new TreeSet<IExecutionPeriod>(executionPeriodComparator);
	        for (final IAttends attends : student.getAssociatedAttends()) {
	        	executionPeriods.add(attends.getDisciplinaExecucao().getExecutionPeriod());
	        }
		}
        return executionPeriods;
	}

	public Collection<IExecutionCourse> getExecutionCourses() throws FenixFilterException, FenixServiceException {
		final IExecutionPeriod executionPeriod = getExecutionPeriod();

		if (executionCourses == null || executionPeriod != executionCourses.iterator().next().getExecutionPeriod()) {
	        final IStudent student = getStudent();

	        executionCourses = new TreeSet<IExecutionCourse>(executionCourseComparator);
	        for (final IAttends attends : student.getAssociatedAttends()) {
	        	final IExecutionCourse executionCourse = attends.getDisciplinaExecucao();
	        	if (executionCourse.getExecutionPeriod() == executionPeriod) {
	        		executionCourses.add(executionCourse);
	        	}
	        }
		}
        return executionCourses;
	}

	public IExecutionPeriod getExecutionPeriod() throws FenixFilterException, FenixServiceException {
		final Integer executionPeriodID = getExecutionPeriodID();
		if (executionPeriod == null || !executionPeriodID.equals(executionPeriod.getIdInternal())) {
			final Collection<IExecutionPeriod> executionPeriods = getExecutionPeriods();
			if (executionPeriods != null) {
				for (final IExecutionPeriod executionPeriod : executionPeriods) {
					if (executionPeriod.getIdInternal().equals(executionPeriodID)) {
						this.executionPeriod = executionPeriod;
						break;
					}
				}
			}			
		}
		return executionPeriod;
	}

	public IStudent getStudent() {
		if (student == null) {
			final List<IStudent> students = getUserView().getPerson().getStudents();
			for (final IStudent student : students) {
				if (student.getDegreeType() == DegreeType.DEGREE) {
					this.student = student;
					break;
				}
			}
		}
		return student;
	}

    public Date getCalendarStartDate() throws FenixFilterException, FenixServiceException {
        final IExecutionPeriod executionPeriod = getExecutionPeriod();
        final String evaluationTypeClassname = getEvaluationTypeClassname();
        final IStudentCurricularPlan studentCurricularPlan = getStudent().getActiveStudentCurricularPlan();
        final IDegreeCurricularPlan degreeCurricularPlan = (studentCurricularPlan != null) ? studentCurricularPlan.getDegreeCurricularPlan() : null;
        final IExecutionDegree executionDegree = findExecutinDegree(degreeCurricularPlan, executionPeriod);
        if (evaluationTypeClassname == null || evaluationTypeClassname.length() == 0 || executionDegree == null) {
        	if (executionDegree != null && executionPeriod.getSemester().intValue() == 1 && executionDegree.getPeriodLessonsFirstSemester() != null) {
        		return executionDegree.getPeriodLessonsFirstSemester().getStart();
        	} else if (executionDegree != null && executionPeriod.getSemester().intValue() == 2 && executionDegree.getPeriodLessonsSecondSemester() != null) {
        		return executionDegree.getPeriodLessonsSecondSemester().getStart();
        	} else {
        		return executionPeriod.getBeginDate();
        	}
        } else {
            if (evaluationTypeClassname.equals(Exam.class.getName())) {
                if (executionPeriod.getSemester().intValue() == 1) {
                    return executionDegree.getPeriodExamsFirstSemester().getStart();
                } else if (executionPeriod.getSemester().intValue() == 2) {
                    return executionDegree.getPeriodExamsSecondSemester().getStart();
                }
            } else if (evaluationTypeClassname.equals(WrittenTest.class.getName())) {
                if (executionPeriod.getSemester().intValue() == 1) {
                    return executionDegree.getPeriodLessonsFirstSemester().getStart();
                } else if (executionPeriod.getSemester().intValue() == 2) {
                    return executionDegree.getPeriodLessonsSecondSemester().getStart();
                }
            } else if (evaluationTypeClassname.equals(WrittenTest.class.getName())
                    || evaluationTypeClassname.equals(Project.class.getName())) {
                if (executionPeriod.getSemester().intValue() == 1) {
                    return executionDegree.getPeriodLessonsFirstSemester().getStart();
                } else if (executionPeriod.getSemester().intValue() == 2) {
                    return executionDegree.getPeriodLessonsSecondSemester().getStart();
                }
            }
        }
        return null;
    }

    public Date getCalendarEndDate() throws FenixFilterException, FenixServiceException {
        final IExecutionPeriod executionPeriod = getExecutionPeriod();
        final String evaluationTypeClassname = getEvaluationTypeClassname();
        final IStudentCurricularPlan studentCurricularPlan = getStudent().getActiveStudentCurricularPlan();
        final IDegreeCurricularPlan degreeCurricularPlan = (studentCurricularPlan != null) ? studentCurricularPlan.getDegreeCurricularPlan() : null;
        final IExecutionDegree executionDegree = findExecutinDegree(degreeCurricularPlan, executionPeriod);
        if (evaluationTypeClassname == null || evaluationTypeClassname.length() == 0 || executionDegree == null) {
        	if (executionDegree != null && executionPeriod.getSemester().intValue() == 1 && executionDegree.getPeriodExamsFirstSemester() != null) {
        		return executionDegree.getPeriodExamsFirstSemester().getEnd();
        	} else if (executionDegree != null && executionPeriod.getSemester().intValue() == 2 && executionDegree.getPeriodExamsSecondSemester() != null) {
        		return executionDegree.getPeriodExamsSecondSemester().getEnd();
        	} else {
        		return executionPeriod.getEndDate();
        	}
        } else {
            if (evaluationTypeClassname.equals(Exam.class.getName())) {
                if (executionPeriod.getSemester().intValue() == 1) {
                    return executionDegree.getPeriodExamsFirstSemester().getEnd();
                } else if (executionPeriod.getSemester().intValue() == 2) {
                    return executionDegree.getPeriodExamsSecondSemester().getEnd();
                }
            } else if (evaluationTypeClassname.equals(WrittenTest.class.getName())
                    || evaluationTypeClassname.equals(Project.class.getName())) {
                if (executionPeriod.getSemester().intValue() == 1) {
                    return executionDegree.getPeriodLessonsFirstSemester().getEnd();
                } else if (executionPeriod.getSemester().intValue() == 2) {
                    return executionDegree.getPeriodLessonsSecondSemester().getEnd();
                }
            }
        }
        return null;
    }

    private IExecutionDegree findExecutinDegree(final IDegreeCurricularPlan degreeCurricularPlan, final IExecutionPeriod executionPeriod) {
        if (degreeCurricularPlan != null) {
            for (final IExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
                if (executionDegree.getExecutionYear() == executionPeriod.getExecutionYear()) {
                    return executionDegree;
                }
            }
        }
        return null;
    }

    public List<CalendarLink> getCalendarLinks() throws FenixFilterException, FenixServiceException {
        List<CalendarLink> calendarLinks = new ArrayList<CalendarLink>();

        final IExecutionPeriod executionPeriod = getExecutionPeriod();
        final IStudent student = getStudent();

        for (final IAttends attends : student.getAssociatedAttends()) {
        	final IExecutionCourse executionCourse = attends.getDisciplinaExecucao();
        	if (executionCourse.getExecutionPeriod() == executionPeriod
                    && (executionCourseID == null || executionCourseID.equals(executionCourse.getIdInternal()))) {
                for (final IEvaluation evaluation : executionCourse.getAssociatedEvaluations()) {
                    if (evaluation instanceof IWrittenEvaluation) {
                        final IWrittenEvaluation writtenEvaluation = (IWrittenEvaluation) evaluation;
                        final String evaluationTypeClassname = getEvaluationTypeClassname();
                        if (evaluationTypeClassname == null
                                || evaluationTypeClassname.length() == 0
                                || evaluationTypeClassname.equals(writtenEvaluation.getClass().getName())) {
                            CalendarLink calendarLink = new CalendarLink();
                            calendarLinks.add(calendarLink);

                            calendarLink.setObjectOccurrence(writtenEvaluation.getDay());
                            calendarLink.setObjectLinkLabel(constructCalendarPresentation(executionCourse, writtenEvaluation));
                            calendarLink.setLinkParameters(constructLinkParameters(executionCourse));
                        }
                    } else if (evaluation instanceof IProject) {
                        final IProject project = (IProject) evaluation;
                        final String evaluationTypeClassname = getEvaluationTypeClassname();
                        if (evaluationTypeClassname == null
                                || evaluationTypeClassname.length() == 0
                                || evaluationTypeClassname.equals(project.getClass().getName())) {
                            CalendarLink calendarLinkBegin = new CalendarLink();
                            calendarLinks.add(calendarLinkBegin);
                            calendarLinkBegin.setObjectOccurrence(project.getBegin());
                            calendarLinkBegin.setObjectLinkLabel(constructCalendarPresentation(executionCourse, project, project.getBegin(), messages.getMessage("label.evaluation.project.begin")));
                            calendarLinkBegin.setLinkParameters(constructLinkParameters(executionCourse));

                            CalendarLink calendarLinkEnd = new CalendarLink();
                            calendarLinks.add(calendarLinkEnd);
                            calendarLinkEnd.setObjectOccurrence(project.getEnd());
                            calendarLinkEnd.setObjectLinkLabel(constructCalendarPresentation(executionCourse, project, project.getEnd(), messages.getMessage("label.evaluation.project.end")));
                            calendarLinkEnd.setLinkParameters(constructLinkParameters(executionCourse));
                        }
                    }
                }        		
        	}
        }
        return calendarLinks;
    }

    public List<SelectItem> getExecutionPeriodSelectItems() throws FenixFilterException, FenixServiceException {
        final List<SelectItem> executionPeriodSelectItems = new ArrayList<SelectItem>();

      	for (final IExecutionPeriod executionPeriod : getExecutionPeriods()) {
       		if (executionPeriod.getState() != PeriodState.NOT_OPEN) {
       			final IExecutionYear executionYear = executionPeriod.getExecutionYear();
       			executionPeriodSelectItems.add(new SelectItem(executionPeriod.getIdInternal(), executionPeriod.getName() + " " + executionYear.getYear()));
       		}
        }

        return executionPeriodSelectItems;
    }

    public List<SelectItem> getExecutionCourseSelectItems() throws FenixFilterException, FenixServiceException {
        final List<SelectItem> executionPeriodSelectItems = new ArrayList<SelectItem>();

      	for (final IExecutionCourse executionCourse : getExecutionCourses()) {
   			executionPeriodSelectItems.add(new SelectItem(executionCourse.getIdInternal(), executionCourse.getNome()));
   		}

        return executionPeriodSelectItems;
    }

    private Map<String, String> constructLinkParameters(final IExecutionCourse executionCourse) {
        final ISite site = executionCourse.getSite();

        final Map<String, String> linkParameters = new HashMap<String, String>();
        linkParameters.put("method", "evaluations");
        linkParameters.put("objectCode", (site != null) ? site.getIdInternal().toString() : null);
        linkParameters.put("executionPeriodOID", executionCourse.getExecutionPeriod().getIdInternal().toString());
        return linkParameters;
    }

    private String constructCalendarPresentation(final IExecutionCourse executionCourse, final IProject project, final Date time, final String tail) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(messages.getMessage("label.evaluation.shortname.project"));        
        stringBuilder.append(" ");
        stringBuilder.append(executionCourse.getSigla());
        stringBuilder.append(" (");
        stringBuilder.append(hourFormat.format(time));
        stringBuilder.append(") ");
        stringBuilder.append(tail);
        return stringBuilder.toString();
    }

    private String constructCalendarPresentation(final IExecutionCourse executionCourse, final IWrittenEvaluation writtenEvaluation) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (writtenEvaluation instanceof WrittenTest) {
        	stringBuilder.append(messages.getMessage("label.evaluation.shortname.test"));
        } else if (writtenEvaluation instanceof Exam) {
        	stringBuilder.append(messages.getMessage("label.evaluation.shortname.exam"));
        }
        stringBuilder.append(" ");
        stringBuilder.append(executionCourse.getSigla());
        stringBuilder.append(" (");
        stringBuilder.append(hourFormat.format(writtenEvaluation.getBeginningDate()));
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public String getApplicationContext() {
        final String appContext = PropertiesManager.getProperty("app.context");
        return (appContext != null && appContext.length() > 0) ? "/" + appContext : "";
    }

	public Integer getExecutionPeriodID() throws FenixFilterException, FenixServiceException {
		if (executionPeriodID == null) {
			final Collection<IExecutionPeriod> executionPeriods = getExecutionPeriods();
			if (executionPeriods != null) {
				for (final IExecutionPeriod executionPeriod : executionPeriods) {
					if (executionPeriod.getState() == PeriodState.CURRENT) {
						this.executionPeriodID = executionPeriod.getIdInternal();
						break;
					}
				}
			}
		}
		return executionPeriodID;
	}

	public void setExecutionPeriodID(Integer executionPeriodID) {
		if (this.executionPeriodID != null || !this.executionPeriodID.equals(executionPeriodID)) {
			this.executionCourseID = null;
			this.executionCourses = null;
		}
		this.executionPeriodID = executionPeriodID;
	}

	public Integer getExecutionCourseID() {
		return executionCourseID;
	}

	public void setExecutionCourseID(Integer executionCourseID) throws FenixFilterException, FenixServiceException {
		this.executionCourseID = executionCourseID;
        final IExecutionCourse executionCourse = getExecutionCourse();
        this.executionPeriodID = executionCourse.getExecutionPeriod().getIdInternal();
	}

    public IExecutionCourse getExecutionCourse() throws FenixFilterException, FenixServiceException {
        final Object[] args = { ExecutionCourse.class, getExecutionCourseID() };
        return (IExecutionCourse) ServiceUtils.executeService(getUserView(), "ReadDomainObject", args);
    }

    public String getEvaluationTypeClassname() {
        return evaluationTypeClassname;
    }

    public void setEvaluationTypeClassname(final String evaluationTypeClassname) {
        this.evaluationTypeClassname = evaluationTypeClassname;
    }

}