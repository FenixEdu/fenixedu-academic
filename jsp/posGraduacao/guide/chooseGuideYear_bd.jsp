<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<h2><bean:message key="title.masterDegree.administrativeOffice.guideListingByYear"/></h2>
<br />
<span class="error"><html:errors/></span>
   <table>
    <html:form action="/guideListingByYear?method=chooseYear">
   	  <html:hidden property="page" value="1"/>
       <!-- Guide Year -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.guideYear"/> </td>
         <td><html:text property="year"/></td>
         </td>
       </tr>
   </table>
<br />
<html:submit value="Seguinte" styleClass="inputbutton" property="ok" />
</html:form>