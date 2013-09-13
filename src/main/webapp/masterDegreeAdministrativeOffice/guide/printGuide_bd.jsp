<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<!-- masterDegreeAdministrativeOffice/guide/printGuide_bd.jsp -->

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
   <bean:define id="number" name="guide" property="number" />
   <bean:define id="year" name="guide" property="year" />
   <html:link page="<%= "/printGuide.do?method=printByNumberAndYear&amp;number=" + number.toString() + "&amp;year=" + year.toString() %>">
   		<bean:message key="link.masterDegree.administrativeOffice.printGuide" /> 
   </html:link>
   
   
   
  </body>
</html>
