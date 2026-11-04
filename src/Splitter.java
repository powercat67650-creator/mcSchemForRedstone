import java.util.ArrayList;
import java.util.List;

public class Splitter {

    public Splitter(){}

    public String[] splitOutsideParentheses(String text){
        int depth = 0;
        List<String> result = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < text.length(); i++) {
            char current = text.charAt(i);
            if (current == '(') {
                depth++;
            } else if (current == ')') {
                depth--;
            }
            if (current == ' ' && depth == 0 && !builder.isEmpty()) {
                result.add(builder.toString());
                builder.setLength(0);
            }else if(current == ' ' && depth == 0){

            }else{
                builder.append(current);
            }
            if (i == text.length() - 1){
                result.add(builder.toString());
            }
        }
        String[] split = new String[result.size()];
        for(int i = 0; i < result.size(); i++){
            split[i] = result.get(i);
        }
        return split;
    }

    public String[] splitOutsideParentheses(String text, char chr){
        int depth = 0;
        List<String> result = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < text.length(); i++) {
            char current = text.charAt(i);
            if (current == '(') {
                depth++;
            } else if (current == ')') {
                depth--;
            }
            if (current == chr && depth == 0 && !builder.isEmpty()) {
                result.add(builder.toString());
                builder.setLength(0);
            }else if(current == chr && depth == 0){

            }else{
                builder.append(current);
            }
            if (i == text.length() - 1){
                result.add(builder.toString());
            }
        }
        String[] split = new String[result.size()];
        for(int i = 0; i < result.size(); i++){
            split[i] = result.get(i);
        }
        return split;
    }

    public String[] splitOutsideParentheses(String text, char chr, int start, int stop){
        int depth = 0;

        List<String> result = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for(int i = start; i < stop; i++){
            builder.append(text.charAt(i));
        }
        text = builder.toString();
        builder.setLength(0);
        for(int i = 0; i < text.length(); i++) {
            char current = text.charAt(i);
            if (current == '(') {
                depth++;
            } else if (current == ')') {
                depth--;
            }
            if (current == chr && depth == 0 && !builder.isEmpty()) {
                result.add(builder.toString());
                builder.setLength(0);
            }else if(current == chr && depth == 0){

            }else{
                builder.append(current);
            }
            if (i == text.length() - 1){
                result.add(builder.toString());
            }
        }
        String[] split = new String[result.size()];
        for(int i = 0; i < result.size(); i++){
            split[i] = result.get(i);
        }
        return split;
    }
}
