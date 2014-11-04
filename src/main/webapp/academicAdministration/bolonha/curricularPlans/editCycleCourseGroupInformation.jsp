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
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<fp:select actionClass="org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication$CurricularPlansManagement" />

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	
	<h:outputText value="#{CycleCourseGroupInformationManagement.degreeCurricularPlan.name}" style="font-style: italic"/>
	<h:outputFormat value="<h2>#{bolonhaBundle['edit.param']} </h2>" escape="false">
		<f:param value="#{bolonhaBundle['courseGroup']}"/>
	</h:outputFormat>

	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
	<h:form>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CycleCourseGroupInformationManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.executionYearID' id='executionYearID' name='executionYearID' type='hidden' value='#{CycleCourseGroupInformationManagement.executionYearID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.courseGroupID' id='courseGroupID' name='courseGroupID' type='hidden' value='#{CycleCourseGroupInformationManagement.courseGroupID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.contextID' id='contextID' name='contextID' type='hidden' value='#{CycleCourseGroupInformationManagement.contextID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{CycleCourseGroupInformationManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.showRules' id='showRules' name='showRules' type='hidden' value='#{CycleCourseGroupInformationManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{CycleCourseGroupInformationManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{CycleCourseGroupInformationManagement.action}'/>"/>
		<h:outputText escape="false" value="<input alt='input.toOrder' id='toOrder' name='toOrder' type='hidden' value='#{CycleCourseGroupInformationManagement.toOrder}'/>"/>

		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<fieldset class='lfloat2'>" escape="false"/>

		
		<h:outputText value="<p><label>#{bolonhaBundle['cycleCourseGroupInformation.executionYear']}:</label> " escape="false"/>		
		<h:selectOneMenu value="#{CycleCourseGroupInformationManagement.informationExecutionYearId}">
			<f:selectItems value="#{CycleCourseGroupInformationManagement.executionYearItems}" />
		</h:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<p><label>#{bolonhaBundle['cycleCourseGroupInformation.graduateTitleSuffix']}: </label>" escape="false"/>
		<h:inputText alt="#{htmlAltBundle['inputText.suffixName']}" id="suffixName" size="60" maxlength="100" value="#{CycleCourseGroupInformationManagement.graduateTitleSuffix}"/>
		<h:outputText value="</p>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['cycleCourseGroupInformation.graduateTitleSuffix']} (en):</label>" escape="false"/>
		<h:inputText alt="#{htmlAltBundle['inputText.suffixNameEn']}" id="suffixNameEn" size="60" maxlength="100" value="#{CycleCourseGroupInformationManagement.graduateTitleSuffixEn}"/>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="</fieldset></div>" escape="false"/>	
			
		<h:outputText escape="false" value="<input alt='input.cycleCourseGroupInformationId' id='informationId' name='informationId' type='hidden' value='#{CycleCourseGroupInformationManagement.informationId}'" rendered="#{!empty CycleCourseGroupInformationManagement.informationId}"/>

		<h:outputText value="<p>" escape="false"/>		
		<h:commandButton alt="#{htmlAltBundle['commandButton.save']}" styleClass="inputbutton" value="#{bolonhaBundle['save']}"
			action="#{CycleCourseGroupInformationManagement.editCourseGroupInformation}" onclick="$('#informationId').attr('value', '#informationId'})"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}"
			action="editCurricularPlanStructure"/>	
		<h:outputText value="</p>" escape="false"/>
	</h:form>	
	
	
	<script src="<%= request.getContextPath() + "/javaScript/jquery/jquery.js" %>" type="text/javascript" >
	</script>
	
	<h:form>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CycleCourseGroupInformationManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.executionYearID' id='executionYearID' name='executionYearID' type='hidden' value='#{CycleCourseGroupInformationManagement.executionYearID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.courseGroupID' id='courseGroupID' name='courseGroupID' type='hidden' value='#{CycleCourseGroupInformationManagement.courseGroupID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.contextID' id='contextID' name='contextID' type='hidden' value='#{CycleCourseGroupInformationManagement.contextID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{CycleCourseGroupInformationManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.showRules' id='showRules' name='showRules' type='hidden' value='#{CycleCourseGroupInformationManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{CycleCourseGroupInformationManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{CycleCourseGroupInformationManagement.action}'/>"/>
		<h:outputText escape="false" value="<input alt='input.toOrder' id='toOrder' name='toOrder' type='hidden' value='#{CycleCourseGroupInformationManagement.toOrder}'/>"/>
		<h:outputText escape="false" value="<input alt='input.cycleCourseGroupInformationId' id='informationId' name='informationId' type='hidden'/>"/>
	
		<h:outputText value="<table style='width: 750px' class='showinfo1 bgcolor1'>" escape="false"/>
		<h:outputText value="<thead>" escape="false"/>
		<h:outputText value="<tr>" escape="false"/>
		<h:outputText value="<th>#{bolonhaBundle['cycleCourseGroupInformation.executionYear']}</th>" escape="false"/>
		<h:outputText value="<th>#{bolonhaBundle['cycleCourseGroupInformation.graduateTitleSuffix']}</th>" escape="false"/>
		<h:outputText value="<th>#{bolonhaBundle['cycleCourseGroupInformation.graduateTitleSuffix']} (en)</th>" escape="false"/>
		<h:outputText value="<th>-</th>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>
		<h:outputText value="</thead>" escape="false"/>
		<h:outputText value="<tbody>" escape="false"/>
		<fc:dataRepeater value="#{CycleCourseGroupInformationManagement.cycleCourseGroupInformationList}" var="cycleCourseGroupInformation" rendered="#{!empty CycleCourseGroupInformationManagement.cycleCourseGroupInformationList}" rowIndexVar="index">
			<h:outputText value="<tr>" escape="false"/>
				<h:outputText value="<td>#{cycleCourseGroupInformation.executionYear.year}</td>" escape="false"/>
				<h:outputText value="<td>#{cycleCourseGroupInformation.graduateTitleSuffixDefault}</td>" escape="false"/>
				<h:outputText value="<td>#{cycleCourseGroupInformation.graduateTitleSuffixEn}</td>" escape="false"/>
				<h:outputText value="<td>" escape="false"/>
				<h:commandButton alt="#{htmlAltBundle['commandButton.edit']}" styleClass="inputbutton" value="#{bolonhaBundle['edit']}"
					action="#{CycleCourseGroupInformationManagement.prepareEditCourseGroupInformation}" onclick="$('#informationId').attr('value', #{cycleCourseGroupInformation.externalId})"/>
				<h:outputText value="</td>" escape="false"/>
			<h:outputText value="</tr>" escape="false"/>
		</fc:dataRepeater>
		<h:outputText value="</tbody>" escape="false"/>
		<h:outputText value="</table>" escape="false"/>		
	</h:form>
</f:view>