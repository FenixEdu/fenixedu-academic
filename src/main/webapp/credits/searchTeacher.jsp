<%@page contentType="text/html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:form action="/teacherSearch" focus="teacherId">
	<input alt="input.method" type="hidden" name="method" value="searchByNumber"/>
	<input alt="input.page" type="hidden" name="page" value="1"/>
	<p class="infoop">
		<bean:message key="message.edit.teacher.credits"/>
	</p>
	<bean:message key="label.teacher.number"/> <html:text bundle="HTMLALT_RESOURCES" altKey="text.teacherId" property="teacherId"	/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Ok"/>
</html:form>