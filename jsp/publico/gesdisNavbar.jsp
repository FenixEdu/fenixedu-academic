<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:link page="/viewSite.do">
	<li><bean:message key="link.home"/></li>
</html:link>
<logic:present name="<%=SessionConstants.INFO_SITE%>" property="alternativeSite" >
	<html:link page="/accessAlternativeSite.do">
		<li><bean:message key="link.alternative"/></li>
	</html:link>
</logic:present>
<html:link  page="/accessAnnouncements.do">
	<li><bean:message key="link.announcements"/></li>
</html:link>
<html:link page="/accessObjectives.do?method=acessObjectives">
	<li><bean:message key="link.objectives"/></li>
</html:link>
<html:link page="/accessProgram.do?method=acessProgram">
	<li><bean:message key="link.program"/></li>
</html:link>
<html:link page="/accessBibliographicReferences.do?method=viewBibliographicReference">
	<li><bean:message key="link.bibliography"/></li>
</html:link>
	<html:link page="/accessTeachers.do">
	<li><bean:message key="link.teachers"/></li>
</html:link>
<html:link page="/viewTimeTable.do">
		<li><bean:message key="link.executionCourse.timeTable"/></li>
</html:link>
<html:link page="/viewExecutionCourseShifts.do">
		<li><bean:message key="link.executionCourse.shifts"/></li>
</html:link>

<h3><bean:message key="label.contacts" /></h3>
<logic:present name="<%=SessionConstants.INFO_SITE%>" property="mail" >
<bean:define id="mail" name="<%=SessionConstants.INFO_SITE%>" property="mail"/>
<html:link href="<%= "mailto:" + pageContext.findAttribute("mail") %>">
<bean:write name="mail" />		
</html:link>
</logic:present>
<logic:iterate id="responsible" name="<%=SessionConstants.RESPONSIBLE_TEACHERS_LIST %>" />
<p class="contacto"><bean:message key="label.prof"/> <bean:write name="responsible" property="infoPerson.nome"/><p/>
<p class="contacto"><bean:write name="responsible" property="infoPerson.email"/><p/>
<p class="contacto"><bean:write name="responsible" property="infoPerson.enderecoWeb"/><p/>	
</logic:iterate>	

