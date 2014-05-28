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
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<fp:select actionClass="net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.DepartmentAdmOfficeApp$CompetenceCoursesManagementGroup" />

<f:view>
	<f:loadBundle basename="resources/DepartmentAdmOfficeResources" var="bundleDEP" />

	<h:panelGroup rendered="#{empty ManagementGroupsBackingBean.department}">
		<h:outputText value="<h2>#{bundleDEP['competenceCourseManagementGroups']}</h2>" escape="false" />
		<h:outputText value="<i>#{bundleDEP['error.noDepartment']}</i>" escape="false" />		
	</h:panelGroup>

	<h:panelGroup rendered="#{!empty ManagementGroupsBackingBean.department}">
		<h:outputText value="<em>#{ManagementGroupsBackingBean.department.realName}</em>" escape="false" />
		<h:outputText value="<h2>#{bundleDEP['competenceCourseManagementGroups']}</h2>" escape="false" />
		
		<h:outputText styleClass="error" rendered="#{!empty managerFunctionsManagementBackingBean.errorMessage}" value="#{bundleDEP[managerFunctionsManagementBackingBean.errorMessage]}" />

		<h:outputText value="<div class='simpleblock1'>#{bundleDEP['competenceCourseManagementGroups.instructions']}</div>" escape="false" />	
				
		<h:form>
			<h:outputText value="<p><b>#{bundleDEP['label.group.members']}</b> #{bundleDEP['label.group.members.explanation']}:</p>" escape="false" />	
			<h:outputText value="<p><em>#{bundleDEP['label.group.members.empty']}</em></p>" rendered="#{empty ManagementGroupsBackingBean.selectedDepartmentEmployeesSelectItems}" escape="false" />	
			
			<h:panelGroup rendered="#{!empty ManagementGroupsBackingBean.selectedDepartmentEmployeesSelectItems}">
				<h:selectManyCheckbox value="#{ManagementGroupsBackingBean.selectedPersonsIDsToRemove}" layout="pageDirection">
					<f:selectItems value="#{ManagementGroupsBackingBean.selectedDepartmentEmployeesSelectItems}"  />
				</h:selectManyCheckbox>
				<h:commandLink value="#{bundleDEP['link.group.removeMembers']}" actionListener="#{ManagementGroupsBackingBean.removeMembers}" />
			</h:panelGroup>
			
			<h:outputText value="<br/><br/>" escape="false" />
				
			<h:outputText value="<p><b>#{bundleDEP['add.new.members']}</b>:</p>" escape="false" />		
			<h:outputText value="<p>#{bundleDEP['number.of.persons']}: #{ManagementGroupsBackingBean.departmentEmployeesSize}</p>" escape="false" />
			<h:outputText value="<p>" escape="false" />
			<h:commandLink value="#{bundleDEP['link.group.addPersons']}" actionListener="#{ManagementGroupsBackingBean.addMembers}"/>
			<h:outputText value="</p>" escape="false" />
			
			<h:selectManyCheckbox value="#{ManagementGroupsBackingBean.selectedPersonsIDsToAdd}" layout="pageDirection">
				<f:selectItems value="#{ManagementGroupsBackingBean.departmentEmployeesSelectItems}"  />
			</h:selectManyCheckbox>
			<h:outputText value="<p>" escape="false" />
			<h:commandLink value="#{bundleDEP['link.group.addPersons']}" actionListener="#{ManagementGroupsBackingBean.addMembers}" />
			<h:outputText value="</p>" escape="false" />
		</h:form>
	</h:panelGroup>

</f:view>
