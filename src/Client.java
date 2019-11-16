import java.util.*;
import java.util.stream.*;

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
   *
   * @param foundations A {@code List} of the foundations (of which there
   *   should be exactly 4.) This method will not mutate the foundations.
   *
   * @return A one-line, 23-character string, showing the foundation piles in
   *   a user-readable format. Only the top card of each pile will be shown.
   *   If the pile has no cards in it, the symbol for the pile's suit will be
   *   shown.
   */
  private static String stringOfFoundations(List<Stack<Card>> foundations)
  {
    List<String> cardStrings = new ArrayList<>();
    for (int i = 0; i < foundations.size(); i++)
    {
      Stack<Card> foundation = foundations.get(i);
      if (foundation.empty())
      {
        cardStrings.add(String.format(" -%1s- ", suitsOfFoundations.get(i)));
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
   *
   * <p>
   *   The top of each tableau pile will be aligned, so that the cards
   *   will look something like this:
   * </p>
   *
   * <pre>
   *   CCC CC
   *    CC CC
   *    C   C
   *    C
   * </pre>
   *
   *
   * @param tableaus A {@code List} of the tableaus (of which there
   *   should be exactly 7.) This method will not mutate the tableaus.
   *
   * @return A several-line string, showing the tableau piles in
   *   a user-readable format. All of the cards in the tableau piles will
   *   be shown, not just the top card. (If a tableau pile is empty, a suitable
   *   "empty pile" representation will be shown instead.)
   */
  private static String stringOfTableaus(List<Stack<Card>> tableaus)
  {
    // (IntelliJ would prefer that I write "tableaux", but French is hard so
    // I'm pluralizing it the Englishy way instead.)
    
    // Clone all of the tableaus before reversing them
    @SuppressWarnings("unchecked")
    List<Stack<Card>> reversedTableaus = tableaus.stream()
        .map(Stack<Card>::clone)
        // .clone() will only ever return Stack<Card>.
        // This cast is only here to satisfy the compiler. (It doesn't need
        // to be safe at runtime; it should never fail.)
        .map(object -> (Stack<Card>)object)
        .collect(Collectors.toList());
    
    // We'll want to iterate through the tableaus from the back to the
    // front. (So that the top items get displayed last.)
    reversedTableaus.forEach(Collections::reverse);
    
    List<String> linesOfOutput = new ArrayList<>();
    while (!reversedTableaus.stream().allMatch(Stack::empty))
    {
      List<String> cardStrings = new ArrayList<>();
      for (Stack<Card> tableau : reversedTableaus)
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
