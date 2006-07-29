<%@page contentType="text/html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
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
<html:form action="<%= path %>" focus="teacherNumber">
	<input alt="input.method" type="hidden" name="method" value="doSearch"/>
	<input alt="input.page" type="hidden" name="page" value="1"/>
	<bean:message key="label.teacher.number"/> <html:text bundle="HTMLALT_RESOURCES" altKey="text.teacherNumber" property="teacherNumber"	/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.ok"/>
	</html:submit>
</html:form>