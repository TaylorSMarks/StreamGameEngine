1. Ability to identify yourself by a name. (BABYLON.GUI.InputText)
2. Fix positioning of text boxes.
3. Make it clear whose turn it is, and whether it's your turn or not.
4. Button to start the game.
5. End the game when all players lose all their lives (or once a winner is determined?)
6. Pause the countdown when a swap starts. Penalize and resume if the swap is invalid.
7. Fix Bolt model.
8. Meshes, not just spheres
    * Heart (Red)
    * Crescent Moon (Purple)
    * Snowflake (White)
    * Rupee (Green)
    * Raindrop (Blue)
9. Fill in the far left and right edges of an iPhone in landscape.
10. Also fix the damn issue where it keeps crashing based on the SSE Event streams being resized or whatever.
11. Include a hash for meshes. Cache meshes in local storage on the javascript side.
12. Use a proper logger, not println.
13. Unit tests.
14. Bug where startTurn() gets called twice back-to-back after a match?