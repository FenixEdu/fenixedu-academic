<%@page contentType="text/html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<p class="infoop">
	<bean:message key="label.search.for.creditsSheet"/>
</p>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:form action="/teacherSearchForTeacherCreditsSheet" focus="teacherId">
	<input alt="input.method" type="hidden" name="method" value="doSearch"/>
	<input alt="input.page" type="hidden" name="page" value="1"/>
	<bean:message key="label.teacher.number"/> <html:text bundle="HTMLALT_RESOURCES" altKey="text.teacherId" property="teacherId"	/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.ok"/>
	</html:submit>
</html:form>