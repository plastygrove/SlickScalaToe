package game

import java.util.Random

class MoveSuggester {
  type Matrix = Array[Int]
  val winPos = Vector(Vector(0, 1, 2), Vector(3, 4, 5), Vector(6, 7, 8), Vector(0, 3, 6), Vector(1, 4, 7), Vector(2, 5, 8), Vector(0, 4, 8), Vector(2, 4, 6))
  val forkPos = List(List(1, 0, 0, 0, 2, 0, 0, 0, 1), List(0, 0, 1, 0, 2, 0, 1, 0, 0))

  private var humanPlayer = 1;
  private var compPlayer = 2;
  
  def setCompPlayer(num:Int)={
    if(num == 1){
      compPlayer = 2;
      humanPlayer = 1;
    } else {
      compPlayer = 1;
      humanPlayer = 2;
    }
  }
  
  private def nextStep(mList: List[Int], player: Int): List[List[Int]] = {
    val out = for {
      p <- (0 until mList.length)
      if (mList(p) == 0)
      val lst = (mList.slice(0, p)) ::: (player :: (mList.slice(p + 1, mList.length)))
    } yield lst
    out.toList
  }

  def isWinner(curMove: Array[Int], player: Int): Boolean = {
    val current = curMove.toList
    testWin(current, player)
  }

  private def testWin(mList: List[Int], player: Int): Boolean = {
    !(winPos filter (p => mList(p(0)) == player && mList(p(0)) == mList(p(1)) && mList(p(1)) == mList(p(2)))).isEmpty
  }

  private def checkImmediateWin(mList: List[Int], player: Int): List[Int] = {
    val nextPos = nextStep(mList, player)
    val immediateWins = nextPos map (p => testWin(p, player))
    if (immediateWins.count(_ == true) > 0)
      nextPos(immediateWins.indexOf(true))
    else
      List.empty
  }
  
  
  def isOver(current: Array[Int]):Int = {
    isOver(current.toList)
  }

  def isOver(mList: List[Int]): Int = {
    if (testWin(mList, 1))
      1 //has won
    else if (testWin(mList, 2))
      2 //has won
    else if (mList.indexOf(0) == -1)
      0 //draw - no moves left
    else
      -1 //no win yet, continue
  }

  private def getScore(mList: List[Int], player: Int): Int = {
    val status = isOver(mList)
    val nextPlayer = getNextPlayer(player)
    if (status == 0)
      1
    else if (status == humanPlayer)
      1 * (mList.count(_ == 0) + 1)
    else if (status == compPlayer)
      -1 * (mList.count(_ == 0) + 1)
    else {
      val nextSteps = nextStep(mList, player)
      val nextPlayer = if (player == 1) 2 else 1

      nextSteps.foldLeft(0)((tot, p) => tot + getScore(p, nextPlayer))
    }
  }

  private def getNextPlayer(player: Int): Int = if (player == 1) 2 else 1

  private def diff(origL: List[Int], newL: List[Int]): Int = {
    if (origL.isEmpty || newL.isEmpty)
      1
    else if (origL.head != newL.head)
      1
    else {
      1 + diff(origL.tail, newL.tail)
    }
  }

  def isFork(mList: List[Int], player: Int): Int = {
    if (mList == forkPos(0) || mList == forkPos(1))
      1
    else
      -1
  }
  
  private def isFirstMove(mList:List[Int]):Boolean = {
    !(mList.contains(1) || mList.contains(2))
  }
  
  def nextMove(curMove: Array[Int], player: Int): Int = {
    val current = curMove.toList
    if(isFirstMove(current)){
      val rand = new Random(System.currentTimeMillis());
      val randIdx = rand.nextInt(current.length);
      (0 until current.length)(randIdx)
    }else if (isOver(current) != -1)
      -1
    else {
      val nextPlayer = getNextPlayer(player)
      val immediateWin = checkImmediateWin(current, player)
      val immediateLoss = checkImmediateWin(current, nextPlayer)
      val forkCheck = isFork(current, player)
      
      if (!immediateWin.isEmpty) {
        diff(current, immediateWin) - 1
      }else if (!immediateLoss.isEmpty){
        diff(current, immediateLoss)-1
      } else if (forkCheck != -1) {
        forkCheck
      } else {
        val possibleMoves = nextStep(current, player)
        val scores = possibleMoves map (t => getScore(t, nextPlayer))
        val bestMove = possibleMoves(scores.indexOf(scores.max))
        diff(current, bestMove) - 1
      }
    }

  }

}