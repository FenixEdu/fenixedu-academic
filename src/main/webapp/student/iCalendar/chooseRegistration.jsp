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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<logic:present role="role(STUDENT)">
    <h2><bean:message key="label.title.calendar" bundle="MESSAGING_RESOURCES" /></h2>

	<logic:notEmpty name="bean" property="registrations">
		<fr:form action="/ICalTimeTable.do?method=show">
			<fr:edit name="bean" id="bean" schema="registration.selection"/>

			<html:submit><bean:message key="messaging.submit.button" bundle="MESSAGING_RESOURCES" /></html:submit>
		</fr:form>
	</logic:notEmpty>
	<logic:empty  name="registrations">
		<p class="mvert15"><em><bean:message key="message.no.registration" bundle="STUDENT_RESOURCES"/></em></p>
	</logic:empty>
</logic:present>

