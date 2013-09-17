package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;


import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchContract;
import pt.ist.fenixframework.Atomic;

public class DeleteResearchContract {

    @Atomic
    public static void run(ResearchContract contract) {
        contract.delete();
    }
}