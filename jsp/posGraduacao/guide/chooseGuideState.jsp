<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<h2><bean:message key="label.masterDegree.administrativeOffice.listGuideByState"/></h2>
<br />    
<span class="error"><html:errors/></span>   
<br />
   <table>
    <html:form action="/guideListingByState?method=chooseState">
   	  <html:hidden property="page" value="1"/>
       <!-- Guide Year -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.guideYear"/>: </td>
         <td><html:text property="year"/></td>
         </td>
       </tr>
       <!-- Guide State -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.guideSituation"/>: </td>
         <td>
         	<html:select property="state">
                <html:options collection="situations" property="value" labelProperty="label"/>
             </html:select>
         </td>
       </tr>
   </table>
<br />
<html:submit value="Listar" styleClass="inputbutton" property="ok"/>
</html:form>


