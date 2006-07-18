<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<title><bean:message key="title.masterDegree.administrativeOffice.createContributor" /></title>
<span class="error"><html:errors/></span>
<h2><bean:message key="label.action.contributors.create" /></h2>
   <table>
    <html:form action="/createContributorDispatchAction?method=create">
   	  <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
       <!-- Contributor Number -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.contributorNumber"/>: </td>
         <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.contributorNumber" property="contributorNumber"/></td>
         </td>
       </tr>
       <!-- Contributor Name -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.contributorName"/>: </td>
         <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.contributorName" property="contributorName"/></td>
         </td>
       </tr>
       <!-- Contributor Address -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.contributorAddress"/>: </td>
         <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.contributorAddress" property="contributorAddress"/></td>
         </td>
       </tr>
 	</table>
<br />
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Criar" styleClass="inputbutton" property="ok"/>
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" value="Limpar" styleClass="inputbutton"/>
</html:form>


