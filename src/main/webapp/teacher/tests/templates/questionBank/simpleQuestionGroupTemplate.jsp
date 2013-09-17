<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-template" prefix="ft" %>

<span style="font-weight: bolder;">&gt;
<ft:view layout="short" />
</span>

<span style="font-style: italic; margin-left: 2em;">
(Grupos: 
<ft:view property="childQuestionGroupsCount" />, Perguntas: 
<ft:view property="childAtomicQuestionsCount" />)
</span>
