<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld"  prefix="c" %>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>


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
text-align: center;
}
.center {
text-align: center;
}
.backwhite {
text-align: left;
background-color: #fff;
}
.backwhite a {
//color: #888;
}
.backwhite ul {
margin: 0.3em 0;
}
.backwhite ul li {
padding: 0.2em 0.5em;
}
.backwhite ul li a {
//text-decoration: none;
//border-bottom: 1px solid #ddd;
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


	
	<f:loadBundle basename="resources/DepartmentMemberResources"
		var="bundle" />
	<f:loadBundle basename="resources/EnumerationResources"
		var="bundleEnumeration" />
		
	<h:outputText value="<i>#{bundle['label.teacherService.title']}</i><p /><p />" escape="false"/>
	
	<h:outputText value="<h2>#{viewTeacherService.departmentName}</h2> <p />" escape="false"/>
	
	<h:form>
		<h:panelGrid columns="2" styleClass="search">
			<h:outputText value="#{bundle['label.common.executionYear']}&nbsp;" escape="false" styleClass="leftColumn" />
			<fc:selectOneMenu value="#{viewTeacherService.selectedExecutionYearID}"
				onchange="this.form.submit();">
				<f:selectItems binding="#{viewTeacherService.executionYearItems}"/>
			</fc:selectOneMenu>
		 	<h:outputText value="#{bundle['label.common.courseSemester']}&nbsp;" escape="false" styleClass="leftColumn" />
			<fc:selectOneMenu value="#{viewTeacherService.selectedExecutionPeriodID}"
				onchange="this.form.submit();">
				<f:selectItems binding="#{viewTeacherService.executionPeriodsItems}"/>
			</fc:selectOneMenu>
		</h:panelGrid>
	</h:form>	
	
	<h:outputText value="<br />" escape="false" />

	<h:outputText value="<b>#{bundle['label.teacherService.navigateByTeacher']}</b>" escape="false"/>
	<h:outputText value=" #{bundle['label.teacherService.separator']} " escape="false"/>
	<h:outputText value="<a href='viewTeacherServiceByCourse.faces?selectedExecutionYearID=#{viewTeacherService.selectedExecutionYearID}'>" escape="false"/>
	<h:outputText value="#{bundle['label.teacherService.navigateByCourse']}" escape="false"/>
	<c:out value="</a> <br /> <p />" escapeXml="false"/>
	
	
	
	<h:outputText value="<p /><b>#{bundle['label.teacherService.teacher.title']}</b><p />" escape="false"/>
	
	<c:out value="<table class='vtsbc'>" escapeXml="false" />
		<c:out value="<tr class='center'>" escapeXml="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.number']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.category']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.name']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.hours']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.credits']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.totalLecturedHours']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.availability']}</th>" escape="false" />
			<h:outputText value="<th>#{bundle['label.teacherService.teacher.accumulatedCredits']}</th>" escape="false" />
		<c:out value="</tr>" escapeXml="false" />
		<c:forEach items="${viewTeacherService.teacherServiceDTO}" var="teacher">
			<c:out value="<tr id=${teacher.teacherIdInternal}>" escapeXml="false" />
			<c:out value="<td title=\"${bundle['label.teacherService.teacher.number']}\">${teacher.teacherNumber}</td>" escapeXml="false" />					
			<c:out value="<td title=\"${bundle['label.teacherService.teacher.category']}\">${teacher.teacherCategory}</td>" escapeXml="false" />	
			<c:out value="<td class='courses' title=\"${bundle['label.teacherService.teacher.name']}\">${teacher.teacherName}</td>" escapeXml="false" />	
			<c:out value="<td title=\"${bundle['label.teacherService.teacher.hours']}\">${teacher.teacherRequiredHours}</td>" escapeXml="false" />	
			<c:out value="<td title=\"${bundle['label.teacherService.teacher.credits']}\">${teacher.formattedTeacherSpentCredits}</td>" escapeXml="false" />	
			<c:out value="<td title=\"${bundle['label.teacherService.teacher.totalLecturedHours']}\">${teacher.totalLecturedHours}</td>" escapeXml="false" />	
			<c:out value="<td title=\"${bundle['label.teacherService.teacher.availability']}\"> ${teacher.availability} </td>" escapeXml="false" />
			<c:out value="<td title=\"${bundle['label.teacherService.teacher.accumulatedCredits']}\"> ${teacher.formattedTeacherAccumulatedCredits} </td>" escapeXml="false" />
			<c:out value="</tr>" escapeXml="false" />
			
			<c:out value="<tr>" escapeXml="false" />
			<c:out value="<td colspan=8 class='backwhite' style='background-color: #fff;'>" escapeXml="false" />
				<c:out value="<ul>" escapeXml="false" />
					<c:forEach items="${teacher.executionCourseTeacherServiceList}" var="coursesList">
						<c:out value="<li><a href='viewTeacherServiceByCourse.faces?selectedExecutionYearID=${viewTeacherService.selectedExecutionYearID}#${coursesList.executionCourseIdInternal}'>" escapeXml="false"/>
						<c:out value="${coursesList.description} " escapeXml="false" />	
					 	<h:outputText value="#{bundle['label.teacherService.hours']}" escape="false" />
					 	<c:out value="</a></li>" escapeXml="false"/>
					</c:forEach>
					<c:forEach items="${teacher.managementFunctionList}" var="managementFunction">
						<c:out value="<li>${managementFunction.functionName} - ${managementFunction.credits}</li>" escapeXml="false" />
					</c:forEach>
					<c:forEach items="${teacher.exemptionSituationList}" var="exemptionSituation">
						<c:out value="<li>" escapeXml="false"/>
						<c:out value="${bundleEnumeration[exemptionSituation.functionName]}" />
						<c:out value=" - ${exemptionSituation.credits}</li>" escapeXml="false" />
					</c:forEach>
				<c:out value="</ul>" escapeXml="false" />
			<c:out value="</td>" escapeXml="false" />
			<c:out value="</tr>" escapeXml="false" />
		</c:forEach>
	<c:out value="</table>" escapeXml="false" />

	
</ft:tilesView>