<%@page contentType="text/html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<p class="infoop">
	<bean:message key="label.search.for.creditsSheet"/>
</p>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:form action="/teacherSearchForTeacherCreditsSheet" focus="teacherNumber">
	<input alt="input.method" type="hidden" name="method" value="doSearch"/>
	<input alt="input.page" type="hidden" name="page" value="1"/>
	<bean:message key="label.teacher.number"/> <html:text bundle="HTMLALT_RESOURCES" altKey="text.teacherNumber" property="teacherNumber"	/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.ok"/>
	</html:submit>
</html:form>