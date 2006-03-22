<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="definition.degreeAdministrativeOffice.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<i>#{bolonhaBundle['bolonhaManager']}</i>" escape="false"/>
	<h:outputFormat value="<h2>#{bolonhaBundle['delete.param']}</h2>" escape="false">
		<f:param value="#{bolonhaBundle['curricularRule']}"/>
	</h:outputFormat>
	
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>

	<h:form>
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularRulesManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input id='executionYearID' name='executionYearID' type='hidden' value='#{CurricularCourseManagement.executionYearID}'/>"/>
		<h:outputText escape="false" value="<input id='curricularRuleID' name='curricularRuleID' type='hidden' value='#{CurricularRulesManagement.curricularRuleID}'/>"/>
		<h:outputText escape="false" value="<input id='organizeBy' name='organizeBy' type='hidden' value='#{CurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input id='showRules' name='showRules' type='hidden' value='#{CurricularCourseManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input id='hideCourses' name='hideCourses' type='hidden' value='#{CurricularCourseManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input id='action' name='action' type='hidden' value='#{CurricularCourseManagement.action}'/>"/>
		
		<h:outputText value="<div class='simpleblock4'> " escape="false"/>
		<h:outputText value="<h4 class='first'>#{bolonhaBundle['curricularRule']}: </h4>" escape="false"/>
		<h:outputText value="<p>#{CurricularRulesManagement.ruleLabel}</p>" escape="false"/>
		<h:outputText value="</div>" escape="false"/>	
		
		<h:outputText value="<p>" escape="false"/>
		<h:outputText value="#{bolonhaBundle['confirmDeleteMessage']}" escape="false" styleClass="error0"/>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<p class='mtop2'>" escape="false"/>
		<h:commandButton value="#{bolonhaBundle['yes']}" styleClass="inputbutton" action="#{CurricularRulesManagement.deleteCurricularRule}"/>
		<h:commandButton immediate="true" value="#{bolonhaBundle['no']}" styleClass="inputbutton" action="setCurricularRules"/>
		<h:outputText value="</p>" escape="false"/>
	</h:form>
</ft:tilesView>
