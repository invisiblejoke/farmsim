package farmsim.crops;

import farmsim.tiles.TileProperty;
import farmsim.world.terrain.Biomes;
import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import farmsim.entities.tileentities.crops.*;
import farmsim.tiles.Tile;

@PrepareForTest({DatabaseHandler.class})
@RunWith(PowerMockRunner.class)
public class BananaTest {

    private Banana banana;
    private DatabaseHandler dbHandler;
    private Map<String, Object> properties;
    private Tile tile;

    /**
     * Mocks the relevant classes and static calls so the Banana class can be
     * tested in isolation.
     */
    @Before
    public void setup() {
        properties = new HashMap<>();
        properties.put("Clevel", 1);
        properties.put("Gtime1", 0);
        properties.put("Gtime2", 480000);
        properties.put("Gtime3", 960000);
        properties.put("Gtime4", 1440000);
        properties.put("Gtime5", 2160000);
        properties.put("Hquantity", 12);
        properties.put("StNames1", "seed");
        properties.put("StNames2", "Banana1");
        properties.put("StNames3", "Banana2");
        properties.put("StNames4", "Banana3");
        properties.put("DeadStage1", "Banana1dead");
        properties.put("DeadStage2", "Banana2dead");
        properties.put("DeadStage3", "Banana3dead");
        properties.put("Penvironment", "FOREST");
        properties.put("Nenvironment", "ROCKY");
        tile = mock(Tile.class);
        dbHandler = mock(DatabaseHandler.class);
        PowerMockito.mockStatic(DatabaseHandler.class);
        PowerMockito.when(DatabaseHandler.getInstance()).thenReturn(dbHandler);
        when(dbHandler.getPlantData("Banana")).thenReturn(properties);

        when(tile.getProperty(TileProperty.BIOME)).thenReturn(Biomes.GRASSLAND);
    }

    /**
     * Checks the initialised state of the banana.
     */
    @Test
    public void initialisationTest() {
        banana = new Banana(tile);
        assertEquals("Banana", banana.getName());
        assertFalse(banana.isDead());
        assertTrue(banana.isOrchard(banana.getName()));
    }

    /**
     * Check that the tick method behaves correctly depending on the water 
     * state of the tile.
     */
    @Test
    public void tickTest() {
        banana = new Banana(tile);
        when(tile.getWaterLevel()).thenReturn((float) 0.1);
        banana.tick();
        assertFalse("Not dead yet", banana.isDead());
        when(tile.getWaterLevel()).thenReturn((float) 0.0);
        banana.tick();
        assertTrue("Is dead", banana.isDead());
        assertEquals("Incorrect stage", banana.getTileType(), "seed");
    }
}

