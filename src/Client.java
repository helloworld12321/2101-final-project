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
    
    // TESTING
    test();
  }

  /**
   * Test some of the methods in this class.
   *
   * <p>
   *   This method is for development/debugging use only--it should be unused
   *   (or removed completely) by the time the project is finished.
   * </p>
   */
  private static void test()
  {
    List<Stack<Card>> foundations = new ArrayList<>();
    for (int i = 0; i < 4; i++)
    {
      foundations.add(new Stack<>());
    }
  
    List<Stack<Card>> tableaus = new ArrayList<>();
    for (int i = 0; i < 7; i++)
    {
      tableaus.add(game.getTableau(i));
    }
  
    Queue<Card> stock = game.getStock();
  
    Deque<Card> waste = new ArrayDeque<>();
  
    System.out.println(GamePrinter.stringOfEverything(
        foundations,
        tableaus,
        stock,
        waste));
  }
}