<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="title.masterDegree.administrativeOffice.createCandidate" /></h2>
<br />
<span class="error"><html:errors/></span>  
   <table>
    <bean:define id="specializations" name="<%= SessionConstants.SPECIALIZATIONS %>" scope="request"/>
 
    <bean:define id="identificationDocumentTypeList" name="<%= SessionConstants.IDENTIFICATION_DOCUMENT_TYPE_LIST %>" scope="request"/>
    <logic:present name="<%= SessionConstants.EXECUTION_DEGREE %>">
    	<bean:define id="executionDegree" name="<%= SessionConstants.EXECUTION_DEGREE %>" scope="request"/>
    </logic:present>
     <bean:define id="executionYearName" name="<%= SessionConstants.EXECUTION_YEAR %>"/>
    <html:form action="/createCandidateDispatchAction?method=create">
	   <html:hidden property="page" value="1"/>
		<html:hidden property="executionYear"/>
		<logic:present name="curricularPlanID">
			<html:hidden property="curricularPlanID" value="<%= pageContext.findAttribute("curricularPlanID").toString() %>"/>
		</logic:present>
		<logic:present name="executionDegree">
			<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE %>" value="<%= pageContext.findAttribute("executionDegree").toString() %>" />			
			<html:hidden property="executionDegreeOID" value="<%= pageContext.findAttribute("executionDegree").toString() %>" />
		</logic:present>
       <!-- Degree Type -->
       <tr>
			<td colspan="2">
				<bean:message key="label.executionYear"/> <bean:write name="executionYearName"/>
			</td>       
       </tr>
       <tr>
         <td><bean:message key="label.candidate.specialization"/>:</td>
         <td><html:select property="specialization">
                <html:options collection="specializations" property="value" labelProperty="label"/>
             </html:select>
         </td>
       </tr>


       <!-- Name -->
       <tr>
         <td><bean:message key="label.candidate.name"/>:</td>
         <td><html:text property="name"/></td>
         </td>
       </tr>

       <!-- Identification Document Number -->
       <tr>
         <td><bean:message key="label.candidate.identificationDocumentNumber"/>:</td>
         <td><html:text property="identificationDocumentNumber"/></td>
         </td>
       </tr>
       <!-- Identification Document Type -->
       <tr>
         <td><bean:message key="label.candidate.identificationDocumentType"/>:</td>
         <td><html:select property="identificationDocumentType">
                <html:options collection="identificationDocumentTypeList" property="value" labelProperty="label"/>
             </html:select>
         </td>
       </tr>
</table>
<br />
<html:submit value="Criar Candidato" styleClass="inputbutton" property="ok"/>
<html:reset value="Limpar" styleClass="inputbutton"/>
</html:form>