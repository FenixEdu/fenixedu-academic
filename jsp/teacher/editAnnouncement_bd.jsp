<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="title.editAnnouncement" /></h2>
<html:form action="/editAnnouncement" focus="title" >
<html:hidden property="page" value="1"/>
<strong><bean:message key="label.title" /></strong>
<br />
<html:textarea rows="2" cols="56" name="Announcement" property="title" >
</html:textarea>
<span class="error" ><html:errors property="title" /></span>
<br />
<br />
<strong><bean:message key="label.information" /></strong>
<br />
<html:textarea rows="10" cols="60" name="Announcement" property="information" >
</html:textarea>
<span class="error" ><html:errors property="information" /></span>
<br />
<br />
<html:submit styleClass="inputbutton"> <bean:message key="button.save" />
</html:submit> 
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>
<html:hidden property="method" value="editAnnouncement" />
</html:form>