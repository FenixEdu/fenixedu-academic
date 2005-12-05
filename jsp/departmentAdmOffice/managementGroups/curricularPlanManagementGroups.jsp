<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="departmentAdmOffice.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/DepartmentAdmOfficeResources" var="bundleDEP"/>
	
	<h:outputText value="<h2>#{bundleDEP['curricularPlanManagementGroups']}</h2>" escape="false"/>
	<h:outputText value="<br/>" escape="false" />
	
	
</ft:tilesView>
