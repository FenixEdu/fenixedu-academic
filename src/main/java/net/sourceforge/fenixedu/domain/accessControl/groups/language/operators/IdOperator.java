package net.sourceforge.fenixedu.domain.accessControl.groups.language.operators;

import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupContextProvider;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.OperatorArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.StaticArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongNumberOfArgumentsException;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * The <code>$I</code> operator obtains a domain object by it's id and type. The
 * id is obtained from a context parameter and the type is specified as an
 * argument. Optionally you can give the id explicitly as a number.
 * 
 * <p>
 * <code>$I(executionCourseId, '{@link net.sourceforge.fenixedu.domain.ExecutionCourse}')</code>
 * <code>$I(1234, '{@link net.sourceforge.fenixedu.domain.ExecutionCourse}')</code>
 * 
 * @author cfgi
 */
@Deprecated
public class IdOperator extends OperatorArgument {

    private static final long serialVersionUID = 1L;

    private static final int PARAMETER = 0;
    private static final int TYPE = 1;

    private NumberOperator number;
    private ClassOperator type;

    public IdOperator(Argument parameter, Argument type) {
        super();

        addArgument(parameter);
        addArgument(type);
    }

    @Override
    protected void checkOperatorArguments() {
        int size = getArguments().size();

        if (size != 2) {
            throw new WrongNumberOfArgumentsException(size, 2, 2);
        }
    }

    @Override
    protected DomainObject execute() {
        throw new UnsupportedOperationException("Arguments by IdInternal are no longer supported!");
    }

    protected Integer getNumber() {
        Argument argument = argument(PARAMETER);
        if (argument instanceof StaticArgument) {
            StaticArgument staticArgument = (StaticArgument) argument;

            if (staticArgument.isNumber()) {
                return staticArgument.getNumber();
            }
        }

        return (Integer) getNumberOperator().getValue();
    }

    /**
     * @return the NumberOperator that will fetch the oid
     */
    protected NumberOperator getNumberOperator() {
        if (this.number == null) {
            this.number = new NumberOperator((GroupContextProvider) this, new ParameterOperator(this, argument(PARAMETER)));
        }

        return this.number;
    }

    @Override
    public boolean isDynamic() {
        checkOperatorArguments();

        Argument argument = argument(PARAMETER);

        if (argument instanceof StaticArgument) {
            StaticArgument staticArgument = (StaticArgument) argument;

            if (staticArgument.isString()) {
                return true;
            }
        }

        return argument(TYPE).isDynamic();
    }

    /**
     * @return the target domain class type
     */
    protected Class getClassType() {
        if (this.type == null) {
            this.type = new ClassOperator(this, argument(TYPE));
        }

        return (Class) this.type.getValue();
    }

    @Override
    public String getMainValueString() {
        return String.format("$I(%s, %s)", argument(PARAMETER), argument(TYPE));
    }

}
