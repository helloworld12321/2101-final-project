import java.util.*;

/**
 * This is the main class of this application.
 *
 * <p>
 *   It's in charge of talking to the user, but it shouldn't handle gameplay
 *   logic--that's {@code SolitaireGame}'s job.
 * </p>
 */
public class Client
{
  private static SolitaireGame game;
  
  public static void main(String[] args)
  {
    System.out.println("Welcome to Solitaire!");
    game = new SolitaireGame();
    
    MoveResult previousMoveResult = MoveResult.LEGAL;
    // TODO: Add check for is-the-game-finished.
    while (previousMoveResult != MoveResult.QUIT)
    {
      previousMoveResult = makeMove(previousMoveResult);
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
      System.out.println(stringOfGame());
      System.out.println();
      System.out.println("Please enter a move.");
    }
    else
    {
      System.out.println("Please enter another move.");
    }
    
    Move move;
    try
    {
      move = InputGetter.askForMove();
    }
    catch (QuitTheGameException e)
    {
      return MoveResult.QUIT;
    }
  
    try
    {
      game.makeMove(move);
    
      // If we reach this line, makeMove didn't throw,
      // and the move the user entered was legal.
      return MoveResult.LEGAL;
    }
    catch (IllegalMoveException e)
    {
      System.out.printf("\tThat move isn't legal: %s\n", e.getMessage());
      return MoveResult.ILLEGAL;
    }
  }
  
  /**
   * Returns a string representing the state of the game.
   *
   * <p>
   *   The returned string shows all of the cards on the table in a pretty,
   *   human-readable format.
   * </p>
   *
   * @return a string representing the state of the game.
   */
  private static String stringOfGame()
  {
    List<Stack<Card>> foundations = new ArrayList<>();
    for (int i = 0; i < 4; i++)
    {
      foundations.add(game.getFoundation(i));
    }
  
    List<Stack<Card>> tableaus = new ArrayList<>();
    for (int i = 0; i < 7; i++)
    {
      tableaus.add(game.getTableau(i));
    }
  
    Queue<Card> stock = game.getStock();
  
    Deque<Card> waste = game.getWaste();
  
    return GamePrinter.stringOfEverything(
        foundations,
        tableaus,
        stock,
        waste);
  }
}
