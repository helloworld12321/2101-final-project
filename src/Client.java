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
   * Returns a string showing all the foundation piles (as they will be
   * displayed to the user).
   *
   * <p>
   *   Only the top card of the foundation piles will be shown, so
   *   the returned string will only have one line. This line should
   *   have exactly 23 characters.
   * </p>
   *
   */
  private static String stringOfFoundations()
  {
    List<String> strings = new ArrayList<>();
    for (int i = 0; i < 3; i++)
    {
      Stack<Card> foundation = game.getFoundation(i);
      if (foundation.empty())
      {
        strings.add(String.format("[-%1s-]", suitsOfFoundations.get(i)));
      }
      else
      {
        Card topCard = foundation.peek();
        strings.add(String.format("[%3s]", topCard));
      }
    }
    return String.join(" ", strings);
  }
}
