package net.sourceforge.fenixedu.persistenceTier.delegatedObjects;

public class DAOResultLogger {

    public static void log(final Object mainDAO, final Object secondaryDAO, final String methodName,
            final Object[] parameters) {
        if (secondaryDAO == null) {
            System.out.println("Versioned Object DAO for " + mainDAO.getClass().getName() + " is not implemented!");
        } else {
            System.out.println("DAO results differ for: " + generateDAOCall(mainDAO, methodName, parameters) + " != " + generateDAOCall(secondaryDAO, methodName, parameters));
        }
    }

    public static void logOK(final Object mainDAO, final Object secondaryDAO, final String methodName,
            final Object[] parameters) {
        System.out.println("DAO results for: " + generateDAOCallWithoutParameters(mainDAO, methodName) + " == " + generateDAOCallWithoutParameters(secondaryDAO, methodName));
    }

    private static String generateDAOCall(final Object dAO, String methodName, final Object[] parameters) {
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(dAO.getClass().getName());
        stringBuilder.append(".");
        stringBuilder.append(methodName);
        stringBuilder.append("(");
        for (int i = 0; i < parameters.length; i++) {
            if (i > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(parameters[i]);
        }
        stringBuilder.append(")");

        return stringBuilder.toString();
    }

    private static String generateDAOCallWithoutParameters(final Object dAO, String methodName) {
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(dAO.getClass().getName());
        stringBuilder.append(".");
        stringBuilder.append(methodName);
        stringBuilder.append("(");
        stringBuilder.append("...");
        stringBuilder.append(")");

        return stringBuilder.toString();
    }

}
