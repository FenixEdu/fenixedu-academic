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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

    <div align="center">
     <font color="#023264" size="-1">
        <h2>          Candidato Criado !         </h2>
      </font>
    </div>
    <table>
    <bean:define id="newCandidate" name="<%= PresentationConstants.NEW_MASTER_DEGREE_CANDIDATE %>" scope="request" />
      <logic:present name="newCandidate">
          <!-- Name -->
          <tr>
            <td><bean:message key="label.candidate.name" /></td>
            <td><bean:write name="newCandidate" property="infoPerson.nome"/></td>
          </tr>

          <!-- Candidate Number -->
          <tr>
            <td><bean:message key="label.candidate.candidateNumber" /></td>
            <td><bean:write name="newCandidate" property="candidateNumber"/></td>
          </tr>

          <!-- Specialization -->
          <tr>
            <td><bean:message key="label.candidate.specialization" /></td>
            <td><bean:message name="newCandidate" property="specialization.name" bundle="ENUMERATION_RESOURCES"/></td>
          </tr>

          <!-- Degree  -->
          <tr>
            <td><bean:message key="label.candidate.degree" /></td>
            <td><bean:write name="newCandidate" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.nome"/> - 
                <bean:write name="newCandidate" property="infoExecutionDegree.infoDegreeCurricularPlan.name"/>
            </td>
          </tr>

          <!-- Execution Year  
          <tr>
            <td><bean:message key="label.masterDegree.administrativeOffice.executionYear" /></td>
            <td><bean:write name="newCandidate" property="infoExecutionDegree.infoExecutionYear.year"/></td>
          </tr>
			-- >
          <!-- Identification Document Number -->
          <tr>
            <td><bean:message key="label.candidate.identificationDocumentNumber" /></td>
            <td><bean:write name="newCandidate" property="infoPerson.numeroDocumentoIdentificacao"/></td>
          </tr>

          <!-- Specialization -->
          <tr>
            <td><bean:message key="label.candidate.identificationDocumentType" /></td>
            <td>
            	<bean:define id="idType" name="newCandidate" property="infoPerson.tipoDocumentoIdentificacao"/>
            	<bean:message key='<%=idType.toString()%>'/>
            </td>
          </tr>
          
		<tr>
			<td>&nbsp;</td>
		</tr>
          
        <!-- Edit Candidate Link -->
		<bean:define id="editCandidateLink">
			/editCandidate.do?method=prepareEdit&candidateID=<bean:write name="newCandidate" property="externalId"/>
		</bean:define>		
		<tr>
		  	<td><html:link page='<%= editCandidateLink %>'><bean:message key="link.masterDegree.administrativeOffice.editCandidate" /></html:link></td>
		</tr>

		<tr>
			<td>&nbsp;</td>
		</tr>
<%-- 		
		<tr>
			<td class="infoop" colspan="2"><bean:message key="label.candidate.changePasswordInfo"/></td>
		</tr>
		<bean:define id="changePasswordLink">
			/editCandidate.do?method=changePassword&candidateID=<bean:write name="newCandidate" property="externalId"/>
		</bean:define>
		<tr>
		  	<td><html:link page='<%= changePasswordLink %>' target="_blank"><bean:message key="link.masterDegree.administrativeOffice.changePassword" /></html:link></td>
		</tr>
--%>          
      </logic:present>
    </table>

