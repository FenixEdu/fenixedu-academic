<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<i>#{bolonhaBundle['bolonhaManager']}</i>" escape="false"/>
	<h:outputFormat value="<h2>#{bolonhaBundle['delete.param']}</h2>" escape="false">
		<f:param value="#{bolonhaBundle['curricularRule']}"/>
	</h:outputFormat>
	
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>

	<h:form>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularRulesManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.curricularRuleID' id='curricularRuleID' name='curricularRuleID' type='hidden' value='#{CurricularRulesManagement.curricularRuleID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{CurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{CurricularCourseManagement.hideCourses}'/>"/>		
		
		<h:outputText value="<div class='simpleblock4'> " escape="false"/>
		<h:outputText value="<h4 class='first'>#{bolonhaBundle['curricularRule']}: </h4>" escape="false"/>
		<h:outputText value="<p>#{CurricularRulesManagement.ruleLabel}</p>" escape="false"/>
		<h:outputText value="</div>" escape="false"/>	
		
		<h:outputText value="<p>" escape="false"/>
		<h:outputText value="#{bolonhaBundle['confirmDeleteMessage']}" escape="false" styleClass="error0"/>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<p class='mtop2'>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.yes']}" value="#{bolonhaBundle['yes']}" styleClass="inputbutton" action="#{CurricularRulesManagement.deleteCurricularRule}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.no']}" immediate="true" value="#{bolonhaBundle['no']}" styleClass="inputbutton" action="setCurricularRules"/>
		<h:outputText value="</p>" escape="false"/>
	</h:form>
</ft:tilesView>
