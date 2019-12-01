import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is in charge of stringifying out the state of the game.
 *
 * <p>
 *   It isn't meant to be instantiated; it just provides some static methods.
 * </p>
 */
class GameFormatter
{
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
  
  /**
   * Returns a string showing the entire solitaire game--all the cards
   * in all the piles--as it will be displayed to the user.
   *
   * @param game The solitaire game to stringify
   *
   * @return a pretty, human-readable string showing the state of the game.
   *   (This string will contain a trailing newline.)
   */
  static String stringOfGame(SolitaireGame game)
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
  
  
    StringBuilder everything = new StringBuilder();
    
    // Print out the foundations in their own box on the top.
    
    String fourteenSpaces = "              ";
    
    String foundationsString =
        "║" + fourteenSpaces + "  C     D     S     H  " + fourteenSpaces + "║\n"
        + "║" + fourteenSpaces + stringOfFoundations(foundations) + fourteenSpaces + "║\n";
    
    everything.append("╔═══════════════════════════════════════════════════╗\n");
    
    everything.append(foundationsString);
    
    everything.append("╠═══════╦═══════════════════════════════════════════╣\n");
    
    // Put the stock and waste on the left, and the tableaus on the right.
    
    String stringOfStock =
        stock.isEmpty() ?
            " --- " :
            String.format("[%3s]", stock.element());
    
    String stringOfWaste =
        waste.isEmpty() ?
            " --- " :
            String.format("[%3s]", waste.getFirst());
    
    String[] leftLines = {
        "  8  ",
        String.format("%5s", stringOfStock),
        "  9  ",
        String.format("%5s", stringOfWaste),
        };
    
    String tableausString = stringOfTableaus(tableaus);
    
    String[] rightLines =
        ("  1     2     3     4     5     6     7  \n"
         + tableausString).split("\\n");
    
    // Pad the lines on the left and the lines on the right, so that
    // there are the same number of lines in each.
    
    if (leftLines.length < rightLines.length)
    {
      String[] newLeftLines = Arrays.copyOf(leftLines, rightLines.length);
      for (int i = leftLines.length; i < rightLines.length; i++)
      {
        // five spaces.
        newLeftLines[i] = "     ";
      }
      leftLines = newLeftLines;
    }
    else
    {
      String[] newRightLines = Arrays.copyOf(rightLines, leftLines.length);
      for (int i = rightLines.length; i < leftLines.length; i++)
      {
        // forty-one spaces.
        newRightLines[i] = "                                         ";
      }
      rightLines = newRightLines;
    }
    
    for (int i = 0; i < leftLines.length; i++)
    {
      everything
          .append("║ ")
          .append(leftLines[i])
          .append(" ║ ")
          .append(rightLines[i])
          .append(" ║")
          .append("\n");
    }
  
    everything.append("╚═══════╩═══════════════════════════════════════════╝\n");
  
    return everything.toString();
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
    // I'm pluralizing it the anglophone way instead.)
    
    // Clone all of the tableaus before reversing them
    @SuppressWarnings("unchecked")
    List<Stack<Card>> reversedTableaus =
        tableaus.stream()
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
