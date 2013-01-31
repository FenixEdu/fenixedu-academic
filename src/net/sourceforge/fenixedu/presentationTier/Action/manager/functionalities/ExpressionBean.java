package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.contents.Content;

import org.apache.commons.lang.StringUtils;

/**
 * Helper used to collect a single expression string from the user.
 * 
 * @author cfgi
 */
public class ExpressionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String expression;

	public ExpressionBean() {
		super();
	}

	public ExpressionBean(final String expression) {
		setExpression(expression);
	}

	public ExpressionBean(final Group group) {
		this(group == null ? null : group.getExpression());
	}

	public ExpressionBean(final Content content) {
		this(content == null ? null : content.getPermittedGroup());
	}

	public String getExpression() {
		return this.expression;
	}

	public void setExpression(String expression) {
		if (expression == null) {
			this.expression = null;
		} else {
			// replace \r\n or \r newlines
			this.expression = expression.replaceAll("\\r\\n?", "\n");
		}
	}

	public boolean isEmpty() {
		return StringUtils.isBlank(this.expression);
	}

}
