<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<em><bean:message key="label.facultyAdmOffice.portal.name"/></em>
<h2><bean:message key="label.grant.owner.edition"/></h2>

<html:form action="/editGrantOwner" style="display:inline">

	<%-- Presenting Errors--%>
	<logic:messagesPresent>
		<html:errors/>
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


<p class="mtop2 mbottom05"><b><bean:message key="label.grant.owner.information"/></b></p>

<table class="tstyle5 mtop05">
	<tr>
		<td><bean:message key="label.grant.owner.dateSendCGD"/>:</td>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.dateSendCGD" property="dateSendCGD" size="10"/><bean:message key="label.dateformat"/></td>
	</tr>
	<tr>
		<td><bean:message key="label.grant.owner.cardCopyNumber"/>:</td>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.cardCopyNumber" property="cardCopyNumber" size="3"/></td>
	</tr>
</table>


<p class="mtop15 mbottom05"><b><bean:message key="label.grant.owner.infoperson.idinformation"/></b></p>

<table class="tstyle5 mtop05">
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.idNumber"/>:
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.idNumber" property="idNumber"/> <bean:message key="label.requiredfield"/>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.idType"/>:
		</td>
		<td>
			<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.person.IDDocumentType"/>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.idType" property="idType">
				<html:option key="dropDown.Default" value=""/>
				<html:options collection="values" property="value" labelProperty="label"/>
			</html:select> <bean:message key="label.requiredfield"/>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.idLocation"/>:
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.idLocation" property="idLocation" size="30"/>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.idDate"/>:
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.idDate" property="idDate" size="10"/> <bean:message key="label.requiredfield"/>
			<bean:message key="label.dateformat"/>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.idValidDate"/>:
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.idValidDate" property="idValidDate" size="10"/> <bean:message key="label.requiredfield"/>
			<bean:message key="label.dateformat"/>
		</td>
	</tr>
</table>


<logic:notPresent name="toShow">
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" property="searchPerson"
		 onclick="this.form.page.value='0';this.form.method.value='prepareEditGrantOwnerForm';">
		<bean:message key="button.continue"/>
	</html:submit>
</logic:notPresent>
			
<logic:messagesPresent message="true">
	<html:messages id="message" message="true" property="message">
		<span class="highlight1"><bean:write name="message"/></span>
	</html:messages>
</logic:messagesPresent>			
			
<logic:present name="toShow">


<p class="mtop15 mbottom05"><strong><bean:message key="label.grant.owner.personalinformation"/></strong></p>

<table class="tstyle5 mtop05">
	<tr>
		<td><bean:message key="label.grant.owner.infoperson.name"/>:</td>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" size="70"/> <bean:message key="label.requiredfield"/></td>
	</tr>
	<tr>
		<td><bean:message key="label.grant.owner.infoperson.sex"/>:</td>
		<td>
			Masculino:<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.sex" property="sex" value="MALE"/>
			Feminino:<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.sex" property="sex" value="FEMALE"/> <bean:message key="label.requiredfield"/>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.maritalStatus"/>:
		</td>
		<td>
			<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.person.MaritalStatus"/>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.maritalStatus" property="maritalStatus">
        		<html:option key="dropDown.Default" value="null"/>
            	<html:options collection="values" property="value" labelProperty="label"/>
			</html:select>            
		</td>
	</tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.birthdate"/>:
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.birthdate" property="birthdate" size="10"/> <bean:message key="label.requiredfield"/>
			<bean:message key="label.dateformat"/>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.fatherName"/>:
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.fatherName" property="fatherName" size="70"/>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.motherName"/>:
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.motherName" property="motherName" size="70"/>
		</td>
	</tr>		
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.districtBirth"/>:
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.districtBirth" property="districtBirth" size="30"/>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.parishOfBirth"/>:
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.parishOfBirth" property="parishOfBirth" size="30"/>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.districtSubBirth"/>:
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.districtSubBirth" property="districtSubBirth" size="30"/>
		</td>			
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.country"/>:
		</td>
		<td>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.country" property="country">
				<html:options collection="countryList" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.address"/>:
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.address" property="address" size="70"/>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.area"/>:
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.area" property="area"/>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.areaCode"/>:
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.areaCode" property="areaCode" size="8"/>(1234-567)
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.areaOfAreaCode"/>:
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.areaOfAreaCode" property="areaOfAreaCode"/>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.addressParish"/>:
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.addressParish" property="addressParish" size="30"/>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.addressDistrictSub"/>:
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.addressDistrictSub" property="addressDistrictSub" size="30"/>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.addressDistrict"/>:
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.addressDistrict" property="addressDistrict" size="30"/>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.phone"/>:
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.phone" property="phone"/>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.workphone"/>:
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.workphone" property="workphone"/>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.cellPhone"/>:
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.cellphone" property="cellphone"/>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.email"/>:
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.email" property="email" size="60"/>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.homepage"/>:
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.homepage" property="homepage" size="60"/>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.socialSecurityNumber"/>:
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.socialSecurityNumber" property="socialSecurityNumber"/>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.profession"/>:
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.profession" property="profession" size="30"/>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.fiscalCode"/>:
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.fiscalCode" property="fiscalCode"/>
		</td>
	</tr>
</table>	

</logic:present>


<table>
	<tr>
		<td>
		<logic:present name="toShow">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
				<bean:message key="button.save"/>
			</html:submit>
		</logic:present>			
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
	
