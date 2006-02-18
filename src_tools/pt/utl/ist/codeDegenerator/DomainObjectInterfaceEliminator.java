package pt.utl.ist.codeDegenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import antlr.ANTLRException;
import dml.DmlCompiler;
import dml.DomainClass;
import dml.DomainModel;

public class DomainObjectInterfaceEliminator {

	private static List<String> processArgs(final String[] args) {
		final List<String> dmlFiles = new ArrayList<String>();

		int i = 0;
		while (i < args.length) {
			if ("-d".equals(args[i])) {
				dmlFiles.add(getNextArg(args, i));
				args[i] = null;
				i += 2;
			} else {
				i++;
			}
		}

		return dmlFiles;
	}


	private static File fileToProcess(String[] args) {
		int i = 0;
		while (i < args.length) {
			if ("-f".equals(args[i])) {
				final String filename = getNextArg(args, i);
				final File file = new File(filename);
				if (file.exists()) {
					System.out.println("Will replace file(s): " + filename);
					return file;
				} else {
					throw new Error("File " + filename + " does not exist.");
				}
			} else {
				i++;
			}
		}
		throw new Error("No replace file(s) specified.");
	}

	private static List<String> ignorePatterns(final String[] args) {
		final List<String> ignorePatterns = new ArrayList<String>();

		int i = 0;
		while (i < args.length) {
			if ("-i".equals(args[i])) {
				ignorePatterns.add(getNextArg(args, i));
				args[i] = null;
				i += 2;
			} else {
				i++;
			}
		}

		return ignorePatterns;
	}

	private static String getNextArg(String[] args, int i) {
		int next = i + 1;
		if ((next >= args.length) || (args[next] == null)) {
			throw new Error("Invalid argument following '" + args[i] + "'");
		}
		String result = args[next];
		args[next] = null;
		return result;
	}

	public static void main(String[] args) {
		final File file = fileToProcess(args);
		final List<String> ignorePatterns = ignorePatterns(args);
		final DomainModel domainModel = retreiveDomainModel(args);
		final Set<TokenValueBean> replaceTokens = determineReplaceTokens(domainModel);
		final Report report = new Report();
		try {
			final File outputFile = new File("interfaceEliminator.sh");
			outputFile.createNewFile();
			final FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

			final List<String> filenames = new ArrayList<String>();

			replace(replaceTokens, file, ignorePatterns, report, fileOutputStream, filenames);

			fileOutputStream.write("#!/bin/sh\n".getBytes());

			generateScript(replaceTokens, fileOutputStream, new String[]{ " " }, new ArrayList<String>(filenames));
			generateScript(replaceTokens, fileOutputStream, new String[]{ "<" }, new ArrayList<String>(filenames));
			generateScript(replaceTokens, fileOutputStream, new String[]{ ">" }, new ArrayList<String>(filenames));
			generateScript(replaceTokens, fileOutputStream, new String[]{ "(" }, new ArrayList<String>(filenames));
			generateScript(replaceTokens, fileOutputStream, new String[]{ ")" }, new ArrayList<String>(filenames));
			generateScript(replaceTokens, fileOutputStream, new String[]{ "," }, new ArrayList<String>(filenames));
			generateScript(replaceTokens, fileOutputStream, new String[]{ ";" }, new ArrayList<String>(filenames));
			generateScript(replaceTokens, fileOutputStream, new String[]{ "." }, new ArrayList<String>(filenames));

			fileOutputStream.close();
		} catch (IOException e) {
			throw new Error(e);
		}
		report.writeReport();
	}

	private static final int BATCH_SIZE = 500;

