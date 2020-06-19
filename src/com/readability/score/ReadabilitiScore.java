package com.readability.score;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ReadabilitiScore {
	    public static void main(String[] args) {
	        File file = new File(args[0]);
	        int wordCount = 0;
	        int sentenceCount = 0;
	        int characterCount = 0;
	        int syllablesCount = 0;
	        double avgWords = 0;
	        double avgSentence = 0;
	        int pollysyllablesCount = 0;
	        String sentance = null;
	        String choice;
	        
	     
	        int scr = 0;
	    	try (Scanner scanner = new Scanner(file)) {
	    	    while (scanner.hasNext()) {
	    	       sentance = scanner.nextLine();
	    	    }
	    	} catch (FileNotFoundException e) {
	    	    System.out.println("No file found");
	    	}
	        System.out.println("The text is:");
	        System.out.println(sentance + ".\n");
	        String[] strng = sentance.split(" ");
	        String[] str = new String[strng.length];
	        for (int j = 0; j < strng.length; j++) {
	            str[j] = strng[j].replaceAll(",", "");
	        	int count = 0;
	        	if(str[j].charAt(str[j].length()-1) == 'e' || str[j].charAt(str[j].length()-1) == 'E') {
	        		for (int k = 0; k < str[j].length() - 1; k++) {
	        			if (syllableCheck(str[j].charAt(k))) {
	        				while(k < str[j].length()-1 && syllableCheck(str[j].charAt(k+1))) {
	        				k++;
	        				}
	        				count++;
	        			}
	        		}
	        	} else {
	        		for (int k = 0; k < str[j].length(); k++) {
	        			if (syllableCheck(str[j].charAt(k))) {
	        				while(k < str[j].length()-1 && syllableCheck(str[j].charAt(k+1))) {
	        				k++;
	        				}
	        				count++;
	        			}
	        		}
	        	}
	        	if(count == 0 && !str[j].matches("\\d+"))
	        	{
	        	count++;	
	        	}
	        	syllablesCount += count; 
	        	if (count > 2) {
	        		pollysyllablesCount += 1;
	        	}
	        }
	         for (int i = 0; i < sentance.length(); i++) {
	            if (sentance.charAt(i) == ' ' || sentance.charAt(i) == '\n' || sentance.charAt(i) == '\t' || i + 1 == sentance.length()) {
	                wordCount++;
	            }
	            if (sentance.charAt(i) != ' ' && sentance.charAt(i) != '\n' && sentance.charAt(i) != '\t') {
	                characterCount++;
	            }
	            if (sentance.charAt(i) == '.' || sentance.charAt(i) == '!' || sentance.charAt(i) == '?' || i + 1 == sentance.length()) {
	            	sentenceCount++;
	            }
	        }
	        System.out.println("Words: " +wordCount+ "\nSentences: " +sentenceCount+ "\nCharacters: " +
	                            characterCount+ "\nSyllables: " +syllablesCount+ "\nPolysyllables: " +pollysyllablesCount);
	        avgWords = (double) characterCount / (double) wordCount * 100;
			avgSentence = (double) sentenceCount / (double) wordCount * 100;
	        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
	        Scanner scanner = new Scanner(System.in);
	        choice = scanner.next();
	        switch (choice) {
	        	case "ARI" :
	        		ARI(wordCount, sentenceCount, characterCount);
	        		break;
	        	case "FK" :
	        		FK(wordCount, sentenceCount, characterCount, syllablesCount);
	        		break;
	        	case "SMOG" :
	        		SMOG(sentenceCount, pollysyllablesCount);
	        		break;
	        	case "CL" :
	        		System.out.println(avgWords);
	        		System.out.println(avgSentence);
	        		CL(avgWords, avgSentence);
	        		break;
	        	case "all" :
	        		double a = ARI(wordCount, sentenceCount, characterCount);
	        		double b = FK(wordCount, sentenceCount, characterCount, syllablesCount);
	        		double c = SMOG(sentenceCount, pollysyllablesCount);
	        		double d = CL(avgWords, avgSentence);
	        		System.out.println("\n\nThis text should be understood in average by " +(a+b+c+d)/4+ " year olds.");
	        		break;
	        }
	        
	    }
	    public static boolean syllableCheck(char ch) {
	   	 	if (ch == 'A' || ch == 'a' || ch == 'E' || ch == 'e' || ch == 'I' || ch == 'i' || ch == 'O' || ch == 'o' || ch == 'U' || ch == 'u' || ch == 'Y' || ch == 'y') {
				return true;
			} else {
				return false;
			}
	    }   
	    public static int FK(int wordCount, int senstanceCount, int characterCount, int syllablesCount) {
	    	double score = 0.39  * ((double) wordCount / (double) senstanceCount) + 11.8 * ((double) syllablesCount / (double) wordCount) - 15.59;
	        int scr = (int) (Math.round(score));
	        int result = 0;
	        if (scr < 14) {
	            result = Integer.parseInt(ageGroup(scr).split("-")[1]);
	        } else {
	            result = Integer.parseInt(ageGroup(scr));
	        }
	        System.out.printf("\nFlesch–Kincaid readability tests: %.2f", score);
	        System.out.print(" (about " +result+ " year olds).");
	        return result;
	    }
	    public static int SMOG(int senstanceCount,int pollysyllablesCount) {
	    	double score = 1.043  * Math.sqrt((double) pollysyllablesCount * 30 / (double) senstanceCount) + 3.1291;
	        int scr = (int) (Math.round(score));
	        int result = 0;
	        if (scr < 14) {
	            result = Integer.parseInt(ageGroup(scr).split("-")[1]);
	        } else {
	            result = Integer.parseInt(ageGroup(scr));
	        }
	        System.out.printf("\nSimple Measure of Gobbledygook: %.2f", score);
	        System.out.print(" (about " +result+ " year olds).");
	        return result;
	    }
	    public static int CL(double avgWords, double avgSentence) {
	    	double score = (0.0588 * avgWords) - (0.296 * avgSentence) - 15.8;
	        int scr = (int) (Math.round(score));
	        int result = 0;
	        if (scr < 14) {
	            result = Integer.parseInt(ageGroup(scr).split("-")[1]);
	        } else {
	            result = Integer.parseInt(ageGroup(scr));
	        }
	        System.out.printf("\nColeman–Liau index: %.2f", score);
	        System.out.print(" (about " +result+ " year olds).");
	        return result;
	    }
	    public static int ARI( int wordCount, int senstanceCount, int characterCount) {
	        double score = 4.71  * ((double) characterCount / (double) wordCount) + 0.5 * ((double) wordCount / (double) senstanceCount) - 21.43;
	        int scr = (int) (Math.round(score));
	        int result = 0;
	        if (scr < 14) {
	            result = Integer.parseInt(ageGroup(scr).split("-")[1]);
	        } else {
	            result = Integer.parseInt(ageGroup(scr));
	        }
	        System.out.printf("\nAutomated Readability Index: %.2f", score);
	        System.out.print(" (about " +result+ " year olds).");
	        return result;
	    }
	    public static String ageGroup(int scr) {
	        if (scr == 1) {
	        	return "5-6";
	        } else if (scr == 2) {
	        	return "6-7";
	        } else if (scr == 3) {
	        	return "7-9";
	        } else if (scr == 13) {
	        	return "18-24";
	        } else if (scr >= 14) {
	        	return "24";
	        } else {
	            return String.format("%d-%d",scr + 5, scr + 6);
	        }
	    }
}
