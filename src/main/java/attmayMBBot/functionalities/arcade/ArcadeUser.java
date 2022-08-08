package attmayMBBot.functionalities.arcade;

public class ArcadeUser {
    private Long id;
    private Long xp;
    public Long getId() {
        return id;
    }
    public Long getXp() {
        return xp;
    }
    public void setXp(Long xp){
        this.xp = xp;
    }
    public ArcadeUser(Long id, Long xp) {
        this.id = id;
        this.xp = xp;
    }
}
