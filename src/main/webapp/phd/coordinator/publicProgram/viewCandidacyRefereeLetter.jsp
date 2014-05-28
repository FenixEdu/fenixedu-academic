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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<html:xhtml/>

<logic:present role="role(COORDINATOR)">

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.viewProcess" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<bean:define id="candidacyProcess" name="candidacyRefereeLetter" property="phdProgramCandidacyProcess" />

<html:link action="/candidacies/phdProgramCandidacyProcess.do?method=viewProcess" paramId="hashCodeId" paramName="candidacyProcess" paramProperty="candidacyHashCode.externalId">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>

<br/><br/>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<h2>Referee form</h2>

<p class="mbottom05"><strong>Applicant's Name: </strong><bean:write name="candidacyProcess" property="person.name" /></p>
<p class="mtop05"><strong>Focus Area: </strong>
	<logic:notEmpty name="candidacyProcess" property="individualProgramProcess.phdProgramFocusArea">
		<bean:write name="candidacyProcess" property="individualProgramProcess.phdProgramFocusArea.name.content" />
	</logic:notEmpty>
	<logic:empty name="candidacyProcess" property="individualProgramProcess.phdProgramFocusArea"> -- </logic:empty>
</p>
<p class="mtop05"><strong>Referee: </strong><bean:write name="candidacyRefereeLetter" property="candidacyReferee.name" /></p>

<br/>

<fr:view name="candidacyRefereeLetter" schema="PhdCandidacyRefereeLetter.applicant.information">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight"/>
        <fr:property name="columnClasses" value="width175px,,,,"/>
	</fr:layout>
</fr:view>

<fr:view name="candidacyRefereeLetter" schema="PhdCandidacyRefereeLetter.overall.promise">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight"/>
        <fr:property name="columnClasses" value="width175px,,,,"/>
	</fr:layout>
</fr:view>

<logic:notEmpty name="candidacyRefereeLetter" property="file">
	<fr:view name="candidacyRefereeLetter" schema="PhdCandidacyRefereeLetter.comments.with.file">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight"/>
	        <fr:property name="columnClasses" value="width175px,,,,"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="candidacyRefereeLetter" property="file">
	<fr:view name="candidacyRefereeLetter" schema="PhdCandidacyRefereeLetter.comments">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight"/>
	        <fr:property name="columnClasses" value="width175px,,,,"/>
		</fr:layout>
	</fr:view>
</logic:empty>

<fr:view name="candidacyRefereeLetter" schema="PhdCandidacyRefereeLetter.referee.information">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight"/>
        <fr:property name="columnClasses" value="width175px,,,,"/>
	</fr:layout>
</fr:view>

</logic:present>