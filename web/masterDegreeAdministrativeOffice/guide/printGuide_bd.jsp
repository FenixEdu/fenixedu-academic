<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<html>
  <head>
    <title><bean:message key="title.masterDegree.administraiveOffice.createGuide" /></title>
  </head>
  <body>
   <span class="error"><!-- Error messages go here --><html:errors /></span>
     <bean:define id="guide" name="<%= PresentationConstants.GUIDE %>" />
	<h2><bean:message key="label.masterDegree.administrativeOffice.guideCreationSuccess" /></h2>
   <table>
   <tr>
   	<td><bean:message key="label.masterDegree.administrativeOffice.requesterNumber" /></td> <td><bean:write name="<%= PresentationConstants.REQUESTER_NUMBER %>"/></td>
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
   <%--<bean:define id="link">/printGuidePages.do?graduationType=<bean:write name="graduationType"/>&<bean:write name="<%= PresentationConstants.REQUESTER_NUMBER %>"/></bean:define>
   <bean:define id="graduationType" name="graduationType"/>
   <html:link page='<%= pageContext.findAttribute("link").toString() %>' target="_blank">
   <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.graduationType" property="graduationType" value='<%= pageContext.findAttribute("graduationType").toString()%>'/>
   <html:hidden alt="<%= PresentationConstants.REQUESTER_NUMBER %>" property="<%= PresentationConstants.REQUESTER_NUMBER %>" value='<%= pageContext.findAttribute("PresentationConstants.REQUESTER_NUMBER").toString()%>'/>
   <bean:message key="link.masterDegree.administrativeOffice.printGuide" />
   </html:link>--%>
   <html:link page="<%= "/printGuidePages.do?graduationType=" + pageContext.findAttribute("graduationType") + "&amp;" + PresentationConstants.REQUESTER_NUMBER + "=" + pageContext.findAttribute(PresentationConstants.REQUESTER_NUMBER)%>" >
   <bean:message key="link.masterDegree.administrativeOffice.printGuide" /> 
   </html:link>
   
   
   
  </body>
</html>
