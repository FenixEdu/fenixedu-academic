<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page import="org.joda.time.LocalDate"%>
<%@page import="pt.ist.fenixWebFramework.security.UserView"%><html:xhtml/>

<%@page import="net.sourceforge.fenixedu.predicates.PermissionPredicates" %>
<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl" %>

<logic:present role="COORDINATOR">
	
	<ul>
		<li class="navheader"><bean:message  key="label.coordinator.management"/></li>
		<li>
			<html:link page="/index.do"><bean:message  key="label.coordinator.degrees"/></html:link>
		</li>
		
		<li class="navheader"><bean:message key="label.phds" bundle="PHD_RESOURCES"/></li>
		<li>
			<html:link page="/phdIndividualProgramProcess.do?method=manageProcesses">
				<bean:message  key="label.coordinator.phdProcesses"/>
			</html:link>
		</li>
		<li>
			<html:link page="/phdEnrolmentsManagement.do?method=showPhdProgram">
				Inscrições
			</html:link>
		</li>
		
		<%-- BIG HACK :) - temporary --%>
		<logic:equal name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" 
				property="person.istUsername" value="ist12760">

			<li class="navheader">
				<bean:message key="label.phd.ist.epfl.collaboration.type" bundle="PHD_RESOURCES" />
			</li>
			<li>
				<html:link page="/candidacies/phdProgramCandidacyProcess.do?method=listProcesses">
					<bean:message key="label.viewProcesses" bundle="PHD_RESOURCES" />
				</html:link>
			</li>
		</logic:equal>
	</ul>
</logic:present>
