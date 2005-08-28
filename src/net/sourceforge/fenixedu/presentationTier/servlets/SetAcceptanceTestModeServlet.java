package net.sourceforge.fenixedu.presentationTier.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.stm.OJBFunctionalSetWrapper;

import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.CollectionDescriptor;
import org.apache.ojb.broker.metadata.DescriptorRepository;
import org.apache.ojb.broker.metadata.FieldDescriptor;
import org.apache.ojb.broker.metadata.MetadataManager;
import org.apache.ojb.broker.metadata.ObjectReferenceDescriptor;

public class SetAcceptanceTestModeServlet extends HttpServlet {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException,
            IOException {
        final int nextID = DomainObject.autoDetermineId();
        fixDescriptors();

        res.setContentType("text/html");

        PrintWriter out = res.getWriter();
        out.println("<HEAD><TITLE>Set Acceptance Test Method</TITLE></HEAD><BODY>");
        out.print("Domain object id's will be generated sequentially starting at: ");
        out.print(nextID);
        out.println("</BODY>");
        out.close();
    }

    private void fixDescriptors() {
        final MetadataManager metadataManager = MetadataManager.getInstance();
        final DescriptorRepository descriptorRepository = metadataManager.getGlobalRepository();
        final Map<Object, ClassDescriptor> descriptorTable = descriptorRepository.getDescriptorTable();

        for (final ClassDescriptor classDescriptor : descriptorTable.values()) {
            try {
                final FieldDescriptor[] fieldDescriptors = classDescriptor.getAutoIncrementFields();
                for (final FieldDescriptor fieldDescriptor : fieldDescriptors) {
                    fieldDescriptor.setAutoIncrement(false);
                }
            } catch (Exception ex) {
            }
        }
    }

}
