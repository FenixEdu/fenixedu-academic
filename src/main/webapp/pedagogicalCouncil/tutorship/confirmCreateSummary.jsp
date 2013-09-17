<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="label.tutorshipSummary" bundle="APPLICATION_RESOURCES" />: <bean:write name="createSummaryBean" property="teacher.person.name" /></h2>
<h3>
<bean:message key="label.curricular.course.semester" bundle="APPLICATION_RESOURCES" /> 
<bean:write name="createSummaryBean" property="executionSemester.semester" /> -
<bean:write name="createSummaryBean" property="executionSemester.executionYear.year" />
</h3>
<h3>
<bean:message key="label.degree.name" bundle="APPLICATION_RESOURCES" />:
<bean:write name="createSummaryBean" property="degree.sigla" />
</h3>

<div class="infoop2">
<p><bean:message key="message.tutorshipSummary.confirm" bundle="APPLICATION_RESOURCES" /></p>
<p><bean:message key="label.tutorshipSummary.dueDate" bundle="APPLICATION_RESOURCES" />
<bean:write name="createSummaryBean" property="executionSemester.tutorshipSummaryPeriod.endDate" /> 
</p>
<html:link page="/tutorshipSummary.do?method=searchTeacher">[voltar]
</html:link></p>
</div>