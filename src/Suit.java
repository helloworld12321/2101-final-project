/**
 * This enum represents the suit of a playing card.
 */
public enum Suit
{
  CLUBS('♣'), DIAMONDS('♢'), SPADES('♠'), HEARTS('♡');


  /**
   * The symbol representing this suit (a unicode character).
   */
  private final char symbol;

  Suit(char symbol)
  {
    this.symbol = symbol;
  }

  @Override
  public String toString()
  {
    return Character.toString(symbol);
  }
}