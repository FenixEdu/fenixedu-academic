<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<fp:select actionClass="org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication$CurricularPlansManagement" />

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputFormat value="<h2>#{bolonhaBundle['set.param']}</h2>" escape="false">
		<f:param value="#{bolonhaBundle['curricularRule']}"/>
	</h:outputFormat>
	
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>

	<h:form>
		<fc:viewState binding="#{CurricularRulesManagement.viewState}"/>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularRulesManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.executionYearID' id='executionYearID' name='executionYearID' type='hidden' value='#{CurricularRulesManagement.executionYearID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.degreeModuleID' id='degreeModuleID' name='degreeModuleID' type='hidden' value='#{CurricularRulesManagement.degreeModuleID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{CurricularRulesManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.showRules' id='showRules' name='showRules' type='hidden' value='#{CurricularRulesManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{CurricularRulesManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{CurricularRulesManagement.action}'/>"/>
		
		<h:outputText value="<p><strong>#{bolonhaBundle['degreeModule.to.apply.rule']}:</strong> " escape="false"/>
		<h:outputText value="<span class='attention'>#{CurricularRulesManagement.degreeModule.name}</span><p/>" escape="false"/>

		<h:panelGroup rendered="#{!empty CurricularRulesManagement.degreeModule.curricularRules}">
		<h:outputText value="<p><strong>#{bolonhaBundle['existent.curricularRules']}: </strong><br/>" escape="false"/>
			<h:outputText value="<ul>" escape="false"/>
			<fc:dataRepeater value="#{CurricularRulesManagement.rulesLabels}" var="curricularRule">
				<h:outputText value="<li>#{curricularRule}</li>" escape="false"/>
			</fc:dataRepeater>
			<h:outputText value="</ul>" escape="false"/>
		</h:panelGroup>
		
		<%-- new rule --%>
		<h:panelGroup rendered="#{CurricularRulesManagement.type == 'rule'}">
			<h:outputText escape="false" value="<input alt='input.type' id='type' name='type' type='hidden' value='#{CurricularRulesManagement.type}'/>"/>

			<h:outputText value="<div class='simpleblock4'> " escape="false"/>
			<h:outputText value="<h4 class='first'>#{bolonhaBundle['new.rule']}</h4>" escape="false"/>
			<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
	
			<h:outputText value="<p><label>#{bolonhaBundle['type.of.rule']}:</label>" escape="false"/>
			<fc:selectOneMenu value="#{CurricularRulesManagement.selectedCurricularRuleType}" onchange="this.form.submit();"
					valueChangeListener="#{CurricularRulesManagement.onChangeCurricularRuleTypeDropDown}">
				<f:selectItems binding="#{CurricularRulesManagement.curricularRuleTypeItems}"/>
			</fc:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:panelGroup rendered="#{CurricularRulesManagement.selectedCurricularRuleType == 'PRECEDENCY_APPROVED_DEGREE_MODULE' 
						|| CurricularRulesManagement.selectedCurricularRuleType == 'PRECEDENCY_ENROLED_DEGREE_MODULE' 
						|| CurricularRulesManagement.selectedCurricularRuleType == 'RESTRICTION_NOT_ENROLED_DEGREE_MODULE'
						|| CurricularRulesManagement.selectedCurricularRuleType == 'PRECEDENCY_BETWEEN_DEGREE_MODULES' 
						|| CurricularRulesManagement.selectedCurricularRuleType == 'EXCLUSIVENESS'}">
				<h:outputText value="<p><label>#{bolonhaBundle['curricularCourse']}:</label>" escape="false"/>
				<fc:selectOneMenu value="#{CurricularRulesManagement.selectedDegreeModuleID}">
					<f:selectItems binding="#{CurricularRulesManagement.degreeModuleItems}"/>
				</fc:selectOneMenu>
				<h:outputText value="</p>" escape="false"/> 
			</h:panelGroup>				
	
			<h:panelGroup rendered="#{CurricularRulesManagement.selectedCurricularRuleType == 'DEGREE_MODULES_SELECTION_LIMIT'}">
				<h:outputText value="<p><label>#{bolonhaBundle['options']}:</label>" escape="false"/>
				<h:outputText value="#{bolonhaBundle['minimum']}: " escape="false"/>
				<h:inputText alt="#{htmlAltBundle['inputText.minimumLimit']}" id="minimumLimit" maxlength="8" size="4" value="#{CurricularRulesManagement.minimumLimit}"/>
				<h:outputText value=" " escape="false"/>
				<h:outputText value="#{bolonhaBundle['maximum']}: " escape="false"/>
				<h:inputText alt="#{htmlAltBundle['inputText.maximumLimit']}" id="maximumLimit" maxlength="8" size="4" value="#{CurricularRulesManagement.maximumLimit}"/>
				<h:outputText value="</p>" escape="false"/>
			</h:panelGroup>
			
			<h:panelGroup rendered="#{CurricularRulesManagement.selectedCurricularRuleType == 'CREDITS_LIMIT'
										|| CurricularRulesManagement.selectedCurricularRuleType == 'ANY_CURRICULAR_COURSE'}">
				<h:outputText value="<p><label>#{bolonhaBundle['credits']}:</label>" escape="false"/>
				<h:outputText value="#{bolonhaBundle['minimum']}: " escape="false"/>
				<h:inputText alt="#{htmlAltBundle['inputText.minimumCredits']}" id="minimumCredits" maxlength="8" size="4" value="#{CurricularRulesManagement.minimumCredits}"/>
				<h:outputText value=" " escape="false"/>
				<h:outputText value="#{bolonhaBundle['maximum']}: " escape="false"/>
				<h:inputText alt="#{htmlAltBundle['inputText.maximumCredits']}" id="maximumCredits" maxlength="8" size="4" value="#{CurricularRulesManagement.maximumCredits}"/>
				<h:outputText value="</p>" escape="false"/>
			</h:panelGroup>
			
			<h:panelGroup rendered="#{CurricularRulesManagement.selectedCurricularRuleType == 'PRECEDENCY_BETWEEN_DEGREE_MODULES'
						|| CurricularRulesManagement.selectedCurricularRuleType == 'MINIMUM_NUMBER_OF_CREDITS_TO_ENROL'}">
				<h:outputText value="<p><label>#{bolonhaBundle['credits']}:</label>" escape="false"/>			
				<h:inputText alt="#{htmlAltBundle['inputText.minimumCredits']}" maxlength="8" size="4" value="#{CurricularRulesManagement.minimumCredits}"/>
				<h:outputText value="</p>" escape="false"/>
			</h:panelGroup>
			
			<h:panelGroup rendered="#{CurricularRulesManagement.selectedCurricularRuleType == 'EVEN_ODD'}">
				<h:outputText value="<p><label>#{bolonhaBundle['apply.in']} #{bolonhaBundle['semester']}:</label>" escape="false"/>
				<fc:selectOneMenu value="#{CurricularRulesManagement.selectedSemester}">
					<f:selectItem itemLabel="1" itemValue="1"/>
					<f:selectItem itemLabel="2" itemValue="2"/>
				</fc:selectOneMenu>
				<h:outputText value="<p><label>#{bolonhaBundle['student.with.number']} :</label>" escape="false"/>
				<fc:selectOneMenu value="#{CurricularRulesManagement.selectedEven}">
					<f:selectItem itemLabel="#{bolonhaBundle['label.even']}" itemValue="true"/>
					<f:selectItem itemLabel="#{bolonhaBundle['label.odd']}" itemValue="false"/>
				</fc:selectOneMenu>					
				<h:outputText value="</p>" escape="false"/>
			</h:panelGroup>			
	 
			<h:panelGroup rendered="#{CurricularRulesManagement.selectedCurricularRuleType == 'ANY_CURRICULAR_COURSE'}">
				<h:outputText value="<p><label>#{bolonhaBundle['years']}:</label>" escape="false"/>
				<h:outputText value="#{bolonhaBundle['minimum']}: " escape="false"/>
				<h:inputText alt="#{htmlAltBundle['inputText.minimumYear']}" id="minimumYear" maxlength="8" size="4" value="#{CurricularRulesManagement.minimumYear}"/>
				<h:outputText value=" " escape="false"/>
				<h:outputText value="#{bolonhaBundle['maximum']}: " escape="false"/>
				<h:inputText alt="#{htmlAltBundle['inputText.maximumYear']}" id="maximumYear" maxlength="8" size="4" value="#{CurricularRulesManagement.maximumYear}"/>
				<h:outputText value="</p>" escape="false"/>
				<h:outputText value="<p><label>#{bolonhaBundle['apply.in']} #{bolonhaBundle['semester']}:</label>" escape="false"/>
				<fc:selectOneMenu value="#{CurricularRulesManagement.selectedSemester}">
					<f:selectItem itemLabel="#{bolonhaBundle['both']}" itemValue="0"/>
					<f:selectItem itemLabel="1" itemValue="1"/>
					<f:selectItem itemLabel="2" itemValue="2"/>
				</fc:selectOneMenu>
				<h:outputText value="</p>" escape="false"/>
				<h:outputText value="<p><label>#{bolonhaBundle['degreeType']}:</label>" escape="false"/>
				<fc:selectOneMenu value="#{CurricularRulesManagement.selectedDegreeType}" onchange="this.form.submit();"
						valueChangeListener="#{CurricularRulesManagement.onChangeDegreeTypeDropDown}">
					<f:selectItems binding="#{CurricularRulesManagement.degreeTypeItems}"/>
				</fc:selectOneMenu>
				<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID2' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
				<h:outputText value="</p>" escape="false"/>
				<h:outputText value="<p><label>#{bolonhaBundle['degree']}:</label>" escape="false"/>
				<fc:selectOneMenu value="#{CurricularRulesManagement.selectedDegreeID}">
					<f:selectItems binding="#{CurricularRulesManagement.degreeItems}"/>
				</fc:selectOneMenu>
				<h:outputText value="</p>" escape="false"/>
				<h:outputText value="<p><label>#{bolonhaBundle['department']}:</label>" escape="false"/>
				<fc:selectOneMenu value="#{CurricularRulesManagement.selectedDepartmentUnitID}">
					<f:selectItems binding="#{CurricularRulesManagement.departmentUnitItems}"/>
				</fc:selectOneMenu>
				<h:outputText value="</p>" escape="false"/>
			</h:panelGroup>
			
			<h:outputText value="<p><label>#{bolonhaBundle['apply.in']} #{bolonhaBundle['group']}:</label>" escape="false"/>
			<fc:selectOneMenu value="#{CurricularRulesManagement.selectedContextCourseGroupID}">
				<f:selectItems binding="#{CurricularRulesManagement.courseGroupItems}"/>
			</fc:selectOneMenu>
			<h:outputText value="</p>" escape="false"/>
			
			<h:panelGroup rendered="#{CurricularRulesManagement.selectedCurricularRuleType == 'PRECEDENCY_APPROVED_DEGREE_MODULE' 
						|| CurricularRulesManagement.selectedCurricularRuleType == 'PRECEDENCY_ENROLED_DEGREE_MODULE'
						|| CurricularRulesManagement.selectedCurricularRuleType == 'RESTRICTION_NOT_ENROLED_DEGREE_MODULE'}">
				<h:outputText value="<p><label>#{bolonhaBundle['apply.in']} #{bolonhaBundle['semester']}:</label>" escape="false"/>
				<fc:selectOneMenu value="#{CurricularRulesManagement.selectedSemester}">
					<f:selectItem itemLabel="#{bolonhaBundle['both']}" itemValue="0"/>
					<f:selectItem itemLabel="1" itemValue="1"/>
					<f:selectItem itemLabel="2" itemValue="2"/>
				</fc:selectOneMenu>
				<h:outputText value="</p>" escape="false"/>
			</h:panelGroup>
			
			<h:outputText value="<p><label>#{bolonhaBundle['beginExecutionPeriod.validity']}:</label> " escape="false"/>
			<h:selectOneMenu value="#{CurricularRulesManagement.beginExecutionPeriodID}">
				<f:selectItems binding="#{CurricularRulesManagement.beginExecutionPeriodItemsForRule}" />
			</h:selectOneMenu>
			<h:outputText value="</p>" escape="false"/>

			<h:outputText value="<p><label>#{bolonhaBundle['endExecutionPeriod.validity']}:</label> " escape="false"/>
			<h:selectOneMenu value="#{CurricularRulesManagement.endExecutionPeriodID}">
				<f:selectItems binding="#{CurricularRulesManagement.endExecutionPeriodItemsForRule}" />
			</h:selectOneMenu>
			<h:outputText value="</p>" escape="false"/>

			<h:outputText value="</fieldset></div>" escape="false"/>
		</h:panelGroup>
		
		<%-- composite rule --%>
		<h:panelGroup rendered="#{CurricularRulesManagement.type == 'compositeRule'}">
			<h:outputText escape="false" value="<input alt='input.type' id='type' name='type' type='hidden' value='#{CurricularRulesManagement.type}'/>"/>
		
			<h:outputText value="<div class='simpleblock4'> " escape="false"/>
			<h:outputText value="<h4 class='first'>#{bolonhaBundle['createCompositeRule']}</h4>" escape="false"/>
			
			<h:panelGroup rendered="#{empty CompositeRulesManagement.degreeModule.curricularRules}">
				<h:outputText value="<i>#{bolonhaBundle['no.existent.curricularRules']}</i>" escape="false"/>
			</h:panelGroup>
			<h:panelGroup rendered="#{!empty CompositeRulesManagement.degreeModule.curricularRules}">
				<h:outputText value="<br/>" escape="false"/>
				<h:outputText value="<p>#{bolonhaBundle['existent.curricularRules']}: <br/>" escape="false"/>
				<h:selectManyCheckbox value="#{CompositeRulesManagement.selectedCurricularRuleIDs}" layout="pageDirection">
					<f:selectItems binding="#{CompositeRulesManagement.curricularRuleItems}"/>
				</h:selectManyCheckbox>
				<h:outputText value="</p>" escape="false"/>
				<h:outputText value="<br/>" escape="false"/>
				
				<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
				<h:outputText value="<p><label>#{bolonhaBundle['logicOperators']}:</label>" escape="false"/>
				<h:selectOneRadio value="#{CompositeRulesManagement.selectedLogicOperator}" styleClass="nospace">
					<f:selectItem itemLabel="#{bolonhaBundle['label.operator.and']}" itemValue="AND"/>
					<f:selectItem itemLabel="#{bolonhaBundle['label.operator.or']}" itemValue="OR"/>
				</h:selectOneRadio>
				<h:outputText value="</fieldset>" escape="false"/>

			</h:panelGroup>
			<h:outputText value="</p></div><br/>" escape="false"/>
		</h:panelGroup>
 		
		<h:panelGroup rendered="#{empty CurricularRulesManagement.type}">
			<h:outputText value="<br/>" escape="false"/>
			<h:outputText value="<p>" escape="false"/>
			<h:outputLink value="#{facesContext.externalContext.requestContextPath}/academicAdministration/bolonha/curricularRules/createCurricularRule.faces">
				<h:outputText value="#{bolonhaBundle['new.rule']}" />
				<f:param name="type" value="rule" />
				<f:param name="degreeCurricularPlanID" value="#{CurricularRulesManagement.degreeCurricularPlanID}" />
				<f:param name="executionYearID" value="#{CurricularRulesManagement.executionYearID}"/>
				<f:param name="degreeModuleID" value="#{CurricularRulesManagement.degreeModuleID}" />
				<f:param name="organizeBy" value="#{CurricularRulesManagement.organizeBy}"/>
				<f:param name="showRules" value="#{CurricularRulesManagement.showRules}"/>
				<f:param name="hideCourses" value="#{CurricularRulesManagement.hideCourses}"/>
				<f:param name="action" value="#{CurricularRulesManagement.action}"/>
			</h:outputLink>
			<h:outputText value="</p>" escape="false"/>

			<h:outputText value="<p>" escape="false"/>
			<h:outputLink value="#{facesContext.externalContext.requestContextPath}/academicAdministration/bolonha/curricularRules/createCurricularRule.faces">
				<h:outputText value="#{bolonhaBundle['createCompositeRule']}" />
				<f:param name="type" value="compositeRule" />
				<f:param name="degreeCurricularPlanID" value="#{CurricularRulesManagement.degreeCurricularPlanID}" />
				<f:param name="executionYearID" value="#{CurricularRulesManagement.executionYearID}"/>
				<f:param name="degreeModuleID" value="#{CurricularRulesManagement.degreeModuleID}" />
				<f:param name="organizeBy" value="#{CurricularRulesManagement.organizeBy}"/>
				<f:param name="showRules" value="#{CurricularRulesManagement.showRules}"/>
				<f:param name="hideCourses" value="#{CurricularRulesManagement.hideCourses}"/>
				<f:param name="action" value="#{CurricularRulesManagement.action}"/>
			</h:outputLink>

			<h:outputText value="</p><br/>" escape="false"/>
		</h:panelGroup>

		<h:outputText value="<p>" escape="false"/>
		<h:panelGroup rendered="#{CurricularRulesManagement.type == 'rule'}">
			<h:commandButton alt="#{htmlAltBundle['commandButton.create']}" value="#{bolonhaBundle['create']}" styleClass="inputbutton" action="#{CurricularRulesManagement.createCurricularRule}"/>
		</h:panelGroup>
		<h:panelGroup rendered="#{CurricularRulesManagement.type == 'compositeRule'}">
			<h:commandButton alt="#{htmlAltBundle['commandButton.create']}" value="#{bolonhaBundle['create']}" styleClass="inputbutton" action="#{CompositeRulesManagement.createCompositeRule}"/>
		</h:panelGroup>
		
		<h:panelGroup rendered="#{CurricularRulesManagement.type == 'rule'}">
			<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" value="#{bolonhaBundle['cancel']}" styleClass="inputbutton" action="setCurricularRules"/>
		</h:panelGroup>
		<h:panelGroup rendered="#{empty CurricularRulesManagement.type || CurricularRulesManagement.type == 'compositeRule'}">
			<h:commandButton alt="#{htmlAltBundle['commandButton.back']}" immediate="true" value="#{bolonhaBundle['back']}" styleClass="inputbutton" action="setCurricularRules"/>
		</h:panelGroup>
		
		<h:outputText value="</p>" escape="false"/>
	</h:form>
</f:view>
