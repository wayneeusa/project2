///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            Program 2
// Files:            Receiver.java, BadImageHeaderException.java,
//					 PacketLinkedList.java, BadImageContentException.java.
//					 PacketLinkedListIterator.java
// Semester:         CS 367 Spring 2016
//
// Author:           Jonathan Santoso
// Email:            jsantoso2@wisc.edu
// CS Login:         santoso
// Lecturer's Name:  Jim Skrentny
// Lab Section:      (your lab section number)
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ////////////////////
//
//                   CHECK ASSIGNMENT PAGE TO see IF PAIR-PROGRAMMING IS ALLOWED
//                   If pair programming is allowed:
//                   1. Read PAIR-PROGRAMMING policy (in cs302 policy)
//                   2. choose a partner wisely
//                   3. REGISTER THE TEAM BEFORE YOU WORK TOGETHER
//                      a. one partner creates the team
//                      b. the other partner must join the team
//                   4. complete this section for each program file.
//
// Pair Partner:     Wayne Eternicka
// Email:            wayne@badgers.me
// CS Login:         eternicka
// Lecturer's Name:  Deb Deppeler
// Lab Section:      (your partner's lab section number)
//
//////////////////// STUDENTS WHO GET HELP FROM OTHER THAN THEIR PARTNER //////

import java.io.IOException;
import java.util.Collections;

/**
 * The main class. It simulates a application (image viewer) receiver by
 * maintaining a list buffer. It collects packets from the queue of
 * InputDriver and arrange them properly, and then reconstructs the image
 * file from its list buffer.
 */
public class Receiver {
    private InputDriver input;
    private ImageDriver img;
    private PacketLinkedList<SimplePacket> list;

    /**
     * Constructs a Receiver to obtain the image file transmitted.
     * @param file the filename you want to receive
     */
    public Receiver(String file) {
        try {
            input = new InputDriver(file, true);
        } catch (IOException e) {
            System.out.println(
                    "The file, " + file + ", isn't existed on the server.");
            System.exit(0);
        }
        img = new ImageDriver(input);
        // TODO: properly initialize your field
        // initializes list to new PacketLinkedlist
        list = new PacketLinkedList<SimplePacket>();
    }

    /**
     * Returns the PacketLinkedList buffer in the receiver
     *
     * @return the PacketLinkedList object
     */
    public PacketLinkedList<SimplePacket> getListBuffer() {
        return list;
    }

    /**
     * Asks for retransmitting the packet. The new packet with the sequence
     * number will arrive later by using {@link #askForNextPacket()}.
     * Notice that ONLY packet with invalid checksum will be retransmitted.
     *
     * @param pkt the packet with bad checksum
     * @return true if the requested packet is added in the receiving queue; otherwise, false
     */
    public boolean askForRetransmit(SimplePacket pkt) {
        return input.resendPacket(pkt);
    }


    /**
     * Asks for retransmitting the packet with a sequence number.
     * The requested packet will arrive later by using
     * {@link #askForNextPacket()}. Notice that ONLY missing
     * packet will be retransmitted.
     *
     * @param seq the sequence number of the requested missing packet
     * @return true if the requested packet is added in the receiving queue; otherwise, false
     */
    public boolean askForMissingPacket(int seq) {


        return input.resendMissingPacket(seq);
    }

    /**
     * Returns the next packet.
     *
     * @return the next SimplePacket object; returns null if no more packet to
     *         receive
     */
    public SimplePacket askForNextPacket() {
        return input.getNextPacket();
    }

    /**
     * Returns true if the maintained list buffer has a valid image content. Notice
     * that when it returns false, the image buffer could either has a bad
     * header, or just bad body, or both.
     *
     * @return true if the maintained list buffer has a valid image content;
     *         otherwise, false
     */
    public boolean validImageContent() {
        return input.validFile(list);
    }

    /**
     * Returns if the maintained list buffer has a valid image header
     *
     * @return true if the maintained list buffer has a valid image header;
     *         otherwise, false
     */
    public boolean validImageHeader() {
        return input.validHeader(list.get(0));
    }

