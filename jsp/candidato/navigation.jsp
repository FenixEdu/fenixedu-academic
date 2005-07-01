<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<bean:define id="candidateID" name="candidateID" />

<ul>
    <li><html:link page='<%= "/visualizeApplicationInfo.do?candidateID=" + candidateID %>' ><bean:message key="link.candidate.visualizeSituation" /></html:link></li>
    <li><html:link page='<%= "/changeApplicationInfoDispatchAction.do?method=prepare&candidateID=" + candidateID %>' ><bean:message key="link.candidate.changeApplicationInfo" /></html:link></li>
    <li><html:link page="/insertApplicationDocuments.do"><bean:message key="label.masterDegree.tiles.insertApplicationDocuments"/></html:link></li>
    <li><html:link page="/showApplicationDocuments.do?method=prepare"><bean:message key="label.masterDegree.tiles.showApplicationDocuments"/></html:link></li>
    <li><html:link page="/showStudyPlanForCandidate.do"><bean:message key="label.masterDegree.tiles.showStudyPlanForCandidate"/></html:link></li>
</ul>