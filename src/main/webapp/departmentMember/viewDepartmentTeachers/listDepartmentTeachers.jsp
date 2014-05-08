<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<fp:select actionClass="net.sourceforge.fenixedu.presentationTier.Action.departmentMember.DepartmentMemberApp$ListDepartmentTeachers" />

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
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
	
		<h:outputText value="<br/>" escape="false" />

		<h:dataTable value="#{viewDepartmentTeachers.departmentTeachers}"
			var="teacher" styleClass="table">
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.teacher.id']}"></h:outputText>
				</f:facet>
				<h:outputText value="#{teacher.teacherId}" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.teacher.name']}"></h:outputText>
				</f:facet>
                <h:outputText value="#{teacher.person.name}" styleClass="aright" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.teacher.category']}"></h:outputText>
				</f:facet>
				<h:outputText value="#{teacher.category.name}" />
			</h:column>
		</h:dataTable>
	</h:form>
</f:view>
