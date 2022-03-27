
class OperatingSystem {
    private static final int CPU_ADDRESS_WIDTH = 16;
    private static final int PAGE_OFFSET_WIDTH = 8;
    private static final int R_BIT_RESET_COUNT = 10;
    private static final int PAGE_NUMBER_WIDTH = CPU_ADDRESS_WIDTH - PAGE_OFFSET_WIDTH;
    static final int NUMBER_OF_PAGES = (int) Math.pow(2,PAGE_NUMBER_WIDTH);
    static final int PAGE_SIZE = (int) Math.pow(2,(PAGE_OFFSET_WIDTH));

    enum InstructionType{READ, WRITE}

    private int instructionCount = 0;

    private Clock clock = new Clock();
    private Storage storage = new Storage();
    private MMU mmu;


    OperatingSystem(){
        this.mmu = new MMU(this);
    }

    int[] execute(Instruction instruction){
        instructionCount++;
        if(instructionCount % R_BIT_RESET_COUNT == 0) clockInterrupt();
        int[] out = mmu.accessMemory(instruction.getInstructionType(), instruction.getAddress(), instruction.getWriteValue());

        return new int[]{instruction.getAddress(),
                instruction.getInstructionType() == InstructionType.READ ? 0 : 1,
                out[0], out[1], out[2], out[3], out[4], out[5]};
    }

    private void clockInterrupt(){
            mmu.clockInterrupt();
    }

    int[] trap(int pageNumber){
        int[] evictableFrameNumberAndReplace = findEvictableFrameNumber();
        int evictableFrameNumber = evictableFrameNumberAndReplace[0];
        boolean replacePage = (evictableFrameNumberAndReplace[1] == 1);
        return insertFrame(pageNumber, evictableFrameNumber, replacePage);
    }

    private int[] findEvictableFrameNumber() {
        int replacePage = (clock.size < Memory.PHYSICAL_FRAME_CAPACITY) ? 0 : 1;
        int evictableFrameNumber = clock.getEvicatbleFrame();
        return new int[]{evictableFrameNumber,replacePage};
    }

    private int[] insertFrame(int pageNumber, int evictableFrameNumber, boolean replacePage){
        MMU.TLBEntry oldEntry = mmu.getTlbEntryFromFrameNumber(evictableFrameNumber);

        int dirty = 0, evictedPageNumber = -1;

        if(oldEntry != null ){
            evictedPageNumber = oldEntry.getPageNumber();
            if (replacePage && oldEntry.isValid() && oldEntry.isDirty()) {
                storage.writePageToSwap(oldEntry.getPageNumber(), mmu.readFrameFromRAM(evictableFrameNumber));
                mmu.setDirty(oldEntry.getPageNumber(),false);
                dirty = 1;
            }
            mmu.setValid(oldEntry.getPageNumber(),false);
        }
        mmu.writePageToRAM(pageNumber, evictableFrameNumber, storage.loadPage(pageNumber));
        clock.add(evictableFrameNumber);
        return new int[]{evictedPageNumber, dirty};

    }

    class Clock{
        ClockNode head, tail, pointer;
        int size;
        Clock(){
            head = null;
            tail = null;
            size = 0;
        }

        void add(int frameNumber){
            ClockNode node = new ClockNode(frameNumber, head);
            if(size == 0) {
                head = node;
                node.next = head;
                tail = head;
                pointer = head;
                size++;
            }
            else if(size < Memory.PHYSICAL_FRAME_CAPACITY){
                tail.next = node;
                tail = node;
                size++;
            }
            else pointer.frameNumber = frameNumber;
        }

        int getEvicatbleFrame(){
            if (size < Memory.PHYSICAL_FRAME_CAPACITY) return size;

            MMU.TLBEntry tlbEntry = mmu.getTlbEntryFromFrameNumber(pointer.frameNumber);
            while(tlbEntry.isReferenced()){
                mmu.setReferenced(tlbEntry.getPageNumber(),false);
                pointer = pointer.next;
                tlbEntry = mmu.getTlbEntryFromFrameNumber(pointer.frameNumber);
            }
            return pointer.frameNumber;
        }

        class ClockNode{
            int frameNumber;
            ClockNode next;

            ClockNode(int frameNumber, ClockNode next){
                this.frameNumber = frameNumber;
                this.next = next;
            }
        }
    }
}

