package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degreeStructure.Context;

public abstract class DegreeModuleScope {
    
    public abstract Integer getIdInternal();
    public abstract Integer getCurricularSemester();
    public abstract Integer getCurricularYear();    
    public abstract String getBranch();
    public abstract String getAnotation();
    public abstract CurricularCourse getCurricularCourse();
    public abstract boolean isActiveForExecutionPeriod(ExecutionPeriod executionPeriod);
    
    public static List<DegreeModuleScope> getDegreeModuleScopes(WrittenEvaluation writtenEvaluation) {
        return getDegreeModuleScopes(writtenEvaluation.getAssociatedCurricularCourseScope(), writtenEvaluation.getAssociatedContexts());
    }
    
    public static List<DegreeModuleScope> getDegreeModuleScopes(CurricularCourse curricularCourse) {
        return getDegreeModuleScopes(curricularCourse.getScopes(), curricularCourse.getParentContexts());
    }
    
    private static List<DegreeModuleScope> getDegreeModuleScopes(List<CurricularCourseScope> curricularCourseScopes, List<Context> contexts){
        List<DegreeModuleScope> degreeModuleScopes = new ArrayList<DegreeModuleScope>();
        for (CurricularCourseScope curricularCourseScope : curricularCourseScopes) {
            degreeModuleScopes.add(curricularCourseScope.getDegreeModuleScopeCurricularCourseScope());
        }
        for (Context context : contexts) {
            degreeModuleScopes.add(context.getDegreeModuleScopeContext());
        }
        return degreeModuleScopes;
    }
    
}
