/**
 * This class represents one move that the player makes in solitaire.
 *
 * <p>
 *   There's no guarantee that a {@code Move} object represents a legal move;
 *   The {@code Move} class isn't in charge of validation.
 * </p>
 *
 * <p>
 *   Each move object contains information about two piles of cards: the
 *   starting pile and the destination pile. Piles are identified using two
 *   pieces of information: the <em>pile type</em> and <em>pile ID</em>.
 * </p>
 *
 * <p>
 *   The <em>pile type</em> identifies whether a pile is the stock, the
 *   waste, the tableau, or the foundation.
 * </p>
 *
 * <p>
 *   The <em>pile ID</em> is an integer that disambiguates piles of the same
 *   type:
 * </p>
 *
 * <ul>
 *   <li>
 *     The tableaus have pile IDs {@code 0} through {@code 6}.
 *   </li>
 *   <li>
 *     The foundations have pile IDs {@code 0} through {@code 3}.
 *   </li>
 *   <li>
 *     The stock's pile ID is {@code 0}.
 *   </li>
 *   <li>
 *     The waste's pile ID is {@code 0}.
 *   </li>
 * </ul>
 */
public class Move
{
  PileType getStartType()
  {
    return null;
  }
  
  int getStartID()
  {
    return 0;
  }
  
  PileType getDestinationType()
  {
    return null;
  }

  int getDestinationID()
  {
    return 0;
  }
  
  enum PileType
  {
    TABLEAU, FOUNDATION, STACK, WASTE
  }
}
