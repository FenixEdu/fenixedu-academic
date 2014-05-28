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
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.util.State" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>
<%@ page import="java.util.Date" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate" %>

<bean:define id="candidateList" name="<%= PresentationConstants.MASTER_DEGREE_CANDIDATE_LIST %>" scope="request"/>

<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
<bean:define id="link">
	<bean:write name="path"/>.do?method=visualize<%= "&" %>candidateID=
</bean:define>

	<h2><bean:message key="label.candidate"/></h2>

<logic:iterate id="candidate" name="candidateList" >

	       <logic:notPresent name="first">
		    <table class="tstyle2">
	          <!-- Nome -->
	          <tr>
	            <td><bean:message key="label.person.name" /></td>
	            <td><bean:write name="candidate" property="infoPerson.nome"/></td>
	          </tr>
	 	      <!-- Numero do Documento de Identificacao -->
	          <tr>
	            <td><bean:message key="label.person.identificationDocumentNumber" /></td>
	            <td><bean:write name="candidate" property="infoPerson.numeroDocumentoIdentificacao"/></td>
	          </tr>
	          <!-- Tipo do Documento de Identificacao -->
	          <tr>
	            <td><bean:message key="label.person.identificationDocumentType" /></td>
	            <td>
	            	<bean:define id="idType" name="candidate" property="infoPerson.tipoDocumentoIdentificacao"/>
	            	<bean:message key='<%=idType.toString()%>'/>
	            </td>
	          </tr>
	          <!-- Username -->
	          <tr>
	            <td><bean:message key="label.person.username" /></td>
	            <td><bean:write name="candidate" property="infoPerson.username"/></td>
	          </tr>
	      </table>
	         
	      <p class="mtop1 mbottom05"><strong><bean:message key="label.person.title.contactInfo" /></strong><p>
	      <table class="tstyle2 mtop05">
	          <!-- Telefone -->
	          <tr>
	            <td><bean:message key="label.person.telephone" /></td>
	            <td><bean:write name="candidate" property="infoPerson.telefone"/></td>
	          </tr>
	          <!-- Telemovel -->
	          <tr>
	            <td><bean:message key="label.person.mobilePhone" /></td>
	            <td><bean:write name="candidate" property="infoPerson.telemovel"/></td>
	          </tr>
	          <!-- E-Mail -->
	          <tr>
	            <td><bean:message key="label.person.email" /></td>
	            <td><bean:write name="candidate" property="infoPerson.email"/></td>
	          </tr>
	          <!-- WebPage -->
	          <tr>
	            <td><bean:message key="label.person.webSite" /></td>
	            <td><bean:write name="candidate" property="infoPerson.enderecoWeb"/></td>
	          </tr>
			</table>

			</logic:notPresent> 

	          
	

	        <logic:notPresent name="first">
		        <bean:define id="first" value="true" />

				<p class="mtop1"><strong><bean:message key="label.candidate.applications" /></strong><p>

			</logic:notPresent> 



		<bean:define id="candidateLink">
			<bean:write name="link"/><bean:write name="candidate" property="externalId"/>
		</bean:define>
		
      <p class="mvert05">
	   <html:link page='<%= pageContext.findAttribute("candidateLink").toString() %>'>
	 	  <bean:write name="candidate" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.nome"/> - 
	 	  <bean:write name="candidate" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.sigla"/> ( 
	 	  <bean:message name="candidate" property="specialization.name" bundle="ENUMERATION_RESOURCES"/> )
	   </html:link>
	  </p>
	   
	   


</logic:iterate>
		


