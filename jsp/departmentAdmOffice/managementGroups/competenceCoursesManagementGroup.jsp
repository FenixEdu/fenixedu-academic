<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="departmentAdmOffice.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/DepartmentAdmOfficeResources" var="bundleDEP" />

	<h:panelGroup rendered="#{empty ManagementGroupsBackingBean.department}">
		<h:outputText value="<h2>#{bundleDEP['competenceCourseManagementGroups']}</h2>" escape="false" />
		<h:outputText value="<i>#{bundleDEP['error.noDepartment']}</i>" escape="false" />		
	</h:panelGroup>

	<h:panelGroup rendered="#{!empty ManagementGroupsBackingBean.department}">
		<h:outputText value="<i>#{ManagementGroupsBackingBean.department.realName}</i><br/>" escape="false" />
		<h:outputText value="<h2>#{bundleDEP['competenceCourseManagementGroups']}</h2><br/>" escape="false" />
		
		<h:outputText styleClass="error" rendered="#{!empty managerFunctionsManagementBackingBean.errorMessage}" value="#{bundleDEP[managerFunctionsManagementBackingBean.errorMessage]}" />
		
		<h:form>
			<h:panelGroup rendered="#{!empty ManagementGroupsBackingBean.selectedDepartmentEmployeesSelectItems}">
				<h:outputText value="<b>#{bundleDEP['label.group.members']}</b> #{bundleDEP['label.group.members.explanation']}<br/>" escape="false" />		
				<h:selectManyCheckbox value="#{ManagementGroupsBackingBean.selectedPersonsIDsToRemove}" layout="pageDirection">
					<f:selectItems value="#{ManagementGroupsBackingBean.selectedDepartmentEmployeesSelectItems}"  />
				</h:selectManyCheckbox>
				<h:commandLink value="#{bundleDEP['link.group.removeMembers']}" actionListener="#{ManagementGroupsBackingBean.removeMembers}" />
				<h:outputText value="<br/><br/><br/>" escape="false" />
			</h:panelGroup>
				
			<h:outputText value="<b>#{bundleDEP['add.new.members']}</b><br/>" escape="false" />		
			<h:outputText value="#{bundleDEP['number.of.persons']}: #{ManagementGroupsBackingBean.departmentEmployeesSize}<br/>" escape="false" />
			<h:outputText value="<br/>" escape="false" />
			
			<h:selectManyCheckbox value="#{ManagementGroupsBackingBean.selectedPersonsIDsToAdd}" layout="pageDirection">
				<f:selectItems value="#{ManagementGroupsBackingBean.departmentEmployeesSelectItems}"  />
			</h:selectManyCheckbox>
					
			<h:commandLink value="#{bundleDEP['link.group.addPersons']}" actionListener="#{ManagementGroupsBackingBean.addMembers}" />
		</h:form>
	</h:panelGroup>

</ft:tilesView>
