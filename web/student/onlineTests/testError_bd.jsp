<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>	
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<em class="invisible"><bean:message key="title.student.portalTitle"/></em>
<h2><bean:message key="link.tests"/></h2>

<logic:present name="cantDoTest">
	<h3 class="mtop15 mbottom1"><span class="warning0"><bean:message key="error.test.cantDoTest"/></span></h3>
	<logic:present name="distributedTest">
		<bean:define id="beginDate" name="distributedTest" property="beginDateTimeFormatted"/>
		<bean:define id="endDate" name="distributedTest" property="endDateTimeFormatted"/>
		<p><bean:message key="message.testDates" arg0="<%=beginDate.toString()%>" arg1="<%=endDate.toString()%>"/></p>
	</logic:present>
	<p><bean:message key="message.actualHour"/><bean:write name="date"/></p>
</logic:present>
<logic:present name="cantShowTestCorrection">
	<h3 class="mtop15 mbottom1"><span class="warning0"><bean:message key="error.test.cantShowTestCorrection"/></span></h3>
</logic:present>
<logic:present name="invalidTest">
	<h3 class="mtop15 mbottom1"><span class="warning0"><bean:message key="error.test.invalidTest"/></span></h3>
</logic:present>

<html:form action="/studentTests">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="viewStudentExecutionCoursesWithTests"/>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.back" styleClass="inputbutton" property="back"><bean:message key="button.back"/></html:submit>
</html:form>
