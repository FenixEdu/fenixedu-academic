<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<em><bean:message bundle="EXTERNAL_SUPERVISION_RESOURCES" key="externalSupervision"/></em>
<h2><bean:message bundle="EXTERNAL_SUPERVISION_RESOURCES" key="title.chooseDissertation.chooseThesis"/></h2>

<bean:define id="personExternalId" name="student" property="person"/>

<html:link page="/viewStudent.do?method=showStats" paramName="personExternalId" paramProperty="externalId" paramId="personId">
	<bean:message key="link.back" bundle="EXTERNAL_SUPERVISION_RESOURCES"/>
</html:link>

<p class="mvert2">
	<span class="showpersonid">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>

<logic:iterate id="dissertations" name="dissertations">
	<strong><bean:write name="dissertations" property="executionYear.qualifiedName"/></strong>
	<bean:write name="dissertations" property="curricularCourse.name"/>
	<bean:define id="theses" name="dissertations" property="theses"/>
	<logic:empty name="theses">
		<ul class="nobullet"><li>
			<em><bean:message bundle="EXTERNAL_SUPERVISION_RESOURCES" key="label.chooseDissertation.notAssigned"/></em>
		</li></ul>
	</logic:empty>
	<logic:notEmpty name="theses">
		<ul>
		<logic:iterate id="theses" name="theses">
			<li>
				<html:link page="/viewDissertation.do?method=viewThesisForSupervisor" paramName="theses" paramProperty="externalId" paramId="thesisID">
					<bean:write name="theses" property="finalTitle"/>
				</html:link>
				&nbsp;<strong><bean:message bundle="EXTERNAL_SUPERVISION_RESOURCES" key="label.chooseDissertation.state"/>:</strong> <em><fr:view name="theses" property="state"/></em>
			</li>
		</logic:iterate>
		</ul>
	</logic:notEmpty>
	<br/>
</logic:iterate>
