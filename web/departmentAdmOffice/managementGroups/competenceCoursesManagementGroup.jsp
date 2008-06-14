<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="departmentAdmOffice.masterPage" attributeName="body-inline">
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

</ft:tilesView>
