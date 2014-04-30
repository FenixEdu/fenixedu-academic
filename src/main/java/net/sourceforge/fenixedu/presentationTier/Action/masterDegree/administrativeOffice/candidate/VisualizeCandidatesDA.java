package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.candidate;

import net.sourceforge.fenixedu.presentationTier.Action.commons.ChooseExecutionYearToVisualizeCandidatesDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/visualizeCandidates", module = "masterDegreeAdministrativeOffice", input = "/chooseCandidateList_bd.jsp",
        formBean = "listCandidatesForm", functionality = ChooseExecutionYearToVisualizeCandidatesDA.class)
@Forwards({ @Forward(name = "PrepareReady", path = "/masterDegreeAdministrativeOffice/chooseCandidateList_bd.jsp"),
        @Forward(name = "ActionReady", path = "/masterDegreeAdministrativeOffice/visualizePersonCandidateList_bd.jsp"),
        @Forward(name = "VisualizeCandidate", path = "/masterDegreeAdministrativeOffice/visualizeCandidate_bd.jsp"),
        @Forward(name = "ChooseCandidate", path = "/masterDegreeAdministrativeOffice/candidateList_bd.jsp") })
public class VisualizeCandidatesDA extends EditCandidateDA {

}
