<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<strong><p align="center"><bean:message key="label.grant.contract.edition"/></p></strong><br/>

<html:form action="/editGrantContract" style="display:inline">
	<%-- Presenting errors --%>
	<logic:messagesPresent>
	<span class="error">
		<html:errors/>
	</span><br/>
	</logic:messagesPresent>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="doEdit"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<%-- contract --%>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idGrantContract" property="idGrantContract"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.contractNumber" property="contractNumber"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.grantContractOrientationTeacherId" property="grantContractOrientationTeacherId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.grantCostCenterId" property="grantCostCenterId"/>

	<%-- grant owner --%>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal"/>

	<%-- contract regime --%>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.grantContractRegimeId" property="grantContractRegimeId"/>

	<strong><p><bean:message key="label.grant.contract.information"/></p></strong>

	<table>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.orientationTeacher"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.grantContractOrientationTeacherNumber" property="grantContractOrientationTeacherNumber" size="10"/>
				<bean:message key="label.requiredfield"/>
				&nbsp;<html:link page='<%= "/showTeachersList.do?method=showForm" %>' target="_blank">
					<bean:message key="link.teacher.showList"/>
				</html:link>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.type"/>:&nbsp;
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.grantType" property="grantType">
					<html:options collection="grantTypeList" property="sigla" labelProperty="name"/>
				</html:select>*
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.regime.dateAcceptTerm"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.dateAcceptTerm" property="dateAcceptTerm" size="10"/>
				&nbsp;
				<bean:message key="label.dateformat"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.endMotive"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.endContractMotive" property="endContractMotive" size="40"/>
			</td>
		</tr>	
		
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.work.place"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.keyCostCenterNumber" property="keyCostCenterNumber" size="10"/>
			
	<%--		 Create a new Grant CostCenter 
			
			<html:link page="/editGrantCostCenter.do?method=prepareEditGrantCostCenterForm">
				<bean:message key="link.create.grant.costcenter"/>
			</html:link><br><br>--%>
			</td>
		</tr>	
	</table>

	<br/><br/><strong><p><bean:message key="label.grant.contract.regime.information"/></p></strong>

	<table>
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
				<bean:message key="label.requiredfield"/>&nbsp;
				<bean:message key="label.dateformat"/>
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
				<html:form action="/manageGrantContract" style="display:inline">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareManageGrantContractForm"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal" value='<%= request.getAttribute("idInternal").toString() %>'/>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" style="display:inline">
						<bean:message key="button.cancel"/>
					</html:submit>
				</html:form>
			</td>
		</tr>
	</table>