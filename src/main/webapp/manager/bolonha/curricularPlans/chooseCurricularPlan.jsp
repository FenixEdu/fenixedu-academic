<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/DegreeAdministrativeOfficeResources" var="bundle"/>

	<h:form>

		<h:outputText value="#{bundle['label.choose.year.execution']}" />
		<h:selectOneMenu value="#{displayCurricularPlan.choosenExecutionYearID}">
			<f:selectItems value="#{displayCurricularPlan.executionYears}" />
		</h:selectOneMenu>
				
		<p/>
		
		<h:outputText value="#{bundle['label.choose.curricularPlan']}" />:
		<h:selectManyCheckbox value="#{displayCurricularPlan.choosenDegreeCurricularPlansIDs}" layout="pageDirection">
			<f:selectItems value="#{displayCurricularPlan.degreeCurricularPlans}" />
		</h:selectManyCheckbox>

		<p/>
			
		<h:commandButton alt="#{htmlAltBundle['commandButton.select']}" action="#{displayCurricularPlan.choose}" value="#{bundle['button.select']}" />

	</h:form>

</ft:tilesView>



