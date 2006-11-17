package net.sourceforge.fenixedu.domain.accessControl.groups.language.operators;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupContextProvider;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.OperatorArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.StaticArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongNumberOfArgumentsException;

/**
 * The <code>$I</code> operator obtains a domain object by it's id and type.
 * The id is obtained from a context parameter and the type is specified as an
 * argument. Optionally you can give the id explicitly as a number.
 * 
 * <p>
 * <code>$I(executionCourseId, '{@link net.sourceforge.fenixedu.domain.ExecutionCourse}')</code>
 * <code>$I(1234, '{@link net.sourceforge.fenixedu.domain.ExecutionCourse}')</code>
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

    public IdOperator(DomainObject object) {
        this(new StaticArgument(object.getIdInternal()), new StaticArgument(ClassOperator
                .simplify(object.getClass().getName())));
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
        Integer number = getNumber();
        Class type = getClassType();

        return RootDomainObject.readDomainObjectByOID(type, number);
    }

    protected Integer getNumber() {
        Argument argument = argument(PARAMETER);
        if (! argument.isDynamic()) {
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
            this.number = new NumberOperator((GroupContextProvider) this, argument(PARAMETER));
        }

        return this.number;
    }

    /**
     * @return the target domain class type
     */
    protected Class getClassType() {
        if (this.type == null) {
            this.type = new ClassOperator((GroupContextProvider) this, argument(TYPE));
        }

        return (Class) this.type.getValue();
    }

    @Override
    public String getMainValueString() {
        return String.format("$I(%s, %s)", argument(PARAMETER), argument(TYPE));
    }

}
