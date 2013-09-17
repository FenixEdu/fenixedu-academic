<%@page contentType="text/html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ page import="org.apache.struts.Globals" %>
<tiles:useAttribute id="searchInfo" name="searchInfo"/>
<p class="infoop">
	<bean:message name="searchInfo"/>
</p>

<span class="error"><!-- Error messages go here --><html:errors />
 <logic:present name="errors">
  <bean:write name="errors" filter="true" />
 </logic:present >
</span>

<bean:define id="path" name="<%= Globals.MAPPING_KEY %>" property="path" type="java.lang.String"/>
<html:form action="<%= path %>" focus="teacherId">
	<input alt="input.method" type="hidden" name="method" value="doSearch"/>
	<input alt="input.page" type="hidden" name="page" value="1"/>
	<bean:message key="label.teacher.id"/> <html:text bundle="HTMLALT_RESOURCES" altKey="text.teacherId" property="teacherId"	/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.ok"/>
	</html:submit>
</html:form>