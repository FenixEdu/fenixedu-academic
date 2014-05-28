<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<!-- alumniRegistrationResult.jsp -->

<h1>Inscrição Alumni</h1>

<div class="alumnilogo">
	
	<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
		<p><span class="error"><!-- Error messages go here --><bean:write name="message" /></span></p>
	</html:messages>
	
	<logic:equal name="registrationResult" value="true">
		<h2>
			<bean:message key="label.registration.succeeded" bundle="ALUMNI_RESOURCES" />
		</h2>
		
		<p class="greytxt">
			<bean:message key="label.registration.login" bundle="ALUMNI_RESOURCES" />
		</p>
		<ul>
			<li class="greytxt">
				<a href="<bean:message key="label.fenix.login.hostname" bundle="ALUMNI_RESOURCES" />" >
					<bean:message key="label.fenix.login.url" bundle="ALUMNI_RESOURCES" />
				</a>
			</li>
		</ul>
		<p style="margin-top: 2em;">
			<bean:message key="label.fenix.login" bundle="ALUMNI_RESOURCES" /> <strong><bean:write name="loginAlias" /></strong> 
		</p>
		<p>
			Caso nunca tenha acedido ao sistema deverá <a href="https://id.ist.utl.pt/password/recover.php" target="_blank">obter uma password</a>.
		</p>		
	</logic:equal>
	
	
	<logic:equal name="registrationResult" value="false">
		<h2 class="mtop15">
			<bean:message key="label.registration.failure" bundle="ALUMNI_RESOURCES" />
		</h2>
		<p class="greytxt">
			<bean:message key="label.registration.failure.description" bundle="ALUMNI_RESOURCES" />
		</p>
	</logic:equal>

		
	<div class="h_box_alt" style="margin-top: 3em;">
		<ul class="material">
			<li style="padding-left: 35px;">
				Participe activamente na melhoria contínua do <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>. O seu contributo é fundamental.
			</li>
		</ul>
	</div>

	
	<!-- END CONTENTS -->
</div>
