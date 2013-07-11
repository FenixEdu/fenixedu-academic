package net.sourceforge.fenixedu.applicationTier.Servico.person;


import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode;
import net.sourceforge.fenixedu.domain.functionalities.AvailabilityPolicy;
import net.sourceforge.fenixedu.domain.functionalities.ExpressionGroupAvailability;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.GroupAvailability;
import net.sourceforge.fenixedu.domain.functionalities.Module;
import pt.ist.fenixframework.Atomic;

public class CopyModuleFunctionalityToContainer {

    @Atomic
    public static void run(Module module, Container container) {
        process(module, container);
    }

    private static void process(Module module, Container container) {
        mimicPolicy(module.getAvailabilityPolicy(), container);
        for (Functionality functionality : module.getFunctionalities()) {
            final ExplicitOrderNode explicitOrderNode = new ExplicitOrderNode(container, functionality);
        }
        for (Module subModule : module.getChildren(Module.class)) {
            Section section = new Section(container, subModule.getName());
            mimicPolicy(module.getAvailabilityPolicy(), section);
            process(subModule, section);
        }
    }

    private static void mimicPolicy(AvailabilityPolicy availabilityPolicy, Content content) {
        if (availabilityPolicy instanceof GroupAvailability) {
            new GroupAvailability(content, ((GroupAvailability) availabilityPolicy).getTargetGroup());
        } else if (availabilityPolicy instanceof ExpressionGroupAvailability) {
            new ExpressionGroupAvailability(content, ((ExpressionGroupAvailability) availabilityPolicy).getExpression());
        }
    }
}