<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<em><bean:message key="label.facultyAdmOffice.portal.name"/></em>
<h2><bean:message key="label.grant.project.edition"/></h2>


<html:form action="/editGrantProject" style="display:inline">

	<%-- Presenting errors --%>
	<logic:messagesPresent>
		<html:errors/>
	</logic:messagesPresent>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="doEdit"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<%-- grant project --%>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal"/>
 
	<table class="tstyle5">
		<tr>
			<td>
				<bean:message key="label.grant.project.number"/>:
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.number" property="number"/> <bean:message key="label.requiredfield"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.grant.project.designation"/>:
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.designation" property="designation" size="80"/> <bean:message key="label.requiredfield"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.grant.project.responsibleTeacher.number"/>:
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.responsibleTeacherNumber" property="responsibleTeacherNumber" size="10"/> <bean:message key="label.requiredfield"/>
				<html:link page='<%= "/showTeachersList.do?method=showForm" %>' target="_blank">
					<bean:message key="link.teacher.showList"/>
				</html:link>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.grant.project.grantCostCenter.number"/>:
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.grantCostCenterNumber" property="grantCostCenterNumber"/> <bean:message key="label.requiredfield"/>
				<html:link page='<%= "/showPaymentEntitiesList.do?method=showForm&amp;costcenter=1" %>' target="_blank">
					<bean:message key="link.grantcostcenter.showList"/>
				</html:link>
			</td>
		</tr>		
	</table>



	<table>
		<tr>
			<td>
				<%-- Save button (edit/create) --%>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="button.save"/>
				</html:submit>
</html:form>
			</td>
			<td>
				<html:form action="/manageGrantProject" style="display:inline">
					<%-- button cancel --%>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareManageGrantProject"/>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" style="display:inline">
						<bean:message key="button.cancel"/>
					</html:submit>
				</html:form>
			</td>
		</tr>
	</table>