<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<!-- localNavigationBar.jsp -->

<logic:present role="ALUMNI">
	<ul>
		<li class="navheader">
			<bean:message key="academic.path" bundle="ALUMNI_RESOURCES" />
		</li>
		<li>
			<html:link page="/viewCurriculum.do?method=checkValidation">
				<bean:message key="link.student.curriculum" bundle="ALUMNI_RESOURCES" />
			</html:link>
		</li>
		<li>
			<html:link page="/searchAlumni.do?method=showAlumniList">
				<bean:message key="link.search.alumni" bundle="ALUMNI_RESOURCES" />
			</html:link>
		</li>
        <li>
            <html:link page="/studentDataShareAuthorization.do?method=manageAuthorizations">
                <bean:message key="title.student.dataShareAuthorizations.short" bundle="STUDENT_RESOURCES" />
            </html:link>
        </li>


		<li class="navheader">
			<bean:message key="professional.info" bundle="ALUMNI_RESOURCES" />
		</li>
		<li>
			<html:link page="/professionalInformation.do?method=innerProfessionalInformation">
				<bean:message key="link.professional.information" bundle="ALUMNI_RESOURCES" />
			</html:link>
		</li>


		<li class="navheader">
			<bean:message key="label.formation" bundle="ALUMNI_RESOURCES" />
		</li>
		<li>
			<html:link page="/formation.do?method=innerFormationManagement">
				<bean:message key="link.graduate.education" bundle="ALUMNI_RESOURCES" />
			</html:link>
		</li>


		<li class="navheader">
			<bean:message key="academic.services" bundle="ALUMNI_RESOURCES" />
		</li>
		<li>
			<html:link page="/documentRequest.do?method=chooseRegistration">
				<bean:message key="link.academic.services.document.requirements" bundle="ALUMNI_RESOURCES" />
			</html:link>
		</li>
		<li>
			<html:link page="/documentRequest.do?method=viewDocumentRequests">
				<bean:message key="link.academic.services.pending.requests" bundle="ALUMNI_RESOURCES" />
			</html:link>
		</li>
		<li>
			<html:link page="/payments.do?method=showEvents">
				<bean:message key="link.academic.services.payments" bundle="ALUMNI_RESOURCES" />
			</html:link>
		</li>

		<li>
			<html:link page="/prices.do?method=viewPrices">
				<bean:message key="link.academic.services.prices" bundle="ALUMNI_RESOURCES" />
			</html:link>
		</li>


		<!-- li class="navheader">
			<bean:message key="inquiries.title" bundle="ALUMNI_RESOURCES" />
		</li>
		<li>
			<html:link page="/alumniInquiries.do?method=showMainPage">
				<bean:message key="inquiries.alumni.menu" bundle="ALUMNI_RESOURCES" />
			</html:link>
		</li-->

	</ul>
</logic:present>
