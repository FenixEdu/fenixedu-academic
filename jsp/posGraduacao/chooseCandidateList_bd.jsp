<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<html>
  <head>
    <title><bean:message key="title.masterDegree.administrativeOffice.listCandidates" /></title>
  </head>
  <body>
   
   <table>
    <span class="error"><html:errors/></span>
    <bean:define id="specializationList" name="<%= SessionConstants.SPECIALIZATIONS %>" scope="session" />
    <bean:define id="degreeList" name="<%= SessionConstants.DEGREE_LIST %>" scope="session" />
    <bean:define id="situationList" name="<%= SessionConstants.CANDIDATE_SITUATION_LIST %>" scope="session" />
    <bean:define id="title" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE_ACTION %>" scope="session" />
    
    <bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Action.MAPPING_KEY %>" />
        <html:form action="<%=path%>">
        <html:hidden property="executionYear"/>
        <html:hidden property="page" value="1"/>
        <input type="hidden" value="getCandidates" name="method"/>
        <h2><bean:message name="title"/></h2>
        
       <!-- Degree -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.degree"/></td>
         <td><html:select property="degree">
         		<option value="" selected="selected"><bean:message key="label.candidate.degree.default"/></option>
                <html:options collection="degreeList" property="infoDegreeCurricularPlan.infoDegree.nome" labelProperty="infoDegreeCurricularPlan.infoDegree.nome"/>
             </html:select>
         </td>
       </tr>
       
       <!-- Degree Type -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.specialization"/></td>
         <td><html:select property="specialization">
                <html:options collection="specializationList" property="value" labelProperty="label"/>
             </html:select>
         </td>
       </tr>


       <!-- Candidate Situation List -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.candidateSituation"/></td>
         <td><html:select property="candidateSituation">
                <html:options collection="situationList" property="value" labelProperty="label"/>
             </html:select>
         </td>
       </tr>
    
       <!-- Candidate Number -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.candidateNumber"/></td>
         <td><html:text property="candidateNumber"/></td>
         </td>
       </tr>
    
       <br/>
         <td align="right">
             <html:submit value="Seguinte" styleClass="button" property="ok"/>
            <html:reset value="Limpar" styleClass="button"/>
         </td>
         </tr>
    </html:form>
   </table>
  </body>
</html>
