<%@page contentType="text/html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<span class="error"><html:errors /></span>
<html:form action="/teacherSearch" focus="teacherNumber">
	<input type="hidden" name="method" value="searchByNumber"/>
	<input type="hidden" name="page" value="1"/>
	<p class="infoop">
		<bean:message key="message.edit.teacher.credits"/>
	</p>
	<bean:message key="label.teacher.number"/> <html:text property="teacherNumber"	/>
	<html:submit value="Ok"/>
</html:form>