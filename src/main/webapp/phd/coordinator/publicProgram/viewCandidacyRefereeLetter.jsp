<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>

<html:xhtml/>

<logic:present role="COORDINATOR">

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