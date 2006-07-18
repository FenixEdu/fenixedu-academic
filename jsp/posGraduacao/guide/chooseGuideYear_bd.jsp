<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<h2><bean:message key="title.masterDegree.administrativeOffice.guideListingByYear"/></h2>
<br />
<span class="error"><html:errors/></span>
   <table>
    <html:form action="/guideListingByYear?method=chooseYear">
   	  <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
       <!-- Guide Year -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.guideYear"/> </td>
         <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.year" property="year"/></td>
         </td>
       </tr>
   </table>
<br />
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Seguinte" styleClass="inputbutton" property="ok" />
</html:form>