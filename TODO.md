1. Fix Bolt model.
2. Fix issue of trying to move models before they're loaded/created...
3. Ensure that each given model is only loaded once.
4. Having both a position setter and a move method appear to be coexisting... poorly. Either fix it or undo it.
    * Theory - two different animations are trying to run on a single object. If the wrong animation "wins" it ends up in the wrong spot.
5. Implement addText, changeText, addCountdown, and changeCountdown in the javascript side.
6. Track score, lives, and time on the Kotlin side.
7. When the countdown ends, decrement lives.
8. Buttons to join and start games...?
9. Meshes, not just sphere
    * Heart (Red)
    * Star (Yellow)
    * Crescent Moon (Purple)
    * Lightning Bolt (Orange)
    * Snowflake (White)
    * Rupee (Green)
    * Raindrop (Blue)
10. Notions of players and picking random players.
11. Position the camera appropriately and disallow moving it.
12. Fill in the far left and right edges of an iPhone in landscape.
13. Permit multiple players to join the same game.
14. Also fix the damn issue where it keeps crashing based on the SSE Event streams being resized or whatever.