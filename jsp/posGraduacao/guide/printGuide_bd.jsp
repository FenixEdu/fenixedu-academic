<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<html>
  <head>
    <title><bean:message key="title.masterDegree.administraiveOffice.createGuide" /></title>
  </head>
  <body>
   <span class="error"><html:errors/></span>
     <bean:define id="guide" name="<%= SessionConstants.GUIDE %>" scope="session" />
   <table>
   <tr>
   	<td>Ano da guia</td><td> <bean:write name="guide" property="year"/></td>
    <td>numero da guia</td> <td><bean:write name="guide" property="number"/></td>
   </tr>
   
   </table>
   
   <html:link page="/printGuidePage.do" target="_blank">
   		<bean:message key="link.masterDegree.administrativeOffice.printGuide" />
   </html:link>
   
  </body>
</html>
