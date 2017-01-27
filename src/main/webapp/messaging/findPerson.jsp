<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.google.common.base.Strings"%>
<%@page import="com.google.common.base.Joiner"%>
<%@page import="org.fenixedu.bennu.core.domain.User"%>
<%@page import="org.fenixedu.academic.domain.student.Student"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<h2>
	<bean:message key="label.person.findPerson" />
</h2>


<fr:edit id="searchForm" name="bean"
	action="/findPerson.do?method=findPerson">
	<fr:schema bundle="APPLICATION_RESOURCES"
		type="org.fenixedu.academic.ui.struts.action.messaging.FindPersonBean">

		<fr:slot name="roleType" layout="menu-postback" key="label.type">
			<fr:property name="includedValues"
				value="STUDENT,TEACHER,ALUMNI" />
			<fr:property name="destination" value="postback" />
			<fr:destination name="postback" path="/findPerson.do?method=postback" />
		</fr:slot>
		<logic:present name="bean" property="roleType">
			<logic:equal name="bean" property="roleType" value="STUDENT">
				<fr:slot name="degreeType" layout="menu-postback" key="label.degree.type">
					<fr:property name="excludedValues" value="EMPTY"/>
					<fr:property name="destination" value="postback" />
					<fr:destination name="postback"
						path="/findPerson.do?method=postback" />
				</fr:slot>
				<logic:present name="bean" property="degreeType">
					<fr:slot name="degree" layout="menu-select-postback" key="label.degree.name">
						<fr:property name="providerClass"
							value="org.fenixedu.academic.ui.renderers.providers.person.PersonSearchDegreeProvider" />
						<fr:destination name="postback"
							path="/findPerson.do?method=postback" />
						<fr:property name="destination" value="postback" />
						<fr:property name="format" value="\${presentationName}" />
					</fr:slot>
				</logic:present>
			</logic:equal>


			<logic:equal name="bean" property="roleType" value="TEACHER">
				<fr:slot name="department" layout="menu-select-postback" key="label.teacher.finalWork.department">
					<fr:property name="providerClass"
						value="org.fenixedu.academic.ui.renderers.providers.ActiveDepartmentsProvider" />
					<fr:destination name="postback"
						path="/findPerson.do?method=postback" />
					<fr:property name="destination" value="postback" />
					<fr:property name="format" value="\${fullName.content}" />
				</fr:slot>
			</logic:equal>
		</logic:present>

		<fr:slot name="name" key="label.name" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
			<fr:property name="size" value="50"/>
		</fr:slot>
		<fr:slot name="viewPhoto" key="label.viewPhoto"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright mtop025 thmiddle"/>
	    <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	    <fr:property name="rowClasses" value="width46em"/>
	</fr:layout>
	<fr:destination name="cancel" path="/findPerson.do?method=prepareFindPerson"/>
</fr:edit>

<p>
	<em> <!-- Error messages go here --> <html:errors /> </em>
</p>




