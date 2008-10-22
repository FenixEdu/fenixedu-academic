package net.sourceforge.fenixedu.tools.berserk;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import pt.utl.ist.berserk.logic.serviceManager.IServiceDefinition;
import pt.utl.ist.berserk.storage.digester.exceptions.StorageBrokerDigesterException;
import pt.utl.ist.berserk.storage.digester.serviceManager.ServiceDefinitionDigester;

public class BerserkReplacer {
    private static final String ROOT_PATH = "/home/pedro/workspace/fenix/";

    private static final String SOURCE_PATH = ROOT_PATH + "src/";

    private static final FileFilter CALLER_FILE_FILTER = new FileFilter() {
	@Override
	public boolean accept(File file) {
	    return (file.isDirectory() || file.getName().endsWith(".java")) && !file.getName().contains("BerserkReplacer");
	}
    };

    private static final FileFilter SOURCE_FILE_FILTER = new FileFilter() {
	@Override
	public boolean accept(File file) {
	    return !file.getName().contains("svn") && !file.getName().endsWith(".class");
	}
    };

    public Map<String, IServiceDefinition> readServices() throws StorageBrokerDigesterException, InstantiationException,
	    IllegalAccessException, FileNotFoundException {
	ServiceDefinitionDigester digester = new ServiceDefinitionDigester();
	Map<String, IServiceDefinition> serviceMap = new HashMap<String, IServiceDefinition>();
	List services = digester.readAll();
	for (Object object : services) {
	    IServiceDefinition service = (IServiceDefinition) object;
	    if (service.getIsTransactional() && service.getFilterChains().isEmpty()) {
		serviceMap.put(service.getName(), service);
	    }
	}
	return serviceMap;
    }

    public void findCalls(File file, LinkedList<PatternReplacer> patterns) throws IOException {
	if (file.isDirectory()) {
	    for (File child : file.listFiles(CALLER_FILE_FILTER)) {
		findCalls(child, patterns);
	    }
	} else {
	    String contents = readFileToString(file);
	    SortedSet<Replacement> replacements = new TreeSet<Replacement>();
	    for (PatternReplacer pattern : patterns) {
		try {
		    replacements.addAll(pattern.matchAndReplace(contents));
		} catch (FileUntouchableException e) {
		    System.out.println("Ignoring file: " + file.getAbsolutePath());
		}
	    }
	    if (!replacements.isEmpty()) {
		String replaced = replace(replacements, contents);
		writeFile(file, replaced);
		// System.out.println(replaced);
	    }
	}
    }

    public void processService(IServiceDefinition service) throws InstantiationException, IllegalAccessException, IOException,
	    FileUntouchableException {
	File file = new File((SOURCE_PATH + service.getImplementationClass().getName().replaceAll("\\.", "/") + ".java"));
	String contents = readFileToString(file);
	if (!contents.contains("@Service")) {
	    SortedSet<Replacement> replacements = new ServiceAnnotationInserter().matchAndReplace(contents);
	    replacements.addAll(new ImportInserter("pt.ist.fenixWebFramework.services.Service").matchAndReplace(contents));
	    if (!replacements.isEmpty()) {
		String replaced = replace(replacements, contents);
		writeFile(file, replaced);
		// System.out.println(replaced);
	    }
	}
    }

    private String findUnprocessedCalls(File file, Map<String, IServiceDefinition> services) throws IOException {
	StringBuilder unprocessed = new StringBuilder();
	if (file.isDirectory()) {
	    for (File child : file.listFiles(SOURCE_FILE_FILTER)) {
		unprocessed.append(findUnprocessedCalls(child, services));
	    }
	} else if (file.getName().endsWith(".java")) {
	    String contents = readFileToString(file);
	    for (IServiceDefinition service : services.values()) {
		if (contents.contains("\"" + service.getName() + "\"")) {
		    unprocessed.append("Unprocessed service: " + service.getName() + " found in: " + file.getName() + "\n");
		}
	    }
	} else {
	    String contents = readFileToString(file);
	    for (IServiceDefinition service : services.values()) {
		if (contents.contains(service.getName())) {
		    unprocessed.append("Unprocessed service: " + service.getName() + " found in: " + file.getName() + "\n");
		}
	    }
	}
	return unprocessed.toString();
    }

