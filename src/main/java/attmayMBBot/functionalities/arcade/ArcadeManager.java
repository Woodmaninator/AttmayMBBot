package attmayMBBot.functionalities.arcade;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

public class ArcadeManager {
    //These are the variables that are used for formula for the arcade game. Total XP = baseXp * (level^exponent)
    private int baseXp;
    private double exponent;
    private int maxLevel;
    private List<ArcadeUser> users;
    public List<ArcadeUser> getUsers(){
        return this.users;
    }
    public boolean userExists(Long id){
        return users.stream().anyMatch(user -> user.getId().equals(id));
    }

    public ArcadeUser getUser(Long id){
        if(userExists(id))
            return users.stream().filter(user -> user.getId().equals(id)).findFirst().get();
        return null;
    }

    public ArcadeUser addNewUser(Long id){
        //only call this method if the user does not exist already
        //method also returns the new user
        ArcadeUser newUser = new ArcadeUser(id, 0L);
        users.add(newUser);
        return newUser;
    }
    public int getUserLevel(Long id){
        //only call this method if the user does not exist already
        for(int i = 1; i <= this.maxLevel; i++){
            if(getUser(id).getXp() < getXpForLevel(i))
                return i - 1;
        }
        return maxLevel;
    }
    public int getXpForLevel(int level){
        int xpDirty = (int) (baseXp * Math.pow(level, exponent));
        int xpOffset = xpDirty % 100;
        return xpDirty - xpOffset;
    }
    public void saveArcadeInfoToFile(){
        String path = "AMBBArcade.json";
        try {
            String jsonString = new Gson().toJson(this);
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(jsonString);
            writer.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    public String getLevelInfoStringFromUser(Long id){
        //Add a message about the xp to the user!
        ArcadeUser arcadeUser = getUser(id);
        int currentLevel = getUserLevel(id);
        int xpToNextLevel = getXpForLevel(currentLevel + 1);
        StringBuilder progressBar = new StringBuilder();
        int currentLevelXP = getXpForLevel(currentLevel);
        int nextLevelXP = getXpForLevel(currentLevel + 1);
        int xpForLevel = nextLevelXP - currentLevelXP;
        int xpInCurrentLevel = arcadeUser.getXp().intValue() - currentLevelXP;
        int ratio = (int) (((double) xpInCurrentLevel / xpForLevel) * 10);
        //Loops for the progress bar
        for(int i = 1; i <= ratio; i++)
            progressBar.append("\uD83D\uDFE9");
        for(int i = 1; i <= 10-ratio; i++)
            progressBar.append("\u2B1B");

        //Build the actual string!
        return "Level " + currentLevel + " - " + arcadeUser.getXp() + " XP\n" +
                "" + (xpToNextLevel - arcadeUser.getXp()) + " additional XP needed for level " + (currentLevel + 1) + "\n" +
                progressBar.toString();
    }
}
