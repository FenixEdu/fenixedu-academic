<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<bean:define id="title" name="<%= SessionConstants.CONTRIBUTOR_ACTION %>" scope="session" />    
<h2><bean:message name="title"/></h2>
<span class="error"><!-- Error messages go here --><html:errors /><br/></span>    
<table>
    <bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
        <html:form action="<%=path%>">
        <input alt="input.method" type="hidden" value="getContributors" name="method"/>
     	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>        
       <!-- Contributor Number -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.contributorNumber"/>: </td>
         <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.contributorNumber" property="contributorNumber" /></td>
         </td>
       </tr>
   </table>
   <br />
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Escolher" styleClass="inputbutton" property="ok"/>
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" value="Limpar" styleClass="inputbutton"/>
</html:form>