<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<bean:define id="title" name="<%= SessionConstants.ACTION %>" scope="session" />
<h2><bean:message name="title"/></h2>
<br />    
<span class="error"><html:errors/></span>   
   <table>
    <html:form action="/chooseGuideDispatchAction?method=choose">
   	  <html:hidden property="page" value="1"/>
       <!-- Guide Year -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.guideYear"/>: </td>
         <td><html:text property="guideYear"/></td>
         </td>
       </tr>
       <!-- Guide Number -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.guideNumber"/>: </td>
         <td><html:text property="guideNumber"/></td>
         </td>
       </tr>
   </table>
<br />
<html:submit value="Seguinte" styleClass="inputbutton" property="ok"/>
</html:form>


