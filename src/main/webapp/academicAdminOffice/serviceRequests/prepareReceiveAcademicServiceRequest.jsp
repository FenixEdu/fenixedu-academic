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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2 class="mbottom1"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="receive.request" /></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here -->
			<bean:write name="message" />
		</span>
	</p>
</html:messages>

<bean:define id="academicServiceRequest" name="academicServiceRequest" scope="request" type="net.sourceforge.fenixedu.domain.serviceRequests.RegistrationAcademicServiceRequest"/>
<p class="mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="request.information"/></strong></p>
<fr:view name="academicServiceRequest" schema="AcademicServiceRequest.view-short">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop025 mbottom05"/>
	</fr:layout>
</fr:view>

<logic:present name="academicServiceRequest" property="activeSituation">
	<p class="mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="request.situation"/></strong></p>
	<bean:define id="schema" name="academicServiceRequest" property="activeSituation.class.simpleName" />
	<fr:view name="academicServiceRequest" property="activeSituation" schema="<%= schema.toString() + ".view" %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop025 mbottom05"/>
			<fr:property name="rowClasses" value="tdhl1,,,"/>
		</fr:layout>
	</fr:view>
</logic:present>

<p class="mbottom025"><strong><bean:message key="label.information" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>
<html:form action="<%="/academicServiceRequestsManagement.do?method=receiveAcademicServiceRequest&amp;academicServiceRequestId=" + academicServiceRequest.getExternalId().toString()%>" style="display: inline;">
	<fr:edit id="serviceRequestBean" name="serviceRequestBean" schema="AcademicServiceRequestBean.external.entity.edit">
		<fr:layout>
			<fr:property name="classes" value="tstyle4 thright thlight mtop025 mbottom05"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	<span>
		<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES"/></html:submit>		
	</span>
</html:form>

<html:form action="<%="/student.do?method=visualizeRegistration&amp;registrationID=" + academicServiceRequest.getRegistration().getExternalId().toString()%>" style="display: inline;">
	<span>
		<html:submit><bean:message key="label.back" bundle="APPLICATION_RESOURCES"/></html:submit>
	</span>
</html:form>
