<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<ft:tilesView definition="df.coordinator.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
<style>
.boldFontClass { 
	font-weight: bold
}
table.style2u {
	margin-bottom: 1em;
	border: 2px solid #ccc;
	border-collapse: collapse;
	margin-top: 0.5em;
}
table.style2u th {
	padding: 0.2em 1em;
	border-bottom: 2px solid #ccc;
	background-color: #ddd;
	border-left: 1px solid #ccc;
	text-align: center;
}
table.style2u td.header {
	padding: 0.2em 0.5em;
	border: 1px solid #ddd;
	background-color: #eee;
	font-weight: bold;
	text-align: left;
	margin-top: 1em;
}
table.style2u td {
	padding: 0.2em 0.5em;
	background-color: #fafafa;
	border: none;
	border: 1px solid #ddd;
	text-align: center;
}
table.style2u td.courses {
	border-right: 1px solid #ddd;
	background-color: #ffe;
	padding: 0 1em;
}
table.style2u tr.space {
	padding: 1em;
}
table.style2u tr.space td {
	padding: 0.5em;
	background: none;
	border: none;
}
table.executionCoursesWithoutWrittenEvaluations {
	margin-bottom: 1em;
	border: 2px solid #ccc;
	border-collapse: collapse;
	margin-top: 0.5em;
}
table.executionCoursesWithoutWrittenEvaluations td {
	padding: 0.2em 0.5em;
	background-color: #fafafa;
	border: none;
	border: 1px solid #ddd;
	text-align: left;
}
</style>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>	
	<h:form>
		<h:inputHidden binding="#{coordinatorProjectsInformationBackingBean.degreeCurricularPlanIdHidden}"/>		
		<h:outputText value="<h2>#{bundle['label.coordinator.manageProjects']}</h2>" escape="false" />
		<h:outputText value="<div class='infoop' style='width: 30em'>" escape="false" />		
		<h:panelGrid columns="2">
			<h:outputText value="#{bundle['label.coordinator.selectedExecutionPeriod']}:" styleClass="boldFontClass" />
			<h:outputText value="" />
			<h:outputText value="#{bundle['property.executionPeriod']}: " escape="false" />
			<h:selectOneMenu value="#{coordinatorProjectsInformationBackingBean.executionPeriodID}">
				<f:selectItems value="#{coordinatorProjectsInformationBackingBean.executionPeriodsLabels}" />
			</h:selectOneMenu>					
			<h:outputText value="#{bundle['property.curricularYear']}: " escape="false" />
			<h:selectOneMenu value="#{coordinatorProjectsInformationBackingBean.curricularYearID}">
				<f:selectItems value="#{coordinatorProjectsInformationBackingBean.curricularYearsLabels}" />
			</h:selectOneMenu>
		</h:panelGrid>
		<h:outputText value="<br/>" escape="false" />				
		<h:commandButton alt="#{htmlAltBundle['commandButton.search']}" action="#{coordinatorProjectsInformationBackingBean.searchExecutionCourses}"
			value="#{bundle['button.search']}" styleClass="inputbutton"/>
 		<h:outputText value="</div>" escape="false" /> 		
 		<h:outputText value="<br/>" escape="false" />
		<h:outputText value="<strong>#{bundle['label.coordinator.instructions']}</strong>" escape="false" />
		<h:outputText value="<ul><li>#{bundle['label.coordinator.instruction1']}</li>" escape="false" />
		<h:outputText value="<li>#{bundle['label.coordinator.instruction2']}</li>" escape="false" />
		<h:outputText value="</ul>" escape="false" />
		<h:outputText value="<br/>" escape="false" />
		<style>@import url(<%= request.getContextPath() %>/CSS/dotist_calendars.css);</style>	
 		<fc:fenixCalendar 
 		   begin="#{coordinatorProjectsInformationBackingBean.calendarBeginDate}"
		   end="#{coordinatorProjectsInformationBackingBean.calendarEndDate}"
		   createLink="showExecutionCourses.faces?evaluationType=Project&degreeCurricularPlanID=#{coordinatorProjectsInformationBackingBean.degreeCurricularPlanID}&executionPeriodID=#{coordinatorProjectsInformationBackingBean.executionPeriodID}&curricularYearID=#{coordinatorProjectsInformationBackingBean.curricularYearID}"
		   editLinkPage="editProject.faces"
		   editLinkParameters="#{coordinatorProjectsInformationBackingBean.projectsCalendarLink}"
 		/>
 		<h:outputText value="<br/><i>#{bundle['label.coordinator.definedProjects']}</i><br/>" escape="false" styleClass="boldFontClass"/>
		<h:panelGroup rendered="#{empty coordinatorProjectsInformationBackingBean.executionCoursesWithProjects}">
			<h:outputText value="(#{bundle['label.coordinator.noExecutionCoursesWithProjects']})<br/>" escape="false" />
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty coordinatorProjectsInformationBackingBean.executionCoursesWithProjects}">
			<f:verbatim>
				<table class="style2u">
				<tr>			
					<th><c:out value="${bundle['label.coordinator.identification']}" escapeXml="false"/></th>
					<th><c:out value="${bundle['label.publish.date']}" escapeXml="false"/></th>
					<th><c:out value="${bundle['label.delivery.date']}" escapeXml="false"/></th>
					<th><c:out value="${bundle['label.description']}" escapeXml="false"/></th>
					<th style="width: 9em;"></th>				
				</tr>			
				<c:forEach items="${coordinatorProjectsInformationBackingBean.executionCoursesWithProjects}" var="executionCourse">
					<tr class="space"><td></td></tr>
					<tr><td colspan="5" class="header"><c:out value="${executionCourse.sigla} - ${executionCourse.nome}" /></td></tr>
					<c:forEach items="${coordinatorProjectsInformationBackingBean.projects[executionCourse.idInternal]}" var="evaluation">
						<tr>
							<td><c:out value="${evaluation.name}"/></td>
							<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${evaluation.begin}"/></td>
							<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${evaluation.end}"/></td>
							<td><c:out value="${evaluation.description}"/></td>
							<td>
								<c:url var="editEvaluationURL" value="editProject.faces">
									<c:param name="degreeCurricularPlanID" value="${coordinatorProjectsInformationBackingBean.degreeCurricularPlanID}"/>
									<c:param name="executionPeriodID" value="${coordinatorProjectsInformationBackingBean.executionPeriodID}"/>
									<c:param name="curricularYearID" value="${coordinatorProjectsInformationBackingBean.curricularYearID}"/>
									<c:param name="executionCourseID" value="${executionCourse.idInternal}"/>
									<c:param name="evaluationID" value="${evaluation.idInternal}"/>
								</c:url>
								<a href='<c:out value="${editEvaluationURL}"/>'>
									<c:out value="${bundle['label.edit']}"/>
								</a>
								<c:out value=" | "/>
								<c:url var="deleteEvaluationURL" value="deleteProject.faces">
									<c:param name="degreeCurricularPlanID" value="${coordinatorProjectsInformationBackingBean.degreeCurricularPlanID}"/>
									<c:param name="executionPeriodID" value="${coordinatorProjectsInformationBackingBean.executionPeriodID}"/>
									<c:param name="curricularYearID" value="${coordinatorProjectsInformationBackingBean.curricularYearID}"/>
									<c:param name="executionCourseID" value="${executionCourse.idInternal}"/>
									<c:param name="evaluationID" value="${evaluation.idInternal}"/>
								</c:url>
								<a href='<c:out value="${deleteEvaluationURL}"/>'>
									<c:out value="${bundle['label.remove']}"/>
								</a>
							</td>
						</tr>
					</c:forEach>
				</c:forEach>			
				</table>		
			</f:verbatim>
		</h:panelGroup>		
		<h:outputText value="<br/><i>#{bundle['label.coordinator.executionCourseWithoutDefinedProjects']}</i><br/>" escape="false" styleClass="boldFontClass"/>
		<h:panelGroup rendered="#{empty coordinatorProjectsInformationBackingBean.executionCoursesWithoutProjects}">
			<h:outputText value="(#{bundle['label.coordinator.noExecutionCoursesWithoutProjects']})<br/>" escape="false" />
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty coordinatorProjectsInformationBackingBean.executionCoursesWithoutProjects}">
			<f:verbatim>
				<table class="executionCoursesWithoutWrittenEvaluations">
					<c:forEach items="${coordinatorProjectsInformationBackingBean.executionCoursesWithoutProjects}" var="executionCourse">
						<tr>
							<td>
								<c:url var="evaluationManagementURL" value="createProject.faces">
									<c:param name="degreeCurricularPlanID" value="${coordinatorProjectsInformationBackingBean.degreeCurricularPlanID}"/>
									<c:param name="executionPeriodID" value="${coordinatorProjectsInformationBackingBean.executionPeriodID}"/>
									<c:param name="curricularYearID" value="${coordinatorProjectsInformationBackingBean.curricularYearID}"/>
									<c:param name="executionCourseID" value="${executionCourse.idInternal}"/>
								</c:url>
								<c:out value="${executionCourse.sigla} - ${executionCourse.nome}: ("/>
								<a href='<c:out value="${evaluationManagementURL}"/>' style="text-decoration:none">
									<c:out value="${bundle['link.coordinator.create.project']}"/>
								</a>
								<c:out value=")"/>
							</td>
						</tr>
					</c:forEach>				
				</table>
			</f:verbatim>
		</h:panelGroup>	
	</h:form>
</ft:tilesView>