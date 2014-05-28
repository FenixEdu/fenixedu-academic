<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<h2><bean:message key="title.masterDegree.administrativeOffice.createCandidate" /></h2>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>  
</p>

    <logic:present name="<%= PresentationConstants.EXECUTION_DEGREE %>">
    	<bean:define id="executionDegree" name="<%= PresentationConstants.EXECUTION_DEGREE %>" scope="request"/>
    </logic:present>
     <bean:define id="executionYearName" name="<%= PresentationConstants.EXECUTION_YEAR %>"/>
    <html:form action="/createCandidateDispatchAction?method=create">
	   <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYear" property="executionYear"/>
		<logic:present name="curricularPlanID">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularPlanID" property="curricularPlanID" value="<%= pageContext.findAttribute("curricularPlanID").toString() %>"/>
		</logic:present>
		<logic:present name="executionDegree">
			<html:hidden alt="<%= PresentationConstants.EXECUTION_DEGREE %>" property="<%= PresentationConstants.EXECUTION_DEGREE %>" value="<%= pageContext.findAttribute("executionDegree").toString() %>" />			
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeOID" property="executionDegreeOID" value="<%= pageContext.findAttribute("executionDegree").toString() %>" />
		</logic:present>

	<!-- Degree Type -->
	<p class="mtop15"><strong><bean:message key="label.executionYear"/> <bean:write name="executionYearName"/></strong></p>

   <table class="tstyle5">

       <tr>
         <td><bean:message key="label.candidate.specialization"/>:</td>
         <td>
         	<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization" excludedFields="STUDENT_CURRICULAR_PLAN_INTEGRATED_MASTER_DEGREE" bundle="ENUMERATION_RESOURCES"/>
         	<html:select bundle="HTMLALT_RESOURCES" altKey="select.specialization" property="specialization">
         		<html:option key="dropDown.Default" value=""/>
                <html:options collection="values" property="value" labelProperty="label"/>
             </html:select>
         </td>
       </tr>


       <!-- Name -->
       <tr>
         <td><bean:message key="label.candidate.name"/>:</td>
         <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" size="50"/></td>
         </td>
       </tr>

       <!-- Identification Document Number -->
       <tr>
         <td><bean:message key="label.candidate.identificationDocumentNumber"/>:</td>
         <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.identificationDocumentNumber" property="identificationDocumentNumber"/></td>
         </td>
       </tr>
       <!-- Identification Document Type -->
       <tr>
         <td><bean:message key="label.candidate.identificationDocumentType"/>:</td>
         <td>
         	<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.person.IDDocumentType"/>
         	<html:select bundle="HTMLALT_RESOURCES" altKey="select.identificationDocumentType" property="identificationDocumentType">
         		<html:option key="dropDown.Default" value=""/>
                <html:options collection="values" property="value" labelProperty="label"/>
             </html:select>
         </td>
       </tr>
</table>

<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Criar Candidato" styleClass="inputbutton" property="ok"/>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" value="Limpar" styleClass="inputbutton"/>
</p>

</html:form>