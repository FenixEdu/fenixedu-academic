<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="df.page.structure" attributeName="body-inline">
<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/DepartmentAdmOfficeResources" var="bundle"/>
	
	 <h:form>
		<h:inputHidden value="#{organizationalStructure.unitID}" />		
		<h:inputHidden value="#{organizationalStructure.subUnit}" />		
		<h:inputHidden value="#{organizationalStructure.choosenExecutionYearID}" />
		<h:panelGroup >	
			<h:outputText value="#{organizationalStructure.title}" escape="false"/>	
		</h:panelGroup>
		<h:outputText value="#{bundleDegreeAdministrativeOffice['label.choose.year.execution']}" />
		<fc:selectOneMenu value="#{organizationalStructure.choosenExecutionYearID}" onchange="this.form.submit();">
			<f:selectItems value="#{organizationalStructure.executionYears}" />
		</fc:selectOneMenu>
		<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
		<h:panelGroup >	
			<h:outputText value="#{organizationalStructure.functions}" escape="false"/>		
		</h:panelGroup>
	</h:form>

</ft:tilesView> 