package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;

public class LessonPlanning extends LessonPlanning_Base {
    
    public static final Comparator<LessonPlanning> COMPARATOR_BY_ORDER = new BeanComparator("orderOfPlanning");     
    
    private LessonPlanning() {        
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public LessonPlanning(MultiLanguageString title, MultiLanguageString planning, ShiftType lessonType, ExecutionCourse executionCourse) {
        this();
        checkParameters(title, planning, lessonType, executionCourse);
        setLastOrder(executionCourse, lessonType);
        setTitle(title);
        setPlanning(planning);
        setLessonType(lessonType);        
        setExecutionCourse(executionCourse);        
    }
    
    public void delete() {        
        reOrderLessonPlannings();
        removeExecutionCourse();
        deleteDomainObject();
    }

    public void moveTo(Integer order) {        
        List<LessonPlanning> lessonPlannings = getExecutionCourse().getLessonPlanningsOrderedByOrder(getLessonType());        
        if(!lessonPlannings.isEmpty() && order != getOrderOfPlanning() && order <= lessonPlannings.size() && order >= 1) {                       
            LessonPlanning posPlanning = lessonPlannings.get(order - 1);
            Integer posOrder = posPlanning.getOrderOfPlanning();
            posPlanning.setOrderOfPlanning(getOrderOfPlanning());
            setOrderOfPlanning(posOrder);
        }
    }
      
    private void reOrderLessonPlannings() {
        List<LessonPlanning> lessonPlannings = getExecutionCourse().getLessonPlanningsOrderedByOrder(getLessonType());
        if(!lessonPlannings.isEmpty() && !lessonPlannings.get(lessonPlannings.size() - 1).equals(this)) {                             
            for (int i = getOrderOfPlanning(); i < lessonPlannings.size(); i++) {
                LessonPlanning planning = lessonPlannings.get(i);
                planning.setOrderOfPlanning(planning.getOrderOfPlanning() - 1);
            }            
        }        
    }   

    private void setLastOrder(ExecutionCourse executionCourse, ShiftType lessonType) {
        List<LessonPlanning> lessonPlannings = executionCourse.getLessonPlanningsOrderedByOrder(lessonType);
        Integer order = (!lessonPlannings.isEmpty()) ? (lessonPlannings.get(lessonPlannings.size() - 1).getOrderOfPlanning() + 1) : 1;
        setOrderOfPlanning(order);
    }
    
    private void checkParameters(MultiLanguageString title, MultiLanguageString planning, ShiftType lessonType, ExecutionCourse executionCourse) {
        if(StringUtils.isEmpty(title.getContent())) {
            throw new DomainException("error.LessonPlanning.no.title");
        }
        if(StringUtils.isEmpty(planning.getContent())) {
            throw new DomainException("error.LessonPlanning.no.planning");
        }
        if(lessonType == null) {
            throw new DomainException("error.LessonPlanning.no.lessonType");
        }
        if(executionCourse == null) {
            throw new DomainException("error.LessonPlanning.no.executionCourse");
        }       
    }
}
