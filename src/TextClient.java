import java.util.*;

/**
 * This is the main class of this application.
 *
 * <p>
 *   It's in charge of talking to the user, but it shouldn't handle gameplay
 *   logic--that's {@code SolitaireGame}'s job.
 * </p>
 */
public class TextClient
{
  private static SolitaireGame game;
  
  public static void main(String[] args)
  {
    System.out.println("Welcome to Solitaire!");
    game = new SolitaireGame();
    
    MoveResult previousMoveResult = MoveResult.LEGAL;
    while (!game.hasWon()
           && previousMoveResult != MoveResult.QUIT)
    {
      previousMoveResult = makeMove(previousMoveResult);
    }
  
    if (game.hasWon())
    {
      System.out.println(GameFormatter.stringOfGame(game));
      System.out.println();
      System.out.println("You win!");
    }
    
    System.out.println("Thanks for playing!");
  }
  
  /**
   * These enum values represent the various things that could happen
   * when the user makes a move.
   */
  private enum MoveResult
  {
    /**
     * The move was legal, and the game state was changed accordingly.
     */
    LEGAL,
  
    /**
     * The move was illegal, so the game state was not changed.
     */
    ILLEGAL,
  
    /**
     * The user wants to quit.
     */
    QUIT
  }
  
  /**
   * Ask the user for a move, and try to execute it.
   *
   * @param previousMoveResult whether the last move was legal or not.
   *
   * @return whether the move was made successfully.
   */
  private static MoveResult makeMove(MoveResult previousMoveResult)
  {
    if (previousMoveResult == MoveResult.LEGAL)
    {
      System.out.println(GameFormatter.stringOfGame(game));
      System.out.println();
      System.out.println("Please enter a move.");
    }
    else
    {
      System.out.println("Please enter another move.");
    }
    
    Move move;
    
    // InputGetter will tell us whether the user entered a real move, or
    // whether they want to quit.
    try
    {
      move = InputGetter.askForMove();
    }
    catch (QuitTheGameException e)
    {
      return MoveResult.QUIT;
    }
  
    // SolitaireGame will tell us whether that move is legal or not.
    // (and if possible, it'll execute the move.)
    try
    {
      game.makeMove(move);
      
      return MoveResult.LEGAL;
    }
    catch (IllegalMoveException e)
    {
      System.out.printf("\tThat move isn't legal: %s\n", e.getMessage());
      return MoveResult.ILLEGAL;
    }
  }
}
