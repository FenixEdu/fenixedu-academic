package net.sourceforge.fenixedu.domain;

import java.util.Set;

import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.util.Verhoeff;

import org.apache.commons.lang.StringUtils;

public class LibraryCardSystem extends LibraryCardSystem_Base {

    private static final String MILLENIUM_INSTITUTION_PREFIX = "0710";

    private static final int COUNTER_SIZE = 5;

    private static final String CODE_FILLER = "0";

    public LibraryCardSystem() {
        super();
    }

    @Override
    public Group getHigherClearenceGroup() {
        Group group = super.getHigherClearenceGroup();
        if (group == null) {
            group = new FixedSetGroup();
            setHigherClearenceGroup(group);
        }
        return group;
    }

    public String generateNewMilleniumCode() {
        setMilleniumCodeCounter(getMilleniumCodeCounter() + 1);
        String baseCode =
                MILLENIUM_INSTITUTION_PREFIX
                        + StringUtils.leftPad(Integer.toString(getMilleniumCodeCounter()), COUNTER_SIZE, CODE_FILLER);
        return baseCode + Verhoeff.generateVerhoeff(baseCode);
    }

    public static class HigherClearenceGroupBuilder implements GroupBuilder {

        static final public String GROUP_EXPRESSION_NAME = "libraryHigherClearanceGroup";

        @Override
        public Group build(Object[] arguments) {
            return new Group() {

                @Override
                protected String getGroupExpressionName() {
                    return GROUP_EXPRESSION_NAME;
                }

                @Override
                protected Argument[] getExpressionArguments() {
                    return new Argument[0];
                }

                @Override
                public Set<Person> getElements() {
                    return RootDomainObject.getInstance().getLibraryCardSystem().getHigherClearenceGroup().getElements();
                }
            };
        }

        @Override
        public int getMinArguments() {
            return 0;
        }

        @Override
        public int getMaxArguments() {
            return 0;
        }

    }
}
