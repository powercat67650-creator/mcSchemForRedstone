import com.flowpowered.nbt.*;
import com.flowpowered.nbt.stream.NBTOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        BaseConverter baseConverter = new BaseConverter();
        System.out.println("schematic name ? ");
        String name = scanner.nextLine();
        System.out.println("How many numbers in rom ?");
        int numberCount = scanner.nextInt();
        double[] number = new double[numberCount];
        System.out.println("number or function (\"n\" or \"f\")? ");
        Scanner secScanner = new Scanner(System.in);
        String selection = secScanner.nextLine();
        if (selection.contains("f")){
            System.out.println("you NEED to put space between arguments !!");
            System.out.println("for the list of format or additional information, please check the github repository!");
            String functionInput = secScanner.nextLine();
            System.out.println("start ?");
            double start = secScanner.nextDouble();
            System.out.println("step ?");
            double step = secScanner.nextDouble();
            for (int i = 0; i < numberCount; i++) {
                number[i] = new Function(functionInput).solveFor( i * step + start);
            }
        }else if(selection.contains("n")) {
            for (int i = 0; i < numberCount; i++) {
                System.out.println("number " + (i + 1) + " ?");
                number[i] = scanner.nextDouble();
            }
        }else throw new Exception("no or not valid selection");
        System.out.println("Binary (\"b\") or other base (\"o\") ?");
        int base;
        String mode = "b";
        Scanner test = new Scanner(System.in);
        String baseSelection = test.nextLine();
        if(baseSelection.contains("b")){
            base = 2;
            System.out.println("Use repeaters (\"r\"), barrels (\"b\") or levers (\"l\") ? ");
            String selectedMode = test.nextLine();
            if(selectedMode.contains("r")) mode = "r";
            else if (selectedMode.contains("l")) mode = "l";
            else if (selectedMode.contains("b")) mode = "b";
            else throw new Exception("no or not valid selection");
        }else if (baseSelection.contains("o")){
            System.out.println("Base ? (3-16)");
            base = test.nextInt() % 17;
            if(base < 2){
                base = 16;
                System.out.println("carful invalid base");
            }
        }else throw new Exception("no or not valid selection");
        System.out.println("fixe point format : rigid (\"ri\") or automatic (\"auto\") ? ");
        int size;
        int integerSize;
        int fractionalSize;
        Scanner idk = new Scanner(System.in);
        String pointSelection = idk.nextLine();
        if(pointSelection.contains("ri")) {
            System.out.println("Integer size ? ");
            integerSize = test.nextInt();
            System.out.println("Fractional size ? ");
            fractionalSize = test.nextInt();
            size = integerSize + fractionalSize;
        }else if(pointSelection.contains("auto")){
            System.out.println("Size ? ");
            size = test.nextInt();
            int max = 1;
            for (double v : number) {
                if (v > max) max = (int) Math.round(v);
            }
            integerSize = baseConverter.size(max,base);
            fractionalSize = size - integerSize;
        }else throw new Exception("no or not valid selection");

        int Z = 1;
        int X = numberCount * 2;
        int Y = size * 2;
        CompoundMap palette = new CompoundMap();
        CompoundMap blocksContent = new CompoundMap();
        CompoundMap content = new CompoundMap();
        if(mode.contains("r")){
            System.out.println("facing ? (\"north\" or \"south\")");
            String direction = idk.nextLine();
            if(direction.isEmpty())throw new Exception("error direction is empty");
            palette.put("minecraft:repeater[facing="+direction+",delay=1,locked=false]", new IntTag("minecraft:repeater[facing="+direction+",delay=1,locked=false]", 1));
            palette.put("minecraft:air", new IntTag("minecraft:air", 0));
            palette.put("minecraft:white_wool", new IntTag("minecraft:white_wool", 2));
            blocksContent.put("PaletteMax", new IntTag("PaletteMax", 3));
        }else if(mode.contains("l")){
            System.out.println("facing ? (\"north\" or \"south\")");
            String direction = idk.nextLine();
            if(direction.isEmpty())throw new Exception("error direction is empty");
            palette.put("minecraft:air", new IntTag("minecraft:air", 0));
            palette.put("minecraft:lever[facing="+direction+",powered=true,face=wall]", new IntTag("minecraft:lever[facing="+direction+",powered=true,face=wall]", 1));
            palette.put("minecraft:lever[facing="+direction+",powered=false,face=wall]", new IntTag("minecraft:lever[facing="+direction+",powered=false,face=wall]", 2));
            blocksContent.put("PaletteMax", new IntTag("PaletteMax", 3));
        }else if(mode.contains("b")){
            palette.put("minecraft:air", new IntTag("minecraft:air", 0));
            palette.put("minecraft:barrel", new IntTag("minecraft:barrel", 1));
            blocksContent.put("PaletteMax", new IntTag("PaletteMax", 2));
        }else throw new Exception("error in mode");

        content.put("Version", new IntTag("Version", 3));
        content.put("DataVersion", new IntTag("DataVersion", 4671));
        content.put("Width", new ShortTag("Width", (short) X));
        content.put("Height", new ShortTag("Height", (short) Y));
        content.put("Length", new ShortTag("Length", (short) Z));
        content.put("Offset",new IntArrayTag("Offset",new int[]{0,0,0}));
        blocksContent.put("Palette", new CompoundTag("Palette", palette));
        List<CompoundTag> itemsList = new ArrayList<>();
        CompoundMap component = new CompoundMap();
        for(int j = 0; j <= 26 ; j ++){
            CompoundMap item = new CompoundMap();
            item.put("Slot", new ByteTag("Slot", (byte)j));
            item.put("id", new StringTag("id", "minecraft:wooden_sword"));
            item.put("count", new IntTag("count", 1));
            itemsList.add(new CompoundTag("",item));
        }
        ListTag<CompoundTag> Items = new ListTag<CompoundTag>("Items",TagType.TAG_COMPOUND,itemsList);
        List<CompoundTag> blockEntitiesList = new ArrayList<>();
        CompoundMap data = new CompoundMap();
        data.put("Items", Items);
        data.put("components", new CompoundTag("components",component));
        data.put("id", new StringTag("id", "minecraft:barrel"));
        byte[] blocks = new byte[Y * X];
        for(int x = 0; x < X; x++) {
            String numberToString = "";
            if(x % 2 == 0){
                numberToString = baseConverter.convertToString(number[x/2], new FixedPointFormat(integerSize,fractionalSize), base);
                System.out.println((x/2) + ")" + numberToString);
                numberToString = new StringBuilder(numberToString).reverse().toString();
            }
            if(mode.contains("r")) {
                for (int y = 0; y < Y; y++) {
                    if (x % 2 == 0) {
                        if (y % 2 == 1) {
                            if (numberToString.charAt(y / 2) == '0') {
                                blocks[y * X + x] = 0;
                            } else if (numberToString.charAt(y / 2) == '1') {
                                blocks[y * X + x] = 1;
                            }
                        } else {
                            blocks[y * X + x] = 2;
                        }
                    } else {
                        blocks[y * X + x] = 0;
                    }
                }
            }else if(mode.contains("l")){
                for (int y = 0; y < Y; y++){
                    if(x % 2 == 0){
                        if(y % 2 == 1){
                            if(numberToString.charAt(y/2) == '1'){
                                blocks[y * X + x] = 1;
                            }else if (numberToString.charAt(y/2) == '0'){
                                blocks[y * X + x] = 2;
                            }
                        }else{
                            blocks[y * X + x] = 0;
                        }
                    }else{
                        blocks[y * X + x] = 0;
                    }
                }
            }else if(mode.contains("b")){
                if(base == 2){
                    for (int y = 0; y < Y; y++){
                        if(x % 2 == 0){
                            if(y % 2 == 1){
                                if(numberToString.charAt(y/2) == '1'){
                                    CompoundMap blockEntytieMap = new CompoundMap();
                                    blocks[y * X + x] = 1;
                                    blockEntytieMap.put("Id", new StringTag("Id", "minecraft:barrel"));
                                    int[] positionArray = new int[]{x, y, 0};
                                    blockEntytieMap.put("Pos", new IntArrayTag("Pos", positionArray));
                                    blockEntytieMap.put("Data", new CompoundTag("Data", data));
                                    blockEntitiesList.add(new CompoundTag("", blockEntytieMap));
                                }else if (numberToString.charAt(y/2) == '0'){
                                    blocks[y * X + x] = 1;
                                }
                            }else{
                                blocks[y * X + x] = 0;
                            }
                        }else{
                            blocks[y * X + x] = 0;
                        }
                    }
                }else{
                    for (int y = 0; y < Y; y++){
                        if(x % 2 == 0){
                            if(y % 2 == 1){
                                blocks[y * X + x] = 1;
                                List<CompoundTag> itemsList1 = new ArrayList<>();
                                CompoundMap component1 = new CompoundMap();
                                int valueInItems = (int) (Math.round(numberToString.charAt(y/2) - 48) * 1.8);
                                for(int j = 0; j < valueInItems ; j ++){
                                    CompoundMap item = new CompoundMap();
                                    item.put("Slot", new ByteTag("Slot", (byte)j));
                                    item.put("id", new StringTag("id", "minecraft:wooden_sword"));
                                    item.put("count", new IntTag("count", 1));
                                    itemsList1.add(new CompoundTag("",item));
                                }
                                ListTag<CompoundTag> Items1 = new ListTag<CompoundTag>("Items",TagType.TAG_COMPOUND, itemsList1);
                                CompoundMap data1 = new CompoundMap();
                                data1.put("Items", Items1);
                                data1.put("components", new CompoundTag("components", component1));
                                data1.put("id", new StringTag("id", "minecraft:barrel"));
                                CompoundMap blockEntytieMap = new CompoundMap();
                                blockEntytieMap.put("Id", new StringTag("Id", "minecraft:barrel"));
                                int[] positionArray = new int[]{x, y, 0};
                                blockEntytieMap.put("Pos", new IntArrayTag("Pos", positionArray));
                                blockEntytieMap.put("Data", new CompoundTag("Data", data1));
                                blockEntitiesList.add(new CompoundTag("", blockEntytieMap));
                            }else{
                                blocks[y * X + x] = 0;
                            }
                        }else{
                            blocks[y * X + x] = 0;
                        }
                    }
                }
            }else throw new Exception("issue with mode");
        }
        if(mode.contains("b")) blocksContent.put("BlockEntities",new ListTag<CompoundTag>("BlockEntities",TagType.TAG_COMPOUND,blockEntitiesList));
        blocksContent.put("Data", new ByteArrayTag("Data", blocks));
        content.put("Blocks", new CompoundTag("Blocks",blocksContent));
        CompoundTag Schematic = new CompoundTag("Schematic", content);
        CompoundMap rootMap = new CompoundMap();
        rootMap.put("Schematic", Schematic);
        CompoundTag root = new CompoundTag("", rootMap);
        try (FileOutputStream file = new FileOutputStream(name + ".schem");
             NBTOutputStream NBT = new NBTOutputStream(file)) {
            NBT.writeTag(root);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}