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
	<p>
		<em><bean:message key="message.evaluation.not.available"/></em>
	</p>
</logic:empty>

<logic:notEmpty name="executionCourse" property="evaluationMethod">
	<div class="mtop1" style="line-height: 1.5em;">
		<fr:view name="executionCourse" property="evaluationMethod.evaluationElements">
			<fr:layout>
				<fr:property name="escaped" value="false" />
				<fr:property name="newlineAware" value="false" />
			</fr:layout>
		</fr:view>
	</div>
</logic:notEmpty>
