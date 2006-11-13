<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="label.title.seniorInfo"/></h2>
	<br/>
	<table border="0" width="100%" cellspacing="9">
          <tr><td colspan="2">
          	<table border="0" width="100%" height="100%" cellspacing="4">
				<tr>
        		    <td align="right"><bean:message key="label.person.name" bundle="APPLICATION_RESOURCES"/>:</td>
		        	<td colspan="5"><bean:write name="seniorInfoForm" property="name"/></td>
					<td rowspan="3" valign="center" height="12px" width="5px" align="center">
						<logic:equal name="seniorInfoForm" property="availablePhoto" value="true">
							<bean:define id="personID" name="seniorInfoForm" property="personID"/>
      						<html:img src="<%= request.getContextPath() +"/person/viewPhoto.do?personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES"/>
						</logic:equal>
					</td>
		        </tr>
          		<tr>
		            <td align="right"><bean:message key="label.person.address" bundle="APPLICATION_RESOURCES" /></td>
		            <td colspan="5"><bean:write name="seniorInfoForm" property="address"/></td>
		        </tr>
           		<tr>
		            <td align="right"><bean:message key="label.person.postCode" bundle="APPLICATION_RESOURCES" /></td>
		            <td><bean:write name="seniorInfoForm" property="areaCode" filter="false"/></td>
		            <td align="right" colspan="2"><bean:message key="label.person.areaOfPostCode" bundle="APPLICATION_RESOURCES" /></td>
		            <td><bean:write name="seniorInfoForm" property="areaCodeArea" filter="false"/></td>
		        </tr>
				<tr>	
					<td align="right" width="14%"><bean:message key="label.person.telephone" bundle="APPLICATION_RESOURCES" /></td>
            		<td width="8%"><bean:write name="seniorInfoForm" property="phone"/></td>
            		<td align="right"width="16%"><bean:message key="label.person.mobilePhone" bundle="APPLICATION_RESOURCES" /></td>
            		<td width="16%" colspan="2"><bean:write name="seniorInfoForm" property="mobilePhone"/></td>
            		<td align="right" width="16%"><bean:message key="label.person.email" bundle="APPLICATION_RESOURCES" /></td>
	        		<td width="16%"><bean:write name="seniorInfoForm" property="email"/></td>
				</tr>
	        </table>				
	      </td></tr>
		  <tr>
			<td colspan="2">&nbsp;</td>
		  </tr>
		  <tr>
            <td width="20%"><bean:message key="label.senior.expectedDegreeTermination"/></td>
            <td>
				<bean:write name="seniorInfoForm" property="expectedDegreeTerminationDay"/> / <bean:write name="seniorInfoForm" property="expectedDegreeTerminationMonth"/> / <bean:write name="seniorInfoForm" property="expectedDegreeTerminationYear"/>
				<bean:message key="label.senior.expectedDegreeTerminationHelp"/>
			</td>
          </tr>
		  <tr>
            <td><bean:message key="label.senior.expectedDegreeAverageGrade"/>:</td>
            <td><bean:write name="seniorInfoForm" property="expectedDegreeAverageGrade"/></td>
          </tr>
		  <tr>
            <td valign="top"><bean:message key="label.senior.specialtyField"/></td>
            <td><bean:write name="seniorInfoForm" property="specialtyField" filter="false"/></td>
          </tr>
		  <tr>
            <td valign="top"><bean:message key="label.senior.professionalInterests"/></td>
            <td><bean:write name="seniorInfoForm" property="professionalInterests" filter="false"/></td>
          </tr>
		  <tr>
            <td valign="top"><bean:message key="label.senior.languageSkills"/></td>
            <td><bean:write name="seniorInfoForm" property="languageSkills" filter="false"/></td>
          </tr>
		  <tr>
            <td valign="top"><bean:message key="label.senior.informaticsSkills"/></td>
            <td><bean:write name="seniorInfoForm" property="informaticsSkills" filter="false"/></td>
          </tr>
		  <tr>
            <td valign="top"><bean:message key="label.senior.extracurricularActivities"/></td>
            <td><bean:write name="seniorInfoForm" property="extracurricularActivities" filter="false"/></td>
          </tr>
  		  <tr>
            <td valign="top"><bean:message key="label.senior.professionalExperience"/></td>
            <td><bean:write name="seniorInfoForm" property="professionalExperience" filter="false"/></td>
          </tr>		  		  
		  <tr>
			<td>&nbsp;</td>
		  </tr>
  		  <tr>
            <td valign="bottom" align="right" colspan="2">
				<bean:message key="label.senior.lastModificationDate"/>
				<bean:define id="formatDate">
					<dt:format pattern="dd/MM/yyyy">
						<bean:write name="seniorInfoForm" property="lastModificationDate.time"/>
					</dt:format>
				</bean:define>
				<br/><bean:write name="formatDate"/>
			</td>
          </tr>
   	</table>
<br /><br />

