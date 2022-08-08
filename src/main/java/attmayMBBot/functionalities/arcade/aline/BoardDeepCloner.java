package attmayMBBot.functionalities.arcade.aline;

public class BoardDeepCloner {
    public static EAlineTileType[][] deepCloneBoard(EAlineTileType[][] input) {
        if (input == null)
            return null;
        EAlineTileType[][] result = new EAlineTileType[input.length][];
        for (int r = 0; r < input.length; r++) {
            result[r] = input[r].clone();
        }
        return result;
    }
}
