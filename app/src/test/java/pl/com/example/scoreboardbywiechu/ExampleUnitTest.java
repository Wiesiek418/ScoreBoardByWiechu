package pl.com.example.scoreboardbywiechu;

import org.junit.Test;

import static org.junit.Assert.*;

import pl.com.example.scoreboardbywiechu.games.Football;
import pl.com.example.scoreboardbywiechu.games.GameSettings;
import pl.com.example.scoreboardbywiechu.elements.Player;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addingPlayersTooMany()
    {
        GameSettings gameSettings = new GameSettings(2,2);
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        Player p3 = new Player("P3");

        gameSettings.addPlayer(p1);
        gameSettings.addPlayer(p2);

        assertThrows(IllegalStateException.class,()-> gameSettings.addPlayer(p3));
    }

    @Test
    public void addingPlayersWithThisSameName()
    {
        GameSettings gameSettings = new GameSettings(2,2);
        Player p1 = new Player("P1");
        Player p2 = new Player("P1");

        gameSettings.addPlayer(p1);

        assertThrows(IllegalArgumentException.class,()-> gameSettings.addPlayer(p2));
    }

    @Test
    public void creatingGameSettings()
    {
        assertThrows(IllegalArgumentException.class,() -> new GameSettings(0,3));
        assertThrows(IllegalArgumentException.class,() -> new GameSettings(1,0));
        assertThrows(IllegalArgumentException.class,() -> new GameSettings(-1,-1));
    }

    @Test
    public void createFootball()
    {
        Football football = new Football();
        assertEquals("Football",football.getNameOfGame());
    }

    @Test
    public void setRandomExtraTime()
    {
        Football football = new Football();
        football.setFlagRandomExtraTime();
        assertTrue(football.getFlagRandomExtraTime());
    }

    @Test
    public void setRandomExtraTimeNumber()
    {
        Football football = new Football();
        football.randomExtraTime();
    }

    @Test
    public void workingTest()
    {
        Football football = new Football(true);
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");

        football.addPlayer(p1);


        assertThrows(IllegalStateException.class,()->football.addPlayer(p2));

    }
}