<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView locale="<%=request.getAttribute(org.apache.struts.Globals.LOCALE_KEY).toString()%>" definition="definition.public.department" attributeName="body-inline">
	<style>@import "<%= request.getContextPath() %>/CSS/transitional.css";</style>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	<f:loadBundle basename="resources/PublicDepartmentResources" var="publicDepartmentBundle"/>
	<f:loadBundle basename="resources/GlobalResources" var="globalBundle"/>

	<h:outputLink value="#{globalBundle['institution.url']}" >
		<h:outputText value="#{globalBundle['institution.name.abbreviation']}"/>
	</h:outputLink>
	&nbsp;&gt;&nbsp;
	<h:outputText rendered="#{empty CompetenceCourseManagement.selectedDepartmentUnit.webAddress}" value="#{CompetenceCourseManagement.selectedDepartmentUnit.department.realName}"/>
	<h:outputLink rendered="#{!empty CompetenceCourseManagement.selectedDepartmentUnit.webAddress}" value="#{CompetenceCourseManagement.selectedDepartmentUnit.webAddress}" target="_blank">
		<h:outputText value="#{CompetenceCourseManagement.selectedDepartmentUnit.department.realName}"/>
	</h:outputLink>
	&nbsp;&gt;&nbsp;
	<h:outputText value="#{publicDepartmentBundle['department.teachers']}"/>
	
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>

	<h:outputText value="<br/><h1>#{publicDepartmentBundle['department.teachers']} #{publicDepartmentBundle['from.masculine']} #{CompetenceCourseManagement.selectedDepartmentUnit.department.acronym}</h1>" escape="false"/>
	<h:outputText value="<br/>" escape="false"/>
	
	<h:form>
		<h:dataTable value="#{DepartmentManagement.departmentTeachers}"
			var="teacher" columnClasses="listClasses" headerClass="listClasses-header" style="width: 70%;">
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.teacher.name']}"></h:outputText>
				</f:facet>
				<h:outputLink rendered="#{!empty teacher.person.homepage && teacher.person.homepage.activated}" value="../../homepage/#{teacher.person.user.userUId}" target="_blank">
					<h:outputText value="#{teacher.person.nome}"/>
				</h:outputLink>
				<h:outputText rendered="#{empty teacher.person.homepage || !teacher.person.homepage.activated}" value="#{teacher.person.nome}"/>
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
