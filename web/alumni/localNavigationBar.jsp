<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!-- localNavigationBar.jsp -->

<logic:present role="ALUMNI">
	<ul>
		<li class="navheader"><bean:message key="academic.path"
			bundle="ALUMNI_RESOURCES" /></li>
		<li><html:link page="/viewCurriculum.do?method=checkValidation">
			<bean:message key="link.student.curriculum" bundle="ALUMNI_RESOURCES" />
		</html:link></li>
<%--
		<li><html:link page="/searchAlumni.do?method=showAlumniList">
			<bean:message key="link.search.alumni" bundle="ALUMNI_RESOURCES" />
		</html:link></li>

		<li class="navheader"><bean:message key="professional.info"
			bundle="ALUMNI_RESOURCES" /></li>
		<li><html:link
			page="/professionalInformation.do?method=innerProfessionalInformation">
			<bean:message key="link.professional.information"
				bundle="ALUMNI_RESOURCES" />
		</html:link></li>

		<li class="navheader"><bean:message key="label.formation"
			bundle="ALUMNI_RESOURCES" /></li>
		<li><html:link
			page="/formation.do?method=innerFormationManagement">
			<bean:message key="link.post.graduate.education"
				bundle="ALUMNI_RESOURCES" />
		</html:link></li>

		<li class="navheader"><bean:message key="academic.services"
			bundle="ALUMNI_RESOURCES" /></li>
			
		<li><html:link page="/documentRequest.do?method=chooseRegistration">
			<bean:message key="link.academic.services.document.requirements"
				bundle="ALUMNI_RESOURCES" />
		</html:link></li>

		<li><html:link page="/documentRequest.do?method=viewDocumentRequests">
			<bean:message key="link.academic.services.pending.requests"
				bundle="ALUMNI_RESOURCES" />
		</html:link></li>

		<li><html:link page="/payments.do?method=showEvents">
			<bean:message key="link.academic.services.payments"
				bundle="ALUMNI_RESOURCES" />
		</html:link></li>

		<li><html:link page="/prices.do?method=viewPrices">
			<bean:message key="link.academic.services.prices"
				bundle="ALUMNI_RESOURCES" />
		</html:link></li>
--%>
	</ul>
</logic:present>
