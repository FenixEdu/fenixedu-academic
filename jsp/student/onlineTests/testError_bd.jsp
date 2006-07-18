<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>	
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<logic:present name="cantDoTest">
	<h2><bean:message key="error.test.cantDoTest"/></h2>
</logic:present>
<logic:present name="cantShowTestCorrection">
	<h2><bean:message key="error.test.cantShowTestCorrection"/></h2>
</logic:present>
<logic:present name="invalidTest">
	<h2><bean:message key="error.test.invalidTest"/></h2>
</logic:present>


<html:form action="/studentTests">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="viewStudentExecutionCoursesWithTests"/>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.back" styleClass="inputbutton" property="back"><bean:message key="button.back"/></html:submit>
</html:form>
