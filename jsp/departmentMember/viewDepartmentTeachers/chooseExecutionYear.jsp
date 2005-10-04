<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="departmentMember.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/DepartmentMemberResources" var="bundle"/>
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="bundleEnumeration"/>
	
	<h:outputText value="<h2>" escape="false" />
		<h:outputText value="#{bundle['label.common.chooseExecutionYear']}" />
	<h:outputText value="</h2>" escape="false" />
	
	<h:form>
		<h:panelGrid columns="2">
			<h:selectOneMenu id="dropDownListExecutionYearID" value="#{viewDepartmentTeachers.selectedExecutionYearID}">
				<f:selectItems value="#{viewDepartmentTeachers.executionYears}" />
			</h:selectOneMenu>
			<h:commandLink action="listDepartmentTeachers" value="#{bundle['link.continue']}" />
		</h:panelGrid>
	</h:form>

</ft:tilesView>