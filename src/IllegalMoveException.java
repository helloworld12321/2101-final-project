/**
 * This exception is thrown to indicate that someone is trying to make
 * a move that's against the rules.
 */
class IllegalMoveException extends Exception
{
  IllegalMoveException(String message)
  {
    super(message);
  }
}
