///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  Receiver.java
// File:             PacketLinkedListIterator.java
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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The iterator implementation for PacketLinkedList.
 */
public class PacketLinkedListIterator<T> implements Iterator<T> {
    // TODO: add field when needed
    private Listnode <T> listNode;

    /**
     * Constructs a PacketLinkedListIterator by passing a head node of
     * PacketLinkedList.
     *
     * @param head
     */
    public PacketLinkedListIterator(Listnode<T> head) {
        // TODO
    	// set listnode to head
       listNode = head;
    }

    /**
     * Returns the next element in the iteration.
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public T next() {
        //TODO: replace the default return statment
    	
    	// if next element in listnode is null, throw new exception
        if(listNode.getNext() == null){
            throw new NoSuchElementException();
        }
        // return next element in iteration
        listNode = listNode.getNext();
        return listNode.getData();

    }

    /**
     * Returns true if the iteration has more elements.
     * @return true if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        //TODO: replace the default return statment
    	// return if iteration has more elements
       return listNode.getNext() != null;
    }

    /**
     * The remove operation is not supported by this iterator
     * @throws UnsupportedOperationException if the remove operation is not supported by this iterator
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
