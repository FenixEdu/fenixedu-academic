<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="title.masterDegree.administrativeOffice.createCandidate" /></h2>
<br />
<span class="error"><html:errors/></span>  
   <table>
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
         <td>
         	<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization" excludedFields="INTEGRATED_MASTER_DEGREE" bundle="ENUMERATION_RESOURCES"/>
         	<html:select property="specialization">
         		<html:option key="dropDown.Default" value=""/>
                <html:options collection="values" property="value" labelProperty="label"/>
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
         <td>
         	<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.person.IDDocumentType"/>
         	<html:select property="identificationDocumentType">
         		<html:option key="dropDown.Default" value=""/>
                <html:options collection="values" property="value" labelProperty="label"/>
             </html:select>
         </td>
       </tr>
</table>
<br />
<html:submit value="Criar Candidato" styleClass="inputbutton" property="ok"/>
<html:reset value="Limpar" styleClass="inputbutton"/>
</html:form>