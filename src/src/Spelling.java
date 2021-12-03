import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Spelling extends Trie2{

    private Trie2 dictionary = new Trie2();
    private HashMap<String, Long> frequencyList = new HashMap<>();


    public List<ArrayList<String>> suggest (String token, int count){

        ArrayList<ArrayList<String>> suggestions = new ArrayList<>();
        suggestions = dictionary.getPrefixStrings(token, this.frequencyList);


        return suggestions;

        //for (int i = 0; i < suggestions.size(); i++) {
            //suggestions.get(i).sort(Collections.max(frequencyList.values()));
        //}

    }


    public static void main(String [] args){

        String path = "/Users/frankieg/Desktop/unigram_freq.csv";
        String line = "";

        Spelling spelling = new Spelling();


        try{
            BufferedReader br = new BufferedReader(new FileReader(path));
            line = br.readLine(); //skip the first line
            while((line = br.readLine()) != null){
                String []values = line.split(",");
                long frequency = Long.parseLong(values[1]);
                spelling.dictionary.insert(values[0], frequency);
            }

            //ArrayList<ArrayList<String>> suggestions = new ArrayList<>();
            List<ArrayList<String>> lol = spelling.suggest("the", 3);
            System.out.println(lol.size());

            System.out.println(lol);

            for (int i = 0; i < lol.size(); i++) {
                for (int j = 0; j < 5; j++) {
                    System.out.print(lol.get(i).get(j) + " ");
                }
                System.out.println();
            }

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
