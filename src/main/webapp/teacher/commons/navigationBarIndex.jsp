<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>



<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%>
<%@page import="net.sourceforge.fenixedu.domain.person.RoleType"%><html:xhtml />

<ul>
	<logic:present name="LOGGED_USER_ATTRIBUTE" property="person.teacher">
		<logic:equal name="LOGGED_USER_ATTRIBUTE" property="person.teacher.erasmusCoordinator" value="true"> 
			<li class="navheader"><bean:message key="title.candidacies" bundle="CANDIDATE_RESOURCES" /></li>
			<li><html:link page="/caseHandlingMobilityApplicationProcess.do?method=intro"><bean:message key="link.coordinator.erasmus.application" bundle="APPLICATION_RESOURCES" /></html:link></li>
		</logic:equal>
	</logic:present>
</ul>
