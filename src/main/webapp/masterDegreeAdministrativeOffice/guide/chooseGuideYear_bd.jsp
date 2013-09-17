<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<h2><bean:message key="title.masterDegree.administrativeOffice.guideListingByYear"/></h2>
<br />
<span class="error"><!-- Error messages go here --><html:errors /></span>
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