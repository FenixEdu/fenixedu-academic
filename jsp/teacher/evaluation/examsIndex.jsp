<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<style>
ul.links {
list-style: none;
padding-left: 2em;
}
ul.links li {
padding-top: 0;
padding-bottom: 0;
margin-top: 0;
margin-bottom: 0;
}
</style>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ApplicationResources" var="bundle"/>
	
	<h:form>
	
	<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
	
	<h:outputText value="<h2>#{bundle['title.evaluation.Exam']}</h2>" escape="false" />
	<h:dataTable value="#{evaluationManagementBackingBean.examList}" var="exam">
		<h:column>
			<h:outputText value="<b>#{bundle['label.exam']}:</b> " escape="false"/>
			<h:outputText value="#{exam.season.season}, "/>
			<h:outputText value="#{bundle['label.day']}" />
			<h:outputFormat value="{0, date, MM/dd/yyyy}">
				<f:param value="#{exam.dayDate}"/>
			</h:outputFormat>
			<h:outputText value=" #{bundle['label.at']}" />
			<h:outputFormat value="{0, date, HH:mm}">
				<f:param value="#{exam.beginningDate}"/>
			</h:outputFormat>

			<h:outputText value="<br/><ul class=\"links\"><li>" escape="false"/>
			<h:commandLink action="enterEditExamEnrolmentPeriod">
				<f:param name="evaluationID" value="#{exam.idInternal}" />
				<h:outputFormat value="#{bundle['link.evaluation.enrollment.period']}">
					<f:param value="#{bundle['label.exam']}" />
				</h:outputFormat>
			</h:commandLink>
	
			<h:outputText value="<b> | </b>" escape="false"/>
			<h:commandLink action="enterShowStudentsEnroled">
				<f:param name="evaluationID" value="#{exam.idInternal}" />
				<h:outputFormat value="#{bundle['link.students.enrolled.inExam']}" />
			</h:commandLink>

			<h:outputText value="<b> | </b>" escape="false"/>
			<h:commandLink action="enterDistributeStudentsByRoom">
				<f:param name="evaluationID" value="#{exam.idInternal}" />
				<h:outputFormat value="#{bundle['link.students.distribution']}" />
			</h:commandLink>
			<h:outputText value="</li>" escape="false"/>
			
			<h:outputText value="<li>" escape="false"/>
			<h:commandLink action="enterShowMarksListOptions">
				<f:param name="evaluationID" value="#{exam.idInternal}" />
				<h:outputFormat value="#{bundle['label.students.listMarks']}" />
			</h:commandLink>
			<h:outputText value="</li><ul>" escape="false"/>
			<h:outputText value="<br/>" escape="false"/>
		</h:column>
	</h:dataTable>
	
	</h:form>

</ft:tilesView>
