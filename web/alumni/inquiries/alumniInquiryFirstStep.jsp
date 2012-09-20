<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<!-- alumni/inquiries/alumniInquiryFirstStep.jsp -->

<em><bean:message key="label.portal.alumni" bundle="ALUMNI_RESOURCES" /></em>
<h2><bean:message key="inquiries.alumni.title" bundle="ALUMNI_RESOURCES" /></h2>

<p class="breadcumbs"><span class="highlight1">1ª parte - <bean:message key="inquiries.alumni.job.history" bundle="ALUMNI_RESOURCES" /></span> > 2ª parte - <bean:message key="inquiries.alumni.job.evolution" bundle="ALUMNI_RESOURCES" /></p> 

<h3 class="mtop15">
	<bean:message key="inquiries.alumni.job.history" bundle="ALUMNI_RESOURCES" />
</h3>

<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
	<p><span class="error"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>


<div class="infoop7 dinlineinside">
	<fr:form id="alumniEmployment" action="/alumniInquiries.do?method=updateIsEmployedPostback">
		<fr:edit id="employed" name="alumni" schema="alumni.isEmployed">
			<fr:layout name="flow" >
				<fr:property name="classes" value="inobullet "/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				<fr:property name="labelTerminator" value=""/>
				<fr:property name="showMarkRequired" value="true"/>
			</fr:layout>
			<fr:destination name="updateIsEmployedPostback" path="/alumniInquiries.do?method=updateIsEmployedPostback"/>
		</fr:edit>	
	</fr:form>
</div>


<form action="http://groups.ist.utl.pt/gep/inqweb/index.php?sid=85246" method="post">
	<p class="mtop15 mbottom05">
		<html:submit>
			<bean:message key="label.continue.inquiry" bundle="ALUMNI_RESOURCES" />
		</html:submit>
	</p>
</form>



<p class="mtop15 mbottom05">
	<html:link action="/alumniInquiries.do?method=prepareProfessionalInformationCreation">
		+ <bean:message key="label.create.professional.information" bundle="ALUMNI_RESOURCES" />
	</html:link>
</p>


<logic:empty name="alumni" property="jobs">
	<p>
		<em><bean:message key="alumni.no.professional.information" bundle="ALUMNI_RESOURCES" />.</em>
	</p>
</logic:empty>


<logic:notEmpty name="alumni" property="jobs">
	<logic:iterate id="eachJob" indexId="jobIndex" name="alumni" property="jobs">
		<bean:define id="jobID" name="eachJob" property="idInternal" />
		<fr:view name="eachJob" layout="tabular" schema="alumni.professional.information.job">
			<fr:layout>
				<fr:property name="subLayout" value="values"/>
				<fr:property name="subSchema" value="alumni.professional.information.job"/>
				<fr:property name="classes" value="tstyle2 thlight thright mbottom025" />
			</fr:layout>
		</fr:view>
		<p class="mtop025">
			<html:link page="<%= "/alumniInquiries.do?method=prepareUpdateProfessionalInformation&jobId=" + jobID  %>">
				<bean:message key="label.edit" bundle="ALUMNI_RESOURCES"/> 
			</html:link> | 
			<html:link href="#" onclick="<%= "document.getElementById('deleteConfirm" + jobIndex + "').style.display='block'" %>" >
				<bean:message key="label.delete" bundle="ALUMNI_RESOURCES"/> 
			</html:link>
		</p>

		<div id="<%= "deleteConfirm" + jobIndex %>" class="infoop2 width300px switchInline">
			<fr:form id="deleteForm" action="<%= "/alumniInquiries.do?method=deleteProfessionalInformation&jobId=" + jobID  %>" >
				<p class="mvert05"><bean:message key="label.confirm.delete" bundle="ALUMNI_RESOURCES" /></p>
				<p class="mvert05">
					<html:submit>
						<bean:message key="label.delete" bundle="ALUMNI_RESOURCES" />
					</html:submit>
					<html:cancel property="cancel" onclick="<%= "document.getElementById('deleteConfirm" + jobIndex + "').style.display='none'; return false;'" %>">
						<bean:message key="label.cancel" bundle="ALUMNI_RESOURCES" />
					</html:cancel>
				</p>
			</fr:form>
		</div>
		
	</logic:iterate>
</logic:notEmpty>


<logic:notEmpty name="alumni" property="jobs">
	<form action="http://groups.ist.utl.pt/gep/inqweb/index.php?sid=85246" method="post">
		<p>
			<html:submit>
				<bean:message key="label.continue.inquiry" bundle="ALUMNI_RESOURCES" />
			</html:submit>
		</p>
	</form>
</logic:notEmpty>