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
.space {
	padding: 1em;
}
</style>
<ft:tilesView definition="definition.student.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/StudentResources" var="bundle"/>		
	<h:outputText value="<h2>#{bundle['label.evaluations.enrolment']}</h2>" escape="false" />
	<h:outputText value="#{bundle['label.do.enrolment']}: " styleClass="boldFont" />
	<h:outputLink value="#{displayEvaluationsToEnrol.contextPath}/student/enrollment/evaluations/showWrittenEvaluations.faces?evaluationType=1">
		<h:outputText value="#{bundle['label.page']} #{bundle['link.exams.enrolment']}" />
	</h:outputLink>
	<h:outputText value=" | " />
	<h:outputLink value="#{displayEvaluationsToEnrol.contextPath}/student/enrollment/evaluations/showWrittenEvaluations.faces?evaluationType=2">
		<h:outputText value="#{bundle['label.page']} #{bundle['link.writtenTests.enrolment']}"/>
	</h:outputLink>	
	<h:outputText value="<h3>#{bundle['label.evaluations.list']}:</h3>" escape="false" />
	<h:form>
		<h:panelGrid columns="2" columnClasses="alignright,," styleClass="infoop">
			<h:outputText value="#{bundle['label.student.enrollment.executionPeriod']}: " styleClass="boldFontClass" />
			<fc:selectOneMenu value="#{displayEvaluationsToEnrol.executionPeriodID}" onchange="this.form.submit();" 
			   valueChangeListener="#{displayEvaluationsToEnrol.changeExecutionPeriod}">
				<f:selectItems value="#{displayEvaluationsToEnrol.executionPeriodsLabels}" />
			</fc:selectOneMenu>
			<h:outputText value="#{bundle['link.evaluations.enrolment']}: " styleClass="boldFontClass" />
			<fc:selectOneMenu value="#{displayEvaluationsToEnrol.evaluationType}" onchange="this.form.submit();"
			   valueChangeListener="#{displayEvaluationsToEnrol.changeEvaluationType}">
				<f:selectItems value="#{displayEvaluationsToEnrol.evaluationTypes}" />
			</fc:selectOneMenu>
		</h:panelGrid>
		<h:outputText value="<br/>" escape="false"/>		
		<%-- Evaluations Table --%>
		<h:outputText value="<table border='1'>" escape="false"/>
		<h:outputText value="<tr><th>#{bundle['label.type']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.executionCourse']}</th>" escape="false" />		
		<h:outputText value="<th>#{bundle['label.identification']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.evaluationDate']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.enrolmentPeriod']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.enrolmentPage']}</th></tr>" escape="false" />				
		<%-- Not Enroled Evaluations --%>
		<h:outputText value="<tr class='space'><td></td></tr>" escape="false"/>
		<h:outputText value="<tr class='space'><td></td></tr>" escape="false"/>
		<h:outputText value="<tr><td colspan='6' style='font-weight: bold'>#{bundle['label.notEnroledEvaluations']}: </td></tr>" escape="false"/>
		<h:panelGroup rendered="#{empty displayEvaluationsToEnrol.notEnroledEvaluations}">
			<h:outputText value="<tr><td colspan='6'>(#{bundle['message.no.evaluations.to.enroll']})</td></tr>" escape="false" />
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty displayEvaluationsToEnrol.notEnroledEvaluations}">
			<fc:dataRepeater value="#{displayEvaluationsToEnrol.notEnroledEvaluations}" var="evaluation">
				<h:outputText value="<tr><td>#{bundle['label.evaluation.shortname.exam']}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'Exam'}"/>
				<h:outputText value="<tr><td>#{bundle['label.evaluation.shortname.test']}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'WrittenTest'}"/>			
				
				<h:outputText value="<td>" escape="false" />
	 			<fc:dataRepeater value="#{displayEvaluationsToEnrol.executionCourses[evaluation.idInternal]}" var="executionCourse">
					<h:outputText value="#{executionCourse.nome}" escape="false" />
				</fc:dataRepeater>
				<h:outputText value="</td>" escape="false" />
							
				<h:outputText value="<td>#{evaluation.season}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'Exam'}"/>
				<h:outputText value="<td>#{evaluation.description}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'WrittenTest'}"/>
				
				<h:outputText value="<td>" escape="false" />
				<h:outputFormat value="{0, date, dd/MM/yyyy} " >
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
				
				<h:panelGroup rendered="#{evaluation.isInEnrolmentPeriod}">
					<h:outputText value="<td>#{bundle['label.opened']}</td>" escape="false" />
					<h:outputText value="<td>" escape="false" />
					<h:outputLink value="#{displayEvaluationsToEnrol.contextPath}/student/enrollment/evaluations/showWrittenEvaluations.faces?evaluationType=1"
					   rendered="#{evaluation.class.simpleName == 'Exam'}">
						<f:verbatim>>></f:verbatim>
					</h:outputLink>
					<h:outputLink value="#{displayEvaluationsToEnrol.contextPath}/student/enrollment/evaluations/showWrittenEvaluations.faces?evaluationType=2"
					   rendered="#{evaluation.class.simpleName == 'WrittenTest'}">
						<f:verbatim>>></f:verbatim>
					</h:outputLink>
					<h:outputText value="</td></tr>" escape="false" />					
				</h:panelGroup>
				<h:panelGroup rendered="#{!evaluation.isInEnrolmentPeriod}">
					<h:outputText value="<td>#{bundle['label.closed']}</td>" escape="false" />
					<h:outputText value="<td></td></tr>" escape="false" />				
				</h:panelGroup>		
			</fc:dataRepeater>
		</h:panelGroup>				
		<%-- Enroled Evaluations --%>
		<h:outputText value="<tr class='space'><td></td></tr>" escape="false"/>
		<h:outputText value="<tr class='space'><td></td></tr>" escape="false"/>
		<h:outputText value="<tr><td colspan='5' style='font-weight: bold'>#{bundle['label.enroledEvaluations']}: </td></tr>" escape="false"/>
		<h:panelGroup rendered="#{empty displayEvaluationsToEnrol.enroledEvaluations}">
			<h:outputText value="<tr><td colspan='6'>(#{bundle['message.no.enroled.evaluations']})</td></tr>" escape="false" />
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty displayEvaluationsToEnrol.enroledEvaluations}">
			<fc:dataRepeater value="#{displayEvaluationsToEnrol.enroledEvaluations}" var="evaluation">
				<h:outputText value="<tr><td>#{bundle['label.evaluation.shortname.exam']}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'Exam'}"/>
				<h:outputText value="<tr><td>#{bundle['label.evaluation.shortname.test']}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'WrittenTest'}"/>			
				
				<h:outputText value="<td>" escape="false" />
	 			<fc:dataRepeater value="#{displayEvaluationsToEnrol.executionCourses[evaluation.idInternal]}" var="executionCourse">
					<h:outputText value="#{executionCourse.nome}" escape="false" />
				</fc:dataRepeater>
				<h:outputText value="</td>" escape="false" />
							
				<h:outputText value="<td>#{evaluation.season}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'Exam'}"/>
				<h:outputText value="<td>#{evaluation.description}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'WrittenTest'}"/>
				
				<h:outputText value="<td>" escape="false" />
				<h:outputFormat value="{0, date, dd/MM/yyyy} " >
					<f:param value="#{evaluation.dayDate}" />
				</h:outputFormat>
				<h:outputFormat value="{0, date, HH:mm}" >
					<f:param value="#{evaluation.beginningDate}" />
				</h:outputFormat>
				<h:outputText value=" #{bundle['label.to']} "/>
				<h:outputFormat value="{0, date, HH:mm}" >
					<f:param value="#{evaluation.endDate}" />
				</h:outputFormat>
				<h:outputText value="</td>" escape="false" />
				
				<h:panelGroup rendered="#{evaluation.isInEnrolmentPeriod}">
					<h:outputText value="<td>#{bundle['label.opened']}</td>" escape="false" />
					<h:outputText value="<td>" escape="false" />
					<h:outputLink value="#{displayEvaluationsToEnrol.contextPath}/student/enrollment/evaluations/showWrittenEvaluations.faces?evaluationType=1"
					   rendered="#{evaluation.class.simpleName == 'Exam'}">
						<f:verbatim>>></f:verbatim>
					</h:outputLink>
					<h:outputLink value="#{displayEvaluationsToEnrol.contextPath}/student/enrollment/evaluations/showWrittenEvaluations.faces?evaluationType=2"
					   rendered="#{evaluation.class.simpleName == 'WrittenTest'}">
						<f:verbatim>>></f:verbatim>
					</h:outputLink>		
					<h:outputText value="</td></tr>" escape="false" />					
				</h:panelGroup>
				<h:panelGroup rendered="#{!evaluation.isInEnrolmentPeriod}">
					<h:outputText value="<td>#{bundle['label.closed']}</td>" escape="false" />
					<h:outputText value="<td></td></tr>" escape="false" />				
				</h:panelGroup>		
			</fc:dataRepeater>
		</h:panelGroup>		
		<%-- Evaluations Without Enrolment Period --%>
		<h:outputText value="<tr class='space'><td></td></tr>" escape="false"/>
		<h:outputText value="<tr class='space'><td></td></tr>" escape="false"/>
		<h:outputText value="<tr><td colspan='5' style='font-weight: bold'>#{bundle['label.evaluationsWithoutEnrolmentPeriod']}:* </td></tr>" escape="false"/>
		<h:panelGroup rendered="#{empty displayEvaluationsToEnrol.evaluationsWithoutEnrolmentPeriod}">
			<h:outputText value="<tr><td colspan='6'>(#{bundle['message.no.evaluations.without.enrolment.period']})</td></tr>" escape="false" />
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty displayEvaluationsToEnrol.evaluationsWithoutEnrolmentPeriod}">
			<fc:dataRepeater value="#{displayEvaluationsToEnrol.evaluationsWithoutEnrolmentPeriod}" var="evaluation">
				<h:outputText value="<tr><td>#{bundle['label.evaluation.shortname.exam']}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'Exam'}"/>
				<h:outputText value="<tr><td>#{bundle['label.evaluation.shortname.test']}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'WrittenTest'}"/>			
				
				<h:outputText value="<td>" escape="false" />
	 			<fc:dataRepeater value="#{displayEvaluationsToEnrol.executionCourses[evaluation.idInternal]}" var="executionCourse">
					<h:outputText value="#{executionCourse.nome}" escape="false" />
				</fc:dataRepeater>
				<h:outputText value="</td>" escape="false" />
							
				<h:outputText value="<td>#{evaluation.season}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'Exam'}"/>
				<h:outputText value="<td>#{evaluation.description}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'WrittenTest'}"/>
				
				<h:outputText value="<td>" escape="false" />
				<h:outputFormat value="{0, date, dd/MM/yyyy} " >
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
				<h:outputText value="<td></td></tr>" escape="false" />							
			</fc:dataRepeater>
		</h:panelGroup>		
		<h:outputText value="</table>" escape="false"/>
		<h:outputText value="* #{bundle['label.evaluationNote']}<br/><br/>" escape="false"/>	
		<h:outputText value="#{bundle['label.do.enrolment']}: " styleClass="boldFont" />
		<h:outputLink value="#{displayEvaluationsToEnrol.contextPath}/student/enrollment/evaluations/showWrittenEvaluations.faces?evaluationType=1">
			<h:outputText value="#{bundle['label.page']} #{bundle['link.exams.enrolment']}" />
		</h:outputLink>
		<h:outputText value=" | " />
		<h:outputLink value="#{displayEvaluationsToEnrol.contextPath}/student/enrollment/evaluations/showWrittenEvaluations.faces?evaluationType=2">
			<h:outputText value="#{bundle['label.page']} #{bundle['link.writtenTests.enrolment']}"/>
		</h:outputLink>
	</h:form>
</ft:tilesView>