
<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld"  prefix="c" %>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld"  prefix="fc" %>


<ft:tilesView definition="departmentMember.masterPage"
	attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/DepartmentMemberResources"
		var="bundle" />
	<f:loadBundle basename="resources/EnumerationResources"
		var="bundleEnumeration" />
			
	<h:outputText value="<i>#{bundle['label.teacherService.title']}</i><p /><p />" escape="false"/>
	
	<h:outputText value="<h2>#{viewTeacherService.departmentName}</h2> <p />" escape="false"/>
	
	<h:form>
		<h:panelGrid columns="1" styleClass="search">
			<h:panelGrid columns="3" styleClass="search">
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
	
	<h:outputText value="<br/>" escape="false" />
	
	<h:outputText value="<a href='viewTeacherService.faces?selectedExecutionYearID=#{viewTeacherService.selectedExecutionYearID}'>" escape="false"/>
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
			</h:panelGroup>
			
			<h:outputText value="<th>#{bundle['label.teacherService.course.totalHours']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.course.availability']}</th>" escape="false" />
			
			<h:panelGroup rendered="#{viewTeacherService.viewStudentsPerShift == true}">
				<h:outputText value="<th>#{bundle['label.teacherService.course.studentsNumberByTheoreticalShift']}</th>" escape="false" />
				<h:outputText value="<th>#{bundle['label.teacherService.course.studentsNumberByPraticalShift']}</th>" escape="false" />
				<h:outputText value="<th>#{bundle['label.teacherService.course.studentsNumberByLaboratorialShift']}</th>" escape="false" />
				<h:outputText value="<th>#{bundle['label.teacherService.course.studentsNumberByTheoPraticalShift']}</th>" escape="false" />
			</h:panelGroup>
		<h:outputText value="</tr>" escape="false" />
		<f:verbatim>
			<fc:dataRepeater value="#{viewTeacherService.executionCourseServiceDTO}" var="course">
				<h:outputText value="<tr id=#{course.executionCourseIdInternal}>" escape="false" />
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
					</h:panelGroup>
									
					<h:outputText value="<td title=\"#{bundle['label.teacherService.course.totalHours']}\">#{course.executionCourseTotalHours}</td>" escape="false" />
					<h:outputText value="<td title=\"#{bundle['label.teacherService.course.availability']}\">#{course.executionCourseHoursBalance}</td>" escape="false" />
					
					<h:panelGroup rendered="#{viewTeacherService.viewStudentsPerShift == true}">
						<h:outputText value="<td title=\"#{bundle['label.teacherService.course.studentsNumberByTheoreticalShift']}\">#{course.formattedExecutionCourseStudentsNumberByTheoreticalShift}</td>" escape="false" />
						<h:outputText value="<td title=\"#{bundle['label.teacherService.course.studentsNumberByPraticalShift']}\">#{course.formattedExecutionCourseStudentsNumberByPraticalShift}</td>" escape="false" />
						<h:outputText value="<td title=\"#{bundle['label.teacherService.course.studentsNumberByLaboratorialShift']}\">#{course.formattedExecutionCourseStudentsNumberByLaboratorialShift}</td>" escape="false" />
						<h:outputText value="<td title=\"#{bundle['label.teacherService.course.studentsNumberByTheoPraticalShift']}\">#{course.formattedExecutionCourseStudentsNumberByTheoPraticalShift}</td>" escape="false" />
					</h:panelGroup>
				<h:outputText value="</tr>" escape="false" />
				
				<h:outputText value="<tr>" escape="false" />
				<h:outputText value="<td class='backwhite' style='background-color: #fff;' colspan='#{viewTeacherService.columnsCount}' >" escape="false" />
					<h:outputText value="<ul>" escape="false" />
						<fc:dataRepeater value="#{course.teacherExecutionCourseServiceList}" var="teacherList">
						 	<h:panelGroup rendered="#{teacherList.teacherOfDepartment == true}">
								<h:outputText value="<li><a href='viewTeacherService.faces?selectedExecutionYearID=#{viewTeacherService.selectedExecutionYearID}##{teacherList.teacherIdInternal}'>"  escape="false"/>
								<h:outputText value="#{teacherList.description} " escape="false" />	
							  	<h:outputText  value="#{bundle['label.teacherService.hours']}" escape="false" />
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


</ft:tilesView>