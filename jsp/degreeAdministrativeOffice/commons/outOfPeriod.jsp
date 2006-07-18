<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html:errors/>
<center>
<br/>
<br/>
<b><bean:message key="message.enrolment.wish.to.continue"/></b>
<br/>
<br/>
<html:form action="/curricularCourseEnrolmentWithRulesManager.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="outOfPeriod"/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.yes"/></html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message key="button.no"/></html:cancel>
</html:form>
</center>
