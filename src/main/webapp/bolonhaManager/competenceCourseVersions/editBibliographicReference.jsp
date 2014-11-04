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

<bean:define id="competecenceCourseID" name="bean" property="competenceCourse.externalId"/>

<em><bean:message key="label.manage.versions" bundle="BOLONHA_MANAGER_RESOURCES"/></em>
<h2><bean:message key="label.competenceCourse.createVersion" bundle="BOLONHA_MANAGER_RESOURCES"/></h2>

<logic:messagesPresent message="true">
	<p>
		<html:messages id="messages" message="true" bundle="BOLONHA_MANAGER_RESOURCES">
			<span class="error0"><bean:write name="messages" /></span>
		</html:messages>
	</p>
</logic:messagesPresent>

<p class="mbottom05"><strong><bean:message key="label.editBibliographicEntry" bundle="BOLONHA_MANAGER_RESOURCES"/></strong></p>
<div class="dinline forminline">
<fr:form action="<%= "/competenceCourses/manageVersions.do?competenceCourseID=" + competecenceCourseID + "&method=editBibliographicReference" %>">
<fr:edit id="editReference" name="editBean" schema="edit.reference.from.bean">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
		<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
	</fr:layout>
</fr:edit>
	<fr:edit name="bean" id="editVersion" visible="false"/>
	<fr:edit name="beanLoad" id="editVersionLoad" visible="false"/>
	<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES"/></html:submit>
</fr:form>
<fr:form action="<%= "/competenceCourses/manageVersions.do?competenceCourseID=" + competecenceCourseID + "&method=viewBibliography"%>">
	<fr:edit name="bean" id="editVersion" visible="false"/>
	<fr:edit name="beanLoad" id="editVersionLoad" visible="false"/>
	<html:submit><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES"/></html:submit>
</fr:form>
</div>