<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<bean:define id="userView" name="<%= SessionConstants.U_VIEW %>" scope="session"/>

<logic:iterate id="role" name="userView" property="roles">
<%--	<bean:write name="role" property="page"/><br/>
	<bean:write name="role" property="portalSubApplication"/><br/>
	<bean:write name="role" property="pageNameProperty"/><br/> --%>
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