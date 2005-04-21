<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<br/><strong><p align="center"><bean:message key="label.grant.owner.edition"/></p></strong><br/>

<html:form action="/editGrantOwner" style="display:inline">

	<%-- Presenting Errors--%>
	<logic:messagesPresent>
	<span class="error">
		<html:errors/>
	</span><br/><br/>
	</logic:messagesPresent>

<html:hidden property="method" value="doEdit"/>
<html:hidden property="page" value="1"/>

<%-- grant owner --%>
<html:hidden property="idGrantOwner"/>
<html:hidden property="grantOwnerNumber"/>

<%-- person --%>
<html:hidden property="personUsername"/>
<html:hidden property="password"/>
<html:hidden property="idInternalPerson"/>

<table>
	<tr>
		<td colspan="2"><b><bean:message key="label.grant.owner.information"/></b></td>
	</tr>
	<tr>
		<td align="left"><bean:message key="label.grant.owner.dateSendCGD"/>:&nbsp;</td>
		<td><html:text property="dateSendCGD" size="10"/>&nbsp;<bean:message key="label.dateformat"/></td>
	</tr>
	<tr>
		<td align="left"><bean:message key="label.grant.owner.cardCopyNumber"/>:&nbsp;</td>
		<td><html:text property="cardCopyNumber" size="3"/></td>
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
			<html:text property="idNumber"/><bean:message key="label.requiredfield"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.idType"/>:&nbsp;
		</td>
		<td>
			<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.person.IDDocumentType"/>
			<html:select property="idType">
				<html:option key="dropDown.Default" value=""/>
				<html:options collection="values" property="value" labelProperty="label"/>
			</html:select><bean:message key="label.requiredfield"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.idLocation"/>:&nbsp;
		</td>
		<td>
			<html:text property="idLocation" size="30"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.idDate"/>:&nbsp;
		</td>
		<td>
			<html:text property="idDate" size="10"/><bean:message key="label.requiredfield"/>
			&nbsp;<bean:message key="label.dateformat"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.idValidDate"/>:&nbsp;
		</td>
		<td>
			<html:text property="idValidDate" size="10"/><bean:message key="label.requiredfield"/>
			&nbsp;<bean:message key="label.dateformat"/>
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
		<td><html:text property="name" size="70"/><bean:message key="label.requiredfield"/></td>
	</tr>
	<tr>
		<td align="left"><bean:message key="label.grant.owner.infoperson.sex"/>:&nbsp;</td>
		<td>
			Masculino:&nbsp;<html:radio property="sex" value="MALE"/>&nbsp;&nbsp;
			Feminino:&nbsp;<html:radio property="sex" value="FEMALE"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.maritalStatus"/>:&nbsp;
		</td>
		<td>
			<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.person.MaritalStatus"/>
			<html:select property="maritalStatus">
        		<html:option key="dropDown.Default" value="null"/>
            	<html:options collection="values" property="value" labelProperty="label"/>
			</html:select>            
		</td>
	</tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.birthdate"/>:&nbsp;
		</td>
		<td>
			<html:text property="birthdate" size="10"/><bean:message key="label.requiredfield"/>
			&nbsp;<bean:message key="label.dateformat"/>
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
			<html:text property="districtBirth" size="30"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.parishOfBirth"/>:&nbsp;
		</td>
		<td>
			<html:text property="parishOfBirth" size="30"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.districtSubBirth"/>:&nbsp;
		</td>
		<td>
			<html:text property="districtSubBirth" size="30"/>
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
			<html:text property="address" size="70"/>
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
			<html:text property="areaCode" size="8"/>&nbsp;(1234-567)
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
			<html:text property="addressParish" size="30"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.addressDistrictSub"/>:&nbsp;
		</td>
		<td>
			<html:text property="addressDistrictSub" size="30"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.addressDistrict"/>:&nbsp;
		</td>
		<td>
			<html:text property="addressDistrict" size="30"/>
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
			<bean:message key="label.grant.owner.infoperson.workphone"/>:&nbsp;
		</td>
		<td>
			<html:text property="workphone"/>
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
			<html:text property="email" size="60"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.homepage"/>:&nbsp;
		</td>
		<td>
			<html:text property="homepage" size="60"/>
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
			<html:text property="profession" size="30"/>
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
			<logic:present name="idInternal">
				<html:form action="/manageGrantOwner" style="display:inline">
					<html:hidden property="method" value="prepareManageGrantOwnerForm"/>
					<html:hidden property="page" value="1"/>
					<html:hidden property="idInternal" value='<%= request.getAttribute("idInternal").toString() %>'/>
					<html:submit styleClass="inputbutton" style="display:inline">
						<bean:message key="button.cancel"/>
					</html:submit>
				</html:form>					
			</logic:present>
		</td>
	</tr>
</table>
	