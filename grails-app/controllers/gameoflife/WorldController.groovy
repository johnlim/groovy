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
//    for(neighbours in cell.neighbours()){
//      if(setOfLiveCells.contains(neighbours)){
//        numberOfLiveNeighbours++;
//      }   
//    }   
    cell.neighbours().each{ if(isCellAlive(it)) numberOfLiveNeighbours++; }
    return numberOfLiveNeighbours; 
  }

  World tick(){
    World nextGeneration = new World();
    //for (liveCell in setOfLiveCells){
    setOfLiveCells.each{
        if((getNumberOfLiveNeighbours(it) == 2) || (getNumberOfLiveNeighbours(it) == 3)){
         nextGeneration.giveCellLife(it); 
        }
    //}
    //setOfLiveCells.each{
      it.neighbours().each{
        if(setOfLiveCells.contains(it) != true){
          if(getNumberOfLiveNeighbours(it) == 3){
            nextGeneration.giveCellLife(it);
          }
        }
      }
    }
    return nextGeneration;
   }
}

//@groovy.transform.EqualsAndHashCode( includeFields = true)
@Canonical
//@EqualsAndHashCode( includeFields = true)
class Cell implements Comparable<Cell>{
  
 /* private*/ int x;
 /* private*/ int y;
  
//  Cell(int _x, int _y){
//    x = _x;
//    y = _y;
//  }

  int compareTo(Cell cell){
//    if (x == cell.x && y == cell.y)
//      return 0;
//    if (x <= cell.x)
//      return -1;
//    else
//      return 1;
//   
    x <=> cell.x ?: y <=> cell.y ?: 0;
  }

  List neighbours(){
    List index = [[-1,0], [1,0], [0, -1], [0, 1], [-1, -1], [-1, 1], [1, -1], [1, 1]];
    //List cellNeighbours = [[x-1, y],[x+1, y], [x, y-1], [x, y+1], [x-1, y-1], [x-1, y+1], [x+1, y-1], [x+1, y+1]];
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
