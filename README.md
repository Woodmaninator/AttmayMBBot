# AttmayMBBot
This is a Discord Bot created with Discord4J. The purpose of this bot is to mimic attmayMB.

## How to get the Bot running
In order to get this Bot running you have to change a few things in the AMBBConfig.json file.

* **token:** You have to replace the placeholder token (that is not working btw) with the token of your Discord Bot. You can get that token and find more information about it on the [Discord Developer Page](https://discord.com/developers/applications).
* **spoonacularApiKey:** You have to replace the placeholder API-Key with your Spoonacular Food API Key. You can get this by signing up on the [Spoonacular API Page](https://spoonacular.com/food-api). Note that with the free version of this API you can only send a limited number of requests per day. So far this API is only used for the !dinnerpost command.
* **authorizedRoleID:** You have to replace the placeholder 12345 with the ID of the Discord-Role that is needed for some commands. You can get the ID of the role by entering developer mode in Discord pressing Right-Click (on the role) -> Copy ID. This Role is not needed for most of the commands though.
* **guildID**: You have to replace the placeholder 12345 with the ID of the Discord-Server. You can get it by Right-Click (on the server) -> Copy ID. Note that the role mentioned above has to be on this server.

## Commands
This is a list of all the commands this bot provides.

### !goodnight
This command utilizes the [Datamuse API](https://www.datamuse.com/api/).
Without any parameters this command produces a Goodnight-Message with a random adjecvite that is fitting for the day.

Here are all the different ways you can use this command:
#### !goodnight [noun]
This produces a Goodnight-Message with a random adjective that is fitting for the day and is used to describe the noun.
#### !goodnight n
This produces a Goodnight-Message with a random noun + compound modifier that is fitting for the day.
#### !goodnight [topic] n
This produces a Goodnight-Message with a random noun + compound modifier that is fitting for the day and is related to the topic.


### !joke
This command utilized the [JokeAPI](https://sv443.net/jokeapi/v2/) and returns a random joke. (I blacklisted the racist ones though)


### !quote
This command returns a random quote. (This requires you to have added quotes before)


### !addquote [quote]
This command adds a quote. The quotes are stored in AMBBConfig.json. In order for this command to work you have to be an authorized user. (Have the proper role)


### !dinnerpost
This command returns a random dinnerpost. It actually is just a random recipe plus a proper image. This command utilizes the [Spoonacular Food API](https://spoonacular.com/food-api). 
