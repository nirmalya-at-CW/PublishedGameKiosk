package com.OneHuddle.Quiz.preparation

import org.json4s.ShortTypeHints
import org.json4s.jackson.Serialization

import scala.{Option => ScalaOption}

/**
  * Created by nirmalya on 3/19/18.
  */
object QuizQuestionAnswerProtocol {

  object AllowedPerAnswer extends Enumeration {
    val TwoOptions   = Value(2)
    val FourOptions  = Value(4)
  }

  case class InvidualOptionPromptAndCorrectnessIndicator (val prompt: String, val indicator: String)
  case class OneHuddleAnswerOptions(val options: List[(String,String)] )
  object NonExistentOneHuddleAnswerOptions extends OneHuddleAnswerOptions(List(("No answer options provided","No correctness indicator provided")))


  case class AnswersAvailableInDB(
                                   val subjective:ScalaOption[String],
                                   val objective: ScalaOption[OneHuddleAnswerOptions],
                                   val total_options: Int,
                                   val correct_options: Int)

  class FailedToParseAnswersAvailableInDB(hintInput: String) extends AnswersAvailableInDB(Some(hintInput),None,-1,-1)

  sealed trait PossibleAnswer {
    val rawByteForm: Array[Byte]
    val humanReadableAnswerText: String
  }

  trait ObjectiveAnswer   extends PossibleAnswer
  trait SubjectiveAnswer  extends PossibleAnswer
  trait ImpossibleAnswer  extends PossibleAnswer

  object IncorrectAnswerBothSubjectiveObjectiveUnacceptable extends ImpossibleAnswer  {
    override val humanReadableAnswerText: String = "Both subjective and objective cannot co-exist"
    override val rawByteForm: Array[Byte] = humanReadableAnswerText.getBytes
  }


  case class ObjectiveCorrectAnswer(val humanReadableAnswerText: String) extends ObjectiveAnswer {

    override val rawByteForm: Array[Byte] = humanReadableAnswerText.getBytes
  }

  case class ObjectiveIncorrectAnswer(val humanReadableAnswerText: String) extends ObjectiveAnswer {

    override val rawByteForm: Array[Byte] = humanReadableAnswerText.getBytes
  }

  case class SubjectiveCorrectAnswer(val humanReadableAnswerText: String) extends SubjectiveAnswer {

    override val rawByteForm: Array[Byte] = humanReadableAnswerText.getBytes

  }

  case class OnlyTFTypeObjectiveAnswer(val incorrectAnswer: ObjectiveIncorrectAnswer, val correctAnswer: ObjectiveCorrectAnswer)

  object UnavailableSubjectiveCorrectAnswer extends SubjectiveCorrectAnswer("No subjective questions available")

  object UnavailableObjectiveAnswer         extends ObjectiveCorrectAnswer("No objective answer available")

  object NoneOfTheAboveObjectiveAnswer      extends ObjectiveCorrectAnswer("None of the above")

  object AllOfTheAboveObjectiveAnswer       extends ObjectiveCorrectAnswer("All of the above")

  case class Question(val  humanReadableQuestionText: String, val score: Int) {

    val rawByteForm: Array[Byte] = humanReadableQuestionText.getBytes
  }

  case class QuestionAnswerPairPack(val ques: Question, val ansSubjective: Option[SubjectiveCorrectAnswer], val ansObjective: Option[List[ObjectiveAnswer]] )


  // 'ans' here is a JSONIfied string, of the form
  // '{"subjective":null,"objective":{"options":[{"option 1":"N"},{"option 2":"Y"},{"option 3":"N"},{"option 4":"N"}],"total_options":4,"correct_options":1}}'
  //  Convention to follow: score == 0, indicates that if this question is chosen in a quiz, its score is determined dynamically
  case class RawQuestionAnswerScoreTriple(ques: String, ans: String, score: Int)

  trait SmartAnswerPicker {
    def pickAvailableSubjectiveAnswer(): SubjectiveCorrectAnswer
    def pickAvailableObjectiveAnswers(): List[ObjectiveAnswer]
  }

  // implicit val formats = DefaultFormats

  implicit val formats_2 = Serialization.formats(
    ShortTypeHints(
      List(
        classOf[AnswersAvailableInDB]
      )
    )
  )


}
