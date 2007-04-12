<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="departmentMember.masterPage"
	attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/DepartmentMemberResources"
		var="bundle" />
	<f:loadBundle basename="resources/EnumerationResources"
		var="bundleEnumeration" />
		
	<h:form>
	
		<h:outputText value="<h2>#{bundle['label.teacher.list.title']}</h2>" escape="false" />
		<h:outputText value="<h3>#{viewDepartmentTeachers.department.realName}</h3>" escape="false" />

	
		<h:panelGrid columns="2" styleClass="search">
			<h:outputText value="#{bundle['label.common.executionYear']}:" styleClass="aright"/>
			<fc:selectOneMenu id="dropDownListExecutionYearID" value="#{viewDepartmentTeachers.selectedExecutionYearID}" valueChangeListener="#{viewDepartmentTeachers.onSelectedExecutionYearChanged}" onchange="this.form.submit();">
				<f:selectItems value="#{viewDepartmentTeachers.executionYears}" />
			</fc:selectOneMenu>
		</h:panelGrid>
		<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
		
	
		<h:outputText value="<br/>" escape="false" />
			
		<h:dataTable value="#{viewDepartmentTeachers.departmentTeachers}"
			var="teacher" styleClass="tstyle2 thleft" columnClasses="width5em,," style="">
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.teacher.number']}"></h:outputText>
				</f:facet>
				<h:outputText value="#{teacher.teacherNumber}" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.teacher.name']}"></h:outputText>
				</f:facet>

			<h:outputLink value="#{viewDepartmentTeachers.contextPath}/researcher/viewCurriculum.do?personOID=#{teacher.person.idInternal}" ><h:outputText value="#{teacher.person.name}" styleClass="aright"/></h:outputLink>

			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.teacher.category']}"></h:outputText>
				</f:facet>
				<h:outputText value="#{teacher.category.shortName}" />
			</h:column>
		</h:dataTable>
	</h:form>
</ft:tilesView>
