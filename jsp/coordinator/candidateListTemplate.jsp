<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html>
    <body>
    
    <table width="100%" height="100%" border="0">
	    <tr height="30">
    	    <td>
			     <table width="100%" border="0" valign="top">
			      <tr> 
			        <td height="100" colspan="2">
			          <table border="0" width="100%" height="100" align="center" cellpadding="0" cellspacing="0">
			            <tr> 
			              <td width="50" height="100">
			               <img src="<%= request.getContextPath() %>/posGraduacao/guide/images/istlogo.gif" alt="<bean:message key="istlogo" bundle="IMAGE_RESOURCES" />" width="50" height="104" border="0"/> 
			              </td>
			              <td>
			                &nbsp;
			              </td>
			              <td>
			                <table border="0" width="100%" height="100%">
			                  <tr valign="top" align="left"> 
			                    <td>&nbsp;<b><bean:message key="label.masterDegree.candidateListFilter.ISTTitle"/></b><br/>
			                      <hr size="1">
			                    </td>
			                  </tr>
			                </table>
			              </td>
			            </tr>
			          </table>
			        </td>
			      </tr>
				</table>
			</td>
		</tr>
	    <tr align="right">
			<td>	
				<h2><bean:message key="label.masterDegree.candidateListFilter.listTitle"/></h2>
			</td>
		</tr>
		
		<tr>
			<td>
				<b><bean:message key="label.masterDegree.candidateListFilter.filteredBy"/><bean:write name="filteredBy"/></b>
				<br/>
				<table width="100%" border="1">
					<tr>
						<td><b><bean:message key="label.masterDegree.candidateListFilter.candidacyNumber"/></b></td>
						<td><b><bean:message key="label.masterDegree.candidateListFilter.candidateName"/></b></td>
						<td><b><bean:message key="label.masterDegree.candidateListFilter.candidacySchool"/></b></td>
						<td><b><bean:message key="label.masterDegree.candidateListFilter.degree"/></b></td>
						<td><b><bean:message key="label.masterDegree.candidateListFilter.specialization"/></b></td>
						<td><b><bean:message key="label.masterDegree.candidateListFilter.situation"/></b></td>
						<td><b><bean:message key="label.masterDegree.candidateListFilter.classAssistant"/></b></td>
						<td><b><bean:message key="label.masterDegree.candidateListFilter.phoneNumber"/></b></td>
						<td><b><bean:message key="label.masterDegree.candidateListFilter.email"/></b></td>
					</tr>
					<logic:iterate id="infoMasterDegreeCandidate" name="candidatesList">
					<bean:define id="infoPersonFromMasterDegree" name="infoMasterDegreeCandidate" property="infoPerson"/>
					<tr>
						<td><bean:write name="infoMasterDegreeCandidate" property="candidateNumber"/></td>
						<td><bean:write name="infoPersonFromMasterDegree" property="nome"/></td>
						<td><bean:write name="infoMasterDegreeCandidate" property="majorDegreeSchool"/></td>
						<td><bean:write name="infoMasterDegreeCandidate" property="majorDegree"/></td>
						<td><bean:write name="infoMasterDegreeCandidate" property="specialization"/></td>
						<td><bean:write name="infoMasterDegreeCandidate" property="infoCandidateSituation"/></td>
						<td>
						<logic:equal name="infoMasterDegreeCandidate" property="courseAssistant" value="true"><bean:message key="label.masterDegree.candidateListFilter.courseAssistantYes"/></logic:equal>
						<logic:notEqual name="infoMasterDegreeCandidate" property="courseAssistant" value="true"><bean:message key="label.masterDegree.candidateListFilter.courseAssistantNo"/></logic:notEqual>
						</td>
						<td><bean:write name="infoPersonFromMasterDegree" property="telefone"/></td>
						<td><bean:write name="infoPersonFromMasterDegree" property="email"/></td>
					</tr>
					</logic:iterate>
				</table>
	</table>
    </body>
</html>