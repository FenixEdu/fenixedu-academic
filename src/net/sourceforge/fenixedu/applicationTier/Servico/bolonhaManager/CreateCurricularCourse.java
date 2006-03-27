/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.CurricularPeriodInfoDTO;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateCurricularCourse extends Service {

    private DegreeCurricularPlan degreeCurricularPlan = null;
    private CourseGroup parentCourseGroup = null;
    private CurricularPeriod curricularPeriod = null;
    private ExecutionPeriod beginExecutionPeriod = null;
    private ExecutionPeriod endExecutionPeriod = null;

    public void run(CreateCurricularCourseArgs createCurricularCourseArgs) throws ExcepcaoPersistencia,
            FenixServiceException {

        readDomainObjects(createCurricularCourseArgs.getDegreeCurricularPlanID(),
                createCurricularCourseArgs.getParentCourseGroupID(), createCurricularCourseArgs
                        .getYear(), createCurricularCourseArgs.getSemester(), createCurricularCourseArgs
                        .getBeginExecutionPeriodID(), createCurricularCourseArgs
                        .getEndExecutionPeriodID());

        final CompetenceCourse competenceCourse = rootDomainObject
                .readCompetenceCourseByOID(createCurricularCourseArgs.getCompetenceCourseID());
        if (competenceCourse == null) {
            throw new FenixServiceException("error.noCompetenceCourse");
        }
        degreeCurricularPlan.createCurricularCourse(createCurricularCourseArgs.getWeight(),
                createCurricularCourseArgs.getPrerequisites(), createCurricularCourseArgs
                        .getPrerequisitesEn(), CurricularStage.DRAFT, competenceCourse,
                parentCourseGroup, curricularPeriod, beginExecutionPeriod, endExecutionPeriod);
    }

    public void run(CreateOptionalCurricularCourseArgs createOptionalCurricularCourseArgs)
            throws ExcepcaoPersistencia, FenixServiceException {

        readDomainObjects(createOptionalCurricularCourseArgs.getDegreeCurricularPlanID(),
                createOptionalCurricularCourseArgs.getParentCourseGroupID(),
                createOptionalCurricularCourseArgs.getYear(), createOptionalCurricularCourseArgs
                        .getSemester(), createOptionalCurricularCourseArgs.getBeginExecutionPeriodID(),
                createOptionalCurricularCourseArgs.getEndExecutionPeriodID());

        degreeCurricularPlan.createCurricularCourse(parentCourseGroup,
                createOptionalCurricularCourseArgs.getName(), createOptionalCurricularCourseArgs
                        .getNameEn(), CurricularStage.DRAFT, curricularPeriod, beginExecutionPeriod,
                endExecutionPeriod);
    }

    protected void readDomainObjects(Integer degreeCurricularPlanID, Integer parentCourseGroupID,
            Integer year, Integer semester, Integer beginExecutionPeriodID, Integer endExecutionPeriodID)
            throws ExcepcaoPersistencia, FenixServiceException {

        degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
        if (degreeCurricularPlan == null) {
            throw new FenixServiceException("error.noDegreeCurricularPlan");
        }

        parentCourseGroup = (CourseGroup) rootDomainObject.readDegreeModuleByOID(parentCourseGroupID);
        if (parentCourseGroup == null) {
            throw new FenixServiceException("error.noCourseGroup");
        }

        final CurricularPeriod degreeCurricularPeriod = (CurricularPeriod) degreeCurricularPlan
                .getDegreeStructure();
        final CurricularPeriodInfoDTO curricularPeriodInfoYear = new CurricularPeriodInfoDTO(year,
                CurricularPeriodType.YEAR);
        final CurricularPeriodInfoDTO curricularPeriodInfoSemester = new CurricularPeriodInfoDTO(
                semester, CurricularPeriodType.SEMESTER);
        curricularPeriod = degreeCurricularPeriod.getCurricularPeriod(curricularPeriodInfoYear,
                curricularPeriodInfoSemester);
        if (curricularPeriod == null) {
            curricularPeriod = degreeCurricularPeriod.addCurricularPeriod(curricularPeriodInfoYear,
                    curricularPeriodInfoSemester);
        }

        if (beginExecutionPeriodID == null) {
            final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
            final ExecutionYear nextExecutionYear = currentExecutionYear.getNextExecutionYear();
            if (nextExecutionYear == null) {
                throw new FenixServiceException("error.no.next.execution.year");
            }
            beginExecutionPeriod = nextExecutionYear.getExecutionPeriodForSemester(Integer.valueOf(1));
        } else {
            beginExecutionPeriod = rootDomainObject.readExecutionPeriodByOID(beginExecutionPeriodID);
        }

        endExecutionPeriod = (endExecutionPeriodID == null) ? null : rootDomainObject
                .readExecutionPeriodByOID(endExecutionPeriodID);
    }

    public static class CreateCurricularCourseArgs {
        private Double weight;
        private String prerequisites, prerequisitesEn;
        private Integer competenceCourseID, parentCourseGroupID, year, semester, degreeCurricularPlanID,
                beginExecutionPeriodID, endExecutionPeriodID;

        public CreateCurricularCourseArgs(Double weight, String prerequisites, String prerequisitesEn,
                Integer competenceCourseID, Integer parentCourseGroupID, Integer year, Integer semester,
                Integer degreeCurricularPlanID, Integer beginExecutionPeriodID,
                Integer endExecutionPeriodID) {

            setWeight(weight);
            setPrerequisites(prerequisites);
            setPrerequisitesEn(prerequisitesEn);
            setCompetenceCourseID(competenceCourseID);
            setParentCourseGroupID(parentCourseGroupID);
            setYear(year);
            setSemester(semester);
            setDegreeCurricularPlanID(degreeCurricularPlanID);
            setBeginExecutionPeriodID(beginExecutionPeriodID);
            setEndExecutionPeriodID(endExecutionPeriodID);
        }

        public Integer getBeginExecutionPeriodID() {
            return beginExecutionPeriodID;
        }

        public void setBeginExecutionPeriodID(Integer beginExecutionPeriodID) {
            this.beginExecutionPeriodID = beginExecutionPeriodID;
        }

        public Integer getCompetenceCourseID() {
            return competenceCourseID;
        }

        public void setCompetenceCourseID(Integer competenceCourseID) {
            this.competenceCourseID = competenceCourseID;
        }

        public Integer getDegreeCurricularPlanID() {
            return degreeCurricularPlanID;
        }

        public void setDegreeCurricularPlanID(Integer degreeCurricularPlanID) {
            this.degreeCurricularPlanID = degreeCurricularPlanID;
        }

        public Integer getEndExecutionPeriodID() {
            return endExecutionPeriodID;
        }

        public void setEndExecutionPeriodID(Integer endExecutionPeriodID) {
            this.endExecutionPeriodID = endExecutionPeriodID;
        }

        public Integer getParentCourseGroupID() {
            return parentCourseGroupID;
        }

        public void setParentCourseGroupID(Integer parentCourseGroupID) {
            this.parentCourseGroupID = parentCourseGroupID;
        }

        public String getPrerequisites() {
            return prerequisites;
        }

        public void setPrerequisites(String prerequisites) {
            this.prerequisites = prerequisites;
        }

        public String getPrerequisitesEn() {
            return prerequisitesEn;
        }

        public void setPrerequisitesEn(String prerequisitesEn) {
            this.prerequisitesEn = prerequisitesEn;
        }

        public Integer getSemester() {
            return semester;
        }

        public void setSemester(Integer semester) {
            this.semester = semester;
        }

        public Double getWeight() {
            return weight;
        }

        public void setWeight(Double weight) {
            this.weight = weight;
        }

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }

    }

    public static class CreateOptionalCurricularCourseArgs {
        private Integer degreeCurricularPlanID, parentCourseGroupID, year, semester,
                beginExecutionPeriodID, endExecutionPeriodID;
        private String name, nameEn;

        public CreateOptionalCurricularCourseArgs(Integer degreeCurricularPlanID,
                Integer parentCourseGroupID, String name, String nameEn, Integer year, Integer semester,
                Integer beginExecutionPeriodID, Integer endExecutionPeriodID) {

            setDegreeCurricularPlanID(degreeCurricularPlanID);
            setParentCourseGroupID(parentCourseGroupID);
            setName(name);
            setNameEn(nameEn);
            setYear(year);
            setSemester(semester);
            setBeginExecutionPeriodID(beginExecutionPeriodID);
            setEndExecutionPeriodID(endExecutionPeriodID);
        }

        public Integer getBeginExecutionPeriodID() {
            return beginExecutionPeriodID;
        }

        public void setBeginExecutionPeriodID(Integer beginExecutionPeriodID) {
            this.beginExecutionPeriodID = beginExecutionPeriodID;
        }

        public Integer getDegreeCurricularPlanID() {
            return degreeCurricularPlanID;
        }

        public void setDegreeCurricularPlanID(Integer degreeCurricularPlanID) {
            this.degreeCurricularPlanID = degreeCurricularPlanID;
        }

        public Integer getEndExecutionPeriodID() {
            return endExecutionPeriodID;
        }

        public void setEndExecutionPeriodID(Integer endExecutionPeriodID) {
            this.endExecutionPeriodID = endExecutionPeriodID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNameEn() {
            return nameEn;
        }

        public void setNameEn(String nameEn) {
            this.nameEn = nameEn;
        }

        public Integer getParentCourseGroupID() {
            return parentCourseGroupID;
        }

        public void setParentCourseGroupID(Integer parentCourseGroupID) {
            this.parentCourseGroupID = parentCourseGroupID;
        }

        public Integer getSemester() {
            return semester;
        }

        public void setSemester(Integer semester) {
            this.semester = semester;
        }

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }
    }
}
