/**
 * This class represents a playing card.
 *
 * <p>
 *   It can be either face up (visible) or face down (not visible).
 * </p>
 */
public class Card
{
  int getRank()
  {
    return 0;
  }

  Suit getSuit()
  {
    return null;
  }

  boolean isShowing()
  {
    return false;
  }

  void setShowing(boolean shouldShow)
  {
  }

  @Override
  public String toString()
  {
    if (isShowing())
    {
      return String.format("%d%s", getRank(), getSuit());
    }
    else
    {
      // I'm not really sure what to print out for face-down playing cards.
      // Here's a tentative idea.
      // (For UI purposes, I'd prefer something three characters long or
      // less.)
      return "???";
    }
  }
}
