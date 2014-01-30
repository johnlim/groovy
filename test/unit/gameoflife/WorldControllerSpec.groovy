package gameoflife

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
//Any live cell with fewer than two live neighbours dies, as if caused by under-population.
//Any live cell with two or three live neighbours lives on to the next generation.
//Any live cell with more than three live neighbours dies, as if by overcrowding.
//Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.

@TestFor(WorldController)
class WorldControllerSpec extends Specification {
    
    World world;
    
    def setup() {
      world = new World();
    }

    def cleanup() {
    }

    void "cellsEqual"(){
      Cell cell = new Cell(0,0);
      Cell anotherCell = new Cell(0,0);  
      expect:
        cell == anotherCell;
    }

    void "cellsNotEqual"(){
      Cell cell = new Cell(0,0)
      Cell differentCell = new Cell(1,0) 
      Cell anotherCell = new Cell(0,1)
      expect:
        cell != differentCell;
        cell != anotherCell;
      }

      
    void "worldGivesCellLife"() {
      Cell cell = new Cell(0,0);
      Cell deadCell = new Cell(5,5);
      world.giveCellLife(cell);
      expect:
        world.isCellAlive(cell) == true;
        world.isCellAlive(deadCell) == false;
        world.countNumberOfLiveCells() == 1; 
    }

    void "worldGivesMultipleCellsLife"() {
      Cell cell = new Cell(0,0);
      Cell anotherCell = new Cell(1,1);
      world.giveCellLife(cell);
      world.giveCellLife(anotherCell);
      expect:
        world.isCellAlive(cell) == true;
        world.isCellAlive(anotherCell) == true;
        world.setOfLiveCells.size() == 2; 
    }

    void "setOfLiveCellsIsUnique"(){
      Cell cell = new Cell(5,6);
      Cell sameCell = new Cell(5,6);
      world.giveCellLife(cell);
      world.giveCellLife(sameCell);
      expect:
        world.countNumberOfLiveCells() == 1;
      }

    void "cellHas8Neighbours"() {
      Cell cell = new Cell(10 ,9)
      expect:
        cell.neighbours().size() == 8;
    }

    void "cellKnowsItsNeighbours"() {
      int x = 5, y =1;
      Cell cell = new Cell(x,y);
      Cell neighbour1 = new Cell(x-1, y);
      Cell neighbour2 = new Cell(x+1, y);
      Cell neighbour3 = new Cell(x,y-1);
      Cell neighbour4 = new Cell(x,y+1);
      Cell neighbour5 = new Cell(x-1,y-1);
      Cell neighbour6 = new Cell(x-1,y+1);
      Cell neighbour7 = new Cell(x+1,y-1);
      Cell neighbour8 = new Cell(x+1,y+1);

      expect:
        cell.neighbours() == [neighbour1, neighbour2, neighbour3, neighbour4, neighbour5, neighbour6, neighbour7, neighbour8];
    }
    
    void "liveCellWithZeroLiveNeighboutDiesOnNextTick"() {
      Cell cell = new Cell(0,0);
      world.giveCellLife(cell);
      expect:
        world.tick().isCellAlive(cell) == false;
    }
    
    void "liveCellWithOneLiveNeighboutDiesOnNextTick"() {
      Cell cell = new Cell(0,0);
      world.giveCellLife(cell);
      world.giveCellLife(cell.neighbours()[0]);

      expect:
        world.tick().isCellAlive(cell) == false;
    }

    void "liveCellWith2LiveNeighboursLivesOnNextTick"(){
      Cell cell = new Cell(0,0);
      world.giveCellLife(cell);
      world.giveCellLife(cell.neighbours()[0]);
      world.giveCellLife(cell.neighbours()[7])
      expect:
        world.tick().isCellAlive(cell) == true;
    }

    void "liveCellWith3LiveNeighboursLivesOnNextTick"(){
      Cell cell = new Cell(9,7);
      world.giveCellLife(cell);
      world.giveCellLife(cell.neighbours()[2]);
      world.giveCellLife(cell.neighbours()[4]);
      world.giveCellLife(cell.neighbours()[6]);
      expect:
        world.tick().isCellAlive(cell) == true;
    }

    void "liveCellsWithMoreThan3LiveNeighboursDiesOnNextTick"(){
      Cell cell = new Cell(8,90);
      world.giveCellLife(cell);
      world.giveCellLife(cell.neighbours()[0]);
      world.giveCellLife(cell.neighbours()[1]);
      world.giveCellLife(cell.neighbours()[2]);
      world.giveCellLife(cell.neighbours()[3]);
      world.giveCellLife(cell.neighbours()[4]);
      expect:
        world.tick().isCellAlive(cell) == false;

      }

    void "deadCellWithThreeLiveNeigboursLivesOnNextTick"(){
      Cell cell = new Cell(100, 1000);
      world.giveCellLife(cell.neighbours()[7]);
      world.giveCellLife(cell.neighbours()[6]);
      world.giveCellLife(cell.neighbours()[5]);
      expect:
        world.tick().isCellAlive(cell) == true;
      }
}
