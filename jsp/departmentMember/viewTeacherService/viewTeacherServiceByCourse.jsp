<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld"  prefix="c" %>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld"  prefix="fc" %>



<ft:tilesView definition="departmentMember.masterPage"
	attributeName="body-inline">
	
<style>
table.vtsbc {
margin-bottom: 1em;
border: 2px solid #ccc;
text-align: center;
border-collapse: collapse;
}
table.vtsbc th {
padding: 0.2em 0.2em;
border: 1px solid #ddd;
border-bottom: 1px solid #ccc;
background-color: #eaeaea;
font-weight: normal;
}
table.vtsbc td {
background-color: #fafafa;
border: none;
border: 1px solid #eee;
padding: 0.25em 0.5em;
}
table.vtsbc td.courses {
background-color: #ffe;
width: 300px;
padding: 0.25em 0.25em;
text-align: left;
}
.center {
text-align: center;
}
.backwhite {
text-align: left;
background-color: #fff;
}
.backwhite a {
color: #888;
}
.backwhite ul {
margin: 0.3em 0;
}
.backwhite ul li {
padding: 0 0.5em;
}
.backwhite ul li a {
text-decoration: none;
border-bottom: 1px solid #ddd;
}
table.search {
background-color: #f5f5f5;
border-collapse: collapse;
}
table.search tr td {
border: 1px solid #fff;
padding: 0.3em;
}
.leftcolumn {
text-align: right;
}
</style>

	<f:loadBundle basename="ServidorApresentacao/DepartmentMemberResources"
		var="bundle" />
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources"
		var="bundleEnumeration" />
		<h:form>
		
			<h:outputText value="<i>#{bundle['label.teacherService.title']}</i><p /><p />" escape="false"/>
			
			<h:outputText value="<h2>#{viewTeacherService.departmentName}</h2> <p />" escape="false"/>
			
			<h:panelGrid columns="2" styleClass="search">
				<h:outputText value="#{bundle['label.common.executionYear']}&nbsp;" escape="false" styleClass="leftColumn" />
				<fc:selectOneMenu value="#{viewTeacherService.selectedExecutionYearID}"
					onchange="this.form.submit();">
					<f:selectItems binding="#{viewTeacherService.executionYearItems}"/>
				</fc:selectOneMenu>
			</h:panelGrid>
			
			<h:outputText value="<br/>" escape="false" />
			
			<h:outputText value="<a href='viewTeacherService.faces?selectedExecutionYearID=#{viewTeacherService.selectedExecutionYearID}'>" escape="false"/>
			<h:outputText value="#{bundle['label.teacherService.navigateByTeacher']}" escape="false"/>		
			<c:out value="</a>"  escapeXml="false"/>
			<h:outputText value=" #{bundle['label.teacherService.separator']} " escape="false"/>
			<h:outputText value="<b>#{bundle['label.teacherService.navigateByCourse']}</b> <br /> <p />" escape="false"/>
			
			<h:outputText value="<p /><b>#{bundle['label.teacherService.course.title']}</b><p />" escape="false"/>
			
			<c:out value="<table class='vtsbc'>" escapeXml="false" />
				<c:out value="<tr class='center'>" escapeXml="false" />
					<h:outputText value="<th>#{bundle['label.teacherService.course.name']}</th>" escape="false" />
					<h:outputText value="<th>#{bundle['label.teacherService.course.campus']}</th>" escape="false" />
					<h:outputText value="<th>#{bundle['label.teacherService.course.degrees']}</th>" escape="false" />
					<h:outputText value="<th>#{bundle['label.teacherService.course.curricularYears']}</th>" escape="false" />
					<h:outputText value="<th>#{bundle['label.teacherService.course.semester']}</th>" escape="false" />
					<h:outputText value="<th>#{bundle['label.teacherService.course.firstTimeEnrolledStudentsNumber']}</th>" escape="false" />
					<h:outputText value="<th>#{bundle['label.teacherService.course.secondTimeEnrolledStudentsNumber']}</th>" escape="false" />
					<h:outputText value="<th>#{bundle['label.teacherService.course.totalStudentsNumber']}</th>" escape="false" />
					<h:outputText value="<th>#{bundle['label.teacherService.course.theoreticalHours']}</th>" escape="false" />
					<h:outputText value="<th>#{bundle['label.teacherService.course.praticalHours']}</th>" escape="false" />
					<h:outputText value="<th>#{bundle['label.teacherService.course.laboratorialHours']}</th>" escape="false" />
					<h:outputText value="<th>#{bundle['label.teacherService.course.theoPratHours']}</th>" escape="false" />
					<h:outputText value="<th>#{bundle['label.teacherService.course.totalHours']}</th>" escape="false" />
					<h:outputText value="<th>#{bundle['label.teacherService.course.availability']}</th>" escape="false" />
					<h:outputText value="<th>#{bundle['label.teacherService.course.studentsNumberByTheoreticalShift']}</th>" escape="false" />
					<h:outputText value="<th>#{bundle['label.teacherService.course.studentsNumberByPraticalShift']}</th>" escape="false" />
					<h:outputText value="<th>#{bundle['label.teacherService.course.studentsNumberByLaboratorialShift']}</th>" escape="false" />
					<h:outputText value="<th>#{bundle['label.teacherService.course.studentsNumberByTheoPraticalShift']}</th>" escape="false" />
				<c:out value="</tr>" escapeXml="false" />
				<c:forEach items="${viewTeacherService.executionCourseServiceDTO}" var="course">
					<c:out value="<tr id=${course.executionCourseIdInternal}>" escapeXml="false" />
						<c:out value="<td class='courses' title=\"${bundle['label.teacherService.course.name']}\">${course.executionCourseName}</td>" escapeXml="false" />	
						<c:out value="<td  title=\"${bundle['label.teacherService.course.campus']}\">${course.executionCourseCampus}</td>" escapeXml="false" />	
						<c:out value="<td  title=\"${bundle['label.teacherService.course.degrees']}\"> " escapeXml="false" />	
						<c:forEach items="${course.executionCourseDegreeList}" var="degreeList">
								<c:out value="${degreeList} " escapeXml="false" />	
						</c:forEach>
						<c:out value="</td>" escapeXml="false" />
						<c:out value="<td title=\"${bundle['label.teacherService.course.curricularYears']}\"> " escapeXml="false" />	
						<c:forEach items="${course.executionCourseCurricularYearsList}" var="yearList">
								<c:out value="${yearList} " escapeXml="false" />	
						</c:forEach>
						<c:out value="</td>" escapeXml="false" />				
						<c:out value="<td title=\"${bundle['label.teacherService.course.semester']}\">${course.executionCourseSemester}</td>" escapeXml="false" />		
						<c:out value="<td title=\"${bundle['label.teacherService.course.firstTimeEnrolledStudentsNumber']}\">${course.executionCourseFirstTimeEnrollementStudentNumber}</td>" escapeXml="false" />	
						<c:out value="<td title=\"${bundle['label.teacherService.course.secondTimeEnrolledStudentsNumber']}\">${course.executionCourseSecondTimeEnrollementStudentNumber}</td>" escapeXml="false" />	
						<c:out value="<td title=\"${bundle['label.teacherService.course.totalStudentsNumber']}\">${course.executionCourseStudentsTotalNumber}</td>" escapeXml="false" />
						<c:out value="<td title=\"${bundle['label.teacherService.course.theoreticalHours']}\">${course.formattedExecutionCourseTheoreticalHours}</td>" escapeXml="false" />	
						<c:out value="<td title=\"${bundle['label.teacherService.course.praticalHours']}\">${course.formattedExecutionCoursePraticalHours}</td>" escapeXml="false" />
						<c:out value="<td title=\"${bundle['label.teacherService.course.laboratorialHours']}\">${course.formattedExecutionCourseLaboratorialHours}</td>" escapeXml="false" />
						<c:out value="<td title=\"${bundle['label.teacherService.course.theoPratHours']}\">${course.formattedExecutionCourseTheoPratHours}</td>" escapeXml="false" />
						<c:out value="<td title=\"${bundle['label.teacherService.course.totalHours']}\">${course.executionCourseTotalHours}</td>" escapeXml="false" />
						<c:out value="<td title=\"${bundle['label.teacherService.course.availability']}\">${course.executionCourseHoursBalance}</td>" escapeXml="false" />	
						<c:out value="<td title=\"${bundle['label.teacherService.course.studentsNumberByTheoreticalShift']}\">${course.formattedExecutionCourseStudentsNumberByTheoreticalShift}</td>" escapeXml="false" />
						<c:out value="<td title=\"${bundle['label.teacherService.course.studentsNumberByPraticalShift']}\">${course.formattedExecutionCourseStudentsNumberByPraticalShift}</td>" escapeXml="false" />
						<c:out value="<td title=\"${bundle['label.teacherService.course.studentsNumberByLaboratorialShift']}\">${course.formattedExecutionCourseStudentsNumberByLaboratorialShift}</td>" escapeXml="false" />
						<c:out value="<td title=\"${bundle['label.teacherService.course.studentsNumberByTheoPraticalShift']}\">${course.formattedExecutionCourseStudentsNumberByTheoPraticalShift}</td>" escapeXml="false" />		
					<c:out value="</tr>" escapeXml="false" />
					
					<c:out value="<tr>" escapeXml="false" />
					<c:out value="<td colspan=18 class='backwhite' style='background-color: #fff;'>" escapeXml="false" />
						<c:out value="<ul>" escapeXml="false" />
							<c:forEach items="${course.teacherExecutionCourseServiceList}" var="teacherList">
								<logic:equal name="teacherList" property="teacherOfDepartment" value="true" >
									<c:out value="<li><a href='viewTeacherService.faces?selectedExecutionYearID=${viewTeacherService.selectedExecutionYearID}#${teacherList.teacherIdInternal}'>"  escapeXml="false"/>
									<c:out value="${teacherList.description} " escapeXml="false" />	
								 	<h:outputText  value="#{bundle['label.teacherService.hours']}" escape="false" />
								 	<c:out value="</a></li>"  escapeXml="false"/>
							 	</logic:equal>
								<logic:notEqual name="teacherList" property="teacherOfDepartment" value="true" >
								 	<c:out value="<li>"  escapeXml="false"/>
									<c:out value="${teacherList.description} " escapeXml="false" />	
								 	<h:outputText  value="#{bundle['label.teacherService.hours']}" escape="false" />
								 	<c:out value="</li>"  escapeXml="false"/>
							 </logic:notEqual> 
							</c:forEach>
						<c:out value="</ul>" escapeXml="false" />
					<c:out value="</td>" escapeXml="false" />
					<c:out value="</tr>" escapeXml="false" />
				</c:forEach>
			<c:out value="</table>" escapeXml="false" />
		</h:form>
	
</ft:tilesView>