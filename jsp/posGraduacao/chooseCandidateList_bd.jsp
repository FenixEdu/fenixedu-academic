<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<span class="error"><html:errors/></span>
<bean:define id="executionDegree" name="<%= SessionConstants.EXECUTION_DEGREE %>" scope="request"/>
     <bean:define id="executionYearName" name="<%= SessionConstants.EXECUTION_YEAR %>"/>
    <bean:define id="degreeList" name="<%= SessionConstants.DEGREE_LIST %>" scope="session" />
    <bean:define id="situationList" name="<%= SessionConstants.CANDIDATE_SITUATION_LIST %>" scope="session" />
    <bean:define id="title" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE_ACTION %>" scope="session" />
    <bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
        <html:form action="<%=path%>">
        <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYear" property="executionYear"/>
        <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeOID" property="executionDegreeOID" value="<%= pageContext.findAttribute("executionDegree").toString() %>" />
        <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
        <input alt="input.method" type="hidden" value="getCandidates" name="method"/>
<h2><bean:message name="title"/></h2>
<table>    
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
         <td><bean:message key="label.masterDegree.administrativeOffice.specialization"/></td>
         <td>
         	<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization" excludedFields="INTEGRATED_MASTER_DEGREE" bundle="ENUMERATION_RESOURCES"/>
         	<html:select bundle="HTMLALT_RESOURCES" altKey="select.specialization" property="specialization">
                <html:option key="dropDown.Default" value=""/>
                <html:options collection="values" property="value" labelProperty="label"/>
             </html:select>
         </td>
       </tr>


       <!-- Candidate Situation List -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.candidateSituation"/></td>
         <td><html:select bundle="HTMLALT_RESOURCES" altKey="select.candidateSituation" property="candidateSituation">
                <html:options collection="situationList" property="value" labelProperty="label"/>
             </html:select>
         </td>
       </tr>
    
       <!-- Candidate Number -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.candidateNumber"/></td>
         <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.candidateNumber" property="candidateNumber"/></td>
         </td>
       </tr>
</table>
<br />
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Seguinte" styleClass="inputbutton" property="ok"/>
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" value="Limpar" styleClass="inputbutton"/>
</html:form>

