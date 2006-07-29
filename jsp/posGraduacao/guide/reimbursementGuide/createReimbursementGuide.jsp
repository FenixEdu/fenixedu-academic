<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoGuide" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoGuideEntry" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<center>
	<h2><bean:message key="title.masterDegree.administrativeOffice.guide.reimbursementGuide.createReimbursementGuide"/></h2>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</center>

<bean:define id="guide" name="<%= SessionConstants.GUIDE %>" scope="request"/>



<html:form action="/createReimbursementGuide.do?method=create">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
 	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.number" property="number" />
 	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.year" property="year" />
 	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.version" property="version" />	 	
	
	<table width="100%" >
		<tr align="center">
			<td><strong><bean:message key="label.masterDegree.administrativeOffice.documentType" /></strong></td>
			<td colspan="2"><strong><bean:message key="label.masterDegree.administrativeOffice.description" /></strong></td>
			<td><strong><bean:message key="label.masterDegree.administrativeOffice.quantity" /></strong></td>
			<td><strong><bean:message key="label.masterDegree.administrativeOffice.price" /></strong></td>
			<td><strong><bean:message key="label.masterDegree.administrativeOffice.totalPrice" /></strong></td>
			<td><strong><bean:message key="label.masterDegree.administrativeOffice.reimbursementValue" /></strong></td>
		</tr>
		<logic:iterate id="guideEntry" name="guide" property="infoGuideEntries">           
			<tr>
				<td>&nbsp;</td>				
			</tr>	
			<tr>
				<td align="center">
					<bean:define id="documentType"><bean:write name="guideEntry" property="documentType"/></bean:define>
					<bean:message name="documentType" bundle="ENUMERATION_RESOURCES" />
				</td>
				<td colspan="2"><bean:write name="guideEntry" property="description"/></td>
				<td align="center"><bean:write name="guideEntry" property="quantity"/></td>
				<td align="right"><bean:write name="guideEntry" property="price"/> <bean:message key="label.currencySymbol" /></td>
				<bean:define id="price" name="guideEntry" property="price" />
				<bean:define id="quantity" name="guideEntry" property="quantity" />	
				<td align="right"><%= Double.parseDouble(price.toString()) * Double.parseDouble(quantity.toString()) %> <bean:message key="label.currencySymbol" /></td>
				<td align="center"><html:text bundle="HTMLALT_RESOURCES" altKey="text.values" property="values" size="8" value="0.00" />&nbsp;<bean:message key="label.currencySymbol"/></td>		
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td align="left" valign="top" colspan="6">
					<strong><bean:message key="label.masterDegree.administrativeOffice.guide.reimbursementGuide.justification" />: </strong>
					<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.justifications" property="justifications"/>
				</td>			
			</tr>
		</logic:iterate>
		<tr>
			<td>&nbsp;</td>				
		</tr>
		<tr>
			<td>&nbsp;</td>				
		</tr>
		<tr>
			<td align="center" valign="top"><strong><bean:message key="label.masterDegree.administrativeOffice.guide.reimbursementGuide.remarks" />: </strong></td>
			<td colspan="5" align="left"><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.remarks" property="remarks"/></td>				
		</tr>
		<tr>
			<td>&nbsp;</td>				
		</tr>	
		<tr>
			<td>&nbsp;</td>				
		</tr>				
		<tr>
			<td colspan="7" align="center">
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">				
					<bean:message key="button.submit.masterDegree.reimbursementGuide.reimburse"/>
				</html:submit>
			</td>		
		</tr>
	</table>         
</html:form>         
