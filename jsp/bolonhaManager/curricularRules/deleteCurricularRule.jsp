<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<i>#{bolonhaBundle['bolonhaManager']}</i>" escape="false"/>
	<h:outputFormat value="<h2>#{bolonhaBundle['delete.param']}</h2>" escape="false">
		<f:param value="#{bolonhaBundle['curricularRule']}"/>
	</h:outputFormat>
	
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>

	<h:form>
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularRulesManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input id='curricularRuleID' name='curricularRuleID' type='hidden' value='#{CurricularRulesManagement.curricularRuleID}'/>"/>
		
		<h:outputText value="<div class='simpleblock4'> " escape="false"/>
		<h:outputText value="<h4 class='first'>#{bolonhaBundle['curricularRule']}:</h4>" escape="false"/>
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
		
		<h:outputText value="<p>#{CurricularRulesManagement.ruleLabel}</p>" escape="false"/>
		<h:outputText value="</fieldset></div>" escape="false"/>	
		
		<h:outputText value="#{bolonhaBundle['confirmDeleteMessage']}" escape="false" styleClass="error0"/>
		
		<h:outputText value="<br/><br/>" escape="false"/>
		<h:commandButton value="#{bolonhaBundle['yes']}" styleClass="inputbutton" action="#{CurricularRulesManagement.deleteCurricularRule}"/>
		<h:commandButton immediate="true" value="#{bolonhaBundle['no']}" styleClass="inputbutton" action="setCurricularRules"/>
	</h:form>
</ft:tilesView>
