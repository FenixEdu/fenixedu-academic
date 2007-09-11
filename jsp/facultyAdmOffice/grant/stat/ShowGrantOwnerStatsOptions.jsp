<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<em><bean:message key="label.facultyAdmOffice.portal.name"/></em>
<h2><bean:message key="label.grant.owner.stats.search"/></h2>

<div class="infoop2"> 
	<p><b><bean:message key="label.grant.owner.stats"/></b></p>
	<p><bean:message key="message.grant.stat.grantcontractbycriteria.resume"/></p>
	<p><bean:message key="message.grant.stat.grantcontractbycriteria.options"/></p>
	<p><bean:message key="message.grant.stat.grantcontractbycriteria.optionsselected"/></p>
</div>


<html:form action="/grantOwnerStats" style="display:inline">

<%-- Presenting errors --%>
<logic:messagesPresent>
	<html:errors/>
</logic:messagesPresent>

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="doStat"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

<table class="tstyle5 thlight thright">
	<tr>
		<th>
			<bean:message key="label.stat.byCriteria.grant.owner.radio.all"/>:&nbsp;
		</th>
		<td>
			<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.filterType" name="grantOwnerStatsForm" property="filterType"  value="1"/>
		</td>
	</tr>
   <tr>
		<th>
			<bean:message key="label.stat.byCriteria.grant.owner.radio.justActive"/>:&nbsp;
		</th>
		<td>
			<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.filterType" name="grantOwnerStatsForm" property="filterType"  value="2"/>
		</td>
	</tr>
	<tr>
		<th>
			<bean:message key="label.stat.byCriteria.grant.owner.radio.justDesactive"/>:&nbsp;
		</th>
		<td>
			<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.filterType" name="grantOwnerStatsForm" property="filterType"  value="3"/>			
		</td>
	</tr>
</table>


<table class="tstyle5 thlight thright">
	<tr>
		<th>
			<bean:message key="label.stat.byCriteria.grant.owner.dateBegin"/>:&nbsp;
		</th>
		<td colspan="2">
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.beginContract" property="beginContract" size="10"/>&nbsp;<bean:message key="label.dateformat"/>
		</td>
	</tr>
	<tr>
		<th>
			<bean:message key="label.stat.byCriteria.grant.owner.dateEnd"/>:&nbsp;
		</th>
		<td colspan="2">
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.endContract" property="endContract" size="10"/>&nbsp;<bean:message key="label.dateformat"/>
		</td>
	</tr>
	<tr>
		<th>
			<bean:message key="label.stat.byCriteria.grant.contract.grantType"/>:&nbsp;
		</th>
		<td>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.grantType" property="grantType">
				<html:options collection="grantTypeList" property="idInternal" labelProperty="sigla"/>
			</html:select>
		</td>
	</tr>
</table>


<%-- Search button --%>
<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.stat"/>
	</html:submit>
</p>

</html:form>
