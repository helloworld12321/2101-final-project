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

  /**
   * A mapping between the numbers of the foundation piles (0 through 3,
   * inclusive) and the suit that corresponds to those piles.
   */
  private static final Map<Integer, Suit> suitsOfFoundations;

  static {
    suitsOfFoundations = new HashMap<>();
    suitsOfFoundations.put(0, Suit.CLUBS);
    suitsOfFoundations.put(1, Suit.DIAMONDS);
    suitsOfFoundations.put(2, Suit.SPADES);
    suitsOfFoundations.put(3, Suit.HEARTS);
  }

  public static void main(String[] args)
  {
    System.out.println("Welcome to Solitaire!");

    game = new SolitaireGame();
    // TODO
  }

  private static void displayEverything()
  {
    // TODO
  }

  /**
   * Returns a String showing all the foundation piles (as they will be
   * displayed to the user).
   *
   * <p>
   *   Only the top card of the foundation piles will be shown, so
   *   the returned string will only have one line. This line should
   *   have exactly 23 characters.
   * </p>
   */
  private static String stringOfFoundations()
  {
    List<String> cardStrings = new ArrayList<>();
    for (int i = 0; i < 4; i++)
    {
      Stack<Card> foundation = game.getFoundation(i);
      if (foundation.empty())
      {
        cardStrings.add(String.format("[-%1s-]", suitsOfFoundations.get(i)));
      }
      else
      {
        Card topCard = foundation.peek();
        cardStrings.add(String.format("[%3s]", topCard));
      }
    }
    return String.join(" ", cardStrings);
  }

  /**
   * Returns a String showing all the tableau piles (as they will be
   * displayed to the user).
   *
   * <p>
   *   The returned string should consist of several lines, each containing
   *   exactly 41 characters.
   * </p>
   */
  private static String stringOfTableaus()
  {
    List<Stack<Card>> tableaus = new ArrayList<>();
    for (int i = 0; i < 7; i++)
    {
      // .clone() will only ever return Stack<Card>.
      // This cast is only here to satisfy the compiler. (It doesn't need
      // to be safe at runtime; it should never fail.)
      @SuppressWarnings("unchecked")
      Stack<Card> reversedTableau = (Stack<Card>)game.getTableau(i).clone();
      tableaus.add(reversedTableau);
    }

    // We'll want to iterate through the tableaus from the back to the
    // front. (So that the top items get displayed last.)
    tableaus.forEach(Collections::reverse);

    List<String> linesOfOutput = new ArrayList<>();
    while (!tableaus.stream().allMatch(Stack::empty))
    {
      List<String> cardStrings = new ArrayList<>();
      for (Stack<Card> tableau : tableaus)
      {
        if (tableau.empty())
        {
          // Five spaces
          cardStrings.add("     ");
        }
        else
        {
          Card topCard = tableau.pop();
          cardStrings.add(String.format("[%3s]", topCard));
        }
      }
      linesOfOutput.add(String.join(" ", cardStrings));
    }
    return String.join("\n", linesOfOutput);
  }
}
