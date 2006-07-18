<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<html:form action="/searchGrantOwner">

	<p><b><bean:message key="info.grant.searchform.name.and.id"/></b></p>

	<table class="infoop">
		<tr>
			<td rowspan=4><p class="emphasis-box">i</p></td>
	  		<td><bean:message key="info.grant.searchform.by.firstname"/></td>
		</tr>
	 	<tr>
	 		<td><bean:message key="info.grant.searchform.by.lastname"/></td>
	 	</tr>
	 	<tr>
	 		<td><bean:message key="info.grant.searchform.by.name"/></td>
	 	</tr>
	 	<tr>
	 		<td><bean:message key="info.grant.searchform.only.grants"/></td>
	 	</tr>
	</table><br/>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="doSearch"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.startIndex" property="startIndex" value="0"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<%-- Presenting Errors --%>
	<logic:messagesPresent>
	<p align="center"><span class="error">
	<html:errors/>
	</span></p><br/>
	</logic:messagesPresent>    
	
	<%-- Search Form By Identification Information --%>
	<table>
		<tr>
			<td align="left"><bean:message key="label.grant.owner.name"/>:&nbsp;</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" size="70"/></td>
		</tr>
		<tr>
			<td align="left"><bean:message key="label.grant.owner.idNumber"/>:&nbsp;</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.idNumber" property="idNumber" size="15"/></td>
		</tr>
		<tr>
			<td align="left"><bean:message key="label.grant.owner.idType"/>:&nbsp;</td>
			<td>
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.person.IDDocumentType"/>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.idType" property="idType">
					<html:option key="dropDown.Default" value=""/>
					<html:options collection="values" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td align="left"><bean:message key="label.search.just.grant.owner"/>:&nbsp;</td>
			<td><html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.justGrantOwner" property="justGrantOwner"></html:checkbox></td>
		</tr>
	</table>
	
	<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.search"/>
	</html:submit></p>
</html:form>

<br/><br/>

<html:form action="/searchGrantOwnerByNumber" style="display:inline">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="searchGrantOwner"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<p><b><bean:message key="info.grant.searchform.grantowner.number"/></b></p>

	<%-- Search Form By Grant Owner number --%>	
	<table>
		<tr>
			<td align="left"><bean:message key="label.grant.owner.number"/>:&nbsp;</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.grantOwnerNumber" property="grantOwnerNumber"/></td>
		</tr>
	</table>
	
	<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.search"/>
	</html:submit></p>
</html:form>
