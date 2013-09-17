<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
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

