<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<i>#{bolonhaBundle['bolonhaManager']}</i>" escape="false"/>
	<h:outputFormat value="<h2>#{bolonhaBundle['edit.param']}</h2>" escape="false">
		<f:param value="#{bolonhaBundle['curricularRule']}"/>
	</h:outputFormat>
	
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>

	<h:form>
		<fc:viewState binding="#{CurricularRulesManagement.viewState}"/>
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularRulesManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input id='curricularRuleID' name='curricularRuleID' type='hidden' value='#{CurricularRulesManagement.curricularRuleID}'/>"/>
		
		<h:outputText value="<strong>#{bolonhaBundle['degreeModule.to.apply.rule']}: </strong>" escape="false"/>
		<h:outputText value="#{CurricularRulesManagement.degreeModule.name}<br/>" escape="false"/>
		
		<h:outputText value="<strong>#{bolonhaBundle['existent.curricularRules']}: </strong><br/>" escape="false"/>
		<h:panelGroup rendered="#{empty CurricularRulesManagement.degreeModule.curricularRules}">
			<h:outputText value="<em>#{bolonhaBundle['no.existent.curricularRules']}</em><br/><br/>" escape="false"/>
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty CurricularRulesManagement.degreeModule.curricularRules}">
			<h:outputText value="<ul>" escape="false"/>
			<fc:dataRepeater value="#{CurricularRulesManagement.degreeModule.curricularRules}" var="curricularRule">
				<h:outputText value="<li>#{curricularRule.label}</li>" escape="false"/>
			</fc:dataRepeater>
			<h:outputText value="</ul>" escape="false"/>
		</h:panelGroup>
		
		<h:outputText value="<div class='simpleblock4'> " escape="false"/>
		<h:outputText value="<h4 class='first'>#{bolonhaBundle['new.rule']}:</h4>" escape="false"/>
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>

		<h:outputText value="<p><label>#{bolonhaBundle['type.of.rule']}:</label>" escape="false"/>
		<fc:selectOneMenu value="#{CurricularRulesManagement.selectedCurricularRuleType}" onchange="this.form.submit();"
				valueChangeListener="#{CurricularRulesManagement.onChangeCurricularRuleTypeDropDown}">
			<f:selectItems binding="#{CurricularRulesManagement.curricularRuleTypeItems}"/>
		</fc:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>
		
		<h:panelGroup rendered="#{CurricularRulesManagement.selectedCurricularRuleType == 'PRECEDENCY_APPROVED_DEGREE_MODULE' || CurricularRulesManagement.selectedCurricularRuleType == 'PRECEDENCY_ENROLED_DEGREE_MODULE'}">
			<h:outputText value="<p><label>#{bolonhaBundle['curricularCourse']}:</label>" escape="false"/>
			<fc:selectOneMenu value="#{CurricularRulesManagement.selectedPrecendenceDegreeModuleID}">
				<f:selectItems binding="#{CurricularRulesManagement.degreeModuleItems}"/>
			</fc:selectOneMenu>
			<h:outputText value="</p>" escape="false"/> 
		</h:panelGroup>				

		<h:panelGroup rendered="#{CurricularRulesManagement.selectedCurricularRuleType == 'DEGREE_MODULES_SELECTION_LIMIT'}">
			<h:outputText value="<p><label>#{bolonhaBundle['credits']}:</label>" escape="false"/>
			
			<h:outputText value="#{bolonhaBundle['minimum']}: " escape="false"/>
			<h:inputText id="minimumLimit" maxlength="8" size="4" value="#{CurricularRulesManagement.minimumLimit}"/>
	
			<h:outputText value="#{bolonhaBundle['maximum']}: " escape="false"/>
			<h:inputText id="maximumLimit" maxlength="8" size="4" value="#{CurricularRulesManagement.maximumLimit}"/>
			
			<h:outputText value="</p>" escape="false"/>
		</h:panelGroup>
		
		<h:panelGroup rendered="#{CurricularRulesManagement.selectedCurricularRuleType == 'CREDITS_LIMIT'}">
			<h:outputText value="<p><label>#{bolonhaBundle['options']}:</label>" escape="false"/>
			
			<h:outputText value="#{bolonhaBundle['minimum']}: " escape="false"/>
			<h:inputText id="minimumCredits" maxlength="8" size="4" value="#{CurricularRulesManagement.minimumCredits}"/>
	
			<h:outputText value="#{bolonhaBundle['maximum']}: " escape="false"/>
			<h:inputText id="maximumCredits" maxlength="8" size="4" value="#{CurricularRulesManagement.maximumCredits}"/>
	
			<h:outputText value="</p>" escape="false"/>
		</h:panelGroup>
		
		<h:outputText value="<p><label>#{bolonhaBundle['apply.in']} #{bolonhaBundle['group']}:</label>" escape="false"/>
		<fc:selectOneMenu value="#{CurricularRulesManagement.selectedContextCourseGroupID}">
			<f:selectItems binding="#{CurricularRulesManagement.courseGroupItems}"/>
		</fc:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>
		
		<h:panelGroup rendered="#{CurricularRulesManagement.selectedCurricularRuleType == 'PRECEDENCY_APPROVED_DEGREE_MODULE' || CurricularRulesManagement.selectedCurricularRuleType == 'PRECEDENCY_ENROLED_DEGREE_MODULE'}">
			<h:outputText value="<p><label>#{bolonhaBundle['apply.in']} #{bolonhaBundle['semester']}:</label>" escape="false"/>
			<fc:selectOneMenu value="#{CurricularRulesManagement.selectedSemester}">
				<f:selectItem itemLabel="#{bolonhaBundle['both']}" itemValue="0"/>
				<f:selectItem itemLabel="1" itemValue="1"/>
				<f:selectItem itemLabel="2" itemValue="2"/>
			</fc:selectOneMenu>
			<h:outputText value="</p>" escape="false"/>
		</h:panelGroup>
		
		<h:outputText value="</fieldset></div>" escape="false"/>	

		<h:commandButton value="#{bolonhaBundle['submit']}" styleClass="inputbutton" action="#{CurricularRulesManagement.createCurricularRule}"/>
		<h:commandButton immediate="true" value="#{bolonhaBundle['back']}" styleClass="inputbutton" action="setCurricularRules"/>
	</h:form>
</ft:tilesView>
