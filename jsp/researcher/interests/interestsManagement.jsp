<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">	

  	<h2 id='pageTitle'/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.interestsManagement.title"/> </h2>
		
		<fr:view name="researchInterests" layout="tabular-list" >
			<fr:layout>
				<fr:property name="subLayout" value="values"/>
				<fr:property name="subSchema" value="researchInterest.summary"/>

				<fr:property name="link(translations)" value="/interests/interestsManagement.do?method=manageTranslations"/>
				<fr:property name="param(translations)" value="idInternal/oid"/>
				<fr:property name="key(translations)" value="researcher.interestsManagement.manageTranslations"/>
				<fr:property name="bundle(translations)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="order(translations)" value="0"/>
				
				<fr:property name="link(down)" value="/interests/interestsManagement.do?method=down"/>
				<fr:property name="param(down)" value="idInternal/oid"/>
				<fr:property name="key(down)" value="researcher.interestsManagement.down"/>
				<fr:property name="bundle(down)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="order(down)" value="1"/>

				<fr:property name="link(up)" value="/interests/interestsManagement.do?method=up"/>
				<fr:property name="param(up)" value="idInternal/oid"/>
				<fr:property name="key(up)" value="researcher.interestsManagement.up"/>
				<fr:property name="bundle(up)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="order(up)" value="2"/>

				<fr:property name="link(delete)" value="/interests/interestsManagement.do?method=delete"/>
				<fr:property name="param(delete)" value="idInternal/oid"/>
				<fr:property name="key(delete)" value="researcher.interestsManagement.delete"/>
				<fr:property name="bundle(delete)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="order(delete)" value="3"/>
			</fr:layout>
		</fr:view>
	
		<fr:create type="net.sourceforge.fenixedu.domain.research.ResearchInterest" schema="researchInterest.simpleCreate"
	  	         action="/interests/interestsManagement.do?method=prepare">
			<fr:hidden slot="order" name="lastOrder"/>
			<fr:hidden slot="party" name="party" />
		</fr:create>
</logic:present>

