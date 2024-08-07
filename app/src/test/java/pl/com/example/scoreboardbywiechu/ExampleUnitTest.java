package pl.com.example.scoreboardbywiechu;

import org.junit.Test;

import static org.junit.Assert.*;

import pl.com.example.scoreboardbywiechu.elements.points.PointsCalculator;
import pl.com.example.scoreboardbywiechu.gamesSettings.FootballSettings;
import pl.com.example.scoreboardbywiechu.gamesSettings.GameSettings;
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
        PointsCalculator pointsCalculator = new PointsCalculator();
        GameSettings gameSettings = new GameSettings(2,2);
        Player p1 = new Player("P1");
        Player p2 = new Player("P1");

        gameSettings.addPlayer(p1);

        assertThrows(IllegalArgumentException.class,()-> gameSettings.addPlayer(p2));
    }



    @Test
    public void createFootball()
    {
        FootballSettings footballSettings = new FootballSettings();
        assertEquals("Football", footballSettings.getNameOfGame());
    }

    @Test
    public void setRandomExtraTime()
    {
        FootballSettings footballSettings = new FootballSettings();
        footballSettings.setFlagRandomExtraTime();
        assertTrue(footballSettings.getFlagRandomExtraTime());
    }

    @Test
    public void setRandomExtraTimeNumber()
    {
        FootballSettings footballSettings = new FootballSettings();
        footballSettings.randomExtraTime();
    }

    @Test
    public void workingTest()
    {
        FootballSettings footballSettings = new FootballSettings(true);
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");

        footballSettings.addPlayer(p1);


        assertThrows(IllegalStateException.class,()-> footballSettings.addPlayer(p2));

    }



}