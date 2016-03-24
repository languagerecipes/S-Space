/*
 * Copyright (c) 2011, Lawrence Livermore National Security, LLC. Produced at
 * the Lawrence Livermore National Laboratory. Written by Keith Stevens,
 * kstevens@cs.ucla.edu OCEC-10-073 All rights reserved. 
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

package edu.ucla.sspace.text.corpora;

import edu.ucla.sspace.text.CorpusReader;
import edu.ucla.sspace.text.Document;

import java.io.StringReader;
import java.util.Iterator;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * @author Keith Stevens
 */
public class PukWacCorpusReaderTest {

    public static final String TEST_TEXT =
        "<text>\n" +
        "<s>\n" +
        "the blah blah blah\n" +
        "chicken blah blah blah\n" +
        "ate blah blah blah\n" +
        "a blah blah blah\n" +
        "dog blah blah blah\n" +
        ". blah blah blah\n" +
        "</s>\n" +
        "<s>\n" +
        "bob blah blah blah\n" +
        "rocks blah blah blah\n" +
        "! blah blah blah\n" +
        "</s>\n" +
        "</text>\n" +
        "<text>\n" +
        "<s>\n" +
        "blah blah blah\n" +
        "bloh blah blah\n" +
        "</s>\n" +
        "</text>\n";

    @Test public void testIterator() throws Exception {
        CorpusReader<Document> reader = new PukWacCorpusReader();
        Iterator<Document> docIter = reader.read(new StringReader(TEST_TEXT));
        assertTrue(docIter.hasNext());
        assertEquals("the chicken ate a dog . bob rocks ! ",
                     docIter.next().reader().readLine());
        assertTrue(docIter.hasNext());
        assertEquals("blah bloh ",
                     docIter.next().reader().readLine());
        assertFalse(docIter.hasNext());
    }
}

