<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<bean:define id="candidateID" name="candidateID" />

<ul>
    <li><html:link page='<%= "/visualizeApplicationInfo.do?candidateID=" + candidateID %>' ><bean:message key="link.candidate.visualizeSituation" /></html:link></li>
    <li><html:link page='<%= "/changeApplicationInfoDispatchAction.do?method=prepare&candidateID=" + candidateID %>' ><bean:message key="link.candidate.changeApplicationInfo" /></html:link></li>
    <li><html:link page='<%= "/insertApplicationDocuments.do?method=prepare&candidateID=" + candidateID %>' ><bean:message key="label.masterDegree.tiles.insertApplicationDocuments"/></html:link></li>
    <li><html:link page='<%= "/showApplicationDocuments.do?method=prepare&candidateID=" + candidateID %>' ><bean:message key="label.masterDegree.tiles.showApplicationDocuments"/></html:link></li>
    <li><html:link page='<%= "/showStudyPlanForCandidate.do?candidateID=" + candidateID %>' ><bean:message key="label.masterDegree.tiles.showStudyPlanForCandidate"/></html:link></li>
</ul>