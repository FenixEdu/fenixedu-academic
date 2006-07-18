<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<span class="error"><html:errors /></span>

<bean:define id="sectionsList" name="infoWebSite" property="sections" />
<logic:notEmpty name="sectionsList" >
	<logic:iterate id="section" name="sectionsList">
		<logic:equal name="section" property="idInternal" value="<%=pageContext.findAttribute("objectCode").toString()%>">
			<h2><bean:message key="label.management"/>&nbsp;<bean:write name="section" property="name"/></h2>
			<br />
			<ul>
				<li>
					<html:link page="<%="/itemsManagement.do?method=prepareAddItem&amp;objectCode=" + pageContext.findAttribute("objectCode") %>">
						<bean:message key="label.add"/>&nbsp;
						<bean:write name="section" property="name"/>
					</html:link>
				</li>
				<li>
					<html:link page="<%="/listItemsManagement.do?method=listItems&amp;objectCode=" + pageContext.findAttribute("objectCode") %>">
						<bean:message key="label.list"/>&nbsp;
						<bean:write name="section" property="name"/>
					</html:link>
				</li>
			</ul>
		</logic:equal>
	</logic:iterate> 
</logic:notEmpty>
