<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-template.tld" prefix="ft" %>

<span style="font-weight: bolder;">&gt;
<ft:view layout="short" />
</span>

<span style="font-style: italic; margin-left: 2em;">
(Grupos: 
<ft:view property="childQuestionGroupsCount" />, Perguntas: 
<ft:view property="childAtomicQuestionsCount" />)
</span>
