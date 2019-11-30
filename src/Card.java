/**
 * This class represents a playing card.
 *
 * <p>
 *   It can be either face up (visible) or face down (not visible).
 * </p>
 */
class Card
{
  private final int rank;
  private final Suit suit;
  private boolean showing = false;
  private boolean onTop = false;

  Card(int r, Suit s){
    this.rank = r;
    this.suit = s;
  }
  
  /**
   *   Returns the rank of the card
   */
  int getRank()
  {
    return rank;
  }
  
  /**
   * Returns the suit of the card
   */
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
    if(getSuit().equals(Suit.CLUBS) || getSuit().equals(Suit.SPADES))
      return 0;
    else
      return 1;
  }
  
  /**
   * Returns if the card is flipped over or not
   */
  boolean isShowing()
  {
    return showing;
  }

  /**
   * Returns if the card is on top of the stack or not
   */
  boolean isTop(){
    return onTop;
  }
  
  /**
   * Sets if the card is showing or not
   */
  void setShowing(boolean shouldShow)
  {
    showing = shouldShow;
  }
  
  /**
   * Sets if the card is on top of the stack or not
   */
  void setTop(boolean top){
    onTop = top;
  }
  
  /**
   * Converts a card to a string depending on if it is face up or not
   */
  @Override
  public String toString()
  {
    if (isShowing())
    {
      return String.format("%s%s", stringOfRank(), getSuit());
    }
    else
    {
      // I got some feedback from my parents, who say that question marks look
      // a little 'busy' on the screen. How about rectangles?
      return "▓▓▓";
    }
  }
  
  /**
   * Returns the string of the rank of the card
   */
  private String stringOfRank()
  {
    int rank = getRank();
    switch (rank)
    {
      case 1:
        return "A";
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
