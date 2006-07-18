<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
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

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="doEdit"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

<%-- grant owner --%>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idGrantOwner" property="idGrantOwner"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.grantOwnerNumber" property="grantOwnerNumber"/>

<%-- person --%>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.personUsername" property="personUsername"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.password" property="password"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternalPerson" property="idInternalPerson"/>

<table>
	<tr>
		<td colspan="2"><b><bean:message key="label.grant.owner.information"/></b></td>
	</tr>
	<tr>
		<td align="left"><bean:message key="label.grant.owner.dateSendCGD"/>:&nbsp;</td>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.dateSendCGD" property="dateSendCGD" size="10"/>&nbsp;<bean:message key="label.dateformat"/></td>
	</tr>
	<tr>
		<td align="left"><bean:message key="label.grant.owner.cardCopyNumber"/>:&nbsp;</td>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.cardCopyNumber" property="cardCopyNumber" size="3"/></td>
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
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.idNumber" property="idNumber"/><bean:message key="label.requiredfield"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.idType"/>:&nbsp;
		</td>
		<td>
			<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.person.IDDocumentType"/>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.idType" property="idType">
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
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.idLocation" property="idLocation" size="30"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.idDate"/>:&nbsp;
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.idDate" property="idDate" size="10"/><bean:message key="label.requiredfield"/>
			&nbsp;<bean:message key="label.dateformat"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.idValidDate"/>:&nbsp;
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.idValidDate" property="idValidDate" size="10"/><bean:message key="label.requiredfield"/>
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
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" size="70"/><bean:message key="label.requiredfield"/></td>
	</tr>
	<tr>
		<td align="left"><bean:message key="label.grant.owner.infoperson.sex"/>:&nbsp;</td>
		<td>
			Masculino:&nbsp;<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.sex" property="sex" value="MALE"/>&nbsp;&nbsp;
			Feminino:&nbsp;<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.sex" property="sex" value="FEMALE"/><bean:message key="label.requiredfield"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.maritalStatus"/>:&nbsp;
		</td>
		<td>
			<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.person.MaritalStatus"/>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.maritalStatus" property="maritalStatus">
        		<html:option key="dropDown.Default" value="null"/>
            	<html:options collection="values" property="value" labelProperty="label"/>
			</html:select>            
		</td>
	</tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.birthdate"/>:&nbsp;
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.birthdate" property="birthdate" size="10"/><bean:message key="label.requiredfield"/>
			&nbsp;<bean:message key="label.dateformat"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.fatherName"/>:&nbsp;
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.fatherName" property="fatherName" size="70"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.motherName"/>:&nbsp;
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.motherName" property="motherName" size="70"/>
		</td>
	</tr>		
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.districtBirth"/>:&nbsp;
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.districtBirth" property="districtBirth" size="30"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.parishOfBirth"/>:&nbsp;
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.parishOfBirth" property="parishOfBirth" size="30"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.districtSubBirth"/>:&nbsp;
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.districtSubBirth" property="districtSubBirth" size="30"/>
		</td>			
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.country"/>:&nbsp;
		</td>
		<td>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.country" property="country">
				<html:options collection="countryList" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.address"/>:&nbsp;
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.address" property="address" size="70"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.area"/>:&nbsp;
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.area" property="area"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.areaCode"/>:&nbsp;
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.areaCode" property="areaCode" size="8"/>&nbsp;(1234-567)
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.areaOfAreaCode"/>:&nbsp;
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.areaOfAreaCode" property="areaOfAreaCode"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.addressParish"/>:&nbsp;
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.addressParish" property="addressParish" size="30"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.addressDistrictSub"/>:&nbsp;
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.addressDistrictSub" property="addressDistrictSub" size="30"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.addressDistrict"/>:&nbsp;
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.addressDistrict" property="addressDistrict" size="30"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.phone"/>:&nbsp;
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.phone" property="phone"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.workphone"/>:&nbsp;
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.workphone" property="workphone"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.cellPhone"/>:&nbsp;
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.cellphone" property="cellphone"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.email"/>:&nbsp;
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.email" property="email" size="60"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.homepage"/>:&nbsp;
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.homepage" property="homepage" size="60"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.socialSecurityNumber"/>:&nbsp;
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.socialSecurityNumber" property="socialSecurityNumber"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.profession"/>:&nbsp;
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.profession" property="profession" size="30"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.fiscalCode"/>:&nbsp;
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.fiscalCode" property="fiscalCode"/>
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
			<logic:present name="idInternal">
				<html:form action="/manageGrantOwner" style="display:inline">
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareManageGrantOwnerForm"/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal" value='<%= request.getAttribute("idInternal").toString() %>'/>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" style="display:inline">
						<bean:message key="button.cancel"/>
					</html:submit>
				</html:form>					
			</logic:present>
		</td>
	</tr>
</table>
	