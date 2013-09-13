<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<bean:define id="title" name="<%= PresentationConstants.ACTION %>" />
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


