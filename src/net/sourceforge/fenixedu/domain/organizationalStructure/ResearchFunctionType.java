package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public enum ResearchFunctionType {

	PERMANENT_RESEARCHER,
	INVITED_RESEARCHER,
	TECHNICAL_STAFF,
	COLLABORATORS,
	OTHER_STAFF,
	PHD_STUDENT,
	MSC_STUDENT,
	POST_DOC_STUDENT
	;
	
	public String getName() {
		return RenderUtils.getEnumString(this);
	}
}
