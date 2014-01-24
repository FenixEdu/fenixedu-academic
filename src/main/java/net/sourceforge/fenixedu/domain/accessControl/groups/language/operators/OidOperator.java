package net.sourceforge.fenixedu.domain.accessControl.groups.language.operators;

import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.OperatorArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.StaticArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongNumberOfArgumentsException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

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
 * @deprecated Use Bennu Groups instead
 */
@Deprecated
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
        DomainObject domainObject;
        String oid;

        if (argument instanceof StaticArgument) {
            oid = ((StaticArgument) argument).getString();
        } else {
            value = (this.value == null) ? new ParameterOperator(this, argument) : value;
            oid = (String) value.getValue();
        }

        domainObject = FenixFramework.getDomainObject(oid);

        if (!FenixFramework.isDomainObjectValid(domainObject)) {
            throw new DomainException("error.domain.invalidDomainObject", oid);
        }

        return domainObject;
    }

    @Override
    protected String getMainValueString() {
        return String.format("$O(%s)", argument(PARAMETER));
    }

}
