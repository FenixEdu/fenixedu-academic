<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<ft:tilesView definition="definition.student.masterPage" attributeName="body-inline">
<style>
div.evalcontainer {
/*background-color: #fafafa;*/
padding: 1em 0;
/*border: 2px solid #aca;*/
}
table.evallist {
margin-bottom: 1em;
text-align: center;
border-collapse: collapse;
}
table.evallist tr {
}
table.evallist th {
padding: 0.25em 0.5em;
background-color: #eaeaea;
font-weight: normal;
}
table.evallist td {
border-bottom: 1px solid #ddd;
border-top: 1px solid #ddd;
padding: 0.25em 0.5em;
}
table.evallist td.evallist_empty {
text-align: left;
background-color: #fff;
border: none;
padding: 1.0em 0 0 0;
}
.left {
text-align: left;
}
table.evallist td.title {
padding: 0.3em;
text-align: left;
background-color: #fcfcee;
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
table tr.eval_gray {
background-color: #fff;
color: #777;
}
td.eval_green {
background-color: #f5f5f5;
color: #000;
}
tr.eval_title {
}
tr.eval_title td {
border: none;
}
tr.eval_title td p {
margin-top: 2em;
text-align: left;
padding-bottom: 0em;
margin-bottom: 0.25em;
}
</style>
	<f:loadBundle basename="resources/StudentResources" var="bundle"/>	
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>

	<h:outputText value="<h2>#{bundle['link.exams.enrolment']}</h2>" escape="false"
	  rendered="#{manageEvaluationsForStudent.evaluationTypeString == 'net.sourceforge.fenixedu.domain.Exam'}"/>
	<h:outputText value="<h2>#{bundle['link.writtenTests.enrolment']}</h2>" escape="false"
	  rendered="#{manageEvaluationsForStudent.evaluationTypeString == 'net.sourceforge.fenixedu.domain.WrittenTest'}"/>
	<h:form>
		<h:inputHidden binding="#{manageEvaluationsForStudent.evaluationTypeHidden}"/>
		<h:outputText value="<div class='evalcontainer'>	" escape="false"/>
		<h:panelGrid columns="2" columnClasses="alignright,," styleClass="search">
			<h:outputText value="#{bundle['label.student.enrollment.executionPeriod']}: " styleClass="boldFontClass" />
			<fc:selectOneMenu value="#{manageEvaluationsForStudent.executionPeriodID}" onchange="this.form.submit();" 
			   valueChangeListener="#{manageEvaluationsForStudent.changeExecutionPeriod}">
				<f:selectItems value="#{manageEvaluationsForStudent.executionPeriodsLabels}" />
			</fc:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
		</h:panelGrid>
		<h:outputText value="</div>	" escape="false"/>
		
		<%-- Evaluations Table --%>
		<h:outputText value="<table class='evallist'>" escape="false"/>
		<h:outputText value="<tr class='eval_title'><td colspan='6'><p><strong>#{bundle['label.evaluationsWithEnrolmentPeriodOpened']}:</strong></p></td></tr>" escape="false"/>	
		
		<h:outputText value="<tr><th>#{bundle['label.identification']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.evaluationDate']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.lesson.room']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.enrolmentPeriodNoBR']}</th>" escape="false" />
		<h:outputText value="<th></th>" escape="false" />
		<h:outputText value="<th></th></tr>" escape="false" />

		<h:outputText value="<tr><td colspan='6' class='evallist_empty'></td></tr>" escape="false"/>		
		<h:panelGroup rendered="#{empty manageEvaluationsForStudent.evaluationsWithEnrolmentPeriodOpened}">
			<h:outputText value="<tr><td colspan='6'>(#{bundle['message.no.evaluations.to.enroll']})</td></tr>" escape="false" />
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty manageEvaluationsForStudent.evaluationsWithEnrolmentPeriodOpened}">
			<fc:dataRepeater value="#{manageEvaluationsForStudent.evaluationsWithEnrolmentPeriodOpened}" var="evaluation">
			
				<h:outputText value="<tr><td colspan='6' class='title'>" escape="false" />
				<fc:dataRepeater value="#{manageEvaluationsForStudent.executionCourses[evaluation.idInternal]}" var="executionCourse">
					<h:outputText value="#{executionCourse.nome}" escape="false" />
				</fc:dataRepeater>
				<h:outputText value="</td></tr>" escape="false" />
				
				<h:outputText value="<tr class='eval_gray'>" escape="false"/>
				<h:outputText value="<td>#{evaluation.season}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'Exam'}"/>
				<h:outputText value="<td>#{evaluation.description}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'WrittenTest'}"/>

				<h:outputText value="<td>" escape="false" />
				<h:outputFormat value="{0, date, dd/MM/yyyy}<br/>" escape="false">
					<f:param value="#{evaluation.dayDate}" />
				</h:outputFormat>
				<h:outputFormat value="{0, date, HH:mm}" >
					<f:param value="#{evaluation.beginningDate}" />
				</h:outputFormat>
				<h:outputText value=" #{bundle['label.to']} " escape="false" />
				<h:outputFormat value="{0, date, HH:mm}" >
					<f:param value="#{evaluation.endDate}" />
				</h:outputFormat>
				<h:outputText value="</td>" escape="false" />
				
				<h:outputText value="<td>-</td>" escape="false" />

				<h:outputText value="<td>" escape="false" />
				<h:outputText value=" #{bundle['label.lesson.beginning']}: " escape="false" />
				<h:panelGroup rendered="#{!empty evaluation.enrollmentBeginDayDate}">
					<h:outputFormat value="{0, date, dd/MM/yyyy} - " escape="false">
						<f:param value="#{evaluation.enrollmentBeginDayDate}" />
					</h:outputFormat>
					<h:outputFormat value="{0, date, HH:mm}<br/>" escape="false" >
						<f:param value="#{evaluation.enrollmentBeginTimeDate}" />
					</h:outputFormat>
				</h:panelGroup>
				<h:outputText value=" #{bundle['label.lesson.end']}: " escape="false" />
				<h:panelGroup rendered="#{!empty evaluation.enrollmentEndDayDate}">
					<h:outputFormat value="{0, date, dd/MM/yyyy} - " escape="false">
						<f:param value="#{evaluation.enrollmentEndDayDate}" />
					</h:outputFormat>				
					<h:outputFormat value="{0, date, HH:mm}" >
						<f:param value="#{evaluation.enrollmentEndTimeDate}" />
					</h:outputFormat>
				</h:panelGroup>
				<h:outputText value="</td>" escape="false" />
				
				<h:panelGroup rendered="#{manageEvaluationsForStudent.enroledEvaluationsForStudent[evaluation.idInternal]}">
					<h:outputText value="<td class='eval_green'>#{bundle['label.enroled']}</td>" escape="false"/>
					<h:outputText value="<td>" escape="false"/>
					<fc:commandLink value="#{bundle['label.unEnroll']}" action="#{manageEvaluationsForStudent.unenrolStudent}">
						<f:param id="evaluationIDToUnenrol" name="evaluationID" value="#{evaluation.idInternal}"/> 
					</fc:commandLink>
					<h:outputText value="</td>" escape="false"/>
				</h:panelGroup>
 				<h:panelGroup rendered="#{!manageEvaluationsForStudent.enroledEvaluationsForStudent[evaluation.idInternal]}">
					<h:outputText value="<td class='eval_green'>#{bundle['label.notEnroled']}</td>" escape="false"/>
					<h:outputText value="<td>" escape="false"/>
					<fc:commandLink value="#{bundle['label.enroll']}" action="#{manageEvaluationsForStudent.enrolStudent}">
						<f:param id="evaluationIDToEnrol" name="evaluationID" value="#{evaluation.idInternal}"/> 
					</fc:commandLink>
					<h:outputText value="</td>" escape="false"/>					
				</h:panelGroup>
				<h:outputText value="</tr>" escape="false"/>
			</fc:dataRepeater>
		</h:panelGroup>

		<%-- Evaluations Table --%>
		<h:outputText value="<tr class='eval_title'><td colspan='6'><p><strong>#{bundle['label.evaluationsWithEnrolmentPeriodClosed']}:</strong></p></td></tr>" escape="false"/>				
		<h:outputText value="<tr><th>#{bundle['label.identification']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.evaluationDate']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.lesson.room']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.enrolmentPeriodNoBR']}</th>" escape="false" />
		<h:outputText value="<th></th>" escape="false" />
		<h:outputText value="<th></th></tr>" escape="false" />
		
		<h:outputText value="<tr><td colspan='6' class='evallist_empty'></td></tr>" escape="false"/>
		<h:panelGroup rendered="#{empty manageEvaluationsForStudent.evaluationsWithEnrolmentPeriodClosed}">
			<h:outputText value="<tr><td colspan='6'>(#{bundle['message.no.evaluations.with.enrolmentPeriodClosed']})</td></tr>" escape="false" />
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty manageEvaluationsForStudent.evaluationsWithEnrolmentPeriodClosed}">
			<fc:dataRepeater value="#{manageEvaluationsForStudent.evaluationsWithEnrolmentPeriodClosed}" var="evaluation">
			
				<h:outputText value="<tr><td colspan='6' class='title'>" escape="false" />
				<fc:dataRepeater value="#{manageEvaluationsForStudent.executionCourses[evaluation.idInternal]}" var="executionCourse">
					<h:outputText value="#{executionCourse.nome}" escape="false" />
				</fc:dataRepeater>
				<h:outputText value="</td></tr>" escape="false" />
				
				<h:outputText value="<tr class='eval_gray'>" escape="false"/>
				<h:outputText value="<td>#{evaluation.season}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'Exam'}"/>
				<h:outputText value="<td>#{evaluation.description}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'WrittenTest'}"/>

				<h:outputText value="<td>" escape="false" />
				<h:outputFormat value="{0, date, dd/MM/yyyy}<br/>" escape="false">
					<f:param value="#{evaluation.dayDate}" />
				</h:outputFormat>
				<h:outputFormat value="{0, date, HH:mm}" >
					<f:param value="#{evaluation.beginningDate}" />
				</h:outputFormat>
				<h:outputText value=" #{bundle['label.to']} " escape="false" />
				<h:outputFormat value="{0, date, HH:mm}" >
					<f:param value="#{evaluation.endDate}" />
				</h:outputFormat>
				<h:outputText value="</td>" escape="false" />

				<h:outputText value="<td>#{manageEvaluationsForStudent.studentRooms[evaluation.idInternal]}</td>" escape="false"/>

				<h:outputText value="<td>" escape="false" />				
				<h:outputText value=" #{bundle['label.lesson.beginning']}: " escape="false" />
				<h:panelGroup rendered="#{!empty evaluation.enrollmentBeginDayDate}">
					<h:outputFormat value="{0, date, dd/MM/yyyy} - " escape="false">
						<f:param value="#{evaluation.enrollmentBeginDayDate}" />
					</h:outputFormat>
					<h:outputFormat value="{0, date, HH:mm}<br/>" escape="false" >
						<f:param value="#{evaluation.enrollmentBeginTimeDate}" />
					</h:outputFormat>
				</h:panelGroup>
				<h:outputText value=" #{bundle['label.lesson.end']}: " escape="false" />
				<h:panelGroup rendered="#{!empty evaluation.enrollmentEndDayDate}">
					<h:outputFormat value="{0, date, dd/MM/yyyy} - " escape="false">
						<f:param value="#{evaluation.enrollmentEndDayDate}" />
					</h:outputFormat>				
					<h:outputFormat value="{0, date, HH:mm}" >
						<f:param value="#{evaluation.enrollmentEndTimeDate}" />
					</h:outputFormat>
				</h:panelGroup>
				<h:outputText value="</td>" escape="false" />
				
				<h:outputText value="<td class='eval_green'>#{bundle['label.enroled']}</td>" escape="false"
				  rendered="#{manageEvaluationsForStudent.enroledEvaluationsForStudent[evaluation.idInternal]}"/>
				<h:outputText value="<td class='eval_green'>#{bundle['label.notEnroled']}</td>" escape="false"
				  rendered="#{!manageEvaluationsForStudent.enroledEvaluationsForStudent[evaluation.idInternal]}"/>
				
				<h:outputText value="</tr>" escape="false"/>
			</fc:dataRepeater>
		</h:panelGroup>	
		
		<%-- Evaluations Table --%>
		<h:outputText value="<tr class='eval_title'><td colspan='6'><p><strong>#{bundle['label.evaluationsWithoutEnrolmentPeriod']}:</strong></p></td></tr>" escape="false"/>		
		<h:outputText value="<tr><th>#{bundle['label.identification']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.evaluationDate']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.lesson.room']}</th>" escape="false" />
		<h:outputText value="<th></th>" escape="false" />
		<h:outputText value="<th></th>" escape="false" />
		<h:outputText value="<th></th></tr>" escape="false" />
		
		<h:outputText value="<tr><td colspan='6' class='evallist_empty'></td></tr>" escape="false"/>
		<h:panelGroup rendered="#{empty manageEvaluationsForStudent.evaluationsWithoutEnrolmentPeriod}">
			<h:outputText value="<tr><td colspan='6'>(#{bundle['message.no.evaluations.without.enrolment.period']})</td></tr>" escape="false" />
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty manageEvaluationsForStudent.evaluationsWithoutEnrolmentPeriod}">
			<fc:dataRepeater value="#{manageEvaluationsForStudent.evaluationsWithoutEnrolmentPeriod}" var="evaluation">

				<h:outputText value="<tr><td colspan='6' class='title'>" escape="false" />
				<fc:dataRepeater value="#{manageEvaluationsForStudent.executionCourses[evaluation.idInternal]}" var="executionCourse">
					<h:outputText value="#{executionCourse.nome}" escape="false" />
				</fc:dataRepeater>
				<h:outputText value="</td></tr>" escape="false" />
				
				<h:outputText value="<tr class='eval_gray'>" escape="false"/>
				<h:outputText value="<td>#{evaluation.season}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'Exam'}"/>
				<h:outputText value="<td>#{evaluation.description}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'WrittenTest'}"/>

				<h:outputText value="<td>" escape="false" />
				<h:outputFormat value="{0, date, dd/MM/yyyy}<br/>" escape="false">
					<f:param value="#{evaluation.dayDate}" />
				</h:outputFormat>
				<h:outputFormat value="{0, date, HH:mm}" >
					<f:param value="#{evaluation.beginningDate}" />
				</h:outputFormat>
				<h:outputText value=" #{bundle['label.to']} " escape="false" />
				<h:outputFormat value="{0, date, HH:mm}" >
					<f:param value="#{evaluation.endDate}" />
				</h:outputFormat>
				<h:outputText value="</td>" escape="false" />

				<h:outputText value="<td>#{manageEvaluationsForStudent.studentRooms[evaluation.idInternal]}</td>" escape="false"/>

				<h:outputText value="<td></td>" escape="false" />
				<h:outputText value="<td></td>" escape="false" />				
				<h:outputText value="<td></td></tr>" escape="false" />
			</fc:dataRepeater>
		</h:panelGroup>
		<h:outputText value="</table>" escape="false"/>
	</h:form>	
</ft:tilesView>