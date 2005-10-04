<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="departmentMember.masterPage"
	attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/DepartmentMemberResources"
		var="bundle" />
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources"
		var="bundleEnumeration" />

	<h:outputText value="<h2>" escape="false" />
		<h:outputText value="#{bundle['label.teacher.list.title']}" escape="false" />
	<h:outputText value="</h2>" escape="false" />

	<h:form>
		<h:inputHidden value="#{viewDepartmentTeachers.selectedExecutionYearID}" />
	
		<h:panelGrid columns="1">
			<h:dataTable value="#{viewDepartmentTeachers.departmentTeachers}"
				var="teacher" columnClasses="listClasses" headerClass="listClasses-header" >
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
					<h:outputText value="#{teacher.infoPerson.nome}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.teacher.category']}"></h:outputText>
					</f:facet>
					<h:outputText value="#{teacher.infoCategory.shortName}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle['label.teacher.details']}"></h:outputText>
					</f:facet>
					<h:commandLink value="#{bundle['link.teacher.viewDetails']}"
						action="viewDetails"
						actionListener="#{viewDepartmentTeachers.selectTeacher}">
						<f:param id="teacherId" name="teacherID"
							value="#{teacher.idInternal}"></f:param>
					</h:commandLink>
				</h:column>
			</h:dataTable>
		</h:panelGrid>
	</h:form>

</ft:tilesView>
