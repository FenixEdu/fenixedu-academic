<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="title.masterDegree.administrativeOffice.createCandidate" /></h2>
<br />
<span class="error"><html:errors/></span>  
   <table>
    <bean:define id="specializations" name="<%= SessionConstants.SPECIALIZATIONS %>" scope="session" />
    <bean:define id="degreeList" name="<%= SessionConstants.DEGREE_LIST %>" scope="session" />
    <bean:define id="identificationDocumentTypeList" name="<%= SessionConstants.IDENTIFICATION_DOCUMENT_TYPE_LIST %>" scope="session" />
    <html:form action="/createCandidateDispatchAction?method=create">
	   <html:hidden property="page" value="1"/>

       <!-- Degree Type -->
       <tr>
         <td><bean:message key="label.candidate.specialization"/>:</td>
         <td><html:select property="specialization">
                <html:options collection="specializations" property="value" labelProperty="label"/>
             </html:select>
         </td>
       </tr>

       <!-- Degree -->
       <tr>
         <td><bean:message key="label.candidate.degree"/>:</td>
         <td><html:select property="degree">
         		<option value="" selected="selected"><bean:message key="label.candidate.degree.default"/></option>
                <html:options collection="degreeList" property="infoDegreeCurricularPlan.infoDegree.nome" labelProperty="infoDegreeCurricularPlan.infoDegree.nome"/>
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