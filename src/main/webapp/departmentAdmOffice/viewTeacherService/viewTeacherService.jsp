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

<fp:select actionClass="net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.DepartmentAdmOfficeApp$ViewTeacherService" />

<f:view>
	
	<f:loadBundle basename="resources/DepartmentMemberResources"
		var="bundle" />
	<f:loadBundle basename="resources/EnumerationResources"
		var="bundleEnumeration" />
			
	<h:outputText value="<h2>#{viewTeacherService.departmentName}</h2> <p />" escape="false"/>
	
	<h:form id="teacherServiceForm">
		<h:panelGrid columns="1" styleClass="search">
			<h:panelGrid columns="3" styleClass="search">
				<h:outputText value="#{bundle['label.common.executionYear']}&nbsp;" escape="false" styleClass="aright" />
				<fc:selectOneMenu id="selectedExecutionYearID" value="#{viewTeacherService.selectedExecutionYearID}"
					onchange="this.form.submit();">
					<f:selectItems binding="#{viewTeacherService.executionYearItems}"/>
				</fc:selectOneMenu>
			 	<h:outputText value="#{bundle['label.common.courseSemester']}&nbsp;" escape="false" styleClass="aright" />
				<fc:selectOneMenu id="selectedExecutionPeriodID" value="#{viewTeacherService.selectedExecutionPeriodID}"
					onchange="this.form.submit();">
					<f:selectItems binding="#{viewTeacherService.executionPeriodsItems}"/>
				</fc:selectOneMenu>	
			</h:panelGrid>
			<h:panelGrid columns="2" styleClass="search" width="100%">
				<h:selectManyCheckbox value="#{viewTeacherService.selectedViewByTeacherOptions}" layout="pageDirection"
					onchange="this.form.submit();">
					<f:selectItems binding="#{viewTeacherService.viewByTeacherOptionsItems}"/>
				</h:selectManyCheckbox>
			</h:panelGrid>
		</h:panelGrid>
	</h:form>	
		
	<h:outputText value="<br />" escape="false" />

	<h:outputText value="<b>#{bundle['label.teacherService.navigateByTeacher']}</b>" escape="false"/>
	<h:outputText value=" #{bundle['label.teacherService.separator']} " escape="false"/>
	<h:outputText value="<a href='#{facesContext.externalContext.requestContextPath}/departmentAdmOffice/viewTeacherService/viewTeacherServiceByCourse.faces?selectedExecutionYearID=#{viewTeacherService.selectedExecutionYearID}'>" escape="false"/>
	<h:outputText value="#{bundle['label.teacherService.navigateByCourse']}" escape="false"/>
	<h:outputText value="</a> <br /> <p />" escape="false"/>
		
	<h:outputText value="<p /><b>#{bundle['label.teacherService.teacher.title']}</b><p />" escape="false"/>
	
	<h:panelGroup rendered="#{viewTeacherService.teacherServiceDTO == null}" style="error0">
		<h:outputText value="#{bundle['message.not-authorized']}" escape="false" />
	</h:panelGroup>
	<h:panelGroup rendered="#{viewTeacherService.teacherServiceDTO != null}">
	<h:outputText value="<table class='vtsbc'>" escape="false" />
		<h:outputText value="<tr class='acenter'>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.number']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.category']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.name']}</th>" escape="false" />
			<%--
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.hours']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.credits']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.totalLecturedHours']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.availability']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.accumulatedCredits']} <br/> #{viewTeacherService.previousExecutionYear.year}</th>" escape="false" />
			 --%>
		<h:outputText value="</tr>" escape="false" />
		<f:verbatim>
			<fc:dataRepeater value="#{viewTeacherService.teacherServiceDTO}" var="teacher">
				<h:outputText value="<tr id=#{teacher.teacherExternalId}>" escape="false" />
				<h:outputText value="<td title=\"#{bundle['label.teacherService.teacher.number']}\">#{teacher.teacherId}</td>" escape="false" />					
				<h:outputText value="<td title=\"#{bundle['label.teacherService.teacher.category']}\">#{teacher.teacherCategory}</td>" escape="false" />	
				<h:outputText value="<td class='courses' title=\"#{bundle['label.teacherService.teacher.name']}\">#{teacher.teacherName}</td>" escape="false" />
				<%--	
				<h:outputText value="<td title=\"#{bundle['label.teacherService.teacher.hours']}\">#{teacher.teacherRequiredHours}</td>" escape="false" />	
				<h:outputText value="<td title=\"#{bundle['label.teacherService.teacher.credits']}\">#{teacher.formattedTeacherSpentCredits}</td>" escape="false" />	
				<h:outputText value="<td title=\"#{bundle['label.teacherService.teacher.totalLecturedHours']}\">#{teacher.totalLecturedHours}</td>" escape="false" />	
				<h:outputText value="<td title=\"#{bundle['label.teacherService.teacher.availability']}\"> #{teacher.availability} </td>" escape="false" />
				<h:outputText value="<td title=\"#{bundle['label.teacherService.teacher.accumulatedCredits']}\"> #{teacher.formattedTeacherAccumulatedCredits} </td>" escape="false" />
				--%>
				<h:outputText value="</tr>" escape="false" />
										
				<h:panelGroup rendered="#{viewTeacherService.viewCreditsInformation == true}">
					<h:outputText value="<tr>" escape="false" />
					<h:outputText value="<td colspan=8 class='backwhite' style='background-color: #fff;'>" escape="false" />
						<h:outputText value="<ul>" escape="false" />
							<fc:dataRepeater value="#{teacher.executionCourseTeacherServiceList}" var="coursesList">
								<h:outputText value="<li><a href='#{facesContext.externalContext.requestContextPath}/departmentAdmOffice/viewTeacherService/viewTeacherServiceByCourse.faces?selectedExecutionYearID=#{viewTeacherService.selectedExecutionYearID}##{coursesList.executionCourseExternalId}'>" escape="false"/>
								<h:outputText value="#{coursesList.description} " escape="false" />	
							 	<h:outputText value="</a></li>" escape="false"/>
							</fc:dataRepeater>
							<fc:dataRepeater value="#{teacher.managementFunctionList}" var="managementFunction">
								<h:outputText value="<li>#{managementFunction.functionName}</li>" escape="false" />
								<%--<h:outputText value="<li>#{managementFunction.functionName} - #{managementFunction.credits}</li>" escape="false" /> --%>
							</fc:dataRepeater>
							<fc:dataRepeater value="#{teacher.exemptionSituationList}" var="exemptionSituation">
								<h:outputText value="<li>" escape="false"/>
								<fc:dataRepeater value="#{exemptionSituation.exemptionTypes}" var="exemptionType">
									<h:outputText value="#{exemptionType.contractSituation.name.content};" escape="false"/>
								</fc:dataRepeater>								
								<%--<h:outputText value="- #{exemptionSituation.credits}</li>" escape="false" /> --%>
							</fc:dataRepeater>
						<h:outputText value="</ul>" escape="false" />
					<h:outputText value="</td>" escape="false" />
					<h:outputText value="</tr>" escape="false" />
				</h:panelGroup>
			</fc:dataRepeater>
		</f:verbatim>
	<h:outputText value="</table>" escape="false" />
	</h:panelGroup>

	
</f:view>
