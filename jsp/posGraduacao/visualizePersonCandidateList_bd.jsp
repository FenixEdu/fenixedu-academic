	<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.util.State" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>
<%@ page import="java.util.Date" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate" %>



<bean:define id="candidateList" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE_LIST %>" scope="request"/>

<br />


<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
<bean:define id="link">
	<bean:write name="path"/>.do?method=visualize<%= "&" %>candidateID=
</bean:define>


<logic:iterate id="candidate" name="candidateList" >



	    <table>
	        <logic:notPresent name="first">
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
	          <!-- Contactos -->
	          <tr>
	            <td><h2><bean:message key="label.person.title.contactInfo" /><h2></td>
	          </tr>
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


			</logic:notPresent> 

	          
	          <tr></tr>
	          <tr></tr>      
	          <tr></tr>
	          <tr></tr>      
	

	        <logic:notPresent name="first">
		        <bean:define id="first" value="true" />
	            <tr>
		            <td><h2><bean:message key="label.candidate.applications" /><h2></td>
		        </tr>
			</logic:notPresent> 
    	</table>


		<bean:define id="candidateLink">
			<bean:write name="link"/><bean:write name="candidate" property="idInternal"/>
		</bean:define>

	   <html:link page='<%= pageContext.findAttribute("candidateLink").toString() %>'>
	 	  <bean:write name="candidate" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.nome"/> - 
	 	  <bean:write name="candidate" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.sigla"/> ( 
	 	  <bean:message name="candidate" property="specialization.name" bundle="ENUMERATION_RESOURCES"/> )
	   </html:link>


</logic:iterate>
		


