/*
 * Copyright 2014 David Jurgens
 *
 * This file is part of the S-Space package and is covered under the terms and
 * conditions therein.
 *
 * The S-Space package is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation and distributed hereunder to you.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND NO REPRESENTATIONS OR WARRANTIES,
 * EXPRESS OR IMPLIED ARE MADE.  BY WAY OF EXAMPLE, BUT NOT LIMITATION, WE MAKE
 * NO REPRESENTATIONS OR WARRANTIES OF MERCHANT- ABILITY OR FITNESS FOR ANY
 * PARTICULAR PURPOSE OR THAT THE USE OF THE LICENSED SOFTWARE OR DOCUMENTATION
 * WILL NOT INFRINGE ANY THIRD PARTY PATENTS, COPYRIGHTS, TRADEMARKS OR OTHER
 * RIGHTS.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package edu.ucla.sspace.vsm;

import java.io.*;
import java.util.*;

import org.junit.Ignore;
import org.junit.Test;

import edu.ucla.sspace.text.IteratorFactory;

import static org.junit.Assert.*;


/**
 * A collection of unit tests for {@link VectorSpaceModel} 
 */
public class VsmTest {

    @Test public void testCreate() throws Exception {
        VectorSpaceModel vsm = new VectorSpaceModel();
    }

    @Test public void testProcessDocument() throws Exception {
        VectorSpaceModel vsm = new VectorSpaceModel();
        vsm.processDocument(new BufferedReader(new StringReader("This is a test document.")));
    }

    @Test public void testEmptyDocument() throws Exception {
        VectorSpaceModel vsm = new VectorSpaceModel();
        vsm.processDocument(new BufferedReader(new StringReader("")));
    }

    @Test public void testProcessSpace() throws Exception {
        VectorSpaceModel vsm = new VectorSpaceModel();
        vsm.processDocument(new BufferedReader(new StringReader("This is a test document.")));
        vsm.processDocument(new BufferedReader(new StringReader("This is a second doc.")));
        vsm.processDocument(new BufferedReader(new StringReader("After the second, this could be the third document.")));
        vsm.processSpace(new Properties());
    }

    @Test public void testGetDocumentVector() throws Exception {
        IteratorFactory.setProperties(new Properties());
        VectorSpaceModel vsm = new VectorSpaceModel();
        try {
            String[] docArr = new String[] {
                "shipment of gold damaged in a fire",
                "delivery of silver arrived in a silver truck",
                "shipment of gold arrived in a truck"
            };
            for (String doc : docArr) {
                vsm.processDocument(new BufferedReader(new StringReader(doc)));
            }           

            vsm.processSpace(new Properties());
            System.out.printf("Vsm has %d docs and %d words%n",
                              vsm.documentSpaceSize(), vsm.getWords().size());
            
            assertEquals(3, vsm.documentSpaceSize());
            for (int i = 0; i < 3; ++i)
                vsm.getDocumentVector(i);
        }
        catch (Throwable t) {
            throw new Error(t);
        }
    }
}
