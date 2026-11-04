public class BaseConverter {
    private int base;
    private FixedPointFormat fixedPointFormat;

    public int getBase() {return base;}
    public void setBase(int base) {this.base = base;}

    public FixedPointFormat getFixedPointFormat() {return fixedPointFormat;}
    public void setFixedPointFormat(FixedPointFormat fixedPointFormat) {this.fixedPointFormat = fixedPointFormat;}

    public BaseConverter(){}

    public BaseConverter(int base, FixedPointFormat fixedPointFormat){
        this.base = base;
        this.fixedPointFormat = fixedPointFormat;
    }

    public int size(int number, int base){
        int size;
        boolean found = false;
        int i = 0;
        while(!found){
            if(number<Math.pow(base,i+1)){
                found = true;
            }
            i++;
        }
        size = i;
        return size;
    }

    public String convertToString(int number, int base){
        int size = size(number, base);
        String convertingNumber = "";
        for(int i = size - 1; i>=0 ; i--){
            int power = (int) Math.pow(base, i);
            int digit = number / power;
            convertingNumber = convertingNumber + digit;
            number %= power;
        }
        return convertingNumber;
    }

    public String convertToString(int number, int sizeMin, int base){
        int size = Math.max(size(number, base),sizeMin);
        String convertingNumber = "";
        for(int i = size - 1; i>=0 ; i--){
            int power = (int) Math.pow(base, i);
            int digit = number / power;
            convertingNumber = convertingNumber + digit;
            number %= power;
        }
        return convertingNumber;
    }

    public String convertToString(float number, FixedPointFormat fixedPointFormat, int base){
        int size = Math.max(size((int)number, base), fixedPointFormat.getIntegerSize());
        String convertingNumber = "";
        for(int i = size - 1; i>=(-fixedPointFormat.getDecimalSize()) ; i--){
            float power = (float) Math.pow(base, i);
            int digit = (int) (number / power);
            convertingNumber = convertingNumber + digit;
            number %= power;
        }
        return convertingNumber;
    }

    public String convertToString(double number, FixedPointFormat fixedPointFormat, int base){
        int size = Math.max(size((int)number, base), fixedPointFormat.getIntegerSize());
        String convertingNumber = "";
        if(number % Math.pow(base,-fixedPointFormat.getDecimalSize()) > Math.pow(base,-fixedPointFormat.getDecimalSize() - 1)){
            number += Math.pow(base,-fixedPointFormat.getDecimalSize() - 1);
        }
        for(int i = size - 1; i>=(-fixedPointFormat.getDecimalSize()) ; i--){
            double power = Math.pow(base, i);
            char digit = (char)(int) ((number / power)+48);
            convertingNumber = convertingNumber + digit;
            number %= power;
        }
        return convertingNumber;
    }

    public double convertFromString(String number, int base, FixedPointFormat fixedPointFormat){
        double processingNumber = 0;
        number = new StringBuilder(number).reverse().toString();
        int diff = fixedPointFormat.getTotalSize() - number.length();
        if(diff>0) {
            number = number + "0".repeat(diff);
        }
        for (int i = -fixedPointFormat.getDecimalSize(); i < fixedPointFormat.getIntegerSize(); i ++){
            processingNumber += (number.charAt(i + fixedPointFormat.getDecimalSize()) - 48) * Math.pow(base,i);
        }
        return processingNumber;
    }

    public String convertStringToString(String number, BaseConverter firstSettings, BaseConverter secondSettings){
        double converted = convertFromString(number, firstSettings.getBase(), firstSettings.getFixedPointFormat());
        return convertToString(converted, secondSettings.getFixedPointFormat(), secondSettings.getBase());
    }
}