<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:form action="/editGrantOwner">

<logic:messagesPresent>
    <span class="error">
    	<html:errors/>
    </span>
    </logic:messagesPresent>
    <html:hidden property="method" value="doEdit"/>
  	<html:hidden property="page" value="1"/>
  	<table>
		<tr><td colspan="2"><b><bean:message key="label.grant.owner.information"/></b></td></tr>
  	    <tr>
			<td>
				<bean:message key="label.grant.owner.datesendcgd"/>
			</td>
			<td>
				<html:text property="dateSendCGD"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.grant.owner.cardcopynumber"/>
			</td>
			<td>
				<html:text property="cardCopyNumber"/>
			</td>
		</tr>
	</table>
	<br>
	<table>
		<tr><td colspan="2"><b><bean:message key="label.grant.owner.personalinformation"/></td></tr>

		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.name"/>
			</td>
			<td>
				<html:text property="name" size="70"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.sex"/>
			</td>
			<td>
				Masculino:&nbsp;<html:radio property="sex" value="1"/>&nbsp;&nbsp;
				Feminino:&nbsp;<html:radio property="sex" value="2"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.maritalStatus"/>
			</td>
			<td>
				<html:select property="maritalStatus">
					<html:options collection="maritalStatusList" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.birthdate"/>
			</td>
			<td>
				<html:text property="birthdate"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.fatherName"/>
			</td>
			<td>
				<html:text property="fatherName" size="70"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.motherName"/>
			</td>
			<td>
				<html:text property="motherName" size="70"/>
			</td>
		</tr>
		
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.districtBirth"/>
			</td>
			<td>
				<html:text property="districtBirth"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.parishOfBirth"/>
			</td>
			<td>
				<html:text property="parishOfBirth"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.districtSubBirth"/>
			</td>
			<td>
				<html:text property="districtSubBirth"/>
			</td>			
		</tr>
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.idNumber"/>
			</td>
			<td>
				<html:text property="idNumber"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.idType"/>
			</td>
			<td>
				<html:select property="idType">
					<html:options collection="documentTypeList" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.idLocation"/>
			</td>
			<td>
				<html:text property="idLocation"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.idDate"/>
			</td>
			<td>
				<html:text property="idDate"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.idValidDate"/>
			</td>
			<td>
				<html:text property="idValidDate"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.nationality"/>
			</td>
			<td>
				<html:text property="nationality"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.country"/>
			</td>
			<td>
				<html:text property="country"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.address"/>
			</td>
			<td>
				<html:text property="address"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.area"/>
			</td>
			<td>
				<html:text property="area"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.areaCode"/>
			</td>
			<td>
				<html:text property="areaCode"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.areaOfAreaCode"/>
			</td>
			<td>
				<html:text property="areaOfAreaCode"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.addressParish"/>
			</td>
			<td>
				<html:text property="addressParish"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.addressDistrictSub"/>
			</td>
			<td>
				<html:text property="addressDistrictSub"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.addressDistrict"/>
			</td>
			<td>
				<html:text property="addressDistrict"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.phone"/>
			</td>
			<td>
				<html:text property="phone"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.cellPhone"/>
			</td>
			<td>
				<html:text property="cellphone"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.email"/>
			</td>
			<td>
				<html:text property="email"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.homepage"/>
			</td>
			<td>
				<html:text property="homepage"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.socialSecurityNumber"/>
			</td>
			<td>
				<html:text property="socialSecurityNumber"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.profession"/>
			</td>
			<td>
				<html:text property="profession"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<bean:message key="label.grant.owner.infoperson.fiscalCode"/>
			</td>
			<td>
				<html:text property="fiscalCode"/>
			</td>
		</tr>
		
		
	</table>
	
		<html:submit styleClass="inputbutton">
			<bean:message key="button.create"/>
		</html:submit>
	</html:form>		
		
		<html:form action="/index.do">
			<html:submit styleClass="inputbutton">
				<bean:message key="button.back"/>
			</html:submit>
	</html:form>					
		<html:form action="/index.do">
			<html:submit styleClass="inputbutton">
				<bean:message key="button.cancel"/>
			</html:submit>
	</html:form>					

	