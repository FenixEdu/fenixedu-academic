<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<html>
  <head>
    <title><bean:message key="title.masterDegree.administraiveOffice.createGuide" /></title>
  </head>
  <body>
   <span class="error"><html:errors/></span>
     <bean:define id="guide" name="<%= SessionConstants.GUIDE %>" scope="session" />
	<h2><bean:message key="label.masterDegree.administrativeOffice.guideCreationSuccess" /></h2>
   <table>
   <tr>
   	<td><bean:message key="label.masterDegree.administrativeOffice.requesterNumber" /></td> <td><bean:write name="<%= SessionConstants.REQUESTER_NUMBER %>"/></td>
   </tr>
   <tr>
   	<td><bean:message key="label.masterDegree.administrativeOffice.guideNumber" /></td> <td><bean:write name="guide" property="number"/></td>
   </tr>
   <tr>
    <td><bean:message key="label.masterDegree.administrativeOffice.guideYear" /></td> <td><bean:write name="guide" property="year"/></td>
   </tr>
   <tr>
    <td><bean:message key="label.masterDegree.administrativeOffice.guideTotal" /></td> <td><bean:write name="guide" property="total"/>&nbsp;
    	<bean:message key="label.currencySymbol" /></td>
   </tr>
   
   
   </table>
   <%--<bean:define id="link">/printGuidePages.do?graduationType=<bean:write name="graduationType"/>&<bean:write name="<%= SessionConstants.REQUESTER_NUMBER %>"/></bean:define>
   <bean:define id="graduationType" name="graduationType"/>
   <html:link page='<%= pageContext.findAttribute("link").toString() %>' target="_blank">
   <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.graduationType" property="graduationType" value='<%= pageContext.findAttribute("graduationType").toString()%>'/>
   <html:hidden alt="<%= SessionConstants.REQUESTER_NUMBER %>" property="<%= SessionConstants.REQUESTER_NUMBER %>" value='<%= pageContext.findAttribute("SessionConstants.REQUESTER_NUMBER").toString()%>'/>
   <bean:message key="link.masterDegree.administrativeOffice.printGuide" />
   </html:link>--%>
   <html:link page="<%= "/printGuidePages.do?graduationType=" + pageContext.findAttribute("graduationType") + "&amp;" + SessionConstants.REQUESTER_NUMBER + "=" + pageContext.findAttribute(SessionConstants.REQUESTER_NUMBER)%>" >
   <bean:message key="link.masterDegree.administrativeOffice.printGuide" /> 
   </html:link>
   
   
   
  </body>
</html>
