<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.guide.reimbursementGuide.InfoReimbursementGuide" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.guide.reimbursementGuide.InfoReimbursementGuideSituation" %>
<%@ page import="net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>

<bean:define id="reimbursementGuide" name="<%= SessionConstants.REIMBURSEMENT_GUIDE %>" scope="request"/>
<bean:define id="number" name="reimbursementGuide" property="number" />
	
<center>
	<h2>
		<bean:message key="title.masterDegree.administrativeOffice.guide.reimbursementGuide.viewReimbursementGuideDetails" 
					arg0='<%= pageContext.findAttribute("number").toString() %>'/>
	</h2>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</center>

<table>

	<tr>
		<td><bean:message key="label.masterDegree.administrativeOffice.guide.reimbursementGuide.creationDate" />: </td>		
		<td>	
			<bean:define id="creationDate" name="reimbursementGuide" property="creationDate" />
			<%= Data.format2DayMonthYear(((Calendar) creationDate).getTime(), "-") %>				
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>				
	</tr>
	
	<tr>
		<td><strong><bean:message key="label.masterDegree.administrativeOffice.guide.reimbursementGuide.situations" />:</strong></td>				
	</tr>
		
	<tr align="center">
		<td><strong><bean:message key="label.masterDegree.administrativeOffice.guide.reimbursementGuide.state" /></strong></td>
		<td><strong><bean:message key="label.masterDegree.administrativeOffice.guide.reimbursementGuide.date" /></strong></td>
		<td colspan="2"><strong><bean:message key="label.masterDegree.administrativeOffice.guide.reimbursementGuide.remarks" /></strong></td>
	</tr>
		
	<logic:iterate id="reimbursementGuideSituation" name="reimbursementGuide" property="infoReimbursementGuideSituations">
		<tr align="center">
			<td>
				<bean:define id="stateName" value='<%= "label.reimbursementGuideState." + ((InfoReimbursementGuideSituation)reimbursementGuideSituation).getReimbursementGuideState().getName() %>'/>
				<bean:message name="stateName" />
			</td>
			<bean:define id="officialDate" name="reimbursementGuideSituation" property="officialDate" />
			<td>
				<%= Data.format2DayMonthYear(((Calendar) officialDate).getTime(), "-") %>
			</td> 						
			<td colspan="2">
				<bean:write name="reimbursementGuideSituation" property="remarks"/>
			</td>							
		</tr>			
	</logic:iterate>

	<tr>
		<td>&nbsp;</td>				
	</tr>
	
	<tr>
		<td><strong><bean:message key="label.masterDegree.administrativeOffice.guide.reimbursementGuide.entries" />:</strong></td>				
	</tr>
	
	<tr align="center">
		<td><strong><bean:message key="label.masterDegree.administrativeOffice.documentType" /></strong></td>
		<td><strong><bean:message key="label.masterDegree.administrativeOffice.description" /></strong></td>
		<td><strong><bean:message key="label.masterDegree.administrativeOffice.price" /></strong></td>
		<td><strong><bean:message key="label.masterDegree.administrativeOffice.guide.reimbursementGuide.justification" /></strong></td>
	</tr>
		
	<logic:iterate id="reimbursementGuideEntry" name="reimbursementGuide" property="infoReimbursementGuideEntries">
		<tr align="center">
			<td>
				<bean:define id="documentType"><bean:write name="reimbursementGuideEntry" property="infoGuideEntry.documentType"/></bean:define>
				<bean:message name="documentType" bundle="ENUMERATION_RESOURCES" />
			</td>
			<td><bean:write name="reimbursementGuideEntry" property="infoGuideEntry.description"/></td>		
			<td><bean:write name="reimbursementGuideEntry" property="value"/>&nbsp;<bean:message key="label.currencySymbol"/></td>							
			<td><bean:write name="reimbursementGuideEntry" property="justification"/></td>							
		</tr>	
	</logic:iterate>
	
</table>