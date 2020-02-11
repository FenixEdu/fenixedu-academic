package org.fenixedu.academic.domain.degreeStructure;

public class CompetenceCourseExtraInformationChangeRequest extends CompetenceCourseExtraInformationChangeRequest_Base {

    public CompetenceCourseExtraInformationChangeRequest(
            CompetenceCourseInformationChangeRequest competenceCourseInformationChangeRequest, String prerequisites,
            String prerequisitesEn, String laboratorialComponent, String laboratorialComponentEn,
            String programmingAndComputingComponent, String programmingAndComputingComponentEn, String crossCompetenceComponent,
            String crossCompetenceComponentEn, String ethicalPrinciples, String ethicalPrinciplesEn) {
        super();
        setCompetenceCourseInformationChangeRequest(competenceCourseInformationChangeRequest);
        edit(prerequisites, prerequisitesEn, laboratorialComponent, laboratorialComponentEn, programmingAndComputingComponent,
                programmingAndComputingComponentEn, crossCompetenceComponent, crossCompetenceComponentEn, ethicalPrinciples,
                ethicalPrinciplesEn);
    }

    public void edit(String prerequisites, String prerequisitesEn, String laboratorialComponent, String laboratorialComponentEn,
            String programmingAndComputingComponent, String programmingAndComputingComponentEn, String crossCompetenceComponent,
            String crossCompetenceComponentEn, String ethicalPrinciples, String ethicalPrinciplesEn) {
        setPrerequisites(prerequisites);
        setPrerequisitesEn(prerequisitesEn);
        setLaboratorialComponent(laboratorialComponent);
        setLaboratorialComponentEn(laboratorialComponentEn);
        setProgrammingAndComputingComponent(programmingAndComputingComponent);
        setProgrammingAndComputingComponentEn(programmingAndComputingComponentEn);
        setCrossCompetenceComponent(crossCompetenceComponent);
        setCrossCompetenceComponentEn(crossCompetenceComponentEn);
        setEthicalPrinciples(ethicalPrinciples);
        setEthicalPrinciplesEn(ethicalPrinciplesEn);
    }

    public void delete() {
        setCompetenceCourseInformationChangeRequest(null);
        super.deleteDomainObject();
    }

}
