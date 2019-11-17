/**
 * This class represents a playing card.
 *
 * <p>
 *   It can be either face up (visible) or face down (not visible).
 * </p>
 */
public class Card
{
  private int rank; 
  private Suit suit;
  private boolean showing = false;

  public Card(int r, Suit s){
    this.rank = r;
    this.suit = s;
  }
  int getRank()
  {
    return rank;
  }

  Suit getSuit()
  {
    return suit;
  }

  boolean isShowing()
  {
    return showing;
  }

  void setShowing(boolean shouldShow)
  {
    showing = shouldShow;
  }

  @Override
  public String toString()
  {
    if (isShowing())
    {
      return String.format("%s%s", stringOfRank(), getSuit());
    }
    else
    {
      //I think it would be good to use something that is 
      //two characters just like the regular cards, such as -- or ??
      return "??";
    }
  }
  
  private String stringOfRank()
  {
    int rank = getRank();
    switch (rank)
    {
      case 11:
        return "J";
      case 12:
        return "Q";
      case 13:
        return "K";
      default:
        return Integer.toString(rank);
    }
  }
}

