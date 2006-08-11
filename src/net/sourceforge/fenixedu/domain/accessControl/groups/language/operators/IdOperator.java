package net.sourceforge.fenixedu.domain.accessControl.groups.language.operators;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.OperatorArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongNumberOfArgumentsException;

/**
 * The <code>$I</code> operator obtains a domain object by it's id and type.
 * The id is obtained from a context parameter and the type is specified as an
 * argument.
 * 
 * <p>
 * <code>$I(executionCourseId, '{@link net.sourceforge.fenixedu.domain.ExecutionCourse}')</code>
 * 
 * @author cfgi
 */
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
        NumberOperator number = getNumberOperator();
        ClassOperator type = getClassOperator();

        return RootDomainObject.readDomainObjectByOID((Class) type.getValue(), (Integer) number
                .getValue());
    }

    /**
     * @return the NumberOperator that will fetch the oid
     */
    protected NumberOperator getNumberOperator() {
        if (this.number == null) {
            this.number = new NumberOperator(this, getArguments().get(PARAMETER));
        }

        return this.number;
    }

    /**
     * @return the ClassOperator that will obtain the type
     */
    protected ClassOperator getClassOperator() {
        if (this.type == null) {
            this.type = new ClassOperator(this, getArguments().get(TYPE));
        }

        return this.type;
    }

}
