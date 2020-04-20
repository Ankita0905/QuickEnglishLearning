package com.team.capestone.models;

public class AddWords {
    private String word;
    private String wordMeaning;
    private String wordUses;

    public AddWords() {
    }

    public AddWords(String word, String wordMeaning, String wordUses) {
        this.word = word.replace(":", "").replace(".","");
        this.wordMeaning = wordMeaning;
        this.wordUses = wordUses;
    }

    public String getWord() {
        return word;
    }

    public String getWordMeaning() {
        return wordMeaning;
    }

    public String getWordUses() {
        return wordUses;
    }
}
