<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="title.editAnnouncement" /></h2>

<logic:present name="siteView"> 
<bean:define id="announcement" name="siteView" property="component"/>

<html:form action="/editAnnouncement" focus="title" >
<html:hidden property="page" value="1"/>
<strong><bean:message key="label.title" /></strong>
<br />
<html:textarea rows="2" cols="56" name="announcement" property="title" >
</html:textarea>

<span class="error"><html:errors/>
	<logic:present name="errors">
		<bean:write name="errors" filter="true" />
	</logic:present	>
</span>

<br />
<br />
<strong><bean:message key="label.information" /></strong>
<br />
<html:textarea rows="10" cols="60" name="announcement" property="information" >
</html:textarea>
<span class="error" ><html:errors property="information" /></span>
<br />
<br />
<html:submit styleClass="inputbutton"> <bean:message key="button.save" />
</html:submit> 
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>

<html:hidden property="method" value="editAnnouncement" />
<html:hidden  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<bean:define id="announcementCode" name="announcement" property="idInternal" />
<html:hidden  property="announcementCode" value="<%= announcementCode.toString() %>" />

</html:form>
</logic:present>