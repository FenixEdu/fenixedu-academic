<%@page contentType="text/html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="org.apache.struts.Globals" %>

<tiles:useAttribute id="searchInfo" name="searchInfo"/>
<p class="infoop">
	<bean:message name="searchInfo"/>
</p>
<html:errors />
<bean:define id="path" name="<%= Globals.MAPPING_KEY %>" property="path" type="java.lang.String"/>
<html:form action="<%= path %>" focus="teacherNumber">
	<input type="hidden" name="method" value="doSearch"/>
	<input type="hidden" name="page" value="1"/>
	<html:hidden property="executionPeriodId"/>
	<bean:message key="label.teacher.number"/> <html:text property="teacherNumber"	/>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.ok"/>
	</html:submit>
</html:form>