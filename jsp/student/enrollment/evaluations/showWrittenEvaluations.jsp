<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<style>
.boldFont { 
	font-weight: bold
}
.alignright {
text-align: right;
}
</style>
<ft:tilesView definition="definition.student.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/StudentResources" var="bundle"/>	
	
	<h:outputText value="<i>#{bundle['link.evaluations.enrolment']}</i>" escape="false" />
	<h:outputText value="<h2>#{bundle['link.exams.enrolment']}</h2>" escape="false"
	  rendered="#{manageEvaluationsForStudent.evaluationTypeString == 'net.sourceforge.fenixedu.domain.Exam'}"/>
	<h:outputText value="<h2>#{bundle['link.writtenTests.enrolment']}</h2>" escape="false"
	  rendered="#{manageEvaluationsForStudent.evaluationTypeString == 'net.sourceforge.fenixedu.domain.WrittenTest'}"/>
	<h:form>
		<h:inputHidden binding="#{manageEvaluationsForStudent.evaluationTypeHidden}"/>
		<h:panelGrid columns="2" columnClasses="alignright,," styleClass="infoop">
			<h:outputText value="#{bundle['label.student.enrollment.executionPeriod']}: " styleClass="boldFontClass" />
			<fc:selectOneMenu value="#{manageEvaluationsForStudent.executionPeriodID}" onchange="this.form.submit();" 
			   valueChangeListener="#{manageEvaluationsForStudent.changeExecutionPeriod}">
				<f:selectItems value="#{manageEvaluationsForStudent.executionPeriodsLabels}" />
			</fc:selectOneMenu>
		</h:panelGrid>
		<h:outputText value="<br/>" escape="false"/>
		
		<%-- Evaluations Table --%>
		<h:outputText value="<table border='1'>" escape="false"/>
		<h:outputText value="<tr><th>#{bundle['label.identification']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.evaluationDate']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.enrolmentPeriod']}</th>" escape="false" />
		<h:outputText value="<th></th>" escape="false" />
		<h:outputText value="<th></th></tr>" escape="false" />

		<%-- Evaluations with EnrolmentPeriod Opened --%>
		<h:outputText value="<tr class='space'><td></td></tr>" escape="false"/>
		<h:outputText value="<tr class='space'><td></td></tr>" escape="false"/>
		<h:outputText value="<tr><td colspan='5' style='font-weight: bold'>#{bundle['label.evaluationsWithEnrolmentPeriodOpened']}: </td></tr>" escape="false"/>
		<h:panelGroup rendered="#{empty manageEvaluationsForStudent.evaluationsWithEnrolmentPeriodOpened}">
			<h:outputText value="<tr><td colspan='5'>(#{bundle['message.no.evaluations.to.enroll']})</td></tr>" escape="false" />
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty manageEvaluationsForStudent.evaluationsWithEnrolmentPeriodOpened}">
			<fc:dataRepeater value="#{manageEvaluationsForStudent.evaluationsWithEnrolmentPeriodOpened}" var="evaluation">
			
				<h:outputText value="<tr class='space'><td></td></tr>" escape="false"/>
				<h:outputText value="<tr><td colspan='5'>" escape="false" />
				<fc:dataRepeater value="#{manageEvaluationsForStudent.executionCourses[evaluation.idInternal]}" var="executionCourse">
					<h:outputText value="#{executionCourse.nome}" escape="false" />
				</fc:dataRepeater>
				<h:outputText value="</td></tr>" escape="false" />
				
				<h:outputText value="<tr>" escape="false"/>
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
					<h:outputText value="<td>#{bundle['label.enroled']}</td>" escape="false"/>
					<h:outputText value="<td>" escape="false"/>
					<fc:commandLink value="#{bundle['label.unEnroll']}" action="#{manageEvaluationsForStudent.unenrolStudent}">
						<f:param id="evaluationIDToUnenrol" name="evaluationID" value="#{evaluation.idInternal}"/> 
					</fc:commandLink>
					<h:outputText value="</td>" escape="false"/>
				</h:panelGroup>
 				<h:panelGroup rendered="#{!manageEvaluationsForStudent.enroledEvaluationsForStudent[evaluation.idInternal]}">
					<h:outputText value="<td>#{bundle['label.notEnroled']}</td>" escape="false"/>
					<h:outputText value="<td>" escape="false"/>
					<fc:commandLink value="#{bundle['label.enroll']}" action="#{manageEvaluationsForStudent.enrolStudent}">
						<f:param id="evaluationIDToEnrol" name="evaluationID" value="#{evaluation.idInternal}"/> 
					</fc:commandLink>
					<h:outputText value="</td>" escape="false"/>					
				</h:panelGroup>
				<h:outputText value="</tr>" escape="false"/>
			</fc:dataRepeater>
		</h:panelGroup>
		<h:outputText value="</table>" escape="false"/>
		<h:outputText value="<br/>" escape="false"/>
		
		<%-- Evaluations Table --%>
		<h:outputText value="<table border='1'>" escape="false"/>
		<h:outputText value="<tr><th>#{bundle['label.identification']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.evaluationDate']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.lesson.room']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.enrolmentPeriod']}</th>" escape="false" />
		<h:outputText value="<th></th></tr>" escape="false" />
		
		<%-- Evaluations with EnrolmentPeriod Closed --%>
		<h:outputText value="<tr class='space'><td></td></tr>" escape="false"/>
		<h:outputText value="<tr class='space'><td></td></tr>" escape="false"/>
		<h:outputText value="<tr><td colspan='5' style='font-weight: bold'>#{bundle['label.evaluationsWithEnrolmentPeriodClosed']}: </td></tr>" escape="false"/>
		<h:panelGroup rendered="#{empty manageEvaluationsForStudent.evaluationsWithEnrolmentPeriodClosed}">
			<h:outputText value="<tr><td colspan='5'>(#{bundle['message.no.evaluations.with.enrolmentPeriodClosed']})</td></tr>" escape="false" />
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty manageEvaluationsForStudent.evaluationsWithEnrolmentPeriodClosed}">
			<fc:dataRepeater value="#{manageEvaluationsForStudent.evaluationsWithEnrolmentPeriodClosed}" var="evaluation">
			
				<h:outputText value="<tr class='space'><td></td></tr>" escape="false"/>
				<h:outputText value="<tr><td colspan='5'>" escape="false" />
				<fc:dataRepeater value="#{manageEvaluationsForStudent.executionCourses[evaluation.idInternal]}" var="executionCourse">
					<h:outputText value="#{executionCourse.nome}" escape="false" />
				</fc:dataRepeater>
				<h:outputText value="</td></tr>" escape="false" />
				
				<h:outputText value="<tr>" escape="false"/>
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
				
				<h:outputText value="<td>#{bundle['label.enroled']}</td>" escape="false"
				  rendered="#{manageEvaluationsForStudent.enroledEvaluationsForStudent[evaluation.idInternal]}"/>
				<h:outputText value="<td>#{bundle['label.notEnroled']}</td>" escape="false"
				  rendered="#{!manageEvaluationsForStudent.enroledEvaluationsForStudent[evaluation.idInternal]}"/>
				
				<h:outputText value="</tr>" escape="false"/>
			</fc:dataRepeater>
		</h:panelGroup>
		<h:outputText value="</table>" escape="false"/>
		<h:outputText value="<br/>" escape="false"/>
		
		<%-- Evaluations Table --%>
		<h:outputText value="<table border='1'>" escape="false"/>
		<h:outputText value="<tr><th>#{bundle['label.identification']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.evaluationDate']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.lesson.room']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.enrolmentPeriod']}</th>" escape="false" />
		<h:outputText value="<th></th></tr>" escape="false" />
		<%-- Evaluations without EnrolmentPeriod --%>
		<h:outputText value="<tr class='space'><td></td></tr>" escape="false"/>
		<h:outputText value="<tr class='space'><td></td></tr>" escape="false"/>
		<h:outputText value="<tr><td colspan='5' style='font-weight: bold'>#{bundle['label.evaluationsWithoutEnrolmentPeriod']}: </td></tr>" escape="false"/>
		<h:panelGroup rendered="#{empty manageEvaluationsForStudent.evaluationsWithoutEnrolmentPeriod}">
			<h:outputText value="<tr><td colspan='5'>(#{bundle['message.no.evaluations.without.enrolment.period']})</td></tr>" escape="false" />
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty manageEvaluationsForStudent.evaluationsWithoutEnrolmentPeriod}">
			<fc:dataRepeater value="#{manageEvaluationsForStudent.evaluationsWithoutEnrolmentPeriod}" var="evaluation">
			
				<h:outputText value="<tr class='space'><td></td></tr>" escape="false"/>
				<h:outputText value="<tr><td colspan='5'>" escape="false" />
				<fc:dataRepeater value="#{manageEvaluationsForStudent.executionCourses[evaluation.idInternal]}" var="executionCourse">
					<h:outputText value="#{executionCourse.nome}" escape="false" />
				</fc:dataRepeater>
				<h:outputText value="</td></tr>" escape="false" />
				
				<h:outputText value="<tr>" escape="false"/>
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
				<h:outputText value="</td></tr>" escape="false" />
			</fc:dataRepeater>
		</h:panelGroup>
		<h:outputText value="</table>" escape="false"/>
	</h:form>	
</ft:tilesView>