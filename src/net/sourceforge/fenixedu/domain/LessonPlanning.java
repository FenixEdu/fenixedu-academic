package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.commons.beanutils.BeanComparator;

public class LessonPlanning extends LessonPlanning_Base {

    public static final Comparator<LessonPlanning> COMPARATOR_BY_ORDER = new BeanComparator(
	    "orderOfPlanning");

    private LessonPlanning() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public LessonPlanning(MultiLanguageString title, MultiLanguageString planning, ShiftType lessonType,
	    ExecutionCourse executionCourse) {
	this();
	setLastOrder(executionCourse, lessonType);
	setTitle(title);
	setPlanning(planning);
	setLessonType(lessonType);
	setExecutionCourse(executionCourse);
    }

    @Override
    public void setLessonType(ShiftType lessonType) {
	if (lessonType == null) {
	    throw new DomainException("error.LessonPlanning.no.lessonType");
	}
	super.setLessonType(lessonType);
    }

    @Override
    public void setExecutionCourse(ExecutionCourse executionCourse) {
	if (executionCourse == null) {
	    throw new DomainException("error.LessonPlanning.no.executionCourse");
	}
	super.setExecutionCourse(executionCourse);
    }

    @Override
    public void setPlanning(MultiLanguageString planning) {
	if (planning == null || planning.getAllContents().isEmpty()) {
	    throw new DomainException("error.LessonPlanning.no.planning");
	}
	super.setPlanning(planning);
    }

    @Override
    public void setTitle(MultiLanguageString title) {
	if (title == null || title.getAllContents().isEmpty()) {
	    throw new DomainException("error.LessonPlanning.no.title");
	}
	super.setTitle(title);
    }

    public void delete() {
	reOrderLessonPlannings();
	deleteWithoutReOrder();
    }

    public void deleteWithoutReOrder() {
	super.setExecutionCourse(null);
	removeRootDomainObject();
	deleteDomainObject();
    }

    public void moveTo(Integer order) {
	List<LessonPlanning> lessonPlannings = getExecutionCourse().getLessonPlanningsOrderedByOrder(
		getLessonType());
	if (!lessonPlannings.isEmpty() && order != getOrderOfPlanning()
		&& order <= lessonPlannings.size() && order >= 1) {
	    LessonPlanning posPlanning = lessonPlannings.get(order - 1);
	    Integer posOrder = posPlanning.getOrderOfPlanning();
	    posPlanning.setOrderOfPlanning(getOrderOfPlanning());
	    setOrderOfPlanning(posOrder);
	}
    }

    private void reOrderLessonPlannings() {
	List<LessonPlanning> lessonPlannings = getExecutionCourse().getLessonPlanningsOrderedByOrder(
		getLessonType());
	if (!lessonPlannings.isEmpty() && !lessonPlannings.get(lessonPlannings.size() - 1).equals(this)) {
	    for (int i = getOrderOfPlanning(); i < lessonPlannings.size(); i++) {
		LessonPlanning planning = lessonPlannings.get(i);
		planning.setOrderOfPlanning(planning.getOrderOfPlanning() - 1);
	    }
	}
    }

    private void setLastOrder(ExecutionCourse executionCourse, ShiftType lessonType) {
	List<LessonPlanning> lessonPlannings = executionCourse
		.getLessonPlanningsOrderedByOrder(lessonType);
	Integer order = (!lessonPlannings.isEmpty()) ? (lessonPlannings.get(lessonPlannings.size() - 1)
		.getOrderOfPlanning() + 1) : 1;
	setOrderOfPlanning(order);
    }

    public String getLessonPlanningLabel() {
	StringBuilder builder = new StringBuilder();
	builder.append(RenderUtils.getResourceString("DEFAULT", "label.lesson")).append(" ");
	builder.append(getOrderOfPlanning()).append(" (");
	builder.append(RenderUtils.getEnumString(getLessonType(), null)).append(") - ");
	builder.append(getTitle().getContent());
	return builder.toString();
    }
}
