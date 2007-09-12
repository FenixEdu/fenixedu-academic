<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<div>
<ul class="ostructure">
	<logic:notEmpty name="researchUnit" property="researcherContracts">
		<li class="osfunction">
			<p><strong><bean:message key="label.researchers" bundle="SITE_RESOURCES"/></strong></p>
		
		<fr:view name="researchUnit" property="researchers">
			<fr:layout name="person-presentation-card">
				<fr:property name="sortBy" value="name"/>
				<fr:property name="eachLayout" value="values-as-list"/>
				<fr:property name="eachSchema" value="present.research.member"/>
			</fr:layout>
		</fr:view>
		</li>
	</logic:notEmpty>

	<logic:notEmpty name="researchUnit" property="technicalStaff">
		<li class="osfunction">
			<p><strong><bean:message key="label.technicalStaff" bundle="SITE_RESOURCES"/></strong></p>
				<fr:view name="researchUnit" property="technicalStaff">
					<fr:layout name="person-presentation-card">
						<fr:property name="sortBy" value="name"/>
						<fr:property name="eachLayout" value="values-as-list"/>
						<fr:property name="eachSchema" value="present.research.member"/>
					</fr:layout>
				</fr:view>		
		</li>
	</logic:notEmpty>
	
	<logic:notEmpty name="researchUnit" property="internships">
		<li class="osfunction">
			<p><strong><bean:message key="label.internship" bundle="SITE_RESOURCES"/></strong></p>
	
				<fr:view name="researchUnit" property="internships">
					<fr:layout name="person-presentation-card">
						<fr:property name="sortBy" value="name"/>
						<fr:property name="eachLayout" value="values-as-list"/>
						<fr:property name="eachSchema" value="present.research.member"/>
					</fr:layout>
				</fr:view>

		</li>
	</logic:notEmpty>
	
	<logic:notEmpty name="researchUnit" property="scholarships">
		<li class="osfunction">
			<p><strong><bean:message key="label.scholarship" bundle="SITE_RESOURCES"/></strong></p>
		
				<fr:view name="researchUnit" property="scholarships">
					<fr:layout name="person-presentation-card">
						<fr:property name="sortBy" value="name"/>
						<fr:property name="eachLayout" value="values-as-list"/>
						<fr:property name="eachSchema" value="present.research.member"/>
					</fr:layout>
				</fr:view>

		</li>
	</logic:notEmpty>
</ul>
</div>