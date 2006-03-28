<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="interest" name="interestId"/>

<logic:present role="RESEARCHER">		
  	<h2 id='pageTitle'/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.interestsManagement.manageTranslations.title"/> </h2>
	<fr:view name="interestTranslations" layout="tabular-list">
		<fr:layout>
			<fr:property name="subLayout" value="values"/>
			<fr:property name="subSchema" value="researchInterestTranslation"/>
			
			<fr:property name="link(remove)" value="<%= "/interests/interestsManagement.do?method=deleteTranslation&oid=" + interest %>"/>
			<fr:property name="param(remove)" value="language"/>
			<fr:property name="key(remove)" value="researcher.interestsManagement.manageTranslations.remove"/>
			<fr:property name="bundle(remove)" value="RESEARCHER_RESOURCES"/>
			<fr:property name="order(remove)" value="0"/>

		</fr:layout>
	</fr:view>
	
	<fr:create type="net.sourceforge.fenixedu.domain.research.ResearchInterest$ResearchInterestTranslation" schema="researchInterestTranslation.simpleCreate"
       	   action="<%= "/interests/interestsManagement.do?method=manageTranslations&oid=" + interest %>">
	</fr:create>	
	
</logic:present>
	