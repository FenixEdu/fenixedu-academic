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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jstl/core"  prefix="c" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix"  prefix="fc" %>

<fp:select actionClass="net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.DepartmentAdmOfficeApp$ViewTeacherService" />

<f:view>
	
	<f:loadBundle basename="resources/DepartmentMemberResources"
		var="bundle" />
	<f:loadBundle basename="resources/EnumerationResources"
		var="bundleEnumeration" />
				
	<h:outputText value="<h2>#{viewTeacherService.departmentName}</h2> <p />" escape="false"/>
	
	<h:form>
		<h:panelGrid columns="1" styleClass="search">
			<h:panelGrid columns="2" styleClass="search">
				<h:outputText value="#{bundle['label.common.executionYear']}&nbsp;" escape="false" styleClass="aright" />
				<fc:selectOneMenu value="#{viewTeacherService.selectedExecutionYearID}"
					onchange="this.form.submit();">
					<f:selectItems binding="#{viewTeacherService.executionYearItems}"/>
				</fc:selectOneMenu>
			 	<h:outputText value="#{bundle['label.common.courseSemester']}&nbsp;" escape="false" styleClass="aright" />
				<fc:selectOneMenu value="#{viewTeacherService.selectedExecutionPeriodID}"
					onchange="this.form.submit();">
					<f:selectItems binding="#{viewTeacherService.executionPeriodsItems}"/>
				</fc:selectOneMenu>
			</h:panelGrid>
			<h:panelGrid columns="2" styleClass="search" width="100%">
				<h:selectManyCheckbox value="#{viewTeacherService.selectedViewOptions}" layout="pageDirection"
					onchange="this.form.submit();">
					<f:selectItems binding="#{viewTeacherService.viewOptionsItems}"/>
				</h:selectManyCheckbox>
			</h:panelGrid>
		</h:panelGrid>
		
		
		
	</h:form>	
	
	<h:form>
		<h:commandLink action="#{viewTeacherService.exportTeacherServiceByCourseToXLS}" value="#{bundle['label.teacherService.exportToExcel'] }" >
		</h:commandLink>
	</h:form>
	
	
	<h:outputText value="<br/>" escape="false" />
	
	<h:outputText value="<a href='#{facesContext.externalContext.requestContextPath}/departmentAdmOffice/viewTeacherService/viewTeacherService.faces?selectedExecutionYearID=#{viewTeacherService.selectedExecutionYearID}'>" escape="false"/>
	<h:outputText value="#{bundle['label.teacherService.navigateByTeacher']}" escape="false"/>		
	<h:outputText value="</a>"  escape="false"/>
	<h:outputText value=" #{bundle['label.teacherService.separator']} " escape="false"/>
	<h:outputText value="<b>#{bundle['label.teacherService.navigateByCourse']}</b> <br /> <p />" escape="false"/>
	
	<h:outputText value="<p /><b>#{bundle['label.teacherService.course.title']}</b><p />" escape="false"/>
	
	<h:outputText value="<table class='vtsbc'>" escape="false" />
		<h:outputText value="<tr class='acenter'>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.course.name']}</th>" escape="false" />
			
			<h:panelGroup rendered="#{viewTeacherService.viewCourseInformation == true}">
				<h:outputText value="<th>#{bundle['label.teacherService.course.campus']}</th>" escape="false" />
				<h:outputText value="<th>#{bundle['label.teacherService.course.degrees']}</th>" escape="false" />
				<h:outputText value="<th>#{bundle['label.teacherService.course.curricularYears']}</th>" escape="false" />
				<h:outputText value="<th>#{bundle['label.teacherService.course.semester']}</th>" escape="false" />
			</h:panelGroup>
			
			<h:panelGroup rendered="#{viewTeacherService.viewStudentsEnrolments == true}">
				<h:outputText value="<th>#{bundle['label.teacherService.course.firstTimeEnrolledStudentsNumber']}</th>" escape="false" />
				<h:outputText value="<th>#{bundle['label.teacherService.course.secondTimeEnrolledStudentsNumber']}</th>" escape="false" />
			</h:panelGroup>
			
			<h:outputText value="<th>#{bundle['label.teacherService.course.totalStudentsNumber']}</th>" escape="false" />
			
			<h:panelGroup rendered="#{viewTeacherService.viewHoursPerShift == true}">
				<h:outputText value="<th>#{bundle['label.teacherService.course.theoreticalHours']}</th>" escape="false" />
				<h:outputText value="<th>#{bundle['label.teacherService.course.praticalHours']}</th>" escape="false" />
				<h:outputText value="<th>#{bundle['label.teacherService.course.laboratorialHours']}</th>" escape="false" />
				<h:outputText value="<th>#{bundle['label.teacherService.course.theoPratHours']}</th>" escape="false" />
				<h:outputText value="<th>#{bundle['label.teacherService.course.seminary']}</th>" escape="false" />
				<h:outputText value="<th>#{bundle['label.teacherService.course.problems']}</th>" escape="false" />
				<h:outputText value="<th>#{bundle['label.teacherService.course.tutorialOrientation']}</th>" escape="false" />
				<h:outputText value="<th>#{bundle['label.teacherService.course.fieldWork']}</th>" escape="false" />
				<h:outputText value="<th>#{bundle['label.teacherService.course.trainingPeriod']}</th>" escape="false" />
			</h:panelGroup>
			
			<h:outputText value="<th>#{bundle['label.teacherService.course.totalHours']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.course.availability']}</th>" escape="false" />
			
			<h:panelGroup rendered="#{viewTeacherService.viewStudentsPerShift == true}">
				<h:outputText value="<th>#{bundle['label.teacherService.course.studentsNumberByTheoreticalShift']}</th>" escape="false" />
				<h:outputText value="<th>#{bundle['label.teacherService.course.studentsNumberByPraticalShift']}</th>" escape="false" />
				<h:outputText value="<th>#{bundle['label.teacherService.course.studentsNumberByLaboratorialShift']}</th>" escape="false" />
				<h:outputText value="<th>#{bundle['label.teacherService.course.studentsNumberByTheoPraticalShift']}</th>" escape="false" />
				<h:outputText value="<th>#{bundle['label.teacherService.course.studentsNumberBySeminaryShift']}</th>" escape="false" />
				<h:outputText value="<th>#{bundle['label.teacherService.course.studentsNumberByProblemsShift']}</th>" escape="false" />
				<h:outputText value="<th>#{bundle['label.teacherService.course.studentsNumberByTutorialOrientationShift']}</th>" escape="false" />
				<h:outputText value="<th>#{bundle['label.teacherService.course.studentsNumberByFieldWorkShift']}</th>" escape="false" />
				<h:outputText value="<th>#{bundle['label.teacherService.course.studentsNumberByTrainingPeriodShift']}</th>" escape="false" />
			</h:panelGroup>
		<h:outputText value="</tr>" escape="false" />
		<f:verbatim>
			<fc:dataRepeater value="#{viewTeacherService.executionCourseServiceDTO}" var="course">
				<h:outputText value="<tr id=#{course.executionCourseExternalId}>" escape="false" />
					<h:outputText value="<td class='courses' title=\"#{bundle['label.teacherService.course.name']}\">#{course.executionCourseName}</td>" escape="false" />	
					
					<h:panelGroup rendered="#{viewTeacherService.viewCourseInformation == true}">
						<h:outputText value="<td  title=\"#{bundle['label.teacherService.course.campus']}\">#{course.executionCourseCampus}</td>" escape="false" />	
						<h:outputText value="<td  title=\"#{bundle['label.teacherService.course.degrees']}\"> " escape="false" />	
						<h:outputText value="#{course.degreeListString} " escape="false"/>	
						<h:outputText value="</td>" escape="false" />
						<h:outputText value="<td title=\"#{bundle['label.teacherService.course.curricularYears']}\"> " escape="false" />	
						<h:outputText value="#{course.curricularYearListString} " escape="false"/>	
						<h:outputText value="</td>" escape="false" />				
						<h:outputText value="<td title=\"#{bundle['label.teacherService.course.semester']}\">#{course.executionCourseSemester}</td>" escape="false" />		
					</h:panelGroup>
					
					<h:panelGroup rendered="#{viewTeacherService.viewStudentsEnrolments == true}">
						<h:outputText value="<td title=\"#{bundle['label.teacherService.course.firstTimeEnrolledStudentsNumber']}\">#{course.executionCourseFirstTimeEnrollementStudentNumber}</td>" escape="false" />	
						<h:outputText value="<td title=\"#{bundle['label.teacherService.course.secondTimeEnrolledStudentsNumber']}\">#{course.executionCourseSecondTimeEnrollementStudentNumber}</td>" escape="false" />	
					</h:panelGroup>
									
					<h:outputText value="<td title=\"#{bundle['label.teacherService.course.totalStudentsNumber']}\">#{course.executionCourseStudentsTotalNumber}</td>" escape="false" />
					
					<h:panelGroup rendered="#{viewTeacherService.viewHoursPerShift == true}">
						<h:outputText value="<td title=\"#{bundle['label.teacherService.course.theoreticalHours']}\">#{course.formattedExecutionCourseTheoreticalHours}</td>" escape="false" />	
						<h:outputText value="<td title=\"#{bundle['label.teacherService.course.praticalHours']}\">#{course.formattedExecutionCoursePraticalHours}</td>" escape="false" />
						<h:outputText value="<td title=\"#{bundle['label.teacherService.course.laboratorialHours']}\">#{course.formattedExecutionCourseLaboratorialHours}</td>" escape="false" />
						<h:outputText value="<td title=\"#{bundle['label.teacherService.course.theoPratHours']}\">#{course.formattedExecutionCourseTheoPratHours}</td>" escape="false" />
						<h:outputText value="<td title=\"#{bundle['label.teacherService.course.seminary']}\">#{course.executionCourseSeminaryHours}</td>" escape="false" />
						<h:outputText value="<td title=\"#{bundle['label.teacherService.course.problems']}\">#{course.executionCourseProblemsHours}</td>" escape="false" />
						<h:outputText value="<td title=\"#{bundle['label.teacherService.course.tutorialOrientation']}\">#{course.executionCourseTutorialOrientationHours}</td>" escape="false" />
						<h:outputText value="<td title=\"#{bundle['label.teacherService.course.fieldWork']}\">#{course.executionCourseFieldWorkHours}</td>" escape="false" />
						<h:outputText value="<td title=\"#{bundle['label.teacherService.course.trainingPeriod']}\">#{course.executionCourseTrainingPeriodHours}</td>" escape="false" />
					</h:panelGroup>
									
					<h:outputText value="<td title=\"#{bundle['label.teacherService.course.totalHours']}\">#{course.totalDurationString}</td>" escape="false" />
					<h:outputText value="<td title=\"#{bundle['label.teacherService.course.availability']}\">#{course.executionCourseDurationBalance}</td>" escape="false" />
					
					<h:panelGroup rendered="#{viewTeacherService.viewStudentsPerShift == true}">
						<h:outputText value="<td title=\"#{bundle['label.teacherService.course.studentsNumberByTheoreticalShift']}\">#{course.formattedExecutionCourseStudentsNumberByTheoreticalShift}</td>" escape="false" />
						<h:outputText value="<td title=\"#{bundle['label.teacherService.course.studentsNumberByPraticalShift']}\">#{course.formattedExecutionCourseStudentsNumberByPraticalShift}</td>" escape="false" />
						<h:outputText value="<td title=\"#{bundle['label.teacherService.course.studentsNumberByLaboratorialShift']}\">#{course.formattedExecutionCourseStudentsNumberByLaboratorialShift}</td>" escape="false" />
						<h:outputText value="<td title=\"#{bundle['label.teacherService.course.studentsNumberByTheoPraticalShift']}\">#{course.formattedExecutionCourseStudentsNumberByTheoPraticalShift}</td>" escape="false" />
						
						<h:outputText value="<td title=\"#{bundle['label.teacherService.course.studentsNumberBySeminaryShift']}\">#{course.executionCourseStudentsNumberBySeminaryShift}</td>" escape="false" />
						<h:outputText value="<td title=\"#{bundle['label.teacherService.course.studentsNumberByProblemsShift']}\">#{course.executionCourseStudentsNumberByProblemsShift}</td>" escape="false" />
						<h:outputText value="<td title=\"#{bundle['label.teacherService.course.studentsNumberByTutorialOrientationShift']}\">#{course.executionCourseStudentsNumberByTutorialOrientationShift}</td>" escape="false" />
						<h:outputText value="<td title=\"#{bundle['label.teacherService.course.studentsNumberByFieldWorkShift']}\">#{course.executionCourseStudentsNumberByFieldWorkShift}</td>" escape="false" />
						<h:outputText value="<td title=\"#{bundle['label.teacherService.course.studentsNumberByTrainingPeriodShift']}\">#{course.executionCourseStudentsNumberByTrainingPeriodShift}</td>" escape="false" />
					</h:panelGroup>
				<h:outputText value="</tr>" escape="false" />
				
				<h:outputText value="<tr>" escape="false" />
				<h:outputText value="<td class='backwhite' style='background-color: #fff;' colspan='#{viewTeacherService.columnsCount}' >" escape="false" />
					<h:outputText value="<ul>" escape="false" />
						<fc:dataRepeater value="#{course.teacherExecutionCourseServiceList}" var="teacherList">
						 	<h:panelGroup rendered="#{teacherList.teacherOfDepartment == true}">
								<h:outputText value="<li><a href='#{facesContext.externalContext.requestContextPath}/departmentAdmOffice/viewTeacherService/viewTeacherService.faces?selectedExecutionYearID=#{viewTeacherService.selectedExecutionYearID}##{teacherList.teacherExternalId}'>"  escape="false"/>
								<h:outputText value="#{teacherList.description} " escape="false" />	
							 	<h:outputText value="</a></li>"  escape="false"/>
						 	</h:panelGroup>
						 	<h:panelGroup rendered="#{teacherList.teacherOfDepartment == false}">
							 	<h:outputText value="<li>"  escape="false"/>
								<h:outputText value="#{teacherList.description} " escape="false" />	
								<h:outputText  value="#{bundle['label.teacherService.hours']}" escape="false" />
							 	<h:outputText value="</li>"  escape="false"/>
							</h:panelGroup>
						</fc:dataRepeater>
					<h:outputText value="</ul>" escape="false" />
				<h:outputText value="</td>" escape="false" />
				<h:outputText value="</tr>" escape="false" />
			</fc:dataRepeater>
		</f:verbatim>
	<h:outputText value="</table>" escape="false" />


</f:view>