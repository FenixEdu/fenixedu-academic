package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.domain.contents.Node;
import pt.ist.fenixframework.Atomic;

public class DeleteNode {

    @Atomic
    public static void run(Node node) {
        node.delete();
    }
}