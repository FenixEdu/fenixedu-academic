<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoStudent" %>
<%@ page import="DataBeans.InfoTeacher" %>


<bean:define id="student" name="<%= SessionConstants.STUDENT %>" scope="request"/>
<bean:define id="dissertationTitleFromRequest" name="<%= SessionConstants.DISSERTATION_TITLE %>" />
		
<logic:present name="<%= SessionConstants.CLASSIFICATION %>" scope="request">
	<bean:define id="classification" name="<%= SessionConstants.CLASSIFICATION %>" type="java.util.List"/>
</logic:present>	
				
<h2 align="center"><bean:message key="link.masterDegree.administrativeOffice.thesis.changeProof"/></h2>
<center>
<span class="error"><html:errors/></span>

<br/>

<html:form action="/changeMasterDegreeProofLookup.do">
	<html:hidden property="studentNumber" />
	<html:hidden property="degreeType" />


	<html:hidden property="dissertationTitle" />

	<table border="0" width="100%" cellspacing="3" cellpadding="10">
		<tr>
			<th align="left"><bean:message key="label.masterDegree.administrativeOffice.studentNumber"/></th>
			<th align="left"><bean:message key="label.masterDegree.administrativeOffice.studentName"/></th>
		</tr>
	
		<tr>
			<td align="left">
				<bean:write name="student" property="number"/>
			</td>
			<td align="left">
				<bean:write name="student" property="infoPerson.nome"/>
			</td>			
		</tr>
		<tr> 
			<td>&nbsp;</td>
		</tr>
	
		<!-- Dissertation Title -->
		<tr>
			<th align="left" colspan="2">
				<bean:message key="label.masterDegree.administrativeOffice.dissertationTitle"/>&nbsp;
				<bean:write name="dissertationTitleFromRequest"/>
				
				
			</th>
		</tr>
		<tr> 
			<td>&nbsp;</td>
		</tr>
		
	</table>
	
	<table cellspacing="3" cellpadding="10"  >					
		<tr >
			<td align="left">&nbsp;</td>
			<th align="left"><bean:message key="label.masterDegree.administrativeOffice.day"/></th>
			<th align="left"><bean:message key="label.masterDegree.administrativeOffice.month"/></th>
			<th align="left"><bean:message key="label.masterDegree.administrativeOffice.year"/></th>
		</tr>	
		
		<!-- Proof Date -->
		<tr >
			<th align="left"><bean:message key="label.masterDegree.administrativeOffice.proofDate"/>&nbsp;</th>
			<th><html:text property="proofDateDay" size="2"/></th>
			<th><html:text property="proofDateMonth" size="2"/></th>
			<th><html:text property="proofDateYear" size="4"/></th>
		</tr>
		
		<!-- Thesis Delivery Date -->
		<tr >
			<th align="left"><bean:message key="label.masterDegree.administrativeOffice.thesisDeliveryDate"/>&nbsp;</th>
			<th><html:text property="thesisDeliveryDateDay" size="2"/></th>
			<th><html:text property="thesisDeliveryDateMonth" size="2"/></th>
			<th><html:text property="thesisDeliveryDateYear" size="4"/></th>
		</tr>
			
	</table>				
	
	<br/>
		
	<table border="0" width="100%" cellspacing="3" cellpadding="10">
	
	<!-- Final Result -->
		<tr>	
			<th align="left" colspan="3">
				<bean:message key="label.masterDegree.administrativeOffice.finalResult"/>&nbsp;
			    <html:select property="finalResult">
			    	<html:options collection="<%= SessionConstants.CLASSIFICATION %>" property="value" labelProperty="label" />
			   </html:select> 
			</th>
		</tr>
					
		

		
		<!-- Attached Copies Number -->
		<tr>
			<th align="left" colspan="4">
				<bean:message key="label.masterDegree.administrativeOffice.attachedCopiesNumber"/>&nbsp;
				<html:text property="attachedCopiesNumber" size="5"/>
			</th>
		</tr>
		<tr> 
			<td>&nbsp;</td>
		</tr>
				
		<!-- Juries -->
		<tr>
			<th align="left" colspan="4"><bean:message key="label.masterDegree.administrativeOffice.juries"/></th>				
		</tr>
			<logic:present name="<%= SessionConstants.JURIES_LIST %>" scope="request">
				<bean:define id="juriesList" name="<%= SessionConstants.JURIES_LIST %>" type="java.util.List"/>
				<tr>
					<th align="left"><bean:message key="label.masterDegree.administrativeOffice.teacherNumber"/></th>
					<th align="left" width="40%"><bean:message key="label.masterDegree.administrativeOffice.teacherName"/></th>
					<td width="30%">&nbsp;</td>
					<td>&nbsp;</td>						
				</tr>					
				<logic:iterate id="jury" name="juriesList">
					<html:hidden property="juriesNumbers" value="<%= ((InfoTeacher)jury).getTeacherNumber().toString() %>"/>
					<tr>
						<td align="left"><bean:write name="jury" property="teacherNumber"/></td>
						<td align="left"><bean:write name="jury" property="infoPerson.nome"/></td>
						<td>&nbsp;</td>
						<td align="center">
							<html:multibox property="removedJuriesNumbers">
								<bean:write name="jury" property="teacherNumber"/>
							</html:multibox>	
						</td>						
					</tr>				
				</logic:iterate>
				<tr>
					<td colspan="4" align="right">
						<html:submit styleClass="inputbuttonSmall" property="method">
							<bean:message key="button.submit.masterDegree.thesis.removeJuries"/>
						</html:submit>
					</td>
				</tr>
			</logic:present >				
		<tr>
			<th align="left" colspan="4">
				<bean:message key="label.masterDegree.administrativeOffice.teacherNumber"/>:
				<html:text property="juriesNumbers" size="5" value="" />
				<html:submit styleClass="inputbuttonSmall" property="method">
					<bean:message key="button.submit.masterDegree.thesis.addJury"/>
				</html:submit>
				<html:errors property="guidersNumbers" />
			</th>
		</tr>
		<tr> 
			<td>&nbsp;</td>
		</tr>
		
		
		
		<tr>		
			<td colspan="4" align="center">
				<html:submit styleClass="inputbuttonSmall" property="method">
					<bean:message key="button.submit.masterDegree.thesis.changeProof"/>
				</html:submit>
				<html:submit styleClass="inputbuttonSmall" property="method">
					<bean:message key="button.cancel"/>
				</html:submit>
			</td>

		</tr>
		
		
	</table>
</html:form>


</center>