<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>

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
		<h:inputHidden binding="#{coordinatorWrittenTestsInformationBackingBean.degreeCurricularPlanIdHidden}"/>		
		<h:outputText value="<h2>#{bundle['label.coordinator.manageWrittenTests']}</h2>" escape="false" />		
		<h:outputText value="<div class='infoop' style='width: 30em'>" escape="false" />		
		<h:panelGrid columns="2">
			<h:outputText value="#{bundle['label.coordinator.selectedExecutionPeriod']}:" styleClass="boldFontClass" />
			<h:outputText value="" />
			<h:outputText value="#{bundle['property.executionPeriod']}: " escape="false" />
			<h:selectOneMenu value="#{coordinatorWrittenTestsInformationBackingBean.executionPeriodID}">
				<f:selectItems value="#{coordinatorWrittenTestsInformationBackingBean.executionPeriodsLabels}" />
			</h:selectOneMenu>
			<h:outputText value="#{bundle['property.curricularYear']}: " escape="false" />
			<h:selectOneMenu value="#{coordinatorWrittenTestsInformationBackingBean.curricularYearID}">
				<f:selectItems value="#{coordinatorWrittenTestsInformationBackingBean.curricularYearsLabels}" />
			</h:selectOneMenu>
		</h:panelGrid>
		<h:outputText value="<br/>" escape="false" />
		<h:commandButton alt="#{htmlAltBundle['commandButton.search']}" action="#{coordinatorWrittenTestsInformationBackingBean.searchExecutionCourses}"
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
			begin="#{coordinatorWrittenTestsInformationBackingBean.calendarBeginDate}"
			end="#{coordinatorWrittenTestsInformationBackingBean.calendarEndDate}"
			createLink="showExecutionCourses.faces?evaluationType=WrittenTest&degreeCurricularPlanID=#{coordinatorWrittenTestsInformationBackingBean.degreeCurricularPlanID}&executionPeriodID=#{coordinatorWrittenTestsInformationBackingBean.executionPeriodID}&curricularYearID=#{coordinatorWrittenTestsInformationBackingBean.curricularYearID}"
			editLinkPage="editWrittenTest.faces"
			editLinkParameters="#{coordinatorWrittenTestsInformationBackingBean.writtenTestsCalendarLink}"
		/>
		<h:outputText value="<br/><i>#{bundle['label.coordinator.definedWrittenTests']}</i><br/>" escape="false" styleClass="boldFontClass"/>
		<h:panelGroup rendered="#{empty coordinatorWrittenTestsInformationBackingBean.executionCoursesWithWrittenTests}">
			<h:outputText value="(#{bundle['label.coordinator.noExecutionCoursesWithWrittenTests']})<br/>" escape="false" />
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty coordinatorWrittenTestsInformationBackingBean.executionCoursesWithWrittenTests}">
			<f:verbatim>
				<table class="style2u">
				<tr>			
					<th><c:out value="${bundle['label.coordinator.identification']}" escapeXml="false"/></th>
					<th><c:out value="${bundle['label.coordinator.evaluationDate']}" escapeXml="false"/></th>
					<th><c:out value="${bundle['label.coordinator.enroledStudents']}"escapeXml="false"/></th>
					<th><c:out value="${bundle['label.coordinator.freeSpaces']}" escapeXml="false"/></th>
					<th><c:out value="${bundle['label.coordinator.enrolmentPeriod']}" escapeXml="false"/></th>
					<th><c:out value="${bundle['label.coordinator.rooms']}"/></th>
					<th style="width: 9em;"></th>				
				</tr>			
				<c:forEach items="${coordinatorWrittenTestsInformationBackingBean.executionCoursesWithWrittenTests}" var="executionCourse">
					<tr class="space"><td></td></tr>
					<tr><td colspan="7" class="header"><c:out value="${executionCourse.sigla} - ${executionCourse.nome}" /></td></tr>
					<c:forEach items="${coordinatorWrittenTestsInformationBackingBean.writtenTests[executionCourse.idInternal]}" var="evaluation">
						<tr>
							<td><c:out value="${evaluation.description}"/></td>
							<td>
								<fmt:formatDate pattern="dd/MM/yyyy" value="${evaluation.dayDate}"/><br/>
								<fmt:formatDate pattern="HH:mm" value="${evaluation.beginningDate}"/>
								<c:out value=" ${bundle['label.coordinator.to']} "/>
								<fmt:formatDate pattern="HH:mm" value="${evaluation.endDate}"/>
							</td>
							<td><c:out value="${evaluation.writtenEvaluationEnrolmentsCount}"/></td>
							<td>
								<c:if test="${coordinatorWrittenTestsInformationBackingBean.writtenTestsFreeSpace[evaluation.idInternal] != 0}">
									<c:out value="${coordinatorWrittenTestsInformationBackingBean.writtenTestsFreeSpace[evaluation.idInternal]}"/>
								</c:if>
								<c:if test="${coordinatorWrittenTestsInformationBackingBean.writtenTestsFreeSpace[evaluation.idInternal] == 0}">
									<c:out value="-"/>
								</c:if>
							</td>
							<td>
								<c:out value=" ${bundle['label.coordinator.enrolmentBegin']}: "/>
								<c:if test="${evaluation.enrollmentBeginDayDate != null}">
									<fmt:formatDate pattern="dd/MM/yyyy" value="${evaluation.enrollmentBeginDayDate}"/>
									<c:out value=" - "/>
									<fmt:formatDate pattern="HH:mm" value="${evaluation.enrollmentBeginTimeDate}"/><br/>
									<c:out value=" ${bundle['label.coordinator.enrolmentBegin']}: "/>
								</c:if>
								<c:if test="${evaluation.enrollmentBeginDayDate == null}"><br/></c:if>
								
								<c:out value=" ${bundle['label.coordinator.enrolmentEnd']}: "/>
								<c:if test="${evaluation.enrollmentEndDayDate != null}">
									<fmt:formatDate pattern="dd/MM/yyyy" value="${evaluation.enrollmentEndDayDate}"/>
									<c:out value=" - "/>
									<fmt:formatDate pattern="HH:mm" value="${evaluation.enrollmentEndTimeDate}"/><br/>
								</c:if>
								<c:if test="${evaluation.enrollmentEndDayDate == null}"></c:if>
							</td>
							<td>
								<c:if test="${coordinatorWrittenTestsInformationBackingBean.writtenTestsRooms[evaluation.idInternal] != ''}">
									<c:out value="${coordinatorWrittenTestsInformationBackingBean.writtenTestsRooms[evaluation.idInternal]}"/>
								</c:if>
								<c:if test="${coordinatorWrittenTestsInformationBackingBean.writtenTestsRooms[evaluation.idInternal] == ''}">
									<c:out value="-"/>
								</c:if>
							</td>
							<td>
								<c:url var="editEvaluationURL" value="editWrittenTest.faces">
									<c:param name="degreeCurricularPlanID" value="${coordinatorWrittenTestsInformationBackingBean.degreeCurricularPlanID}"/>
									<c:param name="executionPeriodID" value="${coordinatorWrittenTestsInformationBackingBean.executionPeriodID}"/>
									<c:param name="curricularYearID" value="${coordinatorWrittenTestsInformationBackingBean.curricularYearID}"/>
									<c:param name="executionCourseID" value="${executionCourse.idInternal}"/>
									<c:param name="evaluationID" value="${evaluation.idInternal}"/>
								</c:url>
								<a href='<c:out value="${editEvaluationURL}"/>'>
									<c:out value="${bundle['label.edit']}"/>
								</a>
								<c:out value=" | "/>
								<c:url var="deleteEvaluationURL" value="deleteWrittenTest.faces">
									<c:param name="degreeCurricularPlanID" value="${coordinatorWrittenTestsInformationBackingBean.degreeCurricularPlanID}"/>
									<c:param name="executionPeriodID" value="${coordinatorWrittenTestsInformationBackingBean.executionPeriodID}"/>
									<c:param name="curricularYearID" value="${coordinatorWrittenTestsInformationBackingBean.curricularYearID}"/>
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
		<h:outputText value="<br/><i>#{bundle['label.coordinator.executionCourseWithoutDefinedWrittenTests']}</i><br/>" escape="false" styleClass="boldFontClass"/>
		<h:panelGroup rendered="#{empty coordinatorWrittenTestsInformationBackingBean.executionCoursesWithoutWrittenTests}">
			<h:outputText value="(#{bundle['label.coordinator.noExecutionCoursesWithoutWrittenTests']})<br/>" escape="false" />
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty coordinatorWrittenTestsInformationBackingBean.executionCoursesWithoutWrittenTests}">
			<f:verbatim>
				<table class="executionCoursesWithoutWrittenEvaluations">
					<c:forEach items="${coordinatorWrittenTestsInformationBackingBean.executionCoursesWithoutWrittenTests}" var="executionCourse">
						<tr>
							<td>
								<c:url var="evaluationManagementURL" value="createWrittenTest.faces">
									<c:param name="degreeCurricularPlanID" value="${coordinatorWrittenTestsInformationBackingBean.degreeCurricularPlanID}"/>
									<c:param name="executionPeriodID" value="${coordinatorWrittenTestsInformationBackingBean.executionPeriodID}"/>
									<c:param name="curricularYearID" value="${coordinatorWrittenTestsInformationBackingBean.curricularYearID}"/>
									<c:param name="executionCourseID" value="${executionCourse.idInternal}"/>
								</c:url>
								<c:out value="${executionCourse.sigla} - ${executionCourse.nome}: ("/>
								<a href='<c:out value="${evaluationManagementURL}"/>' style="text-decoration:none">
									<c:out value="${bundle['link.coordinator.create.written.test']}"/>
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