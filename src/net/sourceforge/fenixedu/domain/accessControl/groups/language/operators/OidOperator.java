package net.sourceforge.fenixedu.domain.accessControl.groups.language.operators;

import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.OperatorArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.StaticArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongNumberOfArgumentsException;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * The <code>$O</code> operator obtains a domain object by it's OID (domain
 * object's externalId). The oid is obtained from a context parameter or
 * explicitly as a String.
 * 
 * <p>
 * <code>$O(executionCourseOid)</code>
 * </p>
 * <p>
 * <code>$O(1902670573189)</code>
 * </p>
 * 
 * @author Pedro Santos
 */
public class OidOperator extends OperatorArgument {
    private static final int PARAMETER = 0;

    private ParameterOperator value;

    public OidOperator(Argument parameter) {
	addArgument(parameter);
    }

    public OidOperator(DomainObject domainObject) {
	this(new StaticArgument(domainObject.getExternalId()));
    }

    @Override
    protected void checkOperatorArguments() {
	int size = getArguments().size();

	if (size != 1) {
	    throw new WrongNumberOfArgumentsException(size, 1, 1);
	}
    }

    @Override
    protected DomainObject execute() {
	Argument argument = argument(PARAMETER);
	if (argument instanceof StaticArgument) {
	    StaticArgument staticArgument = (StaticArgument) argument;
	    return AbstractDomainObject.fromExternalId(staticArgument.getString());
	}

	if (this.value == null) {
	    this.value = new ParameterOperator(this, argument(PARAMETER));
	}
	return AbstractDomainObject.fromExternalId((String) value.getValue());
    }

    @Override
    protected String getMainValueString() {
	return String.format("$O(%s)", argument(PARAMETER));
    }

}
