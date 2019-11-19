import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

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
  ArrayList<Stack<Card>> foundations;
  Queue<Card> stock;

  public SolitaireGame()
  {
    //ArrayList to hold all our cards and shuffle them
    ArrayList<Card> allCards = new ArrayList<>();

    //Create the cards and fill the array list
    for(int i = 0; i < 4; i++)
    {
      Suit currentSuit = null;
      switch(i)
      {
        case 0:
          currentSuit = Suit.CLUBS;
          break;
        case 1:
          currentSuit = Suit.SPADES;
          break;
        case 2:
          currentSuit = Suit.DIAMONDS;
          break;
        case 3:
          currentSuit = Suit.HEARTS;
          break;
      }

      for(int j = 0; j < 13; j++)
      {
        int currentRank = j + 1;
        Card currentCard = new Card(currentRank, currentSuit);
        allCards.add(currentCard);
      }
    }

    //Shuffle all the cards
    Collections.shuffle(allCards);

    //Initialize the list of tableaus
    tableaus = new ArrayList<>();

    for(int i = 0; i < 7; i++)
    {
      //Create the tableau
      Stack<Card> currentTableau = new Stack<>();

      //Fill the tableau with cards from the shuffled arraylist
      for(int j = 0; j <= i; j++)
      {
        int lastIndex = allCards.size() - 1;
        Card currentCard = allCards.remove(lastIndex);
        currentTableau.add(currentCard);
      }

      //Show the top card
      currentTableau.peek().setShowing(true);

      //Add to the list of tableaus
      tableaus.add(currentTableau);
    }

    stock = new ConcurrentLinkedQueue<>();

    while(!allCards.isEmpty())
    {
      Card currentCard = allCards.remove(0);
      stock.add(currentCard);
    }

    //Create the foundations
    foundations = new ArrayList<>();
    for(int i = 0; i < 4; i++)
    {
      Stack<Card> currentFoundation = new Stack<>();
      foundations.add(currentFoundation);
    }
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
    return stock;
  }

  Deque<Card> getWaste()
  {
    return null;
  }

  Stack<Card> getFoundation(int number)
  {
    return foundations.get(number);
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
   * @return the color 0 for black, 1 for red, 2 for any colored King
   */
  private int getTableauNextColor(Card topCard)
  {
    //If the ending tableau is empty, we're looking for any colored king
    if(topCard == null)
      return 2;

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
    //If tableau is empty, we want a king
    if(topCard == null)
      return 13;

    int rank = topCard.getRank();
    int nextRank;

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
      boolean faceUp = currentCard.isShowing();

      if(satisfiesColor && satisfiesRank && faceUp)
      {
        stoppingCard = currentCard;
        break;
      }
      else if(satisfiesRank && faceUp && requiredColor == 2)
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