    private void cleanUpServices() throws IOException, FileUntouchableException {
	List<String> serviceNames = new ArrayList<String>();
	LineNumberReader reader = new LineNumberReader(new FileReader(ROOT_PATH + "processed.report"));
	String line;
	while ((line = reader.readLine()) != null) {
	    serviceNames.add(line);
	}
	ServiceCleaner cleaner = new ServiceCleaner(serviceNames);
	File file = new File(ROOT_PATH + "config/services.xml");
	String contents = readFileToString(file);
	SortedSet<Replacement> replacements = cleaner.matchAndReplace(contents);
	if (!replacements.isEmpty()) {
	    String replaced = replace(replacements, contents);
	    writeFile(file, replaced);
	    // System.out.println(replaced);
	}
    }

    public static void main(String[] args) throws IOException, FileUntouchableException {
	BerserkReplacer replacer = new BerserkReplacer();
	replacer.cleanUpServices();
    }

    // public static void main(String[] args) throws
    // StorageBrokerDigesterException, InstantiationException,
    // IllegalAccessException,
    // IOException {
    // BerserkReplacer replacer = new BerserkReplacer();
    // Map<String, IServiceDefinition> services = replacer.readServices();
    // Set<IServiceDefinition> untouchables = new HashSet<IServiceDefinition>();
    // for (IServiceDefinition service : services.values()) {
    // try {
    // replacer.processService(service);
    // } catch (FileUntouchableException e) {
    // untouchables.add(service);
    // }
    // }
    // for (IServiceDefinition untouchable : untouchables) {
    // services.remove(untouchable.getName());
    // }
    // LinkedList<PatternReplacer> patterns = new LinkedList<PatternReplacer>();
    // patterns.add(new NoArgsCaller(services));
    // patterns.add(new InlinedCaller(services));
    // // patterns.add(new MultiArgsCaller(services));
    // patterns.add(new OuterArgsCaller(services));
    // replacer.findCalls(new File(SOURCE_PATH), patterns);
    // String unprocessed = replacer.findUnprocessedCalls(new File(ROOT_PATH),
    // services);
    // File output = new File(ROOT_PATH + "unprocessed.report");
    // if (output.exists()) {
    // output.delete();
    // }
    // writeFile(output, unprocessed);
    // StringBuilder processed = new StringBuilder();
    // for (IServiceDefinition service : services.values()) {
    // processed.append(service.getName() + "\n");
    // }
    // File processedFile = new File(ROOT_PATH + "processed.report");
    // if (processedFile.exists()) {
    // processedFile.delete();
    // }
    // writeFile(processedFile, processed.toString());
    // }

    private String readFileToString(File file) throws IOException {
	FileReader reader = null;
	StringBuilder input = new StringBuilder();
	try {
	    reader = new FileReader(file);
	    LineNumberReader lineReader = new LineNumberReader(reader);
	    String line;
	    while ((line = lineReader.readLine()) != null) {
		input.append(line + "\n");
	    }
	    return input.toString();
	} finally {
	    if (reader != null)
		reader.close();
	}
    }

    public String replace(SortedSet<Replacement> replacements, String contents) {
	StringBuilder output = new StringBuilder();
	int current = 0;
	for (Replacement replacement : replacements) {
	    output.append(contents.substring(current, replacement.startIndex));
	    output.append(replacement.replacement);
	    current = replacement.endIndex;
	}
	output.append(contents.substring(current, contents.length() - 1));
	return output.toString();
    }

    public static void writeFile(File file, String replaced) {
	FileWriter writer = null;
	try {
	    writer = new FileWriter(file);
	    writer.write(replaced);
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (writer != null)
		    writer.close();
	    } catch (IOException e) {
	    }
	}
    }
}
