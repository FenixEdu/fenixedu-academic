<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<style>@import url(<%= request.getContextPath() %>/CSS/navlateralnew.css);</style> <!-- Import new CSS for this section: #navlateral  -->

<ul>
	<li class="navheader"><bean:message key="link.student.portalTitle"/></li>
	<li><html:link href='<%= request.getContextPath() + "/dotIstPortal.do?prefix=/student&amp;page=/index.do" %>'><bean:message key="link.student.portal.home"/></html:link></li>
  	<li><html:link page="/viewCurriculum.do?method=getStudentCP" titleKey="link.title.curriculum"><bean:message key="link.student.curriculum"/></html:link></li>
	<li><html:link page="/studentTimeTable.do" target="_blank" titleKey="link.title.timetable"><bean:message key="link.my.timetable"/></html:link></li>
	<li><html:link page="/studentTests.do?method=viewStudentExecutionCoursesWithTests" ><bean:message key="link.tests"/></html:link></li>
	<li><html:link page="/studentGaugingTestResults.do" titleKey="link.title.results.test"><bean:message key="link.results.test"/></html:link></li>
	<li><html:link page="/fillInquiries.do?method=prepareCourses&amp;page=0" titleKey="link.title.inquiry.students.courses"><bean:message key="link.inquiries" bundle="INQUIRIES_RESOURCES"/></html:link></li>  			
	<li class="navheader"><bean:message key="link.student.enrollmentTitle"/></li>
	<li><html:link page="/warningFirst.do" titleKey="link.title.student.enrollment"><bean:message key="link.student.enrollment"/></html:link></li>
	<li><html:link page="/studentShiftEnrollmentManager.do?method=prepareStartViewWarning" titleKey="link.title.shift.enrolment"><bean:message key="link.shift.enrolment"/></html:link></li>
	<li><html:link page="/viewEnroledExecutionCourses.do" titleKey="link.title.groupEnrolment"><bean:message key="link.groupEnrolment" /></html:link></li>
	<li><html:link page="/examEnrollmentManager.do?method=viewExamsToEnroll" titleKey="link.title.exams.enrolment" ><bean:message key="link.exams.enrolment"/></html:link></li>
	<li><html:link page="/listAllSeminaries.do" titleKey="link.title.seminaries.enrolment" ><bean:message key="link.seminaries.enrolment"/></html:link>	<a href="<bean:message key="link.seminaries.rules"/>" title="<bean:message key="link.title.seminaries.rules"/>" target="_blank"><bean:message key="label.seminairies.seeRules"/></a></li> 
	<li class="navheader"><bean:message key="link.student.seniorTitle"/></li>
	<li><html:link page="/seniorInformation.do?method=prepareEdit&amp;page=0" ><bean:message key="link.senior.info"/></html:link></li>			  	
	<li><acronym title="<bean:message key="link.title.finalWorkTitle"/>"><bean:message key="link.student.finalWorkTitle"/></acronym></li>
	<li class="sub">
		<ul>
		<li><html:link target="_blank" href='<%= request.getContextPath() + "/publico/viewFinalDegreeWorkProposals.do" %>'><bean:message key="link.finalDegreeWork.proposal.listings"/></html:link></li>
		<li><html:link page="/finalDegreeWorkCandidacy.do?method=prepareCandidacy&amp;page=0"><bean:message key="link.finalDegreeWork.candidacy"/></html:link></li>
		<li><html:link page="/finalDegreeWorkAttribution.do?method=prepare&amp;page=0"><bean:message key="link.finalDegreeWork.confirmAttribution"/></html:link></li>
		</ul>
	</li>
</ul>




<!--

<script language="JavaScript" type="text/javascript" src="<%= request.getContextPath() %>/javaScript/navlateralsub.js"></script>

	<li><html:link target="_blank" href='<%= request.getContextPath() + "/publico/viewFinalDegreeWorkProposals.do" %>'><bean:message key="link.finalDegreeWork.proposal.listings"/></html:link></li>
	<li><html:link page="/finalDegreeWorkCandidacy.do?method=prepareCandidacy&amp;page=0"><bean:message key="link.finalDegreeWork.candidacy"/></html:link></li>
	<li><html:link page="/finalDegreeWorkAttribution.do?method=prepare&amp;page=0"><bean:message key="link.finalDegreeWork.confirmAttribution"/></html:link></li>




	<li class="sub">
		<li><html:link target="_blank" href='<%= request.getContextPath() + "/publico/viewFinalDegreeWorkProposals.do" %>'><bean:message key="link.finalDegreeWork.proposal.listings"/></html:link></li>
		<li><html:link page="/finalDegreeWorkCandidacy.do?method=prepareCandidacy&amp;page=0"><bean:message key="link.finalDegreeWork.candidacy"/></html:link></li>
		<li><html:link page="/finalDegreeWorkAttribution.do?method=prepare&amp;page=0"><bean:message key="link.finalDegreeWork.confirmAttribution"/></html:link></li>
	</li>
	
<html:link page="" titleKey=""><bean:message key=""/></html:link>
	<ul>
		<li class="navheader">Portal do Estudante</li>
		<li><a href="">Home</a></li>
	  	<li><a href="/fenix/student/viewCurriculum.do?method=getStudentCP" title="Currículo do Aluno">Curriculo</a></li>
		<li><a href="/fenix/student/studentTimeTable.do" target="_blank" title="Horário do Aluno">Horário</a></li>
	  	<li><a href="/fenix/student/studentTests.do?method=viewStudentExecutionCoursesWithTests">Fichas de Trabalho</a></li>
		<li><a href="/fenix/student/fillInquiries.do?method=prepareCourses&amp;page=0" title="Avalia&ccedil;&atilde;o do Funcionamento das Disciplinas">Inqu&eacute;ritos</a></li>
		<li><a href="" title="Resultados da Prova de Aferição de Física">Prova de Aferição de Física</a></li>
		<li class="navheader">Inscrições</li>
		<li><a href="/fenix/student/warningFirst.do" title="Inscrição em Disciplinas">Disciplinas</a></li>
		<li><a href="/fenix/student/studentShiftEnrollmentManager.do?method=prepareStartViewWarning" title="Reserva de Turmas">Turmas</a></li>
		<li><a href="/fenix/student/viewEnroledExecutionCourses.do" title="Inscrição em Grupos">Grupos</a></li>
		<li><a href="/fenix/student/examEnrollmentManager.do?method=viewExamsToEnroll" title="Inscrição em Exames">Exames</a></li>
		<li><a href="/fenix/student/listAllSeminaries.do" title="Candidatura a Seminários">Seminários</a> <a href='http://seminarios.ist.utl.pt/' target="_blank">(regulamento)</a></li>
		<li class="navheader">Finalistas</li>
		<li class="minus"><a href="index01plus.html" title="Trabalho Final de Curso">TFC</a></li>
			<li class="sub">
			<ul>
			<li><a href="">Propostas</a></li>
			<li><a href="">Candidatura</a></li>
			<li><a href="">Confirmar Atribuição</a></li>
			</ul>
			</li>
	  	<li><a href="/fenix/student/seniorInformation.do?method=prepareEdit&amp;page=0">Ficha de Finalista</a></li>
	</ul>
	
-->