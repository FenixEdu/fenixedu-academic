<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html>
  <head>
    <title><bean:message key="title.masterDegree.administrativeOffice.createCandidate" /></title>
  </head>
  <body>
   
   <table>
    <span class="error"><html:errors/></span>
    <html:form action="/createCandidateDispatchAction?method=create">
	   <html:hidden property="page" value="1"/>

       <!-- Degree Type -->
       <tr>
         <td><bean:message key="label.candidate.specialization"/></td>
         <td><html:select property="specialization">
                <html:options collection="specializations" property="value" labelProperty="label"/>
             </html:select>
         </td>
       </tr>

       <!-- Degree -->
       <tr>
         <td><bean:message key="label.candidate.degree"/></td>
         <td><html:select property="degree">
         		<option value="" selected="selected"><bean:message key="label.candidate.degree.default"/></option>
                <html:options collection="degreeList" property="infoDegreeCurricularPlan.infoDegree.nome" labelProperty="infoDegreeCurricularPlan.infoDegree.nome"/>
             </html:select>
         </td>
       </tr>

       <!-- Name -->
       <tr>
         <td><bean:message key="label.candidate.name"/></td>
         <td><html:text property="name"/></td>
         </td>
       </tr>

       <!-- Identification Document Number -->
       <tr>
         <td><bean:message key="label.candidate.identificationDocumentNumber"/></td>
         <td><html:text property="identificationDocumentNumber"/></td>
         </td>
       </tr>

       <!-- Identification Document Type -->
       <tr>
         <td><bean:message key="label.candidate.identificationDocumentType"/></td>
         <td><html:select property="identificationDocumentType">
                <html:options collection="identificationDocumentTypeList" property="value" labelProperty="label"/>
             </html:select>
         </td>
       </tr>
    
       <br/>
         <td align="right">
             <html:submit value="Criar Candidato" styleClass="button" property="ok"/>
            <html:reset value="Limpar" styleClass="button"/>
         </td>
         </tr>
    </html:form>
   </table>
  </body>
</html>
