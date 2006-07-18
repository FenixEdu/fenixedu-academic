<%@ page language="java" %>
<%@ page import="org.apache.struts.util.RequestUtils" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<ul>
<%--	<li>
		<html:link page="/sectionsManagement.do?method=prepareCreateSection">
			<bean:message key="link.createSection"/>
		</html:link>
	</li>--%>
</ul>
<ul>
<bean:define id="sectionsList" name="infoWebSite" property="sections" />
<logic:notEmpty name="sectionsList" >
	<logic:iterate id="section" name="sectionsList">
	<li>
		<bean:define id="objectCode" name="section" property="idInternal"/> 
		<html:link page="<%="/sectionsManagement.do?method=getSection&amp;objectCode=" + objectCode %>">
			<bean:message key="label.management"/>&nbsp;
			<bean:write name="section" property="name"/>
		</html:link>
	</li>
	</logic:iterate> 
</logic:notEmpty>
<logic:empty name="sectionsList" > lista de seccoes vazia</logic:empty>
<li><html:link page="<%="/sectionsConfiguration.do?method=prepareSectionsConfiguration&amp;objectCode=1"%>"><bean:message key="label.sections.configuration"/></html:link></li>
</ul>
<br/>
<br/>
<br/>
<br/>
<ul>
<li><html:link page="/index.do"><bean:message key="link.WebSiteManagement"/></html:link></li>
</ul>