<logic:present name="personListFinded">

	<logic:notEqual name="totalFindedPersons" value="1">
		<p>
			<b><bean:message key="label.manager.numberFindedPersons"
					arg0="${totalFindedPersons}" />
			</b>
		</p>
	</logic:notEqual>
	<logic:equal name="totalFindedPersons" value="1">
		<p>
			<b><bean:message key="label.manager.findedOnePersons"
					arg0="${totalFindedPersons}" />
			</b>
		</p>
	</logic:equal>
	
	 <bean:define id="url">/messaging/findPerson.do?method=findPerson&amp;name=<bean:write
			name="name" />&amp;roleType=<bean:write name="roleType" />&amp;degreeId=<bean:write
			name="degreeId"  />&amp;degreeType=<bean:write name="degreeType" />&amp;departmentId=<bean:write
			name="departmentId" />&amp;viewPhoto=<bean:write name="viewPhoto" />
	</bean:define> 
	
	<p>
		<bean:message key="label.pages" />
		:
		<cp:collectionPages url="<%= url %>" numberOfVisualizedPages="11"
			pageNumberAttributeName="pageNumber"
			numberOfPagesAttributeName="numberOfPages" />
	</p>

	<logic:iterate id="personalInfo" name="personListFinded"
		indexId="personIndex" type="org.fenixedu.academic.domain.Person">
		<bean:define id="personID" name="personalInfo" property="externalId" />
		<% 
			String username = personalInfo.getUser() !=null ? personalInfo.getUser().getUsername() : null;
			Integer studentNumber = personalInfo.getStudent() != null ? personalInfo.getStudent().getNumber() : null;
			String personalIds = Joiner.on(", ").skipNulls().join(username, studentNumber);
			personalIds = StringUtils.isNotBlank(personalIds) ? "(" + personalIds + ")" : "";
		%>
		<div class="pp">
			<table class="ppid" cellpadding="0" cellspacing="0">
				<tr>
					<td width="70%"><strong> <bean:write name="personalInfo" property="name" /> </strong> <%= personalIds %></td>
					<td width="30%" style="text-align: right;"><bean:define
							id="aa" value="<%= "aa" + personIndex %>" /> <bean:define
							id="id" value="<%= "id" + (personIndex.intValue() + 40)  %>" />
						<bean:define
							id="aa" value="<%= "aa" + personIndex %>" /> <bean:define
							id="id" value="<%= "id" + (personIndex.intValue() + 40) %>" /> 
							<span> <button type="button" alt="input.input" type="button" value="+" data-toggle="collapse" data-target="#collapse${personalInfo.username}">+</button>
					</span> <!-- </td>--></td>
				</tr>
			</table>

			<logic:equal name="viewPhoto" value="true">
				<bean:define id="personIDForPhoto" name="personalInfo" property="username" />
				<html:img
					src="<%= request.getContextPath() + "/user/photo/" + personIDForPhoto.toString()%>"
					altKey="personPhoto" bundle="IMAGE_RESOURCES" />
			</logic:equal>

			<table class="ppdetails">
				<tr class="highlight">
					<td class="ppleft" valign="top"><bean:message
							key="label.person.workPhone.short" /></td>
					<td class="ppright" valign="top" style="width: 18em;"><fr:view
							name="personalInfo" property="phones">
							<fr:layout name="contact-list">
								<fr:property name="classes" value="nobullet list6" />
							</fr:layout>
						</fr:view> <fr:view name="personalInfo" property="mobilePhones">
							<fr:layout name="contact-list">
								<fr:property name="classes" value="nobullet list6" />
							</fr:layout>
						</fr:view></td>
					<td class="ppleft2" valign="top" style="text-align: right;"><bean:message
							key="label.person.email" /></td>
					<td class="ppright" valign="top"><fr:view name="personalInfo"
							property="emailAddresses">
							<fr:layout name="contact-list">
								<fr:property name="classes" value="nobullet list6" />
							</fr:layout>
						</fr:view></td>
				</tr>
			</table>

			<div id="collapse${personalInfo.username}" class="collapse">
				<table class="ppdetails">
					<logic:notEmpty name="personalInfo" property="teacher">
						<logic:notEmpty name="personalInfo"
							property="teacher.category">
							<tr>
								<td class="ppleft2"><bean:message
										key="label.teacher.category" />:</td>
								<td class="ppright"><bean:write name="personalInfo"
										property="teacher.category.name.content" />
								</td>
							</tr>
						</logic:notEmpty>
					</logic:notEmpty>
					<fr:view name="personalInfo" property="webAddresses">
						<fr:layout name="contact-table">
							<fr:property name="types" value="WORK" />
							<fr:property name="bundle" value="APPLICATION_RESOURCES" />
							<fr:property name="label" value="label.person.webSite" />
							<fr:property name="defaultLabel"
								value="label.partyContacts.defaultContact" />
							<fr:property name="leftColumnClasses" value="ppleft2" />
							<fr:property name="rightColumnClasses" value="ppright" />
						</fr:layout>
					</fr:view>

					<logic:present name="personalInfo" property="student">
						<logic:notEmpty name="personalInfo"
							property="student.registrations">

							<logic:iterate id="registration" name="personalInfo"
								property="student.registrations">
								<tr>
									<td class="ppleft2" style="vertical-align: top;"><bean:message
											key="label.degree.name" />:</td>
									<td class="ppright"><bean:write name="registration"
											property="degreeName" />
									</td>
								</tr>
							</logic:iterate>
						</logic:notEmpty>
					</logic:present>
				</table>
			</div>
		</div>
	</logic:iterate>

	<logic:notEqual name="numberOfPages" value="1">
		<p class="mtop15">
			<bean:message key="label.pages" />
			:
			<cp:collectionPages url="<%= url %>" numberOfVisualizedPages="11"
				pageNumberAttributeName="pageNumber"
				numberOfPagesAttributeName="numberOfPages" />
		</p>
	</logic:notEqual>

</logic:present>
