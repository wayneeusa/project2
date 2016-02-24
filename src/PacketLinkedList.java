
/**
 * A Single-linked linkedlist with a "dumb" header node (no data in the node), but
 * without a tail node. It implements ListADT<E> and returns
 * PacketLinkedListIterator when requiring a iterator.
 */
public class PacketLinkedList<E> implements ListADT<E> {
    // TODO: Add your fields here.
    //       Please see ListADT for detailed javadoc
    //
    private Listnode <E> head;
    private int numitems;

    /**
     * Constructs a empty PacketLinkedList
     */
    public PacketLinkedList() {
        // TODO
        head = new Listnode <E> (null);
        numitems = 0;
    }

    @Override
    public void add(E item) {
        // TODO
        Listnode <E> newnode = new Listnode <E> (item);
        Listnode <E> curr = head;
        while (curr.getNext() != null){
            curr = curr.getNext();
        }
        curr.setNext(newnode);

        if (item == null){
            //curr.setNext(null);
            throw new IllegalArgumentException();
        }
        this.numitems++;
    }

    @Override
    public void add(int pos, E item) {
        // TODO
        if (item == null){
            throw new IllegalArgumentException();
        }
        if (pos < 0 || pos > numitems){
            throw new IndexOutOfBoundsException();
        }
        Listnode <E> newnode = new Listnode <E> (item);
        Listnode <E> curr = head;
        for (int i = 0; i < pos - 1; i++){
            curr = curr.getNext();
        }
        this.numitems++;
        newnode.setNext(curr.getNext());
        curr.setNext(newnode);
    }

    @Override
    public boolean contains(E item) {
        // TODO: replace the default return statement
        Listnode <E> curr = head;
        if (curr.getData() == item || curr.getData().equals(item)){
            return true;
        } else{
            return false;
        }
    }

    @Override
    public E get(int pos) {
        // TODO: replace the default return statement
        if (pos < 0 || pos - 1 >= numitems){
            throw new IndexOutOfBoundsException();
        }
        Listnode <E> curr = head;
        for (int i = 0; i < pos; i++){
            curr = curr.getNext();
        }
        return curr.getData();
    }

    @Override
    public boolean isEmpty() {
        // TODO: replace the default return statement
        if (this.numitems == 0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public E remove(int pos) {
        // TODO: replace the default return statement
        if (pos < 0 || pos > numitems){
            throw new IndexOutOfBoundsException();
        }
        Listnode <E> curr = head;
        for (int i = 0; i < pos-1; i++){
            curr = curr.getNext();
        }
        curr.setNext(curr.getNext().getNext());
        this.numitems--;
        return curr.getData();
    }

    @Override
    public int size() {
        // TODO: replace the default return statement
        return numitems;
    }

    @Override
    public PacketLinkedListIterator<E> iterator() {
        // TODO: replace the default return statement
        return new PacketLinkedListIterator <E> (head);
    }

}
