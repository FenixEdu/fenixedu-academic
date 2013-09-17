<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@page import="org.joda.time.LocalDate"%>
<%@page import="pt.ist.fenixWebFramework.security.UserView"%><html:xhtml/>

<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl" %>

<ul>
	<li class="navheader"><bean:message  key="label.coordinator.management"/></li>
	<logic:present role="COORDINATOR">
		<li>
			<html:link page="/index.do"><bean:message  key="label.coordinator.degrees"/></html:link>
		</li>
	</logic:present>
	<li>
		<html:link href="<%= request.getContextPath() + "/academicAdministration/outboundMobilityCandidacy.do?method=prepare" %>">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound"/>
		</html:link>
	</li>
	<logic:present role="COORDINATOR">
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
	</logic:present>
</ul>
