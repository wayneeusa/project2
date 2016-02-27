///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  Receiver.java
// File:             PacketLinkedList.java
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

/**
 * A Single-linked linkedlist with a "dumb" header node (no data in the node), but
 * without a tail node. It implements ListADT<E> and returns
 * PacketLinkedListIterator when requiring a iterator.
 */
public class PacketLinkedList<E> implements ListADT<E> {
    // TODO: Add your fields here.
    //       Please see ListADT for detailed javadoc

    private Listnode <E> head;
    private int numitems;

    /**
     * Constructs a empty PacketLinkedList
     */
    public PacketLinkedList() {
        // TODO
        // creates header node and initializes numitems
        head = new Listnode <E> (null);
        numitems = 0;
    }

    @Override
    public void add(E item) {
        // TODO
        Listnode <E> newnode = new Listnode <E> (item);
        Listnode <E> curr = head;
        // traverse through list
        while (curr.getNext() != null){
            curr = curr.getNext();
        }
        // add item to end
        curr.setNext(newnode);

        // if item is null, throw new exception
        if (item == null){
            throw new IllegalArgumentException();
        }
        // increment numitems
        this.numitems++;
    }

    @Override
    public void add(int pos, E item) {
        // TODO
        // if item is null, throw new exception
        if (item == null){
            throw new IllegalArgumentException();
        }
        // if position is invalid, throw new exception
        if (pos < 0 || pos > numitems){
            throw new IndexOutOfBoundsException();
        }
        Listnode <E> newnode = new Listnode <E> (item);
        Listnode <E> curr = head;
        // traverse through list
        for (int i = 0; i < pos; i++){
            curr = curr.getNext();
        }
        // increment numitems
        this.numitems++;
        // add item to pos
        newnode.setNext(curr.getNext());
        curr.setNext(newnode);
    }

    @Override
    public boolean contains(E item) {
        // TODO: replace the default return statement
        Listnode <E> curr = head;
        // return true if item is in list, else false
        if (curr.getData() == item || curr.getData().equals(item)){
            return true;
        } else{
            return false;
        }
    }

    @Override
    public E get(int pos) {
        // TODO: replace the default return statement
        // if index is invalid, throw new exception
        if (pos < 0 || pos > numitems){
            throw new IndexOutOfBoundsException();
        }
        Listnode <E> curr = head;
        // traverse
        for (int i = 0; i < pos+1 ; i++){
            curr = curr.getNext();
        }
        return curr.getData();
    }

    @Override
    public boolean isEmpty() {
        // TODO: replace the default return statement
        // if list is empty, return true. else return false
        if (this.numitems == 0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public E remove(int pos) {
        // TODO: replace the default return statement
        // if index is invalid, throw new exception
        if (pos < 0 || pos > numitems){
            throw new IndexOutOfBoundsException();
        }
        Listnode <E> curr = head;
        // traverse
        for (int i = 0; i < pos; i++){
            curr = curr.getNext();
        }
        // remove item and set prev item to the next next item
        // decrement numitems
        curr.setNext(curr.getNext().getNext());
        this.numitems--;
        return curr.getData();
    }

    @Override
    public int size() {
        // TODO: replace the default return statement
        // returns size of list
        return numitems;
    }

    @Override
    public PacketLinkedListIterator<E> iterator() {
        // TODO: replace the default return statement
        // return new iterator
        return new PacketLinkedListIterator <E> (head);
    }

}