	private static void generateScript(final Set<TokenValueBean> replaceTokens, final FileOutputStream fileOutputStream, final String[] replacementTokens, final ArrayList<String> filenames) throws IOException {
		fileOutputStream.write("echo Replacing for tokens: ".getBytes());
		for (final String string : replacementTokens) {
			fileOutputStream.write("\\".getBytes());
			fileOutputStream.write(string.getBytes());
		}
		fileOutputStream.write("\n".getBytes());
		while (!filenames.isEmpty()) {
			fileOutputStream.write("date\n".getBytes());
			fileOutputStream.write(REPLACE_PREFIX);
			for (final TokenValueBean tokenValueBean : replaceTokens) {
				writeReplacementTokens(fileOutputStream, tokenValueBean, replacementTokens);
			}
			fileOutputStream.write(FILENAME_PREFIX);
			for (int i = 0; i++ < Math.min(500, filenames.size()); filenames.remove(0)) {
				fileOutputStream.write(SEPERATOR);
				fileOutputStream.write(filenames.get(0).getBytes());
			}
			fileOutputStream.write(" > /dev/null".getBytes());
			fileOutputStream.write(NEWLINE);
			fileOutputStream.write("date\n".getBytes());
			fileOutputStream.write(("echo Processed " + BATCH_SIZE + "\n").getBytes());
		}
	}

    private static final byte[] PARENTHESIS = "\"".getBytes();

    private static void writeReplacementTokens(final FileOutputStream fileOutputStream, final TokenValueBean tokenValueBean, final String ... replacementTokens) throws IOException {
    	for (final String replacementToken : replacementTokens) {
    		writeReplacementToken(fileOutputStream, tokenValueBean, replacementToken);
    	}
    }


    private static void writeReplacementToken(final FileOutputStream fileOutputStream, final TokenValueBean tokenValueBean, final String postfix) throws IOException {
        fileOutputStream.write(SEPERATOR);
        fileOutputStream.write(PARENTHESIS);
        fileOutputStream.write(tokenValueBean.getToken().getBytes());
        fileOutputStream.write(postfix.getBytes());
        fileOutputStream.write(PARENTHESIS);
        fileOutputStream.write(SEPERATOR);
        fileOutputStream.write(PARENTHESIS);
        fileOutputStream.write(tokenValueBean.getValue().getBytes());
        fileOutputStream.write(postfix.getBytes());
        fileOutputStream.write(PARENTHESIS);
    }

	private static DomainModel retreiveDomainModel(final String[] args) {
		try {
			return DmlCompiler.getDomainModel(processArgs(args));
		} catch (ANTLRException e) {
			throw new Error(e);
		}
	}

	private static Set<TokenValueBean> determineReplaceTokens(final DomainModel domainModel) {
		final Set<TokenValueBean> replaceTokens = new HashSet<TokenValueBean>();

		for (final Iterator<DomainClass> iterator = domainModel.getClasses(); iterator.hasNext();) {
			final DomainClass object = iterator.next();
			final String domainClassName = object.getName();
			final String interfaceName = "I" + domainClassName;
			replaceTokens.add(new TokenValueBean(interfaceName, domainClassName));
		}
		System.out.println("Found " + replaceTokens.size() + " domain classes.");

		return replaceTokens;
	}

	private static final byte[] REPLACE_PREFIX = "replace".getBytes();
	private static final byte[] SEPERATOR = " ".getBytes();
	private static final byte[] FILENAME_PREFIX = " -- ".getBytes();
	private static final byte[] NEWLINE = "\n".getBytes();

	

	private static void replace(final Set<TokenValueBean> replaceTokens, final File file, final List<String> ignorePatterns, final Report report, final FileOutputStream fileOutputStream, final List<String> filenames) throws IOException {
		for (final String ignorePattern : ignorePatterns) {
			if (file.getAbsolutePath().indexOf(ignorePattern) != -1) {
				return;
			}
		}

		if (file.isDirectory()) {
			report.registerDirAsProccessed(file);
			for (final File subFile : file.listFiles()) {
				replace(replaceTokens, subFile, ignorePatterns, report, fileOutputStream, filenames);
			}
		} else {
			report.registerFileAsProccessed(file);
			filenames.add(normalizeFileName(file.getAbsolutePath()));
		}
	}


	private static String normalizeFileName(final String filename) {
		String replacedString = filename;
		replacedString = replacedString.replace(" ", "\\ ");
		replacedString = replacedString.replace("(", "\\(");
		replacedString = replacedString.replace(")", "\\)");
		replacedString = replacedString.replace("$", "\\$");
		replacedString = replacedString.replace("{", "\\{");
		replacedString = replacedString.replace("}", "\\}");
		return replacedString;
	}

}
