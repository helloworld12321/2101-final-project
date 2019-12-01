import java.util.*;
import java.util.concurrent.*;

/**
 * This class is in charge of running the game of solitaire.
 *
 * <p>
 *   It contains the gameplay logic and information about the state of the
 *   game. The {@code Client} will talk to it.
 * </p>
 */
class SolitaireGame
{
  private final ArrayList<Stack<Card>> tableaus;
  private final ArrayList<Stack<Card>> foundations;
  private final Queue<Card> stock;
  private final Deque<Card> waste;
  
  /**
   * Create a new solitaire game.
   *
   * <p>
   *   This constructor sets up all the cards and puts them into the right
   *   piles; the game should be ready to start immediately.
   * </p>
   */
  SolitaireGame()
  {
    //ArrayList to hold all our cards and shuffle them
    ArrayList<Card> allCards = new ArrayList<>();

    //Create the cards and fill the array list
    for(Suit currentSuit : Suit.values())
    {
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

      //Fill the tableau with cards from the shuffled ArrayList
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

    waste = new ConcurrentLinkedDeque<>();
    //Create the foundations
    foundations = new ArrayList<>();
    for(int i = 0; i < 4; i++)
    {
      //The foundations start out empty
      Stack<Card> currentFoundation = new Stack<>();
      foundations.add(currentFoundation);
    }
  }
  
  
  /**
   * Get the tableau of the corresponding number.
   *
   * <p>
   *   For a discussion of solitaire terminology, see
   *   <a href="https://cardgames.io/solitaire/#rules">here</a>.
   * </p>
   *
   * @param number the number corresponding to the tableau you want to return.
   *  Note: goes from 0 to 6 not 1 to 7
   * @return the tableau as a stack of cards
   */
  Stack<Card> getTableau(int number)
  {
    return tableaus.get(number);
  }
  
  /**
   * Returns the stock (ie, the pile you draw from).
   *
   * <p>
   *   The front of the queue is the top of the stock.
   * </p>
   *
   * <p>
   *   For a discussion of solitaire terminology, see
   *   <a href="https://cardgames.io/solitaire/#rules">here</a>.
   * </p>
   *
   * @return The stock.
   */
  Queue<Card> getStock()
  {
    return stock;
  }
  
  /**
   * Returns the waste.
   *
   * <p>
   *   The front of the deque is the top of the waste.
   * </p>
   *
   * <p>
   *   For a discussion of solitaire terminology, see
   *   <a href="https://cardgames.io/solitaire/#rules">here</a>.
   * </p>
   *
   * @return the waste.
   */
  Deque<Card> getWaste()
  {
    return waste;
  }
  
  /**
   * Gets the foundation of the corresponding number.
   *
   * <p>
   *   For a discussion of solitaire terminology, see
   *   <a href="https://cardgames.io/solitaire/#rules">here</a>.
   * </p>
   *
   * @param number the number corresponding to the tableau you want to return.
   *  Note: goes from 0 to 3, not 1 to 4.
   *
   * @return The foundation, as a stack of cards.
   */
  Stack<Card> getFoundation(int number)
  {
    return foundations.get(number);
  }
  
  /**
   * Return whether the user has won the game yet.
   *
   * <p>
   *   The user has won if and only if all of the cards on the tableaus
   *   are turned face-up.
   * </p>
   *
   * @return {@code true} if the user has won the game, and {@code false} if
   *   not.
   */
  boolean hasWon()
  {
    return tableaus.stream().allMatch(
        tableau -> tableau.stream().allMatch(Card::isShowing));
  }
  
  /**
   * Given a move that the user wants to make, execute that move.
   *
   * <p>
   *   (This usually involves moving cards around from one pile to another.)
   * </p>
   *
   * <p>
   *   This method mutates the {@code SolitaireGame} object.
   * </p>
   *
   * @param move The move the user wants to make.
   *
   * @throws IllegalMoveException If the move in question is against the
   *   rules. (The message carried by this exception should be suitable
   *   to display to the user.)
   */
  void makeMove(Move move) throws IllegalMoveException
  {

    PileType startType = move.getStartType();
    PileType endType = move.getDestinationType();
    int startID = move.getStartID();
    int endID = move.getDestinationID();

    switch(startType)
    {
      case TABLEAU:
        moveFromTableau(startID, endID, endType);
        break;
      case STOCK:
        moveFromStock(endType);
        break;
      case WASTE:
        moveFromWaste(endID, endType);
        break;
      case FOUNDATION:
        moveFromFoundation(startID, endID, endType);
        break;
    }
  }
  
  /**
   * Method for moving cards from a tableau to any other pile type.
   *
   * @param startID The ID number of the tableau to move cards from. (As
   *   described in the {@link Move} class.)
   *
   * @param endID The ID number of the pile to move cards to.
   *
   * @param endType The type of the pile to move cards to.
   *
   * @throws IllegalMoveException If the move in question is against the
   *   rules. (The message carried by this exception should be suitable
   *   to display to the user.)
   */
  private void moveFromTableau(int startID, int endID, PileType endType)
      throws IllegalMoveException
  {
    switch (endType)
    {
      case FOUNDATION:
        tableauToFoundation(startID, endID);
        break;
      case TABLEAU:
        tableauToTableau(startID, endID);
        break;
      case STOCK:
        throw new IllegalMoveException(
            "Can't move from a tableau to the stock");
      case WASTE:
        throw new IllegalMoveException(
            "Can't move from a tableau to the waste");
    }
  }
  
  /**
   * Method for moving cards from the stock to any other pile type.
   *
   * @param endType The type of the pile to move cards to.
   *
   * @throws IllegalMoveException If the move in question is against the
   *   rules. (The message carried by this exception should be suitable
   *   to display to the user.)
   */
  private void moveFromStock(PileType endType)
      throws IllegalMoveException
  {
    switch (endType)
    {
      case STOCK:
      case WASTE:
        wastePileDraw();
        break;
      case TABLEAU:
        throw new IllegalMoveException(
            "Can't move from the stock to a tableau");
      case FOUNDATION:
        throw new IllegalMoveException(
            "Can't move from the stock to a foundation");
    }
  }
  
  /**
   * Method for moving cards from the waste to any other pile type.
   *
   * @param endID The ID number of the pile to move cards to. (As
   *   described in the {@link Move} class.)
   *
   * @param endType The type of the pile to move cards to.
   *
   * @throws IllegalMoveException If the move in question is against the
   *   rules. (The message carried by this exception should be suitable
   *   to display to the user.)
   */
  private void moveFromWaste(int endID, PileType endType)
      throws IllegalMoveException
  {
    switch (endType)
    {
      case STOCK:
      case WASTE:
        wastePileDraw();
        break;
      case TABLEAU:
        wasteToTableau(endID);
        break;
      case FOUNDATION:
        wasteToFoundation(endID);
        break;
    }
  }
  
  /**
   * Method for moving cards from a foundation to any other pile type.
   *
   * @param startID The ID number of the foundation to move cards from. (As
   *   described in the {@link Move} class.)
   *
   * @param endID The ID number of the pile to move cards to.
   *
   * @param endType The type of the pile to move cards to.
   *
   * @throws IllegalMoveException If the move in question is against the
   *   rules. (The message carried by this exception should be suitable
   *   to display to the user.)
   */
  private void moveFromFoundation(int startID, int endID, PileType endType)
      throws IllegalMoveException
  {
    switch (endType)
    {
      case TABLEAU:
        // TODO: Make and implement moving cards from foundations to tableaus.
        // foundationToTableau(startID, endID);
        break;
      case FOUNDATION:
        throw new IllegalMoveException(
            "Can't move from one foundation to another.");
      case STOCK:
        throw new IllegalMoveException(
            "Can't move from a foundation to the stock.");
      case WASTE:
        throw new IllegalMoveException(
            "Can't move from a foundation to the waste.");
    }
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
      throw new IllegalMoveException("Can't move cards from an empty tableau");

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

    //If the top card isn't showing, show it
    revealTopOfTableau(startTableau);
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
   * Get the rank that the next card on the tableau should be given its top
   * card.
   * @param topCard the current top card of the tableau, null means tableau is
   *   empty.
   * @return the rank.
   * @throws IllegalMoveException If no card could possibly be placed on this
   *   tableau.
   */
  private int getTableauNextRank(Card topCard) throws IllegalMoveException
  {
    //If tableau is empty, we want a king
    if(topCard == null)
      return 13;

    int rank = topCard.getRank();
    int nextRank;

    if(rank == 1)
      throw new IllegalMoveException("Can't move to tableau with Ace on top");
    else
      nextRank = rank - 1;

    return nextRank;
  }
  
  /**
   * When moving multiple cards from one tableau to another, return which
   * card is the first card to move (ie, the bottom card to move).
   *
   * <p>
   *   This card, and every card above it, will be transferred to the new
   *   tableau.
   * </p>
   *
   * <hr>
   *
   * <p>
   *   An example will make things clearer. Suppose we have two tableaus,
   *   tableau zero and tableau one:
   * </p>
   *
   * <pre>
   *     0     1
   *   [ Q♠] [ ??]
   *   [ J♡] [ ??]
   *   [10♣] [ 9♡]
   *   [ 9♢] [ 8♠]
   *   [ 8♣] [ 7♡]
   *         [ 6♣]
   *         [ 5♢]
   * </pre>
   *
   * <p>
   *   (Here, some of the cards in tableau zero are face-down.)
   * </p>
   *
   * <p>
   *   Now, suppose we want to move cards from tableau one to tableau
   *   zero. Tableau zero needs a red seven next, so we would call
   *   {@code getEndOfStack(tableaus.get(1), 1, 7)}. (If tableau zero
   *   needed a <em>black</em> seven, we would call
   *   {@code getEndOfStack(tableaus.get(1), 0, 7)} instead.)
   * </p>
   *
   * <p>
   *   Now, this call will return the first (most bottom) card in tableau one
   *   that may be placed on tableau zero. In this case, that card is the
   *   seven of hearts, {@code 7♡}.
   * </p>
   *
   * <p>
   *   Note that this method will never return one of the face-down cards,
   *   even if one of them happens to be a red seven. It's never legal to
   *   move a face-down card onto another tableau.
   * </p>
   *
   * <p>
   *   After the seven of hearts is returned, the caller knows to move
   *   the seven of hearts, and all cards above it, onto tableau zero.
   *   (So, the caller would want to do this:)
   * </p>
   *
   * <pre>
   *     0     1
   *   [ Q♠] [ ??]
   *   [ J♡] [ ??]
   *   [10♣] [ 9♡]
   *   [ 9♢] [ 8♠]
   *   [ 8♣]
   *   [ 7♡]
   *   [ 6♣]
   *   [ 5♢]
   * </pre>
   *
   * <hr>
   *
   * @param tableau The tableau that we want to move cards from.
   * @param requiredColor The destination tableau needs this color next.
   *   (0 means black; 1 means red; 2 means either.)
   * @param requiredRank The destination tableau needs this rank next.
   * @return The first (ie, the most bottom) card in {@code tableau} that may
   *   legally be placed on the destination tableau.
   * @throws IllegalMoveException If no card in {@code tableau} may be legally
   *   be placed on the destination tableau.
   */
  private Card getEndOfStack(Stack<Card> tableau, int requiredColor, int requiredRank) throws IllegalMoveException
  {
    //Iterate through every card, stopping at the first one that satisfies our required rank and color
    Card stoppingCard = null;
    for (Card currentCard : tableau)
    {
      boolean satisfiesColor = requiredColor == currentCard.getColor();
      boolean satisfiesRank = requiredRank == currentCard.getRank();
      boolean faceUp = currentCard.isShowing();
    
      if (satisfiesColor && satisfiesRank && faceUp)
      {
        stoppingCard = currentCard;
        break;
      }
      else if (satisfiesRank && faceUp && requiredColor == 2)
      {
        stoppingCard = currentCard;
        break;
      }
    }

    //If we didn't find a satisfactory card in the first tableau, move is illegal
    if(stoppingCard == null)
      throw new IllegalMoveException("No viable cards found in the starting tableau");

    return stoppingCard;
  }
  
  
  /**
   * Move one card from the front stock to the front of the waste.
   *
   * <p>
   *   If the waste is empty, this method puts all of the cards from the
   *   back of the waste into the back of the stock.
   * </p>
   *
   * @throws IllegalMoveException If both the stock and the waste are empty.
   */
  private void wastePileDraw() throws IllegalMoveException{
    if (stock.isEmpty() && waste.isEmpty()) {
      throw new IllegalMoveException(
          "There are no cards left to draw from the waste or stock");
    }
  
    if (stock.isEmpty()) {
      while(!waste.isEmpty()) {
        waste.getLast().setShowing(false);
        stock.add(waste.removeLast());
      }
    }
    else {
      waste.addFirst(stock.poll());
      waste.getFirst().setShowing(true);
    }
  }
  
  /**
   * Move one card from the front of the waste to the top of a tableau.
   *
   * @param endTableau The tableau that we're moving the card to.
   *
   * @throws IllegalMoveException If the card on top of the waste doesn't
   *   match the bottom of the tableau. (Ie, its rank or its color isn't
   *   right.)
   */
  private void wasteToTableau(int endTableau) throws IllegalMoveException
  {
  
    //Get the tableau
    Stack<Card> end = getTableau(endTableau);
  
    //Set to null in case the destination is empty
    Card endTopCard = null;
  
    //If it's not empty, get the top card
    if (!end.isEmpty())
      endTopCard = end.peek();
  
    //Find the color and rank that the next card should be
    int requiredColor = getTableauNextColor(endTopCard);
    int requiredRank = getTableauNextRank(endTopCard);
  
    //If waste is empty, move is illegal
    if (waste.isEmpty()) {
      throw new IllegalMoveException("Can't move cards from an empty waste");
    }

    //If the card is the correct one adds it to the tableau
    else if (
         waste.getFirst().getRank() == requiredRank
         && (waste.getFirst().getColor() == requiredColor || requiredColor == 2)) {
      end.add(waste.pop());
    }
    else {
      throw new IllegalMoveException("The waste card cannot be added to the tableau");
    }
  }

  /**
   * Moves a card from the waste to the foundation with the given index
   * @param foundationIndex the index of the foundation the card's being moved to
   * @throws IllegalMoveException if the waste is empty or if the wrong foundation is given
   */
  private void wasteToFoundation(int foundationIndex) throws IllegalMoveException
  {
    Suit foundationSuit = findFoundationSuit(foundationIndex);
    Stack<Card> foundation = getFoundation(foundationIndex);

    if(waste.isEmpty())
      throw new IllegalMoveException("Can't move cards from an empty waste");

    //Get the rank that the next card on the foundation should be
    int requiredRank;
    //Note: if the foundation is empty, looking for an ace
    if(!foundation.isEmpty())
      requiredRank = foundation.peek().getRank() + 1;
    else
      requiredRank = 1;

    //Get the top card of the tableau
    Card topCard = waste.peek();

    boolean satisfiesRank = topCard.getRank() == requiredRank;
    boolean satisfiesSuit = topCard.getSuit().equals(foundationSuit);

    if(satisfiesRank && satisfiesSuit)
    {
      topCard = waste.pop();
      foundation.add(topCard);
    }
    else
    {
      throw new IllegalMoveException("That card can't be added to this foundation");
    }
  }
  
  
  /**
   * Move one card from tableau number {@code tableauIndex} to foundation
   * number {@code foundationIndex}.
   *
   * @param tableauIndex The number of the start tableau.
   * @param foundationIndex The number of the destination foundation.
   * @throws IllegalMoveException If the card on top of the start tableau
   *   doesn't fit on top of the desitination foundation. (Ie, its rank or
   *   its color isn't right.)
   */
  private void tableauToFoundation(int tableauIndex, int foundationIndex) throws IllegalMoveException
  {
    Suit foundationSuit = findFoundationSuit(foundationIndex);
    Stack<Card> foundation = getFoundation(foundationIndex);
    Stack<Card> tableau = getTableau(tableauIndex);

    if(tableau.isEmpty())
      throw new IllegalMoveException("Can't move anything from an empty tableau");

    //Get the rank that the next card on the foundation should be
    int requiredRank;
    //Note: if the foundation is empty, looking for an ace
    if(!foundation.isEmpty())
      requiredRank = foundation.peek().getRank() + 1;
    else
      requiredRank = 1;

    //Get the top card of the tableau
    Card topCard = tableau.peek();

    boolean satisfiesRank = topCard.getRank() == requiredRank;
    boolean satisfiesSuit = topCard.getSuit().equals(foundationSuit);

    if(satisfiesRank && satisfiesSuit)
    {
      topCard = tableau.pop();
      foundation.add(topCard);
    }
    else
    {
      throw new IllegalMoveException("That card can't be added to this foundation");
    }

    //If the top card isn't showing, show it
    revealTopOfTableau(tableauIndex);
  }

  /**
   * Get the suit of the foundation with the given index
   * @param foundationIndex index of the foundation whose suit you want to find
   * @return the suit of the foundation
   */
  private Suit findFoundationSuit(int foundationIndex)
  {
    Suit foundationSuit = null;

    //Figure out the suit of the foundation
    switch(foundationIndex)
    {
      case 0:
        foundationSuit = Suit.CLUBS;
        break;
      case 1:
        foundationSuit = Suit.DIAMONDS;
        break;
      case 2:
        foundationSuit = Suit.SPADES;
        break;
      case 3:
        foundationSuit = Suit.HEARTS;
        break;
    }

    return foundationSuit;
  }
  
  
  /**
   * Show the top card of tableau number {@code tableauIndex}. (Flip it
   * face-up.)
   *
   * @param tableauIndex The number of the tableau to show the top card of.
   */
  private void revealTopOfTableau(int tableauIndex)
  {
    Stack<Card> tableau = getTableau(tableauIndex);

    if(!tableau.isEmpty())
    {
      Card topCard = tableau.peek();

      if(!topCard.isShowing())
      {
        topCard.setShowing(true);
      }
    }
  }
}
