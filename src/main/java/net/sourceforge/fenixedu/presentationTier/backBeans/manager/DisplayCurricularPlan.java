package net.sourceforge.fenixedu.presentationTier.backBeans.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadActiveDegreeCurricularPlansByDegreeType;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionYearByID;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionYears;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.curriculumHistoric.ReadActiveCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.CurricularCourseScopesForPrintDTO;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.CurricularCourseScope.DegreeModuleScopeCurricularCourseScope;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DisplayCurricularPlan extends FenixBackingBean {

    private Integer[] choosenDegreeCurricularPlansIDs;

    private Integer choosenExecutionYearID;

    public String choose() {
        return "success";
    }

    public List getDegreeCurricularPlans() throws FenixFilterException, FenixServiceException {

        List degreeCurricularPlans = (List) ReadActiveDegreeCurricularPlansByDegreeType.run(DegreeType.DEGREE);

        List<SelectItem> result = new ArrayList<SelectItem>(degreeCurricularPlans.size());
        for (InfoDegreeCurricularPlan degreeCurricularPlan : (List<InfoDegreeCurricularPlan>) degreeCurricularPlans) {
            String label = degreeCurricularPlan.getInfoDegree().getNome() + " - " + degreeCurricularPlan.getName();
            result.add(new SelectItem(degreeCurricularPlan.getIdInternal(), label));
        }

        return result;
    }

    public List getExecutionYears() throws FenixFilterException, FenixServiceException {

        List<InfoExecutionYear> executionYears = ReadNotClosedExecutionYears.run();

        List<SelectItem> result = new ArrayList<SelectItem>(executionYears.size());
        for (InfoExecutionYear executionYear : executionYears) {
            result.add(new SelectItem(executionYear.getIdInternal(), executionYear.getYear()));
        }

        if (executionYears.size() > 0) {
            setChoosenExecutionYearID(executionYears.get(executionYears.size() - 1).getIdInternal());
        }

        return result;
    }

    public String getChoosenExecutionYear() throws FenixFilterException, FenixServiceException {

        InfoExecutionYear executionYear = ReadExecutionYearByID.run(getChoosenExecutionYearID());

        return executionYear.getYear();
    }

    public List getScopes() throws FenixFilterException, FenixServiceException {

        List<InfoCurricularCourseScope> scopes = new ArrayList<InfoCurricularCourseScope>();

        for (Integer degreeCurricularPlanID : this.getChoosenDegreeCurricularPlansIDs()) {
            Collection<DegreeModuleScope> degreeModuleScopes =
                    ReadActiveCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear
                            .runReadActiveCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlanID,
                                    this.choosenExecutionYearID);

            for (final DegreeModuleScope degreeModuleScope : degreeModuleScopes) {
                if (degreeModuleScope instanceof DegreeModuleScopeCurricularCourseScope) {
                    scopes.add(InfoCurricularCourseScope
                            .newInfoFromDomain(((DegreeModuleScopeCurricularCourseScope) degreeModuleScope)
                                    .getCurricularCourseScope()));
                }
            }
        }

        sortScopes(scopes);

        CurricularCourseScopesForPrintDTO scopesForPrintDTO = new CurricularCourseScopesForPrintDTO();
        for (InfoCurricularCourseScope scope : scopes) {
            scopesForPrintDTO.add(scope);
        }

        return scopesForPrintDTO.getDegreeCurricularPlans();
    }

    private void sortScopes(List<InfoCurricularCourseScope> scopes) {
        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.infoCurricularYear.year"));
        comparatorChain.addComparator(new BeanComparator("infoBranch.name"));
        comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.semester"));
        comparatorChain.addComparator(new BeanComparator("infoCurricularCourse.name"));
        Collections.sort(scopes, comparatorChain);
    }

    public Integer getChoosenExecutionYearID() {
        return choosenExecutionYearID;
    }

    public void setChoosenExecutionYearID(Integer choosenExecutionYearID) {
        this.choosenExecutionYearID = choosenExecutionYearID;
    }

    public Integer[] getChoosenDegreeCurricularPlansIDs() {
        return choosenDegreeCurricularPlansIDs;
    }

    public void setChoosenDegreeCurricularPlansIDs(Integer[] choosenDegreeCurricularPlansIDs) {
        this.choosenDegreeCurricularPlansIDs = choosenDegreeCurricularPlansIDs;
    }

}