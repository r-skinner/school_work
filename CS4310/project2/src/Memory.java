class Memory {
    private static final int ADDRESS_WIDTH = 12;
    private static final int NUMBER_OF_RAM_ADDRESSES = (int) Math.pow(2, ADDRESS_WIDTH);
    static final int PHYSICAL_FRAME_CAPACITY = NUMBER_OF_RAM_ADDRESSES / OperatingSystem.PAGE_SIZE;
    private int[][] ram;

    Memory(){
        ram = new int[PHYSICAL_FRAME_CAPACITY][OperatingSystem.PAGE_SIZE];
    }

    int[] readFrame(int frameNumber) {
        return ram[frameNumber];
    }

    void writePageToFrame(int[] page, int frameNumber) {
        ram[frameNumber] = page;
    }
}
