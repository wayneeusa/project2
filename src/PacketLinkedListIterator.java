import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The iterator implementation for PacketLinkedList.
 */
public class PacketLinkedListIterator<T> implements Iterator<T> {
    // TODO: add field when needed
    private Listnode <T> listNode;
    private int currPos;
    private int numItems;

    /**
     * Constructs a PacketLinkedListIterator by passing a head node of
     * PacketLinkedList.
     *
     * @param head
     */
    public PacketLinkedListIterator(Listnode<T> head) {
        // TODO

       listNode = head;

       /* int count = 0;
        list = head;
        while (list.getNext() != null){
            count++;
            list = list.getNext();
        }
        list = head;
        numItems = count;
        currPos = 0;*/
    }

    /**
     * Returns the next element in the iteration.
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public T next() {
        //TODO: replace the default return statment

        if(listNode.getNext() == null)
            throw new NoSuchElementException();

        listNode = listNode.getNext();
        return listNode.getData();

        /*if (currPos >= numItems){
            throw new NoSuchElementException();
        }
        T result = null;
        for (int i = 0; i < currPos; i++){
            result = list.getNext().getData();
        }
        currPos ++;
        return result;*/
    }

    /**
     * Returns true if the iteration has more elements.
     * @return true if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        //TODO: replace the default return statment

       return listNode.getNext() != null;
       // return currPos < numItems;
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
