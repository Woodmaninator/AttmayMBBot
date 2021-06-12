# AttmayMBBot
This is a Discord Bot created with Discord4J. The purpose of this bot is to mimic attmayMB.

## How to get the Bot running
In order to get this Bot running you have to change a few things in the AMBBConfig.json file.

* **token:** You have to replace the placeholder "abcd" with the token of your Discord Bot. You can get that token and find more information about it on the [Discord Developer Page](https://discord.com/developers/applications).
* **spoonacularApiKey:** You have to replace the placeholder "abcd" with your Spoonacular Food API Key. You can get this by signing up on the [Spoonacular API Page](https://spoonacular.com/food-api). Note that with the free version of this API you can only send a limited number of requests per day. So far this API is only used for the !dinnerpost command.
* **authorizedRoleID:** You have to replace the placeholder 12345 with the ID of the Discord-Role that is needed for some commands. You can get the ID of the role by entering developer mode in Discord pressing Right-Click (on the role) -> Copy ID. This Role is not needed for most of the commands though.
* **guildID**: You have to replace the placeholder 12345 with the ID of the Discord-Server. You can get it by Right-Click (on the server) -> Copy ID. Note that the role mentioned above has to be on this server.
