<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView locale="<%=request.getAttribute(org.apache.struts.Globals.LOCALE_KEY).toString()%>" definition="definition.public.departments" attributeName="body-inline">
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	<f:loadBundle basename="resources/PublicDepartmentResources" var="publicDepartmentBundle"/>
	<f:loadBundle basename="resources/GlobalResources" var="globalBundle"/>
	
	<h:outputText value="<div class='breadcumbs mvert0'>" escape="false"/>
		<h:outputLink value="#{globalBundle['institution.url']}">
			<h:outputText value="#{globalBundle['institution.name.abbreviation']}"/>
		</h:outputLink>
		&nbsp;&gt;&nbsp;
		<h:outputLink target="_blank" value="#{globalBundle['institution.url']}#{globalBundle['link.institution.structure']}">
			<h:outputText value="#{publicDepartmentBundle['structure']}"/>
		</h:outputLink>
		&nbsp;&gt;&nbsp;
		<h:outputText value="#{publicDepartmentBundle['academic.units']}"/>
	<h:outputText value="</div>" escape="false"/>


	<h:outputText value="<h1>#{publicDepartmentBundle['structure']}</h1>" escape="false"/>
	<h:outputText value="<h2>#{publicDepartmentBundle['academic.units']}</h2>" escape="false"/>
	
	<h:dataTable value="#{DepartmentManagement.departmentUnits}" var="departmentUnit" style="padding-left: 3em; width: 70em;" columnClasses=",aright">
		<h:column>
            <h:outputLink value="#{DepartmentManagement.contextPath}/publico/department/departmentSite.do?method=presentation&selectedDepartmentUnitID=#{departmentUnit.idInternal}">
        			<h:outputText value="#{departmentUnit.department.realName}"/>
            </h:outputLink>
		</h:column>
	</h:dataTable>
	
</ft:tilesView>
