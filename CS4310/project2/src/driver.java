import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class driver {
    public static void main(String[] args) throws Exception {

        //args = new String[]{"out/production/p2/test_files/test_1.txt"};

        if (args.length != 1) {
            throw new Exception("Invalid input.\nTry 'java driver {inputdata.txt}'");
        }

        FileWriter fileWriter = new FileWriter("output_" +
                args[0].replace('/', '_').replace('\\', '_') .replace(':', '_') + ".csv");
        fileWriter.write("Address, r/w, value, soft, hard, hit, evicted_pg#, dirty_evicted_page\n");

        OperatingSystem os = new OperatingSystem();

        Scanner scanner = new Scanner(new File(args[0]));
        while (scanner.hasNextInt()) {
            int instructionType = scanner.nextInt();
            Instruction instruction;
            switch (instructionType) {
                case 0:
                    instruction = new Instruction(scanner.nextInt(16));
                    break;
                case 1:
                    instruction = new Instruction(scanner.nextInt(16), scanner.nextInt());
                    break;
                default:
                    throw new Exception("Invalid instruction type: " + instructionType);
            }
            int[] out = os.execute(instruction);

            String evicted = (out[6] == -1) ? "-1" : Integer.toHexString(out[6]).toUpperCase();
            if(evicted.length() == 1) evicted = "0" + evicted;

            String address = Integer.toHexString(out[0]).toUpperCase();
            if(evicted.length() == 3) address = "0" + address;

            fileWriter.write( address+ "," +
                    out[1] + "," + out[2] + "," + out[3] + "," + out[4] + "," + out[5] + "," +
                    evicted + "," + out[7] + "\n");
        }
        System.out.println("Output printed to output_" +
                args[0].replace('/', '_').replace('\\', '_') + ".csv");

        scanner.close();
        fileWriter.close();
    }
}
class Instruction{
    private OperatingSystem.InstructionType instructionType;
    private int address;
    private int writeValue;

    Instruction(int address){
        this.instructionType = OperatingSystem.InstructionType.READ;
        this.address = address;
        this.writeValue = -1;
    }

    Instruction(int address, int writeValue){
        this.instructionType = OperatingSystem.InstructionType.WRITE;
        this.address = address;
        this.writeValue = writeValue;
    }

    OperatingSystem.InstructionType getInstructionType() {
        return instructionType;
    }

    int getAddress() {
        return address;
    }

    int getWriteValue() {
        return writeValue;
    }

    @Override
    public String toString() {
        return (instructionType == OperatingSystem.InstructionType.READ ? "Read from " : "Write " + writeValue + " to ") + address;
    }
}