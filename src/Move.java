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
 *   (This information should suffice to identify every possible move,
 *   without ambiguity.)
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
class Move
{
  private final PileType startType;
  private final int startID;
  private final PileType destinationType;
  private final int destinationID;
  
  /**
   * Construct a new {@code Move} object.
   *
   * @param startType The type of pile that we're moving cards from.
   *
   * @param startID The ID number of the pile that we're moving cards from.
   *
   * @param destinationType The type of pile that we're moving cards to.
   *
   * @param destinationID The number of the pile that we're moving cards to.
   */
  Move(
      PileType startType,
      int startID,
      PileType destinationType,
      int destinationID)
  {
    this.startType = startType;
    this.startID = startID;
    this.destinationType = destinationType;
    this.destinationID = destinationID;
  }
  
  /**
   * Returns the type of pile that we're moving cards from.
   *
   * @return The type of pile that we're moving cards from.
   */
  PileType getStartType()
  {
    return startType;
  }
  
  /**
   * Returns a number that identifies the start pile.
   *
   * <p>
   *   The ID number will be unique among piles of the same type. However,
   *   piles with different types might share the same ID number.
   * </p>
   *
   * <p>
   *   For a listing of all ID numbers, see the javadoc for the {@link Move}
   *   class.
   * </p>
   *
   * @return The ID number of the pile that we're moving cards from.
   */
  int getStartID()
  {
    return startID;
  }
  
  /**
   * Returns the type of pile that we're moving cards to.
   *
   * @return The type of pile that we're moving cards to.
   */
  PileType getDestinationType()
  {
    return destinationType;
  }
  
  /**
   * Returns a number that identifies the destination pile.
   *
   * <p>
   *   The ID number will be unique among piles of the same type. However,
   *   piles with different types might share the same ID number.
   * </p>
   *
   * <p>
   *   For a listing of all ID numbers, see the javadoc for the {@link Move}
   *   class.
   * </p>
   *
   * @return The ID number of the pile that we're moving cards from.
   */
  int getDestinationID()
  {
    return destinationID;
  }
}
