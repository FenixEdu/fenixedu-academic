<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="df.page.structure" attributeName="body-inline">
<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
<style>
.eo_highlight { background-color: #ffc; }
</style>

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
	
		<h:outputText value="<p class='mtop2 mbottom025'><em>Legenda:</em></p>" escape="false" />
		<h:outputText value="<p class='mvert025'><div style='width: 10px; height: 10px; background-color: #606080; margin-top: 4px; float: left;'></div> <div style='padding-left: 15px;'><em>Funcionários da unidade</em></div></p>" escape="false" />
		<h:outputText value="<p class='mvert025'><div style='width: 10px; height: 10px; background-color: #808060; margin-top: 4px; float: left;'></div> <div style='padding-left: 15px;'><em>Pessoas com cargos de gestão</em></div></p>" escape="false" />
		
		
</ft:tilesView> 