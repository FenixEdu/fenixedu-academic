<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/academic.tld" prefix="academic" %>
<html:xhtml/>

<div style="float: right;">
	<bean:define id="personID" name="student" property="person.externalId"/>
	<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
</div>

<h2><bean:message key="label.studentPage" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>


<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>


<h3 class="mtop15 mbottom025"><bean:message key="label.studentDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<table class="mtop025">
	<tr>
		<td>
			<fr:view name="student" schema="student.show.personAndStudentInformation">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thright thlight mtop0 mbottom0"/>
		      		<fr:property name="rowClasses" value="tdhl1,,,,"/>
				</fr:layout>
			</fr:view>
		</td>
	</tr>
</table>


<p class="mvert05">
	<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
	<html:link page="/student.do?method=viewPersonalData" paramId="studentID" paramName="student" paramProperty="externalId">
		<bean:message key="link.student.viewPersonalData" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>

	<academic:allowed operation="EDIT_STUDENT_PERSONAL_DATA">
		<span class="pleft05">
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
			<html:link page="/student.do?method=prepareEditPersonalData" paramId="studentID" paramName="student" paramProperty="externalId">
				<bean:message key="link.student.editPersonalData" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</html:link>
		</span>
		<span class="pleft05">
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
			<html:link page="/student.do?method=viewStudentLog" paramId="studentID" paramName="student" paramProperty="externalId">
				<bean:message key="link.executionCourse.log" bundle="APPLICATION_RESOURCES"/>
			</html:link>
		</span>
	</academic:allowed>
</p>

<academic:allowed operation="MANAGE_REGISTRATIONS">
<h3 class="mtop15 mbottom025"><bean:message key="label.studentRegistrations" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="student" property="registrations" schema="student.registrationDetail.short" >
	<fr:layout name="tabular">
		<fr:property name="sortBy" value="startDate=desc"/>

		<fr:property name="classes" value="tstyle1 thlight mtop025 boldlink1"/>
		<fr:property name="columnClasses" value="acenter,acenter,tdhl1,,acenter,nowrap"/>

		<fr:property name="linkFormat(view)" value="/student.do?method=visualizeRegistration&registrationID=${externalId}" />
		<fr:property name="key(view)" value="link.student.visualizeRegistration"/>
		<fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES"/>
		<fr:property name="visibleIf(view)" value="allowedToManageRegistration"/>
		<fr:property name="contextRelative(view)" value="true"/>
	</fr:layout>
</fr:view>
</academic:allowed>

<academic:notAllowed operation="MANAGE_REGISTRATIONS">
	<academic:allowed operation="VIEW_FULL_STUDENT_CURRICULUM">
		<h3 class="mtop15 mbottom025">
			<bean:message key="label.studentRegistrations" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</h3>
		<fr:view name="student" property="registrations" schema="student.registrationDetail.short" >
			<fr:layout name="tabular">
		<fr:property name="sortBy" value="startDate=desc"/>

		<fr:property name="classes" value="tstyle1 thlight mtop025 boldlink1"/>
		<fr:property name="columnClasses" value="acenter,acenter,tdhl1,,acenter,nowrap"/>

		<fr:property name="linkFormat(view)" value="/student.do?method=visualizeRegistration&registrationID=${externalId}" />
		<fr:property name="key(view)" value="link.student.visualizeRegistration"/>
		<fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES"/>
		<fr:property name="visibleIf(view)" value="allowedToManageRegistration"/>
		<fr:property name="contextRelative(view)" value="true"/>
		</fr:layout>
		</fr:view>	
	</academic:allowed>
</academic:notAllowed>

<!-- Student Status -->
<academic:allowed operation="MANAGE_STATUTES">
<h3 class="mbottom025"><bean:message key="label.statutes" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<p class="mvert05">
	<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
	<html:link action="/studentStatutes.do?method=prepare" paramName="student" paramProperty="externalId" paramId="studentId">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.studentStatutes.manage" />
	</html:link>
</p>

<fr:view name="student" property="currentStatutes" schema="student.statutes.current" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight mtop025 mbottom0"/>
		<fr:property name="columnClasses" value=",tdhl1"/>
	</fr:layout>
</fr:view>
</academic:allowed>

<!-- Extra Curricular Activities -->
<academic:allowed operation="MANAGE_EXTRA_CURRICULAR_ACTIVITIES">
<h3 class="mbottom025"><bean:message key="label.extraCurricularActivities" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<p class="mvert05">
    <img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
    <html:link action="/studentExtraCurricularActivities.do?method=prepare" paramName="student" paramProperty="externalId" paramId="studentId">
        <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.extraCurricularActivities.manage" />
    </html:link>
</p>
</academic:allowed>

<!-- Payments -->
<academic:allowed operation="MANAGE_STUDENT_PAYMENTS">
<h3 class="mbottom025"><bean:message key="label.payments" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<p class="mtop05 mbottom15">
	<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
	<html:link action="/payments.do?method=showOperations" paramName="student" paramProperty="person.externalId" paramId="personId">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.management" />
	</html:link>
</p>
</academic:allowed>

<!-- RAIDES -->
<academic:allowed operation="MANAGE_REGISTRATIONS">
<h3 class="mbottom025"><bean:message key="label.editCandidacies" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<p class="mtop05 mbottom15">
	<fr:form action="/editCandidacyInformation.do?method=prepareEdit">
		<fr:edit id="choosePhdOrRegistration" name="choosePhdOrRegistration" type="net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.EditCandidacyInformationDA$ChooseRegistrationOrPhd">
			<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.EditCandidacyInformationDA$ChooseRegistrationOrPhd">
				<fr:slot name="phdRegistrationWrapper" key="label.raides.choosePhdOrRegistration" layout="menu-select-postback">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.academicAdminOffice.ActiveAndRecentRegistrationsAndPhds" />
					<fr:property name="format" value="${displayName}" />
				</fr:slot>
			</fr:schema>
		</fr:edit>
	</fr:form>
</p>
</academic:allowed>