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
  private boolean onTop = false;

  public Card(int r, Suit s){
    this.rank = r;
    this.suit = s;
  }

  //Returns the rank of the card
  int getRank()
  {
    return rank;
  }

  //Returns the suit of the card
  Suit getSuit()
  {
    return suit;
  }

  /**
   * Get the color of the card
   * @return 0 for black, 1 for red
   */
  int getColor()
  {
    return 0;
  }

  //Returns if the card is flipped over or not
  boolean isShowing()
  {
    return showing;
  }

  //Returns if the card is on top of the stack or not
  boolean isTop(){
    return onTop;
  }

  //Sets if the card is showing or not
  void setShowing(boolean shouldShow)
  {
    showing = shouldShow;
  }

  //Sets if the card is on top of the stack or not
  void setTop(boolean top){
    onTop = top;
  }

  //Converts a card to a string depending on if it is face up or not
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
  
  //Returns the string of the rank of the card
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
