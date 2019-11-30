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
    
    // TODO: Add check for is-the-game-finished.
    while (true)
    {
      printGameState();
      System.out.println("Please enter a move.");
      
      // Keep prompting for user input until they enter something legit.
      while (true)
      {
        Move move;
        try
        {
          move = InputGetter.askForMove();
        }
        catch (QuitTheGameException e)
        {
          quit();
          
          // These lines should never be reached.
          assert false;
          return;
        }

        try
        {
          game.makeMove(move);

          // If we reach this line, makeMove didn't throw,
          // and the move the user entered was legal.
          break;
        }
        catch (IllegalMoveException e)
        {
          System.out.printf("\tThat move isn't legal: %s\n", e.getMessage());
          System.out.println("Please enter another move.");
          continue;
        }
      }
    }
  }
  
  /**
   * Quit the game and terminate the program.
   *
   * <p>
   *   The program will terminate with exit code {@code 0}.
   * </p>
   */
  static void quit()
  {
    System.out.println("Thanks for playing!");
    System.exit(0);
  }

  /**
   * Display the state of the game on the standard output.
   *
   * <p>
   *   This method prints all of the cards on the table in a pretty, human-
   *   readable format.
   * </p>
   */
  private static void printGameState()
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
  
    System.out.println(GamePrinter.stringOfEverything(
        foundations,
        tableaus,
        stock,
        waste));
  }
}
