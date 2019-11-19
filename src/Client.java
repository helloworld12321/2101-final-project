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
    
    // TESTING
    test();
  }
  
  /**
   * Returns a string showing the entire solitaire game--all the cards
   * in all the piles--as it will be displayed to the user.
   *
   * For more information on
   *
   * @param foundations A {@code List} of the foundations (of which there
   *   should be exactly 4.) This method will not mutate the foundations.
   *
   * @param tableaus A {@code List} of the tableaus (of which there
   *   should be exactly 7.) This method will not mutate the tableaus.
   *
   * @param stock The stock of the solitaire game (that is, the pile
   *   of face-down cards the user draws from).
   *
   * @param waste The waste of the solitaire game (that is, the cards that the
   *   user has drawn from the stock.)
   */
  private static void stringOfEverything(
      List<Stack<Card>> foundations,
      List<Stack<Card>> tableaus,
      Queue<Card> stock,
      Deque<Card> waste)
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
    System.out.println("--------");
    System.out.println(stringOfFoundations(foundations));
  
  
    Card c1 = new Card(2, Suit.CLUBS);
    Card c2 = new Card(4, Suit.HEARTS);
    Card c5 = new Card(13, Suit.SPADES);
    Card c4 = new Card(12, Suit.HEARTS);
    Card c3 = new Card(11, Suit.CLUBS);
    Card c6 = new Card(10, Suit.HEARTS);
    c1.setShowing(true);
    c2.setShowing(false);
    c3.setShowing(true);
    c4.setShowing(true);
    c5.setShowing(true);
    c6.setShowing(true);
    foundations.get(0).push(c1);
    foundations.get(2).push(c2);
    foundations.get(3).push(c3);
    foundations.get(3).push(c4);
    foundations.get(3).push(c5);
    foundations.get(3).push(c6);
    System.out.println("--------");
    System.out.println(stringOfFoundations(foundations));
    
    List<Stack<Card>> tableaus = new ArrayList<>();
    for (int i = 0; i < 7; i++)
    {
      tableaus.add(new Stack<>());
    }
    System.out.println("--------");
    System.out.println(stringOfTableaus(tableaus));
  
  
    tableaus.get(0).push(c1);
    tableaus.get(2).push(c2);
    tableaus.get(3).push(c3);
    tableaus.get(3).push(c4);
    tableaus.get(3).push(c5);
    tableaus.get(3).push(c6);
    tableaus.get(5).push(c2);
    tableaus.get(5).push(c2);
    tableaus.get(5).push(c2);
    tableaus.get(5).push(c2);
    tableaus.get(5).push(c1);
    System.out.println("--------");
    System.out.println(stringOfTableaus(tableaus));
  }
}