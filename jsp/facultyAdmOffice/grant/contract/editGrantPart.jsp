<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<strong><p align="center"><bean:message key="label.grant.part.edition"/></p></strong><br/>

<html:form action="/editGrantPart" style="display:inline">

	<%-- Presenting errors --%>
	<logic:messagesPresent>
	<span class="error">
		<html:errors/>
	</span><br/>
	</logic:messagesPresent>

	<html:hidden property="method" value="doEdit"/>
	<html:hidden property="page" value="1"/>

	<%-- grant type --%>
	<html:hidden property="idInternal"/>
	<html:hidden property="grantSubsidyId"/>

	<table>
		<tr>
			<td align="left">
				<bean:message key="label.grant.part.percentage"/>:&nbsp;
			</td>
			<td>
				<html:text property="percentage" size="5"/><bean:message key="label.requiredfield"/>
			</td>
		</tr>
		<tr>
			<td align="left" colspan="2">
				<bean:message key="label.grant.part.grantPaymentEntity.designation"/> *&nbsp;
			</td>
		</tr>
		<tr>
			<td align="left">
				&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="label.grant.paymententity.project"/>&nbsp;
			</td>
			<td>
				<html:text property="project" size="10"/>&nbsp; 
					<html:link page='<%= "/showPaymentEntitiesList.do?method=showForm&amp;project=1" %>' target="_blank">
						<bean:message key="link.grantproject.showList"/>
					</html:link>
			</td>
		</tr>
		<tr>
			<td align="left">
				&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="label.grant.paymententity.costcenter"/>&nbsp;
			</td>
			<td>
				<html:text property="costCenter" size="10"/>&nbsp; 
				<html:link page='<%= "/showPaymentEntitiesList.do?method=showForm&amp;costcenter=1" %>' target="_blank">
					<bean:message key="link.grantcostcenter.showList"/>
				</html:link>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.part.responsibleTeacher.number"/>:&nbsp;
			</td>
			<td>
				<html:text property="responsibleTeacherNumber" size="10"/>&nbsp;
				<html:link page='<%= "/showTeachersList.do?method=showForm" %>' target="_blank">
					<bean:message key="link.teacher.showList"/>
				</html:link>
			</td>
		</tr>
	</table>

	<br/>

	<table>
		<tr>
			<td>
				<%-- Save button (edit/create) --%>
				<html:submit styleClass="inputbutton">
					<bean:message key="button.save"/>
				</html:submit>
</html:form>
			</td>
			<td>
				<html:form action="/manageGrantPart" style="display:inline">
					<%-- button cancel --%>
					<html:hidden property="method" value="prepareManageGrantPart"/>
					<html:hidden property="idSubsidy" value='<%= request.getAttribute("idSubsidy").toString() %>'/>
					<html:submit styleClass="inputbutton" style="display:inline">
						<bean:message key="button.cancel"/>
					</html:submit>
				</html:form>
			</td>
		</tr>
	</table>