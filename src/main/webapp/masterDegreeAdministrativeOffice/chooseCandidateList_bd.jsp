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
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<bean:define id="executionDegree" name="<%= PresentationConstants.EXECUTION_DEGREE %>" scope="request"/>
     <bean:define id="executionYearName" name="<%= PresentationConstants.EXECUTION_YEAR %>"/>
    <bean:define id="degreeList" name="<%= PresentationConstants.DEGREE_LIST %>" />
    <bean:define id="situationList" name="<%= PresentationConstants.CANDIDATE_SITUATION_LIST %>" />
    <bean:define id="title" name="<%= PresentationConstants.MASTER_DEGREE_CANDIDATE_ACTION %>" />
    <bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
    <bean:define id="action" type="java.lang.String" scope="request" name="action" />
    
        <html:form action="<%=path + "?action=" + action %>">
        <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYear" property="executionYear"/>
        <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeOID" property="executionDegreeOID" value="<%= pageContext.findAttribute("executionDegree").toString() %>" />
        <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
        <input alt="input.method" type="hidden" value="getCandidates" name="method"/>

<h2><bean:message name="title"/></h2>

<table class="tstyle5">    
    <!-- Degree   
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.degree"/></td>
         <td><html:select bundle="HTMLALT_RESOURCES" altKey="select.degree" property="degree">
         		<option value="" selected="selected"><bean:message key="label.candidate.degree.default"/></option>
                <html:options collection="degreeList" property="value" labelProperty="label"/>
             </html:select>
         </td>
       </tr> -->
       
       <!-- Degree Type -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.specialization"/>:</td>
         <td>
         	<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization" excludedFields="STUDENT_CURRICULAR_PLAN_INTEGRATED_MASTER_DEGREE" bundle="ENUMERATION_RESOURCES"/>
         	<html:select bundle="HTMLALT_RESOURCES" altKey="select.specialization" property="specialization">
                <html:option key="dropDown.Default" value=""/>
                <html:options collection="values" property="value" labelProperty="label"/>
             </html:select>
         </td>
       </tr>


       <!-- Candidate Situation List -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.candidateSituation"/>:</td>
         <td><html:select bundle="HTMLALT_RESOURCES" altKey="select.candidateSituation" property="candidateSituation">
                <html:options collection="situationList" property="value" labelProperty="label"/>
             </html:select>
         </td>
       </tr>
    
       <!-- Candidate Number -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.candidateNumber"/>:</td>
         <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.candidateNumber" property="candidateNumber"/></td>
         </td>
       </tr>
</table>

<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Seguinte" styleClass="inputbutton" property="ok"/>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" value="Limpar" styleClass="inputbutton"/>
</p>
</html:form>

