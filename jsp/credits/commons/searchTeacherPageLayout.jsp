<%@page contentType="text/html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="org.apache.struts.Globals" %>

<tiles:useAttribute id="searchTitle" name="searchTitle"/>

<em><bean:message key="label.teacherService.credits"/></em>
<h2><bean:message name="searchTitle"/></h2>

<%--<h3><bean:message name="searchTitle"/></h3>--%>
<%--<h2><bean:message key="label.teacherService.credits.resume"/></h2>--%>

<tiles:useAttribute id="searchInfo" name="searchInfo"/>
<p class="infoop">
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
<html:form action="<%= path %>" focus="teacherNumber">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method"/>
	<input alt="input.page" type="hidden" name="page" value="0"/>
	<bean:message key="label.teacher.number"/>: <html:text bundle="HTMLALT_RESOURCES" altKey="text.teacherNumber" property="teacherNumber"	size="6" maxlength="7"/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.ok"/>
	</html:submit>
</html:form>