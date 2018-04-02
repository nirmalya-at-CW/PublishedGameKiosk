package com.OneHuddle.Quiz.preparation.parsing

import com.OneHuddle.Quiz.preparation.QuizQuestionAnswerProtocol.{AnswersAvailableInDB, FailedToParseAnswersAvailableInDB, NonExistentOneHuddleAnswerOptions, ObjectiveAnswer, ObjectiveCorrectAnswer, ObjectiveIncorrectAnswer, OneHuddleAnswerOptions, PossibleAnswer, SubjectiveCorrectAnswer}
import org.json4s.{DefaultFormats, Formats, ShortTypeHints}
import org.json4s.jackson.Serialization._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization
import de.heikoseeberger.akkahttpjson4s.Json4sSupport

import scala.util.{Failure, Success, Try}



/**
  * Created by nirmalya on 7/3/18.
  */

object AnswerBuilder {


  implicit val formats_2 = Serialization.formats(
    ShortTypeHints(
      List(
        classOf[AnswersAvailableInDB]
      )
    )
  )
  def unJasonifyAnswers(rawContentsFromDB: String): AnswersAvailableInDB = {

    // TODO: Explore if more contextual error message can be produced: which field is malformed?

    val answersAvailableInDB  = Try {
      parse(rawContentsFromDB).extract[AnswersAvailableInDB]
    } match {
      case Success(e)    => e
      case Failure(ex)   => new FailedToParseAnswersAvailableInDB(ex.getMessage + ",contents begins with " + rawContentsFromDB.substring(0,15))
    }


    // val answersAvailableInDB = read[AnswersAvailableInDB](rawContentsFromDB)
    answersAvailableInDB
  }


  def assembleAvailableAnswers(
        answersAvailableInDB: AnswersAvailableInDB)
        : (Option[SubjectiveCorrectAnswer],Option[List[ObjectiveAnswer]]) = {

      val (subjectiveAnswers, objectiveAnswers) =

        (
          (answersAvailableInDB.subjective.map (e => SubjectiveCorrectAnswer(e))),
          (answersAvailableInDB.objective.map  (e => prepareObjetiveAnswerBunch(e)))
        )

      (subjectiveAnswers, objectiveAnswers)
  }

  private
  def prepareObjetiveAnswerBunch(answersJSONFromDB:OneHuddleAnswerOptions): List[ObjectiveAnswer] = {

    val possibleAnswerBunch = List[ObjectiveAnswer]()

    answersJSONFromDB
      .options
      .foldLeft(possibleAnswerBunch)((accu,userPromptAndCorrectnessIndicator) => {

      if (userPromptAndCorrectnessIndicator._2.equalsIgnoreCase("Y"))
        accu :+ ObjectiveCorrectAnswer(userPromptAndCorrectnessIndicator._1)
      else
        accu :+ ObjectiveIncorrectAnswer(userPromptAndCorrectnessIndicator._1)
    })

  }

}