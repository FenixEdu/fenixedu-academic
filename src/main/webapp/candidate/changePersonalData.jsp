<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	<br />
</logic:messagesPresent>

<fr:form action="/changePersonalData.do?method=change">

	<p class="infoop"><span><h2 class="inline"><bean:message key="label.person.title.personal.info" /></h2></span></p>
	<fr:edit id="personData" name="LOGGED_USER_ATTRIBUTE" property="person" schema="candidate.personalData-edit" >
	    <fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
			<fr:property name="columnClasses" value="nowrap aleft,,,,,"/>
			<fr:property name="validatorClasses" value="error0"/>
	    </fr:layout>
	</fr:edit>

	<p class="infoop"><span><h2 class="inline"><bean:message key="label.person.title.filiation" /></h2></span></p>
	<fr:edit id="personFiliation" name="LOGGED_USER_ATTRIBUTE" property="person" schema="candidate.filiation-edit" >
	    <fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
			<fr:property name="columnClasses" value="nowrap aleft,,,,,"/>
			<fr:property name="validatorClasses" value="error0"/>
	    </fr:layout>
	</fr:edit>

	<p class="infoop"><span><h2 class="inline"><bean:message key="label.person.title.addressInfo" /></h2></span></p>
	<fr:edit id="personAddress" name="LOGGED_USER_ATTRIBUTE" property="person" schema="candidate.address-edit" >
	    <fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
			<fr:property name="columnClasses" value="nowrap aleft,,,,,"/>
			<fr:property name="validatorClasses" value="error0"/>
	    </fr:layout>
	</fr:edit>
	
	<p class="infoop"><span><h2 class="inline"><bean:message key="label.person.title.contactInfo" /></h2></span></p>
	<fr:edit id="personContacts" name="LOGGED_USER_ATTRIBUTE" property="person" schema="candidate.contacts-edit" >
	    <fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
			<fr:property name="columnClasses" value="nowrap aleft,,,,,"/>
			<fr:property name="validatorClasses" value="error0"/>
	    </fr:layout>
	</fr:edit>

	<logic:present name="precedentDegreeInformation">
		<p class="infoop"><span><h2 class="inline"><bean:message key="label.person.title.precedenceDegreeInfo" /></h2></span></p>	
		<fr:edit id="precedentDegreeInformation" name="precedentDegreeInformation" schema="candidate.precedentDegreeInformation-edit" >
		    <fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1"/>
				<fr:property name="columnClasses" value="nowrap aleft,,,,,"/>
				<fr:property name="validatorClasses" value="error0"/>
		    </fr:layout>
		</fr:edit>	
	</logic:present>

	<html:submit><bean:message key="button.submit" /></html:submit>	
	
</fr:form>
