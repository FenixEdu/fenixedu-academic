<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<br/><strong><p align="center">Edição de Bolseiro</p></strong>
<table class="listClasses" align="center">
<tr><td>Preencha correctamente o formulário (não deixando campos obrigatórios em branco).</td></tr></table>
<br/><br/>
<html:form action="/editGrantOwner" style="display:inline">

	<%-- Presenting Errors--%>
	<logic:messagesPresent>
	<span class="error">
		<html:errors/>
	</span><br/><br/>
	</logic:messagesPresent>

<html:hidden property="method" value="doEdit"/>
<html:hidden property="page" value="1"/>

<%-- person --%>
<html:hidden property="idInternal"/>
<html:hidden property="grantOwnerNumber"/>

<%-- grant owner --%>
<html:hidden property="personUsername"/>
<html:hidden property="password"/>
<html:hidden property="idInternalPerson"/>

<table>
	<tr>
		<td colspan="2" ><b><bean:message key="label.grant.owner.information"/></b></td>
	</tr>
	<tr>
		<td align="left"><bean:message key="label.grant.owner.dateSendCGD"/>:&nbsp;</td>
		<td><html:text property="dateSendCGD"/></td>
	</tr>
	<tr>
		<td align="left"><bean:message key="label.grant.owner.cardCopyNumber"/>:&nbsp;</td>
		<td><html:text property="cardCopyNumber"/></td>
	</tr>
</table>

<br/>

<table>
	<tr>
		<td colspan="2" ><b><bean:message key="label.grant.owner.infoperson.idinformation"/></td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.idNumber"/>:&nbsp;
		</td>
		<td>
			<html:text property="idNumber"/>*
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.idType"/>:&nbsp;
		</td>
		<td>
			<html:select property="idType">
				<html:options collection="documentTypeList" property="value" labelProperty="label"/>
			</html:select>*
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.idLocation"/>:&nbsp;
		</td>
		<td>
			<html:text property="idLocation"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.idDate"/>:&nbsp;
		</td>
		<td>
			<html:text property="idDate"/><dd-mm-aaaa>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.idValidDate"/>:&nbsp;
		</td>
		<td>
				<html:text property="idValidDate"/><dd-mm-aaaa>
		</td>
	</tr>
</table>

<br/>

<table>
	<tr>
		<td colspan="2" ><b><bean:message key="label.grant.owner.personalinformation"/></td>
	</tr>
	<tr>
		<td align="left"><bean:message key="label.grant.owner.infoperson.name"/>:&nbsp;</td>
		<td><html:text property="name" size="70"/></td>
	</tr>
	<tr>
		<td align="left"><bean:message key="label.grant.owner.infoperson.sex"/>:&nbsp;</td>
		<td>
			Masculino:&nbsp;<html:radio property="sex" value="1"/>&nbsp;&nbsp;
			Feminino:&nbsp;<html:radio property="sex" value="2"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.maritalStatus"/>:&nbsp;
		</td>
		<td>
			<html:select property="maritalStatus">
				<html:options collection="maritalStatusList" property="value" labelProperty="label"/>
			</html:select>
		</td>
	</tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.birthdate"/>:&nbsp;
		</td>
		<td>
			<html:text property="birthdate"/><dd-mm-aaaa>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.fatherName"/>:&nbsp;
		</td>
		<td>
			<html:text property="fatherName" size="70"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.motherName"/>:&nbsp;
		</td>
		<td>
			<html:text property="motherName" size="70"/>
		</td>
	</tr>		
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.districtBirth"/>:&nbsp;
		</td>
		<td>
			<html:text property="districtBirth"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.parishOfBirth"/>:&nbsp;
		</td>
		<td>
			<html:text property="parishOfBirth"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.districtSubBirth"/>:&nbsp;
		</td>
		<td>
			<html:text property="districtSubBirth"/>
		</td>			
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.nationality"/>:&nbsp;
		</td>
		<td>
			<html:text property="nationality"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.country"/>:&nbsp;
		</td>
		<td>
			<html:select property="country">
				<html:options collection="countryList" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.address"/>:&nbsp;
		</td>
		<td>
			<html:text property="address"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.area"/>:&nbsp;
		</td>
		<td>
			<html:text property="area"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.areaCode"/>:&nbsp;
		</td>
		<td>
			<html:text property="areaCode"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.areaOfAreaCode"/>:&nbsp;
		</td>
		<td>
			<html:text property="areaOfAreaCode"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.addressParish"/>:&nbsp;
		</td>
		<td>
			<html:text property="addressParish"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.addressDistrictSub"/>:&nbsp;
		</td>
		<td>
			<html:text property="addressDistrictSub"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.addressDistrict"/>:&nbsp;
		</td>
		<td>
			<html:text property="addressDistrict"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.phone"/>:&nbsp;
		</td>
		<td>
			<html:text property="phone"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.cellPhone"/>:&nbsp;
		</td>
		<td>
			<html:text property="cellphone"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.email"/>:&nbsp;
		</td>
		<td>
			<html:text property="email"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.homepage"/>:&nbsp;
		</td>
		<td>
			<html:text property="homepage"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.socialSecurityNumber"/>:&nbsp;
		</td>
		<td>
			<html:text property="socialSecurityNumber"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.profession"/>:&nbsp;
		</td>
		<td>
			<html:text property="profession"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.fiscalCode"/>:&nbsp;
		</td>
		<td>
			<html:text property="fiscalCode"/>
		</td>
	</tr>
</table>	

<br/>

<table>
	<tr>
		<td>
			<html:submit styleClass="inputbutton">
				<bean:message key="button.save"/>
			</html:submit>
		</html:form>		
		</td>
		<td>
			<logic:present name="idInternalGrantOwner">
				<html:form action="/manageGrantOwner.do?method=prepareManageGrantOwnerForm" style="display:inline">
					<html:hidden property="idInternal" value='<%= request.getAttribute("idInternalGrantOwner").toString() %>'/>
					<html:submit styleClass="inputbutton" style="display:inline">
						<bean:message key="button.cancel"/>
					</html:submit>
				</html:form>					
			</logic:present>
		</td>
	</tr>
</table>
	