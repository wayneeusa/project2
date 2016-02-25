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

        while(DItr.hasNext()) {
            SimplePacket pack = DItr.next();
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
    /*for (int i = 0; i < list.size(); i++){
            if (list.get(i) == null){
                continue;
            }else{
                if (list.get(i).isValidCheckSum() == false){
                    System.out.print("[" + list.get(i).getSeq() + "] ,");
                }else {
                    System.out.print(list.get(i).getSeq() + ", ");
                }
            }
        }
        System.out.println();
    }*/

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
        SimplePacket temp = askForNextPacket();
        int noOfPacketsNeeded = 0;
        boolean boolReady = false;
        while (temp != null) {
            if (temp.isValidCheckSum() == false) {
                askForRetransmit(temp);
            } else {
                if (list.size() == 0) {
                    if(temp.isValidCheckSum()) {
                    list.add(temp); }
                } else if (temp.getSeq() < 0) {   //get sequence number and check for correct number of packets
                    //because EOS packet was received
                    System.out.println("Got last packet");
                       noOfPacketsNeeded = (temp.getSeq() * -1);
                      if(noOfPacketsNeeded != list.size()) {      //// problem with using list.size() ?
                        System.out.println("no of packets not right");

                        for (int i = 1; i <= list.size(); i++) { //check if each packet is sequestial
                            //if not, ask for retransmit
                            if (i == 1) {
                                if (list.get(i).getSeq() == 1) {
                                    continue;
                                } else {
                                    System.out.println("Missing first packet");
                                    askForMissingPacket(1);
                                    SimplePacket missing = askForNextPacket();

                                    while(missing.isValidCheckSum() == false) {
                                        askForRetransmit(missing);
                                        missing = askForNextPacket(); //edited to fix corrupted packages
                                    }


                                    list.add(1, missing);

                                }
                            } else {
                                System.out.println("preparing check to resend packets");
                                if (!(list.get(i).getSeq() == (list.get(i - 1).getSeq() + 1))) {
                                    System.out.println("Asking for packet retransmission");
                                    askForMissingPacket(i);
                                    System.out.println("Got here");
                                    SimplePacket missing = askForNextPacket();

                                    while (missing.isValidCheckSum() == false) {
                                        if(askForRetransmit(missing)){
                                            missing = askForNextPacket();
                                        }
                                    }
                                        list.add(i, missing);

                                    }
                                }

                            }
                            list.add(temp);
                        }

                    } else {
                        int insert_pos = -1;
                        for (int i = 1; i <= list.size(); i++) {
                            if (temp.getSeq() < list.get(i).getSeq()) {
                                list.add(i, temp);
                                insert_pos = i;
                                break;
                            } else if (temp.getSeq() == list.get(i).getSeq()) {

                                list.add(i, temp);
                                list.remove(i + 1);
                                insert_pos = i;
                                break;
                            }
                        }
                        if (insert_pos == -1) {
                            list.add(temp);
                        }
                    }
                }
                temp = askForNextPacket();
            }

            if (list.get(list.size()).getSeq() >= 0) {  //Means the EOS packet was missing

                boolean inQue = false;
                while (!inQue) {
                    inQue = askForMissingPacket(0);
                }

                System.out.println("was missing EOS packet");
                SimplePacket missing2 = askForNextPacket();

                while( missing2.isValidCheckSum() == false || (missing2 == null)){
                    askForRetransmit(missing2);
                    missing2 = askForNextPacket();
                }

                list.add(missing2);

                System.out.println("Got last packet");
                  noOfPacketsNeeded = (missing2.getSeq() * -1);
                if (noOfPacketsNeeded != list.size()) {
                    System.out.println("no of packets not right");

                    for (int i = 1; i <= list.size(); i++) { //check if each packet is sequestial
                        //if not, ask for retransmit
                        if (i == 1) {
                            if (list.get(i).getSeq() == 1) {
                                continue;
                            } else {
                                askForMissingPacket(1);
                                SimplePacket missing = askForNextPacket();

                                while(missing.isValidCheckSum() == false) {
                                    askForRetransmit(missing);
                                    missing = askForNextPacket(); //edited to fix corrupted packages
                                }


                                list.add(1, missing);

                            }
                        } else { //Where we check the sequence of packets for missing packets after the
                            //case when the EOS packet was missing
                            System.out.println("preparing check to resend packets");
                            if (!(list.get(i).getSeq() == (list.get(i - 1).getSeq() + 1))) {
                                System.out.println("Asking for packet retransmission");

                               // boolean boolReady = false;

                                while(!boolReady) {
                                    boolReady = askForMissingPacket(i); //Think it's getting hung here

                                }
                                boolReady = false;
                                System.out.println("Got here");
                                SimplePacket missing = askForNextPacket();

                                while (missing.isValidCheckSum() == false) { //error here NullPointer
                                    if(askForRetransmit(missing)){
                                        missing = askForNextPacket();
                                        if(missing.isValidCheckSum()){
                                            break;
                                        }
                                    }

                              /*  System.out.println("Got here");

                                boolean boolReady2 = false;
                                while(!boolReady2){
                                    boolReady2 = askForMissingPacket(i);

                                }*/
                             //   System.out.println("Right after boolReady statement");
                             //   SimplePacket missing = askForNextPacket();

                             /*   while (missing.isValidCheckSum() == false) {
                                    if(askForRetransmit(missing)){
                                        missing = askForNextPacket();*/
                                    }

                                list.add(i, missing);
                                }


                            }
                        //}

                    }
                    list.add(temp);
                }

            }


            System.out.println("list size: " + list.size());
            displayList();

            // TODO: Processing missing packets for the other four images. You should
            //       utilize the information provided by "End of Streaming Notification
            //       Packet" though this special packet could be lost while transmitting.

            //
        //if(noOfPacketsNeeded != list.size()){
          //  reconstructFile();
        //}
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
        catch (Exception e) {
            System.out.println(
                    "Please catch the proper Image-related Exception.");
            e.printStackTrace();
            // MAY be wrong?
            if (validImageHeader() == false){
                throw new BadImageHeaderException();
            }
            if (validImageContent() == false){
                throw new BadImageContentException();
            }
        }
    }

    private void check(){   //This is to test our PLinkedList and Iterator
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
        //recv.check();
        //recv.openImage();
    }
}
