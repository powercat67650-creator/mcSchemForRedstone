public class FixedPointFormat {

    private int integerSize;
    private int decimalSize;

    public FixedPointFormat(int integerSize,int decimalSize){
        this.integerSize = integerSize;
        this.decimalSize = decimalSize;
    }

    public int getDecimalSize() {return decimalSize;}
    public int getIntegerSize() {return integerSize;}
    public void setDecimalSize(int decimalSize) {this.decimalSize = decimalSize;}
    public void setIntegerSize(int integerSize) {this.integerSize = integerSize;}

    public int getTotalSize() {return this.getDecimalSize()+this.getIntegerSize();}
}
