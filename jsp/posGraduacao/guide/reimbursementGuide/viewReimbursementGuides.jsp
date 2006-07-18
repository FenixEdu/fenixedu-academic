<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoGuide" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoGuideEntry" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.guide.reimbursementGuide.InfoReimbursementGuideSituation" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.guide.reimbursementGuide.InfoReimbursementGuide" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>
<%@ page import="net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState" %>


<center>
	<h2><bean:message key="title.masterDegree.administrativeOffice.guide.reimbursementGuide.viewReimbursementGuides"/></h2>
	<span class="error"><html:errors/></span>

	<bean:define id="reimbursementGuidesList" name="<%= SessionConstants.REIMBURSEMENT_GUIDES_LIST %>" type="java.util.List"/>
	
	<table>
		
		<tr align="center">
			<td><strong><bean:message key="label.number" /></strong></td>
			<td><strong><bean:message key="label.masterDegree.administrativeOffice.guide.reimbursementGuide.creationDate" /></strong></td>
			<td><strong><bean:message key="label.masterDegree.administrativeOffice.guide.reimbursementGuide.state" /></strong></td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>	
		
		<logic:iterate id="reimbursementGuide" name="reimbursementGuidesList">
			<tr>
				<bean:define id="linkViewReimbursementGuideDetails">
					/viewReimbursementGuideDetails.do?method=view&id=<bean:write name="reimbursementGuide" property="idInternal"/>
				</bean:define>
				<bean:define id="linkPrintReimbursementGuide">
					/printReimbursementGuide.do?method=print&id=<bean:write name="reimbursementGuide" property="idInternal"/>
				</bean:define>
				<bean:define id="linkEditReimbursementGuideSituation">
					/editReimbursementGuideSituation.do?method=prepare&id=<bean:write name="reimbursementGuide" property="idInternal"/>
				</bean:define>
				
				
				<td align="center"><bean:write name="reimbursementGuide" property="number"/></td>
				
				<bean:define id="creationDate" name="reimbursementGuide" property="creationDate" />
				<td align="center"><%= Data.format2DayMonthYear(((Calendar) creationDate).getTime(), "-") %></td> 
				
				<bean:define id="stateName" value="<%= "label.reimbursementGuideState." + ((InfoReimbursementGuide)reimbursementGuide).getActiveInfoReimbursementGuideSituation().getReimbursementGuideState().getName() %>"/>
				<td align="center"><bean:message name="stateName"/></td>
						
				<td align="center">
					<html:link page="<%= linkViewReimbursementGuideDetails %>" ><bean:message key="link.masterDegree.administrativeOffice.viewDetails"/></html:link>
				</td>
				
				<td align="center">
					<html:link page="<%= linkPrintReimbursementGuide %>" target="_blank"><bean:message key="link.masterDegree.administrativeOffice.printShort"/></html:link>
				</td>
				
				<%
					if( (((InfoReimbursementGuide)reimbursementGuide).getActiveInfoReimbursementGuideSituation().getReimbursementGuideState() != ReimbursementGuideState.PAYED) 
						&& (((InfoReimbursementGuide)reimbursementGuide).getActiveInfoReimbursementGuideSituation().getReimbursementGuideState() != ReimbursementGuideState.ANNULLED))
					{
				%>
				<td align="center">
					<html:link page="<%= linkEditReimbursementGuideSituation %>"><bean:message key="link.masterDegree.administrativeOffice.editShort"/></html:link>
				</td>			
				<%
					}			
				%>
													
			</tr>				
		</logic:iterate>
	
	
	</table>

</center>