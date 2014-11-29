/*
 * The MIT License
 *
 * Copyright 2014 Ryan Gilera.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.daytron.flipit.map.creator;

import com.github.daytron.flipit.map.creator.utility.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.experimental.categories.Category;

/**
 *
 * @author Ryan Gilera ryangilera@gmail.com
 */
@Category({AllTest.class})
public class StringUtilsTest {

    public StringUtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of capitalizeFirstLetterWord method, of class StringUtils.
     */
    @Test
    public void testCapitalizeFirstLetterWord() {
        System.out.println("Test: capitalizeFirstLetterWord(String)");

        // given:        
        String[] listOfStringsInput = new String[]{
            "hello", // 1
            "HELLO", // 2
            "hELLO", // 3
            " hello", // 4
            " h e l l o", // 5
            "", // 6
            null, // 7
            "         hello     ", // 8
            "h"}; // 9

        String[] listOfExpectedResults = new String[]{
            "Hello", // 1
            "HELLO", // 2
            "HELLO", // 3
            "Hello", // 4
            "H e l l o", // 5
            "", // 6
            null, // 7
            "Hello", // 8
            "H"}; // 9

        for (int i = 0; i < listOfExpectedResults.length; i++) {
            // when
            String result = StringUtils.capitalizeFirstLetterWord(
                    listOfStringsInput[i]);

            // then:
            assertEquals(listOfExpectedResults[i], result);
            System.out.println("scenario: " + (i + 1) + ", result: " + result + ", expected: " + listOfExpectedResults[i]);

        }
    }

    /**
     * Test of capitalizeFirstLetterEachWord method, of class StringUtils.
     */
    @Test
    public void testCapitalizeFirstLetterEachWord() {
        System.out.println("Test: capitalizeFirstLetterEachWord(String)");
        
        // given:        
        String[] listOfStringsInput = new String[]{
            "hello World", // 1
            "HELLO THERE!", // 2
            "hELLO wORLD", // 3
            " hello   world.", // 4
            " h e l l o        ", // 5
            "", // 6
            null, // 7
            "h"}; // 8
        
        String[] listOfExpectedResults = new String[]{
            "Hello World", // 1
            "HELLO THERE!", // 2
            "HELLO WORLD", // 3
            "Hello   World.", // 4
            "H E L L O", // 5
            "", // 6
            null, // 7
            "H"}; // 8
        
        for (int i = 0; i < listOfExpectedResults.length; i++) {
            // when
            String result = StringUtils.capitalizeFirstLetterEachWord(
                    listOfStringsInput[i]);

            // then:
            assertEquals(listOfExpectedResults[i], result);
            System.out.println("scenario: " + (i + 1) + ", result: " + result + ", expected: " + listOfExpectedResults[i]);
        }
    }

}
