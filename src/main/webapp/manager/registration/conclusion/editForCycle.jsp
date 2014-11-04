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
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt"%>

<logic:present role="role(MANAGER)">
	<h3><bean:message key="student.registrationConclusionProcess"
		bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>

	<bean:define id="cycleCurriculumGroupId" name="cycleCurriculumGroup"
		property="externalId" />
		
	<bean:define id="registrationId" name="cycleCurriculumGroup" property="studentCurricularPlan.registration.externalId" />

	<fr:edit name="cycleCurriculumGroup"
		schema="CycleCurriculumGroup.editConclusionInformation">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05" />
		</fr:layout>
		<fr:destination name="cancel"
			path="<%="/registrationConclusion.do?method=show&registrationId=" + registrationId%>" />
		<fr:destination name="success" 
			path="<%="/registrationConclusion.do?method=show&registrationId=" + registrationId%>" />
		
	</fr:edit>
</logic:present>


