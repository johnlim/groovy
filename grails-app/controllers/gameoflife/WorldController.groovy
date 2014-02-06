package gameoflife
import groovy.transform.*

class WorldController {

    def index() { }
}

class World{

  private def setOfLiveCells = [] as Set;
  void giveCellLife(Cell cell){
    setOfLiveCells.add(cell);
  }

  int countNumberOfLiveCells(){
    return setOfLiveCells.size();
  }
  
  boolean isCellAlive(Cell cell){
    return (setOfLiveCells.contains(cell));
  }

  private int getNumberOfLiveNeighbours(Cell cell){
    int numberOfLiveNeighbours = 0;
    cell.neighbours().each{ if(isCellAlive(it)) numberOfLiveNeighbours++; }
    return numberOfLiveNeighbours; 
  }
  
  World tick(){
    World nextGeneration = new World();
    def cellIsDead = {cell ->  setOfLiveCells.contains(cell) == false};
    def tickOfLiveCells = { 
      if((getNumberOfLiveNeighbours(it) == 2) || (getNumberOfLiveNeighbours(it) == 3)){
        nextGeneration.giveCellLife(it); 
      }
    }
    def tickOfDeadCells = {
      it.neighbours().each{
        if(cellIsDead(it) && getNumberOfLiveNeighbours(it) == 3){ 
            nextGeneration.giveCellLife(it);
        }
      } 
    }
    setOfLiveCells.each{
      tickOfLiveCells(it);
      tickOfDeadCells(it);
//      it.neighbours().each{
//        if(cellIsDead(it) && getNumberOfLiveNeighbours(it) == 3){ 
//            nextGeneration.giveCellLife(it);
//        }
//      }
    }
    return nextGeneration;
   }
}

@TupleConstructor(includeFields = true)
@Canonical
class Cell implements Comparable<Cell>{
  int x;
  int y;
  
  int compareTo(Cell cell){
    x <=> cell.x ?: y <=> cell.y ?: 0;
  }

  List neighbours(){
    List cellNeighbours = [];
    cellNeighbours.add(new Cell(x-1, y));
    cellNeighbours.add(new Cell(x+1, y));
    cellNeighbours.add(new Cell(x, y-1));
    cellNeighbours.add(new Cell(x, y+1));
    cellNeighbours.add(new Cell(x-1, y-1));
    cellNeighbours.add(new Cell(x-1, y+1));
    cellNeighbours.add(new Cell(x+1, y-1));
    cellNeighbours.add(new Cell(x+1, y+1));
    return cellNeighbours;
  }
} 
