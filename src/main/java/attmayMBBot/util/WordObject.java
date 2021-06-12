package attmayMBBot.util;

public class WordObject {
    private String term;
    private EWordType wordType;

    public WordObject(String term, EWordType wordType){
        this.term = term;
        this.wordType = wordType;
    }

    public String getTerm(){
        return this.term;
    }
    public EWordType getWordType(){
        return this.wordType;
    }
}
