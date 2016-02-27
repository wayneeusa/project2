///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  Receiver.java
// File:             BadImageContentException.java
// Semester:         CS 367 Spring 2016
//
// Author:           Jonathan Santoso, jsantoso2@wisc.edu
// CS Login:         santoso
// Lecturer's Name:  Jim Skrentny
// Lab Section:      (your lab section number)
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ////////////////////
//
// Pair Partner:     Wayne Eternicka
// Email:            wayne@badgers.me
// CS Login:         eternicka
// Lecturer's Name:  Deb Deppeler
// Lab Section:      (your partner's lab section number)
//
//////////////////// STUDENTS WHO GET HELP FROM OTHER THAN THEIR PARTNER //////

public class BadImageContentException extends RuntimeException{
    /**
     * Construct an instance of this exception with a custom message
     *
     * @param message the custom message to be wrapped around by this exception
     */
    public BadImageContentException(String message){
        super(message);
    }

    /**
     * Construct an instance of this exception without any message
     */
    public BadImageContentException(){
        super();
    }
}
//