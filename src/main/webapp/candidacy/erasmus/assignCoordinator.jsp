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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.edit" bundle="APPLICATION_RESOURCES"/></h2>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="processName" name="processName" />

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
</html:messages>


<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=prepareExecuteViewErasmusCoordinators&amp;processId=" + processId.toString() %>'>
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
</html:link>
<br/>

<p><bean:message key="title.erasmus.erasmusCoordinators.assign" bundle="ACADEMIC_OFFICE_RESOURCES" /></p>

<fr:form action='<%= "/caseHandling" + processName.toString() + ".do?method=searchTeacherByNumber&amp;processId=" + processId.toString() %>'>
	<fr:edit id="erasmus.coordinator.bean" name="erasmusCoordinatorBean" visible="false" />
	  
	<fr:edit id="erasmus.coordinator.bean.search" name="erasmusCoordinatorBean">
		<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusCoordinatorBean">
			<fr:slot name="teacherId" key="label.erasmus.coordinator.teacher.id" >
			</fr:slot>
		</fr:schema>
		<fr:destination name="invalid" path='<%= "/caseHandling" + processName.toString() + ".do?method=prepareExecuteViewErasmusCoordinators&amp;processId=" + processId.toString() %>'/>
		
	</fr:edit>
	
	<html:submit><bean:message key="label.erasmus.coordinator.teacher.search" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:submit>
</fr:form>

<logic:notEmpty name="erasmusCoordinatorBean" property="teacher">
	<p><bean:message key="label.erasmus.coordinator.teacher.found" bundle="ACADEMIC_OFFICE_RESOURCES" /></p>
	<fr:view name="erasmusCoordinatorBean" property="teacher">
		<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="net.sourceforge.fenixedu.domain.Teacher">
			<fr:slot name="teacherId" key="label.erasmus.coordinator.teacher.number" />
			<fr:slot name="person.name" key="label.erasmus.coordinator.teacher.name" />
		</fr:schema>
		<fr:layout name="tabular">
		</fr:layout>
	</fr:view>
	
	<p><bean:message key="message.erasmus.coordinator.choose.degree" bundle="ACADEMIC_OFFICE_RESOURCES" /></p>
	<fr:form action='<%= "/caseHandling" + processName.toString() + ".do?method=executeAssignCoordinator&amp;processId=" + processId.toString() %>'>
 		<fr:edit id="erasmus.coordinator.bean" name="erasmusCoordinatorBean" visible="false" />
 		
		<fr:edit id="erasmus.coordinator.bean.assign" name="erasmusCoordinatorBean">
			<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusCoordinatorBean">
				<fr:slot name="degree" layout="menu-select" key="label.erasmus.degree" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus.ErasmusCandidacyProcessDA$ErasmusCandidacyDegreesProvider" />
			        <fr:property name="format" value="${degreeType.localizedName} - ${nameI18N}" />
			        <fr:property name="sortBy" value="degreeType.localizedName=asc, nameI18N=asc"/>
				</fr:slot>
			</fr:schema>
			
			<fr:destination name="invalid" path='<%= "/caseHandling" + processName.toString() + ".do?method=executeAssignCoordinatorInvalid&amp;processId=" + processId.toString() %>'/>
		</fr:edit> 		
 		 		
 		<html:submit> 
 			<bean:message key="label.erasmus.coordinator.assign.teacher" bundle="ACADEMIC_OFFICE_RESOURCES" />
 		</html:submit>
	</fr:form>
	
</logic:notEmpty>
