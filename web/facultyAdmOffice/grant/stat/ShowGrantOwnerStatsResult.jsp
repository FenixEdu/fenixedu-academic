<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<logic:present name="infoStatGrantOwner">
<p class="infoselected">

	<b><bean:message key="label.grant.stat.grantowner.selection"/></b><br/>
	
	<logic:present name="filterType">
	<bean:message key="label.grant.stat.grantowner.filterType"/>:&nbsp;
		<logic:equal name="filterType" value="1">
			<bean:message key="label.stat.byCriteria.grant.owner.radio.all"/>
		</logic:equal>
		<logic:equal name="filterType" value="2">
			<bean:message key="label.stat.byCriteria.grant.owner.radio.justActive"/>
		</logic:equal>
		<logic:equal name="filterType" value="3">
			<bean:message key="label.stat.byCriteria.grant.owner.radio.justDesactive"/>
		</logic:equal>
	</logic:present><br/>

	<logic:present name="infoStatGrantOwner" property="dateBeginContract">
		<bean:message key="label.stat.byCriteria.grant.owner.dateBegin"/>:&nbsp;
        <dt:format pattern="dd-MM-yyyy">
			<bean:write name="infoStatGrantOwner" property="dateBeginContract.time"/>
		</dt:format><br/>
	</logic:present>

	<logic:present name="infoStatGrantOwner" property="dateEndContract">
		<bean:message key="label.stat.byCriteria.grant.owner.dateEnd"/>:&nbsp;
		<dt:format pattern="dd-MM-yyyy">
			<bean:write name="infoStatGrantOwner" property="dateEndContract.time"/>
		</dt:format><br/>
	</logic:present>

	<logic:present name="infoStatGrantOwner" property="grantTypeSigla">
		<bean:message key="label.stat.byCriteria.grant.contract.grantType"/>:&nbsp;
		<bean:write name="infoStatGrantOwner" property="grantTypeSigla"/><br/>
	</logic:present>
</p>
</logic:present>
<br/>
	
<b><bean:message key="label.grant.stat.grantowner.result"/>:</b><br/>

<logic:present name="infoStatResultGrantOwner">
<table>
<tr>
	<td colspan="2">
		&nbsp;
	</td>
</tr>
<tr>    
	<td>
		<bean:message key="label.stat.grantowner.totalValue"/>:&nbsp;
	</td>
	<td>
		<bean:write name="infoStatResultGrantOwner" property="totalNumberOfGrantOwners"/>
	</td>
</tr>
<tr>
	<td>
		<bean:message key="label.stat.grantowner.numberOfResultsOfCriteria"/>:&nbsp;
	</td>
	<td>
		<bean:write name="infoStatResultGrantOwner" property="numberOfGrantOwnerByCriteria"/>
	</td>
</tr>
<tr>
<td>
	<bean:message key="label.stat.grantowner.resultpercentage"/>:&nbsp;
</td>
<td>
	<bean:write name="infoStatResultGrantOwner" property="percentageGrantOwnerResult"/><bean:message key="label.percentage"/>
</td>
</tr>

<tr>
<td colspan="2">
	&nbsp;
</td>
</tr>
<tr>
	<td>
		<bean:message key="label.stat.grantcontract.totalValue"/>:&nbsp;
	</td>
	<td>
		<bean:write name="infoStatResultGrantOwner" property="totalNumberOfGrantContracts"/>
	</td>
</tr>
<tr>
	<td>
		<bean:message key="label.stat.grantcontract.numberOfResultsOfCriteria"/>:&nbsp;
	</td>
	<td>
		<bean:write name="infoStatResultGrantOwner" property="numberOfGrantContractsByCriteria"/>
	</td>
</tr>
<tr>
<td>
	<bean:message key="label.stat.grantcontract.resultpercentage"/>:&nbsp;
</td>
<td>
	<bean:write name="infoStatResultGrantOwner" property="percentageGrantContractResult"/><bean:message key="label.percentage"/>
</td>
</tr>


</table>
</logic:present>