<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<bean:define id="userView" name="<%= SessionConstants.U_VIEW %>" scope="session"/>

<logic:notEmpty name="userView" property="person.user.lastLoginHost">
	<bean:define id="lastLoginDateTime" type="java.util.Date" name="userView" property="person.user.lastLoginDateTime"/>
	<bean:define id="timestamp" ><%= lastLoginDateTime.getTime() %></bean:define>
	<bean:define id="lastLoginHost" name="userView" property="person.user.lastLoginHost"/>
    <p class="mtop0 mbottom2"><span style="background-color: #eee; padding: 0.25em;"><bean:message key="last.login.dateTime"/>&nbsp;<b><date:format pattern="dd-MM-yyyy HH:mm"><bean:write name="timestamp"/></date:format></b> (<bean:write name="lastLoginHost"/>)</span></p>	
</logic:notEmpty>

<br/>
<bean:message key="message.person.welcome.header"/>
<br/>
<bean:message key="message.person.welcome.body"/>
