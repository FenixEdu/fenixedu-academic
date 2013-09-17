<%@page contentType="text/html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ page import="org.apache.struts.Globals" %>

<tiles:useAttribute id="searchTitle" name="searchTitle"/>

<em><bean:message key="label.teacherService.credits"/></em>
<h2><bean:message name="searchTitle"/></h2>

<%--<h3><bean:message name="searchTitle"/></h3>--%>
<%--<h2><bean:message key="label.teacherService.credits.resume"/></h2>--%>

<tiles:useAttribute id="searchInfo" name="searchInfo"/>
<p class="infoop2">
	<bean:message name="searchInfo"/>
</p>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<logic:present name="teacherNotFound">
<p>
<span class="error"><!-- Error messages go here -->
<bean:message key="message.indicates.error"/>:
	<ul>
		<li><bean:message key="message.teacher.not-found-or-not-belong-to-department"/></li>
	</ul>
</span>
</p>
</logic:present>

<%--
<p class="mbottom05"><strong><bean:message name="searchTitle"/>:</strong></p>
--%>

<bean:define id="path" name="<%= Globals.MAPPING_KEY %>" property="path" type="java.lang.String"/>
<html:form action="<%= path %>" focus="teacherId">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method"/>
	<input alt="input.page" type="hidden" name="page" value="0"/>
	<table class="tstyle5 thlight thright thmiddle mvert05">
		<tr>
			<th><bean:message key="label.teacher.id"/>:</th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.teacherId" property="teacherId"	size="6" /></td>
		</tr>
	</table>
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.ok"/>
		</html:submit>
	</p>
</html:form>