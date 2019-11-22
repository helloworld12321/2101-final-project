import java.util.*;

/**
 * This class is in charge of getting input from the user.
 *
 * <p>
 *   It isn't meant to be instantiated; it just provides some static methods.
 * </p>
 */
class InputGetter
{
  /**
   * Ask the user for a move over the standard input.
   *
   * <p>
   *   This method doesn't validate that the move is legal. It does, however,
   *   make sure that the move is from a real, existing pile to a real,
   *   existing pile.
   * </p>
   *
   * <p>
   *   For example, if the user types <pre>asdfkljasdlf</pre> for one of the
   *   piles, this method will prompt them to enter something better.
   * </p>
   *
   * <p>
   *   This method prints a small, minimal prompt. If you want to give
   *   the user a more detailed message, you should print that yourself
   *   before calling {@code askForMove()}.
   * </p>
   *
   * @return The move that the user provided.
   */
  static Move askForMove()
  {
    PileTypeAndID start = askForPile("Starting pile: ");
    PileTypeAndID destination = askForPile("Destination pile: ");

    return new Move(
        start.getPileType(),
        start.getPileID(),
        destination.getPileType(),
        destination.getPileID());
  }
  
  /**
   * Asks the user to input one pile over the standard input.
   *
   * @param prompt The string to prompt the user with. (No newline will be
   *   printed after the prompt.)
   *
   * @return the pile type and ID of the pile the user entered.
   */
  private static PileTypeAndID askForPile(String prompt)
  {
    Scanner sc = new Scanner(System.in);

    // Repeat until they give us a good input.
    Move.PileType pileType = null;
    Integer pileID = null;
    //noinspection ConstantConditions
    do
    {
      // Prompt the user.
      System.out.print(prompt);
      String input = sc.nextLine().trim();
    
      if (input.length() != 1)
      {
        System.out.println("Please enter a single letter or number.");
        continue;
      }
    
      if (input.matches("\\d"))
      {
        int intInput = Integer.parseInt(input);
        if (1 <= intInput && intInput <= 7)
        {
          pileType = Move.PileType.TABLEAU;
          pileID = intInput - 1;
        }
        else if (intInput == 8)
        {
          pileType = Move.PileType.STOCK;
          pileID = 0;
        }
        else if (intInput == 9)
        {
          pileType = Move.PileType.WASTE;
          pileID = 0;
        }
        else
        {
          System.out.printf(
              "%d isn't a valid pile; please try again.\n",
              intInput);
        }
      }
      else
      {
        switch (input.toUpperCase())
        {
          case "C":
            pileType = Move.PileType.FOUNDATION;
            pileID = 0;
            break;
        
          case "D":
            pileType = Move.PileType.FOUNDATION;
            pileID = 1;
            break;
        
          case "S":
            pileType = Move.PileType.FOUNDATION;
            pileID = 2;
            break;
        
          case "H":
            pileType = Move.PileType.FOUNDATION;
            pileID = 3;
            break;
        
          default:
            System.out.printf(
                "%s isn't a valid pile; please try again.\n",
                input);
        }
      }
    } while (pileType == null || pileID == null);
    
    return new PileTypeAndID(pileType, pileID);
  }

  /**
   * This class stores the type and ID of one pile.
   *
   * <p>
   *   This class doesn't do any validation; it just stores the data it's
   *   given.
   * </p>
   *
   * <p>
   *   (For more information on pile types and pile IDs, see the
   *   javadoc for {@link Move}.)
   * </p>
   */
  private static class PileTypeAndID
  {
    private final Move.PileType pileType;
    
    private final int pileID;
  
    /**
     * Create a new object storing a pile type and a pile ID.
     *
     * @param pileType The pile type to store.
     * @param pileID The pile ID to store.
     */
    private PileTypeAndID(Move.PileType pileType, int pileID)
    {
      this.pileType = pileType;
      this.pileID = pileID;
    }
  
    /**
     * Return the pile type of this pile.
     *
     * @return The pile type of this pile.
     */
    Move.PileType getPileType()
    {
      return pileType;
    }
  
    /**
     * Return the pile ID of this pile.
     *
     * @return The pile ID of this pile.
     */
    int getPileID()
    {
      return pileID;
    }
  }
}
