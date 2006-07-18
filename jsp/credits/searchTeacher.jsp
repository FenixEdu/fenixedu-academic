<%@page contentType="text/html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<span class="error"><html:errors /></span>
<html:form action="/teacherSearch" focus="teacherNumber">
	<input alt="input.method" type="hidden" name="method" value="searchByNumber"/>
	<input alt="input.page" type="hidden" name="page" value="1"/>
	<p class="infoop">
		<bean:message key="message.edit.teacher.credits"/>
	</p>
	<bean:message key="label.teacher.number"/> <html:text bundle="HTMLALT_RESOURCES" altKey="text.teacherNumber" property="teacherNumber"	/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Ok"/>
</html:form>