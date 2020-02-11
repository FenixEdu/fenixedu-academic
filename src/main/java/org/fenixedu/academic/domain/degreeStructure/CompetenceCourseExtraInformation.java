package org.fenixedu.academic.domain.degreeStructure;

public class CompetenceCourseExtraInformation extends CompetenceCourseExtraInformation_Base {

    public CompetenceCourseExtraInformation(CompetenceCourseInformation competenceCourseInformation, String prerequisites,
            String prerequisitesEn, String laboratorialComponent, String laboratorialComponentEn,
            String programmingAndComputingComponent, String programmingAndComputingComponentEn, String crossCompetenceComponent,
            String crossCompetenceComponentEn, String ethicalPrinciples, String ethicalPrinciplesEn) {
        super();
        setCompetenceCourseInformation(competenceCourseInformation);
        edit(prerequisites, prerequisitesEn, laboratorialComponent, laboratorialComponentEn, programmingAndComputingComponent,
                programmingAndComputingComponentEn, crossCompetenceComponent, crossCompetenceComponentEn, ethicalPrinciples,
                ethicalPrinciplesEn);
    }

    public void edit(String prerequisites, String prerequisitesEn, String laboratorialComponent, String laboratorialComponentEn,
            String programmingAndComputingComponent, String programmingAndComputingComponentEn, String crossCompetenceComponent,
            String crossCompetenceComponentEn, String ethicalPrinciples, String ethicalPrinciplesEn) {
        setPrerequisites(prerequisites);
        setLaboratorialComponent(laboratorialComponent);
        setProgrammingAndComputingComponent(programmingAndComputingComponent);
        setCrossCompetenceComponent(crossCompetenceComponent);
        setEthicalPrinciples(ethicalPrinciples);
        setPrerequisitesEn(prerequisitesEn);
        setLaboratorialComponentEn(laboratorialComponentEn);
        setProgrammingAndComputingComponentEn(programmingAndComputingComponentEn);
        setCrossCompetenceComponentEn(crossCompetenceComponentEn);
        setEthicalPrinciplesEn(ethicalPrinciplesEn);
    }

    public void delete() {
        setCompetenceCourseInformation(null);
        super.deleteDomainObject();
    }
}
