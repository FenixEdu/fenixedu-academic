<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@page import="net.sourceforge.fenixedu.domain.Role"%>
<%@page import="net.sourceforge.fenixedu.domain.person.RoleType"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<bean:define id="userView" name="USER_SESSION_ATTRIBUTE"/>

<logic:iterate id="roleType" type="net.sourceforge.fenixedu.domain.person.RoleType" name="userView" property="roleTypes">
	<%
		final Role role = Role.getRoleByRoleType(roleType);
		pageContext.setAttribute("role", role);
	%>
	<bean:define id="bundleKeyPageName"><bean:write name="role" property="pageNameProperty"/>.name</bean:define>
	<bean:define id="bundleKeyPageDescription"><bean:write name="role" property="pageNameProperty"/>.description</bean:define>
	<bean:define id="link">/dotIstPortal.do?prefix=<bean:write name="role" property="portalSubApplication"/>&amp;page=<bean:write name="role" property="page"/></bean:define>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td nowrap="nowrap" class="infoIcons">
	    	<img src="<%= request.getContextPath() %>/images/square-bullet.gif" alt="<bean:message key="square-bullet" bundle="IMAGE_RESOURCES" />"/> 
	    </td>
	    <td class="infoop">
	    	<strong><html:link page='<%= link %>'><bean:message name="bundleKeyPageName" bundle="PORTAL_RESOURCES"/></html:link></strong>
	    </td>
	  </tr>
	  <tr>
	  	<td>
		  <p>&nbsp;</p>
	    </td>
	  	<td><p><bean:message name="bundleKeyPageDescription" bundle="PORTAL_RESOURCES"/></p>
		</td>
	  </tr>
	</table>
</logic:iterate>	