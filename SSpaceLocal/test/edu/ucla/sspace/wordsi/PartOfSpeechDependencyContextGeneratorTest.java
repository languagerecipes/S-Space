/*
 * Copyright (c) 2011, Lawrence Livermore National Security, LLC. Produced at
 * the Lawrence Livermore National Laboratory. Written by Keith Stevens,
 * kstevens@cs.ucla.edu OCEC-10-073 All rights reserved. 
 *
 * This file is part of the C-Cat package and is covered under the terms and
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


package edu.ucla.sspace.wordsi;

import edu.ucla.sspace.basis.*;

import edu.ucla.sspace.dependency.*;

import edu.ucla.sspace.vector.*;

import java.io.*;

import java.util.HashMap;
import java.util.Queue;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Keith Stevens
 */
public class PartOfSpeechDependencyContextGeneratorTest {

    public static final String SINGLE_PARSE = 
        toTabs("1   Mr. _   NNP NNP _   2   NMOD    _   _\n" +
               "2   Holt    _   NNP NNP _   3   SBJ _   _\n" +
               "3   is  _   VBZ VBZ _   0   ROOT    _   _\n" +
               "4   a   _   DT  DT  _   5   NMOD    _   _\n" +
               "5   columnist   _   NN  NN  _   3   PRD _   _\n" +
               "6   for _   IN  IN  _   5   NMOD    _   _\n" +
               "7   the _   DT  DT  _   9   NMOD    _   _\n" +
               "8   Literary    _   NNP NNP _   9   NMOD    _   _\n" +
               "9   Review  _   NNP NNP _   6   PMOD    _   _\n" +
               "10  in  _   IN  IN  _   9   ADV _   _\n" +
               "11  London  _   NNP NNP _   10  PMOD    _   _\n" +
               "12  .   _   .   .   _   3   P   _   _");

    @Test public void testGenerate() throws Exception {
        DependencyExtractor extractor = new CoNLLDependencyExtractor();
        DependencyTreeNode[] tree = extractor.readNextTree(
                new BufferedReader(new StringReader(SINGLE_PARSE)));
        DependencyContextGenerator generator =
            new PartOfSpeechDependencyContextGenerator(
                    new MockOrderBasis(), 5);
        SparseDoubleVector result = generator.generateContext(tree, 4);
        assertTrue(result.length() >= 9);
        for (int i = 0; i < 9; ++i)
            assertEquals(1, result.get(i), .00001);
    }

    class MockOrderBasis extends AbstractBasisMapping<String, String> {

        private static final long serialVersionUID = 1L;

        public int getDimension(String key) {
            System.out.println(key);
            if (key.equals("a-DT"))
                return 0;
            if (key.equals("is-VBZ"))
                return 1;
            if (key.equals("Holt-NNP"))
                return 2;
            if (key.equals("Mr.-NNP"))
                return 3;

            if (key.equals("for-IN"))
                return 4;
            if (key.equals("the-DT"))
                return 5;
            if (key.equals("Literary-NNP"))
                return 6;
            if (key.equals("Review-NNP"))
                return 7;
            if (key.equals("in-IN"))
                return 8;
            return -1;
        }
    }

    static String toTabs(String doc) {
        StringBuilder sb = new StringBuilder();
        String[] arr = doc.split("\n");
        for (String line : arr) {
            String[] cols = line.split("\\s+");
            for (int i = 0; i < cols.length; ++i) {
                sb.append(cols[i]);
                if (i + 1 < cols.length)
                    sb.append('\t');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
