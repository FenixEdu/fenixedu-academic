<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">	

  	<h2 id='pageTitle'/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.interestsManagement.title"/> </h2>
		
		<fr:view name="researchInterests" layout="tabular-list" >
			<fr:layout>
				<fr:property name="subLayout" value="values"/>
				<fr:property name="subSchema" value="researchInterest.summary"/>
		
				<fr:property name="link(down)" value="/interests/interestsManagement.do?method=down"/>
				<fr:property name="param(down)" value="idInternal/oid"/>
				<fr:property name="key(down)" value="researcher.interestsManagement.down"/>
				<fr:property name="bundle(down)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="order(down)" value="0"/>
				<fr:property name="excludedFromLast(down)" value="true"/>
				
				<fr:property name="link(up)" value="/interests/interestsManagement.do?method=up"/>
				<fr:property name="param(up)" value="idInternal/oid"/>
				<fr:property name="key(up)" value="researcher.interestsManagement.up"/>
				<fr:property name="bundle(up)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="order(up)" value="1"/>
				<fr:property name="excludedFromFirst(up)" value="true"/>

				<fr:property name="link(edit)" value="/interests/interestsManagement.do?method=prepareEditInterest"/>
				<fr:property name="param(edit)" value="idInternal/oid"/>
				<fr:property name="key(edit)" value="researcher.interestsManagement.edit"/>
				<fr:property name="bundle(edit)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="order(edit)" value="2"/>

				<fr:property name="link(delete)" value="/interests/interestsManagement.do?method=delete"/>
				<fr:property name="param(delete)" value="idInternal/oid"/>
				<fr:property name="key(delete)" value="researcher.interestsManagement.delete"/>
				<fr:property name="bundle(delete)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="order(delete)" value="3"/>
			</fr:layout>
		</fr:view>

	<br/>	
	<html:link module="/researcher" page="/interests/interestsManagement.do?method=prepareInsertInterest">
		<bean:message bundle="RESEARCHER_RESOURCES" key="link.new.interest" />
	</html:link>
		
</logic:present>

