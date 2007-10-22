<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.interRelaOffice" bundle="INTERNATIONAL_RELATION_OFFICE"/></em>
<h2><bean:message key="label.visualizeRegistration" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>


<ul class="mtop2 list5">
	<li>
		<html:link page="/students.do?method=visualizeStudent" paramId="studentID" paramName="registration" paramProperty="student.idInternal">
			<bean:message key="link.student.backToSudentDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</li>
</ul>

<div style="float: right;">
	<bean:define id="personID" name="registration" property="student.person.idInternal"/>
	<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
</div>

<p class="mvert2">
	<span class="showpersonid">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>



<logic:messagesPresent message="true">
	<ul class="list7 mtop2 warning0" style="list-style: none;">
		<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
			<li>
				<span><!-- Error messages go here --><bean:write name="message" /></span>
			</li>
		</html:messages>
	</ul>
</logic:messagesPresent>



<%-- Registration Details --%>
<logic:present name="registration" property="ingressionEnum">
<h3 class="mtop2 mbottom05 separator2"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="registration" schema="student.registrationDetail" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thright thlight"/>
		<fr:property name="rowClasses" value=",,,,,,,,"/>
	</fr:layout>
</fr:view>
</logic:present>
<logic:notPresent name="registration" property="ingressionEnum">
<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="registration" schema="student.registrationsWithStartData" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thright thlight mtop0"/>
		<fr:property name="rowClasses" value=",,,,,,,"/>
	</fr:layout>
</fr:view>
</logic:notPresent>

<p class="mtop0">
	<span>
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
		<html:link page="/registration.do?method=viewRegistrationCurriculum" paramId="registrationID" paramName="registration" paramProperty="idInternal">
			<bean:message key="link.registration.viewCurriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</span>

</p>



<%-- Curricular Plans --%>

<h3 class="mbottom05 mtop25 separator2"><bean:message key="label.studentCurricularPlans" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

<fr:view name="registration" property="sortedStudentCurricularPlans" schema="student.studentCurricularPlans" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thright thlight thcenter"/>
		<fr:property name="groupLinks" value="false"/>
	</fr:layout>
</fr:view>

<p class="mtop0">
	<span>
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
		<html:link page="/viewCurriculum.do?method=prepare" paramId="registrationOID" paramName="registration" paramProperty="idInternal">
			<bean:message key="link.registration.viewStudentCurricularPlans" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</span>
	
</p>





<%-- Precedence Info --%>

<logic:present name="registration" property="studentCandidacy">
	<h3 class="mtop2 mbottom05 separator2"><bean:message key="label.person.title.precedenceDegreeInfo" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<fr:view name="registration" property="studentCandidacy.precedentDegreeInformation" schema="student.precedentDegreeInformation" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
		</fr:layout>
	</fr:view>
</logic:present>

