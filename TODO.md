1. Fix Bolt model.
2. Implement addText, changeText, addCountdown, and changeCountdown in the javascript side.
3. Track score, lives, and time on the Kotlin side.
4. When the countdown ends, decrement lives.
5. Buttons to join and start games...?
6. Meshes, not just spheres
    * Heart (Red)
    * Crescent Moon (Purple)
    * Snowflake (White)
    * Rupee (Green)
    * Raindrop (Blue)
7. Notions of players and picking random players.
8. Fill in the far left and right edges of an iPhone in landscape.
9. Permit multiple players to join the same game.
10. Also fix the damn issue where it keeps crashing based on the SSE Event streams being resized or whatever.
11. Having both a position setter and a move method appear to be coexisting... poorly. Either fix it or undo it.
    * Theory - two different animations are trying to run on a single object. If the wrong animation "wins" it ends up in the wrong spot.
    * Oddly, making addModel be async reduces the frequency of the issue in Edge? I guess I'll just leave it that way for now, even though there's no other reason to have it be async...
12. Include a hash for meshes. Cache meshes in local storage on the javascript side.