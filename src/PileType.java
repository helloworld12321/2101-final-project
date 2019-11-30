/**
 * This enum represents the type of a pile of cards.
 *
 * <p>
 *   For example, it distinguishes the stock, which the user draws from,
 *   from the tableaus, which is where the user places cards.
 * </p>
 *
 * <p>
 *   For a discussion of solitaire terminology, see
 *   <a href="https://cardgames.io/solitaire/#rules">here</a>.
 * </p>
 */
enum PileType
{
  TABLEAU, FOUNDATION, STOCK, WASTE
}
