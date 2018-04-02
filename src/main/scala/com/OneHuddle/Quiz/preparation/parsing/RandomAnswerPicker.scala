package com.OneHuddle.Quiz.preparation.parsing

import com.OneHuddle.Quiz.preparation.QuizQuestionAnswerProtocol.ObjectiveAnswer

/**
  * Created by nirmalya on 3/19/18.
  */
object RandomAnswerPicker {

  def pick(
          availableAnswers: List[ObjectiveAnswer],
          countRequired: Int,
          randomizer: (List[ObjectiveAnswer]) => List[ObjectiveAnswer]): List[ObjectiveAnswer] = {

    require((countRequired >=2), "At least 2 answers have to be picked up")

    availableAnswers match {

      // Nothing to do for an empty bunch of answers
      case Nil => List[ObjectiveAnswer]()

      //  If there are only two objective answers, then we don't need to randomize. The corresponding question effectively
      //  has only TRUE/FALSE answers.
      case ans1 :: ans2 :: Nil =>   availableAnswers

      case ans1 :: ans2 :: ans3 :: rest  =>
                         val randmomized = randomizer(availableAnswers)
                         randmomized take countRequired
    }

  }

}
