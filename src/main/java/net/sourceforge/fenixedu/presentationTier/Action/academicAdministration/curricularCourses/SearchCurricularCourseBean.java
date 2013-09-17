package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.curricularCourses;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.RootCourseGroup;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class SearchCurricularCourseBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private final DegreeCurricularPlan degreeCurricularPlan;
    private ExecutionYear beginExecutionYear;
    private ExecutionYear endExecutionYear;
    private String name;

    public SearchCurricularCourseBean(DegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    public Set<Context> search() {
        Set<Context> result = new HashSet<Context>();
        RootCourseGroup root = getDegreeCurricularPlan().getRoot();
        searchRecursive(root, result);

        return result;
    }

    private void searchRecursive(final CourseGroup courseGroup, Set<Context> result) {
        Collection<Context> childContexts = courseGroup.getChildContexts();

        for (Context context : childContexts) {
            DegreeModule childDegreeModule = context.getChildDegreeModule();

            if (childDegreeModule.isCourseGroup()) {
                searchRecursive((CourseGroup) childDegreeModule, result);
                continue;
            }

            if (getBeginExecutionYear() != null
                    && context.getBeginExecutionPeriod().getExecutionYear().isBefore(getBeginExecutionYear())) {
                continue;
            }

            if (getEndExecutionYear() != null
                    && ((context.getEndExecutionPeriod() == null) || (context.getEndExecutionPeriod().getExecutionYear()
                            .isAfter(getEndExecutionYear())))) {
                continue;
            }

            MultiLanguageString nameI18N = childDegreeModule.getNameI18N();
            Collection<String> allContents = nameI18N.getAllContents();

            Pattern searchPattern = getSearchRegex();

            for (String degreeModuleName : allContents) {
                if (searchPattern.matcher(degreeModuleName).matches()) {
                    result.add(context);
                }
            }
        }
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return degreeCurricularPlan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Pattern getSearchRegex() {
        String[] split = getName().split("\\s+");

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < split.length; i++) {
            String compound = split[i];
            sb.append("(.*").append(compound).append(".*)");

            if (i < split.length - 1) {
                sb.append("(\\s+)");
            }
        }

        Pattern searchPattern = Pattern.compile(sb.toString());
        return searchPattern;
    }

    public ExecutionYear getBeginExecutionYear() {
        return beginExecutionYear;
    }

    public void setBeginExecutionYear(ExecutionYear beginExecutionYear) {
        this.beginExecutionYear = beginExecutionYear;
    }

    public ExecutionYear getEndExecutionYear() {
        return endExecutionYear;
    }

    public void setEndExecutionYear(ExecutionYear endExecutionYear) {
        this.endExecutionYear = endExecutionYear;
    }
}
