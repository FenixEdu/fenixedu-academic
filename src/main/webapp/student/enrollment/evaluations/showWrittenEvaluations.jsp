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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<fp:select actionClass="net.sourceforge.fenixedu.presentationTier.Action.student.StudentApplication$ShowStudentEvaluations" />

<f:view>
	<f:loadBundle basename="resources/StudentResources" var="bundle"/>	
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<h:outputText value="<h2>#{bundle['link.exams.enrolment']}</h2>" escape="false"
	  rendered="#{manageEvaluationsForStudent.evaluationTypeString == 'net.sourceforge.fenixedu.domain.Exam'}"/>
	<h:outputText value="<h2>#{bundle['link.writtenTests.enrolment']}</h2>" escape="false"
	  rendered="#{manageEvaluationsForStudent.evaluationTypeString == 'net.sourceforge.fenixedu.domain.WrittenTest'}"/>
	<h:outputText value="<div class='infoop2'>
	<b>Sincronizar Calendário</b><br/>
    Pode ficar a par de novidades relacionadas com testes e exames directamente no seu calendário pessoal.
    A funcionalidade \"Sincronizar Calendário\" permite-lhe sincronizar o calendário de testes e exames com o seu calendário digital.
    Para mais informações consulte a página <a href='/student/ICalTimeTable.do?method=prepare'>Sincronizar Calendário</a>.
	</div>" escape="false"/>
	  
	<h:form>
		<h:inputHidden binding="#{manageEvaluationsForStudent.evaluationTypeHidden}"/>
		<h:outputText value="<div class='evalcontainer' style='padding-bottom: 0;'>	" escape="false"/>
		<h:panelGrid columns="2" columnClasses="alignright," styleClass="search">
			<h:outputText value="#{bundle['label.student.enrollment.executionPeriod']}: " styleClass="" />
			<fc:selectOneMenu value="#{manageEvaluationsForStudent.executionPeriodID}" onchange="this.form.submit();" 
			   valueChangeListener="#{manageEvaluationsForStudent.changeExecutionPeriod}">
				<f:selectItems value="#{manageEvaluationsForStudent.executionPeriodsLabels}" />
			</fc:selectOneMenu>
			<%-- 
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
			--%>
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
				<fc:dataRepeater value="#{manageEvaluationsForStudent.executionCourses[evaluation.externalId]}" var="executionCourse">
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
				
				<h:panelGroup rendered="#{manageEvaluationsForStudent.enroledEvaluationsForStudent[evaluation.externalId]}">
					<h:outputText value="<td class='eval_green'>#{bundle['label.enroled']}</td>" escape="false"/>
					<h:outputText value="<td>" escape="false"/>
					<fc:commandLink value="#{bundle['label.unEnroll']}" action="#{manageEvaluationsForStudent.unenrolStudent}">
						<f:param id="evaluationIDToUnenrol" name="evaluationID" value="#{evaluation.externalId}"/> 
					</fc:commandLink>
					<h:outputText value="</td>" escape="false"/>
				</h:panelGroup>
 				<h:panelGroup rendered="#{!manageEvaluationsForStudent.enroledEvaluationsForStudent[evaluation.externalId]}">
					<h:outputText value="<td class='eval_green'>#{bundle['label.notEnroled']}</td>" escape="false"/>
					<h:outputText value="<td>" escape="false"/>
					<fc:commandLink value="#{bundle['label.enroll']}" action="#{manageEvaluationsForStudent.enrolStudent}">
						<f:param id="evaluationIDToEnrol" name="evaluationID" value="#{evaluation.externalId}"/> 
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
				<fc:dataRepeater value="#{manageEvaluationsForStudent.executionCourses[evaluation.externalId]}" var="executionCourse">
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

				<h:outputText value="<td>#{manageEvaluationsForStudent.studentRooms[evaluation.externalId]}</td>" escape="false"/>

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
				  rendered="#{manageEvaluationsForStudent.enroledEvaluationsForStudent[evaluation.externalId]}"/>
				<h:outputText value="<td class='eval_green'>#{bundle['label.notEnroled']}</td>" escape="false"
				  rendered="#{!manageEvaluationsForStudent.enroledEvaluationsForStudent[evaluation.externalId]}"/>
				
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
				<fc:dataRepeater value="#{manageEvaluationsForStudent.executionCourses[evaluation.externalId]}" var="executionCourse">
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

				<h:outputText value="<td>#{manageEvaluationsForStudent.studentRooms[evaluation.externalId]}</td>" escape="false"/>

				<h:outputText value="<td></td>" escape="false" />
				<h:outputText value="<td></td>" escape="false" />				
				<h:outputText value="<td></td></tr>" escape="false" />
			</fc:dataRepeater>
		</h:panelGroup>
		<h:outputText value="</table>" escape="false"/>
	</h:form>	
</f:view>