<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoGuide" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoGuideEntry" %>
<%@ page import="net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<bean:define id="reimbursementGuide" name="<%= PresentationConstants.REIMBURSEMENT_GUIDE %>" scope="request"/>
<bean:define id="days" name="<%= PresentationConstants.MONTH_DAYS_KEY %>" scope="request"/>
<bean:define id="months" name="<%= PresentationConstants.MONTH_LIST_KEY %>" scope="request"/>
<bean:define id="years" name="<%= PresentationConstants.YEARS_KEY %>" scope="request"/>
<bean:define id="reimbursementGuideStates" name="<%= PresentationConstants.REIMBURSEMENT_GUIDE_STATES_LIST %>" scope="request"/>
<bean:define id="number" name="reimbursementGuide" property="number" />
	
<center>
	<h2>
		<bean:message key="title.masterDegree.administrativeOffice.guide.reimbursementGuide.editReimbursementGuideSituation" 
					arg0='<%= pageContext.findAttribute("number").toString() %>'/>
	</h2>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</center>

<html:form action="/editReimbursementGuideSituation.do?method=edit">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.id" property="id" />
	
	<table>
		
		<tr>
			<td>
				<bean:message key="label.masterDegree.administrativeOffice.guide.reimbursementGuide.newSituation" />
      		</td>
      		<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.situation" property="situation">
					<html:option value="">
						<bean:message key="label.choose.one"/>
					</html:option>						
					<logic:iterate id="state" name="reimbursementGuideStates">
						<bean:define id="stateName" name="state" property="name"/>
						<bean:define id="stateNameKEY" value="<%= "label.reimbursementGuideState." + stateName.toString() %>"/>						
						<html:option value="<%= stateName.toString() %>" >
							<bean:message name="stateNameKEY"/>
						</html:option>
					</logic:iterate>
				</html:select>
		
			</td>
		</tr>
					
		<tr>
			<td>
				<bean:message key="label.masterDegree.administrativeOffice.guide.reimbursementGuide.date" />
      		</td>
      		<td>				
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.officialDateDay" property="officialDateDay">
					<html:options collection="<%= PresentationConstants.MONTH_DAYS_KEY %>" property="value" labelProperty="label"/>
				</html:select>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.officialDateMonth" property="officialDateMonth">
					<html:options collection="<%= PresentationConstants.MONTH_LIST_KEY %>" property="value" labelProperty="label"/>
				</html:select>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.officialDateYear" property="officialDateYear">
					<html:options collection="<%= PresentationConstants.YEARS_KEY %>" property="value" labelProperty="label"/>
				</html:select>				
			</td>          
		</tr>
		<tr>
			<td>
				<bean:message key="label.masterDegree.administrativeOffice.guide.reimbursementGuide.remarks" />
      		</td>
      		<td>	
				<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.remarks" property="remarks"/>
			</td>				
		</tr>		
		<tr>
			<td>&nbsp;</td>				
		</tr>		
		<tr>
			<td colspan="2" align="center" >
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">				
					<bean:message key="button.submit.masterDegree.reimbursementGuide.edit"/>
				</html:submit>
			</td>		
		</tr>
	</table>         
</html:form>         
