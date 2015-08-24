<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<h2 class="mbottom1"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="document.print" /></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p class="mtop1">
		<span class="warning0">
			<bean:write name="message" />
		</span>
	</p>
</html:messages>

<html:link action="/documentRequest.do?method=chooseRegistration">
	<bean:message key="link.student.back" bundle="STUDENT_RESOURCES" />
</html:link>

<bean:define id="simpleClassName" name="documentRequest" property="class.simpleName" />
<fr:view name="documentRequest" schema="<%= simpleClassName  + ".view"%>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight thright" />
		<fr:property name="rowClasses" value=",,,,,,"/>
	</fr:layout>
</fr:view>

<logic:present name="documentRequest" property="activeSituation">
	<p class="mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="request.situation"/></strong></p>
	<fr:view name="documentRequest" property="activeSituation" schema="AcademicServiceRequestSituation.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thright thlight mtop025"/>
		<fr:property name="rowClasses" value=",,tdhl1,"/>
		</fr:layout>
	</fr:view>
</logic:present>

<bean:define id="documentRequest" name="documentRequest" type="org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequest"/>

<logic:equal name="documentRequest" property="toPrint" value="true">
<p>
	<fr:form action="<%= "/documentRequest.do?method=printDocument&amp;documentRequestId=" + documentRequest.getExternalId().toString() %>">
		<html:submit><bean:message key="print" bundle="APPLICATION_RESOURCES"/></html:submit>
	</fr:form>
</p>
</logic:equal>
