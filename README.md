## At the moment, this kiosk only provides a component that realizes the concept of a library, in an executable form.

It will grow in size, in days to come, and provide a complete game kiosk: a set of functionality which provides all information
about games that can be played and are being played, in 1Huddle.

## Code organisation

All the classes and JSONification code are kept inside [QuizQuestionAnswerProtocol](com.OneHuddle.Quiz.preparation.QuizQuestionAnswerProtocol) .

A library is represented as _GameLibrary_ (code here: [GameLibrary](com/OneHuddle/Quiz/preparation/GameLibrary.scala). 

A library is initialized with 2 randomization functions: one randomizes the questions, and other randomizes the answers,
they are selected to be a part of the quiz being being dealt. Shelves are attached to libraries.

```val library =
       GameLibrary(
         quesRandomizer,
         ansRandomizer
       )
         .attachAShelf(shelfIDForSoccerUSEnglish,       questAndAnsProcessedCategorySoccer)
         .attachAShelf(shelfIDForIndiaUSEnglish,        questAndAnsProcessedCategoryIndia)
         .attachAShelf(shelfIDForWorldHistoryUSEnglish, questAndAnsProcessedCategoryWorldHistory)
         .attachAShelf(shelfIDForSubjectiveAnwers,      subjectiveQuestionAndAnswerSet)
```

Shleves are identified by a ShelfID, represented as 

```case class GameLibraryShelfID(gameID: Int, gameCategory: String, prefLang: String)```

So, a sample shelf, meant for soccer and provided in Enlish language onle ('en-US'), looks like this:

```val shelfIDForSoccerUSEnglish = GameLibraryShelfID(1001,"Soccer","en_US")```


Data, available from underlying DB (or perhaps, cache in REDIS - not yet implemented), contain question and answers in JSONified form.
Parsing and preparing them for being stored as ```QuestionAnswerPairPack```   , are handled using functions provided in
[LibraryUtilities](com.OneHuddle.Quiz.preparation.LibraryUtilities) .  

The two front-facing functions are defined in [QuizDealer](com.OneHuddle.Quiz.QuizDealer).
They are:

*  ```com.OneHuddle.Quiz.QuizDealer.dealASubjectiveQuiz```
*  ```com.OneHuddle.Quiz.QuizDealer.dealAnObjectiveQuiz```
 

All tests are in [here](src/test/scala/com/OneHuddle) .

For more explanatory material, please refer to [Confluence]() .