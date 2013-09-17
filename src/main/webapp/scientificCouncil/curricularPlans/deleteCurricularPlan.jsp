<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>	
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>

	<h:outputText value="<i>#{DegreeCurricularPlanManagement.dcp.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[DegreeCurricularPlanManagement.dcp.curricularStage.name]})</i>" escape="false"/>
	<h:outputFormat value="<h2>#{scouncilBundle['delete.param']}</h2>" escape="false">
		<f:param value="#{scouncilBundle['curricularPlan']}" />
	</h:outputFormat>
	<h:form>
		<h:outputText escape="false" value="<input alt='input.dcpId' id='dcpId' name='dcpId' type='hidden' value='#{DegreeCurricularPlanManagement.dcpId}'/>"/>

		<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
		
		<h:outputText value="<div class='infoop2'/>" escape="false"/>
 			<h:outputText value="<p>#{scouncilBundle['curricularStage']}: " escape="false"/>
			<h:outputText value="<b>#{enumerationBundle[DegreeCurricularPlanManagement.dcp.curricularStage.name]}</b></p>" escape="false"/>

			<h:outputText value="<p>#{scouncilBundle['name']}:" escape="false"/>
			<h:outputText value="<b>#{DegreeCurricularPlanManagement.name}</b></p>" escape="false"/>
		<h:outputText value="</div>" escape="false"/>
		
		
		<h:outputText value="<p class='mtop2'>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.confirm']}" styleClass="inputbutton" value="#{scouncilBundle['confirm']}"
			action="#{DegreeCurricularPlanManagement.deleteCurricularPlan}" onclick="return confirm('#{scouncilBundle['confirm.delete.curricularPlan']}')"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" styleClass="inputbutton" value="#{scouncilBundle['cancel']}"
			action="curricularPlansManagement"/>
		<h:outputText value="</p>" escape="false"/>
	</h:form>

</ft:tilesView>