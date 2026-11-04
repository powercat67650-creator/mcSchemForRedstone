import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Function {
    public String function;
    public String[] dictionary = {
            "tan","atan","sin","asin","cos","acos","atanh","tanh","sinh","cosh","log","ln","exp","sqrt","("
    };

    public String getFunction(){return this.function;}

    public void setFunction(String function) {this.function = function;}

    public Function(String function){
        this.function = function;
    }

    public double solveRegisteredFunctionFor(double x) throws Exception{
        double solved = 0;
        if (this.function.contains("(")) {
            int first = this.function.indexOf("(");
            int second = this.function.length() - 1;
            int charsSize = second - first;
            char[] chars = new char[charsSize - 1];
            this.function.getChars(first + 1,second,chars,0);
            this.setFunction(new StringBuilder(this.function).delete(first,second + 1).toString());
            Function xValue = new Function(new String(chars));
            x = xValue.solveFor(x);
        }
        solved = switch (this.function) {
            case "tan" -> Math.tan(x);
            case "atan" -> Math.atan(x);
            case "sin" -> Math.sin(x);
            case "asin" -> Math.asin(x);
            case "cos" -> Math.cos(x);
            case "acos" -> Math.acos(x);
            case "atanh" -> 0.5 * (Math.log((1 + x) / (1 - x)));
            case "tanh" -> Math.tanh(x);
            case "sinh" -> Math.sinh(x);
            case "cosh" -> Math.cosh(x);
            case "log" -> Math.log10(x);
            case "ln" -> Math.log(x);
            case "exp" -> Math.exp(x);
            case "sqrt" -> Math.sqrt(x);
            case "" -> x;
            case null, default -> throw new Exception("not registered function");
        };
        return solved;
    }

    public double solveFor(double x) throws Exception {
        Splitter splitter = new Splitter();
        double solved = 0;
        String[] splitFunction = splitter.splitOutsideParentheses(this.function);
        Map<Integer, Operations> token = new HashMap<>();
        Map<Integer, Double> numbers = new HashMap<>();
        int numbersKey = 0;
        int tokenKey = 0;
        for (String s : splitFunction) {
            double number = 0;
            boolean numberExist = false;
            try {
                number = Double.parseDouble(s);
                numberExist = true;
            }catch (NumberFormatException e) {}
            if(numberExist){
                numbers.put(numbersKey, number);
                numbersKey++;
            }else if (Objects.equals(s, "+")) {
                token.put(tokenKey, new Add());
                tokenKey++;
            } else if (Objects.equals(s, "-")) {
                token.put(tokenKey, new Sub());
                tokenKey++;
            } else if (Objects.equals(s, "*")) {
                token.put(tokenKey, new Multiply());
                tokenKey++;
            } else if (Objects.equals(s, "/")) {
                token.put(tokenKey, new Divide());
                tokenKey++;
            } else if (Objects.equals(s, "^")) {
                token.put(tokenKey, new Pow());
                tokenKey++;
            } else if (Objects.equals(s, "x")) {
                numbers.put(numbersKey, x);
                numbersKey++;
            }else if (Objects.equals(s, "-x")) {
                numbers.put(numbersKey, -x);
                numbersKey++;
            }else if (Objects.equals(s, "pi")) {
                numbers.put(numbersKey, Math.PI);
                numbersKey++;
            }else if (Objects.equals(s, "e")) {
                numbers.put(numbersKey, Math.E);
                numbersKey++;
            }else {
                boolean registered = false;
                for (String st : dictionary) {
                    if (s.contains(st)) {
                        registered = true;
                        numbers.put(numbersKey, new Function(s).solveRegisteredFunctionFor(x));
                        numbersKey++;
                        break;
                    }
                }
                if(!registered)throw new Exception("invalid character");
            }
        }
        if(token.size() >= numbers.size())throw new Exception("invalid function");
        else if(token.size() < numbers.size() - 1)throw new Exception("invalid function");
        for(int i = 0; i< token.size(); i++){
            double operationValue = token.get(i).getValue(numbers.get(i), numbers.get(i + 1));
            numbers.remove(i);
            numbers.replace(i, operationValue);
            solved += operationValue;
        }
        if (token.isEmpty()) solved = numbers.get(0);
        return solved;
    }
    public static void main(String[] args) throws Exception {
        Function function = new Function("ln(e)");
        System.out.println(function.solveFor(1));
    }
}
