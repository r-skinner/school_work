class PageTable {
    private PageTableEntry[] pageTable = new PageTableEntry[OperatingSystem.NUMBER_OF_PAGES];

    void setEntry(int pageNumber, boolean valid, boolean reference, boolean dirty, int frameNumber){
        pageTable[pageNumber] = new PageTableEntry(valid, reference, dirty, frameNumber);
    }

    PageTableEntry getEntry(int pageNumber){
        return  pageTable[pageNumber];
    }

    class PageTableEntry{
        private boolean valid;
        private boolean referenced;
        private boolean dirty;
        private int frameNumber;

        PageTableEntry(boolean valid, boolean referenced, boolean dirty, int frameNumber) {
            this.valid = valid;
            this.referenced = referenced;
            this.dirty = dirty;
            this.frameNumber = frameNumber;
        }

        boolean isValid() {
            return valid;
        }

        int getFrameNumber() {
            return frameNumber;
        }

        boolean isDirty() {
            return dirty;
        }


        boolean isReferenced() {
            return referenced;
        }

        void setReferenced(boolean referenced) {
            this.referenced = referenced;
        }
    }
}
