<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<span class="error"><html:errors/></span>
<logic:present name="<%= SessionConstants.EXECUTION_DEGREE %>" scope="request">
   <table>
    <html:form action="/createGuideDispatchAction?method=requesterChosen">
      <!-- Degree -->
	  <tr>
   		<td align="center" class="infoselected" colspan="2">
			<bean:define id="executionDegree" name="<%= SessionConstants.EXECUTION_DEGREE %>" scope="request" />
   			<b><bean:message key="label.masterDegree.administrativeOffice.degree"/>: &nbsp;</b>
			<bean:write name="executionDegree" property="infoDegreeCurricularPlan.infoDegree.nome" /> -
			<bean:write name="executionDegree" property="infoDegreeCurricularPlan.name" /> -
			<bean:write name="executionDegree" property="infoExecutionYear.year" />
         </td>
      </tr>
      <tr>
      	<td>&nbsp;</td>
      </tr>
       <!-- Requester Number -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.requesterNumber"/>: </td>
         <td><html:text property="number"/></td>
         </td>
       </tr>
       <!-- Requester Type -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.requesterType"/>: </td>
         <td>
            <html:select property="requester">
                <html:options collection="<%= SessionConstants.GUIDE_REQUESTER_LIST %>" property="value" labelProperty="label"/>
             </html:select>          
         </td>
        </tr>
       <!-- Graduation Type -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.graduationType"/>: </td>
         <td>
            <html:select property="graduationType">
                <html:options collection="<%= SessionConstants.SPECIALIZATIONS %>" property="value" labelProperty="label"/>
             </html:select>          
         </td>
        </tr>
        
   	   <!-- Execution Degree ID-->
       <html:hidden property="executionDegreeID"/>
       
       <!-- Contributor -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.contributorNumber"/>: </td>
         <td><html:text property="contributorNumber"/>
         <logic:notPresent name="<%= SessionConstants.UNEXISTING_CONTRIBUTOR %>">
     	  <html:hidden property="page" value="1"/>
     	  
                  &nbsp;&nbsp;ou&nbsp;&nbsp; 
                 <html:select property="contributorList">
                   <option value="" selected="selected"><bean:message key="label.masterDegree.administrativeOffice.contributor.default"/></option>
                   <html:options collection="<%= SessionConstants.CONTRIBUTOR_LIST %>" property="value" labelProperty="label"/>
            	 </html:select>        
             </td>
           </tr> 
		</logic:notPresent >
		<logic:present name="<%= SessionConstants.UNEXISTING_CONTRIBUTOR %>">
     	  <html:hidden property="page" value="2"/>
             </td>
           </tr> 
    		<h2><bean:message key="error.masterDegree.administrativeOffice.nonExistingContributor"/></h2>
			<tr>		
             <td><bean:message key="label.masterDegree.administrativeOffice.contributorName"/>: </td>
             <td><html:text property="contributorName"/>
    		</tr>
    		<tr>
             <td><bean:message key="label.masterDegree.administrativeOffice.contributorAddress"/>: </td>
             <td><html:text property="contributorAddress"/>
    		</tr>
		</logic:present >
		<tr>
			<td>
				<html:submit value="Seguinte" styleClass="inputbutton" property="ok"/>
			</td>
			<td>
				<html:reset value="Limpar" styleClass="inputbutton"/>
			</td>
		</tr>
	  </html:form>
	</table>
</logic:present>