package at.ac.tuwien.ims.latex2mobiformulaconv.tests.utils;

import org.apache.log4j.Logger;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

/**
 * The MIT License (MIT)
 * latex2mobi -- LaTeX Formulas to Mobi Converter
 * Copyright (c) 2014 Michael Auß
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * <p/>
 * <p/>
 * For Third Party Software Licenses read LICENSE file in base dir.
 * <p/>
 * Created on 11.04.15.
 * <p/>
 * Thanks to Paco Castro
 * via http://gilmation.com/articles/junit-with-standard-and-error-output-in-main-methods/
 *
 * @author Michael Auß
 * @author Paco Castro
 */
public abstract class AbstractMainTests {
    public static Logger logger = Logger.getLogger(AbstractMainTests.class);

    /**
     * This Security Manager prevents a the Main class from exiting before any assertions or debugging could be made.
     */
    public static class StopExitSecurityManager extends SecurityManager {
        private SecurityManager prevManager = System.getSecurityManager();

        /**
         * Intentionally does nothing
         *
         * @param perm
         */
        public void checkPermission(Permission perm) {
            // Do nothing
        }

        /**
         * This throws an exception if System.exit is called.
         */
        public void checkExit(int status) {
            super.checkExit(status);
            throw new SecurityException();
        }

        public SecurityManager getPreviousManager() {
            return prevManager;
        }
    }

    private static final PrintStream OUT = System.out;
    private static final PrintStream ERR = System.err;

    private static void recoverOriginalOutput() {
        System.err.flush();
        System.out.flush();
        System.setOut(AbstractMainTests.OUT);
        System.setErr(AbstractMainTests.ERR);
    }

    public static String[] executeMain(String className, String[] args) {
        // First, change the standard and error output streams
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream tempOutput = new PrintStream(bos, true);
        System.setOut(tempOutput);
        System.setErr(tempOutput);

        List<String> result = new ArrayList<String>();
        try {
            AbstractMainTests.invokeMain(className, args); // Call main!!
            // Collect main() execution output
            BufferedReader reader =
                    new BufferedReader(new StringReader(bos.toString()));
            String line = reader.readLine();
            while (line != null) {
                result.add(line);
                line = reader.readLine();
            }
        } catch (Throwable e) {
            throw new RuntimeException(
                    "Error obtaining output for [" + className + "]", e
            );
        } finally {
            recoverOriginalOutput();  // Return output to its original form
            try {
                bos.close();
                tempOutput.close();  // Close streams
            } catch (IOException e) {
            }
        }
        return result.toArray(new String[0]); // Convert from list to an array
    }

    public static void invokeMain(String test, String[] args) {
        try {
            Class clazz = Class.forName(test);
            Object app = clazz.newInstance();
            Method m = app.getClass().
                    getMethod("main", (new String[0]).getClass());

            // Make sure it is the static void main(String[]) method
            if ((m.getReturnType() != Void.TYPE) ||
                    (!Modifier.isStatic(m.getModifiers()))) {
                throw new RuntimeException(
                        "Not executable found: static main(String[])"
                );
            }

            // Deactivate System.exit() calls
            SecurityManager securityManager = new StopExitSecurityManager();
            System.setSecurityManager(securityManager);

            Object[] param = {args};

            try {
                m.invoke(app, param);
            } catch (InvocationTargetException e) {
                // Do nothing
            } catch (SecurityException e) {
                // Do nothing
            } finally {
                // Reset security manager
                SecurityManager currentManager = System.getSecurityManager();
                if (currentManager != null
                        && currentManager instanceof StopExitSecurityManager) {
                    StopExitSecurityManager stopExitSecurityManager = (StopExitSecurityManager) currentManager;
                    System.setSecurityManager(stopExitSecurityManager.getPreviousManager());
                } else {
                    System.setSecurityManager(null);
                }
            }


        } catch (Throwable e) {
            throw new RuntimeException("Error executing main", e);
        }
    }
}
