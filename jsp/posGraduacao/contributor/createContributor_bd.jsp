<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<title><bean:message key="title.masterDegree.administrativeOffice.createContributor" /></title>
<span class="error"><html:errors/></span>
<h2><bean:message key="label.action.contributors.create" /></h2>
   <table>
    <html:form action="/createContributorDispatchAction?method=create">
   	  <html:hidden property="page" value="1"/>
       <!-- Contributor Number -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.contributorNumber"/>: </td>
         <td><html:text property="contributorNumber"/></td>
         </td>
       </tr>
       <!-- Contributor Name -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.contributorName"/>: </td>
         <td><html:text property="contributorName"/></td>
         </td>
       </tr>
       <!-- Contributor Address -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.contributorAddress"/>: </td>
         <td><html:text property="contributorAddress"/></td>
         </td>
       </tr>
 	</table>
<br />
<html:submit value="Criar" styleClass="inputbutton" property="ok"/>
<html:reset value="Limpar" styleClass="inputbutton"/>
</html:form>


