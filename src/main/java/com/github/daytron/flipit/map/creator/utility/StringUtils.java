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
package com.github.daytron.flipit.map.creator.utility;

/**
 * String utility class for formatting Strings.
 * @author Ryan Gilera ryangilera@gmail.com
 */
public final class StringUtils {

    private StringUtils() {
    }
    
    /**
     * Capitalize only the first letter of a given string.
     * @param word The string text to capitalize.
     * @return A formatted <code>String</code> with the first letter capitalise.
     */
    public static String capitalizeFirstLetterWord(String word) {
        if (word == null) {
            return null;
        }
        
        if (word.isEmpty()) {
            return word;
        }
        
        if (word.length() == 1) {
            return word.toUpperCase();
        }
        
        String newWord = word.trim();
        
        return newWord.substring(0, 1).toUpperCase() + newWord.substring(1);
    }
    
     /**
     * Capitalize the first letter of each word in a series of words 
     * separated by a space character.
     * @param sentence The text to capitalize.
     * @return A formatted <code>String</code> object with each first 
     * letter of each word is capitalized.
     */
    public static String capitalizeFirstLetterEachWord(String sentence) {
        if (sentence == null) {
            return null;
        }
        
        if (sentence.isEmpty()) {
            return sentence;
        }
        
        sentence = sentence.trim();
        
        if (sentence.length() == 1) {
            return sentence.toUpperCase();
        }
        
        // Capitalize first word first
        String firstLetterWordFormatted = capitalizeFirstLetterWord(sentence);

        // Converts all first letter of words separated by space to uppercase
        for (int i = 0; i < sentence.length(); i++) {
            if (sentence.charAt(i) == ' ') {
                firstLetterWordFormatted = firstLetterWordFormatted
                        .substring(0, i + 1)
                        + firstLetterWordFormatted.substring(i + 1, i + 2)
                        .toUpperCase()
                        + firstLetterWordFormatted.substring(i + 2);
            }
        }

        // Get back the output string reference
        return firstLetterWordFormatted;
    }

}
