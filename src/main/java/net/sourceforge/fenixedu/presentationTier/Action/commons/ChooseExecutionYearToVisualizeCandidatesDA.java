package net.sourceforge.fenixedu.presentationTier.Action.commons;

import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.MasterDegreeOfficeApplication.MasterDegreeCandidatesApp;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = MasterDegreeCandidatesApp.class, path = "visualize-candidates",
        titleKey = "link.masterDegree.administrativeOffice.visualizeCandidateInformations")
@Mapping(path = "/chooseExecutionYearToVisualizeCandidates", module = "masterDegreeAdministrativeOffice",
        input = "/chooseExecutionYear_bd.jsp", formBean = "chooseExecutionYearForm")
@Forwards(@Forward(name = "ChooseSuccess",
        path = "/masterDegreeAdministrativeOffice/visualizeCandidates.do?method=prepareChoose&action=visualize"))
public class ChooseExecutionYearToVisualizeCandidatesDA extends ChooseExecutionYearDispatchAction {

}
