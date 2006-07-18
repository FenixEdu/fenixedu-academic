<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<strong><p align="center"><bean:message key="label.grant.contract.regime.edition"/></p></strong><br/>

<html:form action="/editGrantContractRegime" style="display:inline">
	<%-- Presenting errors --%>
	<logic:messagesPresent>
	<span class="error">
		<html:errors/>
	</span><br/>
	</logic:messagesPresent>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="doEdit"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<%-- contract --%>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idContract" property="idContract"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.contractNumber" property="contractNumber"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.grantCostCenterId" property="grantCostCenterId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.keyCostCenterNumber" property="keyCostCenterNumber"/>
	

	<%-- contract regime --%>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.grantContractRegimeId" property="grantContractRegimeId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.state" property="state"/>

	<table>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.orientationTeacher"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.grantContractRegimeTeacherNumber" property="grantContractRegimeTeacherNumber" size="10"/>
				&nbsp;<html:link page='<%= "/showTeachersList.do?method=showForm" %>' target="_blank">
					<bean:message key="link.teacher.showList"/>
				</html:link>
			</td>
		</tr>
	
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.regime.beginDate"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.dateBeginContract" property="dateBeginContract" size="10"/>
				<bean:message key="label.requiredfield"/>&nbsp;
				<bean:message key="label.dateformat"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.regime.endDate"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.dateEndContract" property="dateEndContract" size="10"/>
				<bean:message key="label.requiredfield"/>
				&nbsp;<bean:message key="label.dateformat"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.regime.dateSendDispatchCC"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.dateSendDispatchCC" property="dateSendDispatchCC" size="10"/>
				&nbsp;
				<bean:message key="label.dateformat"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.regime.dateDispatchCC"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.dateDispatchCC" property="dateDispatchCC"  size="10"/>
				&nbsp;
				<bean:message key="label.dateformat"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.regime.dateSendDispatchCD"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.dateSendDispatchCD" property="dateSendDispatchCD" size="10"/>
				&nbsp;
				<bean:message key="label.dateformat"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.regime.dateDispatchCD"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.dateDispatchCD" property="dateDispatchCD" size="10"/>
				&nbsp;
				<bean:message key="label.dateformat"/>
			</td>
		</tr>
		<tr>
		<td align="left">
			<bean:message key="label.grant.contract.work.place"/>:&nbsp;
		</td>
		<td>
			<bean:write name ="editGrantContractRegimeForm" property="keyCostCenterNumber" /> -- <bean:write name ="editGrantContractRegimeForm" property="designation" />
		
		<%-- Create a new Grant CostCenter 
		
		<html:link page="/editGrantCostCenter.do?method=prepareEditGrantCostCenterForm">
			<bean:message key="link.create.grant.costcenter"/>
		</html:link><br><br>--%>
		</td>
		</tr>	
		
	</table>

	<br/>

	<table>
		<tr>
			<td>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="button.save"/>
				</html:submit>
</html:form>
			</td>
			<td>
				<html:form action="/manageGrantContractRegime" style="display:inline">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareManageGrantContractRegime"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idContract" property="idContract" value='<%= request.getAttribute("idContract").toString() %>'/>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" style="display:inline">
						<bean:message key="button.cancel"/>
					</html:submit>
				</html:form>
			</td>
		</tr>
	</table>