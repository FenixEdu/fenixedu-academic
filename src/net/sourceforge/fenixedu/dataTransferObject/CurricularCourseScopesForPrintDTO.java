package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CurricularCourseScopesForPrintDTO extends DataTranferObject {

    private List<DegreeCurricularPlanForPrintDTO> degreeCurricularPlans;

    public CurricularCourseScopesForPrintDTO() {
        super();
        degreeCurricularPlans = new ArrayList<DegreeCurricularPlanForPrintDTO>();
    }

    public void add(final InfoCurricularCourseScope scope) {

        DegreeCurricularPlanForPrintDTO selectedCurricularPlan = getSelectedCurricularPlan(scope);

        CurricularYearForPrintDTO selectedCurricularYear = getSelectedCurricularYear(scope,
                selectedCurricularPlan);

        BranchForPrintDTO selectedBranch = getSelectedBranch(scope, selectedCurricularYear);

        CurricularSemesterForPrintDTO selectedSemester = getSelectedSemester(scope, selectedBranch);

        addScope(scope, selectedSemester);

    }

    private void addScope(final InfoCurricularCourseScope scope,
            CurricularSemesterForPrintDTO selectedSemester) {
        selectedSemester.getScopes().add(scope);
        sortScopes(selectedSemester.getScopes());
    }

    private void sortScopes(List<InfoCurricularCourseScope> scopes) {
        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("anotation"));
        comparatorChain.addComparator(new BeanComparator("infoCurricularCourse.name"));
        Collections.sort(scopes, comparatorChain);
    }

    private CurricularSemesterForPrintDTO getSelectedSemester(final InfoCurricularCourseScope scope,
            BranchForPrintDTO selectedBranch) {
        CurricularSemesterForPrintDTO selectedSemester = (CurricularSemesterForPrintDTO) CollectionUtils
                .find(selectedBranch.getSemesters(), new Predicate() {

                    public boolean evaluate(Object arg0) {
                        CurricularSemesterForPrintDTO curricularSemesterForPrintDTO = (CurricularSemesterForPrintDTO) arg0;
                        if (curricularSemesterForPrintDTO.getSemester().equals(
                                scope.getInfoCurricularSemester().getSemester())) {
                            return true;
                        }
                        return false;
                    }

                });
        if (selectedSemester == null) {
            selectedSemester = new CurricularSemesterForPrintDTO(scope.getInfoCurricularSemester()
                    .getSemester());
            selectedBranch.getSemesters().add(selectedSemester);
        }

        return selectedSemester;
    }

    private BranchForPrintDTO getSelectedBranch(final InfoCurricularCourseScope scope,
            CurricularYearForPrintDTO selectedCurricularYear) {
        BranchForPrintDTO selectedBranch = (BranchForPrintDTO) CollectionUtils.find(
                selectedCurricularYear.getBranches(), new Predicate() {

                    public boolean evaluate(Object arg0) {
                        BranchForPrintDTO branchForPrintDTO = (BranchForPrintDTO) arg0;
                        if (branchForPrintDTO.getName().equals(scope.getInfoBranch().getName())) {
                            return true;
                        }

                        return false;
                    }

                });

        if (selectedBranch == null) {
            selectedBranch = new BranchForPrintDTO(scope.getInfoBranch().getName());
            selectedCurricularYear.getBranches().add(selectedBranch);

        }

        return selectedBranch;
    }

    private CurricularYearForPrintDTO getSelectedCurricularYear(final InfoCurricularCourseScope scope,
            DegreeCurricularPlanForPrintDTO selectedCurricularPlan) {
        CurricularYearForPrintDTO selectedCurricularYear = (CurricularYearForPrintDTO) CollectionUtils
                .find(selectedCurricularPlan.getYears(), new Predicate() {

                    public boolean evaluate(Object arg0) {
                        CurricularYearForPrintDTO curricularYearForPrintDTO = (CurricularYearForPrintDTO) arg0;
                        if (curricularYearForPrintDTO.getYear().equals(
                                scope.getInfoCurricularSemester().getInfoCurricularYear().getYear())) {
                            return true;
                        }

                        return false;
                    }

                });
        if (selectedCurricularYear == null) {
            selectedCurricularYear = new CurricularYearForPrintDTO(scope.getInfoCurricularSemester()
                    .getInfoCurricularYear().getYear());
            selectedCurricularPlan.getYears().add(selectedCurricularYear);

        }

        return selectedCurricularYear;
    }

    private DegreeCurricularPlanForPrintDTO getSelectedCurricularPlan(
            final InfoCurricularCourseScope scope) {
        DegreeCurricularPlanForPrintDTO selectedCurricularPlan = (DegreeCurricularPlanForPrintDTO) CollectionUtils
                .find(getDegreeCurricularPlans(), new Predicate() {

                    public boolean evaluate(Object arg0) {
                        DegreeCurricularPlanForPrintDTO degreeCurricularPlanForPrintDTO = (DegreeCurricularPlanForPrintDTO) arg0;
                        if (degreeCurricularPlanForPrintDTO.name.equals(scope.getInfoCurricularCourse()
                                .getInfoDegreeCurricularPlan().getName())) {
                            return true;
                        }

                        return false;
                    }
                });

        if (selectedCurricularPlan == null) {
            InfoDegreeCurricularPlan degreeCurricularPlan = scope.getInfoCurricularCourse()
                    .getInfoDegreeCurricularPlan();
            selectedCurricularPlan = new DegreeCurricularPlanForPrintDTO(degreeCurricularPlan.getName(),
                    degreeCurricularPlan.getInfoDegree().getNome(), degreeCurricularPlan.getAnotation());
            this.getDegreeCurricularPlans().add(selectedCurricularPlan);
        }

        return selectedCurricularPlan;
    }

    public class DegreeCurricularPlanForPrintDTO extends DataTranferObject {

        private String name;

        private String degreeName;

        private String anotation;

        private List<CurricularYearForPrintDTO> years;

        public DegreeCurricularPlanForPrintDTO(String name, String degreeName, String anotation) {
            super();

            this.years = new ArrayList<CurricularYearForPrintDTO>();
            this.name = name;
            this.degreeName = degreeName;
            this.anotation = anotation;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<CurricularYearForPrintDTO> getYears() {
            return years;
        }

        public void setYears(List<CurricularYearForPrintDTO> years) {
            this.years = years;
        }

        public String getAnotation() {
            return anotation;
        }

        public void setAnotation(String anotation) {
            this.anotation = anotation;
        }

        public String getDegreeName() {
            return degreeName;
        }

        public void setDegreeName(String degreeName) {
            this.degreeName = degreeName;
        }

    }

    public class CurricularYearForPrintDTO extends DataTranferObject {

        private Integer year;

        private List<BranchForPrintDTO> branches;

        public CurricularYearForPrintDTO(Integer year) {
            super();

            this.branches = new ArrayList<BranchForPrintDTO>();
            this.year = year;
        }

        public List<BranchForPrintDTO> getBranches() {
            return branches;
        }

        public void setBranches(List<BranchForPrintDTO> branches) {
            this.branches = branches;
        }

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }

    }

    public class BranchForPrintDTO extends DataTranferObject {

        private String name;

        private List<CurricularSemesterForPrintDTO> semesters;

        public BranchForPrintDTO(String name) {
            super();

            this.semesters = new ArrayList<CurricularSemesterForPrintDTO>();
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<CurricularSemesterForPrintDTO> getSemesters() {
            return semesters;
        }

        public void setSemesters(List<CurricularSemesterForPrintDTO> semester) {
            this.semesters = semester;
        }

    }

    public class CurricularSemesterForPrintDTO extends DataTranferObject {

        private Integer semester;

        private List<InfoCurricularCourseScope> scopes;

        public CurricularSemesterForPrintDTO(Integer semester) {
            super();

            this.scopes = new ArrayList<InfoCurricularCourseScope>();
            this.semester = semester;
        }

        public void add(InfoCurricularCourseScope scope) {
            getScopes().add(scope);

        }

        public List<InfoCurricularCourseScope> getScopes() {
            return scopes;
        }

        public void setScopes(List<InfoCurricularCourseScope> scopes) {
            this.scopes = scopes;
        }

        public Integer getSemester() {
            return semester;
        }

        public void setSemester(Integer semester) {
            this.semester = semester;
        }

    }

    public List<DegreeCurricularPlanForPrintDTO> getDegreeCurricularPlans() {
        return degreeCurricularPlans;
    }

    public void setDegreeCurricularPlans(List<DegreeCurricularPlanForPrintDTO> degreeCurricularPlans) {
        this.degreeCurricularPlans = degreeCurricularPlans;
    }

}
