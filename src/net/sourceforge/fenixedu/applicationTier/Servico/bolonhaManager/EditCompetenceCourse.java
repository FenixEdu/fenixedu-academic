/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ICompetenceCourse;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditCompetenceCourse implements IService {

    public void run(EditCompetenceCourse.CompetenceCourseInformation courseInformation)
            throws ExcepcaoPersistencia, FenixServiceException {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        ICompetenceCourse competenceCourse = (ICompetenceCourse) persistentSupport
                .getIPersistentCompetenceCourse().readByOID(CompetenceCourse.class,
                        courseInformation.getCompetenceCourseID());
        if (competenceCourse == null) {
            throw new FenixServiceException("error.noCompetenceCourse");
        }
        competenceCourse.edit(courseInformation.getName(), "", courseInformation.getEctsCredits(),
                courseInformation.getBasic(), courseInformation.getTheoreticalHours(), courseInformation
                        .getProblemsHours(), courseInformation.getLabHours(), courseInformation
                        .getProjectHours(), courseInformation.getSeminaryHours(), courseInformation
                        .getRegime(), courseInformation.getStage());
        competenceCourse.edit(courseInformation.getProgram(), courseInformation.getGeneralObjectives(),
                courseInformation.getOperationalObjectives(), courseInformation.getEvaluationMethod(),
                courseInformation.getPrerequisites(), courseInformation.getNameEn(), courseInformation
                        .getProgramEn(), courseInformation.getGeneralObjectivesEn(), courseInformation
                        .getOperationalObjectivesEn(), courseInformation.getEvaluationMethodEn(),
                courseInformation.getPrerequisitesEn());
    }

    public static class CompetenceCourseInformation {
        private Integer competenceCourseID;
        private String name;
        private Double ectsCredits;
        private Boolean basic;
        private Double theoreticalHours;
        private Double problemsHours;
        private Double labHours;
        private Double projectHours;
        private Double seminaryHours;
        private RegimeType regime;
        private CurricularStage stage;
        private String program;
        private String generalObjectives;
        private String operationalObjectives;
        private String evaluationMethod;
        private String prerequisites;
        private String nameEn;
        private String programEn;
        private String generalObjectivesEn;
        private String operationalObjectivesEn;
        private String evaluationMethodEn;
        private String prerequisitesEn;

        public CompetenceCourseInformation(Integer competenceCourseID, String name, Double ectsCredits, Boolean basic,
                Double theoreticalHours, Double problemsHours, Double labHours, Double projectHours,
                Double seminaryHours, RegimeType regime, CurricularStage curricularStage,
                String program, String generalObjectives, String operationalObjectives,
                String evaluationMethod, String prerequisites, String nameEn, String programEn,
                String generalObjectivesEn, String operationalObjectivesEn, String evaluationMethodEn,
                String prerequisitesEn) {

            setCompetenceCourseID(competenceCourseID);
            setName(name);
            setEctsCredits(ectsCredits);
            setBasic(basic);
            setTheoreticalHours(theoreticalHours);
            setProblemsHours(problemsHours);
            setLabHours(labHours);
            setProjectHours(projectHours);
            setSeminaryHours(seminaryHours);
            setRegime(regime);
            setStage(curricularStage);
            setProgram(program);
            setGeneralObjectives(generalObjectives);
            setOperationalObjectives(operationalObjectives);
            setEvaluationMethod(evaluationMethod);
            setPrerequisites(prerequisites);
            setNameEn(nameEn);
            setProgramEn(programEn);
            setGeneralObjectivesEn(generalObjectivesEn);
            setOperationalObjectivesEn(operationalObjectivesEn);
            setEvaluationMethodEn(evaluationMethodEn);
            setPrerequisitesEn(prerequisitesEn);
        }

        public Boolean getBasic() {
            return basic;
        }

        public void setBasic(Boolean basic) {
            this.basic = basic;
        }

        public Integer getCompetenceCourseID() {
            return competenceCourseID;
        }

        public void setCompetenceCourseID(Integer competenceCourseID) {
            this.competenceCourseID = competenceCourseID;
        }

        public Double getEctsCredits() {
            return ectsCredits;
        }

        public void setEctsCredits(Double ectsCredits) {
            this.ectsCredits = ectsCredits;
        }

        public String getEvaluationMethod() {
            return evaluationMethod;
        }

        public void setEvaluationMethod(String evaluationMethod) {
            this.evaluationMethod = evaluationMethod;
        }

        public String getEvaluationMethodEn() {
            return evaluationMethodEn;
        }

        public void setEvaluationMethodEn(String evaluationMethodEn) {
            this.evaluationMethodEn = evaluationMethodEn;
        }

        public String getGeneralObjectives() {
            return generalObjectives;
        }

        public void setGeneralObjectives(String generalObjectives) {
            this.generalObjectives = generalObjectives;
        }

        public String getGeneralObjectivesEn() {
            return generalObjectivesEn;
        }

        public void setGeneralObjectivesEn(String generalObjectivesEn) {
            this.generalObjectivesEn = generalObjectivesEn;
        }

        public Double getLabHours() {
            return labHours;
        }

        public void setLabHours(Double labHours) {
            this.labHours = labHours;
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

        public String getOperationalObjectives() {
            return operationalObjectives;
        }

        public void setOperationalObjectives(String operationalObjectives) {
            this.operationalObjectives = operationalObjectives;
        }

        public String getOperationalObjectivesEn() {
            return operationalObjectivesEn;
        }

        public void setOperationalObjectivesEn(String operationalObjectivesEn) {
            this.operationalObjectivesEn = operationalObjectivesEn;
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

        public Double getProblemsHours() {
            return problemsHours;
        }

        public void setProblemsHours(Double problemsHours) {
            this.problemsHours = problemsHours;
        }

        public String getProgram() {
            return program;
        }

        public void setProgram(String program) {
            this.program = program;
        }

        public String getProgramEn() {
            return programEn;
        }

        public void setProgramEn(String programEn) {
            this.programEn = programEn;
        }

        public Double getProjectHours() {
            return projectHours;
        }

        public void setProjectHours(Double projectHours) {
            this.projectHours = projectHours;
        }

        public RegimeType getRegime() {
            return regime;
        }

        public void setRegime(RegimeType regime) {
            this.regime = regime;
        }

        public Double getSeminaryHours() {
            return seminaryHours;
        }

        public void setSeminaryHours(Double seminaryHours) {
            this.seminaryHours = seminaryHours;
        }

        public CurricularStage getStage() {
            return stage;
        }

        public void setStage(CurricularStage stage) {
            this.stage = stage;
        }

        public Double getTheoreticalHours() {
            return theoreticalHours;
        }

        public void setTheoreticalHours(Double theoreticalHours) {
            this.theoreticalHours = theoreticalHours;
        }
    }
}
