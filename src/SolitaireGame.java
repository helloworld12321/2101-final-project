import java.util.*;

/**
 * This class is in charge of running the game of solitaire.
 *
 * <p>
 *   It contains the gameplay logic and information about the state of the
 *   game. The {@code Client} will talk to it.
 * </p>
 */
public class SolitaireGame
{

  ArrayList<Stack<Card>> tableaus;

  public SolitaireGame()
  {
    //Initialize the list of tableaus and fill it with stacks
    tableaus = new ArrayList<>();

    for(int i = 0; i < 7; i++)
    {
      Stack<Card> currentTableau = new Stack<>();
      tableaus.add(currentTableau);
    }

    //TODO Shuffle the cards and fill both the tableaus and the stack
  }

  void makeMove(Move move) throws IllegalMoveException
  {
  }

  /**
   * Get the tableau of the corresponding number
   * @param number the number corresponding to the tableau you want to return, 
   *  Note: goes from 0 to 6 not 1 to 7
   * @return the tableau as a stack of cards
   */
  Stack<Card> getTableau(int number)
  {
    return tableaus.get(number);
  }

  Queue<Card> getStock()
  {
    return null;
  }

  Deque<Card> getWaste()
  {
    return null;
  }

  Stack<Card> getFoundation(int number)
  {
    return null;
  }

  /**
   * Method for moving cards from one tableau to the next
   * @param startTableau the tableau the stack of cards come from
   * @param endTableau the tableau the stack of cards ends up on
   */
  private void tableauToTableau(int startTableau, int endTableau) throws IllegalMoveException
  {
    //Get the tableaus
    Stack<Card> start = getTableau(startTableau);
    Stack<Card> end = getTableau(endTableau);

    //Set to null in case the destination is empty
    Card endTopCard = null;
    //If it's not empty, get the top card
    if(!end.isEmpty())
      endTopCard = end.peek();

    //Find the color and rank that the next card should be
    int requiredColor = getTableauNextColor(endTopCard);
    int requiredRank = getTableauNextRank(endTopCard);

    //If the starting stack is empty, move is illegal
    if(start.isEmpty())
      throw new IllegalMoveException("Can't move cards from an empty Tableau");

    //Get the first card that meets both requirements in the starting tableau
    Card stoppingCard = getEndOfStack(start, requiredColor, requiredRank);
    Stack<Card> tempStack = new Stack<>();

    //Move the cards before it into a temporary stack for storage(and to preserve their order)
    while(!start.peek().equals(stoppingCard))
    {
      Card currentCard = start.pop();
      tempStack.add(currentCard);
    }

    //Add the card that meets the requirements(didn't happen in the loop above)
    end.add(start.pop());

    //Move the other cards into the ending tableau
    while(!tempStack.isEmpty())
    {
      Card currentCard = tempStack.pop();
      end.add(currentCard);
    }
  }

  /**
   * Get the color that the next card on the tableau should be given its top card
   * @param topCard the current top card of the tableau, null means tableau is empty
   * @return the color 0 for black, 1 for red
   */
  private int getTableauNextColor(Card topCard)
  {
    int color = topCard.getColor();
    int nextColor = -1;

    if(color == 0)
      nextColor = 1;
    else if(color == 1)
      nextColor = 0;

    return nextColor;
  }

  /**
   * Get the rank that the next card on the tableau should be given its top card
   * @param topCard the current top card of the tableau, null means tableau is empty
   * @return the rank
   */
  private int getTableauNextRank(Card topCard) throws IllegalMoveException
  {
    int rank = topCard.getRank();
    int nextRank = -1;

    if(rank == 1)
      throw new IllegalMoveException("Can't move to Tableau with Ace on top");
    else
      nextRank = rank - 1;

    return nextRank;
  }

  private Card getEndOfStack(Stack<Card> tableau, int requiredColor, int requiredRank) throws IllegalMoveException
  {
    //Iterate through every card, stopping at the first one that satisfies our required rank and color
    Card currentCard;
    Card stoppingCard = null;
    for(int i = 0; i < tableau.size(); i++)
    {
      currentCard = tableau.get(i);

      boolean satisfiesColor = requiredColor == currentCard.getColor();
      boolean satisfiesRank = requiredRank == currentCard.getRank();

      if(satisfiesColor && satisfiesRank)
      {
        stoppingCard = currentCard;
        break;
      }
    }

    //If we didn't find a satisfactory card in the first tableau, move is illegal
    if(stoppingCard == null)
      throw new IllegalMoveException("No viable cards found in first tableau");

    return stoppingCard;
  }
}
