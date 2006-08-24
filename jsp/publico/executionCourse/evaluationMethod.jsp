<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2>
	<bean:message key="title.evaluationMethod"/>
</h2>
<logic:empty name="executionCourse" property="evaluationMethod">
	<h3>
		<bean:message key="message.evaluation.not.available"/>
	</h3>
</logic:empty>
<logic:notEmpty name="executionCourse" property="evaluationMethod">
	<p>
		<fr:view name="executionCourse" property="evaluationMethod.evaluationElements">
			<fr:layout>
				<fr:property name="escaped" value="false" />
				<fr:property name="newlineAware" value="false" />
			</fr:layout>
		</fr:view>
	</p>
</logic:notEmpty>