    /**
     * Outputs the formatted content in the PacketLinkedList buffer. See
     * course webpage for the formatting detail.
     */
    public void displayList() {
        // TODO: implement this method firstly to help you debug
        System.out.println("try to display..");
        PacketLinkedListIterator <SimplePacket> DItr = list.iterator();

        // Iterator to iterate through list
        while(DItr.hasNext()) {
            SimplePacket pack = DItr.next();
            // if packet is not valid checksum, it is corrupted and print brackets
            if (!pack.isValidCheckSum()) {
                System.out.print("[" + pack.getSeq() + "]");
            } else {
                System.out.print(pack.getSeq());
            }
            if(DItr.hasNext()) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }
    /**
     * Reconstructs the file by arranging the {@link PacketLinkedList} in
     * correct order. It uses {@link #askForNextPacket()} to get packets until
     * no more packet to receive. It eliminates the duplicate packets and asks
     * for retransmitting when getting a packet with invalid checksum.
     */
    public void reconstructFile() {
        // TODO: Collect the packets and arrange them in order.
        // 		 You can try to collect all packets and put them into your list
        //       without any processing and use implemented displayList() to see
        //       the pattern of packets you are going to receive.
        //       Then, properly handle the invalid checksum and duplicates. The
        //       first image file, "secret0.jpg", would not result in missing
        //       packets into your receiving queue such that you can test it once
        //       you get the first two processing done.

        // TODO: Processing missing packets for the other four images. You should
        //       utilize the information provided by "End of Streaming Notification
        //       Packet" though this special packet could be lost while transmitting.

        SimplePacket temp = askForNextPacket();
        int noOfPacketsNeeded = 0;

        // while askForNextPacket does not equal null, add to list
        while (temp != null) {

            // if checksum is false, ask for retransmit
            if (temp.isValidCheckSum() == false) {
                askForRetransmit(temp);
            } else {

                // if list is empty, add first element at end
                if (list.size() == 0 && temp.isValidCheckSum() == true) {
                    list.add(temp);

                    // if sequence number is negative, check if all elements is complete
                } else if (temp.getSeq() < 0) {
                    // Last packet received here
                    // numPackets needed is the negative packet times -1
                    noOfPacketsNeeded = (temp.getSeq() * -1);
                    if(noOfPacketsNeeded != list.size()) {
                        // check if each packet is sequential
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getSeq() == i+1){
                                continue;
                                // if not, ask for missing packet again
                                // which will later arrive in queue
                            } else{
                                askForMissingPacket(i+1);
                                SimplePacket missing = askForNextPacket();
                                // continue askForNextPacket until it has a valid checksum
                                while (missing.isValidCheckSum() == false){
                                    if(askForRetransmit(missing)){
                                        missing = askForNextPacket();
                                    }
                                }
                                // add to buffer at correct index for missing packets
                                list.add(i,missing);
                            }
                        }
                    }
                } else {
                    // insert_pos is the insertion position in list
                    int insert_pos = -1;
                    for (int i = 0; i < list.size(); i++) {
                        // if next packet sequence number is smaller, add in the front
                        if (temp.getSeq() < list.get(i).getSeq()) {
                            list.add(i, temp);
                            insert_pos = i;
                            break;
                            // if next packet sequence number is the same,
                            // remove the old one and add the new one
                        } else if (temp.getSeq() == list.get(i).getSeq()) {
                            list.add(i, temp);
                            list.remove(i + 1);
                            insert_pos = i;
                            break;
                        }
                    }
                    // add at end if sequence number of next packet is larger than
                    // everything in the list
                    if (insert_pos == -1) {
                        list.add(temp);
                    }
                }
            }
            // continue ask for next packet
            temp = askForNextPacket();
        }

        // if not received the EOS packet
        if (list.size() != list.get(list.size()-1).getSeq()){
            // check if it is sequential, and ask for missing packets and add at
            // proper index
            for (int i = 0; i < list.size(); i++){
                if (list.get(i).getSeq() != i+1){
                    askForMissingPacket(i+1);
                    SimplePacket missing = askForNextPacket();
                    while (missing.isValidCheckSum() == false){
                        if(askForRetransmit(missing)){
                            missing = askForNextPacket();
                        }
                    }
                    list.add(i,missing);
                }
            }
            // handles case for missing EOS packet because askForNextPacket is null
            if (askForNextPacket() == null){
                askForMissingPacket(0);
                SimplePacket Eos = askForNextPacket();
                // ask for EOS packet until it has valid checksum
                while(Eos.isValidCheckSum() == false){
                    askForMissingPacket(0);
                    Eos = askForNextPacket();
                }
                // got EOS packet
                if (Eos.getSeq() < 0){
                }
            }
        }
    }


    /**
     * Opens the image file by merging the content in the maintained list
     * buffer.
     */
    public void openImage() {
        try {
            img.openImage(list);
        }
        // TODO: catch the image-related exception
		/* throws BadImageHeaderException if the maintained list buffer has an
		 * invalid image header, throws BadImageContentException if the
		 * maintained list buffer has an invalid image content*/

        // catch the broken image exception
        catch (BrokenImageException e) {
            // if validImageHeader is false, throw new exception
            if (validImageHeader() == false){
                throw new BadImageHeaderException();
            }
            // if validImageContent is false, throw new exception
            if (validImageContent() == false){
                throw new BadImageContentException();
            }
            System.out.println(
                    "Please catch the proper Image-related Exception.");
            e.printStackTrace();
        }
    }

    //This is to test our PLinkedList and Iterator
    private void check(){
        list.add(new SimplePacket(1, true, null));
        displayList();
    }

    /**
     * Initiates a Receiver to reconstruct collected packets and open the Image
     * file, which is specified by args[0].
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Receiver [filename_on_server]");
            return;
        }
        Receiver recv = new Receiver(args[0]);
        recv.reconstructFile();
        //recv.displayList(); //use for debugging
        recv.openImage();
    }
}
