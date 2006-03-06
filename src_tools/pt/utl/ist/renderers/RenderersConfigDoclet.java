package pt.utl.ist.renderers;

import java.util.Set;

import pt.utl.ist.renderers.producers.DocumentationProducer;
import pt.utl.ist.renderers.producers.DocumentationProducerFactory;

import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.RootDoc;

public class RenderersConfigDoclet {
    public static boolean start(RootDoc root) {
        ParserConfiguration configuration = parseOptions(root.options());
        
        RenderersParser parser = new RenderersParser(configuration);
        parser.parse(root);
        
        DocumentationProducer producer = getProducer(configuration);
        producer.produce(parser);
        
        return true;
    }
    
    private static DocumentationProducer getProducer(ParserConfiguration configuration) {
        return DocumentationProducerFactory.getInstance().getProducer(configuration.getType());
    }

    public static int optionLength(String option) {
        if (option.equals("-d")) { // destiny
            return 2;
        }
        
        if (option.equals("-c")) { // configuration
            return 2;
        }
        
        if (option.equals("-dtd")) { // dtd
            return 2;
        }
        
        if (option.equals("-type")) { // type
            return 2;
        }
        
        return 0;
    }

    public static boolean validOptions(String options[][], DocErrorReporter reporter) {
        boolean typeOptionFound = false;
        
        for (int i = 0; i < options.length; i++) {
            String[] option = options[i];
            
            if (option[0].equals("-type")) {
                typeOptionFound = true;
                
                Set<String> availableTypes = DocumentationProducerFactory.getInstance().getAvailableTypes();
                
                if (! availableTypes.contains(option[1])) {
                    reporter.printError("Type '" + option[1] + "' is not supported.");
                    printAvailableTypes(reporter);
                    
                    return false;
                }
            }
        }
        
        if (! typeOptionFound) {
            reporter.printError("You must choose a output type with -type");
            printAvailableTypes(reporter);
        }
        
        return true;
    }
    
    private static void printAvailableTypes(DocErrorReporter reporter) {
        reporter.printNotice("Available types:");

        Set<String> availableTypes = DocumentationProducerFactory.getInstance().getAvailableTypes();
        for (String type : availableTypes) {
            reporter.printNotice("  - " + type);
        }
    }
    
    private static ParserConfiguration parseOptions(String[][] options) {
        ParserConfiguration configuration = new ParserConfiguration();
        
        for (int i = 0; i < options.length; i++) {
            String[] option = options[i];
            
            if (option[0].equals("-d")) {
                configuration.setDestination(option[1]);
            }
            
            if (option[0].equals("-c")) {
                configuration.setConfiguration(option[1]);
            }
            
            if (option[0].equals("-dtd")) {
                configuration.setDtd(option[1]);
            }

            if (option[0].equals("-type")) {
                configuration.setType(option[1]);
            }
        }

        System.out.println("  destination: " + configuration.getDestination());
        System.out.println("configuration: " + configuration.getConfiguration());
        System.out.println("          dtd: " + configuration.getDtd());
        System.out.println("         type: " + configuration.getType());
        
        return configuration;
    }
    
}
