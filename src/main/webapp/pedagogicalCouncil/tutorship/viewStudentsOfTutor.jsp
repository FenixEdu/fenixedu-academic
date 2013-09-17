<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<em><bean:message key="pedagogical.council" bundle="PEDAGOGICAL_COUNCIL" /></em>
<h2><bean:message key="title.tutorship.tutor.tutorships" bundle="PEDAGOGICAL_COUNCIL" /></h2>

<html:link page="/viewTutors.do?method=backToTutors" paramId="tutorshipIntentionID" paramName="tutorshipIntention" paramProperty="externalId">
	<bean:message key="label.return" bundle="APPLICATION_RESOURCES"/>
</html:link>

<h3><bean:write name="tutorshipIntention" property="teacher.person.name"/> - <bean:write name="tutorshipIntention" property="teacher.person.istUsername"/></h3>
		
<fr:view name="tutorshipIntention" property="tutorships">
	<fr:schema type="net.sourceforge.fenixedu.domain.Tutorship" bundle="APPLICATION_RESOURCES">
		<fr:slot name="student.number" key="label.number"/>
		<fr:slot name="student.name" key="label.name"/>
		<fr:slot name="student.person.defaultMobilePhoneNumber" key="label.mobile"/>
		<fr:slot name="student.person.defaultEmailAddressValue" key="label.mail"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1" />
	</fr:layout>
</fr:view>