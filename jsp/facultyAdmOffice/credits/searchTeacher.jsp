<%@page contentType="text/html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<p class="infoop">
	<bean:message key="message.edit.teacher.credits"/>
</p>
<span class="error"><html:errors /></span>
<html:form action="/teacherSearch">
	<input type="hidden" name="method" value="doSearch"/>
	<input type="hidden" name="page" value="1"/>
	<bean:message key="label.teacher.number"/> <html:text property="teacherNumber"	/>
	<html:submit>
		<bean:message key="button.ok"/>
	</html:submit>
</html:form>