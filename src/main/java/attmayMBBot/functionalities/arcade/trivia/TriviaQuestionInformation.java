package attmayMBBot.functionalities.arcade.trivia;

import java.util.List;

public class TriviaQuestionInformation {
    private String question;
    private String correctAnswer;
    private List<String> wrongAnswers;
    private ETriviaDifficulty difficulty;

    public TriviaQuestionInformation(String question, String correctAnswer, List<String> wrongAnswers, ETriviaDifficulty difficulty) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.wrongAnswers = wrongAnswers;
        this.difficulty = difficulty;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public List<String> getWrongAnswers() {
        return wrongAnswers;
    }

    public ETriviaDifficulty getDifficulty() {
        return difficulty;
    }
}
