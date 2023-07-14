# AttmayMBBot
This is a Discord Bot created with Discord4J. At the beginning of development the purpose of this bot was to mimic attmayMB.
However, at this stage the bot is way better than the original attmayMB.

attmayMBBot is a Woodlabs product.

We at Woodlabs® care about furthering research of artificial intelligence, block chain, big data, machine learning, augmented reality, quantum computing, and advanced woodworking.

## How to get the Bot running
This bot is only meant to be used on one specific Discord server. It is a lot of work to get it running on another server, since you will have to host it yourself, but it is possible.
In order to get this bot running you have to change a few things in the AMBBConfig.json file.

* **token:** You have to replace the placeholder token (that is not working btw) with the token of your Discord Bot. You can get that token and find more information about it on the [Discord Developer Page](https://discord.com/developers/applications).
* **spoonacularApiKey:** You have to replace the placeholder API-Key with your Spoonacular Food API Key. You can get this by signing up on the [Spoonacular API Page](https://spoonacular.com/food-api). Note that with the free version of this API you can only send a limited number of requests per day. So far this API is only used for the /dinnerpost command.
* **authorizedRoleID:** You have to replace the placeholder 12345 with the ID of the Discord-Role that is needed for some commands. You can get the ID of the role by entering developer mode in Discord pressing Right-Click (on the role) -> Copy ID. This Role is not needed for most of the commands though.
* **guildID**: You have to replace the placeholder 12345 with the ID of the Discord-Server. You can get it by Right-Click (on the server) -> Copy ID. Note that the role mentioned above has to be on this server.
* **generalArcadeChannelId**: You have to replace the placeholder 12345 with the ID of the Discord-Channel that is used for the general arcade commands (/xp and /highscore).
* **alineChannelId**: You have to replace the placeholder 12345 with the ID of the Discord-Channel that is used for the aLine game.
* **triviaChannelId**: You have to replace the placeholder 12345 with the ID of the Discord-Channel that is used for the trivia game.

## Commands
This is a list of all the commands this bot provides.

### /commands
This shows you a list of all the commands this bot provides. (It just links you to this exact page)


### /goodnight
This command utilizes the [Datamuse API](https://www.datamuse.com/api/).
Without any parameters this command produces a Goodnight-Message with a random adjective that is fitting for the day.

Here are all the different ways you can use this command:
#### /goodnight [noun]
This produces a Goodnight-Message with a random adjective that is fitting for the day and is used to describe the noun.
#### /goodnight n
This produces a Goodnight-Message with a random noun + compound modifier that is fitting for the day.
#### /goodnight [topic] n
This produces a Goodnight-Message with a random noun + compound modifier that is fitting for the day and is related to the topic.


### /joke
This command utilized the [JokeAPI](https://sv443.net/jokeapi/v2/) and returns a random joke. (I blacklisted the racist ones though)


### /quote
This command returns a random quote.

#### /quote [Quote ID]
This command returns the quote that the ID belongs to.


### /addquote [Author] [Year] [Quote]
This command adds a quote. The quotes are stored in AMBBQuotes.json. In order for this command to work you have to be an authorized user. (Have the proper role). The author also needs to exist before running this command.


### /removequote
This command removes the last added quote. In order for this command to work you have to be an authorized user. (Have the proper role).

#### /removequote [Quote ID]
This command removes the quote associated with the ID. In order for this command to work you have to be an authorized user. (Have the proper role).


### /editquote [New Quote Text]
This command edits the quote text of the last added quote. In order for this command to work you have to be an authorized user. (Have the proper role).

#### /editquote [Quote ID] [New Quote Text]
This command edits the quote text of the quote associated with the ID. In order for this command to work you have to be an authorized user. (Have the proper role).


### /quotelist
This command returns a list of all the quotes.

#### /quotelist [Author or Author Alias]
This command returns a list of all the quotes by the author.


### /quoteidlist
This command returns a list of all the quotes and their IDs.

#### /quoteidlist [Author]
This command returns a list of all the quotes with quote IDs by the author.


### /addAuthor [Name] [Discord ID]
This command adds a new author. The authors are stored in AMBBQuotes.json. In order for this command to work you have to be an authorized user. (Have the proper role).


### /renameAuthor [current Name] [new Name]
This command renames the author in the AMBBQuotes.json file. In order for this command to work you have to be an authorized user. (Have the proper role).


### /removeAuthor [Name]
This command deletes the author entry from the AMBBQuotes.json file. This subsequently removes all of their quotes. In order for this command to work you have to be an authorized user. (Have the proper role).


### /addAlias [Name] [new Alias]
This command adds a new alias to an author. The aliases for an author are stored in AMBBQuotes.json. In order for this command to work you have to be an authorized user. (Have the proper role). When using /addquote or /quotelist [Name] you can also use one of the aliases of a user instead of the original name.


### /removeAlias [Alias]
This command removes the alias from the author it's attached to. In order for this command to work you have to be an authorized user. (Have the proper role).


### /authorlist
This command will list all the authors in the AMBBQuotes.json file sorted by number of quotes.

#### /authorlist [count]
This command will list [count] authors from the AMBBQuotes.json file sorted by number of quotes.

#### /authorlist top [count]
This command will list all the authors that are in the top [count] by number of quotes.


### /quoteQuiz
This command starts a Quote Quiz for the user that entered the command. The Quiz consists of a random quote and the user has to guess the author of the quote. The user answers via reactions to the message. Only the answer from the user that started the quiz will be accepted.


### /rankQuotes
This command will prompt the bot to send a message with which you can vote for your favourite of two different quotes. The quotes are chosen randomly and by deciding which one of these two quotes you prefer, a ranking is created. You vote using reactions and after reacting the same message will display two new quotes, so you can continue voting using the same message.


### /rankedQuoteList
This command will display the top 10 quotes based on the ranking from /rankQuotes.

#### /rankedQuoteList [Author]
This command will display the top 10 quotes of the quote author based on the ranking from /rankQuotes.

#### /rankedQuoteList [Count]
This command will display the top [Count] quotes based on the ranking from /rankQuotes.

#### /rankedQuoteList [Author] [Count]
This command will display the top [Count] quotes of the quote author based on the ranking from /rankQuotes.


### /dinnerpost
This command returns a random dinnerpost. It actually is just a random recipe plus a proper image. This command utilizes the [Spoonacular Food API](https://spoonacular.com/food-api). In order for this command to work you have to be an authorized user. (Have the proper role).

#### /dinnerpost [Keyword]
This command returns a random dinnerpost that is related to the keyword. In order for this command to work you have to be an authorized user. (Have the proper role).


### /uwu
This command returns an uwu-ified version of the previous message in the text-chat.

#### /uwu [Text]
This command returns an uwu-ified version of the text that is passed as an argument.


### /combine [emoji1] [emoji2]
This command lets you combine two emojis. It uses the Google gboard emoji kitchen combinations. There are about 26420 combinations available at the moment. This list has to be manually updated for future updates.
Make sure to leave a space between the two emojis.

## Arcade Commands

### /xp
This command shows the xp and level of the user that used the command.


### /highscore
This command shows all users and their xp and level.


### /aline
This command starts an aLine game. If no difficulty is specified, it will choose the easy difficulty.

#### /aline [easy|medium|hard]
This command starts an aLine game with the specified difficulty.

#### /aline tutorial
This command displays a tutorial for the aLine game.


### /trivia
This command starts a trivia game. If no difficulty is specified, it will choose the easy difficulty. This command utilises the [Open Trivia Database API](https://opentdb.com/).

#### /trivia [easy|medium|hard]
This command starts trivia game with the specified difficulty.