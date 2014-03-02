/*
 * (C) Copyright 2014 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Anahide Tchertchian
 */
package org.nuxeo.build.ant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.tools.ant.BuildException;
import org.junit.Test;

/**
 * @since 2.0.1
 */
public class TestAntClientErrorOnFailure {

    protected void checkErrorOnFailure(String filepath,
            Class<?> exceptionClass, String message) throws Exception {
        AntClient client = new AntClient(null);
        try {
            client.run(Thread.currentThread().getContextClassLoader().getResource(
                    filepath));
            fail(String.format("Should have failed with error '%s' ", message));
        } catch (Exception e) {
            assertTrue(String.format("Error %s should be an instance of %s", e,
                    exceptionClass), exceptionClass.isInstance(e));
            assertEquals(message, e.getMessage());
        }
    }

    @Test
    public void testErrorOnEmpty() throws Exception {
        checkErrorOnFailure("test-it-empty.xml", BuildException.class,
                "Premature end of file.");
    }

    @Test
    public void testErrorOnFailure() throws Exception {
        checkErrorOnFailure("test-it-fail.xml", BuildException.class,
                "I'm failing");
    }

}