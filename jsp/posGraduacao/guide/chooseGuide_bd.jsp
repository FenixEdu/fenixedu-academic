<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<bean:define id="title" name="<%= SessionConstants.ACTION %>" scope="session" />
<h2><bean:message name="title"/></h2>
<br />    
<span class="error"><!-- Error messages go here --><html:errors /></span>   
   <table>
    <html:form action="/chooseGuideDispatchAction?method=choose">
   	  <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
       <!-- Guide Year -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.guideYear"/>: </td>
         <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.guideYear" property="guideYear"/></td>
         </td>
       </tr>
       <!-- Guide Number -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.guideNumber"/>: </td>
         <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.guideNumber" property="guideNumber"/></td>
         </td>
       </tr>
   </table>
<br />
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Seguinte" styleClass="inputbutton" property="ok"/>
</html:form>


