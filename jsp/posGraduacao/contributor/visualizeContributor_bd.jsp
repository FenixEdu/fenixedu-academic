<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.util.State" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation" %>



<html>
  <head>
    <title><bean:message key="label.masterDegree.administrativeOffice.visualizeContributor" /></title>
  </head>
  <body>
     <h2><bean:message key="label.action.contributors.visualize" /></h2>
     <br>
    <table>
     <bean:define id="contributor" name="<%= SessionConstants.CONTRIBUTOR %>" scope="session"/>
        <logic:present name="contributor">
          <!-- Contributor Number -->
          <tr>
            <td><bean:message key="label.masterDegree.administrativeOffice.contributorNumber" /></td>
            <td><bean:write name="contributor" property="contributorNumber"/></td>
          </tr>
          <!-- Contributor Name -->
          <tr>
            <td><bean:message key="label.masterDegree.administrativeOffice.contributorName" /></td>
            <td><bean:write name="contributor" property="contributorName"/></td>
          </tr>
          <!-- Contributor Address -->
          <tr>
            <td><bean:message key="label.masterDegree.administrativeOffice.contributorAddress" /></td>
            <td><bean:write name="contributor" property="contributorAddress"/></td>
          </tr>
        </logic:present>
    </table>
    <br>
    <br>
    <html:link page="/editContributor.do?method=prepareEdit">
    	<bean:message key="link.masterDegree.administrativeOffice.editContributor" />
    </html:link>
  </body>
</html>
