<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.title.RegistrationState" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<%--
<style>
a.abutton3 {
background: #e6e6e6;
padding: 5px;
position: relative;
display: block;
text-align: center;
/*color: #3f719c;*/
text-decoration: none;
}
a.abutton3 span {
text-decoration: underline;
}
a.abutton3:hover span {
text-decoration: none;
}
</style>


<a href="" class="abutton3">
	<img src="<%= request.getContextPath() %>/images/corner_tl.gif" style="position: absolute; top: 0; left: 0;"/>
	<img src="<%= request.getContextPath() %>/images/corner_tr.gif" style="position: absolute; top: 0; right: 0;"/>
	<img src="<%= request.getContextPath() %>/images/corner_bl.gif" style="position: absolute; bottom: 0; left: 0;"/>
	<img src="<%= request.getContextPath() %>/images/corner_br.gif" style="position: absolute; bottom: 0; right: 0;"/>
		asdfasdfasdf
</a>


<html:link styleClass="abutton3 width7em" page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="registration" paramProperty="idInternal">
	<img src="<%= request.getContextPath() %>/images/corner_tl.gif" style="position: absolute; top: 0; left: 0;"/>
	<img src="<%= request.getContextPath() %>/images/corner_tr.gif" style="position: absolute; top: 0; right: 0;"/>
	<img src="<%= request.getContextPath() %>/images/corner_bl.gif" style="position: absolute; bottom: 0; left: 0;"/>
	<img src="<%= request.getContextPath() %>/images/corner_br.gif" style="position: absolute; bottom: 0; right: 0;"/>
	<<
	<span><bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></span>
</html:link>
--%>


<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<h3 class="mtop15 mbottom025"><bean:message key="label.studentDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<table>
	<tr>
		<td>
			<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 thright thlight mtop025"/>
				</fr:layout>
			</fr:view>
		</td>
		<td>
			<bean:define id="personID" name="registration" property="student.person.idInternal"/>
			<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES"/>
		</td>
	</tr>
</table>

<logic:present name="registration" property="ingressionEnum">
<h3 class="mbottom025"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="registration" schema="student.registrationDetail" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop025"/>	</fr:layout>
	</fr:view>
</logic:present>
<logic:notPresent name="registration" property="ingressionEnum">
<h3 class="mbottom025"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="registration" schema="student.registrationsWithStartData" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop025"/>	</fr:layout>
</fr:view>
</logic:notPresent>



<h3 class="mbottom025"><bean:message key="label.registration.manageState" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:edit name="registrationStateBean" schema="student.manageRegistrationState" action="/manageRegistrationState.do?method=createNewState">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop025"/>
	</fr:layout>
</fr:edit>



<logic:notEmpty name="registration" property="registrationStates" >
	<h3 class="mtop2 mbottom025"><bean:message key="label.registration.historic" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<fr:view name="registration" property="registrationStates" schema="student.viewRegistrationStatesHistoric" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop025"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

