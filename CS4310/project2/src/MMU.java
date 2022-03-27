class MMU {
    private static final int TLB_SIZE = 16;
    private int pointer;

    private TLBEntry[] TLB;

    private OperatingSystem operatingSystem;
    private PageTable pageTable;
    private Memory memory;

    MMU(OperatingSystem operatingSystem){
        this.pointer = 0;
        this.TLB = new TLBEntry[TLB_SIZE];
        this.operatingSystem = operatingSystem;
        this.memory = new Memory();
        this.pageTable = new PageTable();
    }


    int[] accessMemory(OperatingSystem.InstructionType instructionType, int address, int writeValue){
        int pageNumber = address / OperatingSystem.PAGE_SIZE;
        int offset = address % OperatingSystem.PAGE_SIZE;
        TLBEntry tlbEntry = getTLBEntryFromPageNumber(pageNumber);
        int value, softMiss = 0, hardMiss = 0, hit = 1;
        int[] out = new int[]{-1,-1};


        //fault
        if(tlbEntry == null){
            hit = 0;
            PageTable.PageTableEntry pageTableEntry = pageTable.getEntry(pageNumber);
            if (pageTableEntry == null || !pageTableEntry.isValid()) {
                hardMiss = 1;
                out = operatingSystem.trap(pageNumber);
                tlbEntry = getTLBEntryFromPageNumber(pageNumber);
            }
            else {
                softMiss = 1;
                tlbEntry = loadPageTableEntryToTLB(pageNumber, pageTableEntry);
            }
        }

        int frameNumber = tlbEntry.getFrameNumber();

        if(instructionType == OperatingSystem.InstructionType.WRITE){
            memory.readFrame(frameNumber)[offset] = writeValue;
            value = writeValue;
            setDirty(pageNumber,true);
        }
        else{
            value = memory.readFrame(frameNumber)[offset];
        }
        setReferenced(pageNumber,true);

        return new int[]{value, softMiss, hardMiss, hit, out[0], out[1]};
    }



    int[] readFrameFromRAM(int frameNumber) {
        return memory.readFrame(frameNumber);
    }

    void writePageToRAM(int pageNumber, int frameNumber, int[] page) {
        memory.writePageToFrame(page, frameNumber);
        if(TLB[pointer] != null) TLB[pointer].copyToPageTable();

        newTlbEntry(pageNumber, frameNumber);
    }

    private void newTlbEntry(int pageNumber, int frameNumber){
        TLB[pointer] = new TLBEntry(pageNumber, true, false, false, frameNumber);
        pointer = (pointer + 1) % TLB_SIZE;
    }

    void clockInterrupt() {
        for(TLBEntry entry: TLB){
            if(entry != null) {
                entry.setReferenced(false);
                entry.copyToPageTable();
            }
        }
        for(int pageNumber = 0; pageNumber < OperatingSystem.NUMBER_OF_PAGES; pageNumber++){
            if(pageTable.getEntry(pageNumber) != null) pageTable.getEntry(pageNumber).setReferenced(false);
        }
    }

    TLBEntry getTlbEntryFromFrameNumber(int frameNumber) {
        for(TLBEntry tlbEntry: TLB){
            if(tlbEntry != null && tlbEntry.isValid() && tlbEntry.getFrameNumber() == frameNumber) {
                return tlbEntry;
            }
        }
        return loadOnSoftMissFromFrameNumber(frameNumber);
    }

    private TLBEntry getTLBEntryFromPageNumber(int pageNumber) {
        for(TLBEntry entry: TLB){
            if(entry != null && entry.pageNumber == pageNumber && entry.isValid()) return  entry;
        }
        return loadOnSoftMissFromPageNumber(pageNumber);
    }

    private TLBEntry loadOnSoftMissFromFrameNumber(int frameNumber) {
        for(int pageNumber = 0; pageNumber < OperatingSystem.PAGE_SIZE; pageNumber ++) {
            PageTable.PageTableEntry pageTableEntry = pageTable.getEntry(pageNumber);
            if(pageTableEntry != null && pageTableEntry.isValid() && pageTableEntry.getFrameNumber() == frameNumber){
                return loadPageTableEntryToTLB(pageNumber, pageTableEntry);
            }
        }
        return null;
    }
    private TLBEntry loadOnSoftMissFromPageNumber(int pageNumber) {
        PageTable.PageTableEntry pageTableEntry = pageTable.getEntry(pageNumber);
        if(pageTableEntry != null && pageTableEntry.isValid()){
            return loadPageTableEntryToTLB(pageNumber, pageTableEntry);
        }
        return null;
    }

    private TLBEntry loadPageTableEntryToTLB(int pageNumber, PageTable.PageTableEntry pageTableEntry) {
        TLBEntry tlbEntry = new TLBEntry(pageNumber, pageTableEntry);
        TLB[pointer].copyToPageTable();
        TLB[pointer] = tlbEntry;
        pointer = (pointer + 1) % TLB_SIZE;
        return tlbEntry;
    }

    void setValid(int pageNumber, boolean valid) {
        TLBEntry tlbEntry = getTLBEntryFromPageNumber(pageNumber);
        if(tlbEntry == null) {
            tlbEntry = loadOnSoftMissFromPageNumber(pageNumber);
            if(tlbEntry == null){
                return;
            }
        }
        tlbEntry.setValid(valid);
    }


    void setReferenced(int pageNumber, boolean referenced) {
        TLBEntry tlbEntry = getTLBEntryFromPageNumber(pageNumber);
        if(tlbEntry == null) {
            tlbEntry = loadOnSoftMissFromPageNumber(pageNumber);
            if(tlbEntry == null){
                return;
            }
        }
        tlbEntry.setReferenced(referenced);
    }

    void setDirty(int pageNumber, boolean dirty) {
        TLBEntry tlbEntry = getTLBEntryFromPageNumber(pageNumber);
        if(tlbEntry == null) {
            tlbEntry = loadOnSoftMissFromPageNumber(pageNumber);
            if(tlbEntry == null){
                return;
            }
        }
        tlbEntry.setDirty(dirty);
    }

    class TLBEntry {
        private int pageNumber;
        private boolean valid;
        private boolean referenced;
        private boolean dirty;
        private int frameNumber;

        TLBEntry(int pageNumber, boolean valid, boolean referenced, boolean dirty, int frameNumber){
            this.pageNumber = pageNumber;
            this.valid = valid;
            this.referenced = referenced;
            this.dirty = dirty;
            this.frameNumber = frameNumber;
        }

        TLBEntry(int pageNumber, PageTable.PageTableEntry pageTableEntry) {
            this.pageNumber = pageNumber;
            this.valid = pageTableEntry.isValid();
            this.referenced = pageTableEntry.isReferenced();
            this.dirty = pageTableEntry.isDirty();
            this.frameNumber = pageTableEntry.getFrameNumber();
        }

        boolean isValid() {
            return valid;
        }

        void setValid(boolean valid) {
            this.valid = valid;
        }

        boolean isReferenced() {
            return referenced;
        }

        void setReferenced(boolean referenced) {
            this.referenced = referenced;
        }

        boolean isDirty() {
            return dirty;
        }

        void setDirty(boolean dirty) {
            this.dirty = dirty;
        }

        int getFrameNumber() {
            return frameNumber;
        }

        int getPageNumber() {
            return pageNumber;
        }

        void copyToPageTable() {
            pageTable.setEntry(pageNumber,valid,referenced,dirty,frameNumber);
        }
    }
}
