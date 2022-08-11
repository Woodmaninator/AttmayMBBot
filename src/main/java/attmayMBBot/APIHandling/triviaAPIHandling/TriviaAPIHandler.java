package attmayMBBot.APIHandling.triviaAPIHandling;

import attmayMBBot.functionalities.arcade.trivia.ETriviaDifficulty;
import attmayMBBot.functionalities.arcade.trivia.TriviaQuestionInformation;
import attmayMBBot.httpHandling.HttpConnectionHandler;
import attmayMBBot.util.HTMLUnescaper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class TriviaAPIHandler {
    public TriviaQuestionInformation getTriviaQuestion(ETriviaDifficulty difficulty){
        //Returns null if stuff fucks up, I guess
        String difficultyString = "";
        switch(difficulty){
            case EASY:
                difficultyString = "easy";
                break;
            case MEDIUM:
                difficultyString = "medium";
                break;
            case HARD:
                difficultyString = "hard";
                break;
        }

        String url = "https://opentdb.com/api.php?amount=1&difficulty=" + difficultyString + "&type=multiple";
        JsonObject jsonObject = new Gson().fromJson(new HttpConnectionHandler().get(url),JsonObject.class);

        if(jsonObject != null){
            JsonObject result = jsonObject.get("results").getAsJsonArray().get(0).getAsJsonObject();
            String question = HTMLUnescaper.unescapeHtml3(result.get("question").getAsString());
            String correctAnswer = HTMLUnescaper.unescapeHtml3(result.get("correct_answer").getAsString());
            List<String> wrongAnswers = new ArrayList<>();
            JsonArray wrongAnswersArr = result.get("incorrect_answers").getAsJsonArray();
            for(int i = 0; i < wrongAnswersArr.size(); i++){
                wrongAnswers.add(HTMLUnescaper.unescapeHtml3(wrongAnswersArr.get(i).getAsString()));
            }

            return new TriviaQuestionInformation(question, correctAnswer, wrongAnswers, difficulty);
        } else {
            return null;
        }
    }
}
