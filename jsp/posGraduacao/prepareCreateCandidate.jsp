<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html>
  <head>
    <title><bean:message key="masterDegree.administrativeOffice.createCandidate" /></title>
  </head>
  <body>

   <table>
    <span class="error"><html:errors/></span>
    <html:form action="/createCandidateDispatchAction?method=create">

       <!-- Degree Type -->
       <tr>
         <td><bean:message key="label.degreeType"/></td>
         <td><html:select property="degreeType">
                <html:options collection="degreeTypes" property="value" labelProperty="label"/>
             </html:select>
         </td>
       </tr>

       <!-- Degree -->
       <tr>
         <td><bean:message key="label.degree"/></td>
         <td><html:select property="degree">
                <html:options collection="degreeList" property="value" labelProperty="label"/>
             </html:select>
         </td>
       </tr>

       <!-- Name -->
       <tr>
         <td><bean:message key="label.name"/></td>
         <td><html:text property="name"/></td>
         </td>
       </tr>

       <!-- Identification Document Number -->
       <tr>
         <td><bean:message key="label.identificationDocumentNumber"/></td>
         <td><html:text property="identificationDocumentNumber"/></td>
         </td>
       </tr>

       <!-- Identification Document Type -->
       <tr>
         <td><bean:message key="label.identificationDocumentType"/></td>
         <td><html:select property="identificationDocumentType">
                <html:options collection="identificationDocumentTypeList" property="value" labelProperty="label"/>
             </html:select>
         </td>
       </tr>
    
       <br/>
           <td align="right">
             <html:submit value="Criar" styleClass="button" property="ok"/>
           </td>
           <td width='20'> </td>
           <td align="left">
            <html:reset value="Limpar" styleClass="button"/>
         </td>
         </tr>
    </html:form>
   </table>
  </body>
</html>
