<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>

<ft:tilesView definition="definition.sop.examsPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>

<style type="text/css">@import url(<%= request.getContextPath() %>/CSS/dotist_calendars.css);</style>

<style type="text/css">
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
table.style2u td.description {
	padding: 0.2em 0.5em;
	background-color: #fafafa;
	border: none;
	border: 1px solid #ddd;
	text-align: left;
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

	<f:loadBundle basename="resources/ApplicationResourcesSOP" var="bundleSOP"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:outputFormat value="<h2>#{bundleSOP['link.writtenEvaluation.map']}</h2>" escape="false"/>

	<h:outputText styleClass="error" rendered="#{!empty SOPEvaluationManagementBackingBean.errorMessage}"
		value="#{bundleSOP[SOPEvaluationManagementBackingBean.errorMessage]}"/>
	<h:messages showSummary="true" errorClass="error" rendered="#{empty SOPEvaluationManagementBackingBean.errorMessage}"/>

	<h:form>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionPeriodIdHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionDegreeIdHidden}" />
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname}"/>
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.originPage}"/>
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.selectedBegin}"/>
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.selectedEnd}"/>
		<fc:viewState binding="#{SOPEvaluationManagementBackingBean.viewState}" />
		<h:outputText escape="false" value="<input alt='input.executionPeriodOID' id='executionPeriodOID' name='executionPeriodOID' type='hidden' value='#{SOPEvaluationManagementBackingBean.executionPeriodOID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.curricularYearIDsParameterString' id='curricularYearIDsParameterString' name='curricularYearIDsParameterString' type='hidden' value='#{SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}'/>"/>

		<h:panelGrid columns="2" styleClass="infotable">
			<h:outputText value="<b><i>#{bundleSOP['title.selected.degree']}:</b></i>" escape="false"/>
			<h:outputText value="&nbsp;" escape="false"/>
			
			<h:outputText value="#{bundleSOP['property.executionPeriod']}: " />
	 		<h:selectOneMenu value="#{SOPEvaluationManagementBackingBean.executionPeriodID}" 
	 						onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.enableDropDowns}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.executionPeriods}" />
			</h:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
			
			<h:outputText value="#{bundleSOP['property.context.degree']}: " />
			<h:selectOneMenu id="executionDegreeID" value="#{SOPEvaluationManagementBackingBean.executionDegreeID}"
							disabled="#{SOPEvaluationManagementBackingBean.disableDropDown}"
							onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.setNewValueExecutionDegreeID}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.executionDegrees}"/>
			</h:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID2' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>

			<h:outputText value="#{bundleSOP['property.context.period']}: " />
			<h:selectOneMenu id="calendarPeriod" value="#{SOPEvaluationManagementBackingBean.calendarPeriod}"
							disabled="#{SOPEvaluationManagementBackingBean.disableDropDown}"
							onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.setNewValueCalendarPeriod}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.calendarPeriodItems}"/>
			</h:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID3' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>

			<h:outputText value="#{bundleSOP['property.context.curricular.year']}: " />
			<h:selectManyCheckbox id="curricularYearIDs"  value="#{SOPEvaluationManagementBackingBean.curricularYearIDs}"
					disabled="#{SOPEvaluationManagementBackingBean.disableDropDown}"
					onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.setNewValueCurricularYearIDs}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.curricularYearItems}"/>
			</h:selectManyCheckbox>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID4' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
		</h:panelGrid>
		<h:outputText value="<br/>" escape="false"/>

	 	<h:panelGroup rendered="#{SOPEvaluationManagementBackingBean.renderContextSelection}">

		 	<fc:fenixCalendar 
		 		begin="#{SOPEvaluationManagementBackingBean.writtenEvaluationsCalendarBegin}" 
		 		end="#{SOPEvaluationManagementBackingBean.writtenEvaluationsCalendarEnd}"
		 		createLink="showExecutionCourses.faces?executionPeriodID=#{SOPEvaluationManagementBackingBean.executionPeriodID}&executionDegreeID=#{SOPEvaluationManagementBackingBean.executionDegreeID}&executionPeriodOID=#{SOPEvaluationManagementBackingBean.executionPeriodOID}&curricularYearIDsParameterString=#{SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}"
		 		editLinkPage="editWrittenTest.faces"
		 		editLinkParameters="#{SOPEvaluationManagementBackingBean.writtenTestsCalendarLink}"
		 	/>

			<h:outputText value="<br/><i>#{bundleSOP['label.defined.written.evaluations']}:</i><br/>" escape="false" styleClass="boldFontClass"/>
			<h:panelGroup rendered="#{empty SOPEvaluationManagementBackingBean.executionCoursesWithWrittenEvaluations}">
				<h:outputText value="<i>#{bundleSOP['label.no.defined.written.evaluations']}</i><br/>" escape="false" />
			</h:panelGroup>

		<h:panelGroup rendered="#{!empty SOPEvaluationManagementBackingBean.executionCoursesWithWrittenEvaluations}">
			<f:verbatim>
				<table class="style2u">
				<tr>			
					<th><c:out value="${bundle['label.coordinator.identification']}" escapeXml="false"/></th>
					<th><c:out value="${bundle['label.coordinator.evaluationDate']}" escapeXml="false"/></th>
					<th><c:out value="${bundle['label.coordinator.enroledStudents']}"escapeXml="false"/></th>
					<th><c:out value="${bundle['label.coordinator.missingPlaces']}" escapeXml="false"/></th>
					<th><c:out value="${bundle['label.coordinator.enrolmentPeriod']}" escapeXml="false"/></th>
					<th><c:out value="${bundle['label.coordinator.rooms']}"/></th>
					<th style="width: 9em;"></th>				
				</tr>			
				<c:forEach items="${SOPEvaluationManagementBackingBean.executionCourseWrittenEvaluationAgregationBeans}" var="executionCourseWrittenEvaluationAgregationBean">
					<tr class="space"><td></td></tr>
					<tr>
						<td colspan="2" class="header" style="font-weight: bold">
							<c:out value="${executionCourseWrittenEvaluationAgregationBean.executionCourse.sigla} - ${executionCourseWrittenEvaluationAgregationBean.executionCourse.nome}"/>
							<c:out value=": ${executionCourseWrittenEvaluationAgregationBean.curricularYear} ${bundleSOP['label.year.simple']}"/>
						</td>
						<td colspan="2" class="header" style="font-weight: bold">
							<c:url var="creationURL" value="showExecutionCourses.faces">
								<c:param name="executionPeriodID" value="${SOPEvaluationManagementBackingBean.executionPeriodID}"/>
								<c:param name="executionDegreeID" value="${SOPEvaluationManagementBackingBean.executionDegreeID}"/>
								<c:param name="executionPeriodOID" value="${SOPEvaluationManagementBackingBean.executionPeriodOID}"/>
								<c:param name="executionCourseID" value="${executionCourseWrittenEvaluationAgregationBean.executionCourse.idInternal}"/>
								<c:param name="curricularYearIDsParameterString" value="${SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}"/>
							</c:url>
							<a href='<c:out value="${creationURL}"/>' style="text-decoration:none">
								<c:out value="${bundleSOP['link.create.evaluation']}"/>
							</a>
						</td>
						<td colspan="3" class="header">
							<c:url var="commentURL" value="commentExecutionCourse.faces">
								<c:param name="executionDegreeID" value="${SOPEvaluationManagementBackingBean.executionDegreeID}"/>
								<c:param name="executionPeriodID" value="${SOPEvaluationManagementBackingBean.executionPeriodID}"/>
								<c:param name="executionCourseID" value="${executionCourse.idInternal}"/>
								<c:param name="executionPeriodOID" value="${SOPEvaluationManagementBackingBean.executionPeriodOID}"/>
								<c:param name="curricularYearIDsParameterString" value="${SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}"/>
							</c:url>
							<a href='<c:out value="${commentURL}"/>' style="text-decoration:none">
								<c:if test="${executionCourseWrittenEvaluationAgregationBean.executionCourse.comment != null && executionCourseWrittenEvaluationAgregationBean.executionCourse.comment != ''}">
									<c:out value="${executionCourseWrittenEvaluationAgregationBean.executionCourse.comment}"/>
								</c:if>
								<c:if test="${executionCourseWrittenEvaluationAgregationBean.executionCourse.comment == null || executionCourseWrittenEvaluationAgregationBean.executionCourse.comment == ''}">
									<c:out value="(${bundleSOP['link.define.comment']})"/>
								</c:if>
							</a>
						</td>
					</tr>						
					<c:forEach items="${executionCourseWrittenEvaluationAgregationBean.writtenEvaluations}" var="evaluation">
						<tr>
							<td class="description">
								<c:if test="${evaluation.class.name == 'net.sourceforge.fenixedu.domain.WrittenTest'}">
									<c:out value="${evaluation.description}"/>
								</c:if>
								<c:if test="${evaluation.class.name == 'net.sourceforge.fenixedu.domain.Exam'}">
									<c:out value="${evaluation.season}"/>
								</c:if>
							</td>
							<td>
								<fmt:formatDate pattern="dd/MM/yyyy" value="${evaluation.dayDate}"/><br/>
								<fmt:formatDate pattern="HH:mm" value="${evaluation.beginningDate}"/>
								<c:out value=" ${bundle['label.coordinator.to']} "/>
								<fmt:formatDate pattern="HH:mm" value="${evaluation.endDate}"/>
							</td>
							<td><c:out value="${SOPEvaluationManagementBackingBean.executionCoursesEnroledStudents[executionCourseWrittenEvaluationAgregationBean.executionCourse.idInternal]}"/></td>
							<td>
								<c:if test="${SOPEvaluationManagementBackingBean.writtenEvaluationsFreeSpace[evaluation.idInternal] != 0}">
									<c:out value="${SOPEvaluationManagementBackingBean.writtenEvaluationsFreeSpace[evaluation.idInternal]}"/>
								</c:if>
								<c:if test="${SOPEvaluationManagementBackingBean.writtenEvaluationsFreeSpace[evaluation.idInternal] == 0}">
									<c:out value="-"/>
								</c:if>
							</td>
							<c:if test="${evaluation.enrollmentBeginDayDate != null && evaluation.enrollmentEndDayDate != null}">
								<td>
									<c:out value=" ${bundle['label.coordinator.enrolmentBegin']}: "/>
									<fmt:formatDate pattern="dd/MM/yyyy" value="${evaluation.enrollmentBeginDayDate}"/>
									<c:out value=" - "/>
									<fmt:formatDate pattern="HH:mm" value="${evaluation.enrollmentBeginTimeDate}"/><br/>
									<c:out value=" ${bundle['label.coordinator.enrolmentEnd']}: "/>
									<fmt:formatDate pattern="dd/MM/yyyy" value="${evaluation.enrollmentEndDayDate}"/>
									<c:out value=" - "/>
								</td>
							</c:if>
							<c:if test="${evaluation.enrollmentBeginDayDate == null && evaluation.enrollmentEndDayDate == null}">
								<td>
									<c:out value=" - "/>
								</td>
							</c:if>
							<td>
								<c:if test="${SOPEvaluationManagementBackingBean.writtenEvaluationsRooms[evaluation.idInternal] != ''}">
									<c:out value="${SOPEvaluationManagementBackingBean.writtenEvaluationsRooms[evaluation.idInternal]}"/>
								</c:if>
								<c:if test="${SOPEvaluationManagementBackingBean.writtenEvaluationsRooms[evaluation.idInternal] == ''}">
									<c:out value="-"/>
								</c:if>
							</td>
							<td>
								<c:url var="editEvaluationURL" value="editWrittenTest.faces">
									<c:param name="executionDegreeID" value="${SOPEvaluationManagementBackingBean.executionDegreeID}"/>
									<c:param name="evaluationID" value="${evaluation.idInternal}"/>
									<c:param name="evaluationTypeClassname" value="${evaluation.class.name}"/>
									<c:param name="executionPeriodID" value="${SOPEvaluationManagementBackingBean.executionPeriodID}"/>
									<c:param name="executionCourseID" value="${executionCourseWrittenEvaluationAgregationBean.executionCourse.idInternal}"/>
									<c:param name="executionPeriodOID" value="${SOPEvaluationManagementBackingBean.executionPeriodOID}"/>
									<c:param name="curricularYearIDsParameterString" value="${SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}"/>
								</c:url>
								<a href='<c:out value="${editEvaluationURL}"/>'>
									<c:out value="${bundle['label.edit']}"/>
								</a>
								<c:out value=" | "/>
								<c:url var="deleteEvaluationURL" value="deleteWrittenEvaluation.faces">
									<c:param name="executionDegreeID" value="${SOPEvaluationManagementBackingBean.executionDegreeID}"/>
									<c:param name="evaluationID" value="${evaluation.idInternal}"/>
									<c:param name="evaluationTypeClassname" value="${evaluation.class.name}"/>
									<c:param name="executionPeriodID" value="${SOPEvaluationManagementBackingBean.executionPeriodID}"/>
									<c:param name="executionCourseID" value="${executionCourseWrittenEvaluationAgregationBean.executionCourse.idInternal}"/>
									<c:param name="executionPeriodOID" value="${SOPEvaluationManagementBackingBean.executionPeriodOID}"/>
									<c:param name="curricularYearIDsParameterString" value="${SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}"/>
								</c:url>
								<a href='<c:out value="${deleteEvaluationURL}"/>' onclick="return confirm('#{bundle['message.confirm.written.test']}')">
									<c:out value="${bundle['label.remove']}"/>
								</a>
							</td>
						</tr>
					</c:forEach>
				</c:forEach>			
				</table>		
			</f:verbatim>
			</h:panelGroup>

			<h:outputText value="<br/><br/><i>#{bundleSOP['label.execution.courses.without.written.evaluations']}:</i><br/>" escape="false" styleClass="boldFontClass"/>
			<h:panelGroup rendered="#{empty SOPEvaluationManagementBackingBean.executionCoursesWithoutWrittenEvaluations}">
				<h:outputText value="<i>#{bundleSOP['label.no.execution.courses.without.written.evaluations']}</i><br/>" escape="false" />
			</h:panelGroup>
			<h:panelGroup rendered="#{!empty SOPEvaluationManagementBackingBean.executionCoursesWithoutWrittenEvaluations}">
				<f:verbatim>
					<table class="executionCoursesWithoutWrittenEvaluations">
						<c:forEach items="${SOPEvaluationManagementBackingBean.executionCoursesWithoutWrittenEvaluations}" var="executionCourse">
							<tr>
								<td><b>
									<c:out value="${executionCourse.sigla} - ${executionCourse.nome}"/>
									</b>
								</td>
								<td>
									<c:url var="creationURL" value="showExecutionCourses.faces">
										<c:param name="executionPeriodID" value="${SOPEvaluationManagementBackingBean.executionPeriodID}"/>
										<c:param name="executionDegreeID" value="${SOPEvaluationManagementBackingBean.executionDegreeID}"/>
										<c:param name="executionPeriodOID" value="${SOPEvaluationManagementBackingBean.executionPeriodOID}"/>
										<c:param name="executionCourseID" value="${executionCourse.idInternal}"/>										
										<c:param name="curricularYearIDsParameterString" value="${SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}"/>
									</c:url>
									<a href='<c:out value="${creationURL}"/>' style="text-decoration:none">
										<c:out value="${bundleSOP['link.create.evaluation']}"/>
									</a>
								</td>
								<td>
									<c:url var="commentURL" value="commentExecutionCourse.faces">
										<c:param name="executionDegreeID" value="${SOPEvaluationManagementBackingBean.executionDegreeID}"/>
										<c:param name="executionPeriodID" value="${SOPEvaluationManagementBackingBean.executionPeriodID}"/>
										<c:param name="executionCourseID" value="${executionCourse.idInternal}"/>
										<c:param name="executionPeriodOID" value="${SOPEvaluationManagementBackingBean.executionPeriodOID}"/>							
										<c:param name="curricularYearIDsParameterString" value="${SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}"/>			
									</c:url>
									<a href='<c:out value="${commentURL}"/>' style="text-decoration:none">
										<c:if test="${executionCourse.comment != null && executionCourse.comment != ''}">
											<c:out value="${executionCourse.comment}"/>
										</c:if>
										<c:if test="${executionCourse.comment == null || executionCourse.comment == ''}">
											<c:out value="(${bundleSOP['link.define.comment']})"/>
										</c:if>
									</a>
								</td>
							</tr>
						</c:forEach>				
					</table>
				</f:verbatim>
			</h:panelGroup>

	 	</h:panelGroup>

	</h:form>

</ft:tilesView>
