<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jstl/core"  prefix="c" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>


<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/DepartmentMemberResources"
		var="bundle" />
	<f:loadBundle basename="resources/EnumerationResources"
		var="bundleEnumeration" />

	<h:outputText value="<h2>#{bundle['label.teacherService.title']}</h2>" escape="false"/>
	<h:outputText value="<h3>#{viewTeacherService.departmentName}</h3>" escape="false"/>
	
	<h:outputText value="#{bundle['label.teacherService.unavailable']}" escape="false" styleClass="error"/>
	<%-- 
	<h:form>
		<h:outputText value="<table class='tstyle5 mtop05 mbottom15'>" escape="false" />
		<h:outputText value="<tr><td>" escape="false" />
			<h:outputText value="#{bundle['label.common.executionYear']}:" escape="false" styleClass="aright" />
		<h:outputText value="</td><td>" escape="false" />
			<fc:selectOneMenu value="#{viewTeacherService.selectedExecutionYearID}"
				onchange="this.form.submit();">
				<f:selectItems binding="#{viewTeacherService.executionYearItems}"/>
			</fc:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
		<h:outputText value="</td></tr><tr><td>" escape="false" />
		 	<h:outputText value="#{bundle['label.common.courseSemester']}:" escape="false" styleClass="aright" />
		 <h:outputText value="</td><td>" escape="false" />
			<fc:selectOneMenu value="#{viewTeacherService.selectedExecutionPeriodID}"
				onchange="this.form.submit();">
				<f:selectItems binding="#{viewTeacherService.executionPeriodsItems}"/>
			</fc:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID2' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
		<h:outputText value="</td></tr>" escape="false" />
		<h:outputText value="</table>" escape="false" />

		<h:selectManyCheckbox value="#{viewTeacherService.selectedViewByTeacherOptions}" layout="pageDirection"
			onchange="this.form.submit();">
			<f:selectItems binding="#{viewTeacherService.viewByTeacherOptionsItems}"/>
		</h:selectManyCheckbox>
		<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID3' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>

	</h:form>	
		


	<h:outputText value="<p class='mtop15'>Visualizar por: <b>#{bundle['label.teacherService.navigateByTeacher']}</b>" escape="false"/>
	<h:outputText value=" #{bundle['label.teacherService.separator']} " escape="false"/>
	<h:outputText value="#{facesContext.externalContext.requestContextPath}/departmentMember/viewTeacherService/<a href='viewTeacherServiceByCourse.faces?selectedExecutionYearID=#{viewTeacherService.selectedExecutionYearID}'>" escape="false"/>
	<h:outputText value="#{bundle['label.teacherService.navigateByCourse']}" escape="false"/>
	<h:outputText value="</a></p>" escape="false"/>
	
	
	<h:outputText value="<table class='tstyle4'>" escape="false" />
		<h:outputText value="<tr class='acenter'>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.number']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.category']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.name']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.hours']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.credits']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.totalLecturedHours']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.availability']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.accumulatedCredits']} <br/> #{viewTeacherService.previousExecutionYear.year}</th>" escape="false" />
		<h:outputText value="</tr>" escape="false" />
		<f:verbatim>
			<fc:dataRepeater value="#{viewTeacherService.teacherServiceDTO}" var="teacher">
				<h:outputText value="<tr id=#{teacher.teacherExternalId}>" escape="false" />
				<h:outputText value="<td title=\"#{bundle['label.teacherService.teacher.number']}\">#{teacher.teacherId}</td>" escape="false" />					
				<h:outputText value="<td title=\"#{bundle['label.teacherService.teacher.category']}\">#{teacher.teacherCategory}</td>" escape="false" />	
				<h:outputText value="<td class='highlight1' title=\"#{bundle['label.teacherService.teacher.name']}\">#{teacher.teacherName}</td>" escape="false" />	
				<h:outputText value="<td class='acenter' title=\"#{bundle['label.teacherService.teacher.hours']}\">#{teacher.teacherRequiredHours}</td>" escape="false" />	
				<h:outputText value="<td class='acenter' title=\"#{bundle['label.teacherService.teacher.credits']}\">#{teacher.formattedTeacherSpentCredits}</td>" escape="false" />	
				<h:outputText value="<td class='acenter' title=\"#{bundle['label.teacherService.teacher.totalLecturedHours']}\">#{teacher.totalLecturedHours}</td>" escape="false" />	
				<h:outputText value="<td class='acenter' title=\"#{bundle['label.teacherService.teacher.availability']}\"> #{teacher.availability} </td>" escape="false" />
				<h:outputText value="<td class='acenter' title=\"#{bundle['label.teacherService.teacher.accumulatedCredits']}\"> #{teacher.formattedTeacherAccumulatedCredits} </td>" escape="false" />
				<h:outputText value="</tr>" escape="false" />
										
				<h:panelGroup rendered="#{viewTeacherService.viewCreditsInformation == true}">
					<h:outputText value="<tr>" escape="false" />
					<h:outputText value="<td colspan=8 class='backwhite' style='background-color: #fff; padding-bottom: 1em;'>" escape="false" />
						<h:outputText value="<ul class='smalltxt mbottom2'>" escape="false" />
							<fc:dataRepeater value="#{teacher.executionCourseTeacherServiceList}" var="coursesList">
								<h:outputText value="#{facesContext.externalContext.requestContextPath}/departmentMember/viewTeacherService/<li><a href='viewTeacherServiceByCourse.faces?selectedExecutionYearID=#{viewTeacherService.selectedExecutionYearID}##{coursesList.executionCourseExternalId}'>" escape="false"/>
								<h:outputText value="#{coursesList.description} " escape="false" />	
							 	<h:outputText value="#{bundle['label.teacherService.hours']}" escape="false" />
							 	<h:outputText value="</a></li>" escape="false"/>
							</fc:dataRepeater>
							<fc:dataRepeater value="#{teacher.managementFunctionList}" var="managementFunction">
								<h:outputText value="<li>#{managementFunction.functionName} - #{managementFunction.credits}</li>" escape="false" />
							</fc:dataRepeater>
							<fc:dataRepeater value="#{teacher.exemptionSituationList}" var="exemptionSituation">
								<h:outputText value="<li>" escape="false"/>
								<fc:dataRepeater value="#{exemptionSituation.exemptionTypes}" var="exemptionType">
									<h:outputText value="#{exemptionType.contractSituation.name.content}; " escape="false"/>
								</fc:dataRepeater>								
								<h:outputText value="- #{exemptionSituation.credits}</li>" escape="false" />
							</fc:dataRepeater>
						<h:outputText value="</ul>" escape="false" />
					<h:outputText value="</td>" escape="false" />
					<h:outputText value="</tr>" escape="false" />
				</h:panelGroup>
			</fc:dataRepeater>
		</f:verbatim>
	<h:outputText value="</table>" escape="false" />
--%>
	
</f:view>