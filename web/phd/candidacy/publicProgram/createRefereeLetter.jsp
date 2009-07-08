<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<a href="http://www.ist.utl.pt/en/about-IST/global-cooperation/IST-EPFL/">IST-EPFL</a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<br/>

<h1>IST-EPFL Joint Doctoral Initiative</h1>
<%-- ### End of Title ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=createRefereeLetterBean.overall.promise" />
<%--  ### End of Error Messages  ### --%>

<logic:notPresent name="createRefereeLetterBean">
	<em><bean:message key="label.phd.public.refereeLetter.no.information" bundle="PHD_RESOURCES" /></em>
</logic:notPresent> 

<logic:present name="createRefereeLetterBean">

<table class="thlight thleft thtop mtop05">
	<tr>
		<td><strong>Applicant's Name:</strong></td>
		<td><bean:write name="createRefereeLetterBean" property="person.name" /></td>
	</tr>
	<tr>
		<td><strong>Focus Area:</strong></td>
		<td><bean:write name="createRefereeLetterBean" property="focusArea.name.content" /></td>
	</tr>
</table>

<p>You have been indicated as a referee on behalf of a prospective student applying to the IST-PhD Joint Doctoral Initiative. We would appreciate your personal evaluation concerning the applicant's intellectual ability, aptitude in research or professional skill.</p>

<ul>
	<li>For the recommendation to be complete this form has to be fully filled out and an additional letter has to be attached, commenting on the applicant's character, the quality of previous work and the promise of productive scholarship.</li>
	<li>If the applicant's first language is not English, please evaluate her / his proficiency to read, write and speak English.</li>
	<li>Indicate how long and in what capacity you have known the applicant.</li>
	<li>If you have any reason to believe that the applicant should not be considered, please explain or send a separate letter.</li>
	<li>If you know of other students who have entered IST or EPFL from your institution, a comparison will be especially valuable.</li>
</ul>

<p>Please feel free to add information about your own educational and professional background if you feel that such information will enhance our understanding of your evaluation.</p>


<div class="fs_form">	

	<fr:form id="refereeForm" action="/candidacies/phdProgramCandidacyProcess.do" encoding="multipart/form-data">
		<input type="hidden" id="methodForm" name="method" value="createRefereeLetter" />
		<fr:edit id="createRefereeLetterBean" name="createRefereeLetterBean" visible="false" />

		<fieldset style="display: block;">
		<legend>Referee form</legend>
	
		<p class="mvert05">Please rate this applicant in overall promise for the doctorate (check one)</p>
		<fr:edit id="createRefereeLetterBean.overall.promise" name="createRefereeLetterBean" 
			schema="Public.PhdCandidacyRefereeLetterBean.overall.promise">
			<fr:layout name="tabular">
					<fr:property name="classes" value="thlight thleft thtop mtop05"/>
			        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
					<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
		
			<fr:destination name="invalid" path="/candidacies/phdProgramCandidacyProcess.do?method=createRefereeLetterInvalid" />
		</fr:edit>
	
		<p class="mvert05">On the following scale, please rank the applicant against other students in comparable fields and indicate the comparison group used (e.g.: final year undergraduate EE students at your university). If you can rank the candidate precisely in her / his class, please do so using rank value (format: value/total).</p>
		<fr:edit id="createRefereeLetterBean.rank" name="createRefereeLetterBean" 
			schema="Public.PhdCandidacyRefereeLetterBean.rank">
			<fr:layout name="tabular">
					<fr:property name="classes" value="thlight thleft thtop mtop05"/>
			        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
					<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
		
			<fr:destination name="invalid" path="/candidacies/phdProgramCandidacyProcess.do?method=createRefereeLetterInvalid" />
		</fr:edit>
		
		<br/>
		<br/>
		<p class="mvert05">Referee's information: </p>
		<fr:edit id="createRefereeLetterBean.referee.information" name="createRefereeLetterBean" 
			schema="Public.PhdCandidacyRefereeLetterBean.referee.information">
			<fr:layout name="tabular">
					<fr:property name="classes" value="thlight thleft thtop mtop05"/>
			        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
					<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
		
			<fr:destination name="invalid" path="/candidacies/phdProgramCandidacyProcess.do?method=createRefereeLetterInvalid" />
		</fr:edit>
		
		<br/>
		<br/>
		<p class="mvert05">Give your comments here</p>
		<em>Your personal evaluation about the candidate (or attach your reference letter)</em>
		<fr:edit id="createRefereeLetterBean.comments" name="createRefereeLetterBean" 
			schema="Public.PhdCandidacyRefereeLetterBean.comments">
			<fr:layout name="tabular">
					<fr:property name="classes" value="thlight thleft thtop mtop05"/>
			        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
					<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
		
			<fr:destination name="invalid" path="/candidacies/phdProgramCandidacyProcess.do?method=createRefereeLetterInvalid" />
		</fr:edit>
	
		<p class="mvert05">Attach your reference letter (optional)</p>
		
		<fr:edit id="createRefereeLetterBean.file" name="createRefereeLetterBean" 
			schema="Public.PhdCandidacyRefereeLetterBean.file">
			<fr:layout name="tabular">
					<fr:property name="classes" value="thlight thleft thtop mtop05"/>
			        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
					<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
		
			<fr:destination name="invalid" path="/candidacies/phdProgramCandidacyProcess.do?method=createRefereeLetterInvalid" />
		</fr:edit>
		<logic:notEmpty name="createRefereeLetterBean" property="existingFileFilename">
			<em>Previous file: <bean:write name="createRefereeLetterBean" property="existingFileFilename" /> (<bean:write name="createRefereeLetterBean" property="existingFileSize" />Kb)</em>
		</logic:notEmpty>
		<p><em><bean:message key="message.max.file.size" bundle="PHD_RESOURCES"/></em></p>

		</fieldset>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.edit"/></html:submit>
	</fr:form>
</div>

</logic:present>
