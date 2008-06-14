<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:useAttribute id="property" name="property"/>
<tiles:useAttribute id="enumList" name="enumList" />

	
<html:select alt="<%= property.toString() %>" property="<%= property.toString() %>">
	<html:option value="">---------</html:option>
	<logic:iterate id="enumVar" name="enumList">
		<bean:define id="value" name="enumVar" property="value"/>
		<bean:define id="key" name="enumVar" property="name"/>
		<html:option bundle="ENUMERATION_RESOURCES" key="<%= key.toString() %>" value="<%= value.toString() %>" />
	</logic:iterate>
</html:select>

