package pt.utl.ist.codeGenerator.database;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.FunctionalityCall;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;
import net.sourceforge.fenixedu.domain.contents.Node;
import net.sourceforge.fenixedu.domain.contents.Portal;
import net.sourceforge.fenixedu.domain.functionalities.ExpressionGroupAvailability;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.Module;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CreateFunctionallityTree {
    public static void doIt() {
        Bennu bennu = Bennu.getInstance();
        // DEBUG: tldNode x823 1198295917755 null
        Module x823 = new Module(MultiLanguageString.importFromString("en4:Rootpt4:Raiz"), "/");
        x823.setNormalizedName(MultiLanguageString.importFromString("en4:rootpt4:raiz"));
        Module x822 = new Module(MultiLanguageString.importFromString("pt7:Pessoal"), "/person");
        x823.addChild(x822);
        x822.setNormalizedName(MultiLanguageString.importFromString("pt7:pessoal"));
        x822.setExecutionPath("/person/index.do");
        x822.setVisible(true);
        Functionality x821 = new Functionality(MultiLanguageString.importFromString("pt7:Pessoal"));
        x822.addChild(x821);
        x821.setTitle(MultiLanguageString.importFromString("pt7:Pessoal"));
        x821.setNormalizedName(MultiLanguageString.importFromString("pt7:pessoal"));
        x821.setExecutionPath("/index.do");
        x821.setVisible(true);
        ExpressionGroupAvailability x1605 = new ExpressionGroupAvailability(x822, "role(PERSON)");
        x1605.setTargetGroup(Group.fromString("role(PERSON)"));
        x822.setAvailabilityPolicy(x1605);
        Module x826 = new Module(MultiLanguageString.importFromString("pt9:Estudante"), "/student");
        x823.addChild(x826);
        x826.setNormalizedName(MultiLanguageString.importFromString("pt9:estudante"));
        x826.setExecutionPath("/student/index.do");
        x826.setVisible(true);
        Functionality x825 = new Functionality(MultiLanguageString.importFromString("pt9:Estudante"));
        x826.addChild(x825);
        x825.setTitle(MultiLanguageString.importFromString("pt9:Estudante"));
        x825.setNormalizedName(MultiLanguageString.importFromString("pt9:estudante"));
        x825.setExecutionPath("/index.do");
        x825.setVisible(true);
        ExpressionGroupAvailability x1606 = new ExpressionGroupAvailability(x826, "role(STUDENT)");
        x1606.setTargetGroup(Group.fromString("role(STUDENT)"));
        x826.setAvailabilityPolicy(x1606);
        Module x829 = new Module(MultiLanguageString.importFromString("pt8:Docência"), "/teacher");
        x823.addChild(x829);
        x829.setNormalizedName(MultiLanguageString.importFromString("pt8:docencia"));
        x829.setExecutionPath("/teacher/index.do");
        x829.setVisible(true);
        Functionality x828 = new Functionality(MultiLanguageString.importFromString("pt8:Docência"));
        x829.addChild(x828);
        x828.setTitle(MultiLanguageString.importFromString("pt8:Docência"));
        x828.setNormalizedName(MultiLanguageString.importFromString("pt8:docencia"));
        x828.setExecutionPath("/index.do");
        x828.setVisible(true);
        ExpressionGroupAvailability x1607 = new ExpressionGroupAvailability(x829, "role(TEACHER)");
        x1607.setTargetGroup(Group.fromString("role(TEACHER)"));
        x829.setAvailabilityPolicy(x1607);
        Module x832 = new Module(MultiLanguageString.importFromString("pt12:Departamento"), "/departmentMember");
        x823.addChild(x832);
        x832.setNormalizedName(MultiLanguageString.importFromString("pt12:departamento"));
        x832.setExecutionPath("/departmentMember/index.do");
        x832.setVisible(true);
        Functionality x831 = new Functionality(MultiLanguageString.importFromString("pt12:Departamento"));
        x832.addChild(x831);
        x831.setTitle(MultiLanguageString.importFromString("pt12:Departamento"));
        x831.setNormalizedName(MultiLanguageString.importFromString("pt12:departamento"));
        x831.setExecutionPath("/index.do");
        x831.setVisible(true);
        ExpressionGroupAvailability x1608 = new ExpressionGroupAvailability(x832, "role(DEPARTMENT_MEMBER)");
        x1608.setTargetGroup(Group.fromString("role(DEPARTMENT_MEMBER)"));
        x832.setAvailabilityPolicy(x1608);
        Module x835 = new Module(MultiLanguageString.importFromString("pt18:Gestão de Recursos"), "/resourceAllocationManager");
        x823.addChild(x835);
        x835.setNormalizedName(MultiLanguageString.importFromString("pt18:gestao-de-recursos"));
        x835.setExecutionPath("/resourceAllocationManager/paginaPrincipal.do");
        x835.setVisible(true);
        Functionality x834 = new Functionality(MultiLanguageString.importFromString("pt18:Gestão de Recursos"));
        x835.addChild(x834);
        x834.setTitle(MultiLanguageString.importFromString("pt18:Gestão de Recursos"));
        x834.setNormalizedName(MultiLanguageString.importFromString("pt18:gestao-de-recursos"));
        x834.setExecutionPath("/index.do");
        x834.setVisible(true);
        ExpressionGroupAvailability x1609 = new ExpressionGroupAvailability(x835, "role(RESOURCE_ALLOCATION_MANAGER)");
        x1609.setTargetGroup(Group.fromString("role(RESOURCE_ALLOCATION_MANAGER)"));
        x835.setAvailabilityPolicy(x1609);
        Module x838 =
                new Module(MultiLanguageString.importFromString("pt36:Portal do Candidato a Pós-Graduações"),
                        "/masterDegreeCandidate");
        x823.addChild(x838);
        x838.setNormalizedName(MultiLanguageString.importFromString("pt36:portal-do-candidato-a-pos-graduacoes"));
        x838.setExecutionPath("/candidato/index.do");
        x838.setVisible(true);
        Functionality x837 = new Functionality(MultiLanguageString.importFromString("pt36:Portal do Candidato a Pós-Graduações"));
        x838.addChild(x837);
        x837.setTitle(MultiLanguageString.importFromString("pt36:Portal do Candidato a Pós-Graduações"));
        x837.setNormalizedName(MultiLanguageString.importFromString("pt36:portal-do-candidato-a-pos-graduacoes"));
        x837.setExecutionPath("/index.do");
        x837.setVisible(true);
        ExpressionGroupAvailability x1610 = new ExpressionGroupAvailability(x838, "role(MASTER_DEGREE_CANDIDATE)");
        x1610.setTargetGroup(Group.fromString("role(MASTER_DEGREE_CANDIDATE)"));
        x838.setAvailabilityPolicy(x1610);
        Module x841 =
                new Module(MultiLanguageString.importFromString("pt35:Portal de Serviços de Pós-Graduação"),
                        "/masterDegreeAdministrativeOffice");
        x823.addChild(x841);
        x841.setNormalizedName(MultiLanguageString.importFromString("pt35:portal-de-servicos-de-pos-graduacao"));
        x841.setExecutionPath("/posGraduacao/index.do");
        x841.setVisible(true);
        Functionality x840 = new Functionality(MultiLanguageString.importFromString("pt35:Portal de Serviços de Pós-Graduação"));
        x841.addChild(x840);
        x840.setTitle(MultiLanguageString.importFromString("pt35:Portal de Serviços de Pós-Graduação"));
        x840.setNormalizedName(MultiLanguageString.importFromString("pt35:portal-de-servicos-de-pos-graduacao"));
        x840.setExecutionPath("/index.do");
        x840.setVisible(true);
        ExpressionGroupAvailability x1611 = new ExpressionGroupAvailability(x841, "role(MASTER_DEGREE_ADMINISTRATIVE_OFFICE)");
        x1611.setTargetGroup(Group.fromString("role(MASTER_DEGREE_ADMINISTRATIVE_OFFICE)"));
        x841.setAvailabilityPolicy(x1611);
        Module x844 = new Module(MultiLanguageString.importFromString("pt11:Coordenador"), "/coordinator");
        x823.addChild(x844);
        x844.setNormalizedName(MultiLanguageString.importFromString("pt11:coordenador"));
        x844.setExecutionPath("/coordinator/index.do");
        x844.setVisible(true);
        Functionality x843 = new Functionality(MultiLanguageString.importFromString("pt11:Coordenador"));
        x844.addChild(x843);
        x843.setTitle(MultiLanguageString.importFromString("pt11:Coordenador"));
        x843.setNormalizedName(MultiLanguageString.importFromString("pt11:coordenador"));
        x843.setExecutionPath("/index.do");
        x843.setVisible(true);
        ExpressionGroupAvailability x1612 = new ExpressionGroupAvailability(x844, "role(COORDINATOR)");
        x1612.setTargetGroup(Group.fromString("role(COORDINATOR)"));
        x844.setAvailabilityPolicy(x1612);
        Module x847 = new Module(MultiLanguageString.importFromString("pt11:Funcionário"), "/employee");
        x823.addChild(x847);
        x847.setNormalizedName(MultiLanguageString.importFromString("pt11:funcionario"));
        x847.setExecutionPath("/employee/index.do");
        x847.setVisible(true);
        Functionality x846 = new Functionality(MultiLanguageString.importFromString("pt11:Funcionário"));
        x847.addChild(x846);
        x846.setTitle(MultiLanguageString.importFromString("pt11:Funcionário"));
        x846.setNormalizedName(MultiLanguageString.importFromString("pt11:funcionario"));
        x846.setExecutionPath("/index.do");
        x846.setVisible(true);
        ExpressionGroupAvailability x1613 = new ExpressionGroupAvailability(x847, "role(EMPLOYEE)");
        x1613.setTargetGroup(Group.fromString("role(EMPLOYEE)"));
        x847.setAvailabilityPolicy(x1613);
        Module x851 = new Module(MultiLanguageString.importFromString("pt15:Àrea de Pessoal"), "/personnelSection");
        x823.addChild(x851);
        x851.setNormalizedName(MultiLanguageString.importFromString("pt15:area-de-pessoal"));
        x851.setExecutionPath("/personnelSection/index.do");
        x851.setVisible(true);
        Module x850 = new Module(MultiLanguageString.importFromString("pt25:Consultas por Funcionário"), "/");
        x851.addChild(x850);
        x850.setNormalizedName(MultiLanguageString.importFromString("pt25:consultas-por-funcionario"));
        x850.setExecutionPath("");
        x850.setVisible(true);
        Functionality x849 = new Functionality(MultiLanguageString.importFromString("pt7:Verbete"));
        x850.addChild(x849);
        x849.setTitle(MultiLanguageString.importFromString("pt7:Verbete"));
        x849.setNormalizedName(MultiLanguageString.importFromString("pt7:verbete"));
        x849.setExecutionPath("/viewEmployeeAssiduousness.do?method=chooseEmployee&action=showWorkSheet");
        x849.setVisible(true);
        Functionality x852 = new Functionality(MultiLanguageString.importFromString("pt7:Horário"));
        x850.addChild(x852);
        x852.setTitle(MultiLanguageString.importFromString("pt7:Horário"));
        x852.setNormalizedName(MultiLanguageString.importFromString("pt7:horario"));
        x852.setExecutionPath("/viewEmployeeAssiduousness.do?method=chooseEmployee&action=showSchedule");
        x852.setVisible(true);
        Functionality x853 = new Functionality(MultiLanguageString.importFromString("pt9:Marcações"));
        x850.addChild(x853);
        x853.setTitle(MultiLanguageString.importFromString("pt9:Marcações"));
        x853.setNormalizedName(MultiLanguageString.importFromString("pt9:marcacoes"));
        x853.setExecutionPath("/viewEmployeeAssiduousness.do?method=chooseEmployee&action=showClockings");
        x853.setVisible(true);
        Functionality x854 = new Functionality(MultiLanguageString.importFromString("pt13:Justificações"));
        x850.addChild(x854);
        x854.setTitle(MultiLanguageString.importFromString("pt13:Justificações"));
        x854.setNormalizedName(MultiLanguageString.importFromString("pt13:justificacoes"));
        x854.setExecutionPath("/viewEmployeeAssiduousness.do?method=chooseEmployee&action=showJustifications");
        x854.setVisible(true);
        Functionality x855 = new Functionality(MultiLanguageString.importFromString("pt12:Resumo Saldo"));
        x850.addChild(x855);
        x855.setTitle(MultiLanguageString.importFromString("pt12:Resumo Saldo"));
        x855.setNormalizedName(MultiLanguageString.importFromString("pt12:resumo-saldo"));
        x855.setExecutionPath("/viewEmployeeAssiduousness.do?method=chooseEmployee&action=showBalanceResume");
        x855.setVisible(true);
        Functionality x856 = new Functionality(MultiLanguageString.importFromString("pt6:Férias"));
        x850.addChild(x856);
        x856.setTitle(MultiLanguageString.importFromString("pt6:Férias"));
        x856.setNormalizedName(MultiLanguageString.importFromString("pt6:ferias"));
        x856.setExecutionPath("/viewEmployeeAssiduousness.do?method=chooseEmployee&action=showVacations");
        x856.setVisible(true);
        Functionality x857 = new Functionality(MultiLanguageString.importFromString("pt6:Status"));
        x850.addChild(x857);
        x857.setTitle(MultiLanguageString.importFromString("pt6:Status"));
        x857.setNormalizedName(MultiLanguageString.importFromString("pt6:status"));
        x857.setExecutionPath("/viewEmployeeAssiduousness.do?method=chooseEmployee&action=showStatus");
        Functionality x858 = new Functionality(MultiLanguageString.importFromString("en13:Vacations Mappt14:Mapa de Férias"));
        x850.addChild(x858);
        x858.setTitle(MultiLanguageString.importFromString("en13:Vacations Mappt14:Mapa de Férias"));
        x858.setNormalizedName(MultiLanguageString.importFromString("en13:vacations-mappt14:mapa-de-ferias"));
        x858.setExecutionPath("/viewEmployeeAssiduousness.do?method=chooseEmployee&action=showVacationsMap");
        ExpressionGroupAvailability x1614 =
                new ExpressionGroupAvailability(x850, "assiduousnessSectionStaff || assiduousnessManagers");
        x1614.setTargetGroup(Group.fromString("assiduousnessSectionStaff || assiduousnessManagers"));
        x850.setAvailabilityPolicy(x1614);
        Module x861 = new Module(MultiLanguageString.importFromString("pt21:Todos os Funcionários"), "/");
        x851.addChild(x861);
        x861.setTitle(MultiLanguageString.importFromString("pt21:Todos os Funcionários"));
        x861.setNormalizedName(MultiLanguageString.importFromString("pt21:todos-os-funcionarios"));
        x861.setExecutionPath("");
        x861.setVisible(true);
        Functionality x860 = new Functionality(MultiLanguageString.importFromString("pt20:Inserir Justificação"));
        x861.addChild(x860);
        x860.setTitle(MultiLanguageString.importFromString("pt20:Inserir Justificação"));
        x860.setNormalizedName(MultiLanguageString.importFromString("pt20:inserir-justificacao"));
        x860.setExecutionPath("/employeeAssiduousness.do?method=insertJustification");
        x860.setVisible(true);
        ExpressionGroupAvailability x1615 = new ExpressionGroupAvailability(x861, "assiduousnessManagers");
        x1615.setTargetGroup(Group.fromString("assiduousnessManagers"));
        x861.setAvailabilityPolicy(x1615);
        Module x864 = new Module(MultiLanguageString.importFromString("pt9:Consultas"), "/");
        x851.addChild(x864);
        x864.setTitle(MultiLanguageString.importFromString("pt9:Consultas"));
        x864.setNormalizedName(MultiLanguageString.importFromString("pt9:consultas"));
        x864.setExecutionPath("");
        x864.setVisible(true);
        Functionality x863 = new Functionality(MultiLanguageString.importFromString("pt8:Horários"));
        x864.addChild(x863);
        x863.setTitle(MultiLanguageString.importFromString("pt8:Horários"));
        x863.setNormalizedName(MultiLanguageString.importFromString("pt8:horarios"));
        x863.setExecutionPath("/assiduousnessParametrization.do?method=showSchedules");
        x863.setVisible(true);
        Functionality x865 = new Functionality(MultiLanguageString.importFromString("pt13:Justificações"));
        x864.addChild(x865);
        x865.setTitle(MultiLanguageString.importFromString("pt13:Justificações"));
        x865.setNormalizedName(MultiLanguageString.importFromString("pt13:justificacoes"));
        x865.setExecutionPath("/assiduousnessParametrization.do?method=showJustificationMotives");
        x865.setVisible(true);
        Functionality x866 = new Functionality(MultiLanguageString.importFromString("pt14:Regularizações"));
        x864.addChild(x866);
        x866.setTitle(MultiLanguageString.importFromString("pt14:Regularizações"));
        x866.setNormalizedName(MultiLanguageString.importFromString("pt14:regularizacoes"));
        x866.setExecutionPath("/assiduousnessParametrization.do?method=showRegularizationMotives");
        x866.setVisible(true);
        Functionality x867 = new Functionality(MultiLanguageString.importFromString("pt11:Tolerâncias"));
        x864.addChild(x867);
        x867.setTitle(MultiLanguageString.importFromString("pt11:Tolerâncias"));
        x867.setNormalizedName(MultiLanguageString.importFromString("pt11:tolerancias"));
        x867.setExecutionPath("/assiduousnessParametrization.do?method=showAssiduousnessExemptions");
        Functionality x868 = new Functionality(MultiLanguageString.importFromString("pt6:Status"));
        x864.addChild(x868);
        x868.setTitle(MultiLanguageString.importFromString("pt6:Status"));
        x868.setNormalizedName(MultiLanguageString.importFromString("pt6:status"));
        x868.setExecutionPath("/assiduousnessParametrization.do?method=showAssiduousnessStatus");
        ExpressionGroupAvailability x1616 =
                new ExpressionGroupAvailability(x864, "assiduousnessSectionStaff || assiduousnessManagers");
        x1616.setTargetGroup(Group.fromString("assiduousnessSectionStaff || assiduousnessManagers"));
        x864.setAvailabilityPolicy(x1616);
        Module x871 = new Module(MultiLanguageString.importFromString("pt8:Exportar"), "/");
        x851.addChild(x871);
        x871.setTitle(MultiLanguageString.importFromString("pt8:Exportar"));
        x871.setNormalizedName(MultiLanguageString.importFromString("pt8:exportar"));
        x871.setExecutionPath("");
        x871.setVisible(true);
        Functionality x870 = new Functionality(MultiLanguageString.importFromString("pt17:Exportar Verbetes"));
        x871.addChild(x870);
        x870.setTitle(MultiLanguageString.importFromString("pt17:Exportar Verbetes"));
        x870.setNormalizedName(MultiLanguageString.importFromString("pt17:exportar-verbetes"));
        x870.setExecutionPath("/exportAssiduousness.do?method=chooseYearMonth&action=exportWorkSheets&chooseBetweenDates=true");
        x870.setVisible(true);
        Functionality x872 = new Functionality(MultiLanguageString.importFromString("pt16:Exportar Resumos"));
        x871.addChild(x872);
        x872.setTitle(MultiLanguageString.importFromString("pt16:Exportar Resumos"));
        x872.setNormalizedName(MultiLanguageString.importFromString("pt16:exportar-resumos"));
        x872.setExecutionPath("/exportAssiduousness.do?method=chooseYearMonth&action=exportMonthResume");
        x872.setVisible(true);
        Functionality x873 = new Functionality(MultiLanguageString.importFromString("pt22:Exportar Justificações"));
        x871.addChild(x873);
        x873.setTitle(MultiLanguageString.importFromString("pt22:Exportar Justificações"));
        x873.setNormalizedName(MultiLanguageString.importFromString("pt22:exportar-justificacoes"));
        x873.setExecutionPath("/exportAssiduousness.do?method=chooseYearMonth&action=exportJustifications&chooseBetweenDates=true");
        x873.setVisible(true);
        Functionality x874 = new Functionality(MultiLanguageString.importFromString("pt19:Exportar Destacados"));
        x871.addChild(x874);
        x874.setTitle(MultiLanguageString.importFromString("pt19:Exportar Destacados"));
        x874.setNormalizedName(MultiLanguageString.importFromString("pt19:exportar-destacados"));
        x874.setExecutionPath("/exportAssiduousness.do?method=prepareExportAssignedEmployees&action=exportAssignedEmployees");
        x874.setVisible(true);
        Functionality x875 = new Functionality(MultiLanguageString.importFromString("pt14:Exportar ADIST"));
        x871.addChild(x875);
        x875.setNormalizedName(MultiLanguageString.importFromString("pt14:exportar-adist"));
        x875.setExecutionPath("/exportAssiduousness.do?method=prepareExportADISTEmployees&action=exportADISTEmployees");
        Functionality x876 = new Functionality(MultiLanguageString.importFromString("pt15:Exportar Férias"));
        x871.addChild(x876);
        x876.setTitle(MultiLanguageString.importFromString("pt15:Exportar Férias"));
        x876.setNormalizedName(MultiLanguageString.importFromString("pt15:exportar-ferias"));
        x876.setExecutionPath("/exportAssiduousness.do?method=chooseYearMonth&action=exportVacations&chooseYear=true");
        ExpressionGroupAvailability x1617 =
                new ExpressionGroupAvailability(x871, "assiduousnessSectionStaff || assiduousnessManagers");
        x1617.setTargetGroup(Group.fromString("assiduousnessSectionStaff || assiduousnessManagers"));
        x871.setAvailabilityPolicy(x1617);
        Module x879 = new Module(MultiLanguageString.importFromString("pt12:Fecho do Mês"), "/");
        x851.addChild(x879);
        x879.setTitle(MultiLanguageString.importFromString("pt12:Fecho do Mês"));
        x879.setNormalizedName(MultiLanguageString.importFromString("pt12:fecho-do-mes"));
        x879.setExecutionPath("");
        x879.setVisible(true);
        Functionality x878 = new Functionality(MultiLanguageString.importFromString("pt21:Fechar Mês e Exportar"));
        x879.addChild(x878);
        x878.setTitle(MultiLanguageString.importFromString("pt21:Fechar Mês e Exportar"));
        x878.setNormalizedName(MultiLanguageString.importFromString("pt21:fechar-mes-e-exportar"));
        x878.setExecutionPath("/monthClosure.do?method=prepareToCloseMonth");
        x878.setVisible(true);
        ExpressionGroupAvailability x1618 = new ExpressionGroupAvailability(x879, "assiduousnessManagers");
        x1618.setTargetGroup(Group.fromString("assiduousnessManagers"));
        x879.setAvailabilityPolicy(x1618);
        Module x882 = new Module(MultiLanguageString.importFromString("pt23:Trabalho Extraordinário"), "/");
        x851.addChild(x882);
        x882.setTitle(MultiLanguageString.importFromString("pt23:Trabalho Extraordinário"));
        x882.setNormalizedName(MultiLanguageString.importFromString("pt23:trabalho-extraordinario"));
        x882.setExecutionPath("");
        x882.setVisible(true);
        Functionality x881 = new Functionality(MultiLanguageString.importFromString("pt19:Inserir Autorização"));
        x882.addChild(x881);
        x881.setTitle(MultiLanguageString.importFromString("pt19:Inserir Autorização"));
        x881.setNormalizedName(MultiLanguageString.importFromString("pt19:inserir-autorizacao"));
        x881.setExecutionPath("/createExtraWorkAuthorization.do?method=prepareCreateExtraWorkAuthorization");
        x881.setVisible(true);
        Functionality x883 = new Functionality(MultiLanguageString.importFromString("pt16:Ver Autorizações"));
        x882.addChild(x883);
        x883.setTitle(MultiLanguageString.importFromString("pt16:Ver Autorizações"));
        x883.setNormalizedName(MultiLanguageString.importFromString("pt16:ver-autorizacoes"));
        x883.setExecutionPath("/manageExtraWorkAuthorization.do?method=prepareExtraWorkAuthorizationsSearch");
        x883.setVisible(true);
        Functionality x884 = new Functionality(MultiLanguageString.importFromString("pt17:Pedidos Pagamento"));
        x882.addChild(x884);
        x884.setTitle(MultiLanguageString.importFromString("pt17:Pedidos Pagamento"));
        x884.setNormalizedName(MultiLanguageString.importFromString("pt17:pedidos-pagamento"));
        x884.setExecutionPath("/extraWorkPaymentRequest.do?method=chooseUnitYearMonth");
        x884.setVisible(true);
        Functionality x885 = new Functionality(MultiLanguageString.importFromString("pt19:Verbas Centro Custo"));
        x882.addChild(x885);
        x885.setTitle(MultiLanguageString.importFromString("pt19:Verbas Centro Custo"));
        x885.setNormalizedName(MultiLanguageString.importFromString("pt19:verbas-centro-custo"));
        x885.setExecutionPath("/manageUnitsExtraWorkAmounts.do?method=chooseYear");
        x885.setVisible(true);
        Functionality x886 = new Functionality(MultiLanguageString.importFromString("pt26:Fechar Trab Extraordinário"));
        x882.addChild(x886);
        x886.setTitle(MultiLanguageString.importFromString("pt27:Fechar Trab. Extraordinário"));
        x886.setNormalizedName(MultiLanguageString.importFromString("pt26:fechar-trab-extraordinario"));
        x886.setExecutionPath("/monthClosure.do?method=prepareToCloseExtraWorkMonth");
        x886.setVisible(true);
        Functionality x887 = new Functionality(MultiLanguageString.importFromString("pt25:Exportar por Funcionários"));
        x882.addChild(x887);
        x887.setTitle(MultiLanguageString.importFromString("pt25:Exportar por Funcionários"));
        x887.setNormalizedName(MultiLanguageString.importFromString("pt25:exportar-por-funcionarios"));
        x887.setExecutionPath("/exportExtraWork.do?method=chooseYearMonth&action=exportByEmployees");
        x887.setVisible(true);
        Functionality x888 = new Functionality(MultiLanguageString.importFromString("pt21:Exportar por Unidades"));
        x882.addChild(x888);
        x888.setTitle(MultiLanguageString.importFromString("pt21:Exportar por Unidades"));
        x888.setNormalizedName(MultiLanguageString.importFromString("pt21:exportar-por-unidades"));
        x888.setExecutionPath("/exportExtraWork.do?method=chooseYearMonth&action=exportByUnits&chooseMonth=false");
        x888.setVisible(true);
        Functionality x889 = new Functionality(MultiLanguageString.importFromString("pt10:Verbete TE"));
        x882.addChild(x889);
        x889.setTitle(MultiLanguageString.importFromString("pt31:Verbete Trabalho Extraordinário"));
        x889.setNormalizedName(MultiLanguageString.importFromString("pt10:verbete-te"));
        x889.setExecutionPath("/viewEmployeeAssiduousness.do?method=chooseEmployee&action=showExtraWorkSheet");
        ExpressionGroupAvailability x1619 = new ExpressionGroupAvailability(x882, "assiduousnessManagers");
        x1619.setTargetGroup(Group.fromString("assiduousnessManagers"));
        x882.setAvailabilityPolicy(x1619);
        Module x892 = new Module(MultiLanguageString.importFromString("pt5:Bónus"), "/");
        x851.addChild(x892);
        x892.setTitle(MultiLanguageString.importFromString("pt5:Bónus"));
        x892.setNormalizedName(MultiLanguageString.importFromString("pt5:bonus"));
        x892.setExecutionPath("");
        x892.setVisible(true);
        Functionality x891 = new Functionality(MultiLanguageString.importFromString("pt17:Prestações Anuais"));
        x892.addChild(x891);
        x891.setTitle(MultiLanguageString.importFromString("pt17:Prestações Anuais"));
        x891.setNormalizedName(MultiLanguageString.importFromString("pt17:prestacoes-anuais"));
        x891.setExecutionPath("/anualInstallments.do?method=showAnualInstallment");
        x891.setVisible(true);
        Functionality x893 = new Functionality(MultiLanguageString.importFromString("pt14:Importar Bónus"));
        x892.addChild(x893);
        x893.setTitle(MultiLanguageString.importFromString("pt14:Importar Bónus"));
        x893.setNormalizedName(MultiLanguageString.importFromString("pt14:importar-bonus"));
        x893.setExecutionPath("/uploadAnualInstallments.do?method=prepareUploadAnualInstallment");
        x893.setVisible(true);
        Functionality x894 = new Functionality(MultiLanguageString.importFromString("pt15:Consultar Bónus"));
        x892.addChild(x894);
        x894.setTitle(MultiLanguageString.importFromString("pt15:Consultar Bónus"));
        x894.setNormalizedName(MultiLanguageString.importFromString("pt15:consultar-bonus"));
        x894.setExecutionPath("/anualInstallments.do?method=showBonusInstallment");
        x894.setVisible(true);
        ExpressionGroupAvailability x1620 = new ExpressionGroupAvailability(x892, "payrollSectionStaff");
        x1620.setTargetGroup(Group.fromString("payrollSectionStaff"));
        x892.setAvailabilityPolicy(x1620);
        Functionality x896 = new Functionality(MultiLanguageString.importFromString("pt17:Secção de Pessoal"));
        x851.addChild(x896);
        x896.setTitle(MultiLanguageString.importFromString("pt17:Secção de Pessoal"));
        x896.setNormalizedName(MultiLanguageString.importFromString("pt17:seccao-de-pessoal"));
        x896.setExecutionPath("/index.do");
        x896.setVisible(true);
        Module x898 = new Module(MultiLanguageString.importFromString("pt6:Férias"), "/");
        x851.addChild(x898);
        x898.setTitle(MultiLanguageString.importFromString("pt6:Férias"));
        x898.setNormalizedName(MultiLanguageString.importFromString("pt6:ferias"));
        Functionality x897 = new Functionality(MultiLanguageString.importFromString("pt18:Calcular A17 e A18"));
        x898.addChild(x897);
        x897.setTitle(MultiLanguageString.importFromString("pt18:Calcular A17 e A18"));
        x897.setNormalizedName(MultiLanguageString.importFromString("pt18:calcular-a17-e-a18"));
        x897.setExecutionPath("/vacationsManagement.do?method=chooseYearMonth&action=calculateA17AndA18");
        ExpressionGroupAvailability x1621 =
                new ExpressionGroupAvailability(x898, "assiduousnessSectionStaff || assiduousnessManagers");
        x1621.setTargetGroup(Group.fromString("assiduousnessSectionStaff || assiduousnessManagers"));
        x898.setAvailabilityPolicy(x1621);
        Module x901 = new Module(MultiLanguageString.importFromString("pt11:Comunicação"), "/");
        x851.addChild(x901);
        x901.setNormalizedName(MultiLanguageString.importFromString("pt11:comunicacao"));
        Functionality x900 = new Functionality(MultiLanguageString.importFromString("en6:Groupspt6:Grupos"));
        x901.addChild(x900);
        x900.setTitle(MultiLanguageString.importFromString("en6:Groupspt6:Grupos"));
        x900.setNormalizedName(MultiLanguageString.importFromString("en6:groupspt6:grupos"));
        x900.setExecutionPath("/personnelUnitFunctionalities.do?method=configureGroups");
        Functionality x902 = new Functionality(MultiLanguageString.importFromString("pt9:Ficheiros"));
        x901.addChild(x902);
        x902.setTitle(MultiLanguageString.importFromString("pt9:Ficheiros"));
        x902.setNormalizedName(MultiLanguageString.importFromString("pt9:ficheiros"));
        x902.setExecutionPath("/personnelUnitFunctionalities.do?method=manageFiles");
        Module x905 = new Module(MultiLanguageString.importFromString("pt14:Interface GIAF"), "/");
        x851.addChild(x905);
        x905.setTitle(MultiLanguageString.importFromString("pt14:Interface GIAF"));
        x905.setNormalizedName(MultiLanguageString.importFromString("pt14:interface-giaf"));
        Functionality x904 = new Functionality(MultiLanguageString.importFromString("pt18:Obter Salário Hora"));
        x905.addChild(x904);
        x904.setTitle(MultiLanguageString.importFromString("pt18:Obter Salário Hora"));
        x904.setNormalizedName(MultiLanguageString.importFromString("pt18:obter-salario-hora"));
        x904.setExecutionPath("/giafInterface.do?method=showEmployeeHourValue");
        Functionality x906 = new Functionality(MultiLanguageString.importFromString("pt30:Inserir Dias de Férias por A17"));
        x905.addChild(x906);
        x906.setTitle(MultiLanguageString.importFromString("pt30:Inserir Dias de Férias por A17"));
        x906.setNormalizedName(MultiLanguageString.importFromString("pt30:inserir-dias-de-ferias-por-a17"));
        x906.setExecutionPath("/giafInterface.do?method=insertA17Vacations");
        Functionality x907 = new Functionality(MultiLanguageString.importFromString("pt23:Situações Profissionais"));
        x905.addChild(x907);
        x907.setTitle(MultiLanguageString.importFromString("pt23:Situações Profissionais"));
        x907.setNormalizedName(MultiLanguageString.importFromString("pt23:situacoes-profissionais"));
        x907.setExecutionPath("/giafParametrization.do?method=showContractSituations");
        ExpressionGroupAvailability x1622 = new ExpressionGroupAvailability(x907, "role(MANAGER)");
        x1622.setTargetGroup(Group.fromString("role(MANAGER)"));
        x907.setAvailabilityPolicy(x1622);
        Functionality x908 = new Functionality(MultiLanguageString.importFromString("pt24:Categorias Profissionais"));
        x905.addChild(x908);
        x908.setTitle(MultiLanguageString.importFromString("pt24:Categorias Profissionais"));
        x908.setNormalizedName(MultiLanguageString.importFromString("pt24:categorias-profissionais"));
        x908.setExecutionPath("/giafParametrization.do?method=showProfessionalCategories");
        ExpressionGroupAvailability x1623 = new ExpressionGroupAvailability(x908, "role(MANAGER)");
        x1623.setTargetGroup(Group.fromString("role(MANAGER)"));
        x908.setAvailabilityPolicy(x1623);
        Functionality x909 = new Functionality(MultiLanguageString.importFromString("pt23:Equiparações a Bolseiro"));
        x905.addChild(x909);
        x909.setTitle(MultiLanguageString.importFromString("pt23:Equiparações a Bolseiro"));
        x909.setNormalizedName(MultiLanguageString.importFromString("pt23:equiparacoes-a-bolseiro"));
        x909.setExecutionPath("/giafParametrization.do?method=showGrantOwnerEquivalences");
        ExpressionGroupAvailability x1624 = new ExpressionGroupAvailability(x909, "role(MANAGER)");
        x1624.setTargetGroup(Group.fromString("role(MANAGER)"));
        x909.setAvailabilityPolicy(x1624);
        Functionality x910 = new Functionality(MultiLanguageString.importFromString("pt20:Dispensas de Serviço"));
        x905.addChild(x910);
        x910.setTitle(MultiLanguageString.importFromString("pt20:Dispensas de Serviço"));
        x910.setNormalizedName(MultiLanguageString.importFromString("pt20:dispensas-de-servico"));
        x910.setExecutionPath("/giafParametrization.do?method=showServiceExemptions");
        ExpressionGroupAvailability x1625 = new ExpressionGroupAvailability(x910, "role(MANAGER)");
        x1625.setTargetGroup(Group.fromString("role(MANAGER)"));
        x910.setAvailabilityPolicy(x1625);
        ExpressionGroupAvailability x1626 =
                new ExpressionGroupAvailability(x905, "assiduousnessSectionStaff || assiduousnessManagers");
        x1626.setTargetGroup(Group.fromString("assiduousnessSectionStaff || assiduousnessManagers"));
        x905.setAvailabilityPolicy(x1626);
        ExpressionGroupAvailability x1627 = new ExpressionGroupAvailability(x851, "role(PERSONNEL_SECTION)");
        x1627.setTargetGroup(Group.fromString("role(PERSONNEL_SECTION)"));
        x851.setAvailabilityPolicy(x1627);
        Module x914 = new Module(MultiLanguageString.importFromString("pt7:Manager"), "/manager");
        x823.addChild(x914);
        x914.setNormalizedName(MultiLanguageString.importFromString("pt7:manager"));
        x914.setExecutionPath("/manager/index.do");
        x914.setVisible(true);
        Functionality x913 = new Functionality(MultiLanguageString.importFromString("en8:Homepagept16:Página Principal"));
        x914.addChild(x913);
        x913.setNormalizedName(MultiLanguageString.importFromString("en8:homepagept16:pagina-principal"));
        x913.setExecutionPath("/index.do");
        x913.setVisible(true);
        Module x916 = new Module(MultiLanguageString.importFromString("en21:Messages and Warningspt18:Mensagens e Avisos"), "/");
        x914.addChild(x916);
        x916.setNormalizedName(MultiLanguageString.importFromString("en21:messages-and-warningspt18:mensagens-e-avisos"));
        x916.setExecutionPath("");
        x916.setVisible(true);
        Functionality x915 = new Functionality(MultiLanguageString.importFromString("en12:Sending Mailpt16:Envio de E-mails"));
        x916.addChild(x915);
        x915.setNormalizedName(MultiLanguageString.importFromString("en12:sending-mailpt16:envio-de-e-mails"));
        x915.setExecutionPath("/sendEmail.do?method=prepare&amp;allowChangeSender=true");
        x915.setVisible(true);
        Functionality x917 =
                new Functionality(MultiLanguageString.importFromString("en18:Warning Managementpt16:Gestão de Avisos"));
        x916.addChild(x917);
        x917.setNormalizedName(MultiLanguageString.importFromString("en18:warning-managementpt16:gestao-de-avisos"));
        x917.setExecutionPath("/manageAdvisories.do?method=start");
        x917.setVisible(true);
        Functionality x918 =
                new Functionality(MultiLanguageString.importFromString("en17:Boards Managementpt16:Gestão de Boards"));
        x916.addChild(x918);
        x918.setNormalizedName(MultiLanguageString.importFromString("en17:boards-managementpt16:gestao-de-boards"));
        x918.setExecutionPath("/announcements/announcementBoardsManagement.do?method=start");
        x918.setVisible(true);
        Module x921 =
                new Module(
                        MultiLanguageString
                                .importFromString("en35:Organizational Structure Managementpt34:Gestão da Estrutura Organizacional"),
                        "/");
        x914.addChild(x921);
        x921.setNormalizedName(MultiLanguageString
                .importFromString("en35:organizational-strucutre-managementpt34:gestao-da-estrutura-organizacional"));
        x921.setExecutionPath("");
        x921.setVisible(true);
        Functionality x920 =
                new Functionality(
                        MultiLanguageString
                                .importFromString("en30:Units and Functions Managementpt27:Gestão de Unidades e Cargos"));
        x921.addChild(x920);
        x920.setNormalizedName(MultiLanguageString
                .importFromString("en30:units-and-functions-managementpt27:gestao-de-unidades-e-cargos"));
        x920.setExecutionPath("/organizationalStructureManagament/listAllUnits.faces");
        x920.setVisible(true);
        Functionality x922 =
                new Functionality(
                        MultiLanguageString.importFromString("en20:External Units Mergept26:Merge de Unidades Externas"));
        x921.addChild(x922);
        x922.setNormalizedName(MultiLanguageString.importFromString("en20:external-units-mergept26:merge-de-unidades-externas"));
        x922.setExecutionPath("/unitsMerge.do?method=chooseUnitToStart");
        x922.setVisible(true);
        Functionality x923 =
                new Functionality(
                        MultiLanguageString.importFromString("en20:Unit Site Managementpt26:Gestão de Sites de Unidade"));
        x921.addChild(x923);
        x923.setNormalizedName(MultiLanguageString.importFromString("en20:unit-site-managementpt26:gestao-de-sites-de-unidade"));
        x923.setExecutionPath("/unitSiteManagement.do?method=prepare");
        x923.setVisible(true);
        Module x925 =
                new Module(
                        MultiLanguageString
                                .importFromString("en30:Units and Functions Managementpt27:Gestão de Unidades e Cargos"),
                        "/organizationalStructureManagament");
        x921.addChild(x925);
        x925.setNormalizedName(MultiLanguageString
                .importFromString("en30:units-and-functions-managementpt27:gestao-de-unidades-e-cargos"));
        x925.setExecutionPath("/organizationalStructureManagament/listAllUnits.faces");
        x925.setVisible(false);
        Functionality x924 =
                new Functionality(MultiLanguageString.importFromString("en17:View Unit Detailspt23:Ver Detalhes de Unidade"));
        x925.addChild(x924);
        x924.setNormalizedName(MultiLanguageString.importFromString("en17:view-unit-detailspt23:ver-detalhes-de-unidade"));
        x924.setExecutionPath("/unitDetails.faces");
        x924.setVisible(true);
        Functionality x926 = new Functionality(MultiLanguageString.importFromString("en9:Edit Unitpt14:Editar Unidade"));
        x925.addChild(x926);
        x926.setNormalizedName(MultiLanguageString.importFromString("en9:edit-unitpt14:editar-unidade"));
        x926.setExecutionPath("/editUnit.faces");
        x926.setVisible(true);
        Functionality x927 =
                new Functionality(MultiLanguageString.importFromString("en18:Choose Parent Unitpt20:Escolher Unidade Pai"));
        x925.addChild(x927);
        x927.setNormalizedName(MultiLanguageString.importFromString("en18:choose-parent-unitpt20:escolher-unidade-pai"));
        x927.setExecutionPath("/listUnitParentUnits.faces");
        x927.setVisible(true);
        Functionality x928 =
                new Functionality(MultiLanguageString.importFromString("en19:Create New Functionpt16:Criar Novo Cargo"));
        x925.addChild(x928);
        x928.setNormalizedName(MultiLanguageString.importFromString("en19:create-new-functionpt16:criar-novo-cargo"));
        x928.setExecutionPath("/createNewFunction.faces");
        x928.setVisible(true);
        Functionality x929 = new Functionality(MultiLanguageString.importFromString("en13:Edit Functionpt12:Editar Cargo"));
        x925.addChild(x929);
        x929.setNormalizedName(MultiLanguageString.importFromString("en13:edit-functionpt12:editar-cargo"));
        x929.setExecutionPath("/editFunction.faces");
        x929.setVisible(true);
        Functionality x930 = new Functionality(MultiLanguageString.importFromString("en15:Choose Functionpt16:Escolha do Cargo"));
        x925.addChild(x930);
        x930.setNormalizedName(MultiLanguageString.importFromString("en15:choose-functionpt16:escolha-do-cargo"));
        x930.setExecutionPath("/chooseFunction.faces");
        x930.setVisible(true);
        Functionality x931 =
                new Functionality(MultiLanguageString.importFromString("en18:Choose Parent Unitpt20:Escolher Unidade Pai"));
        x925.addChild(x931);
        x931.setNormalizedName(MultiLanguageString.importFromString("en18:choose-parent-unitpt20:escolher-unidade-pai"));
        x931.setExecutionPath("/chooseParentUnit.faces");
        x931.setVisible(true);
        Functionality x932 =
                new Functionality(MultiLanguageString.importFromString("en18:Choose Parent Unitpt20:Escolher Unidade Pai"));
        x925.addChild(x932);
        x932.setNormalizedName(MultiLanguageString.importFromString("en18:choose-parent-unitpt20:escolher-unidade-pai"));
        x932.setExecutionPath("/listAllUnitsToChooseParentUnit.faces");
        x932.setVisible(true);
        Functionality x933 =
                new Functionality(MultiLanguageString.importFromString("en15:Create New Unitpt18:Criar Nova Unidade"));
        x925.addChild(x933);
        x933.setNormalizedName(MultiLanguageString.importFromString("en15:create-new-unitpt18:criar-nova-unidade"));
        x933.setExecutionPath("/createNewUnit.faces");
        x933.setVisible(true);
        Functionality x934 =
                new Functionality(MultiLanguageString.importFromString("en19:Create New Sub Unitpt21:Criar Nova SubUnidade"));
        x925.addChild(x934);
        x934.setNormalizedName(MultiLanguageString.importFromString("en19:create-new-sub-unitpt21:criar-nova-subunidade"));
        x934.setExecutionPath("/createNewSubUnit.faces");
        x934.setVisible(true);
        Module x938 =
                new Module(
                        MultiLanguageString
                                .importFromString("en32:Educational Structure Managementpt29:Gestão da Estrutura de Ensino"),
                        "/");
        x914.addChild(x938);
        x938.setNormalizedName(MultiLanguageString
                .importFromString("en32:educational-structure-managementpt29:gestao-da-estrutura-de-ensino"));
        x938.setExecutionPath("");
        x938.setVisible(true);
        Functionality x937 =
                new Functionality(
                        MultiLanguageString.importFromString("en20:Old Degree Structurept26:Estrutura de Cursos Antiga"));
        x938.addChild(x937);
        x937.setNormalizedName(MultiLanguageString.importFromString("en20:old-degree-structurept26:estrutura-de-cursos-antiga"));
        x937.setExecutionPath("/readDegrees.do");
        x937.setVisible(true);
        Functionality x939 =
                new Functionality(
                        MultiLanguageString.importFromString("en25:Before Bolonha Competencypt23:Competência Pré-Bolonha"));
        x938.addChild(x939);
        x939.setNormalizedName(MultiLanguageString.importFromString("en25:before-bolonha-competencypt23:competencia-pre-bolonha"));
        x939.setExecutionPath("/competenceCourseManagement.do?method=prepare");
        x939.setVisible(true);
        Functionality x940 =
                new Functionality(MultiLanguageString.importFromString("en16:Degree Structurept19:Estrutura de Cursos"));
        x938.addChild(x940);
        x940.setNormalizedName(MultiLanguageString.importFromString("en16:degree-structurept19:estrutura-de-cursos"));
        x940.setExecutionPath("/bolonha/curricularPlans/curricularPlansManagement.faces");
        x940.setVisible(true);
        Functionality x941 =
                new Functionality(MultiLanguageString.importFromString("en20:Bolonha Competenciespt20:Competências Bolonha"));
        x938.addChild(x941);
        x941.setNormalizedName(MultiLanguageString.importFromString("en20:bolonha-competenciespt20:competencias-bolonha"));
        x941.setExecutionPath("/bolonha/competenceCourses/competenceCoursesManagement.faces");
        x941.setVisible(true);
        Functionality x942 =
                new Functionality(
                        MultiLanguageString
                                .importFromString("en29:Department Degrees Managementpt29:Gerir Cursos de Departamentos"));
        x938.addChild(x942);
        x942.setNormalizedName(MultiLanguageString
                .importFromString("en29:department-degrees-managementpt29:gerir-cursos-de-departamentos"));
        x942.setExecutionPath("/manageDepartmentDegrees.do?method=prepare");
        x942.setVisible(true);
        Module x944 =
                new Module(MultiLanguageString.importFromString("en20:Old Degree Structurept26:Estrutura de Cursos Antiga"), "/");
        x938.addChild(x944);
        x944.setNormalizedName(MultiLanguageString.importFromString("en20:old-degree-structurept26:estrutura-de-cursos-antiga"));
        x944.setExecutionPath("/readDegrees.do");
        x944.setVisible(false);
        Functionality x943 = new Functionality(MultiLanguageString.importFromString("en13:Insert Degreept13:Inserir curso"));
        x944.addChild(x943);
        x943.setNormalizedName(MultiLanguageString.importFromString("en13:insert-degreept13:inserir-curso"));
        x943.setExecutionPath("/insertDegree.do?method=prepareInsert");
        x943.setVisible(true);
        Functionality x945 = new Functionality(MultiLanguageString.importFromString("en14:Delete Degreespt13:Apagar Cursos"));
        x944.addChild(x945);
        x945.setNormalizedName(MultiLanguageString.importFromString("en14:delete-degreespt13:apagar-cursos"));
        x945.setExecutionPath("/deleteDegrees.do");
        x945.setVisible(true);
        Module x947 = new Module(MultiLanguageString.importFromString("en21:Degree Administrationpt17:Administrar Curso"), "/");
        x944.addChild(x947);
        x947.setNormalizedName(MultiLanguageString.importFromString("en21:degree-administrationpt17:administrar-curso"));
        x947.setExecutionPath("/readDegree.do");
        x947.setVisible(true);
        Functionality x946 = new Functionality(MultiLanguageString.importFromString("en11:Edit Degreept12:Editar Curso"));
        x947.addChild(x946);
        x946.setNormalizedName(MultiLanguageString.importFromString("en11:edit-degreept12:editar-curso"));
        x946.setExecutionPath("/editDegree.do?method=prepareEdit");
        x946.setVisible(true);
        Functionality x948 =
                new Functionality(
                        MultiLanguageString.importFromString("en22:Insert curricular planpt24:Inserir plano curricular"));
        x947.addChild(x948);
        x948.setNormalizedName(MultiLanguageString.importFromString("en22:insert-curricular-planpt24:inserir-plano-curricular"));
        x948.setExecutionPath("/insertDegreeCurricularPlan.do?method=prepareInsert");
        x948.setVisible(true);
        Functionality x949 =
                new Functionality(
                        MultiLanguageString.importFromString("en23:Delete Curricular Planspt26:Apagar Planos Curriculares"));
        x947.addChild(x949);
        x949.setNormalizedName(MultiLanguageString
                .importFromString("en23:delete-curricular-planspt26:apagar-planos-curriculares"));
        x949.setExecutionPath("/deleteDegreeCurricularPlans.do");
        x949.setVisible(true);
        Module x951 =
                new Module(
                        MultiLanguageString
                                .importFromString("en30:Curricular Plan Administrationpt28:Administrar Plano Curricular"),
                        "/");
        x947.addChild(x951);
        x951.setNormalizedName(MultiLanguageString
                .importFromString("en30:curricular-plan-administrationpt28:administrar-plano-curricular"));
        x951.setExecutionPath("/readDegreeCurricularPlan.do");
        x951.setVisible(true);
        Functionality x950 =
                new Functionality(MultiLanguageString.importFromString("en20:Edit Curricular Planpt23:Editar plano curricular"));
        x951.addChild(x950);
        x950.setNormalizedName(MultiLanguageString.importFromString("en20:edit-curricular-planpt23:editar-plano-curricular"));
        x950.setExecutionPath("/editDegreeCurricularPlan.do?method=prepareEdit");
        x950.setVisible(true);
        Functionality x952 =
                new Functionality(
                        MultiLanguageString.importFromString("en24:Insert curricular coursept29:Inserir disciplina curricular"));
        x951.addChild(x952);
        x952.setNormalizedName(MultiLanguageString
                .importFromString("en24:insert-curricular-coursept29:inserir-disciplina-curricular"));
        x952.setExecutionPath("/insertCurricularCourse.do?method=prepareInsert");
        x952.setVisible(true);
        Functionality x953 =
                new Functionality(MultiLanguageString.importFromString("en17:Branch Managementpt15:Gestão de Ramos"));
        x951.addChild(x953);
        x953.setNormalizedName(MultiLanguageString.importFromString("en17:branch-managementpt15:gestao-de-ramos"));
        x953.setExecutionPath("/manageBranches.do?method=showBranches");
        x953.setVisible(true);
        Functionality x954 =
                new Functionality(
                        MultiLanguageString
                                .importFromString("en34:Curricular Course Group Managementpt44:Gestão de Grupos de Disciplinas Curriculares"));
        x951.addChild(x954);
        x954.setNormalizedName(MultiLanguageString
                .importFromString("en34:curricular-course-group-managementpt44:gestao-de-grupos-de-disciplinas-curriculares"));
        x954.setExecutionPath("/manageCurricularCourseGroups.do?method=viewCurricularCourseGroups");
        x954.setVisible(true);
        Module x956 =
                new Module(MultiLanguageString.importFromString("en21:Precedence Managementpt22:Gestão de Precedências"), "/");
        x951.addChild(x956);
        x956.setNormalizedName(MultiLanguageString.importFromString("en21:precedence-managementpt22:gestao-de-precedencias"));
        x956.setExecutionPath("/managePrecedences.do?method=showMenu");
        x956.setVisible(true);
        Functionality x955 =
                new Functionality(
                        MultiLanguageString.importFromString("en24:Create Simple Precedencept25:Criar Precedência Simples"));
        x956.addChild(x955);
        x955.setNormalizedName(MultiLanguageString
                .importFromString("en24:create-simple-precedencept25:criar-precedencia-simples"));
        x955.setExecutionPath("makeSimplePrecedence.do?method=showAllRestrictions&page=0");
        x955.setVisible(true);
        Functionality x957 =
                new Functionality(
                        MultiLanguageString
                                .importFromString("en29:Create Precedence Conjunctionpt31:Criar Conjunção de Precedências"));
        x956.addChild(x957);
        x957.setNormalizedName(MultiLanguageString
                .importFromString("en29:create-precedence-conjunctionpt31:criar-conjuncao-de-precedencias"));
        x957.setExecutionPath("/makePrecedenceConjunction.do?method=showFirstPage");
        x957.setVisible(true);
        Functionality x958 =
                new Functionality(MultiLanguageString.importFromString("en17:Delete Precedencept18:Apagar Precedência"));
        x956.addChild(x958);
        x958.setNormalizedName(MultiLanguageString.importFromString("en17:delete-precedencept18:apagar-precedencia"));
        x958.setExecutionPath("/deletePrecedence.do");
        x958.setVisible(false);
        Module x961 =
                new Module(
                        MultiLanguageString
                                .importFromString("en32:Curricular Course Administrationpt33:Administrar Disciplina Curricular"),
                        "/");
        x951.addChild(x961);
        x961.setNormalizedName(MultiLanguageString
                .importFromString("en32:curricular-course-administrationpt33:administrar-disciplina-curricular"));
        x961.setExecutionPath("/readCurricularCourse.do");
        x961.setVisible(true);
        Functionality x960 =
                new Functionality(
                        MultiLanguageString.importFromString("en22:Edit Curricular Coursept28:Editar Disciplina Curricular"));
        x961.addChild(x960);
        x960.setNormalizedName(MultiLanguageString
                .importFromString("en22:edit-curricular-coursept28:editar-disciplina-curricular"));
        x960.setExecutionPath("/editCurricularCourse.do?method=prepareEdit");
        x960.setVisible(true);
        Functionality x962 = new Functionality(MultiLanguageString.importFromString("en12:Insert Scopept14:Inserir âmbito"));
        x961.addChild(x962);
        x962.setNormalizedName(MultiLanguageString.importFromString("en12:insert-scopept14:inserir-ambito"));
        x962.setExecutionPath("/insertCurricularCourseScope.do?method=prepareInsert");
        x962.setVisible(true);
        Functionality x963 =
                new Functionality(
                        MultiLanguageString
                                .importFromString("en27:Edit Curricular Informationpt31:Edição de Informação Curricular"));
        x961.addChild(x963);
        x963.setNormalizedName(MultiLanguageString
                .importFromString("en27:edit-curricular-informationpt31:edicao-de-informacao-curricular"));
        x963.setExecutionPath("/editCurriculum.do?method=prepareEdit");
        x963.setVisible(true);
        Functionality x964 = new Functionality(MultiLanguageString.importFromString("en11:Create Sitept11:Criar Sitio"));
        x961.addChild(x964);
        x964.setNormalizedName(MultiLanguageString.importFromString("en11:create-sitept11:criar-sitio"));
        x964.setExecutionPath("/createSite.do");
        x964.setVisible(true);
        Functionality x965 = new Functionality(MultiLanguageString.importFromString("pt13:Editar âmbito"));
        x961.addChild(x965);
        x965.setNormalizedName(MultiLanguageString.importFromString("pt13:editar-ambito"));
        x965.setExecutionPath("/editCurricularCourseScope.do?method=prepareEdit");
        x965.setVisible(true);
        Functionality x966 = new Functionality(MultiLanguageString.importFromString("en12:Insert Scopept14:Inserir âmbito"));
        x961.addChild(x966);
        x966.setNormalizedName(MultiLanguageString.importFromString("en12:insert-scopept14:inserir-ambito"));
        x966.setExecutionPath("/insertCurricularCourseScopeFromAnother.do?method=prepareInsert");
        x966.setVisible(true);
        Functionality x967 = new Functionality(MultiLanguageString.importFromString("en9:End Scopept15:Terminar âmbito"));
        x961.addChild(x967);
        x967.setNormalizedName(MultiLanguageString.importFromString("en9:end-scopept15:terminar-ambito"));
        x967.setExecutionPath("/endCurricularCourseScope.do?method=prepareEnd");
        x967.setVisible(true);
        Functionality x968 = new Functionality(MultiLanguageString.importFromString("en12:Delete Scopept13:Apagar âmbito"));
        x961.addChild(x968);
        x968.setNormalizedName(MultiLanguageString.importFromString("en12:delete-scopept13:apagar-ambito"));
        x968.setExecutionPath("/deleteCurricularCourseScope.do");
        x968.setVisible(true);
        Functionality x969 =
                new Functionality(MultiLanguageString.importFromString("en15:View All Scopespt27:Visualizar todos os Âmbitos"));
        x961.addChild(x969);
        x969.setNormalizedName(MultiLanguageString.importFromString("en15:view-all-scopespt27:visualizar-todos-os-ambitos"));
        x969.setExecutionPath("/readAllCurricularCourseScopes.do");
        x969.setVisible(true);
        Module x971 =
                new Module(
                        MultiLanguageString.importFromString("en26:Associate Execution Coursept28:Associar disciplina execução"),
                        "/");
        x961.addChild(x971);
        x971.setNormalizedName(MultiLanguageString
                .importFromString("en26:associate-execution-coursept28:associar-disciplina-execucao"));
        x971.setExecutionPath("");
        x971.setVisible(true);
        Functionality x970 =
                new Functionality(
                        MultiLanguageString.importFromString("en23:Choose Execution Periodpt28:Escolher Periodo de Execução"));
        x971.addChild(x970);
        x970.setNormalizedName(MultiLanguageString
                .importFromString("en23:choose-execution-periodpt28:escolher-periodo-de-execucao"));
        x970.setExecutionPath("/readExecutionPeriodToAssociateExecutionCoursesAction.do");
        x970.setVisible(true);
        Functionality x972 =
                new Functionality(
                        MultiLanguageString.importFromString("en23:Choose Execution Coursept28:Escolher Disciplina Execução"));
        x971.addChild(x972);
        x972.setNormalizedName(MultiLanguageString
                .importFromString("en23:choose-execution-coursept28:escolher-disciplina-execucao"));
        x972.setExecutionPath("/associateExecutionCourseToCurricularCourse.do?method=prepare");
        x972.setVisible(true);
        Module x975 = new Module(MultiLanguageString.importFromString("en12:Edit Facultypt20:Editar Corpo Docente"), "/");
        x961.addChild(x975);
        x975.setNormalizedName(MultiLanguageString.importFromString("en12:edit-facultypt20:editar-corpo-docente"));
        x975.setExecutionPath("/readTeacherInCharge.do");
        x975.setVisible(true);
        Functionality x974 =
                new Functionality(MultiLanguageString.importFromString("en19:Associate Professorpt16:Associar Docente"));
        x975.addChild(x974);
        x974.setNormalizedName(MultiLanguageString.importFromString("en19:associate-professorpt16:associar-docente"));
        x974.setExecutionPath("/insertProfessorShipByNumber.do?method=prepareInsert");
        x974.setVisible(true);
        Functionality x976 =
                new Functionality(
                        MultiLanguageString
                                .importFromString("en34:Associate non affiliated professorpt30:Associar docente não vinculado"));
        x975.addChild(x976);
        x976.setNormalizedName(MultiLanguageString
                .importFromString("en34:Associate non affiliated professorpt30:associar-docente-nao-vinculado"));
        x976.setExecutionPath("/insertProfessorShipNonAffiliatedTeacher.do?method=prepare");
        x976.setVisible(true);
        Functionality x977 = new Functionality(MultiLanguageString.importFromString("en12:Save Changespt18:Guardar Alterações"));
        x975.addChild(x977);
        x977.setNormalizedName(MultiLanguageString.importFromString("en12:save-changespt18:guardar-alteracoes"));
        x977.setExecutionPath("/saveTeachersBody.do");
        x977.setVisible(true);
        Module x984 =
                new Module(MultiLanguageString.importFromString("en25:Before Bolonha Competencypt23:Competência Pré-Bolonha"),
                        "/");
        x938.addChild(x984);
        x984.setNormalizedName(MultiLanguageString.importFromString("en25:before-bolonha-competencypt23:competencia-pre-bolonha"));
        x984.setExecutionPath("/competenceCourseManagement.do?method=prepare");
        x984.setVisible(false);
        Functionality x983 =
                new Functionality(
                        MultiLanguageString
                                .importFromString("en29:Insert-Edit Competence Coursept37:Inserir-Editar Disciplina Competencia"));
        x984.addChild(x983);
        x983.setNormalizedName(MultiLanguageString
                .importFromString("en29:insert-edit-competence-coursept37:inserir-editar-disciplina-competencia"));
        x983.setExecutionPath("/createEditCompetenceCourse.do?method=prepareCreate");
        x983.setVisible(true);
        Functionality x985 =
                new Functionality(
                        MultiLanguageString
                                .importFromString("en28:Create New Competence Coursept33:Criar Nova Disciplina Competência"));
        x984.addChild(x985);
        x985.setNormalizedName(MultiLanguageString
                .importFromString("en28:create-new-competence-coursept33:criar-nova-disciplina-competencia"));
        x985.setExecutionPath("/curricularCoursesCompetenceCourse.do?method=readDegrees");
        x985.setVisible(true);
        Module x988 =
                new Module(MultiLanguageString.importFromString("en20:Bolonha Competenciespt20:Competências Bolonha"),
                        "/bolonha/competenceCourses/");
        x938.addChild(x988);
        x988.setNormalizedName(MultiLanguageString.importFromString("en20:bolonha-competenciespt20:competencias-bolonha"));
        x988.setExecutionPath("/bolonha/competenceCourses/competenceCoursesManagement.faces");
        x988.setVisible(false);
        Functionality x987 =
                new Functionality(
                        MultiLanguageString.importFromString("en22:View Competence Coursept26:Ver Disciplina Competência"));
        x988.addChild(x987);
        x987.setNormalizedName(MultiLanguageString.importFromString("en22:view-competence-coursept26:ver-disciplina-competencia"));
        x987.setExecutionPath("/showCompetenceCourse.faces?action=ccm");
        x987.setVisible(true);
        Functionality x989 =
                new Functionality(
                        MultiLanguageString.importFromString("en22:Edit Competence Coursept29:Editar Disciplina Competência"));
        x988.addChild(x989);
        x989.setNormalizedName(MultiLanguageString
                .importFromString("en22:edit-competence-coursept29:editar-disciplina-competencia"));
        x989.setExecutionPath("/editCompetenceCourseMainPage.faces");
        x989.setVisible(true);
        Functionality x990 =
                new Functionality(
                        MultiLanguageString.importFromString("en22:Edit Competence Coursept29:Editar Disciplina Competência"));
        x988.addChild(x990);
        x990.setNormalizedName(MultiLanguageString
                .importFromString("en22:edit-competence-coursept29:editar-disciplina-competencia"));
        x990.setExecutionPath("/editCompetenceCourse.faces?&action=viewccm");
        x990.setVisible(true);
        Module x994 = new Module(MultiLanguageString.importFromString("en20:Execution Managementpt19:Gestão de Execuções"), "/");
        x914.addChild(x994);
        x994.setNormalizedName(MultiLanguageString.importFromString("en20:execution-managementpt19:gestao-de-execucoes"));
        x994.setExecutionPath("");
        x994.setVisible(true);
        Functionality x993 =
                new Functionality(MultiLanguageString.importFromString("en17:Execution Periodspt17:Periodos Execução"));
        x994.addChild(x993);
        x993.setNormalizedName(MultiLanguageString.importFromString("en17:execution-periodspt17:periodos-execucao"));
        x993.setExecutionPath("/manageExecutionPeriods.do?method=prepare");
        x993.setVisible(true);
        Functionality x995 =
                new Functionality(MultiLanguageString.importFromString("en20:Execution Curriculumpt19:Currículos Execução"));
        x994.addChild(x995);
        x995.setNormalizedName(MultiLanguageString.importFromString("en20:execution-curriculumpt19:curriculos-execucao"));
        x995.setExecutionPath("/executionDegreesManagementMainPage.do");
        x995.setVisible(true);
        Functionality x996 =
                new Functionality(
                        MultiLanguageString
                                .importFromString("en25:Execution Course Homepagept35:Página Inicial Disciplinas Execução"));
        x994.addChild(x996);
        x996.setNormalizedName(MultiLanguageString
                .importFromString("en25:execution-course-homepagept35:pagina-inicial-disciplinas-execucao"));
        x996.setExecutionPath("/executionCourseManagement.do?method=firstPage");
        x996.setVisible(true);
        Functionality x997 =
                new Functionality(MultiLanguageString.importFromString("en17:Enrolment Periodspt22:Periodos de Inscrições"));
        x994.addChild(x997);
        x997.setNormalizedName(MultiLanguageString.importFromString("en17:enrolment-periodspt22:periodos-de-inscricoes"));
        x997.setExecutionPath("/manageEnrolementPeriods.do?method=prepare");
        x997.setVisible(true);
        Module x999 = new Module(MultiLanguageString.importFromString("en20:Execution Curriculumpt19:Currículos Execução"), "/");
        x994.addChild(x999);
        x999.setNormalizedName(MultiLanguageString.importFromString("en20:execution-curriculumpt19:curriculos-execucao"));
        x999.setExecutionPath("/executionDegreesManagementMainPage.do");
        x999.setVisible(false);
        Functionality x998 = new Functionality(MultiLanguageString.importFromString("en8:Homepagept16:Página Principal"));
        x999.addChild(x998);
        x998.setNormalizedName(MultiLanguageString.importFromString("en8:homepagept16:pagina-principal"));
        x998.setExecutionPath("/index.do");
        x998.setVisible(true);
        Module x1001 =
                new Module(
                        MultiLanguageString
                                .importFromString("en31:Execution Curriculum Managementpt29:Gestão de Currículos Execução"),
                        "/");
        x999.addChild(x1001);
        x1001.setNormalizedName(MultiLanguageString
                .importFromString("en31:execution-curriculum-managementpt29:gestao-de-curriculos-execucao"));
        x1001.setExecutionPath("");
        x1001.setVisible(true);
        Functionality x1000 =
                new Functionality(
                        MultiLanguageString.importFromString("en27:Create Execution Curriculumpt25:Criar Currículos Execução"));
        x1001.addChild(x1000);
        x1000.setNormalizedName(MultiLanguageString
                .importFromString("en27:create-execution-curriculumpt25:criar-curriculos-execucao"));
        x1000.setExecutionPath("/degree/chooseDegreeType.faces");
        x1000.setVisible(true);
        Functionality x1002 =
                new Functionality(
                        MultiLanguageString.importFromString("en25:Edit Execution Curriculumpt26:Editar Currículos Execução"));
        x1001.addChild(x1002);
        x1002.setNormalizedName(MultiLanguageString
                .importFromString("en25:edit-execution-curriculumpt26:editar-curriculos-execucao"));
        x1002.setExecutionPath("/executionDegreesManagement.do?method=readDegreeCurricularPlans");
        x1002.setVisible(true);
        Functionality x1003 =
                new Functionality(
                        MultiLanguageString.importFromString("en23:Choose Curricular Planspt28:Escolher Planos Curriculares"));
        x1001.addChild(x1003);
        x1003.setNormalizedName(MultiLanguageString
                .importFromString("en23:choose-curricular-planspt28:escolher-planos-curriculares"));
        x1003.setExecutionPath("/degree/chooseDegreeCurricularPlans.faces");
        x1003.setVisible(false);
        Functionality x1004 =
                new Functionality(
                        MultiLanguageString.importFromString("en28:Execution Curriculum Createdpt25:Currículo Execução Criado"));
        x1001.addChild(x1004);
        x1004.setNormalizedName(MultiLanguageString
                .importFromString("en28:execution-curriculum-createdpt25:curriculo-execucao-criado"));
        x1004.setExecutionPath("/degree/insertExecutionDegreesSuccess.faces");
        x1004.setVisible(false);
        Module x1008 = new Module(MultiLanguageString.importFromString("en17:Execution Coursespt20:Disciplinas Execução"), "/");
        x994.addChild(x1008);
        x1008.setNormalizedName(MultiLanguageString.importFromString("en17:execution-coursespt20:disciplinas-execucao"));
        x1008.setExecutionPath("/executionCourseManagement.do?method=firstPage");
        x1008.setVisible(false);
        Functionality x1007 = new Functionality(MultiLanguageString.importFromString("en8:Homepagept14:Página Inicial"));
        x1008.addChild(x1007);
        x1007.setNormalizedName(MultiLanguageString.importFromString("en8:homepagept14:pagina-inicial"));
        x1007.setExecutionPath("/index.do");
        x1007.setVisible(true);
        Module x1010 = new Module(MultiLanguageString.importFromString("en17:Execution Coursespt20:Disciplinas Execução"), "/");
        x1008.addChild(x1010);
        x1010.setNormalizedName(MultiLanguageString.importFromString("en17:execution-coursespt20:disciplinas-execucao"));
        x1010.setExecutionPath("");
        x1010.setVisible(true);
        Functionality x1009 =
                new Functionality(
                        MultiLanguageString.importFromString("en23:Insert Execution Coursept27:Inserir Disciplina Execução"));
        x1010.addChild(x1009);
        x1009.setNormalizedName(MultiLanguageString
                .importFromString("en23:insert-execution-coursept27:inserir-disciplina-execucao"));
        x1009.setExecutionPath("/insertExecutionCourse.do?method=prepareInsertExecutionCourse");
        x1009.setVisible(true);
        Functionality x1011 =
                new Functionality(
                        MultiLanguageString.importFromString("en21:Edit Execution Coursept26:Editar Disciplina Execução"));
        x1010.addChild(x1011);
        x1011.setNormalizedName(MultiLanguageString.importFromString("en21:edit-execution-coursept26:editar-disciplina-execucao"));
        x1011.setExecutionPath("/editExecutionCourseChooseExPeriod.do?method=prepareEditExecutionCourse");
        x1011.setVisible(true);
        Functionality x1012 =
                new Functionality(
                        MultiLanguageString.importFromString("en23:Merge Execution Coursespt27:Juntar Disciplinas Execução"));
        x1010.addChild(x1012);
        x1012.setNormalizedName(MultiLanguageString
                .importFromString("en23:merge-execution-coursespt27:juntar-disciplinas-execucao"));
        x1012.setExecutionPath("/chooseDegreesForExecutionCourseMerge.do?method=prepareChooseDegreesAndExecutionPeriod");
        x1012.setVisible(true);
        Functionality x1013 =
                new Functionality(
                        MultiLanguageString.importFromString("en23:Create Teaching Reportspt28:Criar Relatórios de Docência"));
        x1010.addChild(x1013);
        x1013.setNormalizedName(MultiLanguageString
                .importFromString("en23:create-teaching-reportspt28:criar-relatorios-de-docencia"));
        x1013.setExecutionPath("/executionCourseManagement/createCourseReportsForExecutionPeriod.faces");
        x1013.setVisible(true);
        Functionality x1014 =
                new Functionality(
                        MultiLanguageString.importFromString("en24:Create Execution Coursespt29:Criar Disciplinas de Execução"));
        x1010.addChild(x1014);
        x1014.setNormalizedName(MultiLanguageString
                .importFromString("en24:create-execution-coursespt29:criar-disciplinas-de-execucao"));
        x1014.setExecutionPath("/createExecutionCourses.do?method=chooseDegreeType");
        x1014.setVisible(true);
        Functionality x1015 =
                new Functionality(
                        MultiLanguageString.importFromString("en23:Edit Execution Course 1pt28:Editar Disciplina Execução 1"));
        x1010.addChild(x1015);
        x1015.setNormalizedName(MultiLanguageString
                .importFromString("en23:edit-execution-course-1pt28:editar-disciplina-execucao-1"));
        x1015.setExecutionPath("/editExecutionCourseChooseExDegree.do");
        x1015.setVisible(false);
        Functionality x1016 =
                new Functionality(
                        MultiLanguageString.importFromString("en23:Edit Execution Course 2pt28:Editar Disciplina Execução 2"));
        x1010.addChild(x1016);
        x1016.setNormalizedName(MultiLanguageString
                .importFromString("en23:edit-execution-course-2pt28:editar-disciplina-execucao-2"));
        x1016.setExecutionPath("/editExecutionCourse.do?method=editExecutionCourse");
        x1016.setVisible(false);
        Functionality x1017 =
                new Functionality(
                        MultiLanguageString
                                .importFromString("en27:Associate Curricular Coursept30:Associar Disciplina Curricular"));
        x1010.addChild(x1017);
        x1017.setNormalizedName(MultiLanguageString
                .importFromString("en27:associate-curricular-coursept30:associar-disciplina-curricular"));
        x1017.setExecutionPath("editExecutionCourseManageCurricularCourses.do?method=prepareAssociateCurricularCourseChooseDegreeCurricularPlan");
        x1017.setVisible(false);
        Functionality x1018 =
                new Functionality(
                        MultiLanguageString.importFromString("en25:Separate Execution Coursept27:Separar Disciplina Execução"));
        x1010.addChild(x1018);
        x1018.setNormalizedName(MultiLanguageString
                .importFromString("en25:separate-execution-coursept27:separar-disciplina-execucao"));
        x1018.setExecutionPath("/seperateExecutionCourse.do?method=prepareTransfer");
        x1018.setVisible(false);
        Functionality x1019 =
                new Functionality(
                        MultiLanguageString.importFromString("en25:Merge Execution Courses 2pt29:Juntar Disciplinas Execução 2"));
        x1010.addChild(x1019);
        x1019.setNormalizedName(MultiLanguageString
                .importFromString("en25:merge-execution-courses-2pt29:juntar-disciplinas-execucao-2"));
        x1019.setExecutionPath("/mergeExecutionCoursesForm.do");
        x1019.setVisible(false);
        Module x1024 = new Module(MultiLanguageString.importFromString("en19:Personal Managementpt17:Gestão de Pessoal"), "/");
        x914.addChild(x1024);
        x1024.setNormalizedName(MultiLanguageString.importFromString("en19:personal-managementpt17:gestao-de-pessoal"));
        x1024.setExecutionPath("");
        x1024.setVisible(true);
        Functionality x1023 =
                new Functionality(MultiLanguageString.importFromString("en18:Teacher Managementpt18:Gestão de Docentes"));
        x1024.addChild(x1023);
        x1023.setNormalizedName(MultiLanguageString.importFromString("en18:teacher-managementpt18:gestao-de-docentes"));
        x1023.setExecutionPath("/teachersManagement.do?method=firstPage");
        x1023.setVisible(true);
        Functionality x1025 =
                new Functionality(MultiLanguageString.importFromString("en18:Student Managementpt16:Gestão de Alunos"));
        x1024.addChild(x1025);
        x1025.setNormalizedName(MultiLanguageString.importFromString("en18:student-managementpt16:gestao-de-alunos"));
        x1025.setExecutionPath("/studentsManagement.do?method=show");
        x1025.setVisible(true);
        Functionality x1026 =
                new Functionality(MultiLanguageString.importFromString("en17:People Managementpt17:Gestão de Pessoas"));
        x1024.addChild(x1026);
        x1026.setNormalizedName(MultiLanguageString.importFromString("en17:people-managementpt17:gestao-de-pessoas"));
        x1026.setExecutionPath("/personManagement.do?method=firstPage");
        x1026.setVisible(true);
        Functionality x1027 =
                new Functionality(MultiLanguageString.importFromString("en18:Holiday Managementpt18:Gestão de Feriados"));
        x1024.addChild(x1027);
        x1027.setNormalizedName(MultiLanguageString.importFromString("en18:holiday-managementpt18:gestao-de-feriados"));
        x1027.setExecutionPath("/manageHolidays.do?method=prepare&page=0");
        x1027.setVisible(true);
        Module x1029 = new Module(MultiLanguageString.importFromString("en18:Teacher Managementpt18:Gestão de Docentes"), "/");
        x1024.addChild(x1029);
        x1029.setNormalizedName(MultiLanguageString.importFromString("en18:teacher-managementpt18:gestao-de-docentes"));
        x1029.setExecutionPath("/teachersManagement.do?method=firstPage");
        x1029.setVisible(false);
        Functionality x1028 = new Functionality(MultiLanguageString.importFromString("en8:Homepagept14:Página Inicial"));
        x1029.addChild(x1028);
        x1028.setNormalizedName(MultiLanguageString.importFromString("en8:homepagept14:pagina-inicial"));
        x1028.setExecutionPath("/index.do");
        x1028.setVisible(true);
        Module x1031 = new Module(MultiLanguageString.importFromString("en18:Teacher Managementpt18:Gestão de Docentes"), "/");
        x1029.addChild(x1031);
        x1031.setNormalizedName(MultiLanguageString.importFromString("en18:teacher-managementpt18:gestao-de-docentes"));
        x1031.setExecutionPath("");
        x1031.setVisible(true);
        Functionality x1030 =
                new Functionality(MultiLanguageString.importFromString("en15:Edit Categoriespt17:Editar Categorias"));
        x1031.addChild(x1030);
        x1030.setNormalizedName(MultiLanguageString.importFromString("en15:edit-categoriespt17:editar-categorias"));
        x1030.setExecutionPath("/teacherCategoriesManagement.do?method=prepareEdit");
        x1030.setVisible(true);
        Functionality x1032 =
                new Functionality(
                        MultiLanguageString
                                .importFromString("en30:Disassociate Execution Coursespt32:Desassociar Disciplinas Execução"));
        x1031.addChild(x1032);
        x1032.setNormalizedName(MultiLanguageString
                .importFromString("en30:disassociate-execution-coursespt32:desassociar-disciplinas-execucao"));
        x1032.setExecutionPath("/dissociateProfShipsAndRespFor.do?method=prepareDissociateEC");
        x1032.setVisible(true);
        Module x1036 = new Module(MultiLanguageString.importFromString("en18:Student Managementpt16:Gestão de Alunos"), "/");
        x1024.addChild(x1036);
        x1036.setNormalizedName(MultiLanguageString.importFromString("en18:student-managementpt16:gestao-de-alunos"));
        x1036.setExecutionPath("/studentsManagement.do?method=show");
        x1036.setVisible(false);
        Functionality x1035 =
                new Functionality(MultiLanguageString.importFromString("en22:Create Classificationspt20:Criar Classificações"));
        x1036.addChild(x1035);
        x1035.setNormalizedName(MultiLanguageString.importFromString("en22:create-classificationspt20:criar-classificacoes"));
        x1035.setExecutionPath("/createClassificationsForStudents.do?method=prepare");
        x1035.setVisible(true);
        Module x1039 = new Module(MultiLanguageString.importFromString("en17:People Managementpt17:Gestão de Pessoas"), "/");
        x1024.addChild(x1039);
        x1039.setNormalizedName(MultiLanguageString.importFromString("en17:people-managementpt17:gestao-de-pessoas"));
        x1039.setExecutionPath("/personManagement.do?method=firstPage");
        x1039.setVisible(false);
        Functionality x1038 = new Functionality(MultiLanguageString.importFromString("en8:Homepagept14:Página Inicial"));
        x1039.addChild(x1038);
        x1038.setNormalizedName(MultiLanguageString.importFromString("en8:homepagept14:pagina-inicial"));
        x1038.setExecutionPath("/index.do");
        x1038.setVisible(true);
        Module x1041 = new Module(MultiLanguageString.importFromString("en17:People Managementpt17:Gestão de Pessoas"), "/");
        x1039.addChild(x1041);
        x1041.setNormalizedName(MultiLanguageString.importFromString("en17:people-managementpt17:gestao-de-pessoas"));
        x1041.setExecutionPath("");
        x1041.setVisible(true);
        Functionality x1040 = new Functionality(MultiLanguageString.importFromString("en13:Create Peoplept13:Criar Pessoas"));
        x1041.addChild(x1040);
        x1040.setNormalizedName(MultiLanguageString.importFromString("en13:create-peoplept13:criar-pessoas"));
        x1040.setExecutionPath("/personManagement/createPerson.do?method=prepare");
        x1040.setVisible(false);
        Functionality x1042 = new Functionality(MultiLanguageString.importFromString("en13:Search Personpt15:Procurar Pessoa"));
        x1041.addChild(x1042);
        x1042.setNormalizedName(MultiLanguageString.importFromString("en13:search-personpt15:procurar-pessoa"));
        x1042.setExecutionPath("/findPerson.do?method=prepareFindPerson");
        x1042.setVisible(true);
        Functionality x1043 =
                new Functionality(MultiLanguageString.importFromString("en15:Role Managementpt21:Gestão de Privilégios"));
        x1041.addChild(x1043);
        x1043.setNormalizedName(MultiLanguageString.importFromString("en15:role-managementpt21:gestao-de-privilegios"));
        x1043.setExecutionPath("/manageRoles.do?method=prepare");
        x1043.setVisible(true);
        Functionality x1044 =
                new Functionality(MultiLanguageString.importFromString("en17:Generate Passwordpt14:Gerar Password"));
        x1041.addChild(x1044);
        x1044.setNormalizedName(MultiLanguageString.importFromString("en17:generate-passwordpt14:gerar-password"));
        x1044.setExecutionPath("/generateNewPassword.do?method=prepare&page=0");
        x1044.setVisible(true);
        Functionality x1045 =
                new Functionality(MultiLanguageString.importFromString("en20:Management Functionspt16:Cargos de Gestão"));
        x1041.addChild(x1045);
        x1045.setNormalizedName(MultiLanguageString.importFromString("en20:management-functionspt16:cargos-de-gestao"));
        x1045.setExecutionPath("/functionsManagement/personSearchForFunctionsManagement.faces");
        x1045.setVisible(true);
        Functionality x1046 =
                new Functionality(MultiLanguageString.importFromString("en20:Management Functionspt16:Cargos de Gestão"));
        x1041.addChild(x1046);
        x1046.setNormalizedName(MultiLanguageString.importFromString("en20:management-functionspt16:cargos-de-gestao"));
        x1046.setExecutionPath("/functionsManagement/listPersonFunctions.faces");
        x1046.setVisible(false);
        Functionality x1047 =
                new Functionality(MultiLanguageString.importFromString("en20:Management Functionspt16:Cargos de Gestão"));
        x1041.addChild(x1047);
        x1047.setNormalizedName(MultiLanguageString.importFromString("en20:management-functionspt16:cargos-de-gestao"));
        x1047.setExecutionPath("/functionsManagement/editFunction.faces");
        x1047.setVisible(false);
        Functionality x1048 = new Functionality(MultiLanguageString.importFromString("en15:Activate Personpt14:Activar Pessoa"));
        x1041.addChild(x1048);
        x1048.setNormalizedName(MultiLanguageString.importFromString("en15:activate-personpt14:activar-pessoa"));
        x1048.setExecutionPath("/recoverInactivePerson.do?method=prepare&page=0");
        x1048.setVisible(true);
        Functionality x1049 =
                new Functionality(MultiLanguageString.importFromString("en20:Management Functionspt16:Cargos de Gestão"));
        x1041.addChild(x1049);
        x1049.setTitle(MultiLanguageString.importFromString("pt14:Escolher Cargo"));
        x1049.setNormalizedName(MultiLanguageString.importFromString("en20:management-functionspt16:cargos-de-gestao"));
        x1049.setExecutionPath("functionsManagement/chooseFunction.faces");
        x1049.setVisible(false);
        Functionality x1050 =
                new Functionality(MultiLanguageString.importFromString("en20:Management Functionspt16:Cargos de Gestão"));
        x1041.addChild(x1050);
        x1050.setTitle(MultiLanguageString.importFromString("pt11:Confirmação"));
        x1050.setNormalizedName(MultiLanguageString.importFromString("en20:management-functionspt16:cargos-de-gestao"));
        x1050.setExecutionPath("functionsManagement/confirmation.faces");
        x1050.setVisible(false);
        Functionality x1051 =
                new Functionality(MultiLanguageString.importFromString("en20:Management Functionspt16:Cargos de Gestão"));
        x1041.addChild(x1051);
        x1051.setTitle(MultiLanguageString.importFromString("pt22:Apagar Cargo de Gestão"));
        x1051.setNormalizedName(MultiLanguageString.importFromString("en20:management-functionspt16:cargos-de-gestao"));
        x1051.setExecutionPath("functionsManagement/deletePersonFunction.faces");
        x1051.setVisible(false);
        Functionality x1052 = new Functionality(MultiLanguageString.importFromString("en11:Edit Personpt13:Editar Pessoa"));
        x1041.addChild(x1052);
        x1052.setNormalizedName(MultiLanguageString.importFromString("en11:edit-personpt13:editar-pessoa"));
        x1052.setExecutionPath("/editPerson.do?method=prepareSearchPersonToEdit");
        x1052.setVisible(true);
        Module x1054 =
                new Module(MultiLanguageString.importFromString("en23:External Invited Peoplept27:Pessoas Externas-Convidadas"),
                        "/");
        x1041.addChild(x1054);
        x1054.setNormalizedName(MultiLanguageString
                .importFromString("en23:external-invited-peoplept27:pessoas-externas-convidadas"));
        x1054.setExecutionPath("");
        x1054.setVisible(true);
        Functionality x1053 =
                new Functionality(
                        MultiLanguageString
                                .importFromString("en30:Create External Invited Personpt30:Criar Pessoa Externa-Convidada"));
        x1054.addChild(x1053);
        x1053.setTitle(MultiLanguageString.importFromString("pt20:Criar Pessoa Externa"));
        x1053.setNormalizedName(MultiLanguageString
                .importFromString("en30:create-external-invited-personpt30:criar-pessoa-externa-convidada"));
        x1053.setExecutionPath("createInvitedPerson.do?method=prepareSearchExistentPersonBeforeCreateNewInvitedPerson");
        x1053.setVisible(true);
        Functionality x1055 =
                new Functionality(MultiLanguageString.importFromString("en22:Invitations Managementpt18:Gestão de Convites"));
        x1054.addChild(x1055);
        x1055.setNormalizedName(MultiLanguageString.importFromString("en22:invitations-managementpt18:gestao-de-convites"));
        x1055.setExecutionPath("/invitationsManagement.do?method=prepareSearchPersonForManageInvitations");
        x1055.setVisible(true);
        Functionality x1059 = new Functionality(MultiLanguageString.importFromString("en15:Role Managementpt15:Gestão de Roles"));
        x1024.addChild(x1059);
        x1059.setTitle(MultiLanguageString.importFromString("en15:Role Managementpt15:Gestão de Roles"));
        x1059.setNormalizedName(MultiLanguageString.importFromString("en15:role-managementpt15:gestao-de-roles"));
        x1059.setExecutionPath("/viewPersonsWithRole.do?method=prepare");
        Module x1062 = new Module(MultiLanguageString.importFromString("en12:Equivalencespt13:Equivalências"), "/");
        x914.addChild(x1062);
        x1062.setNormalizedName(MultiLanguageString.importFromString("en12:equivalencespt13:equivalencias"));
        x1062.setExecutionPath("");
        x1062.setVisible(true);
        Functionality x1061 =
                new Functionality(MultiLanguageString.importFromString("en17:No need to enrollpt22:Não necessita de fazer"));
        x1062.addChild(x1061);
        x1061.setNormalizedName(MultiLanguageString.importFromString("en17:no-need-to-enrollpt22:nao-necessita-de-fazer"));
        x1061.setExecutionPath("/prepareNotNeedToEnroll.do");
        x1061.setVisible(true);
        Functionality x1063 = new Functionality(MultiLanguageString.importFromString("en12:Equivalencespt13:Equivalências"));
        x1062.addChild(x1063);
        x1063.setNormalizedName(MultiLanguageString.importFromString("en12:equivalencespt13:equivalencias"));
        x1063.setExecutionPath("/curricularCourseEquivalencies.do?method=prepare");
        x1063.setVisible(true);
        Module x1066 = new Module(MultiLanguageString.importFromString("en15:File Managementpt19:Gestão de Ficheiros"), "/");
        x914.addChild(x1066);
        x1066.setNormalizedName(MultiLanguageString.importFromString("en15:file-managementpt19:gestao-de-ficheiros"));
        x1066.setExecutionPath("");
        x1066.setVisible(false);
        Functionality x1065 =
                new Functionality(MultiLanguageString.importFromString("en15:File Generationpt20:Geração de Ficheiros"));
        x1066.addChild(x1065);
        x1065.setNormalizedName(MultiLanguageString.importFromString("en15:file-generationpt20:geracao-de-ficheiros"));
        x1065.setExecutionPath("/generateFiles.do?method=firstPage");
        x1065.setVisible(true);
        Functionality x1067 = new Functionality(MultiLanguageString.importFromString("en11:File Uploadpt19:Upload de Ficheiros"));
        x1066.addChild(x1067);
        x1067.setNormalizedName(MultiLanguageString.importFromString("en11:file-uploadpt19:upload-de-ficheiros"));
        x1067.setExecutionPath("/uploadFiles.do?method=firstPage");
        x1067.setVisible(true);
        Module x1069 = new Module(MultiLanguageString.importFromString("en15:File Generationpt20:Geração de Ficheiros"), "/");
        x1066.addChild(x1069);
        x1069.setNormalizedName(MultiLanguageString.importFromString("en15:file-generationpt20:geracao-de-ficheiros"));
        x1069.setExecutionPath("/generateFiles.do?method=firstPage");
        x1069.setVisible(false);
        Functionality x1068 = new Functionality(MultiLanguageString.importFromString("en8:Homepagept16:Página Principal"));
        x1069.addChild(x1068);
        x1068.setNormalizedName(MultiLanguageString.importFromString("en8:homepagept16:pagina-principal"));
        x1068.setExecutionPath("/index.do");
        x1068.setVisible(true);
        Module x1071 = new Module(MultiLanguageString.importFromString("en15:File Generationpt20:Geração de Ficheiros"), "/");
        x1069.addChild(x1071);
        x1071.setNormalizedName(MultiLanguageString.importFromString("en15:file-generationpt20:geracao-de-ficheiros"));
        x1071.setExecutionPath("");
        x1071.setVisible(true);
        Functionality x1070 =
                new Functionality(MultiLanguageString.importFromString("en13:File for SIBSpt20:Ficheiro para a SIBS"));
        x1071.addChild(x1070);
        x1070.setNormalizedName(MultiLanguageString.importFromString("en13:file-for-sibspt20:ficheiro-para-a-sibs"));
        x1070.setExecutionPath("/generateFiles.do?method=prepareChooseForGenerateFiles&file=sibs");
        x1070.setVisible(true);
        Functionality x1072 =
                new Functionality(MultiLanguageString.importFromString("en16:File for Letterspt20:Ficheiro para Cartas"));
        x1071.addChild(x1072);
        x1072.setNormalizedName(MultiLanguageString.importFromString("en16:file-for-letterspt20:ficheiro-para-cartas"));
        x1072.setExecutionPath("/generateFiles.do?method=prepareChooseForGenerateFiles&file=letters");
        x1072.setVisible(true);
        Module x1076 = new Module(MultiLanguageString.importFromString("en11:File Uploadpt19:Upload de Ficheiros"), "/");
        x1066.addChild(x1076);
        x1076.setNormalizedName(MultiLanguageString.importFromString("en11:file-uploadpt19:upload-de-ficheiros"));
        x1076.setExecutionPath("/uploadFiles.do?method=firstPage");
        x1076.setVisible(false);
        Functionality x1075 = new Functionality(MultiLanguageString.importFromString("en8:Homepagept16:Página Principal"));
        x1076.addChild(x1075);
        x1075.setNormalizedName(MultiLanguageString.importFromString("en8:homepagept16:pagina-principal"));
        x1075.setExecutionPath("/index.do");
        x1075.setVisible(true);
        Module x1078 = new Module(MultiLanguageString.importFromString("en15:File Managementpt19:Gestão de Ficheiros"), "/");
        x1076.addChild(x1078);
        x1078.setNormalizedName(MultiLanguageString.importFromString("en15:file-managementpt19:gestao-de-ficheiros"));
        x1078.setExecutionPath("");
        x1078.setVisible(true);
        Functionality x1077 = new Functionality(MultiLanguageString.importFromString("en9:SIBS Filept16:Ficheiro da SIBS"));
        x1078.addChild(x1077);
        x1077.setNormalizedName(MultiLanguageString.importFromString("en9:sibs-filept16:ficheiro-da-sibs"));
        x1077.setExecutionPath("/uploadFiles.do?method=prepareChooseForUploadFiles&file=sibs");
        x1077.setVisible(true);
        Module x1083 = new Module(MultiLanguageString.importFromString("en20:Finalcial Managementpt17:Gestão Financeira"), "/");
        x914.addChild(x1083);
        x1083.setNormalizedName(MultiLanguageString.importFromString("en20:financial-managementpt17:gestao-financeira"));
        x1083.setExecutionPath("");
        x1083.setVisible(true);
        Functionality x1082 =
                new Functionality(MultiLanguageString.importFromString("en16:Guide Managementpt15:Gestão de Guias"));
        x1083.addChild(x1082);
        x1082.setNormalizedName(MultiLanguageString.importFromString("en16:guide-managementpt15:gestao-de-guias"));
        x1082.setExecutionPath("/guideManagement.do?method=firstPage");
        x1082.setVisible(true);
        Functionality x1084 =
                new Functionality(
                        MultiLanguageString
                                .importFromString("en25:Update Tuition Situationspt31:Actualizar Situações de Propina"));
        x1083.addChild(x1084);
        x1084.setNormalizedName(MultiLanguageString
                .importFromString("en25:update-tuition-situationspt31:actualizar-situacoes-de-propina"));
        x1084.setExecutionPath("/gratuity/updateGratuitySituations.faces");
        x1084.setVisible(true);
        Module x1086 = new Module(MultiLanguageString.importFromString("en16:Guide Managementpt15:Gestão de Guias"), "/");
        x1083.addChild(x1086);
        x1086.setNormalizedName(MultiLanguageString.importFromString("en16:guide-managementpt15:gestao-de-guias"));
        x1086.setExecutionPath("/guideManagement.do?method=firstPage");
        x1086.setVisible(false);
        Functionality x1085 = new Functionality(MultiLanguageString.importFromString("en8:Homepagept16:Página Principal"));
        x1086.addChild(x1085);
        x1085.setNormalizedName(MultiLanguageString.importFromString("en8:homepagept16:pagina-principal"));
        x1085.setExecutionPath("/index.do");
        x1085.setVisible(true);
        Module x1088 = new Module(MultiLanguageString.importFromString("en16:Guide Managementpt15:Gestão de Guias"), "/");
        x1086.addChild(x1088);
        x1088.setNormalizedName(MultiLanguageString.importFromString("en16:guide-managementpt15:gestao-de-guias"));
        x1088.setExecutionPath("");
        x1088.setVisible(true);
        Functionality x1087 = new Functionality(MultiLanguageString.importFromString("en13:Guide Editionpt15:Edição de Guias"));
        x1088.addChild(x1087);
        x1087.setNormalizedName(MultiLanguageString.importFromString("en13:guide-editionpt15:edicao-de-guias"));
        x1087.setExecutionPath("/guideManagement.do?method=prepareChooseGuide");
        x1087.setVisible(true);
        Module x1093 = new Module(MultiLanguageString.importFromString("en14:CMS Managementpt13:Gestão de CMS"), "/");
        x914.addChild(x1093);
        x1093.setNormalizedName(MultiLanguageString.importFromString("en14:cms-managementpt13:gestao-de-cms"));
        x1093.setExecutionPath("");
        x1093.setVisible(true);
        Functionality x1092 =
                new Functionality(MultiLanguageString.importFromString("en11:User Groupspt22:Grupos de Utilizadores"));
        x1093.addChild(x1092);
        x1092.setNormalizedName(MultiLanguageString.importFromString("en11:user-groupspt22:grupos-de-utilizadores"));
        x1092.setExecutionPath("/cms/personalGroupsManagement.do?method=prepare");
        x1092.setVisible(true);
        Functionality x1094 = new Functionality(MultiLanguageString.importFromString("en14:Configurationspt13:Configurações"));
        x1093.addChild(x1094);
        x1094.setNormalizedName(MultiLanguageString.importFromString("en14:configurationspt13:configuracoes"));
        x1094.setExecutionPath("/cms/cmsConfigurationManagement.do?method=prepare");
        x1094.setVisible(true);
        Functionality x1095 =
                new Functionality(MultiLanguageString.importFromString("en14:Course Websitept22:Páginas de Disciplinas"));
        x1093.addChild(x1095);
        x1095.setNormalizedName(MultiLanguageString.importFromString("en14:course-websitept22:paginas-de-disciplinas"));
        x1095.setExecutionPath("/cms/executionCourseWebsiteManagement.do?method=viewAll");
        x1095.setVisible(true);
        Module x1098 = new Module(MultiLanguageString.importFromString("en18:Support Managementpt17:Gestão do Suporte"), "/");
        x914.addChild(x1098);
        x1098.setNormalizedName(MultiLanguageString.importFromString("en18:support-managementpt17:gestao-do-suporte"));
        x1098.setExecutionPath("");
        x1098.setVisible(true);
        Functionality x1097 =
                new Functionality(MultiLanguageString.importFromString("en19:Glossary Managementpt19:Gestão de Glossário"));
        x1098.addChild(x1097);
        x1097.setNormalizedName(MultiLanguageString.importFromString("en19:glossary-managementpt19:gestao-de-glossario"));
        x1097.setExecutionPath("/manageGlossary.do?method=prepare");
        x1097.setVisible(true);
        Functionality x1099 = new Functionality(MultiLanguageString.importFromString("en14:FAQ Managementpt15:Gestão de FAQ s"));
        x1098.addChild(x1099);
        x1099.setNormalizedName(MultiLanguageString.importFromString("en14:faq-managementpt15:gestao-de-faq-s"));
        x1099.setExecutionPath("/manageFAQs.do?method=prepare");
        x1099.setVisible(true);
        Functionality x1100 = new Functionality(MultiLanguageString.importFromString("en14:FAQ Managementpt15:Gestão de FAQ s"));
        x1098.addChild(x1100);
        x1100.setNormalizedName(MultiLanguageString.importFromString("en14:faq-managementpt15:gestao-de-faq-s"));
        x1100.setExecutionPath("/manageFAQSections.do?page=1&method=createFAQSection");
        x1100.setVisible(false);
        Module x1103 = new Module(MultiLanguageString.importFromString("en17:Object Managementpt18:Gestão de Objectos"), "/");
        x914.addChild(x1103);
        x1103.setNormalizedName(MultiLanguageString.importFromString("en17:object-managementpt18:gestao-de-objectos"));
        x1103.setExecutionPath("");
        x1103.setVisible(true);
        Functionality x1102 =
                new Functionality(MultiLanguageString.importFromString("en16:Cache Managementpt15:Gestão da Cache"));
        x1103.addChild(x1102);
        x1102.setNormalizedName(MultiLanguageString.importFromString("en16:cache-managementpt15:gestao-da-cache"));
        x1102.setExecutionPath("/manageCache.do?method=prepare");
        x1102.setVisible(true);
        Functionality x1104 =
                new Functionality(
                        MultiLanguageString.importFromString("en21:Properties Formattingpt26:Formatação de Propriedades"));
        x1103.addChild(x1104);
        x1104.setNormalizedName(MultiLanguageString.importFromString("en21:properties-formattingpt26:formatacao-de-propriedades"));
        x1104.setExecutionPath("/domainObjectStringPropertyFormatter.do?method=prepare");
        x1104.setVisible(true);
        Functionality x1105 = new Functionality(MultiLanguageString.importFromString("en13:Merge Objectspt17:Fusão de Objectos"));
        x1103.addChild(x1105);
        x1105.setNormalizedName(MultiLanguageString.importFromString("en13:merge-objectspt17:fusao-de-objectos"));
        x1105.setExecutionPath("/mergeObjects.do?method=prepare");
        x1105.setVisible(true);
        Functionality x1106 =
                new Functionality(MultiLanguageString.importFromString("en14:Object Editionpt18:Edição de Objectos"));
        x1103.addChild(x1106);
        x1106.setNormalizedName(MultiLanguageString.importFromString("en14:object-editionpt18:edicao-de-objectos"));
        x1106.setExecutionPath("/domainObjectManager.do?method=prepare");
        x1106.setVisible(true);
        Functionality x1107 = new Functionality(MultiLanguageString.importFromString("en12:Merge Peoplept16:Fusão de Pessoas"));
        x1103.addChild(x1107);
        x1107.setTitle(MultiLanguageString.importFromString("en12:Merge Peoplept16:Fusão de Pessoas"));
        x1107.setDescription(MultiLanguageString.importFromString("en12:Merge Peoplept16:Fusão de Pessoas"));
        x1107.setNormalizedName(MultiLanguageString.importFromString("en12:merge-peoplept16:fusao-de-pessoas"));
        x1107.setExecutionPath("mergePersons.do?method=prepare");
        Module x1110 = new Module(MultiLanguageString.importFromString("en17:System Managementpt17:Gestão do Sistema"), "/");
        x914.addChild(x1110);
        x1110.setNormalizedName(MultiLanguageString.importFromString("en17:system-managementpt17:gestao-do-sistema"));
        x1110.setExecutionPath("");
        x1110.setVisible(true);
        Functionality x1109 =
                new Functionality(
                        MultiLanguageString.importFromString("en23:Services Monitorizationpt25:Monitorização de Serviços"));
        x1110.addChild(x1109);
        x1109.setNormalizedName(MultiLanguageString
                .importFromString("en23:services-monitorizationpt25:monitorizacao-de-servicos"));
        x1109.setExecutionPath("/monitorServices.do?method=monitor");
        x1109.setVisible(true);
        Functionality x1111 =
                new Functionality(
                        MultiLanguageString.importFromString("en23:Requests Monitorizationpt24:Monitorização de Pedidos"));
        x1110.addChild(x1111);
        x1111.setNormalizedName(MultiLanguageString.importFromString("en23:requests-monitorizationpt24:monitorizacao-de-pedidos"));
        x1111.setExecutionPath("/monitorRequestLogs.do?method=listFiles");
        x1111.setVisible(true);
        Functionality x1112 =
                new Functionality(
                        MultiLanguageString.importFromString("en20:Users Monitorizationpt29:Monitorização de Utilizadores"));
        x1110.addChild(x1112);
        x1112.setNormalizedName(MultiLanguageString
                .importFromString("en20:users-monitorizationpt29:monitorizacao-de-utilizadores"));
        x1112.setExecutionPath("/monitorUsers.do?method=monitor");
        x1112.setVisible(true);
        Functionality x1113 =
                new Functionality(MultiLanguageString.importFromString("en18:System Informationpt22:Informações do Sistema"));
        x1110.addChild(x1113);
        x1113.setNormalizedName(MultiLanguageString.importFromString("en18:system-informationpt22:informacoes-do-sistema"));
        x1113.setExecutionPath("/monitorSystem.do?method=monitor");
        x1113.setVisible(true);
        Functionality x1114 = new Functionality(MultiLanguageString.importFromString("en13:Kerberos Testpt14:Teste Kerberos"));
        x1110.addChild(x1114);
        x1114.setNormalizedName(MultiLanguageString.importFromString("en13:kerberos-testpt14:teste-kerberos"));
        x1114.setExecutionPath("/changePasswordForward.do");
        x1114.setVisible(true);
        Functionality x1115 = new Functionality(MultiLanguageString.importFromString("en4:Cronpt4:Cron"));
        x1110.addChild(x1115);
        x1115.setNormalizedName(MultiLanguageString.importFromString("en4:Cronpt4:cron"));
        x1115.setExecutionPath("/cron.do?method=showScripts&page=0");
        x1115.setVisible(true);
        Functionality x1116 = new Functionality(MultiLanguageString.importFromString("en13:Kerberos Testpt14:Teste Kerberos"));
        x1110.addChild(x1116);
        x1116.setNormalizedName(MultiLanguageString.importFromString("en13:kerberos-testpt14:teste-kerberos"));
        x1116.setExecutionPath("/changePasswordForm.do");
        x1116.setVisible(false);
        Functionality x1117 =
                new Functionality(MultiLanguageString.importFromString("en18:Transaction Systempt22:Sistema de Transacções"));
        x1110.addChild(x1117);
        x1117.setNormalizedName(MultiLanguageString.importFromString("en18:transaction-systempt22:sistema-de-transaccoes"));
        x1117.setExecutionPath("/transactionSystem.do?method=view");
        x1117.setVisible(true);
        Functionality x1118 =
                new Functionality(MultiLanguageString.importFromString("en16:Login Managementpt16:Gestão de Logins"));
        x1110.addChild(x1118);
        x1118.setNormalizedName(MultiLanguageString.importFromString("en16:login-managementpt16:gestao-de-logins"));
        x1118.setExecutionPath("/loginsManagement.do?method=prepareSearchPerson");
        x1118.setVisible(true);
        Functionality x1119 = new Functionality(MultiLanguageString.importFromString("en10:Queue jobspt10:Queue jobs"));
        x1110.addChild(x1119);
        x1119.setTitle(MultiLanguageString.importFromString("en10:Queue jobspt10:Queue jobs"));
        x1119.setNormalizedName(MultiLanguageString.importFromString("en10:queue-jobspt10:queue-jobs"));
        x1119.setExecutionPath("/undoneQueueJobs.do?method=prepareUndoneQueueJobList");
        Module x1122 =
                new Module(MultiLanguageString.importFromString("en26:Functionalities Managementpt25:Gestão de Funcionalidades"),
                        "/functionalities");
        x914.addChild(x1122);
        x1122.setNormalizedName(MultiLanguageString
                .importFromString("en26:functionalities-managementpt25:gestao-de-funcionalidades"));
        x1122.setExecutionPath("");
        x1122.setVisible(true);
        Functionality x1121 =
                new Functionality(MultiLanguageString.importFromString("en20:View Functionalitiespt19:Ver Funcionalidades"));
        x1122.addChild(x1121);
        x1121.setNormalizedName(MultiLanguageString.importFromString("en20:view-functionalitiespt19:ver-funcionalidades"));
        x1121.setExecutionPath("/toplevel/view.do");
        x1121.setVisible(true);
        Functionality x1123 = new Functionality(MultiLanguageString.importFromString("en11:Test Filterpt13:Testar Filtro"));
        x1122.addChild(x1123);
        x1123.setNormalizedName(MultiLanguageString.importFromString("en11:test-filterpt13:testar-filtro"));
        x1123.setExecutionPath("/filter/index.do");
        x1123.setVisible(true);
        Functionality x1124 =
                new Functionality(MultiLanguageString.importFromString("en14:Filter Resultspt20:Resultados do Filtro"));
        x1122.addChild(x1124);
        x1124.setNormalizedName(MultiLanguageString.importFromString("en14:filter-resultspt20:resultados-do-filtro"));
        x1124.setExecutionPath("/filter/results.do");
        x1124.setVisible(false);
        Functionality x1125 =
                new Functionality(MultiLanguageString.importFromString("pt14:Group Languagept19:Linguagem de Grupos"));
        x1122.addChild(x1125);
        x1125.setNormalizedName(MultiLanguageString.importFromString("en14:group-languagept19:linguagem-de-grupos"));
        x1125.setExecutionPath("/groupLanguage.do");
        x1125.setVisible(true);
        Module x1128 = new Module(MultiLanguageString.importFromString("en14:Other Mappingspt18:Outros mapeamentos"), "/");
        x1122.addChild(x1128);
        x1128.setNormalizedName(MultiLanguageString.importFromString("en14:other-mappingspt18:outros-mapeamentos"));
        x1128.setExecutionPath("");
        x1128.setVisible(false);
        Module x1127 = new Module(MultiLanguageString.importFromString("en6:Modulept6:Modulo"), "/module");
        x1128.addChild(x1127);
        x1127.setNormalizedName(MultiLanguageString.importFromString("en6:modulept6:modulo"));
        x1127.setExecutionPath("");
        x1127.setVisible(true);
        Functionality x1126 = new Functionality(MultiLanguageString.importFromString("en4:Viewpt3:Ver"));
        x1127.addChild(x1126);
        x1126.setNormalizedName(MultiLanguageString.importFromString("en4:viewpt3:ver"));
        x1126.setExecutionPath("/view.do");
        x1126.setVisible(true);
        Functionality x1129 = new Functionality(MultiLanguageString.importFromString("en4:Editpt6:Editar"));
        x1127.addChild(x1129);
        x1129.setNormalizedName(MultiLanguageString.importFromString("en4:editpt6:editar"));
        x1129.setExecutionPath("/edit.do");
        x1129.setVisible(true);
        Functionality x1130 = new Functionality(MultiLanguageString.importFromString("en6:Creatept5:Criar"));
        x1127.addChild(x1130);
        x1130.setNormalizedName(MultiLanguageString.importFromString("en6:creatept5:criar"));
        x1130.setExecutionPath("/create.do");
        x1130.setVisible(true);
        Functionality x1131 =
                new Functionality(MultiLanguageString.importFromString("en20:Structure Managementpt19:Organizar Estrutura"));
        x1127.addChild(x1131);
        x1131.setNormalizedName(MultiLanguageString.importFromString("en20:structure-managementpt19:organizar-estrutura"));
        x1131.setExecutionPath("/organize.do");
        x1131.setVisible(true);
        Functionality x1132 =
                new Functionality(MultiLanguageString.importFromString("en16:Export Structurept18:Exportar estrutura"));
        x1127.addChild(x1132);
        x1132.setNormalizedName(MultiLanguageString.importFromString("en16:export-structurept18:exportar-estrutura"));
        x1132.setExecutionPath("/exportStructure.do");
        x1132.setVisible(true);
        Functionality x1133 =
                new Functionality(MultiLanguageString.importFromString("en16:Import Structurept18:Importar Estrutura"));
        x1127.addChild(x1133);
        x1133.setNormalizedName(MultiLanguageString.importFromString("en16:import-structurept18:importar-estrutura"));
        x1133.setExecutionPath("/uploadStructure.do");
        x1133.setVisible(true);
        Functionality x1134 =
                new Functionality(MultiLanguageString.importFromString("en16:Import Structurept18:Importar Estrutura"));
        x1127.addChild(x1134);
        x1134.setNormalizedName(MultiLanguageString.importFromString("en16:import-structurept18:importar-estrutura"));
        x1134.setExecutionPath("/importStructure.do");
        x1134.setVisible(true);
        Module x1137 =
                new Module(MultiLanguageString.importFromString("en13:Functionalitypt15:Functionalidade"), "/functionality");
        x1128.addChild(x1137);
        x1137.setNormalizedName(MultiLanguageString.importFromString("en13:functionalitypt15:functionalidade"));
        x1137.setExecutionPath("");
        x1137.setVisible(true);
        Functionality x1136 = new Functionality(MultiLanguageString.importFromString("en4:Viewpt3:Ver"));
        x1137.addChild(x1136);
        x1136.setNormalizedName(MultiLanguageString.importFromString("en4:viewpt3:ver"));
        x1136.setExecutionPath("/view.do");
        x1136.setVisible(true);
        Functionality x1138 = new Functionality(MultiLanguageString.importFromString("en4:Editpt6:Editar"));
        x1137.addChild(x1138);
        x1138.setNormalizedName(MultiLanguageString.importFromString("en4:editpt6:editar"));
        x1138.setExecutionPath("/edit.do");
        x1138.setVisible(true);
        Functionality x1139 = new Functionality(MultiLanguageString.importFromString("en6:Creatept5:Criar"));
        x1137.addChild(x1139);
        x1139.setNormalizedName(MultiLanguageString.importFromString("en6:creatept5:criar"));
        x1139.setExecutionPath("/create.do");
        x1139.setVisible(true);
        Functionality x1140 = new Functionality(MultiLanguageString.importFromString("en14:Confirm Deletept16:Confirmar Apagar"));
        x1137.addChild(x1140);
        x1140.setNormalizedName(MultiLanguageString.importFromString("en14:confirm-deletept16:confirmar-apagar"));
        x1140.setExecutionPath("/confirm.do");
        x1140.setVisible(true);
        Functionality x1141 = new Functionality(MultiLanguageString.importFromString("en6:Deletept6:Apagar"));
        x1137.addChild(x1141);
        x1141.setNormalizedName(MultiLanguageString.importFromString("en6:deletept6:dpagar"));
        x1141.setExecutionPath("/delete.do");
        x1141.setVisible(true);
        Functionality x1142 =
                new Functionality(MultiLanguageString.importFromString("en19:Manage Availabilitypt21:Gerir Disponibilidade"));
        x1137.addChild(x1142);
        x1142.setNormalizedName(MultiLanguageString.importFromString("en19:manage-availabilitypt21:gerir-disponibilidade"));
        x1142.setExecutionPath("/manage.do");
        x1142.setVisible(true);
        Functionality x1143 =
                new Functionality(
                        MultiLanguageString
                                .importFromString("en33:Interpret Availability Expressionpt40:Interpretar expressão de disponibilidade"));
        x1137.addChild(x1143);
        x1143.setNormalizedName(MultiLanguageString
                .importFromString("en33:interpret-availability-expressionpt40:interpretar-expressao-de-disponibilidade"));
        x1143.setExecutionPath("/parse.do");
        x1143.setVisible(true);
        Functionality x1144 = new Functionality(MultiLanguageString.importFromString("en7:Disablept10:Desactivar"));
        x1137.addChild(x1144);
        x1144.setNormalizedName(MultiLanguageString.importFromString("en7:disablept10:desactivar"));
        x1144.setExecutionPath("/disable.do");
        x1144.setVisible(true);
        Functionality x1145 = new Functionality(MultiLanguageString.importFromString("en6:Enablept7:Activar"));
        x1137.addChild(x1145);
        x1145.setNormalizedName(MultiLanguageString.importFromString("en6:enablept7:activar"));
        x1145.setExecutionPath("/enable.do");
        x1145.setVisible(true);
        Module x1148 = new Module(MultiLanguageString.importFromString("en3:Toppt4:Topo"), "/toplevel");
        x1128.addChild(x1148);
        x1148.setNormalizedName(MultiLanguageString.importFromString("en3:toppt4:topo"));
        x1148.setExecutionPath("");
        x1148.setVisible(true);
        Functionality x1147 = new Functionality(MultiLanguageString.importFromString("en4:Viewpt3:Ver"));
        x1148.addChild(x1147);
        x1147.setNormalizedName(MultiLanguageString.importFromString("en4:viewpt3:ver"));
        x1147.setExecutionPath("/view.do");
        x1147.setVisible(true);
        Functionality x1149 =
                new Functionality(MultiLanguageString.importFromString("en20:Structure Managementpt19:Organizar Estrutura"));
        x1148.addChild(x1149);
        x1149.setNormalizedName(MultiLanguageString.importFromString("en20:structure-managementpt19:organizar-estrutura"));
        x1149.setExecutionPath("/organize.do");
        x1149.setVisible(true);
        Functionality x1150 =
                new Functionality(MultiLanguageString.importFromString("en16:Import Structurept18:Importar Estrutura"));
        x1148.addChild(x1150);
        x1150.setNormalizedName(MultiLanguageString.importFromString("en16:import-structurept18:importar-estrutura"));
        x1150.setExecutionPath("/upload.do");
        x1150.setVisible(true);
        Module x1155 = new Module(MultiLanguageString.importFromString("en10:Frameworkspt10:Frameworks"), "/");
        x914.addChild(x1155);
        x1155.setNormalizedName(MultiLanguageString.importFromString("en10:frameworkspt10:frameworks"));
        x1155.setExecutionPath("");
        x1155.setVisible(true);
        Functionality x1154 =
                new Functionality(
                        MultiLanguageString.importFromString("en25:Java Server Faces Examplept27:Exemplo Servidor Java Faces"));
        x1155.addChild(x1154);
        x1154.setNormalizedName(MultiLanguageString
                .importFromString("en25:java-server-faces-examplept27:exemplo-servidor-java-faces"));
        x1154.setExecutionPath("/somePage.faces");
        x1154.setVisible(true);
        Module x1157 = new Module(MultiLanguageString.importFromString("en6:Strutspt6:Struts"), "/");
        x1155.addChild(x1157);
        x1157.setNormalizedName(MultiLanguageString.importFromString("en6:strutspt6:struts"));
        x1157.setExecutionPath("");
        x1157.setVisible(true);
        Functionality x1156 = new Functionality(MultiLanguageString.importFromString("en14:Struts Examplept14:Exemplo Struts"));
        x1157.addChild(x1156);
        x1156.setNormalizedName(MultiLanguageString.importFromString("en14:struts-examplept14:exemplo-struts"));
        x1156.setExecutionPath("/someStrutsPage.do?method=showFirstPage");
        x1156.setVisible(true);
        Functionality x1158 =
                new Functionality(
                        MultiLanguageString
                                .importFromString("en27:Reload Struts Configurationpt30:Recarregar Configuração Struts"));
        x1157.addChild(x1158);
        x1158.setNormalizedName(MultiLanguageString
                .importFromString("en27:reload-struts-configurationpt30:recarregar-configuracao-struts"));
        x1158.setExecutionPath("/reloadStruts.do");
        x1158.setVisible(true);
        Module x1161 = new Module(MultiLanguageString.importFromString("en9:Rendererspt9:Renderers"), "/renderers");
        x1155.addChild(x1161);
        x1161.setNormalizedName(MultiLanguageString.importFromString("en9:rendererspt9:renderers"));
        x1161.setExecutionPath("");
        x1161.setVisible(true);
        Functionality x1160 =
                new Functionality(MultiLanguageString.importFromString("en18:Renderers Examplespt18:Exemplos Renderers"));
        x1161.addChild(x1160);
        x1160.setNormalizedName(MultiLanguageString.importFromString("en18:renderers-examplespt18:exemplos-renderers"));
        x1160.setExecutionPath("/index.do");
        x1160.setVisible(true);
        Functionality x1162 =
                new Functionality(
                        MultiLanguageString
                                .importFromString("en30:Reload Renderers Configurationpt28:Reler Configuração Renderers"));
        x1161.addChild(x1162);
        x1162.setNormalizedName(MultiLanguageString
                .importFromString("en30:reload-renderers-configurationpt28:reler-configuracao-renderers"));
        x1162.setExecutionPath("/reload.do");
        x1162.setVisible(true);
        Functionality x1163 =
                new Functionality(
                        MultiLanguageString
                                .importFromString("en30:Example 1: Input from the userpt30:Exemplo 1: Input do utilizador"));
        x1161.addChild(x1163);
        x1163.setNormalizedName(MultiLanguageString
                .importFromString("en29:example-1-input-from-the-userpt29:exemplo-1-input-do-utilizador"));
        x1163.setExecutionPath("/searchPersons.do?method=prepare");
        x1163.setVisible(true);
        Module x1165 = new Module(MultiLanguageString.importFromString("en5:Partspt6:Partes"), "/");
        x1161.addChild(x1165);
        x1165.setNormalizedName(MultiLanguageString.importFromString("en5:partspt6:partes"));
        x1165.setExecutionPath("");
        x1165.setVisible(false);
        Functionality x1164 =
                new Functionality(
                        MultiLanguageString
                                .importFromString("en41:The first situation: presenting the worldpt37:Primeira situação: apresentar o mundo"));
        x1165.addChild(x1164);
        x1164.setNormalizedName(MultiLanguageString
                .importFromString("en40:the-first-situation-presenting-the-worldpt36:primeira-situacao-apresentar-o-mundo"));
        x1164.setExecutionPath("/output.do");
        x1164.setVisible(true);
        Functionality x1166 =
                new Functionality(
                        MultiLanguageString
                                .importFromString("en35:The second situation: give me inputpt29:Segunda situação: dá-me input"));
        x1165.addChild(x1166);
        x1166.setNormalizedName(MultiLanguageString
                .importFromString("en34:the-second-situation-give-me-inputpt28:segunda-situacao-da-me-input"));
        x1166.setExecutionPath("/input.do");
        x1166.setVisible(true);
        Functionality x1167 =
                new Functionality(
                        MultiLanguageString
                                .importFromString("en38:The third situation: input on steroidspt39:Terceira situação: input sob esteróides"));
        x1165.addChild(x1167);
        x1167.setNormalizedName(MultiLanguageString
                .importFromString("en37:the-third-situation-input-on-steroidspt38:terceira-situacao-input-sob-esteroides"));
        x1167.setExecutionPath("/steroids.do");
        x1167.setVisible(true);
        Functionality x1168 =
                new Functionality(
                        MultiLanguageString
                                .importFromString("en44:The fourth situation: renderers meet actionspt39:Quarta situação: renderers meet actions"));
        x1165.addChild(x1168);
        x1168.setNormalizedName(MultiLanguageString
                .importFromString("en43:the-fourth-situation-renderers-meet-actionspt38:quarta-situacao-renderers-meet-actions"));
        x1168.setExecutionPath("/actions.do");
        x1168.setVisible(true);
        Functionality x1169 =
                new Functionality(
                        MultiLanguageString
                                .importFromString("en35:The fifth situation: a new rendereren33:Quinta situação: um novo renderer"));
        x1165.addChild(x1169);
        x1169.setNormalizedName(MultiLanguageString
                .importFromString("en34:the-fifth-situation-a-new-rendererpt32:quinta-situacao-um-novo-renderer"));
        x1169.setExecutionPath("/new.do");
        x1169.setVisible(true);
        Functionality x1170 =
                new Functionality(MultiLanguageString.importFromString("en19:Appendix A: Schemaspt20:Apêndice A: Esquemas"));
        x1165.addChild(x1170);
        x1170.setNormalizedName(MultiLanguageString.importFromString("en18:appendix-a-schemaspt19:apendice-a-esquemas"));
        x1170.setExecutionPath("/schemas.do");
        x1170.setVisible(true);
        Module x1175 = new Module(MultiLanguageString.importFromString("en8:Paymentspt10:Pagamentos"), "/");
        x914.addChild(x1175);
        x1175.setNormalizedName(MultiLanguageString.importFromString("en8:paymentspt10:pagamentos"));
        x1175.setExecutionPath("");
        x1175.setVisible(true);
        Functionality x1174 = new Functionality(MultiLanguageString.importFromString("en11:File Uploadpt18:Upload de Ficheiro"));
        x1175.addChild(x1174);
        x1174.setNormalizedName(MultiLanguageString.importFromString("en11:file-uploadpt18:upload-de-ficheiro"));
        x1174.setExecutionPath("/SIBSPayments.do?method=prepareUploadSIBSPaymentFiles");
        x1174.setVisible(true);
        Functionality x1176 =
                new Functionality(MultiLanguageString.importFromString("en15:Manage Paymentspt16:Gerir Pagamentos"));
        x1175.addChild(x1176);
        x1176.setTitle(MultiLanguageString.importFromString("en15:Manage Paymentspt16:Gerir Pagamentos"));
        x1176.setNormalizedName(MultiLanguageString.importFromString("en15:manage-paymentspt16:gerir-pagamentos"));
        x1176.setExecutionPath("/payments.do?method=prepareSearchPerson");
        x1176.setVisible(true);
        ExpressionGroupAvailability x1628 = new ExpressionGroupAvailability(x1176, "role(MANAGER)");
        x1628.setTargetGroup(Group.fromString("role(MANAGER)"));
        x1176.setAvailabilityPolicy(x1628);
        Functionality x1177 =
                new Functionality(MultiLanguageString.importFromString("en13:Payment Rulespt20:Regras de Pagamentos"));
        x1175.addChild(x1177);
        x1177.setTitle(MultiLanguageString.importFromString("en13:Payment Rulespt20:Regras de Pagamentos"));
        x1177.setNormalizedName(MultiLanguageString.importFromString("pt20:regras-de-pagamentos"));
        x1177.setExecutionPath("/postingRules.do?method=prepare");
        x1177.setVisible(true);
        Functionality x1178 = new Functionality(MultiLanguageString.importFromString("en13:Payment Codespt7:Códigos"));
        x1175.addChild(x1178);
        x1178.setTitle(MultiLanguageString.importFromString("en13:Payment Codespt7:Códigos"));
        x1178.setNormalizedName(MultiLanguageString.importFromString("en13:payment-codespt7:codigos"));
        x1178.setExecutionPath("/payments.do?method=prepareViewPaymentCodeMappings");
        Functionality x1179 = new Functionality(MultiLanguageString.importFromString("en7:Reportspt10:Relatórios"));
        x1175.addChild(x1179);
        x1179.setTitle(MultiLanguageString.importFromString("en20:Sibs payment reportspt28:Relatórios de pagamento sibs"));
        x1179.setNormalizedName(MultiLanguageString.importFromString("en7:reportspt10:relatorios"));
        x1179.setExecutionPath("/sibsReports.do?method=prepareReportByYearAndMonth");
        Functionality x1180 =
                new Functionality(MultiLanguageString.importFromString("en14:Tuition reportpt21:Relatório de propinas"));
        x1175.addChild(x1180);
        x1180.setTitle(MultiLanguageString.importFromString("en14:Tuition reportpt21:Relatório de propinas"));
        x1180.setDescription(MultiLanguageString.importFromString("en14:Tuition reportpt21:Relatório de propinas"));
        x1180.setNormalizedName(MultiLanguageString.importFromString("en14:tuition-reportpt21:relatorio-de-propinas"));
        x1180.setExecutionPath("/gratuityReports.do?method=listReports");
        Functionality x1181 =
                new Functionality(MultiLanguageString.importFromString("en16:Reference Exportpt25:Exportação de Referências"));
        x1175.addChild(x1181);
        x1181.setTitle(MultiLanguageString.importFromString("en16:Reference Exportpt25:Exportação de Referências"));
        x1181.setDescription(MultiLanguageString.importFromString("en16:Reference Exportpt25:Exportação de Referências"));
        x1181.setNormalizedName(MultiLanguageString.importFromString("en16:reference-reportpt25:exportacao-de-referencias"));
        x1181.setExecutionPath("exportSIBSPayments.do?method=listOutgoingPaymentsFile");
        Functionality x1182 =
                new Functionality(MultiLanguageString.importFromString("en11:Debt Reportpt20:Relatório de Dividas"));
        x1175.addChild(x1182);
        x1182.setTitle(MultiLanguageString.importFromString("en11:Debt Reportpt20:Relatório de Dividas"));
        x1182.setDescription(MultiLanguageString.importFromString("en11:Debt Reportpt20:Relatório de Dividas"));
        x1182.setNormalizedName(MultiLanguageString.importFromString("en11:debt-reportpt20:relatorio-de-dividas"));
        x1182.setExecutionPath("/eventReports.do?method=listReports");
        ExpressionGroupAvailability x1629 = new ExpressionGroupAvailability(x1175, "role(MANAGER)");
        x1629.setTargetGroup(Group.fromString("role(MANAGER)"));
        x1175.setAvailabilityPolicy(x1629);
        Module x1185 =
                new Module(MultiLanguageString.importFromString("en19:Calendar Managementpt21:Gestão de Calendários"), "/");
        x914.addChild(x1185);
        x1185.setNormalizedName(MultiLanguageString.importFromString("en19:calendar-managementpt21:gestao-de-calendarios"));
        x1185.setExecutionPath("");
        x1185.setVisible(true);
        Functionality x1184 =
                new Functionality(MultiLanguageString.importFromString("en18:Academic Calendarspt22:Calendários Académicos"));
        x1185.addChild(x1184);
        x1184.setNormalizedName(MultiLanguageString.importFromString("en18:academic-calendarspt22:calendarios-academicos"));
        x1184.setExecutionPath("/academicCalendarsManagement.do?method=prepareChooseCalendar");
        x1184.setVisible(true);
        Functionality x1186 =
                new Functionality(
                        MultiLanguageString.importFromString("en24:Create Academic Calendarpt26:Criar Calendário Académico"));
        x1185.addChild(x1186);
        x1186.setNormalizedName(MultiLanguageString
                .importFromString("en24:create-academic-calendarpt26:criar-calendario-academico"));
        x1186.setExecutionPath("/prepareCreateAcademicCalendar.do");
        x1186.setVisible(false);
        Module x1189 =
                new Module(MultiLanguageString.importFromString("en21:Scientific Activitiespt23:Actividades Científicas"), "/");
        x914.addChild(x1189);
        x1189.setTitle(MultiLanguageString.importFromString("en21:Scientific Activitiespt23:Actividades Científicas"));
        x1189.setNormalizedName(MultiLanguageString.importFromString("en21:scientific-activitiespt23:actividades-cientificas"));
        x1189.setExecutionPath("");
        x1189.setVisible(true);
        Functionality x1188 = new Functionality(MultiLanguageString.importFromString("en12:Edit Journalpt14:Editar Revista"));
        x1189.addChild(x1188);
        x1188.setTitle(MultiLanguageString.importFromString("en12:Edit Journalpt14:Editar Revista"));
        x1188.setNormalizedName(MultiLanguageString.importFromString("en12:edit-journalpt14:editar-revista"));
        x1188.setExecutionPath("/editScientificJournal.do?method=prepare");
        x1188.setVisible(true);
        Functionality x1190 = new Functionality(MultiLanguageString.importFromString("en10:Edit Eventpt13:Editar Evento"));
        x1189.addChild(x1190);
        x1190.setTitle(MultiLanguageString.importFromString("en10:Edit Eventpt13:Editar Evento"));
        x1190.setNormalizedName(MultiLanguageString.importFromString("en10:edit-eventpt13:editar-evento"));
        x1190.setExecutionPath("/editEvent.do?method=prepare");
        x1190.setVisible(true);
        Functionality x1191 = new Functionality(MultiLanguageString.importFromString("en14:Merge Journalspt15:Juntar Revistas"));
        x1189.addChild(x1191);
        x1191.setTitle(MultiLanguageString.importFromString("en14:Merge Journalspt15:Juntar Revistas"));
        x1191.setNormalizedName(MultiLanguageString.importFromString("en14:merge-journalspt15:juntar-revistas"));
        x1191.setExecutionPath("/mergeScientificJournal.do?method=prepare");
        x1191.setVisible(true);
        Functionality x1192 = new Functionality(MultiLanguageString.importFromString("en12:Merge Eventspt14:Juntar Eventos"));
        x1189.addChild(x1192);
        x1192.setTitle(MultiLanguageString.importFromString("en12:Merge Eventspt14:Juntar Eventos"));
        x1192.setNormalizedName(MultiLanguageString.importFromString("en12:merge-eventspt14:juntar-eventos"));
        x1192.setExecutionPath("/mergeEvents.do?method=prepare");
        x1192.setVisible(true);
        Functionality x1193 =
                new Functionality(MultiLanguageString.importFromString("en20:Merge Journal Issuespt25:Juntar Volumes de Revista"));
        x1189.addChild(x1193);
        x1193.setTitle(MultiLanguageString.importFromString("en20:Merge Journal Issuespt25:Juntar Volumes de Revista"));
        x1193.setNormalizedName(MultiLanguageString.importFromString("en20:merge-journal-issuespt25:juntar-volumes-de-revista"));
        x1193.setExecutionPath("/editScientificJournalMergeJournalIssues.do?method=prepare");
        x1193.setVisible(true);
        Functionality x1194 =
                new Functionality(MultiLanguageString.importFromString("en20:Merge Event Editionspt24:Juntar Edições de Evento"));
        x1189.addChild(x1194);
        x1194.setTitle(MultiLanguageString.importFromString("en20:Merge Event Editionspt24:Juntar Edições de Evento"));
        x1194.setNormalizedName(MultiLanguageString.importFromString("en20:merge-event-editionspt24:juntar-edicoes-de-evento"));
        x1194.setExecutionPath("/editEventMergeEventEditions.do?method=prepare");
        x1194.setVisible(true);
        Functionality x1195 =
                new Functionality(MultiLanguageString.importFromString("en20:Merge Journal Issuespt25:Juntar Volumes de Revista"));
        x1189.addChild(x1195);
        x1195.setNormalizedName(MultiLanguageString.importFromString("en20:merge-journal-issuespt25:juntar-volumes-de-revista"));
        x1195.setExecutionPath("/mergeJournalIssues.do");
        x1195.setVisible(false);
        Functionality x1196 =
                new Functionality(MultiLanguageString.importFromString("en20:Merge Event Editionspt24:Juntar Edições de Evento"));
        x1189.addChild(x1196);
        x1196.setNormalizedName(MultiLanguageString.importFromString("en20:merge-event-editionspt24:juntar-edicoes-de-evento"));
        x1196.setExecutionPath("/mergeEventEditions.do");
        x1196.setVisible(false);
        Module x1199 =
                new Module(
                        MultiLanguageString
                                .importFromString("en34:Persistent Groups (Access Control)pt40:Grupos Persistentes (Controlo de Acesso)"),
                        "/");
        x914.addChild(x1199);
        x1199.setNormalizedName(MultiLanguageString
                .importFromString("en32:persistent-groups-access-controlpt38:grupos-persistentes-controlo-de-acesso"));
        x1199.setExecutionPath("");
        x1199.setVisible(true);
        Functionality x1198 = new Functionality(MultiLanguageString.importFromString("en13:Manage Groupspt12:Gerir Grupos"));
        x1199.addChild(x1198);
        x1198.setNormalizedName(MultiLanguageString.importFromString("en13:manage-groupspt12:gerir-grupos"));
        x1198.setExecutionPath("/accessControlPersistentGroupsManagement.do?method=listAllGroups");
        x1198.setVisible(true);
        Module x1202 = new Module(MultiLanguageString.importFromString("en18:Bolonha Transitionpt20:Transição de Bolonha"), "/");
        x914.addChild(x1202);
        x1202.setNormalizedName(MultiLanguageString.importFromString("en18:bolonha-transitionpt20:transicao-de-bolonha"));
        x1202.setExecutionPath("");
        x1202.setVisible(true);
        Functionality x1201 =
                new Functionality(
                        MultiLanguageString.importFromString("en24:Global Equivalence Planspt30:Planos de Equivalência Globais"));
        x1202.addChild(x1201);
        x1201.setNormalizedName(MultiLanguageString
                .importFromString("en24:global-equivalence-planspt30:planos-de-equivalencia-globais"));
        x1201.setExecutionPath("/degreeCurricularPlan/equivalencyPlan.do?method=showPlan");
        x1201.setVisible(true);
        Functionality x1203 =
                new Functionality(
                        MultiLanguageString.importFromString("en23:Local Equivalence Planspt29:Planos de Equivalência Locais"));
        x1202.addChild(x1203);
        x1203.setNormalizedName(MultiLanguageString
                .importFromString("en23:local-equivalence-planspt29:planos-de-equivalencia-locais"));
        x1203.setExecutionPath("/degreeCurricularPlan/studentEquivalencyPlan.do?method=showPlan");
        x1203.setVisible(true);
        Functionality x1204 =
                new Functionality(MultiLanguageString.importFromString("en18:Student Curriculumpt18:Currículo do Aluno"));
        x1202.addChild(x1204);
        x1204.setNormalizedName(MultiLanguageString.importFromString("en18:student-curriculumpt18:curriculo-do-aluno"));
        x1204.setExecutionPath("/bolonhaTransitionManagement.do?method=prepareChooseStudent");
        x1204.setVisible(true);
        Module x1207 = new Module(MultiLanguageString.importFromString("en8:Studentspt6:Alunos"), "/");
        x914.addChild(x1207);
        x1207.setNormalizedName(MultiLanguageString.importFromString("en8:studentspt6:alunos"));
        x1207.setExecutionPath("");
        x1207.setVisible(true);
        Functionality x1206 = new Functionality(MultiLanguageString.importFromString("en6:Managept5:Gerir"));
        x1207.addChild(x1206);
        x1206.setNormalizedName(MultiLanguageString.importFromString("en6:managept5:gerir"));
        x1206.setExecutionPath("/bolonhaStudentEnrolment.do?method=prepareSearchStudent");
        x1206.setVisible(true);
        Functionality x1208 =
                new Functionality(MultiLanguageString.importFromString("en15:Move Enrolmentspt16:Mover Inscrições"));
        x1207.addChild(x1208);
        x1208.setNormalizedName(MultiLanguageString.importFromString("en15:move-enrolmentspt16:mover-inscricoes"));
        x1208.setExecutionPath("/curriculumLinesLocationManagement.do?method=prepare");
        x1208.setVisible(false);
        Functionality x1209 =
                new Functionality(MultiLanguageString.importFromString("en20:Dismissal Managementpt19:Gestão de Dispensas"));
        x1207.addChild(x1209);
        x1209.setNormalizedName(MultiLanguageString.importFromString("en20:dismissal-managementpt19:gestao-de-dispensas"));
        x1209.setExecutionPath("/studentDismissals.do?method=manage");
        x1209.setVisible(false);
        Functionality x1210 =
                new Functionality(
                        MultiLanguageString.importFromString("en23:Substitution Managementpt23:Gestão de Substituições"));
        x1207.addChild(x1210);
        x1210.setNormalizedName(MultiLanguageString.importFromString("en23:substitution-managementpt23:gestao-de-substituicoes"));
        x1210.setExecutionPath("/studentSubstitutions.do?method=prepare");
        x1210.setVisible(false);
        Functionality x1211 =
                new Functionality(MultiLanguageString.importFromString("en22:Equivalence Managementpt23:Gestão de Equivalências"));
        x1207.addChild(x1211);
        x1211.setNormalizedName(MultiLanguageString.importFromString("en22:equivalence-managementpt23:gestao-de-equivalencias"));
        x1211.setExecutionPath("/studentEquivalences.do?method=prepare");
        x1211.setVisible(false);
        Functionality x1212 =
                new Functionality(MultiLanguageString.importFromString("en17:Credit Managementpt18:Gestão de Créditos"));
        x1207.addChild(x1212);
        x1212.setNormalizedName(MultiLanguageString.importFromString("en17:credit-managementpt18:gestao-de-creditos"));
        x1212.setExecutionPath("/studentCredits.do?method=prepare");
        x1212.setVisible(false);
        Functionality x1213 =
                new Functionality(MultiLanguageString.importFromString("en21:Conclusion Managementpt20:Gestão de Apuramento"));
        x1207.addChild(x1213);
        x1213.setNormalizedName(MultiLanguageString.importFromString("en21:conclusion-managementpt20:gestao-de-apuramento"));
        x1213.setExecutionPath("/registrationConclusion.do?method=show");
        x1213.setVisible(false);
        Functionality x1214 =
                new Functionality(MultiLanguageString.importFromString("en16:State Managementpt17:Gestão de Estados"));
        x1207.addChild(x1214);
        x1214.setNormalizedName(MultiLanguageString.importFromString("en16:state-management"));
        x1214.setExecutionPath("/manageRegistrationState.do?method=prepare");
        x1214.setVisible(false);
        Functionality x1215 =
                new Functionality(MultiLanguageString.importFromString("en19:View Enrolment Logspt21:Ver Logs de Inscrição"));
        x1207.addChild(x1215);
        x1215.setNormalizedName(MultiLanguageString.importFromString("en19:view-enrolment-logspt21:ver-logs-de-inscricao"));
        x1215.setExecutionPath("/curriculumLineLogs.do?method=prepareViewCurriculumLineLogs");
        Functionality x1216 = new Functionality(MultiLanguageString.importFromString("en11:DGES Importpt18:Importação da DGES"));
        x1207.addChild(x1216);
        x1216.setTitle(MultiLanguageString.importFromString("en11:DGES Importpt18:Importação da DGES"));
        x1216.setDescription(MultiLanguageString.importFromString("en11:DGES Importpt18:Importação da DGES"));
        x1216.setNormalizedName(MultiLanguageString.importFromString("en11:dges-importpt18:importacao-da-dges"));
        x1216.setExecutionPath("/dgesStudentImportationProcess.do?method=list");
        Functionality x1217 =
                new Functionality(
                        MultiLanguageString.importFromString("en25:Special Season Enrolmentspt28:Inscrições em Época Especial"));
        x1207.addChild(x1217);
        x1217.setNormalizedName(MultiLanguageString
                .importFromString("en25:special-season-enrolmentspt28:inscricoes-em-epoca-especial"));
        x1217.setExecutionPath("/specialSeason/specialSeasonStatusTracker.do?method=selectCourses");
        Functionality x1218 =
                new Functionality(
                        MultiLanguageString
                                .importFromString("en29:1st Time Applications Summarypt27:Sumário Candidaturas 1ª vez"));
        x1207.addChild(x1218);
        x1218.setTitle(MultiLanguageString.importFromString("en29:1st Time Applications Summarypt27:Sumário Candidaturas 1ª vez"));
        x1218.setNormalizedName(MultiLanguageString
                .importFromString("en29:1st-time-applications-summarypt26:sumario-candidaturas-1-vez"));
        x1218.setExecutionPath("/candidacySummary.do?method=prepare");
        Module x1221 = new Module(MultiLanguageString.importFromString("en11:Mark Sheetspt6:Pautas"), "/");
        x914.addChild(x1221);
        x1221.setNormalizedName(MultiLanguageString.importFromString("en11:mark-sheetspt6:pautas"));
        x1221.setExecutionPath("");
        x1221.setVisible(true);
        Functionality x1220 = new Functionality(MultiLanguageString.importFromString("en17:Cancel Mark Sheet"));
        x1221.addChild(x1220);
        x1220.setNormalizedName(MultiLanguageString.importFromString("en17:cancel-mark-sheetpt12:anular-pauta"));
        x1220.setExecutionPath("/markSheetManagement.do?method=prepareSearchMarkSheet");
        x1220.setVisible(true);
        Module x1224 = new Module(MultiLanguageString.importFromString("en14:Enrolment Logspt17:Logs de Inscrição"), "/");
        x914.addChild(x1224);
        x1224.setNormalizedName(MultiLanguageString.importFromString("en14:enrolment-logspt17:logs-de-inscricao"));
        Functionality x1223 =
                new Functionality(MultiLanguageString.importFromString("en19:View Enrolment Logspt21:Ver Logs de Inscrição"));
        x1224.addChild(x1223);
        x1223.setNormalizedName(MultiLanguageString.importFromString("en19:view-enrolment-logspt21:ver-logs-de-inscricao"));
        x1223.setExecutionPath("/curriculumLineLogs.do?method=prepareViewCurriculumLineLogs");
        Module x1227 =
                new Module(
                        MultiLanguageString
                                .importFromString("en30:Generated Documents Managementpt28:Gestao de Documentos Gerados"),
                        "/");
        x914.addChild(x1227);
        x1227.setTitle(MultiLanguageString
                .importFromString("en30:Generated Documents Managementpt28:Gestao de Documentos Gerados"));
        x1227.setNormalizedName(MultiLanguageString
                .importFromString("en30:generated-documents-managementpt28:gestao-de-documentos-gerados"));
        Functionality x1226 =
                new Functionality(MultiLanguageString.importFromString("en16:Search Documentspt19:Procurar Documentos"));
        x1227.addChild(x1226);
        x1226.setTitle(MultiLanguageString.importFromString("en16:Search Documentspt19:Procurar Documentos"));
        x1226.setNormalizedName(MultiLanguageString.importFromString("en16:search-documentspt19:procurar-documentos"));
        x1226.setExecutionPath("/generatedDocuments.do?method=prepare");
        Module x1230 = new Module(MultiLanguageString.importFromString("en11:Photographspt11:Fotografias"), "/photographs");
        x914.addChild(x1230);
        x1230.setTitle(MultiLanguageString.importFromString("en11:Photographspt11:Fotografias"));
        x1230.setNormalizedName(MultiLanguageString.importFromString("en11:photographspt11:fotografias"));
        Functionality x1229 = new Functionality(MultiLanguageString.importFromString("en7:Historypt9:Historial"));
        x1230.addChild(x1229);
        x1229.setTitle(MultiLanguageString.importFromString("en7:Historypt9:Historial"));
        x1229.setNormalizedName(MultiLanguageString.importFromString("en7:historypt9:historial"));
        x1229.setExecutionPath("/history.do?method=history");
        Functionality x1231 = new Functionality(MultiLanguageString.importFromString("en10:Rejectionspt9:Rejeicoes"));
        x1230.addChild(x1231);
        x1231.setTitle(MultiLanguageString.importFromString("en10:Rejectionspt9:Rejeicoes"));
        x1231.setNormalizedName(MultiLanguageString.importFromString("en10:rejectionspt9:rejeicoes"));
        x1231.setExecutionPath("/history.do?method=rejections");
        Functionality x1232 = new Functionality(MultiLanguageString.importFromString("en9:Approvalspt10:Aprovacoes"));
        x1230.addChild(x1232);
        x1232.setTitle(MultiLanguageString.importFromString("en9:Approvalspt10:Aprovacoes"));
        x1232.setNormalizedName(MultiLanguageString.importFromString("en9:approvalspt10:aprovacoes"));
        x1232.setExecutionPath("/history.do?method=approvals");
        Module x1235 = new Module(MultiLanguageString.importFromString("pt23:Núcleo de Pós Graduação"), "/");
        x914.addChild(x1235);
        x1235.setTitle(MultiLanguageString.importFromString("pt23:Núcleo de Pós Graduação"));
        x1235.setDescription(MultiLanguageString.importFromString("pt23:Núcleo de Pós Graduação"));
        x1235.setNormalizedName(MultiLanguageString.importFromString("pt23:nucleo-de-pos-graduacao"));
        Functionality x1234 =
                new Functionality(MultiLanguageString.importFromString("en13:PhD Processespt25:Processos de doutoramento"));
        x1235.addChild(x1234);
        x1234.setTitle(MultiLanguageString.importFromString("en13:PhD Processespt25:Processos de doutoramento"));
        x1234.setDescription(MultiLanguageString.importFromString("en13:PhD Processespt25:Processos de doutoramento"));
        x1234.setNormalizedName(MultiLanguageString.importFromString("en13:phd-processespt25:processos-de-doutoramento"));
        x1234.setExecutionPath("/phdIndividualProgramProcess.do?method=manageProcesses");
        Module x1238 = new Module(MultiLanguageString.importFromString("en21:External Scholarshipspt15:Bolsas Externas"), "/");
        x914.addChild(x1238);
        x1238.setNormalizedName(MultiLanguageString.importFromString("en21:external-scholarshipspt15:bolsas-externas"));
        Functionality x1237 = new Functionality(MultiLanguageString.importFromString("en13:List Entitiespt16:Listar Entidades"));
        x1238.addChild(x1237);
        x1237.setNormalizedName(MultiLanguageString.importFromString("en13:list-entitiespt16:listar-entidades"));
        x1237.setExecutionPath("/externalScholarshipProvider.do?method=list");
        Functionality x1239 = new Functionality(MultiLanguageString.importFromString("en10:Add Entitypt18:Adicionar Entidade"));
        x1238.addChild(x1239);
        x1239.setNormalizedName(MultiLanguageString.importFromString("en10:add-entitypt18:adicionar-entidade"));
        x1239.setExecutionPath("/externalScholarshipProvider.do?method=add");
        Module x12380 = new Module(MultiLanguageString.importFromString("en18:Associated Objectspt19:Objectos Associados"), "/");
        x914.addChild(x12380);
        x12380.setNormalizedName(MultiLanguageString.importFromString("en18:associated-objectspt19:objectos-associados"));
        Functionality x12370 =
                new Functionality(
                        MultiLanguageString.importFromString("en25:Manage Associated Objectspt25:Gerir Objectos Associados"));
        x12380.addChild(x12370);
        x12370.setNormalizedName(MultiLanguageString
                .importFromString("en25:manage-associated-objectspt25:gerir-objectos-associados"));
        x12370.setExecutionPath("/manageAssociatedObjects.do?method=list");
        ExpressionGroupAvailability x1630 = new ExpressionGroupAvailability(x914, "role(MANAGER)");
        x1630.setTargetGroup(Group.fromString("role(MANAGER)"));
        x914.setAvailabilityPolicy(x1630);
        Module x1243 =
                new Module(MultiLanguageString.importFromString("pt40:Secção de Pessoal Docente e Investigador"),
                        "/facultyAdmOffice");
        x823.addChild(x1243);
        x1243.setNormalizedName(MultiLanguageString.importFromString("pt40:seccao-de-pessoal-docente-e-investigador"));
        x1243.setExecutionPath("/facultyAdmOffice/index.do");
        x1243.setVisible(true);
        Functionality x1242 =
                new Functionality(MultiLanguageString.importFromString("pt40:Secção de Pessoal Docente e Investigador"));
        x1243.addChild(x1242);
        x1242.setTitle(MultiLanguageString.importFromString("pt40:Secção de Pessoal Docente e Investigador"));
        x1242.setNormalizedName(MultiLanguageString.importFromString("pt40:seccao-de-pessoal-docente-e-investigador"));
        x1242.setExecutionPath("/index.do");
        x1242.setVisible(true);
        ExpressionGroupAvailability x1631 = new ExpressionGroupAvailability(x1243, "role(CREDITS_MANAGER)");
        x1631.setTargetGroup(Group.fromString("role(CREDITS_MANAGER)"));
        x1243.setAvailabilityPolicy(x1631);
        Module x1246 =
                new Module(MultiLanguageString.importFromString("pt53:Administração de créditos de docentes do departamento"),
                        "/departmentAdmOffice");
        x823.addChild(x1246);
        x1246.setNormalizedName(MultiLanguageString
                .importFromString("pt53:administracao-de-creditos-de-docentes-do-departamento"));
        x1246.setExecutionPath("/departmentAdmOffice/index.do");
        x1246.setVisible(true);
        Functionality x1245 =
                new Functionality(
                        MultiLanguageString.importFromString("pt53:Administração de créditos de docentes do departamento"));
        x1246.addChild(x1245);
        x1245.setTitle(MultiLanguageString.importFromString("pt53:Administração de créditos de docentes do departamento"));
        x1245.setNormalizedName(MultiLanguageString
                .importFromString("pt53:administracao-de-creditos-de-docentes-do-departamento"));
        x1245.setExecutionPath("/index.do");
        x1245.setVisible(true);
        ExpressionGroupAvailability x1632 = new ExpressionGroupAvailability(x1246, "role(DEPARTMENT_CREDITS_MANAGER)");
        x1632.setTargetGroup(Group.fromString("role(DEPARTMENT_CREDITS_MANAGER)"));
        x1246.setAvailabilityPolicy(x1632);
        Module x1249 = new Module(MultiLanguageString.importFromString("pt19:Conselho Científico"), "/scientificCouncil");
        x823.addChild(x1249);
        x1249.setNormalizedName(MultiLanguageString.importFromString("pt19:conselho-cientifico"));
        x1249.setExecutionPath("/scientificCouncil/index.do");
        x1249.setVisible(true);
        Functionality x1248 = new Functionality(MultiLanguageString.importFromString("pt19:Conselho Científico"));
        x1249.addChild(x1248);
        x1248.setTitle(MultiLanguageString.importFromString("pt19:Conselho Científico"));
        x1248.setNormalizedName(MultiLanguageString.importFromString("pt19:conselho-cientifico"));
        x1248.setExecutionPath("/index.do");
        x1248.setVisible(true);
        ExpressionGroupAvailability x1633 = new ExpressionGroupAvailability(x1249, "role(SCIENTIFIC_COUNCIL)");
        x1633.setTargetGroup(Group.fromString("role(SCIENTIFIC_COUNCIL)"));
        x1249.setAvailabilityPolicy(x1633);
        Module x1252 = new Module(MultiLanguageString.importFromString("pt8:Operador"), "/operator");
        x823.addChild(x1252);
        x1252.setNormalizedName(MultiLanguageString.importFromString("pt8:operador"));
        x1252.setExecutionPath("/operator/index.do");
        x1252.setVisible(true);
        Functionality x1251 = new Functionality(MultiLanguageString.importFromString("pt8:Operador"));
        x1252.addChild(x1251);
        x1251.setTitle(MultiLanguageString.importFromString("pt8:Operador"));
        x1251.setNormalizedName(MultiLanguageString.importFromString("pt8:operador"));
        x1251.setExecutionPath("/index.do");
        x1251.setVisible(true);
        ExpressionGroupAvailability x1634 = new ExpressionGroupAvailability(x1252, "role(OPERATOR)");
        x1634.setTargetGroup(Group.fromString("role(OPERATOR)"));
        x1252.setAvailabilityPolicy(x1634);
        Module x1255 = new Module(MultiLanguageString.importFromString("pt10:Seminários"), "/teacher");
        x823.addChild(x1255);
        x1255.setNormalizedName(MultiLanguageString.importFromString("pt10:seminarios"));
        x1255.setExecutionPath("/teacher/seminariesIndex.jsp");
        x1255.setVisible(true);
        Functionality x1254 = new Functionality(MultiLanguageString.importFromString("pt10:Seminários"));
        x1255.addChild(x1254);
        x1254.setTitle(MultiLanguageString.importFromString("pt10:Seminários"));
        x1254.setNormalizedName(MultiLanguageString.importFromString("pt10:seminarios"));
        x1254.setExecutionPath("/seminariesIndex.jsp");
        x1254.setVisible(true);
        ExpressionGroupAvailability x1635 = new ExpressionGroupAvailability(x1255, "role(SEMINARIES_COORDINATOR)");
        x1635.setTargetGroup(Group.fromString("role(SEMINARIES_COORDINATOR)"));
        x1255.setAvailabilityPolicy(x1635);
        Module x1258 = new Module(MultiLanguageString.importFromString("pt8:Bolseiro"), "/grantOwner");
        x823.addChild(x1258);
        x1258.setNormalizedName(MultiLanguageString.importFromString("pt8:bolseiro"));
        x1258.setExecutionPath("/grantOwner/index.do");
        x1258.setVisible(true);
        Functionality x1257 = new Functionality(MultiLanguageString.importFromString("pt8:Bolseiro"));
        x1258.addChild(x1257);
        x1257.setTitle(MultiLanguageString.importFromString("pt8:Bolseiro"));
        x1257.setNormalizedName(MultiLanguageString.importFromString("pt8:bolseiro"));
        x1257.setExecutionPath("/index.do");
        x1257.setVisible(true);
        ExpressionGroupAvailability x1636 = new ExpressionGroupAvailability(x1258, "role(GRANT_OWNER)");
        x1636.setTargetGroup(Group.fromString("role(GRANT_OWNER)"));
        x1258.setAvailabilityPolicy(x1636);
        Module x1261 =
                new Module(MultiLanguageString.importFromString("pt26:Secretaria de Departamento"), "/departmentAdmOffice");
        x823.addChild(x1261);
        x1261.setNormalizedName(MultiLanguageString.importFromString("pt26:secretaria-de-departamento"));
        x1261.setExecutionPath("/departmentAdmOffice/index.do");
        x1261.setVisible(true);
        Functionality x1260 = new Functionality(MultiLanguageString.importFromString("pt26:Secretaria de Departamento"));
        x1261.addChild(x1260);
        x1260.setTitle(MultiLanguageString.importFromString("pt26:Secretaria de Departamento"));
        x1260.setNormalizedName(MultiLanguageString.importFromString("pt26:secretaria-de-departamento"));
        x1260.setExecutionPath("/index.do");
        x1260.setVisible(true);
        ExpressionGroupAvailability x1637 = new ExpressionGroupAvailability(x1261, "role(DEPARTMENT_ADMINISTRATIVE_OFFICE)");
        x1637.setTargetGroup(Group.fromString("role(DEPARTMENT_ADMINISTRATIVE_OFFICE)"));
        x1261.setAvailabilityPolicy(x1637);
        Module x1264 = new Module(MultiLanguageString.importFromString("pt33:Gabinete de Estudos e Planeamento"), "/gep");
        x823.addChild(x1264);
        x1264.setNormalizedName(MultiLanguageString.importFromString("pt33:gabinete-de-estudos-e-planeamento"));
        x1264.setExecutionPath("/gep/index.do");
        x1264.setVisible(true);
        Functionality x1263 = new Functionality(MultiLanguageString.importFromString("pt33:Gabinete de Estudos e Planeamento"));
        x1264.addChild(x1263);
        x1263.setTitle(MultiLanguageString.importFromString("pt33:Gabinete de Estudos e Planeamento"));
        x1263.setNormalizedName(MultiLanguageString.importFromString("pt33:gabinete-de-estudos-e-planeamento"));
        x1263.setExecutionPath("/index.do");
        x1263.setVisible(true);
        ExpressionGroupAvailability x1638 = new ExpressionGroupAvailability(x1264, "role(GEP)");
        x1638.setTargetGroup(Group.fromString("role(GEP)"));
        x1264.setAvailabilityPolicy(x1638);
        Module x1267 = new Module(MultiLanguageString.importFromString("pt18:Conselho Directivo"), "/directiveCouncil");
        x823.addChild(x1267);
        x1267.setNormalizedName(MultiLanguageString.importFromString("pt18:conselho-directivo"));
        x1267.setExecutionPath("/directiveCouncil/index.do");
        x1267.setVisible(true);
        Functionality x1266 = new Functionality(MultiLanguageString.importFromString("pt18:Conselho Directivo"));
        x1267.addChild(x1266);
        x1266.setTitle(MultiLanguageString.importFromString("pt18:Conselho Directivo"));
        x1266.setNormalizedName(MultiLanguageString.importFromString("pt18:conselho-directivo"));
        x1266.setExecutionPath("/index.do");
        x1266.setVisible(true);
        ExpressionGroupAvailability x1639 = new ExpressionGroupAvailability(x1267, "role(DIRECTIVE_COUNCIL)");
        x1639.setTargetGroup(Group.fromString("role(DIRECTIVE_COUNCIL)"));
        x1267.setAvailabilityPolicy(x1639);
        Module x1270 = new Module(MultiLanguageString.importFromString("pt8:Delegado"), "/delegate");
        x823.addChild(x1270);
        x1270.setNormalizedName(MultiLanguageString.importFromString("pt8:delegado"));
        x1270.setExecutionPath("/delegate/index.do");
        x1270.setVisible(true);
        Functionality x1269 = new Functionality(MultiLanguageString.importFromString("pt8:Delegado"));
        x1270.addChild(x1269);
        x1269.setTitle(MultiLanguageString.importFromString("pt8:Delegado"));
        x1269.setNormalizedName(MultiLanguageString.importFromString("pt8:delegado"));
        x1269.setExecutionPath("/index.do");
        x1269.setVisible(true);
        ExpressionGroupAvailability x1640 = new ExpressionGroupAvailability(x1270, "role(DELEGATE)");
        x1640.setTargetGroup(Group.fromString("role(DELEGATE)"));
        x1270.setAvailabilityPolicy(x1640);
        Module x1273 = new Module(MultiLanguageString.importFromString("pt9:Projectos"), "/projectsManagement");
        x823.addChild(x1273);
        x1273.setNormalizedName(MultiLanguageString.importFromString("pt9:projectos"));
        x1273.setExecutionPath("/projectsManagement/index.do");
        x1273.setVisible(true);
        Functionality x1272 = new Functionality(MultiLanguageString.importFromString("pt9:Projectos"));
        x1273.addChild(x1272);
        x1272.setTitle(MultiLanguageString.importFromString("pt9:Projectos"));
        x1272.setNormalizedName(MultiLanguageString.importFromString("pt9:projectos"));
        x1272.setExecutionPath("/index.do?backendInstance=IST");
        x1272.setVisible(true);
        ExpressionGroupAvailability x1641 = new ExpressionGroupAvailability(x1273, "role(PROJECTS_MANAGER)");
        x1641.setTargetGroup(Group.fromString("role(PROJECTS_MANAGER)"));
        x1273.setAvailabilityPolicy(x1641);
        Module x1276 =
                new Module(MultiLanguageString.importFromString("pt24:Projectos Institucionais"),
                        "/institucionalProjectsManagement");
        x823.addChild(x1276);
        x1276.setNormalizedName(MultiLanguageString.importFromString("pt24:projectos-institucionais"));
        x1276.setExecutionPath("/institucionalProjectsManagement/institucionalProjectIndex.do");
        x1276.setVisible(true);
        Functionality x1275 = new Functionality(MultiLanguageString.importFromString("pt24:Projectos Institucionais"));
        x1276.addChild(x1275);
        x1275.setTitle(MultiLanguageString.importFromString("pt24:Projectos Institucionais"));
        x1275.setNormalizedName(MultiLanguageString.importFromString("pt24:projectos-institucionais"));
        x1275.setExecutionPath("/institucionalProjectIndex.do?backendInstance=IST");
        x1275.setVisible(true);
        ExpressionGroupAvailability x1642 = new ExpressionGroupAvailability(x1276, "role(INSTITUCIONAL_PROJECTS_MANAGER)");
        x1642.setTargetGroup(Group.fromString("role(INSTITUCIONAL_PROJECTS_MANAGER)"));
        x1276.setAvailabilityPolicy(x1642);
        Module x1279 = new Module(MultiLanguageString.importFromString("pt7:Bolonha"), "/bolonhaManager");
        x823.addChild(x1279);
        x1279.setNormalizedName(MultiLanguageString.importFromString("pt7:bolonha"));
        x1279.setExecutionPath("/bolonhaManager/index.do");
        x1279.setVisible(true);
        Functionality x1278 = new Functionality(MultiLanguageString.importFromString("pt7:Bolonha"));
        x1279.addChild(x1278);
        x1278.setTitle(MultiLanguageString.importFromString("pt7:Bolonha"));
        x1278.setNormalizedName(MultiLanguageString.importFromString("pt7:bolonha"));
        x1278.setExecutionPath("/index.do");
        x1278.setVisible(true);
        ExpressionGroupAvailability x1643 = new ExpressionGroupAvailability(x1279, "role(BOLONHA_MANAGER)");
        x1643.setTargetGroup(Group.fromString("role(BOLONHA_MANAGER)"));
        x1279.setAvailabilityPolicy(x1643);
        Module x1282 = new Module(MultiLanguageString.importFromString("pt17:Gestão de Espaços"), "/SpaceManager");
        x823.addChild(x1282);
        x1282.setNormalizedName(MultiLanguageString.importFromString("pt17:gestao-de-espacos"));
        x1282.setExecutionPath("/SpaceManager/index.do");
        x1282.setVisible(true);
        Functionality x1281 = new Functionality(MultiLanguageString.importFromString("pt17:Gestão de Espaços"));
        x1282.addChild(x1281);
        x1281.setTitle(MultiLanguageString.importFromString("pt17:Gestão de Espaços"));
        x1281.setNormalizedName(MultiLanguageString.importFromString("pt17:gestao-de-espacos"));
        x1281.setExecutionPath("/index.do");
        x1281.setVisible(true);
        ExpressionGroupAvailability x1644 = new ExpressionGroupAvailability(x1282, "role(SPACE_MANAGER)");
        x1644.setTargetGroup(Group.fromString("role(SPACE_MANAGER)"));
        x1282.setAvailabilityPolicy(x1644);
        Module x1285 = new Module(MultiLanguageString.importFromString("pt12:Investigação"), "/researcher");
        x823.addChild(x1285);
        x1285.setNormalizedName(MultiLanguageString.importFromString("pt12:investigacao"));
        x1285.setExecutionPath("/researcher/index.do");
        x1285.setVisible(true);
        Functionality x1284 = new Functionality(MultiLanguageString.importFromString("pt12:Investigação"));
        x1285.addChild(x1284);
        x1284.setTitle(MultiLanguageString.importFromString("pt12:Investigação"));
        x1284.setNormalizedName(MultiLanguageString.importFromString("pt12:investigacao"));
        x1284.setExecutionPath("/index.do");
        x1284.setVisible(true);
        Functionality x1286 = new Functionality(MultiLanguageString.importFromString("pt21:Avaliação de Docentes"));
        x1285.addChild(x1286);
        x1286.setNormalizedName(MultiLanguageString.importFromString("pt21:avaliacao-de-docentes"));
        x1286.setExecutionPath("/teacherEvaluation.do?method=viewManagementInterface");
        ExpressionGroupAvailability x1645 = new ExpressionGroupAvailability(x1286, "role(MANAGER)");
        x1645.setTargetGroup(Group.fromString("role(MANAGER)"));
        x1286.setAvailabilityPolicy(x1645);
        ExpressionGroupAvailability x1646 = new ExpressionGroupAvailability(x1285, "role(RESEARCHER)");
        x1646.setTargetGroup(Group.fromString("role(RESEARCHER)"));
        x1285.setAvailabilityPolicy(x1646);
        Module x1289 = new Module(MultiLanguageString.importFromString("pt6:Alumni"), "/alumni");
        x823.addChild(x1289);
        x1289.setNormalizedName(MultiLanguageString.importFromString("pt6:alumni"));
        x1289.setExecutionPath("/alumni/index.do");
        x1289.setVisible(true);
        Functionality x1288 = new Functionality(MultiLanguageString.importFromString("pt6:Alumni"));
        x1289.addChild(x1288);
        x1288.setTitle(MultiLanguageString.importFromString("pt6:Alumni"));
        x1288.setNormalizedName(MultiLanguageString.importFromString("pt6:alumni"));
        x1288.setExecutionPath("/index.do");
        x1288.setVisible(true);
        ExpressionGroupAvailability x1647 = new ExpressionGroupAvailability(x1289, "role(ALUMNI)");
        x1647.setTargetGroup(Group.fromString("role(ALUMNI)"));
        x1289.setAvailabilityPolicy(x1647);
        Module x1292 = new Module(MultiLanguageString.importFromString("pt19:Conselho Pedagógico"), "/pedagogicalCouncil");
        x823.addChild(x1292);
        x1292.setNormalizedName(MultiLanguageString.importFromString("pt19:conselho-pedagogico"));
        x1292.setExecutionPath("/pedagogicalCouncil/index.do");
        x1292.setVisible(true);
        Functionality x1291 = new Functionality(MultiLanguageString.importFromString("pt19:Conselho Pedagógico"));
        x1292.addChild(x1291);
        x1291.setTitle(MultiLanguageString.importFromString("pt19:Conselho Pedagógico"));
        x1291.setNormalizedName(MultiLanguageString.importFromString("pt19:conselho-pedagogico"));
        x1291.setExecutionPath("/index.do");
        x1291.setVisible(true);
        ExpressionGroupAvailability x1648 = new ExpressionGroupAvailability(x1292, "role(PEDAGOGICAL_COUNCIL)");
        x1648.setTargetGroup(Group.fromString("role(PEDAGOGICAL_COUNCIL)"));
        x1292.setAvailabilityPolicy(x1648);
        Module x1295 = new Module(MultiLanguageString.importFromString("pt19:Portal do Candidato"), "/candidate");
        x823.addChild(x1295);
        x1295.setNormalizedName(MultiLanguageString.importFromString("pt19:portal-do-candidato"));
        x1295.setExecutionPath("/candidate/index.do");
        x1295.setVisible(true);
        Functionality x1294 = new Functionality(MultiLanguageString.importFromString("pt19:Portal do Candidato"));
        x1295.addChild(x1294);
        x1294.setTitle(MultiLanguageString.importFromString("pt19:Portal do Candidato"));
        x1294.setNormalizedName(MultiLanguageString.importFromString("pt19:portal-do-candidato"));
        x1294.setExecutionPath("/index.do");
        x1294.setVisible(true);
        ExpressionGroupAvailability x1649 = new ExpressionGroupAvailability(x1295, "role(CANDIDATE)");
        x1649.setTargetGroup(Group.fromString("role(CANDIDATE)"));
        x1295.setAvailabilityPolicy(x1649);
        Module x1298 = new Module(MultiLanguageString.importFromString("en9:Messagingpt11:Comunicação"), "/messaging");
        x823.addChild(x1298);
        x1298.setNormalizedName(MultiLanguageString.importFromString("en9:messagingpt11:comunicacao"));
        x1298.setExecutionPath("/messaging/index.do");
        x1298.setVisible(true);
        Functionality x1297 = new Functionality(MultiLanguageString.importFromString("pt11:Comunicação"));
        x1298.addChild(x1297);
        x1297.setTitle(MultiLanguageString.importFromString("pt11:Comunicação"));
        x1297.setNormalizedName(MultiLanguageString.importFromString("pt11:comunicacao"));
        x1297.setExecutionPath("/index.do");
        x1297.setVisible(true);
        ExpressionGroupAvailability x1650 = new ExpressionGroupAvailability(x1298, "role(PERSON)");
        x1650.setTargetGroup(Group.fromString("role(PERSON)"));
        x1298.setAvailabilityPolicy(x1650);
        Module x1303 = new Module(MultiLanguageString.importFromString("pt7:Público"), "/publico");
        x823.addChild(x1303);
        x1303.setNormalizedName(MultiLanguageString.importFromString("pt7:publico"));
        x1303.setExecutionPath("");
        x1303.setVisible(false);
        Module x1302 = new Module(MultiLanguageString.importFromString("pt5:Sites"), "/");
        x1303.addChild(x1302);
        x1302.setNormalizedName(MultiLanguageString.importFromString("pt5:sites"));
        x1302.setExecutionPath("");
        x1302.setVisible(false);
        Module x1301 = new Module(MultiLanguageString.importFromString("pt19:Disciplina Execução"), "/");
        x1302.addChild(x1301);
        x1301.setNormalizedName(MultiLanguageString.importFromString("pt19:disciplina-execucao"));
        x1301.setExecutionPath("");
        x1301.setVisible(false);
        Functionality x1300 =
                new Functionality(MultiLanguageString.importFromString("en12:Initial Pageen8:Homepagept14:Página Inicial"));
        x1301.addChild(x1300);
        x1300.setNormalizedName(MultiLanguageString.importFromString("en12:initial-pageen8:homepagept14:pagina-inicial"));
        x1300.setExecutionPath("/executionCourse.do?method=firstPage");
        x1300.setVisible(true);
        Functionality x1304 = new Functionality(MultiLanguageString.importFromString("en13:Announcementspt8:Anuncios"));
        x1301.addChild(x1304);
        x1304.setNormalizedName(MultiLanguageString.importFromString("en13:announcementspt8:anuncios"));
        x1304.setExecutionPath("/announcementManagement.do?method=start");
        x1304.setVisible(true);
        Functionality x1305 = new Functionality(MultiLanguageString.importFromString("en8:Planningpt11:Planeamento"));
        x1301.addChild(x1305);
        x1305.setNormalizedName(MultiLanguageString.importFromString("en8:planningpt11:planeamento"));
        x1305.setExecutionPath("/executionCourse.do?method=lessonPlannings");
        x1305.setVisible(true);
        Functionality x1306 = new Functionality(MultiLanguageString.importFromString("en9:Summariespt8:Sumários"));
        x1301.addChild(x1306);
        x1306.setNormalizedName(MultiLanguageString.importFromString("en9:summariespt8:sumarios"));
        x1306.setExecutionPath("/executionCourse.do?method=summaries");
        x1306.setVisible(true);
        Functionality x1307 = new Functionality(MultiLanguageString.importFromString("en10:Objectivespt10:Objectivos"));
        x1301.addChild(x1307);
        x1307.setNormalizedName(MultiLanguageString.importFromString("en10:objectivespt10:objectivos"));
        x1307.setExecutionPath("/executionCourse.do?method=objectives");
        x1307.setVisible(true);
        Functionality x1308 = new Functionality(MultiLanguageString.importFromString("en7:Programpt8:Programa"));
        x1301.addChild(x1308);
        x1308.setNormalizedName(MultiLanguageString.importFromString("en7:programpt8:programa"));
        x1308.setExecutionPath("/executionCourse.do?method=program");
        x1308.setVisible(true);
        Functionality x1309 =
                new Functionality(MultiLanguageString.importFromString("en17:Evaluation Methodpt19:Método de Avaliação"));
        x1301.addChild(x1309);
        x1309.setNormalizedName(MultiLanguageString.importFromString("en17:evaluation-methodpt19:metodo-de-avaliacao"));
        x1309.setExecutionPath("/executionCourse.do?method=evaluationMethod");
        x1309.setVisible(true);
        Functionality x1310 = new Functionality(MultiLanguageString.importFromString("en12:Bibliographypt12:Bibliografia"));
        x1301.addChild(x1310);
        x1310.setNormalizedName(MultiLanguageString.importFromString("en12:bibliographypt12:bibliografia"));
        x1310.setExecutionPath("/executionCourse.do?method=bibliographicReference");
        x1310.setVisible(true);
        Functionality x1311 = new Functionality(MultiLanguageString.importFromString("en9:Timesheetpt7:Horário"));
        x1301.addChild(x1311);
        x1311.setNormalizedName(MultiLanguageString.importFromString("en9:timesheetpt7:horario"));
        x1311.setExecutionPath("/executionCourse.do?method=schedule");
        x1311.setVisible(true);
        Functionality x1312 = new Functionality(MultiLanguageString.importFromString("en6:Shiftspt6:Turnos"));
        x1301.addChild(x1312);
        x1312.setNormalizedName(MultiLanguageString.importFromString("en6:shiftspt6:turnos"));
        x1312.setExecutionPath("/executionCourse.do?method=shifts");
        x1312.setVisible(true);
        Functionality x1313 = new Functionality(MultiLanguageString.importFromString("en10:Evaluationpt9:Avaliaçâo"));
        x1301.addChild(x1313);
        x1313.setNormalizedName(MultiLanguageString.importFromString("en10:evaluationpt9:avaliacao"));
        x1313.setExecutionPath("/executionCourse.do?method=evaluations");
        x1313.setVisible(true);
        Functionality x1314 = new Functionality(MultiLanguageString.importFromString("en6:Groupspt12:Agrupamentos"));
        x1301.addChild(x1314);
        x1314.setNormalizedName(MultiLanguageString.importFromString("en6:groupspt12:agrupamentos"));
        x1314.setExecutionPath("/executionCourse.do?method=groupings");
        x1314.setVisible(true);
        Functionality x1315 = new Functionality(MultiLanguageString.importFromString("pt10:Ver Secção"));
        x1301.addChild(x1315);
        x1315.setNormalizedName(MultiLanguageString.importFromString("pt10:ver-seccao"));
        x1315.setExecutionPath("/executionCourse.do?method=section");
        x1315.setVisible(false);
        Functionality x1316 =
                new Functionality(MultiLanguageString.importFromString("en15:Search Contentspt21:Pesquisa de Conteúdos"));
        x1301.addChild(x1316);
        x1316.setTitle(MultiLanguageString.importFromString("en15:Search Contentspt19:Pesquisar Conteúdos"));
        x1316.setNormalizedName(MultiLanguageString.importFromString("en15:search-contentspt21:pesquisa-de-conteudos"));
        x1316.setExecutionPath("/searchFileContent.do?method=prepareSearchForExecutionCourse");
        x1316.setVisible(true);
        Functionality x1317 = new Functionality(MultiLanguageString.importFromString("pt14:Resultados QUC"));
        x1301.addChild(x1317);
        x1317.setTitle(MultiLanguageString.importFromString("pt14:Resultados QUC"));
        x1317.setNormalizedName(MultiLanguageString.importFromString("pt14:resultados-quc"));
        x1317.setExecutionPath("/executionCourse.do?method=studentInquiriesResults");
        Module x1320 = new Module(MultiLanguageString.importFromString("pt12:Site Pessoal"), "/");
        x1302.addChild(x1320);
        x1320.setNormalizedName(MultiLanguageString.importFromString("pt12:site-pessoal"));
        x1320.setExecutionPath("");
        x1320.setVisible(true);
        Functionality x1319 = new Functionality(MultiLanguageString.importFromString("en12:Presentationpt12:Apresentação"));
        x1320.addChild(x1319);
        x1319.setTitle(MultiLanguageString.importFromString("en12:Presentationpt12:Apresentação"));
        x1319.setNormalizedName(MultiLanguageString.importFromString("en12:presentationpt12:apresentacao"));
        x1319.setExecutionPath("/viewHomepage.do?method=show");
        x1319.setVisible(true);
        Functionality x1321 =
                new Functionality(MultiLanguageString.importFromString("en18:Research Interestspt22:Interesses Científicos"));
        x1320.addChild(x1321);
        x1321.setTitle(MultiLanguageString.importFromString("en18:Research Interestspt10:Interesses"));
        x1321.setNormalizedName(MultiLanguageString.importFromString("en18:research-interestspt22:interesses-cientificos"));
        x1321.setExecutionPath("/showInterests.do?method=showInterests");
        x1321.setVisible(true);
        Functionality x1322 = new Functionality(MultiLanguageString.importFromString("en12:Publicationspt11:Publicações"));
        x1320.addChild(x1322);
        x1322.setTitle(MultiLanguageString.importFromString("en12:Publicationspt11:Publicações"));
        x1322.setNormalizedName(MultiLanguageString.importFromString("en12:publicationspt11:publicacoes"));
        x1322.setExecutionPath("/showPublications.do?method=showPublications");
        x1322.setVisible(true);
        Functionality x1323 = new Functionality(MultiLanguageString.importFromString("en7:Patentspt8:Patentes"));
        x1320.addChild(x1323);
        x1323.setTitle(MultiLanguageString.importFromString("en7:Patentspt8:Patentes"));
        x1323.setNormalizedName(MultiLanguageString.importFromString("en7:patentspt8:patentes"));
        x1323.setExecutionPath("/showPatents.do?method=showPatents");
        x1323.setVisible(true);
        Functionality x1324 =
                new Functionality(
                        MultiLanguageString.importFromString("en23:Scientifical Activitiespt23:Actividades Científicas"));
        x1320.addChild(x1324);
        x1324.setTitle(MultiLanguageString.importFromString("en23:Scientifical Activitiespt23:Actividades Científicas"));
        x1324.setNormalizedName(MultiLanguageString.importFromString("en23:scientifical-activitiespt23:actividades-cientificas"));
        x1324.setExecutionPath("/showParticipations.do?method=showParticipations");
        x1324.setVisible(true);
        Functionality x1325 = new Functionality(MultiLanguageString.importFromString("en6:Prizespt7:Prémios"));
        x1320.addChild(x1325);
        x1325.setTitle(MultiLanguageString.importFromString("en6:Prizespt7:Prémios"));
        x1325.setNormalizedName(MultiLanguageString.importFromString("en6:prizespt7:premios"));
        x1325.setExecutionPath("/showPrizes.do?method=showPrizes");
        x1325.setVisible(true);
        Module x1328 = new Module(MultiLanguageString.importFromString("en6:Degreept5:Curso"), "/");
        x1302.addChild(x1328);
        x1328.setNormalizedName(MultiLanguageString.importFromString("en6:degreept5:curso"));
        x1328.setExecutionPath("");
        x1328.setVisible(true);
        Functionality x1327 = new Functionality(MultiLanguageString.importFromString("en11:Descriptionpt9:Descrição"));
        x1328.addChild(x1327);
        x1327.setNormalizedName(MultiLanguageString.importFromString("en11:descriptionpt9:descricao"));
        x1327.setExecutionPath("/showDegreeSite.do?method=showDescription");
        x1327.setVisible(true);
        Functionality x1329 = new Functionality(MultiLanguageString.importFromString("en13:Announcementspt8:Anuncios"));
        x1328.addChild(x1329);
        x1329.setNormalizedName(MultiLanguageString.importFromString("en13:announcementspt8:anuncios"));
        x1329.setExecutionPath("/showDegreeAnnouncements.do?method=viewAnnouncements");
        x1329.setVisible(true);
        Functionality x1330 =
                new Functionality(MultiLanguageString.importFromString("en22:Admission Requirementspt16:Regime de Acesso"));
        x1328.addChild(x1330);
        x1330.setNormalizedName(MultiLanguageString.importFromString("en22:admission-requirementspt16:regime-de-acesso"));
        x1330.setExecutionPath("/showDegreeSite.do?method=showAccessRequirements");
        x1330.setVisible(true);
        Functionality x1331 =
                new Functionality(MultiLanguageString.importFromString("en19:Professional Statuspt21:Estatuto Profissional"));
        x1328.addChild(x1331);
        x1331.setNormalizedName(MultiLanguageString.importFromString("en19:professional-statuspt21:estatuto-profissional"));
        x1331.setExecutionPath("/showDegreeSite.do?method=showProfessionalStatus");
        x1331.setVisible(true);
        Functionality x1332 =
                new Functionality(MultiLanguageString.importFromString("en15:Curricular Planpt16:Plano Curricular"));
        x1328.addChild(x1332);
        x1332.setNormalizedName(MultiLanguageString.importFromString("en15:curricular-planpt16:plano-curricular"));
        x1332.setExecutionPath("/showDegreeSite.do?method=showCurricularPlan");
        x1332.setVisible(true);
        Functionality x1333 =
                new Functionality(MultiLanguageString.importFromString("en16:Course Web Pagespt22:Páginas de Disciplinas"));
        x1328.addChild(x1333);
        x1333.setNormalizedName(MultiLanguageString.importFromString("en16:course-web-pagespt22:paginas-de-disciplinas"));
        x1333.setExecutionPath("/showExecutionCourseSites.do?method=listSites&showTwoSemesters=true");
        x1333.setVisible(true);
        Functionality x1334 =
                new Functionality(MultiLanguageString.importFromString("en15:Class timetablept18:Horários por Turma"));
        x1328.addChild(x1334);
        x1334.setNormalizedName(MultiLanguageString.importFromString("en15:class-timetablept18:horarios-por-turma"));
        x1334.setExecutionPath("/showClasses.do?method=listClasses");
        x1334.setVisible(true);
        Functionality x1335 = new Functionality(MultiLanguageString.importFromString("en11:Evaluationspt10:Avaliações"));
        x1328.addChild(x1335);
        x1335.setNormalizedName(MultiLanguageString.importFromString("en11:evaluationspt10:avaliacoes"));
        x1335.setExecutionPath("/degreeSite/publicEvaluations.faces");
        x1335.setVisible(true);
        Functionality x1336 = new Functionality(MultiLanguageString.importFromString("en13:Dissertationspt12:Dissertações"));
        x1328.addChild(x1336);
        x1336.setNormalizedName(MultiLanguageString.importFromString("en13:dissertationspt12:dissertacoes"));
        x1336.setExecutionPath("/showDegreeTheses.do?method=showTheses");
        x1336.setVisible(true);
        Module x1339 = new Module(MultiLanguageString.importFromString("pt12:Departamento"), "/department");
        x1302.addChild(x1339);
        x1339.setNormalizedName(MultiLanguageString.importFromString("pt12:departamento"));
        x1339.setExecutionPath("");
        x1339.setVisible(true);
        Functionality x1338 = new Functionality(MultiLanguageString.importFromString("en13:Announcementspt8:Anuncios"));
        x1339.addChild(x1338);
        x1338.setNormalizedName(MultiLanguageString.importFromString("en13:announcementspt8:anuncios"));
        x1338.setExecutionPath("/announcements.do?method=viewAnnouncements");
        x1338.setVisible(true);
        Functionality x1340 = new Functionality(MultiLanguageString.importFromString("en7:Facultypt8:Docentes"));
        x1339.addChild(x1340);
        x1340.setNormalizedName(MultiLanguageString.importFromString("en7:facultypt8:docentes"));
        x1340.setExecutionPath("/teachers.do");
        x1340.setVisible(true);
        Functionality x1341 = new Functionality(MultiLanguageString.importFromString("en5:Staffpt12:Funcionários"));
        x1339.addChild(x1341);
        x1341.setNormalizedName(MultiLanguageString.importFromString("en5:staffpt12:funcionarios"));
        x1341.setExecutionPath("/departmentEmployees.do");
        x1341.setVisible(true);
        Functionality x1342 = new Functionality(MultiLanguageString.importFromString("en7:Coursespt11:Disciplinas"));
        x1339.addChild(x1342);
        x1342.setNormalizedName(MultiLanguageString.importFromString("en7:coursespt11:disciplinas"));
        x1342.setExecutionPath("/showDepartmentCompetenceCourses.faces");
        x1342.setVisible(true);
        Functionality x1343 = new Functionality(MultiLanguageString.importFromString("en6:Eventspt7:Eventos"));
        x1339.addChild(x1343);
        x1343.setNormalizedName(MultiLanguageString.importFromString("en6:eventspt7:eventos"));
        x1343.setExecutionPath("/events.do?method=viewAnnouncements");
        x1343.setVisible(true);
        Functionality x1344 = new Functionality(MultiLanguageString.importFromString("en7:Degreespt6:Cursos"));
        x1339.addChild(x1344);
        x1344.setNormalizedName(MultiLanguageString.importFromString("en7:degreespt6:cursos"));
        x1344.setExecutionPath("/degrees.do");
        x1344.setVisible(true);
        Functionality x1345 = new Functionality(MultiLanguageString.importFromString("en12:Organizationpt9:Estrutura"));
        x1339.addChild(x1345);
        x1345.setNormalizedName(MultiLanguageString.importFromString("en12:organizationpt9:estrutura"));
        x1345.setExecutionPath("/departmentSite.do?method=organization");
        x1345.setVisible(true);
        Functionality x1346 =
                new Functionality(MultiLanguageString.importFromString("en16:Scientific Areaspt17:Áreas Científicas"));
        x1339.addChild(x1346);
        x1346.setNormalizedName(MultiLanguageString.importFromString("en16:scientific-areaspt17:areas-cientificas"));
        x1346.setExecutionPath("/departmentSite.do?method=subunits");
        x1346.setVisible(true);
        Functionality x1347 = new Functionality(MultiLanguageString.importFromString("en13:Dissertationspt12:Dissertações"));
        x1339.addChild(x1347);
        x1347.setNormalizedName(MultiLanguageString.importFromString("en13:dissertationspt12:dissertacoes"));
        x1347.setExecutionPath("/theses.do?method=showTheses");
        x1347.setVisible(true);
        Functionality x1348 = new Functionality(MultiLanguageString.importFromString("en12:Publicationspt11:Publicações"));
        x1339.addChild(x1348);
        x1348.setTitle(MultiLanguageString.importFromString("en12:Publicationspt11:Publicações"));
        x1348.setNormalizedName(MultiLanguageString.importFromString("en12:publicationspt11:publicacoes"));
        x1348.setExecutionPath("/departmentSite.do?method=showPublications");
        x1348.setVisible(true);
        Functionality x1349 = new Functionality(MultiLanguageString.importFromString("en4:Homept6:Início"));
        x1339.addChild(x1349);
        x1349.setTitle(MultiLanguageString.importFromString("pt6:Início"));
        x1349.setNormalizedName(MultiLanguageString.importFromString("en4:homept6:inicio"));
        x1349.setExecutionPath("/departmentSite.do?method=presentation");
        x1349.setVisible(true);
        Module x1352 =
                new Module(MultiLanguageString.importFromString("en13:Research Sitept20:Site de Investigação"), "/researchSite");
        x1302.addChild(x1352);
        x1352.setNormalizedName(MultiLanguageString.importFromString("en13:research-sitept20:site-de-investigacao"));
        x1352.setExecutionPath("");
        x1352.setVisible(true);
        Functionality x1351 = new Functionality(MultiLanguageString.importFromString("en6:Eventspt7:Eventos"));
        x1352.addChild(x1351);
        x1351.setTitle(MultiLanguageString.importFromString("en6:Eventspt7:Eventos"));
        x1351.setNormalizedName(MultiLanguageString.importFromString("en6:eventspt7:eventos"));
        x1351.setExecutionPath("/manageResearchUnitAnnouncements.do?method=viewEvents");
        x1351.setVisible(true);
        Functionality x1353 = new Functionality(MultiLanguageString.importFromString("en13:Announcementspt8:Anuncios"));
        x1352.addChild(x1353);
        x1353.setTitle(MultiLanguageString.importFromString("en13:Announcementspt8:Anuncios"));
        x1353.setNormalizedName(MultiLanguageString.importFromString("en13:announcementspt8:anuncios"));
        x1353.setExecutionPath("/manageResearchUnitAnnouncements.do?method=viewAnnouncements");
        x1353.setVisible(true);
        Functionality x1354 = new Functionality(MultiLanguageString.importFromString("en7:Memberspt7:Membros"));
        x1352.addChild(x1354);
        x1354.setTitle(MultiLanguageString.importFromString("en7:Memberspt7:Membros"));
        x1354.setNormalizedName(MultiLanguageString.importFromString("en7:memberspt7:membros"));
        x1354.setExecutionPath("/viewResearchUnitSite.do?method=showResearchers");
        x1354.setVisible(true);
        Functionality x1355 = new Functionality(MultiLanguageString.importFromString("en12:Publicationspt11:Publicações"));
        x1352.addChild(x1355);
        x1355.setTitle(MultiLanguageString.importFromString("en12:Publicationspt11:Publicações"));
        x1355.setNormalizedName(MultiLanguageString.importFromString("en12:publicationspt11:publicacoes"));
        x1355.setExecutionPath("/viewResearchUnitSite.do?method=showPublications");
        x1355.setVisible(true);
        Functionality x1356 = new Functionality(MultiLanguageString.importFromString("en12:Organizationpt9:Estrutura"));
        x1352.addChild(x1356);
        x1356.setNormalizedName(MultiLanguageString.importFromString("en12:organizationpt9:estrutura"));
        x1356.setExecutionPath("/viewResearchUnitSite.do?method=organization");
        x1356.setVisible(true);
        Functionality x1357 = new Functionality(MultiLanguageString.importFromString("en8:Subunitspt11:Subunidades"));
        x1352.addChild(x1357);
        x1357.setNormalizedName(MultiLanguageString.importFromString("en8:subunitspt11:subunidades"));
        x1357.setExecutionPath("/viewResearchUnitSite.do?method=subunits");
        x1357.setVisible(true);
        Functionality x1358 = new Functionality(MultiLanguageString.importFromString("en4:Homept6:Início"));
        x1352.addChild(x1358);
        x1358.setTitle(MultiLanguageString.importFromString("pt6:Início"));
        x1358.setNormalizedName(MultiLanguageString.importFromString("en4:homept6:inicio"));
        x1358.setExecutionPath("/viewResearchUnitSite.do?method=presentation");
        x1358.setVisible(true);
        Module x1361 = new Module(MultiLanguageString.importFromString("pt19:Conselho Pedagógico"), "/pedagogicalCouncil");
        x1302.addChild(x1361);
        x1361.setNormalizedName(MultiLanguageString.importFromString("pt19:conselho-pedagogico"));
        x1361.setExecutionPath("");
        x1361.setVisible(true);
        Functionality x1360 = new Functionality(MultiLanguageString.importFromString("en12:Organizationpt9:Estrutura"));
        x1361.addChild(x1360);
        x1360.setNormalizedName(MultiLanguageString.importFromString("en12:organizationpt9:estrutura"));
        x1360.setExecutionPath("/viewSite.do?method=organization");
        x1360.setVisible(true);
        Functionality x1362 = new Functionality(MultiLanguageString.importFromString("pt6:Início"));
        x1361.addChild(x1362);
        x1362.setTitle(MultiLanguageString.importFromString("pt6:Início"));
        x1362.setNormalizedName(MultiLanguageString.importFromString("pt6:inicio"));
        x1362.setExecutionPath("/viewSite.do?method=presentation");
        x1362.setVisible(true);
        Module x1365 = new Module(MultiLanguageString.importFromString("pt19:Conselho Científico"), "/scientificCouncil");
        x1302.addChild(x1365);
        x1365.setNormalizedName(MultiLanguageString.importFromString("pt19:conselho-cientifico"));
        x1365.setExecutionPath("");
        x1365.setVisible(true);
        Functionality x1364 = new Functionality(MultiLanguageString.importFromString("en12:Organizationpt9:Estrutura"));
        x1365.addChild(x1364);
        x1364.setNormalizedName(MultiLanguageString.importFromString("en12:organizationpt9:estrutura"));
        x1364.setExecutionPath("/viewSite.do?method=organization");
        x1364.setVisible(true);
        Functionality x1366 = new Functionality(MultiLanguageString.importFromString("pt6:Início"));
        x1365.addChild(x1366);
        x1366.setTitle(MultiLanguageString.importFromString("pt6:Início"));
        x1366.setNormalizedName(MultiLanguageString.importFromString("pt6:inicio"));
        x1366.setExecutionPath("/viewSite.do?method=presentation");
        x1366.setVisible(true);
        Module x1369 =
                new Module(MultiLanguageString.importFromString("en14:ScientificAreapt15:Área Científica"), "/scientificArea");
        x1302.addChild(x1369);
        x1369.setNormalizedName(MultiLanguageString.importFromString("en14:scientificareapt15:area-cientifica"));
        x1369.setExecutionPath("");
        x1369.setVisible(true);
        Functionality x1368 = new Functionality(MultiLanguageString.importFromString("en12:Publicationspt11:Publicações"));
        x1369.addChild(x1368);
        x1368.setNormalizedName(MultiLanguageString.importFromString("en12:publicationspt11:publicacoes"));
        x1368.setExecutionPath("/viewSite.do?method=showPublications");
        x1368.setVisible(true);
        Functionality x1370 = new Functionality(MultiLanguageString.importFromString("en6:Eventspt7:Eventos"));
        x1369.addChild(x1370);
        x1370.setNormalizedName(MultiLanguageString.importFromString("en6:eventspt7:eventos"));
        x1370.setExecutionPath("/events.do?method=viewAnnouncements");
        x1370.setVisible(true);
        Functionality x1371 = new Functionality(MultiLanguageString.importFromString("en13:Announcementspt8:Anuncios"));
        x1369.addChild(x1371);
        x1371.setNormalizedName(MultiLanguageString.importFromString("en13:announcementspt8:anuncios"));
        x1371.setExecutionPath("/announcements.do?method=viewAnnouncements");
        x1371.setVisible(true);
        Functionality x1372 = new Functionality(MultiLanguageString.importFromString("en12:Organizationpt11:Organização"));
        x1369.addChild(x1372);
        x1372.setNormalizedName(MultiLanguageString.importFromString("en12:organizationpt11:organizacao"));
        x1372.setExecutionPath("/viewSite.do?method=organization");
        x1372.setVisible(true);
        Functionality x1373 = new Functionality(MultiLanguageString.importFromString("en8:SubUnitspt11:SubUnidades"));
        x1369.addChild(x1373);
        x1373.setNormalizedName(MultiLanguageString.importFromString("en8:subunitspt11:subunidades"));
        x1373.setExecutionPath("/viewSite.do?method=subunits");
        x1373.setVisible(true);
        Functionality x1374 = new Functionality(MultiLanguageString.importFromString("en7:Facultypt8:Docentes"));
        x1369.addChild(x1374);
        x1374.setNormalizedName(MultiLanguageString.importFromString("en7:facultypt8:docentes"));
        x1374.setExecutionPath("/viewSite.do?method=viewTeachers");
        x1374.setVisible(true);
        Functionality x1375 = new Functionality(MultiLanguageString.importFromString("en5:Staffpt12:Funcionários"));
        x1369.addChild(x1375);
        x1375.setNormalizedName(MultiLanguageString.importFromString("en5:staffpt12:funcionarios"));
        x1375.setExecutionPath("/viewSite.do?method=viewEmployees");
        x1375.setVisible(true);
        Functionality x1376 = new Functionality(MultiLanguageString.importFromString("en7:Coursespt11:Disciplinas"));
        x1369.addChild(x1376);
        x1376.setNormalizedName(MultiLanguageString.importFromString("en7:coursespt11:disciplinas"));
        x1376.setExecutionPath("/viewSite.do?method=viewCourses");
        x1376.setVisible(true);
        Functionality x1377 = new Functionality(MultiLanguageString.importFromString("en4:Homept6:Início"));
        x1369.addChild(x1377);
        x1377.setTitle(MultiLanguageString.importFromString("pt6:Início"));
        x1377.setNormalizedName(MultiLanguageString.importFromString("en4:homept6:inicio"));
        x1377.setExecutionPath("/viewSite.do?method=presentation");
        x1377.setVisible(true);
        Module x1380 = new Module(MultiLanguageString.importFromString("pt8:Tutorado"), "/tutoring");
        x1302.addChild(x1380);
        x1380.setTitle(MultiLanguageString.importFromString("pt8:Tutorado"));
        x1380.setNormalizedName(MultiLanguageString.importFromString("pt8:tutorado"));
        Functionality x1379 = new Functionality(MultiLanguageString.importFromString("pt6:Início"));
        x1380.addChild(x1379);
        x1379.setTitle(MultiLanguageString.importFromString("pt6:Início"));
        x1379.setNormalizedName(MultiLanguageString.importFromString("pt6:inicio"));
        x1379.setExecutionPath("/viewSite.do?method=presentation");
        Module x1383 = new Module(MultiLanguageString.importFromString("pt9:Alunos CD"), "/studentsSite");
        x1302.addChild(x1383);
        x1383.setNormalizedName(MultiLanguageString.importFromString("pt9:alunos-cd"));
        Functionality x1382 = new Functionality(MultiLanguageString.importFromString("en8:Homepagept14:Página Inicial"));
        x1383.addChild(x1382);
        x1382.setTitle(MultiLanguageString.importFromString("en8:Homepagept14:Página Inicial"));
        x1382.setNormalizedName(MultiLanguageString.importFromString("en8:homepagept14:pagina-inicial"));
        x1382.setExecutionPath("viewSite.do?method=presentation");
        Module x1386 = new Module(MultiLanguageString.importFromString("pt12:Dissertacoes"), "/theses");
        x1302.addChild(x1386);
        x1386.setTitle(MultiLanguageString.importFromString("pt12:Dissertacoes"));
        x1386.setNormalizedName(MultiLanguageString.importFromString("pt12:dissertacoes"));
        Functionality x1385 = new Functionality(MultiLanguageString.importFromString("en6:Thesispt11:Dissertacao"));
        x1386.addChild(x1385);
        x1385.setTitle(MultiLanguageString.importFromString("en6:Thesispt11:Dissertacao"));
        x1385.setNormalizedName(MultiLanguageString.importFromString("en6:thesispt11:dissertacao"));
        x1385.setExecutionPath("thesis.do");
        Module x1389 = new Module(MultiLanguageString.importFromString("pt22:Assembleia Estatutária"), "/aestatutaria");
        x1302.addChild(x1389);
        x1389.setTitle(MultiLanguageString.importFromString("pt22:Assembleia Estatutária"));
        x1389.setNormalizedName(MultiLanguageString.importFromString("pt22:assembleia-estatutaria"));
        Functionality x1388 = new Functionality(MultiLanguageString.importFromString("pt6:Início"));
        x1389.addChild(x1388);
        x1388.setTitle(MultiLanguageString.importFromString("pt6:Início"));
        x1388.setNormalizedName(MultiLanguageString.importFromString("pt6:inicio"));
        x1388.setExecutionPath("/viewSite.do?method=presentation");
        Module x1392 = new Module(MultiLanguageString.importFromString("pt18:Conselho de Gestão"), "/managementCouncilSite");
        x1302.addChild(x1392);
        x1392.setTitle(MultiLanguageString.importFromString("pt18:Conselho de Gestão"));
        x1392.setNormalizedName(MultiLanguageString.importFromString("pt18:conselho-de-gestao"));
        Functionality x1391 = new Functionality(MultiLanguageString.importFromString("en4:Homept6:Início"));
        x1392.addChild(x1391);
        x1391.setTitle(MultiLanguageString.importFromString("en4:Homept6:Início"));
        x1391.setNormalizedName(MultiLanguageString.importFromString("en4:homept6:inicio"));
        x1391.setExecutionPath("/viewSite.do?method=presentation");
        Module x1395 = new Module(MultiLanguageString.importFromString("pt17:Sites de Unidades"), "/units");
        x1302.addChild(x1395);
        x1395.setTitle(MultiLanguageString.importFromString("pt17:Sites de Unidades"));
        x1395.setNormalizedName(MultiLanguageString.importFromString("pt17:sites-de-unidades"));
        Functionality x1394 = new Functionality(MultiLanguageString.importFromString("en4:Homept6:Início"));
        x1395.addChild(x1394);
        x1394.setTitle(MultiLanguageString.importFromString("en4:Homept6:Início"));
        x1394.setNormalizedName(MultiLanguageString.importFromString("en4:homept6:inicio"));
        x1394.setExecutionPath("/viewSite.do?method=presentation");
        Functionality x1398 = new Functionality(MultiLanguageString.importFromString("pt19:Pesquisa de Espaços"));
        x1303.addChild(x1398);
        x1398.setTitle(MultiLanguageString.importFromString("pt19:Pesquisa de Espaços"));
        x1398.setDescription(MultiLanguageString.importFromString("pt19:Pesquisa de Espaços"));
        x1398.setNormalizedName(MultiLanguageString.importFromString("pt19:pesquisa-de-espacos"));
        x1398.setExecutionPath("/findSpaces.do?method=prepareSearchSpaces");
        Functionality x1399 =
                new Functionality(MultiLanguageString.importFromString("pt40:Consulta de Corpo Docente por Disciplina"));
        x1303.addChild(x1399);
        x1399.setNormalizedName(MultiLanguageString.importFromString("pt40:consulta-de-corpo-docente-por-disciplina"));
        x1399.setExecutionPath("/searchProfessorships.do?method=prepareForm");
        Functionality x1400 = new Functionality(MultiLanguageString.importFromString("pt14:Registo Alumni"));
        x1303.addChild(x1400);
        x1400.setTitle(MultiLanguageString.importFromString("pt14:Registo Alumni"));
        x1400.setNormalizedName(MultiLanguageString.importFromString("pt14:registo-alumni"));
        x1400.setExecutionPath("/alumni.do?method=initFenixPublicAccess");
        Functionality x1401 = new Functionality(MultiLanguageString.importFromString("pt24:Conclusão Registo Alumni"));
        x1303.addChild(x1401);
        x1401.setTitle(MultiLanguageString.importFromString("pt24:Conclusão Registo Alumni"));
        x1401.setNormalizedName(MultiLanguageString.importFromString("pt24:conclusao-registo-alumni"));
        x1401.setExecutionPath("/alumni.do?method=innerFenixPublicAccessValidation");
        Module x1403 = new Module(MultiLanguageString.importFromString("pt8:Estagios"), "/");
        x1303.addChild(x1403);
        x1403.setTitle(MultiLanguageString.importFromString("pt8:Estagios"));
        x1403.setNormalizedName(MultiLanguageString.importFromString("pt8:estagios"));
        Functionality x1402 = new Functionality(MultiLanguageString.importFromString("pt12:Candidaturas"));
        x1403.addChild(x1402);
        x1402.setTitle(MultiLanguageString.importFromString("pt12:Candidaturas"));
        x1402.setNormalizedName(MultiLanguageString.importFromString("pt12:candidaturas"));
        x1402.setExecutionPath("/internship.do?method=prepareCandidacy");
        Functionality x1405 = new Functionality(MultiLanguageString.importFromString("pt26:Pesquisa de Resultados QUC"));
        x1303.addChild(x1405);
        x1405.setTitle(MultiLanguageString.importFromString("pt26:Pesquisa de Resultados QUC"));
        x1405.setNormalizedName(MultiLanguageString.importFromString("pt26:pesquisa-de-resultados-quc"));
        x1405.setExecutionPath("/searchInquiriesResultPage.do?method=prepare");
        Functionality x1406 = new Functionality(MultiLanguageString.importFromString("en5:filespt9:ficheiros"));
        x1303.addChild(x1406);
        x1406.setTitle(MultiLanguageString.importFromString("en5:filespt9:ficheiros"));
        x1406.setNormalizedName(MultiLanguageString.importFromString("en5:filespt9:ficheiros"));
        x1406.setExecutionPath("/files.do");
        Module x1408 = new Module(MultiLanguageString.importFromString("pt12:Candidaturas"), "/");
        x1303.addChild(x1408);
        x1408.setTitle(MultiLanguageString.importFromString("pt11:Candidatura"));
        x1408.setNormalizedName(MultiLanguageString.importFromString("pt12:candidaturas"));
        Functionality x1407 = new Functionality(MultiLanguageString.importFromString("pt10:introducao"));
        x1408.addChild(x1407);
        x1407.setTitle(MultiLanguageString.importFromString("pt12:Candidaturas"));
        x1407.setDescription(MultiLanguageString.importFromString("pt23:Inicio das candidaturas"));
        x1407.setNormalizedName(MultiLanguageString.importFromString("pt10:introducao"));
        x1407.setExecutionPath("candidacies/caseHandlingDegreeChangeIndividualCandidacyProcess.do?method=candidaciesTypesInformationIntro");
        Functionality x1409 = new Functionality(MultiLanguageString.importFromString("pt13:licenciaturas"));
        x1408.addChild(x1409);
        x1409.setTitle(MultiLanguageString.importFromString("pt13:Licenciaturas"));
        x1409.setDescription(MultiLanguageString.importFromString("pt13:Licenciaturas"));
        x1409.setNormalizedName(MultiLanguageString.importFromString("pt13:licenciaturas"));
        x1409.setExecutionPath("candidacies/caseHandlingDegreeChangeIndividualCandidacyProcess.do?method=candidaciesTypesInformationIntro");
        Functionality x1410 = new Functionality(MultiLanguageString.importFromString("pt9:mestrados"));
        x1408.addChild(x1410);
        x1410.setTitle(MultiLanguageString.importFromString("pt9:Mestrados"));
        x1410.setDescription(MultiLanguageString.importFromString("pt9:Mestrados"));
        x1410.setNormalizedName(MultiLanguageString.importFromString("pt9:mestrados"));
        x1410.setExecutionPath("candidacies/caseHandlingSecondCycleCandidacyIndividualProcess.do?method=beginCandidacyProcessIntro");
        Functionality x1411 = new Functionality(MultiLanguageString.importFromString("pt14:mudancas_curso"));
        x1408.addChild(x1411);
        x1411.setTitle(MultiLanguageString.importFromString("pt17:Mudancas de Curso"));
        x1411.setDescription(MultiLanguageString.importFromString("pt17:Mudancas de Curso"));
        x1411.setNormalizedName(MultiLanguageString.importFromString("pt14:mudancas_curso"));
        x1411.setExecutionPath("candidacies/caseHandlingDegreeChangeIndividualCandidacyProcess.do?method=candidaciesTypesInformationIntro");
        Functionality x1412 = new Functionality(MultiLanguageString.importFromString("pt8:transfer"));
        x1408.addChild(x1412);
        x1412.setTitle(MultiLanguageString.importFromString("pt14:Transferencias"));
        x1412.setDescription(MultiLanguageString.importFromString("pt14:Transferencias"));
        x1412.setNormalizedName(MultiLanguageString.importFromString("pt8:transfer"));
        x1412.setExecutionPath("candidacies/caseHandlingDegreeTransferIndividualCandidacyProcess.do?method=beginCandidacyProcessIntro");
        Functionality x1413 = new Functionality(MultiLanguageString.importFromString("pt24:cursos_medios_superiores"));
        x1408.addChild(x1413);
        x1413.setTitle(MultiLanguageString.importFromString("pt39:Titulares de cursos médios e superiores"));
        x1413.setDescription(MultiLanguageString.importFromString("pt39:Titulares de cursos médios e superiores"));
        x1413.setNormalizedName(MultiLanguageString.importFromString("pt24:cursos_medios_superiores"));
        x1413.setExecutionPath("candidacies/caseHandlingDegreeCandidacyForGraduatedPersonIndividualProcess.do?method=beginCandidacyProcessIntro");
        Functionality x1414 = new Functionality(MultiLanguageString.importFromString("pt18:maiores_vinte_tres"));
        x1408.addChild(x1414);
        x1414.setTitle(MultiLanguageString.importFromString("pt13:Maiores de 23"));
        x1414.setDescription(MultiLanguageString.importFromString("pt13:Maiores de 23"));
        x1414.setNormalizedName(MultiLanguageString.importFromString("pt18:maiores_vinte_tres"));
        x1414.setExecutionPath("candidacies/caseHandlingOver23CandidacyIndividualProcess.do?method=beginCandidacyProcessIntro");
        Functionality x1415 = new Functionality(MultiLanguageString.importFromString("pt24:concurso_nacional_acesso"));
        x1408.addChild(x1415);
        x1415.setTitle(MultiLanguageString.importFromString("pt27:Concurso Nacional de Acesso"));
        x1415.setDescription(MultiLanguageString.importFromString("pt27:Concurso Nacional de Acesso"));
        x1415.setNormalizedName(MultiLanguageString.importFromString("pt24:concurso_nacional_acesso"));
        x1415.setExecutionPath("candidacies/caseHandlingDegreeChangeIndividualCandidacyProcess.do?method=nationalAdmissionTestIntro");
        Module x1417 = new Module(MultiLanguageString.importFromString("pt15:vinte_tres_anos"), "/");
        x1408.addChild(x1417);
        x1417.setTitle(MultiLanguageString.importFromString("pt17:Vinte e tres anos"));
        x1417.setDescription(MultiLanguageString.importFromString("pt17:Vinte e tres anos"));
        x1417.setNormalizedName(MultiLanguageString.importFromString("pt15:vinte_tres_anos"));
        Functionality x1416 = new Functionality(MultiLanguageString.importFromString("pt6:acesso"));
        x1417.addChild(x1416);
        x1416.setTitle(MultiLanguageString.importFromString("pt6:Acesso"));
        x1416.setDescription(MultiLanguageString.importFromString("pt6:Acesso"));
        x1416.setNormalizedName(MultiLanguageString.importFromString("pt6:acesso"));
        x1416.setExecutionPath("candidacies/caseHandlingOver23IndividualCandidacyProcess.do?method=prepareCandidacyCreation");
        Functionality x1418 = new Functionality(MultiLanguageString.importFromString("pt9:submissao"));
        x1417.addChild(x1418);
        x1418.setTitle(MultiLanguageString.importFromString("pt21:Submissao Candidatura"));
        x1418.setDescription(MultiLanguageString.importFromString("pt21:Submissao Candidatura"));
        x1418.setNormalizedName(MultiLanguageString.importFromString("pt9:submissao"));
        x1418.setExecutionPath("candidacies/caseHandlingOver23IndividualCandidacyProcess.do?method=showApplicationSubmissionConditions");
        Functionality x1419 = new Functionality(MultiLanguageString.importFromString("pt10:preregisto"));
        x1417.addChild(x1419);
        x1419.setTitle(MultiLanguageString.importFromString("pt10:preregisto"));
        x1419.setDescription(MultiLanguageString.importFromString("pt10:preregisto"));
        x1419.setNormalizedName(MultiLanguageString.importFromString("pt10:preregisto"));
        x1419.setExecutionPath("candidacies/caseHandlingOver23IndividualCandidacyProcess.do?method=preparePreCreationOfCandidacy");
        Module x1422 = new Module(MultiLanguageString.importFromString("pt13:segundo_ciclo"), "/");
        x1408.addChild(x1422);
        x1422.setTitle(MultiLanguageString.importFromString("pt13:Segundo Ciclo"));
        x1422.setDescription(MultiLanguageString.importFromString("pt13:Segundo Ciclo"));
        x1422.setNormalizedName(MultiLanguageString.importFromString("pt13:segundo_ciclo"));
        Functionality x1421 = new Functionality(MultiLanguageString.importFromString("pt6:acesso"));
        x1422.addChild(x1421);
        x1421.setTitle(MultiLanguageString.importFromString("pt6:Acesso"));
        x1421.setDescription(MultiLanguageString.importFromString("pt6:Acesso"));
        x1421.setNormalizedName(MultiLanguageString.importFromString("pt6:acesso"));
        x1421.setExecutionPath("candidacies/caseHandlingSecondCycleCandidacyIndividualProcess.do?method=prepareCandidacyCreation");
        Functionality x1423 = new Functionality(MultiLanguageString.importFromString("pt9:submissao"));
        x1422.addChild(x1423);
        x1423.setTitle(MultiLanguageString.importFromString("pt21:Submissao Candidatura"));
        x1423.setDescription(MultiLanguageString.importFromString("pt21:Submissao Candidatura"));
        x1423.setNormalizedName(MultiLanguageString.importFromString("pt9:submissao"));
        x1423.setExecutionPath("candidacies/caseHandlingSecondCycleCandidacyIndividualProcess.do?method=showApplicationSubmissionConditions");
        Functionality x1424 = new Functionality(MultiLanguageString.importFromString("pt10:preregisto"));
        x1422.addChild(x1424);
        x1424.setTitle(MultiLanguageString.importFromString("pt11:Pre Registo"));
        x1424.setDescription(MultiLanguageString.importFromString("pt11:Pre Registo"));
        x1424.setNormalizedName(MultiLanguageString.importFromString("pt10:preregisto"));
        x1424.setExecutionPath("candidacies/caseHandlingSecondCycleCandidacyIndividualProcess.do?method=preparePreCreationOfCandidacy");
        Functionality x1425 = new Functionality(MultiLanguageString.importFromString("pt11:recuperacao"));
        x1422.addChild(x1425);
        x1425.setTitle(MultiLanguageString.importFromString("pt11:recuperacao"));
        x1425.setDescription(MultiLanguageString.importFromString("pt11:recuperacao"));
        x1425.setNormalizedName(MultiLanguageString.importFromString("pt11:recuperacao"));
        x1425.setExecutionPath("candidacies/caseHandlingSecondCycleCandidacyIndividualProcess.do?method=prepareRecoverAccessLink");
        Module x1428 = new Module(MultiLanguageString.importFromString("pt17:Programa Doutoral"), "/");
        x1408.addChild(x1428);
        x1428.setNormalizedName(MultiLanguageString.importFromString("pt17:programa-doutoral"));
        Functionality x1427 = new Functionality(MultiLanguageString.importFromString("en6:Acesso"));
        x1428.addChild(x1427);
        x1427.setNormalizedName(MultiLanguageString.importFromString("en6:acesso"));
        x1427.setExecutionPath("/candidacies/phdProgramCandidacyProcess.do?method=prepareCreateCandidacyIdentification");
        Functionality x1429 = new Functionality(MultiLanguageString.importFromString("en16:Recuperar Acesso"));
        x1428.addChild(x1429);
        x1429.setNormalizedName(MultiLanguageString.importFromString("en16:recuperar-acesso"));
        x1429.setExecutionPath("/candidacies/phdProgramCandidacyProcess.do?method=prepareCandidacyIdentificationRecovery");
        Functionality x1430 = new Functionality(MultiLanguageString.importFromString("en9:Submissao"));
        x1428.addChild(x1430);
        x1430.setNormalizedName(MultiLanguageString.importFromString("en9:submissao"));
        x1430.setExecutionPath("/candidacies/phdProgramCandidacyProcess.do?method=prepareCreateCandidacy");
        Functionality x1431 = new Functionality(MultiLanguageString.importFromString("en16:Carta Referencia"));
        x1428.addChild(x1431);
        x1431.setNormalizedName(MultiLanguageString.importFromString("en16:carta-referencia"));
        x1431.setExecutionPath("/candidacies/phdProgramCandidacyProcess.do?method=prepareCreateRefereeLetter");
        Module x1434 = new Module(MultiLanguageString.importFromString("pt13:mudanca_curso"), "/");
        x1408.addChild(x1434);
        x1434.setTitle(MultiLanguageString.importFromString("pt16:Mudanca de Curso"));
        x1434.setDescription(MultiLanguageString.importFromString("pt16:Mudanca de Curso"));
        x1434.setNormalizedName(MultiLanguageString.importFromString("pt13:mudanca_curso"));
        Functionality x1433 = new Functionality(MultiLanguageString.importFromString("pt10:preregisto"));
        x1434.addChild(x1433);
        x1433.setTitle(MultiLanguageString.importFromString("pt11:Pre Registo"));
        x1433.setDescription(MultiLanguageString.importFromString("pt11:Pre Registo"));
        x1433.setNormalizedName(MultiLanguageString.importFromString("pt10:preregisto"));
        x1433.setExecutionPath("candidacies/caseHandlingDegreeChangeIndividualCandidacyProcess.do?method=preparePreCreationOfCandidacy");
        Functionality x1435 = new Functionality(MultiLanguageString.importFromString("pt6:acesso"));
        x1434.addChild(x1435);
        x1435.setTitle(MultiLanguageString.importFromString("pt6:Acesso"));
        x1435.setDescription(MultiLanguageString.importFromString("pt6:Acesso"));
        x1435.setNormalizedName(MultiLanguageString.importFromString("pt6:acesso"));
        x1435.setExecutionPath("candidacies/caseHandlingDegreeChangeIndividualCandidacyProcess.do?method=prepareCandidacyCreation");
        Functionality x1436 = new Functionality(MultiLanguageString.importFromString("pt9:submissao"));
        x1434.addChild(x1436);
        x1436.setTitle(MultiLanguageString.importFromString("pt9:Submissao"));
        x1436.setDescription(MultiLanguageString.importFromString("pt9:Submissao"));
        x1436.setNormalizedName(MultiLanguageString.importFromString("pt9:submissao"));
        x1436.setExecutionPath("candidacies/caseHandlingDegreeChangeIndividualCandidacyProcess.do?method=showApplicationSubmissionConditions");
        Module x1439 = new Module(MultiLanguageString.importFromString("pt24:cursos_medios_superiores"), "/");
        x1408.addChild(x1439);
        x1439.setTitle(MultiLanguageString.importFromString("pt36:Titulares Cursos Medios e Superiores"));
        x1439.setDescription(MultiLanguageString.importFromString("pt36:Titulares Cursos Medios e Superiores"));
        x1439.setNormalizedName(MultiLanguageString.importFromString("pt24:cursos_medios_superiores"));
        Functionality x1438 = new Functionality(MultiLanguageString.importFromString("pt10:preregisto"));
        x1439.addChild(x1438);
        x1438.setTitle(MultiLanguageString.importFromString("pt11:Pre Registo"));
        x1438.setDescription(MultiLanguageString.importFromString("pt11:Pre Registo"));
        x1438.setNormalizedName(MultiLanguageString.importFromString("pt10:preregisto"));
        x1438.setExecutionPath("candidacies/caseHandlingDegreeCandidacyForGraduatedPersonIndividualProcess.do?method=preparePreCreationOfCandidacy");
        Functionality x1440 = new Functionality(MultiLanguageString.importFromString("pt6:acesso"));
        x1439.addChild(x1440);
        x1440.setTitle(MultiLanguageString.importFromString("pt6:Acesso"));
        x1440.setDescription(MultiLanguageString.importFromString("pt6:Acesso"));
        x1440.setNormalizedName(MultiLanguageString.importFromString("pt6:acesso"));
        x1440.setExecutionPath("candidacies/caseHandlingDegreeCandidacyForGraduatedPersonIndividualProcess.do?method=prepareCandidacyCreation");
        Functionality x1441 = new Functionality(MultiLanguageString.importFromString("pt9:submissao"));
        x1439.addChild(x1441);
        x1441.setTitle(MultiLanguageString.importFromString("pt21:Submissao Candidatura"));
        x1441.setDescription(MultiLanguageString.importFromString("pt21:Submissao Candidatura"));
        x1441.setNormalizedName(MultiLanguageString.importFromString("pt9:submissao"));
        x1441.setExecutionPath("candidacies/caseHandlingDegreeCandidacyForGraduatedPersonIndividualProcess.do?method=showApplicationSubmissionConditions");
        Module x1444 = new Module(MultiLanguageString.importFromString("pt13:transferencia"), "/");
        x1408.addChild(x1444);
        x1444.setTitle(MultiLanguageString.importFromString("pt13:Transferencia"));
        x1444.setDescription(MultiLanguageString.importFromString("pt13:Transferencia"));
        x1444.setNormalizedName(MultiLanguageString.importFromString("pt13:transferencia"));
        Functionality x1443 = new Functionality(MultiLanguageString.importFromString("pt10:preregisto"));
        x1444.addChild(x1443);
        x1443.setTitle(MultiLanguageString.importFromString("pt11:Pre Registo"));
        x1443.setDescription(MultiLanguageString.importFromString("pt11:Pre Registo"));
        x1443.setNormalizedName(MultiLanguageString.importFromString("pt10:preregisto"));
        x1443.setExecutionPath("candidacies/caseHandlingDegreeTransferIndividualCandidacyProcess.do?method=preparePreCreationOfCandidacy");
        Functionality x1445 = new Functionality(MultiLanguageString.importFromString("pt6:acesso"));
        x1444.addChild(x1445);
        x1445.setTitle(MultiLanguageString.importFromString("pt6:Acesso"));
        x1445.setDescription(MultiLanguageString.importFromString("pt6:Acesso"));
        x1445.setNormalizedName(MultiLanguageString.importFromString("pt6:acesso"));
        x1445.setExecutionPath("candidacies/caseHandlingDegreeTransferIndividualCandidacyProcess.do?method=prepareCandidacyCreation");
        Functionality x1446 = new Functionality(MultiLanguageString.importFromString("pt9:submissao"));
        x1444.addChild(x1446);
        x1446.setTitle(MultiLanguageString.importFromString("pt21:Submissao Candidatura"));
        x1446.setDescription(MultiLanguageString.importFromString("pt21:Submissao Candidatura"));
        x1446.setNormalizedName(MultiLanguageString.importFromString("pt9:submissao"));
        x1446.setExecutionPath("candidacies/caseHandlingDegreeTransferIndividualCandidacyProcess.do?method=showApplicationSubmissionConditions");
        Module x1449 = new Module(MultiLanguageString.importFromString("pt7:erasmus"), "/");
        x1408.addChild(x1449);
        x1449.setTitle(MultiLanguageString.importFromString("pt7:erasmus"));
        x1449.setDescription(MultiLanguageString.importFromString("pt7:erasmus"));
        x1449.setNormalizedName(MultiLanguageString.importFromString("pt7:erasmus"));
        Functionality x1448 = new Functionality(MultiLanguageString.importFromString("pt5:intro"));
        x1449.addChild(x1448);
        x1448.setTitle(MultiLanguageString.importFromString("pt5:intro"));
        x1448.setDescription(MultiLanguageString.importFromString("pt5:intro"));
        x1448.setNormalizedName(MultiLanguageString.importFromString("pt5:intro"));
        x1448.setExecutionPath("candidacies/caseHandlingMobilityApplicationIndividualProcess.do?method=intro");
        Functionality x1450 = new Functionality(MultiLanguageString.importFromString("pt15:submission-type"));
        x1449.addChild(x1450);
        x1450.setTitle(MultiLanguageString.importFromString("pt15:submission-type"));
        x1450.setDescription(MultiLanguageString.importFromString("pt15:submission-type"));
        x1450.setNormalizedName(MultiLanguageString.importFromString("pt15:submission-type"));
        x1450.setExecutionPath("candidacies/caseHandlingMobilityApplicationIndividualProcess.do?method=chooseSubmissionType");
        Functionality x1451 = new Functionality(MultiLanguageString.importFromString("pt15:preregistration"));
        x1449.addChild(x1451);
        x1451.setTitle(MultiLanguageString.importFromString("pt15:preregistration"));
        x1451.setDescription(MultiLanguageString.importFromString("pt15:preregistration"));
        x1451.setNormalizedName(MultiLanguageString.importFromString("pt15:preregistration"));
        x1451.setExecutionPath("candidacies/caseHandlingMobilityApplicationIndividualProcess.do?method=preparePreCreationOfCandidacy");
        Functionality x1452 = new Functionality(MultiLanguageString.importFromString("pt10:submission"));
        x1449.addChild(x1452);
        x1452.setTitle(MultiLanguageString.importFromString("pt10:submission"));
        x1452.setDescription(MultiLanguageString.importFromString("pt10:submission"));
        x1452.setNormalizedName(MultiLanguageString.importFromString("pt10:submission"));
        x1452.setExecutionPath("candidacies/caseHandlingMobilityApplicationIndividualProcess.do?method=showApplicationSubmissionConditions");
        Functionality x1453 = new Functionality(MultiLanguageString.importFromString("pt6:access"));
        x1449.addChild(x1453);
        x1453.setTitle(MultiLanguageString.importFromString("pt6:access"));
        x1453.setDescription(MultiLanguageString.importFromString("pt6:access"));
        x1453.setNormalizedName(MultiLanguageString.importFromString("pt6:access"));
        x1453.setExecutionPath("candidacies/caseHandlingMobilityApplicationIndividualProcess.do?method=prepareCandidacyCreation");
        Functionality x1454 = new Functionality(MultiLanguageString.importFromString("pt14:returnFromPeps"));
        x1449.addChild(x1454);
        x1454.setTitle(MultiLanguageString.importFromString("pt14:returnFromPeps"));
        x1454.setDescription(MultiLanguageString.importFromString("pt14:returnFromPeps"));
        x1454.setNormalizedName(MultiLanguageString.importFromString("pt14:returnfrompeps"));
        x1454.setExecutionPath("candidacies/caseHandlingMobilityApplicationIndividualProcess.do?method=returnFromPeps");
        Functionality x1455 = new Functionality(MultiLanguageString.importFromString("pt22:nationalCardSubmission"));
        x1449.addChild(x1455);
        x1455.setTitle(MultiLanguageString.importFromString("pt22:nationalCardSubmission"));
        x1455.setDescription(MultiLanguageString.importFromString("pt22:nationalCardSubmission"));
        x1455.setNormalizedName(MultiLanguageString.importFromString("pt22:nationalcardsubmission"));
        x1455.setExecutionPath("candidacies/caseHandlingMobilityApplicationIndividualProcess.do?method=submitWithNationalCitizenCard");
        Functionality x1456 = new Functionality(MultiLanguageString.importFromString("pt29:nationalCardApplicationAccess"));
        x1449.addChild(x1456);
        x1456.setTitle(MultiLanguageString.importFromString("pt29:nationalCardApplicationAccess"));
        x1456.setDescription(MultiLanguageString.importFromString("pt29:nationalCardApplicationAccess"));
        x1456.setNormalizedName(MultiLanguageString.importFromString("pt29:nationalcardapplicationaccess"));
        x1456.setExecutionPath("candidacies/caseHandlingMobilityApplicationIndividualProcess.do?method=accessApplicationWithNationalCitizenCard");
        Functionality x1457 = new Functionality(MultiLanguageString.importFromString("pt14:accessRecovery"));
        x1449.addChild(x1457);
        x1457.setTitle(MultiLanguageString.importFromString("pt14:accessRecovery"));
        x1457.setDescription(MultiLanguageString.importFromString("pt14:accessRecovery"));
        x1457.setNormalizedName(MultiLanguageString.importFromString("pt14:accessrecovery"));
        x1457.setExecutionPath("candidacies/caseHandlingMobilityApplicationIndividualProcess.do?method=prepareRecoverAccessLink");
        Functionality x1458 = new Functionality(MultiLanguageString.importFromString("pt21:pepsApplicationAccess"));
        x1449.addChild(x1458);
        x1458.setTitle(MultiLanguageString.importFromString("pt21:pepsApplicationAccess"));
        x1458.setDescription(MultiLanguageString.importFromString("pt21:pepsApplicationAccess"));
        x1458.setNormalizedName(MultiLanguageString.importFromString("pt21:pepsapplicationaccess"));
        x1458.setExecutionPath("candidacies/caseHandlingMobilityApplicationIndividualProcess.do?method=returnFromPepsToAccessApplication");
        Functionality x1459 = new Functionality(MultiLanguageString.importFromString("en9:testStork"));
        x1449.addChild(x1459);
        x1459.setTitle(MultiLanguageString.importFromString("en9:testStork"));
        x1459.setDescription(MultiLanguageString.importFromString("en9:testStork"));
        x1459.setNormalizedName(MultiLanguageString.importFromString("en9:teststork"));
        x1459.setExecutionPath("candidacies/caseHandlingMobilityApplicationIndividualProcess.do?method=prepareTestStorkAttrString");
        Module x1462 = new Module(MultiLanguageString.importFromString("pt3:phd"), "/");
        x1408.addChild(x1462);
        x1462.setTitle(MultiLanguageString.importFromString("pt3:phd"));
        x1462.setDescription(MultiLanguageString.importFromString("pt3:phd"));
        x1462.setNormalizedName(MultiLanguageString.importFromString("pt3:phd"));
        Functionality x1461 = new Functionality(MultiLanguageString.importFromString("en14:identificationpt13:identificacao"));
        x1462.addChild(x1461);
        x1461.setTitle(MultiLanguageString.importFromString("pt13:identificacao"));
        x1461.setDescription(MultiLanguageString.importFromString("pt13:identificacao"));
        x1461.setNormalizedName(MultiLanguageString.importFromString("en14:identificationpt13:identificacao"));
        x1461.setExecutionPath("/applications/phd/phdProgramApplicationProcess.do?method=prepareCreateIdentification");
        Functionality x1463 = new Functionality(MultiLanguageString.importFromString("en10:submissionpt9:submissao"));
        x1462.addChild(x1463);
        x1463.setTitle(MultiLanguageString.importFromString("pt9:submissao"));
        x1463.setDescription(MultiLanguageString.importFromString("pt9:submissao"));
        x1463.setNormalizedName(MultiLanguageString.importFromString("en10:submissionpt9:submissao"));
        x1463.setExecutionPath("/applications/phd/phdProgramApplicationProcess.do?method=prepareFillPersonalData");
        Functionality x1464 = new Functionality(MultiLanguageString.importFromString("en6:accesspt6:acesso"));
        x1462.addChild(x1464);
        x1464.setTitle(MultiLanguageString.importFromString("pt6:acesso"));
        x1464.setDescription(MultiLanguageString.importFromString("pt6:acesso"));
        x1464.setNormalizedName(MultiLanguageString.importFromString("en6:accesspt6:acesso"));
        x1464.setExecutionPath("/applications/phd/phdProgramApplicationProcess.do?method=viewApplication");
        Functionality x1465 = new Functionality(MultiLanguageString.importFromString("en8:recoverypt11:recuperacao"));
        x1462.addChild(x1465);
        x1465.setTitle(MultiLanguageString.importFromString("pt11:recuperacao"));
        x1465.setDescription(MultiLanguageString.importFromString("pt11:recuperacao"));
        x1465.setNormalizedName(MultiLanguageString.importFromString("en8:recoverypt11:recuperacao"));
        x1465.setExecutionPath("/applications/phd/phdProgramApplicationProcess.do?method=prepareIdentificationRecovery");
        Functionality x1466 = new Functionality(MultiLanguageString.importFromString("pt12:recomendacao"));
        x1462.addChild(x1466);
        x1466.setTitle(MultiLanguageString.importFromString("pt12:recomendacao"));
        x1466.setDescription(MultiLanguageString.importFromString("pt12:recomendacao"));
        x1466.setNormalizedName(MultiLanguageString.importFromString("pt12:recomendacao"));
        x1466.setExecutionPath("/applications/phd/phdProgramApplicationProcess.do?method=prepareCreateRefereeLetter");
        Functionality x1467 = new Functionality(MultiLanguageString.importFromString("pt19:validateApplication"));
        x1462.addChild(x1467);
        x1467.setTitle(MultiLanguageString.importFromString("pt19:validateApplication"));
        x1467.setDescription(MultiLanguageString.importFromString("pt19:validateApplication"));
        x1467.setNormalizedName(MultiLanguageString.importFromString("pt19:validateapplication"));
        x1467.setExecutionPath("/applications/phd/phdProgramApplicationProcess.do?method=prepareValidateApplication");
        Functionality x1468 = new Functionality(MultiLanguageString.importFromString("en14:identification"));
        x1462.addChild(x1468);
        x1468.setTitle(MultiLanguageString.importFromString("en14:identification"));
        x1468.setDescription(MultiLanguageString.importFromString("en14:identification"));
        x1468.setNormalizedName(MultiLanguageString.importFromString("en14:identification"));
        x1468.setExecutionPath("/applications/phd/phdProgramApplicationProcess.do?method=prepareCreateIdentification&locale=en");
        Module x1471 = new Module(MultiLanguageString.importFromString("pt4:epfl"), "/");
        x1408.addChild(x1471);
        x1471.setTitle(MultiLanguageString.importFromString("pt4:epfl"));
        x1471.setDescription(MultiLanguageString.importFromString("pt4:epfl"));
        x1471.setNormalizedName(MultiLanguageString.importFromString("pt4:epfl"));
        Functionality x1470 = new Functionality(MultiLanguageString.importFromString("en14:identificationpt13:identificacao"));
        x1471.addChild(x1470);
        x1470.setTitle(MultiLanguageString.importFromString("en14:identificationpt13:identificacao"));
        x1470.setDescription(MultiLanguageString.importFromString("en14:identificationpt13:identificacao"));
        x1470.setNormalizedName(MultiLanguageString.importFromString("en14:identificationpt13:identificacao"));
        x1470.setExecutionPath("/applications/epfl/phdProgramCandidacyProcess.do?method=prepareCreateCandidacyIdentification");
        Functionality x1472 = new Functionality(MultiLanguageString.importFromString("en10:submissionpt9:submissao"));
        x1471.addChild(x1472);
        x1472.setTitle(MultiLanguageString.importFromString("en10:submissionpt9:submissao"));
        x1472.setDescription(MultiLanguageString.importFromString("en10:submissionpt9:submissao"));
        x1472.setNormalizedName(MultiLanguageString.importFromString("en10:submissionpt9:submissao"));
        x1472.setExecutionPath("/applications/epfl/phdProgramCandidacyProcess.do?method=prepareCreateCandidacy");
        Functionality x1473 = new Functionality(MultiLanguageString.importFromString("en6:accesspt6:acesso"));
        x1471.addChild(x1473);
        x1473.setTitle(MultiLanguageString.importFromString("en6:accesspt6:acesso"));
        x1473.setDescription(MultiLanguageString.importFromString("en6:accesspt6:acesso"));
        x1473.setNormalizedName(MultiLanguageString.importFromString("en6:accesspt6:acesso"));
        x1473.setExecutionPath("/applications/epfl/phdProgramCandidacyProcess.do?method=viewCandidacy");
        Functionality x1474 = new Functionality(MultiLanguageString.importFromString("pt12:referee-form"));
        x1471.addChild(x1474);
        x1474.setTitle(MultiLanguageString.importFromString("pt12:referee-form"));
        x1474.setDescription(MultiLanguageString.importFromString("pt12:referee-form"));
        x1474.setNormalizedName(MultiLanguageString.importFromString("pt12:referee-form"));
        x1474.setExecutionPath("/applications/epfl/phdProgramCandidacyProcess.do?method=prepareCreateRefereeLetter");
        Functionality x1475 = new Functionality(MultiLanguageString.importFromString("en8:recoverypt11:recuperacao"));
        x1471.addChild(x1475);
        x1475.setTitle(MultiLanguageString.importFromString("en8:recoverypt11:recuperacao"));
        x1475.setDescription(MultiLanguageString.importFromString("en8:recoverypt11:recuperacao"));
        x1475.setNormalizedName(MultiLanguageString.importFromString("en8:recoverypt11:recuperacao"));
        x1475.setExecutionPath("/applications/epfl/phdProgramCandidacyProcess.do?method=prepareCandidacyIdentificationRecovery");
        Module x1479 = new Module(MultiLanguageString.importFromString("en11:Phd Programpt17:Programa Doutoral"), "/");
        x1303.addChild(x1479);
        x1479.setNormalizedName(MultiLanguageString.importFromString("en11:phd-programpt17:programa-doutoral"));
        Functionality x1478 = new Functionality(MultiLanguageString.importFromString("en6:Accesspt6:Acesso"));
        x1479.addChild(x1478);
        x1478.setNormalizedName(MultiLanguageString.importFromString("en6:accesspt6:acesso"));
        x1478.setExecutionPath("/phdExternalAccess.do?method=prepare");
        Module x1483 = new Module(MultiLanguageString.importFromString("pt20:Portal da Tesouraria"), "/treasury");
        x823.addChild(x1483);
        x1483.setNormalizedName(MultiLanguageString.importFromString("pt20:portal-da-tesouraria"));
        x1483.setExecutionPath("/treasury/index.do");
        x1483.setVisible(true);
        Functionality x1482 =
                new Functionality(MultiLanguageString.importFromString("en12:Initial Pageen8:Homepagept14:Página Inicial"));
        x1483.addChild(x1482);
        x1482.setTitle(MultiLanguageString.importFromString("en12:Initial Pageen8:Homepagept14:Página Inicial"));
        x1482.setNormalizedName(MultiLanguageString.importFromString("en12:initial-pageen8:homepagept14:pagina-inicial"));
        x1482.setExecutionPath("/index.do");
        Functionality x1484 = new Functionality(MultiLanguageString.importFromString("en8:Paymentspt10:Pagamentos"));
        x1483.addChild(x1484);
        x1484.setTitle(MultiLanguageString.importFromString("en8:Paymentspt10:Pagamentos"));
        x1484.setNormalizedName(MultiLanguageString.importFromString("en8:paymentspt10:pagamentos"));
        x1484.setExecutionPath("/paymentManagement.do?method=searchPeople");
        ExpressionGroupAvailability x1651 = new ExpressionGroupAvailability(x1483, "role(TREASURY)");
        x1651.setTargetGroup(Group.fromString("role(TREASURY)"));
        x1483.setAvailabilityPolicy(x1651);
        Module x1487 = new Module(MultiLanguageString.importFromString("en7:Librarypt10:Biblioteca"), "/library");
        x823.addChild(x1487);
        x1487.setTitle(MultiLanguageString.importFromString("en14:Library Portalpt20:Portal da Biblioteca"));
        x1487.setNormalizedName(MultiLanguageString.importFromString("en7:librarypt10:biblioteca"));
        x1487.setExecutionPath("/library/index.do");
        x1487.setVisible(true);
        Functionality x1486 = new Functionality(MultiLanguageString.importFromString("en5:Startpt6:Início"));
        x1487.addChild(x1486);
        x1486.setTitle(MultiLanguageString.importFromString("en19:Portal inicial pagept24:Página inicial do portal"));
        x1486.setNormalizedName(MultiLanguageString.importFromString("en5:startpt6:inicio"));
        x1486.setExecutionPath("/index.do");
        x1486.setVisible(false);
        Module x1489 = new Module(MultiLanguageString.importFromString("en6:Thesespt12:Dissertações"), "/theses");
        x1487.addChild(x1489);
        x1489.setNormalizedName(MultiLanguageString.importFromString("en6:thesespt12:dissertacoes"));
        x1489.setExecutionPath("");
        x1489.setVisible(false);
        Functionality x1488 = new Functionality(MultiLanguageString.importFromString("en10:Validationpt9:Validação"));
        x1489.addChild(x1488);
        x1488.setTitle(MultiLanguageString.importFromString("en17:Theses Validationpt25:Validação de Dissertações"));
        x1488.setNormalizedName(MultiLanguageString.importFromString("en10:validationpt9:validacao"));
        x1488.setExecutionPath("/search.do?method=prepare");
        Module x1492 = new Module(MultiLanguageString.importFromString("pt7:Cartões"), "/");
        x1487.addChild(x1492);
        x1492.setNormalizedName(MultiLanguageString.importFromString("pt7:cartoes"));
        x1492.setExecutionPath("");
        x1492.setVisible(true);
        Functionality x1491 = new Functionality(MultiLanguageString.importFromString("pt16:Listagem utentes"));
        x1492.addChild(x1491);
        x1491.setTitle(MultiLanguageString.importFromString("pt17:Cartão Biblioteca"));
        x1491.setNormalizedName(MultiLanguageString.importFromString("pt16:listagem-utentes"));
        x1491.setExecutionPath("/cardManagement.do?method=showUsers&dontSearch=yes");
        x1491.setVisible(true);
        Functionality x1493 = new Functionality(MultiLanguageString.importFromString("pt13:Gerar Cartões"));
        x1492.addChild(x1493);
        x1493.setTitle(MultiLanguageString.importFromString("pt31:Gerar pdf para cartões em falta"));
        x1493.setNormalizedName(MultiLanguageString.importFromString("pt13:gerar-cartoes"));
        x1493.setExecutionPath("/cardManagement.do?method=prepareGenerateMissingCards");
        x1493.setVisible(true);
        Functionality x1494 = new Functionality(MultiLanguageString.importFromString("pt12:Gerar Cartas"));
        x1492.addChild(x1494);
        x1494.setTitle(MultiLanguageString.importFromString("pt21:Gerar cartas em falta"));
        x1494.setNormalizedName(MultiLanguageString.importFromString("pt12:gerar-cartas"));
        x1494.setExecutionPath("/cardManagement.do?method=prepareGenerateMissingLetters");
        x1494.setVisible(true);
        Functionality x1495 =
                new Functionality(MultiLanguageString.importFromString("pt36:Gerar Cartas para Alunos e Bolseiros"));
        x1492.addChild(x1495);
        x1495.setTitle(MultiLanguageString.importFromString("pt45:Gerar cartas em falta para Alunos e Bolseiros"));
        x1495.setNormalizedName(MultiLanguageString.importFromString("pt36:gerar-cartas-para-alunos-e-bolseiros"));
        x1495.setExecutionPath("/cardManagement.do?method=prepareGenerateMissingLettersForStudents");
        x1495.setVisible(true);
        Functionality x1497 = new Functionality(MultiLanguageString.importFromString("pt22:Operador de Biblioteca"));
        x1487.addChild(x1497);
        x1497.setTitle(MultiLanguageString.importFromString("pt22:Operador de Biblioteca"));
        x1497.setNormalizedName(MultiLanguageString.importFromString("pt22:operador-de-biblioteca"));
        x1497.setExecutionPath("/libraryOperator.do?method=prepare");
        Functionality x1498 = new Functionality(MultiLanguageString.importFromString("pt31:Actualizar Capacidade e Cacifos"));
        x1487.addChild(x1498);
        x1498.setTitle(MultiLanguageString.importFromString("pt31:Actualizar Capacidade e Cacifos"));
        x1498.setNormalizedName(MultiLanguageString.importFromString("pt31:actualizar-capacidade-e-cacifos"));
        x1498.setExecutionPath("/libraryOperator.do?method=prepareUpdateCapacityAndLockers");
        ExpressionGroupAvailability x1652 =
                new ExpressionGroupAvailability(x1498, "role(LIBRARY) && (libraryHigherClearanceGroup() || role(MANAGER))");
        x1652.setTargetGroup(Group.fromString("role(LIBRARY) && (libraryHigherClearanceGroup() || role(MANAGER))"));
        x1498.setAvailabilityPolicy(x1652);
        Functionality x1499 = new Functionality(MultiLanguageString.importFromString("pt31:Adicionar ou Remover Operadores"));
        x1487.addChild(x1499);
        x1499.setTitle(MultiLanguageString.importFromString("pt31:Adicionar ou Remover Operadores"));
        x1499.setNormalizedName(MultiLanguageString.importFromString("pt31:adicionar-ou-remover-operadores"));
        x1499.setExecutionPath("/libraryOperator.do?method=prepareAddOrRemoveOperators");
        ExpressionGroupAvailability x1653 =
                new ExpressionGroupAvailability(x1499, "role(LIBRARY) && (libraryHigherClearanceGroup() || role(MANAGER))");
        x1653.setTargetGroup(Group.fromString("role(LIBRARY) && (libraryHigherClearanceGroup() || role(MANAGER))"));
        x1499.setAvailabilityPolicy(x1653);
        ExpressionGroupAvailability x1654 = new ExpressionGroupAvailability(x1487, "role(LIBRARY)");
        x1654.setTargetGroup(Group.fromString("role(LIBRARY)"));
        x1487.setAvailabilityPolicy(x1654);
        Module x1502 = new Module(MultiLanguageString.importFromString("pt20:Gestão de Património"), "/resourceManager");
        x823.addChild(x1502);
        x1502.setTitle(MultiLanguageString.importFromString("pt20:Gestão de Património"));
        x1502.setNormalizedName(MultiLanguageString.importFromString("pt20:gestao-de-patrimonio"));
        x1502.setExecutionPath("/resourceManager/index.do");
        x1502.setVisible(true);
        Functionality x1501 = new Functionality(MultiLanguageString.importFromString("pt20:Gestão de Património"));
        x1502.addChild(x1501);
        x1501.setTitle(MultiLanguageString.importFromString("pt20:Gestão de Património"));
        x1501.setNormalizedName(MultiLanguageString.importFromString("pt20:gestao-de-patrimonio"));
        x1501.setExecutionPath("/index.do");
        x1501.setVisible(true);
        ExpressionGroupAvailability x1655 = new ExpressionGroupAvailability(x1502, "role(RESOURCE_MANAGER)");
        x1655.setTargetGroup(Group.fromString("role(RESOURCE_MANAGER)"));
        x1502.setAvailabilityPolicy(x1655);
        Module x1505 = new Module(MultiLanguageString.importFromString("pt14:Estacionamento"), "/parkingManager");
        x823.addChild(x1505);
        x1505.setNormalizedName(MultiLanguageString.importFromString("pt14:estacionamento"));
        x1505.setExecutionPath("/parkingManager/index.do");
        x1505.setVisible(true);
        Functionality x1504 = new Functionality(MultiLanguageString.importFromString("pt14:Estacionamento"));
        x1505.addChild(x1504);
        x1504.setTitle(MultiLanguageString.importFromString("pt14:Estacionamento"));
        x1504.setNormalizedName(MultiLanguageString.importFromString("pt14:estacionamento"));
        x1504.setExecutionPath("/index.do");
        x1504.setVisible(true);
        ExpressionGroupAvailability x1656 = new ExpressionGroupAvailability(x1505, "role(PARKING_MANAGER)");
        x1656.setTargetGroup(Group.fromString("role(PARKING_MANAGER)"));
        x1505.setAvailabilityPolicy(x1656);
        Module x1508 = new Module(MultiLanguageString.importFromString("pt17:Gestão do WebSite"), "/webSiteManager");
        x823.addChild(x1508);
        x1508.setNormalizedName(MultiLanguageString.importFromString("pt17:gestao-do-website"));
        x1508.setExecutionPath("/webSiteManager/index.do");
        x1508.setVisible(true);
        Functionality x1507 = new Functionality(MultiLanguageString.importFromString("pt17:Gestão do WebSite"));
        x1508.addChild(x1507);
        x1507.setTitle(MultiLanguageString.importFromString("pt17:Gestão do WebSite"));
        x1507.setNormalizedName(MultiLanguageString.importFromString("pt17:gestao-do-website"));
        x1507.setExecutionPath("/index.do");
        x1507.setVisible(true);
        ExpressionGroupAvailability x1657 = new ExpressionGroupAvailability(x1508, "role(WEBSITE_MANAGER)");
        x1657.setTargetGroup(Group.fromString("role(WEBSITE_MANAGER)"));
        x1508.setAvailabilityPolicy(x1657);
        Module x1511 = new Module(MultiLanguageString.importFromString("pt9:Avaliação"), "/examCoordination");
        x823.addChild(x1511);
        x1511.setNormalizedName(MultiLanguageString.importFromString("pt9:avaliacao"));
        x1511.setExecutionPath("/examCoordination/index.do");
        x1511.setVisible(true);
        Functionality x1510 = new Functionality(MultiLanguageString.importFromString("pt9:Avaliação"));
        x1511.addChild(x1510);
        x1510.setTitle(MultiLanguageString.importFromString("pt9:Avaliação"));
        x1510.setNormalizedName(MultiLanguageString.importFromString("pt9:avaliacao"));
        x1510.setExecutionPath("/index.do");
        x1510.setVisible(true);
        ExpressionGroupAvailability x1658 = new ExpressionGroupAvailability(x1511, "role(EXAM_COORDINATOR)");
        x1658.setTargetGroup(Group.fromString("role(EXAM_COORDINATOR)"));
        x1511.setAvailabilityPolicy(x1658);
        /*Module x1514 = new Module(MultiLanguageString.importFromString("pt20:Secretaria Académica"), "/academicAdminOffice");
        x823.addChild(x1514);
        x1514.setNormalizedName(MultiLanguageString.importFromString("pt20:secretaria-academica"));
        x1514.setExecutionPath("/academicAdminOffice/index.do");
        x1514.setVisible(true);
        Functionality x1513 = new Functionality(MultiLanguageString.importFromString("pt20:Secretaria Académica"));
        x1514.addChild(x1513);
        x1513.setTitle(MultiLanguageString.importFromString("pt20:Secretaria Académica"));
        x1513.setNormalizedName(MultiLanguageString.importFromString("pt20:secretaria-academica"));
        x1513.setExecutionPath("/index.do");
        x1513.setVisible(true);
        ExpressionGroupAvailability x1659 = new ExpressionGroupAvailability(x1514, "role(ACADEMIC_ADMINISTRATIVE_OFFICE)");
        x1659.setTargetGroup(Group.fromString("role(ACADEMIC_ADMINISTRATIVE_OFFICE)"));
        x1514.setAvailabilityPolicy(x1659);*/
        Module x1517 =
                new Module(MultiLanguageString.importFromString("pt35:Gabinete de Relaçôes Internacionais"),
                        "/internationalRelatOffice");
        x823.addChild(x1517);
        x1517.setTitle(MultiLanguageString.importFromString("pt35:Gabinete de Relaçôes Internacionais"));
        x1517.setNormalizedName(MultiLanguageString.importFromString("pt35:gabinete-de-relacoes-internacionais"));
        Functionality x1516 = new Functionality(MultiLanguageString.importFromString("pt6:Inicio"));
        x1517.addChild(x1516);
        x1516.setTitle(MultiLanguageString.importFromString("pt6:Inicio"));
        x1516.setNormalizedName(MultiLanguageString.importFromString("pt6:inicio"));
        x1516.setExecutionPath("/index.do");
        Module x1519 = new Module(MultiLanguageString.importFromString("pt23:Estagios Internacionais"), "/internship");
        x1517.addChild(x1519);
        x1519.setTitle(MultiLanguageString.importFromString("pt23:Estagios Internacionais"));
        x1519.setNormalizedName(MultiLanguageString.importFromString("pt23:estagios-internacionais"));
        Functionality x1518 = new Functionality(MultiLanguageString.importFromString("pt10:Candidatos"));
        x1519.addChild(x1518);
        x1518.setTitle(MultiLanguageString.importFromString("pt10:Candidatos"));
        x1518.setNormalizedName(MultiLanguageString.importFromString("pt10:candidatos"));
        x1518.setExecutionPath("/internshipCandidacy.do?method=prepareCandidates");
        Functionality x1521 = new Functionality(MultiLanguageString.importFromString("pt17:Visualizar Alunos"));
        x1517.addChild(x1521);
        x1521.setTitle(MultiLanguageString.importFromString("pt17:Visualizar Alunos"));
        x1521.setNormalizedName(MultiLanguageString.importFromString("pt17:visualizar-alunos"));
        x1521.setExecutionPath("/students.do?method=prepareSearch");
        Module x1523 = new Module(MultiLanguageString.importFromString("pt9:Listagens"), "/");
        x1517.addChild(x1523);
        x1523.setTitle(MultiLanguageString.importFromString("pt9:Listagens"));
        x1523.setNormalizedName(MultiLanguageString.importFromString("pt9:listagens"));
        Functionality x1522 = new Functionality(MultiLanguageString.importFromString("pt16:Alunos Por Curso"));
        x1523.addChild(x1522);
        x1522.setTitle(MultiLanguageString.importFromString("pt16:Alunos por Curso"));
        x1522.setNormalizedName(MultiLanguageString.importFromString("pt16:alunos-por-curso"));
        x1522.setExecutionPath("/studentsListByDegree.do?method=prepareByDegree");
        Functionality x1524 = new Functionality(MultiLanguageString.importFromString("pt21:Alunos por Disciplina"));
        x1523.addChild(x1524);
        x1524.setTitle(MultiLanguageString.importFromString("pt21:Alunos por Disciplina"));
        x1524.setNormalizedName(MultiLanguageString.importFromString("pt21:alunos-por-disciplina"));
        x1524.setExecutionPath("/studentsListByCurricularCourse.do?method=prepareByCurricularCourse");
        Module x1527 = new Module(MultiLanguageString.importFromString("pt7:erasmus"), "/");
        x1517.addChild(x1527);
        x1527.setTitle(MultiLanguageString.importFromString("pt7:erasmus"));
        x1527.setDescription(MultiLanguageString.importFromString("pt7:erasmus"));
        x1527.setNormalizedName(MultiLanguageString.importFromString("pt7:erasmus"));
        Functionality x1526 = new Functionality(MultiLanguageString.importFromString("pt7:Erasmus"));
        x1527.addChild(x1526);
        x1526.setTitle(MultiLanguageString.importFromString("pt7:Erasmus"));
        x1526.setDescription(MultiLanguageString.importFromString("pt7:Erasmus"));
        x1526.setNormalizedName(MultiLanguageString.importFromString("pt7:erasmus"));
        x1526.setExecutionPath("/caseHandlingMobilityApplicationProcess.do?method=intro");
        Functionality x1529 = new Functionality(MultiLanguageString.importFromString("pt12:Tabelas ECTS"));
        x1517.addChild(x1529);
        x1529.setTitle(MultiLanguageString.importFromString("pt10:ECTSTables"));
        x1529.setNormalizedName(MultiLanguageString.importFromString("pt12:tabelas-ects"));
        x1529.setExecutionPath("searchEctsComparabilityTables.do?method=index");
        Module x1532 =
                new Module(MultiLanguageString.importFromString("pt35:Gestão da Cartões de Indentificação"),
                        "/identificationCardManager");
        x823.addChild(x1532);
        x1532.setNormalizedName(MultiLanguageString.importFromString("pt35:gestao-da-cartoes-de-indentificacao"));
        Functionality x1531 = new Functionality(MultiLanguageString.importFromString("en8:Homepagept14:Página Inicial"));
        x1532.addChild(x1531);
        x1531.setNormalizedName(MultiLanguageString.importFromString("en8:homepagept14:pagina-inicial"));
        x1531.setExecutionPath("/index.do");
        x1531.setVisible(true);
        Functionality x1533 = new Functionality(MultiLanguageString.importFromString("pt9:Pesquisar"));
        x1532.addChild(x1533);
        x1533.setNormalizedName(MultiLanguageString.importFromString("pt9:pesquisar"));
        x1533.setExecutionPath("/searchPeople.do?method=search");
        Module x1536 = new Module(MultiLanguageString.importFromString("pt29:Gabinete de Relações Públicas"), "/publicRelations");
        x823.addChild(x1536);
        x1536.setTitle(MultiLanguageString.importFromString("pt29:Gabinete de Relações Públicas"));
        x1536.setNormalizedName(MultiLanguageString.importFromString("pt29:gabinete-de-relacoes-publicas"));
        Functionality x1535 = new Functionality(MultiLanguageString.importFromString("en5:Startpt6:Início"));
        x1536.addChild(x1535);
        x1535.setTitle(MultiLanguageString.importFromString("pt6:Início"));
        x1535.setNormalizedName(MultiLanguageString.importFromString("en5:startpt6:inicio"));
        x1535.setExecutionPath("/index.do");
        Functionality x1537 = new Functionality(MultiLanguageString.importFromString("en13:Announcementspt8:Anuncios"));
        x1536.addChild(x1537);
        x1537.setTitle(MultiLanguageString.importFromString("pt8:anúncios"));
        x1537.setNormalizedName(MultiLanguageString.importFromString("en13:announcementspt8:anuncios"));
        x1537.setExecutionPath("/announcementsManagement.do?method=viewAllBoards");
        Functionality x1538 = new Functionality(MultiLanguageString.importFromString("en10:Send Emailpt12:Enviar Email"));
        x1536.addChild(x1538);
        x1538.setTitle(MultiLanguageString.importFromString("pt12:Enviar Email"));
        x1538.setNormalizedName(MultiLanguageString.importFromString("en10:send-emailpt12:enviar-email"));
        x1538.setExecutionPath("/sendEmail.do?method=prepare");
        Functionality x1539 = new Functionality(MultiLanguageString.importFromString("pt19:Listagens de Alunos"));
        x1536.addChild(x1539);
        x1539.setNormalizedName(MultiLanguageString.importFromString("pt19:listagens-de-alunos"));
        x1539.setExecutionPath("/studentReports.do?method=search");
        Functionality x1540 = new Functionality(MultiLanguageString.importFromString("pt13:Gerir Membros"));
        x1536.addChild(x1540);
        x1540.setNormalizedName(MultiLanguageString.importFromString("pt13:gerir-membros"));
        x1540.setExecutionPath("/managePublicRelationsPeople.do?method=managePeople");
        Functionality x1541 = new Functionality(MultiLanguageString.importFromString("pt10:Inquéritos"));
        x1536.addChild(x1541);
        x1541.setNormalizedName(MultiLanguageString.importFromString("pt10:inqueritos"));
        x1541.setExecutionPath("/alumniCerimony.do?method=manage");
        Module x1543 = new Module(MultiLanguageString.importFromString("pt6:Alumni"), "/");
        x1536.addChild(x1543);
        x1543.setTitle(MultiLanguageString.importFromString("pt6:Alumni"));
        x1543.setNormalizedName(MultiLanguageString.importFromString("pt6:alumni"));
        Functionality x1542 = new Functionality(MultiLanguageString.importFromString("pt12:Estatísticas"));
        x1543.addChild(x1542);
        x1542.setTitle(MultiLanguageString.importFromString("pt12:Estatísticas"));
        x1542.setNormalizedName(MultiLanguageString.importFromString("pt12:estatisticas"));
        x1542.setExecutionPath("/alumni.do?method=showAlumniStatistics");
        Functionality x1544 = new Functionality(MultiLanguageString.importFromString("pt15:Procurar Alumni"));
        x1543.addChild(x1544);
        x1544.setTitle(MultiLanguageString.importFromString("pt15:Procurar Alumni"));
        x1544.setNormalizedName(MultiLanguageString.importFromString("pt15:procurar-alumni"));
        x1544.setExecutionPath("/alumni.do?method=searchAlumni");
        Module x1548 = new Module(MultiLanguageString.importFromString("pt21:Núcleo de Alojamentos"), "/residenceManagement");
        x823.addChild(x1548);
        x1548.setTitle(MultiLanguageString.importFromString("pt21:Núcleo de Alojamentos"));
        x1548.setNormalizedName(MultiLanguageString.importFromString("pt21:nucleo-de-alojamentos"));
        Functionality x1547 = new Functionality(MultiLanguageString.importFromString("en8:Homepagept14:Página Inicial"));
        x1548.addChild(x1547);
        x1547.setTitle(MultiLanguageString.importFromString("en8:Homepagept14:Página Inicial"));
        x1547.setNormalizedName(MultiLanguageString.importFromString("en8:homepagept14:pagina-inicial"));
        x1547.setExecutionPath("/index.do");
        Functionality x1549 = new Functionality(MultiLanguageString.importFromString("pt13:Gerir Dúvidas"));
        x1548.addChild(x1549);
        x1549.setTitle(MultiLanguageString.importFromString("pt13:Gerir Dúvidas"));
        x1549.setNormalizedName(MultiLanguageString.importFromString("pt13:gerir-dividas"));
        x1549.setExecutionPath("/residenceEventManagement.do?method=manageResidenceEvents");
        Functionality x1550 = new Functionality(MultiLanguageString.importFromString("pt21:Gestão de Previlégios"));
        x1548.addChild(x1550);
        x1550.setTitle(MultiLanguageString.importFromString("pt21:Gestão de Previlégios"));
        x1550.setNormalizedName(MultiLanguageString.importFromString("pt21:gestao-de-previlegios"));
        x1550.setExecutionPath("/residenceRoleManagement.do?method=residencePersonsManagement");
        Module x1553 = new Module(MultiLanguageString.importFromString("pt12:Projectos IT"), "/itProjectsManagement");
        x823.addChild(x1553);
        x1553.setTitle(MultiLanguageString.importFromString("pt12:Projectos IT"));
        x1553.setNormalizedName(MultiLanguageString.importFromString("pt12:projectos-it"));
        Functionality x1552 = new Functionality(MultiLanguageString.importFromString("pt12:Projectos IT"));
        x1553.addChild(x1552);
        x1552.setTitle(MultiLanguageString.importFromString("pt12:Projectos IT"));
        x1552.setNormalizedName(MultiLanguageString.importFromString("pt12:projectos-it"));
        x1552.setExecutionPath("/index.do?backendInstance=IT");
        ExpressionGroupAvailability x1660 = new ExpressionGroupAvailability(x1553, "role(IT_PROJECTS_MANAGER)");
        x1660.setTargetGroup(Group.fromString("role(IT_PROJECTS_MANAGER)"));
        x1553.setAvailabilityPolicy(x1660);
        Module x1556 = new Module(MultiLanguageString.importFromString("pt18:Supervisão Externa"), "/externalSupervision");
        x823.addChild(x1556);
        x1556.setNormalizedName(MultiLanguageString.importFromString("pt18:supervisao-externa"));
        Functionality x1555 = new Functionality(MultiLanguageString.importFromString("en8:Homepagept14:Página Inicial"));
        x1556.addChild(x1555);
        x1555.setNormalizedName(MultiLanguageString.importFromString("en8:homepagept14:pagina-inicial"));
        x1555.setExecutionPath("/welcome.do?method=welcome");
        Module x1558 = new Module(MultiLanguageString.importFromString("pt9:Consultar"), "/");
        x1556.addChild(x1558);
        x1558.setNormalizedName(MultiLanguageString.importFromString("pt9:consultar"));
        Functionality x1557 = new Functionality(MultiLanguageString.importFromString("pt5:Aluno"));
        x1558.addChild(x1557);
        x1557.setNormalizedName(MultiLanguageString.importFromString("pt5:aluno"));
        x1557.setExecutionPath("/viewStudent.do?method=beginTaskFlow");
        Functionality x1559 = new Functionality(MultiLanguageString.importFromString("pt5:Curso"));
        x1558.addChild(x1559);
        x1559.setNormalizedName(MultiLanguageString.importFromString("pt5:curso"));
        x1559.setExecutionPath("/viewDegree.do?method=beginTaskFlow");
        Functionality x1560 = new Functionality(MultiLanguageString.importFromString("pt11:Ano Lectivo"));
        x1558.addChild(x1560);
        x1560.setNormalizedName(MultiLanguageString.importFromString("pt11:ano-lectivo"));
        x1560.setExecutionPath("/viewYear.do?method=beginTaskFlow");
        Module x1564 = new Module(MultiLanguageString.importFromString("pt4:NAPE"), "/nape");
        x823.addChild(x1564);
        x1564.setTitle(MultiLanguageString.importFromString("pt4:NAPE"));
        x1564.setNormalizedName(MultiLanguageString.importFromString("pt4:nape"));
        Functionality x1563 = new Functionality(MultiLanguageString.importFromString("pt6:Inicio"));
        x1564.addChild(x1563);
        x1563.setTitle(MultiLanguageString.importFromString("pt6:Inicio"));
        x1563.setDescription(MultiLanguageString.importFromString("pt6:Inicio"));
        x1563.setNormalizedName(MultiLanguageString.importFromString("pt6:inicio"));
        x1563.setExecutionPath("index.do");
        Module x1566 = new Module(MultiLanguageString.importFromString("pt12:Candidaturas"), "/");
        x1564.addChild(x1566);
        x1566.setTitle(MultiLanguageString.importFromString("pt12:Candidaturas"));
        x1566.setDescription(MultiLanguageString.importFromString("pt12:Candidaturas"));
        x1566.setNormalizedName(MultiLanguageString.importFromString("pt12:candidaturas"));
        Functionality x1565 = new Functionality(MultiLanguageString.importFromString("pt13:Maiores de 23"));
        x1566.addChild(x1565);
        x1565.setTitle(MultiLanguageString.importFromString("pt13:Maiores de 23"));
        x1565.setDescription(MultiLanguageString.importFromString("pt13:Maiores de 23"));
        x1565.setNormalizedName(MultiLanguageString.importFromString("pt13:maiores-de-23"));
        x1565.setExecutionPath("/caseHandlingOver23CandidacyProcess.do?method=intro");
        Functionality x1567 = new Functionality(MultiLanguageString.importFromString("pt13:Segundo Ciclo"));
        x1566.addChild(x1567);
        x1567.setTitle(MultiLanguageString.importFromString("pt13:Segundo Ciclo"));
        x1567.setDescription(MultiLanguageString.importFromString("pt13:Segundo Ciclo"));
        x1567.setNormalizedName(MultiLanguageString.importFromString("pt13:segundo-ciclo"));
        x1567.setExecutionPath("/caseHandlingSecondCycleCandidacyProcess.do?method=intro");
        Functionality x1568 = new Functionality(MultiLanguageString.importFromString("pt14:Transferências"));
        x1566.addChild(x1568);
        x1568.setTitle(MultiLanguageString.importFromString("pt14:Transferências"));
        x1568.setDescription(MultiLanguageString.importFromString("pt14:Transferências"));
        x1568.setNormalizedName(MultiLanguageString.importFromString("pt14:transferencias"));
        x1568.setExecutionPath("caseHandlingDegreeTransferCandidacyProcess.do?method=intro");
        Functionality x1569 = new Functionality(MultiLanguageString.importFromString("pt17:Mudanças de curso"));
        x1566.addChild(x1569);
        x1569.setTitle(MultiLanguageString.importFromString("pt17:Mudanças de curso"));
        x1569.setDescription(MultiLanguageString.importFromString("pt17:Mudanças de curso"));
        x1569.setNormalizedName(MultiLanguageString.importFromString("pt17:mudancas-de-curso"));
        x1569.setExecutionPath("/caseHandlingDegreeChangeCandidacyProcess.do?method=intro");
        Functionality x1570 =
                new Functionality(MultiLanguageString.importFromString("pt45:Titulares de cursos de grau médio ou superior"));
        x1566.addChild(x1570);
        x1570.setTitle(MultiLanguageString.importFromString("pt45:Titulares de cursos de grau médio ou superior"));
        x1570.setDescription(MultiLanguageString.importFromString("pt45:Titulares de cursos de grau médio ou superior"));
        x1570.setNormalizedName(MultiLanguageString.importFromString("pt45:titulares-de-cursos-de-grau-medio-ou-superior"));
        x1570.setExecutionPath("/caseHandlingDegreeCandidacyForGraduatedPersonProcess.do?method=intro");
        Module x1573 = new Module(MultiLanguageString.importFromString("pt10:Matriculas"), "/");
        x1564.addChild(x1573);
        x1573.setTitle(MultiLanguageString.importFromString("pt10:Matriculas"));
        x1573.setDescription(MultiLanguageString.importFromString("pt10:Matriculas"));
        x1573.setNormalizedName(MultiLanguageString.importFromString("pt10:matriculas"));
        Functionality x1572 = new Functionality(MultiLanguageString.importFromString("pt33:Alunos Matriculados 1ª ano 1ª vez"));
        x1573.addChild(x1572);
        x1572.setTitle(MultiLanguageString.importFromString("pt33:Alunos Matriculados 1ª ano 1ª vez"));
        x1572.setDescription(MultiLanguageString.importFromString("pt33:Alunos Matriculados 1ª ano 1ª vez"));
        x1572.setNormalizedName(MultiLanguageString.importFromString("pt31:alunos-matriculados-1-ano-1-vez"));
        x1572.setExecutionPath("/registeredDegreeCandidacies.do?method=view");
        Module x1577 = new Module(MultiLanguageString.importFromString("pt8:external"), "/external");
        x823.addChild(x1577);
        x1577.setTitle(MultiLanguageString.importFromString("pt8:external"));
        x1577.setDescription(MultiLanguageString.importFromString("pt8:external"));
        x1577.setNormalizedName(MultiLanguageString.importFromString("pt8:external"));
        Functionality x1576 = new Functionality(MultiLanguageString.importFromString("pt4:list"));
        x1577.addChild(x1576);
        x1576.setTitle(MultiLanguageString.importFromString("pt4:list"));
        x1576.setDescription(MultiLanguageString.importFromString("pt4:list"));
        x1576.setNormalizedName(MultiLanguageString.importFromString("pt4:list"));
        x1576.setExecutionPath("/epflCandidateInformation.do");
        Functionality x1578 = new Functionality(MultiLanguageString.importFromString("pt4:show"));
        x1577.addChild(x1578);
        x1578.setTitle(MultiLanguageString.importFromString("pt4:show"));
        x1578.setDescription(MultiLanguageString.importFromString("pt4:show"));
        x1578.setNormalizedName(MultiLanguageString.importFromString("pt4:show"));
        x1578.setExecutionPath("/epflCandidateInformation.do?method=displayCandidatePage");
        Functionality x1579 = new Functionality(MultiLanguageString.importFromString("pt5:login"));
        x1577.addChild(x1579);
        x1579.setTitle(MultiLanguageString.importFromString("pt5:login"));
        x1579.setDescription(MultiLanguageString.importFromString("pt5:login"));
        x1579.setNormalizedName(MultiLanguageString.importFromString("pt5:login"));
        x1579.setExecutionPath("phdIndividualProgramProcess/loginPage.jsp");
        Functionality x1580 = new Functionality(MultiLanguageString.importFromString("pt13:notAuthorized"));
        x1577.addChild(x1580);
        x1580.setTitle(MultiLanguageString.importFromString("pt13:notAuthorized"));
        x1580.setDescription(MultiLanguageString.importFromString("pt13:notAuthorized"));
        x1580.setNormalizedName(MultiLanguageString.importFromString("pt13:notauthorized"));
        x1580.setExecutionPath("phdIndividualProgramProcess/notAuthorized.jsp");
        Functionality x1581 = new Functionality(MultiLanguageString.importFromString("pt7:referee"));
        x1577.addChild(x1581);
        x1581.setTitle(MultiLanguageString.importFromString("pt7:referee"));
        x1581.setDescription(MultiLanguageString.importFromString("pt7:referee"));
        x1581.setNormalizedName(MultiLanguageString.importFromString("pt7:referee"));
        x1581.setExecutionPath("epflCandidateInformation.do?method=displayRefereePage");
        Functionality x1582 = new Functionality(MultiLanguageString.importFromString("pt18:candidateDocuments"));
        x1577.addChild(x1582);
        x1582.setTitle(MultiLanguageString.importFromString("pt18:candidateDocuments"));
        x1582.setDescription(MultiLanguageString.importFromString("pt18:candidateDocuments"));
        x1582.setNormalizedName(MultiLanguageString.importFromString("pt18:candidatedocuments"));
        x1582.setExecutionPath("epflCandidateInformation.do?method=downloadCandidateDocuments");
        Functionality x1583 = new Functionality(MultiLanguageString.importFromString("pt5:photo"));
        x1577.addChild(x1583);
        x1583.setTitle(MultiLanguageString.importFromString("pt5:photo"));
        x1583.setDescription(MultiLanguageString.importFromString("pt5:photo"));
        x1583.setNormalizedName(MultiLanguageString.importFromString("pt5:photo"));
        x1583.setExecutionPath("epflCandidateInformation.do?method=displayPhoto");
        Functionality x1584 = new Functionality(MultiLanguageString.importFromString("pt3:xml"));
        x1577.addChild(x1584);
        x1584.setTitle(MultiLanguageString.importFromString("pt3:xml"));
        x1584.setDescription(MultiLanguageString.importFromString("pt3:xml"));
        x1584.setNormalizedName(MultiLanguageString.importFromString("pt3:xml"));
        x1584.setExecutionPath("epflCandidateInformation.do?method=exportInformationXml");
        Module x1587 = new Module(MultiLanguageString.importFromString("pt8:Reitoria"), "/rectorate");
        x823.addChild(x1587);
        x1587.setTitle(MultiLanguageString.importFromString("pt8:Reitoria"));
        x1587.setNormalizedName(MultiLanguageString.importFromString("pt8:reitoria"));
        Functionality x1586 = new Functionality(MultiLanguageString.importFromString("pt15:Lotes Recebidos"));
        x1587.addChild(x1586);
        x1586.setTitle(MultiLanguageString.importFromString("pt15:Lotes Recebidos"));
        x1586.setDescription(MultiLanguageString.importFromString("pt15:Lotes Recebidos"));
        x1586.setNormalizedName(MultiLanguageString.importFromString("pt15:lotes-recebidos"));
        x1586.setExecutionPath("rectorateIncomingBatches.do?method=index");
        ExpressionGroupAvailability x1661 = new ExpressionGroupAvailability(x1587, "role(RECTORATE)");
        x1661.setTargetGroup(Group.fromString("role(RECTORATE)"));
        x1587.setAvailabilityPolicy(x1661);
        Module x1590 = new Module(MultiLanguageString.importFromString("pt16:Micro-Pagamentos"), "/microPayments");
        x823.addChild(x1590);
        x1590.setNormalizedName(MultiLanguageString.importFromString("pt16:micro-pagamentos"));
        Functionality x1589 = new Functionality(MultiLanguageString.importFromString("pt6:Início"));
        x1590.addChild(x1589);
        x1589.setNormalizedName(MultiLanguageString.importFromString("pt6:inicio"));
        x1589.setExecutionPath("/operator.do?method=startPage");
        Module x1593 = new Module(MultiLanguageString.importFromString("pt16:Projectos IST-ID"), "/istidProjectsManagement");
        x823.addChild(x1593);
        x1593.setTitle(MultiLanguageString.importFromString("pt16:Projectos IST-ID"));
        x1593.setNormalizedName(MultiLanguageString.importFromString("pt16:projectos-ist-id"));
        Functionality x1592 = new Functionality(MultiLanguageString.importFromString("pt16:Projectos IST-ID"));
        x1593.addChild(x1592);
        x1592.setTitle(MultiLanguageString.importFromString("pt16:Projectos IST-ID"));
        x1592.setNormalizedName(MultiLanguageString.importFromString("pt16:projectos-ist-id"));
        x1592.setExecutionPath("/index.do?backendInstance=IST_ID");
        ExpressionGroupAvailability x1662 = new ExpressionGroupAvailability(x1593, "role(ISTID_PROJECTS_MANAGER)");
        x1662.setTargetGroup(Group.fromString("role(ISTID_PROJECTS_MANAGER)"));
        x1593.setAvailabilityPolicy(x1662);
        Module x1596 =
                new Module(MultiLanguageString.importFromString("pt34:Projectos Institucionais do IST-ID"),
                        "/istidInstitucionalProjectsManagement");
        x823.addChild(x1596);
        x1596.setTitle(MultiLanguageString.importFromString("pt34:Projectos Institucionais do IST-ID"));
        x1596.setNormalizedName(MultiLanguageString.importFromString("pt34:projectos-institucionais-do-ist-id"));
        Functionality x1595 = new Functionality(MultiLanguageString.importFromString("pt34:Projectos Institucionais do IST-ID"));
        x1596.addChild(x1595);
        x1595.setTitle(MultiLanguageString.importFromString("pt34:Projectos Institucionais do IST-ID"));
        x1595.setNormalizedName(MultiLanguageString.importFromString("pt34:projectos-institucionais-do-ist-id"));
        x1595.setExecutionPath("/istidInstitucionalProjectIndex.do?backendInstance=IST_ID");
        ExpressionGroupAvailability x1663 = new ExpressionGroupAvailability(x1596, "role(ISTID_INSTITUCIONAL_PROJECTS_MANAGER)");
        x1663.setTargetGroup(Group.fromString("role(ISTID_INSTITUCIONAL_PROJECTS_MANAGER)"));
        x1596.setAvailabilityPolicy(x1663);
        Module x1599 = new Module(MultiLanguageString.importFromString("pt15:Projectos ADIST"), "/adistProjectsManagement");
        x823.addChild(x1599);
        x1599.setTitle(MultiLanguageString.importFromString("pt15:Projectos ADIST"));
        x1599.setNormalizedName(MultiLanguageString.importFromString("pt15:projectos-adist"));
        Functionality x1598 = new Functionality(MultiLanguageString.importFromString("pt15:Projectos ADIST"));
        x1599.addChild(x1598);
        x1598.setTitle(MultiLanguageString.importFromString("pt15:Projectos ADIST"));
        x1598.setNormalizedName(MultiLanguageString.importFromString("pt15:projectos-adist"));
        x1598.setExecutionPath("/index.do?backendInstance=ADIST");
        ExpressionGroupAvailability x1664 = new ExpressionGroupAvailability(x1599, "role(ADIST_PROJECTS_MANAGER)");
        x1664.setTargetGroup(Group.fromString("role(ADIST_PROJECTS_MANAGER)"));
        x1599.setAvailabilityPolicy(x1664);
        Module x1602 =
                new Module(MultiLanguageString.importFromString("pt33:Projectos Institucionais da ADIST"),
                        "/adistInstitucionalProjectsManagement");
        x823.addChild(x1602);
        x1602.setTitle(MultiLanguageString.importFromString("pt33:Projectos Institucionais da ADIST"));
        x1602.setNormalizedName(MultiLanguageString.importFromString("pt33:projectos-institucionais-da-adist"));
        Functionality x1601 = new Functionality(MultiLanguageString.importFromString("pt33:Projectos Institucionais da ADIST"));
        x1602.addChild(x1601);
        x1601.setTitle(MultiLanguageString.importFromString("pt33:Projectos Institucionais da ADIST"));
        x1601.setNormalizedName(MultiLanguageString.importFromString("pt33:projectos-institucionais-da-adist"));
        x1601.setExecutionPath("/adistInstitucionalProjectIndex.do?backendInstance=ADIST");
        ExpressionGroupAvailability x1665 = new ExpressionGroupAvailability(x1602, "role(ADIST_INSTITUCIONAL_PROJECTS_MANAGER)");
        x1665.setTargetGroup(Group.fromString("role(ADIST_INSTITUCIONAL_PROJECTS_MANAGER)"));
        x1602.setAvailabilityPolicy(x1665);

        // DEBUG: tldNode x3 3487513486524 null
        Portal x3 = new Portal();
        Section x2 = new Section(x3, MultiLanguageString.importFromString("en8:Personalpt7:Pessoal"));
        x2.setName(MultiLanguageString.importFromString("en8:Personalpt7:Pessoal"));
        x2.setNormalizedName(MultiLanguageString.importFromString("en8:personalpt7:pessoal"));
        x2.setMaximizable(false);
        FunctionalityCall x1 = makeFunctionalityCall(x2, x821, "pt7:Pessoal", null, null, null, "pt7:pessoal");
        ExpressionGroupAvailability x1667 = new ExpressionGroupAvailability(x2, "role(PERSON)");
        x1667.setTargetGroup(Group.fromString("role(PERSON)"));
        x2.setAvailabilityPolicy(x1667);
        Section x6 = new Section(x3, MultiLanguageString.importFromString("pt9:Estudante"));
        x6.setName(MultiLanguageString.importFromString("pt9:Estudante"));
        x6.setNormalizedName(MultiLanguageString.importFromString("pt9:estudante"));
        FunctionalityCall x5 = makeFunctionalityCall(x6, x825, "pt9:Estudante", null, null, null, "pt9:estudante");
        ExpressionGroupAvailability x1668 = new ExpressionGroupAvailability(x6, "role(STUDENT)");
        x1668.setTargetGroup(Group.fromString("role(STUDENT)"));
        x6.setAvailabilityPolicy(x1668);
        Section x9 = new Section(x3, MultiLanguageString.importFromString("en7:Teacherpt8:Docência"));
        x9.setName(MultiLanguageString.importFromString("en7:Teacherpt8:Docência"));
        x9.setNormalizedName(MultiLanguageString.importFromString("en7:teacherpt8:docencia"));
        x9.setMaximizable(false);
        FunctionalityCall x8 = makeFunctionalityCall(x9, x828, "pt8:Docência", null, null, null, "pt8:docencia");
        ExpressionGroupAvailability x1669 = new ExpressionGroupAvailability(x8, "internalOrExternalTeacher()");
        x1669.setTargetGroup(Group.fromString("internalOrExternalTeacher()"));
        x8.setAvailabilityPolicy(x1669);
        ExpressionGroupAvailability x1670 = new ExpressionGroupAvailability(x9, "internalOrExternalTeacher()");
        x1670.setTargetGroup(Group.fromString("internalOrExternalTeacher()"));
        x9.setAvailabilityPolicy(x1670);
        Section x12 = new Section(x3, MultiLanguageString.importFromString("en10:Departmentpt12:Departamento"));
        x12.setName(MultiLanguageString.importFromString("en10:Departmentpt12:Departamento"));
        x12.setNormalizedName(MultiLanguageString.importFromString("en10:departmentpt12:departamento"));
        x12.setMaximizable(false);
        FunctionalityCall x11 = makeFunctionalityCall(x12, x831, "pt12:Departamento", null, null, null, "pt12:departamento");
        ExpressionGroupAvailability x1671 = new ExpressionGroupAvailability(x12, "role(DEPARTMENT_MEMBER)");
        x1671.setTargetGroup(Group.fromString("role(DEPARTMENT_MEMBER)"));
        x12.setAvailabilityPolicy(x1671);
        Section x15 = new Section(x3, MultiLanguageString.importFromString("pt18:Gestão de Recursos"));
        x15.setName(MultiLanguageString.importFromString("pt18:Gestão de Recursos"));
        x15.setNormalizedName(MultiLanguageString.importFromString("pt18:gestao-de-recursos"));
        FunctionalityCall x14 =
                makeFunctionalityCall(x15, x834, "pt18:Gestão de Recursos", null, null, null, "pt18:gestao-de-recursos");
        ExpressionGroupAvailability x1672 = new ExpressionGroupAvailability(x15, "role(RESOURCE_ALLOCATION_MANAGER)");
        x1672.setTargetGroup(Group.fromString("role(RESOURCE_ALLOCATION_MANAGER)"));
        x15.setAvailabilityPolicy(x1672);
        Section x18 = new Section(x3, MultiLanguageString.importFromString("pt36:Portal do Candidato a Pós-Graduações"));
        x18.setName(MultiLanguageString.importFromString("pt36:Portal do Candidato a Pós-Graduações"));
        x18.setNormalizedName(MultiLanguageString.importFromString("pt36:portal-do-candidato-a-pos-graduacoes"));
        FunctionalityCall x17 =
                makeFunctionalityCall(x18, x837, "pt36:Portal do Candidato a Pós-Graduações", null, null, null,
                        "pt36:portal-do-candidato-a-pos-graduacoes");
        ExpressionGroupAvailability x1673 = new ExpressionGroupAvailability(x18, "role(MASTER_DEGREE_CANDIDATE)");
        x1673.setTargetGroup(Group.fromString("role(MASTER_DEGREE_CANDIDATE)"));
        x18.setAvailabilityPolicy(x1673);
        Section x21 = new Section(x3, MultiLanguageString.importFromString("pt35:Portal de Serviços de Pós-Graduação"));
        x21.setName(MultiLanguageString.importFromString("pt35:Portal de Serviços de Pós-Graduação"));
        x21.setNormalizedName(MultiLanguageString.importFromString("pt35:portal-de-servicos-de-pos-graduacao"));
        FunctionalityCall x20 =
                makeFunctionalityCall(x21, x840, "pt35:Portal de Serviços de Pós-Graduação", null, null, null,
                        "pt35:portal-de-servicos-de-pos-graduacao");
        ExpressionGroupAvailability x1674 = new ExpressionGroupAvailability(x21, "role(MASTER_DEGREE_ADMINISTRATIVE_OFFICE)");
        x1674.setTargetGroup(Group.fromString("role(MASTER_DEGREE_ADMINISTRATIVE_OFFICE)"));
        x21.setAvailabilityPolicy(x1674);
        Section x24 = new Section(x3, MultiLanguageString.importFromString("pt11:Coordenador"));
        x24.setName(MultiLanguageString.importFromString("pt11:Coordenador"));
        x24.setNormalizedName(MultiLanguageString.importFromString("pt11:coordenador"));
        FunctionalityCall x23 = makeFunctionalityCall(x24, x843, "pt11:Coordenador", null, null, null, "pt11:coordenador");
        ExpressionGroupAvailability x1675 = new ExpressionGroupAvailability(x24, "role(COORDINATOR)");
        x1675.setTargetGroup(Group.fromString("role(COORDINATOR)"));
        x24.setAvailabilityPolicy(x1675);
        Section x27 = new Section(x3, MultiLanguageString.importFromString("pt11:Funcionário"));
        x27.setName(MultiLanguageString.importFromString("pt11:Funcionário"));
        x27.setNormalizedName(MultiLanguageString.importFromString("pt11:funcionario"));
        FunctionalityCall x26 = makeFunctionalityCall(x27, x846, "pt11:Funcionário", null, null, null, "pt11:funcionario");
        ExpressionGroupAvailability x1676 = new ExpressionGroupAvailability(x27, "role(EMPLOYEE)");
        x1676.setTargetGroup(Group.fromString("role(EMPLOYEE)"));
        x27.setAvailabilityPolicy(x1676);
        Section x31 = new Section(x3, MultiLanguageString.importFromString("pt15:Àrea de Pessoal"));
        x31.setName(MultiLanguageString.importFromString("pt15:Àrea de Pessoal"));
        x31.setNormalizedName(MultiLanguageString.importFromString("pt15:area-de-pessoal"));
        Section x30 = new Section(x31, MultiLanguageString.importFromString("pt25:Consultas por Funcionário"));
        x30.setName(MultiLanguageString.importFromString("pt25:Consultas por Funcionário"));
        x30.setNormalizedName(MultiLanguageString.importFromString("pt25:consultas-por-funcionario"));
        FunctionalityCall x29 = makeFunctionalityCall(x30, x849, "pt7:Verbete", null, null, null, "pt7:verbete");
        FunctionalityCall x32 = makeFunctionalityCall(x30, x852, "pt7:Horário", null, null, null, "pt7:horario");
        FunctionalityCall x33 = makeFunctionalityCall(x30, x853, "pt9:Marcações", null, null, null, "pt9:marcacoes");
        FunctionalityCall x34 = makeFunctionalityCall(x30, x854, "pt13:Justificações", null, null, null, "pt13:justificacoes");
        FunctionalityCall x35 = makeFunctionalityCall(x30, x855, "pt12:Resumo Saldo", null, null, null, "pt12:resumo-saldo");
        FunctionalityCall x36 = makeFunctionalityCall(x30, x856, "pt6:Férias", null, null, null, "pt6:ferias");
        FunctionalityCall x37 = makeFunctionalityCall(x30, x857, "pt6:Status", null, null, null, "pt6:status");
        ExpressionGroupAvailability x1677 =
                new ExpressionGroupAvailability(x30, "assiduousnessSectionStaff || assiduousnessManagers");
        x1677.setTargetGroup(Group.fromString("assiduousnessSectionStaff || assiduousnessManagers"));
        x30.setAvailabilityPolicy(x1677);
        Section x40 = new Section(x31, MultiLanguageString.importFromString("pt21:Todos os Funcionários"));
        x40.setName(MultiLanguageString.importFromString("pt21:Todos os Funcionários"));
        x40.setNormalizedName(MultiLanguageString.importFromString("pt21:todos-os-funcionarios"));
        FunctionalityCall x39 =
                makeFunctionalityCall(x40, x860, "pt20:Inserir Justificação", null, null, null, "pt20:inserir-justificacao");
        ExpressionGroupAvailability x1678 = new ExpressionGroupAvailability(x40, "assiduousnessManagers");
        x1678.setTargetGroup(Group.fromString("assiduousnessManagers"));
        x40.setAvailabilityPolicy(x1678);
        Section x43 = new Section(x31, MultiLanguageString.importFromString("pt9:Consultas"));
        x43.setName(MultiLanguageString.importFromString("pt9:Consultas"));
        x43.setNormalizedName(MultiLanguageString.importFromString("pt9:consultas"));
        FunctionalityCall x42 = makeFunctionalityCall(x43, x863, "pt8:Horários", null, null, null, "pt8:horarios");
        FunctionalityCall x44 = makeFunctionalityCall(x43, x865, "pt13:Justificações", null, null, null, "pt13:justificacoes");
        FunctionalityCall x45 = makeFunctionalityCall(x43, x866, "pt14:Regularizações", null, null, null, "pt14:regularizacoes");
        FunctionalityCall x46 = makeFunctionalityCall(x43, x867, "pt11:Tolerâncias", null, null, null, "pt11:tolerancias");
        FunctionalityCall x47 = makeFunctionalityCall(x43, x868, "pt6:Status", null, null, null, "pt6:status");
        ExpressionGroupAvailability x1679 =
                new ExpressionGroupAvailability(x43, "assiduousnessSectionStaff || assiduousnessManagers");
        x1679.setTargetGroup(Group.fromString("assiduousnessSectionStaff || assiduousnessManagers"));
        x43.setAvailabilityPolicy(x1679);
        Section x50 = new Section(x31, MultiLanguageString.importFromString("pt8:Exportar"));
        x50.setName(MultiLanguageString.importFromString("pt8:Exportar"));
        x50.setNormalizedName(MultiLanguageString.importFromString("pt8:exportar"));
        FunctionalityCall x49 =
                makeFunctionalityCall(x50, x870, "pt17:Exportar Verbetes", null, null, null, "pt17:exportar-verbetes");
        FunctionalityCall x51 =
                makeFunctionalityCall(x50, x872, "pt16:Exportar Resumos", null, null, null, "pt16:exportar-resumos");
        FunctionalityCall x52 =
                makeFunctionalityCall(x50, x873, "pt22:Exportar Justificações", null, null, null, "pt22:exportar-justificacoes");
        FunctionalityCall x53 =
                makeFunctionalityCall(x50, x874, "pt19:Exportar Destacados", null, null, null, "pt19:exportar-destacados");
        FunctionalityCall x54 = makeFunctionalityCall(x50, x875, "pt14:Exportar ADIST", null, null, null, "pt14:exportar-adist");
        FunctionalityCall x55 =
                makeFunctionalityCall(x50, x876, "pt15:Exportar Férias", null, null, null, "pt15:exportar-ferias");
        ExpressionGroupAvailability x1680 =
                new ExpressionGroupAvailability(x50, "assiduousnessSectionStaff || assiduousnessManagers");
        x1680.setTargetGroup(Group.fromString("assiduousnessSectionStaff || assiduousnessManagers"));
        x50.setAvailabilityPolicy(x1680);
        Section x58 = new Section(x31, MultiLanguageString.importFromString("pt12:Fecho do Mês"));
        x58.setName(MultiLanguageString.importFromString("pt12:Fecho do Mês"));
        x58.setNormalizedName(MultiLanguageString.importFromString("pt12:fecho-do-mes"));
        FunctionalityCall x57 =
                makeFunctionalityCall(x58, x878, "pt21:Fechar Mês e Exportar", null, null, null, "pt21:fechar-mes-e-exportar");
        ExpressionGroupAvailability x1681 = new ExpressionGroupAvailability(x58, "assiduousnessManagers");
        x1681.setTargetGroup(Group.fromString("assiduousnessManagers"));
        x58.setAvailabilityPolicy(x1681);
        Section x61 = new Section(x31, MultiLanguageString.importFromString("pt5:Bónus"));
        x61.setName(MultiLanguageString.importFromString("pt5:Bónus"));
        x61.setNormalizedName(MultiLanguageString.importFromString("pt5:bonus"));
        FunctionalityCall x60 =
                makeFunctionalityCall(x61, x891, "pt17:Prestações Anuais", null, null, null, "pt17:prestacoes-anuais");
        FunctionalityCall x62 = makeFunctionalityCall(x61, x893, "pt14:Importar Bónus", null, null, null, "pt14:importar-bonus");
        FunctionalityCall x63 =
                makeFunctionalityCall(x61, x894, "pt15:Consultar Bónus", null, null, null, "pt15:consultar-bonus");
        ExpressionGroupAvailability x1682 = new ExpressionGroupAvailability(x61, "payrollSectionStaff");
        x1682.setTargetGroup(Group.fromString("payrollSectionStaff"));
        x61.setAvailabilityPolicy(x1682);
        Section x66 = new Section(x31, MultiLanguageString.importFromString("pt6:Férias"));
        x66.setName(MultiLanguageString.importFromString("pt6:Férias"));
        x66.setNormalizedName(MultiLanguageString.importFromString("pt6:ferias"));
        FunctionalityCall x65 =
                makeFunctionalityCall(x66, x897, "pt18:Calcular A17 e A18", null, null, null, "pt18:calcular-a17-e-a18");
        ExpressionGroupAvailability x1683 =
                new ExpressionGroupAvailability(x66, "assiduousnessSectionStaff || assiduousnessManagers");
        x1683.setTargetGroup(Group.fromString("assiduousnessSectionStaff || assiduousnessManagers"));
        x66.setAvailabilityPolicy(x1683);
        Section x70 = new Section(x31, MultiLanguageString.importFromString("pt23:Trabalho Extraordinário"));
        x70.setName(MultiLanguageString.importFromString("pt23:Trabalho Extraordinário"));
        x70.setNormalizedName(MultiLanguageString.importFromString("pt23:trabalho-extraordinario"));
        x70.setMaximizable(false);
        Section x69 = new Section(x70, MultiLanguageString.importFromString("pt23:Trabalho Extraordinário"));
        x69.setName(MultiLanguageString.importFromString("pt23:Trabalho Extraordinário"));
        x69.setNormalizedName(MultiLanguageString.importFromString("pt23:trabalho-extraordinario"));
        x69.setMaximizable(true);
        FunctionalityCall x68 =
                makeFunctionalityCall(x69, x881, "pt19:Inserir Autorização", null, null, null, "pt19:inserir-autorizacao");
        FunctionalityCall x71 =
                makeFunctionalityCall(x69, x883, "pt16:Ver Autorizações", null, null, null, "pt16:ver-autorizacoes");
        FunctionalityCall x72 =
                makeFunctionalityCall(x69, x884, "pt17:Pedidos Pagamento", null, null, null, "pt17:pedidos-pagamento");
        FunctionalityCall x73 =
                makeFunctionalityCall(x69, x885, "pt19:Verbas Centro Custo", null, null, null, "pt19:verbas-centro-custo");
        FunctionalityCall x74 =
                makeFunctionalityCall(x69, x886, "pt26:Fechar Trab Extraordinário", null, null, null,
                        "pt26:fechar-trab-extraordinario");
        FunctionalityCall x75 =
                makeFunctionalityCall(x69, x887, "pt25:Exportar por Funcionários", null, null, null,
                        "pt25:exportar-por-funcionarios");
        FunctionalityCall x76 =
                makeFunctionalityCall(x69, x888, "pt21:Exportar por Unidades", null, null, null, "pt21:exportar-por-unidades");
        FunctionalityCall x77 =
                makeFunctionalityCall(x69, x896, "pt17:Secção de Pessoal", null, null, null, "pt17:seccao-de-pessoal");
        FunctionalityCall x78 = makeFunctionalityCall(x69, x889, "pt10:Verbete TE", null, null, null, "pt10:verbete-te");
        ExpressionGroupAvailability x1684 = new ExpressionGroupAvailability(x69, "assiduousnessManagers");
        x1684.setTargetGroup(Group.fromString("assiduousnessManagers"));
        x69.setAvailabilityPolicy(x1684);
        ExpressionGroupAvailability x1685 = new ExpressionGroupAvailability(x70, "assiduousnessManagers");
        x1685.setTargetGroup(Group.fromString("assiduousnessManagers"));
        x70.setAvailabilityPolicy(x1685);
        Section x82 = new Section(x31, MultiLanguageString.importFromString("en13:Communicationpt11:Comunicação"));
        x82.setName(MultiLanguageString.importFromString("en13:Communicationpt11:Comunicação"));
        x82.setNormalizedName(MultiLanguageString.importFromString("en13:communicationpt11:comunicacao"));
        FunctionalityCall x81 =
                makeFunctionalityCall(x82, x900, "en6:Groupspt6:Grupos", null, null, null, "en6:groupspt6:grupos");
        FunctionalityCall x83 = makeFunctionalityCall(x82, x902, "pt9:Ficheiros", null, null, null, "pt9:ficheiros");
        Section x86 = new Section(x31, MultiLanguageString.importFromString("pt14:Interface GIAF"));
        x86.setName(MultiLanguageString.importFromString("pt14:Interface GIAF"));
        x86.setNormalizedName(MultiLanguageString.importFromString("pt14:interface-giaf"));
        x86.setMaximizable(false);
        Section x85 = new Section(x86, MultiLanguageString.importFromString("pt14:Interface GIAF"));
        x85.setName(MultiLanguageString.importFromString("pt14:Interface GIAF"));
        x85.setNormalizedName(MultiLanguageString.importFromString("pt14:interface-giaf"));
        x85.setMaximizable(true);
        // A previous node
        x85.addChild(x904);
        // A previous node
        x85.addChild(x906);
        FunctionalityCall x87 =
                makeFunctionalityCall(x86, x907, "pt23:Situações Profissionais", null, null, null, "pt23:situacoes-profissionais");
        ExpressionGroupAvailability x1686 = new ExpressionGroupAvailability(x87, "role(MANAGER)");
        x1686.setTargetGroup(Group.fromString("role(MANAGER)"));
        x87.setAvailabilityPolicy(x1686);
        FunctionalityCall x88 =
                makeFunctionalityCall(x86, x908, "pt24:Categorias Profissionais", null, null, null,
                        "pt24:categorias-profissionais");
        ExpressionGroupAvailability x1687 = new ExpressionGroupAvailability(x88, "role(MANAGER)");
        x1687.setTargetGroup(Group.fromString("role(MANAGER)"));
        x88.setAvailabilityPolicy(x1687);
        FunctionalityCall x89 =
                makeFunctionalityCall(x86, x909, "pt23:Equiparações a Bolseiro", null, null, null, "pt23:equiparacoes-a-bolseiro");
        ExpressionGroupAvailability x1688 = new ExpressionGroupAvailability(x89, "role(MANAGER)");
        x1688.setTargetGroup(Group.fromString("role(MANAGER)"));
        x89.setAvailabilityPolicy(x1688);
        FunctionalityCall x90 =
                makeFunctionalityCall(x86, x910, "pt20:Dispensas de Serviço", null, null, null, "pt20:dispensas-de-servico");
        ExpressionGroupAvailability x1689 = new ExpressionGroupAvailability(x90, "role(MANAGER)");
        x1689.setTargetGroup(Group.fromString("role(MANAGER)"));
        x90.setAvailabilityPolicy(x1689);
        ExpressionGroupAvailability x1690 =
                new ExpressionGroupAvailability(x86, "assiduousnessSectionStaff || assiduousnessManagers");
        x1690.setTargetGroup(Group.fromString("assiduousnessSectionStaff || assiduousnessManagers"));
        x86.setAvailabilityPolicy(x1690);
        FunctionalityCall x92 =
                makeFunctionalityCall(x31, x896, "pt17:Secção de Pessoal", null, null, null, "pt17:seccao-de-pessoal");
        ExpressionGroupAvailability x1691 = new ExpressionGroupAvailability(x31, "role(PERSONNEL_SECTION)");
        x1691.setTargetGroup(Group.fromString("role(PERSONNEL_SECTION)"));
        x31.setAvailabilityPolicy(x1691);
        Section x96 = new Section(x3, MultiLanguageString.importFromString("en13:Administratorpt13:Administrador"));
        x96.setName(MultiLanguageString.importFromString("en13:Administratorpt13:Administrador"));
        x96.setNormalizedName(MultiLanguageString.importFromString("en13:administratorpt13:administrador"));
        x96.setMaximizable(false);
        Section x95 = new Section(x96, MultiLanguageString.importFromString("en21:Messages and Warningspt18:Mensagens e Avisos"));
        x95.setName(MultiLanguageString.importFromString("en21:Messages and Warningspt18:Mensagens e Avisos"));
        x95.setNormalizedName(MultiLanguageString.importFromString("en21:messages-and-warningspt18:mensagens-e-avisos"));
        FunctionalityCall x94 =
                makeFunctionalityCall(x95, x915, "en12:Sending Mailpt16:Envio de E-mails", null, null, null,
                        "en12:sending-mailpt16:envio-de-e-mails");
        FunctionalityCall x97 =
                makeFunctionalityCall(x95, x917, "en18:Warning Managementpt16:Gestão de Avisos", null, null, null,
                        "en18:warning-managementpt16:gestao-de-avisos");
        FunctionalityCall x98 =
                makeFunctionalityCall(x95, x918, "en17:Boards Managementpt16:Gestão de Boards", null, null, null,
                        "en17:boards-managementpt16:gestao-de-boards");
        Section x102 =
                new Section(
                        x96,
                        MultiLanguageString
                                .importFromString("en35:Organizational Structure Managementpt34:Gestão da Estrutura Organizacional"));
        x102.setName(MultiLanguageString
                .importFromString("en35:Organizational Structure Managementpt34:Gestão da Estrutura Organizacional"));
        x102.setNormalizedName(MultiLanguageString
                .importFromString("en35:organizational-structure-managementpt34:gestao-da-estrutura-organizacional"));
        Section x101 =
                new Section(x102,
                        MultiLanguageString
                                .importFromString("en30:Units and Functions Managementpt27:Gestão de Unidades e Cargos"));
        x101.setName(MultiLanguageString.importFromString("en30:Units and Functions Managementpt27:Gestão de Unidades e Cargos"));
        x101.setNormalizedName(MultiLanguageString
                .importFromString("en30:units-and-functions-managementpt27:gestao-de-unidades-e-cargos"));
        x101.setMaximizable(true);
        FunctionalityCall x112 =
                makeFunctionalityCall(x101, x920, "en30:Units and Functions Managementpt27:Gestão de Unidades e Cargos", null,
                        null, null, "en30:units-and-functions-managementpt27:gestao-de-unidades-e-cargos");
        FunctionalityCall x100 =
                makeFunctionalityCall(x101, x924, "en17:View Unit Detailspt23:Ver Detalhes de Unidade", null, null, null,
                        "en17:view-unit-detailspt23:ver-detalhes-de-unidade");
        FunctionalityCall x103 =
                makeFunctionalityCall(x101, x926, "en9:Edit Unitpt14:Editar Unidade", null, null, null,
                        "en9:edit-unitpt14:editar-unidade");
        FunctionalityCall x104 =
                makeFunctionalityCall(x101, x927, "en18:Choose Parent Unitpt20:Escolher Unidade Pai", null, null, null,
                        "en18:choose-parent-unitpt20:escolher-unidade-pai");
        FunctionalityCall x105 =
                makeFunctionalityCall(x101, x928, "en19:Create New Functionpt16:Criar Novo Cargo", null, null, null,
                        "en19:create-new-functionpt16:criar-novo-cargo");
        FunctionalityCall x106 =
                makeFunctionalityCall(x101, x929, "en13:Edit Functionpt12:Editar Cargo", null, null, null,
                        "en13:edit-functionpt12:editar-cargo");
        FunctionalityCall x107 =
                makeFunctionalityCall(x101, x930, "en15:Choose Functionpt16:Escolha do Cargo", null, null, null,
                        "en15:choose-functionpt16:escolha-do-cargo");
        FunctionalityCall x108 =
                makeFunctionalityCall(x101, x931, "en18:Choose Parent Unitpt20:Escolher Unidade Pai", null, null, null,
                        "en18:choose-parent-unitpt20:escolher-unidade-pai");
        FunctionalityCall x109 =
                makeFunctionalityCall(x101, x932, "en18:Choose Parent Unitpt20:Escolher Unidade Pai", null, null, null,
                        "en18:choose-parent-unitpt20:escolher-unidade-pai");
        FunctionalityCall x110 =
                makeFunctionalityCall(x101, x933, "en15:Create New Unitpt18:Criar Nova Unidade", null, null, null,
                        "en15:create-new-unitpt18:criar-nova-unidade");
        FunctionalityCall x111 =
                makeFunctionalityCall(x101, x934, "en19:Create New Sub Unitpt21:Criar Nova SubUnidade", null, null, null,
                        "en19:create-new-sub-unitpt21:criar-nova-subunidade");
        FunctionalityCall x114 =
                makeFunctionalityCall(x102, x922, "en20:External Units Mergept26:Merge de Unidades Externas", null, null, null,
                        "en20:external-units-mergept26:merge-de-unidades-externas");
        FunctionalityCall x115 =
                makeFunctionalityCall(x102, x923, "en20:Unit Site Managementpt26:Gestão de Sites de Unidade", null, null, null,
                        "en20:unit-site-managementpt26:gestao-de-sites-de-unidade");
        Section x122 =
                new Section(x96,
                        MultiLanguageString
                                .importFromString("en32:Educational Structure Managementpt29:Gestão da Estrutura de Ensino"));
        x122.setName(MultiLanguageString
                .importFromString("en32:Educational Structure Managementpt29:Gestão da Estrutura de Ensino"));
        x122.setNormalizedName(MultiLanguageString
                .importFromString("en32:educational-structure-managementpt29:gestao-da-estrutura-de-ensino"));
        Section x121 =
                new Section(x122,
                        MultiLanguageString.importFromString("en20:Old Degree Structurept26:Estrutura de Cursos Antiga"));
        x121.setName(MultiLanguageString.importFromString("en20:Old Degree Structurept26:Estrutura de Cursos Antiga"));
        x121.setNormalizedName(MultiLanguageString.importFromString("en20:old-degree-structurept26:estrutura-de-cursos-antiga"));
        x121.setMaximizable(true);
        Section x120 =
                new Section(x121, MultiLanguageString.importFromString("en21:Degree Administrationpt17:Administrar Curso"));
        x120.setName(MultiLanguageString.importFromString("en21:Degree Administrationpt17:Administrar Curso"));
        x120.setNormalizedName(MultiLanguageString.importFromString("en21:degree-administrationpt17:administrar-curso"));
        Section x119 =
                new Section(x120,
                        MultiLanguageString
                                .importFromString("en30:Curricular Plan Administrationpt28:Administrar Plano Curricular"));
        x119.setName(MultiLanguageString.importFromString("en30:Curricular Plan Administrationpt28:Administrar Plano Curricular"));
        x119.setNormalizedName(MultiLanguageString
                .importFromString("en30:curricular-plan-administrationpt28:administrar-plano-curricular"));
        Section x118 =
                new Section(x119, MultiLanguageString.importFromString("en21:Precedence Managementpt22:Gestão de Precedências"));
        x118.setName(MultiLanguageString.importFromString("en21:Precedence Managementpt22:Gestão de Precedências"));
        x118.setNormalizedName(MultiLanguageString.importFromString("en21:precedence-managementpt22:gestao-de-precedencias"));
        FunctionalityCall x117 =
                makeFunctionalityCall(x118, x955, "en24:Create Simple Precedencept25:Criar Precedência Simples", null, null,
                        null, "en24:create-simple-precedencept25:criar-precedencia-simples");
        FunctionalityCall x123 =
                makeFunctionalityCall(x118, x957, "en29:Create Precedence Conjunctionpt31:Criar Conjunção de Precedências", null,
                        null, null, "en29:create-precedence-conjunctionpt31:criar-conjuncao-de-precedencias");
        FunctionalityCall x124 =
                makeFunctionalityCall(x118, x958, "en17:Delete Precedencept18:Apagar Precedência", null, null, null,
                        "en17:delete-precedencept18:apagar-precedencia");
        Section x128 =
                new Section(x119,
                        MultiLanguageString
                                .importFromString("en32:Curricular Course Administrationpt33:Administrar Disciplina Curricular"));
        x128.setName(MultiLanguageString
                .importFromString("en32:Curricular Course Administrationpt33:Administrar Disciplina Curricular"));
        x128.setNormalizedName(MultiLanguageString
                .importFromString("en32:curricular-course-administrationpt33:administrar-disciplina-curricular"));
        Section x127 =
                new Section(x128,
                        MultiLanguageString.importFromString("en26:Associate Execution Coursept28:Associar disciplina execução"));
        x127.setName(MultiLanguageString.importFromString("en26:Associate Execution Coursept28:Associar disciplina execução"));
        x127.setNormalizedName(MultiLanguageString
                .importFromString("en26:associate-execution-coursept28:associar-disciplina-execucao"));
        FunctionalityCall x126 =
                makeFunctionalityCall(x127, x970, "en23:Choose Execution Periodpt28:Escolher Periodo de Execução", null, null,
                        null, "en23:choose-execution-periodpt28:escolher-periodo-de-execucao");
        FunctionalityCall x129 =
                makeFunctionalityCall(x127, x972, "en23:Choose Execution Coursept28:Escolher Disciplina Execução", null, null,
                        null, "en23:choose-execution-coursept28:escolher-disciplina-execucao");
        Section x132 = new Section(x128, MultiLanguageString.importFromString("en12:Edit Facultypt20:Editar Corpo Docente"));
        x132.setName(MultiLanguageString.importFromString("en12:Edit Facultypt20:Editar Corpo Docente"));
        x132.setNormalizedName(MultiLanguageString.importFromString("en12:edit-facultypt20:editar-corpo-docente"));
        FunctionalityCall x131 =
                makeFunctionalityCall(x132, x974, "en19:Associate Professorpt16:Associar Docente", null, null, null,
                        "en19:associate-professorpt16:associar-docente");
        FunctionalityCall x133 =
                makeFunctionalityCall(x132, x976, "en34:Associate non affiliated professorpt30:Associar docente não vinculado",
                        null, null, null, "en34:associate-non-affiliated-professorpt30:associar-docente-nao-vinculado");
        FunctionalityCall x134 =
                makeFunctionalityCall(x132, x977, "en12:Save Changespt18:Guardar Alterações", null, null, null,
                        "en12:save-changespt18:guardar-alteracoes");
        FunctionalityCall x136 =
                makeFunctionalityCall(x128, x960, "en22:Edit Curricular Coursept28:Editar Disciplina Curricular", null, null,
                        null, "en22:edit-curricular-coursept28:editar-disciplina-curricular");
        FunctionalityCall x137 =
                makeFunctionalityCall(x128, x962, "en12:Insert Scopept14:Inserir âmbito", null, null, null,
                        "en12:insert-scopept14:inserir-ambito");
        FunctionalityCall x138 =
                makeFunctionalityCall(x128, x963, "en27:Edit Curricular Informationpt31:Edição de Informação Curricular", null,
                        null, null, "en27:edit-curricular-informationpt31:edicao-de-informacao-curricular");
        FunctionalityCall x139 =
                makeFunctionalityCall(x128, x964, "en11:Create Sitept11:Criar Sitio", null, null, null,
                        "en11:create-sitept11:criar-sitio");
        FunctionalityCall x140 = makeFunctionalityCall(x128, x965, "pt13:Editar âmbito", null, null, null, "pt13:editar-ambito");
        FunctionalityCall x141 =
                makeFunctionalityCall(x128, x966, "en12:Insert Scopept14:Inserir âmbito", null, null, null,
                        "en12:insert-scopept14:inserir-ambito");
        FunctionalityCall x142 =
                makeFunctionalityCall(x128, x967, "en9:End Scopept15:Terminar âmbito", null, null, null,
                        "en9:end-scopept15:terminar-ambito");
        FunctionalityCall x143 =
                makeFunctionalityCall(x128, x968, "en12:Delete Scopept13:Apagar âmbito", null, null, null,
                        "en12:delete-scopept13:apagar-ambito");
        FunctionalityCall x144 =
                makeFunctionalityCall(x128, x969, "en15:View All Scopespt27:Visualizar todos os Âmbitos", null, null, null,
                        "en15:view-all-scopespt27:visualizar-todos-os-ambitos");
        FunctionalityCall x146 =
                makeFunctionalityCall(x119, x950, "en20:Edit Curricular Planpt23:Editar plano curricular", null, null, null,
                        "en20:edit-curricular-planpt23:editar-plano-curricular");
        FunctionalityCall x147 =
                makeFunctionalityCall(x119, x952, "en24:Insert curricular coursept29:Inserir disciplina curricular", null, null,
                        null, "en24:insert-curricular-coursept29:inserir-disciplina-curricular");
        FunctionalityCall x148 =
                makeFunctionalityCall(x119, x953, "en17:Branch Managementpt15:Gestão de Ramos", null, null, null,
                        "en17:branch-managementpt15:gestao-de-ramos");
        FunctionalityCall x149 =
                makeFunctionalityCall(x119, x954,
                        "en34:Curricular Course Group Managementpt44:Gestão de Grupos de Disciplinas Curriculares", null, null,
                        null, "en34:curricular-course-group-managementpt44:gestao-de-grupos-de-disciplinas-curriculares");
        FunctionalityCall x151 =
                makeFunctionalityCall(x120, x946, "en11:Edit Degreept12:Editar Curso", null, null, null,
                        "en11:edit-degreept12:editar-curso");
        FunctionalityCall x152 =
                makeFunctionalityCall(x120, x948, "en22:Insert curricular planpt24:Inserir plano curricular", null, null, null,
                        "en22:insert-curricular-planpt24:inserir-plano-curricular");
        FunctionalityCall x153 =
                makeFunctionalityCall(x120, x949, "en23:Delete Curricular Planspt26:Apagar Planos Curriculares", null, null,
                        null, "en23:delete-curricular-planspt26:apagar-planos-curriculares");
        FunctionalityCall x155 =
                makeFunctionalityCall(x121, x943, "en13:Insert Degreept13:Inserir curso", null, null, null,
                        "en13:insert-degreept13:inserir-curso");
        FunctionalityCall x156 =
                makeFunctionalityCall(x121, x945, "en14:Delete Degreespt13:Apagar Cursos", null, null, null,
                        "en14:delete-degreespt13:apagar-cursos");
        FunctionalityCall x157 =
                makeFunctionalityCall(x121, x937, "en20:Old Degree Structurept26:Estrutura de Cursos Antiga", null, null, null,
                        "en20:old-degree-structurept26:estrutura-de-cursos-antiga");
        Section x160 =
                new Section(x122,
                        MultiLanguageString.importFromString("en25:Before Bolonha Competencypt23:Competência Pré-Bolonha"));
        x160.setName(MultiLanguageString.importFromString("en25:Before Bolonha Competencypt23:Competência Pré-Bolonha"));
        x160.setNormalizedName(MultiLanguageString.importFromString("en25:before-bolonha-competencypt23:competencia-pre-bolonha"));
        x160.setMaximizable(true);
        FunctionalityCall x159 =
                makeFunctionalityCall(x160, x983, "en29:Insert-Edit Competence Coursept37:Inserir-Editar Disciplina Competencia",
                        null, null, null, "en29:insert-edit-competence-coursept37:inserir-editar-disciplina-competencia");
        FunctionalityCall x161 =
                makeFunctionalityCall(x160, x985, "en28:Create New Competence Coursept33:Criar Nova Disciplina Competência",
                        null, null, null, "en28:create-new-competence-coursept33:criar-nova-disciplina-competencia");
        FunctionalityCall x162 =
                makeFunctionalityCall(x160, x939, "en25:Before Bolonha Competencypt23:Competência Pré-Bolonha", null, null, null,
                        "en25:before-bolonha-competencypt23:competencia-pre-bolonha");
        Section x165 =
                new Section(x122, MultiLanguageString.importFromString("en20:Bolonha Competenciespt20:Competências Bolonha"));
        x165.setName(MultiLanguageString.importFromString("en20:Bolonha Competenciespt20:Competências Bolonha"));
        x165.setNormalizedName(MultiLanguageString.importFromString("en20:bolonha-competenciespt20:competencias-bolonha"));
        x165.setMaximizable(true);
        FunctionalityCall x164 =
                makeFunctionalityCall(x165, x987, "en22:View Competence Coursept26:Ver Disciplina Competência", null, null, null,
                        "en22:view-competence-coursept26:ver-disciplina-competencia");
        FunctionalityCall x166 =
                makeFunctionalityCall(x165, x989, "en22:Edit Competence Coursept29:Editar Disciplina Competência", null, null,
                        null, "en22:edit-competence-coursept29:editar-disciplina-competencia");
        FunctionalityCall x167 =
                makeFunctionalityCall(x165, x990, "en22:Edit Competence Coursept29:Editar Disciplina Competência", null, null,
                        null, "en22:edit-competence-coursept29:editar-disciplina-competencia");
        FunctionalityCall x168 =
                makeFunctionalityCall(x165, x941, "en20:Bolonha Competenciespt20:Competências Bolonha", null, null, null,
                        "en20:bolonha-competenciespt20:competencias-bolonha");
        FunctionalityCall x170 =
                makeFunctionalityCall(x122, x942, "en29:Department Degrees Managementpt29:Gerir Cursos de Departamentos", null,
                        null, null, "en29:department-degrees-managementpt29:gerir-cursos-de-departamentos");
        FunctionalityCall x171 =
                makeFunctionalityCall(x122, x940, "en16:Degree Structurept19:Estrutura de Cursos", null, null, null,
                        "en16:degree-structurept19:estrutura-de-cursos");
        Section x176 =
                new Section(x96, MultiLanguageString.importFromString("en20:Execution Managementpt19:Gestão de Execuções"));
        x176.setName(MultiLanguageString.importFromString("en20:Execution Managementpt19:Gestão de Execuções"));
        x176.setNormalizedName(MultiLanguageString.importFromString("en20:execution-managementpt19:gestao-de-execucoes"));
        Section x175 =
                new Section(x176, MultiLanguageString.importFromString("en20:Execution Curriculumpt19:Currículos Execução"));
        x175.setName(MultiLanguageString.importFromString("en20:Execution Curriculumpt19:Currículos Execução"));
        x175.setNormalizedName(MultiLanguageString.importFromString("en20:execution-curriculumpt19:curriculos-execucao"));
        x175.setMaximizable(true);
        Section x174 =
                new Section(x175,
                        MultiLanguageString
                                .importFromString("en31:Execution Curriculum Managementpt29:Gestão de Currículos Execução"));
        x174.setName(MultiLanguageString
                .importFromString("en31:Execution Curriculum Managementpt29:Gestão de Currículos Execução"));
        x174.setNormalizedName(MultiLanguageString
                .importFromString("en31:execution-curriculum-managementpt29:gestao-de-curriculos-execucao"));
        FunctionalityCall x173 =
                makeFunctionalityCall(x174, x1000, "en27:Create Execution Curriculumpt25:Criar Currículos Execução", null, null,
                        null, "en27:create-execution-curriculumpt25:criar-curriculos-execucao");
        FunctionalityCall x177 =
                makeFunctionalityCall(x174, x1002, "en25:Edit Execution Curriculumpt26:Editar Currículos Execução", null, null,
                        null, "en25:edit-execution-curriculumpt26:editar-curriculos-execucao");
        FunctionalityCall x178 =
                makeFunctionalityCall(x174, x1003, "en23:Choose Curricular Planspt28:Escolher Planos Curriculares", null, null,
                        null, "en23:choose-curricular-planspt28:escolher-planos-curriculares");
        FunctionalityCall x179 =
                makeFunctionalityCall(x174, x1004, "en28:Execution Curriculum Createdpt25:Currículo Execução Criado", null, null,
                        null, "en28:execution-curriculum-createdpt25:curriculo-execucao-criado");
        FunctionalityCall x181 =
                makeFunctionalityCall(x175, x998, "en8:Homepagept16:Página Principal", null, null, null,
                        "en8:homepagept16:pagina-principal");
        FunctionalityCall x182 =
                makeFunctionalityCall(x175, x995, "en20:Execution Curriculumpt19:Currículos Execução", null, null, null,
                        "en20:execution-curriculumpt19:curriculos-execucao");
        Section x186 = new Section(x176, MultiLanguageString.importFromString("en17:Execution Coursespt20:Disciplinas Execução"));
        x186.setName(MultiLanguageString.importFromString("en17:Execution Coursespt20:Disciplinas Execução"));
        x186.setNormalizedName(MultiLanguageString.importFromString("en17:execution-coursespt20:disciplinas-execucao"));
        x186.setMaximizable(true);
        Section x185 = new Section(x186, MultiLanguageString.importFromString("en17:Execution Coursespt20:Disciplinas Execução"));
        x185.setName(MultiLanguageString.importFromString("en17:Execution Coursespt20:Disciplinas Execução"));
        x185.setNormalizedName(MultiLanguageString.importFromString("en17:execution-coursespt20:disciplinas-execucao"));
        x185.setMaximizable(false);
        FunctionalityCall x184 =
                makeFunctionalityCall(x185, x1009, "en23:Insert Execution Coursept27:Inserir Disciplina Execução", null, null,
                        null, "en23:insert-execution-coursept27:inserir-disciplina-execucao");
        FunctionalityCall x187 =
                makeFunctionalityCall(x185, x1011, "en21:Edit Execution Coursept26:Editar Disciplina Execução", null, null, null,
                        "en21:edit-execution-coursept26:editar-disciplina-execucao");
        FunctionalityCall x188 =
                makeFunctionalityCall(x185, x1012, "en23:Merge Execution Coursespt27:Juntar Disciplinas Execução", null, null,
                        null, "en23:merge-execution-coursespt27:juntar-disciplinas-execucao");
        FunctionalityCall x189 =
                makeFunctionalityCall(x185, x1013, "en23:Create Teaching Reportspt28:Criar Relatórios de Docência", null, null,
                        null, "en23:create-teaching-reportspt28:criar-relatorios-de-docencia");
        FunctionalityCall x190 =
                makeFunctionalityCall(x185, x1014, "en24:Create Execution Coursespt29:Criar Disciplinas de Execução", null, null,
                        null, "en24:create-execution-coursespt29:criar-disciplinas-de-execucao");
        FunctionalityCall x191 =
                makeFunctionalityCall(x185, x1015, "en23:Edit Execution Course 1pt28:Editar Disciplina Execução 1", null, null,
                        null, "en23:edit-execution-course-1pt28:editar-disciplina-execucao-1");
        FunctionalityCall x192 =
                makeFunctionalityCall(x185, x1016, "en23:Edit Execution Course 2pt28:Editar Disciplina Execução 2", null, null,
                        null, "en23:edit-execution-course-2pt28:editar-disciplina-execucao-2");
        FunctionalityCall x193 =
                makeFunctionalityCall(x185, x1017, "en27:Associate Curricular Coursept30:Associar Disciplina Curricular", null,
                        null, null, "en27:associate-curricular-coursept30:associar-disciplina-curricular");
        FunctionalityCall x194 =
                makeFunctionalityCall(x185, x1018, "en25:Separate Execution Coursept27:Separar Disciplina Execução", null, null,
                        null, "en25:separate-execution-coursept27:separar-disciplina-execucao");
        FunctionalityCall x195 =
                makeFunctionalityCall(x185, x1019, "en25:Merge Execution Courses 2pt29:Juntar Disciplinas Execução 2", null,
                        null, null, "en25:merge-execution-courses-2pt29:juntar-disciplinas-execucao-2");
        FunctionalityCall x197 =
                makeFunctionalityCall(x186, x1007, "en8:Homepagept14:Página Inicial", null, null, null,
                        "en8:homepagept14:pagina-inicial");
        FunctionalityCall x198 =
                makeFunctionalityCall(x186, x996, "en25:Execution Course Homepagept35:Página Inicial Disciplinas Execução", null,
                        null, null, "en25:execution-course-homepagept35:pagina-inicial-disciplinas-execucao");
        FunctionalityCall x200 =
                makeFunctionalityCall(x176, x993, "en17:Execution Periodspt17:Periodos Execução", null, null, null,
                        "en17:execution-periodspt17:periodos-execucao");
        FunctionalityCall x201 =
                makeFunctionalityCall(x176, x997, "en17:Enrolment Periodspt22:Periodos de Inscrições", null, null, null,
                        "en17:enrolment-periodspt22:periodos-de-inscricoes");
        Section x206 = new Section(x96, MultiLanguageString.importFromString("en19:Personal Managementpt17:Gestão de Pessoal"));
        x206.setName(MultiLanguageString.importFromString("en19:Personal Managementpt17:Gestão de Pessoal"));
        x206.setNormalizedName(MultiLanguageString.importFromString("en19:personal-managementpt17:gestao-de-pessoal"));
        Section x205 = new Section(x206, MultiLanguageString.importFromString("en18:Teacher Managementpt18:Gestão de Docentes"));
        x205.setName(MultiLanguageString.importFromString("en18:Teacher Managementpt18:Gestão de Docentes"));
        x205.setNormalizedName(MultiLanguageString.importFromString("en18:teacher-managementpt18:gestao-de-docentes"));
        x205.setMaximizable(true);
        Section x204 = new Section(x205, MultiLanguageString.importFromString("en18:Teacher Managementpt18:Gestão de Docentes"));
        x204.setName(MultiLanguageString.importFromString("en18:Teacher Managementpt18:Gestão de Docentes"));
        x204.setNormalizedName(MultiLanguageString.importFromString("en18:teacher-managementpt18:gestao-de-docentes"));
        FunctionalityCall x203 =
                makeFunctionalityCall(x204, x1030, "en15:Edit Categoriespt17:Editar Categorias", null, null, null,
                        "en15:edit-categoriespt17:editar-categorias");
        FunctionalityCall x207 =
                makeFunctionalityCall(x204, x1032, "en30:Disassociate Execution Coursespt32:Desassociar Disciplinas Execução",
                        null, null, null, "en30:disassociate-execution-coursespt32:desassociar-disciplinas-execucao");
        FunctionalityCall x208 =
                makeFunctionalityCall(x204, x1286, "pt21:Avaliação de Docentes", null, null, null, "pt21:avaliacao-de-docentes");
        ExpressionGroupAvailability x1692 = new ExpressionGroupAvailability(x208, "noOne()");
        x1692.setTargetGroup(Group.fromString("noOne()"));
        x208.setAvailabilityPolicy(x1692);
        ExpressionGroupAvailability x1693 = new ExpressionGroupAvailability(x204, "everyone()");
        x1693.setTargetGroup(Group.fromString("everyone()"));
        x204.setAvailabilityPolicy(x1693);
        FunctionalityCall x210 =
                makeFunctionalityCall(x205, x1028, "en8:Homepagept14:Página Inicial", null, null, null,
                        "en8:homepagept14:pagina-inicial");
        FunctionalityCall x211 =
                makeFunctionalityCall(x205, x1023, "en18:Teacher Managementpt18:Gestão de Docentes", null, null, null,
                        "en18:teacher-managementpt18:gestao-de-docentes");
        Section x214 = new Section(x206, MultiLanguageString.importFromString("en18:Student Managementpt16:Gestão de Alunos"));
        x214.setName(MultiLanguageString.importFromString("en18:Student Managementpt16:Gestão de Alunos"));
        x214.setNormalizedName(MultiLanguageString.importFromString("en18:student-managementpt16:gestao-de-alunos"));
        x214.setMaximizable(true);
        FunctionalityCall x213 =
                makeFunctionalityCall(x214, x1035, "en22:Create Classificationspt20:Criar Classificações", null, null, null,
                        "en22:create-classificationspt20:criar-classificacoes");
        FunctionalityCall x215 =
                makeFunctionalityCall(x214, x1025, "en18:Student Managementpt16:Gestão de Alunos", null, null, null,
                        "en18:student-managementpt16:gestao-de-alunos");
        Section x220 = new Section(x206, MultiLanguageString.importFromString("en17:People Managementpt17:Gestão de Pessoas"));
        x220.setName(MultiLanguageString.importFromString("en17:People Managementpt17:Gestão de Pessoas"));
        x220.setNormalizedName(MultiLanguageString.importFromString("en17:people-managementpt17:gestao-de-pessoas"));
        x220.setMaximizable(true);
        Section x219 = new Section(x220, MultiLanguageString.importFromString("en17:People Managementpt17:Gestão de Pessoas"));
        x219.setName(MultiLanguageString.importFromString("en17:People Managementpt17:Gestão de Pessoas"));
        x219.setNormalizedName(MultiLanguageString.importFromString("en17:people-managementpt17:gestao-de-pessoas"));
        Section x218 =
                new Section(x219,
                        MultiLanguageString.importFromString("en23:External Invited Peoplept27:Pessoas Externas-Convidadas"));
        x218.setName(MultiLanguageString.importFromString("en23:External Invited Peoplept27:Pessoas Externas-Convidadas"));
        x218.setNormalizedName(MultiLanguageString
                .importFromString("en23:external-invited-peoplept27:pessoas-externas-convidadas"));
        FunctionalityCall x217 =
                makeFunctionalityCall(x218, x1053, "en30:Create External Invited Personpt30:Criar Pessoa Externa-Convidada",
                        null, null, null, "en30:create-external-invited-personpt30:criar-pessoa-externa-convidada");
        FunctionalityCall x221 =
                makeFunctionalityCall(x218, x1055, "en22:Invitations Managementpt18:Gestão de Convites", null, null, null,
                        "en22:invitations-managementpt18:gestao-de-convites");
        FunctionalityCall x223 =
                makeFunctionalityCall(x219, x1040, "en13:Create Peoplept13:Criar Pessoas", null, null, null,
                        "en13:create-peoplept13:criar-pessoas");
        FunctionalityCall x224 =
                makeFunctionalityCall(x219, x1042, "en13:Search Personpt15:Procurar Pessoa", null, null, null,
                        "en13:search-personpt15:procurar-pessoa");
        FunctionalityCall x225 =
                makeFunctionalityCall(x219, x1043, "en15:Role Managementpt21:Gestão de Privilégios", null, null, null,
                        "en15:role-managementpt21:gestao-de-privilegios");
        FunctionalityCall x226 =
                makeFunctionalityCall(x219, x1044, "en17:Generate Passwordpt14:Gerar Password", null, null, null,
                        "en17:generate-passwordpt14:gerar-password");
        FunctionalityCall x227 =
                makeFunctionalityCall(x219, x1045, "en20:Management Functionspt16:Cargos de Gestão", null, null, null,
                        "en20:management-functionspt16:cargos-de-gestao");
        FunctionalityCall x228 =
                makeFunctionalityCall(x219, x1046, "en20:Management Functionspt16:Cargos de Gestão", null, null, null,
                        "en20:management-functionspt16:cargos-de-gestao");
        FunctionalityCall x229 =
                makeFunctionalityCall(x219, x1047, "en20:Management Functionspt16:Cargos de Gestão", null, null, null,
                        "en20:management-functionspt16:cargos-de-gestao");
        FunctionalityCall x230 =
                makeFunctionalityCall(x219, x1048, "en15:Activate Personpt14:Activar Pessoa", null, null, null,
                        "en15:activate-personpt14:activar-pessoa");
        FunctionalityCall x231 =
                makeFunctionalityCall(x219, x1049, "en20:Management Functionspt16:Cargos de Gestão", null, null, null,
                        "en20:management-functionspt16:cargos-de-gestao");
        FunctionalityCall x232 =
                makeFunctionalityCall(x219, x1050, "en20:Management Functionspt16:Cargos de Gestão", null, null, null,
                        "en20:management-functionspt16:cargos-de-gestao");
        FunctionalityCall x233 =
                makeFunctionalityCall(x219, x1051, "en20:Management Functionspt16:Cargos de Gestão", null, null, null,
                        "en20:management-functionspt16:cargos-de-gestao");
        FunctionalityCall x234 =
                makeFunctionalityCall(x219, x1052, "en11:Edit Personpt13:Editar Pessoa", null, null, null,
                        "en11:edit-personpt13:editar-pessoa");
        FunctionalityCall x236 =
                makeFunctionalityCall(x220, x1038, "en8:Homepagept14:Página Inicial", null, null, null,
                        "en8:homepagept14:pagina-inicial");
        FunctionalityCall x237 =
                makeFunctionalityCall(x220, x1026, "en17:People Managementpt17:Gestão de Pessoas", null, null, null,
                        "en17:people-managementpt17:gestao-de-pessoas");
        FunctionalityCall x239 =
                makeFunctionalityCall(x206, x1027, "en18:Holiday Managementpt18:Gestão de Feriados", null, null, null,
                        "en18:holiday-managementpt18:gestao-de-feriados");
        FunctionalityCall x240 =
                makeFunctionalityCall(x206, x1059, "en15:Role Managementpt15:Gestão de Roles", null, null, null,
                        "en15:role-managementpt15:gestao-de-roles");
        Section x243 = new Section(x96, MultiLanguageString.importFromString("en12:Equivalencespt13:Equivalências"));
        x243.setName(MultiLanguageString.importFromString("en12:Equivalencespt13:Equivalências"));
        x243.setNormalizedName(MultiLanguageString.importFromString("en12:equivalencespt13:equivalencias"));
        FunctionalityCall x242 =
                makeFunctionalityCall(x243, x1061, "en17:No need to enrollpt22:Não necessita de fazer", null, null, null,
                        "en17:no-need-to-enrollpt22:nao-necessita-de-fazer");
        FunctionalityCall x244 =
                makeFunctionalityCall(x243, x1063, "en12:Equivalencespt13:Equivalências", null, null, null,
                        "en12:equivalencespt13:equivalencias");
        Section x249 = new Section(x96, MultiLanguageString.importFromString("en15:File Managementpt19:Gestão de Ficheiros"));
        x249.setName(MultiLanguageString.importFromString("en15:File Managementpt19:Gestão de Ficheiros"));
        x249.setNormalizedName(MultiLanguageString.importFromString("en15:file-managementpt19:gestao-de-ficheiros"));
        Section x248 = new Section(x249, MultiLanguageString.importFromString("en15:File Generationpt20:Geração de Ficheiros"));
        x248.setName(MultiLanguageString.importFromString("en15:File Generationpt20:Geração de Ficheiros"));
        x248.setNormalizedName(MultiLanguageString.importFromString("en15:file-generationpt20:geracao-de-ficheiros"));
        x248.setMaximizable(true);
        Section x247 = new Section(x248, MultiLanguageString.importFromString("en15:File Generationpt20:Geração de Ficheiros"));
        x247.setName(MultiLanguageString.importFromString("en15:File Generationpt20:Geração de Ficheiros"));
        x247.setNormalizedName(MultiLanguageString.importFromString("en15:file-generationpt20:geracao-de-ficheiros"));
        FunctionalityCall x246 =
                makeFunctionalityCall(x247, x1070, "en13:File for SIBSpt20:Ficheiro para a SIBS", null, null, null,
                        "en13:file-for-sibspt20:ficheiro-para-a-sibs");
        FunctionalityCall x250 =
                makeFunctionalityCall(x247, x1072, "en16:File for Letterspt20:Ficheiro para Cartas", null, null, null,
                        "en16:file-for-letterspt20:ficheiro-para-cartas");
        FunctionalityCall x252 =
                makeFunctionalityCall(x248, x1068, "en8:Homepagept16:Página Principal", null, null, null,
                        "en8:homepagept16:pagina-principal");
        FunctionalityCall x253 =
                makeFunctionalityCall(x248, x1065, "en15:File Generationpt20:Geração de Ficheiros", null, null, null,
                        "en15:file-generationpt20:geracao-de-ficheiros");
        Section x257 = new Section(x249, MultiLanguageString.importFromString("en11:File Uploadpt19:Upload de Ficheiros"));
        x257.setName(MultiLanguageString.importFromString("en11:File Uploadpt19:Upload de Ficheiros"));
        x257.setNormalizedName(MultiLanguageString.importFromString("en11:file-uploadpt19:upload-de-ficheiros"));
        x257.setMaximizable(true);
        Section x256 = new Section(x257, MultiLanguageString.importFromString("en15:File Managementpt19:Gestão de Ficheiros"));
        x256.setName(MultiLanguageString.importFromString("en15:File Managementpt19:Gestão de Ficheiros"));
        x256.setNormalizedName(MultiLanguageString.importFromString("en15:file-managementpt19:gestao-de-ficheiros"));
        FunctionalityCall x255 =
                makeFunctionalityCall(x256, x1077, "en9:SIBS Filept16:Ficheiro da SIBS", null, null, null,
                        "en9:sibs-filept16:ficheiro-da-sibs");
        FunctionalityCall x259 =
                makeFunctionalityCall(x257, x1075, "en8:Homepagept16:Página Principal", null, null, null,
                        "en8:homepagept16:pagina-principal");
        FunctionalityCall x260 =
                makeFunctionalityCall(x257, x1067, "en11:File Uploadpt19:Upload de Ficheiros", null, null, null,
                        "en11:file-uploadpt19:upload-de-ficheiros");
        Section x266 = new Section(x96, MultiLanguageString.importFromString("en20:Finalcial Managementpt17:Gestão Financeira"));
        x266.setName(MultiLanguageString.importFromString("en20:Finalcial Managementpt17:Gestão Financeira"));
        x266.setNormalizedName(MultiLanguageString.importFromString("en20:financial-managementpt17:gestao-financeira"));
        Section x265 = new Section(x266, MultiLanguageString.importFromString("en16:Guide Managementpt15:Gestão de Guias"));
        x265.setName(MultiLanguageString.importFromString("en16:Guide Managementpt15:Gestão de Guias"));
        x265.setNormalizedName(MultiLanguageString.importFromString("en16:guide-managementpt15:gestao-de-guias"));
        x265.setMaximizable(true);
        Section x264 = new Section(x265, MultiLanguageString.importFromString("en16:Guide Managementpt15:Gestão de Guias"));
        x264.setName(MultiLanguageString.importFromString("en16:Guide Managementpt15:Gestão de Guias"));
        x264.setNormalizedName(MultiLanguageString.importFromString("en16:guide-managementpt15:gestao-de-guias"));
        FunctionalityCall x263 =
                makeFunctionalityCall(x264, x1087, "en13:Guide Editionpt15:Edição de Guias", null, null, null,
                        "en13:guide-editionpt15:edicao-de-guias");
        FunctionalityCall x268 =
                makeFunctionalityCall(x265, x1085, "en8:Homepagept16:Página Principal", null, null, null,
                        "en8:homepagept16:pagina-principal");
        FunctionalityCall x269 =
                makeFunctionalityCall(x265, x1082, "en16:Guide Managementpt15:Gestão de Guias", null, null, null,
                        "en16:guide-managementpt15:gestao-de-guias");
        FunctionalityCall x271 =
                makeFunctionalityCall(x266, x1084, "en25:Update Tuition Situationspt31:Actualizar Situações de Propina", null,
                        null, null, "en25:update-tuition-situationspt31:actualizar-situacoes-de-propina");
        Section x274 = new Section(x96, MultiLanguageString.importFromString("en14:CMS Managementpt13:Gestão de CMS"));
        x274.setName(MultiLanguageString.importFromString("en14:CMS Managementpt13:Gestão de CMS"));
        x274.setNormalizedName(MultiLanguageString.importFromString("en14:cms-managementpt13:gestao-de-cms"));
        FunctionalityCall x273 =
                makeFunctionalityCall(x274, x1092, "en11:User Groupspt22:Grupos de Utilizadores", null, null, null,
                        "en11:user-groupspt22:grupos-de-utilizadores");
        FunctionalityCall x275 =
                makeFunctionalityCall(x274, x1094, "en14:Configurationspt13:Configurações", null, null, null,
                        "en14:configurationspt13:configuracoes");
        FunctionalityCall x276 =
                makeFunctionalityCall(x274, x1095, "en14:Course Websitept22:Páginas de Disciplinaspt22:Páginas de Disciplinas",
                        null, null, null, "en14:course-websiteptpt22:paginas-de-disciplinas");
        Section x279 = new Section(x96, MultiLanguageString.importFromString("en18:Support Managementpt17:Gestão do Suporte"));
        x279.setName(MultiLanguageString.importFromString("en18:Support Managementpt17:Gestão do Suporte"));
        x279.setNormalizedName(MultiLanguageString.importFromString("en18:support-managementpt17:gestao-do-suporte"));
        FunctionalityCall x278 =
                makeFunctionalityCall(x279, x1097, "en19:Glossary Managementpt19:Gestão de Glossário", null, null, null,
                        "en19:glossary-managementpt19:gestao-de-glossario");
        FunctionalityCall x280 =
                makeFunctionalityCall(x279, x1099, "en14:FAQ Managementpt15:Gestão de FAQ s", null, null, null,
                        "en14:faq-managementpt15:gestao-de-faq-s");
        FunctionalityCall x281 =
                makeFunctionalityCall(x279, x1100, "en14:FAQ Managementpt15:Gestão de FAQ s", null, null, null,
                        "en14:faq-managementpt15:gestao-de-faq-s");
        Section x284 = new Section(x96, MultiLanguageString.importFromString("en17:Object Managementpt18:Gestão de Objectos"));
        x284.setName(MultiLanguageString.importFromString("en17:Object Managementpt18:Gestão de Objectos"));
        x284.setNormalizedName(MultiLanguageString.importFromString("en17:object-managementpt18:gestao-de-objectos"));
        FunctionalityCall x283 =
                makeFunctionalityCall(x284, x1102, "en16:Cache Managementpt15:Gestão da Cache", null, null, null,
                        "en16:cache-managementpt15:gestao-da-cache");
        FunctionalityCall x285 =
                makeFunctionalityCall(x284, x1104, "en21:Properties Formattingpt26:Formatação de Propriedades", null, null, null,
                        "en21:properties-formattingpt26:formatacao-de-propriedades");
        FunctionalityCall x286 =
                makeFunctionalityCall(x284, x1105, "en13:Merge Objectspt17:Fusão de Objectos", null, null, null,
                        "en13:merge-objectspt17:fusao-de-objectos");
        FunctionalityCall x287 =
                makeFunctionalityCall(x284, x1106, "en14:Object Editionpt18:Edição de Objectos", null, null, null,
                        "en14:object-editionpt18:edicao-de-objectos");
        FunctionalityCall x288 =
                makeFunctionalityCall(x284, x1107, "en12:Merge Peoplept16:Fusão de Pessoas", null, null, null,
                        "en12:merge-peoplept16:fusao-de-pessoas");
        Section x291 = new Section(x96, MultiLanguageString.importFromString("en17:System Managementpt17:Gestão do Sistema"));
        x291.setName(MultiLanguageString.importFromString("en17:System Managementpt17:Gestão do Sistema"));
        x291.setNormalizedName(MultiLanguageString.importFromString("en17:system-managementpt17:gestao-do-sistema"));
        FunctionalityCall x290 =
                makeFunctionalityCall(x291, x1109, "en23:Services Monitorizationpt25:Monitorização de Serviços", null, null,
                        null, "en23:services-monitorizationpt25:monitorizacao-de-servicos");
        FunctionalityCall x292 =
                makeFunctionalityCall(x291, x1111, "en23:Requests Monitorizationpt24:Monitorização de Pedidos", null, null, null,
                        "en23:requests-monitorizationpt24:monitorizacao-de-pedidos");
        FunctionalityCall x293 =
                makeFunctionalityCall(x291, x1112, "en20:Users Monitorizationpt29:Monitorização de Utilizadores", null, null,
                        null, "en20:users-monitorizationpt29:monitorizacao-de-utilizadores");
        FunctionalityCall x294 =
                makeFunctionalityCall(x291, x1113, "en18:System Informationpt22:Informações do Sistema", null, null, null,
                        "en18:system-informationpt22:informacoes-do-sistema");
        FunctionalityCall x295 =
                makeFunctionalityCall(x291, x1114, "en13:Kerberos Testpt14:Teste Kerberos", null, null, null,
                        "en13:kerberos-testpt14:teste-kerberos");
        FunctionalityCall x296 = makeFunctionalityCall(x291, x1115, "en4:Cronpt4:Cron", null, null, null, "en4:cronpt4:cron");
        FunctionalityCall x297 =
                makeFunctionalityCall(x291, x1116, "en13:Kerberos Testpt14:Teste Kerberos", null, null, null,
                        "en13:kerberos-testpt14:teste-kerberos");
        FunctionalityCall x298 =
                makeFunctionalityCall(x291, x1117, "en18:Transaction Systempt22:Sistema de Transacções", null, null, null,
                        "en18:transaction-systempt22:sistema-de-transaccoes");
        FunctionalityCall x299 =
                makeFunctionalityCall(x291, x1118, "en16:Login Managementpt16:Gestão de Logins", null, null, null,
                        "en16:login-managementpt16:gestao-de-logins");
        FunctionalityCall x300 =
                makeFunctionalityCall(x291, x1119, "en10:Queue jobspt10:Queue jobs", null, null, null,
                        "en10:queue-jobspt10:queue-jobs");
        Section x305 =
                new Section(x96,
                        MultiLanguageString.importFromString("en26:Functionalities Managementpt25:Gestão de Funcionalidades"));
        x305.setName(MultiLanguageString.importFromString("en26:Functionalities Managementpt25:Gestão de Funcionalidades"));
        x305.setNormalizedName(MultiLanguageString
                .importFromString("en26:functionalities-managementpt25:gestao-de-funcionalidades"));
        Section x304 = new Section(x305, MultiLanguageString.importFromString("en14:Other Mappingspt18:Outros mapeamentos"));
        x304.setName(MultiLanguageString.importFromString("en14:Other Mappingspt18:Outros mapeamentos"));
        x304.setNormalizedName(MultiLanguageString.importFromString("en14:other-mappingspt18:outros-mapeamentos"));
        Section x303 = new Section(x304, MultiLanguageString.importFromString("en6:Modulept6:Modulo"));
        x303.setName(MultiLanguageString.importFromString("en6:Modulept6:Modulo"));
        x303.setNormalizedName(MultiLanguageString.importFromString("en6:modulept6:modulo"));
        FunctionalityCall x302 = makeFunctionalityCall(x303, x1126, "en4:Viewpt3:Ver", null, null, null, "en4:viewpt3:ver");
        FunctionalityCall x306 = makeFunctionalityCall(x303, x1129, "en4:Editpt6:Editar", null, null, null, "en4:editpt6:editar");
        FunctionalityCall x307 =
                makeFunctionalityCall(x303, x1130, "en6:Creatept5:Criar", null, null, null, "en6:creatept5:criar");
        FunctionalityCall x308 =
                makeFunctionalityCall(x303, x1131, "en20:Structure Managementpt19:Organizar Estrutura", null, null, null,
                        "en20:structure-managementpt19:organizar-estrutura");
        FunctionalityCall x309 =
                makeFunctionalityCall(x303, x1132, "en16:Export Structurept18:Exportar estrutura", null, null, null,
                        "en16:export-structurept18:exportar-estrutura");
        FunctionalityCall x310 =
                makeFunctionalityCall(x303, x1133, "en16:Import Structurept18:Importar Estrutura", null, null, null,
                        "en16:import-structurept18:importar-estrutura");
        FunctionalityCall x311 =
                makeFunctionalityCall(x303, x1134, "en16:Import Structurept18:Importar Estrutura", null, null, null,
                        "en16:import-structurept18:importar-estrutura");
        Section x314 = new Section(x304, MultiLanguageString.importFromString("en13:Functionalitypt15:Functionalidade"));
        x314.setName(MultiLanguageString.importFromString("en13:Functionalitypt15:Functionalidade"));
        x314.setNormalizedName(MultiLanguageString.importFromString("en13:functionalitypt15:functionalidade"));
        FunctionalityCall x313 = makeFunctionalityCall(x314, x1136, "en4:Viewpt3:Ver", null, null, null, "en4:viewpt3:ver");
        FunctionalityCall x315 = makeFunctionalityCall(x314, x1138, "en4:Editpt6:Editar", null, null, null, "en4:editpt6:editar");
        FunctionalityCall x316 =
                makeFunctionalityCall(x314, x1139, "en6:Creatept5:Criar", null, null, null, "en6:creatept5:criar");
        FunctionalityCall x317 =
                makeFunctionalityCall(x314, x1140, "en14:Confirm Deletept16:Confirmar Apagar", null, null, null,
                        "en14:confirm-deletept16:confirmar-apagar");
        FunctionalityCall x318 =
                makeFunctionalityCall(x314, x1141, "en6:Deletept6:Apagar", null, null, null, "en6:deletept6:dpagar");
        FunctionalityCall x319 =
                makeFunctionalityCall(x314, x1142, "en19:Manage Availabilitypt21:Gerir Disponibilidade", null, null, null,
                        "en19:manage-availabilitypt21:gerir-disponibilidade");
        FunctionalityCall x320 =
                makeFunctionalityCall(x314, x1143,
                        "en33:Interpret Availability Expressionpt40:Interpretar expressão de disponibilidade", null, null, null,
                        "en33:interpret-availability-expressionpt40:interpretar-expressao-de-disponibilidade");
        FunctionalityCall x321 =
                makeFunctionalityCall(x314, x1144, "en7:Disablept10:Desactivar", null, null, null, "en7:disablept10:desactivar");
        FunctionalityCall x322 =
                makeFunctionalityCall(x314, x1145, "en6:Enablept7:Activar", null, null, null, "en6:enablept7:activar");
        Section x325 = new Section(x304, MultiLanguageString.importFromString("en3:Toppt4:Topo"));
        x325.setName(MultiLanguageString.importFromString("en3:Toppt4:Topo"));
        x325.setNormalizedName(MultiLanguageString.importFromString("en3:toppt4:topo"));
        FunctionalityCall x324 = makeFunctionalityCall(x325, x1147, "en4:Viewpt3:Ver", null, null, null, "en4:viewpt3:ver");
        FunctionalityCall x326 =
                makeFunctionalityCall(x325, x1149, "en20:Structure Managementpt19:Organizar Estrutura", null, null, null,
                        "en20:structure-managementpt19:organizar-estrutura");
        FunctionalityCall x327 =
                makeFunctionalityCall(x325, x1150, "en16:Import Structurept18:Importar Estrutura", null, null, null,
                        "en16:import-structurept18:importar-estrutura");
        FunctionalityCall x330 =
                makeFunctionalityCall(x305, x1121, "en20:View Functionalitiespt19:Ver Funcionalidades", null, null, null,
                        "en20:view-functionalitiespt19:ver-funcionalidades");
        FunctionalityCall x331 =
                makeFunctionalityCall(x305, x1123, "en11:Test Filterpt13:Testar Filtro", null, null, null,
                        "en11:test-filterpt13:testar-filtro");
        FunctionalityCall x332 =
                makeFunctionalityCall(x305, x1124, "en14:Filter Resultspt20:Resultados do Filtro", null, null, null,
                        "en14:filter-resultspt20:resultados-do-filtro");
        FunctionalityCall x333 =
                makeFunctionalityCall(x305, x1125, "pt14:Group Languagept19:Linguagem de Grupos", null, null, null,
                        "en14:group-languagept19:linguagem-de-grupos");
        Section x337 = new Section(x96, MultiLanguageString.importFromString("en10:Frameworkspt10:Frameworks"));
        x337.setName(MultiLanguageString.importFromString("en10:Frameworkspt10:Frameworks"));
        x337.setNormalizedName(MultiLanguageString.importFromString("en10:frameworkspt10:frameworks"));
        Section x336 = new Section(x337, MultiLanguageString.importFromString("en6:Strutspt6:Struts"));
        x336.setName(MultiLanguageString.importFromString("en6:Strutspt6:Struts"));
        x336.setNormalizedName(MultiLanguageString.importFromString("en6:strutspt6:struts"));
        FunctionalityCall x335 =
                makeFunctionalityCall(x336, x1156, "en14:Struts Examplept14:Exemplo Struts", null, null, null,
                        "en14:struts-examplept14:exemplo-struts");
        FunctionalityCall x338 =
                makeFunctionalityCall(x336, x1158, "en27:Reload Struts Configurationpt30:Recarregar Configuração Struts", null,
                        null, null, "en27:reload-struts-configurationpt30:recarregar-configuracao-struts");
        Section x342 = new Section(x337, MultiLanguageString.importFromString("en9:Rendererspt9:Renderers"));
        x342.setName(MultiLanguageString.importFromString("en9:Rendererspt9:Renderers"));
        x342.setNormalizedName(MultiLanguageString.importFromString("en9:rendererspt9:renderers"));
        Section x341 = new Section(x342, MultiLanguageString.importFromString("en5:Partspt6:Partes"));
        x341.setName(MultiLanguageString.importFromString("en5:Partspt6:Partes"));
        x341.setNormalizedName(MultiLanguageString.importFromString("en5:partspt6:partes"));
        FunctionalityCall x340 =
                makeFunctionalityCall(x341, x1164,
                        "en41:The first situation: presenting the worldpt37:Primeira situação: apresentar o mundo", null, null,
                        null, "en40:the-first-situation-presenting-the-worldpt36:primeira-situacao-apresentar-o-mundo");
        FunctionalityCall x343 =
                makeFunctionalityCall(x341, x1166, "en35:The second situation: give me inputpt29:Segunda situação: dá-me input",
                        null, null, null, "en34:the-second-situation-give-me-inputpt28:segunda-situacao-da-me-input");
        FunctionalityCall x344 =
                makeFunctionalityCall(x341, x1167,
                        "en38:The third situation: input on steroidspt39:Terceira situação: input sob esteróides", null, null,
                        null, "en37:the-third-situation-input-on-steroidspt38:terceira-situacao-input-sob-esteroides");
        FunctionalityCall x345 =
                makeFunctionalityCall(x341, x1168,
                        "en44:The fourth situation: renderers meet actionspt39:Quarta situação: renderers meet actions", null,
                        null, null, "en43:the-fourth-situation-renderers-meet-actionspt38:quarta-situacao-renderers-meet-actions");
        FunctionalityCall x346 =
                makeFunctionalityCall(x341, x1169,
                        "en35:The fifth situation: a new rendereren33:Quinta situação: um novo renderer", null, null, null,
                        "en34:the-fifth-situation-a-new-rendererpt32:quinta-situacao-um-novo-renderer");
        FunctionalityCall x347 =
                makeFunctionalityCall(x341, x1170, "en19:Appendix A: Schemaspt20:Apêndice A: Esquemas", null, null, null,
                        "en18:appendix-a-schemaspt19:apendice-a-esquemas");
        FunctionalityCall x349 =
                makeFunctionalityCall(x342, x1160, "en18:Renderers Examplespt18:Exemplos Renderers", null, null, null,
                        "en18:renderers-examplespt18:exemplos-renderers");
        FunctionalityCall x350 =
                makeFunctionalityCall(x342, x1162, "en30:Reload Renderers Configurationpt28:Reler Configuração Renderers", null,
                        null, null, "en30:reload-renderers-configurationpt28:reler-configuracao-renderers");
        FunctionalityCall x351 =
                makeFunctionalityCall(x342, x1163, "en30:Example 1: Input from the userpt30:Exemplo 1: Input do utilizador",
                        null, null, null, "en29:example-1-input-from-the-userpt29:exemplo-1-input-do-utilizador");
        FunctionalityCall x353 =
                makeFunctionalityCall(x337, x1154, "en25:Java Server Faces Examplept27:Exemplo Servidor Java Faces", null, null,
                        null, "en25:java-server-faces-examplept27:exemplo-servidor-java-faces");
        Section x356 = new Section(x96, MultiLanguageString.importFromString("en8:Paymentspt10:Pagamentos"));
        x356.setName(MultiLanguageString.importFromString("en8:Paymentspt10:Pagamentos"));
        x356.setNormalizedName(MultiLanguageString.importFromString("en8:paymentspt10:pagamentos"));
        FunctionalityCall x355 =
                makeFunctionalityCall(x356, x1174, "en11:File Uploadpt18:Upload de Ficheiro", null, null, null,
                        "en11:file-uploadpt18:upload-de-ficheiro");
        FunctionalityCall x357 =
                makeFunctionalityCall(x356, x1176, "en15:Manage Paymentspt16:Gerir Pagamentos", null, null, null,
                        "en15:manage-paymentspt16:gerir-pagamentos");
        FunctionalityCall x358 =
                makeFunctionalityCall(x356, x1177, "en13:Payment Rulespt20:Regras de Pagamentos", null, null, null,
                        "pt20:regras-de-pagamentos");
        FunctionalityCall x359 =
                makeFunctionalityCall(x356, x1178, "en13:Payment Codespt7:Códigos", null, null, null,
                        "en13:payment-codespt7:codigos");
        FunctionalityCall x360 =
                makeFunctionalityCall(x356, x1179, "en7:Reportspt10:Relatórios", null, null, null, "en7:reportspt10:relatorios");
        FunctionalityCall x361 =
                makeFunctionalityCall(x356, x1180, "en14:Tuition reportpt21:Relatório de propinas", null, null, null,
                        "en14:tuition-reportpt21:relatorio-de-propinas");
        FunctionalityCall x362 =
                makeFunctionalityCall(x356, x1181, "en16:Reference Exportpt25:Exportação de Referências", null, null, null,
                        "en16:reference-reportpt25:exportacao-de-referencias");
        FunctionalityCall x363 =
                makeFunctionalityCall(x356, x1182, "en11:Debt Reportpt20:Relatório de Dividas", null, null, null,
                        "en11:debt-reportpt20:relatorio-de-dividas");
        Section x366 =
                new Section(x96, MultiLanguageString.importFromString("en19:Calendar Managementpt21:Gestão de Calendários"));
        x366.setName(MultiLanguageString.importFromString("en19:Calendar Managementpt21:Gestão de Calendários"));
        x366.setNormalizedName(MultiLanguageString.importFromString("en19:calendar-managementpt21:gestao-de-calendarios"));
        FunctionalityCall x365 =
                makeFunctionalityCall(x366, x1184, "en18:Academic Calendarspt22:Calendários Académicos", null, null, null,
                        "en18:academic-calendarspt22:calendarios-academicos");
        FunctionalityCall x367 =
                makeFunctionalityCall(x366, x1186, "en24:Create Academic Calendarpt26:Criar Calendário Académico", null, null,
                        null, "en24:create-academic-calendarpt26:criar-calendario-academico");
        Section x370 =
                new Section(x96, MultiLanguageString.importFromString("en21:Scientific Activitiespt23:Actividades Científicas"));
        x370.setName(MultiLanguageString.importFromString("en21:Scientific Activitiespt23:Actividades Científicas"));
        x370.setNormalizedName(MultiLanguageString.importFromString("en21:scientific-activitiespt23:actividades-cientificas"));
        FunctionalityCall x369 =
                makeFunctionalityCall(x370, x1188, "en12:Edit Journalpt14:Editar Revista", null, null, null,
                        "en12:edit-journalpt14:editar-revista");
        FunctionalityCall x371 =
                makeFunctionalityCall(x370, x1190, "en10:Edit Eventpt13:Editar Evento", null, null, null,
                        "en10:edit-eventpt13:editar-evento");
        FunctionalityCall x372 =
                makeFunctionalityCall(x370, x1191, "en14:Merge Journalspt15:Juntar Revistas", null, null, null,
                        "en14:merge-journalspt15:juntar-revistas");
        FunctionalityCall x373 =
                makeFunctionalityCall(x370, x1192, "en12:Merge Eventspt14:Juntar Eventos", null, null, null,
                        "en12:merge-eventspt14:juntar-eventos");
        FunctionalityCall x374 =
                makeFunctionalityCall(x370, x1193, "en20:Merge Journal Issuespt25:Juntar Volumes de Revista", null, null, null,
                        "en20:merge-journal-issuespt25:juntar-volumes-de-revista");
        FunctionalityCall x375 =
                makeFunctionalityCall(x370, x1194, "en20:Merge Event Editionspt24:Juntar Edições de Evento", null, null, null,
                        "en20:merge-event-editionspt24:juntar-edicoes-de-evento");
        FunctionalityCall x376 =
                makeFunctionalityCall(x370, x1195, "en20:Merge Journal Issuespt25:Juntar Volumes de Revista", null, null, null,
                        "en20:merge-journal-issuespt25:juntar-volumes-de-revista");
        FunctionalityCall x377 =
                makeFunctionalityCall(x370, x1196, "en20:Merge Event Editionspt24:Juntar Edições de Evento", null, null, null,
                        "en20:merge-event-editionspt24:juntar-edicoes-de-evento");
        Section x380 =
                new Section(
                        x96,
                        MultiLanguageString
                                .importFromString("en34:Persistent Groups (Access Control)pt40:Grupos Persistentes (Controlo de Acesso)"));
        x380.setName(MultiLanguageString
                .importFromString("en34:Persistent Groups (Access Control)pt40:Grupos Persistentes (Controlo de Acesso)"));
        x380.setNormalizedName(MultiLanguageString
                .importFromString("en32:persistent-groups-access-controlpt38:grupos-persistentes-controlo-de-acesso"));
        FunctionalityCall x379 =
                makeFunctionalityCall(x380, x1198, "en13:Manage Groupspt12:Gerir Grupos", null, null, null,
                        "en13:manage-groupspt12:gerir-grupos");
        Section x383 = new Section(x96, MultiLanguageString.importFromString("en18:Bolonha Transitionpt20:Transição de Bolonha"));
        x383.setName(MultiLanguageString.importFromString("en18:Bolonha Transitionpt20:Transição de Bolonha"));
        x383.setNormalizedName(MultiLanguageString.importFromString("en18:bolonha-transitionpt20:transicao-de-bolonha"));
        FunctionalityCall x382 =
                makeFunctionalityCall(x383, x1201, "en24:Global Equivalence Planspt30:Planos de Equivalência Globais", null,
                        null, null, "en24:global-equivalence-planspt30:planos-de-equivalencia-globais");
        FunctionalityCall x384 =
                makeFunctionalityCall(x383, x1203, "en23:Local Equivalence Planspt29:Planos de Equivalência Locais", null, null,
                        null, "en23:local-equivalence-planspt29:planos-de-equivalencia-locais");
        FunctionalityCall x385 =
                makeFunctionalityCall(x383, x1204, "en18:Student Curriculumpt18:Currículo do Aluno", null, null, null,
                        "en18:student-curriculumpt18:curriculo-do-aluno");
        Section x388 = new Section(x96, MultiLanguageString.importFromString("en8:Studentspt6:Alunos"));
        x388.setName(MultiLanguageString.importFromString("en8:Studentspt6:Alunos"));
        x388.setNormalizedName(MultiLanguageString.importFromString("en8:studentspt6:alunos"));
        x388.setMaximizable(false);
        FunctionalityCall x387 =
                makeFunctionalityCall(x388, x1206, "en6:Managept5:Gerir", null, null, null, "en6:managept5:gerir");
        FunctionalityCall x389 =
                makeFunctionalityCall(x388, x1208, "en15:Move Enrolmentspt16:Mover Inscrições", null, null, null,
                        "en15:move-enrolmentspt16:mover-inscricoes");
        FunctionalityCall x390 =
                makeFunctionalityCall(x388, x1209, "en20:Dismissal Managementpt19:Gestão de Dispensas", null, null, null,
                        "en20:dismissal-managementpt19:gestao-de-dispensas");
        FunctionalityCall x391 =
                makeFunctionalityCall(x388, x1210, "en23:Substitution Managementpt23:Gestão de Substituições", null, null, null,
                        "en23:substitution-managementpt23:gestao-de-substituicoes");
        FunctionalityCall x392 =
                makeFunctionalityCall(x388, x1211, "en22:Equivalence Managementpt23:Gestão de Equivalências", null, null, null,
                        "en22:equivalence-managementpt23:gestao-de-equivalencias");
        FunctionalityCall x393 =
                makeFunctionalityCall(x388, x1212, "en17:Credit Managementpt18:Gestão de Créditos", null, null, null,
                        "en17:credit-managementpt18:gestao-de-creditos");
        FunctionalityCall x394 =
                makeFunctionalityCall(x388, x1213, "en21:Conclusion Managementpt20:Gestão de Apuramento", null, null, null,
                        "en21:conclusion-managementpt20:gestao-de-apuramento");
        FunctionalityCall x395 =
                makeFunctionalityCall(x388, x1214, "en16:State Managementpt17:Gestão de Estados", null, null, null,
                        "en16:state-management");
        FunctionalityCall x396 =
                makeFunctionalityCall(x388, x1223, "en19:View Enrolment Logspt21:Ver Logs de Inscrição", null, null, null,
                        "en19:view-enrolment-logspt21:ver-logs-de-inscricao");
        FunctionalityCall x397 =
                makeFunctionalityCall(x388, x1220, "en17:Cancel Mark Sheet", null, null, null,
                        "en17:cancel-mark-sheetpt12:anular-pauta");
        FunctionalityCall x398 =
                makeFunctionalityCall(x388, x1216, "en11:DGES Importpt18:Importação da DGES", null, null, null,
                        "en11:dges-importpt18:importacao-da-dges");
        FunctionalityCall x399 =
                makeFunctionalityCall(x388, x1217, "en25:Special Season Enrolmentspt28:Inscrições em Época Especial", null, null,
                        null, "en25:special-season-enrolmentspt28:inscricoes-em-epoca-especial");
        FunctionalityCall x400 =
                makeFunctionalityCall(x388, x1218, "en29:1st Time Applications Summarypt27:Sumário Candidaturas 1ª vez", null,
                        null, null, "en29:1st-time-applications-summarypt26:sumario-candidaturas-1-vez");
        FunctionalityCall x402 =
                makeFunctionalityCall(x96, x913, "en8:Homepagept16:Página Principal", null, null, null,
                        "en8:homepagept16:pagina-principal");
        Section x404 =
                new Section(x96,
                        MultiLanguageString
                                .importFromString("en30:Generated Documents Managementpt28:Gestao de Documentos Gerados"));
        x404.setName(MultiLanguageString.importFromString("en30:Generated Documents Managementpt28:Gestao de Documentos Gerados"));
        x404.setNormalizedName(MultiLanguageString
                .importFromString("en30:generated-documents-managementpt28:gestao-de-documentos-gerados"));
        x404.setMaximizable(false);
        FunctionalityCall x403 =
                makeFunctionalityCall(x404, x1226, "en16:Search Documentspt19:Procurar Documentos", null, null, null,
                        "en16:search-documentspt19:procurar-documentos");
        Section x407 = new Section(x96, MultiLanguageString.importFromString("en11:Photographspt11:Fotografias"));
        x407.setName(MultiLanguageString.importFromString("en11:Photographspt11:Fotografias"));
        x407.setNormalizedName(MultiLanguageString.importFromString("en11:photographspt11:fotografias"));
        x407.setMaximizable(false);
        FunctionalityCall x406 =
                makeFunctionalityCall(x407, x1229, "en7:Historypt9:Historial", null, null, null, "en7:historypt9:historial");
        FunctionalityCall x408 =
                makeFunctionalityCall(x407, x1231, "en10:Rejectionspt9:Rejeicoes", null, null, null,
                        "en10:rejectionspt9:rejeicoes");
        FunctionalityCall x409 =
                makeFunctionalityCall(x407, x1232, "en9:Approvalspt10:Aprovacoes", null, null, null,
                        "en9:approvalspt10:aprovacoes");
        Section x412 =
                new Section(x96, MultiLanguageString.importFromString("en20:Post Graduation Unitpt20:Núcleo Pós Graduação"));
        x412.setName(MultiLanguageString.importFromString("en20:Post Graduation Unitpt20:Núcleo Pós Graduação"));
        x412.setNormalizedName(MultiLanguageString.importFromString("en20:post-graduation-unitpt20:nucleo-pos-graduacao"));
        FunctionalityCall x411 =
                makeFunctionalityCall(x412, x1234, "en13:PhD Processespt25:Processos de doutoramento", null, null, null,
                        "en13:phd-processespt25:processos-de-doutoramento");
        Section x415 = new Section(x96, MultiLanguageString.importFromString("en21:External Scholarshipspt15:Bolsas Externas"));
        x415.setName(MultiLanguageString.importFromString("en21:External Scholarshipspt15:Bolsas Externas"));
        x415.setNormalizedName(MultiLanguageString.importFromString("en21:external-scholarshipspt15:bolsas-externas"));
        FunctionalityCall x414 =
                makeFunctionalityCall(x415, x1237, "en13:List Entitiespt16:Listar Entidades", null, null, null,
                        "en13:list-entitiespt16:listar-entidades");
        FunctionalityCall x416 =
                makeFunctionalityCall(x415, x1239, "en10:Add Entitypt18:Adicionar Entidade", null, null, null,
                        "en10:add-entitypt18:adicionar-entidade");
        Section x4150 = new Section(x96, MultiLanguageString.importFromString("en21:External Scholarshipspt15:Bolsas Externas"));
        x4150.setName(MultiLanguageString.importFromString("en18:Associated Objectspt19:Objectos Associados"));
        x4150.setNormalizedName(MultiLanguageString.importFromString("en18:associated-objectspt19:objectos-associados"));
        FunctionalityCall x4140 =
                makeFunctionalityCall(x4150, x12370, "en25:Manage Associated Objectspt25:Gerir Objectos Associados", null, null,
                        null, "en25:manage-associated-objectspt25:gerir-objectos-associados");
        ExpressionGroupAvailability x1694 = new ExpressionGroupAvailability(x96, "role(MANAGER)");
        x1694.setTargetGroup(Group.fromString("role(MANAGER)"));
        x96.setAvailabilityPolicy(x1694);
        Section x420 = new Section(x3, MultiLanguageString.importFromString("pt40:Secção de Pessoal Docente e Investigador"));
        x420.setName(MultiLanguageString.importFromString("pt40:Secção de Pessoal Docente e Investigador"));
        x420.setNormalizedName(MultiLanguageString.importFromString("pt40:seccao-de-pessoal-docente-e-investigador"));
        FunctionalityCall x419 =
                makeFunctionalityCall(x420, x1242, "pt40:Secção de Pessoal Docente e Investigador", null, null, null,
                        "pt40:seccao-de-pessoal-docente-e-investigador");
        ExpressionGroupAvailability x1695 = new ExpressionGroupAvailability(x420, "role(CREDITS_MANAGER)");
        x1695.setTargetGroup(Group.fromString("role(CREDITS_MANAGER)"));
        x420.setAvailabilityPolicy(x1695);
        Section x423 =
                new Section(x3,
                        MultiLanguageString.importFromString("pt53:Administração de créditos de docentes do departamento"));
        x423.setName(MultiLanguageString.importFromString("pt53:Administração de créditos de docentes do departamento"));
        x423.setNormalizedName(MultiLanguageString.importFromString("pt53:administracao-de-creditos-de-docentes-do-departamento"));
        FunctionalityCall x422 =
                makeFunctionalityCall(x423, x1245, "pt53:Administração de créditos de docentes do departamento", null, null,
                        null, "pt53:administracao-de-creditos-de-docentes-do-departamento");
        ExpressionGroupAvailability x1696 = new ExpressionGroupAvailability(x423, "role(DEPARTMENT_CREDITS_MANAGER)");
        x1696.setTargetGroup(Group.fromString("role(DEPARTMENT_CREDITS_MANAGER)"));
        x423.setAvailabilityPolicy(x1696);
        Section x426 = new Section(x3, MultiLanguageString.importFromString("en18:Scientific Councilpt19:Conselho Científico"));
        x426.setName(MultiLanguageString.importFromString("en18:Scientific Councilpt19:Conselho Científico"));
        x426.setNormalizedName(MultiLanguageString.importFromString("en18:scientific-councilpt19:conselho-cientifico"));
        x426.setMaximizable(false);
        FunctionalityCall x425 =
                makeFunctionalityCall(x426, x1248, "pt19:Conselho Científico", null, null, null, "pt19:conselho-cientifico");
        ExpressionGroupAvailability x1697 = new ExpressionGroupAvailability(x426, "role(SCIENTIFIC_COUNCIL)");
        x1697.setTargetGroup(Group.fromString("role(SCIENTIFIC_COUNCIL)"));
        x426.setAvailabilityPolicy(x1697);
        Section x429 = new Section(x3, MultiLanguageString.importFromString("en8:Operatorpt8:Operador"));
        x429.setName(MultiLanguageString.importFromString("en8:Operatorpt8:Operador"));
        x429.setNormalizedName(MultiLanguageString.importFromString("en8:operatorpt8:operador"));
        x429.setMaximizable(false);
        FunctionalityCall x428 = makeFunctionalityCall(x429, x1251, "pt8:Operador", null, null, null, "pt8:operador");
        ExpressionGroupAvailability x1698 = new ExpressionGroupAvailability(x429, "role(OPERATOR)");
        x1698.setTargetGroup(Group.fromString("role(OPERATOR)"));
        x429.setAvailabilityPolicy(x1698);
        Section x432 = new Section(x3, MultiLanguageString.importFromString("pt10:Seminários"));
        x432.setName(MultiLanguageString.importFromString("pt10:Seminários"));
        x432.setNormalizedName(MultiLanguageString.importFromString("pt10:seminarios"));
        FunctionalityCall x431 = makeFunctionalityCall(x432, x1254, "pt10:Seminários", null, null, null, "pt10:seminarios");
        ExpressionGroupAvailability x1699 = new ExpressionGroupAvailability(x432, "role(SEMINARIES_COORDINATOR)");
        x1699.setTargetGroup(Group.fromString("role(SEMINARIES_COORDINATOR)"));
        x432.setAvailabilityPolicy(x1699);
        Section x435 = new Section(x3, MultiLanguageString.importFromString("pt8:Bolseiro"));
        x435.setName(MultiLanguageString.importFromString("pt8:Bolseiro"));
        x435.setNormalizedName(MultiLanguageString.importFromString("pt8:bolseiro"));
        FunctionalityCall x434 = makeFunctionalityCall(x435, x1257, "pt8:Bolseiro", null, null, null, "pt8:bolseiro");
        ExpressionGroupAvailability x1700 = new ExpressionGroupAvailability(x435, "role(GRANT_OWNER)");
        x1700.setTargetGroup(Group.fromString("role(GRANT_OWNER)"));
        x435.setAvailabilityPolicy(x1700);
        Section x438 = new Section(x3, MultiLanguageString.importFromString("pt26:Secretaria de Departamento"));
        x438.setName(MultiLanguageString.importFromString("pt26:Secretaria de Departamento"));
        x438.setNormalizedName(MultiLanguageString.importFromString("pt26:secretaria-de-departamento"));
        FunctionalityCall x437 =
                makeFunctionalityCall(x438, x1260, "pt26:Secretaria de Departamento", null, null, null,
                        "pt26:secretaria-de-departamento");
        ExpressionGroupAvailability x1701 = new ExpressionGroupAvailability(x438, "role(DEPARTMENT_ADMINISTRATIVE_OFFICE)");
        x1701.setTargetGroup(Group.fromString("role(DEPARTMENT_ADMINISTRATIVE_OFFICE)"));
        x438.setAvailabilityPolicy(x1701);
        Section x441 = new Section(x3, MultiLanguageString.importFromString("pt33:Gabinete de Estudos e Planeamento"));
        x441.setName(MultiLanguageString.importFromString("pt33:Gabinete de Estudos e Planeamento"));
        x441.setNormalizedName(MultiLanguageString.importFromString("pt33:gabinete-de-estudos-e-planeamento"));
        FunctionalityCall x440 =
                makeFunctionalityCall(x441, x1263, "pt33:Gabinete de Estudos e Planeamento", null, null, null,
                        "pt33:gabinete-de-estudos-e-planeamento");
        ExpressionGroupAvailability x1702 = new ExpressionGroupAvailability(x441, "role(GEP)");
        x1702.setTargetGroup(Group.fromString("role(GEP)"));
        x441.setAvailabilityPolicy(x1702);
        Section x444 = new Section(x3, MultiLanguageString.importFromString("en16:Steering Councilpt18:Conselho Directivo"));
        x444.setName(MultiLanguageString.importFromString("en16:Steering Councilpt18:Conselho Directivo"));
        x444.setNormalizedName(MultiLanguageString.importFromString("en16:steering-councilpt18:conselho-directivo"));
        x444.setMaximizable(false);
        FunctionalityCall x443 =
                makeFunctionalityCall(x444, x1266, "pt18:Conselho Directivo", null, null, null, "pt18:conselho-directivo");
        ExpressionGroupAvailability x1703 = new ExpressionGroupAvailability(x444, "role(DIRECTIVE_COUNCIL)");
        x1703.setTargetGroup(Group.fromString("role(DIRECTIVE_COUNCIL)"));
        x444.setAvailabilityPolicy(x1703);
        Section x447 = new Section(x3, MultiLanguageString.importFromString("pt8:Delegado"));
        x447.setName(MultiLanguageString.importFromString("pt8:Delegado"));
        x447.setNormalizedName(MultiLanguageString.importFromString("pt8:delegado"));
        FunctionalityCall x446 = makeFunctionalityCall(x447, x1269, "pt8:Delegado", null, null, null, "pt8:delegado");
        ExpressionGroupAvailability x1704 = new ExpressionGroupAvailability(x447, "role(DELEGATE)");
        x1704.setTargetGroup(Group.fromString("role(DELEGATE)"));
        x447.setAvailabilityPolicy(x1704);
        Section x450 = new Section(x3, MultiLanguageString.importFromString("en8:Projectspt9:Projectos"));
        x450.setName(MultiLanguageString.importFromString("en8:Projectspt9:Projectos"));
        x450.setNormalizedName(MultiLanguageString.importFromString("en8:projectspt9:projectos"));
        x450.setMaximizable(false);
        FunctionalityCall x449 = makeFunctionalityCall(x450, x1272, "pt9:Projectos", null, null, null, "pt9:projectos");
        ExpressionGroupAvailability x1705 = new ExpressionGroupAvailability(x450, "role(PROJECTS_MANAGER)");
        x1705.setTargetGroup(Group.fromString("role(PROJECTS_MANAGER)"));
        x450.setAvailabilityPolicy(x1705);
        Section x453 = new Section(x3, MultiLanguageString.importFromString("pt24:Projectos Institucionais"));
        x453.setName(MultiLanguageString.importFromString("pt24:Projectos Institucionais"));
        x453.setNormalizedName(MultiLanguageString.importFromString("pt24:projectos-institucionais"));
        FunctionalityCall x452 =
                makeFunctionalityCall(x453, x1275, "pt24:Projectos Institucionais", null, null, null,
                        "pt24:projectos-institucionais");
        ExpressionGroupAvailability x1706 = new ExpressionGroupAvailability(x453, "role(INSTITUCIONAL_PROJECTS_MANAGER)");
        x1706.setTargetGroup(Group.fromString("role(INSTITUCIONAL_PROJECTS_MANAGER)"));
        x453.setAvailabilityPolicy(x1706);
        Section x456 = new Section(x3, MultiLanguageString.importFromString("pt7:Bolonha"));
        x456.setName(MultiLanguageString.importFromString("pt7:Bolonha"));
        x456.setNormalizedName(MultiLanguageString.importFromString("pt7:bolonha"));
        FunctionalityCall x455 = makeFunctionalityCall(x456, x1278, "pt7:Bolonha", null, null, null, "pt7:bolonha");
        ExpressionGroupAvailability x1707 = new ExpressionGroupAvailability(x456, "role(BOLONHA_MANAGER)");
        x1707.setTargetGroup(Group.fromString("role(BOLONHA_MANAGER)"));
        x456.setAvailabilityPolicy(x1707);
        Section x459 = new Section(x3, MultiLanguageString.importFromString("en16:Space Managementpt17:Gestão de Espaços"));
        x459.setName(MultiLanguageString.importFromString("en16:Space Managementpt17:Gestão de Espaços"));
        x459.setNormalizedName(MultiLanguageString.importFromString("en16:space-managementpt17:gestao-de-espacos"));
        x459.setMaximizable(false);
        FunctionalityCall x458 =
                makeFunctionalityCall(x459, x1281, "pt17:Gestão de Espaços", null, null, null, "pt17:gestao-de-espacos");
        ExpressionGroupAvailability x1708 = new ExpressionGroupAvailability(x459, "role(SPACE_MANAGER)");
        x1708.setTargetGroup(Group.fromString("role(SPACE_MANAGER)"));
        x459.setAvailabilityPolicy(x1708);
        Section x462 = new Section(x3, MultiLanguageString.importFromString("pt10:Curriculum"));
        x462.setName(MultiLanguageString.importFromString("pt10:Curriculum"));
        x462.setNormalizedName(MultiLanguageString.importFromString("pt10:curriculum"));
        x462.setMaximizable(false);
        FunctionalityCall x461 = makeFunctionalityCall(x462, x1284, "pt12:Investigação", null, null, null, "pt12:investigacao");
        ExpressionGroupAvailability x1709 = new ExpressionGroupAvailability(x462, "role(RESEARCHER) || author");
        x1709.setTargetGroup(Group.fromString("role(RESEARCHER) || author"));
        x462.setAvailabilityPolicy(x1709);
        Section x465 = new Section(x3, MultiLanguageString.importFromString("pt7:Alumni "));
        x465.setName(MultiLanguageString.importFromString("pt7:Alumni "));
        x465.setNormalizedName(MultiLanguageString.importFromString("pt7:alumni-"));
        x465.setMaximizable(false);
        FunctionalityCall x464 = makeFunctionalityCall(x465, x1288, "pt6:Alumni", null, null, null, "pt6:alumni");
        ExpressionGroupAvailability x1710 = new ExpressionGroupAvailability(x465, "role(ALUMNI)");
        x1710.setTargetGroup(Group.fromString("role(ALUMNI)"));
        x465.setAvailabilityPolicy(x1710);
        Section x468 = new Section(x3, MultiLanguageString.importFromString("pt19:Conselho Pedagógico"));
        x468.setName(MultiLanguageString.importFromString("pt19:Conselho Pedagógico"));
        x468.setNormalizedName(MultiLanguageString.importFromString("pt19:conselho-pedagogico"));
        FunctionalityCall x467 =
                makeFunctionalityCall(x468, x1291, "pt19:Conselho Pedagógico", null, null, null, "pt19:conselho-pedagogico");
        ExpressionGroupAvailability x1711 = new ExpressionGroupAvailability(x468, "role(PEDAGOGICAL_COUNCIL) || role(TUTORSHIP)");
        x1711.setTargetGroup(Group.fromString("role(PEDAGOGICAL_COUNCIL) || role(TUTORSHIP)"));
        x468.setAvailabilityPolicy(x1711);
        Section x471 = new Section(x3, MultiLanguageString.importFromString("pt19:Portal do Candidato"));
        x471.setName(MultiLanguageString.importFromString("pt19:Portal do Candidato"));
        x471.setNormalizedName(MultiLanguageString.importFromString("pt19:portal-do-candidato"));
        FunctionalityCall x470 =
                makeFunctionalityCall(x471, x1294, "pt19:Portal do Candidato", null, null, null, "pt19:portal-do-candidato");
        ExpressionGroupAvailability x1712 = new ExpressionGroupAvailability(x471, "role(CANDIDATE)");
        x1712.setTargetGroup(Group.fromString("role(CANDIDATE)"));
        x471.setAvailabilityPolicy(x1712);
        Section x474 = new Section(x3, MultiLanguageString.importFromString("en9:Messagingpt11:Comunicação"));
        x474.setName(MultiLanguageString.importFromString("en9:Messagingpt11:Comunicação"));
        x474.setNormalizedName(MultiLanguageString.importFromString("en9:messagingpt11:comunicacao"));
        FunctionalityCall x473 = makeFunctionalityCall(x474, x1297, "pt11:Comunicação", null, null, null, "pt11:comunicacao");
        ExpressionGroupAvailability x1713 = new ExpressionGroupAvailability(x474, "role(PERSON)");
        x1713.setTargetGroup(Group.fromString("role(PERSON)"));
        x474.setAvailabilityPolicy(x1713);
        Section x479 = new Section(x3, MultiLanguageString.importFromString("pt8:PúblicoX"));
        x479.setName(MultiLanguageString.importFromString("pt8:PúblicoX"));
        x479.setNormalizedName(MultiLanguageString.importFromString("pt8:publicox"));
        x479.setMaximizable(false);
        x479.setVisible(false);
        Section x478 = new Section(x479, MultiLanguageString.importFromString("pt5:Sites"));
        x478.setName(MultiLanguageString.importFromString("pt5:Sites"));
        x478.setNormalizedName(MultiLanguageString.importFromString("pt5:sites"));
        Section x477 = new Section(x478, MultiLanguageString.importFromString("pt19:Disciplina Execução"));
        x477.setName(MultiLanguageString.importFromString("pt19:Disciplina Execução"));
        x477.setNormalizedName(MultiLanguageString.importFromString("pt19:disciplina-execucao"));
        FunctionalityCall x476 =
                makeFunctionalityCall(x477, x1300, "en12:Initial Pageen8:Homepagept14:Página Inicial", null, null, null,
                        "en12:initial-pageen8:homepagept14:pagina-inicial");
        FunctionalityCall x480 =
                makeFunctionalityCall(x477, x1304, "en13:Announcementspt8:Anuncios", null, null, null,
                        "en13:announcementspt8:anuncios");
        FunctionalityCall x481 =
                makeFunctionalityCall(x477, x1305, "en8:Planningpt11:Planeamento", null, null, null,
                        "en8:planningpt11:planeamento");
        FunctionalityCall x482 =
                makeFunctionalityCall(x477, x1306, "en9:Summariespt8:Sumários", null, null, null, "en9:summariespt8:sumarios");
        FunctionalityCall x483 =
                makeFunctionalityCall(x477, x1307, "en10:Objectivespt10:Objectivos", null, null, null,
                        "en10:objectivespt10:objectivos");
        FunctionalityCall x484 =
                makeFunctionalityCall(x477, x1308, "en7:Programpt8:Programa", null, null, null, "en7:programpt8:programa");
        FunctionalityCall x485 =
                makeFunctionalityCall(x477, x1309, "en17:Evaluation Methodpt19:Método de Avaliação", null, null, null,
                        "en17:evaluation-methodpt19:metodo-de-avaliacao");
        FunctionalityCall x486 =
                makeFunctionalityCall(x477, x1310, "en12:Bibliographypt12:Bibliografia", null, null, null,
                        "en12:bibliographypt12:bibliografia");
        FunctionalityCall x487 =
                makeFunctionalityCall(x477, x1311, "en9:Timesheetpt7:Horário", null, null, null, "en9:timesheetpt7:horario");
        FunctionalityCall x488 =
                makeFunctionalityCall(x477, x1312, "en6:Shiftspt6:Turnos", null, null, null, "en6:shiftspt6:turnos");
        FunctionalityCall x489 =
                makeFunctionalityCall(x477, x1313, "en10:Evaluationpt9:Avaliaçâo", null, null, null,
                        "en10:evaluationpt9:avaliacao");
        FunctionalityCall x490 =
                makeFunctionalityCall(x477, x1314, "en6:Groupspt12:Agrupamentos", null, null, null, "en6:groupspt12:agrupamentos");
        FunctionalityCall x491 = makeFunctionalityCall(x477, x1315, "pt10:Ver Secção", null, null, null, "pt10:ver-seccao");
        FunctionalityCall x492 =
                makeFunctionalityCall(x477, x1316, "en15:Search Contentspt21:Pesquisa de Conteúdos", null, null, null,
                        "en15:search-contentspt21:pesquisa-de-conteudos");
        Section x495 = new Section(x478, MultiLanguageString.importFromString("pt12:Site Pessoal"));
        x495.setName(MultiLanguageString.importFromString("pt12:Site Pessoal"));
        x495.setNormalizedName(MultiLanguageString.importFromString("pt12:site-pessoal"));
        FunctionalityCall x494 =
                makeFunctionalityCall(x495, x1319, "en12:Presentationpt12:Apresentação", null, null, null,
                        "en12:presentationpt12:apresentacao");
        FunctionalityCall x496 =
                makeFunctionalityCall(x495, x1321, "en18:Research Interestspt22:Interesses Científicos", null, null, null,
                        "en18:research-interestspt22:interesses-cientificos");
        FunctionalityCall x497 =
                makeFunctionalityCall(x495, x1322, "en12:Publicationspt11:Publicações", null, null, null,
                        "en12:publicationspt11:publicacoes");
        FunctionalityCall x498 =
                makeFunctionalityCall(x495, x1323, "en7:Patentspt8:Patentes", null, null, null, "en7:patentspt8:patentes");
        FunctionalityCall x499 =
                makeFunctionalityCall(x495, x1324, "en23:Scientifical Activitiespt23:Actividades Científicas", null, null, null,
                        "en23:scientifical-activitiespt23:actividades-cientificas");
        FunctionalityCall x500 =
                makeFunctionalityCall(x495, x1325, "en6:Prizespt7:Prémios", null, null, null, "en6:prizespt7:premios");
        Section x503 = new Section(x478, MultiLanguageString.importFromString("en6:Degreept5:Curso"));
        x503.setName(MultiLanguageString.importFromString("en6:Degreept5:Curso"));
        x503.setNormalizedName(MultiLanguageString.importFromString("en6:degreept5:curso"));
        FunctionalityCall x502 =
                makeFunctionalityCall(x503, x1327, "en11:Descriptionpt9:Descrição", null, null, null,
                        "en11:descriptionpt9:descricao");
        FunctionalityCall x504 =
                makeFunctionalityCall(x503, x1329, "en13:Announcementspt8:Anuncios", null, null, null,
                        "en13:announcementspt8:anuncios");
        FunctionalityCall x505 =
                makeFunctionalityCall(x503, x1330, "en22:Admission Requirementspt16:Regime de Acesso", null, null, null,
                        "en22:admission-requirementspt16:regime-de-acesso");
        FunctionalityCall x506 =
                makeFunctionalityCall(x503, x1331, "en19:Professional Statuspt21:Estatuto Profissional", null, null, null,
                        "en19:professional-statuspt21:estatuto-profissional");
        FunctionalityCall x507 =
                makeFunctionalityCall(x503, x1332, "en15:Curricular Planpt16:Plano Curricular", null, null, null,
                        "en15:curricular-planpt16:plano-curricular");
        FunctionalityCall x508 =
                makeFunctionalityCall(x503, x1333, "en16:Course Web Pagespt22:Páginas de Disciplinas", null, null, null,
                        "en16:course-web-pagespt22:paginas-de-disciplinas");
        FunctionalityCall x509 =
                makeFunctionalityCall(x503, x1334, "en15:Class timetablept18:Horários por Turma", null, null, null,
                        "en15:class-timetablept18:horarios-por-turma");
        FunctionalityCall x510 =
                makeFunctionalityCall(x503, x1335, "en11:Evaluationspt10:Avaliações", null, null, null,
                        "en11:evaluationspt10:avaliacoes");
        FunctionalityCall x511 =
                makeFunctionalityCall(x503, x1336, "en13:Dissertationspt12:Dissertações", null, null, null,
                        "en13:dissertationspt12:dissertacoes");
        Section x514 = new Section(x478, MultiLanguageString.importFromString("pt12:Departamento"));
        x514.setName(MultiLanguageString.importFromString("pt12:Departamento"));
        x514.setNormalizedName(MultiLanguageString.importFromString("pt12:departamento"));
        FunctionalityCall x513 =
                makeFunctionalityCall(x514, x1338, "en13:Announcementspt8:Anuncios", null, null, null,
                        "en13:announcementspt8:anuncios");
        FunctionalityCall x515 =
                makeFunctionalityCall(x514, x1340, "en7:Facultypt8:Docentes", null, null, null, "en7:facultypt8:docentes");
        FunctionalityCall x516 =
                makeFunctionalityCall(x514, x1341, "en5:Staffpt12:Funcionários", null, null, null, "en5:staffpt12:funcionarios");
        FunctionalityCall x517 =
                makeFunctionalityCall(x514, x1342, "en7:Coursespt11:Disciplinas", null, null, null, "en7:coursespt11:disciplinas");
        FunctionalityCall x518 =
                makeFunctionalityCall(x514, x1343, "en6:Eventspt7:Eventos", null, null, null, "en6:eventspt7:eventos");
        FunctionalityCall x519 =
                makeFunctionalityCall(x514, x1344, "en7:Degreespt6:Cursos", null, null, null, "en7:degreespt6:cursos");
        FunctionalityCall x520 =
                makeFunctionalityCall(x514, x1345, "en12:Organizationpt9:Estrutura", null, null, null,
                        "en12:organizationpt9:estrutura");
        FunctionalityCall x521 =
                makeFunctionalityCall(x514, x1346, "en16:Scientific Areaspt17:Áreas Científicas", null, null, null,
                        "en16:scientific-areaspt17:areas-cientificas");
        FunctionalityCall x522 =
                makeFunctionalityCall(x514, x1347, "en13:Dissertationspt12:Dissertações", null, null, null,
                        "en13:dissertationspt12:dissertacoes");
        FunctionalityCall x523 =
                makeFunctionalityCall(x514, x1348, "en12:Publicationspt11:Publicações", null, null, null,
                        "en12:publicationspt11:publicacoes");
        Section x526 = new Section(x478, MultiLanguageString.importFromString("en13:Research Sitept20:Site de Investigação"));
        x526.setName(MultiLanguageString.importFromString("en13:Research Sitept20:Site de Investigação"));
        x526.setNormalizedName(MultiLanguageString.importFromString("en13:research-sitept20:site-de-investigacao"));
        FunctionalityCall x525 =
                makeFunctionalityCall(x526, x1351, "en6:Eventspt7:Eventos", null, null, null, "en6:eventspt7:eventos");
        FunctionalityCall x527 =
                makeFunctionalityCall(x526, x1353, "en13:Announcementspt8:Anuncios", null, null, null,
                        "en13:announcementspt8:anuncios");
        FunctionalityCall x528 =
                makeFunctionalityCall(x526, x1354, "en7:Memberspt7:Membros", null, null, null, "en7:memberspt7:membros");
        FunctionalityCall x529 =
                makeFunctionalityCall(x526, x1355, "en12:Publicationspt11:Publicações", null, null, null,
                        "en12:publicationspt11:publicacoes");
        FunctionalityCall x530 =
                makeFunctionalityCall(x526, x1356, "en12:Organizationpt9:Estrutura", null, null, null,
                        "en12:organizationpt9:estrutura");
        FunctionalityCall x531 =
                makeFunctionalityCall(x526, x1357, "en8:Subunitspt11:Subunidades", null, null, null,
                        "en8:subunitspt11:subunidades");
        Section x534 = new Section(x478, MultiLanguageString.importFromString("pt19:Conselho Pedagógico"));
        x534.setName(MultiLanguageString.importFromString("pt19:Conselho Pedagógico"));
        x534.setNormalizedName(MultiLanguageString.importFromString("pt19:conselho-pedagogico"));
        FunctionalityCall x533 =
                makeFunctionalityCall(x534, x1360, "en12:Organizationpt9:Estrutura", null, null, null,
                        "en12:organizationpt9:estrutura");
        Section x537 = new Section(x478, MultiLanguageString.importFromString("pt19:Conselho Científico"));
        x537.setName(MultiLanguageString.importFromString("pt19:Conselho Científico"));
        x537.setNormalizedName(MultiLanguageString.importFromString("pt19:conselho-cientifico"));
        FunctionalityCall x536 =
                makeFunctionalityCall(x537, x1364, "en12:Organizationpt9:Estrutura", null, null, null,
                        "en12:organizationpt9:estrutura");
        Section x540 = new Section(x478, MultiLanguageString.importFromString("en14:ScientificAreapt15:Área Científica"));
        x540.setName(MultiLanguageString.importFromString("en14:ScientificAreapt15:Área Científica"));
        x540.setNormalizedName(MultiLanguageString.importFromString("en14:scientificareapt15:area-cientifica"));
        FunctionalityCall x539 =
                makeFunctionalityCall(x540, x1368, "en12:Publicationspt11:Publicações", null, null, null,
                        "en12:publicationspt11:publicacoes");
        FunctionalityCall x541 =
                makeFunctionalityCall(x540, x1370, "en6:Eventspt7:Eventos", null, null, null, "en6:eventspt7:eventos");
        FunctionalityCall x542 =
                makeFunctionalityCall(x540, x1371, "en13:Announcementspt8:Anuncios", null, null, null,
                        "en13:announcementspt8:anuncios");
        FunctionalityCall x543 =
                makeFunctionalityCall(x540, x1372, "en12:Organizationpt11:Organização", null, null, null,
                        "en12:organizationpt11:organizacao");
        FunctionalityCall x544 =
                makeFunctionalityCall(x540, x1373, "en8:SubUnitspt11:SubUnidades", null, null, null,
                        "en8:subunitspt11:subunidades");
        FunctionalityCall x545 =
                makeFunctionalityCall(x540, x1374, "en7:Facultypt8:Docentes", null, null, null, "en7:facultypt8:docentes");
        FunctionalityCall x546 =
                makeFunctionalityCall(x540, x1375, "en5:Staffpt12:Funcionários", null, null, null, "en5:staffpt12:funcionarios");
        FunctionalityCall x547 =
                makeFunctionalityCall(x540, x1376, "en7:Coursespt11:Disciplinas", null, null, null, "en7:coursespt11:disciplinas");
        Section x552 = new Section(x3, MultiLanguageString.importFromString("pt20:Portal da Tesouraria"));
        x552.setName(MultiLanguageString.importFromString("pt20:Portal da Tesouraria"));
        x552.setNormalizedName(MultiLanguageString.importFromString("pt20:portal-da-tesouraria"));
        FunctionalityCall x551 =
                makeFunctionalityCall(x552, x1484, "en8:Paymentspt10:Pagamentos", null, null, null, "en8:paymentspt10:pagamentos");
        ExpressionGroupAvailability x1714 = new ExpressionGroupAvailability(x552, "role(TREASURY)");
        x1714.setTargetGroup(Group.fromString("role(TREASURY)"));
        x552.setAvailabilityPolicy(x1714);
        Section x556 = new Section(x3, MultiLanguageString.importFromString("en7:Librarypt10:Biblioteca"));
        x556.setName(MultiLanguageString.importFromString("en7:Librarypt10:Biblioteca"));
        x556.setNormalizedName(MultiLanguageString.importFromString("en7:librarypt10:biblioteca"));
        Section x555 = new Section(x556, MultiLanguageString.importFromString("en6:Thesespt12:Dissertações"));
        x555.setName(MultiLanguageString.importFromString("en6:Thesespt12:Dissertações"));
        x555.setNormalizedName(MultiLanguageString.importFromString("en6:thesespt12:dissertacoes"));
        x555.setMaximizable(false);
        FunctionalityCall x554 =
                makeFunctionalityCall(x555, x1488, "en10:Validationpt9:Validação", null, null, null,
                        "en10:validationpt9:validacao");
        ExpressionGroupAvailability x1715 = new ExpressionGroupAvailability(x555, "role(LIBRARY)");
        x1715.setTargetGroup(Group.fromString("role(LIBRARY)"));
        x555.setAvailabilityPolicy(x1715);
        Section x559 = new Section(x556, MultiLanguageString.importFromString("pt7:Cartões"));
        x559.setName(MultiLanguageString.importFromString("pt7:Cartões"));
        x559.setNormalizedName(MultiLanguageString.importFromString("pt7:cartoes"));
        x559.setMaximizable(false);
        FunctionalityCall x558 =
                makeFunctionalityCall(x559, x1491, "pt16:Listagem utentes", null, null, null, "pt16:listagem-utentes");
        FunctionalityCall x560 = makeFunctionalityCall(x559, x1493, "pt13:Gerar Cartões", null, null, null, "pt13:gerar-cartoes");
        FunctionalityCall x561 = makeFunctionalityCall(x559, x1494, "pt12:Gerar Cartas", null, null, null, "pt12:gerar-cartas");
        FunctionalityCall x562 =
                makeFunctionalityCall(x559, x1495, "pt36:Gerar Cartas para Alunos e Bolseiros", null, null, null,
                        "pt36:gerar-cartas-para-alunos-e-bolseiros");
        ExpressionGroupAvailability x1716 = new ExpressionGroupAvailability(x559, "role(LIBRARY)");
        x1716.setTargetGroup(Group.fromString("role(LIBRARY)"));
        x559.setAvailabilityPolicy(x1716);
        FunctionalityCall x564 =
                makeFunctionalityCall(x556, x1486, "en5:Startpt6:Início", null, null, null, "en5:startpt6:inicio");
        FunctionalityCall x565 =
                makeFunctionalityCall(x556, x1497, "pt22:Operador de Biblioteca", null, null, null, "pt22:operador-de-biblioteca");
        ExpressionGroupAvailability x1717 = new ExpressionGroupAvailability(x565, "role(LIBRARY)");
        x1717.setTargetGroup(Group.fromString("role(LIBRARY)"));
        x565.setAvailabilityPolicy(x1717);
        FunctionalityCall x566 =
                makeFunctionalityCall(x556, x1498, "pt31:Actualizar Capacidade e Cacifos", null, null, null,
                        "pt31:actualizar-capacidade-e-cacifos");
        ExpressionGroupAvailability x1718 =
                new ExpressionGroupAvailability(x566, "role(LIBRARY) && (libraryHigherClearanceGroup() || role(MANAGER))");
        x1718.setTargetGroup(Group.fromString("role(LIBRARY) && (libraryHigherClearanceGroup() || role(MANAGER))"));
        x566.setAvailabilityPolicy(x1718);
        FunctionalityCall x567 =
                makeFunctionalityCall(x556, x1499, "pt31:Adicionar ou Remover Operadores", null, null, null,
                        "pt31:adicionar-ou-remover-operadores");
        ExpressionGroupAvailability x1719 =
                new ExpressionGroupAvailability(x567, "role(LIBRARY) && (libraryHigherClearanceGroup() || role(MANAGER))");
        x1719.setTargetGroup(Group.fromString("role(LIBRARY) && (libraryHigherClearanceGroup() || role(MANAGER))"));
        x567.setAvailabilityPolicy(x1719);
        ExpressionGroupAvailability x1720 = new ExpressionGroupAvailability(x556, "role(LIBRARY)");
        x1720.setTargetGroup(Group.fromString("role(LIBRARY)"));
        x556.setAvailabilityPolicy(x1720);
        Section x570 = new Section(x3, MultiLanguageString.importFromString("pt20:Gestão de Património"));
        x570.setName(MultiLanguageString.importFromString("pt20:Gestão de Património"));
        x570.setNormalizedName(MultiLanguageString.importFromString("pt20:gestao-de-patrimonio"));
        FunctionalityCall x569 =
                makeFunctionalityCall(x570, x1501, "pt20:Gestão de Património", null, null, null, "pt20:gestao-de-patrimonio");
        ExpressionGroupAvailability x1721 = new ExpressionGroupAvailability(x570, "role(RESOURCE_MANAGER)");
        x1721.setTargetGroup(Group.fromString("role(RESOURCE_MANAGER)"));
        x570.setAvailabilityPolicy(x1721);
        Section x573 = new Section(x3, MultiLanguageString.importFromString("pt14:Estacionamento"));
        x573.setName(MultiLanguageString.importFromString("pt14:Estacionamento"));
        x573.setNormalizedName(MultiLanguageString.importFromString("pt14:estacionamento"));
        FunctionalityCall x572 =
                makeFunctionalityCall(x573, x1504, "pt14:Estacionamento", null, null, null, "pt14:estacionamento");
        ExpressionGroupAvailability x1722 = new ExpressionGroupAvailability(x573, "role(PARKING_MANAGER)");
        x1722.setTargetGroup(Group.fromString("role(PARKING_MANAGER)"));
        x573.setAvailabilityPolicy(x1722);
        Section x576 = new Section(x3, MultiLanguageString.importFromString("en18:Website Managementpt17:Gestão do WebSite"));
        x576.setName(MultiLanguageString.importFromString("en18:Website Managementpt17:Gestão do WebSite"));
        x576.setNormalizedName(MultiLanguageString.importFromString("en18:website-managementpt17:gestao-do-website"));
        x576.setMaximizable(false);
        FunctionalityCall x575 =
                makeFunctionalityCall(x576, x1507, "pt17:Gestão do WebSite", null, null, null, "pt17:gestao-do-website");
        ExpressionGroupAvailability x1723 = new ExpressionGroupAvailability(x576, "role(WEBSITE_MANAGER)");
        x1723.setTargetGroup(Group.fromString("role(WEBSITE_MANAGER)"));
        x576.setAvailabilityPolicy(x1723);
        Section x579 = new Section(x3, MultiLanguageString.importFromString("pt9:Avaliação"));
        x579.setName(MultiLanguageString.importFromString("pt9:Avaliação"));
        x579.setNormalizedName(MultiLanguageString.importFromString("pt9:avaliacao"));
        FunctionalityCall x578 = makeFunctionalityCall(x579, x1510, "pt9:Avaliação", null, null, null, "pt9:avaliacao");
        ExpressionGroupAvailability x1724 = new ExpressionGroupAvailability(x579, "role(EXAM_COORDINATOR)");
        x1724.setTargetGroup(Group.fromString("role(EXAM_COORDINATOR)"));
        x579.setAvailabilityPolicy(x1724);
        /* Section x582 = new Section(x3, MultiLanguageString.importFromString("pt20:Secretaria Académica"));
        x582.setName(MultiLanguageString.importFromString("pt20:Secretaria Académica"));
        x582.setNormalizedName(MultiLanguageString.importFromString("pt20:secretaria-academica"));
        FunctionalityCall x581 =
                makeFunctionalityCall(x582, x1513, "pt20:Secretaria Académica", null, null, null, "pt20:secretaria-academica");
        ExpressionGroupAvailability x1725 = new ExpressionGroupAvailability(x582, "role(ACADEMIC_ADMINISTRATIVE_OFFICE)");
        x1725.setTargetGroup(Group.fromString("role(ACADEMIC_ADMINISTRATIVE_OFFICE)"));
        x582.setAvailabilityPolicy(x1725);*/
        MetaDomainObjectPortal x585 = new MetaDomainObjectPortal(null);
        x3.addChild(x585);
        x585.setName(MultiLanguageString.importFromString("pt11:disciplinas"));
        x585.setNormalizedName(MultiLanguageString.importFromString("pt11:disciplinas"));
        x585.setPrefix("");
        FunctionalityCall x584 =
                makeFunctionalityCall(x585, x1316, "en15:Search Contentspt21:Pesquisa de Conteúdos", null, null, null,
                        "en15:search-contentspt21:pesquisa-de-conteudos");
        FunctionalityCall x586 = makeFunctionalityCall(x585, x1315, "pt10:Ver Secção", null, null, null, "pt10:ver-seccao");
        FunctionalityCall x587 =
                makeFunctionalityCall(x585, x1314, "en6:Groupspt12:Agrupamentos", null, null, null, "en6:groupspt12:agrupamentos");
        FunctionalityCall x588 =
                makeFunctionalityCall(x585, x1313, "en10:Evaluationpt9:Avaliaçâo", null, null, null,
                        "en10:evaluationpt9:avaliacao");
        FunctionalityCall x589 =
                makeFunctionalityCall(x585, x1312, "en6:Shiftspt6:Turnos", null, null, null, "en6:shiftspt6:turnos");
        FunctionalityCall x590 =
                makeFunctionalityCall(x585, x1311, "en9:Timesheetpt7:Horário", null, null, null, "en9:timesheetpt7:horario");
        FunctionalityCall x591 =
                makeFunctionalityCall(x585, x1310, "en12:Bibliographypt12:Bibliografia", null, null, null,
                        "en12:bibliographypt12:bibliografia");
        FunctionalityCall x592 =
                makeFunctionalityCall(x585, x1309, "en17:Evaluation Methodpt19:Método de Avaliação", null, null, null,
                        "en17:evaluation-methodpt19:metodo-de-avaliacao");
        FunctionalityCall x593 =
                makeFunctionalityCall(x585, x1308, "en7:Programpt8:Programa", null, null, null, "en7:programpt8:programa");
        FunctionalityCall x594 =
                makeFunctionalityCall(x585, x1307, "en10:Objectivespt10:Objectivos", null, null, null,
                        "en10:objectivespt10:objectivos");
        FunctionalityCall x595 =
                makeFunctionalityCall(x585, x1306, "en9:Summariespt8:Sumários", null, null, null, "en9:summariespt8:sumarios");
        FunctionalityCall x596 =
                makeFunctionalityCall(x585, x1305, "en8:Planningpt11:Planeamento", null, null, null,
                        "en8:planningpt11:planeamento");
        FunctionalityCall x597 =
                makeFunctionalityCall(x585, x1304, "en13:Announcementspt8:Anuncios", null, null, null,
                        "en13:announcementspt8:anuncios");
        FunctionalityCall x598 =
                makeFunctionalityCall(x585, x1300, "en12:Initial Pageen8:Homepagept14:Página Inicial", null, null, null,
                        "en12:initial-pageen8:homepagept14:pagina-inicial");
        FunctionalityCall x599 =
                makeFunctionalityCall(x585, x1317, "pt14:Resultados QUC", null, null, null, "pt14:resultados-quc");
        MetaDomainObjectPortal x602 = new MetaDomainObjectPortal(null);
        x3.addChild(x602);
        x602.setName(MultiLanguageString.importFromString("pt8:homepage"));
        x602.setNormalizedName(MultiLanguageString.importFromString("pt8:homepage"));
        x602.setPrefix("");
        FunctionalityCall x601 =
                makeFunctionalityCall(x602, x1319, "en12:Presentationpt12:Apresentação", null, null, null,
                        "en12:presentationpt12:apresentacao");
        MetaDomainObjectPortal x605 = new MetaDomainObjectPortal(null);
        x3.addChild(x605);
        x605.setName(MultiLanguageString.importFromString("pt6:cursos"));
        x605.setNormalizedName(MultiLanguageString.importFromString("pt6:cursos"));
        x605.setPrefix("");
        FunctionalityCall x604 =
                makeFunctionalityCall(x605, x1327, "en11:Descriptionpt9:Descrição", null, null, null,
                        "en11:descriptionpt9:descricao");
        FunctionalityCall x606 =
                makeFunctionalityCall(x605, x1330, "en22:Admission Requirementspt16:Regime de Acesso", null, null, null,
                        "en22:admission-requirementspt16:regime-de-acesso");
        FunctionalityCall x607 =
                makeFunctionalityCall(x605, x1331, "en19:Professional Statuspt21:Estatuto Profissional", null, null, null,
                        "en19:professional-statuspt21:estatuto-profissional");
        FunctionalityCall x608 =
                makeFunctionalityCall(x605, x1332, "en15:Curricular Planpt16:Plano Curricular", null, null, null,
                        "en15:curricular-planpt16:plano-curricular");
        FunctionalityCall x609 =
                makeFunctionalityCall(x605, x1333, "en16:Course Web Pagespt22:Páginas de Disciplinas", null, null, null,
                        "en16:course-web-pagespt22:paginas-de-disciplinas");
        FunctionalityCall x610 =
                makeFunctionalityCall(x605, x1334, "en15:Class timetablept18:Horários por Turma", null, null, null,
                        "en15:class-timetablept18:horarios-por-turma");
        FunctionalityCall x611 =
                makeFunctionalityCall(x605, x1335, "en11:Evaluationspt10:Avaliações", null, null, null,
                        "en11:evaluationspt10:avaliacoes");
        FunctionalityCall x612 =
                makeFunctionalityCall(x605, x1336, "en13:Dissertationspt12:Dissertações", null, null, null,
                        "en13:dissertationspt12:dissertacoes");
        MetaDomainObjectPortal x614 = new MetaDomainObjectPortal(null);
        x3.addChild(x614);
        x614.setName(MultiLanguageString.importFromString("en11:departmentspt13:departamentos"));
        x614.setNormalizedName(MultiLanguageString.importFromString("en11:departmentspt13:departamentos"));
        x614.setPrefix("");
        MetaDomainObjectPortal x616 = new MetaDomainObjectPortal(null);
        x3.addChild(x616);
        x616.setName(MultiLanguageString.importFromString("en8:researchpt12:Investigação"));
        x616.setNormalizedName(MultiLanguageString.importFromString("en8:researchpt12:investigacao"));
        x616.setPrefix("");
        FunctionalityCall x615 = makeFunctionalityCall(x616, x1358, "en4:Homept6:Início", null, null, null, "en4:homept6:inicio");
        MetaDomainObjectPortal x619 = new MetaDomainObjectPortal(null);
        x3.addChild(x619);
        x619.setName(MultiLanguageString.importFromString("pt18:conselhoCientifico"));
        x619.setNormalizedName(MultiLanguageString.importFromString("pt18:conselhocientifico"));
        x619.setPrefix("");
        FunctionalityCall x618 = makeFunctionalityCall(x619, x1366, "pt6:Início", null, null, null, "pt6:inicio");
        MetaDomainObjectPortal x622 = new MetaDomainObjectPortal(null);
        x3.addChild(x622);
        x622.setName(MultiLanguageString.importFromString("pt18:conselhoPedagogico"));
        x622.setNormalizedName(MultiLanguageString.importFromString("pt18:conselhopedagogico"));
        x622.setPrefix("");
        FunctionalityCall x621 = makeFunctionalityCall(x622, x1362, "pt6:Início", null, null, null, "pt6:inicio");
        MetaDomainObjectPortal x625 = new MetaDomainObjectPortal(null);
        x3.addChild(x625);
        x625.setName(MultiLanguageString.importFromString("pt14:areaCientifica"));
        x625.setNormalizedName(MultiLanguageString.importFromString("pt14:areacientifica"));
        x625.setPrefix("");
        FunctionalityCall x624 = makeFunctionalityCall(x625, x1377, "en4:Homept6:Início", null, null, null, "en4:homept6:inicio");
        Section x628 = new Section(x3, MultiLanguageString.importFromString("pt35:Gabinete de Relaçôes Internacionais"));
        x628.setName(MultiLanguageString.importFromString("pt35:Gabinete de Relaçôes Internacionais"));
        x628.setNormalizedName(MultiLanguageString.importFromString("pt35:gabinete-de-relacoes-internacionais"));
        FunctionalityCall x627 = makeFunctionalityCall(x628, x1516, "pt6:Inicio", null, null, null, "pt6:inicio");
        Section x630 = new Section(x628, MultiLanguageString.importFromString("pt23:Estágios Internacionais"));
        x630.setName(MultiLanguageString.importFromString("pt23:Estágios Internacionais"));
        x630.setNormalizedName(MultiLanguageString.importFromString("pt23:estagios-internacionais"));
        FunctionalityCall x629 = makeFunctionalityCall(x630, x1518, "pt10:Candidatos", null, null, null, "pt10:candidatos");
        Section x633 = new Section(x628, MultiLanguageString.importFromString("pt9:Consultas"));
        x633.setName(MultiLanguageString.importFromString("pt9:Consultas"));
        x633.setNormalizedName(MultiLanguageString.importFromString("pt9:consultas"));
        FunctionalityCall x632 =
                makeFunctionalityCall(x633, x1521, "pt17:Visualizar Alunos", null, null, null, "pt17:visualizar-alunos");
        FunctionalityCall x634 = makeFunctionalityCall(x633, x1529, "pt12:Tabelas ECTS", null, null, null, "pt12:tabelas-ects");
        Section x637 = new Section(x628, MultiLanguageString.importFromString("pt9:Listagens"));
        x637.setName(MultiLanguageString.importFromString("pt9:Listagens"));
        x637.setNormalizedName(MultiLanguageString.importFromString("pt9:listagens"));
        FunctionalityCall x636 =
                makeFunctionalityCall(x637, x1522, "pt16:Alunos Por Curso", null, null, null, "pt16:alunos-por-curso");
        FunctionalityCall x638 =
                makeFunctionalityCall(x637, x1524, "pt21:Alunos por Disciplina", null, null, null, "pt21:alunos-por-disciplina");
        Section x641 = new Section(x628, MultiLanguageString.importFromString("pt7:Erasmus"));
        x641.setName(MultiLanguageString.importFromString("pt7:Erasmus"));
        x641.setNormalizedName(MultiLanguageString.importFromString("pt7:erasmus"));
        FunctionalityCall x640 = makeFunctionalityCall(x641, x1526, "pt7:Erasmus", null, null, null, "pt7:erasmus");
        ExpressionGroupAvailability x1726 = new ExpressionGroupAvailability(x628, "role(INTERNATIONAL_RELATION_OFFICE)");
        x1726.setTargetGroup(Group.fromString("role(INTERNATIONAL_RELATION_OFFICE)"));
        x628.setAvailabilityPolicy(x1726);
        MetaDomainObjectPortal x645 = new MetaDomainObjectPortal(null);
        x3.addChild(x645);
        x645.setName(MultiLanguageString.importFromString("pt8:tutorado"));
        x645.setNormalizedName(MultiLanguageString.importFromString("pt8:tutorado"));
        FunctionalityCall x644 = makeFunctionalityCall(x645, x1379, "pt6:Início", null, null, null, "pt6:inicio");
        Section x648 = new Section(x3, MultiLanguageString.importFromString("pt18:Conteudos Publicos"));
        x648.setName(MultiLanguageString.importFromString("pt18:Conteudos Publicos"));
        x648.setNormalizedName(MultiLanguageString.importFromString("pt18:conteudos-publicos"));
        FunctionalityCall x647 =
                makeFunctionalityCall(x648, x1398, "pt19:Pesquisa de Espaços", null, null, null, "pt19:pesquisa-de-espacos");
        FunctionalityCall x649 =
                makeFunctionalityCall(x648, x1399, "pt40:Consulta de Corpo Docente por Disciplina", null, null, null,
                        "pt40:consulta-de-corpo-docente-por-disciplina");
        FunctionalityCall x650 =
                makeFunctionalityCall(x648, x1400, "pt14:Registo Alumni", null, null, null, "pt14:registo-alumni");
        FunctionalityCall x651 =
                makeFunctionalityCall(x648, x1401, "pt24:Conclusão Registo Alumni", null, null, null,
                        "pt24:conclusao-registo-alumni");
        FunctionalityCall x652 =
                makeFunctionalityCall(x648, x1405, "pt26:Pesquisa de Resultados QUC", null, null, null,
                        "pt26:pesquisa-de-resultados-quc");
        FunctionalityCall x653 =
                makeFunctionalityCall(x648, x1406, "en5:filespt9:ficheiros", null, null, null, "en5:filespt9:ficheiros");
        MetaDomainObjectPortal x656 = new MetaDomainObjectPortal(null);
        x3.addChild(x656);
        x656.setName(MultiLanguageString.importFromString("pt9:Alunos CD"));
        x656.setNormalizedName(MultiLanguageString.importFromString("pt9:alunos-cd"));
        FunctionalityCall x655 =
                makeFunctionalityCall(x656, x1382, "en8:Homepagept14:Página Inicial", null, null, null,
                        "en8:homepagept14:pagina-inicial");
        Section x659 = new Section(x3, MultiLanguageString.importFromString("pt24:Cartões de Identificação"));
        x659.setName(MultiLanguageString.importFromString("pt24:Cartões de Identificação"));
        x659.setNormalizedName(MultiLanguageString.importFromString("pt24:cartoes-de-identificacao"));
        FunctionalityCall x658 =
                makeFunctionalityCall(x659, x1531, "en8:Homepagept14:Página Inicial", null, null, null,
                        "en8:homepagept14:pagina-inicial");
        FunctionalityCall x660 = makeFunctionalityCall(x659, x1533, "pt9:Pesquisar", null, null, null, "pt9:pesquisar");
        ExpressionGroupAvailability x1727 = new ExpressionGroupAvailability(x659, "role(IDENTIFICATION_CARD_MANAGER)");
        x1727.setTargetGroup(Group.fromString("role(IDENTIFICATION_CARD_MANAGER)"));
        x659.setAvailabilityPolicy(x1727);
        Section x663 =
                new Section(x3,
                        MultiLanguageString.importFromString("en23:Public Relations Officept29:Gabinete de Relações Públicas"));
        x663.setName(MultiLanguageString.importFromString("en23:Public Relations Officept29:Gabinete de Relações Públicas"));
        x663.setNormalizedName(MultiLanguageString
                .importFromString("en23:public-relations-officept29:gabinete-de-relacoes-publicas"));
        x663.setMaximizable(false);
        FunctionalityCall x662 =
                makeFunctionalityCall(x663, x1539, "pt19:Listagens de Alunos", null, null, null, "pt19:listagens-de-alunos");
        FunctionalityCall x664 = makeFunctionalityCall(x663, x1540, "pt13:Gerir Membros", null, null, null, "pt13:gerir-membros");
        ExpressionGroupAvailability x1728 = new ExpressionGroupAvailability(x664, "role(MANAGER)");
        x1728.setTargetGroup(Group.fromString("role(MANAGER)"));
        x664.setAvailabilityPolicy(x1728);
        FunctionalityCall x665 = makeFunctionalityCall(x663, x1541, "pt10:Inquéritos", null, null, null, "pt10:inqueritos");
        Section x666 = new Section(x663, MultiLanguageString.importFromString("pt6:Alumni"));
        x666.setName(MultiLanguageString.importFromString("pt6:Alumni"));
        x666.setNormalizedName(MultiLanguageString.importFromString("pt6:alumni"));
        // A previous node
        x666.addChild(x1542);
        // A previous node
        x666.addChild(x1544);
        // A previous node
        x663.addChild(x1535);
        // A previous node
        x663.addChild(x1537);
        // A previous node
        x663.addChild(x1538);
        ExpressionGroupAvailability x1729 = new ExpressionGroupAvailability(x663, "role(PUBLIC_RELATIONS_OFFICE)");
        x1729.setTargetGroup(Group.fromString("role(PUBLIC_RELATIONS_OFFICE)"));
        x663.setAvailabilityPolicy(x1729);
        Section x669 = new Section(x3, MultiLanguageString.importFromString("en18:Housing Managementpt21:Núcleo de Alojamentos"));
        x669.setName(MultiLanguageString.importFromString("en18:Housing Managementpt21:Núcleo de Alojamentos"));
        x669.setNormalizedName(MultiLanguageString.importFromString("en18:housing-managementpt21:nucleo-de-alojamentos"));
        x669.setMaximizable(false);
        FunctionalityCall x668 =
                makeFunctionalityCall(x669, x1547, "en8:Homepagept14:Página Inicial", null, null, null,
                        "en8:homepagept14:pagina-inicial");
        FunctionalityCall x670 = makeFunctionalityCall(x669, x1549, "pt13:Gerir Dúvidas", null, null, null, "pt13:gerir-dividas");
        FunctionalityCall x671 =
                makeFunctionalityCall(x669, x1550, "pt21:Gestão de Previlégios", null, null, null, "pt21:gestao-de-previlegios");
        ExpressionGroupAvailability x1730 = new ExpressionGroupAvailability(x669, "role(RESIDENCE_MANAGER)");
        x1730.setTargetGroup(Group.fromString("role(RESIDENCE_MANAGER)"));
        x669.setAvailabilityPolicy(x1730);
        MetaDomainObjectPortal x674 = new MetaDomainObjectPortal(null);
        x3.addChild(x674);
        x674.setName(MultiLanguageString.importFromString("en6:Thesespt12:Dissertacoes"));
        x674.setNormalizedName(MultiLanguageString.importFromString("en6:thesespt12:dissertacoes"));
        x674.setPrefix("");
        FunctionalityCall x673 =
                makeFunctionalityCall(x674, x1385, "en6:Thesispt11:Dissertacao", null, null, null, "en6:thesispt11:dissertacao");
        Section x677 = new Section(x3, MultiLanguageString.importFromString("pt8:Estagios"));
        x677.setName(MultiLanguageString.importFromString("pt8:Estagios"));
        x677.setNormalizedName(MultiLanguageString.importFromString("pt8:estagios"));
        x677.setMaximizable(false);
        FunctionalityCall x676 = makeFunctionalityCall(x677, x1402, "pt12:Candidaturas", null, null, null, "pt12:candidaturas");
        Section x680 = new Section(x3, MultiLanguageString.importFromString("pt12:Projectos IT"));
        x680.setName(MultiLanguageString.importFromString("pt12:Projectos IT"));
        x680.setNormalizedName(MultiLanguageString.importFromString("pt12:projectos-it"));
        FunctionalityCall x679 = makeFunctionalityCall(x680, x1552, "pt12:Projectos IT", null, null, null, "pt12:projectos-it");
        ExpressionGroupAvailability x1731 = new ExpressionGroupAvailability(x679, "role(IT_PROJECTS_MANAGER)");
        x1731.setTargetGroup(Group.fromString("role(IT_PROJECTS_MANAGER)"));
        x679.setAvailabilityPolicy(x1731);
        MetaDomainObjectPortal x683 = new MetaDomainObjectPortal(null);
        x3.addChild(x683);
        x683.setName(MultiLanguageString.importFromString("pt12:aestatutaria"));
        x683.setNormalizedName(MultiLanguageString.importFromString("pt12:aestatutaria"));
        x683.setPrefix("");
        FunctionalityCall x682 = makeFunctionalityCall(x683, x1388, "pt6:Início", null, null, null, "pt6:inicio");
        Section x686 =
                new Section(x3, MultiLanguageString.importFromString("es12:applicationsen11:Candidaciespt12:Candidaturas"));
        x686.setName(MultiLanguageString.importFromString("es12:applicationsen11:Candidaciespt12:Candidaturas"));
        x686.setNormalizedName(MultiLanguageString.importFromString("es12:applicationsen11:candidaciespt12:candidaturas"));
        x686.setMaximizable(false);
        FunctionalityCall x685 = makeFunctionalityCall(x686, x1407, "pt10:introducao", null, null, null, "pt10:introducao");
        FunctionalityCall x687 = makeFunctionalityCall(x686, x1409, "pt13:licenciaturas", null, null, null, "pt13:licenciaturas");
        FunctionalityCall x688 = makeFunctionalityCall(x686, x1410, "pt9:mestrados", null, null, null, "pt9:mestrados");
        Section x690 = new Section(x686, MultiLanguageString.importFromString("pt3:lic"));
        x690.setName(MultiLanguageString.importFromString("pt3:lic"));
        x690.setNormalizedName(MultiLanguageString.importFromString("pt3:lic"));
        FunctionalityCall x689 =
                makeFunctionalityCall(x690, x1411, "pt14:mudancas_curso", null, null, null, "pt14:mudancas_curso");
        FunctionalityCall x691 = makeFunctionalityCall(x690, x1412, "pt8:transfer", null, null, null, "pt8:transfer");
        FunctionalityCall x692 =
                makeFunctionalityCall(x690, x1413, "pt24:cursos_medios_superiores", null, null, null,
                        "pt24:cursos_medios_superiores");
        FunctionalityCall x693 =
                makeFunctionalityCall(x690, x1414, "pt18:maiores_vinte_tres", null, null, null, "pt18:maiores_vinte_tres");
        FunctionalityCall x694 =
                makeFunctionalityCall(x690, x1415, "pt24:concurso_nacional_acesso", null, null, null,
                        "pt24:concurso_nacional_acesso");
        Section x696 = new Section(x690, MultiLanguageString.importFromString("pt15:vinte_tres_anos"));
        x696.setName(MultiLanguageString.importFromString("pt15:vinte_tres_anos"));
        x696.setNormalizedName(MultiLanguageString.importFromString("pt15:vinte_tres_anos"));
        FunctionalityCall x695 = makeFunctionalityCall(x696, x1416, "pt6:acesso", null, null, null, "pt6:acesso");
        FunctionalityCall x697 = makeFunctionalityCall(x696, x1418, "pt9:submissao", null, null, null, "pt9:submissao");
        FunctionalityCall x698 = makeFunctionalityCall(x696, x1419, "pt10:preregisto", null, null, null, "pt10:preregisto");
        Section x701 = new Section(x690, MultiLanguageString.importFromString("pt13:mudanca_curso"));
        x701.setName(MultiLanguageString.importFromString("pt13:mudanca_curso"));
        x701.setNormalizedName(MultiLanguageString.importFromString("pt13:mudanca_curso"));
        FunctionalityCall x700 = makeFunctionalityCall(x701, x1433, "pt10:preregisto", null, null, null, "pt10:preregisto");
        FunctionalityCall x702 = makeFunctionalityCall(x701, x1435, "pt6:acesso", null, null, null, "pt6:acesso");
        FunctionalityCall x703 = makeFunctionalityCall(x701, x1436, "pt9:submissao", null, null, null, "pt9:submissao");
        Section x706 = new Section(x690, MultiLanguageString.importFromString("pt13:transferencia"));
        x706.setName(MultiLanguageString.importFromString("pt13:transferencia"));
        x706.setNormalizedName(MultiLanguageString.importFromString("pt13:transferencia"));
        FunctionalityCall x705 = makeFunctionalityCall(x706, x1443, "pt10:preregisto", null, null, null, "pt10:preregisto");
        FunctionalityCall x707 = makeFunctionalityCall(x706, x1445, "pt6:acesso", null, null, null, "pt6:acesso");
        FunctionalityCall x708 = makeFunctionalityCall(x706, x1446, "pt9:submissao", null, null, null, "pt9:submissao");
        Section x711 = new Section(x690, MultiLanguageString.importFromString("pt24:cursos_medios_superiores"));
        x711.setName(MultiLanguageString.importFromString("pt24:cursos_medios_superiores"));
        x711.setNormalizedName(MultiLanguageString.importFromString("pt24:cursos_medios_superiores"));
        FunctionalityCall x710 = makeFunctionalityCall(x711, x1438, "pt10:preregisto", null, null, null, "pt10:preregisto");
        FunctionalityCall x712 = makeFunctionalityCall(x711, x1440, "pt6:acesso", null, null, null, "pt6:acesso");
        FunctionalityCall x713 = makeFunctionalityCall(x711, x1441, "pt9:submissao", null, null, null, "pt9:submissao");
        Section x717 = new Section(x686, MultiLanguageString.importFromString("en12:second_cyclept13:segundo_ciclo"));
        x717.setName(MultiLanguageString.importFromString("en12:second_cyclept13:segundo_ciclo"));
        x717.setNormalizedName(MultiLanguageString.importFromString("en12:second_cyclept13:segundo_ciclo"));
        x717.setMaximizable(false);
        FunctionalityCall x716 = makeFunctionalityCall(x717, x1421, "pt6:acesso", null, null, null, "en6:accesspt6:acesso");
        FunctionalityCall x718 =
                makeFunctionalityCall(x717, x1423, "pt9:submissao", null, null, null, "en10:submissionpt9:submissao");
        FunctionalityCall x719 =
                makeFunctionalityCall(x717, x1424, "pt10:preregisto", null, null, null, "en15:preregistrationpt10:preregisto");
        FunctionalityCall x720 = makeFunctionalityCall(x717, x1425, "pt11:recuperacao", null, null, null, "pt11:recuperacao");
        Section x723 = new Section(x686, MultiLanguageString.importFromString("pt7:erasmus"));
        x723.setName(MultiLanguageString.importFromString("pt7:erasmus"));
        x723.setNormalizedName(MultiLanguageString.importFromString("pt7:erasmus"));
        FunctionalityCall x722 = makeFunctionalityCall(x723, x1448, "pt5:intro", null, null, null, "pt5:intro");
        FunctionalityCall x724 =
                makeFunctionalityCall(x723, x1450, "pt15:submission-type", null, null, null, "pt15:submission-type");
        FunctionalityCall x725 =
                makeFunctionalityCall(x723, x1454, "pt14:returnFromPeps", null, null, null, "pt14:returnfrompeps");
        FunctionalityCall x726 =
                makeFunctionalityCall(x723, x1455, "pt22:nationalCardSubmission", null, null, null, "pt22:nationalcardsubmission");
        FunctionalityCall x727 = makeFunctionalityCall(x723, x1452, "pt10:submission", null, null, null, "pt10:submission");
        FunctionalityCall x728 = makeFunctionalityCall(x723, x1453, "pt6:access", null, null, null, "pt6:access");
        FunctionalityCall x729 =
                makeFunctionalityCall(x723, x1451, "pt15:preregistration", null, null, null, "pt15:preregistration");
        FunctionalityCall x730 =
                makeFunctionalityCall(x723, x1456, "pt29:nationalCardApplicationAccess", null, null, null,
                        "pt29:nationalcardapplicationaccess");
        FunctionalityCall x731 =
                makeFunctionalityCall(x723, x1457, "pt14:accessRecovery", null, null, null, "pt14:accessrecovery");
        FunctionalityCall x732 = makeFunctionalityCall(x723, x1459, "en9:testStork", null, null, null, "en9:teststork");
        Section x735 = new Section(x686, MultiLanguageString.importFromString("pt3:phd"));
        x735.setName(MultiLanguageString.importFromString("pt3:phd"));
        x735.setNormalizedName(MultiLanguageString.importFromString("pt3:phd"));
        x735.setVisible(false);
        FunctionalityCall x734 =
                makeFunctionalityCall(x735, x1461, "en14:identificationpt13:identificacao", null, null, null,
                        "pt13:identificacao");
        FunctionalityCall x736 =
                makeFunctionalityCall(x735, x1463, "en10:submissionpt9:submissao", null, null, null,
                        "en10:submissionpt9:submissao");
        FunctionalityCall x737 =
                makeFunctionalityCall(x735, x1464, "en6:accesspt6:acesso", null, null, null, "en6:accesspt6:acesso");
        FunctionalityCall x738 =
                makeFunctionalityCall(x735, x1465, "en8:recoverypt11:recuperacao", null, null, null,
                        "en8:recoverypt11:recuperacao");
        FunctionalityCall x739 =
                makeFunctionalityCall(x735, x1466, "pt12:recomendacao", null, null, null, "en12:referee-formpt12:recomendacao");
        FunctionalityCall x740 =
                makeFunctionalityCall(x735, x1467, "pt19:validateApplication", null, null, null, "pt19:validateapplication");
        FunctionalityCall x741 =
                makeFunctionalityCall(x735, x1468, "en14:identification", null, null, null,
                        "en14:identificationpt14:identification");
        Section x744 = new Section(x686, MultiLanguageString.importFromString("pt4:epfl"));
        x744.setName(MultiLanguageString.importFromString("pt4:epfl"));
        x744.setNormalizedName(MultiLanguageString.importFromString("pt4:epfl"));
        FunctionalityCall x743 =
                makeFunctionalityCall(x744, x1470, "en14:identificationpt13:identificacao", null, null, null,
                        "en14:identificationpt13:identificacao");
        FunctionalityCall x745 =
                makeFunctionalityCall(x744, x1472, "en10:submissionpt9:submissao", null, null, null,
                        "en10:submissionpt9:submissao");
        FunctionalityCall x746 =
                makeFunctionalityCall(x744, x1473, "en6:accesspt6:acesso", null, null, null, "en6:accesspt6:acesso");
        FunctionalityCall x747 = makeFunctionalityCall(x744, x1474, "pt12:referee-form", null, null, null, "pt12:referee-form");
        FunctionalityCall x748 =
                makeFunctionalityCall(x744, x1475, "en8:recoverypt11:recuperacao", null, null, null,
                        "en8:recoverypt11:recuperacao");
        MetaDomainObjectPortal x752 = new MetaDomainObjectPortal(null);
        x3.addChild(x752);
        x752.setName(MultiLanguageString.importFromString("pt2:cg"));
        x752.setNormalizedName(MultiLanguageString.importFromString("pt2:cg"));
        x752.setPrefix("");
        FunctionalityCall x751 = makeFunctionalityCall(x752, x1391, "en4:Homept6:Início", null, null, null, "en4:homept6:inicio");
        MetaDomainObjectPortal x755 = new MetaDomainObjectPortal(null);
        x3.addChild(x755);
        x755.setName(MultiLanguageString.importFromString("pt5:units"));
        x755.setNormalizedName(MultiLanguageString.importFromString("pt5:units"));
        x755.setPrefix("");
        FunctionalityCall x754 = makeFunctionalityCall(x755, x1394, "en4:Homept6:Início", null, null, null, "en4:homept6:inicio");
        Section x761 = new Section(x3, MultiLanguageString.importFromString("pt18:Supervisão Externa"));
        x761.setName(MultiLanguageString.importFromString("pt18:Supervisão Externa"));
        x761.setNormalizedName(MultiLanguageString.importFromString("pt18:supervisao-externa"));
        x761.setMaximizable(false);
        FunctionalityCall x760 =
                makeFunctionalityCall(x761, x1555, "en8:Homepagept14:Página Inicial", null, null, null,
                        "en8:homepagept14:pagina-inicial");
        Section x763 = new Section(x761, MultiLanguageString.importFromString("pt9:Consultar"));
        x763.setName(MultiLanguageString.importFromString("pt9:Consultar"));
        x763.setNormalizedName(MultiLanguageString.importFromString("pt9:consultar"));
        FunctionalityCall x762 = makeFunctionalityCall(x763, x1557, "pt5:Aluno", null, null, null, "pt5:aluno");
        FunctionalityCall x764 = makeFunctionalityCall(x763, x1559, "pt5:Curso", null, null, null, "pt5:curso");
        FunctionalityCall x765 = makeFunctionalityCall(x763, x1560, "pt11:Ano Lectivo", null, null, null, "pt11:ano-lectivo");
        ExpressionGroupAvailability x1733 = new ExpressionGroupAvailability(x761, "role(EXTERNAL_SUPERVISOR)");
        x1733.setTargetGroup(Group.fromString("role(EXTERNAL_SUPERVISOR)"));
        x761.setAvailabilityPolicy(x1733);
        Section x769 = new Section(x3, MultiLanguageString.importFromString("en11:Phd Programpt17:Programa Doutoral"));
        x769.setName(MultiLanguageString.importFromString("en11:Phd Programpt17:Programa Doutoral"));
        x769.setNormalizedName(MultiLanguageString.importFromString("en11:phd-programpt17:programa-doutoral"));
        x769.setMaximizable(false);
        FunctionalityCall x768 =
                makeFunctionalityCall(x769, x1478, "en6:Accesspt6:Acesso", null, null, null, "en6:accesspt6:acesso");
        Section x773 = new Section(x3, MultiLanguageString.importFromString("pt28:Nucleo de Apoio ao Estudante"));
        x773.setName(MultiLanguageString.importFromString("pt28:Nucleo de Apoio ao Estudante"));
        x773.setNormalizedName(MultiLanguageString.importFromString("pt28:nucleo-de-apoio-ao-estudante"));
        FunctionalityCall x772 = makeFunctionalityCall(x773, x1563, "pt6:Inicio", null, null, null, "pt6:inicio");
        Section x775 = new Section(x773, MultiLanguageString.importFromString("pt12:Candidaturas"));
        x775.setName(MultiLanguageString.importFromString("pt12:Candidaturas"));
        x775.setNormalizedName(MultiLanguageString.importFromString("pt12:candidaturas"));
        FunctionalityCall x774 = makeFunctionalityCall(x775, x1565, "pt13:Maiores de 23", null, null, null, "pt13:maiores-de-23");
        FunctionalityCall x776 = makeFunctionalityCall(x775, x1567, "pt13:Segundo Ciclo", null, null, null, "pt13:segundo-ciclo");
        FunctionalityCall x777 =
                makeFunctionalityCall(x775, x1568, "pt14:Transferências", null, null, null, "pt14:transferencias");
        FunctionalityCall x778 =
                makeFunctionalityCall(x775, x1569, "pt17:Mudanças de curso", null, null, null, "pt17:mudancas-de-curso");
        FunctionalityCall x779 =
                makeFunctionalityCall(x775, x1570, "pt45:Titulares de cursos de grau médio ou superior", null, null, null,
                        "pt45:titulares-de-cursos-de-grau-medio-ou-superior");
        Section x782 = new Section(x773, MultiLanguageString.importFromString("pt10:Matriculas"));
        x782.setName(MultiLanguageString.importFromString("pt10:Matriculas"));
        x782.setNormalizedName(MultiLanguageString.importFromString("pt10:matriculas"));
        x782.setMaximizable(false);
        FunctionalityCall x781 =
                makeFunctionalityCall(x782, x1572, "pt33:Alunos Matriculados 1ª ano 1ª vez", null, null, null,
                        "pt31:alunos-matriculados-1-ano-1-vez");
        ExpressionGroupAvailability x1734 = new ExpressionGroupAvailability(x773, "role(NAPE)");
        x1734.setTargetGroup(Group.fromString("role(NAPE)"));
        x773.setAvailabilityPolicy(x1734);
        Section x790 = new Section(x3, MultiLanguageString.importFromString("pt3:phd"));
        x790.setName(MultiLanguageString.importFromString("pt3:phd"));
        x790.setNormalizedName(MultiLanguageString.importFromString("pt3:phd"));
        x790.setVisible(false);
        Section x789 = new Section(x790, MultiLanguageString.importFromString("pt4:epfl"));
        x789.setName(MultiLanguageString.importFromString("pt4:epfl"));
        x789.setNormalizedName(MultiLanguageString.importFromString("pt4:epfl"));
        Section x788 = new Section(x789, MultiLanguageString.importFromString("pt12:applications"));
        x788.setName(MultiLanguageString.importFromString("pt12:applications"));
        x788.setNormalizedName(MultiLanguageString.importFromString("pt12:applications"));
        FunctionalityCall x787 = makeFunctionalityCall(x788, x1576, "pt4:list", null, null, null, "pt4:list");
        FunctionalityCall x791 = makeFunctionalityCall(x788, x1578, "pt4:show", null, null, null, "pt4:show");
        FunctionalityCall x792 = makeFunctionalityCall(x788, x1579, "pt5:login", null, null, null, "pt5:login");
        FunctionalityCall x793 = makeFunctionalityCall(x788, x1580, "pt13:notAuthorized", null, null, null, "pt13:notauthorized");
        FunctionalityCall x794 = makeFunctionalityCall(x788, x1581, "pt7:referee", null, null, null, "pt7:referee");
        FunctionalityCall x795 =
                makeFunctionalityCall(x788, x1582, "pt18:candidateDocuments", null, null, null, "pt18:candidatedocuments");
        FunctionalityCall x796 = makeFunctionalityCall(x788, x1583, "pt5:photo", null, null, null, "pt5:photo");
        FunctionalityCall x797 = makeFunctionalityCall(x788, x1584, "pt3:xml", null, null, null, "pt3:xml");
        Section x802 = new Section(x3, MultiLanguageString.importFromString("en9:Rectoratept8:Reitoria"));
        x802.setName(MultiLanguageString.importFromString("en9:Rectoratept8:Reitoria"));
        x802.setNormalizedName(MultiLanguageString.importFromString("en9:rectoratept8:reitoria"));
        x802.setMaximizable(true);
        FunctionalityCall x801 =
                makeFunctionalityCall(x802, x1586, "pt15:Lotes Recebidos", null, null, null, "pt15:lotes-recebidos");
        ExpressionGroupAvailability x1735 = new ExpressionGroupAvailability(x802, "role(RECTORATE)");
        x1735.setTargetGroup(Group.fromString("role(RECTORATE)"));
        x802.setAvailabilityPolicy(x1735);
        Section x805 = new Section(x3, MultiLanguageString.importFromString("pt16:Micro-Pagamentos"));
        x805.setName(MultiLanguageString.importFromString("pt16:Micro-Pagamentos"));
        x805.setNormalizedName(MultiLanguageString.importFromString("pt16:micro-pagamentos"));
        x805.setMaximizable(false);
        FunctionalityCall x804 = makeFunctionalityCall(x805, x1589, "pt6:Início", null, null, null, "pt6:inicio");
        ExpressionGroupAvailability x1736 = new ExpressionGroupAvailability(x805, "role(TREASURY)");
        x1736.setTargetGroup(Group.fromString("role(TREASURY)"));
        x805.setAvailabilityPolicy(x1736);
        ExpressionGroupAvailability x1741 = new ExpressionGroupAvailability(x3, "everyone()");
        x1741.setTargetGroup(Group.fromString("everyone()"));
        x3.setAvailabilityPolicy(x1741);

        x3.setName(new MultiLanguageString(Language.pt, "FenixEdu").append(new MultiLanguageString(Language.en, "FenixEdu")));
        x3.setRootDomainObject(bennu);
        bennu.setRootPortal(x3);
        bennu.setRootModule(x823);

        makePortals();
    }

    private static FunctionalityCall makeFunctionalityCall(Container parent, Functionality func, String name, String fcTitle,
            String fcBody, String fcDescription, String fcNName) {
        // print "%s%s %s = new %s(%s);" % ("   " * it,  className,nodeName,className, dbOid[node['OID_FUNCTIONALITY']]['__var'])

        FunctionalityCall fc = new FunctionalityCall(func);
        parent.addChild(fc);
        if (name != null) {
            fc.setName(MultiLanguageString.importFromString(name));
        }

        if (fcTitle != null) {
            fc.setTitle(MultiLanguageString.importFromString(fcTitle));
        }

        if (fcBody != null) {
            fc.setBody(MultiLanguageString.importFromString(fcBody));
        }

        if (fcDescription != null) {
            fc.setDescription(MultiLanguageString.importFromString(fcDescription));
        }

        if (fcNName != null) {
            fc.setNormalizedName(MultiLanguageString.importFromString(fcNName));
        }

        return fc;
    }

    private static final String CREATE_REGISTRATION = new AcademicAuthorizationGroup(AcademicOperationType.CREATE_REGISTRATION)
            .getExpression();

    private static final String SERVICE_REQUESTS = new AcademicAuthorizationGroup(AcademicOperationType.SERVICE_REQUESTS)
            .getExpression();

    private static final String SERVICE_REQUESTS_RECTORAL_SENDING = new AcademicAuthorizationGroup(
            AcademicOperationType.SERVICE_REQUESTS_RECTORAL_SENDING).getExpression();

    private static final String MANAGE_AUTHORIZATIONS = new AcademicAuthorizationGroup(
            AcademicOperationType.MANAGE_AUTHORIZATIONS).getExpression();

    private static final String STUDENT_LISTINGS = new AcademicAuthorizationGroup(AcademicOperationType.STUDENT_LISTINGS)
            .getExpression();

    private static final String MANAGE_EXECUTION_COURSES = new AcademicAuthorizationGroup(
            AcademicOperationType.MANAGE_EXECUTION_COURSES).getExpression();

    private static final String MANAGE_EXECUTION_COURSES_ADV = new AcademicAuthorizationGroup(
            AcademicOperationType.MANAGE_EXECUTION_COURSES_ADV).getExpression();

    private static final String VIEW_SCHEDULING_OVERSIGHT = new AcademicAuthorizationGroup(
            AcademicOperationType.VIEW_SCHEDULING_OVERSIGHT).getExpression();

    private static final String MANAGE_ENROLMENT_PERIODS = new AcademicAuthorizationGroup(
            AcademicOperationType.MANAGE_ENROLMENT_PERIODS).getExpression();

    private static final String MANAGE_DEGREE_CURRICULAR_PLANS = new AcademicAuthorizationGroup(
            AcademicOperationType.MANAGE_DEGREE_CURRICULAR_PLANS).getExpression();

    private static final String MANAGE_PRICES = new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_PRICES)
            .getExpression();

    private static final String MANAGE_EXTRA_CURRICULAR_ACTIVITIES = new AcademicAuthorizationGroup(
            AcademicOperationType.MANAGE_EXTRA_CURRICULAR_ACTIVITIES).getExpression();

    private static final String MANAGE_EXTERNAL_UNITS = new AcademicAuthorizationGroup(
            AcademicOperationType.MANAGE_EXTERNAL_UNITS).getExpression();

    private static final String MANAGE_CANDIDACY_PROCESSES = new AcademicAuthorizationGroup(
            AcademicOperationType.MANAGE_CANDIDACY_PROCESSES).getExpression()
            + " || "
            + new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_INDIVIDUAL_CANDIDACIES).getExpression();

    private static final String MARKSHEET_MANAGEMENT = new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_MARKSHEETS)
            .getExpression();

    private static final String MANAGE_CONTRIBUTORS = new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_CONTRIBUTORS)
            .getExpression();

    private static final String MANAGE_DOCUMENTS = new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_DOCUMENTS)
            .getExpression();

    private static final String MANAGE_PHD_ENROLMENT_PERIODS = new AcademicAuthorizationGroup(
            AcademicOperationType.MANAGE_PHD_ENROLMENT_PERIODS).getExpression();

    private static final String MANAGE_PHD_PROCESSES = new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_PHD_PROCESSES)
            .getExpression();

    private static final String MANAGE_PHDS = MANAGE_PHD_ENROLMENT_PERIODS + " || " + MANAGE_PHD_PROCESSES;

    private static final String MANAGE_STUDENTS = "academic('OFFICE')";

    private static final String REPORT_STUDENTS_UTL_CANDIDATES = new AcademicAuthorizationGroup(
            AcademicOperationType.REPORT_STUDENTS_UTL_CANDIDATES).getExpression();

    private static final String MANAGE_REGISTERED_DEGREE_CANDIDACIES = new AcademicAuthorizationGroup(
            AcademicOperationType.MANAGE_REGISTERED_DEGREE_CANDIDACIES).getExpression();

    private static final String MANAGE_EVENT_REPORTS = new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_EVENT_REPORTS)
            .getExpression();

    private static void makePortals() {
        Module root = Bennu.getInstance().getRootModule();
        Portal portal = Bennu.getInstance().getRootPortal();

        fillAdministration(root, portal);
    }

    private static void fillAdministration(Module parent, Container parentSection) {
        Module administrationModule = makeModule(parent, "Administração Académica", "/academicAdministration");

        Section administrationSection = makeSection(parentSection, "Administração Académica", "academic('ADMINISTRATION')");

        Module officeModule = makeModule(parent, "Secretaria Académica", "/academicAdministration");

        Section officeSection = makeSection(parentSection, "Secretaria Académica", "academic('OFFICE')");

        makeFunctionality(administrationModule, administrationSection, "Administração Académica",
                "/academicAdministration.do?method=indexAdmin", "everyone");

        makeFunctionality(officeModule, officeSection, "Secretaria Académica", "/academicAdministration.do?method=indexOffice",
                "everyone");

        fillAuthorizations(administrationModule, administrationSection);

        fillStudentOperations(officeModule, officeSection);
        fillAcademicServiceRequests(officeModule, officeSection);
        fillListings(administrationModule, administrationSection);

        fillExecutionCourseManagement(administrationModule, administrationSection);

        fillDegreeCurricularPlanManagement(administrationModule, administrationSection);

        fillDepartmentDegrees(administrationModule, administrationSection);

        fillPriceManagement(administrationModule, administrationSection);

        fillExtraCurricularActivitiesManagement(administrationModule, administrationSection);

        fillExternalUnitsManagement(administrationModule, administrationSection);

        // Candidacies

        fillOver23Candidacies(officeModule, officeSection);
        fillSecondCycleCandidacies(officeModule, officeSection);
        fillGraduatedPersonCandidacies(officeModule, officeSection);
        fillDegreeChangeManagement(officeModule, officeSection);
        fillDegreeTransferManagement(officeModule, officeSection);
        fillStandaloneCandidacies(officeModule, officeSection);
        fillMobilityCandidacies(officeModule, officeSection);

        // marksheets

        fillMarksheets(administrationModule, administrationSection);

        fillOldMarksheets(administrationModule, administrationSection);

        fillViewMarksheets(administrationModule, administrationSection);

        // contributors

        fillCreateContributors(administrationModule, administrationSection);

        fillViewContributors(administrationModule, administrationSection);

        fillEditContributors(administrationModule, administrationSection);

        // institutions

        fillDocuments(officeModule, officeSection);

        fillStudentsUtlCandidates(administrationModule, administrationSection);

        fillManageRegisteredDegreeCandidacies(administrationModule, administrationSection);

        fillManageEventReports(administrationModule, administrationSection);

        // Phd

        fillPhdProcessManagement(officeModule, officeSection);
        fillPhdCandidacyPeriodsManagement(officeModule, officeSection);
        fillPhdEnrolmentPeriodsManagement(officeModule, officeSection);
    }

    private static void fillStudentOperations(Module parent, Container parentSection) {
        Module module = makeModule(parent, "Operações de Alunos", "/");

        Section section = makeSection(parentSection, "Operações de Alunos", CREATE_REGISTRATION + " || " + MANAGE_STUDENTS);

        makeFunctionality(module, section, "Matricular Aluno", "/createStudent.do?method=prepareCreateStudent",
                CREATE_REGISTRATION);

        makeFunctionality(module, section, "Visualizar Alunos", "/students.do?method=prepareSearch", MANAGE_STUDENTS);
    }

    private static void fillMarksheets(Module parent, Container parentSection) {
        Module module = makeModule(parent, "Pautas", "/");

        Section section = makeSection(parentSection, "Pautas", MARKSHEET_MANAGEMENT);

        makeFunctionality(module, section, "Gestão Pautas", "/markSheetManagement.do?method=prepareSearchMarkSheet",
                MARKSHEET_MANAGEMENT);
    }

    private static void fillOldMarksheets(Module parent, Section parentSection) {
        Module module = makeModule(parent, "Pautas", "/");

        Section section = makeSection(parentSection, "Pautas", MARKSHEET_MANAGEMENT);

        makeFunctionality(module, section, "Gestão Pautas Antigas", "/oldMarkSheetManagement.do?method=prepareSearchMarkSheet",
                MARKSHEET_MANAGEMENT);
    }

    private static void fillViewMarksheets(Module parent, Section parentSection) {
        Module module = makeModule(parent, "Pautas", "/");

        Section section = makeSection(parentSection, "Pautas", MARKSHEET_MANAGEMENT);

        makeFunctionality(module, section, "Consulta", "/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare",
                MARKSHEET_MANAGEMENT);
    }

    private static void fillCreateContributors(Module parent, Container parentSection) {
        Module module = makeModule(parent, "Contribuintes", "/");

        Section section = makeSection(parentSection, "Contribuintes", MANAGE_CONTRIBUTORS);

        makeFunctionality(module, section, "Criar Contribuinte", "/createContributorDispatchAction.do?method=prepare",
                MANAGE_CONTRIBUTORS);
    }

    private static void fillViewContributors(Module parent, Section parentSection) {
        Module module = makeModule(parent, "Contribuintes", "/");

        Section section = makeSection(parentSection, "Contribuintes", MANAGE_CONTRIBUTORS);

        makeFunctionality(module, section, "Consulta de Contribuintes", "/visualizeContributors.do?method=prepare",
                MANAGE_CONTRIBUTORS);
    }

    private static void fillEditContributors(Module parent, Section parentSection) {
        Module module = makeModule(parent, "Contribuintes", "/");

        Section section = makeSection(parentSection, "Contribuintes", MANAGE_CONTRIBUTORS);

        makeFunctionality(module, section, "Alteração de Contribuintes", "/editContributors.do?method=prepare",
                MANAGE_CONTRIBUTORS);
    }

    private static void fillAcademicServiceRequests(Module parent, Container parentSection) {
        Module module = makeModule(parent, "Serviços Académicos", "/");

        Section section =
                makeSection(parentSection, "Serviços Académicos", SERVICE_REQUESTS + " || " + SERVICE_REQUESTS_RECTORAL_SENDING);

        makeFunctionality(module, section, "Pedidos Novos",
                "/academicServiceRequestsManagement.do?method=search&academicSituationType=NEW", SERVICE_REQUESTS);

        makeFunctionality(module, section, "Pedidos em Processamento",
                "/academicServiceRequestsManagement.do?method=search&academicSituationType=PROCESSING", SERVICE_REQUESTS);

        makeFunctionality(module, section, "Pedidos Concluídos",
                "/academicServiceRequestsManagement.do?method=search&academicSituationType=CONCLUDED", SERVICE_REQUESTS);

        makeFunctionality(module, section, "Pedidos por Curso", "/requestListByDegree.do?method=prepareSearch", SERVICE_REQUESTS);

        makeFunctionality(module, section, "Envio à Reitoria", "/rectorateDocumentSubmission.do?method=index",
                SERVICE_REQUESTS_RECTORAL_SENDING);
    }

    private static void fillListings(Module parent, Container parentSection) {
        Module module = makeModule(parent, "Listagens", "/");

        Section section = makeSection(parentSection, "Listagens", STUDENT_LISTINGS);

        makeFunctionality(module, section, "Alunos por Curso", "/studentsListByDegree.do?method=prepareByDegree",
                STUDENT_LISTINGS);

        makeFunctionality(module, section, "Alunos por Disciplina",
                "/studentsListByCurricularCourse.do?method=prepareByCurricularCourse", STUDENT_LISTINGS);

    }

    private static void fillAuthorizations(Module parent, Section parentSection) {
        makeFunctionality(parent, parentSection, "Autorizações", "/authorizations.do?method=authorizations",
                MANAGE_AUTHORIZATIONS);
    }

    private static void fillExecutionCourseManagement(final Module parent, final Section parentSection) {
        Module module = makeModule(parent, "Disciplinas de execução", "/");

        Section section = makeSection(parentSection, "Gestão das disciplinas de execução", MANAGE_EXECUTION_COURSES);

        makeFunctionality(module, section, "Início", "/executionCourseManagement.do?method=index", MANAGE_EXECUTION_COURSES);
        makeFunctionality(module, section, "Criar Disciplinas de Execução", "/createExecutionCourses.do?method=chooseDegreeType",
                MANAGE_EXECUTION_COURSES);
        makeFunctionality(module, section, "Editar Disciplina Execução",
                "/editExecutionCourseChooseExPeriod.do?method=prepareEditExecutionCourse", MANAGE_EXECUTION_COURSES);
        makeFunctionality(module, section, "Juntar Disciplinas Execução",
                "/chooseDegreesForExecutionCourseMerge.do?method=prepareChooseDegreesAndExecutionPeriod",
                MANAGE_EXECUTION_COURSES_ADV);
        makeFunctionality(module, section, "Inserir Disciplina Execução",
                "/insertExecutionCourse.do?method=prepareInsertExecutionCourse", MANAGE_EXECUTION_COURSES);
        makeFunctionality(module, section, "Supervisão de Cargas Horárias", "/courseLoadOverview.do?method=viewInconsistencies",
                VIEW_SCHEDULING_OVERSIGHT);
        makeFunctionality(module, section, "Criar Relatórios de Docência",
                "/executionCourseManagement/createCourseReportsForExecutionPeriod.faces", MANAGE_EXECUTION_COURSES);
        makeFunctionality(module, section, "Periodos de Inscrições", "/manageEnrolementPeriods.do?method=prepare",
                MANAGE_ENROLMENT_PERIODS);
    }

    private static void fillDegreeCurricularPlanManagement(final Module parent, final Section parentSection) {
        Module module = makeModule(parent, "Gestão da Estrutura de Ensino", "/");

        Section section = makeSection(parentSection, "Gestão da Estrutura de Ensino", MANAGE_DEGREE_CURRICULAR_PLANS);

        makeFunctionality(module, section, "Estrutura de Cursos", "/bolonha/curricularPlans/curricularPlansManagement.faces",
                MANAGE_DEGREE_CURRICULAR_PLANS);
    }

    private static void fillDepartmentDegrees(Module parent, Section parentSection) {
        Module module = makeModule(parent, "Gestão da Estrutura de Ensino", "/");

        Section section = makeSection(parentSection, "Gestão da Estrutura de Ensino", MANAGE_DEGREE_CURRICULAR_PLANS);

        makeFunctionality(module, section, "Gerir Cursos de Departamentos", "/manageDepartmentDegrees.do?method=prepare",
                MANAGE_DEGREE_CURRICULAR_PLANS);
    }

    private static void fillPriceManagement(Module parent, Section parentSection) {

        Module module = makeModule(parent, "Pagamentos", "/");

        Section section = makeSection(parentSection, "Preçários", MANAGE_PRICES);

        makeFunctionality(module, section, "Preçário", "/pricesManagement.do?method=viewPrices", MANAGE_PRICES);
    }

    private static void fillExtraCurricularActivitiesManagement(Module parent, Section parentSection) {

        Module module = makeModule(parent, "Actividades Extra Curriculares", "/");

        Section section = makeSection(parentSection, "Actividades Extra Curriculares", MANAGE_EXTRA_CURRICULAR_ACTIVITIES);

        makeFunctionality(module, section, "Gerir Tipos de Actividades Extra Curriculares",
                "/manageExtraCurricularActivities.do?method=list", MANAGE_EXTRA_CURRICULAR_ACTIVITIES);
    }

    private static void fillExternalUnitsManagement(Module parent, Section parentSection) {

        Module module = makeModule(parent, "Instituições", "/");

        Section section = makeSection(parentSection, "Instituições", MANAGE_EXTERNAL_UNITS);

        makeFunctionality(module, section, "Instituições Externas", "/externalUnits.do?method=prepareSearch",
                MANAGE_EXTERNAL_UNITS);
    }

    private static void fillOver23Candidacies(Module parent, Section parentSection) {

        Module module = makeModule(parent, "Candidaturas", "/");

        Section section = makeSection(parentSection, "Candidaturas", MANAGE_CANDIDACY_PROCESSES);

        makeFunctionality(module, section, "Maiores 23", "/caseHandlingOver23CandidacyProcess.do?method=intro",
                MANAGE_CANDIDACY_PROCESSES);
    }

    private static void fillSecondCycleCandidacies(Module parent, Section parentSection) {

        Module module = makeModule(parent, "Candidaturas", "/");

        Section section = makeSection(parentSection, "Candidaturas", MANAGE_CANDIDACY_PROCESSES);

        makeFunctionality(module, section, "2º Ciclo", "/caseHandlingSecondCycleCandidacyProcess.do?method=intro",
                MANAGE_CANDIDACY_PROCESSES);
    }

    private static void fillGraduatedPersonCandidacies(Module parent, Section parentSection) {

        Module module = makeModule(parent, "Candidaturas", "/");

        Section section = makeSection(parentSection, "Candidaturas", MANAGE_CANDIDACY_PROCESSES);

        makeFunctionality(module, section, "Cursos Médios e Superiores",
                "/caseHandlingDegreeCandidacyForGraduatedPersonProcess.do?method=intro", MANAGE_CANDIDACY_PROCESSES);
    }

    private static void fillDegreeChangeManagement(Module parent, Section parentSection) {

        Module module = makeModule(parent, "Candidaturas", "/");

        Section section = makeSection(parentSection, "Candidaturas", MANAGE_CANDIDACY_PROCESSES);

        makeFunctionality(module, section, "Mudanças de Curso", "/caseHandlingDegreeChangeCandidacyProcess.do?method=intro",
                MANAGE_CANDIDACY_PROCESSES);
    }

    private static void fillDegreeTransferManagement(Module parent, Section parentSection) {

        Module module = makeModule(parent, "Candidaturas", "/");

        Section section = makeSection(parentSection, "Candidaturas", MANAGE_CANDIDACY_PROCESSES);

        makeFunctionality(module, section, "Transferências", "/caseHandlingDegreeTransferCandidacyProcess.do?method=intro",
                MANAGE_CANDIDACY_PROCESSES);
    }

    private static void fillStandaloneCandidacies(Module parent, Section parentSection) {

        Module module = makeModule(parent, "Candidaturas", "/");

        Section section = makeSection(parentSection, "Candidaturas", MANAGE_CANDIDACY_PROCESSES);

        makeFunctionality(module, section, "Curriculares Isoladas", "/caseHandlingStandaloneCandidacyProcess.do?method=intro",
                MANAGE_CANDIDACY_PROCESSES);
    }

    private static void fillMobilityCandidacies(Module parent, Section parentSection) {

        Module module = makeModule(parent, "Candidaturas", "/");

        Section section = makeSection(parentSection, "Candidaturas", MANAGE_CANDIDACY_PROCESSES);

        makeFunctionality(module, section, "Mobilidade", "/caseHandlingMobilityApplicationProcess.do?method=intro",
                MANAGE_CANDIDACY_PROCESSES);
    }

    private static void fillDocuments(Module parent, Container parentSection) {
        Module module = makeModule(parent, "Documentos", "/");

        Section section = makeSection(parentSection, "Documentos", MANAGE_DOCUMENTS);

        makeFunctionality(module, section, "IRS Anual", "/generatedDocuments.do?method=prepareSearchPerson", MANAGE_DOCUMENTS);
    }

    private static void fillStudentsUtlCandidates(Module parent, Container parentSection) {
        Module module = makeModule(parent, "Bolsas", "/");

        Section section = makeSection(parentSection, "Bolsas", REPORT_STUDENTS_UTL_CANDIDATES);

        makeFunctionality(module, section, "Bolsas da UTL", "/reportStudentsUTLCandidates.do?method=prepare",
                REPORT_STUDENTS_UTL_CANDIDATES);
    }

    private static void fillManageRegisteredDegreeCandidacies(Module parent, Container parentSection) {
        Module module = makeModule(parent, "Matriculas", "/");

        Section section = makeSection(parentSection, "Matriculas", MANAGE_REGISTERED_DEGREE_CANDIDACIES);

        makeFunctionality(module, section, "Alunos matriculados 1º ano 1º vez", "/registeredDegreeCandidacies.do?method=view",
                MANAGE_REGISTERED_DEGREE_CANDIDACIES);
    }

    private static void fillManageEventReports(Module parent, Container parentSection) {
        Module module = makeModule(parent, "Relatório de dividas", "/");

        Section section = makeSection(parentSection, "Relatório de dividas", MANAGE_EVENT_REPORTS);

        makeFunctionality(module, section, "Relatório de dividas", "/eventReports.do?method=listReports", MANAGE_EVENT_REPORTS);
    }

    private static void fillPhdProcessManagement(Module parent, Container parentSection) {
        Module module = makeModule(parent, "Doutoramentos", "/");

        Section section = makeSection(parentSection, "Doutoramentos", MANAGE_PHDS);

        makeFunctionality(module, section, "Processos de Doutoramento", "/phdIndividualProgramProcess.do?method=manageProcesses",
                MANAGE_PHD_PROCESSES);
    }

    private static void fillPhdCandidacyPeriodsManagement(Module parent, Container parentSection) {
        Module module = makeModule(parent, "Doutoramentos", "/");

        Section section = makeSection(parentSection, "Doutoramentos", MANAGE_PHDS);

        makeFunctionality(module, section, "Gestão dos períodos de candidatura", "/phdCandidacyPeriodManagement.do?method=list",
                MANAGE_PHD_PROCESSES);
    }

    private static void fillPhdEnrolmentPeriodsManagement(Module parent, Container parentSection) {
        Module module = makeModule(parent, "Doutoramentos", "/");

        Section section = makeSection(parentSection, "Doutoramentos", MANAGE_PHDS);

        makeFunctionality(module, section, "Periodos de Inscrição",
                "/phdIndividualProgramProcess.do?method=manageEnrolmentPeriods", MANAGE_PHD_ENROLMENT_PERIODS);
    }

    private static <T extends Content> T findChild(Container module, String name) {
        for (final Node node : module.getChildrenSet()) {
            final Content child = node.getChild();
            if (child.getName().getContent().equals(name)) {
                return (T) child;
            }
        }
        return null;
    }

    private static FunctionalityCall findCall(Section section, Functionality functionality) {
        for (final Node node : section.getChildrenSet()) {
            final Content child = node.getChild();
            if (child instanceof FunctionalityCall) {
                FunctionalityCall call = (FunctionalityCall) child;
                if (call.getFunctionality().equals(functionality)) {
                    return call;
                }
            }
        }
        return null;
    }

    private static Module makeModule(Module parent, String name, String prefix) {
        Module module = findChild(parent, name);
        if (module == null) {
            module = new Module(new MultiLanguageString(name), prefix);
            parent.addChild(module);
        }
        return module;
    }

    private static Section makeSection(Container parentSection, String name, String groupExpression) {
        Section section = findChild(parentSection, name);
        if (section == null) {
            section = new Section(parentSection, new MultiLanguageString(name));
        }
        if (section.getAvailabilityPolicy() != null) {
            section.getAvailabilityPolicy().delete();
        }
        new ExpressionGroupAvailability(section, groupExpression);
        return section;
    }

    private static void makeFunctionality(Module parent, Section parentSection, String name, String path, String groupExpression) {
        Functionality function = findChild(parent, name);
        if (function == null) {
            function = new Functionality(new MultiLanguageString(name));
            parent.addChild(function);
        }
        function.setExecutionPath(path);
        FunctionalityCall call = findCall(parentSection, function);
        if (call == null) {
            call = new FunctionalityCall(function);
            parentSection.addChild(call);
        }
        if (call.getAvailabilityPolicy() != null) {
            call.getAvailabilityPolicy().delete();
        }
        new ExpressionGroupAvailability(call, groupExpression);
    }

}
