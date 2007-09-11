<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<html:form action="/searchGrantOwner">

	<em><bean:message key="label.facultyAdmOffice.portal.name"/></em>
	<h2><bean:message key="link.search.grant.owner"/></h2>
	
	<p><b><bean:message key="info.grant.searchform.name.and.id"/></b></p>

	<div class="infoop2">
		<bean:message key="info.grant.searchform.by.firstname"/>
		<bean:message key="info.grant.searchform.by.lastname"/>
		<bean:message key="info.grant.searchform.by.name"/>
		<bean:message key="info.grant.searchform.only.grants"/>
	</div>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="doSearch"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.startIndex" property="startIndex" value="0"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<%-- Presenting Errors --%>
	<logic:messagesPresent>
		<div class="mtop15">
			<html:errors/>
		</div>
	</logic:messagesPresent>    
	
	<%-- Search Form By Identification Information --%>
	<table class="tstyle5 mbottom05">
		<tr>
			<td><bean:message key="label.grant.owner.name"/>:&nbsp;</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" size="70"/></td>
		</tr>
		<tr>
			<td><bean:message key="label.grant.owner.idNumber"/>:&nbsp;</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.idNumber" property="idNumber" size="15"/></td>
		</tr>
		<tr>
			<td><bean:message key="label.grant.owner.idType"/>:&nbsp;</td>
			<td>
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.person.IDDocumentType"/>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.idType" property="idType">
					<html:option key="dropDown.Default" value=""/>
					<html:options collection="values" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td><bean:message key="label.search.just.grant.owner"/>:&nbsp;</td>
			<td><html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.justGrantOwner" property="justGrantOwner"></html:checkbox></td>
		</tr>
	</table>
	
	<p class="mtop05">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.search"/>
		</html:submit>
	</p>
</html:form>

<html:form action="/searchGrantOwnerByNumber" style="display:inline">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="searchGrantOwner"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<p class="mtop15 mbottom05"><b><bean:message key="info.grant.searchform.grantowner.number"/></b></p>

	<%-- Search Form By Grant Owner number --%>	
	<table class="tstyle5 mvert05">
		<tr>
			<td><bean:message key="label.grant.owner.number"/>:</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.grantOwnerNumber" property="grantOwnerNumber"/></td>
		</tr>
	</table>
	
	<p class="mtop05">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.search"/>
		</html:submit>
	</p>
</html:form>
